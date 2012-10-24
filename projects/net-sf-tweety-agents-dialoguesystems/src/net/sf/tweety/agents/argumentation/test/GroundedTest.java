package net.sf.tweety.agents.argumentation.test;

import net.sf.tweety.agents.ActionEvent;
import net.sf.tweety.agents.Protocol;
import net.sf.tweety.agents.ProtocolListener;
import net.sf.tweety.agents.ProtocolTerminatedException;
import net.sf.tweety.agents.RoundRobinProtocol;
import net.sf.tweety.agents.argumentation.oppmodels.ArguingAgent;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
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
		ArguingAgent proAgent = new ArguingAgent("PRO", null);
		ArguingAgent oppAgent = new ArguingAgent("OPP", null);
		DungTheory myTheory = new DungTheory();
		GroundedGameSystem system = new GroundedGameSystem(myTheory);
		system.add(proAgent);
		system.add(oppAgent);		
		try {
			Protocol prot = new RoundRobinProtocol(system);
			prot.addProtocolListener(new GroundedTest.GroundedGameListener());
			system.execute(prot);
		} catch (ProtocolTerminatedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new GroundedTest().run();
	}
}
