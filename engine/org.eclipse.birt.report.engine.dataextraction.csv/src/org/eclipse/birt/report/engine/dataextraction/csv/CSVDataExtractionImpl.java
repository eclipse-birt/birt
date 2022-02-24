/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.dataextraction.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.dataextraction.CSVDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.ICommonDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.i18n.Messages;
import org.eclipse.birt.report.engine.dataextraction.impl.CommonDataExtractionImpl;

/**
 * Implements the logic to extract data as CSV format.
 */
public class CSVDataExtractionImpl extends CommonDataExtractionImpl {
	public static final String PLUGIN_ID = "org.eclipse.birt.report.engine.dataextraction.csv"; //$NON-NLS-1$
	public static final String DEFAULT_ENCODING = Charset.defaultCharset().name();

	private OutputStream outputStream;
	private String encoding;
	private String sep;
	private boolean addCR;
	private boolean isExportDataType;
	private boolean isExportColumnHeader;
	private String[] selectedColumnNames;
	private int columnLocalizeOption;

	/**
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#initialize(org.eclipse.birt.report.engine.api.script.IReportContext,
	 *      org.eclipse.birt.report.engine.api.IDataExtractionOption)
	 */
	public void initialize(IReportContext context, IDataExtractionOption options) throws BirtException {
		super.initialize(context, options);
		initCsvOptions(options);
	}

	/**
	 * Initializes the CSV options based on the data extraction option. If the
	 * passed option doesn't contain common options, use default values.
	 * 
	 * @param option options
	 */
	private void initCsvOptions(IDataExtractionOption options) {
		this.outputStream = options.getOutputStream();
		ICSVDataExtractionOption csvOptions;
		if (options instanceof ICSVDataExtractionOption) {
			csvOptions = (ICSVDataExtractionOption) options;
		} else {
			csvOptions = new CSVDataExtractionOption(options.getOptions());
		}

		encoding = csvOptions.getEncoding();
		if (encoding == null || "".equals(encoding.trim())) //$NON-NLS-1$
		{
			encoding = null;
		} else {
			encoding = encoding.trim();
		}

		if (encoding == null) {
			encoding = DEFAULT_ENCODING;
		}

		sep = csvOptions.getSeparator();
		if (sep == null || "".equals(sep)) //$NON-NLS-1$
		{
			sep = ICSVDataExtractionOption.SEPARATOR_COMMA;
		}

		addCR = csvOptions.getAddCR();
		isExportDataType = csvOptions.isExportDataType();
		isExportColumnHeader = csvOptions.isExportColumnHeader();
		selectedColumnNames = csvOptions.getSelectedColumns();
		columnLocalizeOption = csvOptions.getColumnLocalizeOption();
	}

	/**
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#output(org.eclipse.birt.report.engine.api.IExtractionResults)
	 */
	public void output(IExtractionResults results) throws BirtException {
		if (results == null) {
			throw new BirtException(PLUGIN_ID,
					Messages.getString("exception.dataextraction.no_extraction_result_error"), //$NON-NLS-1$
					(ResourceBundle) null);
		}
		try {
			String[] columnNames = null;
			String[] columnLabels = null;

			IResultMetaData metaData = results.getResultMetaData();
			int count = metaData.getColumnCount();

			// if selected columns are null or empty, returns all columns
			if (selectedColumnNames == null || selectedColumnNames.length <= 0) {
				columnNames = new String[count];
				columnLabels = new String[count];
				for (int i = 0; i < count; i++) {
					String colName = metaData.getColumnName(i);
					columnNames[i] = colName;
					columnLabels[i] = metaData.getColumnLabel(i);
				}
			} else {
				Map<String, String> nameLabelMap = new HashMap<String, String>();
				for (int i = 0; i < count; i++) {
					String colName = metaData.getColumnName(i);
					String colLabel = metaData.getColumnLabel(i);
					nameLabelMap.put(colName, colLabel);
				}
				int selectedCount = selectedColumnNames.length;
				List<String> labelList = new ArrayList<String>();
				List<String> nameList = new ArrayList<String>();
				for (int i = 0; i < selectedCount; i++) {
					String label = nameLabelMap.get(selectedColumnNames[i]);
					if (label != null) {
						nameList.add(selectedColumnNames[i]);
						labelList.add(label);
					}
				}
				columnLabels = labelList.toArray(new String[0]);
				columnNames = nameList.toArray(new String[0]);
			}

			IDataIterator iData = null;
			if (results != null) {
				iData = results.nextResultIterator();
				if (iData != null && columnNames.length > 0) {
					if (isExportColumnHeader) {
						if ((columnLocalizeOption & ICommonDataExtractionOption.OPTION_COLUMN_NAME) != 0) {
							output(CSVUtil.makeCSVRow(columnNames, sep, addCR));
						}

						if ((columnLocalizeOption & ICommonDataExtractionOption.OPTION_COLUMN_DISPLAY_NAME) != 0) {
							output(CSVUtil.makeCSVRow(columnLabels, sep, addCR));
						}
					}

					int[] columnTypes = getColumnTypes(columnNames, results);
					// Column data type
					if (isExportDataType) {
						output(makeDataTypesRow(columnTypes));
					}

					// Data
					String[] values = new String[columnNames.length];
					createFormatters(columnNames, columnTypes);
					while (iData.next()) {
						for (int i = 0; i < columnNames.length; i++) {
							if (columnTypes[i] != DataType.BLOB_TYPE && columnTypes[i] != DataType.BINARY_TYPE) {
								values[i] = getStringValue(iData, columnNames, i);
							} else {
								values[i] = null;
							}
						}

						output(CSVUtil.makeCSVRow(values, sep, addCR));
					}
				}
			}
		} catch (Exception e) {
			throw new BirtException(PLUGIN_ID, Messages.getString("exception.dataextraction.exception_occured"), //$NON-NLS-1$
					(ResourceBundle) null, e);
		}
	}

	/**
	 * Creates a CSV-row containing the data type names of the given types array.
	 * 
	 * @param types column typee array
	 * @return CSV-row containing the data type names the result set
	 */
	private String makeDataTypesRow(int[] types) {
		String[] values = new String[types.length];
		for (int i = 0; i < types.length; i++) {
			values[i] = DataType.getName(types[i]);
		}
		return CSVUtil.makeCSVRow(values, sep, addCR);
	}

	/**
	 * Returns the column types for the selected columns.
	 * 
	 * @param columnNames selected columns
	 * @param results     result set
	 * @return
	 * @throws BirtException
	 */
	private int[] getColumnTypes(String[] columnNames, IExtractionResults results) throws BirtException {
		Map<String, Integer> typesMap = new HashMap<String, Integer>();
		int count = results.getResultMetaData().getColumnCount();
		for (int i = 0; i < count; i++) {
			String colName = results.getResultMetaData().getColumnName(i);
			int colType = results.getResultMetaData().getColumnType(i);
			typesMap.put(colName, colType);
		}

		int[] types = new int[columnNames.length];
		for (int i = 0; i < columnNames.length; i++) {
			Integer colType = typesMap.get(columnNames[i]);
			if (colType == null) {
				throw new BirtException(
						"The specified column \"" + columnNames[i] + "\" does not exist in the result set!");
			}
			types[i] = colType.intValue();
		}
		return types;
	}

	/**
	 * Outputs a given String to the output stream using the configured encoding.
	 * 
	 * @param s string to output
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private void output(String s) throws IOException, UnsupportedEncodingException {
		outputStream.write(s.getBytes(encoding));
	}
}
