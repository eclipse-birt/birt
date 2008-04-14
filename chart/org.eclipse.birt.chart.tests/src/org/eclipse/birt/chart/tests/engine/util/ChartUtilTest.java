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
		assertEquals( true, ChartXTabUtil.isBinding( "data[\"ab c\"]", false ) );
		assertEquals( true, ChartXTabUtil.isBinding( "data[\"data\"]", false ) );
		assertEquals( false, ChartXTabUtil.isBinding( "data[\"ab c\"]+100",
				false ) );
		assertEquals( true,
				ChartXTabUtil.isBinding( "data[\"ab c\"]+100", true ) );
		assertEquals( true,
				ChartXTabUtil.isBinding( "100+data[\"ab c\"]", true ) );
		assertEquals( true,
				ChartXTabUtil.isBinding( "data[\"ab c\"]+data[\"data\"]", true ) );
	}

	public void testGetBindingName( )
	{
		assertEquals( "ab c", ChartXTabUtil.getBindingName( "data[\"ab c\"]",
				false ) );
		assertEquals( "data", ChartXTabUtil.getBindingName( "data[\"data\"]",
				false ) );
		assertEquals( null,
				ChartXTabUtil.getBindingName( "data[\"data\"] + 100", false ) );
		// assertEquals( null, ChartXTabUtil.getBindingName( "data[\"data\"] +
		// data[\"ab c\"]",
		// false ) );

		assertEquals( "ab c", ChartXTabUtil.getBindingName( "data[\"ab c\"]",
				true ) );
		assertEquals( "ab c",
				ChartXTabUtil.getBindingName( "data[\"ab c\"] + 100", true ) );
		assertEquals( "ab c",
				ChartXTabUtil.getBindingName( "100 * data[\"ab c\"] ", true ) );
		assertEquals( "ab c",
				ChartXTabUtil.getBindingName( "data[\"123\"] + data[\"ab c\"] ",
						true ) );
	}

	public void testGetBindingNameList( )
	{
		List<String> names = ChartXTabUtil.getBindingNameList( "data[\"123\"] + data[\"ab c\"]" );
		assertEquals( 2, names.size( ) );
		assertEquals( "123", names.get( 0 ) );
		assertEquals( "ab c", names.get( 1 ) );

		names = ChartXTabUtil.getBindingNameList( "123" );
		assertEquals( 0, names.size( ) );

		names = ChartXTabUtil.getBindingNameList( "data[\"123\"]" );
		assertEquals( 1, names.size( ) );
		assertEquals( "123", names.get( 0 ) );

		names = ChartXTabUtil.getBindingNameList( "data[\"123\"] + 100" );
		assertEquals( 1, names.size( ) );
		assertEquals( "123", names.get( 0 ) );

		names = ChartXTabUtil.getBindingNameList( "data[\"123\"] + data[\"ab c\"] + data[\"a\"]" );
		assertEquals( 3, names.size( ) );
		assertEquals( "123", names.get( 0 ) );
		assertEquals( "ab c", names.get( 1 ) );
		assertEquals( "a", names.get( 2 ) );
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

}
