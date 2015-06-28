import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class builds a fuzz cube for Powah Cube and has a method to fuzz/unfuzz a input integer array.
 * The fuzz cube is saved global and can be accessed through a getter method.
 * @author Erich Dyck
 *
 */
public class FuzzCube {
	private Integer[][][] mFuzzCube;
	private Integer[] mKey;
	private Integer[][] mOmega;
	
	/**
	 * This constructor initialize the fuzz cube and call the method
	 * buildFuzzCube() which generate the fuzz cube with the key string, which
	 * is given by the constructors argument.
	 * @param key
	 * @throws IllegalArgumentException
	 */
	public FuzzCube(String key) throws IllegalArgumentException{
		if(key.length() < 65536){
			mFuzzCube = new Integer[256][256][256];
			mKey = getIntFromStr(key);
			buildFuzzCube();
		} else
			throw new IllegalArgumentException("The key is too long.");
	}
	
	/**
	 * This constructor initialize the fuzz cube and call the method
	 * buildFuzzCube() which generate the fuzz cube with the key integer array, which
	 * is given by the constructors argument.
	 * @param key
	 * @throws IllegalArgumentException
	 */
	public FuzzCube(Integer[] key) throws IllegalArgumentException{
		if(key.length < 65536){
			mFuzzCube = new Integer[256][256][256];
			mKey = key;
			buildFuzzCube();
		} else
			throw new IllegalArgumentException("The key is too long.");
	}
	
	/**
	 * This method prepare the key and aleph, creates omega and
	 * call the method generateCube with them.
	 */
	private void buildFuzzCube(){
		Set<Integer> keySet = removeRedundantElements(mKey);
		Set<Integer> compSet = getNcomplement(keySet, 65536);
		
		mOmega = fillV_Slice(keySet);
		mOmega = padV_Slice(mOmega, compSet);
		generateCube(keySet, compSet);
	}
	
	/**
	 * This method generates a permutation for each vSlice of the fuzz cube,
	 * apply it on omega and save the elements in the related fuzz cube vSlice.  
	 * @param keySet
	 * @param compSet
	 */
	public void generateCube(Set<Integer> keySet, Set<Integer> compSet){
		Integer[][] per = new Integer[256][256];
		for(int z = 0; z < 256; z ++){
			per = generatePermutation(keySet, compSet, z);
			for(int y = 0; y < 256; y ++){
				for(int x = 0; x < 256; x++){
					mFuzzCube[x][y][z] = mOmega[per[x][y]%256][per[x][y]/256];
				}
			}
		}
	}
	
	/**
	 * This method creates a permutation for one vSlice.
	 * It right-shifts the key elements by the number of the vSlice,
	 * fill it in the permutation and pad it with aleph.
	 * @param keySet
	 * @param compSet
	 * @param z
	 * @return permutation
	 */
	public Integer[][] generatePermutation(Set<Integer> keySet, Set<Integer> compSet, int z){
		keySet = shiftElements(keySet, z);
		Integer[][] permutation = new Integer[256][256];
		
		permutation = fillV_Slice(keySet);
		permutation = padV_Slice(permutation, compSet);
		
		return permutation;
	}
	
	/**
	 * This method creates and fill a vSlice with k~
	 * and return it.
	 * @param key
	 * @return vSlice
	 */
	private Integer[][] fillV_Slice(Set<Integer> key){
		Integer[][] vSlice = new Integer[256][256];
		
		int x = 0;
		for(Integer iter : key){
			vSlice[x%256][x/256] = iter;
			x++;
		}
		
		return vSlice;
	}
	
	/**
	 * This method pad a given vSlice with aleph.
	 * @param vSlice
	 * @param aleph
	 * @return vSlice
	 */
	private Integer[][] padV_Slice(Integer[][] vSlice, Set<Integer> aleph){
		int x = 256 - aleph.size()%256; //Get the x-coordinate where to start with padding.
		int y = 256 - (int)Math.ceil(aleph.size()/256F); //Get the y-coordinate where to start with padding.
		
		for(Integer iter : aleph){
			vSlice[x%256][y] = iter;
			x++;
			if(x % 256 == 0)
				y ++;
		}
		
		return vSlice;
	}
	
	/**
	 * Function c(~k), right shift the input elements by given distance.
	 * @param key
	 * @param distance
	 * @return shiftedKey
	 */
	private Set<Integer> shiftElements(Set<Integer> key, Integer distance){
		Set<Integer> shiftedKey = new LinkedHashSet<Integer>();
		
		int i = 0;
		for(Integer iter : key){
			if(i >= key.size() - distance % key.size())
				shiftedKey.add(iter); // Copy the symbols at index i >= last - distance on the first positions.
			i++;
		}
		shiftedKey.addAll(key); // Then add the old first elements after the now right-shifted ones.

		return shiftedKey;
	}
	
	/**
	 * Function h(k), removes redundant input elements.
	 * @param key
	 * @return remKey
	 */
	public Set<Integer> removeRedundantElements(Integer[] key){
		Set<Integer> remKey = new LinkedHashSet<Integer>();

		for(int i = 0; i < key.length; i++){
			remKey.add(key[i]);
		}
		
		return remKey;
	}

	/**
	 * Get the n-complement (aleph) with given key and n.
	 * @param key
	 * @param n
	 * @return nComplement
	 */
	public Set<Integer> getNcomplement(Set<Integer> key, Integer n){
		Set<Integer> nComplement = new LinkedHashSet<Integer>();
		
		for(int i = 0; i < n; i++){
			if(!key.contains(i))
				nComplement.add(i);
		}

		return nComplement;
	}
	
	/**
	 * Fuzz or Unfuzz the input with the fuzzCube.
	 * Same Row: xA + 1, xB + 1
	 * Same Column: yA + 1, yB + 1
	 * Both different: xA = xB, xB = xA
	 * @param input
	 * @param isFuzzed
	 * @return input
	 */
	public Integer[][] fuzz(Integer[][] input, boolean isFuzzed){ //isFuzzed == false -> fuzz, isFuzzed == true -> unfuzz
		int z = 0, xA = 0, xB = 0, yA = 0, yB = 0, direction = 1;
		if(isFuzzed == true){ //If input is fuzzed, just change the direction.
			direction = -1;
		}

		boolean firstFound = false, secondFound = false;
		for(int i = 0; i < input.length; i ++){
			z = 0;
			
			for(int j = 0; j < input[i].length; j += 2){
				for(int k=0 ; k < 65536; k++){ //Search the input elements in the fuzz cube.
					if(input[i][j].equals(mFuzzCube[k%256][k/256][z%256])){
						firstFound = true;
						xA = k%256;
						yA = k/256;
					}
					else if(input[i][j+1].equals(mFuzzCube[k%256][k/256][z%256])){
						secondFound = true;
						xB = k%256;
						yB = k/256;
					}
					if(firstFound == true && secondFound == true){
						firstFound = false;
						secondFound = false;
						break; //Prevent unnecessary iterations.
					}
				} //End of the search, start fuzzying/unfuzzying
				if(yA == yB){
					input[i][j] = mFuzzCube[(((xA + direction)%256)+256)%256][yA][z%256];
					input[i][j + 1] = mFuzzCube[(((xB + direction)%256)+256)%256][yB][z%256];
				}
				if(xA == xB){
					input[i][j] = mFuzzCube[xA][(((yA + direction)%256)+256)%256][z%256];
					input[i][j + 1] = mFuzzCube[xB][(((yB + direction)%256)+256)%256][z%256];
				}
				if(yA != yB && xA != xB){ //There is no "d" calculated, just xA exchanged with xB.
					input[i][j] = mFuzzCube[xB][yA][z%256];
					input[i][j + 1] = mFuzzCube[xA][yB][z%256];
				}
				z ++;
			}
		}
		return input;
	}
	
	/**
	 * Create a enumeration for the input and return it as an integer array.
	 * @param str
	 * @return output
	 */
	public Integer[] getIntFromStr(String str){
		Integer[] output = new Integer[str.length()];
		
		for(int i = 0; i < str.length(); i ++)
			output[i] = (int)str.charAt(i);
		
		return output;
	}
	
	public Integer[][][] getFuzzCube(){
		return mFuzzCube;
	}
	
	public void setFuzzCube(Integer[][][] fuzz){
		mFuzzCube = fuzz;
	}
	
	public Integer[][] getOmega(){
		return mOmega;
	}
	
	public void setOmega(Integer[][] omega){
		mOmega = omega;
	}
}
