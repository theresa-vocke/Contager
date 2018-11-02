package de.hdm.pinit.shared;

import java.sql.Timestamp;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.pinit.shared.bo.Pinboard;
import de.hdm.pinit.shared.bo.User;

/**
 * Asyncrones Gegenst�ck zu <code>PinitService</code>.
 */
public interface PinitServiceAsync {

	void createUser(String nickname, String email, AsyncCallback<User> callback);

	void getUserById(int id, AsyncCallback<User> callback);

	void init(AsyncCallback<Void> callback);

	void createPinboard(User u, AsyncCallback<Pinboard> callback);

	void getPinboardByOwner(User u, AsyncCallback<Pinboard> callback);
	
	
}
