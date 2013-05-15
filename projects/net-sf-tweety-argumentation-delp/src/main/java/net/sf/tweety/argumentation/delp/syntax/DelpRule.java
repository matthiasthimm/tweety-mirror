package net.sf.tweety.argumentation.delp.syntax;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.Conjuctable;
import net.sf.tweety.logics.commons.syntax.interfaces.Disjunctable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;
import net.sf.tweety.logics.firstorderlogic.syntax.Conjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Disjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Functor;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.util.rules.Rule;

/**
 * This method is the superclass for both a strict rule and a defeasible rule in defeasible logic programming
 * and captures their common attributes and methods.
 *
 * @author Matthias Thimm
 *
 */
public abstract class DelpRule extends RelationalFormula implements Rule{
	
	/**
	 * The head of the rule (this must be a literal).
	 */
	protected FolFormula head;

	/**
	 * The body of the rule (these must be a literals).
	 */
	protected Set<FolFormula> body;

	/**
	 * Default constructor; initializes head and body of the rule
	 * @param head a literal
	 * @param body a set of literals
	 */
	public DelpRule(FolFormula head, Collection<? extends FolFormula> body){
		if(!head.isLiteral())
			throw new IllegalArgumentException("Heads of DeLP rules need to consist of a single literal.");
		for(FolFormula f: body)
			if(!f.isLiteral())
				throw new IllegalArgumentException("Body elements of DeLP rules need to consist of a single literal.");
		this.head = head;
		this.body = new HashSet<FolFormula>(body);
	}

	/**
	 * Checks whether this rule is applicaple in the given context <source>literals</source>,
	 * @param literals a set of literals
	 * @return <source>true</source> iff this rule is applicaple, i.e., if the body of the rule is a subset
	 * 	of the given set of literals
	 */
	public boolean isApplicable(Collection<? extends FolFormula> literals){
		for(FolFormula f: literals)
			if(!f.isLiteral())
				throw new IllegalArgumentException("Parameter 'literals' is expected to consist of literals.");
		return literals.containsAll(body);
	}

	/**
	 * Checks whether there appear any variables in this rule
	 * @return <source>true</source> iff there appears no variable in this rule
	 */
	public boolean isGround(){
		if(!this.head.isGround()) return false;
		for(FolFormula f: this.body)
			if(!f.isGround())
				return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.util.rules.Rule#getPremise()
	 */
	@Override
	public Collection<? extends Formula> getPremise() {
		return this.body;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.util.rules.Rule#getConclusion()
	 */
	@Override
	public Formula getConclusion() {
		return this.head;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.ClassicalFormula#combineWithAnd(net.sf.tweety.ClassicalFormula)
	 */
	@Override
	public Conjunction combineWithAnd(Conjuctable f) {
		throw new UnsupportedOperationException("Combination using AND not permitted for rules.");
	}

	@Override
	public Disjunction combineWithOr(Disjunctable f) {
		throw new UnsupportedOperationException("Combination using OR not permitted for rules.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.ClassicalFormula#complement()
	 */
	@Override
	public RelationalFormula complement() {
		throw new UnsupportedOperationException("Complement not permitted for rules.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getPredicates()
	 */
	@Override
	public Set<Predicate> getPredicates() {
		Set<Predicate> predicates = new HashSet<Predicate>();
		predicates.addAll(this.head.getPredicates());
		for(FolFormula f: this.body)
			predicates.addAll(f.getPredicates());
		return predicates;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getAtoms()
	 */
	@Override
	public Set<FOLAtom> getAtoms() {
		Set<FOLAtom> atoms = new HashSet<FOLAtom>();
		atoms.addAll(this.head.getAtoms());
		for(FolFormula f: this.body)
			atoms.addAll(f.getAtoms());
		return atoms;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> reval = new HashSet<Term<?>>();
		reval.addAll(head.getTerms());
		for(FolFormula b : body) {
			reval.addAll(b.getTerms());
		}
		return reval;
	}

	@Override
	public <C extends Term<?>> Set<C> getTerms(Class<C> cls) {
		Set<C> reval = new HashSet<C>();
		reval.addAll(head.getTerms(cls));
		for(FolFormula b : body) {
			reval.addAll(b.getTerms(cls));
		}
		return reval;
	}

	@Override
	public Set<Variable> getQuantifierVariables() {
		Set<Variable> reval = new HashSet<Variable>();
		reval.addAll(head.getQuantifierVariables());
		for(FolFormula b : body) {
			reval.addAll(b.getQuantifierVariables());
		}
		return reval;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#containsQuantifier()
	 */
	@Override
	public boolean containsQuantifier() {
		return !getQuantifierVariables().isEmpty();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	@Override
	public abstract RelationalFormula substitute(Term<?> v, Term<?> t) throws IllegalArgumentException;

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getUnboundVariables()
	 */
	@Override
	public Set<Variable> getUnboundVariables() {
		Set<Variable> vars = new HashSet<Variable>();
		vars.addAll(this.head.getUnboundVariables());
		for(FolFormula f: this.body)
			vars.addAll(f.getUnboundVariables());
		return vars;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isClosed()
	 */
	@Override
	public boolean isClosed() {
		if(!this.head.isClosed()) 
			return false;
		for(FolFormula f: this.body)
			if(!f.isClosed())
				return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isClosed(java.util.Set)
	 */
	@Override
	public boolean isClosed(Set<Variable> boundVariables) {
		if(!this.head.isClosed(boundVariables)) 
			return false;
		for(FolFormula f: this.body)
			if(!f.isClosed(boundVariables))
				return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isWellBound()
	 */
	@Override
	public boolean isWellBound() {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isWellBound(java.util.Set)
	 */
	@Override
	public boolean isWellBound(Set<Variable> boundVariables) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctors()
	 */
	@Override
	public Set<Functor> getFunctors() {
		Set<Functor> functors = new HashSet<Functor>();
		functors.addAll(this.head.getFunctors());
		for(FolFormula f: this.body)
			functors.addAll(f.getFunctors());
		return functors;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.ClassicalFormula#getUniformProbability()
	 */
	@Override
	public Probability getUniformProbability(){
		throw new UnsupportedOperationException("IMPLEMENT ME");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((head == null) ? 0 : head.hashCode());
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
		DelpRule other = (DelpRule) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
	}
}
