package server;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.net.SocketException;
import database.*;
public class MultiServer {
	public static final int PORT = 8020;

	public static void main(String[] args) throws IOException, DatabaseConnectionException, SQLException, ClassNotFoundException, NoValueException {
		MultiServer MS=new MultiServer();
		MS.run();
	}
/**
 * AVVIA IL SERVER PRINCIPALE, IL QUALE AVRA' LA CAPACITA' DI GESTIRE LE DIVERSE RICHIESTE DI CONNESSIONI DAI VARI CLIENT E DISTRIBUIRE
 * CREARE QUINDI NUOVE SOCKET LE QUALI SI COLLEGHERANNO
 */
	public MultiServer() {
		System.out.println("Server Started");
	}
/**
 *   AVVIA UNA SOCKET CHE SI METTE IN ATTESA DI NUOVE CONNESSIONI. AD OGNI NUOVA CONNESSIONE CREA UN OGGETTO SERVERONECLIENT E LO ASSOCIA ALLA NUOVA CONNESSIONE.
 *   QUESTO PROCEDIMENTO CONSENTE QUINDI AL SERVER STESSO DI LAVORARE IN PARALLELO FRA I DIVERSI CLIENT.
 * @throws IOException
 * @throws DatabaseConnectionException
 * @throws SQLException
 * @throws ClassNotFoundException
 * @throws NoValueException
 */
	public void run() throws IOException, DatabaseConnectionException, SQLException, ClassNotFoundException, NoValueException {
		ServerSocket s = new ServerSocket(PORT);
		try {
			while (true) {
				Socket socket = s.accept();
				try {
					new ServerOneClient(socket).start();
				} catch (IOException e ) {
					socket.close();
				}
			}
		} finally {
			s.close();
		}
	}
}
