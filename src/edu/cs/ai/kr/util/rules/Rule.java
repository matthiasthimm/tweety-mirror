package edu.cs.ai.kr.util.rules;

import java.util.*;
import edu.cs.ai.kr.*;

/**
 * This interface models a general rule, i.e. a structure with some
 * premise (a set of formulas) and some conclusion (a single formula).
 * 
 * @author Matthias Thimm
 */
public interface Rule {

	/**
	 * Returns the premise of this rule.
	 * @return the premise of this rule.
	 */
	public Collection<? extends Formula> getPremise();
	
	/**
	 * Returns the conclusion of this rule.
	 * @return the conclusion of this rule.
	 */
	public Formula getConclusion();
		
}
