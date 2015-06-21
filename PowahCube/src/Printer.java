import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class Printer {
	
	public static void printOperation(String operation, Object input, Object output){
		System.out.println("\nOperation: " + operation + "\nInput: " + input +
				"\nOutput: " + output);
	}
	
	public static void printHeader(String header){
		System.out.println("\n\n" + header);
	}
	
	public static void printStep(String step, Object input){
		System.out.println(step + ": " + input);
	}
	
	public static void printList(String name, List inputList){
		System.out.print("\n" + name + ":");
		for(int i = 0; i < inputList.size(); i ++){
			if(i % 64 == 0)
				System.out.println();
			System.out.print(inputList.get(i).toString() + "\t");
		}
		System.out.println();
	}
	
	public static void printLinkedHashSet(String name, Set<Integer> inputSet){
		System.out.print("\n" + name + ":");
		int i = 0;
		for(Integer iter : inputSet){
			if(i % 64 == 0)
				System.out.println();
			System.out.print(iter + "\t");
			i++;
		}
		System.out.println();
	}
	
	public static void printRuntime(Long start){
		Long end = System.nanoTime();
		System.out.println("Runtime: " + (end - start) + " nanoseconds");
	}
	
	public static void printIntArray(Integer[] mFuzzCube){
		System.out.println();
		for(int i = 0; i < mFuzzCube.length; i++)
			System.out.print(mFuzzCube[i] + "\t");
	}
	
	public static void printIntArray(Integer[][] input, Integer count){
		
		int iter = 0; 
		for(int i = 0 ; i < input[0].length; i++){
			if(iter < count){
				System.out.println();
				for(int j = 0 ; j < input.length; j++){
					if(iter < count)
						System.out.print(input[j][i] + "\t");
					iter ++;
				}
			}
		}
			
		System.out.println();
	}
	
	public static void checkCube(Integer[][][] cube){
		Set<Integer> checkSet = new HashSet<Integer>();
		boolean duplicate = false;
		for(int i = 0 ; i < 256; i++){
			checkSet = new HashSet<Integer>();
			for(int j = 0; j < 256; j ++){
				for(int k = 0; k < 256; k++){
					if(checkSet.add(cube[k][j][i]) == false){
						System.out.println("Error: Duplicate entry: " + cube[k][j][i]);
						System.out.println("Error: At permutation z: " + i);
						duplicate = true;
					}
					else
						duplicate = false;
				}
			}
			if(duplicate == false)
				System.out.println("Permutation z: " + i + " is correct");
			for(int z = 0; z < 65536; z ++){
				if(!checkSet.contains(z))
					System.out.println("Set doesn't contain: " + z + " at z: " + i);
			}
		}
	}
	
	public static void printIntArray(Integer[][][] input, int zIndex){
		System.out.println("PRINT");
		System.out.println();
		for(int j = 0; j < input[0].length; j ++){
			System.out.println();
			for(int k = 0 ; k < input.length; k ++)
				System.out.print(input[k][j][zIndex] + "\t");
		}
		System.out.println();
	}
	
	public static void printIntArrToStr(Integer[] input){
		String output = "";
		for(int i = 0; i < input.length; i ++)
			output += (char)input[i].intValue();
		
		System.out.println(output);
	}
	
	public static void printIntListToStr(List<Integer> input){
		String output = "";
		for(Integer iter: input){
			output += (char)iter.intValue();
		}
		System.out.println(output);
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
