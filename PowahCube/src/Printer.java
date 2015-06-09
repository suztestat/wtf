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
	
	public static void printList(List inputList){
		for(int i = 0; i < inputList.size(); i ++){
			if(i % 32 == 0)
				System.out.println();
			System.out.print(inputList.get(i).toString() + "\t");
		}
	}
	
	public static void printIntArray(Integer[] mFuzzCube){
		System.out.println();
		for(int i = 0; i < mFuzzCube.length; i++)
			System.out.print(mFuzzCube[i] + "\t");
	}
	
	public static void printIntArray(Integer[][] input){
		System.out.println();
		for(int i = 0; i < input.length; i++){
			System.out.println();
			for(int j = 0; j < input[i].length; j++)
				System.out.print(input[i][j] + "\t");
		}
	}
	
	public static void printIntArray(Integer[][][] input){
		System.out.println("PRINT");
		for(int i = 0; i < input.length; i++){
			System.out.println();
			for(int j = 0; j < input[i].length; j++)
					System.out.print(input[i][j][0] + "\t"); //Nur als Test
		}
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
	
	public static void toString(Integer[] input){
		for(int i = 0; i < input.length; i++){
			System.out.print(input[i].toString());
		}
	}
}
