/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
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
	private Locale locale = null;

	/**
	 * @param format format to format report parameter, or recover parameter value as object
	 * given a string as report parameter value
	 * @param locale the locale to format/parse the parameter value
	 */
	public ReportParameterConverter( String format, Locale locale )
	{
		this.format = format;

		if ( this.format != null )
		{
			if ( this.format.indexOf( ":" ) != -1 )
			{
				this.format = this.format.substring( this.format.indexOf( ":" ) + 1 );
			}
		}

		this.locale = locale;
	}
	
	public ReportParameterConverter( String format, ULocale locale )
	{
		this(format, locale.toLocale( ));
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
		
		if ( reportParameterObj != null && locale != null )
		{
			if ( reportParameterObj instanceof String )
			{
				StringFormatter sf = new StringFormatter( locale );
				
				if ( format != null )
				{
					sf.applyPattern( format );
				}
				
				reportParameterValue = sf.format( ( String ) reportParameterObj );
			}
			else if ( reportParameterObj instanceof Date )
			{
				DateFormatter df = new DateFormatter( locale );
				
				if ( format != null )
				{
					df.applyPattern( format );
				}
				
				reportParameterValue = df.format( ( Date ) reportParameterObj );
			}
			else if ( reportParameterObj instanceof Double )
			{
				NumberFormatter nf = new NumberFormatter( locale );
				
				if ( format != null )
				{
					nf.applyPattern( format );
				}

				reportParameterValue = nf.format( ( ( Double ) reportParameterObj ).doubleValue( ) );
			}
			else if ( reportParameterObj instanceof BigDecimal )
			{
				NumberFormatter nf = new NumberFormatter( locale );

				if ( format != null )
				{
					nf.applyPattern( format );
				}
				
				reportParameterValue = nf.format( ( BigDecimal ) reportParameterObj );
			}
			else if ( reportParameterObj instanceof Boolean )
			{
				reportParameterValue = ( ( Boolean ) reportParameterObj ).toString( );
			}
			else if ( reportParameterObj instanceof Number )
			{
				NumberFormatter nf = new NumberFormatter( locale );
				
				if ( format != null )
				{
					nf.applyPattern( format );
				}

				reportParameterValue = nf.format( ( ( Number ) reportParameterObj ) );
			}
			else
			{
				reportParameterValue = ( reportParameterObj.toString( ) );
			}
		}
		
		return reportParameterValue;
	}

	/**
	 * Convert report parameter from string into object.
	 * 
	 * @param reportParameterValue report parameter value in string.
	 * @param parameterValueType report parameter type.
	 * @return parameter value object.
	 */
	public Object parse( String reportParameterValue, int parameterValueType )
	{
		Object parameterValueObj = null;
		
		if ( reportParameterValue != null && locale != null )
		{
			switch ( parameterValueType )
			{
				case IScalarParameterDefn.TYPE_STRING:
				{
					StringFormatter sf = new StringFormatter( locale );
					
					if ( format != null )
					{
						sf.applyPattern( format );
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
					DateFormatter df = new DateFormatter( locale );
					
					if ( format != null )
					{
						df.applyPattern( format );
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
					NumberFormatter nf = new NumberFormatter( locale );

					if ( format != null )
					{
						nf.applyPattern( format );
					}
					
					try
					{
						Number num = nf.parse( reportParameterValue );
	
						if ( num != null )
						{
							parameterValueObj = new Double( num.doubleValue( ) );
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
								parameterValueObj = new Double( num.doubleValue( ) );
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
					NumberFormatter nf = new NumberFormatter( locale );
						
					if ( format != null )
					{
						nf.applyPattern( format );
					}

					try
					{
						Number num = nf.parse( reportParameterValue );
	
						if ( num != null )
						{
							parameterValueObj = new BigDecimal( num.doubleValue( ) );
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
								parameterValueObj = new Double( num.doubleValue( ) );
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
			}
		}
		
		return parameterValueObj;
	}
}