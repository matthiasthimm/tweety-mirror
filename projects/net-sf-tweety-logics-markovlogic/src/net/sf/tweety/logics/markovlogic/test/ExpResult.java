package net.sf.tweety.logics.markovlogic.test;

import java.io.Serializable;

import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.logics.markovlogic.analysis.AbstractCoherenceMeasure;

public class ExpResult implements Serializable{

	private static final long serialVersionUID = -3397914383589483136L;

	public AbstractCoherenceMeasure coherenceMeasure;
	public MarkovLogicNetwork mln;
	public double[][] domain2Coherence;
	
}
