package net.sf.tweety.agents.argumentation.oppmodels.sim;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tweety.agents.argumentation.oppmodels.ArguingAgent;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
import net.sf.tweety.agents.sim.MultiAgentSystemGenerator;
import net.sf.tweety.agents.sim.SimulationParameters;
import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.util.DungTheoryGenerator;

/**
 * Generates grounded games.
 * @author Matthias Thimm
 */
public class GroundedGameGenerator implements MultiAgentSystemGenerator<ArguingAgent,GroundedGameSystem> {

	/** Logger */
	private Log log = LogFactory.getLog(GroundedGameGenerator.class);
	
	/** Key for the simulation parameter which refers to the universal theory generated. */
	public static final int PARAM_UNIVERSALTHEORY = 0;
	/** Key for the simulation parameter which refers to the argument of the dialogue. */
	public static final int PARAM_ARGUMENT = 1;

	/** The percentage of the arguments known to the PRO agent. */
	private double viewPercentagePro;
	/** The percentage of the arguments known to the CON agent. */
	private double viewPercentageCon;
	
	/** for generating Dung theories. */
	private DungTheoryGenerator gen;
	
	/** Random numbers generator. */
	private Random random = new Random();
	
	/**
	 * Creates a new game generator.
	 * @param gen for generating Dung theories.
	 * @param viewPercentagePro the percentage of the arguments known to the PRO agent
	 * @param viewPercentageCon the percentage of the arguments known to the CON agent
	 */
	public GroundedGameGenerator(DungTheoryGenerator gen, double viewPercentagePro, double viewPercentageCon){
		this.gen = gen;
		this.viewPercentagePro = viewPercentagePro;
		this.viewPercentageCon = viewPercentageCon;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.sim.MultiAgentSystemGenerator#generate(net.sf.tweety.agents.sim.SimulationParameters)
	 */
	@Override
	public GroundedGameSystem generate(SimulationParameters params) {
		this.log.info("Starting to generate a grounded game system");
		Argument arg = new Argument("A");
		DungTheory theory = this.gen.generate(arg);		
		this.log.trace("Generated Dung theory with " + this.gen + ":\n" +
				"=========\n" + theory.toString() + "\n=========");		
		this.log.trace("Central argument of dialog is: " + arg);		
		params.put(GroundedGameGenerator.PARAM_UNIVERSALTHEORY, theory);
		params.put(GroundedGameGenerator.PARAM_ARGUMENT, arg);
		// generate each agent view
		Extension proView = new Extension();
		Extension conView = new Extension();		
		//both views must contain the argument of the discussion
		proView.add(arg);
		conView.add(arg);
		for(Argument a: theory){
			if(this.random.nextDouble()<= this.viewPercentagePro)
				proView.add(a);
			if(this.random.nextDouble()<= this.viewPercentageCon)
				conView.add(a);
		}
		params.put(GroundedGameSystem.AgentFaction.PRO, proView);
		params.put(GroundedGameSystem.AgentFaction.CONTRA, conView);
		this.log.trace("Arguments in the view of agent PRO are:\n" +
				"=========\n" + proView + "\n=========");
		this.log.trace("Arguments in the view of agent CON are:\n" +
				"=========\n" + conView + "\n=========");
		this.log.info("Ending to generate a grounded game system");
		return new GroundedGameSystem(theory);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.sim.MultiAgentSystemGenerator#setSeed(long)
	 */
	public void setSeed(long seed){
		this.random = new Random(seed);
	}
}
