package caceresenzo.apps.itemlogger.ui.lend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import caceresenzo.apps.itemlogger.configuration.Config;
import caceresenzo.apps.itemlogger.configuration.Language;
import caceresenzo.apps.itemlogger.managers.ItemLoggerManager;
import caceresenzo.apps.itemlogger.models.ReturnEntry;
import caceresenzo.apps.itemlogger.ui.AddNewDialog;
import caceresenzo.apps.itemlogger.ui.components.DataJTable;
import caceresenzo.apps.itemlogger.ui.models.DatabaseEntryTableModel;
import caceresenzo.frameworks.database.IDatabaseEntry;

public class LendDetailDialog extends JDialog {
	
	/* Constants */
	public static final Class<? extends IDatabaseEntry> MODEL_CLASS = ReturnEntry.class;
	
	private final JPanel contentPanel = new JPanel();
	private DataJTable table;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Config.get();
		Language.get().initialize();
		ItemLoggerManager.get().initialize();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			LendDetailDialog dialog = new LendDetailDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public LendDetailDialog() {
		setBounds(100, 100, 660, 449);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new TitledBorder(null, "Returns", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
										.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE))
								.addContainerGap()));
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
								.addContainerGap()));
		
		JButton button = new JButton("New button");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddNewDialog.open(null, MODEL_CLASS, new AddNewDialog.Callback() {
					@Override
					public void onCreatedItem(Class<?> modelClass, Object instance) {
						
					}
				});
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(503, Short.MAX_VALUE)
					.addComponent(button)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(button, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		table = new DataJTable();
		table.setModel(new DatabaseEntryTableModel<>(table, ReturnEntry.class, new ArrayList<>(Arrays.asList(new ReturnEntry(0, null, 10, LocalDate.now(), "null")))));
		
		scrollPane.setViewportView(table);
		contentPanel.setLayout(gl_contentPanel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		// for (int i = 0; i < 10; i++) {
		// panel_3.add(new ReturnedLendDetailPanel());
		// }
	}
}
