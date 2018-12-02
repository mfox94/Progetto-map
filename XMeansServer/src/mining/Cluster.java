package mining;

import data.Data;
import data.Tuple;

import java.io.Serializable;
import java.util.*;

public class Cluster implements Serializable {
	private Tuple centroid;
	private double intraDistance;
	private Set<Integer> clusteredData;
/**
 * COSTRUTTORE DI UN CLUSTER
 * @param centroide 
 */

	Cluster(Tuple centroid) {
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();

	}
/**
 * RESTITUISCE L'INTRADISTANCE (DISTANZA MEDIA FRA TUPLE)
 * @return intradistance
 */
	public double getIntraDistance() {
		return this.intraDistance;
	}
/**
 * RESTITUISCE IL CENTROIDE DEL CLUSTER
 * @return centroide
 */
	public Tuple getCentroid() {
		return centroid;
	}
/**
 * AGGIORNA IL CENTROIDE DEL CLUSTER (INIZIALMENTE È SCELTO RANDOMICAMENTE), AGGIORNANDO IL VALORE PER CIASCUN ITEM DELLA TUPLACENTROIDE (VOID UPDATE(DATA DATA, ARRAYSET CLUSTEREDDATA)) 
 * @param data
 */
	public void computeCentroid(Data data) {
		Object arr[] = new Object[clusteredData.size()];
		int fin[] = new int[clusteredData.size()];
		for (int i = 0; i < centroid.getLength(); i++) {
			this.centroid.get(i).update(data, clusteredData);
		}
		arr = clusteredData.toArray();
		for (int i = 0; i < clusteredData.size(); i++) {
			fin[i] = (int) arr[i];
		}
		intraDistance = this.centroid.avgDistance(data, fin);

	}

	/**
	 * AGGIORNA IL MEMBRO CLUSTEREDDATA CON LA TUPLA ID: SE LA TUPLA GIÀ ESISTE RESTITUISCE FALSE, ALTRIMENTI TRUE 
	 * @param id
	 * @return boolean
	 */
	
	public boolean addData(int id) {
		return clusteredData.add(id);
	}

	/**
	 * VERIFICA SE IL MEMBRO CLUSTEREDDATA CONTIENE LA TUPLA ID.

	 * @param id
	 * @return boolean
	 */
	boolean contain(int id) {
		return clusteredData.contains(id);
	}

/**
 *RIMUOVE DAL MEMBRO CLUSTEREDDATA LA TUPLA ID. 
 * 
 */
	public void removeTuple(int id) {
		clusteredData.remove(id);

	}
/**
 * RESTITUISCE UNA STRINGA FATTA DA CIASCUN ITEM DEL MEMBRO CENTROID
 */
	public String toString() {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i)+" ";
		str += ")";
		return str;
	}
/**
 * RESTITUISCE UNA STRINGA FATTA DA: CIASCUN ITEM DEL MEMBRO CENTROID, DALLE TUPLE IN CLUSTEREDDATA  APPARTENENTI AL CLUSTER, DALLA DISTANZA TRA CENTROID E CIASCUNA TUPLA IN CLUSTEREDDATA, E DALLA DISTANZA MEDIA TRA CENTROID E CIASCUNA TUPLA.
 * @param data
 * @return
 */
	public String toString(Data data) {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i) + " ";
		str += ")\nExamples:\n";
		Object tmp[] = clusteredData.toArray();
		int array[] = new int[tmp.length];
		for (int k = 0; k < array.length; k++) {
			array[k] = (int) tmp[k];
		}
		for (int i = 0; i < array.length; i++) {
			str += "[";
			for (int j = 0; j < data.getNumberOfExplanatoryAttributes(); j++)
				str += data.getValue(array[i], j) + " ";
			str += "] dist=" + getCentroid().getDistance(data.getItemSet(array[i])) + "\n";

		}
		str += "AvgDistance=" + getCentroid().avgDistance(data, array)+"\n\n";
		return str;

	}

}
