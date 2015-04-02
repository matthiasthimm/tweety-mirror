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
package net.sf.tweety.agents.dialogues.lotteries;

import java.util.Collection;

import net.sf.tweety.agents.Perceivable;
import net.sf.tweety.agents.dialogues.ExecutableDungTheory;
import net.sf.tweety.arg.dung.DungTheory;

/**
 * Audience agents (do nothing) for argumentation games.
 *  
 * @author Matthias Thimm
 *
 */
public class DummyLotteryAgent extends AbstractLotteryAgent{

	public DummyLotteryAgent(String name, DungTheory theory, int semantics) {
		super(name, theory, semantics);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.dialogues.lotteries.AbstractLotteryAgent#next(java.util.Collection)
	 */
	@Override
	public ExecutableDungTheory next(Collection<? extends Perceivable> percepts) {
			return new ExecutableDungTheory();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.dialogues.lotteries.AbstractLotteryAgent#getUtility(net.sf.tweety.arg.dung.DungTheory, int)
	 */
	@Override
	public double getUtility(DungTheory theory, int semantics) {
		return 0;
	}

}
