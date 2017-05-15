package de.adrodoc55.math.term.function;

import de.adrodoc55.math.term.RationalNumber;
import de.adrodoc55.math.term.Term;
import de.adrodoc55.math.term.Variable;

public class Constant extends Term {

	public static final Constant E = new Constant("e", Math.E);
	public static final Constant PI = new Constant("pi", Math.PI);

	private final String identifier;
	private final double value;

	private Constant(String identifier, double value) {
		this.identifier = identifier;
		this.value = value;
	}

	public static Constant[] values() {
		Constant[] values = { E, PI };
		return values;
	}

	/**
	 * Returns the constant with the specified name. The name must match exactly
	 * an identifier of an constant in this type. (Extraneous whitespace
	 * characters are not permitted.)
	 *
	 * @param name
	 *            the name of the constant to return
	 * @return the constant with the specified name
	 * @throws IllegalArgumentException
	 *             if there is no constant with the specified name
	 */
	public static Constant valueOf(String name) {
		for (Constant c : values()) {
			if (c.identifier.equals(name)) {
				return c;
			}
		}
		throw new IllegalArgumentException("No enum constant "
				+ Constant.class.getCanonicalName() + "." + name);
	}

	@Override
	public Term clone() {
		return this;
	}

	@Override
	public Term toSimplestForm() {
		return this;
	}

	@Override
	public Term getDerivation(Variable v) {
		return new RationalNumber(0);
	}

	@Override
	public double toDouble() {
		return value;
	}

	@Override
	public void setValue(Variable v, double value) {
		// Eine Konstante enthält keine Variable
	}

	@Override
	public String toString() {
		return identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Constant other = (Constant) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

}
