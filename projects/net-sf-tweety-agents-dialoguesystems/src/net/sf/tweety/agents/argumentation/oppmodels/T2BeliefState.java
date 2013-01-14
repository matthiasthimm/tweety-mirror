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
 * @author Tjitze Rienstra, Matthias Thimm
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

	/**
	 * Returns a T1-belief state that is a projection of this belief state,
	 * i.e. in every nested model one substate is chose randomly depending on
	 * its probability
	 * @return a T1-projection of this belief state.
	 */
	public T1BeliefState sampleT1BeliefState(){
		//TODO parametrize the sampling
		if(this.prob.isEmpty())
			return new T1BeliefState(this.getKnownArguments(), this.getUtilityFunction());
		else return new T1BeliefState(this.getKnownArguments(), this.getUtilityFunction(), this.prob.sample().sampleT1BeliefState());
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#doMove(net.sf.tweety.agents.argumentation.oppmodels.GroundedEnvironment, net.sf.tweety.agents.argumentation.DialogueTrace)
	 */
	@Override
	protected Pair<Double, Set<ExecutableExtension>> doMove(GroundedEnvironment env, DialogueTrace trace) {
		double bestEU = this.getUtilityFunction().getUtility(env.getDialogueTrace());
		Set<ExecutableExtension> bestMoves = new HashSet<ExecutableExtension>();
		bestMoves.add(new ExecutableExtension());
		/* For every legal move newMove ... */		
		for(ExecutableExtension newMove: this.getLegalMoves(env,trace)){			
			DialogueTrace t2 = trace.addAndCopy(newMove);
			double newMoveEU = 0;			
			/* For all possible opponent states oppState ... */
			if(this.prob.isEmpty())
				newMoveEU = this.getUtilityFunction().getUtility(t2);
			else
				for (T2BeliefState oppState: this.prob.keySet()) {
					Probability oppStateProb = this.prob.probability(oppState);				
					/* Get opponent's best responses to newMove */
					Set<ExecutableExtension> bestOppResponses = oppState.doMove(env,t2).getSecond();				
					/* If opponent has no response, then utility of newMove is determined by current trace / oppStateProb */
					if (bestOppResponses.isEmpty()) {
						newMoveEU += this.getUtilityFunction().getUtility(t2) * oppStateProb.doubleValue();
					}else{
						/* There may be more than 1 opp response, we don't know which one is best, so 
						 * we assign equal probability to each response */
						float oppResponseProb = 1f / (float)bestOppResponses.size();	
						/* For every possible opponent response oppResponse ... */
						for (ExecutableExtension oppResponse: bestOppResponses) {
							// this avoids infinite loops
							// (if there are two consecutive noops the game is over anyway)
							if(newMove.isNoOperation() && oppResponse.isNoOperation()){
								newMoveEU += this.getUtilityFunction().getUtility(t2) * oppStateProb.doubleValue() * oppResponseProb;
								continue;
							}
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
		result += this.getUtilityFunction() + ",\n";
		for(int i = 0; i < indent; i++) result += "  ";
		result += "Prob\n";
		indent++;
		for(T2BeliefState state: this.prob.keySet()){
			for(int i = 0; i < indent; i++) result += "  ";
			result += this.prob.get(state) + ":\n";
			result += state.display(indent+1) + "\n";
		}
		for(int i = 0; i < origIndent; i++) result += "  ";
		result += ">";
		return result;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#clone()
	 */
	public Object clone(){
		if(this.prob.isEmpty())
			return new T2BeliefState(new Extension(this.getKnownArguments()), this.getUtilityFunction());
		ProbabilityFunction<T2BeliefState> prob = new ProbabilityFunction<T2BeliefState>();
		for(java.util.Map.Entry<T2BeliefState, Probability> entry: this.prob.entrySet())
			prob.put((T2BeliefState)entry.getKey().clone(), entry.getValue());
		return new T2BeliefState(new Extension(this.getKnownArguments()), this.getUtilityFunction(), prob);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "<" + this.getKnownArguments() + ", " + this.getUtilityFunction() + ", " + this.prob + ">";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((prob == null) ? 0 : prob.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		T2BeliefState other = (T2BeliefState) obj;
		if (prob == null) {
			if (other.prob != null)
				return false;
		} else if (!prob.equals(other.prob))
			return false;
		return true;
	}
	
	
}
