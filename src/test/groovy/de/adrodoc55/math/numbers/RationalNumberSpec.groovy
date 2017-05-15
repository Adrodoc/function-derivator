package de.adrodoc55.math.numbers

import spock.lang.Specification
import spock.lang.Unroll
import de.adrodoc55.math.term.RationalNumber
import de.adrodoc55.math.term.Term

class RationalNumberSpec extends Specification {

	def "denominator kann nicht 0 sein"() {
		when:
		new RationalNumber(5, 0);
		then:
		thrown ArithmeticException
	}

	def "nummerator = 0 -> 0"() {
		when:
		RationalNumber r = new RationalNumber(0, 5);
		then:
		r == new RationalNumber(0);
	}

	def "-0 -> 0"() {
		when:
		RationalNumber r = new RationalNumber(true, 0, 5);
		then:
		r == new RationalNumber(0);
	}

	def "toString gibt die vollständig gekürtzte Form zurück"() {
		given:
		def r = new RationalNumber(6, 3);
		expect:
		r.toString() == "2"
	}

	def "toString gibt bis zwei nachkomma stellen als dezimal brüche"() {
		given:
		def r = new RationalNumber(11, 4);
		expect:
		r.toString() == "2.75"
	}

	def "toString gibt mehr als zwei nachkomma stellen als brüche"() {
		given:
		def r = new RationalNumber(11, 8);
		expect:
		r.toString() == "(11/8)"
	}

	def "die Negation des Denominators wird rausgezogen"() {
		given:
		def r = new RationalNumber(11, -8);
		expect:
		r.toString() == "(-11/8)"
	}

	def "die Negation des Numerators wird rausgezogen"() {
		given:
		def r = new RationalNumber(-11, 8);
		expect:
		r.toString() == "(-11/8)"
	}

	def "die Negation beider hebt sich auf"() {
		given:
		def r = new RationalNumber(-11, -8);
		expect:
		r.toString() == "(11/8)"
	}

	@Unroll("plus bei ganzen Zahlen (a: #a, b: #b)")
	def "plus bei ganzen Zahlen"(a, b, c) {
		when:
		RationalNumber r = new RationalNumber(a, 1)
		RationalNumber r2 = new RationalNumber(b, 1)
		then:
		RationalNumber result = r.plus(r2)
		result.toInt() == c
		where:
		a << [4, 4, 4, 4, -4, -4, -4, -4]
		b << [2, 3, -2, -3, 2, 3, -2, -3]
		c << [6, 7, 2, 1, -2, -1, -6, -7]
	}

	@Unroll("mult bei ganzen Zahlen (a: #a, b: #b)")
	def "mult bei ganzen Zahlen"(a, b, c) {
		when:
		RationalNumber r = new RationalNumber(a, 1)
		RationalNumber r2 = new RationalNumber(b, 1)
		then:
		RationalNumber result = r.mult(r2)
		result.toInt() == c
		where:
		a << [4, 4, 4, 4, -4, -4, -4, -4]
		b << [2, 3, -2, -3, 2, 3, -2, -3]
		c << [8, 12, -8, -12, -8, -12, 8, 12]
	}

	def "pow gibt eine komplett gekürzten Term zurück: ganze Basis, ganzer Exponent"() {
		given:
		RationalNumber base = new RationalNumber(4, 1)
		RationalNumber exponent = new RationalNumber(5, 1)
		when:
		Term result = base.pow(exponent);
		then:
		result.toString() == '1024'
		result.toDouble() == 1024
	}

	def "pow gibt eine komplett gekürzten Term zurück: ganze Basis, Exponent = 0"() {
		given:
		RationalNumber base = new RationalNumber(4, 1)
		RationalNumber exponent = new RationalNumber(0, 1)
		when:
		Term result = base.pow(exponent);
		then:
		result.toString() == '1'
		result.toDouble() == 1
	}

	def "pow gibt eine komplett gekürzten Term zurück: rationale Basis, rationaler Exponent"() {
		given:
		RationalNumber base = new RationalNumber(4, 3)
		RationalNumber exponent = new RationalNumber(5, 3)
		when:
		Term result = base.pow(exponent);
		then:
		result.toString() == '((1024/243)^(1/3))'
		result.toDouble() == 1.6152183047396798
	}
}

