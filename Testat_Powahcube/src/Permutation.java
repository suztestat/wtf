import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Permutation {

	public int[][][] getPowahCube() {
		return powahCube;
	}

	private int[][][] powahCube = new int[256][256][256];

	private int[] k; //key
	
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	public int[] getK() {
		return k;
	}
	//1.3
	public Permutation(int[] key){

		this.k=key;
		powahCube = new int[256][256][256];
		
		Printer p = new Printer();
		
		//pi generieren
		int[][] omega=generateOmega(k);
//		int[] a2=generatePi(20, k);
//		try {
//			p.write("Pi",a2);
//			p.write("Omega",omega);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		//pi anwenden/create cube
		System.out.println("Creating cube...");
		for (int i=0;i<256;i++){
			//double perc=((i/255F)*100);
			//System.out.print(perc+"%");
			int[] currentPi=generatePi(i, k);
			for (int j=0;j<65536;j++){
				powahCube[j/256][j%256][i]=omega[currentPi[j]/256][currentPi[j]%256];
			}
		}
		
//		try {
//			p.write("omegaTilde", powahCube);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		System.out.println("Cube created.");
	}
	
	public int[][] generateOmega(int[] key){
		int[][] K = new int[256][256];

		int[] kTilde=h(key);

		//ungenutzte zeichen generieren
		int[] unusedNumbers=N(65535,kTilde);

		int [] kTildeFilled = new int [unusedNumbers.length+kTilde.length];
		if (kTildeFilled.length!=65536) System.out.println("failed");
		
		System.arraycopy(kTilde, 0, kTildeFilled, 0, kTilde.length);
		System.arraycopy(unusedNumbers, 0, kTildeFilled, kTilde.length, unusedNumbers.length);
		
		//eindimensionales [65536] array in 2 dimensionales [256][256] array umwandeln		
		for (int i=0;i<kTildeFilled.length;i++)
			K[i/256][i%256]=kTildeFilled[i];
				
		return K;
	}
	
	public int[] generatePi(int i, int[] key){
		int[] kTilde=h(key);
		
		int[]ci=c(kTilde,i);
		int[]n1=N(65535,kTilde);
		
		int[] Pi = new int[ci.length + n1.length];
		System.arraycopy(ci, 0, Pi, 0, ci.length);
		System.arraycopy(n1, 0, Pi, ci.length, n1.length);
		
		return Pi;
	}
	
	public int[] N(int laenge, int[] p){
		List<Integer> listContains = new ArrayList<Integer>();
	    for (int i=0; i<p.length; i++)
	    	if (!listContains.contains(p[i])) listContains.add(p[i]);
	    	    	    
		int [] res = new int[laenge-listContains.size()+1];
		
		int fillcount=0;
		for (int i=0; i<=laenge;i++)
			if (!listContains.contains(i)) 
				if (fillcount<=res.length-1 )res[fillcount++]=i; else System.out.println("Error?");

		return res;
	}
	
	//shiftArray
	public int[] c(int[] arr, int distance){
		distance=distance%arr.length; //um eine maximale verschiebung von arr.length zu durchlaufen
	
		for (int dist=1;dist<=distance;dist++){
			int temp = arr[arr.length-1];
			for (int i=arr.length-2; i>=0; i--)                
			    arr[i+1]=arr[i];
		
			arr[0] = temp;			
		}
		return arr;
	}
	
	//removeDuplicates
	public int[] h(int[] arr){
		List <Integer> temp = new ArrayList<Integer>();

		for (int i=0;i<arr.length;i++)
			if (!temp.contains(arr[i])) temp.add(arr[i]);
			
		int[] res = new int [temp.size()];
		int index=0;
		for (Integer x:temp) res[index++]=x;
		
		return res;
	}


	public int[][] getSlice(boolean horizontal, int index){
		int[][] output = new int[256][256];;
		if (horizontal){
			for (int i=0;i<powahCube.length;i++)
				for (int j=0;j<powahCube[i].length;j++)
					output[i][j]=powahCube[i][j][0];
		} else {
			for (int i=0;i<powahCube.length;i++)
				for (int j=0;j<powahCube[0][i].length;j++)
					output[i][j]=powahCube[j][0][i];
		}
		return output;
	}
}
