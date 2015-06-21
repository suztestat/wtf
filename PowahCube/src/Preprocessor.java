import java.util.ArrayList;
import java.util.List;

/**
 * This class processes the input strings/arrays to 512 UI long blocks.
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
	 * This class works with integers from the beginning to make
	 * the whole project consistent.
	 * @param str
	 * @return preprocessed integer array.
	 */
	public Integer[][] preprocess(String str){
		Integer[] inArr = getIntFromStr(str);
		List<Integer> inList = removeTwins(inArr);
		inList = padTerminator(inList);
		inList = padMultipleUI(inList);
		return getBlocks(inList);
	}
	
	/**
	 * An overloaded preprocess scheduler like above, with the difference, that this
	 * one accepts integer array inputs.
	 * @param input integer array
	 * @return preprocessed integer array.
	 */
	public Integer[][] preprocess(Integer[] input){
		List<Integer> inList = removeTwins(input);
		inList = padTerminator(inList);
		inList = padMultipleUI(inList);
		return getBlocks(inList);
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
	 * First step of the preprocessing. Seperate twins with a fill symbol "pf".
	 * pf = 0x0378 (ndef in unicode)
	 * @param input integer array
	 * @return output array list
	 */
	public List<Integer> removeTwins(Integer[] input){
		List<Integer> output = new ArrayList<Integer>();
		
		for(int i = 0; i < input.length - 1; i += 2){
			output.add(input[i]);
			if(input[i] == input[i+1]){
				output.add((int)'\u0378');
				output.add((int)'\u0378');
			}
			output.add(input[i+1]);
		}
		if(input.length % 2 == 1)
			output.add(input[input.length - 1]);
		//Printer.printIntArrToStr(input);
		//Printer.printIntListToStr(output);
		
		return output;
	}
	
	/**
	 * Second step of the preprocessing. Pad a terminator symbol "pt" to the input if the
	 * size is an even number.
	 * If it isn't pad the fill symbol "pf" and then the terminator symbol "pt"
	 * pf = 0x0378 (ndef in unicode)
	 * pt = 0x0379 (ndef in unicode)
	 * @param input list
	 * @return padded input list
	 */
	public List<Integer> padTerminator(List<Integer> input){
		//Printer.printHeader("Pad terminator");
		//Printer.printStep("Input", input);
		if(input.size() % 2 == 0){
			input.add((int)'\u0378');
			input.add((int)'\u0379');
		}
		else
			input.add((int)'\u0379');
		
		//Printer.printIntListToStr(input);
		return input;
	}
	
	/**
	 * Third step of the preprocessing. Pad the input to a multiple of 512UI.
	 * @param input list
	 * @return padded input list
	 */
	public List<Integer> padMultipleUI(List<Integer> input){
		Integer lastSymb = input.get(input.size() - 1);

		int padSymb = 0, i = 0;
		while(input.size() % 512 != 0){
			padSymb = lastSymb + i % 65536;
			if(padSymb == (int)'\u0379'){
				i++;
				padSymb = lastSymb + i % 65536;
			}
			input.add(padSymb);
			i++;
		}
		
		//Printer.printIntListToStr(input);
		//System.out.println(input.size());
		return input;
	}
	
	/**
	 * Fourth step of the preprocessing. Split the input into 512 UI long blocks and return
	 * them in an integer array.
	 * @param input list
	 * @return output integer array
	 */
	public Integer[][] getBlocks(List<Integer> input){
		Integer[][] output = new Integer[(int)Math.ceil(input.size()/512.0F)][];
		
		int k = 0;
		for(int i = 0 ; i < output.length; i ++){
			if(input.size() - i >= 512)
				output[i] = new Integer[512];
			else
				output[i] = new Integer[input.size() - i]; //Should never happens
			for(int j = 0; j < output[i].length ; j ++){
				output[i][j] = input.get(k);
				k ++;
			}
		}

		return output;
	}
	
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
}
