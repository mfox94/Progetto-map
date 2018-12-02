package data;
import java.util.*;
import java.io.*;
import data.DiscreteAttribute;
abstract public class Item implements Serializable{
	Attribute attribute;
	Object value;
/**
 * COSTRUTTORE DI UN ITEM GENERICO (SIA CONTINUOUS CHE DISCRETE)
 * @param attr attributo sul quale oostruire l'item
 * @param val valore da settare
 */
	Item(Attribute attr, Object val) {
		attribute = attr;
		value = val;
	}
	/**
	 * RESTITUISCE L'ATTRIBUTE SUL QUALE E' COSTRUITO L'ITEM
	 * @return Attributo	
	 */
	Attribute getAttribute(){
		return attribute;
	}
	/**
	 * RESTITUISCE IL VALORE DELL'ITEM
	 * @return Valore
	 */
	Object getValue(){
		return value;
	}
	/**
	 * TRASFORMA L'ITEM IN STRINGA
	 */
	public String toString(){
		return attribute+"="+value;
	}
	abstract double distance(Object a);
	
	
	/**
	 * RISPETTO ALL’INSIEME DI TUPLE CLUSTEREDDATA APPARTENENTI AD UN CLUSTER DETERMINA IL CENTROIDE RISTRETTO ALL’ATTRIBUTO ITEM SUL QUALE È INVOCATO TALE METODO. IMPIEGA IL METODO OBJECT COMPUTEPROTOTYPE(ARRAYSET IDLIST, ATTRIBUTE ATTRIBUTE) CHE GLI RESTITUISCE IL VALORE CENTROIDE RISPETTO  ALL’ATTRIBUTO ITEM SUL QUALE È INVOCATO

	 * @param data 
	 * @param clusteredData
	 */
	public void update(Data data,  Set<Integer> clusteredData){
		this.value=data.computePrototype(clusteredData, attribute);
	}

}
