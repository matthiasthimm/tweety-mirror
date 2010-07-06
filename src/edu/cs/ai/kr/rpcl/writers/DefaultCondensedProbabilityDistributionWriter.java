package edu.cs.ai.kr.rpcl.writers;

import java.text.*;
import edu.cs.ai.kr.*;
import edu.cs.ai.kr.rpcl.*;

/**
 * This class implements a simple writer for writing condensed probability distributions.
 * 
 * @author Matthias Thimm
 */
public class DefaultCondensedProbabilityDistributionWriter extends Writer {
	
	/**
	 * Creates a new writer.
	 */
	public DefaultCondensedProbabilityDistributionWriter() {
		this(null);	
	}
	
	/**
	 * Creates a new writer for the given condensed probability distribution.
	 * @param distribution a condensed probability distribution.
	 */
	public DefaultCondensedProbabilityDistributionWriter(CondensedProbabilityDistribution distribution) {
		super(distribution);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Writer#writeToString()
	 */
	@Override
	public String writeToString() {
		String result = "";
		CondensedProbabilityDistribution distribution = (CondensedProbabilityDistribution) this.getObject();
		NumberFormat formatter = new DecimalFormat("#.###################"); 
		for(Interpretation interpretation: distribution.keySet()){
			result += "{";
			boolean first = true;
			for(InstanceAssignment ia: ((ReferenceWorld)interpretation).values()){
				if(first){
					result += ia.toString();
					first = false;
				}else{
					result += "," + ia.toString();
				}
			}
			result += "}";
			result += " = " + formatter.format(distribution.get(interpretation).getValue()) + "\n";
		}
		return result;
	}	

}
