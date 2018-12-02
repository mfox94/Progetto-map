package data;

import java.io.Serializable;

public class Tuple implements Serializable {
	Item[] tuple;
/**
 * CREA UNA TUPLA 
 * @param size numero di attributi
 */
	Tuple(int size) {
		tuple = new Item[size];
	}
	/**
	 * RESTITUISCE LA DIMENSIONE DELLA TUPLA
	 * @return dimensione
	 */
	public int getLength(){
		return tuple.length;
	}
	/**
	 * RESTITUISCE L'ITEM DELLA TUPLA DI INDICE i
	 * @param indice indice della tupla
	 * @return item della tupla
	 */
	public Item get(int i){
		return tuple[i];
	}
	/**
	 * AGGIUNGE UN ITEM ALLA TUPLA IN INDICE I
	 * @param c item
	 * @param i indice
	 */
	void add(Item c, int i){
		tuple[i]=c;
	}
	/**
	 * CALCOLA LA DISTANZA EUCLIDEA FRA DUE TUPLE
	 * @param obj seconda tupla da analizzare
	 * @return distanza (double)
	 */
	 public double getDistance(Tuple obj){
		  double dist = 0;
		  for(int i = 0 ; i < this.getLength(); i++){
		   dist += Math.pow(this.get(i).distance(obj.get(i)), 2);
		  }
		  return Math.sqrt(dist);
		 }

/**
 *  DETERMINA LA DISTANZA MEDIA TRA LE TUPLE CLUSTEREDDATA E LA TUPLA CORRENTE (SULLA QUALE IL METODO È INVOCATO). 
 * @param data
 * @param clusteredData
 * @return distanza media
 */
	
	public double avgDistance(Data data, int[] clusteredData){
		double sum=0.0;
		for (int j=0;j<clusteredData.length;j++){
			sum=sum+this.getDistance(data.getItemSet(clusteredData[j]));
			
		}
	
		return sum/clusteredData.length;
	}
}
