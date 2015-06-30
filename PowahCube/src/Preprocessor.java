import java.util.ArrayList;
import java.util.List;

/**
 * This class processes the input strings/arrays to 512 UI long blocks.
 * It also has a implemented method to reverse the preprocessing.
 * @author Erich Dyck
 *
 */
public class Preprocessor {
	
	/**
	 * Preprocess scheduler
	 * 1. Remove twins
	 * 2. Pad terminator symbol
	 * 3. Pad to a multiple of 512 UI
	 * 4. Return an integer array with 512 UI long blocks.
	 * 
	 * This project works with integers from the very beginning to make
	 * everything consistent.
	 * @param str
	 * @return preprocessed integer array.
	 */
	public Integer[][] preprocess(String str){
		Integer[] intArr = getIntFromStr(str);
		List<Integer> preList = removeTwins(intArr);
		preList = padTerminator(preList);
		preList = padMultipleUI(preList);
		return getBlocks(preList);
	}
	
	/**
	 * Preprocess scheduler
	 * 1. Remove twins
	 * 2. Pad terminator symbol
	 * 3. Pad to a multiple of 512 UI
	 * 4. Return an integer array with 512 UI long blocks.
	 * 
	 * This project works with integers from the very beginning to make
	 * everything consistent.
	 * @param intArr
	 * @return preprocessed integer array.
	 */
	public Integer[][] preprocess(Integer[] intArr) throws IllegalArgumentException{
		if(!checkElements(intArr))
			throw new IllegalArgumentException("The input contains illegal elements");
		
		List<Integer> preList = removeTwins(intArr);
		preList = padTerminator(preList);
		preList = padMultipleUI(preList);
		return getBlocks(preList);
	}

	/**
	 * First step of the preprocessing. Seperate twins with a fill symbol "pf".
	 * pf = 0x0378 (not defined in unicode)
	 * @param intArr
	 * @return remList
	 */
	private List<Integer> removeTwins(Integer[] intArr){
		List<Integer> remList = new ArrayList<Integer>();
		
		for(int i = 0; i < intArr.length - 1; i += 2){
			remList.add(intArr[i]);
			if(intArr[i] == intArr[i+1]){
				remList.add((int)'\u0378');
				remList.add((int)'\u0378');
			}
			remList.add(intArr[i+1]);
		}
		if(intArr.length % 2 == 1)
			remList.add(intArr[intArr.length - 1]);
		
		return remList;
	}
	
	/**
	 * Second step of the preprocessing. Pad a terminator symbol "pt" to the input if the
	 * size is an even number.
	 * If it isn't pad the fill symbol "pf" and then the terminator symbol "pt"
	 * pf = 0x0378 (not defined in unicode)
	 * pt = 0x0379 (not defined in unicode)
	 * @param intList
	 * @return padded intList
	 */
	private List<Integer> padTerminator(List<Integer> intList){
		if(intList.size() % 2 == 0){
			intList.add((int)'\u0378');
			intList.add((int)'\u0379');
		}
		else
			intList.add((int)'\u0379');
		
		return intList;
	}
	
	/**
	 * Third step of the preprocessing. Pad the input to a multiple of 512UI.
	 * @param intList list
	 * @return padded intList
	 */
	private List<Integer> padMultipleUI(List<Integer> intList){
		Integer lastSymb = intList.get(intList.size() - 1);

		int padSymb = 0, i = 0;
		while(intList.size() % 512 != 0){
			padSymb = lastSymb + i % 65536;
			if(padSymb == (int)'\u0379'){
				i++;
				padSymb = lastSymb + i % 65536;
			}
			intList.add(padSymb);
			i++;
		}
		
		return intList;
	}
	
	/**
	 * Fourth step of the preprocessing. Split the input into 512 UI long blocks and return
	 * them in an integer array.
	 * @param intList
	 * @return intArr
	 */
	private Integer[][] getBlocks(List<Integer> intList){
		Integer[][] intArr = new Integer[(int)Math.ceil(intList.size()/512.0F)][];
		
		int k = 0;
		for(int i = 0 ; i < intArr.length; i ++){
			if(intList.size() - i >= 512)
				intArr[i] = new Integer[512];
			else
				intArr[i] = new Integer[intList.size() - i]; //Should never happens
			for(int j = 0; j < intArr[i].length ; j ++){
				intArr[i][j] = intList.get(k);
				k ++;
			}
		}

		return intArr;
	}
	
	/**
	 * Reverse the preprocessing steps above.
	 * Iterate through the input array and save the elements in an output array, except the
	 * pf symbols, till the terminator symbol is reached.
	 * Return the result as an Integer array.
	 * @param input
	 * @return output
	 */
	public Integer[][] reversePreToInt(Integer[][] input){
		Integer[][] output = new Integer[input.length][512];
		boolean reachedTerm = false;
		for(int i = 0; i < input.length; i ++){
			for(int j = 0; j < input[i].length; j++){
				if(input[i][j].equals((int)'\u0379')){
					reachedTerm = true;
					break;
				}
				if(!input[i][j].equals((int)'\u0378'))
					output[i][j] = input[i][j];
			}
			if(reachedTerm == true)
				break;
		}
		
		return output;
	}
	
	/**
	 * Like reversePreToInt(Integer[][] input) with the difference 
	 * that it returns a string.
	 * @param input
	 * @return
	 */
	public String reversePreToStr(Integer[][] input){
		String output = "";
		boolean reachedTerm = false;
		
		for(int i = 0 ; i < input.length; i ++){
			for(int j = 0; j < input[i].length; j++){
				if(input[i][j].equals((int)'\u0379')){
					reachedTerm = true;
					break;
				}
				if(!input[i][j].equals((int)'\u0378'))
					output += (char)input[i][j].intValue();
			}
			if(reachedTerm == true)
				break;
		}
		
		return output;
	}
	
	/**
	 * This method checks the input elements whether they are >0 and <65536.
	 * This implementation of PowahCube doesn't accept such inputs, there is nothing explicit
	 * said in the tasks, whether it should and only modulo them in the range or not.
	 * @param key
	 * @return result
	 */
	private boolean checkElements(Integer[] input){
		for(int i = 0; i < input.length; i++){
			if(input[i] > 65535 || input[i] < 0)
				return false;
		}
		return true;
	}
	
	/**
	 * Create a enumeration for the input and return it as an integer array.
	 * @param str
	 * @return intArr
	 */
	private Integer[] getIntFromStr(String str){
		Integer[] intArr = new Integer[str.length()];
		
		for(int i = 0; i < str.length(); i ++){
			intArr[i] = (int)str.charAt(i);
		}
		
		return intArr;
	}
}
