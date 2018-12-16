package de.hdm.pinit.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdm.pinit.shared.bo.Pinboard;
import de.hdm.pinit.server.db.DBConnection;

/**
 * Mapper-Klassen dienen dazu, Objekte auf eine relationale DB abzubilden.
 * Das Verfahren des Mappings verl�uft bidirektional, das hei�t, dass die
 * Objekte in Tupel umgewandelt werden und die Tupel in Objekte. Hier werden
 * Methoden definiert, die zum Beispiel zur Erstellung, Modifizierung und
 * Suche von Pinboard-Objekten verhelfen.
 */

/**
 * Genauso wie in der DBConnection, muss die Singleton-Eigenschaft gegeben sein,
 * das hei�t, jede Mapper-Klasse wird nur einmal instanziiert. Soll eine Instanz
 * dieser Klasse erstellt werden, wird immer auf diese Klassenvariable
 * zur�ckgegriffen. Sie speichert die einzige Instanz dieser Klasse.
 */

public class PinboardMapper {

	private static PinboardMapper pinboardMapper = null;

	/**
	 * Die Zugriffsvariable auf den No-Argument Konstruktor muss protected oder
	 * private sein, damit eine Instanziierung von au�en nicht zul�ssig ist. Es soll
	 * verhindert werden, dass �ber den new-Operator neue Instanzen dieser Klasse
	 * erzeugt werden.
	 */
	protected PinboardMapper() {
	}

	/**
	 * statische Methode, um eine Instanz von <code>PinboardMapper</code> zu
	 * erstellen. Wenn es schon eine Instanz dieser Klasse gibt, wird diese einfach
	 * zur�ckgegeben.
	 * 
	 * @return pinboardMapper
	 */
	public static PinboardMapper pinboardMapper() {
		if (pinboardMapper == null) {
			pinboardMapper = new PinboardMapper();
		}

		return pinboardMapper;
	}

	/**
	 * Einf�gen eines Pinboard-Objektes in die DB
	 */

	public Pinboard insert(Pinboard p) {

		/*
		 * zuerst muss eine Verbindung zur DB hergestellt werden. �ber den staischen
		 * Methodenaufruf wird die Verbindung zur DB geholt und in der Referenzvariable
		 * con gespeichert.
		 */
		Connection con = DBConnection.connection();

		try {
			/*
			 * Objekt von Statement erzeugen, damit die SQL Anweisungen an die DB gesendet
			 * werden k�nnen. Werden in der Referenzvariable stmt gespeichert.
			 */
			Statement stmt = con.createStatement();

			/*
			 * Das ResultSet stellt unsere Ergebnisse in einer Tabelle dar. ExecuteQuery
			 * gibt ein einzelnes ResultSet zur�ck. Gibt, wenn vorhanden, ein einzeiliges
			 * Ergebnis zur�ck. Hier �berpr�fen wir, was der momentan h�chste
			 * Prim�rschl�sselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid " + "FROM pinboard");

			/*
			 * Next ist wie ein Iterator, setzt immer auf das aktuelle Element in der
			 * Ergebnistabelle und gibt true zur�ck, wenn ein n�chstes Element existiert und
			 * false, wenn keins existiert.
			 */
			if (rs.next()) {

				/*
				 * p erh�lt den bisher maximalen, nun um 1 inkrementierten Prim�rschl�ssel.
				 */
				p.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				/*
				 * Einf�geoperation. Damit die Objekte auf die Tabelle abgebildet werden. Wir
				 * holen uns �ber unser PinboardObjekt die Werte, die auf die Tabelle abgebildet
				 * werden sollen.
				 */
				stmt.executeUpdate("INSERT INTO pinboard (id, createdate, ownerid) " + "VALUES (" + p.getId() + ",'"
						+ p.getCreateDate() + "','" + p.getOwnerId() + "')");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		/*
		 * R�ckgabe des Pinboards, aus stilistischen Gr�nden.
		 */

		return p;
	}

	/**
	 * L�schen der Daten eines <code>Pinboard</code>-Objekts aus der Datenbank.
	 */
	public void delete(Pinboard p) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE FROM pinboard " + "WHERE ownerid=" + p.getOwnerId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Auslesen aller <code>Pinboard</code>-Objekte nach �bergebener UserId
	 */
	public Pinboard findByOwner(int userId) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT id, createdate, ownerid FROM pinboard "
					+ "WHERE ownerid=" + userId);

			/*
			 * F�r jeden Eintrag in der Ergebnistabelle wird ein Objekt abgebildet. Diese
			 * Ergebnisse werden in ein UserObjekt mit den Setter-Methoden rein gepackt.
			 */
			while (rs.next()) {
				Pinboard p = new Pinboard();
				p.setId(rs.getInt("id"));
				p.setCreateDate(rs.getTimestamp("createdate"));
				p.setOwnerId(rs.getInt("ownerid"));

				return p;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	/**
	 * Auslesen eines <code>Pinboard</code>-Objekts nach der �bergebenen PinboardId aus der
	 * SubscriptionTabelle
	 */
	public Pinboard findById(int pinboardId) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT id, createdate, ownerid FROM pinboard " + "WHERE id="
					+ pinboardId);

			/*
			 * F�r jeden Eintrag in der Ergebnistabelle wird ein Objekt abgebildet. Diese
			 * Ergebnisse werden in ein UserObjekt mit den Setter-Methoden rein gepackt.
			 */
			while (rs.next()) {
				Pinboard p = new Pinboard();
				p.setId(rs.getInt("id"));
				p.setCreateDate(rs.getTimestamp("createdate"));
				p.setOwnerId(rs.getInt("ownerid"));

				return p;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
}
