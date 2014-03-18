package net.sf.tweety.arg.prob.lotteries;

import java.util.Collection;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.divisions.Division;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.graphs.Graph;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.probability.ProbabilityFunction;

/**
 * Represents a probability function on the subgraphs of some Dung theory.
 * 
 * @author Matthias Thimm
 */
public class SubgraphProbabilityFunction extends ProbabilityFunction<DungTheory>{

	/** The theory of this probability function.*/
	private DungTheory theory;
	
	/**
	 * Creates a new uniform probability function for the given theory, i.e.
	 * all sub graphs of the given theory receive the same probability.
	 * @param theory some theory.
	 */
	public SubgraphProbabilityFunction(DungTheory theory){
		super();
		this.theory = theory;
		Collection<Graph<Argument>> subtheories = theory.getSubgraphs();
		for(Graph<Argument> g: theory.getSubgraphs()){
			this.put(new DungTheory(g), new Probability(1d/subtheories.size()));
		}
	}
	
	/**
	 * Returns the theory of this probability function.
	 * @return the theory of this probability function.
	 */
	public DungTheory getTheory(){
		return this.theory;
	}
	
	/**
	 * Returns the epistemic probability of the given argument, i.e. the probability
	 * that the given argument is present in some randomly sampled sub graph.
	 * @param arg some argument
	 * @return a probability
	 */
	public Probability getEpistemicProbability(Argument arg){
		double d = 0;
		for(DungTheory theory: this.keySet()){
			if(theory.contains(arg))
				d += this.probability(theory).doubleValue();
		}
		return new Probability(d);
	}
	
	/**
	 * Returns the epistemic probability of the given attack, i.e. the probability
	 * that the given attack is present in some randomly sampled sub graph.
	 * @param arg some argument
	 * @return a probability
	 */
	public Probability getEpistemicProbability(Attack att){
		double d = 0;
		for(DungTheory theory: this.keySet()){
			if(theory.contains(att))
				d += this.probability(theory).doubleValue();
		}
		return new Probability(d);
	}
	
	/**
	 * Returns the probability of the given division being acceptable wrt.
	 * the given semantics and this probability functions, i.e. the sum
	 * of the probabilities of all sub-graphs that are dividers of the 
	 * given division.  
	 * @param d some division
	 * @param semantics some semantics.
	 * @return a probability
	 */
	public Probability getAcceptanceProbability(Division d, int semantics){
		double p = 0;
		for(Graph<Argument> divider: d.getDividers(this.theory, semantics)){
			p += this.probability(new DungTheory(divider)).doubleValue();
		}
		return new Probability(p);
	}
	
	/**
	 * Returns the probability of the given argument being acceptable wrt.
	 * the given semantics and this probability functions. This is equivalent
	 * to the probability of the division ({arg},{}).  
	 * @param arg some argument
	 * @param semantics some semantics.
	 * @return a probability
	 */
	public Probability getAcceptanceProbability(Argument arg, int semantics){
		Extension a1 = new Extension();
		a1.add(arg);
		Division d = new Division(a1,new Extension());
		return this.getAcceptanceProbability(d, semantics);
	}
	
	/**
	 * Returns the probability of the given set of arguments being acceptable wrt.
	 * the given semantics and this probability functions. This is equivalent
	 * to the probability of the division (ext,{}).  
	 * @param ext some set of arguments
	 * @param semantics some semantics.
	 * @return a probability
	 */
	public Probability getAcceptanceProbability(Extension ext, int semantics){
		return this.getAcceptanceProbability(new Division(ext,new Extension()), semantics);
	}
	
	/**
	 * Updates this probability function with the given extension, i.e.
	 * all theories that do not contain the given arguments get probability zero.
	 * Afterwards the function is normalized.
	 * @param e some extension
	 */
	public SubgraphProbabilityFunction update(Extension e){
		SubgraphProbabilityFunction func = new SubgraphProbabilityFunction(this.theory);
		for(DungTheory t: this.keySet())
			if(t.containsAll(e))
				func.put(t, this.probability(t));
			else func.put(t, new Probability(0d));
		func.normalize();
		return func;
	}
}
