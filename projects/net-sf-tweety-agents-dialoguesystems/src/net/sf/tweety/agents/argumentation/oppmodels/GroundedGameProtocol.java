package net.sf.tweety.agents.argumentation.oppmodels;

import net.sf.tweety.agents.Agent;
import net.sf.tweety.agents.RoundRobinProtocol;
import net.sf.tweety.agents.sim.GameProtocol;

/**
 * This class implements a round robin protocol for the grounded game.
 * 
 * @author Matthias Thimm
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
		Agent maxAgent = null;
		float maxUtility = Float.MIN_VALUE;
		for(Agent a: this.getMultiAgentSystem()){
			float util = ((ArguingAgent)a).getUtility(((GroundedEnvironment)this.getMultiAgentSystem().getEnvironment()).getDialogueTrace());
			if(util > maxUtility){
				maxAgent = a;
				maxUtility = util; 
			}				
		}
		return maxAgent;
	}

}
