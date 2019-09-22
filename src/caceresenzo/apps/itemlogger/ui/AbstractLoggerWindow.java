package caceresenzo.apps.itemlogger.ui;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import caceresenzo.apps.itemlogger.models.Person;
import caceresenzo.apps.itemlogger.ui.models.DatabaseEntryTableModel;
import caceresenzo.frameworks.database.connections.implementations.SqliteConnection;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;

public class AbstractLoggerWindow<T> {
	
	/* UI */
	private JFrame frame;
	private JPanel searchPanel;
	private JPanel searchFilterPanel;
	private JPanel actionContainerPanel;
	private JPanel actionPanel;
	private JPanel dataPanel;
	private JScrollPane dataScrollPane;
	private JTable dataTable;
	private JTextField searchBarTextField;
	private JButton searchButton;
	private JCheckBox checkBox;
	private JCheckBox checkBox_1;
	
	/** Launch the application. */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AbstractLoggerWindow window = new AbstractLoggerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/* Constructor */
	public AbstractLoggerWindow() {
		initialize();
		
		DatabaseEntryTableModel<Person> model = new DatabaseEntryTableModel<>(dataTable, Person.class, new ArrayList<>(), false);
		
		dataTable.setModel(model);
		
		SqliteConnection sqliteConnection = new SqliteConnection("hello.db");
		try {
			sqliteConnection.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DatabaseSynchronizer a = new DatabaseSynchronizer(sqliteConnection);
		model.setSynchronizer(a);
		model.getEntries().addAll(a.load(Person.class));
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(700, 500);
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		searchPanel = new JPanel();
		searchPanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		actionContainerPanel = new JPanel();
		actionContainerPanel.setBorder(new TitledBorder(null, "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		dataPanel = new JPanel();
		dataPanel.setBorder(new TitledBorder(null, "Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(dataPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
										.addComponent(actionContainerPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
										.addComponent(searchPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE))
								.addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(searchPanel, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(actionContainerPanel, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
								.addContainerGap()));
		
		dataScrollPane = new JScrollPane();
		dataScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		dataScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		dataScrollPane.setBorder(null);
		GroupLayout gl_dataPanel = new GroupLayout(dataPanel);
		gl_dataPanel.setHorizontalGroup(
				gl_dataPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_dataPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(dataScrollPane, GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
								.addContainerGap()));
		gl_dataPanel.setVerticalGroup(
				gl_dataPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_dataPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(dataScrollPane, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
								.addContainerGap()));
		
		dataTable = new JTable();
		dataTable.setFillsViewportHeight(true);
		dataScrollPane.setViewportView(dataTable);
		dataPanel.setLayout(gl_dataPanel);
		
		actionPanel = new JPanel();
		actionPanel.setBorder(null);
		GroupLayout gl_actionContainerPanel = new GroupLayout(actionContainerPanel);
		gl_actionContainerPanel.setHorizontalGroup(
				gl_actionContainerPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_actionContainerPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(actionPanel, GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
								.addContainerGap()));
		gl_actionContainerPanel.setVerticalGroup(
				gl_actionContainerPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_actionContainerPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(actionPanel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
		actionContainerPanel.setLayout(gl_actionContainerPanel);
		
		getActionButtons().forEach((button) -> actionPanel.add(button));
		
		searchBarTextField = new JTextField();
		
		searchButton = new JButton("New button");
		
		searchFilterPanel = new JPanel();
		searchFilterPanel.setBorder(new TitledBorder(null, "Filters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
				gl_searchPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_searchPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_searchPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(searchFilterPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
										.addGroup(gl_searchPanel.createSequentialGroup()
												.addComponent(searchBarTextField, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		gl_searchPanel.setVerticalGroup(
				gl_searchPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_searchPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_searchPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(searchButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(searchBarTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(searchFilterPanel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		searchFilterPanel.setLayout(new BoxLayout(searchFilterPanel, BoxLayout.X_AXIS));
		
		checkBox = new JCheckBox("New check box");
		searchFilterPanel.add(checkBox);
		
		checkBox_1 = new JCheckBox("New check box");
		searchFilterPanel.add(checkBox_1);
		searchPanel.setLayout(gl_searchPanel);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	protected List<JButton> getActionButtons() {
		List<JButton> buttons = new ArrayList<>();
		
		buttons.add(new JButton("Print", new ImageIcon(AbstractLoggerWindow.class.getResource("/caceresenzo/apps/itemlogger/assets/icons/icon-print-32px.png"))));
		buttons.add(new JButton("Users", new ImageIcon(AbstractLoggerWindow.class.getResource("/caceresenzo/apps/itemlogger/assets/icons/icon-user-men-32px.png"))));
		buttons.add(new JButton("History", new ImageIcon(AbstractLoggerWindow.class.getResource("/caceresenzo/apps/itemlogger/assets/icons/icon-history-32px.png"))));
		buttons.add(new JButton("Add", new ImageIcon(AbstractLoggerWindow.class.getResource("/caceresenzo/apps/itemlogger/assets/icons/icon-plus-32px.png"))));
		
		return buttons;
	}
	
}