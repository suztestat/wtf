import java.util.ArrayList;
import java.util.List;

public class Permutation {

	int[][][] powahCube;
	private int[] k; //key
	
	//1.3
	public Permutation(){
		powahCube = new int[256][256][256];
		
		int[] k={6,6,4,7,9,6,3,3,4,6,6,1};
		System.out.println("----");
		//pi
		for (int i=0;i<=20;i++){
			//pi=
			int[] keyWOutDupl=h(k); 
			
			//zur ausgabe
			int[]f=c(keyWOutDupl,i);
			int[]g=N(20,keyWOutDupl);
			
			for (int i1=0;i1<f.length;i1++) System.out.print(f[i1]+",");
			for (int i2=0;i2<g.length;i2++) System.out.print(g[i2]+",");
			
			System.out.println("");
			//System.out.println(c(keyWOutDupl,i)+" "+N(65535,keyWOutDupl));
		}
	}
	
	public int[] N(int laenge, int[] p){
		List<Integer> listContains = new ArrayList<Integer>();
	    for (int i = 0; i < p.length; i++)
	    	if (!listContains.contains(p[i])) listContains.add(p[i]);
		int [] res = new int[laenge-listContains.size()+1];
		
		int fillcount=0;
		for (int i=0; i<=laenge;i++)
			if (!listContains.contains(i)) 
				res[fillcount++]=i;

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
	
	//scheiﬂ performance
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
		
	public int pi(int i, int k){
		
		
		return k;
		
	}
}
