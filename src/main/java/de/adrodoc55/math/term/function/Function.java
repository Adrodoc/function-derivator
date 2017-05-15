package de.adrodoc55.math.term.function;

import de.adrodoc55.math.term.Product;
import de.adrodoc55.math.term.Term;
import de.adrodoc55.math.term.TermPart;
import de.adrodoc55.math.term.Variable;

public interface Function extends TermPart {

	public static Function newInstance(String id, Term parameter) {
		switch (id) {
		case LogarithmusNaturalis.ID:
			return new LogarithmusNaturalis(parameter);
		case NaturalExponential.ID:
			return new NaturalExponential(parameter);

		default:
			throw new IllegalArgumentException("Unknown Funktion: " + id);
		}
	}

	public default Term getDerivation(Variable v) {
		return new Product(getSelfDerivation(), getParameter().getDerivation(v)).toSimplestForm();
	}

	public abstract Term getSelfDerivation();

	public abstract Term getParameter();

	public default String defaultToString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getId());
		sb.append("(");
		String parameter = getParameter().toString();
		if (parameter.startsWith("(") && parameter.endsWith(")")) {
			parameter = parameter.substring(1, parameter.length() - 1);
		}
		sb.append(parameter);
		sb.append(")");
		return sb.toString();
	}
	
	public abstract String getId();
}
