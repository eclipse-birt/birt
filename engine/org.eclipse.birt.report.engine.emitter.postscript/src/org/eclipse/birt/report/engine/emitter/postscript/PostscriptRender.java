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

import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.postscript.device.PostscriptPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.model.api.ReportDesignHandle;


public class PostscriptRender extends PageDeviceRender
{
	private OutputStream output = null;
	
	public PostscriptRender( IEmitterServices services ) throws EngineException
	{
		initialize( services );
	}
	
	public IPageDevice createPageDevice( String title, String author, String subject, String description,
			IReportContext context, IReportContent report )
	{
		try
		{
			return new PostscriptPageDevice( output, title, author, description );
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
	 * @throws EngineException 
	 */
	private void initialize( IEmitterServices services ) throws EngineException
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
		this.output = EmitterUtil.getOuputStream( services, "report.ps" );
	}
}
