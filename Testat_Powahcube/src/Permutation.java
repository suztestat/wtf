import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Permutation {

	int[][][] powahCube;
	private int k; //key
	
	public Permutation(){
		powahCube = new int[256][256][256];
		//pi()
		int[] aa={6,6,4,7,9,6,3,3,4,6,6,1};
	
		int [] b=N(20,aa);
		
		for (int i=0;i<b.length;i++) System.out.print(b[i]+",");
	}
	
	public int[] N(int laenge, int[] p){
		
		List<Integer> listContains = new ArrayList();
	    for (int i = 0; i < p.length; i++)
	    	if (!listContains.contains(p[i])) listContains.add(p[i]);
	    
	    System.out.println("jo"+listContains);
	    if (listContains.contains(3)) System.out.println("Wtf is this shuit");
		int [] res = new int[laenge-listContains.size()];
		
		int fillcount=0;
		System.out.println("14="+res.length);
		
		for (int i=1; i<=laenge;i++){
			if (!listContains.contains(i)) {
				res[fillcount]=i+1;
				System.out.println("geaddet:"+(i+1));
			}
			fillcount++;
		}
		
		
		
		return null;
	}
	
	public int[] shiftArray(int[] arr, int distance){
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
	public int[] removeDuplicates(int[] arr){
		List <Integer> temp = new ArrayList();
		for (int i=0;i<arr.length;i++){
			if (!temp.contains(arr[i])) temp.add(arr[i]);
		}

		int[] res = new int [temp.size()];
		int index=0;
		for (Integer x:temp) res[index++]=x;
		
		return res;
	}
	
	public int h(int k){
		
		
		return 0;
	}
	
	public int pi(int i, int k){
		
		
		return k;
		
	}
}
