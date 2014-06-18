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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.BlockTextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * The PPT render class.
 * 
 * It visit a render area, output it to PPTXGrapchis.
 *  
 */
public class PPTXRender extends PageDeviceRender
{

	private OutputStream out = null;

	private final String tempFileDir;

	/** The default output PPT file name. */
	public static final String REPORT_FILE = "Report.pptx"; //$NON-NLS-1$

	private RenderOption renderOption = null;
	private TableWriter tableWriter;

	public PPTXRender( IEmitterServices services ) throws EngineException
	{
		initialize( services );
		this.out = EmitterUtil.getOuputStream( services, REPORT_FILE );
		tempFileDir = services.getReportEngine( ).getConfig( ).getTempDir( );
	}

	public PPTXRender( PPTXRender render, PPTXCanvas canvas )
	{
		initialize( render.services );
		this.out = render.out;
		this.tempFileDir = render.tempFileDir;
		this.currentX = render.currentX;
		this.currentY = render.currentY;
		this.scale = render.scale;
		this.pageDevice = render.pageDevice;
		this.pageGraphic = new PPTXPage( canvas );
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
	 * Returns the output format, always is "ppt".
	 * 
	 * @return the output format
	 */
	@Override
	public String getOutputFormat( )
	{
		return "pptx";
	}

	/**
	 * Initializes the PPTEmitter.
	 * 
	 * @param services
	 *            the emitter services object.
	 * @throws BirtException
	 */
	public void initialize( IEmitterServices services )
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

	@Override
	protected void drawTextAt( ITextArea text, int x, int y, int width,
			int height, TextStyle textStyle )
	{
		pageGraphic.drawText( text.getLogicalOrderText( ), x, y, width, height,
				textStyle );
	}

	@Override
	public void visitContainer( IContainerArea container )
	{
		if ( container instanceof PageArea )
		{
			new SlideWriter( this ).outputSlide( (PageArea) container );
		}
		else if ( container instanceof TableArea )
		{
			if ( tableWriter == null )
			{
				tableWriter = new TableWriter( this );
				tableWriter.outputTable( (TableArea) container );
				tableWriter = null;
			}
			else
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream( );
				OOXmlWriter writer = new OOXmlWriter( );
				writer.open( out );;
				PPTXCanvas canvas = new PPTXCanvas( this.getCanvas( ), writer );
				PPTXRender render = new PPTXRender( this, canvas );
				container.accept( render );
				writer.close( );
				// append the out to current buffer
				try
				{
					this.getCanvas( ).getWriter( )
							.print( out.toString( "utf-8" ) );
				}
				catch ( IOException ex )
				{
					logger.log( Level.WARNING, "failed to output table", ex );
				}
			}
		}
		else if ( container instanceof BlockTextArea )
		{
			int x = currentX + getX( container );
			int y = currentY + getY( container );
			int width = getWidth( container );
			int height = getHeight( container );
			// startContainer(container);
			new TextWriter( this ).writeBlockText( x, y, width, height,
					(BlockTextArea) container );
			// new TextWriter(this).writeBlockText( currentX, currentY, width,
			// height, (BlockTextArea) container);
			// endContainer(container);
		}
		else
		{
			startContainer( container );
			visitChildren( container );
			endContainer( container );
		}
	}

	@Override
	protected void visitPage( PageArea page )
	{
		super.visitPage( page );
	}

	protected void visitTable( TableArea table )
	{
		startContainer( table );
		visitChildren( table );
		endContainer( table );
	}

	protected void visitText( BlockTextArea text )
	{
		startContainer( text );
		visitChildren( text );
		endContainer( text );
	}

	public PPTXPage getGraphic( )
	{
		return (PPTXPage) pageGraphic;
	}

	public PPTXCanvas getCanvas( )
	{
		return ( (PPTXPage) pageGraphic ).getCanvas( );
	}

	public int getCurrentX( )
	{
		return currentX;
	}

	public int getCurrentY( )
	{
		return currentY;
	}

	public void setCurrentX( int x )
	{
		currentX = x;
	}

	public void setCurrentY( int y )
	{
		currentY = y;
	}

	public float getScale( )
	{
		return scale;
	}
}
