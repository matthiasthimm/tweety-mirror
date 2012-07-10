package net.sf.tweety.testing;

import net.sf.tweety.preferences.RankPreferenceOrder;
import net.sf.tweety.preferences.RankedElement;
import net.sf.tweety.util.Pair;

/**
 * First implementation of a testing class for preference orders using
 * ranked elements instead of first used normal elements.
 * 
 * @author Bastian Wolf
 *
 */

public class RankedTesting {

	public static void main(String[] args) {
		
		// ranked StringPreferenceOrder
		RankPreferenceOrder<String> rankedstringtesting = new RankPreferenceOrder<String>();
		
		// elements
		RankedElement<String> alpha = new RankedElement<String>("alpha", 2);
		RankedElement<String> beta = new RankedElement<String>("beta", 3);
		RankedElement<String> gamma = new RankedElement<String>("gamma", 4);
		RankedElement<String> delta = new RankedElement<String>("delta", 5);
		
		// pairs
		Pair<RankedElement<String>, RankedElement<String>> first = new Pair<RankedElement<String>, RankedElement<String>>(alpha, beta);
		Pair<RankedElement<String>, RankedElement<String>> second = new Pair<RankedElement<String>, RankedElement<String>>(beta, gamma);
		Pair<RankedElement<String>, RankedElement<String>> third = new Pair<RankedElement<String>, RankedElement<String>>(gamma, delta);
		Pair<RankedElement<String>, RankedElement<String>> fourth = new Pair<RankedElement<String>, RankedElement<String>>(alpha, gamma);
		Pair<RankedElement<String>, RankedElement<String>> fifth = new Pair<RankedElement<String>, RankedElement<String>>(alpha, delta);
		Pair<RankedElement<String>, RankedElement<String>> six = new Pair<RankedElement<String>, RankedElement<String>>(beta, delta);
		
		// add to order
		rankedstringtesting.addPair(first);
		rankedstringtesting.addPair(second);
		rankedstringtesting.addPair(third);
		rankedstringtesting.addPair(fourth);
		rankedstringtesting.addPair(fifth);
		rankedstringtesting.addPair(six);
		
		// tests
		System.out.println("RankedPreferenceOrder: " + rankedstringtesting);
		System.out.println("Single Elements: " + rankedstringtesting.getSingleElements());
		System.out.println("Totality: " + rankedstringtesting.isTotal());
		System.out.println("Transitivity: " + rankedstringtesting.isTransitive());
	}
	
}
