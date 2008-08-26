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
import java.util.TimeZone;
import java.util.Map;

import org.eclipse.birt.report.engine.api.DataExtractionOption;

/**
 * Extends Data Extraction options for common attributes.
 * 
 */
public class CommonDataExtractionOption extends DataExtractionOption
	implements ICommonDataExtractionOption
{

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#getLocale()
	 */
	public Locale getLocale( )
	{
		return (Locale) getOption( OUTPUT_LOCALE );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#getTimezone()
	 */
	public TimeZone getTimeZone( )
	{
		return (TimeZone) getOption( OUTPUT_TIMEZONE );
	}
	
	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#getDateFormat()
	 */
	public String getDateFormat( )
	{
		return getStringOption( OUTPUT_DATE_FORMAT );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#getEncoding()
	 */
	public String getEncoding( )
	{
		return getStringOption( OUTPUT_ENCODING );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#getSelectedColumns()
	 */
	public String[] getSelectedColumns( )
	{
		return (String[]) getOption( OUTPUT_SELECTED_COLUMNS );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#isExportDataType()
	 */
	public boolean isExportDataType( )
	{
		return getBooleanOption( OUTPUT_EXPORT_DATA_TYPE, false );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#isLocaleNeutralFormat()
	 */
	public boolean isLocaleNeutralFormat( )
	{
		return getBooleanOption( OUTPUT_LOCALE_NEUTRAL_FORMAT, false );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#getUserParameters()
	 */
	public Map getUserParameters( )
	{
		return (Map) getOption( USER_PARAMETERS );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setLocale(java.util.Locale)
	 */
	public void setLocale( Locale locale )
	{
		setOption( OUTPUT_LOCALE, locale );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setTimezone(java.util.TimeZone)
	 */
	public void setTimeZone( TimeZone timeZone )
	{
		setOption( OUTPUT_TIMEZONE, timeZone );
	}
	
	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setDateFormat(java.lang.String)
	 */
	public void setDateFormat( String dateFormat )
	{
		setOption( OUTPUT_DATE_FORMAT, dateFormat );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setEncoding(java.lang.String)
	 */
	public void setEncoding( String encoding )
	{
		setOption( OUTPUT_ENCODING, encoding );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setSelectedColumns(java.lang.String[])
	 */
	public void setSelectedColumns( String[] columnNames )
	{
		setOption( OUTPUT_SELECTED_COLUMNS, columnNames );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setExportDataType(boolean)
	 */
	public void setExportDataType( boolean isExportDataType )
	{
		setOption( OUTPUT_EXPORT_DATA_TYPE, new Boolean( isExportDataType ) );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setLocaleNeutralFormat(boolean)
	 */
	public void setLocaleNeutralFormat( boolean isLocaleNeutralFormat )
	{
		setOption( OUTPUT_LOCALE_NEUTRAL_FORMAT, new Boolean(
				isLocaleNeutralFormat ) );
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.csv.ICSVDataExtractionOption#setUserParameters(java.util.Map)
	 */
	public void setUserParameters( Map map )
	{
		setOption( USER_PARAMETERS, map );
	}

}
