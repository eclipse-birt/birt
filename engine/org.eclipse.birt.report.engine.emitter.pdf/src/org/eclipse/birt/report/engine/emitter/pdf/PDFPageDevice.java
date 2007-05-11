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
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;

import com.ibm.icu.util.ULocale;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;


public class PDFPageDevice implements IPageDevice
{

	/**
	 * The pdf Document object created by iText
	 */
	private Document doc = null;

	/**
	 * The Pdf Writer
	 */
	private PdfWriter writer = null;
	
	private IReportContext context;
	
	private IReportContent report;
	
	private Logger logger = Logger.getLogger( PDFPageDevice.class.getName( ) );

	private PDFPage currentPage = null;
	
	public PDFPageDevice( OutputStream output, String title,
			IReportContext context, IReportContent report )
	{
		this.context = context;
		this.report = report;
		doc = new Document();
		PDFPage.reset( );
		try
		{
			writer = PdfWriter.getInstance( doc, new BufferedOutputStream(
					output ) );
			if ( null != title )
				doc.addTitle( title );
		}
		catch( DocumentException de )
		{
			logger.log( Level.SEVERE, de.getMessage( ), de );
		}
	}
	
	public void close( ) throws Exception
	{
		writer.setPageEmpty( false );
		if(doc.isOpen( ))
		{
			doc.close();
		}
	}

	public IPage newPage( int width, int height, Color backgroundColor )
	{
		currentPage = new PDFPage( width, height, doc, writer );
		currentPage.drawBackgroundColor( backgroundColor, 0, 0, width, height );
		return currentPage;
	}
	
	public void createTOC(Set bookmarks)
	{
		ULocale ulocale = null;
		Locale locale = context.getLocale( );
		if(locale==null)
		{
			ulocale = ULocale.getDefault( );
		}
		else
		{
			ulocale = ULocale.forLocale( locale);
		}
		// Before closing the document, we need to create TOC.
		TOCNode tocTree = report.getTOCTree( "pdf", //$NON-NLS-1$
				ulocale ).getRoot( );
		
		if ( tocTree == null || tocTree.getChildren( ).isEmpty( ) )
		{
			writer.setViewerPreferences( PdfWriter.PageModeUseNone );
		}
		else
		{
			writer.setViewerPreferences( PdfWriter.PageModeUseOutlines );
			TOCHandler tocHandler = new TOCHandler( tocTree, writer.getDirectContent().getRootOutline( ), bookmarks );
			tocHandler.createTOC( );
		}
	}
	
}
