package data;

public class OutOfRangeSamplesize extends Exception  {
	/**
	 * ECCEZIONE CHE GESTISCE IL CASO DI NUMERO DI Cluster SCELTI DALL'UTENTE TROPPO GRANDE
	 * @param k
	 */
	public OutOfRangeSamplesize(int k){
		System.out.println("NUMERO "+k+" TROPPO GRANDE, PREGO INSERIRE NUOVAMENTE");
	}
}
