package net.sf.tweety.logics.markovlogic;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * This MLN reasoner takes another MLN reasoner and performs several iterations
 * with this one and takes the average result as result.
 * 
 * @author Matthias Thimm
 */
public class IteratingMlnReasoner extends AbstractMlnReasoner{ 

	/** The reasoner inside this reasoner. */
	private AbstractMlnReasoner reasoner;
	
	/** The number of iterations. */
	private long numberOfIterations;
	
	/**
	 * Creates a new IteratingMlnReasoner for the given MLN reaasoner.
	 * @param reasoner some MLN reasoner.
	 * @param numberOfIterations the number of iterations for the reasoner 
	 */
	public IteratingMlnReasoner(AbstractMlnReasoner reasoner, long numberOfIterations){
		super(reasoner.getKnowledgBase(), reasoner.getSignature());
		this.reasoner = reasoner;
		this.numberOfIterations = numberOfIterations;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#reset()
	 */
	public void reset(){
		this.reasoner.reset();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#doQuery(net.sf.tweety.logics.firstorderlogic.syntax.FolFormula)
	 */
	@Override
	protected double doQuery(FolFormula query) {
		double resultSum = 0;
		for(long i = 0; i < this.numberOfIterations; i++){
			this.reasoner.reset();
			resultSum += this.reasoner.doQuery(query);
		}
		return resultSum/this.numberOfIterations;
	}

}