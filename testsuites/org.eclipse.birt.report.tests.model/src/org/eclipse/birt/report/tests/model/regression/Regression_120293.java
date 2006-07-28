/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * There is no response when I drag an embedded image from library explorer into
 * outline view.
 * </p>
 * Test description:
 * <p>
 * To create an embedded image from an existed embeded image. 
 * </p>
 */

public class Regression_120293 extends BaseTestCase
{

	/**
	 * @throws Exception
	 */
	
	public void testCreateImageFrom( ) throws Exception
	{
		openLibrary( "Library_1.xml" ); //$NON-NLS-1$

		Iterator imageIter = libraryHandle.imagesIterator( );
		EmbeddedImageHandle baseImage = (EmbeddedImageHandle) imageIter.next( );

		openDesign( "DesignWithoutLibrary.xml" ); //$NON-NLS-1$
		designHandle.includeLibrary( "Library_1.xml", "Lib1" ); //$NON-NLS-1$ //$NON-NLS-2$

		EmbeddedImage newImage = StructureFactory.newEmbeddedImageFrom(
				baseImage, "image1", designHandle ); //$NON-NLS-1$

		assertEquals( "image1", newImage.getName( ) ); //$NON-NLS-1$
		assertNotNull( newImage.getData( design ) );
		
		designHandle.addImage( newImage );

		boolean added = false;
		for ( Iterator iter = designHandle.imagesIterator( ); iter.hasNext( ); )
		{
			String name = ( (EmbeddedImageHandle) iter.next( ) ).getName( );
			if ( "image1".equalsIgnoreCase( name ) ) //$NON-NLS-1$
			{
				added = true;
			}
		}

		assertTrue( added );
	}
}
