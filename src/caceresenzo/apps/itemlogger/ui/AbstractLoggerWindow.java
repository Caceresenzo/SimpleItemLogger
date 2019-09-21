package caceresenzo.apps.itemlogger.ui;

import java.awt.EventQueue;
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

public class AbstractLoggerWindow {
	
	private JFrame frame;
	private JTextField textField;
	private JTable table;
	
	/**
	 * Launch the application.
	 */
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
	
	/**
	 * Create the application.
	 */
	public AbstractLoggerWindow() {
		initialize();
		
		DatabaseEntryTableModel<Person> model = new DatabaseEntryTableModel<>(table, Person.class, Arrays.asList(
				new Person(0, "Hello", "World", "helloworld@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello1", "World1", "helloworld1@gmail.com"),
				new Person(0, "Hello2", "World2", "helloworld2@gmail.com")));
		
		table.setModel(model);
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
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
										.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
										.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE))
								.addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
								.addContainerGap()));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
				gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
								.addContainerGap()
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_4.setVerticalGroup(
				gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
								.addContainerGap()
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
								.addContainerGap()));
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		panel_4.setLayout(gl_panel_4);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(null);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
				gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
								.addContainerGap()
								.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_2.setVerticalGroup(
				gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
								.addContainerGap()
								.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		panel_2.setLayout(gl_panel_2);
		
		getActionButtons().forEach((button) -> panel_3.add(button));
		
		textField = new JTextField();
		
		JButton button = new JButton("New button");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Filters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
										.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
										.addGroup(gl_panel.createSequentialGroup()
												.addComponent(textField, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(button, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(button, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(textField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JCheckBox checkBox = new JCheckBox("New check box");
		panel_1.add(checkBox);
		
		JCheckBox checkBox_1 = new JCheckBox("New check box");
		panel_1.add(checkBox_1);
		panel.setLayout(gl_panel);
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