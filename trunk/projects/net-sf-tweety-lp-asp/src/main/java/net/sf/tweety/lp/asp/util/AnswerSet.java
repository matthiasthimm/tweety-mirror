/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.tweety.lp.asp.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.commons.BeliefSet;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.sf.tweety.lp.asp.syntax.DLPSignature;

/**
 * A answer set is a belief set which only contains literals
 * and represents the deductive belief set of an extended logic
 * program under the answer set semantic.
 * 
 * @author Tim Janus
 */
public class AnswerSet extends BeliefSet<DLPLiteral> {
	public final int level;
	public final int weight;	
	
	public AnswerSet() {
		level = 0;
		weight = 1;
	}
	
	public AnswerSet(Collection<DLPLiteral> lits, int level, int weight) {
		super(lits);
		this.level = level;
		this.weight = weight;
	}
	
	public AnswerSet(AnswerSet other) {
		super(other);
		this.level = other.level;
		this.weight = other.weight;
	}
	
	public Set<DLPLiteral> getLiteralsWithName(String name) {
		Set<DLPLiteral> reval = new HashSet<DLPLiteral>();
		for(DLPLiteral lit : this) {
			if(lit.getName().equals(name)) {
				reval.add(lit);
			}
		}
		return reval;
	}
	
	public Set<String> getFunctors() {
		Set<String> reval = new HashSet<String>();
		return reval;
	}
	
	@Override
	public String toString() {
		return super.toString() + " ["+level+","+weight+"]";
	}
	
	@Override
	public Object clone() {
		return new AnswerSet(this);
	}

	@Override
	public Signature getSignature() {
		DLPSignature reval = new DLPSignature();
		for(DLPLiteral lit : this) {
			reval.addSignature(lit.getSignature());
		}
		return reval;
	}
}
