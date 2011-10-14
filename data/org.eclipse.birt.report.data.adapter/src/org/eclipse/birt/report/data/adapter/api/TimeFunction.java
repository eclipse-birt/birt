/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api;

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

public class TimeFunction 
{

	public static TimeFunction CURRENT_QUARTER = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_QUARTER ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_QUARTER_DES ) );
	public static TimeFunction CURRENT_MONTH = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_MONTH ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_MONTH_DES ) );
	public static TimeFunction PREVIOUS_MONTH = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_DES ) );
	public static TimeFunction PREVIOUS_MONTH_TO_DATE = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_TO_DATE_DES ) );
	public static TimeFunction PREVIOUS_QUARTER = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_DES ) );
	public static TimeFunction PREVIOUS_QUARTER_TO_DATE = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_TO_DATE_DES ) );
	public static TimeFunction PREVIOUS_YEAR = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_DES ) );
	public static TimeFunction TRAILING_30_DAYS = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_30_DAYS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_30_DAYS_DES ) );
	public static TimeFunction TRAILING_60_DAYS = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_60_DAYS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_60_DAYS_DES ) );
	public static TimeFunction TRAILING_90_DAYS = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_90_DAYS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_90_DAYS_DES ) );
	public static TimeFunction TRAILING_12_MONTHS = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_12_MONTHS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_12_MONTHS_DES ) );
	public static TimeFunction YEAR_TO_DATE = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_YEAR_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_YEAR_TO_DATE_DES ) );
	public static TimeFunction QUARTER_TO_DATE = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_DES ) );
	public static TimeFunction MONTH_TO_DATE = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_DES ) );
	public static TimeFunction PREVIOUS_YEAR_TO_DATE = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_TO_DATE_DES ) );
	public static TimeFunction MONTH_TO_DATE_LAST_YEAR = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_LAST_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_LAST_YEAR_DES ) );
	public static TimeFunction QUARTER_TO_DATE_LAST_YEAR = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_LAST_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_LAST_YEAR_DES ) );
	public static TimeFunction CURRENT_YEAR = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_YEAR_DES ) );
	public static TimeFunction WEEK_TO_DATE = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE_DES ) );

	//complex time function
	public static TimeFunction CURRENT_PERIOD_FROM_N_PERIOD_AGO = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_PERIOD_FROM_N_PERIOD_AGO ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_PERIOD_FROM_N_PERIOD_AGO_DES ) );
	public static TimeFunction PERIOD_TO_DATE_FROM_N_PERIOD_AGO = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD_TO_DATE_FROM_N_PERIOD_AGO ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD_TO_DATE_FROM_N_PERIOD_AGO_DES ) );
	public static TimeFunction TRAILING_N_PERIOD_FROM_N_PERIOD_AGO = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_N_PERIOD_FROM_N_PERIOD_AGO_DES ) );
	public static TimeFunction NEXT_N_PERIODS = new TimeFunction( Message.getMessage( ResourceConstants.TIMEFUNCITON_NEXT_N_PERIODS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_NEXT_N_PERIODS_DES ) );
	
	public enum Period_Type { DAY, MONTH, QUARTER, WEEK, YEAR };
	
	private Period_Type periodType;
	private String name, description;
	private List<Period_Type> periodTypeList;
	
	private HashMap argumentValue;
	
	public static final String period1 = Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD1 );
	public static final String period2 = Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD2 );
	public static final String N_value_1 = Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 );
	public static final String N_value_2 = Message.getMessage( ResourceConstants.TIMEFUNCITON_N2 );
	
	private TimeFunction( String functionName, String description )
	{
		this.name = functionName;
		this.description = description;
	}
	
	public TimeFunction( TimeFunction function, List<Period_Type> timeType )
	{
		this.name = function.getName( );
		this.argumentValue = new HashMap( );
		this.description = function.getDescription( );
		this.periodTypeList = timeType;
	}
	
	public TimeFunction( TimeFunction function )
	{
		this.name = function.getName( );
		this.argumentValue = new HashMap( );
		this.description = function.getDescription( );
	}
	
	/**
	 * Get time function name
	 * @return the time function name
	 */
	public String getName( )
	{
		return this.name;
	}
	
	/**
	 * Get the description for time function
	 * @return the description for time function
	 */
	public String getDescription( )
	{
		return this.description;
	}
	
	/**
	 * Get the argument name if the function has
	 * @return the corresponding argument name in time functions.
	 */
	public String[] getArgumentNames( )
	{
		if ( this.name.equals( CURRENT_PERIOD_FROM_N_PERIOD_AGO.getName( ) ) )
		{
			return new String[]{
					period1, N_value_2, period2
			};
		}
		else if ( this.name.equals( PERIOD_TO_DATE_FROM_N_PERIOD_AGO.getName( ) ) )
		{
			return new String[]{
					period1, N_value_2, period2
			};
		}
		else if ( this.name.equals( TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.getName( ) ) )
		{
			return new String[]{
					N_value_1, period1, N_value_2, period2
			};
		}
		else if ( this.name.equals( NEXT_N_PERIODS.getName( ) ) )
		{
			return new String[]{
					N_value_1, period1
			};
		}
		return new String[0];
	}
	
	/**
	 * Get the available <code>TimeFunction.Period_Type<code> for period argument
	 * @param argumentName
	 * @return the available Period_Type list for specified argument
	 */
	public List<Period_Type> getAvailableArguments( String argumentName )
	{
		return this.periodTypeList;
	}
	
	/**
	 * Set argument's value
	 * @param name
	 * @param value
	 */
	public void setArgumentValue( String name, Object value )
	{
		if ( argumentValue != null )
			argumentValue.put( name, value );
	}
	
	/**
	 * Get argument's value for specified argument
	 * @param name
	 * @return
	 */
	public Object getArgumentValue( String name )
	{
		if ( argumentValue != null )
			return this.argumentValue.get( name );
		else
			return null;
	}
}
