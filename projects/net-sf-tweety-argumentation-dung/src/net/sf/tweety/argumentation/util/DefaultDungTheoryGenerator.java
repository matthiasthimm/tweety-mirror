package net.sf.tweety.argumentation.util;

import java.util.Random;

import net.sf.tweety.argumentation.dung.*;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.Attack;

/**
 * Implements a customizable Dung theory generator.
 * @author Matthias Thimm
 */
public class DefaultDungTheoryGenerator implements DungTheoryGenerator {

	/** The parameters for generation. */
	private DungTheoryGenerationParameters params;
	
	/**
	 * Creates a new generator with the given parameters.
	 * @param params some generation parameters.
	 */
	public DefaultDungTheoryGenerator(DungTheoryGenerationParameters params){
		this.params = params;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.util.DungTheoryGenerator#generate()
	 */
	@Override
	public DungTheory generate() {
		DungTheory theory = new DungTheory();
		for(int i = 0; i < this.params.numberOfArguments; i++)
			theory.add(new Argument("A" + i));
		Random rand = new Random();
		for(Argument a: theory)
			for(Argument b: theory){
				if(a == b && this.params.avoidSelfAttacks)
					continue;
				if(rand.nextDouble() <= this.params.attackProbability)
					theory.add(new Attack(a,b));
			}
		return theory;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.util.DungTheoryGenerator#generate(net.sf.tweety.argumentation.dung.syntax.Argument)
	 */
	@Override
	public DungTheory generate(Argument arg){
		DungTheory theory = new DungTheory();
		theory.add(arg);
		for(int i = 1; i < this.params.numberOfArguments; i++)
			theory.add(new Argument("A" + i));
		Random rand = new Random();
		for(Argument a: theory)
			for(Argument b: theory){
				if(a == b && this.params.avoidSelfAttacks)
					continue;
				if(rand.nextDouble() <= this.params.attackProbability){
					Attack att = new Attack(a,b);
					theory.add(att);
					//Check whether this makes the argument out
					if(!new GroundReasoner(theory).getExtensions().iterator().next().contains(arg))
						theory.remove(att);
				}
			}
		return theory;
	}
	
	public String toString(){
		return "Def"+this.params.toString();
	}

}
