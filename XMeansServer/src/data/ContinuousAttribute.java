package data;

import java.util.Iterator;
import java.io.*;
public class ContinuousAttribute extends Attribute implements Serializable  {
	private double max;
	private double min ;
	/**
	 * COSTRUTTORE DI ATTRIBUTO CONTINUO (Es. 20.5)
	 * @param Stringa contenente il nome dell'attributo
	 * @param Indice
	 * @param Minimo
	 * @param Massimo
	 */
	public ContinuousAttribute(String name, int index, double m, double M){
		super(index,name);
		max=M;
		min=m;
	}
	/**
	 * NORMALIZZA IL VALORE DELL'ATTRIBUTO
	 * @param Valore
	 * @return Valore normalizzato
	 */
	double getScaledValue(double v){
		double v1=((v-min)/(max-min))*(1-0)+0;
		return v1;
	}

}
