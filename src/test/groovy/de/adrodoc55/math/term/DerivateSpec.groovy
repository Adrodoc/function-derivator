package de.adrodoc55.math.term

import spock.lang.Specification
import de.adrodoc55.math.term.Term
import de.adrodoc55.math.term.Variable

class DerivateSpec extends Specification {

	def "Faktorregel"() {
		given:
		Term t = Term.parse('5*x').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('x'))
		then:
		term == t.toString()
		d.toString() == '5'
	}
	
	def "Variable nach sich selbst abgeleitet"() {
		given:
		Term t = Term.parse('x').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('x'))
		then:
		term == t.toString()
		d.toString() == '1'
	}
	
	def "Variable nach eine Andere abgeleitet"() {
		given:
		Term t = Term.parse('x').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('y'))
		then:
		term == t.toString()
		d.toString() == '0'
	}
	
	def "einfache Potenzregel"() {
		given:
		Term t = Term.parse('x^2').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('x'))
		then:
		term == t.toString()
		d.toString() == '(2*x)'
	}
	
	def "variablen Potenzregel"() {
		given:
		Term t = Term.parse('x^y').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('x'))
		then:
		term == t.toString()
		d.toString() == '(y*(x^(y+(-1))))'
	}
	
	def "Summenregel"() {
		given:
		Term t = Term.parse('x^3+y^2').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('x'))
		then:
		term == t.toString()
		d.toString() == '(3*(x^2))'
	}
	
	def "Produktregel"() {
		given:
		Term t = Term.parse('x^3*y^2').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('x'))
		then:
		term == t.toString()
		d.toString() == '(3*(x^2)*(y^2))'
	}
	
	def "Produktregel mit mehreren Faktoren"() {
		given:
		Term t = Term.parse('x^3*y^2*z').toSimplestForm()
		String term = t.toString()
		when:
		Term d = t.getDerivation(new Variable('x'))
		then:
		term == t.toString()
		d.toString() == '(3*(x^2)*(y^2)*z)'
	}
	
}
