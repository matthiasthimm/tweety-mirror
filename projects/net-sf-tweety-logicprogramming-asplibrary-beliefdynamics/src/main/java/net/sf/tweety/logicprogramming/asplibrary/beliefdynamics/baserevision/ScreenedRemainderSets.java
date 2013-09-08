package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.parser.ASPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;


/**
 * This class represents the set of Screened Consistent Remainder Sets as 
 * defined in [1]. A screened remainder set X of P regarding a 
 * set of sentences R \subseteq P is a Set X s.t. 
 *  (1) R \subseteq X \subseteq P,
 *  (2) X is consistent and
 *  (3) there is no proper superset X' of X in P that is also consistent.
 * 
 * [1] Krümpelmann, Patrick und Gabriele Kern-Isberner: 
 * 	Belief Base Change Operations for Answer Set Programming. 
 *  In: Cerro, Luis Fariñas, Andreas Herzig und Jérôme Mengin (Herausgeber):
 *  Proceedings of the 13th European conference on Logics in Artificial 
 *  Intelligence, Band 7519, Seiten 294–306, Toulouse, France, 2012. 
 *  Springer Berlin Heidelberg.
 *  
 * @author Sebastian Homann
 *
 */
public class ScreenedRemainderSets extends RemainderSets<Rule> {
	private static final long serialVersionUID = -9146903242327808522L;
	
	private Program program;
	private Program screen;
	private Solver solver;
	
	public ScreenedRemainderSets(Program p, Program r, Solver solver) throws SolverException {
		if(!p.containsAll(r)) {
			throw new IllegalArgumentException("r has to be a subset of p");
		}
		this.program = p.clone();
		this.screen = r.clone();
		this.solver = solver;
		if(!isConsistent(r)) {
			return;
		}
		Set<Program> candidates = calculateRemainderSetCandidates(p);
		// remove non-maximal candidates
		for(Program candidate : candidates) {
			boolean isMaximal = true;
			for(Program check : candidates) {
				if(isProperSubset(candidate, check)) {
					isMaximal = false;
					break;
				}
			}
			if(isMaximal) {
				this.add(candidate);
			}
		}
	}
	
	public Program getInputProgram() {
		return program;
	}
	
	public Program getScreen() {
		return screen;
	}
	
	/**
	 * Returns true iff program p is a proper (strict) subset of program q
	 * @param p a program
	 * @param q another program
	 * @return true iff program p is a proper (strict) subset of program q
	 */
	private boolean isProperSubset(Program p, Program q) {
		return q.containsAll(p) && (! p.containsAll(q) );
	}
	
	/**
	 * Recursively calculates consistent subsets of p. This is slightly faster
	 * than bruteforce calculating all possible combinations, as consistent subsets are
	 * pruned.
	 * @param p
	 * @return
	 * @throws SolverException
	 */
	private Set<Program> calculateRemainderSetCandidates(Program p) throws SolverException {
		Set<Program> result = new HashSet<Program>();
		if(isConsistent(p)) {
			result.add(p);
			return result;
		}
		
		Program toRemove = p.clone();
		toRemove.removeAll(screen);
		for(Rule remove : toRemove) {
			Program candidate = p.clone();
			candidate.remove(remove);
			result.addAll(calculateRemainderSetCandidates(candidate));
		}
		return result;
	}
	
	private boolean isConsistent(Program p) throws SolverException {
		return !solver.computeModels(p, 1).isEmpty();
	}
	
	public Collection<Program> asPrograms() {
		Set<Program> result = new HashSet<Program>();
		for(Collection<Rule> remainder : this) {
			result.add(new Program(remainder));
		}
		return result;
	}
	
	/**
	 * Simple test case taken from [1]
	 * @param args
	 * @throws ParseException
	 * @throws SolverException
	 */
	public static void main(String[] args) throws ParseException, SolverException {
		String input = "a :- b.\n -a. \n b. \n :- not -a, not b.";
		
		String pathToSolver = "/home/sese/devel/asp_solver/unix/dlv";
		Solver solver = new DLV(pathToSolver);
		
		Program p = ASPParser.parseProgram(input);
		ScreenedRemainderSets srs = new ScreenedRemainderSets(p, new Program(), solver);
		System.out.println("P = " + p + "\n\nScreened Remainder Sets: " + srs.size());
		int i = 1;
		for(Collection<Rule> remainder : srs) {
			System.out.println("\n" + i++ + ". Remainder Set:\n" + new Program(remainder));
		}
	}
}
