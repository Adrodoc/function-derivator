package de.adrodoc55.math.term;

import java.util.ArrayList;
import java.util.List;

import de.adrodoc55.Utils;

public abstract class MultiCalculation extends Calculation {

	protected final List<Term> subTerms = new ArrayList<Term>();

	public MultiCalculation(Term... terms) {
		for (Term term : terms) {
			subTerms.add(term.clone());
		}
	}

	public MultiCalculation(Iterable<Term> terms) {
		for (Term term : terms) {
			subTerms.add(term.clone());
		}
	}

	public MultiCalculation(MultiCalculation cal) {
		this(cal.subTerms);
	}

	@SuppressWarnings("unchecked")
	protected final <T extends Term> List<T> getSubTermsWithType(Class<T> clazz) {
		List<T> subTermsWithType = new ArrayList<T>();
		for (Term term : subTerms) {
			if (clazz.isInstance(term)) {
				subTermsWithType.add((T) term);
			}
		}
		return subTermsWithType;
	}

	@Override
	public Term toSimplestForm() {
		method: while (true) {
			subTerms.removeAll(Utils.newArrayList((Object[]) null));
			subTerms.removeAll(Utils.newArrayList(getNeutralElement()));
			if (subTerms.isEmpty()) {
				subTerms.add(getNeutralElement());
			}
			for (Term subTerm : subTerms) {
				Term simpleSubTerm = subTerm.toSimplestForm();
				if (simpleSubTerm != subTerm) {
					int index = subTerms.indexOf(subTerm);
					subTerms.remove(subTerm);
					subTerms.add(index, simpleSubTerm);
					continue method;
				}
			}
			List<? extends MultiCalculation> subMultiCalculations = getSubTermsWithType(getClass());
			for (MultiCalculation element : subMultiCalculations) {
				int index = subTerms.indexOf(element);
				subTerms.remove(element);
				subTerms.addAll(index, element.subTerms);
				continue method;
			}
			List<RationalNumber> subNumbers = getSubTermsWithType(RationalNumber.class);
			if (subNumbers.size() > 1) {
				subTerms.removeAll(subNumbers);
				this.subTerms.add(RationalNumber.calculate(getOperator(),
						subNumbers));
				continue method;
			}
			return this;
		}
	}

	@Override
	public void setValue(Variable v, double value) {
		for (Term subTerm : subTerms) {
			subTerm.setValue(v, value);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (int x = 0; x < subTerms.size(); x++) {
			sb.append(subTerms.get(x));
			if (x < subTerms.size() - 1) {
				sb.append(getOperator());
			}
		}
		sb.append(')');
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((subTerms == null) ? 0 : subTerms.hashCode());
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
		MultiCalculation other = (MultiCalculation) obj;
		if (subTerms == null) {
			if (other.subTerms != null)
				return false;
		} else {
			if (subTerms.size() != other.subTerms.size())
				return false;
			if (!subTerms.containsAll(other.subTerms))
				return false;
			if (!other.subTerms.containsAll(subTerms))
				return false;
		}
		return true;
	}

}
