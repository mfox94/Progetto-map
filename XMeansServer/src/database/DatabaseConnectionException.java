package database;

public class DatabaseConnectionException extends Exception {
	/**
	 * ECCEZIONE CHE GESTISCE ERRORI DI CONNESSIONE AL DB
	 */
	public DatabaseConnectionException() {
		System.out.println("Errore caricamento database..");
	}
}
