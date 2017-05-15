package de.adrodoc55.unused;

import java.util.List;

import de.adrodoc55.math.IntegerMath;
import de.adrodoc55.math.term.Operator;
import de.adrodoc55.math.term.RationalNumber;
import de.adrodoc55.math.term.Term;

@Deprecated
public class RealNumber {

	public static void main(String[] args) {
		RealNumber n = new RealNumber(2, 1, 4, 1);
		String string = n.toString();
		System.out.println(string);
	}
	
	protected boolean sign; // false = +, true = -
	protected int numerator;
	protected int denominator;
	private boolean exSign; // false = +, true = -
	private int exNumerator;
	private int exDenominator;

//	public RealNumber(int numerator, int denominator) {
//		this(false, numerator, denominator);
//	}

//	public RealNumber(RationalNumber base) {
//		this(base.sign, base.numerator, base.denominator);
//	}
//
//	public RealNumber(boolean sign, int numerator, int denominator) {
//		this(sign, numerator, denominator, false, 1, 1);
//	}
//
//	public RealNumber(boolean sign, int numerator, int denominator) {
//		this(sign, numerator, denominator, false, 1, 1);
//	}

	public RealNumber(int numerator, int denominator, int exNumerator,
			int exDenominator) {
		this(false, numerator, denominator, false, exNumerator, exDenominator);
	}

//	public RealNumber(RationalNumber base, RationalNumber exponent) {
//		this(base.sign, base.numerator, base.denominator, exponent.sign,
//				exponent.numerator, exponent.denominator);
//	}

	public RealNumber(boolean sign, int numerator, int denominator,
			boolean exSign, int exNumerator, int exDenominator) {
		this.sign = sign;
		this.numerator = numerator;
		this.denominator = denominator;
		this.exSign = exSign;
		this.exNumerator = exNumerator;
		this.exDenominator = exDenominator;
	}

	public static RationalNumber calculate(Operator operator,
			List<? extends RationalNumber> numbers) {
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

	public RealNumber toSimplestForm() {
		baseToSimplestForm();
		exponentToSimplestForm();

		if (exSign) {
			int temp = numerator;
			numerator = denominator;
			denominator = temp;
			exSign = false;
		}

		numerator = IntegerMath.pow(numerator, exNumerator);
		denominator = IntegerMath.pow(denominator, exNumerator);
		exNumerator = 1;

		int rootNumerator = IntegerMath.sqrt(numerator, exDenominator);
		int rootDenominator = IntegerMath.sqrt(denominator, exDenominator);
		if (rootNumerator != -1 && rootDenominator != -1) {
			numerator = rootNumerator;
			denominator = rootDenominator;
			exDenominator = 1;
		}

		return this;
	}

	protected void baseToSimplestForm() {
		int gcd = IntegerMath.gcd(numerator, denominator);
		numerator /= gcd;
		if (numerator < 0) {
			sign = !sign;
			numerator *= -1;
		}
		denominator /= gcd;
		if (denominator < 0) {
			sign = !sign;
			denominator *= -1;
		}
	}

	private void exponentToSimplestForm() {
		int gcd = IntegerMath.gcd(exNumerator, exDenominator);
		exNumerator /= gcd;
		if (exNumerator < 0) {
			exSign = !exSign;
			exNumerator *= -1;
		}
		exDenominator /= gcd;
		if (exDenominator < 0) {
			exSign = !exSign;
			exDenominator *= -1;
		}
	}

	public void add(RealNumber r) {
		// TODO Auto-generated method stub
	}

	public RealNumber plus(RealNumber r) {
		// TODO Auto-generated method stub
		return null;
	}

	public RealNumber mult(RealNumber r) {
		// TODO Auto-generated method stub
		return null;
	}

	public Term pow(RealNumber r) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNatural() {
		return isWhole() && sign == false;
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
		return isReal() && exDenominator == 1;
	}

	public boolean isReal() {
		toSimplestForm();
		return true;
	}

	@Override
	public String toString() {
		toSimplestForm();
		String unsigned;
		if (isWhole()) {
			unsigned = String.valueOf(numerator);
		} else if (isShortRational()) {
			unsigned = String.valueOf(IntegerMath.div(numerator, denominator));
		} else if (isRational()) {
			unsigned = numerator + "/" + denominator;
		} else {
			String sign;
			if (this.sign) {
				sign = "-";
			} else {
				sign = "";
			}
			return "(" + sign + numerator + "/" + denominator + ")" + "^"
					+ exDenominator + "^-1";
		}
		if (sign) {
			return "(-" + unsigned + ")";
		} else {
			return unsigned;
		}
	}

	public double toDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int toInt() {
		if (isWhole()) {
			if (sign) {
				return numerator * -1;
			} else {
				return numerator;
			}
		} else {
			String message = String.format("Cannot convert %s to an integer.",
					toString());
			throw new IllegalStateException(message);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + denominator;
		result = prime * result + exDenominator;
		result = prime * result + exNumerator;
		result = prime * result + numerator;
		result = prime * result + (sign ? 1231 : 1237);
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
		RealNumber other = (RealNumber) obj;
		toSimplestForm();
		other.toSimplestForm();
		if (denominator != other.denominator)
			return false;
		if (exDenominator != other.exDenominator)
			return false;
		if (exNumerator != other.exNumerator)
			return false;
		if (numerator != other.numerator)
			return false;
		if (sign != other.sign)
			return false;
		return true;
	}

}
