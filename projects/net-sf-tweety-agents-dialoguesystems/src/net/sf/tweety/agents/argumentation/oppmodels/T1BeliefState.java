package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.agents.argumentation.ExecutableExtension;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.util.Pair;

/**
 * This belief state consists of a simple recursive opponent model.
 * 
 * @author Matthias Thimm, Tjitze Rienstra
 */
public class T1BeliefState extends BeliefState {
	
	/** The opponent model of the agent (as there are only
	 * two agents in the system the model always refers to 
	 * the other agent. */
	private T1BeliefState oppModel = null;
	
	/**
	 * Creates a new T1-belief-state with the given parameters. 
	 * @param knownArguments the set of arguments known by the agent.
	 * @param utilityFunction the utility function of the agent.
	 * @param oppModel the opponent model of the agent (null if no further model is given).
	 */
	public T1BeliefState(Extension knownArguments, UtilityFunction utilityFunction, T1BeliefState oppModel){
		super(knownArguments, utilityFunction);
		this.oppModel = oppModel;
	}
	
	/**
	 * Creates a new T1-belief-state with the given parameters and without nesting. 
	 * @param knownArguments the set of arguments known by the agent.
	 * @param utilityFunction the utility function of the agent.	 
	 */
	public T1BeliefState(Extension knownArguments, UtilityFunction utilityFunction){
		this(knownArguments, utilityFunction, null);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#update(net.sf.tweety.agents.argumentation.DialogueTrace)
	 */
	@Override
	public void update(DialogueTrace trace) {
		this.getKnownArguments().addAll(trace.getArguments());
		this.oppModel.update(trace);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.BeliefState#doMove(net.sf.tweety.agents.argumentation.oppmodels.GroundedEnvironment, net.sf.tweety.agents.argumentation.DialogueTrace)
	 */
	@Override
	public Pair<Float,Set<ExecutableExtension>> doMove(GroundedEnvironment env, DialogueTrace trace) {
		float maxUtility = this.getUtilityFunction().getUtility(env.getDialogueTrace());
		Set<ExecutableExtension> bestMoves = new HashSet<ExecutableExtension>();
		for(ExecutableExtension move: this.getLegalMoves(env)){
			float eu = 0;
			if(this.oppModel == null)
				eu = this.getUtilityFunction().getUtility(env.getDialogueTrace().addAndCopy(move));
			else{			
				Pair<Float,Set<ExecutableExtension>> opponentMoves = this.oppModel.doMove(env,trace.addAndCopy(move));
				for(ExecutableExtension move2: opponentMoves.getSecond()){
					Pair<Float,Set<ExecutableExtension>> myMoves = this.doMove(env, trace.addAndCopy(move).addAndCopy(move2));
					eu += myMoves.getFirst() * 1f/opponentMoves.getSecond().size();
				}				
			}
			if(eu > maxUtility){
				maxUtility = eu;
				bestMoves.clear();
				bestMoves.add(move);
			}else if(eu == maxUtility)
				bestMoves.add(move);
		}		
		return new Pair<Float,Set<ExecutableExtension>>(maxUtility,bestMoves);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((oppModel == null) ? 0 : oppModel.hashCode());
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
		T1BeliefState other = (T1BeliefState) obj;
		if (oppModel == null) {
			if (other.oppModel != null)
				return false;
		} else if (!oppModel.equals(other.oppModel))
			return false;
		return true;
	}
}
