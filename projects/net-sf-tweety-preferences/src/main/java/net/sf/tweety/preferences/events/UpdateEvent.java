package net.sf.tweety.preferences.events;

import java.util.EventObject;

import net.sf.tweety.preferences.aggregation.DynamicPreferenceAggregator;


/**
 * The class for event objects used in dynamic preference aggregation
 * 
 * @author Bastian Wolf
 *
 * @param <T>
 */

public class UpdateEvent<T> extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * constructor for an update
	 * @param source the event source
	 * @param u the update
	 */
	public UpdateEvent(DynamicPreferenceAggregator<T> source) {
		super(source);
	}
	

}
