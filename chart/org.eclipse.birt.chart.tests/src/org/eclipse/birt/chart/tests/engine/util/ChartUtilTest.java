/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.util.ChartUtil;

public class ChartUtilTest extends TestCase
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

	/**
	 * Test whether the given color definition is transparent.
	 * 
	 */
	public void testIsColorTransparent( )
	{
		assertFalse( ChartUtil.isColorTransparent( ColorDefinitionImpl.BLUE( ) ) );
		assertTrue( ChartUtil.isColorTransparent( ColorDefinitionImpl.TRANSPARENT( ) ) );
	}

	/**
	 * Test whether the given label defines a shadow.
	 * 
	 */
	public void testIsShadowDefined( )
	{
		Label label = LabelImpl.create( );
		assertFalse( ChartUtil.isShadowDefined( label ) );
		label.setShadowColor( ColorDefinitionImpl.BLACK( ) );
		assertTrue( ChartUtil.isShadowDefined( label ) );
	}

	/**
	 * Test whether the given left double value is greater than the given right
	 * value within a small precision.
	 * 
	 */
	public void testMathGT( )
	{
		assertTrue( ChartUtil.mathGT( 1.0 + 1.0 * 1E-9, 1.0 ) );
		assertFalse( ChartUtil.mathGT( 1.0 + 1.0 * 1E-11, 1.0 ) );
	}

	/**
	 * Test whether the given left double value is less than the given right
	 * value within a small precision
	 */
	public void testMathLT( )
	{
		assertTrue( ChartUtil.mathLT( 1.0, 1.0 + 1.0 * 1E-9 ) );
		assertFalse( ChartUtil.mathLT( 1.0, 1.0 + 1.0 * 1E-11 ) );
	}

	/**
	 * Test whether the given two double values are equal within a small
	 * precision.
	 * 
	 */
	public void testMathEqual( )
	{
		assertFalse( ChartUtil.mathEqual( 1.0 + 1.0 * 1E-9, 1.0 ) );
		assertTrue( ChartUtil.mathEqual( 1.0 + 1.0 * 1E-11, 1.0 ) );
		assertTrue( ChartUtil.mathEqual( 1.0, 1.0 + 1.0 * 1E-11 ) );
	}

	/**
	 * Test the quadrant for given angle in degree.
	 */
	public void testGetQuadrant( )
	{
		assertEquals( -1, ChartUtil.getQuadrant( 0 ) );
		assertEquals( -2, ChartUtil.getQuadrant( 90 ) );
		assertEquals( -3, ChartUtil.getQuadrant( 180 ) );
		assertEquals( -4, ChartUtil.getQuadrant( 270 ) );
		assertEquals( 1, ChartUtil.getQuadrant( 45 ) );
		assertEquals( 2, ChartUtil.getQuadrant( -200 ) );
		assertEquals( 3, ChartUtil.getQuadrant( -100 ) );
		assertEquals( 4, ChartUtil.getQuadrant( -45 ) );
	}

	public void testIsBinding( )
	{
		assertEquals( true, ChartXTabUtil.isBinding( "data[\"ab c\"]", false ) ); //$NON-NLS-1$
		assertEquals( true, ChartXTabUtil.isBinding( "data[\"data\"]", false ) ); //$NON-NLS-1$
		assertEquals( false, ChartXTabUtil.isBinding( "data[\"ab c\"]+100", //$NON-NLS-1$
				false ) );
		assertEquals( true,
				ChartXTabUtil.isBinding( "data[\"ab c\"]+100", true ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartXTabUtil.isBinding( "100+data[\"ab c\"]", true ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartXTabUtil.isBinding( "data[\"ab c\"]+data[\"data\"]", true ) ); //$NON-NLS-1$
	}

	public void testGetBindingName( )
	{
		assertEquals( "ab c", ChartXTabUtil.getBindingName( "data[\"ab c\"]", //$NON-NLS-1$ //$NON-NLS-2$
				false ) );
		assertEquals( "data", ChartXTabUtil.getBindingName( "data[\"data\"]", //$NON-NLS-1$ //$NON-NLS-2$
				false ) );
		assertEquals( null,
				ChartXTabUtil.getBindingName( "data[\"data\"] + 100", false ) ); //$NON-NLS-1$
		// assertEquals( null, ChartXTabUtil.getBindingName( "data[\"data\"] +
		// data[\"ab c\"]",
		// false ) );

		assertEquals( "ab c", ChartXTabUtil.getBindingName( "data[\"ab c\"]", //$NON-NLS-1$ //$NON-NLS-2$
				true ) );
		assertEquals( "ab c", //$NON-NLS-1$
				ChartXTabUtil.getBindingName( "data[\"ab c\"] + 100", true ) ); //$NON-NLS-1$
		assertEquals( "ab c", //$NON-NLS-1$
				ChartXTabUtil.getBindingName( "100 * data[\"ab c\"] ", true ) ); //$NON-NLS-1$
		assertEquals( "123", //$NON-NLS-1$
				ChartXTabUtil.getBindingName( "data[\"123\"] + data[\"ab c\"] ", //$NON-NLS-1$
						true ) );

		// Test script expression
		assertEquals( "123", //$NON-NLS-1$
				ChartXTabUtil.getBindingName( "data[\"12\"+\"3\"] ", //$NON-NLS-1$
						true ) );
	}

	public void testGetBindingNameList( )
	{
		List<String> names = ChartXTabUtil.getBindingNameList( "data[\"123\"] + data[\"ab c\"]" ); //$NON-NLS-1$
		assertEquals( 2, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$
		assertEquals( "ab c", names.get( 1 ) ); //$NON-NLS-1$

		names = ChartXTabUtil.getBindingNameList( "123" ); //$NON-NLS-1$
		assertEquals( 0, names.size( ) );

		names = ChartXTabUtil.getBindingNameList( "data[\"123\"]" ); //$NON-NLS-1$
		assertEquals( 1, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$

		names = ChartXTabUtil.getBindingNameList( "data[\"123\"] + 100" ); //$NON-NLS-1$
		assertEquals( 1, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$

		names = ChartXTabUtil.getBindingNameList( "data[\"123\"] + data[\"ab c\"] + data[\"a\"]" ); //$NON-NLS-1$
		assertEquals( 3, names.size( ) );
		assertEquals( "123", names.get( 0 ) ); //$NON-NLS-1$
		assertEquals( "ab c", names.get( 1 ) ); //$NON-NLS-1$
		assertEquals( "a", names.get( 2 ) ); //$NON-NLS-1$
	}

	public void testIsDimensionExpresion( )
	{
		assertEquals( true,
				ChartXTabUtil.isDimensionExpresion( "dimension[\"abc\"][\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartXTabUtil.isDimensionExpresion( "dimension[\"a\"+\"bc\"][\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartXTabUtil.isDimensionExpresion( "dimension[\"abc\"][\"12 3\"]+2" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartXTabUtil.isDimensionExpresion( "2+dimension[\"abc\"][\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartXTabUtil.isDimensionExpresion( "dimension[\"abc\"][12 3]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartXTabUtil.isDimensionExpresion( "dimension[\"abc\"]" ) ); //$NON-NLS-1$
	}

	public void testGetLevelNameFromDimensionExpression( )
	{
		String[] levels = ChartXTabUtil.getLevelNameFromDimensionExpression( "dimension[\"abc\"][\"12 3\"]" );//$NON-NLS-1$
		assertEquals( "abc", levels[0] ); //$NON-NLS-1$
		assertEquals( "12 3", levels[1] ); //$NON-NLS-1$

		// dimension["a"+"bc"]["a"+2*3+"b"]
		levels = ChartXTabUtil.getLevelNameFromDimensionExpression( "dimension[\"a\"+\"bc\"][\"a\"+2*3+\"b\"]" ); //$NON-NLS-1$
		assertEquals( "abc", levels[0] ); //$NON-NLS-1$
		assertEquals( "a6b", levels[1] ); //$NON-NLS-1$

		levels = ChartXTabUtil.getLevelNameFromDimensionExpression( "1+dimension[\"abc\"][\"12 3\"]" );//$NON-NLS-1$
		assertNull( levels );
	}

	public void testIsMeasureExpresion( )
	{
		assertEquals( true,
				ChartXTabUtil.isMeasureExpresion( "measure[\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartXTabUtil.isMeasureExpresion( "measure[\"a\"+\"bc\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartXTabUtil.isMeasureExpresion( "measure[\"12 3\"]+1" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartXTabUtil.isMeasureExpresion( "1*measure[\"12 3\"]" ) ); //$NON-NLS-1$
		assertEquals( false, ChartXTabUtil.isMeasureExpresion( "measure[12 3]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartXTabUtil.isMeasureExpresion( "dimension[\"abc\"]" ) ); //$NON-NLS-1$
	}

	public void testGetMeasureName( )
	{
		assertEquals( "12 3", ChartXTabUtil.getMeasureName( "measure[\"12 3\"]" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "abc", ChartXTabUtil.getMeasureName( "measure[\"a\"+\"bc\"]" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull( ChartXTabUtil.getMeasureName( "measure[\"abc\"+5]" ) ); //$NON-NLS-1$
	}

	public void testCheckStringInExpression( )
	{
		assertEquals( true,
				ChartReportItemUtil.checkStringInExpression( "data[\"year\"]+\"Q\"+data[\"quarter\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartReportItemUtil.checkStringInExpression( "\"Q\"+data[\"quarter\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartReportItemUtil.checkStringInExpression( "data[\"quarter\"]+\"Q\"" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartReportItemUtil.checkStringInExpression( "data[\"year\"]+data[\"quarter\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartReportItemUtil.checkStringInExpression( "4+data[\"quarter\"]" ) ); //$NON-NLS-1$
	}

	public void testIsSingleExpression( )
	{
		assertEquals( true,
				ChartReportItemUtil.isSimpleExpression( "row[\"xxxxx\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartReportItemUtil.isSimpleExpression( "row[\"row a\"]" ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartReportItemUtil.isSimpleExpression( "data[\"xxxxx\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartReportItemUtil.isSimpleExpression( "row[\"xxxxx\"]+row[\"xxxxx\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartReportItemUtil.isSimpleExpression( "row[\"xxxxx\"] + 1" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartReportItemUtil.isSimpleExpression( "1+row[\"xxxxx\"]" ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartReportItemUtil.isSimpleExpression( "row[\"xxxxx\"].getDay()" ) ); //$NON-NLS-1$
	}

	/**
	 * Test {@link ChartUtil#compareVersion(String, String)}.
	 */
	public void testCompareVersion( )
	{
		assertEquals( 0, ChartUtil.compareVersion( "2", "2" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 0, ChartUtil.compareVersion( "2", "2." ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 0, ChartUtil.compareVersion( "2", "2.0" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 1, ChartUtil.compareVersion( "3", "2" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 1, ChartUtil.compareVersion( "3.", "2" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( -16, ChartUtil.compareVersion( "3.1", "3.17" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( -15, ChartUtil.compareVersion( "3.2", "3.17" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 0, ChartUtil.compareVersion( "3.1.", "3.1.0" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 2, ChartUtil.compareVersion( "3.1.2", "3.1" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( -1, ChartUtil.compareVersion( "3.1.2", "3.1.3" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 1, ChartUtil.compareVersion( "3.1.1.5", "3.1" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( 0, ChartUtil.compareVersion( "3.1.1.5", "3.1.1.5.0" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( -1, ChartUtil.compareVersion( "3.1.1.5", "3.1.1.6" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testIsCubeRowExpression( )
	{
		assertEquals( true, ChartUtil.isCubeRowExpression( "data[\"a\"]", true ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartUtil.isCubeRowExpression( "data[\"a\"]", false ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartUtil.isCubeRowExpression( "data[\"a\"]+5", true ) ); //$NON-NLS-1$
		assertEquals( false,
				ChartUtil.isCubeRowExpression( "data[\"a\"]+5", false ) ); //$NON-NLS-1$
		assertEquals( true,
				ChartUtil.isCubeRowExpression( "data[\"a\"]+data[\"c d\"]", true ) ); //$NON-NLS-1$
	}

	public void testIsConstantExpression( )
	{
		assertEquals( true, ChartUtil.isConstantExpression( "0" ) ); //$NON-NLS-1$
		assertEquals( true, ChartUtil.isConstantExpression( "20000" ) ); //$NON-NLS-1$
		assertEquals( true, ChartUtil.isConstantExpression( "-1.5" ) ); //$NON-NLS-1$
		assertEquals( false, ChartUtil.isConstantExpression( "1d" ) ); //$NON-NLS-1$
		assertEquals( false, ChartUtil.isConstantExpression( "1.2.4" ) ); //$NON-NLS-1$
	}
}
