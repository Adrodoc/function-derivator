package de.adrodoc55.math.term;

public abstract class Calculation extends Term {

	public abstract Operator getOperator();

	public final RationalNumber getNeutralElement() {
		return getOperator().getNeutralElement();
	}

}
