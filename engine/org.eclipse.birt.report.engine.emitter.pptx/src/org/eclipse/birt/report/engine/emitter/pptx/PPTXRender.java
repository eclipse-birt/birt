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
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil;
import org.eclipse.birt.report.engine.emitter.pptx.util.PPTXUtil;
import org.eclipse.birt.report.engine.emitter.pptx.writer.Presentation;
import org.eclipse.birt.report.engine.emitter.pptx.writer.SlideMaster;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.BlockTextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * The PPTX render class.
 * 
 * It visit a render area, output it to PPTXGrapchis.
 *  
 */
public class PPTXRender extends PageDeviceRender
{

	/**
	 * option to define if export PPTX in edit mode.
	 * 
	 * TRUE: mapping BIRT style properties to PPTX properties, so the final
	 * layout may be difference to PDF.
	 * 
	 * FALSE: the content exactly follows layout constrains, so it has same
	 * layout with PDF.
	 * 
	 * the default value is TRUE.
	 */
	public static final String OPTION_EDIT_MODE = "org.eclipse.birt.report.emitter.PPTX.editMode";

	/** The default output PPT file name. */
	public static final String REPORT_FILE = "Report.pptx"; //$NON-NLS-1$

	private final OutputStream out;
	private final String tempFileDir;

	private RenderOption renderOption;
	private TableWriter tableWriter;
	private boolean editMode;

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
			PPTXPageDevice pageDevice = new PPTXPageDevice( out,
					title,
					author,
					description,
					subject,
					tempFileDir,
					compressionMode );
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
		Object mode = renderOption.getOption( DocxRenderOption.OPTION_COMPRESSION_MODE );
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
	public void initialize( IEmitterServices services )
	{
		this.services = services;
		renderOption = (RenderOption) services.getRenderOption( );
		reportRunnable = services.getReportRunnable( );

		if ( reportRunnable != null )
		{
			reportDesign = (ReportDesignHandle) reportRunnable.getDesignHandle( );
		}
		this.context = services.getReportContext( );
		this.editMode = renderOption.getBooleanOption( OPTION_EDIT_MODE, true );
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
	public void visitContainer( IContainerArea container )
	{
		if ( container instanceof PageArea )
		{
			new SlideWriter( this ).writeSlide( (PageArea) container );
			newPage( container );
			new SlideWriter( this ).writeSlide( (PageArea) container );
			this.pageGraphic.dispose( );
		}
		else if ( container instanceof TableArea )
		{
			outputTable( (TableArea) container );
		}
		else if ( container instanceof BlockTextArea )
		{
			outputText( (BlockTextArea) container );
		}
		else
		{
			startContainer( container );
			visitChildren( container );
			endContainer( container );
		}
	}

	private void outputTable( TableArea table )
	{
		if ( !editMode )
		{
			visitTable( table );
			return;
		}
		if ( tableWriter == null )
		{
			tableWriter = new TableWriter( this );
			tableWriter.outputTable( table );
			tableWriter = null;
		}
		else
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			OOXmlWriter writer = new OOXmlWriter( );
			writer.open( out );;
			PPTXCanvas canvas = new PPTXCanvas( this.getCanvas( ), writer );
			PPTXRender render = new PPTXRender( this, canvas );
			table.accept( render );
			writer.close( );
			// append the out to current buffer
			try
			{
				this.getCanvas( ).getWriter( ).print( out.toString( "utf-8" ) );
			}
			catch ( IOException ex )
			{
				logger.log( Level.WARNING, "failed to output table", ex );
			}
		}
	}

	private void outputText( BlockTextArea text )
	{
		if ( !editMode )
		{
			visitText( text );
			return;
		}
		int x = currentX + getX( text );
		int y = currentY + getY( text );
		int width = getWidth( text );
		int height = getHeight( text );
		// startContainer(container);
		new TextWriter( this ).writeBlockText( x, y, width, height, text );
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

	private String getMasterPageName( PageArea area )
	{
		if ( area.getContent( ) instanceof PageContent )
		{
			PageContent pageContent = (PageContent) area.getContent( );
			return pageContent.getName( );
		}
		return "";
	}

	@Override
	protected void newPage( IContainerArea area )
	{
		assert ( area instanceof PageArea );
		PageArea pageArea = (PageArea) area;
		scale = pageArea.getScale( );
		int pageHeight = getHeight( pageArea );
		int pageWidth = getWidth( pageArea );
		try
		{
			int width = PPTXUtil.convertToPointer( pageWidth );
			int height = PPTXUtil.convertToPointer( pageHeight );
			Presentation presentation = ( (PPTXPageDevice) pageDevice ).getPresentation( );
			String masterPageName = getMasterPageName( pageArea );
			SlideMaster master = presentation.getSlideMaster( masterPageName );
			if ( master == null )
			{
				master = presentation.createSlideMaster( masterPageName,
						pageArea );
				new SlideWriter( this ).writeSlideMaster( master, pageArea );
			}
			this.pageGraphic = new PPTXPage( presentation.createSlide( master,
					width,
					height,
					pageArea ) );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
	}

}
