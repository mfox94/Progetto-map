package data;

import java.sql.SQLException;
import java.util.*;
import database.QUERY_TYPE;
import data.OutOfRangeSamplesize;
import data.DiscreteAttribute;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;
/**
 * 
 * @author Michele Volpe
 *
 */
public class Data {

	static private Object[][] tmp;
	private List<Example> data = new ArrayList<Example>();
	private int numberOfExamples = 0;
	private List<Attribute> explanatorySet = new LinkedList<Attribute>();
	private TreeSet<Example> tempTree = new TreeSet<Example>();
/**
 * 
 * @param tablename Nome della tabella da utilizzare
 * @throws DatabaseConnectionException
 * @throws SQLException
 * @throws NoValueException
 */
	public Data(String tablename) throws DatabaseConnectionException, SQLException, NoValueException {
		DbAccess db = new DbAccess();
		db.initConnection();
		TableData b = new TableData(db);
		TableSchema ts = new TableSchema(db, tablename);
		try {
			data = b.getDistinctTransazioni(tablename);
		} finally {
			numberOfExamples = data.size();
			if(numberOfExamples==0){
				throw new NoValueException();
			}
		}
		String val_Ou[] = { "sunny", "overcast", "rain" };
		DiscreteAttribute<String> Outlook = new DiscreteAttribute<String>("Outlook", 1, val_Ou);
		
		ContinuousAttribute Temperature = new ContinuousAttribute("Temperature", 2, b.getAggregateColumnValue(tablename, ts.getColumn(1), QUERY_TYPE.MIN), b.getAggregateColumnValue(tablename, ts.getColumn(1), QUERY_TYPE.MAX));

		String val_Hum[] = { "high", "normal" };
		DiscreteAttribute<String> Humidity = new DiscreteAttribute<String>("Humidity", 3, val_Hum);

		String val_Wind[] = { "strong", "weak" };
		DiscreteAttribute<String> Wind = new DiscreteAttribute<String>("Wind", 4, val_Wind);

		String val_Pt[] = { "yes", "no" };
		DiscreteAttribute<String> PlayTennis = new DiscreteAttribute<String>("PlayTennis", 5, val_Pt);

		explanatorySet.add(Outlook);
		explanatorySet.add(Temperature);
		explanatorySet.add(Humidity);
		explanatorySet.add(Wind);
		explanatorySet.add(PlayTennis);

		
	
		

	};
/**
 * 
 * 
 * @return numberOfExamples Numero di tuple nella tabella
 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
/**
 * 
 * 
 * @return explanatorySet.size() Numero di colonne della tabella
 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}
/**
 * 
 * @return explanatorySet.toArray() Array contenente le colonne della tabella
 */
	Object[] getAttributeSchema() {
		return explanatorySet.toArray();
	}
/**
 * 
 * @param  Indice
 * @param  Attributo
 * @return  Valore presente in tabella con riga exampleIndex e colonna attributeIndex
 */
	Object getAttributeValue(int exampleIndex, int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);

	}
/**
 * 
 * @param  Indice della tupla da prendere
 * @return  Restituisce la tupla
 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(explanatorySet.size());
		for (int i = 0; i < explanatorySet.size(); i++) {
			if (explanatorySet.get(i).getClass().getName().equals("data.DiscreteAttribute")) {
				String val = String.valueOf(this.getAttributeValue(index, i));
				DiscreteAttribute attr = (DiscreteAttribute) explanatorySet.get(i);
				DiscreteItem it = new DiscreteItem(attr, val);
				tuple.add(it, i);
			} else {
				double val = Double.parseDouble(String.valueOf(this.getAttributeValue(index, i)));
				ContinuousAttribute attr = (ContinuousAttribute) explanatorySet.get(i);
				ContinuousItem it = new ContinuousItem(attr, val);
				tuple.add(it, i);

			}

		}
		return tuple;
	}
/**
 * 
 * @param Prima tupla
 * @param Seconda tupla
 * @return Restituisce true o false in base all'uguaglianza delle 2 tuple
 */
	private boolean compare(int i, int j) {
		boolean b = true;
		for (int k = 0; k < explanatorySet.size(); k++) {
			String val1 = String.valueOf(this.getAttributeValue(i, k));
			String val2 = String.valueOf(this.getAttributeValue(j, k));
			if (!val1.equals(val2)) {
				b = false;
			}
		}
		return b;
	}

/**
 * 
 * @param  Numero di tuple per ogni cluster
 * @return Vettore di k interi(servono a selezionare le tuple). I k interi sono scelti casualmente.
 * @throws OutOfRangeSamplesize
 */
	public int[] sampling(int k) throws OutOfRangeSamplesize {
		int[] a = new int[k];
		
	
		int i = 0, randInt = 0;
		
		Random rand = new Random();
		while (i < k) {
			randInt = rand.nextInt(this.getNumberOfExamples());
			if (i == 0) {
				a[i] = randInt;
				i++;
			} else {
				int j = 0;
				while (j < i) {
					if (compare(a[j], randInt)) {
						randInt = rand.nextInt(this.getNumberOfExamples());
						j = 0;
					} else {
						j++;
					}
				}
				a[i] = randInt;
				i++;
			}
		}
		return a;
		
	}
/**
 * 
 * @param clusteredData Cluster con le tuple provenienti dalla tabella
 * @param Attributo da computare (Discreto o continuo)
 * @return Oggetto di computazione
 */
	Object computePrototype(Set<Integer> clusteredData, Attribute attribute) {
		if (attribute.getClass().getName().equals("data.DiscreteAttribute")) {
			return computePrototype(clusteredData, (DiscreteAttribute) attribute);
		} else {
			return computePrototype(clusteredData, (ContinuousAttribute) attribute);
		}

	}

	/**
	 * 
	 * @param idList
	 * @param attribute
	 * @return Invoca metodi appropriati per i casi discreto e continuo per la determinazione del prototipo (centroide) rispetto ad attribute (discreto o continuo)

	 */
	String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		int maxFreq = 0, currFreq = 0;
		String prototype = new String();
		Iterator<String> iter = attribute.iterator();

		while (iter.hasNext()) {
			String nuovo = iter.next();
			currFreq = attribute.frequency(this, idList, nuovo);
			if (currFreq > maxFreq) {
				maxFreq = currFreq;
				prototype = nuovo;
			}
		}

		return prototype;
	}
/**
 * 
 * @param tuple appartenenti ad un cluster, attributo rispetto al quale calcolare il prototipo (centroide)
 * @param valore continuo-centroide rispetto ad attribute
 * @return Determina il prototipo come valore medio sui valori di attribute nelle tuple idList
 */
	Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double media = 0, somma = 0, nuovo = 0;
		for (Iterator<Integer> it = idList.iterator(); it.hasNext();) {
			int f = it.next();
			nuovo = (double) data.get(f).get(1);
			somma = nuovo + somma;

		}
		media = somma / idList.size();
		return media;
	}
/**
 * 
 * @param row
 * @param cols
 * @return Restituisce valore in posizione “cols” della tupla "row"
 */
	public String getValue(int row, int cols) {
		return data.get(row).get(cols).toString();
	}

}
