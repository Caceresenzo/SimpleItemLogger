package caceresenzo.apps.itemlogger.ui.part.implementations;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.time.DayOfWeek;
import java.time.LocalDate;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import caceresenzo.apps.itemlogger.ui.part.AbstractFieldPartPanel;
import caceresenzo.frameworks.database.binder.BindableColumn;

public class LocalDateFieldPartPanel extends AbstractFieldPartPanel<LocalDate> {
	
	/* Constructor */
	public LocalDateFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		super(modelClass, bindableColumn, keyListener);
	}
	
	@Override
	protected Component createComponent() {
		DatePickerSettings dateSettings = new DatePickerSettings();
		dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
		dateSettings.setAllowEmptyDates(canBeNull());
		
		DatePicker datePicker = new DatePicker(dateSettings);
		
		if (!canBeNull()) {
			datePicker.setDateToToday();
		}
		
		return datePicker;
	}
	
	@Override
	public LocalDate getObject() {
		return ((DatePicker) getFieldComponent()).getDate();
	}
	
}