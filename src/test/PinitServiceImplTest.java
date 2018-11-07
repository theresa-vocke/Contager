//package test;
//
//import static org.junit.Assert.assertTrue;
//import de.hdm.pinit.server.PinitServiceImpl;
//import org.junit.jupiter.api.Test;
//
//import com.google.gwt.junit.client.GWTTestCase;
//
//public class PinitServiceImplTest extends GWTTestCase {
//
//		/**
//		 * Ben�tigte Methode diese implementiert werden muss, um auf den korrekten Pfad der Klasse zugreifen zu k�nnen.  
//		 * Hier wird der Modulpfad der gwt.xml-Datei eingetragen.
//		 */
//		public String getModuleName() {
//			return "de.hdm.pinit.server.PinitServiceImpl";
//		}
//		
//		/**
//		 * Dient zur �berpr�fung ob die Methoden und Testdurchf�hrung funktionieren.
//		 */
//
//		public void testSimple() {
//			assertTrue(true);
//		}
//		
//		/*
//		   * ***************************************************************************
//		   * ABSCHNITT, Anfang: Methoden fuer Nutzer-Objekte Test
//		   * ***************************************************************************
//		   */
//		
//		/**
//		 * Test Case 
//		 */
//		
//		public void createUser() {
//			
//			
//			String email = "tm@gmail.com";
//			String nickname = "tm";
//					
//			//Verbindung zur Test Klasse herstellen 
//			PinitServiceImpl pinit = new PinitServiceImpl();
//			
//			assertNotNull(pinit);
//			
//			
//			pinit.createUser(email, nickname);
//		}
//		
//	
//		
//		
//}
