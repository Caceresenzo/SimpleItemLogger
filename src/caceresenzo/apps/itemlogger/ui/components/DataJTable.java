package caceresenzo.apps.itemlogger.ui.components;

import java.awt.Color;
import java.time.LocalDate;

import javax.swing.JTable;

import com.github.lgooddatepicker.tableeditors.DateTableEditor;

public class DataJTable extends JTable {
	
	public DataJTable() {
		super();
		
		setFillsViewportHeight(true);
		setSelectionForeground(Color.BLACK);
		setSelectionBackground(Color.decode("#D9EBF9"));
		
		setDefaultRenderer(LocalDate.class, new DateTableEditor());
		setDefaultEditor(LocalDate.class, new DateTableEditor());
	}
	
}