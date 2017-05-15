package de.adrodoc55.math;

public class IntegerMath {

	public static int gcd(int a, int b) {
		a = Math.abs(a);
		b = Math.abs(b);
		while (b > 0) {
			int temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	public static int lcm(int a, int b) {
		a = Math.abs(a);
		b = Math.abs(b);
		return a * (b / gcd(a, b));
	}

	public static double div(int a, int b) {
		return (double) a / (double) b;
	}

	/**
	 * Berechnet die Primfaktoren, aus denen sich die Zahl n zusammensetzt.
	 * Multipliziert man diese, ergibt sich die Zahl. Zurückgegeben werden die
	 * Zahlen in einem Array, das soviele Elemente hat wie n Primfaktoren. Sie
	 * sind aufsteigend sortiert.
	 *
	 * @param n
	 *            Die zu zerlegende Zahl
	 * @return Die Primfaktoren in einem Array
	 */
	public static int[] primeFactors(long n) {

		/*
		 * Die Vorgehensweise ist folgende: Aufgrund der Vorgabe, dass das
		 * zurückgegebene Array maximal soviele Elemente haben darf wie die Zahl
		 * n Primfaktoren hat, erzeugen wir zunächst ein "temporäres" Array tmp,
		 * dessen Länge durch maxFactors angegeben wird. Dies geschieht aus
		 * einer Überlegung zum Speicherverbrauch: Man könnte tmp auch mit der
		 * Länge n initialisieren, allerdings ist dies aus
		 * Effizienzgesichtspunkten eher suboptimal, da jede Zahl maximal eine
		 * gewisse Anzahl an Primfaktoren haben kann. Da 2 der kleinstmögliche
		 * Primfaktor ist, ist die Anzahl der Primfaktoren immer kleiner gleich
		 * dem Exponenten der nächst- höheren Zweierpotenz. Daraus folgt: n <=
		 * 2^x log(n) <= log (2^x) x >= log (n) / log(2) Mit x als maximaler
		 * Anzahl der Primfaktoren der Zahl n.
		 */

		// Maximale Faktoranzahl ermitteln
		int maxFactors = (int) Math.ceil(Math.log10(n) / Math.log10(2));

		// Temporäres Array erzeugen
		int[] tmp = new int[maxFactors];

		// Zähler der tatsächlichen Faktoranzahl initialisieren
		int anzahlFaktoren = 0;

		/*
		 * Jetzt kommt der Trick der Zerlegung: In einer Zählschleife wird
		 * wiederholt von 2 (kleinster Primfaktor) bis n (Zahl) gezählt, wobei
		 * bei jedem Durchlauf überprüft wird, ob die Zählvariable ganzzahliger
		 * Teiler der Zahl ist. Ist dies der Fall, ist ein neuer Primfaktor
		 * gefunden. Dieser wird in tmp gesichert, und die ganze Schleife wird
		 * "zurückgesetzt", indem der Zähler erneut bei 2 (1++) beginnt und n
		 * durch n/Primfaktor ersetzt wird.
		 */
		for (int j = 2; j <= n; j++) {
			// Ist j Primfaktor?
			if (n % j == 0) {
				// Primfaktor sichern und Anzahl der Primfaktoren erhöhen
				tmp[anzahlFaktoren++] = j;
				// n ändern
				n = n / j;
				// j erneut auf Startwert 2 (1++) setzen
				j = 1;
			}
		}
		// Rückgabearray erzeugen, mit Länge der tatsächlichen Anzahl
		// von Primfaktoren
		int[] prf = new int[anzahlFaktoren];
		// Überführen der Werte des temporären Arrays in das
		// Rückgabearray
		for (int i = 0; i < anzahlFaktoren; i++) {
			prf[i] = tmp[i];
		}
		// Rückgabe
		return prf;
	}

	public static int pow(int base, int exponent) {
		int pow = 1;
		for (int x = 0; x < exponent; x++) {
			pow *= base;
		}
		return pow;
	}

	/**
	 * Zieht die index'te Wurzel vom radicand falls möglich. Ansonsten return
	 * -1.
	 * 
	 * @param radicand
	 * @param index
	 * @return
	 */
	public static int sqrt(int radicand, int index) {
		int[] primeFactors = primeFactors(radicand);
		if (primeFactors.length % index != 0) {
			return -1;
		}
		int[] rootFactors = new int[primeFactors.length / index];
		for (int x = 0; x < primeFactors.length; x++) {
			if (x % index == 0) {
				rootFactors[x / index] = primeFactors[x];
			} else if (rootFactors[x / index] != primeFactors[x]) {
				return -1;
			}
		}
		int root = 1;
		for (int x = 0; x < rootFactors.length; x++) {
			root *= rootFactors[x];
		}
		return root;
	}

}
