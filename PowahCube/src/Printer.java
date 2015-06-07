import java.util.List;


public class Printer {
	
	public static void printOperation(String operation, Object input, Object output){
		System.out.println("\nOperation: " + operation + "\nInput: " + input +
				"\nOutput: " + output);
	}
	
	public static void printHeader(String header){
		System.out.println("\n" + header);
	}
	
	public static void printStep(String step, Object input){
		System.out.println(step + ": " + input);
	}
	
	public static void printList(String name, List inputList){
		for(int i = 0; i < inputList.size(); i ++)
			System.out.println(name + " " + i + ": " + inputList.get(i).toString());
	}
	
	public static void printInHex(String input){
		for(int i = 0; i < input.length(); i ++){
			if(i%56 == 0)
				System.out.print("\n \\u" + Integer.toHexString(input.charAt(i) | 0x10000).substring(1));
			else
				System.out.print(" \\u" + Integer.toHexString(input.charAt(i) | 0x10000).substring(1));
		}
		System.out.println();
	}
}
