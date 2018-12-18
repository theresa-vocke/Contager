package de.hdm.pinit.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.pinit.shared.bo.Posting;

public class PostingMapper {
	
private static PostingMapper postingMapper = null;

	protected PostingMapper(){
		
	}

	public static PostingMapper postingMapper() {
		if (postingMapper == null) {
			postingMapper = new PostingMapper();
		}

		return postingMapper;
	}
	
	public Posting findPostingById(int id) {
		// DB-Verbindung holen
		Connection con = DBConnection.connection();

		try {
			// Leeres SQL-Statement (JDBC) anlegen
			Statement stmt = con.createStatement();

			// Statement ausf�llen und als Query an die DB schicken
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM posting " + "WHERE id=" + id);

			/*
			 * Da id Prim�rschl�ssel ist, kann max. nur ein Tupel zur�ckgegeben
			 * werden. Pr�fe, ob ein Ergebnis vorliegt.
			 */
			if (rs.next()) {
				// Ergebnis-Tupel in Objekt umwandeln
				Posting p = new Posting();
				p.setId(rs.getInt("id"));
				p.setCreatedate(rs.getTimestamp("createdate"));
				p.setPinboardId(rs.getInt("pinboardid"));
				p.setText(rs.getString("text"));
					
				return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
	
	public Vector<Posting> findPostingsByPinboard(int pinboardId){
		
		Connection con = DBConnection.connection();
		Vector<Posting> pos = new Vector<Posting>();
			try{
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, createdate, pinboardid, text"
						+ " FROM posting " + "WHERE pinboardid= " + pinboardId);
				
				
				while(rs.next()){
					// Ergebnis-Tupel in Objekt umwandeln
					Posting p = new Posting();
					p.setId(rs.getInt("id"));
					p.setCreatedate(rs.getTimestamp("createdate"));
					p.setPinboardId(rs.getInt("pinboardid"));
					p.setText(rs.getString("text"));
					
					pos.add(p);
				}
		}
			catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
			return pos;
		}
	

	public Posting insert(Posting p) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();

			/*
			 * Zun�chst schauen wir nach, welches der momentan h�chste
			 * Prim�rschl�sselwert ist.
			 */
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid " + "FROM posting");

			// Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
			if (rs.next()) {
				/*
				 * n erh�lt den bisher maximalen, nun um 1 inkrementierten
				 * Prim�rschl�ssel.
				 */
				p.setId(rs.getInt("maxid") + 1);

				stmt = con.createStatement();

				// Jetzt erst erfolgt die tats�chliche Einf�geoperation
				stmt.executeUpdate("INSERT INTO posting (id, createdate, pinboardid, text) " + "VALUES (" + p.getId() + ",'"
						+ p.getCreateDate() + "','" + p.getPinboardId() + "','" + p.getText() + "')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return p;
	}

	/**
	 * 
	 */
	public void delete(Posting p) {
		Connection con = DBConnection.connection();

		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate("DELETE FROM posting " + "WHERE id=" + p.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
