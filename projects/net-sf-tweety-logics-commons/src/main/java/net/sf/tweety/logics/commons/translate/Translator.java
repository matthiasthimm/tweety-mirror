package net.sf.tweety.logics.commons.translate;

import java.util.Map;

import net.sf.tweety.logics.commons.error.LanguageException;
import net.sf.tweety.logics.commons.error.LanguageException.LanguageExceptionReason;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Sort;
import net.sf.tweety.logics.commons.syntax.interfaces.AssociativeFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Atom;
import net.sf.tweety.logics.commons.syntax.interfaces.SimpleLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides methods for translation between different logic languages
 * 
 * @author Tim Janus
 */
public class Translator {
	
	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(Translator.class);

	Map<Class<?>, Class<?>>	translateMap;
	
	public Translator(Map<Class<?>, Class<?>> map) {
		this.translateMap = map;
	}
	
	/**
	 * Translates the given source predicate into an instance of the given predicate
	 * class and returns the translation.
	 * @param source		The predicate acting as source for the operation
	 * @param predicateCls	The description of the destination Predicate class
	 * @return				An instance of predicateCls which is syntactically equal to source.
	 * @throws LanguageException
	 */
	public <C extends Predicate> C translatePredicate(Predicate source, 
			Class<C> predicateCls) throws LanguageException {
		if(source == null)
			throw new IllegalArgumentException("Argument 'source' must not be null.");
		C dest = createInstance(predicateCls);
		dest.setName(source.getName());
		for(Sort argType : source.getArgumentTypes()) {
			dest.addArgumentType(argType.clone());
		}
		return dest;
	}
	
	/**
	 * Translates the given source atom into an instance of atomCls and returns the
	 * translation.
	 * @param source		The atom acting as source
	 * @param atomCls		The description of the destination Atom class
	 * @return				The translated atom
	 * @throws LanguageException
	 */
	public <T extends Atom> T translateAtom(Atom source, Class<T> atomCls) 
			throws LanguageException {
		if(source == null)
			throw new IllegalArgumentException("Argument 'source' must not be null.");
		T dest = createInstance(atomCls);
		Predicate dstPredicate = translatePredicate(source.getPredicate(), dest.getPredicateCls());
		
		dest.setPredicate(dstPredicate);
		for(Term<?> arg : source.getArguments()) {
			dest.addArgument(arg.clone());
		}
		return dest;
	}
	
	
	public SimpleLogicalFormula translateFormula(SimpleLogicalFormula source) {
		if(source instanceof Atom) {
			@SuppressWarnings("unchecked")
			Class<? extends Atom> cls = (Class<? extends Atom>) translateMap.get(Atom.class);
			return translateAtom((Atom)source, cls);
		} else if(source instanceof AssociativeFormula<?>) {
			AssociativeFormula<?> srcA = (AssociativeFormula<?>) source;
			@SuppressWarnings("unchecked")
			Class<? extends AssociativeFormula<SimpleLogicalFormula>> cls = (Class<? extends AssociativeFormula<SimpleLogicalFormula>>) translateMap.get(srcA.getClass());
			return null;
			//return translateAssociative(srcA, cls);
		}
		return null;
	}
	
	/*
	public AssociativeFormula<?> translateAssociative(AssociativeFormula<?> source) {
		if(source == null)
			throw new IllegalArgumentException("Argument 'source' must not be null.");
		
		Class<? extends AssociativeFormula<SimpleLogicalFormula>> assocCls = (Class<? extends AssociativeFormula<SimpleLogicalFormula>>) translateMap.get(source.getClass());
		
		return translateAssociative(source, assocCls);
	}
	
	public <C extends AssociativeFormula<SimpleLogicalFormula>> C translateAssociative (
			AssociativeFormula<?> source, Class<C> clsAssoc) {
		if(source == null)
			throw new IllegalArgumentException("Argument 'source' must not be null.");
		
		C dest = createInstance(clsAssoc);
		for(SimpleLogicalFormula srcF : source.getFormulas()) {
			SimpleLogicalFormula t = translateFormula(srcF);
			dest.add(t);
		}
		
		return dest;
	}
	*/
	
	protected static <T> T createInstance(Class<T> cls) throws LanguageException {
		T reval = null;
		LanguageException ex = null;
		try {
			reval = cls.newInstance();
		} catch (InstantiationException e) {
			ex = new LanguageException("", LanguageExceptionReason.LER_INSTANTIATION,
					e.getMessage());
			e.printStackTrace();
			LOG.debug(e.getMessage());
		} catch (IllegalAccessException e) {
			ex = new LanguageException("", LanguageExceptionReason.LER_ILLEGAL_ACCESSS,
					e.getMessage());
			e.printStackTrace();
			LOG.debug(e.getMessage());
		}
		if(ex != null) {
			throw ex;
		}
		return reval;
	}
}
