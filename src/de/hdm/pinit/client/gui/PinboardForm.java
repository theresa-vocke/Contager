package de.hdm.pinit.client.gui;

import java.util.Vector;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.pinit.shared.PinitServiceAsync;
import de.hdm.pinit.shared.bo.Pinboard;
import de.hdm.pinit.shared.bo.Posting;
import de.hdm.pinit.shared.bo.User;

public class PinboardForm extends VerticalPanel {

	PinitServiceAsync pinitService = ClientSideSettings.getPinitService();

	HorizontalPanel btnpanel = null;
	VerticalPanel content = new VerticalPanel();
	VerticalPanel beitragPanel = null;
	final FlexTable flexx = new FlexTable();

	Button beitragBtn = new Button("Pin It!");
	Label user = new Label();
	TextArea text = new TextArea();

	int cookie = 0;
	int pinnwandId = 0;

	Label testlabel = null;
	Label datum = null;

	// alles f�r das Anzeigen von Beitr�gen
	String nickname = null;
	int currentUser = 0;

	Button removeBtn = new Button("Nutzer l�schen");

	VerticalPanel postingContent = new VerticalPanel();

	final FlexTable postingTable = new FlexTable();

	ScrollPanel postingScroll = new ScrollPanel();

	VerticalPanel textPanel = null;

	Label textContent = null;
	Label createdate = null;

	public PinboardForm(final int userId) {
		this.cookie = userId;
		text.setVisibleLines(2);

		pinitService.getUserById(userId, new UserAsyncCallback());
		pinitService.getPinboardByOwner(userId, new PinboardCallBack());
		//pinitService.getPostingsByPinboardOwner(userId, new LoadAllCallback());
		// //postingbeitragCallback());

		content.add(user);
		content.add(text);
		content.add(beitragBtn);
		content.add(flexx);
		this.add(content);

		// f�r das Anzeigen von Beitr�gen
		this.currentUser = userId;
		postingScroll.setSize("900px", "550px");
		
		//pinitService.getUserById(userId, new UserAsyncCallback());

		removeBtn.addClickHandler(new UserDieClickHandler());
		removeBtn.setStylePrimaryName("remove-button");

		postingScroll.add(postingTable);

		this.add(postingScroll);
		this.add(removeBtn);
	}

	public class UserAsyncCallback implements AsyncCallback<User> {

		@Override
		public void onSuccess(User result) {

			user.setText("Schreibe einen neuen Beitrag auf deine Pinnwand:");
			user.setStylePrimaryName("label-posting");

			pinitService.getPostingsByPinboardOwner(result.getId(), new LoadAllCallback());
			nickname = result.getNickname();
		}

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fehler beim Laden " + caught.getMessage());
		}
	}

	public class PinboardCallBack implements AsyncCallback<Pinboard> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fehler beim Laden " + caught.getMessage());
		}

		@Override
		public void onSuccess(Pinboard result) {

			beitragBtn.addClickHandler(new NewClickhandler());

		}
	}

	public class NewClickhandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			String input = text.getValue();
			pinitService.createPostingForPinboard(Integer.parseInt(Cookies.getCookie("id")), input,
					new NewPostingCallback());
		}
	}

	// Speichern eines neuen Beitrags
	public class NewPostingCallback implements AsyncCallback<Posting> {

		@Override
		public void onFailure(Throwable arg0) {
			Window.alert("Der neue Beitrag wurde nicht gespeichert");

		}

		@Override
		public void onSuccess(Posting arg0) {
			Window.alert("Der neue Beitrag wurde tatsaechlich gespeichert");
		
			RootPanel.get("details").clear();
			RootPanel.get("details").add(new PinboardForm(Integer.parseInt(Cookies.getCookie("id"))));
			//new LoadAllCallback();
		}

	}

	// alles f�r das Anzeigen von Beitr�gen

	public class LoadAllCallback implements AsyncCallback<Vector<Posting>> {

		@Override
		public void onFailure(Throwable arg0) {
			Window.alert("Das war wohl nix.");

		}

		@Override
		public void onSuccess(Vector<Posting> result) {
			if (result.size() != 0) {
				for (Posting posting : result) {

					textPanel = new VerticalPanel();

					int rows = postingTable.getRowCount();

					textContent = new Label();
					createdate = new Label();

					createdate.setText(posting.getCreatedate().toString());

					final Grid postingGrid = new Grid(2, 2);
					textContent.setStylePrimaryName("posting-content");
					textContent.setText(posting.getText());

					postingGrid.setWidget(0, 0, textContent);
					postingGrid.setWidget(1, 0, createdate);

					postingGrid.setStylePrimaryName("posting-grid");

					postingTable.setWidget(rows, 0, textPanel);

					textPanel.add(postingGrid);

				}
			} else {
				Window.alert("Momentan noch keine Beitr�ge vorhanden!");
			}
		}

	}

	public class UserDieClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// hier muss der User gel�scht werden

		}
	}

}
