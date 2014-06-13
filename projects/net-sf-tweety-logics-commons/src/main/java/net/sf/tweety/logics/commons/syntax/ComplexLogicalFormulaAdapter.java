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
package net.sf.tweety.logics.commons.syntax;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.interfaces.ComplexLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * Abstract base class for ComplexLogicalFormula, that are formulas which
 * implement substitute(), exchange(), getTerms(), isGround() and isWellFormed() 
 * and therefore use terms to describe themself.
 * It also implements the isLiteral method and returns false as default behavior
 * sub classes needing another behavior shall override this method
 * 
 * @author Tim Janus
 */
public abstract class ComplexLogicalFormulaAdapter 
	implements ComplexLogicalFormula {
	@Override
	public ComplexLogicalFormula substitute(
			Map<? extends Term<?>, ? extends Term<?>> map)
			throws IllegalArgumentException {
		ComplexLogicalFormula f = this;
		for(Term<?> v: map.keySet())
			f = f.substitute(v,map.get(v));
		return f;
	}

	@Override
	public ComplexLogicalFormula exchange(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		if(!v.getSort().equals(t.getSort()))
			throw new IllegalArgumentException("Terms '" + v + "' and '" + t + "' are of different sorts.");
		Constant temp = new Constant("$TEMP$", v.getSort());
		ComplexLogicalFormula rf = this.substitute(v, temp);
		rf = rf.substitute(t, v);
		rf = rf.substitute(temp, t);
		// remove temporary constant from signature
		v.getSort().remove(temp);	
		return rf;
	}
	
	@Override
	public <C extends Term<?>> Set<C> getTerms(Class<C> cls) {
		Set<C> reval = new HashSet<C>();
		for(Term<?> term : getTerms()) {
			if(term.getClass().equals(cls)) {
				@SuppressWarnings("unchecked")
				C castTerm = (C)term;
				reval.add(castTerm);
			}
		}
		return reval;
	}

	@Override
	public <C extends Term<?>> boolean containsTermsOfType(Class<C> cls) {
		return !getTerms(cls).isEmpty();
	}


	@Override
	public boolean isGround() {
		return getTerms(Variable.class).isEmpty();
	}
	
	@Override
	public boolean isWellFormed() {
		return true;
	}
	
	@Override
	public boolean isLiteral() {
		return false;
	}
	
	@Override
	public abstract ComplexLogicalFormula clone();
}
