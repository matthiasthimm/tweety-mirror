package net.sf.tweety.preferences.events;

import java.util.EventListener;
//Event-Listener

/**
 * The interface for UpdateListener used for dynamic preference aggregation
 * 
 * @author Bastian Wolf
 *
 * @param <T> the generic element's type
 */

public interface UpdateListener<T> extends EventListener {

	void eventOccurred(UpdateEvent<T> e);
	
}
