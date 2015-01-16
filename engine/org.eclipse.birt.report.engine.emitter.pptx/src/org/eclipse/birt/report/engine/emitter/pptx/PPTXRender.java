/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx;

import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.CompressionMode;
import org.eclipse.birt.report.engine.api.DocxRenderOption;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil;
import org.eclipse.birt.report.engine.emitter.pptx.writer.Slide;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.BlockTextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * The PPT render class.
 */
public class PPTXRender extends PageDeviceRender
{

	private OutputStream out = null;

	private String tempFileDir;

	/** The default output PPT file name. */
	public static final String REPORT_FILE = "Report.pptx"; //$NON-NLS-1$

	private RenderOption renderOption = null;

	public PPTXRender( IEmitterServices services ) throws EngineException
	{
		initialize( services );
		tempFileDir = services.getReportEngine( ).getConfig( ).getTempDir( );
	}

	@Override
	public IPageDevice createPageDevice( String title, String author,
			String subject, String description, IReportContext context,
			IReportContent report ) throws Exception
	{
		try
		{
			int compressionMode = getCompressionMode( renderOption ).getValue( );
			PPTXPageDevice pageDevice = new PPTXPageDevice( out, title, author,
					description, subject, tempFileDir, compressionMode );
			return pageDevice;
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
		return null;
	}

	private CompressionMode getCompressionMode( RenderOption renderOption )
	{
		CompressionMode compressionMode = CompressionMode.BEST_COMPRESSION;
		Object mode = renderOption
				.getOption( DocxRenderOption.OPTION_COMPRESSION_MODE );
		if ( mode instanceof CompressionMode )
		{
			compressionMode = (CompressionMode) mode;
		}
		return compressionMode;
	}

	/**
	 * Returns the output format, always is "pptx".
	 * 
	 * @return the output format
	 */
	@Override
	public String getOutputFormat( ) 
	{
		return "pptx"; //$NON-NLS-1$
	}

	/**
	 * Initializes the PPTEmitter.
	 * 
	 * @param services
	 *            the emitter services object.
	 * @throws BirtException 
	 */
	public void initialize( IEmitterServices services ) throws EngineException
	{
		this.services = services;
		renderOption = (RenderOption) services.getRenderOption( );
		reportRunnable = services.getReportRunnable( );

		if ( reportRunnable != null )
		{
			reportDesign = (ReportDesignHandle) reportRunnable
					.getDesignHandle( );
		}
		this.context = services.getReportContext( );
		this.out = EmitterUtil.getOuputStream( services, REPORT_FILE );
	}

	@Override
	public void visitImage( IImageArea imageArea ) 
	{
		PPTXPage page = (PPTXPage) pageGraphic;
		page.setLink( PPTUtil.getHyperlink( imageArea, services,
				reportRunnable, context ) );
		super.visitImage( imageArea );
		page.setLink( null );
	}

	@Override
	public void visitText( ITextArea textArea )
	{
		PPTXPage page = (PPTXPage) pageGraphic;
		page.setLink( PPTUtil.getHyperlink( textArea, services, reportRunnable,
				context ) );
		super.visitText( textArea );
		page.setLink( null );
	}

	protected void drawTextAt( ITextArea text, int x, int y, int width,
			int height, TextStyle textStyle )
	{
		pageGraphic.drawText( text.getLogicalOrderText( ), x, y, width, height,
				textStyle );
	}

	public PPTXPage getGraphic( ) 
	{
		return (PPTXPage)pageGraphic;
	}
	
	public Slide getSlide( ) 
	{
		return ((PPTXPage) pageGraphic).getSlide( );
	}
}
