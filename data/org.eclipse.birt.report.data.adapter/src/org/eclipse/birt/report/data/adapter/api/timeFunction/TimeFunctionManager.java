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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;

public class TimeFunctionManager 
{
	
	/**
	 * get a list of TimeFunction instances for the specified type
	 * 
	 * @param dim
	 * @param timeLevelsInXtab
	 * @param isStaticReferenceDate
	 * @return
	 */
	public static List<ITimeFunction> getCalculationTypes( DimensionHandle dim,
			List<String> timeLevelsInXtab, boolean isStaticReferenceDate )
	{
		List<ITimeFunction> availableFunctions = new ArrayList<ITimeFunction>( );

		List<String> timeType = new ArrayList<String>( );
		if ( dim != null && dim.isTimeType( ) )
		{
			String mostDetailedLevel = null, startingLevels = null;
			if ( !timeLevelsInXtab.isEmpty( ) && !isStaticReferenceDate )
			{
				startingLevels = timeLevelsInXtab.get( 0 ).toString( );
				mostDetailedLevel = timeLevelsInXtab.get( timeLevelsInXtab.size( ) - 1 ).toString( );
			}
			TabularHierarchyHandle hierhandle = (TabularHierarchyHandle) dim.getDefaultHierarchy( );
			List levels = hierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );
			for ( int i = 0; i < levels.size( ); i++ )
			{
				TabularLevelHandle level = (TabularLevelHandle) levels.get( i );
				if ( startingLevels != null
						&& level.getName( )
								.equals( startingLevels ) )
				{
					if( !level.getDateTimeLevelType( ).equalsIgnoreCase( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) )
						return availableFunctions;
				}

				
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
			//no year level in time dimension
			if( !timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) )
			{
				return availableFunctions;
			}
		}
		else
		{
			return availableFunctions;
		}

		List<IArgumentInfo.Period_Type> periodType = new ArrayList<IArgumentInfo.Period_Type>( );
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) )
		{
			availableFunctions.add( IBuildInBaseTimeFunction.CURRENT_YEAR_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.PREVIOUS_YEAR_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.YEAR_TO_DATE_FUNCTION );
			
			periodType.add( IArgumentInfo.Period_Type.YEAR );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) )
		{
			availableFunctions.add( IBuildInBaseTimeFunction.CURRENT_QUARTER_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.QUARTER_TO_DATE_FUNCTION );

			periodType.add( IArgumentInfo.Period_Type.QUARTER );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) )
		{
			availableFunctions.add( IBuildInBaseTimeFunction.CURRENT_MONTH_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.PREVIOUS_MONTH_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.MONTH_TO_DATE_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.TRAILING_12_MONTHS_FUNCTION );

			periodType.add( IArgumentInfo.Period_Type.MONTH );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) )
		{
			availableFunctions.add( IBuildInBaseTimeFunction.TRAILING_30_DAYS_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.TRAILING_60_DAYS_FUNCTION );
			availableFunctions.add( IBuildInBaseTimeFunction.TRAILING_90_DAYS_FUNCTION );
			
			periodType.add( IArgumentInfo.Period_Type.DAY );			
		}
		//for WTD, only support static reference date
		if ( isStaticReferenceDate
				&& timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR ) )
		{
			availableFunctions.add( IBuildInBaseTimeFunction.WEEK_TO_DATE_FUNCTION );
			periodType.add( IArgumentInfo.Period_Type.WEEK );
		}

		availableFunctions.add( new BaseTimeFunction( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO_FUNCTION,
				periodType ) );
		availableFunctions.add( new BaseTimeFunction( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO_FUNCTION,
				periodType ) );
		availableFunctions.add( new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO_FUNCTION,
				periodType ) );
		availableFunctions.add( new BaseTimeFunction( IBuildInBaseTimeFunction.NEXT_N_PERIODS_FUNCTION,
				periodType ) );

		return availableFunctions;
	}
	
	/**
	 * get the time function for specified name
	 * @param name
	 * @return
	 */
	public static ITimeFunction getCalculationType( String name )
	{
		if( IBuildInBaseTimeFunction.CURRENT_QUARTER.equals( name ) )
		{
			return IBuildInBaseTimeFunction.CURRENT_QUARTER_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.CURRENT_MONTH.equals( name ) )
		{
			return IBuildInBaseTimeFunction.CURRENT_MONTH_FUNCTION;			
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_MONTH.equals( name ))
		{
			return IBuildInBaseTimeFunction.PREVIOUS_MONTH_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_QUARTER.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PREVIOUS_QUARTER_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_YEAR.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PREVIOUS_YEAR_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_YEAR.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PREVIOUS_YEAR_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.TRAILING_30_DAYS.equals( name ) )
		{
			return IBuildInBaseTimeFunction.TRAILING_30_DAYS_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.TRAILING_60_DAYS.equals( name ) )
		{
			return IBuildInBaseTimeFunction.TRAILING_60_DAYS_FUNCTION;
		}		
		else
		if( IBuildInBaseTimeFunction.TRAILING_90_DAYS.equals( name ) )
		{
			return IBuildInBaseTimeFunction.TRAILING_90_DAYS_FUNCTION;
		}		
		else
		if( IBuildInBaseTimeFunction.TRAILING_12_MONTHS.equals( name ) )
		{
			return IBuildInBaseTimeFunction.TRAILING_12_MONTHS_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.YEAR_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.YEAR_TO_DATE_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.QUARTER_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.QUARTER_TO_DATE_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.MONTH_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.MONTH_TO_DATE_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR.equals( name ) )
		{
			return IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR.equals( name ) )
		{
			return IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR_FUNCTION;
		}		
		else
		if( IBuildInBaseTimeFunction.CURRENT_YEAR.equals( name ) )
		{
			return IBuildInBaseTimeFunction.CURRENT_YEAR_FUNCTION;
		}			
		else
		if( IBuildInBaseTimeFunction.WEEK_TO_DATE.equals( name ) )
		{
			return IBuildInBaseTimeFunction.WEEK_TO_DATE_FUNCTION;
		}	
		else
		if( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.equals( name ) )
		{
			return IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO_FUNCTION;
		}	
		else
		if( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.equals( name ) )
		{
			return IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.equals( name ) )
		{
			return IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO_FUNCTION;
		}
		else
		if( IBuildInBaseTimeFunction.NEXT_N_PERIODS.equals( name ) )
		{
			return IBuildInBaseTimeFunction.NEXT_N_PERIODS_FUNCTION;
		}
		
		return null;
	}
}
