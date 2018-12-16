package de.hdm.pinit.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.pinit.shared.bo.Subscription;

public class SubscriptionMapper {

	private static SubscriptionMapper subscriptionMapper = null;

	/**
	 * Die Zugriffsvariable auf den No-Argument Konstruktor muss protected oder
	 * private sein, damit eine Instanziierung von au�en nicht zul�ssig ist. Es
	 * soll verhindert werden, dass �ber den new-Operator neue Instanzen dieser
	 * Klasse erzeugt werden.
	 */
	protected SubscriptionMapper() {
	}

	/**
	 * statische Methode, um eine Instanz von <code>SubscriptionMapper</code> zu
	 * erstellen. Wenn es schon eine Instanz dieser Klasse gibt, wird diese
	 * einfach zur�ckgegeben.
	 * 
	 * @return subscriptionMapper
	 */
	public static SubscriptionMapper subscriptionMapper() {
		if (subscriptionMapper == null) {
			subscriptionMapper = new SubscriptionMapper();
		}

		return subscriptionMapper;
	}

	/**
	 * Einf�gen eines Subscription-Objektes in die DB
	 */

	public Subscription insert(Subscription s) {

		/*
		 * zuerst muss eine Verbindung zur DB hergestellt werden. �ber den
		 * staischen Methodenaufruf wird die Verbindung zur DB geholt und in der
		 * Referenzvariable con gespeichert.
		 */
		Connection con = DBConnection.connection();

		try {
			/*
			 * Objekt von Statement erzeugen, damit die SQL Anweisungen an die
			 * DB gesendet werden k�nnen. Werden in der Referenzvariable stmt
			 * gespeichert.
			 */
			Statement stmt = con.createStatement();

			/*
			 * Das ResultSet stellt unsere Ergebnisse in einer Tabelle dar.
			 * ExecuteQuery gibt ein einzelnes ResultSet zur�ck. Gibt, wenn
			 * vorhanden, ein einzeiliges Ergebnis zur�ck. Hier �berpr�fen wir,
			 * was der momentan h�chste Prim�rschl�sselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid " + "FROM subscription ");

			/*
			 * Next ist wie ein Iterator, setzt immer auf das aktuelle Element
			 * in der Ergebnistabelle und gibt true zur�ck, wenn ein n�chstes
			 * Element existiert und false, wenn keins existiert.
			 */
			if (rs.next()) {

				/*
				 * s erh�lt den bisher maximalen, nun um 1 inkrementierten
				 * Prim�rschl�ssel.
				 */
				s.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				/*
				 * Einf�geoperation. Damit die Objekte auf die Tabelle
				 * abgebildet werden. Wir holen uns �ber unser
				 * SubscriptionObjekt die Werte, die auf die Tabelle abgebildet
				 * werden sollen.
				 */
				stmt.executeUpdate("INSERT INTO subscription (nutzerid, pinboardid) " + "VALUES (" + s.getUserId()
						+ "','" + s.getPinboardId() + "')");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		/*
		 * R�ckgabe der Subscription, aus stilistischen Gr�nden.
		 */

		return s;
	}

	/**
	 * L�schen der Daten eines <code>Subscription</code>-Objekts aus der
	 * Datenbank.
	 */
	public void delete(Subscription s) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE FROM subscription " + "WHERE id=" + s.getId() 
					+ "AND userid=" + s.getUserId()
					+ "AND pinboardid=" + s.getPinboardId()
					);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Auslesen aller <code>Subscription</code>-Objekte nach Nutzern
	 */
	public Vector<Subscription> findByUserId(int userId) {
		Connection con = DBConnection.connection();

		Vector<Subscription> sub = new Vector<Subscription>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT userid, pinboardid " + "FROM subscription " + "WHERE userid=" + userId);

			/*
			 * F�r jeden Eintrag in der Ergebnistabelle wird ein Objekt
			 * abgebildet. Diese Ergebnisse werden in ein Subscription- Objekt
			 * mit den Setter-Methoden rein gepackt.
			 */
			while (rs.next()) {
				Subscription s = new Subscription();
				s.setUserId(rs.getInt("userid"));
				s.setPinboardId(rs.getInt("pinboardid"));

				sub.add(s);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return sub;
	}

	/**
	 * Auslesen aller <code>Subscription</code>-Objekte nach Pinnw�nden
	 */
	public Vector<Subscription> findByPinboardId(int pinboardId) {
		Connection con = DBConnection.connection();

		Vector<Subscription> sub = new Vector<Subscription>();

		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(
					"SELECT userid, pinboardid " + "FROM subscription " + "WHERE pinboardid LIKE '" + pinboardId);

			/*
			 * F�r jeden Eintrag in der Ergebnistabelle wird ein Objekt
			 * abgebildet. Diese Ergebnisse werden in ein Subscription- Objekt
			 * mit den Setter-Methoden rein gepackt.
			 */
			while (rs.next()) {
				Subscription s = new Subscription();
				s.setUserId(rs.getInt("userid"));
				s.setPinboardId(rs.getInt("pinboardid"));

				sub.add(s);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return sub;
	}

}
