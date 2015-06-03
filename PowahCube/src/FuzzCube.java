
public class FuzzCube {
	private char[][][] mFuzzCube;
	
	public FuzzCube(String key){
		mFuzzCube = new char[256][256][256];
		
		//TEST
		key = removeRedundantElements(key);
		shiftElements(key, 2);
	}
	
	//Function h(k), removes redundant elements from the input.
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
	
	//Function c(~k), right shift the elements by given distance.
	public String shiftElements(String input, Integer distance){
		String output = "";
		for(int i = 0; i < input.length(); i++){
			output += input.substring((input.length() - distance + i)%input.length(), 
					(input.length() - distance + i)%input.length() + 1);
		}
		
		Printer.printOperation("Shift Elements", input, output);
		return output;
	}
}
