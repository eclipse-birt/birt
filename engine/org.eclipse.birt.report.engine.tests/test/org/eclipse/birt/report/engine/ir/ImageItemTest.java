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

package org.eclipse.birt.report.engine.ir;

/**
 * Image Item test
 * 
 * @version $Revision: 1.8 $ $Date: 2006/04/06 12:35:25 $
 */
public class ImageItemTest extends ReportItemTestCase
{

	public ImageItemTest( )
	{
		super( new ImageItemDesign( ) );

	}

	/**
	 * Test get/setAction methods
	 * 
	 * set the action
	 * 
	 * then get it to test if they work correctly
	 */
	public void testAction( )
	{
		ImageItemDesign image = new ImageItemDesign( );
		ActionDesign action = new ActionDesign( );

		//Set
		image.setAction( action );

		//Get
		assertEquals( image.getAction( ), action );

	}

	/**
	 * Test get/setAltText methods
	 * 
	 * set the texts
	 * 
	 * then get them to test if they work correctly
	 */
	public void testAltText( )
	{
		ImageItemDesign image = new ImageItemDesign( );

		//Set
		image.setAltText( "TestKey", "AltText" );

		//Get
		assertEquals( image.getAltText( ), "AltText" );
		assertEquals( image.getAltTextKey( ), "TestKey" );

	}

	/**
	 * Test setImageExpression and getImageFormat methods
	 * 
	 * set the image by a expression
	 * 
	 * then get the expression and check the source type to test if they work
	 * correctly
	 */
	public void testExpression( )
	{
		ImageItemDesign image = new ImageItemDesign( );
		String exp = "exp";
		String typeExp = "typeExp";

		//Set
		image.setImageExpression( exp, typeExp );

		//Get
		assertEquals( image.getImageSource( ), ImageItemDesign.IMAGE_EXPRESSION );
		assertEquals( image.getImageFormat( ), typeExp );
	}

	/**
	 * Test get/setImageName methods
	 * 
	 * set the image by a filename
	 * 
	 * then get the name and check the source type to test if they work
	 * correctly
	 */
	public void testName( )
	{
		ImageItemDesign image = new ImageItemDesign( );

		//Set
		image.setImageName( "TestImage.bmp" );

		//Get
		assertEquals( image.getImageName( ), "TestImage.bmp" );
		assertEquals( image.getImageSource( ), ImageItemDesign.IMAGE_NAME );

	}

	/**
	 * Test get/setImageUri methods
	 * 
	 * set the image by a URI address
	 * 
	 * then get the address and check the source type to test if they work
	 * correctly
	 */
	public void testUri( )
	{
		ImageItemDesign image = new ImageItemDesign( );

		//Set
		image
				.setImageUri( "http://www.actuate.com/images/navimages/v8/logo.gif" );

		//Get
		assertEquals( image.getImageUri( ),
				"http://www.actuate.com/images/navimages/v8/logo.gif" );
		assertEquals( image.getImageSource( ), ImageItemDesign.IMAGE_URI );
	}
}
