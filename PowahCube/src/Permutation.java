
public class Permutation {
	
	//Function h(k), removes redundant Elements from the input.
	public static String removeRedundantElements(String input){
		CharSequence iterChar;
		String output = "";
		
		for(int i = 0; i < input.length(); i++){
			//Look whether the output already contains the iterChar.
			iterChar = input.subSequence(i, i + 1);
			if(!output.contains(iterChar))
				output += iterChar;
		}
		
		Printer.printOperation("Remove redundant Elements", input, output);
		return output;
	}
}
