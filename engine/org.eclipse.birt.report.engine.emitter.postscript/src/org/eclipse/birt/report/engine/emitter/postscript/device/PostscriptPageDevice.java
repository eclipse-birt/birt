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

package org.eclipse.birt.report.engine.emitter.postscript.device;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.api.IPostscriptRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.emitter.postscript.PostscriptRenderOption;
import org.eclipse.birt.report.engine.emitter.postscript.PostscriptWriter;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;

/**
 * Represents a postscript device, which will manage postscript pages.
 */
public class PostscriptPageDevice implements IPageDevice
{

	private PostscriptWriter writer;
	private PostscriptPage currentPage;

	public PostscriptPageDevice( RenderOption renderOption, OutputStream output, String title,
			String author, String description ) throws Exception
	{
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				output );
		writer = new PostscriptWriter( bufferedOutputStream, title );
		
		String paperSize = renderOption
				.getStringOption( PostscriptRenderOption.OPTION_PAPER_SIZE );
		String paperTray = renderOption
				.getStringOption( PostscriptRenderOption.OPTION_PAPER_TRAY );
		String duplex = renderOption
				.getStringOption( PostscriptRenderOption.OPTION_DUPLEX );
		int copies = renderOption.getIntOption(
				PostscriptRenderOption.OPTION_COPIES, 1 );
		boolean collate = renderOption.getBooleanOption(
				PostscriptRenderOption.OPTION_COLLATE, false );
		int resolution = renderOption.getIntOption(
				IPostscriptRenderOption.OPTION_RESOLUTION, 0 );
		writer.startRenderer( author, description, paperSize, paperTray,
				duplex, copies, collate, resolution );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPagableDevice#close()
	 */
	public void close( ) throws IOException
	{
		writer.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.page.IPagableDevice#newPage(float,
	 *      float)
	 */
	public IPage newPage( int width, int height, Color backgroundColor )
	{
		if ( currentPage != null )
		{
			currentPage.dispose( );
		}
		currentPage = new PostscriptPage( width, height, backgroundColor,
				writer );
		return currentPage;
	}
}
