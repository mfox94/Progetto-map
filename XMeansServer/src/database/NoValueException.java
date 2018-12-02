package database;

public class NoValueException extends Exception{
	/**
	 * ECCEZIONE CHE GESTISCE L'ASSENZA DI ALCUNI VALORI NELLA TABELLA (E' PREVEISTA LA CHIUSURA DEL PROGRAMMA)
	 */
		public NoValueException(){
			System.out.println("NESSUN VALORE DI AGGREGAZIONE");
		}
	
}
