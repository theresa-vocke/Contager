package de.hdm.pinit.client.gui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubscriptionButton extends VerticalPanel {

	private VerticalPanel vpSub = new VerticalPanel();

	Button subBtn = new Button("Abonnement suchen");

	public void onLoad() {
		super.onLoad();
		this.add(subBtn);

		vpSub.add(subBtn);
	}

}
