package de.adrodoc55.math.term

import spock.lang.Specification

class MultiCalculationSpec extends Specification {

	def "Zwei Summen sind gleich, wenn alle Summanden gleich sind. Reihenfolge ist egal"() {
		when:
		Sum s1 = new Sum(new Variable("x"), new Variable("y"))
		Sum s2 = new Sum(new Variable("y"), new Variable("x"))
		then:
		s1.equals s2
	}

	def "Zwei Summen sind ungleich, wenn micht alle Summanden gleich sind."() {
		when:
		Sum s1 = new Sum(new Variable("x"), new Variable("y"))
		Sum s2 = new Sum(new Variable("x"), new Variable("x"))
		then:
		s1.equals(s2) == false
	}

	def "Bei Summen von Summen bleibt die Reihenfolge erhalten"() {
		given:
		Sum sum = new Sum(
				new Sum(new Variable("x"), new Variable("y")),
				new Variable("z")
				)
		when:
		sum = sum.toSimplestForm();
		then:
		sum.toString() == '(x+y+z)'
	}

	def "Zwei Produkte sind gleich, wenn alle Faktoren gleich sind. Reihenfolge ist egal"() {
		when:
		Product p1 = new Product(new Variable("x"), new Variable("y"))
		Product p2 = new Product(new Variable("y"), new Variable("x"))
		then:
		p1.equals p2
	}

	def "Zwei Produkte sind ungleich, wenn nicht alle Faktoren gleich sind."() {
		when:
		Product p1 = new Product(new Variable("x"), new Variable("y"))
		Product p2 = new Product(new Variable("x"), new Variable("x"))
		then:
		p1.equals(p2) == false
	}

	def "Bei Produkten von Produkten bleibt die Reihenfolge erhalten"() {
		given:
		Product product = new Product(
				new Product(new Variable("x"), new Variable("y")),
				new Variable("z")
				)
		when:
		product = product.toSimplestForm();
		then:
		product.toString() == '(x*y*z)'
	}
}
