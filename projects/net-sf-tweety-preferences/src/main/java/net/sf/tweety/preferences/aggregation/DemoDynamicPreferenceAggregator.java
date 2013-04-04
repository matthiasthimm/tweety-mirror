package net.sf.tweety.preferences.aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.tweety.preferences.Operation;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.events.UpdateEvent;
import net.sf.tweety.preferences.events.UpdateListener;
import net.sf.tweety.preferences.ranking.LevelingFunction;

/**
 * This Demo-class provides a basic implementation similar to the ScoringPreferenceAggregator but dynamic aggregation instead of static
 * @author Bastian Wolf
 *
 * @param <T>
 */

public class DemoDynamicPreferenceAggregator<T> implements
		DynamicPreferenceAggregator<T> {	
	
	/**
	 * The weight vector for the aggregator given via the constructor
	 */
	private WeightVector v;
	
	
	/**
	 * 
	 */
	private List<PreferenceOrder<T>> input;
	
	/**
	 * 
	 */
	private ArrayList<UpdateListener<T>> _listeners;
	
	/**
	 * Constructor with given weight vector
	 * 
	 * @param v
	 *            the weight vector
	 */
	public DemoDynamicPreferenceAggregator(WeightVector v) {
		this._listeners = new ArrayList<UpdateListener<T>>();
		this.v = v;
	}
	
	
	/**
	 * This method aggregates the given preference orders according to the WeightVector used within construction
	 */
	public PreferenceOrder<T> aggregate(List<PreferenceOrder<T>> input) {
//		PreferenceOrder<T> tempPO = new PreferenceOrder<T>();
		this.input = input;
		Map<T, Integer> elem = new HashMap<T, Integer>();
		
		// all single elements are store in one HashMap
		// note that every input-po only consists of the exact same domain
		// elements
		if (!input.isEmpty()) {

			ListIterator<PreferenceOrder<T>> it = input.listIterator();
			if (it.hasNext()) {
				PreferenceOrder<T> tPO = it.next();

				for (T e : tPO.getDomainElements()) {

					if (!elem.containsKey(e)) {
						elem.put(e, 0);
					} else {
						continue;
					}
					
				}
				while (it.hasNext()){
					PreferenceOrder<T> checkPO = it.next();
					for(T e : checkPO.getDomainElements()){
						if(!elem.containsKey(e)){
							//TODO Exception handling for null pointer exception
							System.out.println("Invalid preference order used");;
						}
					}
				}
			}
			
		}
		
		// for each element in each po the weight vector value is
		// requested
		// and
		// subtracted from the current value in the HashMap
		ListIterator<PreferenceOrder<T>> it2 = input.listIterator();
		while (it2.hasNext()) {
			PreferenceOrder<T> tPO = it2.next();
			Map<T, Integer> temp = tPO.getLevelingFunction();
			for (Entry<T, Integer> e : temp.entrySet()) {
				T t = e.getKey();
				Integer i = e.getValue();
				int val = v.getWeight(i);
				elem.put(t, elem.get(t)-val);
			}
		}

		// finally a temporary ranking function is created an generates the
		// aggregated preference order
		
		LevelingFunction<T> tempRF = new LevelingFunction<T>();
		tempRF.putAll(elem);
	
		return tempRF.generatePreferenceOrder();

	}
	
	
	@Override
	/**
	 * The update-method for dynamically changing the input for preference aggregation
	 * @param update the update element containing the changes to be applied
	 */
	public PreferenceOrder<T> update(Update<T> update) {
		
		// get the list-index of the po to be changed 
		int i = input.indexOf(update.getObj1());
		Operation op = update.getObj2();
		T element = update.getObj4();
		
		if(input.get(i).getDomainElements().contains(element)) {
			int amount = update.getObj3();
			if(op == Operation.WEAKEN) {
				while(amount > 0){
					input.get(i).weakenElement(element);
					amount--;
				}
			} else if(op == Operation.STRENGTHEN) {
				while(amount > 0) {
					input.get(i).strengthenElement(element);
					amount--;
				}
			}			
		}
		
		// aggregate the updated preference orders into a new result
		PreferenceOrder<T> result = aggregate(this.input);
		
		// firing a new event for every update to every listener using this result
		UpdateEvent<T> event = new UpdateEvent<T>(this, result);
		fireEvent(event);
		
		// return the newly aggregated result
		return result;
	}

	
	/**
	 * Fires an event every time a change occurred
	 * @param event
	 */
	private void fireEvent(UpdateEvent<T> event){
		
		Iterator<UpdateListener<T>> i = _listeners.iterator();
		
		while(i.hasNext()){
			(i.next()).eventOccurred(event);
		}
		
	}
	
	
	/**
	 * 
	 */
	@Override
	public synchronized void addListener(UpdateListener<T> listener) {
		_listeners.add(listener);
		
	}

	/**
	 * 
	 */
	@Override
	public synchronized void removeListener(UpdateListener<T> listener) {
		_listeners.remove(listener);
		
	}

}