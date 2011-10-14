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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.adapter.api.TimeFunction.Period_Type;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.TimePeriodHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.TimePeriod;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;

public class TimeFunctionUtil
{
	public static String TODAY_EXPR = "new java.util.Date( )";

	/**
	 * Get available time function for xTab
	 * @param dim
	 * @param timeLevelsInXtab
	 * @param isStaticReferenceDate
	 * @return
	 * @throws AdapterException
	 * @see org.eclipse.birt.report.data.adapter.api.TimeFunction
	 */
	public static List<TimeFunction> getCalculationTypes( DimensionHandle dim,
			List<String> timeLevelsInXtab, boolean isStaticReferenceDate )
			throws AdapterException
	{
		List<TimeFunction> availableFunctions = new ArrayList<TimeFunction>( );

		List<String> timeType = new ArrayList<String>( );
		if ( dim != null && dim.isTimeType( ) )
		{
			String mostDetailedLevel = null;
			if ( !timeLevelsInXtab.isEmpty( ) && !isStaticReferenceDate )
			{
				mostDetailedLevel = timeLevelsInXtab.get( timeLevelsInXtab.size( ) - 1 ).toString( );
			}
			TabularHierarchyHandle hierhandle = (TabularHierarchyHandle) dim.getDefaultHierarchy( );
			List levels = hierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );
			for ( int i = 0; i < levels.size( ); i++ )
			{
				TabularLevelHandle level = (TabularLevelHandle) levels.get( i );
				if ( mostDetailedLevel != null
						&& level.getName( )
								.equals( mostDetailedLevel ) )
				{
					timeType.add( level.getDateTimeLevelType( ) );
					break;
				}
				else
				{
					timeType.add( level.getDateTimeLevelType( ) );
				}
			}
		}
		else
		{
			return availableFunctions;
		}

		List<Period_Type> periodType = new ArrayList<Period_Type>( );
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) )
		{
			availableFunctions.add( TimeFunction.CURRENT_YEAR );
			availableFunctions.add( TimeFunction.PREVIOUS_YEAR );
			availableFunctions.add( TimeFunction.PREVIOUS_YEAR_TO_DATE );
			availableFunctions.add( TimeFunction.YEAR_TO_DATE );
			
			periodType.add( TimeFunction.Period_Type.YEAR );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) )
		{
			availableFunctions.add( TimeFunction.CURRENT_QUARTER );
			availableFunctions.add( TimeFunction.PREVIOUS_QUARTER );
			availableFunctions.add( TimeFunction.PREVIOUS_QUARTER_TO_DATE );
			availableFunctions.add( TimeFunction.QUARTER_TO_DATE );

			periodType.add( TimeFunction.Period_Type.QUARTER );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) )
		{
			availableFunctions.add( TimeFunction.CURRENT_MONTH );
			availableFunctions.add( TimeFunction.PREVIOUS_MONTH );
			availableFunctions.add( TimeFunction.PREVIOUS_MONTH_TO_DATE );
			availableFunctions.add( TimeFunction.MONTH_TO_DATE );
			availableFunctions.add( TimeFunction.TRAILING_12_MONTHS );

			periodType.add( TimeFunction.Period_Type.MONTH );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) )
		{
			availableFunctions.add( TimeFunction.TRAILING_30_DAYS );
			availableFunctions.add( TimeFunction.TRAILING_60_DAYS );
			availableFunctions.add( TimeFunction.TRAILING_90_DAYS );
			
			periodType.add( TimeFunction.Period_Type.DAY );			
		}
		//for WTD, only support static reference date
		if ( isStaticReferenceDate
				&& timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR ) )
		{
			availableFunctions.add( TimeFunction.WEEK_TO_DATE );
		}

		availableFunctions.add( new TimeFunction( TimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO,
				periodType ) );
		availableFunctions.add( new TimeFunction( TimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO,
				periodType ) );
		availableFunctions.add( new TimeFunction( TimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO,
				periodType ) );
		availableFunctions.add( new TimeFunction( TimeFunction.NEXT_N_PERIODS,
				periodType ) );

		return availableFunctions;
	}
	

	/**
	 * Update time function in computed handle
	 * @param handle
	 * @param function
	 * @param referenceDate
	 * @throws SemanticException
	 */
	public static void populateComputedColumn( ComputedColumnHandle handle,
			TimeFunction function, String referenceDate ) throws SemanticException
	{
		handle.getReferenceDate( ).setExpression( new Expression( referenceDate, ExpressionType.JAVASCRIPT ) );
		
		TimePeriod baseTimePeriod = new TimePeriod( );
		TimePeriod relativeTimePeriod = new TimePeriod( );
		
		if ( function.getName( )
				.equals( TimeFunction.CURRENT_QUARTER.getName( ) )
				|| function.getName( )
						.equals( TimeFunction.QUARTER_TO_DATE.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
			baseTimePeriod.setNumberOfUnit( 0 );
		}
		else if ( function.getName( )
				.equals( TimeFunction.CURRENT_MONTH.getName( ) )
				|| function.getName( )
						.equals( TimeFunction.MONTH_TO_DATE.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
			baseTimePeriod.setNumberOfUnit( 0 );
		}
		else if( function.getName( ).equals( TimeFunction.PREVIOUS_MONTH.getName( ) ) )
		{
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
			relativeTimePeriod.setNumberOfUnit( 1 );			
		}
		else if( function.getName( ).equals( TimeFunction.PREVIOUS_MONTH_TO_DATE.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
			baseTimePeriod.setNumberOfUnit( 0 );
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
			relativeTimePeriod.setNumberOfUnit( 1 );
		}
		else if( function.getName( ).equals( TimeFunction.PREVIOUS_QUARTER.getName( ) ) )
		{
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
			relativeTimePeriod.setNumberOfUnit( 1 );
		}
		else if( function.getName( ).equals( TimeFunction.PREVIOUS_QUARTER_TO_DATE.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
			baseTimePeriod.setNumberOfUnit( 0 );
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
			relativeTimePeriod.setNumberOfUnit( 1 );
		}
		else if( function.getName( ).equals( TimeFunction.PREVIOUS_YEAR.getName( ) ) )
		{
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
			relativeTimePeriod.setNumberOfUnit( 1 );
		}
		else if( function.getName( ).equals( TimeFunction.TRAILING_30_DAYS.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_DAY );
			baseTimePeriod.setNumberOfUnit( -30 );
		}
		else if( function.getName( ).equals( TimeFunction.TRAILING_60_DAYS.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_DAY );
			baseTimePeriod.setNumberOfUnit( -60 );
		}
		else if( function.getName( ).equals( TimeFunction.TRAILING_90_DAYS.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_DAY );
			baseTimePeriod.setNumberOfUnit( -90 );
		}
		else if( function.getName( ).equals( TimeFunction.TRAILING_12_MONTHS.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
			baseTimePeriod.setNumberOfUnit( -12 );
		}
		else if ( function.getName( )
				.equals( TimeFunction.YEAR_TO_DATE.getName( ) )
				|| function.getName( )
						.equals( TimeFunction.CURRENT_YEAR.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
			baseTimePeriod.setNumberOfUnit( 0 );
		}
		else if( function.getName( ).equals( TimeFunction.PREVIOUS_YEAR_TO_DATE.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
			baseTimePeriod.setNumberOfUnit( 0 );
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
			relativeTimePeriod.setNumberOfUnit( 1 );
		}
		else if( function.getName( ).equals( TimeFunction.MONTH_TO_DATE_LAST_YEAR.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
			baseTimePeriod.setNumberOfUnit( 0 );
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
			relativeTimePeriod.setNumberOfUnit( 1 );
		}
		else if( function.getName( ).equals( TimeFunction.QUARTER_TO_DATE_LAST_YEAR.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
			baseTimePeriod.setNumberOfUnit( 0 );
			relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
			relativeTimePeriod.setNumberOfUnit( 1 );
		}
		else if( function.getName( ).equals( TimeFunction.WEEK_TO_DATE.getName( ) ) )
		{
			baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_WEEK );
			baseTimePeriod.setNumberOfUnit( 0 );
		}
		else if ( function.getName( )
				.equals( TimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.getName( ) )
				|| function.getName( )
						.equals( TimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.getName( ) ) )
		{
			if ( function.getArgumentValue( TimeFunction.period1 ).equals( Period_Type.YEAR ) )
			{
				baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
				baseTimePeriod.setNumberOfUnit( 0 );
			}
			else
			if( function.getArgumentValue( TimeFunction.period1 ).equals( Period_Type.MONTH ) )
			{
				baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
				baseTimePeriod.setNumberOfUnit( 0 );				
			}
			else
			if( function.getArgumentValue( TimeFunction.period1 ).equals( Period_Type.QUARTER ) )
			{
				baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
				baseTimePeriod.setNumberOfUnit( 0 );				
			}

			if ( function.getArgumentValue( TimeFunction.period2 )
					.equals( Period_Type.YEAR ) )
			{
				relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
				relativeTimePeriod.setNumberOfUnit( Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_2 )
						.toString( ) ) );
			}
			else
			if( function.getArgumentValue( TimeFunction.period2 ).equals( Period_Type.MONTH ) )
			{
				relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
				relativeTimePeriod.setNumberOfUnit( Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_2 )
						.toString( ) ) );
			}
			else
			if( function.getArgumentValue( TimeFunction.period2 ).equals( Period_Type.QUARTER ) )
			{
				relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
				relativeTimePeriod.setNumberOfUnit( Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_2 )
						.toString( ) ) );				
			}
		}
		else
		if( function.getName( ).equals( TimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.getName( ) ) )
		{
			if ( function.getArgumentValue( TimeFunction.period1 )
					.equals( Period_Type.YEAR ) )
			{
				baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
				baseTimePeriod.setNumberOfUnit( 0 - Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_1 )
						.toString( ) ) );
			}
			else if ( function.getArgumentValue( TimeFunction.period1 )
					.equals( Period_Type.MONTH ) )
			{
				baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
				baseTimePeriod.setNumberOfUnit( 0 - Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_1 )
						.toString( ) ) );
			}
			else if( function.getArgumentValue( TimeFunction.period1 ).equals( Period_Type.QUARTER ) )
			{
				baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
				baseTimePeriod.setNumberOfUnit( 0 - Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_1 )
						.toString( ) ) );				
			}
			else
			if( function.getArgumentValue( TimeFunction.period1 ).equals( Period_Type.DAY ) )
			{
				baseTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_DAY );
				baseTimePeriod.setNumberOfUnit( 0 - Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_1 )
						.toString( ) ) );				
			}

			if ( function.getArgumentValue( TimeFunction.period2 ).equals( Period_Type.YEAR ) )
			{
				relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
				relativeTimePeriod.setNumberOfUnit( Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_2 )
						.toString( ) )  );
			}
			else
			if( function.getArgumentValue( TimeFunction.period2 ).equals( Period_Type.MONTH ) )
			{
				relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_MONTH );
				relativeTimePeriod.setNumberOfUnit( Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_2 )
						.toString( ) ) );				
			}
			else
			if( function.getArgumentValue( TimeFunction.period2 ).equals( Period_Type.QUARTER ) )
			{
				relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_QUARTER );
				relativeTimePeriod.setNumberOfUnit( Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_2 )
						.toString( ) ) );				
			}
		}
		else
		if( function.getName( ).equals( TimeFunction.NEXT_N_PERIODS.getName( ) ) )
		{
			if ( function.getArgumentValue( TimeFunction.period1 )
					.equals( Period_Type.YEAR ) )
			{
				relativeTimePeriod.setTimePeriodType( DesignChoiceConstants.INTERVAL_YEAR );
				relativeTimePeriod.setNumberOfUnit( Integer.valueOf( function.getArgumentValue( TimeFunction.N_value_1 )
						.toString( ) ) );
			}
		}
		handle.setBaseTimePeriod( baseTimePeriod );
		handle.setOffset( relativeTimePeriod );
	}
	
	/**
	 * Get time function from computed handle
	 * @param handle
	 * @return
	 */
	public static TimeFunction getCalculationType( ComputedColumnHandle handle )
	{
		TimePeriodHandle baseTimePeriod = handle.getBaseTimePeriod( );
		TimePeriodHandle relativeTimePeriod = handle.getOffset( );
		String referenceDate = handle.getReferenceDate( ).getStringExpression( );
		if( relativeTimePeriod == null && baseTimePeriod!= null )
		{
			String type = baseTimePeriod.getTimePeriodType( );
			int unit = baseTimePeriod.getNumberOfUnit( );
			if( DesignChoiceConstants.INTERVAL_YEAR.equals( type ) )
			{
				if( unit ==0 )
				{
					if ( TODAY_EXPR.equals( referenceDate ) )
						return TimeFunction.CURRENT_YEAR;
					else
						return TimeFunction.YEAR_TO_DATE;
				}
				TimeFunction function = new TimeFunction( TimeFunction.NEXT_N_PERIODS );
				function.setArgumentValue( TimeFunction.period1, Period_Type.YEAR );
				function.setArgumentValue( TimeFunction.N_value_1, unit );
				return function;
			}
			else if ( DesignChoiceConstants.INTERVAL_MONTH.equals( type ) )
			{
				if ( unit == 0 )
				{
					if ( TODAY_EXPR.equals( referenceDate ) )
						return TimeFunction.CURRENT_MONTH;
					else
						return TimeFunction.MONTH_TO_DATE;
				}
				if ( unit > 0 )
				{
					TimeFunction function = new TimeFunction( TimeFunction.NEXT_N_PERIODS );
					function.setArgumentValue( TimeFunction.period1,
							Period_Type.MONTH );
					function.setArgumentValue( TimeFunction.N_value_1, unit );
					return function;
				}
				else
				{
					if ( unit == -12 )
						return TimeFunction.TRAILING_12_MONTHS;
				}
			}
			else if ( DesignChoiceConstants.INTERVAL_QUARTER.equals( type ) )
			{
				if ( unit == 0 )
				{
					if ( TODAY_EXPR.equals( referenceDate ) )
						return TimeFunction.CURRENT_QUARTER;
					else
						return TimeFunction.QUARTER_TO_DATE;
				}
				TimeFunction function = new TimeFunction( TimeFunction.NEXT_N_PERIODS );
				function.setArgumentValue( TimeFunction.period1,
						Period_Type.QUARTER );
				function.setArgumentValue( TimeFunction.N_value_1, unit );
				return function;
			}
			else if ( DesignChoiceConstants.INTERVAL_WEEK.equals( type ) )
			{
				return TimeFunction.WEEK_TO_DATE;
			}
			else if ( DesignChoiceConstants.INTERVAL_DAY.equals( type ) )
			{
				if ( unit == -30 )
				{
					return TimeFunction.TRAILING_30_DAYS;
				}
				if ( unit == -60 )
				{
					return TimeFunction.TRAILING_60_DAYS;
				}
				if ( unit == -90 )
				{
					return TimeFunction.TRAILING_90_DAYS;
				}
			}			
		}
		else if ( relativeTimePeriod != null && baseTimePeriod == null )
		{
			String type = relativeTimePeriod.getTimePeriodType( );
			int unit = relativeTimePeriod.getNumberOfUnit( );
			if( DesignChoiceConstants.INTERVAL_YEAR.equals( type ) && unit ==1 )
			{
				return TimeFunction.PREVIOUS_YEAR;
			}
			if( DesignChoiceConstants.INTERVAL_QUARTER.equals( type ) && unit ==1 )
			{
				return TimeFunction.PREVIOUS_QUARTER;
			}
			if( DesignChoiceConstants.INTERVAL_MONTH.equals( type ) && unit ==1 )
			{
				return TimeFunction.PREVIOUS_MONTH;
			}
		}
		else if ( relativeTimePeriod != null && baseTimePeriod != null )
		{
			String baseType = baseTimePeriod.getTimePeriodType( );
			String relativeType = relativeTimePeriod.getTimePeriodType( );
			int baseUnit = baseTimePeriod.getNumberOfUnit( );
			int relativeUnit = relativeTimePeriod.getNumberOfUnit( );
			if ( DesignChoiceConstants.INTERVAL_MONTH.equals( baseType )
					&& DesignChoiceConstants.INTERVAL_MONTH.equals( relativeType )
					&& baseUnit == 0 && relativeUnit == 1 )
			{
				return TimeFunction.PREVIOUS_MONTH_TO_DATE;
			}
			if ( DesignChoiceConstants.INTERVAL_QUARTER.equals( baseType )
					&& DesignChoiceConstants.INTERVAL_QUARTER.equals( relativeType )
					&& baseUnit == 0 && relativeUnit == 1 )
			{
				return TimeFunction.PREVIOUS_QUARTER_TO_DATE;
			}
			if ( DesignChoiceConstants.INTERVAL_YEAR.equals( baseType )
					&& DesignChoiceConstants.INTERVAL_YEAR.equals( relativeType )
					&& baseUnit == 0 && relativeUnit == 1 )
			{
				return TimeFunction.PREVIOUS_YEAR_TO_DATE;
			}
			if ( DesignChoiceConstants.INTERVAL_MONTH.equals( baseType )
					&& DesignChoiceConstants.INTERVAL_YEAR.equals( relativeType )
					&& baseUnit == 0 && relativeUnit == 1 )
			{
				return TimeFunction.MONTH_TO_DATE_LAST_YEAR;
			}
			if ( DesignChoiceConstants.INTERVAL_QUARTER.equals( baseType )
					&& DesignChoiceConstants.INTERVAL_YEAR.equals( relativeType )
					&& baseUnit == 1 && baseUnit == 1 )
			{
				return TimeFunction.QUARTER_TO_DATE_LAST_YEAR;
			}
			
			if( baseUnit ==0 )
			{
				if( TODAY_EXPR.equals( referenceDate ) )
				{
					TimeFunction func = new TimeFunction( TimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO );
					func.setArgumentValue( TimeFunction.period1, convertToPeriodType( baseType ) );
					func.setArgumentValue( TimeFunction.N_value_2, relativeUnit );
					func.setArgumentValue( TimeFunction.period2, convertToPeriodType( baseType) );
					return func;
				}
				else
				{
					TimeFunction func = new TimeFunction( TimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO );
					func.setArgumentValue( TimeFunction.period1, convertToPeriodType( baseType ) );
					func.setArgumentValue( TimeFunction.N_value_2, relativeUnit );
					func.setArgumentValue( TimeFunction.period2, convertToPeriodType( baseType) );
					return func;
				}
			}
			else
			{
				TimeFunction func = new TimeFunction( TimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO );
				func.setArgumentValue( TimeFunction.period1, convertToPeriodType( baseType ) );
				func.setArgumentValue( TimeFunction.N_value_1, baseUnit );
				func.setArgumentValue( TimeFunction.period2, convertToPeriodType( baseType) );
				func.setArgumentValue( TimeFunction.N_value_2, relativeUnit );
				return func;
			}
		}
		return null;
	}
	
	private static Period_Type convertToPeriodType( String modelTimeType )
	{
		if(  DesignChoiceConstants.INTERVAL_YEAR.equals( modelTimeType ) )
		{
			return Period_Type.YEAR;
		}
		else
		if( DesignChoiceConstants.INTERVAL_MONTH.equals( modelTimeType ) )
		{
			return Period_Type.MONTH;
		}
		else
		if( DesignChoiceConstants.INTERVAL_QUARTER.equals( modelTimeType ) )
		{
			return Period_Type.QUARTER;
		}
		else
		if( DesignChoiceConstants.INTERVAL_WEEK.equals( modelTimeType ) )
		{
			return Period_Type.WEEK;
		}
		else
		if( DesignChoiceConstants.INTERVAL_DAY.equals( modelTimeType ) )
		{
			return Period_Type.DAY;
		}
		return null;
	}
	
	/** 
	 * Get reference date from computed handle
	 * @param handle
	 * @return if the value is null, it means the latest period. If the value is TimeFunction.TODAY_Expr
	 * it indicates today. or it is a user's input static reference date.
	 */
	public static String getReferenceDate( ComputedColumnHandle handle )  
	{
		String referenceDate = handle.getReferenceDate( ).getStringExpression( );
		if( referenceDate.equals( TODAY_EXPR ) )
		{
			return TimeFunctionUtil.TODAY_EXPR;
		}
		
		return referenceDate;
	}
}
