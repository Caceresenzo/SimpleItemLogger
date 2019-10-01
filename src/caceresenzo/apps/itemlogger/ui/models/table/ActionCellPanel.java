package caceresenzo.apps.itemlogger.ui.models.table;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ActionCellPanel extends JPanel implements Cloneable {
	
	/* UI */
	private final List<JButton> buttons;
	private String state;
	
	/* Constructor */
	public ActionCellPanel(List<JButton> buttons) {
		this.buttons = buttons;
		
		initialize();
	}
	
	/** Initialize the layout. */
	private void initialize() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		Iterator<JButton> iterator = buttons.iterator();
		
		add(Box.createHorizontalGlue());
		while (iterator.hasNext()) {
			add(iterator.next());
			
			if (iterator.hasNext()) {
				add(Box.createRigidArea(new Dimension(5, 0)));
			}
		}
		add(Box.createHorizontalGlue());
		
		addActionListener((event) -> state = event.getActionCommand());
	}
	
	/**
	 * Add an {@link ActionListener} for every buttons of this {@link ActionCellPanel}.
	 * 
	 * @param listener
	 *            {@link ActionListener} to add.
	 */
	public void addActionListener(ActionListener listener) {
		buttons.forEach((button) -> button.addActionListener(listener));
	}
	
	/** @return A copied instance of this panel. */
	public ActionCellPanel copy() {
		List<JButton> newButtons = new ArrayList<>();
		
		buttons.forEach((button) -> {
			JButton newButton = new JButton(button.getText(), button.getIcon());
			newButton.setActionCommand(button.getActionCommand());
			
			newButtons.add(newButton);
		});
		
		return new ActionCellPanel(newButtons);
	}
	
	/** @return Last's button action command. */
	public String getState() {
		return state;
	}
	
}