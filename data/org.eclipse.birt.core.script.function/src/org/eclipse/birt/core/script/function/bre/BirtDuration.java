/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script.function.bre;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;


public class BirtDuration implements IScriptFunctionExecutor
{
	private IScriptFunctionExecutor executor;
	private static final String PLUGIN_ID = "org.eclipse.birt.core.function";
	
	public BirtDuration( String functionName ) throws BirtException
	{
		if ( "year".equals( functionName ) )
			this.executor = new Function_Year( );
		else if ( "month".equals( functionName ) )
			this.executor = new Function_Month( );
		else if ( "day".equals( functionName ) )
			this.executor = new Function_Day( );
		else if ( "hour".equals( functionName ) )
			this.executor = new Function_Hours( );
		else if ( "minute".equals( functionName ) )
			this.executor = new Function_Minutes( );
		else if ( "second".equals( functionName ) )
			this.executor = new Function_Seconds( );
		else if ( "timeInMills".equals( functionName ) )
			this.executor = new Function_TimeInMills( );
		else if ( "isLongerThan".equals( functionName ) )
			this.executor = new Function_Longer( );
		else if ( "isShorterThan".equals( functionName ) )
			this.executor = new Function_Shorter( );
		else
			throw new BirtException( PLUGIN_ID,
					null,
					Messages.getString( "invalid.function.name" )
							+ "BirtDuration." + functionName );
	}
	
	/*
	 * @see org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor#execute(java.lang.Object[], org.eclipse.birt.core.script.functionservice.IScriptFunctionContext)
	 */
	public Object execute( Object[] arguments, IScriptFunctionContext context )
			throws BirtException
	{
		return this.executor.execute( arguments, context );
	}
	
	private class Function_Year extends Function_temp
	{

		Function_Year( )
		{
			length = 1;
			isFixed = true;
		}
		
		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						new Object[]{
							args[0].toString( )
						} ) );
			}
			return Integer.valueOf( duration.getYears( ) );
		}
	}
	
	private class Function_Month extends Function_temp
	{

		Function_Month( )
		{
			length = 1;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						new Object[]{
							args[0].toString( )
						} ) );
			}
			return Integer.valueOf( duration.getMonths( ) );
		}
	}
	
	private class Function_Day extends Function_temp
	{

		Function_Day( )
		{
			length = 1;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						new Object[]{
							args[0].toString( )
						} ) );
			}
			return Integer.valueOf( duration.getDays( ) );
		}
	}
	
	private class Function_Hours extends Function_temp
	{

		Function_Hours( )
		{
			length = 1;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						new Object[]{
							args[0].toString( )
						} ) );
			}
			return Integer.valueOf( duration.getHours( ) );
		}
	}

	private class Function_Seconds extends Function_temp
	{

		Function_Seconds( )
		{
			length = 1;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						new Object[]{
							args[0].toString( )
						} ) );
			}
			return Integer.valueOf( duration.getSeconds( ) );
		}
	}
	
	private class Function_TimeInMills extends Function_temp
	{

		Function_TimeInMills( )
		{
			length = 2;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						new Object[]{
							args[0].toString( )
						} ) );
			}
			return Long.valueOf( duration.getTimeInMillis( DataTypeUtil.toDate( args[1] ) ) );
		}
	}
	
	private class Function_Minutes extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_Minutes( )
		{
			length = 1;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						new Object[]{
							args[0].toString( )
						} ) );
			}
			return Integer.valueOf( duration.getMinutes( ) );
		}
	}
	
	private class Function_Longer extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_Longer( )
		{
			length = 2;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration1, duration2;
			try
			{
				duration1 = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
				duration2 = DatatypeFactory.newInstance( )
						.newDuration( args[1].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						args ) );
			}
			return Boolean.valueOf( duration1.isLongerThan( duration2 ) );
		}
	}
	
	private class Function_Shorter extends Function_temp
	{
		Function_Shorter( )
		{
			length = 2;
			isFixed = true;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration1, duration2;
			try
			{
				duration1 = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
				duration2 = DatatypeFactory.newInstance( )
				.newDuration( args[1].toString( ) );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						args ) );
			}
			return Boolean.valueOf( duration1.isShorterThan( duration2 ) );
		}
	}
	
}
