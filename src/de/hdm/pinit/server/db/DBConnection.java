package de.hdm.pinit.server.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;

/**
 * In dieser Klasse findet die Verwaltung zum Verbindungsaufbau der Datenbank
 * statt. Durch die Singleton-Eigenschaft, die hier eingebunden wird, greifen
 * wir immer auf eine klar vordefinierte Datenbank zu. Dadurch wird
 * gew�hrleistet dass immer nur eine Instanz existieren kann und es somit nur
 * eine einzige Verbindung zur Datenbank gibt. Die Verbindung wird hier�ber
 * �berpr�ft und bei vorhandener Verbindung entweder �ber das return
 * zur�ckgegeben oder eben neu instanziiert bzw. erstellt.
 * 
 * @author Miescha, Theresa
 *
 */
/*
 * Das Interface Connection stellt uns eine Sitzung mit einer speziellen DB zur
 * Verf�gung. Anweisungen werden entsprechend ausgef�hrt und dementsprechende
 * Ergebnisse werden im Kontext einer Verbindung �ber unsere Referenzvariable
 * con zur�ckgegeben. Durch die Kennzeichnung static, kann nur eine einzige
 * Instanz einer Klasse erstellt werden.
 */
public class DBConnection {

	/*
	 * In der Variable con vom Typ Connection wird die einzige Instanz dieser
	 * Klasse gespeichert.
	 */
	private static Connection con = null;

	// URL f�r die Verbindung zur GoogleDB
	private static String googleUrl = "";

	// URL f�r die Verbindung zur lokalen DB
	private static String localUrl = "jdbc:mysql://127.0.0.1:3306/pinit?user=root&password=";

	// private static final String testUrl =
	// "jdbc:mysql://localhost:8889/pinit";

	private static final String username = "root";
	private static final String password = "";

	/**
	 * Bei einer Verbindung zur DB, muss eine Instanz von Connection erzeugt
	 * werden. Da wir jedoch die Singleton-Eigenschaft gew�hrleisten m�chten,
	 * kann dies nur �ber die statische Methode erfolgen. Innerhalb der
	 * Mapperklassen wird also mittels DBconnection.connection() die Verbindung
	 * zur DB hergestellt und kann nicht �ber den new-Operator erstellt werden.
	 */

	public static Connection connection() {

		String user = "root";
		String pass = "";

		/**
		 * Wenn es bisher keine Connection zur DB gab, ...
		 */
		if (con == null) {
			// die lokale variable url wird angelegt und hat noch keinen Wert
			String url = null;
			try {

				/*
				 * Zustand des Programms wird �berpr�ft bzgl. GoogleDeployment.
				 * Die �berpr�fung des Production-Status gibt Informationen
				 * dar�ber, ob das Programm schon deployed wurde. Falls es noch
				 * nicht deployed wurde, wird ein anderer DBTreiber ben�tigt.
				 */
				if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
					/*
					 * Die Klasse die so hei�t wie das Argument, welches als
					 * String �bergeben wird, wird zur Laufzeit zugreifbar. �ber
					 * den String wird gesagt welche Klasse zu instanziieren
					 * ist. N�mlich ein Objekt des entsprechden Treibers.
					 */
					Class.forName("com.mysql.jdbc.GoogleDriver");
					url = googleUrl;
					user = username;
					pass = password;

				} else {

					// Lokale MySQLinstanz die w�hrend der Entwicklung verwendet wird.
					Class.forName("com.mysql.jdbc.Driver");
					url = localUrl;
					user = "";
					pass = "";

				}

				 /*
				 * Dann erst kann uns der DriverManager eine Verbindung mit den
				 * oben in der Variable url angegebenen Verbindungsinformationen
				 * aufbauen. Diese Verbindung wird dann in der statischen
				 * Variable con abgespeichert und fortan verwendet.
				 */

				// die oben ausgew�hlte Klasse(Driver), wird hier an der Stelle instantiiert
				con = DriverManager.getConnection(url);

				// Absicherung falls die Connection == null ist.
				if (con != null) {

					System.out.println("Die Verbindung ist aktuell:" + con.toString());

				} else {

					System.out.println("Verbindung ist null.");

				}

				/*
				 * Wenn eine Exception geworfen wird, wird sie hier aufgefangen.
				 * Wenn das passiert wird die statische Variable con null
				 * gesetzt und gibt Informationen und verhilft bei der
				 * Ermittlung der Ausnahme.
				 */

			} catch (Exception e) {
				con = null;
				e.printStackTrace();
				/*
				 * wenn der �bergabeparameter (e) negativ oder null ist, wird
				 * eine neue Exception erstellt und geworfen
				 */
				throw new RuntimeException("Das hat nicht funktioniert!" + e.getMessage().toString()
						+ "Versuchte Infos: " + user + ", " + pass + ", " + url);

			}
		}

		// Die Referenz auf Connection wird zur�ckgegeben.
		return con;
	}
}