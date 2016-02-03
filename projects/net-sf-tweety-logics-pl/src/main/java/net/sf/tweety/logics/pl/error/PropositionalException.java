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
package net.sf.tweety.logics.pl.error;

import net.sf.tweety.logics.commons.error.LanguageException;

/**
 * An Exception for the propositional language, it is thrown if a developer
 * tries to create illegal propositional statements like a predicate with an
 * arity greater than zero. 
 * 
 * @author Tim Janus
 */
public class PropositionalException extends LanguageException {
	
	/** kill warning */
	private static final long serialVersionUID = 843894579984076905L;

	public PropositionalException(LanguageExceptionReason reason) {
		this(reason, "");
	}
	
	public PropositionalException(LanguageExceptionReason reason, String info) {
		super("Propositional-Logic", reason, info);
	}
}
