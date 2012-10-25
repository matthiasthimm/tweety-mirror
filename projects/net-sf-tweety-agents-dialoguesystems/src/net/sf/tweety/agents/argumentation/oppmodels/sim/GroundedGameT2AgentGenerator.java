package net.sf.tweety.agents.argumentation.oppmodels.sim;

import net.sf.tweety.agents.argumentation.oppmodels.BeliefState;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
import net.sf.tweety.agents.sim.SimulationParameters;

/**
 * Generates agents of type T2.
 * @author Matthias Thimm
 */
public class GroundedGameT2AgentGenerator extends GroundedGameAgentGenerator {

	/**
	 * Creates a new generator for agents of type T2.
	 * @param faction the faction of the agents to be generated.
	 */
	public GroundedGameT2AgentGenerator(GroundedGameSystem.AgentFaction faction) {
		super(faction);
		// TODO
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameAgentGenerator#generateBeliefState(net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem, net.sf.tweety.agents.sim.SimulationParameters)
	 */
	@Override
	protected BeliefState generateBeliefState(GroundedGameSystem mas, SimulationParameters params) {
		// TODO Auto-generated method stub
		return null;
	}

}
