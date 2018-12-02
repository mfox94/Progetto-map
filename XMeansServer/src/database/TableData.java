package database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;

import database.DatabaseConnectionException.*;
import database.TableSchema.Column;

public class TableData {

	DbAccess db;

	/**
	 * Istanzia i dati di accesso a un DB
	 * 
	 * @param Dati
	 *            di accesso al DB
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * 
	 * @param Nome della tabella da utilizzare
	 * @return Restituisce la lista delle tuple distinte dal DB
	 * @throws SQLException
	 * @throws DatabaseConnectionException
	 * @throws NoValueException
	 */
	public List<Example> getDistinctTransazioni(String table)
			throws SQLException, DatabaseConnectionException, NoValueException {
		List<Example> le = new ArrayList<Example>();
		db.initConnection();
		TableSchema ts = new TableSchema(db, table);
		try {
			Statement st = db.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT DISTINCT * FROM " + table);
			while (rs.next()) {
				Example ex = new Example();
				for (int i = 1; i < ts.getNumberOfAttributes() + 1; i++) {

					String temp = rs.getString(i);
					try {

						if (temp.equals("")) {
							throw new NoValueException();
						}

					} finally {

					}
					try {
						double tmp = Double.parseDouble(temp);
						ex.add(tmp);
					} catch (Exception e) {
						ex.add(rs.getString(i));
					}

				}
				le.add(ex);
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return le;
	}

	/**
	 * 
	 * @param Nome
	 *            tabella
	 * @param Colonna
	 * @return Restituisce i valori distinti di una colonna
	 * @throws SQLException
	 * @throws DatabaseConnectionException
	 */
	public Set<Object> getDistinctColumnValues(String table, Column column)
			throws SQLException, DatabaseConnectionException {
		Set<Object> le = new HashSet<Object>();
		TableSchema ts = new TableSchema(db, table);
		try {
			Statement st = db.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table + " ORDER BY "
					+ column.getColumnName());
			while (rs.next()) {
				Example ex = new Example();
				ex.add(rs.getString(1));

				le.add(ex);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return le;
	}

	/**
	 * 
	 * @param Nome
	 *            della tabella
	 * @param Colonna
	 * @param Valore
	 *            che imposta l'aggregazione a MIN o MAX
	 * @return MIN o MAX della colonna inserita in input
	 * @throws NoValueException
	 */
	public double getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws NoValueException {
		Object result = new Object();
		try {
			if (aggregate == null) {
				throw new NoValueException();
			}
			Statement st = db.getConnection().createStatement();
			ResultSet rs;

			if (aggregate == QUERY_TYPE.MAX) {
				rs = st.executeQuery("SELECT  MAX(" + column.getColumnName() + ") FROM " + table);
			} else {
				if (aggregate == QUERY_TYPE.MIN) {
					rs = st.executeQuery("SELECT  MIN(" + column.getColumnName() + ") FROM " + table);
				} else {
					System.out.println("ERRORE AGGREGAZIONE");
					rs = null;
				}
			}

			while (rs.next()) {
				Example ex = new Example();
				ex.add(rs.getString(1));
				result = ex.toString();
				return Double.parseDouble((String) result);
			}
		} catch (SQLException e) {
			System.out.println("ERRORE QUERY");
		}

		return Double.parseDouble((String) result);

	}

	/**
	 * 
	 * @return Ritorna tutti i nomi di tabelle presente nel DB. Esso serve per
	 *         la scelta dell'utente a run-time della tabella da utilizzare
	 * @throws SQLException
	 */
	public ArrayList<String> getAllTableName() throws SQLException {
		ArrayList<String> L = new ArrayList<String>();
		DatabaseMetaData md = db.getConnection().getMetaData();
		ResultSet rs = null;

		rs = md.getTables(null, null, null, new String[] { "TABLE" });
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			if (!L.contains(tableName)) {
				L.add(tableName);
			}
		}

		return L;
	}
/**
 * METODO UTILIZZATO PER INSERIRE TUPLE SCELTE DALL'UTENTE NELLA TABELLA DI DATI
 * @param array contenente i dati scelti in input dall'utente
 * @throws SQLException
 */
	public void insertInTable(ArrayList L) throws SQLException {
		String outlook=String.valueOf(L.get(0));
		String temperature=String.valueOf(L.get(1));
		String humidity=String.valueOf(L.get(2));
		String wind=String.valueOf(L.get(3));
		String playtennis=String.valueOf(L.get(4));
		String tablename=String.valueOf(L.get(5));
		Statement st = db.getConnection().createStatement();
		System.out.println("insert into "+tablename+" values('"+outlook+"','"+temperature+"','"+humidity+"','"+wind+"','"+playtennis+"');");
		st.executeUpdate("insert into "+tablename+" values('"+outlook+"','"+temperature+"','"+humidity+"','"+wind+"','"+playtennis+"');");
		
	}

}
