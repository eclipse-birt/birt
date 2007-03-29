/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.script;

import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.script.api.ChartComponentFactory;

/**
 * 
 */

public class ChartTest extends BaseChartTestCase
{

	public void testTitle( )
	{
		assertEquals( "IChart.getTitle", getChartWithAxes( ).getTitle( )
				.getCaption( )
				.getValue( ), "Bar Chart Title" );

		String newTitle = "Test title";
		Label label = LabelImpl.create( );
		label.setCaption( TextImpl.create( newTitle ) );
		getChartWithAxes( ).setTitle( ChartComponentFactory.convertLabel( label ) );
		assertEquals( "IChart.SetTitle", getChartWithAxes( ).getTitle( )
				.getCaption( )
				.getValue( ), newTitle );
	}

	public void testDescription( )
	{
		assertEquals( "IChart.getDescription",
				getChartWithAxes( ).getDescription( ).getValue( ),
				"Description" );

		String newDesc = "Test description";
		getChartWithAxes( ).setDescription( ChartComponentFactory.convertText( TextImpl.create( newDesc ) ) );
		assertEquals( "IChart.SetDescription",
				getChartWithAxes( ).getDescription( ).getValue( ),
				newDesc );
	}

	public void testColorByCategory( )
	{
		assertFalse( "IChart.isColorByCategory",
				getChartWithAxes( ).isColorByCategory( ) );

		getChartWithAxes( ).setColorByCategory( true );
		assertTrue( "IChart.setColorByCategory",
				getChartWithAxes( ).isColorByCategory( ) );
	}

	public void testOutput( )
	{
		assertEquals( "IChart.getOutputType",
				getChartWithAxes( ).getOutputType( ).toUpperCase( ),
				"PNG" );

		getChartWithAxes( ).setOutputType( "SVG" );
		assertEquals( "IChart.setOutputType",
				getChartWithAxes( ).getOutputType( ).toUpperCase( ),
				"SVG" );
	}

	public void testDimension( )
	{
		assertEquals( "IChart.getDimension",
				getChartWithAxes( ).getDimension( ),
				ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) );

		getChartWithAxes( ).setDimension( "ThreeDimensional" );
		assertEquals( "IChart.setDimension",
				getChartWithAxes( ).getDimension( ),
				ChartDimension.THREE_DIMENSIONAL_LITERAL.getName( ) );

		getChartWithAxes( ).setDimension( "3d" );
		assertEquals( "Test invalid chart dimension",
				getChartWithAxes( ).getDimension( ),
				ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) );
	}

	public void testGetCategory( )
	{
		assertNotNull( "IChart.getCategorySeries",
				getChartWithAxes( ).getCategory( ) );
	}

}
