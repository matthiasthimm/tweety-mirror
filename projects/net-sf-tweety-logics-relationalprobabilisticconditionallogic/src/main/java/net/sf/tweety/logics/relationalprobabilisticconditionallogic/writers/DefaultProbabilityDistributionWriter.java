package net.sf.tweety.logics.relationalprobabilisticconditionallogic.writers;

import java.text.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.semantics.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.relationalprobabilisticconditionallogic.semantics.*;


/**
 * This class implements a simple writer for writing probability distributions.
 * 
 * @author Matthias Thimm
 */
public class DefaultProbabilityDistributionWriter extends Writer {

	/**
	 * Creates a new writer.
	 */
	public DefaultProbabilityDistributionWriter() {
		this(null);	
	}
	
	/**
	 * Creates a new writer for the given probability distribution.
	 * @param distribution a probability distribution.
	 */
	public DefaultProbabilityDistributionWriter(RpclProbabilityDistribution distribution) {
		super(distribution);	
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Writer#writeToString()
	 */
	@Override
	public String writeToString() {
		String result = "";
		RpclProbabilityDistribution distribution = (RpclProbabilityDistribution) this.getObject();
		NumberFormat formatter = new DecimalFormat("#.###################");
		for(Interpretation interpretation: distribution.keySet()){
			result += "{";
			boolean first = true;
			for(FOLAtom a: (HerbrandInterpretation)interpretation)
				if(first){
					result += a.toString();
					first = false;
				} else result += "," + a.toString();
			result += "}";
			result += " = " + formatter.format(distribution.get(interpretation).getValue()) + "\n";
		}		
		return result;
	}

}
