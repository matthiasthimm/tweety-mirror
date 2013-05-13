package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.tweety.logics.commons.syntax.Term;
import net.sf.tweety.logics.commons.syntax.TermAdapter;
import net.sf.tweety.util.Pair;

/**
 * A functional term in first-order logic, i.e. a functor and a list
 * of argument terms.
 * @author Matthias Thimm, Tim Janus
 */
public class FunctionalTerm extends TermAdapter<Pair<Functor, List<Term<?>>>> {

	/**
	 * The functor of this atom
	 */
	private Functor functor;
	
	/**
	 * The arguments of the term
	 */
	private List<Term<?>> arguments;
	
	/**
	 * Creates a new functional term with the given functor and the given list
	 * of arguments.
	 * @param functor the functor of this term
	 * @param arguments the list of arguments of this functional term
	 */
	public FunctionalTerm(Functor functor, List<Term<?>> arguments){
		super(functor.getTargetSort());
		this.functor = functor;
		this.arguments = new ArrayList<Term<?>>();
		for(Term<?> t: arguments)
			this.addArgument(t);
	}
	
	public FunctionalTerm(FunctionalTerm other) {
		this.functor = other.functor;
		this.arguments = new ArrayList<Term<?>>(other.arguments);
	}
	
	/**
	 * Checks whether this term is complete, i.e. whether
	 * every argument is set.
	 * @return "true" if the term is complete.
	 */
	public boolean isComplete(){
		return this.arguments.size() == this.functor.getArity();
	}
	
	/**
	 * Creates a new functional term with the given functor.
	 * @param functor
	 */
	public FunctionalTerm(Functor functor){
		this(functor,new ArrayList<Term<?>>());		
	}
	
	/**
	 * Appends the given argument to this term's
	 * arguments and returns itself.
	 * @param arg an argument to be added
	 * @return the term itself.
	 * @throws IllegalArgumentException if the given term does not correspond
	 *   to the expected sort or the argument list is complete.
	 */
	public FunctionalTerm addArgument(Term<?> term) throws IllegalArgumentException{
		if(this.arguments.size() == this.functor.getArity())
			throw new IllegalArgumentException("No more arguments expected.");
		if(!this.functor.getArgumentTypes().get(this.arguments.size()).equals(term.getSort()))
			throw new IllegalArgumentException("The sort \"" + term.getSort() + "\" of the given term does not correspond to the expected sort \"" + this.functor.getArgumentTypes().get(this.arguments.size()) + "\"." );
		this.arguments.add(term);		
		return this;
	}

	
	/**
	 * Returns the functor of this functional term
	 * @return the functor of this functional term
	 */
	public Functor getFunctor(){
		return this.functor;
	}
	
	/**
	 * @return the list of arguments of this functional term.
	 */
	public List<Term<?>> getArguments(){
		return Collections.unmodifiableList(this.arguments);
	}
		
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#toString()
	 */
	public String toString(){
		String output = this.functor.getName();
		if(this.arguments.size() == 0) return output;
		output += "(";
		output += this.arguments.get(0);
		for(int i = 1; i < arguments.size(); i++)
			output += ","+arguments.get(i);
		output += ")";
		return output;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((functor == null) ? 0 : functor.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionalTerm other = (FunctionalTerm) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (functor == null) {
			if (other.functor != null)
				return false;
		} else if (!functor.equals(other.functor))
			return false;
		return true;
	}

	@Override
	public void set(Pair<Functor, List<Term<?>>> value) {
		this.functor = value.getFirst();
		this.arguments = value.getSecond();
	}

	@Override
	public Pair<Functor, List<Term<?>>> get() {
		return new Pair<Functor, List<Term<?>>>(this.functor, this.arguments);
	}
	
	@Override
	public Object clone() {
		return new FunctionalTerm(this);
	}
}
