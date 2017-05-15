package de.adrodoc55.math.term;

public class NotSimpleException extends IllegalStateException {

	private static final long serialVersionUID = 1L;

	public NotSimpleException(Term term) {
		super(assembleMessage(term));
	}

	private static String assembleMessage(Term term) {
		return String.format(
				"The term %s must be in it's simplest for at this point.",
				term);
	}
}