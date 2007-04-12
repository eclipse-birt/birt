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

package org.eclipse.birt.report.engine.emitter.postscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.postscript.device.PostscriptPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.model.api.ReportDesignHandle;


public class PostscriptRender extends PageDeviceRender
{
	private OutputStream output = null;
	
	public PostscriptRender( IEmitterServices services )
	{
		initialize( services );
	}
	
	public IPageDevice createPageDevice( String title,
			IReportContext context, IReportContent report )
	{
		try
		{
			return new PostscriptPageDevice( output, title );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
		return null;
	}

	public String getDefaultOuputFile( )
	{
		return "report.ps";
	}

	public String getOutputFormat( )
	{
		return "postscript";
	}

	/**
	 * Initializes the pdfEmitter.
	 * 
	 * @param services
	 *            the emitter svervices object.
	 */
	private void initialize( IEmitterServices services )
	{
		this.services = services;
		// Gets the output file name from RenderOptionBase.OUTPUT_FILE_NAME.
		// It has the top preference.
		IReportRunnable reportRunnable = services.getReportRunnable( );
		if ( reportRunnable != null )
		{
			reportDesign = (ReportDesignHandle) reportRunnable
					.getDesignHandle( );
		}

		this.context = services.getReportContext( );

		Object fd = services.getOption( RenderOption.OUTPUT_FILE_NAME );
		File file = null;
		try
		{
			if ( fd != null )
			{
				file = new File( fd.toString( ) );
				File parent = file.getParentFile( );
				if ( parent != null && !parent.exists( ) )
				{
					parent.mkdirs( );
				}
				output = new FileOutputStream( file );
			}
		}
		catch ( FileNotFoundException fnfe )
		{
			logger.log( Level.WARNING, fnfe.getMessage( ), fnfe );
		}

		// While failed to get the outputStream from the output file name
		// specified
		// from RenderOptionBase.OUTPUT_FILE_NAME, use
		// RenderOptionBase.OUTPUT_STREAM
		// to build the outputStream
		if ( output == null )
		{
			Object value = services.getOption( RenderOption.OUTPUT_STREAM );
			if ( value != null && value instanceof OutputStream )
			{
				output = (OutputStream) value;
			}

			// If the RenderOptionBase.OUTPUT_STREAM is NOT set, build the
			// outputStream from the
			// REPORT_FILE param defined in this file.
			else
			{
				try
				{
					file = new File( "report.ps" );
					output = new FileOutputStream( file );
				}
				catch ( FileNotFoundException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
	}
}
