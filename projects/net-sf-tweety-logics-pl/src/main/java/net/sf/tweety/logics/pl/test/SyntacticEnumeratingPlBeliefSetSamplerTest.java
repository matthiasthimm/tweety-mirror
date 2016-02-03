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

import java.io.File;
import java.io.IOException;

import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.logics.pl.util.SyntacticEnumeratingPlBeliefSetSampler;

public class SyntacticEnumeratingPlBeliefSetSamplerTest {

	public static void main(String[] args) throws IOException{
		// generates all syntactic variations of propositional belief sets 
		// with 0-3 formulas, each formula has maximal length 4, and 4 propositions		
		PropositionalSignature sig = new PropositionalSignature(4);
		SyntacticEnumeratingPlBeliefSetSampler s = new SyntacticEnumeratingPlBeliefSetSampler(sig,4, new File("/Users/mthimm/Desktop/plfiles/"), false);
		int i = 0;
		while(true){
			System.out.println(i++ + "\t" + s.randomSample(0, 3));			
		}
	}
}
