package net.sf.tweety.agents.argumentation.oppmodels.sim;

/**
 * This class encapsulates configuration options for generating
 * T1 belief states.
 * @author Matthias Thimm
 */
public class T1Configuration {
	/** The maximal depth of the recursive model. */
	public int maxRecursionDepth;
	/** The probability that an argument appearing in depth n does not appear
	 * in depth n+1. */
	public double probRecursionDecay;
	/** this parameter indicates whether the nested model is correct wrt. the other agent. */
	public boolean oppModelCorrect;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxRecursionDepth;
		result = prime * result + (oppModelCorrect ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(probRecursionDecay);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		T1Configuration other = (T1Configuration) obj;
		if (maxRecursionDepth != other.maxRecursionDepth)
			return false;
		if (oppModelCorrect != other.oppModelCorrect)
			return false;
		if (Double.doubleToLongBits(probRecursionDecay) != Double
				.doubleToLongBits(other.probRecursionDecay))
			return false;
		return true;
	}	
}
