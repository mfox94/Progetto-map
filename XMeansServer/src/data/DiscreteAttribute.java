package data;
import java.util.*;
import java.io.*;
public class DiscreteAttribute<T>  extends Attribute  implements Iterable<String>, Serializable{
	private TreeSet<String> values=new TreeSet<String>();
	/**
	 * COSTRUTTORE DI ATTRIBUTI DI TIPO DISCRETO (Es. Sunny, Yes, o altre stringhe)
	 * @param name nome dell'attributo discreto
	 * @param index indice dell'attributo discreto
	 * @param val valore contenente i valori di ingresso (Non devono essere necessariamente quelli)
	 */
	public DiscreteAttribute(String name, int index, String val[]) {
		super(index, name);
		for(int i=0;i<val.length;i++){
		values.add(val[i]);
		}
		
	}
	/**
	 * RESTITUISCE IL NUMERO DI VALORI DISTINTI
	 * @return intero contenente la quantità di valori distinti
	 */
	int getNumberOfDistinctValues() {
		return values.size();
	}


/**
 * DETERMINA IL NUMERO DI OCCORRENZE DI V DENTRO IDLIST
 * @param data tabella iniziale
 * @param Set Set di valori contenente gli attributi discreti
 * @param v stringa da analizzare
 * @return intero rappresentante la frequenza assoluta
 */
	int frequency(Data data, Set<Integer> idList, String v) {
		int freq = 0, iAttr = this.getIndex()-1;
		for (int i = 0; i < data.getNumberOfExamples(); i++) {
			if (idList.contains(i)) {
				String tmp=String.valueOf(data.getAttributeValue(i, iAttr));
				if (tmp.equals(v)) {
					freq++;
				}
			}
		}
		return freq;
	}

	public Iterator<String> iterator(){
		Iterator<String> It=values.iterator();
		return It;
	}
}

	