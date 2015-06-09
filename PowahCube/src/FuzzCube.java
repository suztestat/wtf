import java.util.ArrayList;
import java.util.List;


public class FuzzCube {
	private Integer[][][] mFuzzCube;
	
	public FuzzCube(String key){
		mFuzzCube = new Integer[256][256][256];
		String test = "asldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshgasldkghdsalkghdsalkhglkdsahgdslkghlkdshg";
		//TEST
		List<Integer> testInt = getIntegerFromInput(key);
		testInt = removeRedundantElements(testInt);
		testInt = shiftElements(testInt, 3);
		List<Integer> complements = fillComplement(testInt, 15);
		Integer[][] testSplit = splitSlices(testInt);
		fillVSlice(testSplit, 0);
	}
	
	/**
	 * Create a enumeration for the input and return it as an arraylist.
	 * @param input
	 * @return output
	 */
	public List<Integer> getIntegerFromInput(String input){
		List<Integer> output = new ArrayList();
		
		for(int i = 0; i < input.length(); i ++){
			output.add((int)input.charAt(i));
		}
		
		return output;
	}
	
	/**
	 * Function h(k), removes redundant elements from the input.
	 * @param input
	 * @return output
	 */
	public List<Integer> removeRedundantElements(List<Integer> input){
		List<Integer> output = new ArrayList();
		
		Printer.printHeader("Remove redundant elements");
		
		for(int i = 0; i < input.size(); i++){
			//Look whether the output already contains the iterChar.
			if(!output.contains(input.get(i)))
				//If not, append it.
				output.add(input.get(i));
		}
		
		Printer.printList(output);
		return output;
	}
	
	/**
	 * Function c(~k), right shift the elements by given distance.
	 * @param input
	 * @param distance
	 * @return output
	 */
	public List<Integer> shiftElements(List<Integer> input, Integer distance){
		List<Integer> output = new ArrayList();
		
		Printer.printHeader("Shift Elements");
		
		for(int i = 0; i < input.size(); i++){
			output.add(input.get((input.size()-distance+i)%input.size()));
		}
		
		Printer.printList(output);
		return output;
	}
	
	/**
	 * 
	 * @param input
	 * @param number
	 * @return output
	 */
	public List<Integer> fillComplement(List<Integer> input, Integer number){
		List<Integer> output = new ArrayList();
		
		Printer.printHeader("Fill Complement");
		
		for(int i = 0; i < number; i++){
			if(!input.contains(i))
				output.add(i);
		}
		
		Printer.printList(output);
		return output;
	}
	
	/**
	 * 
	 * @param input
	 * @return output
	 */
	public Integer[][] splitSlices(List<Integer> input){
		Integer[][] output = new Integer[(int)(input.size()/256.0F) + 1][256]; //TODO
		
		Printer.printHeader("Split Slices");
		
		int i = 0;
		for(int j = 0; j < input.size(); j++){
			if(j % 256 == 0 && j > 0){
				i ++;
			}
			output[i][j%256] = input.get(j);
		}
		
		Printer.printIntArray(output);
		return output;
	}
	
	/**
	 * 
	 * @param input
	 * @param zIndex
	 */
	public void fillVSlice(Integer[][] input, Integer zIndex){
		
		for(int i = 0; i < input.length; i ++){
			for(int j = 0; j < input[i].length; j++)
				mFuzzCube[i][j][zIndex] = input[i][j];
		}

		Printer.printIntArray(mFuzzCube);
	}
}
