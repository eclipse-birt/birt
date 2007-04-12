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

package org.eclipse.birt.report.engine.emitter.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class PDFRender extends PageDeviceRender
{
	/**
	 * The output stream
	 */
	private OutputStream output = null;

	private PDFPage currentPage = null;
	
	private boolean isTotalPage = false;
	
	public PDFRender( IEmitterServices services )
	{
		initialize( services );
	}
	
	public IPageDevice createPageDevice( String title, IReportContext context,
			IReportContent report ) throws Exception
	{
		return new PDFPageDevice( output, title, context, report );
	}

	public String getOutputFormat( )
	{
		return "pdf";
	}

	protected void newPage( IContainerArea page )
	{
		super.newPage( page );
		currentPage = (PDFPage) pageGraphic;
	}

	public void visitImage( IImageArea imageArea )
	{
		ContainerPosition curPos = getContainerPosition( );
		float imageX = curPos.x + getX( imageArea );
		float imageY = curPos.y + getY( imageArea );
		super.visitImage( imageArea );
		createBookmark( imageArea, imageX, imageY );
		createHyperlink( imageArea, imageX, imageY );
	}

	public void visitText( ITextArea textArea )
	{
		super.visitText( textArea );
		ContainerPosition curPos = getContainerPosition( );
		float x = curPos.x + getX( textArea );
		float y = curPos.y + getY( textArea );
		createBookmark( textArea, x, y );
		createHyperlink( textArea, x, y );
	}

	public void visitAutoText( ITemplateArea templateArea )
	{
		super.visitAutoText( templateArea );
		ContainerPosition curPos = getContainerPosition( );
		float x = curPos.x + getX( templateArea );
		float y = curPos.y + getY( templateArea );
		createTotalPageTemplate( x, y, getWidth( templateArea ),
				getHeight( templateArea ) );
	}

	public void setTotalPage( ITextArea totalPage )
	{
		super.setTotalPage( totalPage );
		ContainerPosition curPos = getContainerPosition( );
		float x = curPos.x + getX( totalPage );
		float y = curPos.y + getY( totalPage );
		isTotalPage = true;
		drawTextAt( totalPage, x, y );
	}

	protected void drawContainer( IContainerArea container )
	{
		super.drawContainer( container );
		ContainerPosition curPos = getContainerPosition( );
		float x = curPos.x + getX( container );
		float y = curPos.y + getY( container );
		createBookmark( container, x, y );
		createHyperlink( container, x, y );
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
					file = new File( "report.pdf" );
					output = new FileOutputStream( file );
				}
				catch ( FileNotFoundException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
	}

	protected void drawTextAt( ITextArea text, float x, float y, float width,
			float height, TextStyle textInfo )
	{
		if ( isTotalPage )
		{
			currentPage.drawTotalPage( text.getText( ), x, y, width, height,
					textInfo );
		}
		else
		{
			currentPage.drawText( text.getText( ), x, y, width, height,
					textInfo );
		}
	}

	private void createHyperlink( IArea area, float x, float y )
	{
		IContent content = area.getContent( );
		if ( null != content )
		{
			IHyperlinkAction hlAction = content.getHyperlinkAction( );
			String systemId = reportRunnable == null ? null : reportRunnable
					.getReportName( );
			if ( null != hlAction )
				try
				{
					float width = getWidth( area );
					float height = getHeight( area );
					String hyperlink = hlAction.getHyperlink( );
					String bookmark = hlAction.getBookmark( );
					String targetWindow = hlAction.getTargetWindow( );
					int type = hlAction.getType( );
					switch ( type )
					{
						case IHyperlinkAction.ACTION_BOOKMARK :
							currentPage.createHyperlink( hyperlink, bookmark,
									targetWindow, type, x, y, width, height );
							break;

						case IHyperlinkAction.ACTION_HYPERLINK :
							currentPage.createHyperlink( hyperlink, null,
									targetWindow, type, x, y, width, height );
							break;

						case IHyperlinkAction.ACTION_DRILLTHROUGH :
							Action act = new Action( systemId, hlAction );

							IHTMLActionHandler actionHandler = null;
							Object ac = services
									.getOption( RenderOption.ACTION_HANDLER );
							if ( ac != null && ac instanceof IHTMLActionHandler )
							{
								actionHandler = (IHTMLActionHandler) ac;
							}

							String link = actionHandler.getURL( act, context );
							currentPage.createHyperlink( link, null,
									targetWindow, type, x, y, width, height );
							break;
					}
				}
				catch ( Exception e )
				{
					logger.log( Level.WARNING, e.getMessage( ), e );
				}
		}
	}

	private void createBookmark( IArea area, float x, float y )
	{
		float height = getHeight( area );
		float width = getWidth( area );
		IContent content = area.getContent( );
		if ( null != content )
		{
			String bookmark = content.getBookmark( );
			if ( null != bookmark )
			{
				currentPage.createBookmark( bookmark, x, y, width, height );
			}
		}
	}

	private void createTotalPageTemplate( float x, float y, float width,
			float height )
	{
		currentPage.createTotalPageTemplate( x, y, width, height );
	}

	protected void drawTotalPage( String text, float x, float y, float width,
			float height, TextStyle textInfo )
	{
		currentPage.drawTotalPage( text, x, y, width, height, textInfo );
	}
}
