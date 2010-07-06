package edu.cs.ai.kr.rpcl.writers;

import java.text.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.semantics.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.rpcl.semantics.*;

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
	public DefaultProbabilityDistributionWriter(ProbabilityDistribution distribution) {
		super(distribution);	
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Writer#writeToString()
	 */
	@Override
	public String writeToString() {
		String result = "";
		ProbabilityDistribution distribution = (ProbabilityDistribution) this.getObject();
		NumberFormat formatter = new DecimalFormat("#.###################");
		for(Interpretation interpretation: distribution.keySet()){
			result += "{";
			boolean first = true;
			for(Atom a: (HerbrandInterpretation)interpretation)
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
