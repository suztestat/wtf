
/**
 * This class contains a implemented feistel net and a scheduler for the round keys.
 * @author Erich Dyck
 *
 */
public class Feistel {
	
	/**
	 * The feistel net implementation.
	 * Split the input for each iteration in two blocks.
	 * Save the right block in a temp array, then XOR the right block with the
	 * round key which is associated for this round.
	 * XOR the left block with the resulting block from the step above and save it as the new
	 * right block.
	 * Now save the old right (temp) block in the new left block.
	 * Iterate 64 rounds and finish it with a concatenation of the right block
	 * and the left block.
	 * Repeat this steps with all input substrings.
	 * @param input
	 * @param roundKeys
	 * @return result
	 */
	public Integer[][] run(Integer[][] input, Integer[][] roundKeys){
		Integer[][] splittedInput = new Integer[2][256];
		Integer[] roundTemp = new Integer[256];
		Integer[][] result = new Integer[input.length][512];
		
		for(int i = 0; i < input.length; i++){
			splittedInput = splitInput(input[i]);
			for(int j = 0 ; j < 64; j++){
				roundTemp = splittedInput[1]; //Save the old right block in a temp array.
				splittedInput[1] = xorInput(splittedInput[1], roundKeys[j]);
				splittedInput[1] = xorInput(splittedInput[0], splittedInput[1]);
				splittedInput[0] = roundTemp; //Make the old right block to the new left one.
			}
			result[i] = concatBlocks(splittedInput[1], splittedInput[0]);
		}
		return result;
	}
	
	/**
	 * XOR the array blocks and return them as an array.
	 * @param blockA
	 * @param blockB
	 * @return xored
	 */
	private Integer[] xorInput(Integer[] blockA, Integer[] blockB){
		Integer[] xored = new Integer[256];
		for(int i = 0; i < 256; i++)
			xored[i] = blockA[i] ^ blockB[i];
		
		return xored;
	}
	
	/**
	 * Split the input array to 256UI long blocks.
	 * @param input
	 * @return splitted
	 */
	private Integer[][] splitInput(Integer[] input){
		Integer[][] splitted = new Integer[2][256];
		for(int i = 0; i < 512; i++){
			if(i < 256)
				splitted[0][i] = input[i];
			else
				splitted[1][i%256] = input[i];
		}
		return splitted;
	}
	
	/**
	 * Concatenate the input blocks into one array.
	 * @param blockA
	 * @param blockB
	 * @return concatenated
	 */
	private Integer[] concatBlocks(Integer[] blockA, Integer[] blockB){
		Integer[] concatenated = new Integer[512];
		
		for(int i = 0 ; i < 512; i++){
			if(i < 256)
				concatenated[i] = blockA[i];
			else
				concatenated[i] = blockB[i%256];
		}
		
		return concatenated;
	}
	
	/**
	 * Schedule the round keys with the fuzzCube.
	 * On each iteration go 4 hSlices and 4 columns forward and iterate through the z coordinate.
	 * @param fuzzCube
	 * @return roundKeys
	 */
	public Integer[][] scheduleRoundKeys(Integer[][][] fuzzCube){
		Integer[][] roundKeys = new Integer[64][256];
		
		for(int i = 0; i < 64; i++)
			for(int j = 0; j < 256; j ++){
				roundKeys[i][j] = fuzzCube[i*4][i*4][j];
			}
		
		return roundKeys;
	}
}
