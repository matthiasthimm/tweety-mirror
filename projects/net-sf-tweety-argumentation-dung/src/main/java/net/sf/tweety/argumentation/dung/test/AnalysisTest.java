package net.sf.tweety.argumentation.dung.test;

import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.StableReasoner;
import net.sf.tweety.argumentation.util.DungTheoryGenerator;
import net.sf.tweety.argumentation.util.IsoSafeEnumeratingDungTheoryGenerator;
import net.sf.tweety.graphs.util.GraphUtil;
import net.sf.tweety.math.ComplexNumber;

public class AnalysisTest {

	public static void main(String[] args){
		DungTheoryGenerator gen = new IsoSafeEnumeratingDungTheoryGenerator();
		StableReasoner reasoner;
		// 1: theory
		// 2: number of stable extensions
		// 3: number of Eigenvalues
		// 4: actual Eigenvalues
		
		for(int i = 0; i < 40; i++){
			DungTheory theory = gen.generate();
			reasoner = new StableReasoner(theory);			
			
			ComplexNumber[] eigenvalues = GraphUtil.eigenvalues(theory);
			System.out.print(theory + "\t" +
					reasoner.getExtensions().size() + "\t" +
					eigenvalues.length + "\t");			 
			for(ComplexNumber n: eigenvalues){
				System.out.print(n +"\t");
			}
			System.out.println();
		}
		
	}
}

