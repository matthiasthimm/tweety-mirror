package net.sf.tweety.logics.relationalconditionallogic.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.semantics.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.relationalconditionallogic.*;
import net.sf.tweety.logics.relationalconditionallogic.syntax.*;

/**
 * A relational ranking function (or relational ordinal conditional function, ROCF) that maps
 * Herbrand interpretations to integers. 
 * 
 * <br><br>See W. Spohn. Ordinal conditional functions: a dynamic theory of epistemic states.
 * In W.L. Harper and B. Skyrms, editors, Causation in Decision, Belief Change, and Statistics, II,
 * pages 105�134. Kluwer Academic Publishers, 1988.
 * 
 * <br><br>See also [Kern-Isberner,Thimm, "A Ranking Semantics for Relational Defaults", in preparation].
 * 
 * @author Matthias Thimm
 *
 */
public class RelationalRankingFunction extends Interpretation {

	/**
	 * Integer used to define infinity.
	 */
	public static final Integer INFINITY = Integer.MAX_VALUE;
	
	/**
	 * The ranks of the Herbrand interpretations.
	 */
	private Map<HerbrandInterpretation,Integer> ranks;
	
	/**
	 * The signature of the language this ranking function
	 * is defined on.
	 */
	private FolSignature signature;
	
	/**
	 * Creates a new ranking function mapping each
	 * given interpretation to zero.
	 * @param signature the signature of the language this ranking function
	 * is defined on.
	 */
	public RelationalRankingFunction(FolSignature signature){
		this.signature = signature;		
		this.ranks = new HashMap<HerbrandInterpretation,Integer>();
		HerbrandBase hBase = new HerbrandBase(this.signature);
		for(HerbrandInterpretation w: hBase.allHerbrandInterpretations())
			this.ranks.put(w, 0);			
	}
	
	/**
	 * Gets the rank of the given Herbrand interpretation.
	 * @param w a Herbrand interpretation.
	 * @return the rank of the given Herbrand interpretation.
	 * @throws IllegalArgumentException if the given Herbrand interpretation has no
	 *   rank in this ranking function.
	 */
	public Integer rank(HerbrandInterpretation w) throws IllegalArgumentException{
		if(!this.ranks.containsKey(w))
			throw new IllegalArgumentException("No rank defined for the Herbrand interpretation " + w);
		return this.ranks.get(w);
	}
	
	/**
	 * Sets the rank for the given Herbrand interpretation.
	 * @param w a Herbrand interpretation.
	 * @param value the rank for the Herbrand interpretation.
	 */
	public void setRank(HerbrandInterpretation w, Integer value){		
		if(value < 0)
			throw new IllegalArgumentException("Illegal rank value " + value + ". Ranks must be greater or equal zero.");
		this.ranks.put(w, value);
	}
	
	/**
	 * Gets the rank of the given sentence (ground formula). Throws an IllegalArgumentException when
	 * the language of the formula does not correspond to the language of the
	 * interpretations this ranking function is defined on or the formula is not a sentence. Otherwise the rank of a formula
	 * is defined as the minimal rank of its satisfying interpretations.
	 * @param formula a formula.
	 * @return the rank of the given formula.
	 * @throws IllegalArgumentException if the languages of the formula does not correspond to the language of the
	 * 		interpretations this ranking function is defined on or the formula is not a sentence.
	 */
	public Integer rank(FolFormula formula) throws IllegalArgumentException{
		if(!formula.isClosed())
			throw new IllegalArgumentException("Formula " + formula + " is not closed.");
		Integer rank = RelationalRankingFunction.INFINITY;
		for(Interpretation i: this.ranks.keySet())
			if(i.satisfies(formula))
				if(this.ranks.get(i).compareTo(rank)<0)
					rank = this.ranks.get(i); 
		return rank;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.Interpretation#satisfies(net.sf.tweety.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		if(!(formula instanceof RelationalConditional))
			throw new IllegalArgumentException("Formula " + formula + " is not a relational conditional expression.");
		RelationalConditional rc = (RelationalConditional) formula;
		// if conditional is ground check for classical satisfiability
		if(rc.isGround()){
			Integer rankPremiseAndConclusion = this.rank(rc.getConclusion().combineWithAnd(rc.getPremise().iterator().next()));
			Integer rankPremiseAndNotConclusion = this.rank(rc.getConclusion().complement().combineWithAnd(rc.getPremise().iterator().next()));
			return rankPremiseAndConclusion < rankPremiseAndNotConclusion;
		}
		// following [Kern-Isberner,Thimm, "A Ranking Semantics for Relational Defaults", in preparation],
		// a relational ranking function satisfies an open conditional if there is one instantiation
		// where the premise has minimal rank and the conditional is classically satisfied
		Set<RelationalFormula> allInstances = rc.allGroundInstances(this.signature.getConstants());
		for(RelationalFormula rf: allInstances){
			RelationalConditional instance = (RelationalConditional) rf;
			// check satisfaction
			if(!this.satisfies(instance)) continue;
			// check minimality of premise
			Integer instanceRank = this.rank(instance.getPremise().iterator().next());
			for(RelationalFormula rf2: allInstances){
				RelationalConditional otherInstance = (RelationalConditional) rf2;
				if(this.rank(otherInstance.getPremise().iterator().next()) < instanceRank)
					continue;
			}
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.Interpretation#satisfies(net.sf.tweety.BeliefBase)
	 */
	@Override
	public boolean satisfies(BeliefBase beliefBase)	throws IllegalArgumentException {
		if(!(beliefBase instanceof RclBeliefSet))
			throw new IllegalArgumentException("Knowledge base is not a relational conditional knowledge base.");
		for(Formula f: ((RclBeliefSet)beliefBase))
			if(!this.satisfies(f))
				return false;
		return true;
	}

	/**
	 * Returns the minimal rank of this OCF.
	 * @return the minimal rank of this OCF.
	 */
	private Integer minimalRank(){
		Integer min = RelationalRankingFunction.INFINITY;
		for(Integer i: this.ranks.values())
			if(i < min)
				min = i;
		return min;
	}
	
	/**
	 * Normalizes this OCF, i.e. appropriately shifts the ranks
	 * such that the minimal rank equals zero. 
	 */
	public void normalize(){
		Integer minimalRank = this.minimalRank();
		for(HerbrandInterpretation w: this.ranks.keySet()){
			if(this.rank(w) != RelationalRankingFunction.INFINITY)
				this.ranks.put(w, this.rank(w)-minimalRank);
		}			
	}

	/**
	 * Returns all interpretations that are mapped to a rank
	 * unequal to INFINITY.
	 * @return all interpretations that are mapped to a rank
	 * unequal to INFINITY.
	 */
	public Set<HerbrandInterpretation> getPossibleInterpretations(){
		Set<HerbrandInterpretation> worlds = new HashSet<HerbrandInterpretation>();
		for(HerbrandInterpretation w: this.ranks.keySet())
			if(this.ranks.get(w) < RelationalRankingFunction.INFINITY)
				worlds.add(w);
		return worlds;
	}
	
	/** Returns the number of instances of "rc" that are falsified by
	 * the given interpretation.
	 * @param w a Herbrand interpretation.
	 * @param rc a relational conditional.
	 * @return the number of instances of "rc" that are falsified by
	 * the given interpretation.
	 */
	public Integer numFalsifiedInstances(HerbrandInterpretation w, RelationalConditional rc){
		Set<RelationalFormula> instances = rc.allGroundInstances(this.signature.getConstants());
		int num = 0;
		for(RelationalFormula rf: instances){
			FolFormula f = ((RelationalConditional)rf).getPremise().iterator().next().combineWithAnd(((RelationalConditional)rf).getConclusion().complement());
			if(w.satisfies(f)) num++;			
		}
		return num;
	}
	
	/** Returns the number of instances of "rc" that are verified by
	 * the given interpretation.
	 * @param w a Herbrand interpretation.
	 * @param rc a relational conditional.
	 * @return the number of instances of "rc" that are verified by
	 * the given interpretation.
	 */
	public Integer numVerifiedInstances(HerbrandInterpretation w, RelationalConditional rc){
		Set<RelationalFormula> instances = rc.allGroundInstances(this.signature.getConstants());
		int num = 0;
		for(RelationalFormula rf: instances){
			FolFormula f = ((RelationalConditional)rf).getPremise().iterator().next().combineWithAnd(((RelationalConditional)rf).getConclusion());
			if(w.satisfies(f)) num++;			
		}
		return num;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String s = "[\n";
		Iterator<HerbrandInterpretation> it = this.ranks.keySet().iterator();
		while(it.hasNext()){
			HerbrandInterpretation w = it.next();
			s += "  " + w + " => ";
			if(this.rank(w).equals(RelationalRankingFunction.INFINITY))
				s += "INFINITY";
			else s += this.rank(w);
			s += "\n";
		}
		s += "]";
		return s;
	}
}
