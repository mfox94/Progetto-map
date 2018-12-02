package mining;

import java.io.Serializable;

import data.Data;
import data.OutOfRangeSamplesize;
import data.Tuple;

public class ClusterSet implements Serializable {
	private Cluster C[];
	private int i = 0;
	/**
	 * CREA UN NUOVO CLUSTERSET
	 * @param numero di cluster
	 */
	 public ClusterSet(int k) {
		C = new Cluster[k];
	}
/**
 * AGGIUNGE UN CLUSTER AD UN CLUSTERSET
 * @param cluster
 */
	void add(Cluster c) {
		C[i] = c;
	}
/**
 * RESTITUISCE UN SET DA UN CLUSTERSET
 * @param indice
 * @return cluster
 */
	public Cluster get(int i) {
		return C[i];
	}
	/**
	 * RITORNA LA DIMENSIONE DEL CLUSTERSET
	 * @return dimensione
	 */
	public  int size(){
		return this.C.length;
	}
	/**
	 * ESEGUE L’INIZIALIZZAZIONE DEI CLUSTER INDIVIDUANDO I CENTROIDI DALL’INSIEME DI TUPLE DA DATA. INVOCANDO INT[ ] SAMPLING(INT K) DETERMINA UN INSIEME DI INDICI DI TUPLE-CENTROIDI, QUINDI CON QUESTI CREA LE RELATIVE ISTANZE TUPLE CHE USA PER ISTANZIARE OGGETTI CLUSTER
	 * @param data
	 * @throws OutOfRangeSamplesize
	 */
	public void initializeCentroids(Data data) throws OutOfRangeSamplesize {
		int indici[] = data.sampling(C.length);
		
		
		for (int j = 0; j < indici.length; j++) {
			Cluster c = new Cluster(data.getItemSet(indici[j]));
			this.add(c);
			i++;
		}
	}
/**
 *  CALCOLA LA DISTANZA TRA LA TUPLA ED IL CENTROIDE DI CIASCUN CLUSTER DETERMINANDO QUELLO A DISTANZA MINORE.


 * @param tuple
 * @return cluster
 */
	public Cluster nearestCluster(Tuple tuple) {
		int index = 0;
		double minDist = tuple.getDistance(C[0].getCentroid());
		for (int i = 1; i < C.length; i++) {
			double currDist = tuple.getDistance(C[i].getCentroid());
			if (minDist > currDist) {
				minDist = currDist;
				index = i;
			}
		}
		return get(index);
	}
/**
 *  IDENTIFICA E RESTITUISCE IL CLUSTER CUI LA TUPLA APPARTIENE SE LA TUPLA APPARTIENE AD UN CLUSTER, ALTRIMENTI RESTITUISCE NULL. METODO UTILIZZATO PER VERIFICARE SE TRA DUE ITERAZIONI LA DISTRIBUZIONE DELLE TUPLE NEI CLUSTER È CAMBIATA
 * @param id
 * @return cluster
 */
	 public Cluster currentCluster(int id) {
		Cluster cl = null;
		boolean trovato=false;
		for(int i=0;i<C.length && !trovato;i++){
			if(C[i].contain(id)){
				trovato=true;
				cl=C[i];
			}
		}
		return cl;
	}
/**
 *  RI-DETERMINA IL CENTROIDE PER CIASCUN CLUSTER NELL’ ARRAY C INVOCANDO IL CALCOLO DEL CENTROIDE IN CIASCUN OGGETTO  CLUSTER

 * @param data
 */
	public void updateCentroids(Data data){
	
		for(int j=0;j<C.length;j++){
			C[j].computeCentroid(data);
		}
		

	}
/**
 * CONVERTE IL CLUSTERSET IN STRINGA
 */
	public String toString() {
		String s = "";
		for (int j = 0; j < C.length; j++) {
			s = s + C[j].toString() + "\n";
		}
		return s;
	}
	/**
	 * CONVERTE IL CLUSTERSET IN STRINGA
	 */
	public String toString(Data data) {

		String s = "";
		for (int j = 0; j < C.length; j++) {
			s = s + C[j].toString(data) + "\n";
		}
		return s;
	}
}
