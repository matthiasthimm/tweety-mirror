package net.sf.tweety.arg.aba;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.tweety.arg.aba.syntax.ABARule;
import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.commons.syntax.interfaces.Invertable;

public class ABATheory <T extends Invertable> implements BeliefBase {
	
	private Collection<ABARule<T>> rules = new ArrayList<>();

	public ABATheory() {
		// TODO Auto-generated constructor stub
	}
	

	public Collection<ABARule<T>> getRules() {
		return rules;
	}


	@Override
	public Signature getSignature() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DungTheory asDungTheory(){
		return null;
	}

}
