package edu.cs.ai.kr.rpcl.semantics;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.rpcl.syntax.*;
import edu.cs.ai.math.equation.*;
import edu.cs.ai.math.term.*;
import edu.cs.ai.math.term.Term;
import edu.cs.ai.util.*;

/**
 * This class implements averaging semantics due to [Kern-Isberner, Thimm, KR'2010].
 * 
 * @author Matthias Thimm
 */
public class AveragingSemantics extends AbstractRpclSemantics {

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.AbstractRpclSemantics#satisfies(edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution, edu.cs.ai.kr.rpcl.syntax.RelationalProbabilisticConditional)
	 */
	@Override
	public boolean satisfies(ProbabilityDistribution p,	RelationalProbabilisticConditional r) {
		if(r.isGround())
			return this.satisfiesGroundConditional(p, r);
		Set<RelationalFormula> groundInstances = r.allGroundInstances(p.getSignature().getConstants());
		Double leftTerm = 0d;
		for(RelationalFormula f: groundInstances){			
			FolFormula body = ((RelationalProbabilisticConditional)f).getPremise().iterator().next();			
			FolFormula head_and_body;
			if(body instanceof Tautology)
				head_and_body = ((RelationalProbabilisticConditional)f).getConclusion();
			else head_and_body = body.combineWithAnd(((RelationalProbabilisticConditional)f).getConclusion());
			double probBody = p.probability(body).getValue();
			//TODO check the following
			if(probBody != 0)				
				leftTerm += p.probability(head_and_body).getValue() / probBody;
		}	
		return leftTerm < (r.getProbability().getValue()*groundInstances.size()) + Probability.PRECISION &&
			leftTerm > (r.getProbability().getValue()*groundInstances.size()) - Probability.PRECISION ;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.AbstractRpclSemantics#getSatisfactionStatement(edu.cs.ai.kr.rpcl.syntax.RelationalProbabilisticConditional, edu.cs.ai.kr.fol.syntax.FolSignature, java.util.Map)
	 */
	@Override
	public Statement getSatisfactionStatement(RelationalProbabilisticConditional r, FolSignature signature, Map<? extends Interpretation,FloatVariable> worlds2vars){
		Set<RelationalFormula> groundInstances = r.allGroundInstances(signature.getConstants());
		if(r.isFact()){
			Term term = null;
			for(RelationalFormula rf: groundInstances){
				RelationalProbabilisticConditional rfg = (RelationalProbabilisticConditional)rf;
				FolFormula head = rfg.getConclusion();
				Term tHead = this.probabilityTerm(head, worlds2vars);
				if(term == null)
					term = tHead;
				else term = term.add(tHead);					
			}
			return new Equation(term,new FloatConstant(r.getProbability().getValue() * groundInstances.size()));
		}else{			
			Term rightTerm = new FloatConstant(r.getProbability().getValue() * groundInstances.size());
			Set<Product> summands = new HashSet<Product>();
			Product denoms = new Product();
			// The term should look like (a1/b1)+(a2/b2)+(a2/b2) = K*n
			// but we write it as a1b2b3 + a2b1b3 + a3b1b2 = K*n*b1b2b3 for compuation issues
			for(RelationalFormula rf: groundInstances){
				RelationalProbabilisticConditional rfg = (RelationalProbabilisticConditional)rf;
				FolFormula body = rfg.getPremise().iterator().next();
				FolFormula head_and_body = rfg.getConclusion().combineWithAnd(body);
				Term tBody = this.probabilityTerm(body, worlds2vars);
				Term tHead_and_body = this.probabilityTerm(head_and_body, worlds2vars);
				rightTerm = rightTerm.mult(tBody);
				for(Product p: summands)
					p.addTerm(tBody);
				summands.add(tHead_and_body.mult(denoms));
				denoms.addTerm(tBody);								
			}
			return new Equation(new Sum(summands),rightTerm);
		}
	}

}
