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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.timefunction.IParallelPeriod;
import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.ITimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimeFunctionCreatorEngine;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.CalculationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

public class TimeFunctionManager
{

	/**
	 * get a list of TimeFunction instances for the specified type under
	 * specified local
	 * 
	 * @param dim
	 * @param timeLevelsInXtab
	 * @param isStaticReferenceDate
	 * @return
	 */
	public static List<ITimeFunction> getCalculationTypes( DimensionHandle dim,
			List<String> timeLevelsInXtab, boolean isStaticReferenceDate,
			ULocale locale )
	{
		List<ITimeFunction> availableFunctions = new ArrayList<ITimeFunction>( );

		List<String> timeType = new ArrayList<String>( );
		if ( dim != null && dim.isTimeType( ) )
		{
			String startingLevels = null;
			if ( !timeLevelsInXtab.isEmpty( ) && !isStaticReferenceDate )
			{
				startingLevels = timeLevelsInXtab.get( 0 ).toString( );
			}
			TabularHierarchyHandle hierhandle = (TabularHierarchyHandle) dim.getDefaultHierarchy( );
			List levels = hierhandle.getContents( TabularHierarchyHandle.LEVELS_PROP );
			for ( int i = 0, j = 0; i < levels.size( ); i++ )
			{
				TabularLevelHandle level = (TabularLevelHandle) levels.get( i );
				if ( startingLevels != null
						&& level.getName( ).equals( startingLevels ) )
				{
					if ( !level.getDateTimeLevelType( )
							.equalsIgnoreCase( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) )
						return availableFunctions;
				}
				if (timeLevelsInXtab.isEmpty() || isStaticReferenceDate)
				{
					timeType.add( level.getDateTimeLevelType( ) );
				}
				else
				{
					if (level.getName().equals(timeLevelsInXtab.get(j)))
					{
						timeType.add( level.getDateTimeLevelType( ) );
						if ( ++j >= timeLevelsInXtab.size() )
						{
							break;
						}
					}
				}
			}
			// no year level in time dimension
			if ( !timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) )
			{
				return availableFunctions;
			}
		}
		else
		{
			return availableFunctions;
		}

		List<IArgumentInfo.Period_Type> periodType = new ArrayList<IArgumentInfo.Period_Type>( );

		TimeFunctionHandle handle = TimeFunctionHandle.getInstance( locale );
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) )
		{
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.CURRENT_YEAR ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_YEAR ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.YEAR_TO_DATE ) );

			periodType.add( IArgumentInfo.Period_Type.YEAR );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) 
				|| timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR )
				|| timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR))
		{
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.CURRENT_QUARTER ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_QUARTER ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.QUARTER_TO_DATE ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR ) );

			periodType.add( IArgumentInfo.Period_Type.QUARTER );
		}
		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) 
				|| timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR )
				|| timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR) )
		{
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.CURRENT_MONTH ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_MONTH ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.MONTH_TO_DATE ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.TRAILING_12_MONTHS ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR ) );

			periodType.add( IArgumentInfo.Period_Type.MONTH );
		}
		
		// for WTD, only support static reference date
		if ( timeLevelsInXtab.isEmpty( )
				&& ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR ) ) )
		{
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.WEEK_TO_DATE ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR ) );
			periodType.add( IArgumentInfo.Period_Type.WEEK );
		}

		if ( timeType.contains( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) )
		{
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.TRAILING_30_DAYS ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.TRAILING_60_DAYS ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.TRAILING_90_DAYS ) );
			availableFunctions.add( handle.getFunction( IBuildInBaseTimeFunction.TRAILING_120_DAYS ) );
			periodType.add( IArgumentInfo.Period_Type.DAY );
		}
		availableFunctions.add( new BaseTimeFunction( handle.getFunction( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO ),
				periodType ) );
		availableFunctions.add( new BaseTimeFunction( handle.getFunction( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO ),
				periodType ) );
		availableFunctions.add( new BaseTimeFunction( handle.getFunction( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ),
				periodType ) );
		availableFunctions.add( new BaseTimeFunction( handle.getFunction( IBuildInBaseTimeFunction.NEXT_N_PERIODS ),
				periodType ) );

		return availableFunctions;
	}

	
	/**
	 * get a list of TimeFunction instances for the specified type under default
	 * local
	 * 
	 * @param dim
	 * @param timeLevelsInXtab
	 * @param isStaticReferenceDate
	 * @return
	 */
	public static List<ITimeFunction> getCalculationTypes( DimensionHandle dim,
			List<String> timeLevelsInXtab, boolean isStaticReferenceDate )
	{
		return getCalculationTypes( dim,
				timeLevelsInXtab,
				isStaticReferenceDate,
				ULocale.getDefault( ) );
	}

	/**
	 * get the time function for specified name
	 * 
	 * @param name
	 * @return
	 */
	public static ITimeFunction getCalculationType( String name, ULocale locale )
	{
		TimeFunctionHandle handle = TimeFunctionHandle.getInstance( locale );
		if ( IBuildInBaseTimeFunction.CURRENT_QUARTER.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.CURRENT_QUARTER );
		}
		else if ( IBuildInBaseTimeFunction.CURRENT_MONTH.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.CURRENT_MONTH );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_MONTH.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_MONTH );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_QUARTER.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_QUARTER );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_YEAR.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_YEAR );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_YEAR.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_YEAR );
		}
		else if ( IBuildInBaseTimeFunction.TRAILING_30_DAYS.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.TRAILING_30_DAYS );
		}
		else if ( IBuildInBaseTimeFunction.TRAILING_60_DAYS.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.TRAILING_60_DAYS );
		}
		else if ( IBuildInBaseTimeFunction.TRAILING_90_DAYS.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.TRAILING_90_DAYS );
		}
		else if ( IBuildInBaseTimeFunction.TRAILING_120_DAYS.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.TRAILING_120_DAYS );
		}
		else if ( IBuildInBaseTimeFunction.TRAILING_12_MONTHS.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.TRAILING_12_MONTHS );
		}
		else if ( IBuildInBaseTimeFunction.YEAR_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.YEAR_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.QUARTER_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.QUARTER_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.MONTH_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.MONTH_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR );
		}
		else if ( IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR );
		}
		else if ( IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR );
		}
		else if ( IBuildInBaseTimeFunction.CURRENT_YEAR.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.CURRENT_YEAR );
		}
		else if ( IBuildInBaseTimeFunction.WEEK_TO_DATE.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.WEEK_TO_DATE );
		}
		else if ( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO );
		}
		else if ( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO );
		}
		else if ( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO );
		}
		else if ( IBuildInBaseTimeFunction.NEXT_N_PERIODS.equals( name ) )
		{
			return handle.getFunction( IBuildInBaseTimeFunction.NEXT_N_PERIODS );
		}

		return null;
	}

	/**
	 * get the time function for specified name
	 * 
	 * @param name
	 * @return
	 */
	public static ITimeFunction getCalculationType( String name )
	{
		return getCalculationType( name, ULocale.getDefault( ) );
	}
	
	/**
	 * get the time type used in time function binding
	 * @param handle
	 * @return
	 */
	public static String[] getTimeType( ComputedColumnHandle handle )
	{
		if( handle == null )
			return null;
		String calculationType = handle.getCalculationType( );
		if( calculationType == null )
			return null;
		
		if ( IBuildInBaseTimeFunction.CURRENT_YEAR.equals( calculationType )
				|| IBuildInBaseTimeFunction.PREVIOUS_YEAR.equals( calculationType )
				|| IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE.equals( calculationType )
				|| IBuildInBaseTimeFunction.YEAR_TO_DATE.equals( calculationType ) )
		{
			return new String[]{
				DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR
			};
		}
		if ( IBuildInBaseTimeFunction.CURRENT_QUARTER.equals( calculationType )
				|| IBuildInBaseTimeFunction.PREVIOUS_QUARTER.equals( calculationType )
				|| IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE.equals( calculationType )
				|| IBuildInBaseTimeFunction.QUARTER_TO_DATE.equals( calculationType )
				|| IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR.equals( calculationType ) )
		{
			return new String[]{
				DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER
			};
		}
		if ( IBuildInBaseTimeFunction.CURRENT_MONTH.equals( calculationType )
				|| IBuildInBaseTimeFunction.PREVIOUS_MONTH.equals( calculationType )
				|| IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE.equals( calculationType )
				|| IBuildInBaseTimeFunction.MONTH_TO_DATE.equals( calculationType )
				|| IBuildInBaseTimeFunction.TRAILING_12_MONTHS.equals( calculationType ) 
				|| IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR.equals( calculationType ) )
		{
			return new String[]{
				DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH
			};
		}
		if ( IBuildInBaseTimeFunction.TRAILING_30_DAYS.equals( calculationType )
				|| IBuildInBaseTimeFunction.TRAILING_60_DAYS.equals( calculationType )
				|| IBuildInBaseTimeFunction.TRAILING_90_DAYS.equals( calculationType ) 
				|| IBuildInBaseTimeFunction.TRAILING_120_DAYS.equals( calculationType ) )
		{
			return new String[]{
				DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR
			};
		}
		if ( IBuildInBaseTimeFunction.WEEK_TO_DATE.equals( calculationType )
				|| IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE.equals( calculationType )
				|| IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR.equals( calculationType ) )
		{
			return new String[]{
				DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR
			};
		}
		if( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.equals( calculationType ) )
		{
			Iterator iter = handle.calculationArgumentsIterator( );
			String period1 = null, period2 = null;

			while ( iter.hasNext( ) )
			{
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next( );
				if ( IArgumentInfo.PERIOD_1.equals( argument.getName( ) ) )
				{
					period1 = argument.getValue( ).getStringExpression( );
				}
				if ( IArgumentInfo.PERIOD_2.equals( argument.getName( ) ) )
				{
					period2 = argument.getValue( ).getStringExpression( );
				}
			}
			return new String[]{
					DataAdapterUtil.toModelTimeType( DataAdapterUtil.toTimePeriodType( period1 ) ),
					DataAdapterUtil.toModelTimeType( DataAdapterUtil.toTimePeriodType( period2 ) )
			};
		}
		
		if ( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.equals( calculationType )
				|| IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.equals( calculationType )
				|| IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.equals( calculationType ) )
		{
			Iterator iter = handle.calculationArgumentsIterator( );
			String period1 = null, period2 = null;

			while ( iter.hasNext( ) )
			{
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next( );
				if ( IArgumentInfo.PERIOD_1.equals( argument.getName( ) ) )
				{
					period1 = argument.getValue( ).getStringExpression( );
				}
				if ( IArgumentInfo.PERIOD_2.equals( argument.getName( ) ) )
				{
					period2 = argument.getValue( ).getStringExpression( );
				}
			}
			return new String[]{
					DataAdapterUtil.toModelTimeType( DataAdapterUtil.toTimePeriodType( period1 ) ),
					DataAdapterUtil.toModelTimeType( DataAdapterUtil.toTimePeriodType( period2 ) )
			};
		}

		if ( IBuildInBaseTimeFunction.NEXT_N_PERIODS.equals( calculationType ) )
		{
			Iterator iter = handle.calculationArgumentsIterator( );
			String period1 = null;

			while ( iter.hasNext( ) )
			{
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next( );
				if ( IArgumentInfo.PERIOD_1.equals( argument.getName( ) ) )
				{
					period1 = argument.getValue( ).getStringExpression( );
				}
			}
			return new String[]{
					DataAdapterUtil.toModelTimeType( DataAdapterUtil.toTimePeriodType( period1 ) ),
			};
		}
		return new String[0];
	}
	
	private static int[] getValueFromCal( Calendar cal, String[] levelTypes )
	{
		int[] tmp = new int[levelTypes.length];

		for ( int i = 0; i < levelTypes.length; i++ )
		{
			if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_YEAR ) )
			{
				tmp[i] = cal.get( Calendar.YEAR );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_QUARTER ) )
			{
				tmp[i] = cal.get( Calendar.MONTH ) / 3 + 1;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_MONTH ) )
			{
				tmp[i] = cal.get( Calendar.MONTH ) + 1;
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH ) )
			{
				tmp[i] = cal.get( Calendar.WEEK_OF_MONTH );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR ) )
			{
				tmp[i] = cal.get( Calendar.WEEK_OF_YEAR );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK ) )
			{
				tmp[i] = cal.get( Calendar.DAY_OF_WEEK );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH ) )
			{
				tmp[i] = cal.get( Calendar.DAY_OF_MONTH );
			}

			else if ( levelTypes[i].equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR ) )
			{
				tmp[i] = cal.get( Calendar.DAY_OF_YEAR );
			}
		}

		return tmp;
	}
	
	/**
	 * get the description for a specific time function
	 * 
	 * @param column
	 * @param locale
	 * @return
	 * @throws BirtException 
	 */
	public static String getTooltipForTimeFunction(	ComputedColumnHandle column, ULocale locale ) throws BirtException
	{
		String desc = null;
		DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
		IBinding functionBinding = session.getModelAdaptor( ).adaptBinding( column );
		session.shutdown( );
		ITimePeriod basePeriod = functionBinding.getTimeFunction( ).getBaseTimePeriod( );
		ITimePeriod relativePeriod = functionBinding.getTimeFunction( ).getRelativeTimePeriod( );
	
		Date date = functionBinding.getTimeFunction( ).getReferenceDate( ).getDate( );
		Calendar cal = Calendar.getInstance( locale );
		cal.setTime( date );
		int[] values = new int[3];
		String[] levelTypes = new String[3];
		levelTypes[0] = TimeMember.TIME_LEVEL_TYPE_YEAR;
		levelTypes[1] = TimeMember.TIME_LEVEL_TYPE_MONTH;
		levelTypes[2] = TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH;
		
		values = getValueFromCal( cal, levelTypes );
		TimeMember member = new TimeMember( values, levelTypes );
		IPeriodsFunction periodsFunction = null;
		String toDatelevelType = null;
		String paralevelType = null;
		
		toDatelevelType = toLevelType( basePeriod.getType( ) );
		boolean reverse = false;
		if( basePeriod.countOfUnit() == 0 )
		{
			periodsFunction = TimeFunctionCreatorEngine.newTimeFunctionCreator( ).createPeriodsToDateFunction( toDatelevelType, basePeriod.isCurrent() );
		}
		else
		{
			periodsFunction = TimeFunctionCreatorEngine.newTimeFunctionCreator( ).createTrailingFunction( 
							toDatelevelType,basePeriod.countOfUnit() );
			if ( basePeriod.countOfUnit( ) < 0 )
				reverse = true;
		}
		List<TimeMember> list = null;
		if ( relativePeriod != null )
		{
			paralevelType = toLevelType( relativePeriod.getType( ) );
			IParallelPeriod parallelPeriod = TimeFunctionCreatorEngine.newTimeFunctionCreator( ).createParallelPeriodFunction( paralevelType,
					relativePeriod.countOfUnit( ) );
			
			list = periodsFunction.getResult( parallelPeriod.getResult( member ));
		}
		else
		{
			list = periodsFunction.getResult( member );
		}
		if ( reverse )
		{
			desc = constructTimeFunctionToolTip( list.get( list.size( ) - 1 ),
					list.get( 0 ),
					getCalculationType( column.getCalculationType( ), locale ).getDisplayName( ) );
		}
		else
		{
			desc = constructTimeFunctionToolTip( list.get( 0 ),
					list.get( list.size( ) - 1 ),
					getCalculationType( column.getCalculationType( ), locale ).getDisplayName( ) );
		}
		
		
		return desc;
	}
	
	private static String constructTimeFunctionToolTip( TimeMember from,
			TimeMember to, String funcName )
	{
		StringBuffer result = new StringBuffer( "" );
		result.append( funcName ).append( " ( " );
		result.append( from.getMemberValue( )[0] )
				.append( "-" )
				.append( from.getMemberValue( )[1] )
				.append( "-" )
				.append( from.getMemberValue( )[2] );
		result.append( " " )
				.append( Message.getMessage( ResourceConstants.TIMEFUNCTION_TOOLTIP_TO ) )
				.append( " " );
		result.append( to.getMemberValue( )[0] )
				.append( "-" )
				.append( to.getMemberValue( )[1] )
				.append( "-" )
				.append( to.getMemberValue( )[2] );
		result.append( " )" );
		return result.toString( );
	}
	
	private static String toLevelType( TimePeriodType timePeriodType )
	{
		if( timePeriodType == TimePeriodType.YEAR )
		{
			return TimeMember.TIME_LEVEL_TYPE_YEAR;
		}
		else if( timePeriodType == TimePeriodType.QUARTER )
		{
			return TimeMember.TIME_LEVEL_TYPE_QUARTER;
		}
		else if( timePeriodType == TimePeriodType.MONTH )
		{
			return TimeMember.TIME_LEVEL_TYPE_MONTH;
		}
		else if( timePeriodType == TimePeriodType.WEEK )
		{
			return TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR;
		}
		else if( timePeriodType == TimePeriodType.DAY )
		{
			return TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH;
		}
		return null;
	}
}
