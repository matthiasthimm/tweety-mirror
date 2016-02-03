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
package net.sf.tweety.arg.saf.util;

import java.util.*;

import net.sf.tweety.arg.dung.syntax.*;
import net.sf.tweety.arg.saf.*;
import net.sf.tweety.arg.saf.syntax.*;
import net.sf.tweety.commons.*;


/**
 * This class implements a belief base sampler for structured argumentation
 * frameworks.
 * 
 * @author Matthias Thimm
 *
 */
public class SimpleSafSampler extends BeliefBaseSampler<StructuredArgumentationFramework> {

	/**
	 * Creates a new SimpleSafSampler for the given signature. 
	 * @param signature a signature.
	 */
	public SimpleSafSampler(Signature signature) {
		super(signature);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.BeliefBaseSampler#randomSample(int)
	 */
	@Override
	public StructuredArgumentationFramework randomSample(int minLength, int maxLength) {
		//bbLength is interpreted as the maximum number of basic arguments
		Random random = new Random();
		int thisLength = random.nextInt(maxLength-minLength+1) + minLength;
		BasicArgumentSampler argSampler = new BasicArgumentSampler(this.getSignature());
		StructuredArgumentationFramework saf = new StructuredArgumentationFramework();
		for(int i = 0; i < thisLength; i++)
			saf.add(argSampler.randomSample());
		//determine attacks with a probability of 1/5
		// - but do not allow self-attacks (these are useless in SAFs)
		// - avoid with a probability of 3/4 attacks between arguments with same claim
		// - avoid with a probability of 4/5 attacks between arguments where one
		//   argument might support the other
		for(Formula f1 : saf)
			for(Formula f2: saf)
				if(f1 != f2)
					if(random.nextInt(5) == 0){
						BasicArgument arg1 = (BasicArgument) f1;
						BasicArgument arg2 = (BasicArgument) f2;
						if(arg1.getConclusion().equals(arg2.getConclusion())){
							if(random.nextInt(4) == 0)
								saf.add(new Attack(arg1,arg2));
						}else if(arg1.getPremise().contains(arg2.getConclusion()) || arg2.getPremise().contains(arg1.getConclusion())){
							if(random.nextInt(5) == 0)
								saf.add(new Attack(arg1,arg2));
						}else{
							saf.add(new Attack(arg1,arg2));
						}
					}
		return saf;
	}

}
