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

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.util.BundleVersionUtil;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;

import com.ibm.icu.util.ULocale;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfOutline;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFPageDevice implements IPageDevice
{

	/**
	 * The pdf Document object created by iText
	 */
	protected Document doc = null;

	/**
	 * The Pdf Writer
	 */
	protected PdfWriter writer = null;

	protected IReportContext context;

	protected IReportContent report;

	protected static Logger logger = Logger.getLogger( PDFPageDevice.class
			.getName( ) );

	protected PDFPage currentPage = null;

	protected HashMap<Float, PdfTemplate> templateMap = new HashMap<Float, PdfTemplate>( );

	protected HashMap<String, PdfTemplate> imageCache = new HashMap<String, PdfTemplate>( );

	/**
	 * the iText and Birt engine version info.
	 */
	protected static String[] versionInfo = new String[]{
			BundleVersionUtil
					.getBundleVersion( "org.eclipse.birt.report.engine" )};

	protected final static int MAX_PAGE_WIDTH = 14400000; // 200 inch
	protected final static int MAX_PAGE_HEIGHT = 14400000; // 200 inch

	public PDFPageDevice( OutputStream output, String title, String author,
			String subject, String description, IReportContext context,
			IReportContent report )
	{
		this.context = context;
		this.report = report;
		doc = new Document( );
		try
		{
			writer = PdfWriter.getInstance( doc, 
					output );
			writer.setFullCompression( );
			writer.setRgbTransparencyBlending( true );
			EngineResourceHandle handle = new EngineResourceHandle(
					ULocale.forLocale( context.getLocale( ) ) );

			String creator = handle.getMessage( MessageConstants.PDF_CREATOR,
					versionInfo );
			doc.addCreator( creator );

			if ( null != author )
			{
				doc.addAuthor( author );
			}
			if ( null != title )
			{
				doc.addTitle( title );
			}
			if ( null != subject )
			{
				doc.addSubject( subject );
				doc.addKeywords( subject );
			}
			if ( description != null )
			{
				doc.addHeader( "Description", description );
			}
		}
		catch ( DocumentException de )
		{
			logger.log( Level.SEVERE, de.getMessage( ), de );
		}
	}

	/**
	 * constructor for test
	 *
	 * @param output
	 */
	public PDFPageDevice( OutputStream output )
	{
		doc = new Document( );
		try
		{
			writer = PdfWriter.getInstance( doc, new BufferedOutputStream(
					output ) );
		}
		catch ( DocumentException de )
		{
			logger.log( Level.SEVERE, de.getMessage( ), de );
		}
	}

	public void setPDFTemplate( Float scale, PdfTemplate totalPageTemplate )
	{
		templateMap.put( scale, totalPageTemplate );
	}

	public HashMap<Float, PdfTemplate> getTemplateMap( )
	{
		return templateMap;
	}

	public PdfTemplate getPDFTemplate( Float scale )
	{
		return templateMap.get( scale );
	}

	public boolean hasTemplate( Float scale )
	{
		return templateMap.containsKey( scale );
	}

	public HashMap<String, PdfTemplate> getImageCache( )
	{
		return imageCache;
	}

	public void close( ) throws Exception
	{
		if ( !doc.isOpen( ) )
		{
			// to ensure we create a PDF file
			doc.open( );
		}
		writer.setPageEmpty( false );
		if ( doc.isOpen( ) )
		{
			doc.close( );
		}
	}

	public IPage newPage( int width, int height, Color backgroundColor )
	{
		int w = Math.min( width, MAX_PAGE_WIDTH );
		int h = Math.min( height, MAX_PAGE_HEIGHT );
		currentPage = createPDFPage( w, h );
		currentPage.drawBackgroundColor( backgroundColor, 0, 0, w, h );
		return currentPage;
	}

	protected PDFPage createPDFPage( int pageWidth, int pageHeight )
	{
		return new PDFPage( pageWidth, pageHeight, doc, writer, this );
	}

	public void createTOC( Set<String> bookmarks )
	{
		// we needn't create the TOC if there is no page in the PDF file.
		// the doc is opened only if the user invokes newPage.
		if ( !doc.isOpen( ) )
		{
			return;
		}
		if ( bookmarks.isEmpty( ) )
		{
			writer.setViewerPreferences( PdfWriter.PageModeUseNone );
			return;
		}
		ULocale ulocale = null;
		Locale locale = context.getLocale( );
		if ( locale == null )
		{
			ulocale = ULocale.getDefault( );
		}
		else
		{
			ulocale = ULocale.forLocale( locale );
		}
		// Before closing the document, we need to create TOC.
		ITOCTree tocTree = report.getTOCTree( "pdf", //$NON-NLS-1$
				ulocale );
		if ( tocTree == null )
		{
			writer.setViewerPreferences( PdfWriter.PageModeUseNone );
		}
		else
		{
			TOCNode rootNode = tocTree.getRoot( );
			if ( rootNode == null || rootNode.getChildren( ).isEmpty( ) )
			{
				writer.setViewerPreferences( PdfWriter.PageModeUseNone );
			}
			else
			{
				writer.setViewerPreferences( PdfWriter.PageModeUseOutlines );
				TOCHandler tocHandler = new TOCHandler( rootNode, writer
						.getDirectContent( ).getRootOutline( ), bookmarks );
				tocHandler.createTOC( );
			}
		}
	}

	protected TOCHandler createTOCHandler( TOCNode root, PdfOutline outline,
			Set<String> bookmarks )
	{
		return new TOCHandler( root, outline, bookmarks );
	}
}
