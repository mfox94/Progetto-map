package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TableSchema {
	DbAccess db;

	public class Column {
		private String name;
		private String type;
/**
 * COSTRUTTORE DELLA COLONNA DELLA TABELLA
 * @param name
 * @param type
 */
		Column(String name, String type) {
			this.name = name;
			this.type = type;
		}
/**
 * RESTITUISCE IL NOME DELLA COLONNA
 * @return nome colonna
 */
		public String getColumnName() {
			return name;
		}
/**
 * CONTROLLA SE IL VALORE DELLA COLONNA E' NUMERICO
 * @return valore booleano
 */
		public boolean isNumber() {
			return type.equals("number");
		}
/**
 * RESTITUISCE LA CONCATENAZIONE FRA NOME E ATTRIBUTO
 */
		public String toString() {
			return name + ":" + type;
		}
	}

	List<Column> tableSchema = new ArrayList<Column>();
/**
 * COSTRUTTORE DI SCHEMA DELLA TABELLA
 * @param dati di accesso al db
 * @param nome della tabella
 * @throws SQLException
 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {
		this.db = db;
		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		// http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {

			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(
						new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));

		}
		res.close();

	}
/**
 * RESTITUISCE IL NUMERO DI ATTRIBUTI
 * @return intero
 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}
/**
 * DATO UN INDICE RESTITUISCE LA COLONNA 
 * @param indice
 * @return colonna
 */
	public Column getColumn(int index) {
		return tableSchema.get(index);
	}

	

}
