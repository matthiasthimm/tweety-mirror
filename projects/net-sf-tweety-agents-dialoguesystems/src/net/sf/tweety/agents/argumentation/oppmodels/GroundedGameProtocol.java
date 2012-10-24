package net.sf.tweety.agents.argumentation.oppmodels;

import net.sf.tweety.agents.Agent;
import net.sf.tweety.agents.GameProtocol;
import net.sf.tweety.agents.RoundRobinProtocol;

/**
 * @author Matthias Thimm
 *
 */
public class GroundedGameProtocol extends RoundRobinProtocol implements GameProtocol{

	/**
	 * Creates a new grounded game protocol for the given grounded game systems.
	 * @param system a grounded game system.
	 */
	public GroundedGameProtocol(GroundedGameSystem system) {
		super(system);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.GameProtocol#hasWinner()
	 */
	@Override
	public boolean hasWinner() {
		return this.hasTerminated();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.GameProtocol#getWinner()
	 */
	@Override
	public Agent getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

}
