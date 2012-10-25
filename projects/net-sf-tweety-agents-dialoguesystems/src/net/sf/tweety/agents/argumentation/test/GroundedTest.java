package net.sf.tweety.agents.argumentation.test;

import java.util.*;

import net.sf.tweety.agents.ActionEvent;
import net.sf.tweety.agents.ProtocolListener;
import net.sf.tweety.agents.ProtocolTerminatedException;
import net.sf.tweety.agents.argumentation.oppmodels.ArguingAgent;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameProtocol;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameProtocolGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.GroundedGameT1AgentGenerator;
import net.sf.tweety.agents.argumentation.oppmodels.sim.T1Configuration;
import net.sf.tweety.agents.sim.AgentGenerator;
import net.sf.tweety.agents.sim.GameSimulator;
import net.sf.tweety.agents.sim.MultiAgentSystemGenerator;
import net.sf.tweety.agents.sim.ProtocolGenerator;
import net.sf.tweety.argumentation.dung.DungTheory;

public class GroundedTest {

	public class GroundedGameListener implements ProtocolListener{
		public void actionPerformed(ActionEvent actionEvent) {
			System.out.println(actionEvent.getAgent().getName() + "\t" + actionEvent.getAction());			
		}
		public void protocolTerminated() {
			System.out.println("===== GAME ENDED =====");			
		}		
	}
		
	public void run(){
		ArguingAgent proAgent = new ArguingAgent(GroundedGameSystem.AgentFaction.PRO, null);
		ArguingAgent oppAgent = new ArguingAgent(GroundedGameSystem.AgentFaction.CONTRA, null);
		DungTheory myTheory = new DungTheory();
		GroundedGameSystem system = new GroundedGameSystem(myTheory);
		system.add(proAgent);
		system.add(oppAgent);
		GroundedGameProtocol prot = new GroundedGameProtocol(system);
		prot.addProtocolListener(new GroundedTest.GroundedGameListener());
		try {						
			system.execute(prot);
		} catch (ProtocolTerminatedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("And the winner is..." + prot.getWinner());
		}
	}
	
	public void run2() throws ProtocolTerminatedException{
		MultiAgentSystemGenerator<ArguingAgent,GroundedGameSystem> masGenerator = new GroundedGameGenerator(20, 0.3, 0.95);
		List<AgentGenerator<ArguingAgent,GroundedGameSystem>> agentGenerators = new ArrayList<AgentGenerator<ArguingAgent,GroundedGameSystem>>();
		
		T1Configuration configPro = new T1Configuration();
		configPro.maxRecursionDepth = 3;
		configPro.probRecursionDecay = 0.1;
		configPro.oppModelCorrect = true;
		T1Configuration configCon = new T1Configuration();
		configCon.maxRecursionDepth = 3;
		configCon.probRecursionDecay = 0.1;
		configCon.oppModelCorrect = true;
		
		agentGenerators.add(new GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction.PRO,configPro));
		agentGenerators.add(new GroundedGameT1AgentGenerator(GroundedGameSystem.AgentFaction.CONTRA,configCon));
		
		ProtocolGenerator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> protGenerator = new GroundedGameProtocolGenerator();
		GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem> sim = new GameSimulator<GroundedGameProtocol,ArguingAgent,GroundedGameSystem>(masGenerator,protGenerator,agentGenerators);
		Map<AgentGenerator<ArguingAgent,GroundedGameSystem>,Integer> wins = sim.run(100);
		System.out.println(wins);
	}
	
	public static void main(String[] args) throws ProtocolTerminatedException{
		new GroundedTest().run2();
	}
}
