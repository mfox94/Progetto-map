import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.NotValidValueException;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.TableData;
import database.TableSchema;
import server.MultiServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XMeansClient extends JApplet {
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private class TabbedPane extends JPanel {
		private JPanelCluster panelDB;
		private JPanelCluster panelFile;
		private JPanelCluster panelInsert;
		private JPanelCluster panelTable;

		private class JPanelCluster extends JPanel {

			JComboBox comboTableAvaib = new JComboBox();
			JComboBox comboTable = new JComboBox();
			JTextField outlook_F = new JTextField(20);
			JTextField humidity_F = new JTextField(20);
			JTextField temperature_F = new JTextField(20);
			JTextField wind_F = new JTextField(20);
			JComboBox playtennis_F = new JComboBox();
			JTextField newfileName = new JTextField(20);
			JTextField fileName = new JTextField(20);
			JTextField kTextMin = new JTextField(10);
			JTextField kTextMax = new JTextField(10);
			JTextArea clusterOutput = new JTextArea();
			JButton executeButton;
/**
 * ISTANZIA IL PANNELLO ALL'INTERNO ALL'INTERNO DEL TABBEDPANE
 * @param indice del tabbedPane
 * @param nome del bottone
 * @param action listener
 * @throws ClassNotFoundException
 * @throws IOException
 * @throws DatabaseConnectionException
 * @throws SQLException
 */
			JPanelCluster(int index, String buttonName, java.awt.event.ActionListener a)
					throws ClassNotFoundException, IOException, DatabaseConnectionException, SQLException {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.setBackground(Color.DARK_GRAY);
				JPanel upPanel = new JPanel();
				upPanel.setBackground(Color.DARK_GRAY);
				upPanel.setLayout(new FlowLayout());

				if (index == 1) {
					JLabel TableL = new JLabel("Table:");
					TableL.setForeground(Color.WHITE);
					JLabel KMinL = new JLabel("KMin:");
					KMinL.setForeground(Color.WHITE);
					JLabel KMaxL = new JLabel("KMax:");
					KMaxL.setForeground(Color.WHITE);
					JLabel FileNameL = new JLabel("File name:");
					FileNameL.setForeground(Color.WHITE);
					upPanel.add(TableL);
					upPanel.add(comboTable);
					upPanel.add(KMinL);
					upPanel.add(kTextMin);
					upPanel.add(KMaxL);
					upPanel.add(kTextMax);
					upPanel.add(FileNameL);
					upPanel.add(newfileName);
				}
				if (index == 2) {
					JLabel FileNameL = new JLabel("File name:");
					FileNameL.setForeground(Color.WHITE);
					upPanel.add(FileNameL);
					upPanel.add(fileName);
				}
				if (index == 3) {
					JPanel insPanel = new JPanel();
					insPanel.setLayout(new GridLayout(6, 1));
					insPanel.setBackground(Color.DARK_GRAY);
					JLabel Table = new JLabel("Scegli tabella");
					Table.setForeground(Color.WHITE);
					JLabel Outlook_L = new JLabel("Outlook:");
					Outlook_L.setForeground(Color.WHITE);
					JLabel Humidity_L = new JLabel("Humidity:");
					Humidity_L.setForeground(Color.WHITE);
					JLabel Temperature_L = new JLabel("Temperature:");
					Temperature_L.setForeground(Color.WHITE);
					JLabel PlayTennis_L = new JLabel("Play Tennis:");
					PlayTennis_L.setForeground(Color.WHITE);
					JLabel Wind_L = new JLabel("Wind:");
					Wind_L.setForeground(Color.WHITE);
					playtennis_F.addItem("no");
					playtennis_F.addItem("yes");
					insPanel.add(Table);
					insPanel.add(comboTable);
					insPanel.add(Outlook_L);
					insPanel.add(outlook_F);
					insPanel.add(Temperature_L);
					insPanel.add(temperature_F);
					insPanel.add(Humidity_L);
					insPanel.add(humidity_F);
					insPanel.add(Wind_L);
					insPanel.add(wind_F);
					insPanel.add(PlayTennis_L);
					insPanel.add(playtennis_F);
					upPanel.add(insPanel);
				}
				if (index == 4) {
					upPanel.add(comboTableAvaib);
				}
				add(upPanel);
				JPanel centralPanel = new JPanel();
				centralPanel.setBackground(Color.DARK_GRAY);
				centralPanel.setLayout(new GridLayout(1, 1));
				clusterOutput.setBackground(Color.DARK_GRAY);
				clusterOutput.setForeground(Color.WHITE);
				clusterOutput.setEditable(false);
				JScrollPane scrollingArea = new JScrollPane(clusterOutput);
				scrollingArea.setBackground(Color.DARK_GRAY);
				centralPanel.add(scrollingArea);
				add(centralPanel);
				JPanel downPanel = new JPanel();
				downPanel.setBackground(Color.DARK_GRAY);
				// downPanel.setLayout(new GridLayout(1,1));
				downPanel.setLayout(new FlowLayout());
				executeButton = new JButton(buttonName);
				executeButton.setBackground(Color.WHITE);
				executeButton.addActionListener(a);
				downPanel.add(executeButton);
				add(downPanel);
			}
/**
 * CARICA DA DB LE TABELLE PRESENTI IN MODO DA PERMETTERE ALL'UTENTE LA SCELTA DELLA TABELLA DA UTILIZZARE
 * @throws ClassNotFoundException
 * @throws IOException
 * @throws DatabaseConnectionException
 * @throws SQLException
 */
			void InizializeComboBox()
					throws ClassNotFoundException, IOException, DatabaseConnectionException, SQLException {
				ArrayList L = new ArrayList();
				L.add("LCT");
				L.add("LCT");
				out.writeObject(L);
				L = (ArrayList) in.readObject();
			
				if(!L.isEmpty()){
					Iterator<String> iter = L.iterator();
					while (iter.hasNext()) {
						String n = String.valueOf(iter.next());
						panelDB.comboTable.addItem(n);
						panelInsert.comboTable.addItem(n);
						panelTable.comboTableAvaib.addItem(n);
					}
				}else{
					int input = JOptionPane.showOptionDialog(null, "Inserisci almeno una tabella nella base di dati prima di proseguire",
							"ERRORE DI DB.", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					if (input == JOptionPane.ERROR_MESSAGE) {
						System.exit(1);
					}
				}
				
			}
		}
/**
 * COSTRUTTORE DEL TABBED PANE, OVVERO DELL'INSIEME DI SCHEDE LE QUALI L'UTENTE PUO SCEGLIERE
 * @throws ClassNotFoundException
 * @throws IOException
 * @throws DatabaseConnectionException
 * @throws SQLException
 */
		TabbedPane() throws ClassNotFoundException, IOException, DatabaseConnectionException, SQLException {
			super(new GridLayout(1, 1));
			JTabbedPane tabbedPane = new JTabbedPane();

			// copy img in src Directory and bin directory
			java.net.URL imgURL = getClass().getResource("db.png");
			System.out.println(imgURL.toString());
			ImageIcon iconDB = new ImageIcon(imgURL);
			panelDB = new JPanelCluster(1, "MINE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						runXMeans();
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			imgURL = getClass().getResource("file.png");
			ImageIcon iconFile = new ImageIcon(imgURL);
			panelFile = new JPanelCluster(2, "STORE FROM FILE", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						readFromFile();
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			imgURL = getClass().getResource("new.png");
			ImageIcon newIcon = new ImageIcon(imgURL);
			panelInsert = new JPanelCluster(3, "INSERT!", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						insert();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			imgURL = getClass().getResource("table.png");
			ImageIcon tableIcon = new ImageIcon(imgURL);

			panelTable = new JPanelCluster(4, "SHOW TABLE!", new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						showTable();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			tabbedPane.addTab("DB", iconDB, panelDB, "Does nothing");
			tabbedPane.addTab("FILE", iconFile, panelFile, "Does nothing");
			tabbedPane.addTab("INSERT", newIcon, panelInsert, "Does nothing");
			tabbedPane.addTab("SHOW TABLE", tableIcon, panelTable, "Does nothing");
			add(tabbedPane);
			panelDB.InizializeComboBox();
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			tabbedPane.setVisible(true);

		}
/**
 * PERMETTE DI VISUALIZZARE IL CONTENUTO DI UNA TABELLA A SCELTA DELL'UTENTE
 * @throws IOException
 * @throws ClassNotFoundException
 */
		private void showTable() throws IOException, ClassNotFoundException {
			String table = String.valueOf(panelTable.comboTableAvaib.getSelectedItem());
			List L = new ArrayList();
			L.add(table);
			L.add("a");
			L.add("b");
			out.writeObject(L);
			String res = String.valueOf(in.readObject());
			panelTable.clusterOutput.setText(res);
			
		}
/**
 * PERMETTE DI INSERIRE TUPLE NELLA TABELLA A SCELTA DELL'UTENTE
 * @throws IOException
 */
		private void insert() throws IOException {
			try {
				if(!String.valueOf(panelInsert.comboTable.getSelectedItem()).equals("")){
				String outlook = panelInsert.outlook_F.getText();
				String humidity = panelInsert.humidity_F.getText();
				String wind = panelInsert.wind_F.getText();
				String playtennis = String.valueOf(panelInsert.playtennis_F.getSelectedItem());
				double temperature = Double.parseDouble(String.valueOf(panelInsert.temperature_F.getText()));
				if (!outlook.equals("")) {

					if (!humidity.equals("")) {
						if (!wind.equals("")) {
							List L = new ArrayList();
							L.add(outlook);
							L.add(temperature);
							L.add(humidity);
							L.add(wind);
							L.add(playtennis);
							L.add(String.valueOf(panelInsert.comboTable.getSelectedItem()));
							out.writeObject(L);
							String res = "TUPLA INSERITA CORRETTAMENTE!\nOutlook=" + outlook + "\nTemperature="
									+ temperature + "\nHumidity=" + humidity + "\nWind=" + wind + "\nPlaytennis="
									+ playtennis;
							panelInsert.clusterOutput.setText(res);
							panelInsert.temperature_F.setBackground(Color.white);
							panelInsert.wind_F.setBackground(Color.white);
							panelInsert.humidity_F.setBackground(Color.white);
							panelInsert.outlook_F.setBackground(Color.white);
						} else {
							JOptionPane.showMessageDialog(null, "INSERISCI VALORE WIND");
							panelInsert.wind_F.setBackground(Color.red);

						}
					} else {
						JOptionPane.showMessageDialog(null, "INSERISCI VALORE HUMIDITY");
						panelInsert.humidity_F.setBackground(Color.red);
					}
				} else {
					JOptionPane.showMessageDialog(null, "INSERISCI VALORE OUTLOOK");
					panelInsert.outlook_F.setBackground(Color.red);
				}
				}else{
					JOptionPane.showMessageDialog(null, "INSERISCI UNA TABELLA NELLA BASE DI DATI");
					
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "INSERISCI VALORE DI TEMPERATURA CORRETTO (es. 20.6)");
				panelInsert.temperature_F.setBackground(Color.red);
			}

		}
/**
 * INSERITO IL NOME DI UN FILE (SENZA ESTENSIONE .dmp) CARICA I CENTROIDI SALVATI NEL FILE CON IL NOME INSERITO IN INPUT
 * @throws SocketException
 * @throws IOException
 * @throws ClassNotFoundException
 */
		private void readFromFile() throws SocketException, IOException, ClassNotFoundException {
			String nameFile = panelFile.fileName.getText();
			nameFile=nameFile+=".dmp";
			List L = new ArrayList();
			L.add(nameFile);
			out.writeObject(L);
			String r = String.valueOf(in.readObject());

			if (r.equals("ERR")) {
				JOptionPane.showMessageDialog(null, "File non esistente, riprova");
			} else {
				panelFile.clusterOutput.setText(r);
			}

		}
/**
 * ESEGUE L'ALGORITMO DI XMEANS CON Kmin, Kmax, E NOME DELLA TABELLA INSERITI IN INPUT DALL'UTENTE E SALVANDO IL FILE NEL NOME SCELTO DALL'UTENTE
 * @throws SocketException
 * @throws IOException
 * @throws ClassNotFoundException
 */
		private void runXMeans() throws SocketException, IOException, ClassNotFoundException {
			
			try {
				String TableName = String.valueOf(panelDB.comboTable.getSelectedItem());
				if(!TableName.equals("")){
					
				
				int kmin = 0;
				int kmax = 0;
				String fName = panelDB.newfileName.getText();
				if (fName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Nessun nome del file inserito. Esso verrà inserito di default in 'cluster.dmp'");
				}
				if (!panelDB.kTextMin.getText().equals("") && !panelDB.kTextMax.getText().equals("")) {
					kmin = Integer.parseInt(panelDB.kTextMin.getText());
					kmax = Integer.parseInt(panelDB.kTextMax.getText());

					if (kmin == 0 || kmax == 0) {
						JOptionPane.showMessageDialog(null, "Inserisci valori!");
						panelDB.kTextMax.setText("");
						panelDB.kTextMin.setText("");
					} else {
						if (kmin > kmax) {
							JOptionPane.showMessageDialog(null,
									"Range di valori non valido. Prego inserire Kmin minore di Kmax");
							panelDB.kTextMax.setText("");
							panelDB.kTextMin.setText("");
						} else {
							List L = new ArrayList();
							L.add(kmin);
							L.add(kmax);
							L.add(TableName);
							L.add(fName);
							out.writeObject(L);
							String r = "";
							r = String.valueOf(in.readObject());
							if (r.equals("NoValueError")) {
								JOptionPane.showMessageDialog(null, "ERRORE TABELLA, SONO PRESENTI VALORI NULLI.");
							}
							if (r.equals("OutOfRangeError")) {
								JOptionPane.showMessageDialog(null, "ATTENZIONE, kmin SUPERIORE AL NUMERO DI TUPLE.");
							} else {
								panelDB.clusterOutput.setText(r);
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Inserisci valori!");
				}
				}
			} catch (SocketException e1) {
				System.out.println(e1);
			} catch (FileNotFoundException e1) {
				System.out.println(e1);
			} catch (IOException e1) {
				System.out.println(e1);
			} catch (ClassNotFoundException e1) {
				System.out.println(e1);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Inserisci valori numerici!");
				panelDB.kTextMax.setText("");
				panelDB.kTextMin.setText("");
			}
		}
	}
/**
 * INIZIALIZZA L'APPLET
 */
	public void init() {

		String ip = "127.0.0.1";
		int port = 8020;
		try {
			InetAddress addr = InetAddress.getByName(ip); // ip
			System.out.println("addr = " + addr);
			Socket socket = new Socket(addr, MultiServer.PORT); // Port
			System.out.println(socket);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			TabbedPane tab = new TabbedPane();
			tab.setBackground(Color.DARK_GRAY);
			getContentPane().setLayout(new GridLayout(1, 1));
			getContentPane().add(tab);
		} catch (IOException | ClassNotFoundException | DatabaseConnectionException | SQLException e) {
			int input = JOptionPane.showOptionDialog(null, "Server non trovato. Avvia il server e riprova.",
					"ERRORE DI CONNESSIONE.", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (input == JOptionPane.ERROR_MESSAGE) {
				System.exit(1);
			}
		}
	}
}
