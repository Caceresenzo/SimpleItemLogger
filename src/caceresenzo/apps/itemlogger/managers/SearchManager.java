package caceresenzo.apps.itemlogger.managers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import caceresenzo.apps.itemlogger.ui.models.DatabaseEntryTableModel;
import caceresenzo.frameworks.managers.AbstractManager;
import caceresenzo.libs.string.StringUtils;

public class SearchManager extends AbstractManager implements ActionListener, CaretListener {
	
	/* Action Commands */
	public static final String ACTION_COMMAND_SEARCH = "search";
	public static final String ACTION_COMMAND_SEARCH_TEXTFIELD = "search_textfield";
	public static final String ACTION_COMMAND_CLEAR_SEARCH = "clear_search";
	
	/* Singleton */
	private static SearchManager INSTANCE;
	
	/* UI */
	private JTextField searchTextField;
	private JButton searchButton, clearSearchButton;
	private JTable dataTable;
	
	/* Variables */
	private boolean inSearch;
	
	/* Constructor */
	private SearchManager() {
		;
	}
	
	public void updateUiElements(JTextField searchTextField, JButton searchButton, JButton clearSearchButton, JTable dataTable) {
		this.searchTextField = searchTextField;
		this.searchButton = searchButton;
		this.clearSearchButton = clearSearchButton;
		this.dataTable = dataTable;
		
		searchTextField.setActionCommand(ACTION_COMMAND_SEARCH);
		searchButton.setActionCommand(ACTION_COMMAND_SEARCH);
		clearSearchButton.setActionCommand(ACTION_COMMAND_CLEAR_SEARCH);
		
		for (JButton button : Arrays.asList(searchButton, clearSearchButton)) {
			button.addActionListener(this);
			button.setEnabled(false);
		}
		
		searchTextField.addCaretListener(this);
		searchTextField.addActionListener(this);
		
		clearSearch(true);
	}
	
	public void clearSearch(boolean updateModel) {
		if (!inSearch) {
			return;
		}
		
		inSearch = false;
		clearSearchButton.setEnabled(false);
		
		if (updateModel) {
			((DatabaseEntryTableModel<?>) dataTable.getModel()).filter(null);
		}
	}
	
	public String getValidatedSearchQuery() {
		String query = searchTextField.getText().trim();
		
		if (!StringUtils.validate(query)) {
			return null;
		}
		
		return query;
	}
	
	public boolean doSearch() {
		String query = getValidatedSearchQuery();
		
		if (query == null) {
			return false; /* Just in case */
		}
		
		inSearch = true;
		clearSearchButton.setEnabled(true);
		
		((DatabaseEntryTableModel<?>) dataTable.getModel()).filter(query);
		
		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_SEARCH:
			case ACTION_COMMAND_SEARCH_TEXTFIELD: {
				boolean hasSearchBeenDone = doSearch();
				
				if (!hasSearchBeenDone && inSearch) {
					clearSearch(true); /* Text field has send an action event but was empty, so we clear the search */
				}
				
				break;
			}
			case ACTION_COMMAND_CLEAR_SEARCH: {
				clearSearch(true);
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown search action command: " + actionCommand);
			}
		}
	}
	
	@Override
	public void caretUpdate(CaretEvent event) {
		searchButton.setEnabled(getValidatedSearchQuery() != null);
	}
	
	/** @return SearchManager's singleton instance. */
	public static final SearchManager get() {
		if (INSTANCE == null) {
			INSTANCE = new SearchManager();
		}
		
		return INSTANCE;
	}
	
}