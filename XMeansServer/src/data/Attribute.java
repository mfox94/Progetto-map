package data;
import java.io.*;
	public abstract class Attribute implements Serializable{
		protected String name; // nome simbolico dell'attributo
		protected int index; // identificativo numerico dell'attributo
		/**
		 * COSTRUTTORE DI UN ATTRIBUTO
		 * @param indice
		 * @param nome
		 */
		Attribute(int i, String n){
			name=n;
			index=i;
		}
		/**
		 * RESTITUISCE IL NOME DELL'ATTRIBUTO
		 * @return nome attributo
		 */
		String getName(){
			return name;
		}
		/**
		 * RESTITUISCE INDICE DELL'ATTRIBUTO
		 * @return indice attributo
		 */
		int getIndex(){
			return index;
		}
		/**
		 * RESTITUISCE L'ATTRIBUTO SOTTO FORMA DI STRINGA
		 * @return stringa contenente l'attributo
		 */
		public String toString(){
			return name;	
		}
	}
	
	
