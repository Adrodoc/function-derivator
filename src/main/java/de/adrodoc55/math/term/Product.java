package de.adrodoc55.math.term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.adrodoc55.math.term.function.LogarithmusNaturalis;

public class Product extends MultiCalculation {

	public Product(Term... terms) {
		super(terms);
	}

	public Product(Iterable<Term> terms) {
		super(terms);
	}

	public Product(Product product) {
		super(product);
	}

	@Override
	public Operator getOperator() {
		return Operator.MULT;
	}

	@Override
	public Product clone() {
		return new Product(this);
	}

	@Override
	public Term toSimplestForm() {
		method: while (true) {
			super.toSimplestForm();
			if (subTerms.size() == 1) {
				return subTerms.get(0);
			}
			if (subTerms.contains(new RationalNumber(0))) {
				return new RationalNumber(0);
			}
			// (x/y)*(x/y) = (x^2)/(y^2)
			List<Fraction> subFractions = getSubTermsWithType(Fraction.class);
			if (subFractions.size() > 0) {
				subTerms.removeAll(subFractions);
				List<Term> numerators = new ArrayList<Term>(subFractions.size());
				List<Term> denominators = new ArrayList<Term>(
						subFractions.size());
				for (Fraction subFraction : subFractions) {
					numerators.add(subFraction.getNumerator());
					denominators.add(subFraction.getDenominator());
				}
				numerators.addAll(subTerms);
				Product numerator = new Product(numerators);
				Product denominator = new Product(denominators);
				return new Fraction(numerator, denominator).toSimplestForm();
			}
			// Ausmultiplizieren
			List<Sum> subSums = getSubTermsWithType(Sum.class);
			if (subSums.size() > 0) {
				Sum subSum = subSums.get(0);
				subTerms.remove(subSum);
				ArrayList<Term> summands = new ArrayList<Term>();
				for (Term summand : subSum.subTerms) {
					List<Term> factors = new ArrayList<Term>();
					for (Term factor : subTerms) {
						factors.add(factor.clone());
					}
					factors.add(summand);
					summands.add(new Product(factors));
				}
				return new Sum(summands).toSimplestForm();
			}

			// v*log_a(u) = log_a(u^v)
			List<LogarithmusNaturalis> subLns = getSubTermsWithType(LogarithmusNaturalis.class);
			if (subLns.size() == 1) {
				LogarithmusNaturalis ln = subLns.get(0);
				subTerms.remove(ln);
				Term parameter = new Potency(ln.getParameter(), this);
				return new LogarithmusNaturalis(parameter).toSimplestForm();
			}

			if (addupExponents()) {
				continue method;
			}

			// Ausklammern
			// List<Term> termsWithSameBases = null;
			// for (Term subTerm : subTerms) {
			// termsWithSameBases = new ArrayList<Term>();
			// Term base = getBase(subTerm);
			// for (Term innerSubTerm : subTerms) {
			// Term innerBase = getBase(innerSubTerm);
			// if (base.equals(innerBase)) {
			// termsWithSameBases.add(innerSubTerm);
			// }
			// }
			// if (termsWithSameBases.size() > 1) {
			// break;
			// } else {
			// termsWithSameBases = null;
			// }
			// }
			// if (termsWithSameBases != null) {
			// subTerms.removeAll(termsWithSameBases);
			// List<Term> exponents = new ArrayList<Term>(
			// termsWithSameBases.size());
			// for (Term term : termsWithSameBases) {
			// exponents.add(getExponent(term));
			// }
			// Term base = getBase(termsWithSameBases.get(0));
			// Term exponent = new Sum(exponents);
			// subTerms.add(new Potency(base, exponent));
			// continue method;
			// }
			return this;
		}
	}

	/**
	 * Addiert alle Exponenten von Termen mit gleicher Basis zusammen. Falls
	 * sich etwas ändert wird true zurückgegeben, ansonsten false.<br>
	 * Es wird vorausgesetzt, dass alle subTerms in ihrer einfachsten Form sind.<br>
	 * 
	 * @return true falls sich etwas ändert. Sonst false.
	 */
	private boolean addupExponents() {
		boolean somethingChanged = false;
		Map<Term, List<Term>> mapping = getExponentMapping();
		Set<Term> keys = mapping.keySet();
		for (Term base : keys) {
			List<Term> terms = mapping.get(base);
			if (terms.size() > 1) {
				somethingChanged = true;
				for (Term term : terms) {
					subTerms.remove(term);
				}
				List<Term> exponents = getExponents(terms);
				Term exponent = new Sum(exponents).toSimplestForm();
				if (exponent.equals(new RationalNumber(0))) {
					// Faktor fällt weg
				} else if (exponent.equals(new RationalNumber(1))) {
					subTerms.add(base);
				} else {
					subTerms.add(new Potency(base, exponent).toSimplestForm());
				}
			}
		}
		return somethingChanged;
	}

	/**
	 * Gibt eine Map zurück, dessen Schlüssel die einzelnen Basen ohne Exponent
	 * sind und die Werte die Listen aller Faktoren mit den entsprechenden Basen
	 * sind.<br>
	 * <br>
	 * Es wird vorausgesetzt, dass alle subTerms in ihrer einfachsten Form sind.<br>
	 * <br>
	 * <b>Beispiel:</b><br>
	 * Summe: a*a^2*b^x*b^y<br>
	 * <br>
	 * <b>Key | Values</b><br>
	 * a | a, a^2<br>
	 * b | b^x, b^y<br>
	 * 
	 * @return
	 */
	private Map<Term, List<Term>> getExponentMapping() {
		Map<Term, List<Term>> mapping = new HashMap<Term, List<Term>>();
		for (Term subTerm : subTerms) {
			Term base = getBase(subTerm);
			List<Term> terms = mapping.get(base);
			if (terms == null) {
				terms = new ArrayList<Term>();
				mapping.put(base, terms);
			}
			terms.add(subTerm);
		}
		return mapping;
	}

	private Term getBase(Term term) {
		if (term instanceof Potency) {
			return ((Potency) term).getBase();
		} else {
			return term;
		}
	}

	private Term getExponent(Term term) {
		if (term instanceof Potency) {
			return ((Potency) term).getExponent();
		} else {
			return new RationalNumber(1);
		}
	}

	private List<Term> getExponents(Iterable<Term> terms) {
		List<Term> exponents = new ArrayList<Term>();
		for (Term term : terms) {
			exponents.add(getExponent(term));
		}
		return exponents;
	}

	@Override
	public Term getDerivation(Variable v) {
		if (subTerms.size() == 0) {
			return new RationalNumber(0);
		}
		if (subTerms.size() == 1) {
			return subTerms.get(0).getDerivation(v);
		}
		if (subTerms.size() == 2) {
			Product a = new Product(subTerms.get(0).clone(), subTerms.get(1)
					.getDerivation(v));
			Product b = new Product(subTerms.get(0).getDerivation(v), subTerms
					.get(1).clone());
			return new Sum(a, b).toSimplestForm();
		}
		Product product = this.clone();
		Term a = product.subTerms.get(0);
		product.subTerms.remove(0);
		return new Product(a, product).getDerivation(v);
	}

	/**
	 * <b>Potenzen:</b><br>
	 * Fügt die Basis b aller sub-Potenzen b^x, x mal als Faktor hinzu, falls x
	 * eine natürliche Zahl ist. Die Potenzen selbst werden als Faktoren
	 * entfernt.<br>
	 * <br>
	 * <b>Rationale Zahlen:</b><br>
	 * Fügt alle Primfaktoren des Numerators, 1/denominator und falls die Zahl
	 * negativ ist auch noch -1 als Faktoren hinzu. Die rationale Zahl selbst
	 * wird als Faktor entfernt.<br>
	 */
	@Deprecated
	public void flatten() {
		for (int x = 0; x < subTerms.size(); x++) {
			Term subTerm = subTerms.get(x);
			if (subTerm instanceof Potency) {
				Potency potency = (Potency) subTerm;
				Term exponent = potency.getExponent();
				if (exponent instanceof RationalNumber) {
					RationalNumber r = (RationalNumber) exponent;
					if (r.isNatural()) {
						for (int y = 0; y < r.toInt(); y++) {
							subTerms.add(potency.getBase().clone());
						}
						subTerms.remove(potency);
						x--;
					}
				} else {
					subTerms.add(potency.getBase().clone());
					int index = subTerms.indexOf(potency);
					subTerms.remove(potency);
					Sum newExponent = new Sum(exponent, new RationalNumber(-1));
					subTerms.add(index, new Potency(potency.getBase(),
							newExponent));
				}
			} else if (subTerm instanceof RationalNumber) {
				RationalNumber r = (RationalNumber) subTerm;
				List<RationalNumber> factors = r.getFactors();
				subTerms.remove(subTerm);
				subTerms.addAll(0, factors);
				x = x - 1 + factors.size();
			}
		}
	}

	@Override
	public double toDouble() {
		double result = 1;
		for (Term subTerm : subTerms) {
			result *= subTerm.toDouble();
		}
		return result;
	}

}
