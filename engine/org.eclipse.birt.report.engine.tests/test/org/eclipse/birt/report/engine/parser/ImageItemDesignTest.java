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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;

/**
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ImageItemDesignTest extends AbstractDesignTestCase
{

	public void setUp( ) throws Exception
	{
		loadDesign( "imageItem_test.xml" );
	}

	public void testImageBasic( )
	{
		ImageItemDesign image = (ImageItemDesign) report.getContent( 0 );

		assertEquals( "myImage", image.getName( ) );
		assertEquals( 10, image.getHeight( ).getMeasure( ), Double.MIN_VALUE );
		assertEquals( 12, image.getWidth( ).getMeasure( ), Double.MIN_VALUE );
		assertEquals( 1, image.getX( ).getMeasure( ), Double.MIN_VALUE );
		assertEquals( 2, image.getY( ).getMeasure( ), Double.MIN_VALUE );

		assertEquals(
				"C:\\Documents and Settings\\Administrator\\My Documents\\63.jpg",
				image.getImageUri( ).getScriptText( ) );

		assertEquals( ActionDesign.ACTION_HYPERLINK, image.getAction( )
				.getActionType( ) );
		assertEquals( "http://www.msn.com", image.getAction( ).getHyperlink( )
				.getScriptText( ) );
		assertEquals( "This is a sample image of gif type!", image.getAltText( ).toString() );
	}

}
