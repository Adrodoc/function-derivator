package de.adrodoc55.math.term;

import java.util.List;

import de.adrodoc55.math.term.function.Constant;
import de.adrodoc55.math.term.function.LogarithmusNaturalis;
import de.adrodoc55.math.term.function.NaturalExponential;

public class Potency extends Calculation {

	private Term base;
	private Term exponent;

	public Potency(Potency potency) {
		this(potency.base.clone(), potency.exponent.clone());
	}

	public Potency(Term base, Term exponent) {
		this.base = base;
		this.exponent = exponent;
	}

	public Term getBase() {
		return base;
	}

	public Term getExponent() {
		return exponent;
	}

	// void setExponent(Term exponent) {
	// this.exponent = exponent;
	// }

	@Override
	public Operator getOperator() {
		return Operator.POW;
	}

	@Override
	public Potency clone() {
		return new Potency(this);
	}

	@Override
	public Term toSimplestForm() {
		// method:
		while (true) {
			// super.toSimplestForm();
			base = base.toSimplestForm();
			exponent = exponent.toSimplestForm();
			if (base.equals(Constant.E)
					&& !(this instanceof NaturalExponential)) {
				return new NaturalExponential(exponent).toSimplestForm();
			}
			if (exponent.equals(new RationalNumber(0))) {
				return new RationalNumber(1);
			}
			if (exponent.equals(new RationalNumber(1))) {
				return base;
			}
			if (base instanceof RationalNumber
					&& exponent instanceof RationalNumber) {
				// List<RationalNumber> subNumbers = new
				// ArrayList<RationalNumber>(
				// 2);
				return ((RationalNumber) base).pow((RationalNumber) exponent);
				// return RealNumber.calculate(getOperator(), subNumbers);
			}
			if (isNegative(exponent)) {
				exponent = new Product(exponent, new RationalNumber(-1));
				return new Fraction(new RationalNumber(1), this)
						.toSimplestForm();
			}
			return this;
		}
	}

	private boolean isNegative(Term term) {
		if (term instanceof RationalNumber) {
			RationalNumber r = (RationalNumber) term;
			return r.isNegative();
		} else if (term instanceof Product) {
			Product product = (Product) term;
			Term simplestForm = product.clone().toSimplestForm();
			if (simplestForm instanceof Product) {
				Product simpleProduct = (Product) simplestForm;
				List<RationalNumber> rs = simpleProduct
						.getSubTermsWithType(RationalNumber.class);
				if (rs.size() == 1) {
					return rs.get(0).isNegative();
				} else {
					throw new NotSimpleException(simpleProduct);
				}
			} else {
				return isNegative(simplestForm);
			}
		} else {
			return false;
		}
	}

	@Override
	public Term getDerivation(Variable v) {
		// if (base.equals(Constant.E)) {
		// return new NaturalExponential(exponent).getDerivation(v);
		// // new Product(this.clone(),
		// // exponent.getDerivation(v)).toSimplestForm();
		// } else {
		return new NaturalExponential(new Product(new LogarithmusNaturalis(
				this.base.clone()), this.exponent.clone())).getDerivation(v);
		// }
	}

	@Override
	public double toDouble() {
		return Math.pow(base.toDouble(), exponent.toDouble());
	}

	@Override
	public void setValue(Variable v, double value) {
		base.setValue(v, value);
		exponent.setValue(v, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(base);
		sb.append(getOperator());
		sb.append(exponent);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result
				+ ((exponent == null) ? 0 : exponent.hashCode());
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
		Potency other = (Potency) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (exponent == null) {
			if (other.exponent != null)
				return false;
		} else if (!exponent.equals(other.exponent))
			return false;
		return true;
	}

}
