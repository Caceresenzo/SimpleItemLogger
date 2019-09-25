package caceresenzo.apps.itemlogger.ui.part.implementations;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.time.DayOfWeek;
import java.util.Date;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import caceresenzo.apps.itemlogger.ui.part.AbstractFieldPartPanel;
import caceresenzo.frameworks.database.binder.BindableColumn;

public class DateFieldPartPanel extends AbstractFieldPartPanel<Date> {
	
	/* Constructor */
	public DateFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		super(modelClass, bindableColumn, keyListener);
	}
	
	@Override
	public Date getObject() {
		return new Date(((DatePicker) getFieldComponent()).getDate().toEpochDay());
	}
	
	@Override
	protected Component createComponent() {
		DatePickerSettings dateSettings = new DatePickerSettings();
		dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
		dateSettings.setAllowEmptyDates(false);
		
		DatePicker datePicker = new DatePicker(dateSettings);
		datePicker.setDateToToday();
		
		return datePicker;
	}
	
}