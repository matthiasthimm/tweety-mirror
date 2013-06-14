package net.sf.tweety.logics.translate.aspfol;

import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPElement;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPHead;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPNeg;
import net.sf.tweety.logics.commons.syntax.interfaces.SimpleLogicalFormula;
import net.sf.tweety.logics.commons.translate.Translator;
import net.sf.tweety.logics.firstorderlogic.syntax.Disjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.util.Pair;

/**
 * This Translator can translate between FOL and ASP literals (without default
 * negated literals).
 * 
 * @author Tim Janus
 */
public class AspFolTranslator extends Translator
{
	public static final int TT_NEGATION = 1;
	
	/** Default-Ctor */
	public AspFolTranslator() {}
	
	public FOLAtom toFOL(DLPAtom source) {
		return (FOLAtom) this.translateAtom(source, FOLAtom.class);
	}
	
	public DLPAtom toASP(FOLAtom source) {
		return (DLPAtom) this.translateAtom(source, DLPAtom.class);
	}

	public DLPElement toASP(FolFormula source) {
		if(source instanceof FOLAtom) {
			return toASP((FOLAtom)source);
		} else if(source instanceof Negation) {
			return toASP((Negation)source);
		} 
		return null;
	}
	
	public Negation toFOL(DLPNeg source) {
		return new Negation((FOLAtom) 
				this.translateAtom(source.getAtom(), FOLAtom.class));
	}

	public DLPNeg toASP(Negation source) {
		return new DLPNeg((DLPAtom) this.translateAtom(
				source.getAtoms().iterator().next(), DLPAtom.class));
	}
	
	public FolFormula toFOL(DLPLiteral source) {
		if(source instanceof DLPAtom) {
			return toFOL((DLPAtom)source);
		} else if(source instanceof DLPNeg) {
			return toFOL((DLPNeg)source);
		}
		return null;
	}
	
	public Disjunction toFOL(DLPHead source) {
		return (Disjunction) this.translateAssociative(source, Disjunction.class);
	}
	
	public DLPHead toASP(Disjunction source) {
		return (DLPHead) this.translateAssociative(source, DLPHead.class);
	}
	
	@Override
	public SimpleLogicalFormula translateUsingMap(SimpleLogicalFormula source) {
		SimpleLogicalFormula reval = super.translateUsingMap(source);
		if(reval == null) {
			Pair<Integer, Class<?>> translateInfo = getTranslateInfo(source.getClass());
			switch(translateInfo.getFirst()) {
			case TT_NEGATION:
				return translateInfo.getSecond() == Negation.class ? 
						toFOL((DLPNeg)source) : 
						toASP((FOLAtom)source);
			}
		}
		return reval;
	}
	
	@Override
	protected Map<Class<?>, Pair<Integer, Class<?>>> createTranslateMap() {
		Map<Class<?>, Pair<Integer, Class<?>>> tmap = new HashMap<Class<?>, Pair<Integer, Class<?>>>();

		tmap.put(DLPAtom.class, new Pair<Integer, Class<?>>(TT_ATOM, FOLAtom.class));
		tmap.put(FOLAtom.class, new Pair<Integer, Class<?>>(TT_ATOM, DLPAtom.class));
		
		tmap.put(DLPHead.class, new Pair<Integer, Class<?>>(TT_ASSOC, Disjunction.class));
		tmap.put(Disjunction.class, new Pair<Integer, Class<?>>(TT_ASSOC, DLPHead.class));
		
		return tmap;
	}
}
