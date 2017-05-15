package de.adrodoc55.math.term;


public enum Operator implements TermPart {

	PLUS('+', 3, new RationalNumber(0)), MINUS('-', 3, new RationalNumber(0)), MULT(
			'*', 2, new RationalNumber(1)), DIV('/', 2, new RationalNumber(1)), POW(
			'^', 1, new RationalNumber(1));

	public static final int HIGHEST_PRIORITY = 1;
	public static final int LOWEST_PRIORITY = 3;

	private final char operator;
	private final int priority;
	private final RationalNumber neutralElement;

	Operator(char o, int priority, RationalNumber neutralElement) {
		operator = o;
		this.priority = priority;
		this.neutralElement = neutralElement;
	}

	public static boolean isOperator(String token) {
		if (token.length() == 1) {
			return isOperator(token.charAt(0));
		}
		return false;
	}

	public static boolean isOperator(char c) {
		switch (c) {
		case '+':
		case '-':
		case '*':
		case '/':
		case '^':
			return true;
		default:
			return false;
		}
	}

	public static Operator get(String s) {
		if (s.length() != 1)
			throw new IllegalArgumentException(s + " is not a valid operator");
		return get(s.charAt(0));
	}

	public static Operator get(char c) {
		switch (c) {
		case '+':
			return PLUS;
		case '-':
			return MINUS;
		case '*':
			return MULT;
		case '/':
			return DIV;
		case '^':
			return POW;
		default:
			throw new IllegalArgumentException(c + " is not a valid operator");
		}
	}

	public int getPriority() {
		return priority;
	}

	public RationalNumber getNeutralElement() {
		return neutralElement;
	}

	@Override
	public String toString() {
		return String.valueOf(operator);
	}

}
