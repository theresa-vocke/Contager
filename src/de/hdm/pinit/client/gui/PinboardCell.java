package de.hdm.pinit.client.gui;

 import com.google.gwt.cell.client.AbstractCell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


import de.hdm.pinit.shared.bo.User;

 
 public class PinboardCell extends AbstractCell<User> {

 /*
 * Alle Zellen m�ssen die Render-Methode implementieren, die einen
 rechteckigen
 * Bereich mit einem Platzhalter darstellen sollen.
 *
 */
 @Override
 public void render(Context context, User value, SafeHtmlBuilder sb) {

// Wert kann auf NULL gesetzt sein, deshalb muss gepr�ft werden
 if (value == null) {
 return;
 }

// Zum sp�teren Zeitpunkt muss hier aus der SubscriptionTabelle die Nicknamen
// der PinboardOwner herausgelesen werden. Diese Pinboards wurden vom
// angemeldeten Nutzer abonniert
 sb.appendHtmlConstant("<div style=\"color:#c40000; font-size:22px\">");
 sb.appendEscaped(value.getNickname());
 sb.appendHtmlConstant("</div>");
//
 }

 }
