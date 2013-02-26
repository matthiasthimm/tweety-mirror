package net.sf.tweety.logics.probabilisticconditionallogic.test;

import java.io.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.probabilisticconditionallogic.*;
import net.sf.tweety.logics.probabilisticconditionallogic.analysis.*;
import net.sf.tweety.logics.probabilisticconditionallogic.syntax.*;

public class AnalysisTest {
	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException{
		//TweetyLogging.logLevel = TweetyConfiguration.LogLevel.ERROR;
		//TweetyLogging.initLogging();		
		
		PclBeliefSet beliefSet = (PclBeliefSet) new net.sf.tweety.logics.probabilisticconditionallogic.parser.PclParser().parseBeliefBaseFromFile("/Users/mthimm/Desktop/test.pcl");
	
		InconsistencyMeasure dist = new DistanceMinimizationInconsistencyMeasure();
		MeanDistanceCulpabilityMeasure cp = new MeanDistanceCulpabilityMeasure(false);
		System.out.println(beliefSet);
		System.out.println(dist.inconsistencyMeasure(beliefSet));
		
		for(ProbabilisticConditional pc: beliefSet)
			System.out.println(pc + "\t" + cp.culpabilityMeasure(beliefSet, pc));
		
		PenalizingCreepingMachineShop ms = new PenalizingCreepingMachineShop();
		BalancedMachineShop ms2 = new BalancedMachineShop(cp);
		System.out.print(ms.repair(beliefSet));
		System.out.print(ms2.repair(beliefSet));
	}
}
