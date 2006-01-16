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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.PDFRenderContext;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.util.PropertyUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * used to output the area tree in PDF file.
 * 
 * the objects passed in must be formated. Any unformated object
 * will be discarded.
 * 
 * @version $Revision$ $Date$
 */
public class PDFEmitter implements IAreaVisitor
{
	/**
	 * the default output pdf file name
	 */
	public static final String REPORT_FILE = "Report.pdf"; //$NON-NLS-1$
	
	/**
	 * the default image folder
	 */
	public static final String IMAGE_FOLDER = "image"; //$NON-NLS-1$
	
	/**
	 * the output stream
	 */
	private OutputStream output = null;
	
	/**
	 * the emitter configturation
	 */
	//private PDFEmitterConfig ec = null;
	
	/**
	 * The ratio of layout measure to iText measure
	 */
	private static final int layout2PdfRatio = 1000;
	
	/**
	 * the pdf Document object created by iText
	 */
	private Document doc = null;

	/**
	 * the Pdf Writer
	 */
	private PdfWriter writer = null;

	/**
	 * contentByte layer for pdf;
	 * cb covers cbUnder.
	 */
	private PdfContentByte cb, cbUnder = null;

	/**
	 * the height and width of the current pdf page.
	 */
	private float pageHeight, pageWidth = 0f;
	
	/**
	 * the logger logging the error, debug, warning messages.
	 */
	protected static Logger logger = Logger.getLogger( PDFEmitter.class.getName( ) );
	
	protected IReportContent report;
	
	protected IReportRunnable reportRunnable;
	
	protected ReportDesignHandle reportDesign;
	
	protected PDFRenderContext context;
	
	/**
	 * get the output format. here it returns "pdf".
	 */
	public String getOutputFormat()
	{
		return RenderOptionBase.OUTPUT_FORMAT_PDF;
	}

	/**
	 * initialize the pdfEmitter
	 */
	public void initialize(IEmitterServices services)
	{
		//get the output file name from RenderOptionBase.OUTPUT_FILE_NAME.
		//It has the top preference.
		this.reportRunnable = services.getReportRunnable();
		if (reportRunnable != null)
		{
			reportDesign = (ReportDesignHandle)reportRunnable.getDesignHandle();
		}
		Object renderContext = services.getRenderContext();
		if(renderContext!=null && renderContext instanceof Map)
		{
			Object con = ((Map)renderContext).get(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT);
			if(con instanceof PDFRenderContext)
			{
				this.context = (PDFRenderContext)con;
			}
		}
		Object fd = services.getOption( RenderOptionBase.OUTPUT_FILE_NAME );
		File file = null;
		try
		{
			if( fd != null )
			{
				file = new File(fd.toString());
				output = new FileOutputStream( file );
			}
		}
		catch( FileNotFoundException fnfe )
		{
			logger.log( Level.WARNING, fnfe.getMessage( ), fnfe );
		}
		
		//while failed to get the outputStream from the output file name specified
		//from RenderOptionBase.OUTPUT_FILE_NAME, use RenderOptionBase.OUTPUT_STREAM
		//to build the outputStream
		if( output == null )
		{
			Object value = services.getOption( RenderOptionBase.OUTPUT_STREAM );
			if( value != null && value instanceof OutputStream )
			{
				output = ( OutputStream ) value;
			}
			
			//if the RenderOptionBase.OUTPUT_STREAM is NOT set, build the outputStream from the
			//REPORT_FILE param defined in this file.
			else
			{
				try
				{
					file = new File( REPORT_FILE );
					output = new FileOutputStream( file );
				}
				catch ( FileNotFoundException e )
				{
					logger.log(Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
		
		//register the font files.
		FontHandler.prepareFonts();
	}
	
	/**
	 * new a document and create a PdfWriter
	 */
	public void start(IReportContent rc)
	{
		this.report = rc;
		doc = new Document();
		try
		{
			writer = PdfWriter.getInstance( doc, new BufferedOutputStream(output) );
		}
		catch( DocumentException de )
		{
			logger.log( Level.SEVERE, de.getMessage( ), de );
		}
	}

	/**
	 * close the document
	 */
	public void end(IReportContent rc)
	{
		//Before closing the document, we need to create TOC.
		TOCHandler tocHandler = new TOCHandler( rc.getTOC() );
		TOCNode tocRoot = tocHandler.getTOCRoot();
		if (true == tocRoot.getChildren().isEmpty())
		{
			writer.setViewerPreferences(PdfWriter.PageModeUseNone);
		}
		else
		{
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			PdfOutline root = cb.getRootOutline();
			tocHandler.createTOC(tocRoot, root);
		}
		doc.close();
	}

	public void visitText(ITextArea textArea)
	{
		drawText(textArea);
	}

	public void visitImage(IImageArea imageArea)
	{
		drawImage(imageArea);
	}

	/**
	 * If the container is a PageArea, this method news a pdf page.
	 * If the container is the other containerAreas, 
	 * such as TableArea, or just the border of textArea/imageArea
	 * draw the border and background of the container
	 * @param container				the ContainerArea specified from layout
	 */
	public void startContainer(IContainerArea container)
	{
		if (container instanceof PageArea)
		{
			newPage(container);
		}
		else
		{
			drawContainer(container);
		}
	}

	public void endContainer(IContainerArea containerArea)
	{
		// nothing to do with endContainer, because layout sends
		// the absolute position relative to the page now.
	}

	/**
	 * create a new PDF page
	 * @param page		the PageArea specified from layout
	 */
	protected void newPage( IContainerArea page )
	{
		pageHeight = pdfMeasure( page.getHeight() );
		pageWidth = pdfMeasure( page.getWidth() );
		
		//set the pagesize of the new page
        Rectangle pageSize = new Rectangle(pageWidth, pageHeight);

        //new a pdf page, get its contentByte and contentByteUnder
		try
		{
			doc.setPageSize(pageSize);
		    if ( !doc.isOpen() )
		    {
		    	doc.open();
				cb = writer.getDirectContent();
				cbUnder = writer.getDirectContentUnder();
		    }
			doc.newPage();
			//Add an invisible content to the document to make sure that a new page is created. 
			doc.add(Chunk.NEWLINE);
		}
		catch( DocumentException de )
		{
			logger.log( Level.SEVERE, de.getMessage( ), de );
		}
		
		//draw background color for the container, if the backgound color is NOT set, draw nothing.
		Color bc = PropertyUtil.getColor(page.getStyle().getProperty(StyleConstants.STYLE_BACKGROUND_COLOR));
		drawBackgroundColor( bc, 0, pageHeight, pageWidth, pageHeight );
		
		//draw background image for the new page. if the background image is NOT set, draw nothing.
		String bi = PropertyUtil.getBackgroundImage(
        		page.getStyle().getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE));
		drawBackgroundImage( bi, 0, pageHeight, pageWidth, pageHeight, 
				PropertyUtil.getPercentageValue(page.getStyle().getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_X)),
				PropertyUtil.getPercentageValue(page.getStyle().getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_Y)),
				page.getStyle().getBackgroundRepeat());
	}

	private class BorderInfo
	{
		public static final int TOP_BORDER = 0;
		public static final int RIGHT_BORDER = 1;
		public static final int BOTTOM_BORDER = 2;
		public static final int LEFT_BORDER = 3;
		public int startX, startY, endX, endY;
		public int borderWidth;
		public Color borderColor;
		public String borderStyle;
		public int borderType;
		public BorderInfo(int startX, int startY, int endX, int endY, 
				int borderWidth, Color borderColor, String borderStyle, int borderType)
		{
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
			this.borderWidth = borderWidth;
			this.borderColor = borderColor;
			this.borderStyle = borderStyle;
			this.borderType = borderType;
		}
	}
	/**
	 * draw a container's border, and its background color/image if there is any.
	 * @param container			the containerArea whose border and background need to be drawed
	 */
	protected void drawContainer( IContainerArea container )
	{
		//get the style of the container
		IStyle style = container.getStyle();
		if ( null == style )
		{
			return;
		}
		
		//the container's start position (the left top corner of the container)
		float startX = layoutPointX2PDF (container.getAbsoluteX());
		float startY = layoutPointY2PDF (container.getAbsoluteY());

		//the dimension of the container
		float width = pdfMeasure(container.getWidth());
		float height = pdfMeasure(container.getHeight());
		
		//draw background color for the container, if the backgound color is NOT set, draw nothing.
		Color bc = PropertyUtil.getColor(style.getProperty(StyleConstants.STYLE_BACKGROUND_COLOR));
		drawBackgroundColor( bc, startX, startY, width, height );
		
		//draw background image for the container. if the background image is NOT set, draw nothing.
		String bi = PropertyUtil.getBackgroundImage(style.getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE));
		drawBackgroundImage( bi, startX, startY, width, height,
				PropertyUtil.getPercentageValue(style.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_X)),
				PropertyUtil.getPercentageValue(style.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_Y)),
				style.getBackgroundRepeat() );
		
		//the width of each border
		int borderTopWidth = PropertyUtil.getDimensionValue(
				style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH));
		int borderLeftWidth = PropertyUtil.getDimensionValue(
				style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH));
		int borderBottomWidth = PropertyUtil.getDimensionValue(
				style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
		int borderRightWidth = PropertyUtil.getDimensionValue(
				style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
		
		//the color of each border
		Color borderTopColor = PropertyUtil.getColor(
				style.getProperty(StyleConstants.STYLE_BORDER_TOP_COLOR));
		Color borderRightColor = PropertyUtil.getColor(
				style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR));
		Color borderBottomColor = PropertyUtil.getColor(
				style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR));
		Color borderLeftColor = PropertyUtil.getColor(
				style.getProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR));
		
		//cache the border info
		BorderInfo[] borders = new BorderInfo[4];
		borders[BorderInfo.TOP_BORDER] = new BorderInfo(
				container.getAbsoluteX(), container.getAbsoluteY()+ borderTopWidth/2,
				container.getAbsoluteX()+container.getWidth(), container.getAbsoluteY()+ borderTopWidth/2,
				borderTopWidth, borderTopColor, container.getStyle().getBorderTopStyle(), BorderInfo.TOP_BORDER);
		borders[BorderInfo.RIGHT_BORDER] = new BorderInfo(
				container.getAbsoluteX()+container.getWidth()-borderRightWidth/2, container.getAbsoluteY(), 
				container.getAbsoluteX()+container.getWidth()-borderRightWidth/2, container.getAbsoluteY()+ container.getHeight(),       
				borderRightWidth, borderRightColor, container.getStyle().getBorderRightStyle(), BorderInfo.RIGHT_BORDER);
		borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo(
				container.getAbsoluteX(), container.getAbsoluteY()+container.getHeight()-borderBottomWidth/2, 
				container.getAbsoluteX()+container.getWidth(), container.getAbsoluteY()+container.getHeight()-borderBottomWidth/2, 
				borderBottomWidth, borderBottomColor, container.getStyle().getBorderBottomStyle(), BorderInfo.BOTTOM_BORDER);
		borders[BorderInfo.LEFT_BORDER] = new BorderInfo(
				container.getAbsoluteX()+borderLeftWidth/2, container.getAbsoluteY(),
				container.getAbsoluteX()+borderLeftWidth/2, container.getAbsoluteY()+ container.getHeight(), 
				borderLeftWidth, borderLeftColor, container.getStyle().getBorderLeftStyle(), BorderInfo.LEFT_BORDER);
		
		// draw the four borders of the container if there are any. Each border is showed as a line.
		drawBorder(borders);
		
		//Check if itself is the destination of a bookmark.
		//if so, make a bookmark; if not, do nothing
		makeBookmark(container);
		//handle hyper-link action
		handleHyperlinkAction(container);
	}
	
	/**
	 * draw a chunk of text at the pdf
	 * @param text				the textArea to be drawed
	 */
	protected void drawText( ITextArea text )
	{	 
		IStyle style = text.getStyle();
		assert style!=null; 
		
	    //style.getFontVariant();     	small-caps or normal
	    //FIXME does NOT support small-caps now

		float fontSize = pdfMeasure( PropertyUtil.getDimensionValue(
	        	style.getProperty(StyleConstants.STYLE_FONT_SIZE)) );
		float characterSpacing = pdfMeasure( PropertyUtil.getDimensionValue(
	        	style.getProperty(StyleConstants.STYLE_LETTER_SPACING)) );
		float wordSpacing = pdfMeasure( PropertyUtil.getDimensionValue(
	        	style.getProperty(StyleConstants.STYLE_WORD_SPACING)) );
		cb.saveState();
		//start drawing the text content
	    cb.beginText();
	        
	    //set the font handler
	    FontHandler fh = new FontHandler((ITextContent)text.getContent());

	    fh.selectFont(text.getText());
	    cb.setFontAndSize(fh.getBaseFont(), fontSize); 
		cb.setCharacterSpacing(characterSpacing);
		cb.setWordSpacing(wordSpacing);
	        
	    cb.setTextMatrix( layoutAreaX2PDF(text.getAbsoluteX()), 
	    		layoutAreaY2PDF(text.getAbsoluteY(), (int)(fh.getBaseline()*layout2PdfRatio)));
	    Color color = PropertyUtil.getColor(style.getProperty(StyleConstants.STYLE_COLOR));
	    if (null != color)
	    {
	    	cb.setColorFill(color);
	    }
		cb.showText(text.getText());
		cb.endText();
		cb.restoreState();
	        
		//draw the overline,throughline or underline for the text if it has any. 
	    //set 1/20 of fontSize as the line width
	    int lineWidth = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_FONT_SIZE))/20;
		if ("line-through".equalsIgnoreCase(style.getTextLineThrough())) //$NON-NLS-1$
	    {
	    	drawLine( layoutPointX2PDF(text.getAbsoluteX()), 
	        		layoutPointY2PDF(text.getAbsoluteY()+text.getHeight()/2), 
	        		layoutPointX2PDF(text.getAbsoluteX()+text.getWidth()), 
	        		layoutPointY2PDF(text.getAbsoluteY()+text.getHeight()/2), 
	        		pdfMeasure(lineWidth), 
	        		PropertyUtil.getColor(style.getProperty(StyleConstants.STYLE_COLOR)),  
	        		"solid", cb ); //$NON-NLS-1$
	    }
	    if ("overline".equalsIgnoreCase(style.getTextOverline())) //$NON-NLS-1$
	    {	
	        drawLine( layoutPointX2PDF(text.getAbsoluteX()), 
	        		layoutPointY2PDF(text.getAbsoluteY()+2*lineWidth),
	        		layoutPointX2PDF(text.getAbsoluteX()+text.getWidth()), 
	        		layoutPointY2PDF(text.getAbsoluteY()+lineWidth),
	        		pdfMeasure(lineWidth), 
	        		PropertyUtil.getColor(style.getProperty(StyleConstants.STYLE_COLOR)),
	        		"solid", cb ); //$NON-NLS-1$
	    }
		if ("underline".equalsIgnoreCase(style.getTextUnderline())) //$NON-NLS-1$
	    {
	        drawLine(layoutPointX2PDF(text.getAbsoluteX()), 
	        		layoutPointY2PDF(text.getAbsoluteY()+text.getHeight()-lineWidth/2),
	        		layoutPointX2PDF(text.getAbsoluteX()+text.getWidth()), 
	        		layoutPointY2PDF(text.getAbsoluteY()+text.getHeight()-lineWidth/2),
	        		pdfMeasure(lineWidth), 
	        		PropertyUtil.getColor(style.getProperty(StyleConstants.STYLE_COLOR)),
	        		"solid", cb ); //$NON-NLS-1$
	    } 
	 
		//Check if itself is the destination of a bookmark.
		//if so, make a bookmark; if not, do nothing
		makeBookmark(text);
		//handle hyper-link action
		handleHyperlinkAction(text);
	}

	/**
	 * drawImage at the contentByte
	 * @param image		the ImageArea specified from the layout
	 */
	protected void drawImage( IImageArea image )
	{	
		Image img = null;
		cb.saveState();
		try
		{
			//lookup the source type of the image area
			switch (((IImageContent) image.getContent()).getImageSource())
			{
			case IImageContent.IMAGE_FILE:
			case IImageContent.IMAGE_URI:
				if (null == ((IImageContent) image.getContent()).getURI())
					return;
				img = Image.getInstance(((IImageContent) image.getContent()).getURI());
				break;
			case IImageContent.IMAGE_NAME:
			case IImageContent.IMAGE_EXPRESSION:
				if (null == ((IImageContent) image.getContent()).getData())
					return;
				img = Image.getInstance(((IImageContent) image.getContent())
						.getData());
			}
			
			
			//img.setDpi(5*img.getDpiX(),5*img.getDpiY());
			// add the image to the given contentByte
			cb.addImage(img, 
					pdfMeasure(image.getWidth()), 0f, 0f, pdfMeasure(image.getHeight()),
					layoutAreaX2PDF(image.getAbsoluteX()), 
					layoutAreaY2PDF(image.getAbsoluteY(), image.getHeight()));
		} catch (BadElementException bee)
		{
			logger.log( Level.WARNING, bee.getMessage( ), bee );
		} catch (IOException ioe)
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		} catch (DocumentException de)
		{
			logger.log( Level.WARNING, de.getMessage( ), de );
		}
		catch (Throwable t)
		{
			logger.log( Level.WARNING, t.getMessage( ), t );
		}
		
		cb.restoreState();
		
		//Check if itself is the destination of a bookmark.
		//if so, make a bookmark; if not, do nothing
		makeBookmark(image);
		
		//handle hyper-link action
		handleHyperlinkAction(image);
	}
	
	/**
	 * draw the borders of a container.
	 * @param borders		the border info
	 */
	private void drawBorder(BorderInfo[] borders)
	{
		//double>solid>dashed>dotted>none
		ArrayList dbl = null;
		ArrayList solid = null;
		ArrayList dashed = null;
		ArrayList dotted = null;
		
 		for(int i=0; i<borders.length; i++)
		{
			if ( "double".equalsIgnoreCase(borders[i].borderStyle) ) //$NON-NLS-1$
			{
				if (null == dbl)
				{
					dbl = new ArrayList();
				}
				dbl.add(borders[i]);
			}
			if ( "solid".equalsIgnoreCase(borders[i].borderStyle) ) //$NON-NLS-1$
			{
				if (null == solid)
				{
					solid = new ArrayList();
				}
				solid.add(borders[i]);
			}
			if ( "dashed".equalsIgnoreCase(borders[i].borderStyle) ) //$NON-NLS-1$
			{
				if (null == dashed)
				{
					dashed = new ArrayList();
				}
				dashed.add(borders[i]);
			}
			if ( "dotted".equalsIgnoreCase(borders[i].borderStyle) ) //$NON-NLS-1$
			{
				if (null == dotted)
				{
					dotted = new ArrayList();
				}
				dotted.add(borders[i]);
			}
		}
 		if ( null != dotted )
 		{
 			for (Iterator it=dotted.iterator(); it.hasNext();)
 			{
 				BorderInfo bi = (BorderInfo)it.next();
 				drawLine( layoutPointX2PDF( bi.startX ), layoutPointY2PDF( bi.startY ), 
 						layoutPointX2PDF( bi.endX ), layoutPointY2PDF( bi.endY ), 
 						pdfMeasure(bi.borderWidth), bi.borderColor, "dotted", cb ); //$NON-NLS-1$
 			}
 		}
 		if ( null != dashed )
 		{
 			for (Iterator it=dashed.iterator(); it.hasNext();)
 			{
 				BorderInfo bi = (BorderInfo)it.next();
 				drawLine( layoutPointX2PDF( bi.startX ), layoutPointY2PDF( bi.startY ), 
 						layoutPointX2PDF( bi.endX ), layoutPointY2PDF( bi.endY ), 
 						pdfMeasure(bi.borderWidth), bi.borderColor, "dashed", cb ); //$NON-NLS-1$
 			}
 		}
 		if ( null != solid )
 		{
 			for (Iterator it=solid.iterator(); it.hasNext();)
 			{
 				BorderInfo bi = (BorderInfo)it.next();
 				drawLine( layoutPointX2PDF( bi.startX ), layoutPointY2PDF( bi.startY ), 
 						layoutPointX2PDF( bi.endX ), layoutPointY2PDF( bi.endY ), 
 						pdfMeasure(bi.borderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 			}
 		}
 		if ( null != dbl )
 		{
 			for (Iterator it=dbl.iterator(); it.hasNext();)
 			{
 				BorderInfo bi = (BorderInfo)it.next();
 				int outerBorderWidth=bi.borderWidth/3, innerBorderWidth=bi.borderWidth/3;
 				
 				switch (bi.borderType)
 				{
 				case BorderInfo.TOP_BORDER:
 					drawLine( layoutPointX2PDF( bi.startX ), layoutPointY2PDF( bi.startY-bi.borderWidth/2+outerBorderWidth/2 ), 
 	 						layoutPointX2PDF( bi.endX ), layoutPointY2PDF( bi.endY-bi.borderWidth/2+outerBorderWidth/2 ), 
 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( bi.startX+2*borders[BorderInfo.LEFT_BORDER].borderWidth/3 ), 
 	 						layoutPointY2PDF( bi.startY+bi.borderWidth/2-innerBorderWidth/2 ), 
 	 						layoutPointX2PDF( bi.endX-2*borders[BorderInfo.RIGHT_BORDER].borderWidth/3 ), 
 	 						layoutPointY2PDF( bi.endY+bi.borderWidth/2-innerBorderWidth/2 ), 
 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				break;
 				case BorderInfo.RIGHT_BORDER:
 					drawLine( layoutPointX2PDF( bi.startX+bi.borderWidth/2-outerBorderWidth/2 ), layoutPointY2PDF( bi.startY ), 
 	 						layoutPointX2PDF( bi.endX+bi.borderWidth/2-outerBorderWidth/2 ), layoutPointY2PDF( bi.endY ), 
 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( bi.startX-bi.borderWidth/2+innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( bi.startY+2*borders[BorderInfo.TOP_BORDER].borderWidth/3 ), 
 	 						layoutPointX2PDF( bi.endX-bi.borderWidth/2+innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( bi.endY-2*borders[BorderInfo.BOTTOM_BORDER].borderWidth/3 ), 
 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				break;
 				case BorderInfo.BOTTOM_BORDER:
 					drawLine( layoutPointX2PDF( bi.startX ), layoutPointY2PDF( bi.startY+bi.borderWidth/2-outerBorderWidth/2 ), 
 	 						layoutPointX2PDF( bi.endX ), layoutPointY2PDF( bi.endY+bi.borderWidth/2-outerBorderWidth/2 ), 
 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( bi.startX+2*borders[BorderInfo.LEFT_BORDER].borderWidth/3 ), 
 	 						layoutPointY2PDF( bi.startY-bi.borderWidth/2+innerBorderWidth/2 ), 
 	 						layoutPointX2PDF( bi.endX-2*borders[BorderInfo.RIGHT_BORDER].borderWidth/3 ), 
 	 						layoutPointY2PDF( bi.endY-bi.borderWidth/2+innerBorderWidth/2 ), 
 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				break;
 				case BorderInfo.LEFT_BORDER:
 					drawLine( layoutPointX2PDF( bi.startX-bi.borderWidth/2+outerBorderWidth/2 ), layoutPointY2PDF( bi.startY ), 
 	 						layoutPointX2PDF( bi.endX-bi.borderWidth/2+outerBorderWidth/2 ), layoutPointY2PDF( bi.endY ), 
 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( bi.startX+bi.borderWidth/2-innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( bi.startY+2*borders[BorderInfo.TOP_BORDER].borderWidth/3 ), 
 	 						layoutPointX2PDF( bi.endX+bi.borderWidth/2-innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( bi.endY-2*borders[BorderInfo.BOTTOM_BORDER].borderWidth/3 ), 
 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				break;
 				}
 			}
 		}
	}
	
	/**
	 * draw a line from the start position to the end position with the given
	 * linewidth, color, and style at the given pdf layer.
	 * 
	 * @param startX			the start X coordinate of the line
	 * @param startY 			the start Y coordinate of the line
	 * @param endX 				the end X coordinate of the line
	 * @param endY 				the end Y coordinate of the line
	 * @param width 			the lineWidth
	 * @param color 			the color of the line
	 * @param lineStyle 		the given line style
	 * @param contentByte 		the given pdf layer
	 */
	private void drawLine(float startX, float startY, float endX, float endY, float width, 
			Color color, String lineStyle, PdfContentByte contentByte )
	{
		//if the border does NOT have color or the linewidth of the border is zero 
		//or the lineStyle is "none", just return.
		if (null == color || 0f == width || "none".equalsIgnoreCase(lineStyle)) //$NON-NLS-1$
		{
			return;
		}
		contentByte.saveState();
		if ("solid".equalsIgnoreCase(lineStyle)) //$NON-NLS-1$
		{
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		}
		if ("dashed".equalsIgnoreCase(lineStyle)) //$NON-NLS-1$
		{
			contentByte.setLineDash(3*width, 2*width, 0f);
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		}
		else if ("dotted".equalsIgnoreCase(lineStyle)) //$NON-NLS-1$
		{
			contentByte.setLineDash(width, width, 0f);
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		}
		else if ("double".equalsIgnoreCase(lineStyle)) //$NON-NLS-1$
		{
			return;
		}
		//the other line styles, e.g. 'ridge', 'outset', 'groove', 'inset' is NOT supported now. 
		//We look it as the default line style -- 'solid'
		else
		{
			drawRawLine(startX, startY, endX, endY, width, color, contentByte);
		}
		contentByte.restoreState();
	}
	

	/**
	 * draw a line with the line-style specified in advance.from the start position to 
	 * the end position with the given linewidth, color, and style at the given pdf layer.
	 * If the line-style is NOT set before invoking this method, 
	 * "solid" will be used as the default line-style. 
	 * 
	 * @param startX			the start X coordinate of the line
	 * @param startY 			the start Y coordinate of the line
	 * @param endX 				the end X coordinate of the line
	 * @param endY 				the end Y coordinate of the line
	 * @param width 			the lineWidth
	 * @param color 			the color of the line
	 * @param contentByte 		the given pdf layer
	 */
	private void drawRawLine(float startX, float startY, float endX, float endY, float width, 
			Color color, PdfContentByte contentByte )
	{
		contentByte.moveTo(startX, startY);
		contentByte.setLineWidth(width);
		contentByte.lineTo(endX, endY);        
		contentByte.setColorStroke(color);
		contentByte.stroke();
	}
	
	/**
	 * draw the background color at the contentByteUnder of the pdf
	 * @param color			the color to be drawed
	 * @param x				the start X coordinate
	 * @param y				the start Y coordinate
	 * @param width			the width of the background dimension
	 * @param height 		the height of the background dimension
	 */
	private void drawBackgroundColor(Color color, float x, float y, float width, float height)
	{
		if (null == color)
		{
			return;
		}
		cbUnder.saveState();
		cbUnder.setColorFill(color);
		cbUnder.rectangle(x, y-height, width, height);
		cbUnder.fill();
		cbUnder.restoreState();
	}
	
	/**
	 * draw the backgound image at the contentByteUnder of the pdf with the given offset
	 * @param imageURI		the URI referring the image
	 * @param x				the start X coordinate at the pdf where the image is positioned
	 * @param y				the start Y coordinate at the pdf where the image is positioned
	 * @param width			the width of the background dimension
	 * @param height		the height of the background dimension
	 * @param positionX		the offset X percentage relating to start X
	 * @param positionY		the offset Y percentage relating to start Y
	 * @param repeat		the background-repeat property
	 */
	private void drawBackgroundImage(String imageURI, float x, float y, 
			float width, float height, float positionX, float positionY, String repeat)
	{
		//the image URI is empty, ignore it.
		if ( null == imageURI )
		{
			return;
		}
		
		String id = imageURI;
		if (reportDesign != null)
		{
			URL url = reportDesign.findResource(imageURI, IResourceLocator.IMAGE);
			if (url != null)
			{
				id = url.toExternalForm();
			}
		}
		
		if(id==null || "".equals(id)) //$NON-NLS-1$
		{
			return;
		}
		
		//the background-repeat property is empty, use "repeat".
		if ( null == repeat)
		{
			repeat = "repeat"; //$NON-NLS-1$
		}
		cbUnder.saveState();
		Image img = null;
		try
		{
			img = Image.getInstance(id);
			float absPosX = (width - img.scaledWidth()) * positionX;
			float absPosY = (height - img.scaledHeight()) * positionY;
			//"no-repeat":
			if ("no-repeat".equalsIgnoreCase(repeat)) //$NON-NLS-1$
			{
				if (height-absPosY>img.scaledHeight())
				{
					PdfTemplate templateWhole = cbUnder.createTemplate(
							width-absPosX>img.scaledWidth() ? img.scaledWidth() : width-absPosX, img.scaledHeight());
					templateWhole.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 0, 0);
					cbUnder.addTemplate(templateWhole, x+absPosX, y-absPosY-img.scaledHeight());
				}
				else
				{
					PdfTemplate templateWhole = cbUnder.createTemplate(
							width-absPosX>img.scaledWidth() ? img.scaledWidth() : width-absPosX, height-positionY);
					templateWhole.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 
							0, -img.scaledHeight()+height-absPosY);
					cbUnder.addTemplate(templateWhole, x+absPosX, y-absPosY-img.scaledHeight());
				}
			}
			//"repeat-x":
			else if ("repeat-x".equalsIgnoreCase(repeat)) //$NON-NLS-1$
			{
				float remainX = width;
				PdfTemplate template = null;
				//If the width of the container is smaller than the scaled image width,
				//the repeat will never happen. So it is not necessary to build a 
				//template for futher usage.
				if (width > img.scaledWidth())
				{
					if (height-absPosY > img.scaledHeight())
					{
						template = cbUnder.createTemplate(img.scaledWidth(), img.scaledHeight());
						template.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 0, 0);
					}
					else
					{
						template = cbUnder.createTemplate(img.scaledWidth(), height);
						template.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 
								0, -img.scaledHeight()+height);
					}
				}
				while ( remainX > 0 )
				{
					if(remainX<img.scaledWidth())
					{
						
						if (height-absPosY>img.scaledHeight())
						{
							PdfTemplate templateX = cbUnder.createTemplate(remainX, img.scaledHeight());
							templateX.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 0, 0);
							cbUnder.addTemplate(templateX, x+width-remainX, y-absPosY-img.scaledHeight());
						}
						else
						{
							PdfTemplate templateX = cbUnder.createTemplate(remainX, height);
							templateX.addImage(img, img.scaledWidth(), 0, 0, 
									img.scaledHeight(), 0, -img.scaledHeight()+height-absPosY);
							cbUnder.addTemplate(templateX, x+width-remainX, y-absPosY-height);
						}
						remainX=0;
					}
					else
					{
						if (height-absPosY>img.scaledHeight())
							cbUnder.addTemplate(template, x+width-remainX, y-absPosY-img.scaledHeight());
						else
							cbUnder.addTemplate(template, x+width-remainX, y-absPosY-height);
						remainX-=img.scaledWidth();
					}
				}
			}
			//"repeat-y":
			else if ("repeat-y".equalsIgnoreCase(repeat)) //$NON-NLS-1$
			{
				float remainY = height;
				//If the height of the container is smaller than the scaled image height,
				//the repeat will never happen. So it is not necessary to build a 
				//template for futher usage.
				PdfTemplate template = null;
				if (height > img.scaledHeight())
				{
					template = cbUnder.createTemplate(
							width-absPosX>img.scaledWidth() ? img.scaledWidth() : width-absPosX, img.scaledHeight());
					template.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 0, 0);
				}
				while ( remainY > 0 )
				{
					if(remainY<img.scaledHeight())
					{
						PdfTemplate templateY = cbUnder.createTemplate(
								width-absPosX>img.scaledWidth() ? img.scaledWidth() : width-absPosX, remainY);
						templateY.addImage(img, 
								width>img.scaledWidth() ? img.scaledWidth() : width-absPosX, 0, 0, img.scaledHeight(), 
								0, -(img.scaledHeight()-remainY));
						cbUnder.addTemplate(templateY, x+absPosX, y-height);
						remainY=0;
					}
					else
					{
						cbUnder.addTemplate(template, x+absPosX, y-height+remainY-img.scaledHeight());
						remainY-=img.scaledHeight();
					}
				}
			}
			//"repeat":
			else if ("repeat".equalsIgnoreCase(repeat)) //$NON-NLS-1$
			{	
				float remainX = width;
				float remainY = height;
				PdfTemplate template = null;
				//If the width of the container is smaller than the scaled image width,
				//the repeat will never happen. So it is not necessary to build a 
				//template for futher usage.
				if (width > img.scaledWidth() && height > img.scaledHeight())
				{
					template = cbUnder.createTemplate(img.scaledWidth(), img.scaledHeight());
					template.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 0, 0);
				}
				
				while (remainY > 0)
				{
					remainX = width;
					//the bottom line
					if (remainY < img.scaledHeight())
					{
						while (remainX > 0)
						{
							// the right-bottom one
							if (remainX < img.scaledWidth())
							{
								PdfTemplate templateXY = cbUnder.createTemplate(remainX, remainY);
								templateXY.addImage(img,
										img.scaledWidth(), 0, 0, img.scaledHeight(), 
										0, -img.scaledHeight()+remainY);
								cbUnder.addTemplate(templateXY, x+width-remainX, y-height);
								remainX = 0;
							} else
							// non-right bottom line
							{
								PdfTemplate templateY = cbUnder.createTemplate(img.scaledWidth(),remainY);
								templateY.addImage(img,
										img.scaledWidth(), 0, 0, img.scaledHeight(),
										0, -img.scaledHeight()+remainY);
								cbUnder.addTemplate(templateY, x+width-remainX, y-height);
								remainX -= img.scaledWidth();
							}
						}
						remainY = 0;
					}
					else
					//non-bottom lines
					{
						while ( remainX > 0 )
						{
							//the right ones
							if(remainX < img.scaledWidth())
							{
								PdfTemplate templateX = cbUnder.createTemplate(remainX, img.scaledHeight());
								templateX.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), 0, 0);
								cbUnder.addTemplate(templateX, x+width-remainX, y-height+remainY-img.scaledHeight());
								remainX=0;
							}
							else
							{
								cbUnder.addTemplate(template, x+width-remainX, y-height+remainY-img.scaledHeight());
								remainX-=img.scaledWidth();
							}
						}
						remainY -= img.scaledHeight();
					}
				}
			}
		} catch (IOException ioe)
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		} catch (BadElementException bee)
		{
			logger.log( Level.WARNING, bee.getMessage( ), bee );
		} catch (DocumentException de)
		{
			logger.log( Level.WARNING, de.getMessage( ), de );
		} catch (RuntimeException re)
		{
			logger.log( Level.WARNING, re.getMessage( ), re );
		}
		cbUnder.restoreState();
	}
	
	/**
	 * convert the layout measure to PDF, the measure of layout is 1000 times
	 * larger than that of PDF
	 * 
	 * @param layoutMeasure
	 *            the measure computed in layout manager
	 * @return the measure in PDF
	 */
	private float pdfMeasure( int layoutMeasure )
	{
		return layoutMeasure/(float)layout2PdfRatio;
	}

	/**
	 * convert the X coordinate of a point from layout to X coordinate in PDF
	 * @param layoutX 		the X coordinate specified from layout
	 * @return				the PDF X coordinate
	 */
	private float layoutPointX2PDF(int layoutX)
	{
		return pdfMeasure(layoutX);
	}
	
	/**
	 * convert the Y coordinate of a point from layout to Y coordinate in PDF
	 * @param layoutY 		the Y coordinate specified from layout
	 * @return				the PDF Y coordinate
	 */
	private float layoutPointY2PDF (int layoutY)
	{
		return pageHeight - pdfMeasure(layoutY);
	}
	
	/**
	 * convert the left X coordinate of an Area from layout 
	 * to the left X coordinate in PDF
	 * @param layoutX 		the X coordinate specified from layout
	 * @return				the PDF X coordinate
	 */
	private float layoutAreaX2PDF(int layoutX)
	{
		return pdfMeasure(layoutX);
	}
	
	/**
	 * convert the top Y coordinate of an Area from layout 
	 * to the bottom Y coordinate in PDF
	 * @param layoutY 		the Y coordinate specified from layout
	 * @param areaHeight	the height of the area whose coordinate need to be converted.
	 * 						To text area, the height is from the top of the text to
	 * 						the text's baseline.
	 * @return				the PDF Y coordinate
	 */
	private float layoutAreaY2PDF (int layoutY, int areaHeight)
	{
		return pageHeight - pdfMeasure(layoutY) - pdfMeasure(areaHeight);
	}
    
	/**
	 * Set current area as a bookmark. 
	 * if current area does NOT contain any bookmark info,
	 * this method does nothing.
	 * 
	 * @param area			the area which may need to be marked. 
	 */
	private void makeBookmark(IArea area) 
	{
		IContent content = area.getContent();
		if( null != content )
		{
			String bookmark = content.getBookmark();
			if (null != bookmark)
			{
				cb.localDestination( bookmark, new PdfDestination(
						PdfDestination.XYZ, -1, layoutPointY2PDF(area.getAbsoluteY()), 0));
			}
			String tocmark = content.getTOC();
			if (null != tocmark)
			{
				cb.localDestination( tocmark, new PdfDestination(
						PdfDestination.XYZ, -1, layoutPointY2PDF(area.getAbsoluteY()), 0));
			}
		}
	}
	
	/**
	 * handle the hyperlink, bookmark and drillthrough
	 * 
	 * @param area			the area which may need to handle the hyperlink action
	 */
	private void handleHyperlinkAction(IArea area)
	{
		IContent content = area.getContent();
		if( null != content )
		{
			IHyperlinkAction hlAction = content.getHyperlinkAction();
			if ( null != hlAction )
			try
			{
				switch (hlAction.getType())
				{
				case IHyperlinkAction.ACTION_BOOKMARK: 
					writer.addAnnotation( new PdfAnnotation( writer,
							layoutPointX2PDF(area.getAbsoluteX()),
							layoutPointY2PDF(area.getAbsoluteY()+area.getHeight()),
							layoutPointX2PDF(area.getAbsoluteX()+area.getWidth()),
							layoutPointY2PDF(area.getAbsoluteY()),
							createPdfAction(
									hlAction.getHyperlink(), 
									hlAction.getBookmark(), 
									hlAction.getTargetWindow(), IHyperlinkAction.ACTION_BOOKMARK)) );
					break;
					
				case IHyperlinkAction.ACTION_DRILLTHROUGH: 
					String baseURL = null;
					
					if(context!=null)
					{
						baseURL = context.getBaseURL();
					}
					StringBuffer link = new StringBuffer( );

					String reportName = hlAction.getReportName( );
					if ( reportName != null && !reportName.equals( "" ) )//$NON-NLS-1$
					{
						String format = hlAction.getFormat();
						if ( "pdf".equalsIgnoreCase( format ) ) //$NON-NLS-1$
			            {
			    			link.append( baseURL.replaceFirst( "frameset", "run" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			            }
						link.append( "?__report=" );	//$NON-NLS-1$
						try
						{
							link.append( URLEncoder.encode( reportName, "UTF-8" ) ); 	//$NON-NLS-1$
						}
						catch ( UnsupportedEncodingException e1 )
						{
							//It should not happen. Does nothing
						}
						if(format !=null && format.length()>0)
						{
							link.append( "&__format=" + format );//$NON-NLS-1$
						}
						//Adds the parameters
						if ( hlAction.getParameterBindings( ) != null )
						{
							Iterator paramsIte = hlAction.getParameterBindings( ).entrySet( ).iterator( );
							while ( paramsIte.hasNext( ) )
							{
								Map.Entry entry = (Map.Entry) paramsIte.next( );
								try
								{
									link.append( "&" + URLEncoder.encode( (String) entry.getKey( ), "UTF-8" ) + "=" + URLEncoder.encode( (String) entry.getValue( ), "UTF-8" ) );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$				
								}
								catch ( UnsupportedEncodingException e )
								{
									//Does nothing
								}
							}
						}
					}
					if ( hlAction.getBookmark() != null )
					{
						link.append( "&__bookmark=" ); //$NON-NLS-1$
						link.append( hlAction.getBookmark() );
					}
					writer.addAnnotation( new PdfAnnotation( writer,
							layoutPointX2PDF(area.getAbsoluteX()),
							layoutPointY2PDF(area.getAbsoluteY()+area.getHeight()),
							layoutPointX2PDF(area.getAbsoluteX()+area.getWidth()),
							layoutPointY2PDF(area.getAbsoluteY()),
		            		createPdfAction(link.toString( ), null, hlAction.getTargetWindow(), IHyperlinkAction.ACTION_DRILLTHROUGH )));
					break;
					
				case IHyperlinkAction.ACTION_HYPERLINK: 
					writer.addAnnotation( new PdfAnnotation( writer,
							layoutPointX2PDF(area.getAbsoluteX()),
							layoutPointY2PDF(area.getAbsoluteY()+area.getHeight()),
							layoutPointX2PDF(area.getAbsoluteX()+area.getWidth()),
							layoutPointY2PDF(area.getAbsoluteY()),
							createPdfAction(hlAction.getHyperlink(), null, hlAction.getTargetWindow(), IHyperlinkAction.ACTION_HYPERLINK)) );
					break;
				}
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}
	
	/**
	 * create a PdfAction
	 * 
	 * @param hyperlink			
	 * @param bookmark
	 * @param target			if target equals "_blank", the target will be opened in a new window,
	 * 							else the target will be opened in the current window.
	 * @return					the created PdfAction.
	 */
	private PdfAction createPdfAction(String hyperlink, String bookmark, String target, int type)
	{
		if ("_blank".equalsIgnoreCase(target)) //$NON-NLS-1$
		//open the target in a new window
		{
			return new PdfAction(hyperlink);
			
		}
		else
		//open the target in current window
		{
			if (type==IHyperlinkAction.ACTION_BOOKMARK)
			{
				return PdfAction.gotoLocalPage(bookmark, false);
			}
			else
			{
				if(type==IHyperlinkAction.ACTION_HYPERLINK)
				{
					return PdfAction.gotoRemotePage(hyperlink, bookmark, false, false);
				}
				else
				{
					return new PdfAction(hyperlink);
				}
			}
		}
	}
}