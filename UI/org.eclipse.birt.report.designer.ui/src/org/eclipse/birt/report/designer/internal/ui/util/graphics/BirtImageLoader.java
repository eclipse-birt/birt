/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/


package org.eclipse.birt.report.designer.internal.ui.util.graphics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.elements.structures.EmbeddedImage;

/**
 * ImageLoader to load a give image file into embedded image.
 */

public class BirtImageLoader
{

	/**
	 * load file into byte array with given file name.
	 * 
	 * @param fileName
	 * @return byte array data of image file.
	 */
	public byte[] load( String fileName )
	{
		FileInputStream file = null;
		try
		{
			file = new FileInputStream( fileName );
		}
		catch ( FileNotFoundException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		byte data[] = null;
		if ( file != null )
		{
			try
			{
				data = new byte[file.available( )];
				file.read( data );
			}
			catch ( IOException e1 )
			{
				// TODO Auto-generated catch block
				e1.printStackTrace( );
			}
		}
		return data;

	}

	/**
	 * Loads given image file into given Design file.
	 * 
	 * @param handle
	 *            design file instance handle
	 * @param fileName
	 *            file name of image
	 * @return
	 * @throws SemanticException
	 */
	public EmbeddedImage save( ReportDesignHandle handle, String fileName )
			throws SemanticException
	{
		EmbeddedImage embeddedImage = StructureFactory.createEmbeddedImage( );
		embeddedImage.setName( fileName );
		embeddedImage.setData( load( fileName ) );
		handle.addImage( embeddedImage );

		return embeddedImage;
	}
}