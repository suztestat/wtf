
public class Permutation {

	int[][][] powahCube;
	private int k; //key
	//wtf
	
	public Permutation(){
		powahCube = new int[256][256][256];
		//pi()
		int[] aa={1,2,3,4,5,6,7,8,9};
		System.out.println(shiftArray(2,aa));
		
		for (int i=0;i<aa.length;i++) System.out.println(aa[i]);
	}
	
	public int N(int laenge, int p){
		
		
		return p;
		
	}
	
	public int[] shiftArray(int distance, int[] arr){
		distance=distance%arr.length; //um eine maximale verschiebung von arr.length zu durchlaufen
	
		for (int dist=1;dist<=distance;dist++){
			int temp = arr[arr.length-1];
			for (int i=arr.length-2; i>=0; i--)                
			    arr[i+1]=arr[i];
		
			arr[0] = temp;			
		}
		return arr;
	}
	
	public int h(int k){
		
		
		return 0;
	}
	
	public int pi(int i, int k){
		
		
		return k;
		
	}
}
