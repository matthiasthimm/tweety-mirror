package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.agents.argumentation.ExecutableExtension;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.probability.ProbabilityFunction;
import net.sf.tweety.util.Pair;

/**
 * This belief state consists of a probability distribution over 
 * other opponent models.
 * 
 * @author Tjitze, Rienstra, Matthias Thimm
 */
public class T2BeliefState extends BeliefState {

	/** The probability function on opponent models*/
	private ProbabilityFunction<T2BeliefState> prob;
		
	/**
	 * Creates a new T2-belief-state with the given parameters. 
	 * @param knownArguments the set of arguments known by the agent.
	 * @param utilityFunction the utility function of the agent.
	 * @param prob the probability function over opponent models.
	 */
	public T2BeliefState(Extension knownArguments, UtilityFunction utilityFunction, ProbabilityFunction<T2BeliefState> prob){
		super(knownArguments, utilityFunction);
		this.prob = prob;
	}
	
	/**
	 * Creates a new T2-belief-state with the given parameters and without nesting. 
	 * @param knownArguments the set of arguments known by the agent.
	 * @param utilityFunction the utility function of the agent.	 
	 */
	public T2BeliefState(Extension knownArguments, UtilityFunction utilityFunction){
		this(knownArguments, utilityFunction, new ProbabilityFunction<T2BeliefState>());		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#update(net.sf.tweety.agents.argumentation.DialogueTrace)
	 */
	@Override
	public void update(DialogueTrace trace) {
		this.getKnownArguments().addAll(trace.getArguments());
		ProbabilityFunction<T2BeliefState> newProb = new ProbabilityFunction<T2BeliefState>();
		for(T2BeliefState state: this.prob.keySet()){
			state.update(trace);
			if(newProb.keySet().contains(state))
				newProb.put(state, newProb.get(state).add(this.prob.get(state)));
		}
		this.prob = newProb;		
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#doMove(net.sf.tweety.agents.argumentation.oppmodels.GroundedEnvironment, net.sf.tweety.agents.argumentation.DialogueTrace)
	 */
	@Override
	protected Pair<Float, Set<ExecutableExtension>> doMove(GroundedEnvironment env, DialogueTrace trace) {
		float bestEU = this.getUtilityFunction().getUtility(env.getDialogueTrace());
		Set<ExecutableExtension> bestMoves = new HashSet<ExecutableExtension>();		
		/* For every legal move newMove ... */		
		for(ExecutableExtension newMove: this.getLegalMoves(env)){			
			DialogueTrace t2 = trace.addAndCopy(newMove);
			float newMoveEU = 0;			
			/* For all possible opponent states oppState ... */
			for (T2BeliefState oppState: this.prob.keySet()) {
				Probability oppStateProb = this.prob.probability(oppState);				
				/* Get opponent's best responses to newMove */
				Set<ExecutableExtension> bestOppResponses = this.doMove(env,t2).getSecond();				
				/* If opponent has no response, then utility of newMove is determined by current trace / oppStateProb */
				if (bestOppResponses.isEmpty()) {
					newMoveEU += this.getUtilityFunction().getUtility(t2) * oppStateProb.doubleValue();
				}else{
					/* There may be more than 1 opp response, we don't know which one is best, so 
					 * we assign equal probability to each response */
					float oppResponseProb = 1f / (float)bestOppResponses.size();	
					/* For every possible opponent response oppResponse ... */
					for (ExecutableExtension oppResponse: bestOppResponses) {
						DialogueTrace t3 = t2.addAndCopy(oppResponse);		
						/* Get best response to oppResponse */
						Pair<Float, Set<ExecutableExtension>> r = this.doMove(env, t3);						
						/* Expected utility is utility of best response times probability of 
						   opponent model times probability of opponent response */
						newMoveEU += r.getFirst() * oppStateProb.doubleValue() * oppResponseProb;						
					}
				}
			}			
			/* Keep track of the set of best responses */
			if (newMoveEU > bestEU) bestMoves.clear();
			if (newMoveEU >= bestEU) {
				bestMoves.add(newMove);
				bestEU = newMoveEU;
			}			
		}		
		return new Pair<Float, Set<ExecutableExtension>>(bestEU, bestMoves);
	}
}
