import java.util.List;

/**
 * 1.Eingabe I auf I' abbilden. Preprocessing
 * 1.1 Zwillinge splitten
 * Bsp: I = (t1, t2) (t3, t4) (t5, t6) ....
 * sei nun t3 = t4 so wird (t3, t4) zu
 * (t3, pf) (pf, t4)
 * 2. Terminieren
 * Bsp: I = (t1, t2) (t3) so ergibt sich I1 = (t1, t2) (t3, pt)
 * falls I = (t1, t2) (t3, t4) so ergibt sich I2 = (t1, t2) (t3, t4) (pf,pt)
 * 3. Padding
 * Sei |I| = 510 mit I = .... (t509, t510)} (t511, t512)
 * so muss t511 != t510
 * und t511 != t512
 * 
 * 
 * FuzzCube
 * 1. I' = (t1, t2) (t3, t4) (t5, t6) ...
 * Jedes 2er Tupel wird in ein V-Slice geleitet.
 * Bsp: sv1 mit (t1, t2)
 * -----------
 * |t1, x, t2| 1. Fall: gleiche zahlen, auf die werte rechts abbilden
 * |y,  z, w | (t1, t2) werden abgebildet auf I'' = (x, t1) // am Rand wieder am anfang in der gleichen Zeile anfangen
 * |		 |
 * -----------
 * 
 * -----------
 * |e,  ?, ? | Werte in gleicher Spalte
 * |t1, x, y | 2. Fall: gleiche zahlen, auf die werte untereinander abbilden
 * |z,  a, b | (t1, t2) werden abgebildet auf I'' = (z, f)
 * |t2, c, d |
 * |f,  ?, ? |
 * |		 |
 * -----------
 *
 * -----------
 * |t1, a, b | Werte nicht in gleicher spalte oder zeile
 * |c,  f, t2| 3. Fall: (t1, t2) werden abgebildet auf I'' = (b, c) //gleiche zeile t2 bis zur spalte von t1
 * -----------
 * 
 * 
 * Berechnung von Omega bzw Omega~
 * Bsp für Funkt von Aleph:
 * sei K = (3, 5, 6) , n = 10
 * so ist Aleph(k,10) = (0,1,2,4,7,8,9,10)
 * 
 * Bsp für Funkt h(k):
 * h((3,5,5,6,1,5)) = (3,5,6,1)
 * 
 * Bsp für Funkt c(k):
 * c2((3,6,7,1,1)) = (1,1,3,6,7)
 * 
 * 1. Schritt FuzzCube
 * Alle Slices auffüllen
 * 
 * k~ = h(k)
 * Alle v-slices mit dem gleichen k~ und Aleph füllen
 * 
 * 2. Schritt
 * Berechnen der Permutationen Pi ik für i = 0,...,255
 * durch Pi ik = c(k~, Aleph(c(k~)))
 * Bsp Pi 3(2,6,6,1,0) = c3(2,6,1,0 , Aleph(c3(2,6,1,0))) //Dieses Aleph (k,65536)
 * (6,1,0,2), Aleph(6,1,0,2))
 * 
 * Damit jetzt den Cube füllen für Pi 0k Pi 1k Pi 2k .... Pi 255k
 * 
 * 
 * 3. Feistel-Netz
 * Ifuzz element aus [65536]hoch 512
 * Symmetrisch splitten Ifuzz = element aus [65536] hoch 256
 * 64 Runden
 * Feistel Funktion F(Ifuzzx, ki) = Ifuzz XOR ki
 * 
 * Generierung/ Bestimmunmg von ki
 * Omega~ 
 * 	  --------------
 *   /	/ /	      / | Anfang 0,0 dann in die Tiefe z alle 256 Zeichen als k0.
 *  /  / /		 /	| Dann 4 nach rechts und 4 nach unten und das gleiche als k1
 * -------------/	| iteriere weiter für 64 schlüssel.
 * |  |/		|	|
 * |---			|	|
 * |			|  /
 * |			| /
 * |			|/
 * -------------
 * 
 * Sobald alle runden fertig sind werden beide Zweige wieder gemerged -> O element aus [65536] hoch 512
 * 
 * 
 * @author eHealth
 *
 */
public class Main {
	
	public static void main(String[] args){
		

		String plaintxt = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String key = "12";
		String randomKey = "";
		for(int i = 0; i < 64000; i ++){
			//key += (char)(64 + i);
			randomKey += (char)((Math.random()*100000)%64000);
		}
		FuzzCube fuzzy = new FuzzCube();
		//fuzzy.generateOmega(randomKey);
		fuzzy.permuteCube(randomKey);
		//3. Preprocessing
		//String testPreprocessingA = "aaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzZ";
		//String testPreprocessingB = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lo? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lo ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		//Preprocessor proc = new Preprocessor();
		//Integer[][] preprocessed = proc.preprocess(testPreprocessingA);
		//4. Fuzzying
		//5. Feistel-Netz
		//6. Entschlüsselung
	}
}
