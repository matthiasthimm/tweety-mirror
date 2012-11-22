package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.agents.argumentation.ExecutableExtension;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.Attack;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.probability.ProbabilityFunction;
import net.sf.tweety.util.Pair;

/**
 * This belief state consists of a probability distribution over 
 * other opponent models with virtual arguments.
 * 
 * @author Tjitze Rienstra, Matthias Thimm
 */
public class T3BeliefState extends BeliefState{

	/** The set of virtual arguments assumed to exist. */
	private Set<Argument> virtualArguments;	
	/** The set of virtual attacks assumed to exist between
	 * virtual and ordinary arguments. */
	private Set<Attack> virtualAttacks;
	/** The recognition function for recognizing ordinary arguments. */
	private RecognitionFunction rec;
	/** The probability function on opponent models*/
	private ProbabilityFunction<T3BeliefState> prob;
		
	/**
	 * Creates a new T3-belief-state with the given parameters. 
	 * @param knownArguments the set of arguments known by the agent.
	 * @param utilityFunction the utility function of the agent.
	 * @param prob the probability function over opponent models.
	 */
	public T3BeliefState(Extension knownArguments, UtilityFunction utilityFunction, Set<Argument> virtualArguments, Set<Attack> virtualAttacks, RecognitionFunction rec, ProbabilityFunction<T3BeliefState> prob){
		super(knownArguments, utilityFunction);
		this.virtualArguments = virtualArguments;
		this.virtualAttacks = virtualAttacks;
		this.rec = rec;
		this.prob = prob;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#update(net.sf.tweety.agents.argumentation.DialogueTrace)
	 */
	@Override
	public void update(DialogueTrace trace) {
		this.getKnownArguments().addAll(trace.getArguments());
		for(Argument a: trace.getArguments())
			if(this.rec.get(a) != null)
				this.virtualArguments.removeAll(this.rec.get(a));
		Set<Attack> newVirtualAttacks = new HashSet<Attack>();
		for(Attack a: this.virtualAttacks)
			if(this.virtualArguments.contains(a.getAttacker()) || this.virtualArguments.contains(a.getAttacked()))
				newVirtualAttacks.add(a);
		this.virtualAttacks = newVirtualAttacks;
		ProbabilityFunction<T3BeliefState> newProb = new ProbabilityFunction<T3BeliefState>();
		for(T3BeliefState state: this.prob.keySet()){
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
	protected Pair<Double, Set<ExecutableExtension>> doMove(GroundedEnvironment env, DialogueTrace trace) {
		double bestEU = this.getUtilityFunction().getUtility(env.getDialogueTrace(), this.virtualArguments, this.virtualAttacks);
		Set<ExecutableExtension> bestMoves = new HashSet<ExecutableExtension>();
		bestMoves.add(new ExecutableExtension());
		/* For every legal move newMove ... */		
		for(ExecutableExtension newMove: this.getLegalMoves(env,trace)){			
			DialogueTrace t2 = trace.addAndCopy(newMove);
			float newMoveEU = 0;			
			/* For all possible opponent states oppState ... */
			for (T3BeliefState oppState: this.prob.keySet()) {
				Probability oppStateProb = this.prob.probability(oppState);				
				/* Get opponent's best responses to newMove */
				Set<ExecutableExtension> bestOppResponses = this.doMove(env,t2).getSecond();				
				/* If opponent has no response, then utility of newMove is determined by current trace / oppStateProb */
				if (bestOppResponses.isEmpty()) {
					newMoveEU += this.getUtilityFunction().getUtility(t2, this.virtualArguments, this.virtualAttacks) * oppStateProb.doubleValue();
				}else{
					/* There may be more than 1 opp response, we don't know which one is best, so 
					 * we assign equal probability to each response */
					float oppResponseProb = 1f / (float)bestOppResponses.size();	
					/* For every possible opponent response oppResponse ... */
					for (ExecutableExtension oppResponse: bestOppResponses) {
						// this avoids infinite loops
						// (if there are two consecutive noops the game is over anyway)
						if(newMove.isNoOperation() && oppResponse.isNoOperation())
							continue;
						DialogueTrace t3 = t2.addAndCopy(oppResponse);		
						/* Get best response to oppResponse */
						Pair<Double, Set<ExecutableExtension>> r = this.doMove(env, t3);						
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
		return new Pair<Double, Set<ExecutableExtension>>(bestEU, bestMoves);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#display()
	 */
	@Override
	public String display(){
		return this.display(0);
	}
	
	/**
	 * Aux method for pretty print();
	 * @param indent indentation for display, depending on recursion depth
	 * @return a string representation of this state.
	 */
	private String display(int indent){
		int origIndent = indent;
		String result = "";
		for(int i = 0; i < indent; i++) result += "  ";
		result += "<\n";
		indent++;
		for(int i = 0; i < indent; i++) result += "  ";
		result += this.getKnownArguments() + ",\n";
		for(int i = 0; i < indent; i++) result += "  ";
		result += "V: " + this.virtualArguments + ",\n";
		for(int i = 0; i < indent; i++) result += "  ";
		result += "V: " + this.virtualAttacks + ",\n";
		for(int i = 0; i < indent; i++) result += "  ";
		result += "V: " + this.rec + ",\n";
		for(int i = 0; i < indent; i++) result += "  ";
		result += this.getUtilityFunction() + ",\n";
		for(int i = 0; i < indent; i++) result += "  ";
		result += "Prob\n";
		indent++;
		for(T3BeliefState state: this.prob.keySet()){
			for(int i = 0; i < indent; i++) result += "  ";
			result += this.prob.get(state) + ":\n";
			result += state.display(indent+1) + "\n";
		}
		for(int i = 0; i < origIndent; i++) result += "  ";
		result += ">";
		return result;
	}
}
