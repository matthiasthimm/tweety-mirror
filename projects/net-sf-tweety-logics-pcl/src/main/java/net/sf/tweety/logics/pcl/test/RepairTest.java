package net.sf.tweety.logics.pcl.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.analysis.MinimumViolationMachineShop;
import net.sf.tweety.logics.pcl.parser.*;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.math.norm.PNorm;

public class RepairTest {

	public static void main(String[] args) throws ParserException, IOException{
		// some inconsistent belief base
		PclBeliefSet kb = new PclBeliefSet();
		PclParser parser = new PclParser();
		kb.add((ProbabilisticConditional)parser.parseFormula("(A)[0]"));
		kb.add((ProbabilisticConditional)parser.parseFormula("(A)[1]"));	
		kb.add((ProbabilisticConditional)parser.parseFormula("(A)[0.9999]"));
		System.out.println(kb);
		
		// repair
		MinimumViolationMachineShop ms = new MinimumViolationMachineShop(new PNorm(2));
		System.out.println(ms.repair(kb));
		
	}
}
