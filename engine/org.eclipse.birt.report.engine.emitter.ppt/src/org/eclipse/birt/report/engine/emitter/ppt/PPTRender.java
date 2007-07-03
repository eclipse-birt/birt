/*******************************************************************************
 * Copyright (c) 2006, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt;

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
import org.eclipse.birt.report.engine.emitter.ppt.device.PPTPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * The PPT render class.
 */
public class PPTRender extends PageDeviceRender
{

	private OutputStream pptOutput = null;

	/** The default output PPT file name. */
	public static final String REPORT_FILE = "Report.ppt"; //$NON-NLS-1$

	public PPTRender( IEmitterServices services )
	{
		initialize( services );
	}

	public IPageDevice createPageDevice( String title, IReportContext context,
			IReportContent report ) throws Exception
	{
		try
		{
			return new PPTPageDevice( pptOutput );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
		return null;
	}

	/**
	 * Returns the output format, always is "ppt".
	 * 
	 * @return the output format
	 */
	public String getOutputFormat( )
	{
		return "ppt";
	}

	/**
	 * Initializes the PPTEmitter.
	 * 
	 * @param services
	 *            the emitter services object.
	 */
	public void initialize( IEmitterServices services )
	{
		this.services = services;
		IReportRunnable reportRunnable = services.getReportRunnable( );

		if ( reportRunnable != null )
		{
			reportDesign = (ReportDesignHandle) reportRunnable.getDesignHandle( );
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
				pptOutput = new FileOutputStream( file );
			}
		}
		catch ( FileNotFoundException fnfe )
		{
			logger.log( Level.WARNING, fnfe.getMessage( ), fnfe );
		}

		// While failed to get the outputStream from the output file name
		// specified from RenderOptionBase.OUTPUT_FILE_NAME, use
		// RenderOptionBase.OUTPUT_STREAM to build the outputStream.
		if ( pptOutput == null )
		{
			Object value = services.getOption( RenderOption.OUTPUT_STREAM );

			if ( value instanceof OutputStream )
			{
				pptOutput = (OutputStream) value;
			}

			// If the RenderOptionBase.OUTPUT_STREAM is NOT set, build the
			// outputStream from the REPORT_FILE parameter defined in this file.
			else
			{
				try
				{
					file = new File( REPORT_FILE );
					pptOutput = new FileOutputStream( file );
				}
				catch ( FileNotFoundException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
	}

}
