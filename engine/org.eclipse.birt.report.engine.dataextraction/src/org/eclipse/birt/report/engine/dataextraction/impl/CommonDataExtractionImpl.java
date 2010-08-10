/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.dataextraction.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.dataextraction.CommonDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.ICommonDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.i18n.Messages;
import org.eclipse.birt.report.engine.extension.DataExtractionExtensionBase;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Base implementation for the data extraction extensions.
 * It provides utility methods which are initialized according to the data 
 * extraction options. 
 */
public class CommonDataExtractionImpl extends DataExtractionExtensionBase
{
	protected String PLUGIN_ID = "org.eclipse.birt.report.engine.dataextraction"; //$NON-NLS-1$
	
	private IReportContext context;
	private IDataExtractionOption options;
	private DateFormatter dateFormatter = null;
	private NumberFormatter numberFormatter = null;
	private ULocale locale = null;
	private TimeZone timeZone = null;
	private boolean isLocaleNeutral;

	private Map<Object, DateFormatter> columnFormatterMap;

	/**
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#initialize(org.eclipse.birt.report.engine.api.script.IReportContext, org.eclipse.birt.report.engine.api.IDataExtractionOption)
	 */
	public void initialize( IReportContext context, IDataExtractionOption options )
			throws BirtException
	{
		this.context = context;
		this.options = options;
		
		if ( options.getOutputStream( ) == null )
		{	
			throw new BirtException( PLUGIN_ID,
					Messages.getString( "exception.dataextraction.options.outputstream_required" ), null ); //$NON-NLS-1$
		}
		
		initCommonOptions(context, options);
	}

	/**
	 * Initializes the common options based on the data extraction option. If
	 * the passed option doesn't contain common options, use default values.
	 * 
	 * @param context
	 * @param options
	 *            options
	 */
	private void initCommonOptions( IReportContext context,
			IDataExtractionOption options )
	{
		String dateFormat = null;
		ICommonDataExtractionOption commonOptions;		
		if ( options instanceof ICommonDataExtractionOption )
		{
			commonOptions = (ICommonDataExtractionOption)options;
		}
		else
		{
			commonOptions = new CommonDataExtractionOption(options.getOptions( ));
		}
		
		this.isLocaleNeutral = commonOptions.isLocaleNeutralFormat( );

		dateFormat = commonOptions.getDateFormat( );
		// get locale info
		Locale aLocale = null;
		if ( context.getLocale( ) != null )
		{
			aLocale = context.getLocale( );
		}
		else
		{
			aLocale = commonOptions.getLocale( );
		}
		if ( aLocale == null )
		{
			this.locale = ULocale.forLocale( Locale.getDefault( ) );
		}
		else
		{
			this.locale = ULocale.forLocale( aLocale );
		}

		if ( context.getTimeZone( ) != null )
		{
			timeZone = context.getTimeZone( );
		}
		else
		{
			java.util.TimeZone javaTimeZone = commonOptions.getTimeZone( );
			if ( javaTimeZone != null )
			{
				// convert java time zone to ICU time zone
				this.timeZone = TimeZone.getTimeZone( javaTimeZone.getID( ) );
			}
			else
			{
				this.timeZone = null;
			}
		}
			
		if ( !isLocaleNeutral )
		{
			dateFormatter = createDateFormatter( dateFormat, this.locale, this.timeZone );
			numberFormatter = new NumberFormatter( this.locale );
		}

		Map formatters = commonOptions.getFormatter( );
		if ( formatters != null && !formatters.isEmpty( ) )
		{
			columnFormatterMap = new HashMap<Object, DateFormatter>( );
			for ( Object key : formatters.keySet( ) )
			{
				String format = (String) formatters.get( key );
				if ( format != null )
				{
					DateFormatter columnDateFormatter = createDateFormatter( format,
							this.locale,
							this.timeZone );
					columnFormatterMap.put( key, columnDateFormatter );
				}
			}
		}
	}

	/**
	 * Returns the report context with which this instance has
	 * been initialized.
	 * @return report context instance
	 */
	public IReportContext getReportContext()
	{
		return context;
	}
	
	/**
	 * Returns the data extraction options with which this instance
	 * has been initialized
	 * @return instance of IDataExtractionOption
	 */
	public IDataExtractionOption getOptions()
	{
		return this.options;
	}
	
	/**
	 * Must be implemented by subclass.
	 */
	public void output( IExtractionResults results ) throws BirtException	
	{
		throw new BirtException( PLUGIN_ID,
				Messages.getString( "exception.dataextraction.missing_implementation" ), null ); //$NON-NLS-1$
	}

	/**
	 * Must be implemented by subclass.
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#release()
	 */
	public void release( )
	{
	}

	/**
	 * Creates a localized date formatter.
	 * 
	 * @param dateFormat
	 * 		date format string or null for default
	 */
	private DateFormatter createDateFormatter( String dateFormat,
			ULocale locale, TimeZone timeZone )
	{
		return new DateFormatter( dateFormat, locale, timeZone );
	}

	/**
	 * Returns the string value by object, according the the isLocaleNeutral
	 * option and the specified date format, if available.
	 * 
	 * @param dataIterator
	 * 
	 * @param columnNames
	 * @param index
	 * @return string representation of the object
	 * @throws BirtException
	 */
	protected String getStringValue( IDataIterator dataIterator,
			String[] columnNames, int index ) throws BirtException
	{
		Object obj = dataIterator.getValue( columnNames[index] );

		String value = null;
		if ( isLocaleNeutral )
		{
			value = DataTypeUtil.toLocaleNeutralString( obj );
		}
		else
		{
			// implicitly includes its child classes java.sql.Date,
			// java.sql.Time and java.sql.Timestamp
			if ( obj instanceof java.util.Date )
			{
				DateFormatter formatter =null;
				if ( columnFormatterMap != null )
				{
					formatter = columnFormatterMap.get( index + 1 );
					if ( formatter == null )
					{
						formatter = columnFormatterMap.get( columnNames[index] );
					}
				}
				if ( formatter == null )
				{
					formatter = dateFormatter;
				}
				value = formatter.format( (java.util.Date) obj );
			}
			else if ( obj instanceof Number )
			{
				value = numberFormatter.format( (Number) obj );
			}
			else
			{
				value = DataTypeUtil.toString( obj, locale );
			}
		}

		return value;
	}

}
