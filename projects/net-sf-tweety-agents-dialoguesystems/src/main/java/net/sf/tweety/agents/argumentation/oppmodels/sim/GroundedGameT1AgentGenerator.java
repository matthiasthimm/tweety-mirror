package net.sf.tweety.agents.argumentation.oppmodels.sim;

import net.sf.tweety.agents.argumentation.oppmodels.BeliefState;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameUtilityFunction;
import net.sf.tweety.agents.argumentation.oppmodels.T1BeliefState;
import net.sf.tweety.agents.sim.SimulationParameters;
import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates agents of type T1.
 * @author Matthias Thimm
 */
public class GroundedGameT1AgentGenerator extends GroundedGameAgentGenerator {

	/** Logger */
	static private Logger log = LoggerFactory.getLogger(GroundedGameT1AgentGenerator.class);
	
	/** The configuration for generating agents. */
	private T1Configuration config;
	
	/**
	 * Creates a new generator for agents of type T1.
	 * @param faction the faction of the agents to be generated.
	 * @param config
	 */
	public GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction faction, T1Configuration config) {
		super(faction);
		this.config = config;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameAgentGenerator#generateBeliefState(net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem, net.sf.tweety.agents.sim.SimulationParameters)
	 */
	@Override
	protected BeliefState generateBeliefState(GroundedGameSystem mas, SimulationParameters params) {
		BeliefState state = this.generateBeliefState(mas,params,this.config.maxRecursionDepth,(Extension)params.get(this.getFaction()),this.getFaction());
		log.info("Generated a T1-belief state for " + this.getFaction() + " agent: " + state);
		return state;	
	}
	
	/**
	 * Generates the (sub-)belief state of a T1-belief state.
	 * @param mas the multi-agent system under consideration.
	 * @param params parameters for the simulation.
	 * @param depth the maximal depth of the recursive model.
	 * @param arguments the arguments that are currently in the view
	 * @param faction the faction of the model to be generated.
	 * @return a T1-belief state
	 */
	private T1BeliefState generateBeliefState(GroundedGameSystem mas, SimulationParameters params, int depth, Extension arguments, GroundedGameSystem.AgentFaction faction) {
		// end of recursion
		if(depth < 0 || arguments.isEmpty())
			return null;
		Extension subView = new Extension();
		for(Argument a: arguments){
			if(this.getRandom().nextDouble() >= this.config.probRecursionDecay){
				if(this.config.oppModelCorrect)
					if(!((Extension)params.get(this.getFaction().getComplement())).contains(a))
						continue;				
				subView.add(a);
			}
		}	
		return new T1BeliefState(
						arguments,
						new GroundedGameUtilityFunction(
								(DungTheory)params.get(GroundedGameGenerator.PARAM_UNIVERSALTHEORY),
								(Argument)params.get(GroundedGameGenerator.PARAM_ARGUMENT),
								faction),
						this.generateBeliefState(
								mas,
								params,
								depth-1,
								subView,
								faction.getComplement()));		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((config == null) ? 0 : config.hashCode());
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
		GroundedGameT1AgentGenerator other = (GroundedGameT1AgentGenerator) obj;
		if (config == null) {
			if (other.config != null)
				return false;
		} else if (!config.equals(other.config))
			return false;
		return true;
	}
}
