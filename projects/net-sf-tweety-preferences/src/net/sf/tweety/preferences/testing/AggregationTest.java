package net.sf.tweety.preferences.testing;

import java.util.ArrayList;

import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.Relation;
import net.sf.tweety.preferences.aggregation.BordaScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.PluralityScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.VetoScoringPreferenceAggregator;
import net.sf.tweety.util.Triple;

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
		
		Triple<String, String, Relation> one = new Triple<String, String, Relation>(alpha, beta, Relation.LESS);
		Triple<String, String, Relation> two = new Triple<String, String, Relation>(alpha, gamma, Relation.LESS);
		Triple<String, String, Relation> three = new Triple<String, String, Relation>(alpha, delta, Relation.LESS);
		Triple<String, String, Relation> four = new Triple<String, String, Relation>(gamma, delta, Relation.LESS);
		Triple<String, String, Relation> five = new Triple<String, String, Relation>(gamma, beta, Relation.LESS);
		Triple<String, String, Relation> six = new Triple<String, String, Relation>(beta, delta, Relation.LESS);
		
		testing.add(one);
		testing.add(two);
		testing.add(three);testing.add(four);testing.add(five);testing.add(six);
		// Second PO
		
		PreferenceOrder<String> plurality = new PreferenceOrder<String>();
		
		Triple<String, String, Relation> ten = new Triple<String, String, Relation>(beta, alpha, Relation.LESS);
		Triple<String, String, Relation> eleven = new Triple<String, String, Relation>(gamma, alpha, Relation.LESS);
		Triple<String, String, Relation> twelve = new Triple<String, String, Relation>(delta, alpha, Relation.LESS);
		Triple<String, String, Relation> thirteen = new Triple<String, String, Relation>(delta, gamma, Relation.LESS);
		Triple<String, String, Relation> fourteen = new Triple<String, String, Relation>(beta, gamma, Relation.LESS);
		Triple<String, String, Relation> fifteen = new Triple<String, String, Relation>(beta, delta, Relation.LESS);
		
		plurality.add(ten);plurality.add(eleven);plurality.add(twelve);plurality.add(thirteen);plurality.add(fourteen);plurality.add(fifteen);
		
		System.out.println(testing);
		System.out.println(testing.getRankingFunction());
		System.out.println(plurality);
		System.out.println(plurality.getRankingFunction());
		
		ArrayList<PreferenceOrder<String>> t = new ArrayList<PreferenceOrder<String>>();
		t.add(testing);
		t.add(plurality);
		
		//----------------------------------------------------------------
		
		PreferenceOrder<String> plur1 = new PreferenceOrder<String>();
		
		Triple<String, String, Relation> a1 = new Triple<String, String, Relation>(gamma, beta, Relation.LESS);
		Triple<String, String, Relation> a2 = new Triple<String, String, Relation>(gamma, alpha, Relation.LESS);
		Triple<String, String, Relation> a3 = new Triple<String, String, Relation>(beta, alpha,  Relation.LESS);
		plur1.add(a1);plur1.add(a2);plur1.add(a3);
		
		PreferenceOrder<String> plur2 = new PreferenceOrder<String>();
		
		Triple<String, String, Relation> b1 = new Triple<String, String, Relation>(beta, gamma, Relation.LESS);
		Triple<String, String, Relation> b2 = new Triple<String, String, Relation>(gamma, alpha, Relation.LESS);
		Triple<String, String, Relation> b3 = new Triple<String, String, Relation>(beta, alpha,  Relation.LESS);
		Triple<String, String, Relation> b4 = new Triple<String, String, Relation>(beta, delta,  Relation.LESS);
		Triple<String, String, Relation> b5 = new Triple<String, String, Relation>(alpha, delta,  Relation.LESS);
		Triple<String, String, Relation> b6 = new Triple<String, String, Relation>(gamma, delta,  Relation.LESS);
		plur2.add(b1);plur2.add(b2);plur2.add(b3);plur2.add(b4);plur2.add(b5);plur2.add(b6);
		
		ArrayList<PreferenceOrder<String>> x = new ArrayList<PreferenceOrder<String>>();
		x.add(plur1); x.add(plur2);
		
		PluralityScoringPreferenceAggregator<String> plurA = new PluralityScoringPreferenceAggregator<String>();
		
		
		PreferenceOrder<String> plurFinA = plurA.aggregate(x);
		System.out.println(plurFinA);
		System.out.println(plurFinA.getRankingFunction());
		
		// Tests may return invalid preference orders where unique ranking functions are not possible
		
		//Plurality Rule
		
		PluralityScoringPreferenceAggregator<String> plur = new PluralityScoringPreferenceAggregator<String>();
		
		PreferenceOrder<String> plurFin = plur.aggregate(t);
		System.out.println(plurFin);
//		System.out.println("Valid Preference Order: " + plurFin.isValid());
		System.out.println(plur.aggregate(t).getRankingFunction());
		
		//Veto Rule
		
		VetoScoringPreferenceAggregator<String> veto = new VetoScoringPreferenceAggregator<String>(3);
		
		PreferenceOrder<String> vetoFin = veto.aggregate(t);
		System.out.println(vetoFin);
//		System.out.println("Valid Preference Order: " + vetoFin.isValid());
		System.out.println(veto.aggregate(t).getRankingFunction());
		
		//Borda Rule
		
		BordaScoringPreferenceAggregator<String> borda = new BordaScoringPreferenceAggregator<String>(4);
		
		PreferenceOrder<String> bordaFin = borda.aggregate(t);
		System.out.println(bordaFin);		
//		System.out.println("Valid Preference Order: " + bordaFin.isValid());
		System.out.println(borda.aggregate(t).getRankingFunction());
	}
	
	public static void main(String[] args) {
		test();
	}
	
}
