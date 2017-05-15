package de.adrodoc55.math.term

import de.adrodoc55.math.IntegerMath;
import spock.lang.Specification
import spock.lang.Unroll

class MathSpec extends Specification {

	@Unroll("gcd (a: #a, b: #b)")
	def "gcd"(a, b, c) {
		when:
		int gcd = IntegerMath.gcd(a, b)
		then:
		gcd == c
		where:
		a << [4,	4,	4,	4,	-4,	-4,	-4,	-4]
		b << [2,	3,	-2,	-3,	2,	3,	-2,	-3]
		c << [2,	1,	2,	1,	2,	1,	2,	1]
	}
	
	@Unroll("lcm (a: #a, b: #b)")
	def "lcm"(a, b, c) {
		when:
		int lcm = IntegerMath.lcm(a, b)
		then:
		lcm == c
		where:
		a << [4,	4,	4,	4,	-4,	-4,	-4,	-4]
		b << [2,	3,	-2,	-3,	2,	3,	-2,	-3]
		c << [4,	12,	4,	12,	4,	12,	4,	12]
	}
	
}
