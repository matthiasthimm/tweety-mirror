package net.sf.tweety.argumentation.dung;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.dung.semantics.*;
import net.sf.tweety.argumentation.dung.syntax.*;
import net.sf.tweety.graphs.*;


/**
 * This class implements an abstract argumentation theory in the sense of Dung.
 * <br>
 * <br>See
 * <br>
 * <br>Phan Minh Dung. On the Acceptability of Arguments and its Fundamental Role in Nonmonotonic Reasoning, Logic Programming and n-Person Games.
 * In Artificial Intelligence, Volume 77(2):321-358. 1995
 *
 *
 * @author Matthias Thimm, Tjitze Rienstra
 *
 */
public class DungTheory extends BeliefSet<Argument> {

	/**
	 * The set of attacks
	 */
	private Set<Attack> attacks;

	/**
	 * Default constructor; initializes empty sets of arguments and attacks
	 */
	public DungTheory(){
		super();
		attacks = new HashSet<Attack>();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.BeliefBase#getSignature()
	 */
	public Signature getSignature(){
		return new DungSignature(this);
	}

	/**
	 * returns true if <source>arguments</source> attack all other arguments in the theory
	 * @param arguments a set of arguments
	 * @return true if <source>arguments</source> attack all other arguments in the theory
	 */
	public boolean isAttackingAllOtherArguments(Extension ext){
		Extension ext2 = new Extension();
		for(Formula f: this)
			ext2.add((Argument) f);
		ext2.removeAll(ext);		
		Iterator<Argument> it = ext2.iterator();
		while(it.hasNext())			
			if(!this.isAttacked(it.next(),ext))
				return false;		
		return true;
	}

	/**
	 * returns true iff the theory is well-founded, i.e., there is no infinite sequence A1,A2,... of arguments with Ai attacking Ai+1
	 * @return true iff the theory is well-founded
	 */
	public boolean isWellFounded(){
		List<Argument> arguments = new ArrayList<Argument>();		
		for(Formula f: this)
			arguments.add((Argument) f);
		boolean[] dfn = new boolean[arguments.size()];
		boolean[] inProgress = new boolean[arguments.size()];
		for(int i = 0; i < arguments.size(); i++){
			dfn[i] = false;
			inProgress[i] = false;
		}
		for(int i = 0; i < arguments.size(); i++)
			if(!dfn[i])
				if(dfs(i,arguments,dfn,inProgress))
					return false;
		return true;
	}

	/**
	 * Depth-First-Search to find a cycle in the theory. Auxiliary method to determine if the theory is well-founded
	 * @param i current node
	 * @param arguments list of all nodes (arguments)
	 * @param dfn array which keeps track whether a node has been visited
	 * @param inProgress array which keeps track which nodes are currently being processed
	 * @return true iff the theory contains a cycle
	 */
	private boolean dfs(int i, List<Argument> arguments, boolean[] dfn, boolean[] inProgress){
		dfn[i] = true;
		inProgress[i] = true;
		Set<Argument> attackers = getAttackers(arguments.get(i));
		Iterator<Argument> it = attackers.iterator();
		while(it.hasNext()){
			Argument argument = (Argument) it.next();
			if(inProgress[arguments.indexOf(argument)])
				return true;
			else if(!dfn[arguments.indexOf(argument)])
				if(dfs(arguments.indexOf(argument),arguments,dfn,inProgress))
					return true;
		}
		inProgress[i] = false;
		return false;
	}

	/**
	 * Determines if the theory is coherent, i.e., if each preferred extension is stable
	 * @return true if the theory is coherent
	 */
	public boolean isCoherent(){
		Set<Extension> preferredExtensions = new PreferredReasoner(this).getExtensions();;
		Set<Extension> stableExtensions = new StableReasoner(this).getExtensions();
		stableExtensions.retainAll(preferredExtensions);
		return preferredExtensions.size() == stableExtensions.size();
	}

	/**
	 * Determines if the theory is relatively coherent, i.e., if the grounded extension coincides with the intersection of all preferred extensions
	 * @return true if the theory is relatively coherent
	 */
	public boolean isRelativelyCoherent(){
		Extension groundedExtension = new GroundReasoner(this).getExtensions().iterator().next();
		Set<Extension> preferredExtensions = new PreferredReasoner(this).getExtensions();
		Extension cut = new Extension(preferredExtensions.iterator().next());
		for(Extension e: preferredExtensions)
			cut.retainAll(e);
		return groundedExtension.equals(cut);
	}

	/**
	 * Computes the set {A | (A,argument) in attacks}.
	 * @param argument an argument
	 * @return the set of all arguments that attack <source>argument</source>.
	 */
	public Set<Argument> getAttackers(Argument argument){
		Set<Argument> attackers = new HashSet<Argument>();
		Iterator<Attack> it = attacks.iterator();
		while(it.hasNext()){
			Attack attack = it.next();
			if(attack.getAttacked().equals(argument))
				attackers.add((Argument)attack.getAttacker());
		}
		return attackers;
	}
	
	/**
	 * Computes the set {A | (argument,A) in attacks}.
	 * @param argument an argument
	 * @return the set of all arguments that are attacked by <source>argument</source>.
	 */
	public Set<Argument> getAttacked(Argument argument){
		Set<Argument> attacked = new HashSet<Argument>();
		Iterator<Attack> it = attacks.iterator();
		while(it.hasNext()){
			Attack attack = it.next();
			if(attack.getAttacker().equals(argument))
				attacked.add((Argument)attack.getAttacked());
		}
		return attacked;
	}

	/**
	 * returns true if some argument of <source>ext</source> attacks argument.
	 * @param argument an argument
	 * @param ext an extension, ie. a set of arguments
	 * @return true if some argument of <source>ext</source> attacks argument.
	 */
	public boolean isAttacked(Argument argument, Extension ext){
		Set<Argument> attackers = this.getAttackers(argument);
		attackers.retainAll(ext);
		return attackers.size() > 0;
	}
	
	/**
	 * returns true if some argument of <source>ext2</source> attacks some argument
	 * in <source>ext1</source>
	 * @param ext1 an extension, ie. a set of arguments
	 * @param ext2 an extension, ie. a set of arguments
	 * @return true if ext an extension, ie. a set of arguments
	 */
	public boolean isAttacked(Extension ext1, Extension ext2){
		for(Argument a: ext1)
			if(this.isAttacked(a, ext2)) return true;
		return false;
	}

	/**
	 * The characteristic function of an abstract argumentation framework: F_AF(S) = {A|A is acceptable wrt. S}.
	 * @param extension an extension (a set of arguments).
	 * @return an extension (a set of arguments).
	 */
	public Extension faf(Extension extension){
		Extension newExtension = new Extension();
		Iterator<Argument> it = this.iterator();
		while(it.hasNext()){
			Argument argument = it.next();
			if(extension.isAcceptable(argument, this))
				newExtension.add(argument);
		}
		return newExtension;
	}
	
	/**
	 * Checks whether arg1 is attacked by arg2.
	 * @param arg1 an argument.
	 * @param arg2 an argument.
	 * @return "true" if arg1 is attacked by arg2
	 */
	public boolean isAttackedBy(Argument arg1, Argument arg2){
		return this.attacks.contains(new Attack(arg2, arg1));
	}
	
	/**
	 * Checks whether "arg1" indirectly attacks "arg2", i.e. whether there
	 * is an odd-length path from "arg1" to "arg2".
	 * @param arg1 an AbstractArgument.
	 * @param arg2 an AbstractArgument.
	 * @return "true" iff "arg1" indirectly attacks "arg2".
	 */
	public boolean isIndirectAttack(Argument arg1, Argument arg2){
		return this.isIndirectAttack(arg1, arg2, new HashSet<Argument>());
	}
	
	/**
	 * Checks whether "arg1" indirectly attacks "arg2", i.e. whether there
	 * is an odd-length path from "arg1" to "arg2".
	 * @param arg1 an AbstractArgument.
	 * @param arg2 an AbstractArgument.
	 * @param visited already visited arguments.
	 * @return "true" iff "arg1" indirectly attacks "arg2".
	 */
	private boolean isIndirectAttack(Argument arg1, Argument arg2, Set<Argument> visited){
		if(this.isAttackedBy(arg2,arg1)) return true;
		visited.add(arg1);
		Set<Argument> attackedArguments = this.getAttacked(arg1);
		attackedArguments.removeAll(visited);
		for(Argument attacked : attackedArguments){
			if(this.isSupport(attacked, arg2))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks whether "arg1" supports "arg2", i.e. whether there
	 * is an even-length path from "arg1" to "arg2".
	 * @param arg1 an AbstractArgument.
	 * @param arg2 an AbstractArgument.
	 * @return "true" iff "arg1" supports "arg2".
	 */
	public boolean isSupport(Argument arg1, Argument arg2){
		return this.isSupport(arg1, arg2, new HashSet<Argument>());
	}
	
	/**
	 * Checks whether "arg1" supports "arg2", i.e. whether there
	 * is an even-length path from "arg1" to "arg2".
	 * @param arg1 an AbstractArgument.
	 * @param arg2 an AbstractArgument.
	 * @param visited already visited arguments.
	 * @return "true" iff "arg1" supports "arg2".
	 */
	private boolean isSupport(Argument arg1, Argument arg2, Set<Argument> visited){
		if(arg1.equals(arg2)) return true;
		visited.add(arg1);
		Set<Argument> attackedArguments = this.getAttacked(arg1);
		attackedArguments.removeAll(visited);
		for(Argument attacked : attackedArguments){
			if(this.isIndirectAttack(attacked, arg2))
				return true;
		}
		return false;
	}

	// Misc methods

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String output = new String();
		Iterator<Argument> it = this.iterator();
		while(it.hasNext())
			output += "argument("+it.next().toString()+").\n";
		output += "\n";
		Iterator<Attack> it2 = attacks.iterator();
		while(it2.hasNext())
			output += "attack"+it2.next().toString()+".\n";
		return output;
	}
	
	/**
	 * Adds the given attack to this dung theory.
	 * @param attack an attack
	 * @return "true" if the set of attacks has been modified.
	 */
	public boolean add(Attack attack){
		return this.attacks.add(attack);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.BeliefSet#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o){
		if(o instanceof Argument)
			return super.contains(o);
		return this.attacks.contains(o);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.BeliefSet#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c){
		if(c instanceof DungTheory){
			DungTheory other = (DungTheory) c;
			return super.containsAll(other) && this.attacks.containsAll(other.getAttacks());
		}
		return super.containsAll(c);
	}
	
	/**
	 * Adds the set of attacks to this Dung theory.
	 * @param c a collection of attacks
	 * @return "true" if this Dung theory has been modified.
	 */
	public boolean addAllAttacks(Collection<? extends Attack> c){
		return this.attacks.addAll(c);
	}

	/**
	 * Returns all attacks of this theory.
	 * @return all attacks of this theory.
	 */
	public Set<Attack> getAttacks(){
		return new HashSet<Attack>(this.attacks);
	}
	
	/**
	 * Returns a simple graph representation of this
	 * Dung theory.
	 * @return a graph representing this Dung Theory
	 */
	public Graph<Argument> getGraph(){
		Graph<Argument> graph = new DefaultGraph<Argument>();
		for(Argument a: this)
			graph.add(a);
		for(Attack a: this.attacks)
			graph.add(new DirectedEdge<Argument>(a.getAttacker(),a.getAttacked()));
		return graph;
	}
	
	/**
	 * Returns copy of this theory consisting only of the given 
	 * arguments 
	 * @param arguments a set of arguments
	 * @return a Dung theory.
	 */
	public DungTheory getRestriction(Collection<Argument> arguments) {
		DungTheory theory = new DungTheory();
		theory.addAll(arguments);
		for (Attack attack: this.attacks)
			if(arguments.contains(attack.getAttacked()) && arguments.contains(attack.getAttacker()))
				theory.add(attack);
		return theory;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attacks == null) ? 0 : attacks.hashCode());
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
		DungTheory other = (DungTheory) obj;
		if (attacks == null) {
			if (other.attacks != null)
				return false;
		} else if (!attacks.equals(other.attacks))
			return false;
		return true;
	}
}
