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
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IBuildInBaseTimeFunction;
import org.eclipse.birt.report.data.adapter.api.timeFunction.ITimeFunction;
import org.eclipse.birt.report.data.adapter.api.timeFunction.TimeFunctionManager;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

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
		assertTrue( function1.size( ) ==17 );
		
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
		assertTrue( function2.size( ) ==12 );
		
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
	
	public void testInvalidCalculationTypeInCube1() throws SemanticException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		
		List levelsInxTab = new ArrayList( );
		levelsInxTab.add( "month" );
		List<ITimeFunction> function2 = TimeFunctionManager.getCalculationTypes( cube1.getDimension( "TimeDimension" ),levelsInxTab, false );
		assertTrue( function2.size( )==0 );
	}
}
