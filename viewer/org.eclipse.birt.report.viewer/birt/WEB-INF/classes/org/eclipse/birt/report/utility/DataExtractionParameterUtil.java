/*******************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
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

package org.eclipse.birt.report.utility;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.CSVDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.CommonDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.ICommonDataExtractionOption;

/**
 * Utility class to handle parameter related stuff...
 *
 */
public class DataExtractionParameterUtil {

	/**
	 * Parameter of the default separator
	 */
	public static final String DEFAULT_SEP = ","; //$NON-NLS-1$

	/**
	 * URL parameter name to indicate the export encoding.
	 */
	public static final String PARAM_EXPORT_ENCODING = ParameterAccessor.PARAM_EXPORT_ENCODING;

	/**
	 * URL parameter name to indicate the CSV separator.
	 */
	public static final String PARAM_SEP = "__sep";//$NON-NLS-1$

	/**
	 * URL parameter name to indicate whether exports column's data type.
	 */
	public static final String PARAM_EXPORT_DATATYPE = "__exportdatatype";//$NON-NLS-1$

	/**
	 * Parameter name that gives the result set names of the export data form.
	 */
	public static final String PARAM_RESULTSETNAME = ParameterAccessor.PARAM_RESULTSETNAME;

	/**
	 * Parameter name that gives the selected column numbers of the export data
	 * form.
	 */
	public static final String PARAM_SELECTEDCOLUMNNUMBER = ParameterAccessor.PARAM_SELECTEDCOLUMNNUMBER;

	/**
	 * Parameter name that gives the selected column names of the export data form.
	 */
	public static final String PARAM_SELECTEDCOLUMN = ParameterAccessor.PARAM_SELECTEDCOLUMN;

	/**
	 * URL parameter name to indicate whether exports locale neutral value.
	 */
	public static final String PARAM_LOCALENEUTRAL = "__localeneutral"; //$NON-NLS-1$

	/**
	 * URL parameter name to that the export line will be finished with carriage
	 * return
	 */
	public static final String PARAM_CARRIAGERETURN = "__carriagereturn"; //$NON-NLS-1$

	/**
	 * URL parameter name to define the export of column display name
	 */
	public static final String PARAM_COLUMNDISPLAYNAME = "__columndisplayname"; //$NON-NLS-1$

	/**
	 * URL parameter name to define the export of column name
	 */
	public static final String PARAM_COLUMNNAME = "__columnname"; //$NON-NLS-1$

	/**
	 * Known export extension names.
	 */
	public static final String EXTRACTION_FORMAT_CSV = "csv"; //$NON-NLS-1$

	/**
	 * Extraction extension
	 */
	public static final String EXTRACTION_EXTENSION_CSV = "org.eclipse.birt.report.engine.dataextraction.csv"; //$NON-NLS-1$

	/**
	 * Get result set name.
	 *
	 * @param options
	 * @return the result set name
	 */
	public static String getResultSetName(Map<String, String> options) {
		if (options != null) {
			return options.get(PARAM_RESULTSETNAME);
		}
		return null;
	}

	/**
	 * Get selected column name list.
	 *
	 * @param options
	 * @return the selected name list
	 */
	public static String[] getSelectedColumns(Map<String, String> options) {
		if (options == null) {
			return null;
		}

		int columnCount = 0;
		try {
			String numStr = options.get(PARAM_SELECTEDCOLUMNNUMBER);
			if (numStr != null) {
				columnCount = Integer.parseInt(numStr);
			}
		} catch (Exception e) {
			columnCount = 0;
		}

		String[] columns = new String[columnCount];

		// get column names
		for (int i = 0; i < columnCount; i++) {
			String paramName = PARAM_SELECTEDCOLUMN + String.valueOf(i);
			String columnName = options.get(paramName);
			columns[i] = columnName;
		}

		return columns;
	}

	/**
	 * Returns the separator String
	 *
	 * @param options extraction options
	 * @return the separator string
	 */
	public static String getSep(Map<String, String> options) {
		if (options == null) {
			return DEFAULT_SEP;
		}

		String sepKey = options.get(PARAM_SEP);
		if (sepKey == null) {
			return DEFAULT_SEP;
		}

		String key = "viewer.sep." + sepKey; //$NON-NLS-1$
		String sep = ParameterAccessor.getInitProp(key);
		if (sep == null || sep.length() <= 0) {
			return DEFAULT_SEP;
		}
		return sep;
	}

	/**
	 * Returns the encoding for export data.
	 *
	 * @param options extraction options
	 * @return the encoding for export data
	 */
	public static String getExportEncoding(Map<String, String> options) {
		if (options == null) {
			return ICommonDataExtractionOption.UTF_8_ENCODE;
		}

		String encoding = options.get(PARAM_EXPORT_ENCODING);

		// use UTF-8 as the default encoding
		if (encoding == null) {
			encoding = ICommonDataExtractionOption.UTF_8_ENCODE;
		}

		return encoding;
	}

	/**
	 * Returns whether exports column's data type
	 *
	 * @param options
	 * @return is export data type
	 */
	public static boolean isExportDataType(Map<String, String> options) {
		if (options == null) {
			return false;
		}

		String flag = options.get(PARAM_EXPORT_DATATYPE);
		if ("true".equalsIgnoreCase(flag)) { //$NON-NLS-1$
			return true;
		}

		return false;
	}

	/**
	 * Returns whether exports locale neutral value
	 *
	 * @param options
	 * @return is locale neutral
	 */
	public static boolean isLocaleNeutral(Map<String, String> options) {
		if (options == null) {
			return false;
		}

		String flag = options.get(PARAM_LOCALENEUTRAL);
		if ("true".equalsIgnoreCase(flag)) { //$NON-NLS-1$
			return true;
		}

		return false;
	}

	/**
	 * Is extraction with carriage return
	 *
	 * @param options extraction option
	 * @return is extraction with carriage return
	 */
	public static boolean isWithCarriageReturn(Map<String, String> options) {
		if (options == null) {
			return false;
		}

		String flag = options.get(PARAM_CARRIAGERETURN);
		if ("true".equalsIgnoreCase(flag)) { //$NON-NLS-1$
			return true;
		}

		return false;
	}

	/**
	 * Is extraction with usage of column display name
	 *
	 * @param options extraction option
	 * @return is extraction with column display name
	 */
	public static boolean isWithColumnDisplayName(Map<String, String> options) {
		if (options == null) {
			return false;
		}
		return Boolean.parseBoolean(options.get(PARAM_COLUMNDISPLAYNAME));
	}

	/**
	 * Is extraction with usage of column name
	 *
	 * @param options extraction option
	 * @return is extraction with column name
	 */
	public static boolean isWithColumnName(Map<String, String> options) {
		if (options == null) {
			return false;
		}
		return Boolean.parseBoolean(options.get(PARAM_COLUMNNAME));
	}

	/**
	 * Create a CommonDataExtractionOption configured using the common-specific
	 * parameters.
	 *
	 * @param extractOption common data extraction option
	 * @param columns       columns to export
	 * @param locale        locale
	 * @param timeZone      time zone
	 * @param options       general options to use for the configuration
	 * @return instance of CommonDataExtractionOption initialized with the passed
	 *         values
	 */
	public static DataExtractionOption createOptions(CommonDataExtractionOption extractOption, String[] columns,
			Locale locale, TimeZone timeZone, Map<String, String> options) {
		if (extractOption == null) {
			extractOption = new CommonDataExtractionOption();
		}

		extractOption.setEncoding(getExportEncoding(options));
		extractOption.setExportDataType(isExportDataType(options));
		extractOption.setLocaleNeutralFormat(isLocaleNeutral(options));
		extractOption.setLocale(locale);
		extractOption.setTimeZone(timeZone);
		extractOption.setSelectedColumns(columns);
		extractOption.setUserParameters(options);
		return extractOption;
	}

	/**
	 * Create a specific data extraction option for CSV format
	 *
	 * @param columns  columns to be extracted
	 * @param locale   locale to be used
	 * @param timeZone time zone
	 * @param options  extraction options
	 * @return data extraction options
	 */
	public static DataExtractionOption createCSVOptions(String[] columns, Locale locale, TimeZone timeZone,
			Map<String, String> options) {
		CSVDataExtractionOption extractOption = new CSVDataExtractionOption();
		createOptions(extractOption, columns, locale, timeZone, options);

		// CSV separator
		extractOption.setSeparator(getSep(options));
		extractOption.setAddCR(isWithCarriageReturn(options));
		extractOption.setAddColumnDisplayName(isWithColumnDisplayName(options));
		extractOption.setAddColumnName(isWithColumnName(options));
		return extractOption;
	}

	/**
	 * Returns an array of decoded columns names.
	 *
	 * @param columns Collection of column names, in HTML format
	 * @return Returns an array of decoded columns names.
	 */
	public static String[] getColumnNames(Collection<String> columns) {
		if (columns != null && columns.size() > 0) {
			String[] columnNames = new String[columns.size()];
			Iterator<String> iSelectedColumns = columns.iterator();
			for (int i = 0; iSelectedColumns.hasNext(); i++) {
				columnNames[i] = ParameterAccessor.htmlDecode(iSelectedColumns.next());
			}
			return columnNames;
		}
		return null;
	}

}
