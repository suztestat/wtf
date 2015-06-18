import java.util.ArrayList;

public class Preprocessing {

	private String input;
	private Character paddingSymbol=null;
	private Character terminatorSymbol=null;
	public String[] getPreprocessed() {
		return preprocessed;
	}

	private String[] preprocessed;
	
	public Preprocessing(String input){
		this.input=input;
		String temp=addPaddingSymbols(input);
		preprocessed=fillSymbolsAndSplit(temp);
	}
	
	public String addPaddingSymbols(String in){
		//1.4.1
		ArrayList<Integer> doubleSymbolPositions = new ArrayList<Integer>();
		
		//positionen von doppelten zeichen zwischen 2er Tupeln finden
		for (int i=0;i<input.length()-1;i+=2)
			if (input.charAt(i)==input.charAt(i+1))
				doubleSymbolPositions.add(i+1);
		
		System.out.println("pos:"+doubleSymbolPositions);

		//padding/terminatorsymbol finden, das nicht im eingabestring vorkommt
		for (int i=200;i<=65535;i++){
			if (in.indexOf((char)i)==-1){
				if (paddingSymbol==null) paddingSymbol=(char)i; else
					if (terminatorSymbol==null) {
						terminatorSymbol=(char)i;
						break;
					}
			}
		}
		
		//paddingsymbole an den gesuchten stellen rückwärts einfügen, damit sich index nicht verschiebt!
		for (int j=doubleSymbolPositions.size()-1; j>=0; j--)
			in=in.substring(0,doubleSymbolPositions.get(j))+paddingSymbol+paddingSymbol+in.substring(doubleSymbolPositions.get(j),in.length());
		
		//1.4.2
		if ((in.length()%2)==0) in=in+paddingSymbol+terminatorSymbol; else in=in+terminatorSymbol;
		System.out.println("in (mit paddingsymbolen)="+in);

		return in;
	}
	
	
	public String[] fillSymbolsAndSplit(String in){
		//1.4.3
		//
		int startSymbol=in.charAt(in.length()-1);
									//maximal 511 mal inkrementieren
		for (int i=startSymbol;i<startSymbol+512;i++){
			if ((in.length()%512)==0) break;
			char addthis=(char)((startSymbol+i) % 65536);
			if (addthis==terminatorSymbol){
				i++; //frage: wirklich nochmal erhöhen?
				continue;
			}
			in=in+addthis;			
		}
		
		System.out.println("in (aufgefüllt mit zeichen bis einem vielfachen von 512)="+in);
		
		//1.4.4
		int splitParts=(in.length()/512); //wie viele indizes mit der länge von 512 werden benötigt?

		String[] preprocessed = new String[splitParts];
		for (int i=0;i<preprocessed.length;i++){
			preprocessed[i]=in.substring(512*i,512*(i+1));
			System.out.println("preprocessed["+i+"] = "+preprocessed[i]);
		}
		return preprocessed;
	}
	
	
}