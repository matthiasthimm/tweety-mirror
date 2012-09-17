package net.sf.tweety.testing;


import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.util.*;



/**
 * A first simple testing-method for preference orders. To be extended and automated
 * for preference aggregation later.
 * 
 * @author Bastian Wolf
 */

public class Testing {

	public static void main(String[] args) {
		
		// StringPO to RankingFunction and back to StringPO
		
		PreferenceOrder<String> testing = new PreferenceOrder<String>();
		
		String alpha = "a";
		String beta = "b";
		String gamma = "c";
		String delta = "d";
		String eps = "e";
		
		Pair<String, String> one = new Pair<String, String>(alpha, beta);
		Pair<String, String> two = new Pair<String, String>(alpha, gamma);
		Pair<String, String> three = new Pair<String, String>(alpha, delta);
		Pair<String, String> eight = new Pair<String, String>(gamma, delta);
		Pair<String, String> five = new Pair<String, String>(gamma, beta);
		Pair<String, String> six = new Pair<String, String>(beta, delta);
		Pair<String, String> seven = new Pair<String, String>(eps, beta);
		Pair<String, String> nine = new Pair<String, String>(gamma, eps);
		Pair<String, String> ten = new Pair<String, String>(eps, delta);
		Pair<String, String> four = new Pair<String, String>(alpha, eps);
		
		testing.add(one);
		testing.add(two);testing.add(three);
		
		testing.add(five);
		testing.add(six);
		
		testing.add(eight);
		testing.add(nine);testing.add(ten);
		testing.add(four);testing.add(seven);
	
		
		// String-Order
		
	
//		Pair<String, String> alpha = new Pair<String, String>("a1", "a2");
//		Pair<String, String> beta = new Pair<String, String>("be1", "be2");
//		Pair<String, String> gamma = new Pair<String, String>("gam1", "gam2");
//		Pair<String, String> delta = new Pair<String, String>("delt1", "delt2");	
//
//		testing.addPair(alpha);
//		testing.addPair("testing1", "testing2");
//		testing.addPair(beta);
//		testing.addPair(gamma);
//		testing.addPair(delta);
		
		//testing.writeToFile("testing-new.po");
		
		//RankingFunction<String> rankFuncTest = new RankingFunction<String>(testing);
		System.out.println(testing.getRankingFunction());
		System.out.println(testing.getRankingFunction().generatePreferenceOrder());
		System.out.println(testing.getRankingFunction().generatePreferenceOrder().isTotal());
		System.out.println(testing.getRankingFunction().generatePreferenceOrder().isTransitive());
//		rankFuncTest.generateRankingFunction(testing);
		
//		Set<Variable> var = rankFuncTest.getRankingFunction().keySet();
		
		
////		PreferenceOrder<String> po = rankFuncTest.generateStringPreferenceOrder();
//		System.out.println("StringPO +++++++++++++++++++++");
//		System.out.println("StringPO: " + po);
//		System.out.println("Elements: " + po.getSingleElements());
//		System.out.println("Totality: " + po.isTotal());
//		System.out.println("Transitivity: " + po.isTransitive());
//		System.out.println("StringPO +++++++++++++++++++++");
		
//		System.out.println(testing.size());
//		System.out.println(testing);
//		
//		testing.removePair(gamma);
//		
////		String[] testingarray = new String[testing.size()];
//		
//		System.out.println(testing.size());
		
		System.out.println("TESTING +++++++++++++++++++++");
		System.out.println("Testing: " + testing);
		System.out.println("Elements: " + testing.getDomainElements());
//		System.out.println("Array: " + testing.toArray(testingarray).toString());
		System.out.println("Totality: " + testing.isTotal());
		System.out.println("Transitivity: " + testing.isTransitive());
		System.out.println("TESTING +++++++++++++++++++++");
		
		
		
//		// Integer-Order
//		
//		PreferenceOrder<Integer> count = new PreferenceOrder<Integer>();
//
//		Pair<Integer, Integer> onetwo = new Pair<Integer, Integer>(1, 2);
//		Pair<Integer, Integer> threefour = new Pair<Integer, Integer>(3, 4);
//		Pair<Integer, Integer> fivesix = new Pair<Integer, Integer>(5, 6);
//		Pair<Integer, Integer> seveneight = new Pair<Integer, Integer>(7, 8);
//		
//		count.addPair(onetwo);
//		count.addPair(threefour);
//		count.addPair(fivesix);
//		count.addPair(seveneight);
//		count.addPair(9, 10);
//		System.out.println(count.size());
//		
////		Integer[] countarray = new Integer[count.size()];
//		
//		
//		System.out.println("COUNT +++++++++++++++++++++");
//		System.out.println("Count: " + count);
//		System.out.println("Elements: " + count.getSingleElements());
////		System.out.println("Array: " + count.toArray(countarray).toString());
//		System.out.println("Totality: " + count.isTotal());
//		System.out.println("Transitivity: " + count.isTransitive());
//		System.out.println("COUNT +++++++++++++++++++++");
//		
//		PreferenceOrder<Integer> totalcount = new PreferenceOrder<Integer>();
//		
//		totalcount.addPair(1, 2);
//		totalcount.addPair(1, 3);
//		totalcount.addPair(2, 3);
//		totalcount.addPair(3, 4);
//		totalcount.addPair(4, 1);
//		totalcount.addPair(4, 2);
//		totalcount.addPair(4, 5);
		
//		totalcount.writeToFile("testingtotalcount.po");
//		
////		Integer[] totalcountarray = new Integer[totalcount.size()];
//		

		
		
//		System.out.println("TOTALCOUNT +++++++++++++++++++++");
//		System.out.println("Totalcount: " + totalcount);
//		System.out.println("Elements: " + totalcount.getSingleElements());
////		System.out.println("Array: " + totalcount.toArray(totalcountarray).toString());
//		System.out.println("Totality: " + totalcount.isTotal());
//		System.out.println("Transitivity: " + totalcount.isTransitive());
//		System.out.println("TOTALCOUNT +++++++++++++++++++++");
		
	}
}
