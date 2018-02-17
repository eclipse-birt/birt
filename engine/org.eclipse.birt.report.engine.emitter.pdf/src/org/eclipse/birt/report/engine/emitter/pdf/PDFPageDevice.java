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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

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
			writer = PdfWriter.getInstance( doc, new BufferedOutputStream(
					output ) );
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
		
		//modified here. This will grab a global variable called 
		//appendPDF, and take a list of strings of PDF files to 
		//append to the end.
		//this iswhere we will test the merge
		List<InputStream> pdfs = new ArrayList<InputStream>();
		
		String list = (String) context.getReportRunnable().getProperty("AppendList");
		
		//check that the report variable AppendList is set, and actually has value
		if (list != null)
		{
			if (list.length() > 0)
			{
				//iterate over the list, and create a fileinputstream for each file location. 
				for (String s : list.split(","))
				{
					//If there is an exception creating the input stream, don't stop execution.
					//Just graceffully let the user know that there was an error with the variable.
					try {
						FileInputStream fis = new FileInputStream( s.trim() );
						
						pdfs.add(fis);
					} catch (Exception e) {
						System.err.println("Error in append list");
						e.printStackTrace();
					}
				}
			}
		}
		
		//check that we assigned a parameter for appending PDF's
		Object appendPDF = context.getGlobalVariable("appendPDF");
		
		if (appendPDF != null)
		{
			ArrayList<String> pdfList = (ArrayList<String>) appendPDF;
			
			for (String fileName : pdfList)
			{
				//If there is an exception creating the input stream, don't stop execution.
				//Just graceffully let the user know that there was an error with the variable.
				try {
					FileInputStream fis = new FileInputStream( fileName.trim() );
					
					pdfs.add(fis);
				} catch (Exception e) {
					System.err.println("Error in append list");
					e.printStackTrace();
				}
			}
		}
		
	    concatPDFs(pdfs, true);
	    
	    //End Modification
			    
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
	
	/**
	 * Patched PDF to Combine PDF Files
	 * 
	 * Given a list of PDF Files
	 * When a user wants to append PDf files to a PDF emitter output
	 * Then Append the PDF files to the output stream or output file
	 * 
	 * @param streamOfPDFFiles
	 * @param paginate
	 */
	 public void concatPDFs(List<InputStream> streamOfPDFFiles, boolean paginate) {

		    Document document = doc;
		    try {
		      List<InputStream> pdfs = streamOfPDFFiles;
		      List<PdfReader> readers = new ArrayList<PdfReader>();
		      int totalPages = 0;
		      Iterator<InputStream> iteratorPDFs = pdfs.iterator();

		      // Create Readers for the pdfs.
		      while (iteratorPDFs.hasNext()) {
		        InputStream pdf = iteratorPDFs.next();
		        PdfReader pdfReader = new PdfReader(pdf);
		        readers.add(pdfReader);
		        
		        int n = pdfReader.getNumberOfPages();
		              
		        totalPages += n;
		      }
		      // Create a writer for the outputstream
		      PdfWriter writer = this.writer;

		      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		      PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
		      
		      PdfImportedPage page;
		      int currentPageNumber = 0;
		      int pageOfCurrentReaderPDF = 0;
		      Iterator<PdfReader> iteratorPDFReader = readers.iterator();

		      // Loop through the PDF files and add to the output.
		      while (iteratorPDFReader.hasNext()) {
		        PdfReader pdfReader = iteratorPDFReader.next();

		        // Create a new page in the target for each source page.
		        while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
		          pageOfCurrentReaderPDF++;
		          currentPageNumber++;
		          
		          //note: page size has to be set before new page created. current page is already initialized
		          Rectangle sourcePageSize = pdfReader.getPageSize(pageOfCurrentReaderPDF);
		          document.setPageSize(sourcePageSize);
		        	
		          document.newPage();
		                    
		          page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
		                    
		          cb.addTemplate(page, 0, 0);

		          // Code for pagination.
		          if (paginate) {
		            cb.beginText();
		            cb.setFontAndSize(bf, 9);
		            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
		            cb.endText();
		          }
		        }
		        pageOfCurrentReaderPDF = 0;
		      }
		      //outputStream.flush();
		      //document.close();
		      //outputStream.close();
		    } catch (Exception e) {
		      e.printStackTrace();  
		    }
		  }	
}
