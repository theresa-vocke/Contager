package de.hdm.pinit.client.gui;

import java.util.Vector;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import de.hdm.pinit.shared.PinitServiceAsync;
import de.hdm.pinit.shared.bo.User;

public class PinboardCellList extends VerticalPanel {

	// Leeres Panel, worin sp�ter die Celllist platziert wird.
	VerticalPanel cellListPanel = new VerticalPanel();

	// Neue Zelle, die sp�ter in die Celllist geladen wird.
	PinboardCell cell = new PinboardCell();
	
	// Leeres Abonnement-Formular, dieses sp�ter bei Auswahl einer Aktion ge�ffnet wird
	//SubscriptionForm subscriptionForm = new SubscriptionForm();

	// Zugriff auf unsere Async-Methoden �ber unser Proxy
	PinitServiceAsync pinitService = ClientSideSettings.getPinitService();

	/*
	 * Definieren eines Schl�ssels f�r ein Nutzer-Objekt, damit dieser bei
	 * Ver�nderung beibehalten bzw. identifiziert werden kann
	 * 
	 */
	ProvidesKey<User> userKeyProvider = new ProvidesKey<User>() {

		/*
		 * Die Methode getKey ist Inhalt der anonymen Klasse ProvidesKey Die Methode
		 * kann deshalb nur an dieser Stelle verwendet werden
		 */
		public Object getKey(User u) {

			// Pr�fung ob ein User-Objekt vorhanden ist
			return (u == null) ? null : u.getId();
		}
		// Semikolon da Syntax f�r die Anweisung der anonyme Klasse zu schlie�en
	};

	/*
	 * Um die Handlung des Benutzers zu steuern, ben�tigen wir das Interface
	 * SingleSelectionModel Damit es f�r immer festgelegt ist wird es als Final
	 * deklariert
	 */
	final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>();

	// CellList anlegen und die jeweiligen Zellen mit dem zugeh�rigen Schl�ssel
	// �bergeben
	CellList<User> pinboardCellList = new CellList<User>(cell, userKeyProvider);

	/**
	 * Damit die Widgets geladen werden k�nnen, muss die onLoad()-Methode
	 * implementiert werden. Darin wird definiert, welcher Vorgang bei der
	 * Aktivierung der Widgets ausgef�hrt werden soll. Die Methode wird direkt nach
	 * dem Anh�ngen eines Widgets an das Dokument des Browser aufgerufen.
	 * 
	 */
	public void onLoad() {

		/*
		 * Damit alle weiteren Elemente der Superklasse geladen werden und somit die
		 * Sinnhaftigkeit und die Semantik bereits aktiviert ist
		 */
		super.onLoad();

		// Dem VerticalPanel wird unser Celllist hinzugef�gt
		cellListPanel.add(pinboardCellList);

		// Diesem VerticalPanel wird nun das tats�chliche Panel hinzugef�gt.
		this.add(cellListPanel);

		// Unsere CellList wird nun unserem SelectionModel hinzugef�gt
		pinboardCellList.setSelectionModel(selectionModel);

		/*
		 * �ber unser Proxy wird die Methode aufgerufen die einen User �bergibt und
		 * dar�ber Informationen �ber die abonnierten Pinnw�nde des eingeloggten Nutzers
		 * zur�ckgeben soll
		 */
		pinitService.getAllSubscriptionsByUser(Integer.parseInt(Cookies.getCookie("id")),
				new LoadCellListDataCallback());

		/*
		 * Damit unsere Liste Aktionen annehmen und ausf�hren kann, ben�tigen wir das Interface 
		 * SelectionChangeEvent.Handler, welcher vom SingleSelectionModel zur Verf�gung
		 * gestellt wird. Un
		 */
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			/*
			 *Bei Auswahl eines Elements bzw. Zelle innerhalb der CellList, wird nun die 
			 *folgende Aktion ausgel�st. Dies wird in einer anonymen Klasse definiert, worin 
			 *nun die Methode deklariert wird. 
			 */
			
			public void onSelectionChange(SelectionChangeEvent event) {
				
				/*Der ausgew�hlte User wird �ber das selectionModel geholt und der Variable 
				*selectedUser zugewiesen.
				*/
				User selectedUser = selectionModel.getSelectedObject();
				
			if (selectedUser != null) {
								
				RootPanel.get("details").clear();
				RootPanel.get("details").add(new SubscriptionForm (selectedUser.getId()));
			}
			
				
			}
		});

		
	}

	/*
	 * Das CallbackObjekt innerhalb der Methode, l�st folgende innere Klasse aus.
	 * Zudem muss das Interface AsynCallBack implementiert werden, worin die
	 * Anweisung der Ausf�hrung des CallbackObjekts hinterlegt ist. Hierzu gibt es
	 * zwei F�lle die bei R�ckgabe eintreffen k�nnen.
	 */
	public class LoadCellListDataCallback implements AsyncCallback<Vector<User>> {

		// Bei einem Fehlschlag, tritt die Methode ein, die Informationen dar�ber gibt
		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Ihre Abonnements konnten nicht geladen werden!");
		}

		/*
		 * Wird das CallbackObjekt ordnungsgem�� zur�ckgegeben, so wird die
		 * Methode(non-Javadoc) aufgerufen, die die folgenden Aktionen ausf�hrt.
		 */
		@Override
		public void onSuccess(Vector<User> result) {

			/*
			 * Zur erhaltenen cellList wird �ber setRowdata ein StartIndex festgelegt, sowie
			 * die Werte bzw. Inhalte der Zellen die geladen werden sollen
			 */
			pinboardCellList.setRowData(0, result);

			// Die Gesamtanzahl der Reihen wird exakt gez�hlt
			pinboardCellList.setRowCount(result.size(), true);

			/*
			 * Damit die Gesamtanzahl der Reihen immer korrekt angezeigt wird, werden die
			 * Daten hier�ber neugeladen
			 */
			pinboardCellList.redraw();
		}

	}

}