import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the implementation of PowahCube
 * Only task 1!
 * @author Erich Dyck
 *
 */
public class TestCube {

	String key;
	String plaintxt;
	FuzzCube fuzz;
	Preprocessor prep;
	Feistel feistel;
	Integer[][] omega;
	Integer[][][] fuzzCube;
	Integer[][] permutation;
	Integer[] intKey;
	Set<Integer> keySet;
	Set<Integer> compSet;
	Integer[][] preprocessedText;
	Integer[][] fuzzedText;
	Integer[][] roundKeys;
	Integer[][] invertedRoundKeys;
	Integer[][] encrypted;
	Integer[][] decrypted;
	Integer[][] unfuzzed;
	
	Integer[][] sameRow = new Integer[2][256];
	Integer[][] sameColumn = new Integer[2][256];
	Integer[][] bothDifferent = new Integer[2][256];
	
	@Before
	public void setUp(){
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
	public void test() {
		/*Test omega (Only one vSlice, because all other would look the same), 
		 * whether it has no duplicates and all 65536 elements in it.
		 * If it has no duplicates, it should (in this implementation) also have all elements.
		 */
		Set<Integer> checkSet = new HashSet<Integer>();
		for(int j = 0; j < 256; j ++){
			for(int k = 0; k < 256; k++){
				//Check for duplicates.
				assertEquals(true, checkSet.add(omega[k][j]));
			}
		}
		for(int i = 0; i < 65536; i ++){
			//Check whether omega has all 65536 elements;
			assertEquals(true, checkSet.contains(i));
		}
		
		/* Test the permutations, whether the key is right-shifted on i positions, 
		 * where i is the number of the the permutation and whether aleph is not shifted.
		 * In this task there is no restriction about the interface argument types, that's why
		 * I still use Sets. Anyway this interfaces are not for the encryption or decryption,
		 * they are only public for the junit tests.
		 */
		for(int i = 0 ; i < 256; i ++){
			permutation = fuzz.generatePermutation(keySet, compSet, i); //returns one permutation.
			int j = 0;
			for(Integer iter : keySet){ // Check if k~(i) = pi(i+j % k~.size())
				assertEquals(iter, permutation[((j + i)%keySet.size())%256][((j + i)%keySet.size())/256]);
				j ++;
			}
			for(Integer iter : compSet){ // Check if N(l) = pi(l) for l > (|k~| - 1)
				assertEquals(iter, permutation[j%256][j/256]);
				j ++;
			}
		}
		
		/*Test whether all vSlices in the fuzzCube are correctly permutated.
		 * Iterate vSlice i in the fuzzCube and compare it to the
		 * new generated permutation on omega.
		 */
		for(int i = 0; i < 256; i ++){
			permutation = fuzz.generatePermutation(keySet, compSet, i); //returns one permutation.
			for(int j = 0; j < 65536; j++){ //j%256 = x, j/256 = y
				assertEquals(fuzzCube[j%256][j/256][i], 
						omega[permutation[j%256][j/256]%256][permutation[j%256][j/256]/256]);
			}
		}
		
		/*Test whether the round keys are correctly generated.
		 * Iterate through the round keys and compare their elements with the
		 * elements in hSlice sh,4*i(4*i,j).
		 * On each iteration go 4 hSlices and 4 columns forward and iterate through the z coordinate.
		 */
		for(int i = 0; i < 64; i++){
			for(int j = 0; j < 256; j ++){
				assertEquals(roundKeys[i][j], fuzzCube[i*4][i*4][j]);
			}
		}
		
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
		
		fuzz.fuzz(sameRow, false); //isFuzzed is for the unfuzzying, the only difference is the direction variable!.
		fuzz.fuzz(sameColumn, false);
		fuzz.fuzz(bothDifferent, false);
		
		z = 0;
		for(int i = 0; i < 512; i +=2){
			assertEquals(sameRow[i/256][i%256], fuzzCube[(i+1)%256][i/256][z%256]); // row += 1
			assertEquals(sameRow[i/256][(i+1)%256], fuzzCube[(i+2)%256][i/256][z%256]); // row += 1
			assertEquals(sameColumn[i/256][i%256], fuzzCube[i%256][(i/256+1)%256][z%256]); // column += 1
			assertEquals(sameColumn[i/256][(i+1)%256], fuzzCube[i%256][(i/256+2)%256][z%256]); // column += 1
			assertEquals(bothDifferent[i/256][i%256], fuzzCube[(i+5)%256][i/256][z%256]); // rowA = rowB
			assertEquals(bothDifferent[i/256][(i+1)%256], fuzzCube[i%256][((i/256+1)%256)][z%256]); // rowB = rowA
			z++;
		}
		
		/*Test the feistel net.
		 * Encrypt a realistic preprocessed and fuzzed String, then decrypt it, unfuzz, reverse preprocessing
		 * and compare it with the original.
		 */
		Integer[][] preprocessedText = prep.preprocess(plaintxt);				//Preprocess
		Integer[][] fuzzedText = fuzz.fuzz(preprocessedText, false);			//Fuzzying
		Integer[][] encrypted = feistel.run(fuzzedText, roundKeys);				//Encryption
		Integer[][] decrypted = feistel.run(encrypted, invertedRoundKeys);		//Decryption
		
		//Perform an innertest just for the feistel net.
		//assertEquals(Object[],Object[]) is deprecated.
		String before = "", after = "";
		for(int i = 0; i < fuzzedText.length; i++){
			for(int j = 0; j < fuzzedText[i].length; j++){
				before += fuzzedText[i][j];
				after += decrypted[i][j]; //The feistel net doesn't change the length of the strings.
			}
		}
		assertEquals(before, after); //Compare the encryption input with the decryption output.
		
		Integer[][] unFuzzed = fuzz.fuzz(decrypted, true);						//Unfuzzying
		String decStr = prep.reversePreToStr(unFuzzed);							//Returns original string
		
		assertEquals(plaintxt,decStr); //Test the whole procedure.
	}

}
