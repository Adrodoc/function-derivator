package de.adrodoc55.math.term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sum extends MultiCalculation {

	public Sum(Term... terms) {
		super(terms);
	}

	public Sum(Iterable<Term> terms) {
		super(terms);
	}

	public Sum(Sum sum) {
		super(sum);
	}

	@Override
	public Operator getOperator() {
		return Operator.PLUS;
	}

	@Override
	public Sum clone() {
		return new Sum(this);
	}

	@Override
	public Term toSimplestForm() {
		method: while (true) {
			super.toSimplestForm();
			if (subTerms.size() == 1) {
				return subTerms.get(0);
			}
			if (addupPrefactors()) {
				continue method;
			}

			// return ausklammern();
			return this;
		}
	}

	/**
	 * Addiert alle Vorfaktoren von Termen mit ansonsten gleichen Faktoren
	 * zusammen. Falls sich etwas ändert wird true zurückgegeben, ansonsten
	 * false.<br>
	 * Es wird vorausgesetzt, dass alle subTerms in ihrer einfachsten Form sind.<br>
	 * 
	 * @return true falls sich etwas ändert. Sonst false.
	 */
	private boolean addupPrefactors() {
		boolean somethingChanged = false;
		Map<Term, List<Term>> mapping = getWithoutPrefactorMapping();
		Set<Term> keys = mapping.keySet();
		for (Term key : keys) {
			List<Term> terms = mapping.get(key);
			if (terms.size() > 1) {
				somethingChanged = true;
				for (Term term : terms) {
					subTerms.remove(term);
				}
				List<RationalNumber> prefactors = getPrefactors(terms);
				RationalNumber prefactor = RationalNumber.calculate(
						Operator.PLUS, prefactors);
				if (prefactor.equals(new RationalNumber(0))) {
					// Faktor fällt weg
				} else if (prefactor.equals(new RationalNumber(1))) {
					subTerms.add(key);
				} else {
					subTerms.add(new Product(prefactor, key).toSimplestForm());
				}
			}
		}
		return somethingChanged;
	}

	/**
	 * Gibt eine Map zurück, dessen Schlüssel die einzelnen Summanden ohne
	 * Vorfaktor sind und die Werte die Listen aller Vorfaktoren für den
	 * entsprechenden Schlüssel sind.<br>
	 * <br>
	 * Es wird vorausgesetzt, dass alle subTerms in ihrer einfachsten Form sind.<br>
	 * <br>
	 * <b>Beispiel:</b><br>
	 * Summe: a+2*a+3*b+4*b<br>
	 * <br>
	 * <b>Key | Values</b><br>
	 * a | a, 2*a<br>
	 * b | 3*b, 4*b<br>
	 * 
	 * @return
	 */
	private Map<Term, List<Term>> getWithoutPrefactorMapping() {
		Map<Term, List<Term>> mapping = new HashMap<Term, List<Term>>();
		for (Term subTerm : subTerms) {
			Term withoutPrefactor = getWithoutPrefactor(subTerm);
			List<Term> terms = mapping.get(withoutPrefactor);
			if (terms == null) {
				terms = new ArrayList<Term>();
				mapping.put(withoutPrefactor, terms);
			}
			terms.add(subTerm);
		}
		return mapping;
	}

	private Term getWithoutPrefactor(Term subTerm) {
		if (subTerm instanceof Product) {
			Product product = (Product) subTerm.clone();
			RationalNumber prefactor = getPrefactor(product);
			if (prefactor.equals(product.getNeutralElement())) {
				return subTerm;
			} else {
				product.subTerms.remove(prefactor);
				if (product.subTerms.size() == 0) {
					throw new NotSimpleException(subTerm);
				} else if (product.subTerms.size() == 1) {
					return product.subTerms.get(0);
				} else {
					return product;
				}
			}
		} else {
			return subTerm;
		}
	}

	private RationalNumber getPrefactor(Term term) {
		if (term instanceof Product) {
			Product product = (Product) term;
			List<RationalNumber> rationalFactors = product
					.getSubTermsWithType(RationalNumber.class);
			if (rationalFactors.size() == 0) {
				return product.getNeutralElement();
			} else if (rationalFactors.size() == 1) {
				return rationalFactors.get(0);
			} else {
				throw new NotSimpleException(term);
			}
		} else {
			return new RationalNumber(1);
		}
	}

	private List<RationalNumber> getPrefactors(Iterable<Term> terms) {
		List<RationalNumber> prefactors = new ArrayList<RationalNumber>();
		for (Term term : terms) {
			prefactors.add(getPrefactor(term));
		}
		return prefactors;
	}

	// @Deprecated
	// private Term ausklammern() {
	// // Ausklammern
	// List<Term> commonFactors = getFactors(subTerms.get(0));
	// outer: for (Term subTerm : subTerms) {
	// List<Term> subFactors = getFactors(subTerm);
	// for (int x = 0; x < commonFactors.size(); x++) {
	// Term factor = commonFactors.get(x);
	// if (!subFactors.contains(factor)) {
	// commonFactors.remove(factor);
	// if (commonFactors.isEmpty()) {
	// break outer;
	// }
	// x--;
	// } else {
	// subFactors.remove(factor);
	// }
	// }
	// }
	// if (!commonFactors.isEmpty()) {
	// for (int x = 0; x < subTerms.size(); x++) {
	// Term subTerm = subTerms.get(x);
	// removeFactors(subTerm, commonFactors);
	// }
	// Term[] factors = new Term[commonFactors.size() + 1];
	// for (int x = 0; x < commonFactors.size(); x++) {
	// factors[x] = commonFactors.get(x);
	// }
	// factors[commonFactors.size()] = new Sum(this);
	// return new Product(factors).toSimplestForm();
	// // continue method;
	// }
	// return this;
	// }

	@Override
	public Term getDerivation(Variable v) {
		ArrayList<Term> derivates = new ArrayList<Term>(subTerms.size());
		for (Term subTerm : subTerms) {
			derivates.add(subTerm.getDerivation(v));
		}
		return new Sum(derivates).toSimplestForm();
	}

	@Override
	public double toDouble() {
		double result = 0;
		for (Term subTerm : subTerms) {
			result += subTerm.toDouble();
		}
		return result;
	}

}
