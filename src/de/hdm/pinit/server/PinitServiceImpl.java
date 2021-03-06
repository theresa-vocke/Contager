package de.hdm.pinit.server;

import java.sql.Timestamp;
import java.util.Vector;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import de.hdm.pinit.server.db.PinboardMapper;
import de.hdm.pinit.server.db.PostingMapper;
import de.hdm.pinit.server.db.SubscriptionMapper;
import de.hdm.pinit.server.db.UserMapper;
import de.hdm.pinit.shared.PinitService;
import de.hdm.pinit.shared.bo.Pinboard;
import de.hdm.pinit.shared.bo.Posting;
import de.hdm.pinit.shared.bo.Subscription;
import de.hdm.pinit.shared.bo.User;

/**
 * Die serversetigie Implementation des RPC Services. Die Impl Klasse stellt 
 * unser serverseitiges Servlet dar, durch die Erweiterung des 
 * RemoteServiceServlets. Das zu erweiternde RemoteServiceServlet stellt die 
 * Basis zur Verf�gung, dass der RPC-Mechanismus verwendet werden kann.  
 * Sie enth�lt s�mliche Methoden die die Applikationslogik definieren. Hier 
 * werden die Funktionen und Zusammenh�nge unserer Daten und Abl�ufe organisiert. 
 * Gemeinsam mit dem synchronen Interface bildet PinitServiceImpl die 
 * serverseitige Sicht der Applikationslogik ab. 
 * 
 */

/**
 * Compiler erwartet eine SerialVersionUniqueID, f�r alle serialisierbaren
 * Klassen. Wird keine Default-ID vergeben, so unterdr�ckt die Annotation die
 * Warnung. RemoteServiceServlet implementiert das Interface IsSerializable.
 * 
 * @author Miescha
 *
 */
@SuppressWarnings("serial")
public class PinitServiceImpl extends RemoteServiceServlet implements PinitService {

	/**
	 * Referenz auf den DatenbankMapper, der Userobjekte mit der Datenbank
	 * abgleicht.
	 */
	private UserMapper uMapper = null;

	/**
	 * Referenz auf den DatenbankMapper, der Pinboardobjekte mit der Datenbank
	 * abgleicht.
	 */
	private PinboardMapper pMapper = null;

	/**
	 * Referenz auf den DatenbankMapper, der Pinboardobjekte mit der Datenbank
	 * abgleicht.
	 */
	private SubscriptionMapper sMapper = null;

	/**
	 * Referenz auf den DatenbankMapper, der Postingobjekte mit der Datenbank
	 * abgleicht.
	 */
	private PostingMapper poMapper = null;

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Start - Initialisierung
	 * ________________________________________________________________________
	 */

	/**
	 * <p>
	 * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	 * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu ist
	 * ein solcher No-Argument-Konstruktor anzulegen.Bei diesem Aufruf k�nnen dem
	 * Konstruktor keine Werte �bergebene werden, weshalb die Init-Methode die
	 * Initalisierung der Mapperinstanzen ausf�hren muss. Immer wenn eine Instanz
	 * der ServletImplementierungsklasse instanziiert wird, muss die init-Methode
	 * direkt nach <code>GWT.create(Klassenname.class)</code> aufgerufen werden. Bei
	 * der ersten Erzeugung des Proxy beim Laden der onModuleLoad() wird die Instanz
	 * des Servlets erzeugt, wor�ber zun�chst das Servlet zum ersten Mal geladen
	 * wird. Wird das Servlet �ber eine HTTP-Anfrage angesprochen, so wird diese
	 * einzige konfigurierte Instanz des Servlets zur�ckgegeben. Der
	 * NoArgumentConstructors wird lediglich dahingehend erweitert, die
	 * Ausnahmebehandlung durchf�hren zu k�nnen.
	 */
	public PinitServiceImpl() throws IllegalArgumentException {
	}

	@Override
	public void init() throws IllegalArgumentException {

		this.uMapper = UserMapper.userMapper();
		this.pMapper = PinboardMapper.pinboardMapper();
		this.sMapper = SubscriptionMapper.subscriptionMapper();
		this.poMapper = PostingMapper.postingMapper();
	}
	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Ende - Initialisierung
	 * ________________________________________________________________________
	 */

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Start - Methoden f�r User-Objekte
	 * ________________________________________________________________________
	 */

	/**
	 * Anlegen eines Nutzers. Dies f�hrt zu einer Speicherung bzw. Ablage in der
	 * Datenbank. Wenn ein Nutzer angelegt wird, wird eine Pinnwand erstellt und ihm
	 * zugeordnet.
	 */
	@Override
	public User createUser(String nickname, String email) throws IllegalArgumentException {
		User u = new User();

		u.setNickname(nickname);
		u.setEmail(email);
		u.setCreateDate(new Timestamp(System.currentTimeMillis()));

		/*
		 * Setzen einer vorl�ufigen ID, die in der DB nachtr�glich richtig zugeordnet
		 * wird.
		 */
		u.setId(1);

		// U-Objekt in der Db speichern.
		return this.uMapper.insert(u);
	}

	/**
	 * Ein Nutzer wird anhand seiner ID gesucht.
	 */
	@Override
	public User getUserById(int id) throws IllegalArgumentException {

		return this.uMapper.findById(id);
	}

	/**
	 * Alle Nutzer werden ausgelesen und in einem Vektor zur�ckgegeben.
	 */

	@Override
	public Vector<User> getAllUser() {
		Vector<User> u = new Vector<User>();
		u = this.uMapper.findAll();

		return u;
	}

	/**
	 * Anhand der �bergebenen E-Mail wird �berpr�ft, ob sich der Nutzer mit der
	 * entsprechenden Email bereits registriert hat.
	 */

	@Override
	public User checkUser(String email) throws IllegalArgumentException {
		/*
		 * Leerer Vektor, welchem alle in der DB gespeicherten Nutzer zugewiesen werden.
		 */
		Vector<User> u = getAllUser();
		/*
		 * Jeder einzelne Nutzer innerhalb der Liste wird �berpr�ft.
		 */
		for (User user : u) {
			/*
			 * Dies geschieht so lange, bis die �bergebene EMail mit der eines Nutzers (im
			 * Vektor) �bereinstimmt.
			 */
			if (email.equals(user.getEmail())) {
				// Der User wird zur�ckgegeben.
				return user;
			}
		}
		/*
		 * Falls nicht, wird nichts zur�ck gegeben und der aktuelle Nutzer muss sich
		 * dann registrieren.
		 */
		return null;
	}

	/**
	 * F�r einen User wird anhand seiner ID �berpr�ft, welche anderen User (mit
	 * deren Pinnw�nde) er abonniert hat.
	 */
	@Override
	public Vector<User> getAllSubscriptionsByUser(int id) throws IllegalArgumentException {
		/*
		 * leerer Vektor wird angelegt, in welchen dann alle abonnierten User
		 * gespeichert werden.
		 */
		Vector<User> u = new Vector<User>();
		/*
		 * leerer Vektor wird angelegt, in welchen alle Pinnw�nde gespeicher werden, die
		 * der User abonniert hat.
		 */
		Vector<Pinboard> p = new Vector<Pinboard>();
		/*
		 * Vektor wird angelegt, in welchen alle einzelnen Abos des Users direkt
		 * gespeichert werden. Anhand eines Nutzers mit �bergebener userID.
		 */
		Vector<Subscription> s = this.getSubscriptionByUser(id);

		/*
		 * Zu jedem einzeln gespeicherte Abo im Subscription Vektor wird die dazu
		 * geh�rende Pinnwand geholt. F�r jedes Exemplar in diesem Vektor wird die
		 * Schleife ausgef�hrt.
		 */
		for (Subscription subscription : s) {
			/*
			 * Zu dem zuvor angelegten leeren Pinnwand Vektor werden nun die abonnierten
			 * Pinnw�nde hinzugef�gt.
			 */
			p.add(getPinboardBySubscription(subscription));
		}
		/*
		 * Zu jedem einzeln gespeicherte Pinboard-Objekt im Pinnwand Vektor wird der
		 * dazu geh�rige User geholt. F�r jedes Exemplar in diesem Vektor wird die
		 * Schleife ausgef�hrt.
		 */
		for (Pinboard pinboard : p) {
			/*
			 * Zu dem zuvor angelegten leeren User Vektor werden nun die entsprechenden
			 * Eigent�mer Pinnw�nde ausgelesen und dem Vektor hinzugef�gt.
			 */
			u.add(getOwnerByPinboard(pinboard));
		}
		/*
		 * Alle Owner der abonnierten Pinnw�nde, welcher der �bergebene Nutzer abonniert
		 * hat, werden als Vektor zur�ck gegeben.
		 */
		return u;
	}

	/**
	 * Der Eigent�mer einer Pinnwand wird ausgelesen.
	 */
	@Override
	public User getOwnerByPinboard(Pinboard p) throws IllegalArgumentException {
		User u = new User();
		u = this.getUserById(p.getOwnerId());
		return u;
	}

	@Override
	public User getUserByNickname(String nickname) throws IllegalArgumentException {
		return this.uMapper.findByNickname(nickname);
	}

	
	
	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Ende - Methoden f�r User-Objekte
	 * ________________________________________________________________________
	 */

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Start - Methoden f�r Pinboard-Objekte
	 * ________________________________________________________________________
	 */

	/**
	 * Anlegen eines Pinboards bei Registrierung des Nutzers. Dies f�hrt zu einer
	 * Speicherung bzw. Ablage in der Datenbank.
	 */
	@Override
	public Pinboard createPinboard(int ownerId) throws IllegalArgumentException {

		Pinboard p = new Pinboard();

		p.setOwnerId(ownerId);
		p.setCreateDate(new Timestamp(System.currentTimeMillis()));

		p.setId(1);

		return this.pMapper.insert(p);
	}

	/**
	 * Suche nach einer Pinnwand anhand des dazugeh�rigen Nutzers.
	 */
	@Override
	public Pinboard getPinboardByOwner(int userId) throws IllegalArgumentException {
		init();
		return this.pMapper.findByOwner(userId);
	}
	
	@Override
	public Posting createPostingForPinboard(int userId, String text) throws IllegalArgumentException {
		
		Pinboard p = this.getPinboardByOwner(userId);

		Posting po = new Posting();
		po.setPinboardId(p.getId());
		po.setText(text);
		po.setCreateDate(new Timestamp(System.currentTimeMillis()));

		po.setId(1);
		

		return poMapper.insert(po);
	}

	/**
	 * Eine Pinnwand wird anhand eines �bergebenen Abo-Objektes ausgelesen.
	 */
	@Override
	public Pinboard getPinboardBySubscription(Subscription s) throws IllegalArgumentException {
		init();
		/*
		 * �ber das �bergebene Abo-Objekt wird auf die Pinnwand-ID zugegriffen und
		 * dar�ber wird ein tats�chlichen Pinnwand-Objekt in der DB gesucht.
		 */
		Pinboard p = pMapper.findById(s.getPinboardId());
		return p;
	}

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Ende - Methoden f�r Pinboard-Objekte
	 * ________________________________________________________________________
	 */

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Start - Methoden f�r Subscription-Objekte
	 * ________________________________________________________________________
	 */

	/**
	 * Anlegen eines Abonnements. Dies f�hrt zu einer Speicherung bzw. Ablage in der
	 * Datenbank. Einem Nutzer wird eine Pinboard zugewiesen. Die kann sich auf
	 * seine eigene Pinnwand beziehen oder jedoch auf eine Pinnwand eines anderen
	 * Nutzers, die abonniert wird.
	 */
	@Override
	public Subscription createSubscription(int userId, String nickname) throws IllegalArgumentException {

		User u = this.getUserByNickname(nickname);
		Pinboard p = this.getPinboardByOwner(u.getId());
		
		Subscription s = new Subscription();

		s.setUserId(userId);
		s.setPinboardId(p.getId());
		s.setCreateDate(new Timestamp(System.currentTimeMillis()));

		s.setId(1);

		return this.sMapper.insert(s);
	}

	/**
	 * Suche nach dem Nutzer, dem eine Pinnwand zugeordnet ist. Dies kann sich auf
	 * seine eigene Pinnwand beziehen oder jedoch auf eine Pinnwand eines anderen
	 * nutzers, die abonniert wurde.
	 */
	@Override
	public Vector<Subscription> getSubscriptionByUser(int userId) throws IllegalArgumentException {

		return this.sMapper.findByUserId(userId);
	}

	@Override
	public void deleteSubscription(int userId, String nickname) throws IllegalArgumentException {
		User u = this.getUserByNickname(nickname);
		Pinboard p = this.getPinboardByOwner(u.getId());
		Subscription s = new Subscription();
		s.setUserId(userId);
		s.setPinboardId(p.getId());
		s.setId(1);
		
		this.sMapper.delete(s);
		
		return;

	}
	
	@Override
	public Boolean checkSubscription(int userId, String nickname) {
		Vector<User> u = this.getAllSubscriptionsByUser(userId);
		
		for (User user : u) {
			if (user.getNickname().equals(nickname)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Ende - Methoden f�r Subscription-Objekte
	 * ________________________________________________________________________
	 */

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Start - Methoden f�r Pinboard-Objekte
	 * ________________________________________________________________________
	 */

	/**
	 * Anlegen eines Postings, wobei ein Nutzer hinterlegt wird. Dies f�hrt zu einer
	 * Speicherung bzw. Ablage in der Datenbank.
	 */
	@Override
	public Posting createPosting(int pinboardId, String text) {

		Posting p = new Posting();

		p.setPinboardId(pinboardId);
		p.setText(text);
		p.setCreateDate(new Timestamp(System.currentTimeMillis()));

		p.setId(1);
		

		return poMapper.insert(p);
	}

	/**
	 * Auslesen aller Beitr�ge die einer bestimmten Pinboard zugeordnet sind.
	 */
	@Override
	public Vector<Posting> getPostingsByPinboard(int id) {

		return this.poMapper.findPostingsByPinboard(id);
	}

	/**
	 * Alle Beitr�ge einer Pinnwand anzeigen lassen, wovon der Nutzer der Eigent�mer
	 * ist.
	 */
	@Override
	public Vector<Posting> getPostingsByPinboardOwner(int ownerId) {

		Pinboard p = this.getPinboardByOwner(ownerId);
		Vector<Posting> po = this.getPostingsByPinboard(p.getId());
		return po;
	}

	/**
	 * Einen bestimmten Beitrag einer Pinnwand l�schen.
	 * 
	 */
	@Override
	public void deletePosting(Posting p) {

		this.poMapper.delete(p);
	}

	/*
	 * ________________________________________________________________________
	 * ABSCHNITT Ende - Methoden f�r Subscription-Objekte
	 * ________________________________________________________________________
	 */
}
