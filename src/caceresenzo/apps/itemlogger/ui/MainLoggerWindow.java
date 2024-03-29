package caceresenzo.apps.itemlogger.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.apps.itemlogger.assets.Assets;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.managers.SearchManager;
import caceresenzo.apps.itemlogger.models.ConstructionSite;
import caceresenzo.apps.itemlogger.models.Item;
import caceresenzo.apps.itemlogger.models.Lend;
import caceresenzo.apps.itemlogger.models.Person;
import caceresenzo.apps.itemlogger.models.ReturnEntry;
import caceresenzo.apps.itemlogger.ui.components.ActionButton;
import caceresenzo.apps.itemlogger.ui.components.DataJTable;
import caceresenzo.apps.itemlogger.ui.export.implementations.ExportToPdfDialog;
import caceresenzo.apps.itemlogger.ui.export.implementations.ExportToPrinterDialog;
import caceresenzo.apps.itemlogger.ui.lend.LendDetailDialog;
import caceresenzo.apps.itemlogger.ui.models.DatabaseEntryTableModel;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.libs.internationalization.i18n;

public class MainLoggerWindow implements ActionListener, DatabaseEntryTableModel.Callback<IDatabaseEntry> {
	
	/* Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(MainLoggerWindow.class);
	
	/* Action Commands */
	public static final String ACTION_COMMAND_DISPLAY_ITEMS = "action_display_items";
	public static final String ACTION_COMMAND_DISPLAY_PERSONS = "action_display_persons";
	public static final String ACTION_COMMAND_DISPLAY_CONSTRUCTION_SITES = "action_display_construction_sites";
	public static final String ACTION_COMMAND_DISPLAY_LEND = "action_display_lend";
	public static final String ACTION_COMMAND_DISPLAY_RETURNS = "action_display_returns";
	public static final String ACTION_COMMAND_ADD = "action_add";
	public static final String ACTION_COMMAND_EXPORT_TO_PDF = "action_export_to_pdf";
	public static final String ACTION_COMMAND_EXPORT_TO_PRINTER = "action_export_to_printer";
	public static final String ACTION_COMMAND_TABLE_ACTION_DETAILS_ITEM = "action_table_action_details_item";
	
	/* UI */
	private JFrame frame;
	private JPanel searchPanel, actionPanel, actionContainerPanel, modelActionPanel, dataPanel;
	private JScrollPane dataScrollPane;
	private DataJTable dataTable;
	private JTextField searchBarTextField;
	private JButton searchButton;
	
	/* Variables */
	private Class<?> currentDisplayedModelClass;
	
	/* Constructor */
	public MainLoggerWindow() {
		initialize();
	}
	
	/** Initialize the contents of the frame. */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(900, 700);
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(i18n.string("application.title"));
		
		searchPanel = new JPanel();
		searchPanel.setBorder(new TitledBorder(null, i18n.string("logger.panel.search.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		actionContainerPanel = new JPanel();
		actionContainerPanel.setBorder(new TitledBorder(null, i18n.string("logger.panel.action.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		dataPanel = new JPanel();
		dataPanel.setBorder(new TitledBorder(null, i18n.string("logger.panel.data.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(dataPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
										.addComponent(actionContainerPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
										.addComponent(searchPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE))
								.addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(searchPanel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(actionContainerPanel, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(dataPanel, GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
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
		
		dataTable = new DataJTable();
		
		dataScrollPane.setViewportView(dataTable);
		dataPanel.setLayout(gl_dataPanel);
		
		modelActionPanel = new JPanel();
		modelActionPanel.setBorder(null);
		
		actionPanel = new JPanel();
		actionPanel.setBorder(null);
		GroupLayout gl_actionContainerPanel = new GroupLayout(actionContainerPanel);
		gl_actionContainerPanel.setHorizontalGroup(
				gl_actionContainerPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_actionContainerPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_actionContainerPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(modelActionPanel, GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE)
										.addComponent(actionPanel, GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE))
								.addContainerGap()));
		gl_actionContainerPanel.setVerticalGroup(
				gl_actionContainerPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_actionContainerPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(modelActionPanel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(actionPanel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
		modelActionPanel.setLayout(new BoxLayout(modelActionPanel, BoxLayout.X_AXIS));
		actionContainerPanel.setLayout(gl_actionContainerPanel);
		
		getActionButtons().forEach((button) -> actionPanel.add(button));
		getModelActionButtons().forEach((button) -> modelActionPanel.add(button));
		
		searchBarTextField = new JTextField();
		
		searchButton = new JButton(i18n.string("logger.button.search"));
		
		JButton clearSearchButton = new JButton(i18n.string("logger.button.clear-search"));
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
				gl_searchPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_searchPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(searchBarTextField, GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(clearSearchButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		gl_searchPanel.setVerticalGroup(
				gl_searchPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_searchPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_searchPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(clearSearchButton, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
										.addComponent(searchButton, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
										.addComponent(searchBarTextField, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
								.addGap(55)));
		searchPanel.setLayout(gl_searchPanel);
		frame.getContentPane().setLayout(groupLayout);
		
		SearchManager.get().updateUiElements(searchBarTextField, searchButton, clearSearchButton, dataTable);
		
		changeModel(Item.class, null);
	}
	
	/**
	 * Change the {@link JTable table}'s model.
	 * 
	 * @param newClassModel
	 *            New model class to display.
	 * @param rowActionButtons
	 *            A {@link List list} of contextualized buttons to display in a dedicated "Action" column. Can be <code>null</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void changeModel(Class<?> newClassModel, List<JButton> rowActionButtons) {
		SearchManager.get().clearSearch(false);
		
		DatabaseEntryTableModel model = new DatabaseEntryTableModel(dataTable, currentDisplayedModelClass = newClassModel, new ArrayList<>(), rowActionButtons, this);
		
		((TitledBorder) dataPanel.getBorder()).setTitle(i18n.string("logger.panel.data.title.with", i18n.string(String.format("logger.panel.data.title.with.part.%s", newClassModel.getSimpleName().toLowerCase()))));
		dataPanel.repaint();
		
		dataTable.setModel(model);
		model.synchronize();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_ADD: {
				AddNewDialog.open(frame, currentDisplayedModelClass, (Class<?> modelClass, Object instance) -> {
					LOGGER.info(String.valueOf(instance));
					
					DataManager.get().getDatabaseSynchronizer().insert(modelClass, instance);
					((DatabaseEntryTableModel<?>) dataTable.getModel()).synchronize();
				});
				break;
			}
			
			case ACTION_COMMAND_DISPLAY_ITEMS:
			case ACTION_COMMAND_DISPLAY_PERSONS:
			case ACTION_COMMAND_DISPLAY_CONSTRUCTION_SITES:
			case ACTION_COMMAND_DISPLAY_LEND:
			case ACTION_COMMAND_DISPLAY_RETURNS: {
				Class<?> newModelClass;
				List<JButton> tableActionButtons = null;
				
				switch (actionCommand) {
					case ACTION_COMMAND_DISPLAY_ITEMS:
					default: {
						newModelClass = Item.class;
						break;
					}
					
					case ACTION_COMMAND_DISPLAY_PERSONS: {
						newModelClass = Person.class;
						break;
					}
					
					case ACTION_COMMAND_DISPLAY_CONSTRUCTION_SITES: {
						newModelClass = ConstructionSite.class;
						break;
					}
					
					case ACTION_COMMAND_DISPLAY_LEND: {
						newModelClass = Lend.class;
						
						JButton detailButton = new JButton(i18n.string("logger.table.column.actions.button.lend.details"));
						detailButton.setActionCommand(ACTION_COMMAND_TABLE_ACTION_DETAILS_ITEM);
						
						tableActionButtons = Arrays.asList(detailButton);
						break;
					}
					
					case ACTION_COMMAND_DISPLAY_RETURNS: {
						newModelClass = ReturnEntry.class;
					}
				}
				
				changeModel(newModelClass, tableActionButtons);
				break;
			}
			
			case ACTION_COMMAND_EXPORT_TO_PDF:
			case ACTION_COMMAND_EXPORT_TO_PRINTER: {
				String filterText = searchBarTextField.getText();
				
				switch (actionCommand) {
					case ACTION_COMMAND_EXPORT_TO_PDF: {
						ExportToPdfDialog.open(frame, filterText);
						break;
					}
					
					case ACTION_COMMAND_EXPORT_TO_PRINTER: {
						ExportToPrinterDialog.open(frame, filterText);
						break;
					}
				}
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown action command: " + actionCommand);
			}
		}
	}
	
	@Override
	public void onTableActionClick(JTable table, DatabaseEntryTableModel<IDatabaseEntry> tableClass, List<IDatabaseEntry> entries, int row, String action) {
		switch (action) {
			case ACTION_COMMAND_TABLE_ACTION_DETAILS_ITEM: {
				if (!Lend.class.equals(currentDisplayedModelClass)) {
					throw new IllegalArgumentException("Cannot handle the return item action with a different model class than " + Lend.class.getSimpleName() + ".");
				}
				
				Lend lend = (Lend) entries.get(row);
				
				LendDetailDialog.open(frame, lend);
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown table action: " + action);
			}
		}
	}
	
	/** @return A {@link List list} of {@link JButton button}s that will be used to fill the "Actions" section. */
	protected List<JButton> getActionButtons() {
		return createButtonList(Arrays.asList(
				new ActionButton("add", Assets.ICON_PLUS_32PX, ACTION_COMMAND_ADD),
				new ActionButton("export-printer", Assets.ICON_PRINT_32PX, ACTION_COMMAND_EXPORT_TO_PRINTER),
				new ActionButton("export-pdf", Assets.ICON_FILE_PDF_32PX, ACTION_COMMAND_EXPORT_TO_PDF) //
		));
	}
	
	/** @return A {@link List list} of {@link JButton button}s that will be used to fill the "Model Actions" section. */
	protected List<JButton> getModelActionButtons() {
		return createButtonList(Arrays.asList(
				new ActionButton("items", Assets.ICON_NEW_PRODUCT_32PX, ACTION_COMMAND_DISPLAY_ITEMS),
				new ActionButton("persons", Assets.ICON_USER_MEN_32PX, ACTION_COMMAND_DISPLAY_PERSONS),
				new ActionButton("construction-sites", Assets.ICON_IN_CONSTRUCTION_32PX, ACTION_COMMAND_DISPLAY_CONSTRUCTION_SITES),
				new ActionButton("lend", Assets.ICON_HISTORY_32PX, ACTION_COMMAND_DISPLAY_LEND)
				//, new ActionButton("returns", Assets.ICON_HISTORY_32PX, ACTION_COMMAND_DISPLAY_RETURNS) //
		));
	}
	
	/**
	 * Create an {@link List list} of maximized {@link JButton} from {@link ActionButton} data.
	 * 
	 * @param actionButtons
	 *            {@link List} of {@link ActionButton}.
	 * @return A {@link List list} of maximized {@link JButton}.
	 */
	protected List<JButton> createButtonList(List<ActionButton> actionButtons) {
		List<JButton> buttons = new ArrayList<>();
		
		actionButtons.forEach((actionButton) -> {
			actionButton.addActionListener(this);
			actionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, actionButton.getMinimumSize().height));
			
			buttons.add(actionButton);
		});
		
		return buttons;
	}
	
	/** Apply the {@link UIManager#getSystemLookAndFeelClassName() system LookAndFeel} and start a new {@link MainLoggerWindow} instance. */
	public static void open() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			LOGGER.warn("Failed to apply system look and feel.", exception);
		}
		
		EventQueue.invokeLater(() -> {
			MainLoggerWindow window = new MainLoggerWindow();
			window.frame.setVisible(true);
		});
	}
	
}