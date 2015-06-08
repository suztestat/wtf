import java.util.ArrayList;
import java.util.List;


public class FuzzCube {
	private int[][][] mFuzzCube;
	
	public FuzzCube(String key){
		mFuzzCube = new int[256][256][256];
		String test = "asldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshg";
		//TEST
		key = removeRedundantElements(key);
		shiftElements(key, 3);
		List<String> testList = splitSlices(test);
		fillVSlice(testList, 0);
	}
	
	public List<Integer> getIntegerFromInput(String input){
		List<Integer> inputList = new ArrayList();
		
		for(int i = 0; i < input.length(); i ++){
			inputList.add((int)input.charAt(i));
		}
		
		return inputList;
	}
	
	/**
	 * Function h(k), removes redundant elements from the input.
	 * @param input
	 * @return output
	 */
	public String removeRedundantElements(String input){
		CharSequence iterChar;
		String output = "";
		
		for(int i = 0; i < input.length(); i++){
			//Look whether the output already contains the iterChar.
			iterChar = input.subSequence(i, i + 1);
			if(!output.contains(iterChar))
				//If not, append it.
				output += iterChar;
		}
		
		Printer.printOperation("Remove redundant Elements", input, output);
		return output;
	}
	
	/**
	 * Function c(~k), right shift the elements by given distance.
	 * @param input
	 * @param distance
	 * @return output
	 */
	public String shiftElements(String input, Integer distance){
		String output = "";
		for(int i = 0; i < input.length(); i++){
			output += input.substring((input.length() - distance + i)%input.length(), 
					(input.length() - distance + i)%input.length() + 1);
		}
		
		Printer.printOperation("Shift Elements", input, output);
		return output;
	}
	
	/**
	 * 
	 * @param input
	 * @param number
	 * @return output
	 */
	public String fillComplement(String input, Integer number){
		String output = "";
		for(int i = 0; i < number; i++){
			if(!input.contains("" + (char)i))
				output += (char)i;
		}
		return output;
	}
	
	public List<String> splitSlices(String input){
		List<String> outputList = new ArrayList();
		
		Printer.printHeader("Split Slices");
		Printer.printStep("Input", input);
		for(int i = 0; i < input.length(); i += 256){
			if(input.length() > i+256)
				outputList.add(input.substring(i, i+256));
			else
				outputList.add(input.substring(i, input.length()));
		}
		
		Printer.printList("Output", outputList);
		return outputList;
	}
	
	/**
	 * 
	 * @param inputList
	 * @param zIndex
	 */
	public void fillVSlice(List<String> inputList, Integer zIndex){
		List<Integer> iterList = new ArrayList();
		
		for(int i = 0; i < inputList.size(); i ++){
			iterList = getIntegerFromInput(inputList.get(i));
			for(int j = 0; j < iterList.size(); j ++){
				mFuzzCube[i][j][zIndex] = iterList.get(j);
			}
		}

		//TODO AUSGABE
	}
}
