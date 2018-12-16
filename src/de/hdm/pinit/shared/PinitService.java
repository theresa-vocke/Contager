package de.hdm.pinit.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Vector;
import de.hdm.pinit.shared.bo.Pinboard;
import de.hdm.pinit.shared.bo.Posting;
import de.hdm.pinit.shared.bo.Subscription;
import de.hdm.pinit.shared.bo.User;

/**
 * Synchrone Schnittstelle f�r eine RPC-f�hige Klasse. Erweitert das Interface
 * RemoteService, damit der RPC-Mechanismus verwendet werden kann. F�hrt alle
 * RRC-Methoden auf.
 * 
 * @RemoteServiceRelativePath: dem Dienst wird ein Standardpfad relativ zur
 *                             Basis-URL des Moduls zugeordnet.
 */
@RemoteServiceRelativePath("pinitservice")
public interface PinitService extends RemoteService {

	/**
	 * die init-Methode wird ben�tigt, um Instanzen der Mapper bei der
	 * Instantiierung innerhalb der PinitServiceImpl zu erstellen.
	 */	

	public void init() throws IllegalArgumentException;

	/**
	 *  User anlegen
	 */

	public User createUser(String nickname, String email) throws IllegalArgumentException;

	/**
	 * getUserById, um dann User auszulesen f�r den die Pinnwand angelegt werden soll
	 */

	public User getUserById(int id) throws IllegalArgumentException;

	public Pinboard createPinboard(int ownerid);
	
	/**
	 * getPinboardByOwner evtl. dass das Pinboard dann direkt angezeigt wird, wenn der Login abgeschlossen ist
	 */

	public Subscription createSubscription(int userId, String nickname) throws IllegalArgumentException;

	public Vector<Subscription> getSubscriptionByUser(int userId) throws IllegalArgumentException;

	public Vector<User> getAllUser() throws IllegalArgumentException;

	public User checkUser(String email) throws IllegalArgumentException;

	public User getOwnerByPinboard(Pinboard p) throws IllegalArgumentException;

	public Pinboard getPinboardBySubscription(Subscription s) throws IllegalArgumentException;

	public Vector<User> getAllSubscriptionsByUser(int userId) throws IllegalArgumentException;

	public Pinboard getPinboardByOwner(int userId);
	
	public void deleteSubscription(int userId, String nickname) throws IllegalArgumentException;

	public User getUserByNickname(String nickname) throws IllegalArgumentException;

	public Vector<Posting> getPostingsByPinboard(int id);

	public Vector<Posting> getPostingsByPinboardOwner(int ownerId);

	public void deletePosting(Posting p);

	public Posting createPosting(int pinboardId, String text);

	public Boolean checkSubscription(int userId, String nickname);

	public Posting createPostingForPinboard(int userId, String text);

	
}
