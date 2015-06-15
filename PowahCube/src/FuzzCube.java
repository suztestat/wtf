import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class FuzzCube {
	private Integer[][][] mFuzzCube;
	
	public FuzzCube(){
		mFuzzCube = new Integer[256][256][256];
	}
	
	public void generateOmega(String key){
		Integer[] keyArr = getIntFromStr(key);
		Set<Integer> keySet = removeRedundantElements(keyArr);
		Integer[][] splittedKey = splitKey(keySet);
		Set<Integer> compSet = getNcomplement(keySet, 65535);
		for(int i = 0; i < 1; i ++){
			fillV_Slice(splittedKey, i);
			padV_Slice(compSet, i);
		}
	}	
	public void generateOmega(Integer[] key){
		Set<Integer> keySet = removeRedundantElements(key);
		Integer[][] splittedKey = splitKey(keySet);
		Set<Integer> compSet = getNcomplement(keySet, 65535);
		for(int i = 0; i < 1; i ++){
			fillV_Slice(splittedKey, i);
			padV_Slice(compSet, i);
		}
	}
	
	public void permuteCube(String key){
		Integer[] keyArr = getIntFromStr(key);
		Set<Integer> keySet = removeRedundantElements(keyArr);
		Set<Integer> compSet = getNcomplement(keySet, 65535);

		for(int z = 0; z < 2; z ++){
			permuteV_Slice(keySet, compSet, z);
		}
	}	
	public void permuteCube(Integer[] key){
		Set<Integer> keySet = removeRedundantElements(key);
		Set<Integer> compSet = getNcomplement(keySet, 65535);

		for(int z = 0; z < 2; z ++){
			permuteV_Slice(keySet, compSet, z);
		}
	}
	
	private void permuteV_Slice(Set<Integer> keySet, Set<Integer> compSet, int z){
		keySet = shiftElements(keySet, z);
		Integer[][] splittedKey = splitKey(keySet);
		
		fillV_Slice(splittedKey, z);
		padV_Slice(compSet, z);
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
	 * "[..]sequentiell in Teilstücke Ki der Länge 256 gesplittet, das letzte Stück kann jedoch weniger als 256
	 *	Elemente haben." That's why the splitArr is created in (y,x) dimensions and the x-axis
	 *	is initialized dynamically.
	 * @param input
	 * @return output
	 */
	private Integer[][] splitKey(Set<Integer> intSet){
		Integer[][] splitArr = new Integer[(int)Math.ceil(intSet.size()/256.0)][];
		
		int i = 0, j = -1;
		for(Integer iter : intSet){
			if(i % 256 == 0){
				j ++;
				if(intSet.size() - i >= 256)
					splitArr[j] = new Integer[256];
				else
					splitArr[j] = new Integer[intSet.size() - i];
			}
			splitArr[j][i%256] = iter;
			i ++;
		}
		
		return splitArr;
	}
	
	/**
	 * "Bei einem V-Slice sv,i handelt es sich um eine vertikale Scheibe aus Omega d.h.
	 *	es gilt sv,i(x,y) = Omega(x,y,i) [..]"
	 *	The following methods sticks to the definition that the cubes dimensions are [x][y][z].
	 * @param input
	 * @param zIndex
	 */
	private void fillV_Slice(Integer[][] input, int z){
		for(int y = 0; y < input.length; y ++)
			for(int x = 0; x < input[y].length; x++)
				mFuzzCube[x][y][z] = input[y][x];
	}
	
	private void padV_Slice(Set<Integer> input, int z){
		int x = 256 - input.size()%256;
		int y = (int)Math.ceil((65536.0 - input.size())/256.0) - 1;
		
		for(Integer iter : input){
			mFuzzCube[x%256][y][z] = iter;
			x++;
			if(x%256 == 0)
				y++;
		}
		
		Printer.printIntArray(mFuzzCube, z);
	}
	
}
