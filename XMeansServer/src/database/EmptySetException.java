package database;

public class EmptySetException extends Exception{
	/**
	 * ECCEZIONE CHE GESTISCE SET DI TUPLE VUOTI
	 */
	public EmptySetException(){
		System.out.println("SET VUOTO");
	}
}
