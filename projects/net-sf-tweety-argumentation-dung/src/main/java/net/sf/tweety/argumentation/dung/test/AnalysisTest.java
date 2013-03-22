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
		// 4: largest Eigenvalue
		// 5: real Eigenvalues?
		// 6: imag Eigenvalues?
		
		for(int i = 0; i < 40; i++){
			DungTheory theory = gen.generate();
			reasoner = new StableReasoner(theory);			
			
			ComplexNumber[] eigenvalues = GraphUtil.eigenvalues(theory);
			byte real = 0;
			byte imag = 0;
			ComplexNumber largestRealEV = new ComplexNumber(Double.MIN_VALUE,0); 
			for(ComplexNumber n: eigenvalues){
				if(n.getImagPart() > 0.00001 || n.getImagPart() > 0.00001)
					imag = 1;
				if(n.getRealPart() > 0.00001 || n.getRealPart() > 0.00001){
					real = 1;
					if(n.getRealPart() > largestRealEV.getRealPart())
						largestRealEV = n;
				}
			}
			
			
			System.out.println(theory + "\t" +
					reasoner.getExtensions().size() + "\t" +
					eigenvalues.length + "\t" +
					largestRealEV + "\t" + 
					real + "\t" +
					imag);
		}
		
	}
}

