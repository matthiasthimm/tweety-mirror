package net.sf.tweety.logics.markovlogic.test;

import java.io.IOException;
import java.util.*;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.logics.markovlogic.NaiveMlnReasoner;
import net.sf.tweety.logics.markovlogic.analysis.AggregatingCoherenceMeasure;
import net.sf.tweety.logics.markovlogic.analysis.AggregatingDistanceFunction;
import net.sf.tweety.logics.markovlogic.analysis.AggregationFunction;
import net.sf.tweety.logics.markovlogic.analysis.AverageAggregator;
import net.sf.tweety.logics.markovlogic.analysis.DistanceFunction;
import net.sf.tweety.logics.markovlogic.analysis.MaxAggregator;
import net.sf.tweety.logics.markovlogic.analysis.MinAggregator;
import net.sf.tweety.logics.markovlogic.analysis.PNormDistanceFunction;
import net.sf.tweety.logics.markovlogic.analysis.ProductAggregator;
import net.sf.tweety.logics.markovlogic.analysis.SumAggregator;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;

public class MlnTest {

	public static void main(String[] args) throws ParserException, IOException{
		Predicate a = new Predicate("A",1);
		Constant c1 = new Constant("c1");
		Constant c2 = new Constant("c2");
		Constant c3 = new Constant("c3");
		
		FolSignature sig = new FolSignature();
		sig.add(a);
		sig.add(c1);
		sig.add(c2);
		sig.add(c3);
		//for(int i = 0; i< 10; i++)
		//	sig.add(new Constant("d"+i));
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		
		MarkovLogicNetwork mln = new MarkovLogicNetwork();
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("A(X)"), new Double(2)));
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("A(c1)"), new Double(-5)));
				
		System.out.println("MLN: \t\t" + mln);
		System.out.println("Constants: \t" + sig.getConstants());
		System.out.println();
		
		NaiveMlnReasoner reasoner = new NaiveMlnReasoner(mln, sig);
			
		List<AggregationFunction> aggrFunctions = new ArrayList<AggregationFunction>();
		aggrFunctions.add(new MaxAggregator());
		aggrFunctions.add(new MinAggregator());
		aggrFunctions.add(new AverageAggregator());
		aggrFunctions.add(new SumAggregator());
		aggrFunctions.add(new ProductAggregator());
		
		List<DistanceFunction> distFunctions = new ArrayList<DistanceFunction>();
		for(AggregationFunction af: aggrFunctions)
			distFunctions.add(new AggregatingDistanceFunction(af));
		for(int i = 1; i< 4; i++)
			distFunctions.add(new PNormDistanceFunction(i));
		
		for(AggregationFunction af: aggrFunctions)
			for(DistanceFunction df: distFunctions){
				AggregatingCoherenceMeasure measure = new AggregatingCoherenceMeasure(df,af);
				System.out.print(measure.toString());
				for(int i = 0; i+measure.toString().length() < 30; i++)
					System.out.print(" ");
				System.out.println(measure.coherence(mln, reasoner, sig));
			}		
	}
}
