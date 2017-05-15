package de.adrodoc55.math.term.function;

import de.adrodoc55.math.term.Fraction;
import de.adrodoc55.math.term.RationalNumber;
import de.adrodoc55.math.term.Term;
import de.adrodoc55.math.term.Variable;

public class LogarithmusNaturalis extends Term implements Function {

	public static final String ID = "ln";
	private Term parameter;

	public LogarithmusNaturalis(Term parameter) {
		this.parameter = parameter;
	}

	@Override
	public LogarithmusNaturalis clone() {
		return new LogarithmusNaturalis(parameter.clone());
	}

	@Override
	public Term toSimplestForm() {
		// super.toSimplestForm();
		// method:
		while (true) {
			parameter = parameter.toSimplestForm();
			if (parameter instanceof RationalNumber) {
				RationalNumber r = (RationalNumber) parameter;
				if (r.isNegative() || r.equals(new RationalNumber(0))) {
					throw new ArithmeticException(
							"ln(x) ist nur für x > 0 definiert.");
				}
				if (r.equals(new RationalNumber(1))) {
					return new RationalNumber(0);
				}
				if (r.equals(Constant.E)) {
					return new RationalNumber(1);
				}
			}
			return this;
		}
		// // TODO
		// // http://www.frustfrei-lernen.de/mathematik/logarithmus-regeln.html
		//
		// return null;
	}

	@Override
	public Term getDerivation(Variable v) {
		return Function.super.getDerivation(v);
	}

	@Override
	public Term getSelfDerivation() {
		return new Fraction(new RationalNumber(1), parameter.clone());
	}

	@Override
	public Term getParameter() {
		return parameter;
	}

	@Override
	public double toDouble() {
		return Math.log(parameter.toDouble());
	}

	@Override
	public void setValue(Variable v, double value) {
		parameter.setValue(v, value);
	}

	@Override
	public String toString() {
		return Function.super.defaultToString();
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogarithmusNaturalis other = (LogarithmusNaturalis) obj;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

}
