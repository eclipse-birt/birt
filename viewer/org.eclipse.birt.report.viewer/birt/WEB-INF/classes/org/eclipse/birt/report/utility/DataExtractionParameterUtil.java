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

package org.eclipse.birt.report.utility;

import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;

import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.CSVDataExtractionOption;

/**
 * Utility class to handle parameter related stuff...
 * 
 */
public class DataExtractionParameterUtil
{

	public static final String DEFAULT_SEP = ",";

	/**
	 * URL parameter name to indicate the export encoding.
	 */
	public static final String PARAM_EXPORT_ENCODING = "__exportencoding";//$NON-NLS-1$

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
	public static final String PARAM_RESULTSETNAME = "__resultsetname"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the selected column numbers of the export data
	 * form.
	 */
	public static final String PARAM_SELECTEDCOLUMNNUMBER = "__selectedcolumnnumber"; //$NON-NLS-1$

	/**
	 * Parameter name that gives the selected column names of the export data
	 * form.
	 */
	public static final String PARAM_SELECTEDCOLUMN = "__selectedcolumn"; //$NON-NLS-1$

	/**
	 * URL Parameter name to indicate whether exports locale neutral value.
	 */
	public static final String PARAM_LOCALENEUTRAL = "__localeneutral"; //$NON-NLS-1$

	/**
	 * Known export extension names.
	 */
	public static final String EXTRACTION_FORMAT_CSV = "csv";

	public static final String EXTRACTION_EXTENSION_CSV = "org.eclipse.birt.report.engine.dataextraction.csv";

	/**
	 * Get result set name.
	 * 
	 * @param options
	 * @return
	 */
	public static String getResultSetName( Map options )
	{
		if ( options != null )
			return (String) options.get( PARAM_RESULTSETNAME );
		else
			return null;
	}

	/**
	 * Get selected column name list.
	 * 
	 * @param options
	 * @return
	 */
	public static String[] getSelectedColumns( Map options )
	{
		if ( options == null )
			return null;

		int columnCount = 0;
		try
		{
			String numStr = (String) options.get( PARAM_SELECTEDCOLUMNNUMBER );
			if ( numStr != null )
				columnCount = Integer.parseInt( numStr );
		}
		catch ( Exception e )
		{
			columnCount = 0;
		}

		String[] columns = new String[columnCount];

		// get column names
		for ( int i = 0; i < columnCount; i++ )
		{
			String paramName = PARAM_SELECTEDCOLUMN + String.valueOf( i );
			String columnName = (String) options.get( paramName );
			columns[i] = columnName;
		}

		return columns;
	}

	/**
	 * Returns the separator String
	 * 
	 * @param request
	 * @return
	 */
	public static String getSep( Map options )
	{
		if ( options == null )
			return DEFAULT_SEP;

		String sepKey = (String) options.get( PARAM_SEP );		
		if ( sepKey == null )
			return DEFAULT_SEP;
		
		String key = "viewer.sep." + sepKey; //$NON-NLS-1$
		String sep = ParameterAccessor.getInitProp( key );
		if ( sep == null || sep.length( ) <= 0 )
			return DEFAULT_SEP;
		return sep;
	}

	/**
	 * Returns the encoding for export data.
	 * 
	 * @param options
	 * @return
	 */
	public static String getExportEncoding( Map options )
	{
		if ( options == null )
			return CSVDataExtractionOption.UTF_8_ENCODE;

		String encoding = (String) options.get( PARAM_EXPORT_ENCODING );

		// use UTF-8 as the default encoding
		if ( encoding == null )
			encoding = CSVDataExtractionOption.UTF_8_ENCODE;

		return encoding;
	}

	/**
	 * Returns whether exports column's data type
	 * 
	 * @param options
	 * @return
	 */
	public static boolean isExportDataType( Map options )
	{
		if ( options == null )
			return false;

		String flag = (String) options.get( PARAM_EXPORT_DATATYPE );
		if ( "true".equalsIgnoreCase( flag ) ) //$NON-NLS-1$
			return true;

		return false;
	}

	/**
	 * Returns whether exports locale neutral value
	 * 
	 * @param options
	 * @return
	 */
	public static boolean isLocaleNeutral( Map options )
	{
		if ( options == null )
			return false;

		String flag = (String) options.get( PARAM_LOCALENEUTRAL );
		if ( "true".equalsIgnoreCase( flag ) ) //$NON-NLS-1$
			return true;

		return false;
	}

	/**
	 * Create a CSVDataExtractionOption configured using the CSV-specific
	 * parameters. The general options are not set by default.
	 * 
	 * @param columns
	 * 		columns to export
	 * @param locale
	 * 		locale
	 * @param options
	 * 		general options to use for the configuration
	 * @return instance of CSVDataExtractionOption initialized with the passed
	 * 	values
	 */
	public static DataExtractionOption createCSVOptions( String[] columns,
			Locale locale, Map options )
	{
		CSVDataExtractionOption csvExtractOption = new CSVDataExtractionOption( );
		csvExtractOption.setEncoding( getExportEncoding( options ) );
		csvExtractOption.setExportDataType( isExportDataType( options ) );
		csvExtractOption.setLocaleNeutralFormat( isLocaleNeutral( options ) );
		csvExtractOption.setSelectedColumns( columns );
		csvExtractOption.setSeparator( getSep( options ) );
		csvExtractOption.setUserParameters( options );
		return csvExtractOption;
	}

	/**
	 * Returns an array of decoded columns names.
	 * 
	 * @param columns
	 * 		Collection of column names, in HTML format
	 * @return Returns an array of decoded columns names.
	 */
	public static String[] getColumnNames( Collection columns )
	{
		if ( columns != null && columns.size( ) > 0 )
		{
			String[] columnNames = new String[columns.size( )];
			Iterator iSelectedColumns = columns.iterator( );
			for ( int i = 0; iSelectedColumns.hasNext( ); i++ )
			{
				columnNames[i] = ParameterAccessor
						.htmlDecode( (String) iSelectedColumns.next( ) );
			}
			return columnNames;
		}
		else
		{
			return null;
		}
	}

}
