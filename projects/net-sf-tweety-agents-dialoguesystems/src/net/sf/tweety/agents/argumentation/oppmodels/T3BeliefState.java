package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.agents.argumentation.ExecutableExtension;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.Attack;
import net.sf.tweety.math.probability.ProbabilityFunction;
import net.sf.tweety.util.Pair;

/**
 * This belief state consists of a probability distribution over 
 * other opponent models with virtual arguments.
 * 
 * @author Tjitze, Rienstra, Matthias Thimm
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
	protected Pair<Float, Set<ExecutableExtension>> doMove(GroundedEnvironment env, DialogueTrace trace) {
		// TODO
		return null;
	}

}
