import java.util.ArrayList;

public class Preprocessing {

	//kommentar!!
	private String input;
	private final char paddingSymbol='~';
	private final char terminatorSymbol='#';
	
	public Preprocessing(String input){
		this.input=input;
		fillSymbols(input);
	}
	
	
	public String[]  fillSymbols(String in){
		
		//4.1
		ArrayList<Integer> doubleSymbolPositions = new ArrayList<Integer>();
		
		//positionen von doppelten zeichen zwischen 2er Tupeln finden
		for (int i=1;i<input.length()-1;i+=2)
			if (input.charAt(i)==input.charAt(i+1))
				doubleSymbolPositions.add(i+1);
		
		System.out.println("pos:"+doubleSymbolPositions);

		//paddingsymbole an den gesuchten stellen rückwärts einfügen, damit sich index nicht verschiebt!
		for (int j=doubleSymbolPositions.size()-1; j>=0; j--)
			in=in.substring(0,doubleSymbolPositions.get(j))+paddingSymbol+in.substring(doubleSymbolPositions.get(j),in.length());
		
		System.out.println("in="+in);
		
		
		//4.2
		if ((in.length()%2)==0) in=in+paddingSymbol+terminatorSymbol; else in=in+terminatorSymbol;
		System.out.println("in (mit paddingsymbolen)="+in);

		//4.3
		int startSymbol=in.charAt(in.length()-1);
		
		for (int i=0+startSymbol;i<55555;i++){
			if ((in.length()%512)==0) break;
			char addthis=(char)((startSymbol+i) % 65536);
			if (addthis==terminatorSymbol){
				i++;
				continue;
			}
			in=in+addthis;			
		}
		
		System.out.println("in (aufgefüllt mit zeichen bis einem vielfachen von 512)="+in);
		
		//4.4
		int splitParts=0; //wie viele indizes mit der länge von 512 werden benötigt?
		splitParts=(in.length()/512);

		String[] preprocessed = new String[splitParts];
		for (int i=0;i<preprocessed.length;i++){
			preprocessed[i]=in.substring(512*i,512*(i+1));
			System.out.println("preprocessed["+i+"] = "+preprocessed[i]);
		}
		return preprocessed;
	}
	
	
}