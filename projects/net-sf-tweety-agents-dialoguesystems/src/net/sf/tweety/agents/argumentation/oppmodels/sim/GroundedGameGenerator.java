package net.sf.tweety.agents.argumentation.oppmodels.sim;

import java.util.Random;

import net.sf.tweety.agents.argumentation.oppmodels.ArguingAgent;
import net.sf.tweety.agents.argumentation.oppmodels.GroundedGameSystem;
import net.sf.tweety.agents.sim.MultiAgentSystemGenerator;
import net.sf.tweety.agents.sim.SimulationParameters;
import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;

/**
 * Generates grounded games.
 * @author Matthias Thimm
 */
public class GroundedGameGenerator implements MultiAgentSystemGenerator<ArguingAgent,GroundedGameSystem> {

	/** Key for the simulation parameter which refers to the universal theory generated. */
	public static final int PARAM_UNIVERSALTHEORY = 0;
	/** Key for the simulation parameter which refers to the argument of the dialogue. */
	public static final int PARAM_ARGUMENT = 1;
	
	/** The number of arguments to be created in each game. */
	private int argumentSize;
	/** The attack probability for each two arguments in the game. */
	private double attackProb;
	/** The percentage of the arguments known to each agent. */
	private double viewPercentage;
	
	/**
	 * Creates a new game generator.
	 * @param argumentSize The number of arguments to be created in each game.
	 * @param attackProb The attack probability for each two arguments in the game.
	 * @param viewPercentage the percentage of the arguments known to each agent
	 */
	public GroundedGameGenerator(int argumentSize, double attackProb, double viewPercentage){
		this.argumentSize = argumentSize;
		this.attackProb = attackProb;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.sim.MultiAgentSystemGenerator#generate(net.sf.tweety.agents.sim.SimulationParameters)
	 */
	@Override
	public GroundedGameSystem generate(SimulationParameters params) {
		DungTheory theory = DungTheory.generateRandomTheory(this.argumentSize, this.attackProb);
		Argument arg = theory.iterator().next();
		params.put(GroundedGameGenerator.PARAM_UNIVERSALTHEORY, theory);
		params.put(GroundedGameGenerator.PARAM_ARGUMENT, arg);
		// generate each agent view
		Extension proView = new Extension();
		Extension conView = new Extension();		
		//both views must contain the argument of the discussion
		proView.add(arg);
		conView.add(arg);
		Random rand = new Random();
		for(Argument a: theory){
			if(rand.nextDouble()<= this.viewPercentage)
				proView.add(a);
			if(rand.nextDouble()<= this.viewPercentage)
				conView.add(a);
		}
		params.put(GroundedGameSystem.AgentFaction.PRO, proView);
		params.put(GroundedGameSystem.AgentFaction.CONTRA, conView);		
		return new GroundedGameSystem(theory);
	}

}
