import java.util.ArrayList;
import java.util.List;


public class Preprocessing {

	/**
	 * First step of the preprocessing. Seperate twins with a fill symbol "pf".
	 * pf = 0378 (ndef in unicode)
	 * @param input
	 * @return output
	 */
	public static String removeTwins(String input){
		String output = "";
		for(int i = 1; i < input.length(); i ++){
			output += input.charAt(i - 1);
			if(input.charAt(i - 1) == input.charAt(i)){
				output += "\u0378\u0378";
			}
		}
		output += input.charAt(input.length() - 1);
		Printer.printOperation("Preprocessing", input, output);
		return output;
	}
	
	/**
	 * Second step of the preprocessing. Pad a terminator symbol "pt" to the input if the
	 * length is an even number.
	 * If it isn't pad the fill symbol "pf" and then the terminator symbol "pt"
	 * pf = 0378 (ndef in unicode)
	 * pt = 0379 (ndef in unicode)
	 * @param input
	 * @return input
	 */
	public static String padTerminator(String input){
		Printer.printHeader("Pad terminator");
		Printer.printStep("Input", input);
		if(input.length() % 2 == 0)
			input += "\u0378\u0379";
		else
			input += '\u0379';
		
		Printer.printStep("Output", input);
		return input;
	}
	
	/**
	 * Third step of the preprocessing. Pad the input to a multiple of 512UI.
	 * The output symbols won't be printed correct in the console. Check the
	 * hex or integer values for testing.
	 * @param input
	 * @return input
	 */
	public static String padMultipleUI(String input){
		char lastSymb = input.charAt(input.length() - 1);
		int i = 0;
		
		Printer.printHeader("Pad multiple UI");
		Printer.printStep("Input", input);
		char padSymb = '\0';
		while(input.length() % 512 != 0){
			padSymb = (char)(lastSymb + i % 65536);
			if(padSymb == '\u0379'){
				i++;
				padSymb = (char)( lastSymb + i % 65536);
			}
			input += padSymb;
			i++;
		}
		
		Printer.printStep("Output", input);
		Printer.printInHex(input);
		Printer.printStep("Length", input.length());
		return input;
	}
	
	/**
	 * Fourth step of the preprocessing. Split the input into 512 UI strings and return
	 * them in a list.
	 * @param input
	 * @return subList
	 */
	public static List<String> getSubstringList(String input){
		List<String> subList = new ArrayList();
		
		Printer.printHeader("Build substrings");
		for(int i = 0; i < input.length(); i += 512){
			subList.add(input.substring(i, i+512));
		}
		
		Printer.printList("Block", subList);
		return subList;
	}
}
