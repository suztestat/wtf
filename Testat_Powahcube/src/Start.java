import java.util.Random;


public class Start {
	public static
	String plaintext="Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. "
			+ "Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden"
			+ " flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts o"
			+ "f sentences fly into your mouth. Even the all-powerful Pointing has no control about the blind texts it is an almost unortho"
			+ "graphic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Gra"
			+ "mmar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild Question Marks and devious Se"
			+ "mikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial into the belt and made herse"
			+ "lf on the way. When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her home"
			+ "town Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line Lane. Pityful a rethoric que"
			+ "stion ran over her cheek, then she continued her way. On her way she met a copy. The copy warned the Little Blind Text, that"
			+ " where it came from it would have been rewritten a thousand times and everything that was left from its origin would be the "
			+ "word \"and\" and the Little Blind Text should turn around and return to its own, safe country. But nothing the copy said cou"
			+ "ld convince her and so it didn’t take long until a few insidious Copy Writers ambushed her, made her drunk with Longe and Pa"
			+ "role and dragged her into their agency, where they abused her for their projects again and again. And if she hasn’t been rew"
			+ "ritten, then they are still using her. Far far away, behind the word mountains, far from the countries Vokalia and Consonant"
			+ "ia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language "
			+ "ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic c"
			+ "ountry, in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about the bli"
			+ "nd texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided t"
			+ "o leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wi"
			+ "ld Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her init"
			+ "ial into the belt and made herself on the way. When she reached the first hills of the Italic Mountains, she had a last view"
			+ " back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the L"
			+ "ine Lane. Pityful a rethoric question ran over her cheek, then she continued her way. On her way she met a copy. The copy wa"
			+ "rned the Little Blind Text, that where it came from it would have been rewritten a thousand times and everything that was le"
			+ "ft from its origin would be the word \"and\" and the Little Blind Text should turn around and return to its own, safe countr"
			+ "y. But nothing the copy said could convince her and so it didn’t take long until a few insidious Copy Writers ambushed her, "
			+ "made her drunk with Longe and Parole and dragged her into their agency, where they abused her for their projects again and a"
			+ "gain. And if she hasn’t been rewritten, then they are still using her. Far far away, behind the word mountains, far from the"
			+ " countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of "
			+ "the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary reg"
			+ "elialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. Even the all-powerful Point"
			+ "ing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by t"
			+ "he name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there "
			+ "were thousands of bad Commas, wild Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed "
			+ "her seven versalia, put her initial into the belt and made herself on the way. When she reached the first hills of the Itali"
			+ "c Mountains, she had a last view back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and th"
			+ "e subline of her own road, the Line Lane. Pityful a rethoric question ran over her cheek, then she continued her way. On her"
			+ " way she met a copy. The copy warned the Little Blind Text, that where it came from it would have been rewritten a thousand "
			+ "times and everything that was left from its origin would be the word \"and\" and the Little Blind Text should turn around an"
			+ "d return to its own, safe country. But nothing the copy said could convince her and so it didn’t take long until a few insid"
			+ "ious Copy Writers ambushed her, made her drunk with Longe and Parole and dragged her into their agency, where they abused he"
			+ "r for their projects again and again. And if she hasn’t been rewritten, then they are still using her.Far far away, behind t"
			+ "he word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookma"
			+ "rksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and sup"
			+ "plies it with the necessary regelialia.";

	public static int[] key;
	
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		
		int[] key=new int[randInt(100,500)];
		for (int i=0;i<key.length;i++)key[i]=randInt(200,3000);
		
		//random input
//		plaintext="";
//		for (int i=0;i<10000;i++){
//			plaintext+=randInt(0,65535);
//		}
		Preprocessing pre = new Preprocessing(plaintext);
		Permutation per = new Permutation(key);
		Fuzzying fuzz = new Fuzzying (pre.getPreprocessed(), per.getPowahCube());
		
		System.out.println("Final result:\n"+fuzz.getCrypted()+"\n");
		
		for (int i=0;i<fuzz.getCrypted().length();i++) {
			System.out.print((int)fuzz.getCrypted().charAt(i)+"\t");
			if ((i%512)==0) System.out.println("");
		}
		System.out.println("\nALL DONE! Time needed: "+(System.currentTimeMillis()-start)+"ms.");
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public int[] strtoint(String in){
		int [] out = new int[in.length()];
		for (int i=0; i<in.length();i++)
			out[i]=(int) in.charAt(i);
		
		return out;
	}
	

}
