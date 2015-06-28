
/**
 * @author Erich Dyck
 */
public class Main {
	
	public static void main(String[] args){
		String key = "c,mvnseoifjw9föo9hjfkvbnoiuwh839r8hkjbvkfdjghi(/§)Udjlkdshflkdshf38<)=W=IJDKFNLKFDSHGeoifjw9föo9hjfkvbnoiuwh839r8hkweoihjochvnv,eexkhdioufshef";
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
		Integer[][] encrypted = feistel.run(fuzzedText, roundKeys);				//Verschlüsselung
		
		System.out.println("Encrypted: " + justGetString(encrypted));
		
		//Decryption
		Integer[][] invertedRoundKeys = new Integer[64][256];
		
		for(int i = 63; i >= 0; i--)
			for(int j = 0; j < 256; j++)
				invertedRoundKeys[63-i][j] = roundKeys[i][j];					//Invertiere Schlüssel
		
		Integer[][] decrypted = feistel.run(encrypted, invertedRoundKeys);		//Entschlüsselung
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
}
