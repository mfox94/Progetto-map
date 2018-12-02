package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.*;
import data.Data;
import data.OutOfRangeSamplesize;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.Example;
import database.NoValueException;
import database.TableData;
import mining.XmeansMiner;

public class ServerOneClient extends Thread {
	private Socket socket;
	private XmeansMiner xmeans;
	ObjectOutputStream out;
	ObjectInputStream in;

	/**
	 * ASSEGNA THREAD DEL SERVER AD UN CLIENT CONNESSO
	 * 
	 * @param socket
	 * @throws IOException
	 * @throws DatabaseConnectionException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws NoValueException
	 */
	public ServerOneClient(Socket s)
			throws IOException, DatabaseConnectionException, SQLException, ClassNotFoundException, NoValueException {
		System.out.println("NUOVO UTENTE CONNESSO");
		socket = s;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		System.out.println(socket.toString());

	}

	/**
	 * 
	 * 
	 * ESEGUE LA PARTE SERVER DI OGNI CLIENT CONNESSO, ESSO PERMETTE ATTRAVERSO UN ALBERO RICEVUTO IN INPUT DALLO STREAM
	 * DI SAPERE QUALE SCELTA HA EFFETTUATO L'UTENTE, E QUINDI PERMETTE DI CALCOLARE ALGORITMO DI XMEANS, VISUALIZZARE LA TABELLA DELL'UTENTE ED ALTRO
	 * 
	 */
	public void run() {
		while (true) {
			try {
				ArrayList L = (ArrayList) in.readObject();
				if (L.size() == 2) {
					try {
						DbAccess db = new DbAccess();
						db.initConnection();
						TableData b = new TableData(db);
						ArrayList L2 = b.getAllTableName();
						out.writeObject(L2);
					} catch (DatabaseConnectionException | SQLException | IOException e2) {
					}
				}
				if (L.size() == 4) {
					int kmin = (int) L.get(0);
					int kmax = (int) L.get(1);
					String tablename = String.valueOf(L.get(2));
					String fName = String.valueOf(L.get(3));

					try {
						Data data = new Data(tablename);
						String res = "";
						try {
							TreeSet<XmeansMiner> ts = new TreeSet<XmeansMiner>();
							for (int j = kmin; j <= kmax; j++) {
								if (j > data.getNumberOfExamples()) {
									throw new OutOfRangeSamplesize(j);
								}

								XmeansMiner xm2 = new XmeansMiner(j);
								int numIter2 = xm2.Xmeans(data);
								ts.add(xm2);

							}
							XmeansMiner resu = (XmeansMiner) ts.first();

							out.writeObject("Numero tuple minor cluster " + resu.getC().size() + "\nMinor cluster: \n\n"
									+ resu.getC().toString(data) + "DBIndex: " + resu.computeDBIndex());
							System.out.println("User " + socket.getPort() + " has request the service;");
							if (fName.equals("")) {
								ts.first().salva("cluster.dmp");
								System.out.println("User " + socket.getPort() + " has saved cluster into cluster.dmp");
							} else {
								ts.first().salva(fName + ".dmp");
								System.out.println(
										"User " + socket.getPort() + " has saved cluster into " + fName + ".dmp");
							}

						} catch (OutOfRangeSamplesize e) {
							out.writeObject("OutOfRangeError");
						}
					} catch (NoValueException e) {
						out.writeObject("NoValueError");
					} catch (DatabaseConnectionException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}
				if (L.size() == 1) {
					try {
						String filename = String.valueOf(L.get(0));
						System.out.println(filename);
						XmeansMiner Xmeans = new XmeansMiner(filename);
						String res = Xmeans.getC().toString() + Xmeans.computeDBIndex();
						out.writeObject(res);

					} catch (FileNotFoundException e1) {
						out.writeObject("ERR");
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}

				}
				if (L.size() == 3) {
					List<Example> data = new ArrayList<Example>();

					DbAccess db = new DbAccess();
					db.initConnection();
					TableData b = new TableData(db);
					data = b.getDistinctTransazioni(String.valueOf(L.get(0)));
					String res=new String();
					res=L.get(0)+":\n\n";
					for(int i=0;i<data.size();i++){
						res=res+"["+data.get(i)+"]\n";
					}
					out.writeObject(res);
				}
				if (L.size() == 6) {
					DbAccess db = new DbAccess();
					db.initConnection();
					TableData b = new TableData(db);
					b.insertInTable(L);
					
				}

			} catch (IOException e1) {
				System.out.println("User " + socket.getPort() + " has disconnected;");
				this.interrupt();
				break;
			} catch (ClassNotFoundException e1) {
				System.out.println("ERRORE INASPETTATO1");
				this.interrupt();
				break;
			} catch (DatabaseConnectionException e) {
				System.out.println("ERRORE INASPETTATO2");
				this.interrupt();
				break;
			} catch (SQLException e) {
				e.printStackTrace();
				this.interrupt();
				break;
			} catch (NoValueException e) {
				e.printStackTrace();
			}
		}

	}
}