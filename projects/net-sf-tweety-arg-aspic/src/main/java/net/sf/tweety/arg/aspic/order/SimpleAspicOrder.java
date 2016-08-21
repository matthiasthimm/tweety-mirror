/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2016 The Tweety Project Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.arg.aspic.order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sf.tweety.arg.aspic.syntax.AspicArgument;
import net.sf.tweety.arg.aspic.syntax.InferenceRule;
import net.sf.tweety.logics.commons.syntax.interfaces.Invertable;

/**
 * @author Nils Geilen
 * 
 * A simple comparator for Aspic Arguments, that compares their top rules according to a given list of rules
 * 
 * @param <T>	is the type of the language that the ASPIC theory's rules range over 
 */
public class SimpleAspicOrder<T extends Invertable> implements Comparator<AspicArgument<T>> {
	
	Comparator<InferenceRule<T>> rule_comp = new RuleComparator<T>(new ArrayList<>());
	
	/**
	 * Creates a comparator for AspicArguments, that always returns 0
	 */
	public SimpleAspicOrder() {
		
	}
	
	
	/**
	 * Creates a comparator for AspicArguments from a list of AspicInferneceRules
	 * This will return a value <0, ==0 or >0 if the first argument's top rule is <,=,> the second 
	 * argument's top rule
	 * @param rules	list of rules, ordered by their value ascending
	 */
	public SimpleAspicOrder(List<String> rules) {
		rule_comp = new RuleComparator<T>(rules);
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(AspicArgument<T> a, AspicArgument<T> b) {
		return rule_comp.compare(a.getTopRule(),b.getTopRule());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleAspicOrder [" + rule_comp + "]";
	}
	
	

}
