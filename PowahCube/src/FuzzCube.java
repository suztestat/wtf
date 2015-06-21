import java.util.LinkedHashSet;
import java.util.Set;


public class FuzzCube {
	private Integer[][][] mFuzzCube;
	private Integer[] mKey;
	
	public FuzzCube(String key) throws IllegalArgumentException{
		if(key.length() < 65536){
			mFuzzCube = new Integer[256][256][256];
			mKey = getIntFromStr(key);
			buildFuzzCube();
		} else
			throw new IllegalArgumentException("The key is too long.");
	}
	
	public FuzzCube(Integer[] key) throws IllegalArgumentException{
		if(key.length < 65536){
			mFuzzCube = new Integer[256][256][256];
			mKey = key;
			buildFuzzCube();
		} else
			throw new IllegalArgumentException("The key is too long.");
	}
	
	private void buildFuzzCube(){
		Set<Integer> keySet = removeRedundantElements(mKey);
		Set<Integer> compSet = getNcomplement(keySet, 65535);
		
		Integer[][] omega = fillV_Slice(keySet);
		omega = padV_Slice(omega, compSet);
		generateCube(omega, keySet, compSet);
	}
	
	public Integer[][] fuzz(Integer[][] input, boolean isFuzzed){ //isFuzzed == false -> fuzz, isFuzzed == true -> unfuzz
		int z = 0, xA = 0, xB = 0, yA = 0, yB = 0, direction = 1;
		if(isFuzzed == true){
			direction = -1;
		}
		//System.out.println("Direction: " + direction);
		//System.out.println();
		boolean firstFound = false, secondFound = false;
		for(int i = 0; i < input.length; i ++){
			z = 0;
			
			for(int j = 0; j < input[i].length; j += 2){
				//System.out.println("Checking: " + input[i][j] + " and: " + input[i][j+1]);
				for(int k=0 ; k < 65536; k++){
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
						break;
					}
				}
				if(yA == yB){
					//System.out.println("Same Row; Input: " + input[i][j] + " changed to: " + mFuzzCube[(((xA + direction)%256)+256)%256][yA][z%256] + " and Input: " + input[i][j+1] + " changed to: " + mFuzzCube[(((xB + direction)%256)+256)%256][yB][z%256] +
							//" Details: \nxA: " + xA + " changed to: " + ((((xA + direction)%256)+256)%256) + " yA: " + yA + " changed to: " + yA + "\nxB: "+ xB + " changed to: " + ((((xB + direction)%256)+256)%256) + " yB: " + yB + " changed to: " + yB);
					input[i][j] = mFuzzCube[(((xA + direction)%256)+256)%256][yA][z%256];
					input[i][j + 1] = mFuzzCube[(((xB + direction)%256)+256)%256][yB][z%256];
				}
				if(xA == xB){
					//System.out.println("Same Column; Input: " + input[i][j] + " changed to: " + mFuzzCube[xA][(((yA + direction)%256)+256)%256][z%256] + " and Input: " + input[i][j+1] + " changed to: " + mFuzzCube[xB][(((yB + direction)%256)+256)%256][z%256] +
							//" Details: \nxA: " + xA + " changed to: " + xA + " yA: " + yA + " changed to: " + ((((yA + direction)%256)+256)%256) + "\nxB: "+ xB + " changed to: " + xB + " yB: " + yB + " changed to: " + ((((yB + direction)%256)+256)%256));
					input[i][j] = mFuzzCube[xA][(((yA + direction)%256)+256)%256][z%256];
					input[i][j + 1] = mFuzzCube[xB][(((yB + direction)%256)+256)%256][z%256];
				}
				if(yA != yB && xA != xB){
					//System.out.println("Not same Row/Column; Input: " + input[i][j] + " changed to: " + mFuzzCube[xB][yA][z%256] + " and Input: " + input[i][j+1] + " changed to: " + mFuzzCube[xA][yB][z%256] +
							//" Details: \nxA: " + xA + " changed to: " + xB + " yA: " + yA + " changed to: " + yA + "\nxB: "+ xB + " changed to: " + xA + " yB: " + yB + " changed to: " + yB);
					input[i][j] = mFuzzCube[xB][yA][z%256];
					input[i][j + 1] = mFuzzCube[xA][yB][z%256];
				}
				z ++;
			}
		}
		return input;
	}
	
	public void generateCube(Integer[][] vSlice, Set<Integer> keySet, Set<Integer> compSet){
		Integer[][] per = new Integer[256][256];
		Integer[][] firstPer = new Integer[256][256];
		for(int z = 0; z < 256; z ++){
			per = generatePermutation(keySet, compSet, z);
			if(z == 1)
				firstPer = per;
			for(int y = 0; y < 256; y ++){
				for(int x = 0; x < 256; x++){
					mFuzzCube[x][y][z] = vSlice[per[x][y]%256][per[x][y]/256];
				}
			}
		}
		
		//Printer.printIntArray(firstPer, 65536);
	}
	
	private Integer[][] fillV_Slice(Set<Integer> input){
		Integer[][] vSlice = new Integer[256][256];
		
		int x = 0;
		for(Integer iter : input){
			vSlice[x%256][x/256] = iter;
			x++;
		}
		
		return vSlice;
	}
	
	private Integer[][] padV_Slice(Integer[][] vSlice, Set<Integer> input){
		int x = 256 - input.size()%256;
		int y = 256 - (int)Math.ceil(input.size()/256F);
		
		for(Integer iter : input){
			vSlice[x%256][y] = iter;
			x++;
			if(x % 256 == 0)
				y ++;
		}
		
		return vSlice;
	}
	
	private Integer[][] generatePermutation(Set<Integer> keySet, Set<Integer> compSet, int z){
		keySet = shiftElements(keySet, z);
		Integer[][] permutation = new Integer[256][256];
		
		permutation = fillV_Slice(keySet);
		permutation = padV_Slice(permutation, compSet);
		
		return permutation;
	}
	
	/**
	 * Function c(~k), right shift the input elements by given distance.
	 * @param input
	 * @param distance
	 * @return output
	 */
	private Set<Integer> shiftElements(Set<Integer> input, Integer distance){
		Set<Integer> output = new LinkedHashSet<Integer>();
		
		int i = 0;
		for(Integer iter : input){
			if(i >= input.size() - distance % input.size())
				output.add(iter); // Copy the symbols at index i >= last - distance on the first positions.
			i++;
		}
		output.addAll(input); // Then add the old first elements after the now right-shifted ones.

		return output;
	}
	
	/**
	 * Function h(k), removes redundant input elements.
	 * @param intList
	 * @return intSet
	 */
	private Set<Integer> removeRedundantElements(Integer[] input){
		Set<Integer> output = new LinkedHashSet<Integer>();

		for(int i = 0; i < input.length; i++){
			output.add(input[i]);
		}
		
		return output;
	}

	/**
	 * 
	 * @param input
	 * @param number
	 * @return output
	 */
	private Set<Integer> getNcomplement(Set<Integer> input, Integer n){
		Set<Integer> output = new LinkedHashSet<Integer>();
		
		for(int i = 0; i <= n; i++){
			if(!input.contains(i))
				output.add(i);
		}

		return output;
	}
	
	/**
	 * Create a enumeration for the input and return it as an integer array.
	 * @param str
	 * @return output integer array
	 */
	private Integer[] getIntFromStr(String str){
		Integer[] output = new Integer[str.length()];
		
		for(int i = 0; i < str.length(); i ++){
			output[i] = (int)str.charAt(i);
		}
		
		return output;
	}
	
	public Integer[][][] getFuzzCube(){
		return mFuzzCube;
	}
}
