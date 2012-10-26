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
import net.sf.tweety.agents.argumentation.oppmodels.sim.T1Configuration;
import net.sf.tweety.agents.argumentation.oppmodels.sim.T2Configuration;
import net.sf.tweety.agents.sim.AgentGenerator;
import net.sf.tweety.agents.sim.GameSimulator;
import net.sf.tweety.agents.sim.MultiAgentSystemGenerator;
import net.sf.tweety.agents.sim.ProtocolGenerator;
import net.sf.tweety.agents.sim.SimulationResult;
import net.sf.tweety.argumentation.util.DefaultDungTheoryGenerator;
import net.sf.tweety.argumentation.util.DungTheoryGenerationParameters;
import net.sf.tweety.argumentation.util.DungTheoryGenerator;

public class GroundedTest {
	
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
			params.attackProbability = 0.3;
			params.numberOfArguments = 10;			
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
			SimulationResult<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> result = sim.run(100);
			System.out.println("=================");
			System.out.println("Depth of CONTRA agent model: " + i);
			System.out.println(result.display());
		}
	}
	
	public static void runSimulationT2() throws ProtocolTerminatedException{
		// UNDER CONSTRUCTION
		DungTheoryGenerationParameters params = new DungTheoryGenerationParameters();
		params.attackProbability = 0.3;
		params.numberOfArguments = 5;			
		DungTheoryGenerator gen = new DefaultDungTheoryGenerator(params);
		// PRO agent knows 50% of all arguments, CONTRA agent knows 90% of all arguments
		MultiAgentSystemGenerator<ArguingAgent,GroundedGameSystem> masGenerator = new GroundedGameGenerator(gen, 0.5, 0.9);
		List<AgentGenerator<ArguingAgent,GroundedGameSystem>> agentGenerators = new ArrayList<AgentGenerator<ArguingAgent,GroundedGameSystem>>();
		
		// The PRO agent has a T1 belief state without opponent model
		T1Configuration configPro = new T1Configuration();
		configPro.maxRecursionDepth = 0;
		configPro.probRecursionDecay = 0;
		configPro.oppModelCorrect = true;
		// The CONTRA agent has a T2 belief state
		T2Configuration configCon = new T2Configuration();
		configCon.maxRecursionDepth = 1;
		configCon.probRecursionDecay = 0.3;
		configCon.maxRecursionWidth = 3;
		
		agentGenerators.add(new GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction.PRO,configPro));
		agentGenerators.add(new GroundedGameT2AgentGenerator(GroundedGameSystem.AgentFaction.CONTRA,configCon));
		
		ProtocolGenerator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> protGenerator = new GroundedGameProtocolGenerator();
		GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> sim = new GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem>(masGenerator,protGenerator,agentGenerators);
		// run iterated simulations and show aggregated results
		SimulationResult<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> result = sim.run(1);		
		System.out.println(result.display());
	}
	
	public static void main(String[] args) throws ProtocolTerminatedException{
		// set logging level to "TRACE" to get detailed descriptions
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.ERROR;
		TweetyLogging.initLogging();
		
		GroundedTest.runSimulationT1();
		//GroundedTest.runSimulationT2();
	}
}
