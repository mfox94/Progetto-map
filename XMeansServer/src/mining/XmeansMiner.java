package mining;

import mining.ClusterSet;
import data.Data;
import data.OutOfRangeSamplesize;
import mining.Cluster;

import java.io.*;

public class XmeansMiner implements Serializable, Comparable {
	private ClusterSet C;
/**
 * COSTRUISCE UN CLUSTERSET CON PARAMETRO LA DIMENSIONE DI ESSO
 * @param dimensione
 */
	public XmeansMiner(int k) {
		C = new ClusterSet(k);
	}
/**
 * COSTRUISCE UN CLUSTERSET CON PARAMENTRO IL NOME DEL FILE
 * @param nome
 * @throws IOException
 * @throws ClassNotFoundException
 */
	public XmeansMiner(String nome) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nome));
		C = (ClusterSet) ois.readObject();
		ois.close();
	}
/**
 * RESTITUISCE CLUSTERSET
 * @return
 */
	public ClusterSet getC() {
		return C;
	}
/**
 * CALCOLA L'INDICE DI VALUTAZIONE DAVIES-BOULDIN PER LA QUALITÀ DEI CLUSTER GENERATI IN UNA ITERAZIONE
 * PER CIASCUN CLUSTER “I” IDENTIFICA IL MASSIMO TERMINE DIJ RISPETTO AGLI ALTRI CLUSTER “J”, QUINDI RESTITUISCE LA SOMMA FINALE.
 * @return indice
 */
	public double computeDBIndex() {
		double temp = 0;
		double max = 0;
		double result = 0;
		for (int i = 0; i < C.size(); i++) {
			for (int j = 0; j < C.size(); j++) {
				double sumAvg = (C.get(i).getIntraDistance() + C.get(j).getIntraDistance());
				temp = sumAvg / C.get(i).getCentroid().getDistance(C.get(j).getCentroid());
				if (temp > max && i!=j){
					max = temp;
					result += max;
				}
					
			}
		}
		return result / C.size();
	}

/**
 * SALVA IL CLUSTERSET IN UN FILE
 * @param nome del file
 * @throws IOException
 */
	public void salva(String nome) throws IOException {
		FileOutputStream fos = new FileOutputStream(nome);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(C);
		oos.close();
	}
/**
 * ESEGUE L’ALGORITMO DI CLUSTERING SELEZIONANDO K TUPLE COME CENTROIDI, ASSEGNANDO A CIASCUNA TUPLA IL CENTROIDE CON DISTANZA MINIMA, AGGIORNANDO I CENTROIDI E REITERANDO IL TUTTO FIN QUANDO I CENTROIDI NON COINCIDONO IN DUE ITERAZIONI SUCCESSIVE
 * @param data
 * @return
 * @throws IOException
 * @throws OutOfRangeSamplesize
 */
	public int Xmeans(Data data) throws IOException, OutOfRangeSamplesize {

		int numberOfIterations = 0;

		C.initializeCentroids(data);

		boolean changedCluster = false;
		do {
			numberOfIterations++;
			changedCluster = false;
			for (int i = 0; i < data.getNumberOfExamples(); i++) {
				Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
				Cluster oldCluster = C.currentCluster(i);
				boolean currentChange = nearestCluster.addData(i);
				if (!currentChange) {
					changedCluster = true;
				}
				if (!currentChange && oldCluster != null && i > 1) {
					oldCluster.removeTuple(i);
				}
			}
			C.updateCentroids(data);
		} while (changedCluster);
		
		return numberOfIterations;
	}
/**
 * (DALL'INTERFACCIA COMPARABLE) CONFRONTA 2 CLUSTER ATTRAVERSO IL LORO DBINDEX
 */
	@Override
	public int compareTo(Object x) {
		XmeansMiner a = (XmeansMiner) x;
		if (this.computeDBIndex() > a.computeDBIndex()) {
			return 1;
		}
		if (this.computeDBIndex() < a.computeDBIndex()) {
			return -1;
		} else {
			return 0;
		}

	}

}
