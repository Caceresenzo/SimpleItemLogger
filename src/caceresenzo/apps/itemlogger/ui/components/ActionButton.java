package caceresenzo.apps.itemlogger.ui.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import caceresenzo.libs.internationalization.i18n;

public class ActionButton extends JButton {
	
	/* Constructor */
	public ActionButton(String actionKey, String iconRessourcePath, String actionCommand) {
		super(i18n.string("button.action." + actionKey), new ImageIcon(ActionButton.class.getResource(iconRessourcePath)));
		
		setActionCommand(actionCommand);
	}
	
}