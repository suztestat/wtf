import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 *asdf
 */
public class Main {
	
	//TODO: Exception handling fehlt noch fast komplett.
	public static void main(String[] args){
		String key = "c,mvnseoifjw9föo9hjfkvbnoiuwh839r8hkjbvkfdjghi(/§)Udjlkdshflkdshf38<)=W=IJDKFNLKFDSHGeoifjw9föo9hjfkvbnoiuwh839r8hkweoihjochvnv,eexkhdioufshef";
		String emptyKey = "";
		String plaintxt = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		
		Long start = System.currentTimeMillis();
		
		System.out.println("Plaintext: " + plaintxt);
		
		FuzzCube fuzz = new FuzzCube(key);
		Preprocessor prep = new Preprocessor();
		Feistel feistel = new Feistel();
		
		//Encryption
		Integer[][] preprocessedText = prep.preprocess(plaintxt);				//Preprocess
		Integer[][] fuzzedText = fuzz.fuzz(preprocessedText, false);			//Fuzzying
		
		Integer[][] roundKeys = feistel.scheduleRoundKeys(fuzz.getFuzzCube());	//Rundenschlüssel
		Integer[][] encrypted = feistel.encrypt(fuzzedText, roundKeys);			//Verschlüsselung
		
		System.out.println("Encrypted: " + justGetString(encrypted));
		
		//Decryption
		Integer[][] invertedRoundKeys = new Integer[64][256];
		
		for(int i = 63; i >= 0; i--)
			for(int j = 0; j < 256; j++)
				invertedRoundKeys[63-i][j] = roundKeys[i][j];					//Invertiere Schlüssel
		
		Integer[][] decrypted = feistel.encrypt(encrypted, invertedRoundKeys);	//Entschlüsselung
		Integer[][] unFuzzed = fuzz.fuzz(decrypted, true);						//Unfuzzying
		
		String decStr = prep.reversePreToStr(unFuzzed);							//Preprocess umkehren
		
		System.out.println("Decrypted: " + decStr);								//Fertig
		
		Long end = System.currentTimeMillis();
		
		System.out.println("Runtime: " + (end - start));
		

	}
	
	private static String justGetString(Integer[][] encrypted){
		String output = "";
		for(int i = 0; i < encrypted.length; i++)
			for(int j = 0; j < encrypted[i].length; j++)
				output += (char)encrypted[i][j].intValue();
		
		return output;
	}
	
	
	private void oldTest(){
		//String plaintxt = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String key = "asdfdsfdsg";
		String randomKey = "";
		for(int i = 0; i < 64000; i ++){
			//key += (char)(64 + i);
			randomKey += (char)((Math.random()*100000)%64000);
		}
		Long start = System.currentTimeMillis();
		FuzzCube fuzzy = new FuzzCube(key);
		//Printer.checkCube(fuzzy.getFuzzCube());
		String testPreprocessingA = "aaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzZ";
		System.out.println("Plaintext: " + testPreprocessingA);
		///String testPreprocessingB = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lo? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lo ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		Preprocessor proc = new Preprocessor();
		Integer[][] preprocessed = proc.preprocess(testPreprocessingA);
		/*for(int i = 0; i < preprocessed.length; i ++){
			System.out.println();
			for(int j = 0 ; j < preprocessed[i].length; j++)
				System.out.print(preprocessed[i][j] + "\t");
		}*/
		for(int i = 0; i < preprocessed.length; i ++){
			System.out.println("Preprocessed");
			for(int j = 0 ; j < preprocessed[i].length; j++)
				System.out.print(preprocessed[i][j] + "\t");
		}
		
		Integer[][] fuzzed = fuzzy.fuzz(preprocessed, false);
		
		for(int i = 0 ; i < fuzzed.length; i ++){
			System.out.println("\nFuzzed:\n");
			for(int j = 0; j < 512; j ++){
				System.out.print(fuzzed[i][j] + "\t");
			}
		}
		
		Feistel feistel = new Feistel();
		Integer[][] roundKeys = feistel.scheduleRoundKeys(fuzzy.getFuzzCube());
		
		/*for(int i = 0 ; i < roundKeys.length; i ++){
			System.out.println("\nRound Key " + i + ": ");
			for(int j = 0; j < roundKeys[i].length; j++)
				System.out.print(roundKeys[i][j] + "\t");
		}*/
		
		Integer[][] invertedRoundKeys = new Integer[64][256];
		
		for(int i = 63; i >= 0; i--)
			for(int j = 0; j < 256; j++)
				invertedRoundKeys[63-i][j] = roundKeys[i][j];
		
		/*for(int i = 0 ; i < invertedRoundKeys.length; i ++){
			System.out.println("\nInverted Round Key " + i + ": ");
			for(int j = 0; j < invertedRoundKeys[i].length; j++)
				System.out.print(invertedRoundKeys[i][j] + "\t");
		}*/

		Integer[][] encrypted = feistel.encrypt(fuzzed, roundKeys);
		Integer[][] decrypted = feistel.encrypt(encrypted, invertedRoundKeys);
	
		for(int i = 0 ; i < decrypted.length; i ++){
			System.out.println("\nDecrypted:\n");
			for(int j = 0; j < 512; j ++){
				System.out.print(decrypted[i][j] + "\t");
			}
		}
		
		Integer[][] unFuzzed = fuzzy.fuzz(decrypted, true);
		
		for(int i = 0 ; i < unFuzzed.length; i ++){
			System.out.println("\nUnfuzzed:\n");
			for(int j = 0; j < 512; j ++){
				System.out.print(unFuzzed[i][j] + "\t");
			}
		}
		
		String decStr = proc.reversePreToStr(unFuzzed);
		System.out.println("\n\nDecrypted: " + decStr);
		
		/*for(int i = 0 ; i < encrypted.length; i ++){
			System.out.println("\nEncrypted:\n");
			for(int j = 0; j < 512; j ++){
				System.out.print(encrypted[i][j] + "\t");
			}
		}*/
		
		
		Long end = System.currentTimeMillis();
		System.out.println("\n\nRuntime: " + (end - start));
		
		/*String fu = "";
		for(int i = 0 ; i < 100000 ; i ++){
			fu += (char)i;
		}*/
	}
}
