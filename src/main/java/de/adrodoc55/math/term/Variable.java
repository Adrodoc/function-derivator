package de.adrodoc55.math.term;

public class Variable extends Term {

	private String identifier;
	private double value;

	public Variable(String identifier) {
		this(identifier, 1);
	}

	public Variable(String identifier, double value) {
		this.identifier = identifier;
		this.value = value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public Variable clone() {
		return new Variable(identifier, value);
	}

	@Override
	public Term toSimplestForm() {
		return this;
	}

	@Override
	public Term getDerivation(Variable v) {
		if (this.equals(v)) {
			return new RationalNumber(1);
		} else {
			return new RationalNumber(0);
		}
	}

	@Override
	public double toDouble() {
		return value;
	}

	@Override
	public void setValue(Variable v, double value) {
		if (this.identifier.equals(v.identifier)) {
			setValue(value);
		}
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
		Variable other = (Variable) obj;
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
