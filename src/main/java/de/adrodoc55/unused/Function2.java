package de.adrodoc55.unused;

import de.adrodoc55.math.term.Product;
import de.adrodoc55.math.term.Term;
import de.adrodoc55.math.term.Variable;

@Deprecated
public abstract class Function2 extends Term {

	protected Parameter parameter;
	protected Term specification;

	public Function2(Term parameter, Term specification) {
		this.parameter = new Parameter(parameter);
		this.specification = specification;
	}

//	public static Function2 newInstance(String id, Term parameter) {
//		switch (id) {
//		case LogarithmusNaturalis.ID:
//			return new LogarithmusNaturalis(parameter);
//		case NaturalExponential.ID:
//			return new NaturalExponential(parameter);
//
//		default:
//			throw new IllegalArgumentException("Unknown Funktion: " + id);
//		}
//	}

	protected static class Parameter extends Term {

		private final Term parameter;

		public Parameter(Term parameter) {
			this.parameter = parameter.toSimplestForm();
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
			return parameter.getDerivation(v);
		}

		@Override
		public double toDouble() {
			return parameter.toDouble();
		}

		@Override
		public void setValue(Variable v, double value) {
			parameter.setValue(v, value);
		}

		@Override
		public String toString() {
			return parameter.toString();
		}

		@Override
		public int hashCode() {
			return parameter.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

	}

	public Term getParameter() {
		return parameter;
	}

	@Override
	public Term toSimplestForm() {
		return specification.toSimplestForm();
	}

	@Override
	public final Term getDerivation(Variable v) {
		Term selfDerivation = getSelfDerivation();
		Term parameterDerivation = parameter.getDerivation(v);
		return new Product(selfDerivation, parameterDerivation)
				.toSimplestForm();
	}

	protected abstract Term getSelfDerivation();

	protected abstract String getId();

	@Override
	public void setValue(Variable v, double value) {
		parameter.setValue(v, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getId());
		sb.append("(");
		String parameter = this.parameter.toString();
		if (parameter.startsWith("(") && parameter.endsWith(")")) {
			parameter = parameter.substring(1, parameter.length() - 1);
		}
		sb.append(parameter);
		sb.append(")");
		return sb.toString();
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
		Function2 other = (Function2) obj;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

}
