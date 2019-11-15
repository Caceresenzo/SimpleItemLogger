package caceresenzo.apps.itemlogger.ui.lend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lgooddatepicker.components.DatePicker;

import caceresenzo.apps.itemlogger.assets.Assets;
import caceresenzo.apps.itemlogger.export.GoodReleaseExporter;
import caceresenzo.apps.itemlogger.export.ReceiptSlipExporter;
import caceresenzo.apps.itemlogger.export.implementations.pdf.PdfGoodReleaseExporter;
import caceresenzo.apps.itemlogger.export.implementations.pdf.PdfReceiptSlipExporter;
import caceresenzo.apps.itemlogger.export.physical.GoodRelease;
import caceresenzo.apps.itemlogger.export.physical.ReceiptSlip;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.models.Lend;
import caceresenzo.apps.itemlogger.models.ReturnEntry;
import caceresenzo.apps.itemlogger.ui.AddNewDialog;
import caceresenzo.apps.itemlogger.ui.components.ActionButton;
import caceresenzo.apps.itemlogger.ui.components.DataJTable;
import caceresenzo.apps.itemlogger.ui.models.DatabaseEntryTableModel;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;
import caceresenzo.frameworks.printer.Printer;
import caceresenzo.libs.internationalization.i18n;

public class LendDetailDialog extends JDialog implements ActionListener, DatabaseEntryTableModel.Callback<ReturnEntry> {
	
	/* Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(LendDetailDialog.class);
	
	/* Constants */
	public static final Class<? extends IDatabaseEntry> MODEL_CLASS = ReturnEntry.class;
	
	/* Action Commands */
	public static final String ACTION_COMMAND_ADD = "action_add";
	public static final String ACTION_COMMAND_PRINT = "action_export_to_printer";
	public static final String ACTION_COMMAND_FINISH = "action_finish";
	public static final String ACTION_COMMAND_TABLE_ACTION_PRINT_RECEIPT_SLIP = "action_table_action_details_return";
	
	/* UI */
	private final JPanel contentPanel = new JPanel();
	private JPanel detailsPanel;
	private JTextField itemTextField, personTextField, constructionSiteTextField, quantityAndReturnedTextField, dateTextField, extraTextField;
	private JTable dataTable;
	private JButton printButton, addButton;
	
	/* Variables */
	private final Lend lend;
	private final BindableColumn returnEntryLendBindableColumn;
	private final List<ReturnEntry> returnEntries;
	
	/* Synchronizer */
	private final DatabaseSynchronizer synchronizer;
	
	/* Constructor */
	public LendDetailDialog(JFrame parent, Lend lend) {
		super(parent);
		
		this.lend = lend;
		
		this.returnEntryLendBindableColumn = BindableColumn.findColumn(TableAnalizer.get().analizeColumns(MODEL_CLASS), ReturnEntry.COLUMN_LEND);
		this.returnEntries = new ArrayList<>();
		this.synchronizer = DataManager.get().getDatabaseSynchronizer();
		
		setTitle(i18n.string("returned-lend-details-dialog.window.title"));
		setSize(660, 600);
		setMinimumSize(getSize());
		setModal(true);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane returnsScrollPane = new JScrollPane();
		returnsScrollPane.setBorder(new TitledBorder(null, i18n.string("returned-lend-details-dialog.panel.returned"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		returnsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		returnsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		detailsPanel = new JPanel();
		detailsPanel.setBorder(new TitledBorder(null, i18n.string("returned-lend-details-dialog.panel.details"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel actionsContainerPanel = new JPanel();
		actionsContainerPanel.setBorder(new TitledBorder(null, i18n.string("returned-lend-details-dialog.panel.actions"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(returnsScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
										.addComponent(actionsContainerPanel, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
										.addComponent(detailsPanel, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
								.addContainerGap()));
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(detailsPanel, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(actionsContainerPanel, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(returnsScrollPane, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setBorder(null);
		
		addButton = new ActionButton("add", Assets.ICON_PLUS_32PX, ACTION_COMMAND_ADD);
		printButton = new ActionButton("print-good-release", Assets.ICON_PRINT_32PX, ACTION_COMMAND_PRINT);
		actionsPanel.setLayout(new GridLayout(0, 2, 0, 0));
		actionsPanel.add(addButton);
		actionsPanel.add(printButton);
		GroupLayout gl_actionsContainerPanel = new GroupLayout(actionsContainerPanel);
		gl_actionsContainerPanel.setHorizontalGroup(
				gl_actionsContainerPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_actionsContainerPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(actionsPanel, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
								.addContainerGap()));
		gl_actionsContainerPanel.setVerticalGroup(
				gl_actionsContainerPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_actionsContainerPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(actionsPanel, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
								.addContainerGap()));
		actionsContainerPanel.setLayout(gl_actionsContainerPanel);
		
		for (JButton jButton : Arrays.asList(addButton, printButton)) {
			jButton.addActionListener(this);
		}
		
		JLabel itemLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.item"));
		itemLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		itemTextField = new JTextField();
		itemTextField.setEditable(false);
		itemTextField.setColumns(10);
		
		JLabel personLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.person"));
		personLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		personTextField = new JTextField();
		personTextField.setEditable(false);
		personTextField.setColumns(10);
		
		JLabel constructionSiteLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.construction-site"));
		constructionSiteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		constructionSiteTextField = new JTextField();
		constructionSiteTextField.setEditable(false);
		constructionSiteTextField.setColumns(10);
		
		JLabel quantityAndWaitingLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.quantity-and-returned"));
		quantityAndWaitingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel dateLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.date"));
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel extraLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.extra"));
		extraLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		quantityAndReturnedTextField = new JTextField();
		quantityAndReturnedTextField.setEditable(false);
		quantityAndReturnedTextField.setColumns(10);
		
		dateTextField = new JTextField();
		dateTextField.setEditable(false);
		dateTextField.setColumns(10);
		
		extraTextField = new JTextField();
		extraTextField.setColumns(10);
		extraTextField.setEditable(false);
		
		GroupLayout gl_detailsPanel = new GroupLayout(detailsPanel);
		gl_detailsPanel.setHorizontalGroup(
				gl_detailsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_detailsPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(extraLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(dateLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(quantityAndWaitingLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(itemLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(personLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(constructionSiteLabel, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
								.addGap(10)
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(constructionSiteTextField, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
										.addComponent(personTextField, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
										.addComponent(itemTextField, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
										.addComponent(quantityAndReturnedTextField, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
										.addComponent(dateTextField, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
										.addGroup(Alignment.TRAILING, gl_detailsPanel.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(extraTextField, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)))
								.addContainerGap()));
		gl_detailsPanel.setVerticalGroup(
				gl_detailsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_detailsPanel.createSequentialGroup()
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(itemLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
										.addComponent(itemTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(personLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
										.addComponent(personTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(constructionSiteLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
										.addComponent(constructionSiteTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(quantityAndWaitingLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
										.addComponent(quantityAndReturnedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(dateLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
										.addComponent(dateTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_detailsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(extraTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(extraLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		detailsPanel.setLayout(gl_detailsPanel);
		
		dataTable = new DataJTable();
		
		returnsScrollPane.setViewportView(dataTable);
		
		contentPanel.setLayout(gl_contentPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton finishButton = new JButton(i18n.string("returned-lend-details-dialog.button.finish"));
		finishButton.addActionListener(this);
		finishButton.setActionCommand(ACTION_COMMAND_FINISH);
		
		buttonPanel.add(finishButton);
		getRootPane().setDefaultButton(finishButton);
		
		initializeTextFields();
		loadReturnEntries();
	}
	
	/** Initialize the detail {@link TextField} with the informations gaven by the {@link Lend} instance. */
	private void initializeTextFields() {
		DatePicker datePicker = new DatePicker();
		datePicker.setDate(lend.getLendDate());
		
		itemTextField.setText(lend.getItem().describe());
		personTextField.setText(lend.getPerson().describe());
		constructionSiteTextField.setText(lend.getConstructionSite().describe());
		dateTextField.setText(datePicker.getText());
		extraTextField.setText(lend.getExtra());
	}
	
	/** Load the {@link ReturnEntry} and filter ones that are not associated with the {@link Lend} instance. */
	@SuppressWarnings("unchecked")
	private void loadReturnEntries() {
		List<ReturnEntry> rawReturnEntries = (List<ReturnEntry>) synchronizer.load(MODEL_CLASS);
		
		rawReturnEntries.removeIf((returnEntry) -> returnEntry.getLend().getId() != lend.getId());
		
		returnEntries.clear();
		returnEntries.addAll(rawReturnEntries);
		
		JButton detailButton = new JButton("Impr. Bon de Retour");
		detailButton.setActionCommand(ACTION_COMMAND_TABLE_ACTION_PRINT_RECEIPT_SLIP);
		
		dataTable.setModel(new DatabaseEntryTableModel<>(dataTable, ReturnEntry.class, rawReturnEntries, Arrays.asList(detailButton), this));
		
		refreshQuantityAndReturnedTextField();
	}
	
	/** Refresh the "quantity and returned" text. */
	private void refreshQuantityAndReturnedTextField() {
		quantityAndReturnedTextField.setText(i18n.string("returned-lend-details-dialog.field.quantity-and-returned", lend.getQuantity(), computeAlreadyReturnedQuantity()));
	}
	
	/** @return Computed already returned quantity. */
	private int computeAlreadyReturnedQuantity() {
		int waiting = 0;
		
		for (ReturnEntry returnEntry : returnEntries) {
			waiting += returnEntry.getQuantity();
		}
		
		return waiting;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_ADD: {
				if (computeAlreadyReturnedQuantity() >= lend.getQuantity()) {
					JOptionPane.showMessageDialog(this, i18n.string("returned-lend-details-dialog.dialog.error.already-fulfill.message"), i18n.string("returned-lend-details-dialog.dialog.error.already-fulfill.title"), JOptionPane.ERROR_MESSAGE);
				} else {
					AddNewDialog.open((JFrame) getParent(), MODEL_CLASS, new AddNewDialog.Callback() {
						@Override
						public void onCreatedItem(Class<?> modelClass, Object instance) {
							rangeQuantity((ReturnEntry) instance);
							
							DataManager.get().getDatabaseSynchronizer().insert(modelClass, instance);
							
							loadReturnEntries();
						}
						
						public void rangeQuantity(ReturnEntry returnEntry) {
							int already = computeAlreadyReturnedQuantity();
							int newQuantity = already + returnEntry.getQuantity();
							
							if (newQuantity > lend.getQuantity()) {
								BindableColumn bindableColumn = BindableColumn.findColumn(TableAnalizer.get().analizeColumns(MODEL_CLASS), ReturnEntry.COLUMN_QUANTITY);
								Field field = bindableColumn.getField();
								
								newQuantity = Math.min(newQuantity, lend.getQuantity() - already);
								try {
									field.set(returnEntry, newQuantity);
								} catch (IllegalArgumentException | IllegalAccessException exception) {
									LOGGER.warn("Failed to range the quantity for the ReturnEntry model.", exception);
								}
							}
						}
					}, Arrays.asList(new AddNewDialog.FulfilledEntry(returnEntryLendBindableColumn, lend)));
				}
				break;
			}
			
			case ACTION_COMMAND_PRINT: {
				print((file) -> {
					GoodReleaseExporter goodReleaseExporter = new PdfGoodReleaseExporter();
					
					try {
						goodReleaseExporter.exportToFile(new GoodRelease(lend), file);
					} catch (Exception exception) {
						throw new RuntimeException(exception);
					}
				});
				break;
			}
			
			case ACTION_COMMAND_FINISH: {
				setVisible(false);
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown action command: " + actionCommand);
			}
		}
	}
	
	@Override
	public void onTableActionClick(JTable table, DatabaseEntryTableModel<ReturnEntry> tableClass, List<ReturnEntry> entries, int row, String action) {
		ReturnEntry returnEntry = entries.get(row);
		
		switch (action) {
			case ACTION_COMMAND_TABLE_ACTION_PRINT_RECEIPT_SLIP: {
				print((file) -> {
					ReceiptSlipExporter receiptSlipExporter = new PdfReceiptSlipExporter();
					
					try {
						receiptSlipExporter.exportToFile(new ReceiptSlip(returnEntry), file);
					} catch (Exception exception) {
						throw new RuntimeException(exception);
					}
				});
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown table row action: " + action);
			}
		}
	}
	
	/**
	 * Do a print and handle any error that append.
	 * 
	 * @param printAction
	 *            Action to print.
	 */
	private void print(Printer.PrintAction printAction) {
		try {
			new Printer().print(true, printAction);
		} catch (Exception exception) {
			LOGGER.error("Failed to print.", exception);
			
			JOptionPane.showMessageDialog(this, i18n.string("printer.dialog.failed.message", exception.getMessage()), i18n.string("printer.dialog.failed.title"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Open a new {@link AddNewDialog} instance.
	 * 
	 * @param parent
	 *            Parent {@link JFrame}.
	 * @param lend
	 *            {@link Lend} instance to open with.
	 */
	public static void open(JFrame parent, Lend lend) {
		LendDetailDialog dialog = new LendDetailDialog(parent, lend);
		
		dialog.setVisible(true);
	}
	
}