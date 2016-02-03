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

import java.io.IOException;

import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.LingelingSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class LingelingTest {
public static void main(String[] args) throws ParserException, IOException{
		
		
		PlBeliefSet beliefSet = new PlBeliefSet();
		PlParser parser = new PlParser();
		beliefSet.add((PropositionalFormula)parser.parseFormula("a || b || c"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("!a || b && d"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("a"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("!c"));
		
		System.out.println(beliefSet);
		
		SatSolver solver = new LingelingSolver("/Users/mthimm/Projects/misc_bins/lingeling");
		
		
		System.out.println(solver.getWitness(beliefSet));
	}
}
