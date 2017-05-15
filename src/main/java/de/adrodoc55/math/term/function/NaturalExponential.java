package de.adrodoc55.math.term.function;

import de.adrodoc55.math.term.Potency;
import de.adrodoc55.math.term.Term;
import de.adrodoc55.math.term.Variable;

public class NaturalExponential extends Potency implements Function {

	public static final String ID = "e";

	public NaturalExponential(Term parameter) {
		super(Constant.E, parameter);
	}

	@Override
	public NaturalExponential clone() {
		return new NaturalExponential(getExponent().clone());
	}

	@Override
	public Term toSimplestForm() {
		Term simplestForm = super.toSimplestForm();
		if (this != simplestForm) {
			return simplestForm;
		}
		if (getParameter() instanceof LogarithmusNaturalis) {
			LogarithmusNaturalis ln = (LogarithmusNaturalis) getParameter();
			return ln.getParameter();
		}
		return this;
	}

	@Override
	public Term getDerivation(Variable v) {
		return Function.super.getDerivation(v);
	}

	@Override
	public Term getSelfDerivation() {
		return this.clone();
	}

	@Override
	public Term getParameter() {
		return getExponent();
	}

	@Override
	public String toString() {
		return Function.super.defaultToString();
	}

	@Override
	public String getId() {
		return ID;
	}

}
