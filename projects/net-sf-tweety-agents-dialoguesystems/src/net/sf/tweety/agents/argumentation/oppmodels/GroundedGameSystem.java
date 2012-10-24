package net.sf.tweety.agents.argumentation.oppmodels;

import net.sf.tweety.agents.MultiAgentSystem;
import net.sf.tweety.agents.Protocol;
import net.sf.tweety.agents.ProtocolTerminatedException;
import net.sf.tweety.argumentation.dung.DungTheory;

/**
 * This multi-agent system models a grounded dialogue game between
 * two agents.
 * @author Matthias Thimm
 */
public class GroundedGameSystem extends MultiAgentSystem<ArguingAgent> {

	/**
	 * Creates a new grounded game system.
	 * @param universalTheory the universal Dung theory used for argumentation.
	 */
	public GroundedGameSystem(DungTheory universalTheory) {
		super(new GroundedEnvironment(universalTheory));
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.MultiAgentSystem#add(net.sf.tweety.agents.Agent)
	 */
	@Override	
	public boolean add(ArguingAgent e) {
		if(this.size() >= 2)
			throw new IllegalArgumentException("The grounded game is only defined for two agents.");
		return super.add(e);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.MultiAgentSystem#execute(net.sf.tweety.agents.Protocol)
	 */
	@Override
	public void execute(Protocol protocol) throws ProtocolTerminatedException{
		if(this.size() != 2)		
			throw new IllegalArgumentException("The grounded game is only defined for two agents.");
		super.execute(protocol);
	}
}
