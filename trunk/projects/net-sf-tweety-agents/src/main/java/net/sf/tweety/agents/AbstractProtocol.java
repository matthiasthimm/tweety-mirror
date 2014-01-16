package net.sf.tweety.agents;

import java.util.*;

/**
 * A protocol gives instructions in which order agents have to be asked
 * for actions in a multi-agent system. This class encapsulates
 * common functionalities of protocols.
 * @author Matthias Thimm
 */
public abstract class AbstractProtocol implements Protocol{

	/**
	 * The multi-agent system this protocol operates on.
	 */
	private MultiAgentSystem<? extends Agent> multiAgentSystem;
	
	/**
	 * The listener of this protocol. 
	 */
	private Set<ProtocolListener> listener;
	
	/**
	 * Creates a new protocol for the given multi-agent system.
	 * @param multiAgentSystem a multi-agent system.
	 */
	public AbstractProtocol(MultiAgentSystem<? extends Agent> multiAgentSystem){
		this.multiAgentSystem = multiAgentSystem;	
		this.listener = new HashSet<ProtocolListener>();
	}
	
	/**
	 * Returns the multi-agent system this protocol operates on.
	 * @return the multi-agent system this protocol operates on.
	 */
	protected MultiAgentSystem<? extends Agent> getMultiAgentSystem(){
		return this.multiAgentSystem;
	}
	
	/**
	 * This method determines if this protocol has terminated,
	 * i.e. whether no further actions are possible.
	 * @return "true" if this protocol system has terminated.
	 */
	protected abstract boolean hasTerminated();
	
	/**
	 * Executes one step of the protocol. This method also performs
	 * some administrative tasks.
	 * @throws ProtocolTerminatedException if the protocol has already terminated.
	 */
	protected final void step() throws ProtocolTerminatedException{
		if(this.hasTerminated())
			throw new ProtocolTerminatedException();
		Set<ActionEvent> events = this.doStep();
		for(ActionEvent actionEvent: events)
			this.notifyActionPerformed(actionEvent);
		if(this.hasTerminated()) this.notifyTerminated();
	}
	
	/**
	 * Executes one step of the protocol.
	 * @throws ProtocolTerminatedException
	 */
	protected abstract Set<ActionEvent> doStep() throws ProtocolTerminatedException;
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Protocol#addProtocolListener(net.sf.tweety.agents.ProtocolListener)
	 */
	public void addProtocolListener(ProtocolListener listener){
		this.listener.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Protocol#removeProtocolListener(net.sf.tweety.agents.ProtocolListener)
	 */
	public boolean removeProtocolListener(ProtocolListener listener){
		return this.listener.remove(listener);
	}
	
	/**
	 * Notifies every listener that this protocol has terminated.
	 */
	protected void notifyTerminated(){
		for(ProtocolListener listener: this.listener)
			listener.protocolTerminated();
	}
	
	/**
	 * Notifies every listener about the given event.
	 * @param actionEvent an action event.
	 */
	protected void notifyActionPerformed(ActionEvent actionEvent){
		for(ProtocolListener listener: this.listener)
			listener.actionPerformed(actionEvent);
	}
}
