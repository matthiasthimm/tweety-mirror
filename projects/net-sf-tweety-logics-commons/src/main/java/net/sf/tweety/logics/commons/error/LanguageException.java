package net.sf.tweety.logics.commons.error;

/**
 * A language exception is thrown if something illegal is tried in a language
 * like setting the arity of a propositional predicate > zero.
 * 
 * @author Tim Janus
 */
public class LanguageException extends RuntimeException {
	/** kill warning */
	private static final long serialVersionUID = 649864945437272048L;

	public static enum LanguageExceptionReason {
		LER_ILLEGAL_PREDICATE("Tried to generate an illegal predicate."),
		LER_TERM_TYPE_NOT_SUPPORTED("Tried to instantiate an unsupported term."),
		LER_RULES_NOT_SUPPORTED("Rules are not supported by the language."),
		LER_DISJUNCTIONS_NOT_SUPPORTED("Disjunctions are not supported by the language.");
		
		private final String name;
		
		private LanguageExceptionReason(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public LanguageException(String language, LanguageExceptionReason reason, String furtherInformation) {
		super("The language '" + language + "' is used incorrectly: " +
				reason.toString() + (furtherInformation.isEmpty() ? "" : " - " + furtherInformation));
	}
}
