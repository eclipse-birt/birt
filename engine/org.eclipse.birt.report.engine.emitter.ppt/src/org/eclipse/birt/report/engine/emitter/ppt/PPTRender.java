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

import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.ppt.device.PPTPageDevice;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
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

	public IPageDevice createPageDevice( String title, String author, String subject,
			String description, IReportContext context, IReportContent report )
			throws Exception
	{
		try
		{
			return new PPTPageDevice( pptOutput, title, author, description );
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
		this.pptOutput = EmitterUtil.getOuputStream( services, REPORT_FILE );
	}

	protected void drawTextAt( ITextArea text, int x, int y, int width,
			int height, TextStyle textStyle )
	{
		pageGraphic.drawText( text.getLogicalOrderText( ), x, y, width, height, textStyle );
	}
}
