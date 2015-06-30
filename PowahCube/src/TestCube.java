import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the implementation of PowahCube
 * Only task 1!
 * @author Erich Dyck
 *
 */
public class TestCube {

	private static String key;
	private static String plaintxt;
	private static FuzzCube fuzz;
	private static Preprocessor prep;
	private static Feistel feistel;
	private static Integer[][] omega;
	private static Integer[][][] fuzzCube;
	private static Integer[][] permutation;
	private static Integer[] intKey;
	private static Set<Integer> keySet;
	private static Set<Integer> compSet;
	private static Integer[][] preprocessedText;
	private static Integer[][] fuzzedText;
	private static Integer[][] roundKeys;
	private static Integer[][] invertedRoundKeys;
	private static Integer[][] encrypted;
	private static Integer[][] decrypted;
	private static Integer[][] unfuzzed;
	
	private static Integer[][] sameRow = new Integer[2][256];
	private static Integer[][] sameColumn = new Integer[2][256];
	private static Integer[][] bothDifferent = new Integer[2][256];
	
	@BeforeClass //Do the setup only once for all tests.
	public static void setUp(){
		/*Only task 1 (I worked alone), no random keys or plaintexts. If it should be random, 
		 * the symbols pf and pt 
		 * should be declared dynamically to prevent that the random text contains them.
		 * Alternatively the random text could not accept the symbols pf and pt or only choose
		 * unicode defined symbols because in this implementation the choosen values for pf and pt
		 * are not defined in unicode.
		*/
		key = "c,mvnseoifjw9föo9hjfkvbnoiuwh839r8hkjbvkfdjghi(/§)Udjlkdshflkdshf38<)=W=IJDKFNLKFDSHGeoifjw9föo9hjfkvbnoiuwh839r8hkweoihjochvnv,eexkhdioufshefc,mvnseoifjw9föo9hjfkvbnoiuwh839r8hkjbvkfdjghi(/§)Udjlkdshflkdshf38<)=W=IJDKFNLKFDSHGeoifjw9föo9hjfkvbnoiuwh839r8hkweoihjochvnv,eexkhdioufshefc,mvnseoifjw9föo9hjfkvbnoiuwh839r8hkjbvkfdjghi(/§)Udjlkdshflkdshf38<)=W=IJDKFNLKFDSHGeoifjw9föo9hjfkvbnoiuwh839r8hkweoihjochvnv,eexkhdioufshefc,mvnseoifjw9föo9hjfkvbnoiuwh839r8hkjbvkfdjghi(/§)Udjlkdshflkdshf38<)=W=IJDKFNLKFDSHGeoifjw9föo9hjfkvbnoiuwh839r8hkweoihjochvnv,eexkhdioufshef";
		plaintxt = "? 1 Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		
		fuzz = new FuzzCube(key);
		omega = fuzz.getOmega();
		fuzzCube = fuzz.getFuzzCube();
		permutation = new Integer[256][256];
		intKey = fuzz.getIntFromStr(key);
		keySet = fuzz.removeRedundantElements(intKey);
		compSet = fuzz.getNcomplement(keySet, 65536);
		prep = new Preprocessor();
		feistel = new Feistel();
		roundKeys = feistel.scheduleRoundKeys(fuzzCube);
		invertedRoundKeys = new Integer[64][256];
		
		for(int i = 63; i >= 0; i--)
			for(int j = 0; j < 256; j++)
				invertedRoundKeys[63-i][j] = roundKeys[i][j];
		
		sameRow = new Integer[2][256];
		sameColumn = new Integer[2][256];
		bothDifferent = new Integer[2][256];
	}
	
	@Test
	public void testOmega() {
		/*Test omega (Only one vSlice, because all other would look the same), 
		 * whether it has no duplicates and all 65536 elements in it.
		 * If it has no duplicates, it should (in this implementation) also have all elements.
		 */
		Set<Integer> checkSet = new HashSet<Integer>();
		for(int j = 0; j < 256; j ++){
			for(int k = 0; k < 256; k++){
				//Check for duplicates.
				assertEquals("Duplicate entry found.", 
						true, checkSet.add(omega[k][j]));
			}
		}
		for(int i = 0; i < 65536; i ++){
			//Check whether omega has all 65536 elements;
			assertEquals("Omega hasn't all elements.",
					true, checkSet.contains(i));
		}
	}

	@Test
	public void testPermutation(){
		/* Test the permutations, whether the key is right-shifted on i positions, 
		 * where i is the number of the permutation, and whether aleph is not shifted.
		 * In this task there is no restriction about the interface argument types, that's why
		 * there are still used Sets. Anyway this interfaces are not used for the encryption or decryption,
		 * they are only public for the junit tests.
		 */
		for(int i = 0 ; i < 256; i ++){
			permutation = fuzz.generatePermutation(keySet, compSet, i); //returns one permutation.
			int j = 0;
			for(Integer iter : keySet){ // Check if k~(i) = pi(i+j % k~.size())
				assertEquals("Wrong right-shifted",
						iter, permutation[((j + i)%keySet.size())%256][((j + i)%keySet.size())/256]);
				j ++;
			}
			for(Integer iter : compSet){ // Check if N(l) = pi(l) for l > (|k~| - 1)
				assertEquals("Aleph was shifted", 
						iter, permutation[j%256][j/256]);
				j ++;
			}
		}
	}

	@Test
	public void testFuzzCube(){
		/*Test whether all vSlices in the fuzzCube are correctly permutated.
		 * Iterate vSlice i in the fuzzCube and compare it to the
		 * new generated permutation on omega.
		 */
		for(int i = 0; i < 256; i ++){
			permutation = fuzz.generatePermutation(keySet, compSet, i); //returns one permutation.
			for(int j = 0; j < 65536; j++){ //j%256 = x, j/256 = y
				assertEquals("V-Slice: " + i + " was not correctly permuted", 
						fuzzCube[j%256][j/256][i], 
						omega[permutation[j%256][j/256]%256][permutation[j%256][j/256]/256]);
			}
		}
	}

	@Test
	public void testRoundKeys(){
		/*Test whether the round keys are correctly generated.
		 * Iterate through the round keys and compare their elements with the
		 * elements in hSlice sh,4*i(4*i,j).
		 * On each iteration go 4 hSlices and 4 columns forward and iterate through the z coordinate.
		 */
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 256; j ++){
				assertEquals("Round key: " + i + " wasn't correctly generated.",
						roundKeys[i][j], fuzzCube[i*4][i*4][j]);
			}
		}
	}

	@Test
	public void testFuzzying(){
		/*Test the fuzzying.
		 * Create Arrays with elements from the fuzzCube.
		 * sameRow has tupel from the fuzzCube with x, x+1.
		 * sameColumn has tupel with y, y+1.
		 * bothDifferent has tupel with x, x+5 and y, y+1.
		 * Check if sameRow fuzzed x+1, x+2,
		 * sameColumn y+1, y+2,
		 * bothDifferent xA = xB, xB = xA.
		 */
		int z = 0;
		for(int i = 0; i < 512; i +=2){ //testBlocks[y][x] , fuzzCube[x][y][z]
			sameRow[i/256][i%256] = fuzzCube[i%256][i/256][z%256];
			sameRow[i/256][(i+1)%256] = fuzzCube[(i+1)%256][i/256][z%256]; // Same row += 1
			sameColumn[i/256][i%256] = fuzzCube[i%256][i/256][z%256];
			sameColumn[i/256][(i+1)%256] = fuzzCube[i%256][(i/256+1)%256][z%256]; // Same column += 1
			bothDifferent[i/256][i%256] = fuzzCube[i%256][i/256][z%256];
			bothDifferent[i/256][(i+1)%256] = fuzzCube[(i+5)%256][(i/256+1)%256][z%256]; // row += 5, column += 1
			z++;
		}
		
		fuzz.fuzz(sameRow, false); //isFuzzed is for the unfuzzying, the only difference is the direction variable.
		fuzz.fuzz(sameColumn, false);
		fuzz.fuzz(bothDifferent, false);
		
		z = 0;
		for(int i = 0; i < 512; i +=2){ //testBlocks[y][x] , fuzzCube[x][y][z]
			assertEquals("Same row xA wrong fuzzed", 
					sameRow[i/256][i%256], fuzzCube[(i+1)%256][i/256][z%256]); // row += 1
			assertEquals("Same row xB wrong fuzzed",
					sameRow[i/256][(i+1)%256], fuzzCube[(i+2)%256][i/256][z%256]); // row += 1
			assertEquals("Same column yA wrong fuzzed",
					sameColumn[i/256][i%256], fuzzCube[i%256][(i/256+1)%256][z%256]); // column += 1
			assertEquals("Same column yB wrong fuzzed",
					sameColumn[i/256][(i+1)%256], fuzzCube[i%256][(i/256+2)%256][z%256]); // column += 1
			assertEquals("Both different xA wrong fuzzed",
					bothDifferent[i/256][i%256], fuzzCube[(i+5)%256][i/256][z%256]); // rowA = rowB
			assertEquals("Both different xB wrong fuzzed",
					bothDifferent[i/256][(i+1)%256], fuzzCube[i%256][((i/256+1)%256)][z%256]); // rowB = rowA
			z++;
		}
	}

	@Test
	public void testFeistelNet(){
		/*Test the feistel net.
		 * Encrypt a realistic preprocessed and fuzzed String, then decrypt it, unfuzz, reverse preprocessing
		 * and compare it with the original.
		 */
		preprocessedText = prep.preprocess(plaintxt);				//Preprocess
		fuzzedText = fuzz.fuzz(preprocessedText, false);			//Fuzzying
		encrypted = feistel.run(fuzzedText, roundKeys);				//Encryption
		decrypted = feistel.run(encrypted, invertedRoundKeys);		//Decryption
		
		//Perform an inner test, where just the feistel net is tested! ( F~(F(I)) = I )
		for(int i = 0; i < fuzzedText.length; i++){
			for(int j = 0; j < fuzzedText[i].length; j++){
				assertEquals(fuzzedText[i][j], decrypted[i][j]);
			}
		}
		
		unfuzzed = fuzz.fuzz(decrypted, true);						//Unfuzzying
		String decStr = prep.reversePreToStr(unfuzzed);				//Returns original string
		
		assertEquals(plaintxt,decStr); //Compare the result of the whole procedure with the original String.
	}
}
