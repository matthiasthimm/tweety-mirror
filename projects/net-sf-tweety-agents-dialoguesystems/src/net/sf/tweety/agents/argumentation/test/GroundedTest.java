package net.sf.tweety.agents.argumentation.test;

import java.util.*;

import net.sf.tweety.TweetyConfiguration;
import net.sf.tweety.TweetyLogging;
import net.sf.tweety.agents.ProtocolTerminatedException;
import net.sf.tweety.agents.argumentation.oppmodels.ArguingAgent;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameProtocol;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameProtocolGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameT1AgentGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameT2AgentGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameT3AgentGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.T1Configuration;
import net.sf.tweety.agents.argumentation.oppmodels.sim.T2Configuration;
import net.sf.tweety.agents.argumentation.oppmodels.sim.T3Configuration;
import net.sf.tweety.agents.sim.AgentGenerator;
import net.sf.tweety.agents.sim.GameSimulator;
import net.sf.tweety.agents.sim.MultiAgentSystemGenerator;
import net.sf.tweety.agents.sim.ProtocolGenerator;
import net.sf.tweety.agents.sim.SimulationResult;
import net.sf.tweety.argumentation.util.DefaultDungTheoryGenerator;
import net.sf.tweety.argumentation.util.DungTheoryGenerationParameters;
import net.sf.tweety.argumentation.util.DungTheoryGenerator;

public class GroundedTest {
	
	//Global parameters for simulation
	public static int frameworkSize;
	public static double attackProbability;
	public static boolean enforceTreeShape;
	
	public static int numberOfRunsEach = 100;
	
	/**
	 * This method shows that with increasing complexity of the T1-belief state of
	 * the CONTRA agent (and constant model of the PRO agent), the average utility of
	 * the CONTRA agent increases. NOTE: the simulation might take a while.
	 * @throws ProtocolTerminatedException
	 */
	public static void runSimulationT1() throws ProtocolTerminatedException{
		// We run different simulations with increasing recursion depth
		// of the CON agent's belief state
		for(int i = 0; i < 3; i++){
			// We generate Dung theories with 10 arguments and attack probability 0.3.
			// In every theory, the argument under consideration is guaranteed to
			// be in the grounded extension (so under perfect information, the PRO
			// agent should always win)
			DungTheoryGenerationParameters params = new DungTheoryGenerationParameters();
			params.attackProbability = GroundedTest.attackProbability;
			params.numberOfArguments = GroundedTest.frameworkSize;	
			params.enforceTreeShape = GroundedTest.enforceTreeShape;	
			DungTheoryGenerator gen = new DefaultDungTheoryGenerator(params);
			// PRO agent knows 50% of all arguments, CONTRA agent knows 90% of all arguments
			MultiAgentSystemGenerator<ArguingAgent,GroundedGameSystem> masGenerator = new GroundedGameGenerator(gen, 0.5, 0.9);
			List<AgentGenerator<ArguingAgent,GroundedGameSystem>> agentGenerators = new ArrayList<AgentGenerator<ArguingAgent,GroundedGameSystem>>();
			
			// The PRO agent has a T1 belief state without opponent model
			T1Configuration configPro = new T1Configuration();
			configPro.maxRecursionDepth = 0;
			configPro.probRecursionDecay = 0;
			configPro.oppModelCorrect = true;
			// The CONTRA agent has a T1 belief state of depth i,
			// every sub-model correctly and completely models the PRO agent
			T1Configuration configCon = new T1Configuration();
			configCon.maxRecursionDepth = i;
			configCon.probRecursionDecay = 0;
			configCon.oppModelCorrect = true;
			
			agentGenerators.add(new GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction.PRO,configPro));
			agentGenerators.add(new GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction.CONTRA,configCon));
			
			ProtocolGenerator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> protGenerator = new GroundedGameProtocolGenerator();
			GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> sim = new GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem>(masGenerator,protGenerator,agentGenerators);
			// Run iterated simulations and show aggregated results
			SimulationResult<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> result = sim.run(GroundedTest.numberOfRunsEach);
			System.out.println("================= T1 vs T1 ==== " + GroundedTest.frameworkSize + " arguments, " + GroundedTest.attackProbability + " attack probability, " + (GroundedTest.enforceTreeShape?("tree shape"):("no tree shape")) + " ==========");
			System.out.println("Depth of CONTRA agent model (T1): " + i);
			System.out.println(result.display());
		}
	}
	
	/**
	 * Same as runSimulationT1()
	 * @throws ProtocolTerminatedException
	 */
	public static void runSimulationT2() throws ProtocolTerminatedException{
		// We run different simulations with increasing complexity
		// of the CON agent's belief state
		int depth[] = {1,2,3,4};				//depth complexity
		double decay[] = {0.4,0.3,0.2,0.1};		//decay complexity
		int width[] = {4,3,2,2};				//width complexity
		for(int i = 0; i < 3; i++){
			DungTheoryGenerationParameters params = new DungTheoryGenerationParameters();
			params.attackProbability = GroundedTest.attackProbability;
			params.numberOfArguments = GroundedTest.frameworkSize;			
			params.enforceTreeShape = GroundedTest.enforceTreeShape;	
			DungTheoryGenerator gen = new DefaultDungTheoryGenerator(params);
			// PRO agent knows 50% of all arguments, CONTRA agent knows 90% of all arguments
			MultiAgentSystemGenerator<ArguingAgent,GroundedGameSystem> masGenerator = new GroundedGameGenerator(gen, 0.5, 0.9);
			List<AgentGenerator<ArguingAgent,GroundedGameSystem>> agentGenerators = new ArrayList<AgentGenerator<ArguingAgent,GroundedGameSystem>>();
		
			// The PRO agent has a T1 belief state without opponent model
			T1Configuration configPro = new T1Configuration();
			configPro.maxRecursionDepth = 0;
			configPro.probRecursionDecay = 0;
			configPro.oppModelCorrect = true;
			// The CONTRA agent has a T2 belief state of complexity i
			T2Configuration configCon = new T2Configuration();
			configCon.maxRecursionDepth = depth[i];
			configCon.probRecursionDecay = decay[i];
			configCon.maxRecursionWidth = width[i];
		
			agentGenerators.add(new GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction.PRO,configPro));
			agentGenerators.add(new GroundedGameT2AgentGenerator(GroundedGameSystem.AgentFaction.CONTRA,configCon));
			
			ProtocolGenerator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> protGenerator = new GroundedGameProtocolGenerator();
			GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> sim = new GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem>(masGenerator,protGenerator,agentGenerators);
			// run iterated simulations and show aggregated results
			SimulationResult<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> result = sim.run(GroundedTest.numberOfRunsEach);		
			System.out.println("================= T1 vs T2 ==== " + GroundedTest.frameworkSize + " arguments, " + GroundedTest.attackProbability + " attack probability, " + (GroundedTest.enforceTreeShape?("tree shape"):("no tree shape")) + " ==========");			
			System.out.println("Complexity of CONTRA agent model (T2): (" + depth[i] + "," + decay[i] + "," + width[i] + ")");
			System.out.println(result.display());
		}
	}
	
	/**
	 * Same as runSimulationT1()
	 * @throws ProtocolTerminatedException
	 */
	public static void runSimulationT3() throws ProtocolTerminatedException{
		// We run different simulations with increasing complexity
		// of the CON agent's belief state
		int depth[] = {1,2,3,4};				//depth complexity
		double decay[] = {0.4,0.3,0.2,0.1};		//decay complexity
		int width[] = {4,3,2,2};				//width complexity
		double virtArg[] = {0.3,0.25,0.2,0.15};	//virtual arguments
		double virtAtt[] = {0.6,0.7,0.8,0.9};	//virtual attacks
		for(int i = 0; i < 3; i++){
			DungTheoryGenerationParameters params = new DungTheoryGenerationParameters();
			params.attackProbability = GroundedTest.attackProbability;
			params.numberOfArguments = GroundedTest.frameworkSize;	
			params.enforceTreeShape = GroundedTest.enforceTreeShape;
			DungTheoryGenerator gen = new DefaultDungTheoryGenerator(params);
			// PRO agent knows 50% of all arguments, CONTRA agent knows 90% of all arguments
			MultiAgentSystemGenerator<ArguingAgent,GroundedGameSystem> masGenerator = new GroundedGameGenerator(gen, 0.5, 0.9);
			List<AgentGenerator<ArguingAgent,GroundedGameSystem>> agentGenerators = new ArrayList<AgentGenerator<ArguingAgent,GroundedGameSystem>>();
		
			// The PRO agent has a T1 belief state without opponent model
			T1Configuration configPro = new T1Configuration();
			configPro.maxRecursionDepth = 0;
			configPro.probRecursionDecay = 0;
			configPro.oppModelCorrect = true;
			// The CONTRA agent has a T3 belief state
			T3Configuration configCon = new T3Configuration();
			configCon.maxRecursionDepth = depth[i];
			configCon.probRecursionDecay = decay[i];
			configCon.maxRecursionWidth = width[i];
			configCon.percentageVirtualArguments = virtArg[i];
			configCon.percentageVirtualAttacks = virtAtt[i];
				
			agentGenerators.add(new GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction.PRO,configPro));
			agentGenerators.add(new GroundedGameT3AgentGenerator(GroundedGameSystem.AgentFaction.CONTRA,configCon));
		
			ProtocolGenerator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> protGenerator = new GroundedGameProtocolGenerator();
			GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> sim = new GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem>(masGenerator,protGenerator,agentGenerators);
			// run iterated simulations and show aggregated results
			SimulationResult<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> result = sim.run(GroundedTest.numberOfRunsEach);
			System.out.println("================= T1 vs T3 ==== " + GroundedTest.frameworkSize + " arguments, " + GroundedTest.attackProbability + " attack probability, " + (GroundedTest.enforceTreeShape?("tree shape"):("no tree shape")) + " ==========");
			System.out.println("Complexity of CONTRA agent model (T3): (" + depth[i] + "," + decay[i] + "," + width[i] + "," + virtArg[i] + "," + virtAtt[i] + ")");
			System.out.println(result.display());
		}
	}
	
	public static void main(String[] args) throws ProtocolTerminatedException{
		// set logging level to "TRACE" to get detailed descriptions
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.ERROR;
		TweetyLogging.initLogging();
				
		for(int numArguments = 10; numArguments <= 30; numArguments += 5){
			GroundedTest.frameworkSize = numArguments;
			for(double attackProbability = 0.2; attackProbability <= 0.4; attackProbability += 0.5){
				GroundedTest.attackProbability = attackProbability;
				GroundedTest.enforceTreeShape = true;
				GroundedTest.runSimulationT1();
				GroundedTest.runSimulationT2();
				GroundedTest.runSimulationT3();
				GroundedTest.enforceTreeShape = false;
				GroundedTest.runSimulationT1();
				GroundedTest.runSimulationT2();
				GroundedTest.runSimulationT3();
			}
		}
	}
}
