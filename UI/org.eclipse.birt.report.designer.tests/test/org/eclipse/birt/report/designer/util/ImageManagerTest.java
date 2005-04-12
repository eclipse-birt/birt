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
import java.util.Arrays;

import org.eclipse.birt.report.designer.tests.TestsPlugin;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.designer.testutil.PlatformUtil;
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
	private static final String TEST_ERROR_FILE = "icon/error.jpg"; //$NON-NLS-1$ //not exists

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
		Image image = ImageManager.getInstance( ).getImage( iconPath
				+ TEST_FILE );
		assertNotNull( image );
		if ( PlatformUtil.isWindows( ) )
		{//platform related issue
			assertTrue( Arrays.equals( image.getImageData( ).data,
					localData.data ) );
		}
	}

	/*
	 * Class under test for Image getImage(String)
	 */

	public void testGetImageByWrongPath( ) throws Exception
	{
		assertNull( ImageManager.getInstance( ).getImage( TEST_ERROR_FILE ) );
	}

	/*
	 * Class under test for Image getImage(URL)
	 */
	public void testGetImageByURL( ) throws Exception
	{
		Image image = ImageManager.getInstance( ).getImage( TEST_URL );
		assertNotNull( image );
		assertTrue( Arrays.equals( image.getImageData( ).data, localData.data ) );
		assertEquals( image, ImageManager.getInstance( ).getImage( TEST_URL ) );
	}

	/*
	 * Class under test for Image getImage(URL)
	 */

	public void testGetImageByWrongURL( ) throws Exception
	{
		assertNull( ImageManager.getInstance( ).getImage( TEST_ERROR_URL ) );
	}

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
		Image image = ImageManager.getInstance( )
				.getImage( getReportDesignHandle( ), embeddedImage.getName( ) );
		assertNotNull( image );
		if ( PlatformUtil.isWindows( ) )
		{//platform related issue
			assertTrue( Arrays.equals( image.getImageData( ).data,
					localData.data ) );
		}
		assertEquals( image, ImageManager.getInstance( )
				.getImage( getReportDesignHandle( ), embeddedImage.getName( ) ) );
	}

	public void testLoadImage( ) throws IOException
	{
		Image image = ImageManager.getInstance( ).loadImage( iconPath
				+ TEST_FILE );
		assertNotNull( image );
		assertEquals( image, ImageManager.getInstance( ).loadImage( iconPath
				+ TEST_FILE ) );
		assertEquals( image, ImageManager.getInstance( ).getImage( iconPath
				+ TEST_FILE ) );
		try
		{
			ImageManager.getInstance( ).loadImage( TEST_ERROR_FILE );
		}
		catch ( Exception e )
		{
			return;
		}
		fail( );
	}
}