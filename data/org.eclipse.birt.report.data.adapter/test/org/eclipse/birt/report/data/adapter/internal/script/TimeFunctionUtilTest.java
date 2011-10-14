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
package org.eclipse.birt.report.data.adapter.internal.script;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.TimeFunction;
import org.eclipse.birt.report.data.adapter.api.TimeFunctionUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

public class TimeFunctionUtilTest extends TestCase
{
	private CubeHandle cube1;//with year, quarter, month
	
	public void testCalculationTypeInCube1() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "year" );
		List<TimeFunction> function1 = TimeFunctionUtil.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, true );
		List<TimeFunction> function2 = TimeFunctionUtil.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, false );
	}
	
	
	public void testSaveCalculationType1() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "year" );
		List<TimeFunction> function1 = TimeFunctionUtil.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, false );
		assert( function1.get( 4 ).getName( ).equals( "Current Period from N Periods Ago" ));
		function1.get( 4 ).setArgumentValue( TimeFunction.period1, TimeFunction.Period_Type.YEAR );
		function1.get( 4 ).setArgumentValue( TimeFunction.period2, TimeFunction.Period_Type.YEAR );
		function1.get( 4 ).setArgumentValue( TimeFunction.N_value_2, 3 );
		ComputedColumnHandle handle = ModelUtil.createComputedColumnHandle( );
		TimeFunctionUtil.populateComputedColumn( handle,
				function1.get( 4 ),
				TimeFunctionUtil.TODAY_EXPR );
		assert ( handle.getBaseTimePeriod( ).getTimePeriodType( ).equals( TimeFunction.Period_Type.YEAR ) );
		assert ( handle.getOffset( ).getTimePeriodType( ).equals( TimeFunction.Period_Type.YEAR ) );
		assert ( handle.getOffset( ).getNumberOfUnit( )==3 );
		
		assert ( TimeFunctionUtil.getReferenceDate( handle ).equals( TimeFunctionUtil.TODAY_EXPR ) );
		TimeFunction function = TimeFunctionUtil.getCalculationType( handle );
		assert ( function.getName( ).equals( "Current Period from N Periods Ago" ) );
		assert ( function.getArgumentValue( TimeFunction.period1 ).equals( TimeFunction.Period_Type.YEAR ) );
		assert ( function.getArgumentValue( TimeFunction.period2 ).equals( TimeFunction.Period_Type.YEAR ) );
		assert ( function.getArgumentValue( TimeFunction.N_value_2 ).equals( 3 ) );
	}
	
	public void testSaveCalculationType2() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "year" );
		levelsInxTab.add( "month" );
		List<TimeFunction> function1 = TimeFunctionUtil.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, false );
		assert( function1.get( 16 ).getName( ).equals( "Next N Periods" ));
		function1.get( 16 ).setArgumentValue( TimeFunction.period1, TimeFunction.Period_Type.YEAR );
		function1.get( 16 ).setArgumentValue( TimeFunction.N_value_1, 3 );
		ComputedColumnHandle handle = ModelUtil.createComputedColumnHandle( );
		TimeFunctionUtil.populateComputedColumn( handle,
				function1.get( 16 ) , null );
		assert ( handle.getBaseTimePeriod( ).getTimePeriodType( ).equals( TimeFunction.Period_Type.YEAR ) );
		assert ( handle.getBaseTimePeriod( ).getNumberOfUnit( )==3 );
		
		assert ( TimeFunctionUtil.getReferenceDate( handle )== null );
		TimeFunction function = TimeFunctionUtil.getCalculationType( handle );
		assert ( function.getName( ).equals( "Next N Periods" ) );
		assert ( function.getArgumentValue( TimeFunction.period1 ).equals( TimeFunction.Period_Type.YEAR ) );
		assert ( function.getArgumentValue( TimeFunction.N_value_1 ).equals( 3 ) );
	}
}
