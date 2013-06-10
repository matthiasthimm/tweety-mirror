package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.Set;

import net.sf.tweety.logics.commons.LogicalSymbols;
import net.sf.tweety.logics.commons.syntax.Functor;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * The classical negation of first-order logic.
 * @author Matthias Thimm
 */
public class Negation extends FolFormula{
	
	private FolFormula folFormula;
	
	public Negation(RelationalFormula formula){
		if(!formula.isWellFormed())
			throw new IllegalArgumentException("FolFormula not well-formed.");		
		this.folFormula = (FolFormula)formula;		
	}
	
	public FolFormula getFormula(){
		return this.folFormula;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getPredicates()
	 */
	public Set<? extends Predicate> getPredicates(){
		return this.folFormula.getPredicates();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		return this.folFormula.getFunctors();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getAtoms()
	 */
	public Set<FOLAtom> getAtoms(){
		return this.folFormula.getAtoms();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#containsQuantifier()
	 */
	public boolean containsQuantifier(){
		return this.folFormula.containsQuantifier();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed()
	 */
	public boolean isClosed(){
		return this.folFormula.isClosed();
	}
		
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	public Negation substitute(Term<?> v, Term<?> t) throws IllegalArgumentException {
		return new Negation(this.folFormula.substitute(v, t));
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed(java.util.Set)
	 */
	public boolean isClosed(Set<Variable> boundVariables){
		return this.folFormula.isClosed(boundVariables);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getUnboundVariables()
	 */
	public Set<Variable> getUnboundVariables(){
		return this.getTerms(Variable.class);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound()
	 */
	public boolean isWellBound(){
		return this.folFormula.isWellBound();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound(java.util.Set)
	 */
	public boolean isWellBound(Set<Variable> boundVariables){
		return this.folFormula.isWellBound(boundVariables);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isLiteral()
	 */
	@Override
	public boolean isLiteral(){
		return (this.folFormula instanceof FOLAtom);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return LogicalSymbols.CLASSICAL_NEGATION() + this.folFormula;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((folFormula == null) ? 0 : folFormula.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Negation other = (Negation) obj;
		if (folFormula == null) {
			if (other.folFormula != null)
				return false;
		} else if (!folFormula.equals(other.folFormula))
			return false;
		return true;
	}

	

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isDnf()
	 */
	public boolean isDnf() {
		return (this.folFormula instanceof FOLAtom);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toNNF()
	 */
	@Override
	public FolFormula toNnf() {
    // remove double negation    
    if(folFormula instanceof Negation)
      return ((Negation)folFormula).folFormula.toNnf();

     // Distribute negation inside conjunctions or disjunctions according to deMorgan's laws:
     // -(p & q)  = -p || -q
     // -(p || q) = -p & -q
    if(folFormula instanceof Conjunction) {
      Conjunction c = (Conjunction)folFormula;
      Disjunction d = new Disjunction();
      
      for(RelationalFormula p : c) {
        d.add( new Negation( p ).toNnf() );
      }
      return d;
    }
    if(folFormula instanceof Disjunction) {
       Disjunction d = (Disjunction)folFormula;
       Conjunction c = new Conjunction();
       
       for(RelationalFormula p : d) {
         c.add( new Negation( p ).toNnf() );
       }
       return c;
    }
    
    // Distribute negation inside quantifiers:
    // NNF(! FORALL x : R(x)) = EXISTS x : NNF( ! R(x) )
    if(folFormula instanceof ForallQuantifiedFormula) {
      ForallQuantifiedFormula q = (ForallQuantifiedFormula) folFormula;
      return new ExistsQuantifiedFormula( new Negation(q.getFormula()).toNnf(), q.getQuantifierVariables() );
    }
    // NNF(! EXISTS x : R(x)) = FORALL x : NNF( ! R(x) )
    if(folFormula instanceof ExistsQuantifiedFormula) {
      ExistsQuantifiedFormula q = (ExistsQuantifiedFormula) folFormula;
      return new ForallQuantifiedFormula( new Negation(q.getFormula()).toNnf(), q.getQuantifierVariables() );
    }
    if(folFormula instanceof Tautology)
      return new Contradiction();
    if(folFormula instanceof Contradiction)
      return new Tautology();
    
    return new Negation(this.folFormula.toNnf());
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#collapseAssociativeFormulas()
	 */
	@Override
	public FolFormula collapseAssociativeFormulas() {
	  return new Negation( folFormula.collapseAssociativeFormulas() );
	}

	@Override
	public Set<Term<?>> getTerms() {
		return folFormula.getTerms();
	}

	@Override
	public <C extends Term<?>> Set<C> getTerms(Class<C> cls) {
		return folFormula.getTerms(cls);
	}

	@Override
	public Negation clone() {
		return new Negation(this);
	}
}
