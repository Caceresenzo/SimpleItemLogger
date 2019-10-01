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
	
	/**
	 * Update the UI elements instances stored in the {@link SearchManager} for quick response.
	 * 
	 * @param searchTextField
	 *            {@link JTextField} where the search query should be typed.
	 * @param searchButton
	 *            {@link JButton} with which the search will be called.
	 * @param clearSearchButton
	 *            {@link JButton} with which the search will be cleared.
	 * @param dataTable
	 *            {@link JTable} to apply the filter to.
	 */
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
	
	/**
	 * Clear the search and update the UI accordingly.
	 * 
	 * @param updateModel
	 *            If this also should update the filter of the {@link JTable}.
	 */
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
	
	/** @return <code>null</code> if the search query if already <code>null</code> or empty, otherwise, return the {@link String#trim() trimmed} query. */
	public String getValidatedSearchQuery() {
		String query = searchTextField.getText().trim();
		
		if (!StringUtils.validate(query)) {
			return null;
		}
		
		return query;
	}
	
	/**
	 * Do the search and update the UI accordingly.
	 * 
	 * @return If the search has been done or not. By exemple, if the query is <code>null</code>, the search will not be done.
	 * @see #getValidatedSearchQuery()
	 */
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