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

package org.eclipse.birt.report.designer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.birt.report.designer.tests.TestsPlugin;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.elements.structures.EmbeddedImage;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 *  
 */

public class ImageManagerTest extends BaseTestCase
{

	private static ImageData localData;

	private static String iconPath;

	private static final String TEST_FILE = "icon/test.jpg"; //$NON-NLS-1$
	//Doesn't exist
	private static final String TEST_ERROR_FILE = "icon/error.jpg"; //$NON-NLS-1$

	private static final String TEST_URL = "http://www.eclipse.org/images/Idea.jpg"; //$NON-NLS-1$
	//Invalid url
	private static final String TEST_ERROR_URL = "http://"; //$NON-NLS-1$

	static
	{
		try
		{
			iconPath = Platform.asLocalURL( TestsPlugin.getDefault( )
					.getBundle( )
					.getEntry( "/" ) ).getFile( );
			localData = new ImageData( iconPath + TEST_FILE );
		}
		catch ( IOException e )
		{
		}

	}

	/*
	 * Class under test for Image getImage(String)
	 */

	public void testGetImageByPath( ) throws Exception
	{
		Image image = ImageManager.getImage( iconPath + TEST_FILE );
		assertNotNull( image );
		assertTrue( Arrays.equals( image.getImageData( ).data, localData.data ) );
		assertNull( ImageManager.getImage( TEST_ERROR_FILE ) );
	}

	/*
	 * Class under test for Image getImage(String)
	 */

	public void testGetImageByWrongPath( ) throws Exception
	{
		assertNull( ImageManager.getImage( TEST_ERROR_FILE ) );
	}

//	/*
//	 * Class under test for Image getImage(URL)
//	 */
//	public void testGetImageByURL( ) throws Exception
//	{
//		Image image = ImageManager.getImage( new URL( TEST_URL ) );
//		assertNotNull( image );
//		assertTrue( Arrays.equals( image.getImageData( ).data, localData.data ) );
//		assertEquals( image, ImageManager.getImage( new URL( TEST_URL ) ) );
//		assertNull( ImageManager.getImage( new URL( TEST_ERROR_URL ) ) );
//	}
//
//	/*
//	 * Class under test for Image getImage(URL)
//	 */
//
//	public void testGetImageByWrongURL( ) throws Exception
//	{
//		assertNull( ImageManager.getImage( new URL( TEST_ERROR_URL ) ) );
//	}

	/*
	 * Class under test for Image getImage(EmbeddedImage)
	 */
	public void testGetImageByEmbeddedImage( ) throws Exception
	{
		EmbeddedImage embeddedImage = new EmbeddedImage( "Test" ); //$NON-NLS-1$
		FileInputStream is = new FileInputStream( iconPath + TEST_FILE );
		byte[] data = new byte[is.available( )];
		is.read( data );
		embeddedImage.setData( data );
		getReportDesign( ).handle( ).addImage( embeddedImage );
		Image image = ImageManager.getImage( embeddedImage );
		assertNotNull( image );
		assertTrue( Arrays.equals( image.getImageData( ).data, localData.data ) );
		assertEquals( image, ImageManager.getImage( embeddedImage ) );
	}

}