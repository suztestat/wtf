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
}
