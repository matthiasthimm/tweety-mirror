package net.sf.tweety.logics.conditionallogic.kappa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.tweety.logics.conditionallogic.semantics.ConditionalStructure;
import net.sf.tweety.logics.conditionallogic.semantics.ConditionalStructure.Generator;
import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.propositionallogic.semantics.NicePossibleWorld;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;

public class ConditionalStructureKappaBuilder {
	Map<Conditional, KappaValue> build(ConditionalStructure structure) {
		Map<Conditional, KappaValue> reval = new HashMap<Conditional,KappaValue>();
		
		// first instantiate:
		int i=1;
		for(Conditional cond : structure.getConditionals()) {
			reval.put(cond, new KappaValue(i++, cond));
		}
		
		// build the two minimum terms:
		for(KappaValue kv : reval.values()) {
			Conditional curCond = kv.cond;
			
			// for every verifying world we search conditionals that are falsified:
			List<NicePossibleWorld> verifying = structure.getVerifyingWorlds(curCond);
			for(NicePossibleWorld world : verifying) {
				
				// and build the following Sum:
				KappaSum sum = new KappaSum();
				Map<Conditional, Generator> map = structure.getWorldGenerators(world);
				for(Entry<Conditional, Generator> entry : map.entrySet()) {
					if(entry.getKey() == curCond) {
						continue;
					}
					
					if(entry.getValue() == Generator.CG_MINUS) {
						sum.elements.add(reval.get(entry.getKey()));
					}
				}
				
				// if the sum is not empty add it to the positive minimum of the kappa-value:
				if(!sum.elements.isEmpty()) {
					kv.positiveMinimum.elements.add(sum);
				}
			}
			
			// for every falsifying world we search search conditionals that are also falsified by that world:
			List<NicePossibleWorld> falsifying = structure.getFalsifiyingWorlds(curCond);
			for(NicePossibleWorld world : falsifying) {
				
				// and build the following sum:
				KappaSum sum = new KappaSum();
				Map<Conditional, Generator> map = structure.getWorldGenerators(world);
				for(Entry<Conditional, Generator> entry : map.entrySet()) {
					if(entry.getKey() == curCond) {
						continue;
					}
					
					if(entry.getValue() == Generator.CG_PLUS) {
						sum.elements.add(reval.get(entry.getKey()));
					}
				}
				
				// if the sum is not empty add it to the negative minimum of the kappa-value:
				if(!sum.elements.isEmpty()) {
					kv.negativeMinimum.elements.add(sum);
				}
			}
		}
		
		return reval;
	}
	
	public static void main(String [] args) {
		Set<Conditional> conds = new HashSet<Conditional>();
		Proposition b = new Proposition("b");
		Proposition f = new Proposition("f");
		Proposition w = new Proposition("w");
		Proposition p = new Proposition("p");
		
		conds.add(new Conditional(f, b));
		conds.add(new Conditional(p, b));
		conds.add(new Conditional(new Negation(f), p));
		
		ConditionalStructure cs = new ConditionalStructure(conds);
		System.out.println("Conditional Structure:");
		System.out.println(cs);
		
		ConditionalStructureKappaBuilder builder = new ConditionalStructureKappaBuilder();
		List<KappaValue> values = new ArrayList<KappaValue>(builder.build(cs).values());
		Collections.sort(values, new Comparator<KappaValue>() {
			@Override
			public int compare(KappaValue o1, KappaValue o2) {
				return o1.index - o2.index;
			}
		});
		
		System.out.println("Generated:");
		for(KappaValue kv : values) {
			System.out.println(kv.fullString());
			kv.evaluate();
		}
		
		System.out.println("Evaluated:");
		for(KappaValue kv : values) {
			System.out.println(kv.fullString());
		}
	}
}
