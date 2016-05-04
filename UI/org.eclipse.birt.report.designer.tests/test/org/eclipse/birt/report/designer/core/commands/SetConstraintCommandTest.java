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

package org.eclipse.birt.report.designer.core.commands;

import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * @author xzhang
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SetConstraintCommandTest extends CmdBaseTestCase
{

	public void notestSetConstraintTableCmd( )
	{
		SetConstraintCommand cmd = new SetConstraintCommand( );
		Dimension size = new Dimension( 200, 100 );
		//need tableAdapterHelper
		cmd.setModel( table );
		cmd.setSize( size );
		cmd.execute( );
		Dimension dim = adapter.calculateSize( );
		assertEquals( 200, dim.width );
		assertEquals( 100, dim.height );

	}

	public void testSetConstraintImageCmd( ) throws SemanticException
	{
		ImageHandle newImage = getElementFactory( ).newImage( "Test Image" );
		newImage.setWidth( "150px" );
		newImage.setHeight( "180px" );
		assertEquals( 150, (int) newImage.getWidth( ).getMeasure( ) );
		assertEquals( 180, (int) newImage.getHeight( ).getMeasure( ) );

		SetConstraintCommand cmd = new SetConstraintCommand( );
		Dimension size = new Dimension( 200, 100 );
		cmd.setModel( newImage );
		cmd.setSize( size );
		cmd.execute( );

		assertTrue( newImage.getWidth( )
				.getUnits( )
				.equals( DesignChoiceConstants.UNITS_PX ) );
		assertTrue( newImage.getHeight( )
				.getUnits( )
				.equals( DesignChoiceConstants.UNITS_PX ) );
		assertEquals( 200, (int) newImage.getWidth( ).getMeasure( ) );
		assertEquals( 100, (int) newImage.getHeight( ).getMeasure( ) );

	}

	public void testSetConstraintDataItemCmd( )
	{

		DataItemHandle dataItem = getElementFactory( ).newDataItem( "TestDataItem" );

		SetConstraintCommand cmd = new SetConstraintCommand( );
		Dimension size = new Dimension( 100, 50 ); //pixel
		cmd.setModel( dataItem );
		cmd.setSize( size );
		cmd.execute( );

		DimensionHandle width = dataItem.getWidth( );
		DimensionHandle height = dataItem.getHeight( );
		assertTrue( width.getUnits( ).equals( DesignChoiceConstants.UNITS_IN ) );
		assertTrue( height.getUnits( ).equals( DesignChoiceConstants.UNITS_IN ) );
		double widthPixel = MetricUtility.inchToPixel( width.getMeasure( ) );
		double heightPixel = MetricUtility.inchToPixel( height.getMeasure( ) );
		assertEquals( 100, (int) widthPixel );
		assertEquals( 50, (int) heightPixel );
	}

	public void testSetConstraintLabelCmd( )
	{

		LabelHandle label = getElementFactory( ).newLabel( "TestLabel" );

		SetConstraintCommand cmd = new SetConstraintCommand( );
		Dimension size = new Dimension( 50, 100 ); //pixel
		cmd.setModel( label );
		cmd.setSize( size );
		cmd.execute( );

		DimensionHandle width = label.getWidth( );
		DimensionHandle height = label.getHeight( );
		assertTrue( width.getUnits( ).equals( DesignChoiceConstants.UNITS_IN ) );
		assertTrue( height.getUnits( ).equals( DesignChoiceConstants.UNITS_IN ) );
		double widthPixel = MetricUtility.inchToPixel( width.getMeasure( ) );
		double heightPixel = MetricUtility.inchToPixel( height.getMeasure( ) );
		assertEquals( 50, (int) widthPixel );
		assertEquals( 100, (int) heightPixel );

	}

}