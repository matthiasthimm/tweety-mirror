package net.sf.tweety.preferences;

import net.sf.tweety.util.Pair;

/**
 * A first simple testing-method for preference orders. To be extended and automated
 * for preference aggregation later.
 * 
 * @author Bastian Wolf
 */

public class Testing {

	public static void main(String[] args) {

		// String-Order
		
		PreferenceOrder<String> testing = new PreferenceOrder<String>();

		Pair<String, String> alpha = new Pair<String, String>("a1", "a2");
		Pair<String, String> beta = new Pair<String, String>("be1", "be2");
		Pair<String, String> gamma = new Pair<String, String>("gam1", "gam2");
		Pair<String, String> delta = new Pair<String, String>("delt1", "delt2");

		testing.addPair(alpha);
		testing.addPair("testing1", "testing2");
		testing.addPair(beta);
		testing.addPair(gamma);
		testing.addPair(delta);
		
		System.out.println(testing.size());
		System.out.println(testing);
		
		testing.removePair(gamma);
		
		System.out.println(testing.size());
		System.out.println(testing);

		// Integer-Order
		
		PreferenceOrder<Integer> count = new PreferenceOrder<Integer>();

		Pair<Integer, Integer> onetwo = new Pair<Integer, Integer>(1, 2);
		Pair<Integer, Integer> threefour = new Pair<Integer, Integer>(3, 4);
		Pair<Integer, Integer> fivesix = new Pair<Integer, Integer>(5, 6);
		Pair<Integer, Integer> seveneight = new Pair<Integer, Integer>(7, 8);
		
		count.addPair(onetwo);
		count.addPair(threefour);
		count.addPair(fivesix);
		count.addPair(seveneight);
		count.addPair(9, 10);
		System.out.println(count.size());
		
		System.out.println(count);
		System.out.println(count);
		
		
		
		
	}
}
