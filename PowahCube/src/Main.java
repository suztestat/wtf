import java.util.List;


public class Main {
	
	public static void main(String[] args){
		
		//1. PowahCube
		//1.1 IU = 16 bit = char ; 512 IU Blöcke 
		//Ausgangsmenge der möglichen Zeichen = A = [n] ; n = 65535 -> char Unicode Tabelle
		//~k = h(k) so hat die i-te V-Slice Permutation PIik die Form PIik= (ci(~k); N).
		//h(k) = redundate Elemente entfernen
		//2. Permutation
		String plaintxt = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		String key = "";
		for(int i = 0; i < 6000; i ++){
			key += (char)(64 + i);
		}
		FuzzCube fuzzy = new FuzzCube(key);
		//2.1 V-Slice Permutation
		//3. Preprocessing
		String testPreprocessingA = "aaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzz";
		String testPreprocessingB = "aaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzzaabcdefghhijklmmnoppqqrstuvwxxxyzzzzzz";
		testPreprocessingB = Preprocessor.removeTwins(testPreprocessingB);
		testPreprocessingB = Preprocessor.padTerminator(testPreprocessingB);
		testPreprocessingB = Preprocessor.padMultipleUI(testPreprocessingB);
		List<String> testPreprocessingL = Preprocessor.getSubstringList(testPreprocessingB);
		//4. Fuzzying
		//5. Feistel-Netz
		//6. Entschlüsselung
	}
}
