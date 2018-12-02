package data;

import java.io.Serializable;
import java.util.Iterator;

public class ContinuousItem extends Item implements Serializable {
/**
 * COSTRUTTORE DI CONTINUOUS ITEM
 * @param attribute Attributo sul quale costruire l'item
 * @param value Valore dell'Item
 */
	ContinuousItem(ContinuousAttribute attribute, Double value) {
		super(attribute, value);
	}
/** 
 * CALCOLA LA DISTANZA EUCLIDEA TRA 2 TUPLE
 */
	@Override
	double distance(Object a) {
		ContinuousItem curr = (ContinuousItem) a;
		ContinuousAttribute tmp = (ContinuousAttribute) curr.getAttribute();
		return Math.abs(tmp.getScaledValue((double) curr.value)
				- tmp.getScaledValue((double) this.value));
	}

}
