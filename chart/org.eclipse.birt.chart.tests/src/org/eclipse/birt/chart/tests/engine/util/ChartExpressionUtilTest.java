/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.util;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.chart.util.ChartExpressionUtil;

public class ChartExpressionUtilTest extends TestCase
{

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 * Currently Empty.
	 */
	protected void setUp( ) throws Exception
	{

	}

	/**
	 * Collect and empty any objects that are used in multiple tests. Currently
	 * Empty.
	 */
	protected void tearDown( ) throws Exception
	{

	}

	public void testIsCubeBinding( )
	{
		// Test pure binding
		assertEquals( true,
				ChartExpressionUtil.isCubeBinding( "data[\"ab c\"]", false ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartExpressionUtil.isCubeBinding( "data[\"data\"]", false ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isCubeBinding( "data[\"ab c\"]+100", //$NON-NLS-1$
						false ) );
		assertEquals( false,
				ChartExpressionUtil.isCubeBinding( "data[\"year\"]+\"Q\"+data[\"quarter\"]", //$NON-NLS-1$
						false ) );

		// Test complex expression
		assertEquals( true,
				ChartExpressionUtil.isCubeBinding( "data[\"ab c\"]+100", true ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartExpressionUtil.isCubeBinding( "100+data[\"ab c\"]", true ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartExpressionUtil.isCubeBinding( "data[\"year\"]+\"Q\"+data[\"quarter\"]", //$NON-NLS-1$
						true ) );
	}

	public void testGetCubeBindingName( )
	{
		assertEquals( "ab c", ChartExpressionUtil.getCubeBindingName( "data[\"ab c\"]", //$NON-NLS-1$ //$NON-NLS-2$
						false ) );
		assertEquals( "data", ChartExpressionUtil.getCubeBindingName( "data[\"data\"]", //$NON-NLS-1$ //$NON-NLS-2$
						false ) );
		assertEquals( null,
				ChartExpressionUtil.getCubeBindingName( "data[\"data\"] + 100", false ) ); //$NON-NLS-1$
		assertEquals( null,
				ChartExpressionUtil.getCubeBindingName( "data[\"year\"]+\"Q\"+data[\"quarter\"]", false ) ); //$NON-NLS-1$

		assertEquals( "ab c", ChartExpressionUtil.getCubeBindingName( "data[\"ab c\"]", //$NON-NLS-1$ //$NON-NLS-2$
						true ) );
		assertEquals( "ab c", //$NON-NLS-1$
				ChartExpressionUtil.getCubeBindingName( "data[\"ab c\"] + 100", true ) ); //$NON-NLS-1$
		assertEquals( "ab c", //$NON-NLS-1$
				ChartExpressionUtil.getCubeBindingName( "100 * data[\"ab c\"] ", true ) ); //$NON-NLS-1$
		assertEquals( "123", //$NON-NLS-1$
				ChartExpressionUtil.getCubeBindingName( "data[\"123\"] + data[\"ab c\"] ", //$NON-NLS-1$
						true ) );

		// Test script expression
		assertEquals( "123", //$NON-NLS-1$
				ChartExpressionUtil.getCubeBindingName( "data[\"12\"+\"3\"] ", //$NON-NLS-1$
						true ) );
	}

	public void testGetCubeBindingNameList( )
	{
		List<String> names = ChartExpressionUtil.getCubeBindingNameList( "data[\"123\"] + data[\"ab c\"]" ); //$NON-NLS-1$
		assertEquals( 2, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$
		assertEquals( "ab c", names.get( 1 ) ); //$NON-NLS-1$

		names = ChartExpressionUtil.getCubeBindingNameList( "123" ); //$NON-NLS-1$
		assertEquals( 0, names.size( ) );

		names = ChartExpressionUtil.getCubeBindingNameList( "data[\"123\"]" ); //$NON-NLS-1$
		assertEquals( 1, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$

		names = ChartExpressionUtil.getCubeBindingNameList( "data[\"123\"] + 100" ); //$NON-NLS-1$
		assertEquals( 1, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$

		names = ChartExpressionUtil.getCubeBindingNameList( "data[\"123\"] + data[\"ab c\"] + data[\"a\"]" ); //$NON-NLS-1$
		assertEquals( 3, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$
		assertEquals( "ab c", names.get( 1 ) ); //$NON-NLS-1$
		assertEquals( "a", names.get( 2 ) ); //$NON-NLS-1$
	}

	public void testIsDimensionExpresion( )
	{
		assertEquals( true,
				ChartExpressionUtil.isDimensionExpresion( "dimension[\"abc\"][\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartExpressionUtil.isDimensionExpresion( "dimension[\"a\"+\"bc\"][\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isDimensionExpresion( "dimension[\"abc\"][\"12 3\"]+2" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isDimensionExpresion( "2+dimension[\"abc\"][\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isDimensionExpresion( "dimension[\"abc\"][12 3]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isDimensionExpresion( "dimension[\"abc\"]" ) ); //$NON-NLS-1$
	}

	public void testGetLevelNameFromDimensionExpression( )
	{
		String[] levels = ChartExpressionUtil.getLevelNameFromDimensionExpression( "dimension[\"abc\"][\"12 3\"]" );//$NON-NLS-1$
		assertEquals( "abc", levels[0] ); //$NON-NLS-1$
		assertEquals( "12 3", levels[1] ); //$NON-NLS-1$

		// dimension["a"+"bc"]["a"+2*3+"b"]
		levels = ChartExpressionUtil.getLevelNameFromDimensionExpression( "dimension[\"a\"+\"bc\"][\"a\"+2*3+\"b\"]" ); //$NON-NLS-1$
		assertEquals( "abc", levels[0] ); //$NON-NLS-1$
		assertEquals( "a6b", levels[1] ); //$NON-NLS-1$

		levels = ChartExpressionUtil.getLevelNameFromDimensionExpression( "1+dimension[\"abc\"][\"12 3\"]" );//$NON-NLS-1$
		assertNull( levels );
	}

	public void testIsMeasureExpresion( )
	{
		assertEquals( true,
				ChartExpressionUtil.isMeasureExpresion( "measure[\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartExpressionUtil.isMeasureExpresion( "measure[\"a\"+\"bc\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isMeasureExpresion( "measure[\"12 3\"]+1" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isMeasureExpresion( "1*measure[\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isMeasureExpresion( "measure[12 3]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.isMeasureExpresion( "dimension[\"abc\"]" ) ); //$NON-NLS-1$
	}

	public void testGetMeasureName( )
	{
		assertEquals( "12 3", ChartExpressionUtil.getMeasureName( "measure[\"12 3\"]" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "abc", ChartExpressionUtil.getMeasureName( "measure[\"a\"+\"bc\"]" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull( ChartExpressionUtil.getMeasureName( "measure[\"abc\"+5]" ) ); //$NON-NLS-1$
	}

	public void testCheckStringInExpression( )
	{
		assertEquals( true,
				ChartExpressionUtil.checkStringInExpression( "data[\"year\"]+\"Q\"+data[\"quarter\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartExpressionUtil.checkStringInExpression( "\"Q\"+data[\"quarter\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartExpressionUtil.checkStringInExpression( "data[\"quarter\"]+\"Q\"" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.checkStringInExpression( "data[\"year\"]+data[\"quarter\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartExpressionUtil.checkStringInExpression( "4+data[\"quarter\"]" ) ); //$NON-NLS-1$
	}

	public void testGetFullBindingName( )
	{
		// Cube expression
		assertEquals( "abc", //$NON-NLS-1$
				ChartExpressionUtil.getFullBindingName( "data[\"abc\"]" ) ); //$NON-NLS-1$
		assertEquals( "data%5B%22abc%22%5D+%2B+100", //$NON-NLS-1$
				ChartExpressionUtil.getFullBindingName( "data[\"abc\"] + 100" ) ); //$NON-NLS-1$

		// Row expression
		assertEquals( "abc", //$NON-NLS-1$
				ChartExpressionUtil.getFullBindingName( "row[\"abc\"]" ) ); //$NON-NLS-1$
		assertEquals( "row%5B%22abc%22%5D+%2B+100", //$NON-NLS-1$
				ChartExpressionUtil.getFullBindingName( "row[\"abc\"] + 100" ) ); //$NON-NLS-1$

		// Constant
		assertEquals( "\"abc\"", //$NON-NLS-1$
				ChartExpressionUtil.getFullBindingName( "\"abc\"" ) ); //$NON-NLS-1$
		assertEquals( "100", //$NON-NLS-1$
				ChartExpressionUtil.getFullBindingName( "100" ) ); //$NON-NLS-1$
	}

}
