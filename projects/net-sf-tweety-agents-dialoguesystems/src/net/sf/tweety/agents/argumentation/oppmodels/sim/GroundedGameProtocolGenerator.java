package net.sf.tweety.agents.argumentation.oppmodels.sim;

import net.sf.tweety.agents.argumentation.oppmodels.ArguingAgent;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameProtocol;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
import net.sf.tweety.agents.sim.ProtocolGenerator;
import net.sf.tweety.agents.sim.SimulationParameters;

/**
 * Creates protocols for a grounded game.
 * @author Matthias Thimm
 */
public class GroundedGameProtocolGenerator implements ProtocolGenerator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> {

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.sim.ProtocolGenerator#generate(net.sf.tweety.agents.MultiAgentSystem, net.sf.tweety.agents.sim.SimulationParameters)
	 */
	@Override
	public GroundedGameProtocol generate(GroundedGameSystem mas, SimulationParameters params) {
		return new GroundedGameProtocol(mas);
	}

}
