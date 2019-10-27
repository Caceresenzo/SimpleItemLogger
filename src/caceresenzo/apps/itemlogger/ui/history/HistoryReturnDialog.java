package caceresenzo.apps.itemlogger.ui.history;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import caceresenzo.apps.itemlogger.models.LendEntry;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.libs.internationalization.i18n;
import javax.swing.JCheckBox;

public class HistoryReturnDialog extends JDialog implements ActionListener, CaretListener {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(HistoryReturnDialog.class);
	
	/* Action Commands */
	public static final String ACTION_COMMAND_VALIDATE = "action_validate";
	public static final String ACTION_COMMAND_CANCEL = "action_cancel";
	
	/* UI */
	private final JPanel contentPanel = new JPanel();
	private JFormattedTextField returnedQuantityTextField;
	private JTextField maxReturnableQuantityTextField;
	private DatePicker returnedDateDatePicker;
	private JButton validateButton, cancelButton;
	
	/* Variables */
	private final LendEntry historyEntry;
	private final HistoryReturnDialog.Callback callback;
	private JCheckBox addDefaultExtraCheckBox;
	
	/* Constructor */
	public HistoryReturnDialog(JFrame parent, LendEntry historyEntry, HistoryReturnDialog.Callback callback) {
		super(parent);
		
		this.historyEntry = historyEntry;
		this.callback = callback;
		
		setSize(500, 350);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setModal(true);
		setTitle(i18n.string("history-return-dialog.window.title"));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel returnMainPanel = new JPanel();
		returnMainPanel.setBorder(new TitledBorder(null, i18n.string("history-return-dialog.panel.return"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(returnMainPanel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(returnMainPanel, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE));
		
		JPanel quantityPanel = new JPanel();
		quantityPanel.setBorder(new TitledBorder(null, i18n.string("history-return-dialog.panel.quantity"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel datePanel = new JPanel();
		datePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), i18n.string("history-return-dialog.panel.date"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		DatePickerSettings dateSettings = new DatePickerSettings();
		dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
		dateSettings.setAllowEmptyDates(false);
		
		returnedDateDatePicker = new DatePicker(dateSettings);
		returnedDateDatePicker.setDateToToday();
		
		GroupLayout gl_datePanel = new GroupLayout(datePanel);
		gl_datePanel.setHorizontalGroup(
				gl_datePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_datePanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(returnedDateDatePicker, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
								.addContainerGap()));
		gl_datePanel.setVerticalGroup(
				gl_datePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_datePanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(returnedDateDatePicker, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
								.addContainerGap()));
		datePanel.setLayout(gl_datePanel);
		
		JPanel addDefaultExtraPanel = new JPanel();
		addDefaultExtraPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), i18n.string("history-return-dialog.panel.settings"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		addDefaultExtraCheckBox = new JCheckBox(i18n.string("history-return-dialog.checkbox.add-default-extra-message"));
		addDefaultExtraCheckBox.setSelected(true);
		
		GroupLayout gl_addDefaultExtraPanel = new GroupLayout(addDefaultExtraPanel);
		gl_addDefaultExtraPanel.setHorizontalGroup(
				gl_addDefaultExtraPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_addDefaultExtraPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(addDefaultExtraCheckBox, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
								.addContainerGap()));
		gl_addDefaultExtraPanel.setVerticalGroup(
				gl_addDefaultExtraPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_addDefaultExtraPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(addDefaultExtraCheckBox, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
								.addContainerGap()));
		addDefaultExtraPanel.setLayout(gl_addDefaultExtraPanel);
		GroupLayout gl_returnMainPanel = new GroupLayout(returnMainPanel);
		gl_returnMainPanel.setHorizontalGroup(
				gl_returnMainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_returnMainPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_returnMainPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(quantityPanel, GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
										.addComponent(datePanel, GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
										.addComponent(addDefaultExtraPanel, GroupLayout.PREFERRED_SIZE, 442, GroupLayout.PREFERRED_SIZE))
								.addContainerGap()));
		gl_returnMainPanel.setVerticalGroup(
				gl_returnMainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_returnMainPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(quantityPanel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(datePanel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(addDefaultExtraPanel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(39, Short.MAX_VALUE)));
		
		returnedQuantityTextField = new JFormattedTextField();
		
		JLabel slashLabel = new JLabel("/");
		slashLabel.setHorizontalAlignment(SwingConstants.CENTER);
		slashLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		maxReturnableQuantityTextField = new JTextField();
		maxReturnableQuantityTextField.setEditable(false);
		
		GroupLayout gl_quantityPanel = new GroupLayout(quantityPanel);
		gl_quantityPanel.setHorizontalGroup(
				gl_quantityPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_quantityPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(returnedQuantityTextField, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(slashLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(maxReturnableQuantityTextField, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		gl_quantityPanel.setVerticalGroup(
				gl_quantityPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_quantityPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_quantityPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(returnedQuantityTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
										.addComponent(slashLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
										.addComponent(maxReturnableQuantityTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
								.addContainerGap()));
		quantityPanel.setLayout(gl_quantityPanel);
		returnMainPanel.setLayout(gl_returnMainPanel);
		contentPanel.setLayout(gl_contentPanel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		validateButton = new JButton(i18n.string("history-return-dialog.button.validate"));
		validateButton.setActionCommand(ACTION_COMMAND_VALIDATE);
		validateButton.setEnabled(false);
		
		cancelButton = new JButton(i18n.string("history-return-dialog.button.cancel"));
		cancelButton.setActionCommand(ACTION_COMMAND_CANCEL);
		
		for (JButton jButton : Arrays.asList(validateButton, cancelButton)) {
			jButton.addActionListener(this);
			buttonPane.add(jButton);
		}
		
		for (JTextField jTextField : Arrays.asList(returnedQuantityTextField, maxReturnableQuantityTextField)) {
			jTextField.setText(String.valueOf(historyEntry.getQuantity()));
		}
		
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setParseIntegerOnly(true);
		
		NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
		numberFormatter.setMaximum(Integer.MAX_VALUE);
		numberFormatter.setMinimum(0);
		
		returnedQuantityTextField.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
		returnedQuantityTextField.addCaretListener(this);
		
		getRootPane().setDefaultButton(validateButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_VALIDATE: {
				if (callback != null) {
					List<BindableColumn> bindableColumns = TableAnalizer.get().analizeColumns(LendEntry.class);
					
					BindableColumn returnBindableColumn = BindableColumn.findColumn(bindableColumns, LendEntry.COLUMN_RETURN_DATE);
					BindableColumn extraBindableColumn = BindableColumn.findColumn(bindableColumns, LendEntry.COLUMN_EXTRA);
					
					LendEntry remainingHistoryEntry = null;
					try {
						returnBindableColumn.getField().set(historyEntry, returnedDateDatePicker.getDate());
						
						remainingHistoryEntry = historyEntry.split((int) returnedQuantityTextField.getValue());
						
						if (remainingHistoryEntry != null) {
							returnBindableColumn.getField().set(remainingHistoryEntry, (LocalDate) null);
							
							String defaultExtra = i18n.string("history-return-dialog.devise.extra.message");
							String extra = (String) extraBindableColumn.getField().get(remainingHistoryEntry);
							
							if (addDefaultExtraCheckBox.isSelected()) {
								extra = defaultExtra;
							} else if (defaultExtra.equals(extra)) {
								extra = null;
							}
							
							extraBindableColumn.getField().set(remainingHistoryEntry, extra);
						}
					} catch (Exception exception) {
						LOGGER.warn("An exception occured when trying to devise the history entry.", exception);
					}
					
					callback.onValidatedHistoryItem(historyEntry, remainingHistoryEntry);
				}
				
				setVisible(false);
				break;
			}
			
			case ACTION_COMMAND_CANCEL: {
				setVisible(false);
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown action command: " + actionCommand);
			}
		}
	}
	
	@Override
	public void caretUpdate(CaretEvent event) {
		boolean valid = false;
		
		try {
			int number = Integer.parseInt(returnedQuantityTextField.getText().replace(" ", ""));
			
			valid = number <= historyEntry.getQuantity() && number >= 0;
		} catch (Exception exception) {
			;
		}
		
		validateButton.setEnabled(valid);
	}
	
	/**
	 * Open a new {@link HistoryReturnDialog} instance.<br>
	 * But if the {@link LendEntry} has already been returned, a dialog will ask to confirm that the user want to do the return.
	 * 
	 * @param parent
	 *            Parent {@link JFrame}.
	 * @param historyEntry
	 *            {@link LendEntry} to start with.
	 * @param callback
	 *            Callback to do action when the dialog is validated.
	 */
	public static void open(JFrame parent, LendEntry historyEntry, HistoryReturnDialog.Callback callback) {
		if (historyEntry.getReturnDate() != null) {
			int reply = JOptionPane.showConfirmDialog(parent, i18n.string("history-return-dialog.dialog.warning-already-return.message"), i18n.string("history-return-dialog.dialog.warning-already-return.title"), JOptionPane.YES_NO_OPTION);
			
			if (reply == JOptionPane.NO_OPTION || reply == JOptionPane.CLOSED_OPTION) {
				return;
			}
		}
		
		HistoryReturnDialog dialog = new HistoryReturnDialog(parent, historyEntry, callback);
		
		dialog.setVisible(true);
	}
	
	public interface Callback {
		
		/**
		 * Called when the {@link HistoryReturnDialog} has been validated.
		 * 
		 * @param originalHistoryEntry
		 *            The original {@link LendEntry} that has been use to the start the dialog.
		 * @param remainingHistoryEntry
		 *            Splited {@link LendEntry} created if the returned quantity is not equal to the lend quantity, can be <code>null</code>.
		 * @see LendEntry#split(int) Spliting an HistoryEntry.
		 */
		void onValidatedHistoryItem(LendEntry originalHistoryEntry, LendEntry remainingHistoryEntry);
		
	}
	
}