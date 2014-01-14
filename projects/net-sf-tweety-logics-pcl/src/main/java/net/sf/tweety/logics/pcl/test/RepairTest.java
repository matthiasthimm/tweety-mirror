package net.sf.tweety.logics.pcl.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.analysis.MinimumViolationMachineShop;
import net.sf.tweety.logics.pcl.parser.*;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.math.norm.PNorm;
import net.sf.tweety.math.opt.solver.OpenOptWebSolver;

public class RepairTest {

	public static void main(String[] args) throws ParserException, IOException{
		// configure web service 
		OpenOptWebSolver.openopt_webservice_url = "http://141.26.24.199/opt/openopt/solve.php";		
		OpenOptWebSolver.openopt_webservice_apikey = "Please request an API key from Matthias (thimm@uni-koblenz.de) or set up your own web service with the file OpenOptWebSolver.php (OpenOpt has to be installed on the server)";
		
		// some inconsistent belief base
		PclBeliefSet kb = new PclBeliefSet();
		PclParser parser = new PclParser();
		kb.add((ProbabilisticConditional)parser.parseFormula("(A)[0]"));
		kb.add((ProbabilisticConditional)parser.parseFormula("(A)[1]"));
		System.out.println(kb);
		
		// repair
		MinimumViolationMachineShop ms = new MinimumViolationMachineShop(new PNorm(2));
		System.out.println(ms.repair(kb));
		
	}
}