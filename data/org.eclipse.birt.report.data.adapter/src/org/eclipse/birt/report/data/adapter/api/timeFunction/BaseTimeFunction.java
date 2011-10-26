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
package org.eclipse.birt.report.data.adapter.api.timeFunction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.adapter.api.ArgumentInfo;
import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

public class BaseTimeFunction implements ITimeFunction
{

	private String name, displayName, description;
	private IArgumentInfo period1, period2;
	private List<IArgumentInfo.Period_Type> timeType;
		
	public BaseTimeFunction( String functionName, String displayName, String description )
	{
		this.name = functionName;
		this.displayName = displayName;
		this.description = description;
	}
	
	public BaseTimeFunction( ITimeFunction function, List<IArgumentInfo.Period_Type> timeType )
	{
		this( function.getName( ), function.getDisplayName( ), function.getDescription( ) );
		this.timeType = timeType;
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
	 * Get display name for time function
	 */
	public String getDisplayName( )
	{
		return this.displayName;
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
	 * Get the argument info list if the function has
	 * @return the corresponding argument info in time functions.
	 */
	public List<IArgumentInfo> getArguments( )
	{
		List<IArgumentInfo> arguments = new ArrayList<IArgumentInfo>( );
		period1 = new ArgumentInfo( IArgumentInfo.PERIOD_1,
				Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD1_DISPLAYNAME ),
				Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD1 ),
				false );
		( (ArgumentInfo) period1 ).setPeriodChoices( timeType );
		period2 = new ArgumentInfo( IArgumentInfo.PERIOD_2,
				Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD2_DISPLAYNAME ),
				Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD2 ),
				false );
		( (ArgumentInfo )period2 ).setPeriodChoices( timeType );
		
		if ( this.name.equals( IBuildInBaseTimeFunction.PREVIOUS_MONTH ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );
		}
		else
		if( this.name.equals( IBuildInBaseTimeFunction.PREVIOUS_QUARTER ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );
		}
		else
		if( this.name.equals( IBuildInBaseTimeFunction.PREVIOUS_YEAR ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );
		}
		else
		if( this.name.equals( IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );			
		}
		else
		if( this.name.equals( IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );			
		}
		else
		if( this.name.equals( IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );
		}
		else if( this.name.equals( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );
			
		}
		else if( this.name.equals( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE ))
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					true ) );
		}
		else if ( this.name.equals( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO ) )
		{
			arguments.add( period1 );
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD2,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N2 ),
					false ) );
			arguments.add( period2 );
		}
		else if ( this.name.equals( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO ) )
		{
			arguments.add( period1 );
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD2,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N2 ),
					false ) );
			arguments.add( period2);
		}
		else if ( this.name.equals( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					false ) );
			arguments.add( period1 );
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD2,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N2_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N2 ),
					false ) );
			arguments.add( period2 );
		}
		else if ( this.name.equals( IBuildInBaseTimeFunction.NEXT_N_PERIODS ) )
		{
			arguments.add( new ArgumentInfo( IArgumentInfo.N_PERIOD1,
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1_DISPLAYNAME ),
					Message.getMessage( ResourceConstants.TIMEFUNCITON_N1 ),
					false ) );
			arguments.add( period1 );
		}
		return arguments;
	}
	
}
