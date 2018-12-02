package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
		/**
		 * Dati di accesso al db.:
		 * private static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
		*private static final String DBMS = "jdbc:mysql";
		*private static final String SERVER = "127.0.0.1";
		*private static final int PORT = 3306;
		*private static final String DATABASE = "XMeans2016";
		*private static final String USER_ID = "xmuser";
		*private static final String PASSWORD = "xmpassword";
		 */
		private static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
		private static final String DBMS = "jdbc:mysql";
		private static final String SERVER = "127.0.0.1";
		private static final int PORT = 3306;
		private static final String DATABASE = "XMeans2016";
		private static final String USER_ID = "xmuser";
		private static final String PASSWORD = "xmpassword";
		private static Connection conn;
		/**
		 * 
		 * ISTANZIA UNA CONNESSIONE CON IL DATABASE
		 * @throws DatabaseConnectionException
		 * @throws SQLException
		 */
		public void initConnection() throws DatabaseConnectionException, SQLException {
			String connectionString = DBMS+"://" + SERVER + ":" + PORT + "/" + DATABASE;
			try{
				Class.forName(DRIVER_CLASS_NAME).newInstance();
			}catch(Exception e){
				System.err.println("IMPOSSIBILE TROVARE DRIVER");
			
			}
			try{
				conn=DriverManager.getConnection(connectionString, USER_ID, PASSWORD);
				
			}catch(SQLException e){
				System.err.println("IMPOSSIBILE STABILIRE CONNESSIONE DATABASE");
				
			}
		}
		/**
		 * 
		 * @return Restituisce una connessione
		 */
		public Connection getConnection(){
			return conn;
		}
		/**
		 * CHIUDE LA CONNESSIONE
		 * @throws SQLException
		 */
		public void closeConnection() throws SQLException{
			 conn.close();
		}
}
