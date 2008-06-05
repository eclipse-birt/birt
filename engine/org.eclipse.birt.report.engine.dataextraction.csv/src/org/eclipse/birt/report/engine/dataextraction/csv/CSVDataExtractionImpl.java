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

package org.eclipse.birt.report.engine.dataextraction.csv;

import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.impl.CommonDataExtractionImpl;

/**
 * Implements the logic to extract data as CSV format
 * 
 */
public class CSVDataExtractionImpl extends CommonDataExtractionImpl
{
	/**
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#initilize(org.eclipse.birt.report.engine.api.script.IReportContext,
	 *      org.eclipse.birt.report.engine.api.IDataExtractionOption)
	 */
	public void initilize( IReportContext context, IDataExtractionOption option )
			throws BirtException
	{
		assert option instanceof ICSVDataExtractionOption;
		super.initilize( context, option );
	}

	/**
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#output(org.eclipse.birt.report.engine.api.IExtractionResults)
	 */
	public void output( IExtractionResults results ) throws BirtException
	{
		ICSVDataExtractionOption options = (ICSVDataExtractionOption)this.getOptions( );
		String sep = options.getSeparator( );
		String encoding = options.getEncoding( );
		OutputStream outputStream = options.getOutputStream( );
		boolean isExportDataType = options.isExportDataType( );
		String[] columnNames = (String[]) options.getSelectedColumns( );
		
		try
		{
			// if selected columns are null or empty, returns all columns
			if ( columnNames == null || columnNames.length <= 0 )
			{
				int count = results.getResultMetaData( ).getColumnCount( );
				columnNames = new String[count];
				for ( int i = 0; i < count; i++ )
				{
					String colName = results.getResultMetaData( )
							.getColumnName( i );
					columnNames[i] = colName;
				}
			}

			IDataIterator iData = null;
			if ( results != null )
			{
				iData = results.nextResultIterator( );

				if ( iData != null && columnNames.length > 0 )
				{
					StringBuffer buf = new StringBuffer( );

					// Captions
					buf.append( csvConvertor( columnNames[0], sep ) );

					for ( int i = 1; i < columnNames.length; i++ )
					{
						buf.append( sep );
						buf.append( csvConvertor( columnNames[i], sep ) );
					}

					buf.append( '\n' );
					if ( encoding != null && encoding.trim( ).length( ) > 0 )
					{
						outputStream.write( buf.toString( ).getBytes(
								encoding.trim( ) ) );
					}
					else
					{
						outputStream.write( buf.toString( ).getBytes( ) );
					}
					buf.delete( 0, buf.length( ) );

					// Column data type
					if ( isExportDataType )
					{
						Map types = new HashMap( );
						int count = results.getResultMetaData( )
								.getColumnCount( );
						for ( int i = 0; i < count; i++ )
						{
							String colName = results.getResultMetaData( )
									.getColumnName( i );
							String colType = results.getResultMetaData( )
									.getColumnTypeName( i );
							types.put( colName, colType );
						}

						buf.append( (String) types.get( columnNames[0] ) );
						for ( int i = 1; i < columnNames.length; i++ )
						{
							buf.append( sep );
							buf.append( (String) types.get( columnNames[i] ) );
						}
						buf.append( '\n' );
						outputStream.write( buf.toString( ).getBytes( ) );
						buf.delete( 0, buf.length( ) );
					}

					// Data
					while ( iData.next( ) )
					{
						String value = null;

						try
						{
							// convert object to string
							value = csvConvertor( getStringValue( iData
									.getValue( columnNames[0] ) ), sep );
						}
						catch ( Exception e )
						{
							// do nothing
						}

						if ( value != null )
						{
							buf.append( value );
						}

						for ( int i = 1; i < columnNames.length; i++ )
						{
							buf.append( sep );

							try
							{
								// convert object to string
								value = csvConvertor( getStringValue( iData
										.getValue( columnNames[i] ) ), sep );
							}
							catch ( Exception e )
							{
								value = null;
							}

							if ( value != null )
							{
								buf.append( value );
							}
						}

						buf.append( '\n' );
						if ( encoding != null && encoding.trim( ).length( ) > 0 )
						{
							outputStream.write( buf.toString( ).getBytes(
									encoding.trim( ) ) );
						}
						else
						{
							outputStream.write( buf.toString( ).getBytes( ) );
						}
						buf.delete( 0, buf.length( ) );
					}
				}
			}
		}
		catch ( Exception e )
		{
		}
	}

	/**
	 * CSV format convertor. Here is the rule.
	 * 
	 * 1) Fields with given separator must be delimited with double-quote
	 * characters. 2) Fields that contain double quote characters must be
	 * surounded by double-quotes, and the embedded double-quotes must each be
	 * represented by a pair of consecutive double quotes. 3) A field that
	 * contains embedded line-breaks must be surounded by double-quotes. 4)
	 * Fields with leading or trailing spaces must be delimited with
	 * double-quote characters.
	 * 
	 * @param value
	 * @param sep
	 * @return the csv format string value
	 * @throws RemoteException
	 */
	private String csvConvertor( String value, String sep )
			throws RemoteException
	{
		if ( value == null )
		{
			return null;
		}

		value = value.replaceAll( "\"", "\"\"" ); //$NON-NLS-1$  //$NON-NLS-2$

		boolean needQuote = false;
		needQuote = ( value.indexOf( sep ) != -1 )
				|| ( value.indexOf( '"' ) != -1 )
				|| ( value.indexOf( 0x0A ) != -1 )
				|| value.startsWith( " " ) || value.endsWith( " " ); //$NON-NLS-1$ //$NON-NLS-2$
		value = needQuote ? "\"" + value + "\"" : value; //$NON-NLS-1$ //$NON-NLS-2$

		return value;
	}
}
