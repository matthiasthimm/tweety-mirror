/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.tweety.logics.pcl.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Set;

import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.parser.PclParser;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

public class MatlabUtils {

	
	/**
	 * Print constraint matrix for belief set and possible worlds to output stream.
	 * @param out
	 * @param beliefSet
	 * @param worlds
	 */
	public static void printConstraintMatrix(PrintStream out, PclBeliefSet beliefSet, Set<PossibleWorld> worlds) {

		out.println("A = [");
		
		for(ProbabilisticConditional c: beliefSet) {
			
			out.print("  ");
			
			double p = c.getProbability().doubleValue();
			PropositionalFormula conclusion = c.getConclusion();
			
			if(c.isFact()) {
				
				for(PossibleWorld w: worlds) {
					if(w.satisfies(conclusion)) {
						out.print((1-p)+" ");
					}
					else {
						out.print(-p+" ");
					}
				}
				
			}
			else {
				
				PropositionalFormula premise = c.getPremise().iterator().next();
				
				for(PossibleWorld w: worlds) {
					
					if(w.satisfies(premise)) {
						
						if(w.satisfies(conclusion)) {
							out.print((1-p)+" ");
						}
						else {
							out.print(-p+" ");
						}
						
					}
					else {
						out.print("0 ");
						
					}
				}
			}

			out.print("\n");
		}
		

		out.println("];");
		
	}
	
	/**
	 * Print conditional verification matrix (1 if antecedence and consequence are satisfied, 0 otherwise)
	 * @param out
	 * @param beliefSet
	 * @param worlds
	 */
	public static void printConditionalVerificationMatrix(PrintStream out, PclBeliefSet beliefSet, Set<PossibleWorld> worlds) {

		out.println("CV = [");
		
		for(ProbabilisticConditional c: beliefSet) {
			
			out.print("  ");
			
			PropositionalFormula conclusion = c.getConclusion();
			
			if(c.isFact()) {
				
				for(PossibleWorld w: worlds) {
					if(w.satisfies(conclusion)) {
						out.print("1 ");
					}
					else {
						out.print("0 ");
					}
				}
				
			}
			else {
				
				PropositionalFormula premise = c.getPremise().iterator().next();
				
				for(PossibleWorld w: worlds) {
					
					if(w.satisfies(premise) && w.satisfies(conclusion)) {
						out.print("1 ");
					}
					else {
						out.print("0 ");
						
					}
				}
			}

			out.print("\n");
		}
		

		out.println("];");
		
	}
	
	/**
	 * Print antecedence verification matrix (1 if antecedence is satisfied, 0 otherwise)
	 * @param out
	 * @param beliefSet
	 * @param worlds
	 */
	public static void printAntecedenceVerificationMatrix(PrintStream out, PclBeliefSet beliefSet, Set<PossibleWorld> worlds) {

		out.println("CA = [");
		
		for(ProbabilisticConditional c: beliefSet) {
			
			out.print("  ");
			
			if(c.isFact()) {
				
				for(int i=0; i<worlds.size(); i++) {
					out.print("1 ");
				}
				
			}
			else {
				
				PropositionalFormula premise = c.getPremise().iterator().next();
				
				for(PossibleWorld w: worlds) {
					
					if(w.satisfies(premise)) {
						out.print("1 ");
					}
					else {
						out.print("0 ");
						
					}
				}
			}

			out.print("\n");
		}
		

		out.println("];");
		
	}
	
	/**
	 * Print optimization problem corresponding to minimal violation measure.
	 * Call printConstraintMatrix first to define constraint matrix A.
	 * @param out
	 * @param n number of worlds
	 * @param p norm
	 */
	public static void printMinimumViolationProblem(PrintStream out, int n, String p) {
		
		out.println("cvx_begin");
		out.println("  variable x("+n+")");
		out.println("  minimize(norm(A*x ,"+p+"))");
		out.println("  subject to");
		out.println("    sum(x)==1");
		out.println("    x>=0");
		out.println("cvx_end");
		out.println("min = cvx_optval;");
	}
	
	
	/**
	 * Print optimization problem corresponding to ME consolidation.
	 * Call printConstraintMatrix and printMinimumViolationProblem first 
	 * to define constraint matrix A and to compute minimal violation measure.
	 * @param out
	 * @param n number of worlds
	 * @param p norm
	 */
	public static void printMEConsolidationProblem(PrintStream out, int n, String p) {
		
		out.println("cvx_begin");
		out.println("  variable x("+n+")");
		out.println("  maximize( sum(entr(x)))");
		out.println("  subject to");
		out.println("    sum(x)==1");
		out.println("    x>=0");
		out.println("    norm(A*x,"+p+")<=min");
		out.println("cvx_end");
		out.println("x_"+p+"=x;");
	}	 
	
	
	/**
	 * Simple application example yielding the matlab script for spam example from [Potyka, Thimm, 2014].
	 * @param args
	 * @throws ParserException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParserException, IOException {
		
		LinkedList<String> pNorms = new LinkedList<String>();
		pNorms.add("1");
		pNorms.add("2");
		pNorms.add("Inf");
		
		
		PclParser parser = new PclParser();
		/**
		 * sp Spam
		 * ss suspicious subject
		 * sc suspicious content
		 * fc from contact
		 * 
		 */
		PclBeliefSet kb = (PclBeliefSet) parser.parseBeliefBase( 
				  "(sp|+)[0.25]\n"
				
			    + "(ss|sp)[0.5]\n"
                + "(sc|sp)[0.7]\n"
			    + "(ss|!sp)[0.01]\n"
                + "(sc|!sp)[0.05]\n"
			    
                + "(sp|sc)[0.6]\n"
                + "(sp|ss)[0.8]\n"
                );
		
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature) kb.getSignature());
		 
		System.out.println("% "+kb+"\n\n");
		
		
		printConstraintMatrix(System.out, kb, worlds);
		System.out.println("\n");
		
		printConditionalVerificationMatrix(System.out, kb, worlds);
		System.out.println("\n");
		
		printAntecedenceVerificationMatrix(System.out, kb, worlds);
		System.out.println("\n\n\n");
		
		for(String p: pNorms) {
			printMinimumViolationProblem(System.out, worlds.size(), p);
			System.out.println("\n");
			printMEConsolidationProblem(System.out, worlds.size(), p);
			System.out.println("\n\n\n");
		}
		
		System.out.println("disp( 'Original knowledge base:' );");
		for(ProbabilisticConditional c: kb) {
			System.out.println("disp( '"+c.toString()+"' );");
		}
		System.out.println("\n\n\n");
		
		for(String p: pNorms) {
			System.out.println("disp( 'Consolidated probabilities for p="+p+"' );");
			System.out.println("(CV*x_"+p+") ./ (CA*x_"+p+")");
			System.out.println();
		}
		
		
		
	}
	
	
}
