package database;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Example implements Comparable<Example> {
		private List<Object> example = new ArrayList<Object>();
/*
 * AGGIUNGE L'EXAMPLE ALLA LISTA
 * */
		public void add(Object o) {
			example.add(o);
		}
/**
 * RESTITUISCE IL VALORE DELL'ATTRIBUTO I-ESIMO DELLA TUPLA 
 * @param indice
 * @return Restituisce l'example di indice i
 */
		public Object get(int i) {
			return example.get(i);
		}
/**
 * RESTITUISCE UNA STRINGA CHE CONCATENA TUTTI I MEMBRI DI UNA TUPLA
 */
		public String toString() {
			String s = "";
			Iterator<Object> iter = example.iterator();
			while (iter.hasNext()) {
				Object el = (Object) iter.next();
				s = s + el.toString() + "  ";
			}
			return s;
		}
		/**
		 * (DALL'INTERFACCIA COMPARABLE) CONFRONTA DI DUE TUPLE. 
		 */
		public int compareTo(Example ex) {
			return toString().compareTo(ex.toString());
		}
}