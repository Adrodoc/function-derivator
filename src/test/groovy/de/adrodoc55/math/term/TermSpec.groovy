package de.adrodoc55.math.term

import spock.lang.Specification

class TermSpec extends Specification {

	def "Der Parser kann führende Minusse"() {
		given:
		Term t = Term.parse('-4+6')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new RationalNumber(2));
		t.toString() == '2'
	}

	def "Eine Summe wird korrekt berechnet"() {
		given:
		Term t = Term.parse('4+8')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new RationalNumber(12));
		t.toString() == '12'
	}

	def "Ein Produkt wird korrekt berechnet"() {
		given:
		Term t = Term.parse('3*7')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new RationalNumber(21));
		t.toString() == '21'
	}

	def "Eine Potenz wird korrekt berechnet"() {
		given:
		Term t = Term.parse('2^4')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new RationalNumber(16));
		t.toString() == '16'
	}

	def "x*0 = 0"() {
		given:
		Term t = Term.parse('0*x')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new RationalNumber(0));
		t.toString() == '0'
	}

	def "x*1 = x"() {
		given:
		Term t = Term.parse('1*x')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new Variable("x"));
		t.toString() == 'x'
	}

	def "x^0 = 1"() {
		given:
		Term t = Term.parse('x^0')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new RationalNumber(1));
		t.toString() == '1'
	}

	def "x^1 = x"() {
		given:
		Term t = Term.parse('x^1')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new Variable("x"));
		t.toString() == 'x'
	}

	def "Es werden nur Vorfaktoren 'ausgeklammert'"() {
		given:
		Term t = Term.parse('x*x+x*y')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(
				new Sum(
				new Product(
				new Variable("x"),
				new Variable("y")
				),
				new Potency(
				new Variable("x"),
				new RationalNumber(2)
				)
				)
				);
		t.toString() == '((x^2)+(x*y))'
	}

	def "Es werden nur Vorfaktoren 'ausgeklammert'-2"() {
		given:
		Term t = Term.parse('x*x+x*x*y')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((x^2)+((x^2)*y))'
	}

	def "Vorfaktoren werden addiert"() {
		given:
		Term t = Term.parse('6*x+3*x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(9*x)'
	}

	def "negative Vorfaktoren werden subtrahiert"() {
		given:
		Term t = Term.parse('6*x-3*x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(3*x)'
	}

	def "rationale Vorfaktoren werden zusammen addiert"() {
		given:
		Term t = Term.parse('(2/3)*x+4*x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((14/3)*x)'
	}

	def "x^y*x^z -> x^(y+z)"() {
		given:
		Term t = Term.parse('x^y*x^z')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(x^(y+z))'
	}

	def "x*x -> x^2"() {
		given:
		Term t = Term.parse('x*x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(x^2)'
	}

	def "x*x*y*y -> x^2*y^2"() {
		given:
		Term t = Term.parse('x*x*y*y')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((x^2)*(y^2))'
	}

	def "7^(2/7)"() {
		given:
		Term t = Term.parse('7^(2/7)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(49^(1/7))'
	}

	def "x+(5*h*x)-(6*x)"() {
		given:
		Term t = Term.parse('x+(5*h*x)-(6*x)')
		/*
		 * x+(5*h*x)-(6*x)
		 * 5*h*x-5*x
		 * x*(5*h-5)
		 */
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((5*h*x)+((-5)*x))'
	}

	def "Ausmultiplizieren" () {
		given:
		Term t = Term.parse('(a+b)*(c+d)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((a*c)+(a*d)+(b*c)+(b*d))'
	}

	def "Eine Fraction mit 0 als numerator ist 0" () {
		given:
		Term t = Term.parse('0/x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '0'
	}

	def "Eine Fraction aus zwei rationalen Zahlen wird zu einer rationalen Zahl" () {
		given:
		Term t = Term.parse('1/3')
		when:
		t = t.toSimplestForm()
		then:
		t.equals(new RationalNumber(1, 3))
		t.toString() == '(1/3)'
	}

	def "Eine Fraction mit gleichem Numerator und Denominator ist 1" () {
		given:
		Term t = Term.parse('x/x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '1'
	}

	def "Eine Fraction mit einer Summe als Numerator wird zu einer Summe aus Fractions" () {
		given:
		Term t = Term.parse('(x+y)/z')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((x/z)+(y/z))'
	}

	def "Eine Fraction wird gekürzt falls möglich" () {
		given:
		Term t = Term.parse('((x*y)+(x*z))/x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(y+z)'
	}

	def "Eine Fraction mit einem rationalen Denominator wird zu dem Produkt aus dem Numerator und dem inversen Denominator" () {
		given:
		Term t = Term.parse('x/(3/2)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((2/3)*x)'
	}

	def "Ein Produkt mit einer Fraction wird zusammengefasst" () {
		given:
		Term t = Term.parse('2*(1/x)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(2/x)'
	}

	def "Ein Produkt aus Fractions wird zusammengefasst" () {
		given:
		Term t = Term.parse('(x/y)*(x/y)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((x^2)/(y^2))'
	}

	def "Eine Fraction mit einem rationalen Numerator bleibt so" () {
		given:
		Term t = Term.parse('(2/3)/(x+y)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((2/3)/(x+y))'
	}

	def "Ein Produkt aus Fractions wird gekürtzt" () {
		given:
		Term t = Term.parse('(x/1)*(1/x)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '1'
	}

	def "Eine Potenz mit negativem Exponenten wird zu einer Fraction" () {
		given:
		Term t = Term.parse('x^(-1)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(1/x)'
	}

	def "y+x+2*x"() {
		given:
		Term t = Term.parse('y+x+2*x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(y+(3*x))'
	}

	def "(x+y)/(x*y) -> (1/x)+(1/y)"() {
		given:
		Term t = Term.parse('(x+y)/(x*y)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '((1/y)+(1/x))'
	}

	def "2 Potenzen mit gleicher Basis"() {
		given:
		Term t = Term.parse('x^y*x^z')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(x^(y+z))'
	}

	def "2 Potenzen mit gleichem Exponenten"() {
		given:
		Term t = Term.parse('2^x*3^x')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '(6^x)'
	}

	def "LogarithmusNaturalis von 1 ist 0"() {
		given:
		Term t = Term.parse('ln(1)')
		when:
		t = t.toSimplestForm()
		then:
		t.toString() == '0'
	}
}
