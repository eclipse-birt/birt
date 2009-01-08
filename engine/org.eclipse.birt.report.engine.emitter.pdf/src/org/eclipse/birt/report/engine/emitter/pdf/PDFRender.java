/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pdf;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
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
	
	private PDFPageDevice currentPageDevice = null;
	
	private HashSet bookmarks = new HashSet();
	
	public PDFRender( IEmitterServices services ) throws EngineException
	{
		initialize( services );
	}
	
	public IPageDevice createPageDevice( String title, String author, String subject,
			String comments, IReportContext context, IReportContent report )
			throws Exception
	{
		currentPageDevice = new PDFPageDevice( output, title, author, subject, comments,
				context, report );
		return currentPageDevice;
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
		int imageX = currentX + getX( imageArea );
		int imageY = currentY + getY( imageArea );
		super.visitImage( imageArea );
		createBookmark( imageArea, imageX, imageY );
		createHyperlink( imageArea, imageX, imageY );
	}

	public void visitText( ITextArea textArea )
	{
		super.visitText( textArea );
		int x = currentX + getX( textArea );
		int y = currentY + getY( textArea );
		createBookmark( textArea, x, y );
		createHyperlink( textArea, x, y );
	}

	public void visitAutoText( ITemplateArea templateArea )
	{
		super.visitAutoText( templateArea );
		int x = currentX + getX( templateArea );
		int y = currentY + getY( templateArea );
		createTotalPageTemplate( x, y, getWidth( templateArea ),
				getHeight( templateArea ) );
	}

	public void setTotalPage( ITextArea totalPage )
	{
		super.setTotalPage( totalPage );
		isTotalPage = true;
		drawText( totalPage );
		isTotalPage = false;
	}
	
	/**
	 * Closes the document.
	 * 
	 * @param rc
	 *            the report content.
	 */
	public void end( IReportContent rc )
	{
		createTOC( );
		super.end( rc );
	}

	protected void drawContainer( IContainerArea container )
	{
		super.drawContainer( container );
		int x = currentX + getX( container );
		int y = currentY + getY( container );
		createBookmark( container, x, y );
		createHyperlink( container, x, y );
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
		reportRunnable = services.getReportRunnable( );
		if ( reportRunnable != null )
		{
			reportDesign = (ReportDesignHandle) reportRunnable
					.getDesignHandle( );
		}

		this.context = services.getReportContext( );
		this.output = EmitterUtil.getOuputStream( services, "report.pdf" );
	}

	protected void drawTextAt( ITextArea text, int x, int y, int width,
			int height, TextStyle textInfo )
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

	private void createHyperlink( IArea area, int x, int y )
	{
		IContent content = area.getContent( );
		if ( null != content )
		{
			IHyperlinkAction hlAction = content.getHyperlinkAction( );
			if ( null != hlAction )
				try
				{
					String systemId = reportRunnable == null
							? null
							: reportRunnable.getReportName( );
					int width = getWidth( area );
					int height = getHeight( area );
					String bookmark = hlAction.getBookmark( );
					String targetWindow = hlAction.getTargetWindow( );
					int type = hlAction.getType( );
					Action act = new Action( systemId, hlAction );
					String link = null;
					IHTMLActionHandler actionHandler = null;
					Object ac = services
							.getOption( RenderOption.ACTION_HANDLER );
					if ( ac != null && ac instanceof IHTMLActionHandler )
					{
						actionHandler = (IHTMLActionHandler) ac;
					}
					if(actionHandler!=null)
					{
						link = actionHandler.getURL( act, context );
					}
					else
					{
						link = hlAction.getHyperlink( );
					}

					switch ( type )
					{
						case IHyperlinkAction.ACTION_BOOKMARK :
							currentPage.createHyperlink( link, bookmark,
									targetWindow, type, x, y, width, height );
							break;

						case IHyperlinkAction.ACTION_HYPERLINK :
							currentPage.createHyperlink( link, null,
									targetWindow, type, x, y, width, height );
							break;

						case IHyperlinkAction.ACTION_DRILLTHROUGH :
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

	private void createBookmark( IArea area, int x, int y )
	{
		IContent content = area.getContent( );
		if ( null != content )
		{
			String bookmark = content.getBookmark( );
			if ( null != bookmark )
			{
				int height = getHeight( area );
				int width = getWidth( area );
				currentPage.createBookmark( bookmark, x, y, width, height );
				bookmarks.add(bookmark);
			}
		}
	}
	
	private void createTOC( )
	{
		currentPageDevice.createTOC(bookmarks);
	}

	private void createTotalPageTemplate( int x, int y, int width,
			int height )
	{
		currentPage.createTotalPageTemplate( x, y, width, height );
	}

	protected void drawTotalPage( String text, int x, int y, int width,
			int height, TextStyle textInfo )
	{
		currentPage.drawTotalPage( text, x, y, width, height, textInfo );
	}
}
