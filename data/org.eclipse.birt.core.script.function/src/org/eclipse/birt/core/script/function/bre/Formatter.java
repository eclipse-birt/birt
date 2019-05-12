/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script.function.bre;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class Formatter implements IScriptFunctionExecutor
{

	private static final long serialVersionUID = 1L;
	private static final String WRONG_ARGUMENT = "Wrong number of arguments for BirtFormatter function: {0}";
	private static final String FORMAT = "format";

	private IScriptFunctionExecutor executor;
	private ULocale locale = null;
	private TimeZone timeZone = null;

	/**
	 * utilities used in the report execution.
	 */

	private ThreadLocal<HashMap<String, StringFormatter>> stringFormatters = new ThreadLocal<HashMap<String, StringFormatter>>( ) {

		@Override
		protected HashMap<String, StringFormatter> initialValue( )
		{
			return new HashMap<String, StringFormatter>( );
		}
	};

	private ThreadLocal<HashMap<String, NumberFormatter>> numberFormatters = new ThreadLocal<HashMap<String, NumberFormatter>>( ) {

		@Override
		protected HashMap<String, NumberFormatter> initialValue( )
		{
			return new HashMap<String, NumberFormatter>( );
		}
	};

	private ThreadLocal<HashMap<String, DateFormatter>> dateFormatters = new ThreadLocal<HashMap<String, DateFormatter>>( ) {

		@Override
		protected HashMap<String, DateFormatter> initialValue( )
		{
			return new HashMap<String, DateFormatter>( );
		}
	};

	Formatter( String functionName ) throws BirtException
	{
		if ( FORMAT.equals( functionName ) )
		{
			executor = new Function_Format( this );
		}
	}

	public DateFormatter getDateFormatter( String pattern )
	{
		String key = pattern + ":" + locale.toString( );
		DateFormatter fmt = dateFormatters.get( ).get( key );
		if ( fmt == null )
		{
			fmt = new DateFormatter( pattern, locale, timeZone );
			dateFormatters.get( ).put( key, fmt );
		}
		return fmt;
	}

	public Object execute( Object[] arguments, IScriptFunctionContext scriptContext )
			throws BirtException
	{
		if ( scriptContext != null )
		{
			locale = (ULocale) scriptContext
					.findProperty( org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.LOCALE );
			timeZone = (TimeZone) scriptContext
					.findProperty( org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.TIMEZONE );
		}
		if ( timeZone == null )
		{
			timeZone = TimeZone.getDefault( );
		}
		return executor.execute( arguments, scriptContext );
	}

	public StringFormatter getStringFormatter( String pattern )
	{
		String key = pattern + ":" + locale.toString( );
		StringFormatter fmt = stringFormatters.get( ).get( key );
		if ( fmt == null )
		{
			fmt = new StringFormatter( pattern, locale );
			stringFormatters.get( ).put( key, fmt );
		}
		return fmt;
	}

	public NumberFormatter getNumberFormatter( String pattern )
	{
		String key = pattern + ":" + locale.toString( );
		NumberFormatter fmt = numberFormatters.get( ).get( key );
		if ( fmt == null )
		{
			fmt = new NumberFormatter( pattern, locale );
			numberFormatters.get( ).put( key, fmt );
		}
		return fmt;
	}

	private class Function_Format implements IScriptFunctionExecutor
	{

		private static final long serialVersionUID = 1L;
		private Formatter formatter;

		Function_Format( Formatter formatter )
		{
			this.formatter = formatter;
		}

		public Object execute( Object[] args, IScriptFunctionContext context )
				throws BirtException
		{
			if ( args == null || args.length < 2 )
			{
				throw new IllegalArgumentException( MessageFormat.format(
						WRONG_ARGUMENT, new Object[]{FORMAT} ) );
			}
			if ( args[0] != null )
			{
				String pattern = null;
				if ( args[1] != null )
				{
					pattern = args[1].toString( );
				}
				if ( args[0] instanceof Number )
				{
					NumberFormatter fmt = formatter
							.getNumberFormatter( pattern );
					return fmt.format( (Number) args[0] );
				}
				else if ( args[0] instanceof String )
				{
					StringFormatter fmt = formatter
							.getStringFormatter( pattern );
					return fmt.format( (String) args[0] );

				}
				else if ( args[0] instanceof Date )
				{
					DateFormatter fmt = formatter.getDateFormatter( pattern );
					return fmt.format( (Date) args[0] );
				}
				else
				{
					return args[0].toString( );
				}
			}
			return "NULL";
		}
	}

}