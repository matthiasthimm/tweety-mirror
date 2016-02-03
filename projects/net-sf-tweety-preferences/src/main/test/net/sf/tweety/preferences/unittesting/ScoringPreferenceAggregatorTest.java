/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2016 The Tweety Project Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.preferences.unittesting;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.aggregation.BordaScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.PluralityScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.ScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.VetoScoringPreferenceAggregator;
import net.sf.tweety.preferences.io.POParser;
import net.sf.tweety.preferences.io.ParseException;
import static org.junit.Assert.*;

/**
 * The class <code>ScoringPreferenceAggregatorTest</code> contains tests for the class <code>{@link ScoringPreferenceAggregator}</code>.
 *
 * @generatedBy CodePro at 20.12.12 11:01
 * @author Bastian Wolf
 * @version $Revision: 1.0 $
 */
public class ScoringPreferenceAggregatorTest {
	
	private String[] files = {"TestA.po","TestB.po","TestC.po","TestD.po","TestE.po"};
	
	private ArrayList<PreferenceOrder<String>> orders = new ArrayList<PreferenceOrder<String>>();
	
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
	
	public void initializeOrders(){
		for(int i = 0; i < files.length; i++){
			PreferenceOrder<String> po = parseFile(files[i]);
			if (!po.equals(null)){
//				if(po.isValid()){
					orders.add(po);
//				}
			}
		}
	}
	
	@Test
	public void testPluralityScoringPreferenceAggregator_1()
		throws Exception {

		PluralityScoringPreferenceAggregator<String> resultAggr = new PluralityScoringPreferenceAggregator<String>();

		PreferenceOrder<String> result = resultAggr.aggregate(orders);
		// add additional test code here
		assertNotNull(result);
	}

//	@Ignore
//	@Test
//	public void testPluralityScoringPreferenceAggregator_2()
//		throws Exception {
//
//		PluralityScoringPreferenceAggregator<Integer> resultAggr = new PluralityScoringPreferenceAggregator<Integer>();
//
//		
//		// add additional test code here
//		assertNotNull(result);
//	}
	
	
	
	@Test
	public void testVetoScoringPreferenceAggregator_1()
		throws Exception {

		VetoScoringPreferenceAggregator<String> resultAggr = new VetoScoringPreferenceAggregator<String>(0);

		PreferenceOrder<String> result = resultAggr.aggregate(orders);
		// add additional test code here
		assertNotNull(result);
	}

//	@Ignore
//	@Test
//	public void testVetoScoringPreferenceAggregator_2()
//		throws Exception {
//
//		VetoScoringPreferenceAggregator<Integer> resultAggr = new VetoScoringPreferenceAggregator<Integer>(0);
//
//		// add additional test code here
//		assertNotNull(result);
//	}
	
	
	
	@Test
	public void testBordaScoringPreferenceAggregator_1()
		throws Exception {

		BordaScoringPreferenceAggregator<String> resultAggr = new BordaScoringPreferenceAggregator<String>(orders.iterator().next().getDomainElements().size());

		PreferenceOrder<String> result = resultAggr.aggregate(orders);
		// add additional test code here
		assertNotNull(result);
	}
	
//	@Ignore
//	@Test
//	public void testBordaScoringPreferenceAggregator_2()
//		throws Exception {
//
//		BordaScoringPreferenceAggregator<Integer> resultAggr = new BordaScoringPreferenceAggregator<Integer>(orders.iterator().next().getDomainElements().size());
//
//		// add additional test code here
//		assertNotNull(result);
//	}

	
	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 20.12.12 11:01
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 20.12.12 11:01
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 20.12.12 11:01
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(ScoringPreferenceAggregatorTest.class);
	}
}