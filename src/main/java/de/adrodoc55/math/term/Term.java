package de.adrodoc55.math.term;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.adrodoc55.lambda.IntegerCondition;
import de.adrodoc55.math.term.function.Constant;
import de.adrodoc55.math.term.function.Function;
import de.adrodoc55.parsing.SyntaxException;

public abstract class Term implements TermPart {

	public static Term parse(String term) {
		Parser parser = new Parser();
		return parser.parse(term);
	}

	private static Term newInstance(Term a, Operator o, Term b) {
		if (o == Operator.PLUS) {
			return new Sum(a, b);
		}
		if (o == Operator.MINUS) {
			b = new Product(b, new RationalNumber(-1));
			return new Sum(a, b);
		}
		if (o == Operator.MULT) {
			return new Product(a, b);
		}
		if (o == Operator.DIV) {
			return new Fraction(a, b);
		}
		if (o == Operator.POW) {
			return new Potency(a, b);
		}
		throw new NullPointerException("The Operator o must not be null");
	}

	/**
	 * Deep clone. ändert nichts an dem Term selbst.
	 */
	public abstract Term clone();

	public abstract Term toSimplestForm();

	public abstract Term getDerivation(Variable v);

	private static class Parser {

		private int column;
		private StringReader reader;

		public Term parse(String term) {
			reader = new StringReader(term);
			List<TermPart> tokens = tokenize();
			assertCorrectOperatorOrder(tokens);
			for (int prio = Operator.HIGHEST_PRIORITY; prio <= Operator.LOWEST_PRIORITY; prio++) {
				for (int x = 1; x < tokens.size(); x += 2) {
					Operator operator = (Operator) tokens.get(x);
					if (prio == operator.getPriority()) {
						Term a = (Term) tokens.get(x - 1);
						Term b = (Term) tokens.get(x + 1);
						Term newTerm = Term.newInstance(a, operator, b);
						tokens.remove(x - 1);
						tokens.remove(x - 1);
						tokens.remove(x - 1);
						tokens.add(x - 1, newTerm);
						x -= 2;
					}
				}
			}
			if (tokens.size() != 1) {
				String message = "Nach dem Zusammenfassen ist mehr als ein Term übrig geblieben:\n";
				for (TermPart token : tokens) {
					message += token.toString();
				}
				throw new RuntimeException(message);
			}
			reader.close();
			reader = null;
			return (Term) tokens.get(0);
		}

		private void assertCorrectOperatorOrder(List<TermPart> tokens) {
			column = 1;
			// Führendes Minus
			int x = 0;
			if (tokens.get(0) == Operator.MINUS) {
				tokens.remove(Operator.MINUS);
				tokens.add(0, new RationalNumber(-1));
				tokens.add(1, Operator.MULT);
				column++;
				x += 2;
			}
			for (; x < tokens.size(); x++) {
				TermPart token = tokens.get(x);
				boolean tokenIsOperator = token instanceof Operator;
				if (x % 2 == 0) {
					if (tokenIsOperator) {
						String message = "No Operator allowed in this column";
						throw new SyntaxException(column, message);
					}
				} else {
					if (!tokenIsOperator) {
						String message = "Expected Operator, found: "
								+ token.toString();
						throw new SyntaxException(column, message);
					}
				}
				column += token.toString().length();
			}
		}

		private List<TermPart> tokenize() {
			column = 0;
			List<TermPart> tokens = new ArrayList<TermPart>();
			while (true) {
				TermPart token = nextToken();
				if (token == null)
					break;
				tokens.add(token);
			}
			return tokens;
		}

		private TermPart nextToken() {
			int firstI = nextChar();
			if (firstI == -1)
				return null;
			if (Character.isDigit(firstI)) { // Zahl
				String token = getCompleteToken((char) firstI, i -> {
					return Character.isDigit(i);
				});
				return new RationalNumber(Integer.parseInt(token));
			} else if (firstI == (int) '(') { // Klammern
				Term parsed = getParsedTokenInBrackets();
				return parsed;
			} else if (Operator.isOperator((char) firstI)) { // Operator
				String token = getCompleteToken((char) firstI, i -> {
					return false;
				});
				return Operator.get(token);
			} else { // Variable oder Function oder Constant
				String token = getCompleteToken((char) firstI, i -> {
					return !Operator.isOperator((char) i) && i != (int) '(';
				});
				int nextChar = nextChar();
				if (nextChar == (int) '(') { // Function
					Term parsed = getParsedTokenInBrackets();
					return Function.newInstance(token, parsed);
				} else { // Variable oder Constant
					resetReader();
					try {
						return Constant.valueOf(token);
					} catch (IllegalArgumentException ex) {
						return new Variable(token);
					}
				}
			}
		}

		private Term getParsedTokenInBrackets() {
			String token = getTokenInBrackets();
			try {
				return Term.parse(token);
			} catch (SyntaxException ex) {
				throw new SyntaxException(column - token.length()
						+ ex.getColumn() - 1 /*-1 wegen hinterer Klammer*/,
						ex.getRawMessage(), ex);
			}
		}

		private String getTokenInBrackets() {
			String parameter = getCompleteToken('(', new IntegerCondition() {
				private int bracketCount = 1;

				@Override
				public boolean appliesFor(int i) {
					checkForEndOfStream(i);
					boolean bc = bracketCount > 0;
					if (i == (int) '(')
						bracketCount++;
					if (i == (int) ')')
						bracketCount--;
					return bc;
				}
			});
			return parameter.substring(1, parameter.length() - 1);
		}

		private String getCompleteToken(char first, IntegerCondition condition) {
			StringBuilder sb = new StringBuilder();
			sb.append(first);
			while (true) {
				int i = nextChar();
				if (i == -1 || !condition.appliesFor(i)) {
					resetReader();
					break;
				}
				char c = (char) i;
				sb.append(c);
			}
			return sb.toString();
		}

		private int nextChar() {
			try {
				reader.mark(2);
				int c = reader.read();
				column++;
				return c;
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		private void resetReader() {
			try {
				reader.reset();
				column--;
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		private void checkForEndOfStream(int i) {
			if (i == -1)
				throw new SyntaxException(column, "Unexpected EOS");
		}
	}

	public abstract double toDouble();

	public abstract void setValue(Variable v, double value);

	@Override
	public abstract String toString();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);
}
