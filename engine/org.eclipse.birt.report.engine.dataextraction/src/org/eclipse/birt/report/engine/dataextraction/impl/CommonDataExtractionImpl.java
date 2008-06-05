
package org.eclipse.birt.report.engine.dataextraction.impl;

import java.util.Locale;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption;
import org.eclipse.birt.report.engine.extension.IDataExtractionExtension;

import com.ibm.icu.util.ULocale;

/**
 * Base implementation for the data extraction extensions.
 * It provides utility methods which are initialized according to the data 
 * extraction options. 
 */
public class CommonDataExtractionImpl implements IDataExtractionExtension
{

	private IReportContext context;
	private ICSVDataExtractionOption option;
	private DateFormatter dateFormatter = null;
	private NumberFormatter numberFormatter = null;
	private ULocale locale = null;
	private boolean isLocaleNeutral;

	public void initilize( IReportContext context, IDataExtractionOption option )
			throws BirtException
	{
		this.context = context;
		this.option = (ICSVDataExtractionOption) option;

		this.isLocaleNeutral = this.option.isLocaleNeutralFormat( );

		String dateFormat = this.option.getDateFormat( );
		// get locale info
		Locale aLocale = (Locale) this.option.getLocale( );
		if ( aLocale == null )
		{
			locale = ULocale.forLocale( Locale.getDefault( ) );
		}
		else
		{
			locale = ULocale.forLocale( aLocale );
		}

		if ( !isLocaleNeutral )
		{
			dateFormatter = createDateFormatter( dateFormat, locale );
			numberFormatter = new NumberFormatter( locale );
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
		return this.option;
	}
	
	/**
	 * Must be implemented by subclass.
	 */
	public void output( IExtractionResults results ) throws BirtException	
	{
		throw new UnsupportedOperationException("Must be implemented by subclass"); //$NON-NLS-1$
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
			ULocale locale )
	{
		DateFormatter dateFormatter = null;
		if ( dateFormat != null )
		{
			dateFormatter = new DateFormatter( dateFormat, locale );
		}
		else
		{
			dateFormatter = new DateFormatter( locale );
		}
		return dateFormatter;
	}

	/**
	 * Returns the string value by object, according the the isLocaleNeutral
	 * option and the specified date format, if available. 
	 * 
	 * @param obj object to render as string
	 * @return string representation of the object
	 * @throws BirtException
	 */
	protected String getStringValue( Object obj ) throws BirtException
	{
		String value = null;

		if ( isLocaleNeutral )
		{
			value = DataTypeUtil.toLocaleNeutralString( obj );
		}
		else
		{
			if ( obj instanceof java.util.Date || obj instanceof java.sql.Date
					|| obj instanceof java.sql.Time )
			{
				value = dateFormatter.format( (java.util.Date) obj );
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
