/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.pptx.util.PPTXUtil;
import org.eclipse.birt.report.engine.emitter.pptx.writer.Presentation;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;


public class PPTXPageDevice implements IPageDevice
{

	private static Logger logger = Logger.getLogger( PPTXPageDevice.class
			.getName( ) );
	private Presentation presentation;

	public PPTXPageDevice( OutputStream output, String title, String author,
			String description, String subject, String tempFileDir,
			int compressionMode )
	{
		presentation = new Presentation( output, tempFileDir, compressionMode );
		presentation.setAuthor( author );
		presentation.setTitle( title );
		presentation.setDescription( description );
		presentation.setSubject( subject );
	}

	public void close( ) throws Exception
	{
		presentation.close( );
	}

	public IPage newPage( int width, int height, Color backgroundColor )
	{
		IPage page = null;
		try
		{
			width = PPTXUtil.convertToPointer( width );
			height = PPTXUtil.convertToPointer( height );
			page = new PPTXPage( presentation.createSlide( width, height ) );
			page.drawBackgroundColor( backgroundColor, 0, 0, width, height );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
		return page;
	}
}
