package de.adrodoc55.math.term;

import java.util.ArrayList;
import java.util.List;

import de.adrodoc55.Utils;

public class Fraction extends Calculation {

	private Term numerator;
	private Term denominator;

	public Fraction(Fraction fraction) {
		this(fraction.numerator.clone(), fraction.denominator.clone());
	}

	public Fraction(Term numerator, Term denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public Term getNumerator() {
		return numerator;
	}

	public Term getDenominator() {
		return denominator;
	}

	@Override
	public Operator getOperator() {
		return Operator.DIV;
	}

	@Override
	public Fraction clone() {
		return new Fraction(this);
	}

	@Override
	public Term toSimplestForm() {
		method: while (true) {
			numerator = numerator.toSimplestForm();
			denominator = denominator.toSimplestForm();
			if (numerator.equals(new RationalNumber(0))) {
				return new RationalNumber(0);
			}
			if (numerator.equals(denominator)) {
				return getNeutralElement();
			}
			if (denominator instanceof RationalNumber) {
				RationalNumber d = (RationalNumber) denominator;
				Term inverse = d.pow(new RationalNumber(-1));
				return new Product(inverse, numerator).toSimplestForm();
			}
			// if (numerator instanceof RationalNumber) {
			// if (!numerator.equals(getNeutralElement())) {
			// RationalNumber n = (RationalNumber) numerator;
			// Term d = new Fraction(getNeutralElement(), denominator)
			// .toSimplestForm();
			// return new Product(n, d);
			// }
			// }
			if (numerator instanceof Fraction) {
				Fraction fraction = (Fraction) numerator;
				numerator = fraction.numerator;
				denominator = new Product(denominator, fraction.denominator);
				continue method;
			}
			if (denominator instanceof Fraction) {
				Fraction fraction = (Fraction) denominator;
				numerator = new Product(numerator, fraction.denominator);
				denominator = fraction.numerator;
				continue method;
			}
			// (x+y)/z = (x/z)+(y/z)
			if (numerator instanceof Sum) {
				Sum sum = (Sum) numerator;
				List<Term> fractions = new ArrayList<Term>(sum.subTerms.size());
				for (Term subTerm : sum.subTerms) {
					Fraction fraction = new Fraction(subTerm,
							denominator.clone());
					fractions.add(fraction.toSimplestForm());
				}
				return new Sum(fractions).toSimplestForm();
			}
			// ((x*y)+(x*z))/x = y+z
			List<Term> numFactors = getFactors(numerator);
			List<Term> denFactors = getFactors(denominator);
			// commonFactors.retainAll(denFactors);
			List<Term> commonFactors = Utils.commonElementsOf(numFactors,
					denFactors);
			if (!commonFactors.isEmpty()) {
				numerator = removeFactors(numerator, commonFactors);
				denominator = removeFactors(denominator, commonFactors);
				continue method;
			}
			return this;
		}
	}

	@Deprecated
	private List<Term> getFactors(Term term) {
		List<Term> factors;
		if (term instanceof Product) {
			Product product = (Product) term;
			product.flatten();
			factors = new ArrayList<Term>(product.subTerms.size());
			factors.addAll(product.subTerms);
			product.toSimplestForm();
		} else if (term instanceof Potency) {
			Potency potency = (Potency) term;
			if (potency.getExponent() instanceof RationalNumber) {
				RationalNumber exponent = (RationalNumber) potency
						.getExponent();
				if (exponent.isNatural()) {
					factors = new ArrayList<Term>(exponent.toInt());
					for (int x = 0; x < exponent.toInt(); x++) {
						factors.add(potency.getBase());
					}
				} else {
					throw new UnsupportedOperationException(
							"Noch nicht implementiert");
				}
			} else {
				throw new UnsupportedOperationException(
						"Noch nicht implementiert");
			}
		} else if (term instanceof RationalNumber) {
			RationalNumber r = (RationalNumber) term;
			List<RationalNumber> rFactors = r.getFactors();
			factors = new ArrayList<Term>(rFactors.size());
			for (RationalNumber f : rFactors) {
				factors.add(f);
			}
		} else {
			factors = new ArrayList<Term>(1);
			factors.add(term);
		}
		return factors;
	}

	@Deprecated
	private Term removeFactors(Term term, List<Term> commonFactors) {
		if (commonFactors.isEmpty()) {
			return term;
		}
		if (term instanceof Product) {
			Product product = (Product) term;
			product.flatten();
			Utils.removeAllOnce(product.subTerms, commonFactors);
			product.subTerms.add(new RationalNumber(1));
			return product.toSimplestForm();
		} else if (term instanceof Potency) {
			Potency potency = (Potency) term;
			for (Term factor : commonFactors) {
				if (!potency.getBase().equals(factor)) {
					throw new IllegalArgumentException(
							"Cannot remove non base factor from potency");
				}

				// if (potency.getExponent() instanceof RationalNumber) {
				// RationalNumber exponent = (RationalNumber) potency
				// .getExponent();
				// exponent.add(new RationalNumber(-1));
				// } else {
			}
			RationalNumber minus = new RationalNumber(-1)
					.mult(new RationalNumber(commonFactors.size()));
			Sum exponent = new Sum(potency.getExponent(), minus);
			return new Potency(potency.getBase(), exponent).toSimplestForm();
		} else if (term instanceof RationalNumber) {
			RationalNumber r = (RationalNumber) term;
			boolean negative = r.isNegative();
			int numerator = r.getNumerator();
			int denominator = r.getDenominator();
			for (Term termFactor : commonFactors) {
				RationalNumber factor = (RationalNumber) termFactor;
				negative ^= factor.isNegative(); // ^ = xor
				numerator /= factor.getNumerator();
				denominator /= factor.getDenominator();
			}
			return new RationalNumber(negative, numerator, denominator)
					.toSimplestForm();
			// r.setNegative(negative);
			// r.setNumerator(numerator);
			// r.setDenominator(denominator);
		} else {
			// int index = subTerms.indexOf(term);
			// subTerms.add(index, new RationalNumber(1));
			// subTerms.remove(index + 1);
			if (commonFactors.size() != 1) {
				String message = String.format(
						"Cannot remove multiple factors from %s", term
								.getClass().getSimpleName());
				throw new IllegalArgumentException(message);
			}
			return new Product().getNeutralElement();
		}
	}

	@Override
	public Term getDerivation(Variable v) {
		Product a = new Product(numerator.getDerivation(v), denominator.clone());
		Product b = new Product(numerator.clone(),
				denominator.getDerivation(v), new RationalNumber(-1));
		Sum devNumerator = new Sum(a, b);
		Potency devDenominator = new Potency(denominator.clone(),
				new RationalNumber(2));
		return new Fraction(devNumerator, devDenominator).toSimplestForm();
	}

	@Override
	public double toDouble() {
		return numerator.toDouble() / denominator.toDouble();
	}

	@Override
	public void setValue(Variable v, double value) {
		numerator.setValue(v, value);
		denominator.setValue(v, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(numerator);
		sb.append(getOperator());
		sb.append(denominator);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((denominator == null) ? 0 : denominator.hashCode());
		result = prime * result
				+ ((numerator == null) ? 0 : numerator.hashCode());
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
		Fraction other = (Fraction) obj;
		if (denominator == null) {
			if (other.denominator != null)
				return false;
		} else if (!denominator.equals(other.denominator))
			return false;
		if (numerator == null) {
			if (other.numerator != null)
				return false;
		} else if (!numerator.equals(other.numerator))
			return false;
		return true;
	}

}
