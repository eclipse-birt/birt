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

import java.util.Date;

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
	private static final long serialVersionUID = 1L;
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
		else if ( "getSign".equals( functionName ) )
			this.executor = new Function_GetSign( );
		else if ( "multiply".equals( functionName ) )
			this.executor = new Function_Multiply( );
		else if ( "negate".equals( functionName ) )
			this.executor = new Function_Negate( );
		else if ( "subtract".equals( functionName ) )
			this.executor = new Function_Subtract( );
		else if ( "add".equals( functionName ) )
			this.executor = new Function_Add( );
		else if ( "addTo".equals( functionName ) )
			this.executor = new Function_AddTo( );
		else if ( "compare".equals( functionName ) )
			this.executor = new Function_Compare( );
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
	
	private static class Function_Year extends Function_temp
	{

		private static final long serialVersionUID = 1L;

		Function_Year( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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
	
	private static class Function_Month extends Function_temp
	{

		private static final long serialVersionUID = 1L;

		Function_Month( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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
	
	private static class Function_Day extends Function_temp
	{

		private static final long serialVersionUID = 1L;

		Function_Day( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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
	
	private static class Function_Hours extends Function_temp
	{

		private static final long serialVersionUID = 1L;

		Function_Hours( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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

	private static class Function_Seconds extends Function_temp
	{

		private static final long serialVersionUID = 1L;

		Function_Seconds( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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
	
	private static class Function_TimeInMills extends Function_temp
	{

		private static final long serialVersionUID = 1L;

		Function_TimeInMills( )
		{
			minParamCount = 2;
			maxParamCount = 2;
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
	
	private static class Function_Minutes extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_Minutes( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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
	
	private static class Function_Longer extends Function_temp
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Function_Longer( )
		{
			minParamCount = 2;
			maxParamCount = 2;
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
	
	private static class Function_Shorter extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_Shorter( )
		{
			minParamCount = 2;
			maxParamCount = 2;
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
	
	private static class Function_GetSign extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_GetSign( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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
						args ) );
			}
			return Integer.valueOf( duration.getSign( ) );
		}
	}
	
	private static class Function_Multiply extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_Multiply( )
		{
			minParamCount = 2;
			maxParamCount = 2;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			int factor;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );

				factor = DataTypeUtil.toInteger( args[1] ).intValue( );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						args ) );
			}
			return duration.multiply( factor ).toString( );
		}
	}
	
	private static class Function_Negate extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_Negate( )
		{
			minParamCount = 1;
			maxParamCount = 1;
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
						args ) );
			}
			return duration.negate( ).toString( );
		}
	}
	
	private static class Function_Subtract extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_Subtract( )
		{
			minParamCount = 2;
			maxParamCount = 2;
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
			return duration1.subtract( duration2 ).toString( );
		}
	}
	
	private static class Function_Add extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_Add( )
		{
			minParamCount = 2;
			maxParamCount = 2;
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
			return duration1.add( duration2 ).toString( );
		}
	}
	
	private static class Function_AddTo extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_AddTo( )
		{
			minParamCount = 2;
			maxParamCount = 2;
		}

		protected Object getValue( Object[] args ) throws BirtException
		{
			Duration duration;
			Date date;
			try
			{
				duration = DatatypeFactory.newInstance( )
						.newDuration( args[0].toString( ) );
				date = DataTypeUtil.toDate( args[1] );
			}
			catch ( DatatypeConfigurationException e )
			{
				throw new IllegalArgumentException( Messages.getFormattedString( "error.BirtDuration.literal.invalidArgument",
						args ) );
			}
			duration.addTo( date );
			return date;
		}
	}
	
	private static class Function_Compare extends Function_temp
	{
		private static final long serialVersionUID = 1L;

		Function_Compare( )
		{
			minParamCount = 2;
			maxParamCount = 2;
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
			return Integer.valueOf( duration1.compare( duration2 ) );
		}
	}
}
