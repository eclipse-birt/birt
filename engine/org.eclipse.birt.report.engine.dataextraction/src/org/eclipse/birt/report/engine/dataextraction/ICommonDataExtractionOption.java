/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.dataextraction;

import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.report.engine.api.IDataExtractionOption;

/**
 * Extends Data Extraction options for CSV format
 * 
 */
public interface ICommonDataExtractionOption extends IDataExtractionOption
{

	/**
	 * output locale
	 */
	public static final String OUTPUT_LOCALE = "Locale"; //$NON-NLS-1$

	/**
	 * output encoding
	 */
	public static final String OUTPUT_ENCODING = "Encoding"; //$NON-NLS-1$

	/**
	 * output the selected columns
	 */
	public static final String OUTPUT_SELECTED_COLUMNS = "SelectedColumns"; //$NON-NLS-1$

	/**
	 * indicates whether export data type
	 */
	public static final String OUTPUT_EXPORT_DATA_TYPE = "ExportDataType"; //$NON-NLS-1$

	/**
	 * indicates whether export the locale neutral format value
	 */
	public static final String OUTPUT_LOCALE_NEUTRAL_FORMAT = "LocaleNeutralFormat"; //$NON-NLS-1$

	/**
	 * output date format
	 */
	public static final String OUTPUT_DATE_FORMAT = "DateFormat"; //$NON-NLS-1$

	/**
	 * map which can contain user-defined parameters
	 */
	public static final String USER_PARAMETERS = "UserParameters"; //$NON-NLS-1$

	/**
	 * UTF-8 encode constants.
	 */
	public static final String UTF_8_ENCODE = "UTF-8"; //$NON-NLS-1$

	/**
	 * ISO-8859-1 encode constants.
	 */
	public static final String ISO_8859_1_ENCODE = "ISO-8859-1"; //$NON-NLS-1$

	/**
	 * Sets the output locale
	 * 
	 * @param locale
	 */
	void setLocale( Locale locale );

	/**
	 * Returns the output locale
	 * 
	 * @return Locale
	 */
	Locale getLocale( );

	/**
	 * Sets the output encoding
	 * 
	 * @param encoding
	 */
	void setEncoding( String encoding );

	/**
	 * Returns the output encoding
	 * 
	 * @return String
	 */
	String getEncoding( );

	/**
	 * Sets the output selected columns
	 * 
	 * @param columnNames
	 */
	void setSelectedColumns( String[] columnNames );

	/**
	 * Returns the output selected columns
	 * 
	 * @return String[]
	 */
	String[] getSelectedColumns( );

	/**
	 * Sets the flag that indicates whether export data type.
	 * 
	 * @param isExportDataType
	 */
	void setExportDataType( boolean isExportDataType );

	/**
	 * Returns the flag that indicates whether export data type.
	 * 
	 * @return boolean
	 */
	boolean isExportDataType( );

	/**
	 * Sets the flag that indicates whether export the locale neutral format
	 * value.
	 * 
	 * @param isLocaleNeutralFormat
	 */
	void setLocaleNeutralFormat( boolean isLocaleNeutralFormat );

	/**
	 * Returns the flag that indicates whether export the locale neutral format
	 * value.
	 * 
	 * @return boolean
	 */
	boolean isLocaleNeutralFormat( );

	/**
	 * Sets the output date format
	 * 
	 * @param dateFormat
	 */
	void setDateFormat( String dateFormat );

	/**
	 * Returns the output date format
	 * 
	 * @return String
	 */
	String getDateFormat( );

	/**
	 * Sets the user-defined parameter map
	 * 
	 * @param map
	 */
	void setUserParameters( Map map );

	/**
	 * Returns the user-defined parameter map
	 * 
	 * @return Map
	 */
	Map getUserParameters( );
}
