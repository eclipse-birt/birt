/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.internal.script;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.timefunction.ITimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IBuildInBaseTimeFunction;
import org.eclipse.birt.report.data.adapter.api.timeFunction.ITimeFunction;
import org.eclipse.birt.report.data.adapter.api.timeFunction.TimeFunctionManager;
import org.eclipse.birt.report.data.adapter.impl.ModelAdapter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.CalculationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

import com.ibm.icu.util.ULocale;

public class TimeFunctionManagerTest extends TestCase
{
	private CubeHandle cube1;//with year, quarter, month
	
	public void testCalculationTypeInCube1() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "year" );
		List<ITimeFunction> function1 = TimeFunctionManager.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, true );
		for( int i=0; i< function1.size( ); i++ )
		{
			if( function1.get( i ).getName( ).equals( IBuildInBaseTimeFunction.PREVIOUS_MONTH ) )
			{
				assertTrue( function1.get( i ).getArguments( ).size( ) ==1 );
			}
			if( function1.get( i ).getName( ).equals( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ) )
			{
				assertTrue( function1.get( i ).getArguments( ).size( ) ==4 );
				assertTrue( function1.get( i ).getArguments( ).get( 0 ).getName( ).equals( IArgumentInfo.N_PERIOD1 ) );
				assertTrue( function1.get( i ).getArguments( ).get( 1 ).getName( ).equals( IArgumentInfo.PERIOD_1 ) );
				assertTrue( function1.get( i ).getArguments( ).get( 1 ).getPeriodChoices( ).size( ) == 3 );
				assertTrue( function1.get( i ).getArguments( ).get( 2 ).getName( ).equals( IArgumentInfo.N_PERIOD2 ) );
				assertTrue( function1.get( i ).getArguments( ).get( 3 ).getName( ).equals( IArgumentInfo.PERIOD_2 ) );
			}
		}
		List<ITimeFunction> function2 = TimeFunctionManager.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, false );
		
		for( int i=0; i< function2.size( ); i++ )
		{
			if( function2.get( i ).getName( ).equals( IBuildInBaseTimeFunction.PREVIOUS_YEAR ) )
			{
				assertTrue( function2.get( i ).getArguments( ).size( ) ==1 );
			}
			if( function2.get( i ).getName( ).equals( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ) )
			{
				assertTrue( function2.get( i ).getArguments( ).size( ) ==4 );
				assertTrue( function2.get( i ).getArguments( ).get( 0 ).getName( ).equals( IArgumentInfo.N_PERIOD1 ) );
				assertTrue( function2.get( i ).getArguments( ).get( 1 ).getName( ).equals( IArgumentInfo.PERIOD_1 ) );
				assertTrue( function2.get( i ).getArguments( ).get( 1 ).getPeriodChoices( ).size( ) == 1 );
				assertTrue( function2.get( i ).getArguments( ).get( 2 ).getName( ).equals( IArgumentInfo.N_PERIOD2 ) );
				assertTrue( function2.get( i ).getArguments( ).get( 3 ).getName( ).equals( IArgumentInfo.PERIOD_2 ) );
			}			
		}
	}
	
	public void testCalculationTypeInCube2() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "year" );
		levelsInxTab.add( "quarter" );
		List<ITimeFunction> function1 = TimeFunctionManager.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, true );
		assertTrue( function1.size( ) == 18 );
		
		for( int i=0; i< function1.size( ); i++ )
		{
			if( function1.get( i ).getName( ).equals( IBuildInBaseTimeFunction.PREVIOUS_MONTH ) )
			{
				assertTrue( function1.get( i ).getArguments( ).size( ) ==1 );
			}
			if( function1.get( i ).getName( ).equals( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ) )
			{
				assertTrue( function1.get( i ).getArguments( ).size( ) ==4 );
				assertTrue( function1.get( i ).getArguments( ).get( 0 ).getName( ).equals( IArgumentInfo.N_PERIOD1 ) );
				assertTrue( function1.get( i ).getArguments( ).get( 1 ).getName( ).equals( IArgumentInfo.PERIOD_1 ) );
				assertTrue( function1.get( i ).getArguments( ).get( 1 ).getPeriodChoices( ).size( ) == 3 );
				assertTrue( function1.get( i ).getArguments( ).get( 2 ).getName( ).equals( IArgumentInfo.N_PERIOD2 ) );
				assertTrue( function1.get( i ).getArguments( ).get( 3 ).getName( ).equals( IArgumentInfo.PERIOD_2 ) );
			}
		}
		List<ITimeFunction> function2 = TimeFunctionManager.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, false );
		assertTrue( function2.size( ) ==13 );
		
		for( int i=0; i< function2.size( ); i++ )
		{
			if( function2.get( i ).getName( ).equals( IBuildInBaseTimeFunction.PREVIOUS_YEAR ) )
			{
				assertTrue( function2.get( i ).getArguments( ).size( ) ==1 );
			}
			if( function2.get( i ).getName( ).equals( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ) )
			{
				assertTrue( function2.get( i ).getArguments( ).size( ) ==4 );
				assertTrue( function2.get( i ).getArguments( ).get( 0 ).getName( ).equals( IArgumentInfo.N_PERIOD1 ) );
				assertTrue( function2.get( i ).getArguments( ).get( 1 ).getName( ).equals( IArgumentInfo.PERIOD_1 ) );
				assertTrue( function2.get( i ).getArguments( ).get( 1 ).getPeriodChoices( ).size( ) == 2 );
				assertTrue( function2.get( i ).getArguments( ).get( 2 ).getName( ).equals( IArgumentInfo.N_PERIOD2 ) );
				assertTrue( function2.get( i ).getArguments( ).get( 3 ).getName( ).equals( IArgumentInfo.PERIOD_2 ) );
				assertTrue( function2.get( i ).getArguments( ).get( 1 ).getPeriodChoices( ).size( ) == 2 );
			}			
		}
	}
	
	public void testCalculationTypeInCube3() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube2( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "year" );
		levelsInxTab.add( "quarter" );
		List<ITimeFunction> function1 = TimeFunctionManager.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, true );
		assertTrue( function1.size( ) == 18 );
		
		ITimeFunction function = function1.get( 14 );
		assertTrue( function.getName( ).equals( "CURRENT PERIOD FROM N PERIODS AGO" ));
		
		List<IArgumentInfo> arguments = function.getArguments( );
		assert( arguments.size( ) ==3 );
		assertTrue( arguments.get( 0 ).getName( ).equals( IArgumentInfo.PERIOD_1 ) );
		assertTrue(arguments.get( 0 ).getPeriodChoices( ).size( ) == 3 );
		assertTrue( arguments.get( 1 ).getName( ).equals( IArgumentInfo.N_PERIOD2 ) );
		assertTrue( arguments.get( 2 ).getName( ).equals( IArgumentInfo.PERIOD_2 ) );
		assertTrue( arguments.get( 2 ).getPeriodChoices( ).size( ) == 3 );
	}
	
	public void testInvalidCalculationTypeInCube1() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "month" );
		List<ITimeFunction> function2 = TimeFunctionManager.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, false );
		assertTrue( function2.size( )==0 );
	}
	
	public void testAdapterTimeFunction() throws BirtException
	{
		ComputedColumnHandle computedHandle = ModelUtil.createComputedColumnHandle( );
		computedHandle.setAggregateFunction( "SUM" );
		computedHandle.setCalculationType( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO );
		computedHandle.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE );
		computedHandle.getReferenceDateValue( )
				.setExpression( new Expression( "\"2003-02-17\"", null ) );
		computedHandle.setProperty( ComputedColumn.TIME_DIMENSION_MEMBER,
				"dimension[\"time\"]" );
		CalculationArgument argument1 = new CalculationArgument();
		argument1.setName( IArgumentInfo.N_PERIOD1 );
		argument1.setValue( new Expression("10", null ) );
		
		CalculationArgument argument2 = new CalculationArgument();
		argument2.setName( IArgumentInfo.PERIOD_1 );
		argument2.setValue( new Expression( "MONTH", null ) );
		
		CalculationArgument argument3 = new CalculationArgument();
		argument3.setName( IArgumentInfo.N_PERIOD2 );
		argument3.setValue( new Expression("5", null ) );
		
		CalculationArgument argument4 = new CalculationArgument();
		argument4.setName( IArgumentInfo.PERIOD_2 );
		argument4.setValue( new Expression("YEAR", null ) );
		
		computedHandle.addCalculationArgument( argument1 );
		computedHandle.addCalculationArgument( argument2 );
		computedHandle.addCalculationArgument( argument3 );
		computedHandle.addCalculationArgument( argument4 );
		
		IBinding binding = new ModelAdapter( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) ).adaptBinding( computedHandle );
		ITimePeriod basePeriod = binding.getTimeFunction( ).getBaseTimePeriod( );
		ITimePeriod relativePeriod = binding.getTimeFunction( ).getRelativeTimePeriod( );
		
		assertTrue( basePeriod.getType( ).equals( TimePeriodType.MONTH ) );
		assertTrue( basePeriod.countOfUnit( ) == -10 );
		assertTrue( relativePeriod.getType( ).equals( TimePeriodType.YEAR ) );
		assertTrue( relativePeriod.countOfUnit( ) == -5 );		
	}
	
	public void testGetTimeType() throws SemanticException 
	{
		ComputedColumnHandle computedHandle = ModelUtil.createComputedColumnHandle( );
		computedHandle.setAggregateFunction( "SUM" );
	
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.CURRENT_QUARTER );
		String[] timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) );
		
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.CURRENT_MONTH );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.TRAILING_30_DAYS );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.TRAILING_60_DAYS );		
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.TRAILING_90_DAYS );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.TRAILING_120_DAYS );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) );
		
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.TRAILING_12_MONTHS );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.YEAR_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) );
		
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.QUARTER_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.MONTH_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.CURRENT_YEAR );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.WEEK_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PREVIOUS_MONTH );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PREVIOUS_QUARTER );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PREVIOUS_YEAR );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR ) );
		
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );
		
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR ) );
		
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );
		
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) );

		CalculationArgument period1 = new CalculationArgument( );
		CalculationArgument period2 = new CalculationArgument( );
		
		period1.setName( IArgumentInfo.PERIOD_1 );
		period2.setName( IArgumentInfo.PERIOD_2 );
		
		period1.setValue( new Expression( IArgumentInfo.Period_Type.Period_Type_ENUM.YEAR, ExpressionType.CONSTANT ) );
		period2.setValue( new Expression( IArgumentInfo.Period_Type.Period_Type_ENUM.DAY, ExpressionType.CONSTANT ) );

		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO );
		computedHandle.addCalculationArgument( period1 );
		computedHandle.addCalculationArgument( period2 );		
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR ) );
		assertTrue( timeTypes[1].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) );
		
		period1.setValue( new Expression( IArgumentInfo.Period_Type.Period_Type_ENUM.QUARTER, ExpressionType.CONSTANT ) );
		period2.setValue( new Expression( IArgumentInfo.Period_Type.Period_Type_ENUM.MONTH, ExpressionType.CONSTANT ) );
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO );
		computedHandle.addCalculationArgument( period1 );
		computedHandle.addCalculationArgument( period2 );		
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER ) );
		assertTrue( timeTypes[1].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );
				
		period1.setValue( new Expression( IArgumentInfo.Period_Type.Period_Type_ENUM.DAY, ExpressionType.CONSTANT ) );
		period2.setValue( new Expression( IArgumentInfo.Period_Type.Period_Type_ENUM.MONTH, ExpressionType.CONSTANT ) );
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO );
		computedHandle.addCalculationArgument( period1 );
		computedHandle.addCalculationArgument( period2 );		
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) );
		assertTrue( timeTypes[1].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH ) );

		period1.setValue( new Expression( IArgumentInfo.Period_Type.Period_Type_ENUM.DAY, ExpressionType.CONSTANT ) );
		computedHandle.setCalculationType(  IBuildInBaseTimeFunction.NEXT_N_PERIODS );
		timeTypes = TimeFunctionManager.getTimeType( computedHandle );
		assertTrue( timeTypes[0].equals( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR ) );
	}
	
	public void testGettingToolTipForTimeFunction1() throws BirtException
	{
		cube1 = ModelUtil.prepareCube2( );
		
		ComputedColumnHandle computedHandle = ModelUtil.createComputedColumnHandle( );
		computedHandle.setAggregateFunction( "SUM" );
		computedHandle.setCalculationType( IBuildInBaseTimeFunction.QUARTER_TO_DATE );
		computedHandle.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE );
		computedHandle.getReferenceDateValue( )
				.setExpression( new Expression( "\"2003-08-17\"", null ) );
		computedHandle.setProperty( ComputedColumn.TIME_DIMENSION_MEMBER,
				"dimension[\"TimeDimension\"]" );
		String desc = TimeFunctionManager.getTooltipForTimeFunction( computedHandle, ULocale.CHINA );
		assertTrue(desc.equals( "Quarter to Date  ( 2003-7-1 To 2003-8-17 )" ) );
	}
	
	public void testGettingToolTipForTimeFunction2() throws BirtException
	{
		cube1 = ModelUtil.prepareCube2( );
		
		ComputedColumnHandle computedHandle = ModelUtil.createComputedColumnHandle( );
		computedHandle.setAggregateFunction( "SUM" );
		computedHandle.setCalculationType( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE );
		
		computedHandle.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE );
		computedHandle.getReferenceDateValue( )
				.setExpression( new Expression( "\"2003-08-17\"", null ) );
		computedHandle.setProperty( ComputedColumn.TIME_DIMENSION_MEMBER,
				"dimension[\"TimeDimension\"]" );
		
		String desc = TimeFunctionManager.getTooltipForTimeFunction( computedHandle, ULocale.CHINA );
		assertTrue(desc.equals( "Previous Year to Date  ( 2002-1-1 To 2002-8-17 )" ) );
	}
	
	public void testGettingToolTipForTimeFunction3() throws BirtException
	{
		cube1 = ModelUtil.prepareCube2( );
		
		ComputedColumnHandle computedHandle = ModelUtil.createComputedColumnHandle( );
		computedHandle.setAggregateFunction( "SUM" );
		computedHandle.setCalculationType( IBuildInBaseTimeFunction.TRAILING_12_MONTHS );
		
		computedHandle.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE );
		computedHandle.getReferenceDateValue( )
				.setExpression( new Expression( "\"2003-08-17\"", null ) );
		computedHandle.setProperty( ComputedColumn.TIME_DIMENSION_MEMBER,
				"dimension[\"TimeDimension\"]" );
		
		String desc = TimeFunctionManager.getTooltipForTimeFunction( computedHandle, ULocale.CHINA );
		assertTrue(desc.equals( "Trailing 12 Months  ( 2002-8-18 To 2003-8-17 )" ) );
	}
	
	public void testGettingToolTipForTimeFunction4() throws BirtException
	{
		cube1 = ModelUtil.prepareCube2( );
		
		ComputedColumnHandle computedHandle = ModelUtil.createComputedColumnHandle( );
		computedHandle.setAggregateFunction( "SUM" );
		computedHandle.setCalculationType( IBuildInBaseTimeFunction.CURRENT_YEAR );
		computedHandle.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE );
		computedHandle.getReferenceDateValue( )
				.setExpression( new Expression( "\"2003-08-17\"", null ) );
		computedHandle.setProperty( ComputedColumn.TIME_DIMENSION_MEMBER,
				"dimension[\"TimeDimension\"]" );
		String desc = TimeFunctionManager.getTooltipForTimeFunction( computedHandle, ULocale.CHINA );
		assertTrue(desc.equals( "Current Year  ( 2003-1-1 To 2003-12-31 )" ) );
	}
}
