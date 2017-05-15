package de.adrodoc55.math.term;

import java.util.ArrayList;
import java.util.List;

import de.adrodoc55.math.IntegerMath;

public class RationalNumber extends Term {

	private boolean negative; // false = +, true = -
	private int numerator;
	private int denominator;

	public RationalNumber(int numerator) {
		this(numerator, 1);
	}

	public RationalNumber(int numerator, int denominator) {
		this(false, numerator, denominator);
	}

	public RationalNumber(RationalNumber r) {
		this(r.negative, r.numerator, r.denominator);
	}

	public RationalNumber(boolean negative, int numerator, int denominator) {
		if (denominator == 0) {
			throw new ArithmeticException("Cannot divide by 0.");
		}
		this.negative = negative;
		this.numerator = numerator;
		this.denominator = denominator;
		toSimplestForm();
	}

	public boolean isNegative() {
		toSimplestForm();
		return negative;
	}

	// void setNegative(boolean negative) {
	// this.negative = negative;
	// }

	public int getNumerator() {
		return numerator;
	}

	// void setNumerator(int numerator) {
	// this.numerator = numerator;
	// }

	public int getDenominator() {
		return denominator;
	}

	// void setDenominator(int denominator) {
	// if (denominator == 0) {
	// throw new ArithmeticException("Cannot divide by 0.");
	// }
	// this.denominator = denominator;
	// }

	/**
	 * Vereinfacht alle numbers. Gibt eine neue unabhängige RationalNumber
	 * zurück.
	 * 
	 * @param operator
	 *            PLUS oder MULT
	 * @param numbers
	 * @return Eine neue und unabhängige RationalNumber.
	 * @throws IllegalArgumentException
	 *             wenn numbers null oder leer ist
	 * @throws UnsupportedOperationException
	 *             für andere Operatoren
	 */
	public static RationalNumber calculate(Operator operator,
			List<? extends RationalNumber> numbers) {
		if (numbers == null) {
			throw new IllegalArgumentException(
					"The Parameter 'numbers' must not be null!");
		}
		if (numbers.isEmpty()) {
			throw new IllegalArgumentException(
					"The Parameter 'numbers' must not be empty!");
		}
		if (numbers.size() == 1) {
			return numbers.get(0);
		}
		RationalNumber neutral = operator.getNeutralElement();
		switch (operator) {
		case PLUS:
			for (RationalNumber number : numbers) {
				neutral = neutral.plus(number);
			}
			return neutral;
		case MULT:
			for (RationalNumber number : numbers) {
				neutral = neutral.mult(number);
			}
			return neutral;
			// case POW:
			// for (RealNumber number : numbers) {
			// neutral = neutral.pow(number);
			// }
			// return neutral;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public void add(RationalNumber r) {
		aufGleichenNennerBringen(this, r);
		this.numerator += r.numerator;
		this.toSimplestForm();
		r.toSimplestForm();
	}

	/**
	 * Vereinfacht this und r. Gibt eine neue unabhängige RationalNumber zurück.
	 * 
	 * @param r
	 * @return Eine neue und unabhängige RationalNumber.
	 */
	public RationalNumber plus(RationalNumber r) {
		aufGleichenNennerBringen(this, r);
		RationalNumber result = new RationalNumber(numerator + r.numerator,
				denominator);
		this.toSimplestForm();
		r.toSimplestForm();
		result.toSimplestForm();
		return result;
	}

	private static void aufGleichenNennerBringen(RationalNumber a,
			RationalNumber b) {
		a.toSimplestForm();
		b.toSimplestForm();
		a.numerator *= b.denominator;
		b.numerator *= a.denominator;
		a.denominator *= b.denominator;
		b.denominator = a.denominator;
		if (a.negative) {
			a.negative = false;
			a.numerator *= -1;
		}
		if (b.negative) {
			b.negative = false;
			b.numerator *= -1;
		}
	}

	public RationalNumber mult(RationalNumber r) {
		toSimplestForm();
		r.toSimplestForm();
		RationalNumber result = new RationalNumber(numerator * r.numerator,
				denominator * r.denominator);
		result.negative = Boolean.logicalXor(negative, r.negative);
		this.toSimplestForm();
		r.toSimplestForm();
		result.toSimplestForm();
		return result;
	}

	// public RationalNumber div(RationalNumber r) {
	// toSimplestForm();
	// r.toSimplestForm();
	// RationalNumber result = new RationalNumber(numerator * r.denominator,
	// denominator * r.numerator);
	// result.negative = Boolean.logicalXor(negative, r.negative);
	// this.toSimplestForm();
	// r.toSimplestForm();
	// result.toSimplestForm();
	// return result;
	// }

	public Term pow(RationalNumber r) {
		toSimplestForm();
		r.toSimplestForm();

		if (r.negative) {
			int temp = numerator;
			numerator = denominator;
			denominator = temp;
			r.negative = false;
		}
		if (denominator == 0) {
			throw new ArithmeticException("Cannot divide by 0.");
		}

		numerator = IntegerMath.pow(numerator, r.numerator);
		denominator = IntegerMath.pow(denominator, r.numerator);
		r.numerator = 1;

		int rootNumerator = IntegerMath.sqrt(numerator, r.denominator);
		int rootDenominator = IntegerMath.sqrt(denominator, r.denominator);
		if (rootNumerator != -1 && rootDenominator != -1) {
			numerator = rootNumerator;
			denominator = rootDenominator;
			r.denominator = 1;
		}
		if (r.equals(new RationalNumber(1))) {
			return this;
		} else {
			return new Potency(this, r);
		}
	}

	@Override
	public RationalNumber clone() {
		return new RationalNumber(this);
	}

	@Override
	public RationalNumber toSimplestForm() {
		if (numerator == 0) {
			negative = false;
			denominator = 1;
			return this;
		}
		int gcd = IntegerMath.gcd(numerator, denominator);
		numerator /= gcd;
		if (numerator < 0) {
			negative = !negative;
			numerator *= -1;
		}
		denominator /= gcd;
		if (denominator < 0) {
			negative = !negative;
			denominator *= -1;
		}
		return this;
	}

	@Override
	public Term getDerivation(Variable v) {
		return new RationalNumber(0);
	}

	public boolean isNatural() {
		return isWhole() && !isNegative();
	}

	public boolean isWhole() {
		return isRational() && denominator == 1;
	}

	private boolean isShortRational() {
		if (!isRational()) {
			return false;
		}
		double r = IntegerMath.div(numerator, denominator);
		r -= (int) r;
		return String.valueOf(r).length() < 5;
	}

	public boolean isRational() {
		toSimplestForm();
		return true;
	}

	public List<RationalNumber> getFactors() {
		List<RationalNumber> factors;
		toSimplestForm();
		int[] primes = IntegerMath.primeFactors(getNumerator());
		factors = new ArrayList<RationalNumber>(primes.length);
		for (int prim : primes) {
			factors.add(new RationalNumber(prim));
		}
		if (!isWhole()) {
			factors.add(new RationalNumber(1, getDenominator()));
		}
		if (isNegative()) {
			factors.add(new RationalNumber(-1));
		}
		return factors;
	}

	@Override
	public double toDouble() {
		double value = (double) numerator / (double) denominator;
		if (negative) {
			value *= -1;
		}
		return value;
	}

	@Override
	public void setValue(Variable v, double value) {
		// Eine rationale Zahl enthält keine Variable
	}

	public int toInt() {
		if (isWhole()) {
			if (negative) {
				return numerator * -1;
			} else {
				return numerator;
			}
		} else {
			String message = String.format("Cannot convert %s to an integer.",
					this.toString());
			throw new IllegalStateException(message);
		}
	}

	@Override
	public String toString() {
		toSimplestForm();
		String unsigned;
		if (isWhole()) {
			unsigned = String.valueOf(numerator);
		} else if (isShortRational()) {
			unsigned = String.valueOf(IntegerMath.div(numerator, denominator));
		} else {
			unsigned = numerator + "/" + denominator;
			if (!negative) {
				return "(" + unsigned + ")";
			}
		}
		if (negative) {
			return "(-" + unsigned + ")";
		} else {
			return unsigned;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + denominator;
		result = prime * result + (negative ? 1231 : 1237);
		result = prime * result + numerator;
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
		RationalNumber other = (RationalNumber) obj;
		if (denominator != other.denominator)
			return false;
		if (negative != other.negative)
			return false;
		if (numerator != other.numerator)
			return false;
		return true;
	}

}
