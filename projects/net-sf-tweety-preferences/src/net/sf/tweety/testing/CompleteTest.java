package net.sf.tweety.testing;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.Relation;
import net.sf.tweety.preferences.aggregation.BordaScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.PluralityScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.VetoScoringPreferenceAggregator;
import net.sf.tweety.preferences.io.POParser;
import net.sf.tweety.preferences.io.POWriter;
import net.sf.tweety.preferences.io.ParseException;
import net.sf.tweety.util.Triple;

public class CompleteTest {
	public static void main(String[] args) {
		
		// TODO JUnit-Test
		
		/**
		 * filenames of PO-Files (e.g. "Test.po")
		 * Here named "TestA.po" to "TestE.po"
		 * Testfiles are stored in the root folder of this project, if another folder
		 * is meant to be used, the path has to be edited
		 */
		String[] files = {"TestA.po","TestB.po","TestC.po","TestD.po","TestE.po"};
		
		/**
		 * ArrayList used for aggregation
		 */
		ArrayList<PreferenceOrder<String>> orders = new ArrayList<PreferenceOrder<String>>();
		
		/**
		 * loop for parsing in POs from files
		 */
		for(int i = 0; i < files.length; i++){
			PreferenceOrder<String> po = parseFile(files[i]);
			if (!po.equals(null)){
//				if(po.isValid()){
					orders.add(po);
//				}
			}
		}
		
		/**
		 * Aggregation:
		 * PluralityScoringPreferenceAggregator<String> for plurality rule
		 * VetoScoringPreferenceAggregator<String> for veto rule (parameter: highest rank for this PO (e.g. 0 for rank = 0 (lowest number))
		 * BordaScoringPreferenceAggregator<String> for borda rule (parameter: amount of single elements per preference order)
		 * 
		 * Example:
		 * PluralityScoringPreferenceAggregator<String> plur = new PluralityScoringPreferenceAggregator<String>();
		 * PreferenceOrder<String> plurAgg = plur.aggregate(orders);
		 */
		
		PluralityScoringPreferenceAggregator<String> plur = new PluralityScoringPreferenceAggregator<String>();
		PreferenceOrder<String> plurAgg = plur.aggregate(orders);
		
		BordaScoringPreferenceAggregator<String> borda = new BordaScoringPreferenceAggregator<String>(orders.iterator().next().getDomainElements().size());
		PreferenceOrder<String> bordaAgg = borda.aggregate(orders);
		
		VetoScoringPreferenceAggregator<String> veto = new VetoScoringPreferenceAggregator<String>(0);
		PreferenceOrder<String> vetoAgg = veto.aggregate(orders);
		/**
		 * Methods of preference orders:
		 * 
		 * PreferenceOrder po:
		 * po.getDomainElements(); returns a Set of all single elements
		 * po.getRankingFunction(); returns the ranking function for this set
		 * po.isValid(); checks the validity of the po
		 */
		
		System.out.println(plurAgg.isValid());
		System.out.println(plurAgg);
		System.out.println(plurAgg.getDomainElements());
		System.out.println(plurAgg.getRankingFunction());
		
		System.out.println(bordaAgg.isValid());
		System.out.println(bordaAgg.getDomainElements());
		System.out.println(bordaAgg.getRankingFunction());
		
		System.out.println(vetoAgg.isValid());
		System.out.println(vetoAgg.getDomainElements());
		System.out.println(vetoAgg.getRankingFunction());
		/**
		 * Adding new elements to preference orders:
		 * 
		 * Triple<String, String, Relation> with Relation.LESS or Relation.LESS_EQUAL
		 * 
		 * Example (po is given String-PreferenceOrder):
		 * Triple<String, String, Relation> one = new Triple<String, String, Relation>(alpha, beta, Relation.LESS);
		 * po.add(one);
		 */
		
		
		
		/**
		 * Writing to File:
		 * POWriter<String>
		 * writeToFile(String filename,PreferenceOrder<String> po);
		 * 
		 * Example:
		 * POWriter<String> writer = new POWriter<String>();
		 * writer.writeToFile("TestZ.po", po);
		 */
	}
	
	/**
	 * method for parsing in files
	 * @param filename containing the PO to be parsed in
	 * @return po if successful, null if not
	 */
	public static PreferenceOrder<String> parseFile(String filename) {
		try {
			PreferenceOrder<String> test = new PreferenceOrder<String>();

			test = POParser.parse(filename);
			return test;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("The given file was not found" + e);
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Error while parsing" + e);
			e.printStackTrace();
		}
		return null;
	}
}
