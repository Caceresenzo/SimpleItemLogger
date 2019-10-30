package caceresenzo.apps.itemlogger.export.generic;

import java.io.File;

public interface GenericExporter<T> {
	
	/**
	 * Export data to a file.
	 * 
	 * @param data
	 *            Data to export.
	 * @param file
	 *            Target file destination.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	public void exportToFile(T data, File file) throws Exception;
	
}