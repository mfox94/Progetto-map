package data;

import java.io.Serializable;

public class DiscreteItem extends Item implements Serializable {
	/**
	 * COSTRUTTORE DI UN DISCRETE ITEM
	 * @param attributo sul quale creare l'item
	 * @param valore da utilizzare nell'item
	 */
	DiscreteItem(DiscreteAttribute attribute, String value) {
		super(attribute, value);
	}
/**
 * Restituisce 0 se i valori sono identici, 1 altrimenti.
 */
	@Override
	double distance(Object a) {
		String uno=this.toString();
		String due=a.toString();
		if (!uno.equals(due)) {
			return 1;
		} else {
			return 0;
		}

	}
}
