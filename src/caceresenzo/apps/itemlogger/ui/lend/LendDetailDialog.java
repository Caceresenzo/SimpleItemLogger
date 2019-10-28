package caceresenzo.apps.itemlogger.ui.lend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.github.lgooddatepicker.components.DatePicker;

import caceresenzo.apps.itemlogger.assets.Assets;
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
import caceresenzo.libs.internationalization.i18n;

public class LendDetailDialog extends JDialog implements ActionListener {
	
	/* Constants */
	public static final Class<? extends IDatabaseEntry> MODEL_CLASS = ReturnEntry.class;
	
	/* Action Commands */
	public static final String ACTION_COMMAND_ADD = "action_add";
	public static final String ACTION_COMMAND_EXPORT_TO_PDF = "action_export_to_pdf";
	public static final String ACTION_COMMAND_EXPORT_TO_PRINTER = "action_export_to_printer";
	
	/* UI */
	private final JPanel contentPanel = new JPanel();
	private JPanel detailsPanel;
	private JTextField itemTextField, personTextField, constructionSiteTextField, quantityAndWaitingTextField, dateTextField, extraTextField;
	private JTable dataTable;
	
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
		returnsScrollPane.setBorder(new TitledBorder(null, "Returns", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		returnsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		returnsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		detailsPanel = new JPanel();
		detailsPanel.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel actionsPanel = new JPanel();
		actionsPanel.setBorder(new TitledBorder(null, "Actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(returnsScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
										.addGroup(gl_contentPanel.createSequentialGroup()
												.addComponent(detailsPanel, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(actionsPanel, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPanel.createSequentialGroup()
												.addGap(11)
												.addComponent(detailsPanel, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_contentPanel.createSequentialGroup()
												.addContainerGap()
												.addComponent(actionsPanel, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(returnsScrollPane, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
								.addContainerGap()));
		
		JButton addButton = new ActionButton("add", Assets.ICON_PLUS_32PX, ACTION_COMMAND_ADD);
		JButton printButton = new ActionButton("export-printer", Assets.ICON_PRINT_32PX, ACTION_COMMAND_EXPORT_TO_PRINTER);
		JButton exportToPdfButton = new ActionButton("export-pdf", Assets.ICON_FILE_PDF_32PX, ACTION_COMMAND_EXPORT_TO_PDF);
		
		for (JButton jButton : Arrays.asList(addButton, printButton, exportToPdfButton)) {
			jButton.addActionListener(this);
			jButton.setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		GroupLayout gl_actionsPanel = new GroupLayout(actionsPanel);
		gl_actionsPanel.setHorizontalGroup(
				gl_actionsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_actionsPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_actionsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(addButton, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
										.addComponent(printButton, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
										.addComponent(exportToPdfButton, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
								.addContainerGap()));
		gl_actionsPanel.setVerticalGroup(
				gl_actionsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_actionsPanel.createSequentialGroup()
								.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(printButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(exportToPdfButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(41, Short.MAX_VALUE)));
		actionsPanel.setLayout(gl_actionsPanel);
		
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
		
		JLabel quantityAndWaitingLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.quantity-and-waiting"));
		quantityAndWaitingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel dateLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.date"));
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel extraLabel = new JLabel(i18n.string("returned-lend-details-dialog.label.extra"));
		extraLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		quantityAndWaitingTextField = new JTextField();
		quantityAndWaitingTextField.setEditable(false);
		quantityAndWaitingTextField.setColumns(10);
		
		dateTextField = new JTextField();
		dateTextField.setEditable(false);
		dateTextField.setColumns(10);
		
		extraTextField = new JTextField();
		extraTextField.setColumns(10);
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
										.addComponent(quantityAndWaitingTextField, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
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
										.addComponent(quantityAndWaitingTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
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
	
	/** Load the {@link ReturnEntry} and filter once that are not associated with the {@link Lend} instance. */
	@SuppressWarnings("unchecked")
	private void loadReturnEntries() {
		List<ReturnEntry> rawReturnEntries = (List<ReturnEntry>) synchronizer.load(MODEL_CLASS);
		
		rawReturnEntries.removeIf((returnEntry) -> returnEntry.getLend().getId() != lend.getId());
		
		returnEntries.clear();
		returnEntries.addAll(rawReturnEntries);
		
		dataTable.setModel(new DatabaseEntryTableModel<>(dataTable, ReturnEntry.class, rawReturnEntries));
		
		refreshQuantityAndWaitingTextField();
	}
	
	/** Refresh the "quantity and waiting" text. */
	private void refreshQuantityAndWaitingTextField() {
		quantityAndWaitingTextField.setText(i18n.string("returned-lend-details-dialog.field.quantity-and-waiting", lend.getQuantity(), computeWaitingQuantity()));
	}
	
	/** @return Computed waiting-to-return quantity. */
	private int computeWaitingQuantity() {
		int waiting = 0;
		
		for (ReturnEntry returnEntry : returnEntries) {
			waiting += returnEntry.getQuantity();
		}
		
		return waiting;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
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