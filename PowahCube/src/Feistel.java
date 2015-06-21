
public class Feistel {
	
	public Integer[][] encrypt(Integer[][] fuzzed, Integer[][] roundKeys){
		Integer[][] splittedFuzzed = new Integer[2][256];
		Integer[] roundTemp = new Integer[256];
		Integer[][] encrypted = new Integer[fuzzed.length][512];
		
		for(int i = 0; i < fuzzed.length; i++){
			splittedFuzzed = splitFuzzed(fuzzed[i]);
			for(int j = 0 ; j < 64; j++){
				roundTemp = splittedFuzzed[1];
				splittedFuzzed[1] = xorInput(splittedFuzzed[1], roundKeys[j]);
				splittedFuzzed[1] = xorInput(splittedFuzzed[0], splittedFuzzed[1]);
				splittedFuzzed[0] = roundTemp;
			}
			encrypted[i] = concatBlocks(splittedFuzzed[1], splittedFuzzed[0]);
		}
		return encrypted;
	}
	
	public Integer[] xorInput(Integer[] blockA, Integer[] blockB){
		Integer[] computed = new Integer[256];
		for(int i = 0; i < 256; i++)
			computed[i] = blockA[i] ^ blockB[i];
		
		return computed;
	}
	
	public Integer[][] splitFuzzed(Integer[] fuzzed){
		Integer[][] splitted = new Integer[2][256];
		for(int i = 0; i < 512; i++){
			if(i < 256)
				splitted[0][i] = fuzzed[i];
			else
				splitted[1][i%256] = fuzzed[i];
		}
		return splitted;
	}
	
	public Integer[] concatBlocks(Integer[] blockA, Integer[] blockB){
		Integer[] concated = new Integer[512];
		
		for(int i = 0 ; i < 512; i++){
			if(i < 256)
				concated[i] = blockA[i];
			else
				concated[i] = blockB[i%256];
		}
		
		return concated;
	}
	
	public Integer[][] scheduleRoundKeys(Integer[][][] fuzzCube){
		Integer[][] roundKeys = new Integer[64][256];
		
		for(int i = 0; i < 64; i++)
			for(int j = 0; j < 256; j ++){
				roundKeys[i][j] = fuzzCube[i*4][i*4][j];
			}
		
		return roundKeys;
	}
}
