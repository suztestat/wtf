import java.util.List;


public class Main {
	
	public static void main(String[] args){
		
		//1. PowahCube
		//1.1 IU = 16 bit = char ; 512 IU Blöcke 
		//Ausgangsmenge der möglichen Zeichen = A = [n] ; n = 65535 -> char Unicode Tabelle
		String plaintxt = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String key = "aaaaaaaaaaaaaakkaaaabcd34dkkk=?";
		FuzzCube fuzzy = new FuzzCube(key);
		//~k = h(k) so hat die i-te V-Slice Permutation PIik die Form PIik= (ci(~k); N).
		//h(k) = redundate Elemente entfernen
		//2. Permutation
		//2.1 V-Slice Permutation
		//3. Preprocessing
		String testPreprocessingA = "aabcdefghhijklmmnoppqqrstuvwxxxyzzzzzz";
		String testPreprocessingB = "aabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzz";
		testPreprocessingB = Preprocessing.removeTwins(testPreprocessingB);
		testPreprocessingB = Preprocessing.padTerminator(testPreprocessingB);
		testPreprocessingB = Preprocessing.padMultipleUI(testPreprocessingB);
		List<String> testPreprocessingL = Preprocessing.getSubstringList(testPreprocessingB);
		//4. Fuzzying
		//5. Feistel-Netz
		//6. Entschlüsselung
	}
}
