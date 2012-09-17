package net.sf.tweety.testing;

import java.util.ArrayList;

import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.aggregation.BordaScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.PluralityScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.VetoScoringPreferenceAggregator;
import net.sf.tweety.util.Pair;

/**
 * Simple testclass for preference aggregation
 * @author Bastian Wolf
 *
 */

public class AggregationTest {

	public static void test(){
		
		// First PO
		
		PreferenceOrder<String> testing = new PreferenceOrder<String>();
		
		String alpha = "a";
		String beta = "b";
		String gamma = "c";
		String delta = "d";
		
		Pair<String, String> one = new Pair<String, String>(alpha, beta);
		Pair<String, String> two = new Pair<String, String>(alpha, gamma);
		Pair<String, String> three = new Pair<String, String>(alpha, delta);
		Pair<String, String> eight = new Pair<String, String>(gamma, delta);
		Pair<String, String> five = new Pair<String, String>(gamma, beta);
		Pair<String, String> six = new Pair<String, String>(beta, delta);
		
		testing.add(one);
		testing.add(two);
		testing.add(three);
		testing.add(five);
		testing.add(six);
		testing.add(eight);
		
		// Second PO
		
		PreferenceOrder<String> plurality = new PreferenceOrder<String>();
		
		Pair<String, String> ten = new Pair<String, String>(beta, alpha);
		Pair<String, String> eleven = new Pair<String, String>(gamma, alpha);
		Pair<String, String> twelve = new Pair<String, String>(delta, alpha);
		Pair<String, String> thirteen = new Pair<String, String>(delta, gamma);
		Pair<String, String> fourteen = new Pair<String, String>(beta, gamma);
		Pair<String, String> fiveteen = new Pair<String, String>(beta, delta);
	
		plurality.add(ten);
		plurality.add(eleven);
		plurality.add(twelve);
		plurality.add(thirteen);
		plurality.add(fourteen);
		plurality.add(fiveteen);
		
		System.out.println(testing);
		System.out.println(testing.getRankingFunction());
		System.out.println(plurality);
		System.out.println(plurality.getRankingFunction());
		
		ArrayList<PreferenceOrder<String>> t = new ArrayList<PreferenceOrder<String>>();
		t.add(testing);
		t.add(plurality);
		
		
		// Tests may return invalid preference orders where unique ranking functions are not possible
		
		//Plurality Rule
		
		PluralityScoringPreferenceAggregator<String> plur = new PluralityScoringPreferenceAggregator<String>();
		
		PreferenceOrder<String> plurFin = plur.aggregate(t);
		System.out.println(plurFin);
		System.out.println("Valid Preference Order: " + plurFin.isValid());
//		System.out.println(plur.aggregate(t).getRankingFunction());
		
		//Veto Rule
		
		VetoScoringPreferenceAggregator<String> veto = new VetoScoringPreferenceAggregator<String>(3);
		
		PreferenceOrder<String> vetoFin = veto.aggregate(t);
		System.out.println(vetoFin);
		System.out.println("Valid Preference Order: " + vetoFin.isValid());
//		System.out.println(veto.aggregate(t).getRankingFunction());
		
		//Borda Rule
		
		BordaScoringPreferenceAggregator<String> borda = new BordaScoringPreferenceAggregator<String>(4);
		
		PreferenceOrder<String> bordaFin = borda.aggregate(t);
		System.out.println(bordaFin);		
		System.out.println("Valid Preference Order: " + bordaFin.isValid());
//		System.out.println(borda.aggregate(t).getRankingFunction());
	}
	
	public static void main(String[] args) {
		test();
	}
	
}
