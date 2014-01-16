package net.sf.tweety.agents.sim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.tweety.agents.AbstractProtocol;
import net.sf.tweety.agents.Agent;
import net.sf.tweety.agents.MultiAgentSystem;
import net.sf.tweety.agents.ProtocolTerminatedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a game simulator. It takes some agent and multi-agent system generators
 * and runs a series of simulations. In each simulation the winner of the game is determined and
 * recorded.
 * @author Matthias Thimm
 * @param <T> The actual type of agents.
 * @param <S> The actual type of protocols.
 * @param <R> The actual type of the multi-agent system.
 */
public class GameSimulator<S extends AbstractProtocol & GameProtocol, T extends Agent, R extends MultiAgentSystem<T>> {

	/** Logger */
	static private Logger log = LoggerFactory.getLogger(GameSimulator.class);
	
	/** The multi-agent system generator. */
	private MultiAgentSystemGenerator<T,R> masGenerator;
	/** The protocol generator. */
	private ProtocolGenerator<S,T,R> protGenerator;
	/** The agent generators. */
	private List<AgentGenerator<T,R>> agentGenerators;
	
	/**
	 * Creates a new GameSimulator for the given
	 * MultiAgentSystemGenerator and AgentGenerators.
	 * @param masGenerator a MultiAgentSystemGenerator 
	 * @param protGenerator a protocol generator
	 * @param agentGenerators some AgentGenerators.
	 */
	public GameSimulator(MultiAgentSystemGenerator<T,R> masGenerator, ProtocolGenerator<S,T,R> protGenerator, List<AgentGenerator<T,R>> agentGenerators){
		this.masGenerator = masGenerator;
		this.agentGenerators = agentGenerators;
		this.protGenerator = protGenerator;
	}
	
	/**
	 * Simulates the game for the given number of
	 * repetitions and returns a map indicating
	 * which agent generator won how often.
	 * @param repetitions the number of repetitions.
	 * @return a map which assigns to each agent generator
	 *  the number of times an agent generated by it won the game.
	 * @throws ProtocolTerminatedException if a protocol is asked
	 *  to perform a step but has already terminated. 
	 */
	public SimulationResult<S,T,R> run(int repetitions) throws ProtocolTerminatedException{
		SimulationResult<S,T,R> result = new SimulationResult<S,T,R>(this.agentGenerators);
		for(int i = 0; i < repetitions; i++){
			log.info("Starting simulation run #" + (i+1) + "/" + repetitions);
			Map<Agent,AgentGenerator<T,R>> a2ag = new HashMap<Agent,AgentGenerator<T,R>>();
			SimulationParameters params = new SimulationParameters();
			R mas = this.masGenerator.generate(params);
			// create agents
			for(AgentGenerator<T,R> ag: this.agentGenerators){
				T a = ag.generate(mas,params);
				mas.add(a);
				a2ag.put(a, ag);
			}		
			S prot = this.protGenerator.generate(mas,params);
			mas.execute(prot);	
			if(prot.hasWinner()){
				Agent winner = prot.getWinner();
				Map<AgentGenerator<T,R>,Double> utilities = new HashMap<AgentGenerator<T,R>,Double>();
				for(Agent a: a2ag.keySet())
					utilities.put(a2ag.get(a), prot.getUtility(a));
				result.addEntry(a2ag.get(winner), utilities);				
				log.info("Winner of simulation run #" + (i+1) + "/" + repetitions + " is " + winner);
			}
			log.info("Ending simulation run #" + (i+1) + "/" + repetitions);
		}		
		return result;
	}
}
