/*************************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;

import com.ibm.icu.util.ULocale;

/**
 * Utilites class to convert report paramete value between object and string. 
 */
public class ReportParameterConverter
{
	private String format = null;
	private ULocale uLocale = null;
	
	private StringFormatter sf = null;
	private DateFormatter df = null;
	private NumberFormatter nf = null;

	/**
	 * @param format format to format report parameter, or recover parameter value as object
	 * given a string as report parameter value
	 * @param locale the locale to format/parse the parameter value
	 */
	public ReportParameterConverter( String format, Locale locale )
	{
		this( format, ULocale.forLocale( locale ) );
	}

	public ReportParameterConverter( String format, ULocale uLocale )
	{
		this.format = format;
		this.uLocale = uLocale;
	}
	
	private StringFormatter getStringFormatter( )
	{
		if ( sf == null && uLocale != null )
		{
			sf = new StringFormatter( uLocale );
			if ( format != null )
			{
				sf.applyPattern( format );
			}
		}
		return sf;
	}
	
	private NumberFormatter getNumberFormatter( )
	{
		if ( nf == null && uLocale != null )
		{
			nf = new NumberFormatter( uLocale );
			if ( format != null )
			{
				nf.applyPattern( format );
			}
		}
		return nf;
	}
	
	private DateFormatter getDateFormatter( )
	{
		if ( df == null && uLocale != null )
		{
			df = new DateFormatter( uLocale );
			if ( format != null )
			{
				df.applyPattern( format );
			}
		}
		return df;
	}

	/**
	 * Convert report parameter value object into string.
	 * 
	 * @param reportParameterObj report parameter value object.
	 * @return parameter value in string.
	 */
	public String format( Object reportParameterObj )
	{
		String reportParameterValue = null;
		
		if ( reportParameterObj != null && uLocale != null )
		{
			if ( reportParameterObj instanceof String )
			{
				StringFormatter sf = getStringFormatter( );
				if ( sf != null )
				{
					reportParameterValue = sf.format( ( String ) reportParameterObj );
				}
				else
				{
					reportParameterValue = reportParameterObj.toString( );
				}
			}
			else if ( reportParameterObj instanceof Date )
			{
				DateFormatter df = getDateFormatter( );
				if ( df != null )
				{
					reportParameterValue = df.format( ( Date ) reportParameterObj );
				}
				else
				{
					reportParameterValue = reportParameterObj.toString( );
				}				
			}
			else if ( reportParameterObj instanceof Double )
			{
				NumberFormatter nf = getNumberFormatter( );
				if ( nf != null )
				{
					reportParameterValue = nf
							.format( ( (Double) reportParameterObj )
									.doubleValue( ) );
				}
				else
				{
					reportParameterValue = reportParameterObj.toString( );
				}						
			}
			else if ( reportParameterObj instanceof BigDecimal )
			{
				NumberFormatter nf = getNumberFormatter( );				
				if ( nf != null )
				{
					reportParameterValue = nf.format( ( BigDecimal ) reportParameterObj );
				}
				else
				{
					reportParameterValue = reportParameterObj.toString( );
				}
			}
			else if ( reportParameterObj instanceof Boolean )
			{
				reportParameterValue = ( ( Boolean ) reportParameterObj ).toString( );
			}
			else if ( reportParameterObj instanceof Number )
			{
				NumberFormatter nf = getNumberFormatter( );				
				if ( nf != null )
				{
					reportParameterValue = nf.format( ( ( Number ) reportParameterObj ) );
				}
				else
				{
					reportParameterValue = reportParameterObj.toString( );
				}
			}
			else
			{
				reportParameterValue = ( reportParameterObj.toString( ) );
			}
		}
		
		return reportParameterValue;
	}

	/**
	 * Convert report parameter from string into object. Need to be pointed out
	 * is it return a Double object when the value type is Float.
	 * 
	 * @param reportParameterValue
	 *            report parameter value in string.
	 * @param parameterValueType
	 *            report parameter type.
	 * @return parameter value object.
	 */
	public Object parse( String reportParameterValue, int parameterValueType )
	{
		Object parameterValueObj = null;
		
		if ( reportParameterValue != null && uLocale != null )
		{
			switch ( parameterValueType )
			{
				case IScalarParameterDefn.TYPE_STRING:
				{
					StringFormatter sf = getStringFormatter( );
					if ( sf == null )
					{
						parameterValueObj = null;
						break;
					}
	
					try
					{
						parameterValueObj = sf.parser( reportParameterValue );
					}
					catch ( ParseException e )
					{
						parameterValueObj = reportParameterValue;
					}
					break;
				}
				
				case IScalarParameterDefn.TYPE_DATE_TIME:
				{
					DateFormatter df = getDateFormatter( );
					if ( df == null )
					{
						parameterValueObj = null;
						break;
					}
	
					try
					{
						parameterValueObj = df.parse( reportParameterValue );
					}
					catch ( ParseException e )
					{
						df.applyPattern( "Short Date" );
						
						try
						{
							parameterValueObj = df.parse( reportParameterValue );
						}
						catch ( ParseException ex )
						{
							df.applyPattern( "Medium Time" );
							
							try
							{
								parameterValueObj = df.parse( reportParameterValue );
							}
							catch ( ParseException exx )
							{
								parameterValueObj = null;
							}
						}
					}
	
					break;
				}
	
				case IScalarParameterDefn.TYPE_FLOAT:
				{
					NumberFormatter nf = getNumberFormatter( );				
					if ( nf == null )
					{
						parameterValueObj = null;
						break;
					}
					
					try
					{
						Number num = nf.parse( reportParameterValue );
	
						if ( num != null )
						{
							parameterValueObj = new Double( num.toString( ) );
						}
					}
					catch ( ParseException e )
					{
						nf.applyPattern( "General Number" );
						
						try
						{
							Number num = nf.parse( reportParameterValue );
	
							if ( num != null )
							{
								parameterValueObj = new Double( num.toString( ) );
							}
						}
						catch ( ParseException ex )
						{
							parameterValueObj = null;
						}
					}
	
					break;
				}
				
				case IScalarParameterDefn.TYPE_DECIMAL:
				{
					NumberFormatter nf = getNumberFormatter( );				
					if ( nf == null )
					{
						parameterValueObj = null;
						break;
					}

					try
					{
						Number num = nf.parse( reportParameterValue );
	
						if ( num != null )
						{
							parameterValueObj = new BigDecimal( num.toString( ) );
						}
					}
					catch ( ParseException e )
					{
						nf.applyPattern( "General Number" );
						
						try
						{
							Number num = nf.parse( reportParameterValue );
	
							if ( num != null )
							{
								parameterValueObj = new BigDecimal( num.toString( ) );
							}
						}
						catch ( ParseException ex )
						{
							parameterValueObj = null;
						}
					}
	
					break;
				}
	
				case IScalarParameterDefn.TYPE_BOOLEAN:
				{
					parameterValueObj = Boolean.valueOf( reportParameterValue );
					break;
				}
				
				//can use class DataTypeUtil to convert
				case IScalarParameterDefn.TYPE_INTEGER:
				{
					NumberFormatter nf = getNumberFormatter( );				
					if ( nf == null )
					{
						parameterValueObj = null;
						break;
					}
					
					try
					{
						Number num = nf.parse( reportParameterValue );
						
						if ( num != null )
						{
							parameterValueObj = new Integer( num.intValue( ) );
						}
					}
					catch( ParseException ex )
					{
						nf.applyPattern( "General Number" );
						
						try
						{
							Number num = nf.parse( reportParameterValue );
							
							if ( num != null )
							{
								parameterValueObj = new Integer( num.intValue( ) );
							}
						}
						catch( ParseException pex )
						{
							try
							{
								parameterValueObj = new Integer( reportParameterValue );
							}
							catch( NumberFormatException nfe )
							{
								parameterValueObj = null;
							}
						}
					}
				}
			}
		}
		
		return parameterValueObj;
	}
}