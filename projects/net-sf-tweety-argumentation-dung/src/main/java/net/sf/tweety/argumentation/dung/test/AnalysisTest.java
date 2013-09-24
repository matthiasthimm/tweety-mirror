package net.sf.tweety.argumentation.dung.test;

import net.sf.tweety.argumentation.dung.*;
import net.sf.tweety.argumentation.dung.semantics.*;
import net.sf.tweety.argumentation.dung.syntax.*;
import net.sf.tweety.argumentation.util.DungTheoryGenerator;
import net.sf.tweety.argumentation.util.IsoSafeEnumeratingDungTheoryGenerator;
import net.sf.tweety.graphs.Graph;
import net.sf.tweety.graphs.util.GraphUtil;
import net.sf.tweety.math.ComplexNumber;

public class AnalysisTest {

	public static void EigenvalueTest(){
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
	
	public static void PageRankTest(){
		DungTheoryGenerator gen = new IsoSafeEnumeratingDungTheoryGenerator();
		GroundReasoner reasoner;
		for(int i = 0; i < 800; i++){
			DungTheory theory = gen.generate();
			// skip theories with selfloops for now
			if(theory.hasSelfLoops()) continue;
			reasoner = new GroundReasoner(theory);
			Labeling lab = new Labeling(theory,reasoner.getExtensions().iterator().next());
			
			DungTheory invertedTheory = theory;//.getComplementGraph(Graph.IGNORE_SELFLOOPS);
			//System.out.println(theory + "\t\t" + invertedTheory);
			for(Argument arg: invertedTheory){
				for(Argument arg2: invertedTheory)
					if(lab.get(arg).equals(ArgumentStatus.IN) && lab.get(arg2).equals(ArgumentStatus.OUT)){
						if( GraphUtil.pageRank(invertedTheory, arg, 0.75, 0.0001) > GraphUtil.pageRank(invertedTheory, arg2, 0.75, 0.0001)){
							System.out.print(".");
						}else{
							System.out.println(".");
							System.out.println(theory + "\t\t" + invertedTheory);
							System.out.println(lab.get(arg) + "\t" + GraphUtil.pageRank(invertedTheory, arg, 0.75, 0.0001));
							System.out.println(lab.get(arg2) + "\t" + GraphUtil.pageRank(invertedTheory, arg2, 0.75, 0.0001));
						}
					}
					
				//System.out.println(lab.get(arg) + "\t" + GraphUtil.pageRank(invertedTheory, arg, 0.75, 0.001));
			}
			//System.out.println();
			
		}
	}

	public static void PageRankTest2(){
		GroundReasoner reasoner;
		for(int i = 0; i < 100; i++){
			DungTheory theory = new DungTheory();
			Argument prev = new Argument("A0");
			theory.add(prev);
			for(int j = 1; j <= i; j++){
				Argument next = new Argument("A"+j);
				theory.add(next);
				theory.add(new Attack(prev,next));
				prev = next;
			}
			
			reasoner = new GroundReasoner(theory);
			Labeling lab = new Labeling(theory,reasoner.getExtensions().iterator().next());
			
			DungTheory invertedTheory = theory.getComplementGraph(Graph.IGNORE_SELFLOOPS);
			System.out.println(theory + "\t\t" + invertedTheory);
			for(Argument arg: invertedTheory){
				System.out.println(arg + "\t" + lab.get(arg) + "\t" + GraphUtil.hitsRank(invertedTheory, arg, 0.0001, true) + "\t" + GraphUtil.hitsRank(invertedTheory, arg, 0.0001, false));  //GraphUtil.pageRank(invertedTheory, arg, 0.75, 0.0001));				
			}
			System.out.println();
			
		}
	}
	
	public static void main(String[] args){
		//AnalysisTest.EigenvalueTest();
		//AnalysisTest.PageRankTest();
		AnalysisTest.PageRankTest2();
	}
}


