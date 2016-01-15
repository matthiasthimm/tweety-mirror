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
package net.sf.tweety.math.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.solver.ApacheCommonsCMAESOptimizer;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.FloatVariable;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;

public class ApacheCommonsCMAESOptimizerTest {

	@Test
	public void test1() {
		double accuracy = 0.01;
		
		FloatVariable x = new FloatVariable("x",0,1);
		Term t = x.mult(new FloatConstant(1).minus(x));
		OptimizationProblem p = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
		p.setTargetFunction(t);
		ApacheCommonsCMAESOptimizer solver = new ApacheCommonsCMAESOptimizer(10000,100000, 0.000001, true, 100, 100,accuracy/1000);		
		try {
			Map<Variable,Term> result = solver.solve(p);
			assertEquals(result.get(x).doubleValue(),0.5, accuracy);
		} catch (GeneralMathException e) {
			fail("Problem not feasible but should be");
		}
		
	}
	
	@Test
	public void test2() {
		double accuracy = 0.01;
		
		FloatVariable x = new FloatVariable("x",0,1);
		FloatVariable y = new FloatVariable("y",0,1);
		Term t = x.mult(new FloatConstant(1).minus(x)).mult(y).mult(new FloatConstant(1).minus(y));
		OptimizationProblem p = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
		p.setTargetFunction(t);
		ApacheCommonsCMAESOptimizer solver = new ApacheCommonsCMAESOptimizer(10000,100000, 0.000001, true, 100, 100,accuracy/1000);		
		try {
			Map<Variable,Term> result = solver.solve(p);
			assertEquals(result.get(x).doubleValue(),0.5, accuracy);
			assertEquals(result.get(y).doubleValue(),0.5, accuracy);
		} catch (GeneralMathException e) {
			fail("Problem not feasible but should be");
		}
		
	}
	
	@Test
	public void test3() {
		double accuracy = 0.01;
		
		FloatVariable[] x = new FloatVariable[10];
		Term t =  null;
		for(int i = 0; i < 10; i++){
			x[i] = new FloatVariable("x"+i,0,1);
			if(i==0)
				t = x[i].mult(new FloatConstant(1).minus(x[i]));
			else t = t.mult(x[i].mult(new FloatConstant(1).minus(x[i])));
		}
		OptimizationProblem p = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
		p.setTargetFunction(t);
		ApacheCommonsCMAESOptimizer solver = new ApacheCommonsCMAESOptimizer(10000,100000, 0.000001, true, 100, 100,accuracy/1000);		
		try {
			Map<Variable,Term> result = solver.solve(p);
			for(int i = 0; i < 10; i++)
			assertEquals(result.get(x[i]).doubleValue(),0.5, accuracy);
		} catch (GeneralMathException e) {
			fail("Problem not feasible but should be");
		}
		
	}

}
