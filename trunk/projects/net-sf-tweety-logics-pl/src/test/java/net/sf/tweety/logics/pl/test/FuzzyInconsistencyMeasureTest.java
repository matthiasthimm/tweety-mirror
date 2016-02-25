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
package net.sf.tweety.logics.pl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.analysis.FuzzyInconsistencyMeasure;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.math.func.fuzzy.MinimumNorm;
import net.sf.tweety.math.func.fuzzy.ProductNorm;
import net.sf.tweety.math.opt.Solver;
import net.sf.tweety.math.opt.solver.ApacheCommonsCMAESOptimizer;

public class FuzzyInconsistencyMeasureTest {
	
	@Before
	public void setUp() {		
		Solver.setDefaultGeneralSolver(new ApacheCommonsCMAESOptimizer(200,20000,0,true,200,200,0.00000001));
	}
	
	@Test
	public void test() throws ParserException, IOException {
		double accuracy = 0.001;				
				
		PlParser parser = new PlParser();
		PlBeliefSet bs = new PlBeliefSet();
		bs.add((PropositionalFormula) parser.parseFormula("a"));
		bs.add((PropositionalFormula) parser.parseFormula("!a"));
		
		FuzzyInconsistencyMeasure mes_min = new FuzzyInconsistencyMeasure(new MinimumNorm());
		FuzzyInconsistencyMeasure mes_prod = new FuzzyInconsistencyMeasure(new ProductNorm());
		
		assertEquals(mes_min.inconsistencyMeasure(bs), 0.5, accuracy);
		assertEquals(mes_prod.inconsistencyMeasure(bs), 0.75, accuracy);
	}

}
