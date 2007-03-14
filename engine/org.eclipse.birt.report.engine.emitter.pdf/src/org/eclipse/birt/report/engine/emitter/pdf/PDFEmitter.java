package org.eclipse.birt.report.engine.emitter.pdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.print.PrintTranscoder;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.util.ULocale;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfTextArray;
import com.lowagie.text.pdf.PdfWriter;


public class PDFEmitter implements IContentEmitter
{
	protected static Logger logger = Logger.getLogger( PDFEmitter.class.getName( ) );
	
	protected PDFRender render = new PDFRender();
	

	public String getOutputFormat( )
	{
		return render.getOutputFormat( );
	}

	public void initialize( IEmitterServices service )
	{
		render.initialize( service );
		
	}
	
	public void startPage( IPageContent page )
	{
		PageArea pageArea = (PageArea)page.getExtension( IContent.LAYOUT_EXTENSION );
		if(pageArea!=null)
		{
			pageArea.accept( render);
		}
		
	}
	
	public void start( IReportContent report )
	{
		render.start( report );
		
	}

	public void end( IReportContent report )
	{
		render.end( report );
		
	}
	
	public void startAutoText( IAutoTextContent autoText )
	{
		ITextArea totalPage = (ITextArea)autoText.getExtension( IContent.LAYOUT_EXTENSION );
		render.setTotalPage( totalPage );
	}

	public void endCell( ICellContent cell )
	{
		// TODO Auto-generated method stub
		
	}

	public void endContainer( IContainerContent container )
	{
		// TODO Auto-generated method stub
		
	}

	public void endContent( IContent content )
	{
		// TODO Auto-generated method stub
		
	}

	public void endList( IListContent list )
	{
		// TODO Auto-generated method stub
		
	}

	public void endListBand( IListBandContent listBand )
	{
		// TODO Auto-generated method stub
		
	}

	public void endPage( IPageContent page )
	{
		// TODO Auto-generated method stub
		
	}

	public void endRow( IRowContent row )
	{
		// TODO Auto-generated method stub
		
	}

	public void endTable( ITableContent table )
	{
		// TODO Auto-generated method stub
		
	}

	public void endTableBody( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	public void endTableFooter( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	public void endTableHeader( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	

	

	

	public void startCell( ICellContent cell )
	{
		// TODO Auto-generated method stub
		
	}

	public void startContainer( IContainerContent container )
	{
		// TODO Auto-generated method stub
		
	}

	public void startContent( IContent content )
	{
		// TODO Auto-generated method stub
		
	}

	public void startData( IDataContent data )
	{
		// TODO Auto-generated method stub
		
	}

	public void startForeign( IForeignContent foreign )
	{
		// TODO Auto-generated method stub
		
	}

	public void startImage( IImageContent image )
	{
		// TODO Auto-generated method stub
		
	}

	public void startLabel( ILabelContent label )
	{
		// TODO Auto-generated method stub
		
	}

	public void startList( IListContent list )
	{
		// TODO Auto-generated method stub
		
	}

	public void startListBand( IListBandContent listBand )
	{
		// TODO Auto-generated method stub
		
	}

	

	public void startRow( IRowContent row )
	{
		// TODO Auto-generated method stub
		
	}

	public void startTable( ITableContent table )
	{
		// TODO Auto-generated method stub
		
	}

	public void startTableBody( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	public void startTableFooter( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	public void startTableHeader( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	public void startText( ITextContent text )
	{
		// TODO Auto-generated method stub
		
	}

	protected class PDFRender implements IAreaVisitor
	{
		/**
		 * The default output pdf file name
		 */
		public static final String REPORT_FILE = "Report.pdf"; //$NON-NLS-1$
		
		/**
		 * The default image folder
		 */
		public static final String IMAGE_FOLDER = "image"; //$NON-NLS-1$
		
		/**
		 * The output stream
		 */
		private OutputStream output = null;
		
		/**
		 * The ratio of layout measure to iText measure
		 */
		public static final float LAYOUT_TO_PDF_RATIO = 1000f;
		
		public static final  int H_TEXT_SPACE = 30;
		
		public static final int V_TEXT_SPACE = 100;
		
		/**
		 * The pdf Document object created by iText
		 */
		private Document doc = null;

		/**
		 * The Pdf Writer
		 */
		private PdfWriter writer = null;
		
		/**
		 * Template for totalpage
		 */
		private PdfTemplate tpl = null;
		private int tplWidth =0;
		private int tplHeight =0;

		/**
		 * ContentByte layer for pdf,
		 * cb covers cbUnder.
		 */
		private PdfContentByte cb, cbUnder = null;

		/**
		 * The height and width of the current pdf page.
		 */
		private float pageHeight, pageWidth = 0f;
		
		
		protected IReportContent report;
		
		protected IReportRunnable reportRunnable;
		
		protected ReportDesignHandle reportDesign;

		protected IReportContext context;
		
		protected IEmitterServices services;
		
		protected float scale;
		
		protected  int hTextSpace = 30;
		
		protected int vTextSpace = 100;
		
		protected float lToP;
		
		
		private Stack containerStack = new Stack();
		
		private class ContainerPosition
		{
			private int x;
			private int y;
			public ContainerPosition(int x, int y)
			{
				this.x = x;
				this.y = y;
			}
		}
		
		/**
		 * Gets the output format. always returns "pdf".
		 */
		public String getOutputFormat()
		{
			return RenderOption.OUTPUT_FORMAT_PDF;
		}

		/**
		 * Initializes the pdfEmitter.
		 * @param services 							the emitter svervices object.
		 */
		public void initialize(IEmitterServices services)
		{
			this.services = services;
			//Gets the output file name from RenderOptionBase.OUTPUT_FILE_NAME.
			//It has the top preference.
			this.reportRunnable = services.getReportRunnable();
			if (reportRunnable != null)
			{
				reportDesign = (ReportDesignHandle)reportRunnable.getDesignHandle();
			}
		
			this.context = services.getReportContext( );
			
			Object fd = services.getOption( RenderOption.OUTPUT_FILE_NAME );
			File file = null;
			try
			{
				if( fd != null )
				{
					file = new File(fd.toString());
					File parent = file.getParentFile( );
					if ( parent != null && !parent.exists( ) )
					{
						parent.mkdirs( );
					}
					output = new FileOutputStream( file );
				}
			}
			catch( FileNotFoundException fnfe )
			{
				logger.log( Level.WARNING, fnfe.getMessage( ), fnfe );
			}
			
			//While failed to get the outputStream from the output file name specified
			//from RenderOptionBase.OUTPUT_FILE_NAME, use RenderOptionBase.OUTPUT_STREAM
			//to build the outputStream
			if( output == null )
			{
				Object value = services.getOption( RenderOption.OUTPUT_STREAM );
				if( value != null && value instanceof OutputStream )
				{
					output = ( OutputStream ) value;
				}
				
				//If the RenderOptionBase.OUTPUT_STREAM is NOT set, build the outputStream from the
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
		}
		
		/**
		 * Creates a document and create a PdfWriter
		 * @param rc 					the report content.
		 */
		public void start(IReportContent rc)
		{
			this.report = rc;
			doc = new Document();
			try
			{
				writer = PdfWriter.getInstance( doc, new BufferedOutputStream(output) );
				// Gets the title.	
				ReportDesignHandle designHandle = report.getDesign( ).getReportDesign( );
				String title = designHandle.getStringProperty(  IModuleModel.TITLE_PROP );
				if ( null != title )
					doc.addTitle( title );
			}
			catch( DocumentException de )
			{
				logger.log( Level.SEVERE, de.getMessage( ), de );
			}
		}

		/**
		 * Closes the document.
		 * @param rc 					the report content.
		 */
		public void end(IReportContent rc)
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
			TOCHandler tocHandler = new TOCHandler( rc.getTOCTree( "pdf", //$NON-NLS-1$
					ulocale ).getRoot( ) );
			TOCNode tocRoot = tocHandler.getTOCRoot();
			if (tocRoot == null || tocRoot.getChildren().isEmpty())
			{
				writer.setViewerPreferences(PdfWriter.PageModeUseNone);
			}
			else
			{
				writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
				PdfOutline root = cb.getRootOutline();
				tocHandler.createTOC(tocRoot, root);
			}
			writer.setPageEmpty( false );
			if(doc.isOpen( ))
			{
				doc.close();
			}
		}
		
		public void setTotalPage(ITextArea totalPage)
		{
			if(tpl!=null)
			{
				drawTextAt(totalPage, 0, 0, tpl, tpl.getHeight());
			}
		}

		public void visitText(ITextArea textArea)
		{
			ContainerPosition curPos;
			if ( !containerStack.isEmpty() )
				curPos = (ContainerPosition)containerStack.peek();	
			else 
				curPos = new ContainerPosition(0, 0);
			//set default spacing for text
			int x = curPos.x + textArea.getX();
			int y = curPos.y + textArea.getY();
			drawTextAt(textArea, x, y, cb, pageHeight);
			//Checks if itself is the destination of a bookmark.
			//if so, make a bookmark; if not, do nothing
			makeBookmark(textArea, curPos);
			//handle hyper-link action
			handleHyperlinkAction(textArea, curPos);
		}

		public void visitImage(IImageArea imageArea)
		{
			drawImage(imageArea);
		}
		
		public void visitAutoText(ITemplateArea templateArea)
		{
			assert(cb!=null);
			if (null == tpl)
			{
				tplWidth = templateArea.getWidth();
				tplHeight = templateArea.getHeight();
				tpl = cb.createTemplate(pdfMeasure(tplWidth), pdfMeasure(tplHeight));
			}
			cb.saveState();
			ContainerPosition curPos;
			if ( !containerStack.isEmpty() )
				curPos = (ContainerPosition)containerStack.peek();	
			else 
				curPos = new ContainerPosition(0, 0);
			float x = layoutAreaX2PDF(curPos.x + templateArea.getX());
			float y = layoutAreaY2PDF(curPos.y + templateArea.getY(),  tplHeight);
			cb.addTemplate(tpl, x, y);
			cb.restoreState();
		}

		/**
		 * If the container is a PageArea, this method creates a pdf page.
		 * If the container is the other containerAreas, 
		 * such as TableArea, or just the border of textArea/imageArea
		 * this method draws the border and background of the given container.
		 * @param container				the ContainerArea specified from layout
		 */
		public void startContainer(IContainerArea container)
		{
			if (container instanceof PageArea)
			{
				scale = container.getScale();
				lToP = LAYOUT_TO_PDF_RATIO / scale;
				hTextSpace = (int)(H_TEXT_SPACE * scale);
				vTextSpace = (int)(V_TEXT_SPACE * scale);

				newPage(container);
				containerStack.push(new ContainerPosition(0, 0));
			}
			else
			{
				drawContainer(container);
				ContainerPosition pos;
				if ( ! containerStack.isEmpty() )
				{
					pos = (ContainerPosition)containerStack.peek();
					ContainerPosition current = new ContainerPosition(
							pos.x+container.getX(), pos.y+container.getY());
					containerStack.push(current);
				}
				else
				{
					containerStack.push(new ContainerPosition(
							container.getX(), container.getY()));
				}
			}
		}
		
		/**
		 * This method will be invoked while a containerArea ends. 
		 * @param container				the ContainerArea specified from layout
		 */
		public void endContainer(IContainerArea container)
		{
			if (!containerStack.isEmpty())
			{
				containerStack.pop();	
			}
		}

		/**
		 * Creates a new PDF page
		 * @param page		the PageArea specified from layout
		 */
		protected void newPage( IContainerArea page )
		{
			pageHeight = pdfMeasure( page.getHeight() );
			pageWidth = pdfMeasure( page.getWidth() );
			
			// Sets the pagesize of the new page
	        Rectangle pageSize = new Rectangle(pageWidth, pageHeight);

	        // Creates a pdf page, get its contentByte and contentByteUnder
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
				//Adds an invisible content to the document to make sure that a new page is created. 
				//doc.add(Chunk.NEWLINE);
				
			}
			catch( DocumentException de )
			{
				logger.log( Level.SEVERE, de.getMessage( ), de );
			}
			
			//Draws background color for the container, if the backgound color is NOT set, draw nothing.
			Color bc = PropertyUtil.getColor(page.getStyle().getProperty(StyleConstants.STYLE_BACKGROUND_COLOR));
			drawBackgroundColor( bc, 0, pageHeight, pageWidth, pageHeight );
			
			//Draws background image for the new page. if the background image is NOT set, draw nothing.
			drawBackgroundImage(page.getStyle(), 0, pageHeight, pageWidth, pageHeight);
		}
		/**
		 * draw background image for the container
		 * @param containerStyle   the style of the container we draw background image for
		 * @param startX           the absolute horizontal position of the container
		 * @param startY		   the absolute vertical position of the container
		 * @param width            container width
		 * @param height           container height
		 */
		private void drawBackgroundImage(IStyle containerStyle, float startX, float startY, float width, float height)
		{
			String bi = PropertyUtil.getBackgroundImage(
					containerStyle.getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE));
			if(bi==null)
			{
				return;
			}
			FloatValue positionValX = (FloatValue)containerStyle.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_X);
			FloatValue positionValY = (FloatValue)containerStyle.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_Y);
			
			if( positionValX == null || positionValY == null)
				return;
			boolean xMode, yMode;
			float positionX,positionY;
			if( positionValX.getPrimitiveType() == CSSPrimitiveValue.CSS_PERCENTAGE)
			{
				positionX = PropertyUtil.getPercentageValue(positionValX);
				xMode = true;
			}
			else
			{
				positionX = pdfMeasure( PropertyUtil.getDimensionValue(positionValX) );
				xMode = false;
			}
			if(positionValY.getPrimitiveType() == CSSPrimitiveValue.CSS_PERCENTAGE)
			{
				positionY = PropertyUtil.getPercentageValue(positionValY);
				yMode = true;
			}
			else
			{
				positionY = pdfMeasure( PropertyUtil.getDimensionValue(positionValY) );
				yMode = false;
			}
			
			drawBackgroundImage(bi, startX, startY, width, height,
					positionX, positionY, containerStyle.getBackgroundRepeat(), xMode, yMode);
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
			public CSSValue borderStyle;
			public int borderType;
			public BorderInfo(int startX, int startY, int endX, int endY, 
					int borderWidth, Color borderColor, CSSValue borderStyle, int borderType)
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
		 * Draws a container's border, and its background color/image if there is any.
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
			ContainerPosition curPos;
			if ( !containerStack.isEmpty() )
				curPos = (ContainerPosition)containerStack.peek();	
			else 
				curPos = new ContainerPosition(0, 0);
			//content is null means it is the internal line area which has no
			//content mapping, so it has no background/border etc.
			if ( container.getContent( ) != null )
			{
				int layoutX = curPos.x + container.getX();
				int layoutY = curPos.y + container.getY();
				//the container's start position (the left top corner of the container)
				float startX = layoutPointX2PDF (layoutX);
				float startY = layoutPointY2PDF (layoutY);
	
				//the dimension of the container
				float width = pdfMeasure(container.getWidth());
				float height = pdfMeasure(container.getHeight());

				// Draws background color for the container, if the backgound
				// color is NOT set, draw nothing.
				Color bc = PropertyUtil.getColor( style
						.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
				drawBackgroundColor( bc, startX, startY, width, height );

				// Draws background image for the container. if the background
				// image is NOT set, draw nothing.
				drawBackgroundImage( style, startX, startY, width, height );

				// the width of each border
				int borderTopWidth = PropertyUtil.getDimensionValue(
						style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH));
				int borderLeftWidth = PropertyUtil.getDimensionValue(
						style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH));
				int borderBottomWidth = PropertyUtil.getDimensionValue(
						style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
				int borderRightWidth = PropertyUtil.getDimensionValue(
						style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
				
				if(borderTopWidth>0 || borderLeftWidth>0 || borderBottomWidth>0|| borderRightWidth>0)
				{
					// the color of each border
					Color borderTopColor = PropertyUtil.getColor(
							style.getProperty(StyleConstants.STYLE_BORDER_TOP_COLOR));
					Color borderRightColor = PropertyUtil.getColor(
							style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR));
					Color borderBottomColor = PropertyUtil.getColor(
							style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR));
					Color borderLeftColor = PropertyUtil.getColor(
							style.getProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR));
					
					// Caches the border info
					BorderInfo[] borders = new BorderInfo[4];
					borders[BorderInfo.TOP_BORDER] = new BorderInfo(
							layoutX, layoutY + borderTopWidth/2,
							layoutX + container.getWidth(), layoutY + borderTopWidth/2,
							borderTopWidth, borderTopColor, style.getProperty(StyleConstants.STYLE_BORDER_TOP_STYLE), BorderInfo.TOP_BORDER);
					borders[BorderInfo.RIGHT_BORDER] = new BorderInfo(
							layoutX+container.getWidth()-borderRightWidth/2, layoutY, 
							layoutX+container.getWidth()-borderRightWidth/2, layoutY+ container.getHeight(),       
							borderRightWidth, borderRightColor, style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE), BorderInfo.RIGHT_BORDER);
					borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo(
							layoutX, layoutY+container.getHeight()-borderBottomWidth/2, 
							layoutX+container.getWidth(), layoutY+container.getHeight()-borderBottomWidth/2, 
							borderBottomWidth, borderBottomColor, style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE), BorderInfo.BOTTOM_BORDER);
					borders[BorderInfo.LEFT_BORDER] = new BorderInfo(
							layoutX+borderLeftWidth/2, layoutY,
							layoutX+borderLeftWidth/2, layoutY+ container.getHeight(), 
							borderLeftWidth, borderLeftColor,style.getProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE), BorderInfo.LEFT_BORDER);
					
					// Draws the four borders of the container if there are any. Each border is showed as a line.
					drawBorder(borders);
				}
				
				// Checks if itself is the destination of a bookmark.
				// if so, make a bookmark; if not, do nothing
				makeBookmark(container, curPos);
				// Handles hyper-link action
				handleHyperlinkAction(container, curPos);
			}
		}
		
		/**
		 * Draws the borders of a container.
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
				if ( IStyle.DOUBLE_VALUE.equals( borders[i].borderStyle) ) 
				{
					if (null == dbl)
					{
						dbl = new ArrayList();
					}
					dbl.add(borders[i]);
				}
				else if ( IStyle.DASHED_VALUE.equals(borders[i].borderStyle) ) 
				{
					if (null == dashed)
					{
						dashed = new ArrayList();
					}
					dashed.add(borders[i]);
				}
				else if ( IStyle.DOTTED_VALUE.equals(borders[i].borderStyle) ) 
				{
					if (null == dotted)
					{
						dotted = new ArrayList();
					}
					dotted.add(borders[i]);
				}
				// Uses the solid style as default style.
				else
				{
					if (null == solid)
					{
						solid = new ArrayList();
					}
					solid.add(borders[i]);
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
	 				int outerBorderWidth=bi.borderWidth/4; 
	 				int innerBorderWidth=bi.borderWidth/4;
	 				
	 				switch (bi.borderType)
	 				{
	 				case BorderInfo.TOP_BORDER:
	 					drawLine( layoutPointX2PDF( bi.startX ), layoutPointY2PDF( bi.startY-bi.borderWidth/2+outerBorderWidth/2 ), 
	 	 						layoutPointX2PDF( bi.endX ), layoutPointY2PDF( bi.endY-bi.borderWidth/2+outerBorderWidth/2 ), 
	 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				drawLine( layoutPointX2PDF( bi.startX+3*borders[BorderInfo.LEFT_BORDER].borderWidth/4 ), 
	 	 						layoutPointY2PDF( bi.startY+bi.borderWidth/2-innerBorderWidth/2 ), 
	 	 						layoutPointX2PDF( bi.endX-3*borders[BorderInfo.RIGHT_BORDER].borderWidth/4 ), 
	 	 						layoutPointY2PDF( bi.endY+bi.borderWidth/2-innerBorderWidth/2 ), 
	 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				break;
	 				case BorderInfo.RIGHT_BORDER:
	 					drawLine( layoutPointX2PDF( bi.startX+bi.borderWidth/2-outerBorderWidth/2 ), layoutPointY2PDF( bi.startY ), 
	 	 						layoutPointX2PDF( bi.endX+bi.borderWidth/2-outerBorderWidth/2 ), layoutPointY2PDF( bi.endY ), 
	 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				drawLine( layoutPointX2PDF( bi.startX-bi.borderWidth/2+innerBorderWidth/2 ), 
	 	 						layoutPointY2PDF( bi.startY+3*borders[BorderInfo.TOP_BORDER].borderWidth/4 ), 
	 	 						layoutPointX2PDF( bi.endX-bi.borderWidth/2+innerBorderWidth/2 ), 
	 	 						layoutPointY2PDF( bi.endY-3*borders[BorderInfo.BOTTOM_BORDER].borderWidth/4 ), 
	 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				break;
	 				case BorderInfo.BOTTOM_BORDER:
	 					drawLine( layoutPointX2PDF( bi.startX ), layoutPointY2PDF( bi.startY+bi.borderWidth/2-outerBorderWidth/2 ), 
	 	 						layoutPointX2PDF( bi.endX ), layoutPointY2PDF( bi.endY+bi.borderWidth/2-outerBorderWidth/2 ), 
	 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				drawLine( layoutPointX2PDF( bi.startX+3*borders[BorderInfo.LEFT_BORDER].borderWidth/4 ), 
	 	 						layoutPointY2PDF( bi.startY-bi.borderWidth/2+innerBorderWidth/2 ), 
	 	 						layoutPointX2PDF( bi.endX-3*borders[BorderInfo.RIGHT_BORDER].borderWidth/4 ), 
	 	 						layoutPointY2PDF( bi.endY-bi.borderWidth/2+innerBorderWidth/2 ), 
	 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				break;
	 				case BorderInfo.LEFT_BORDER:
	 					drawLine( layoutPointX2PDF( bi.startX-bi.borderWidth/2+outerBorderWidth/2 ), layoutPointY2PDF( bi.startY ), 
	 	 						layoutPointX2PDF( bi.endX-bi.borderWidth/2+outerBorderWidth/2 ), layoutPointY2PDF( bi.endY ), 
	 	 						pdfMeasure(outerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				drawLine( layoutPointX2PDF( bi.startX+bi.borderWidth/2-innerBorderWidth/2 ), 
	 	 						layoutPointY2PDF( bi.startY+3*borders[BorderInfo.TOP_BORDER].borderWidth/4 ), 
	 	 						layoutPointX2PDF( bi.endX+bi.borderWidth/2-innerBorderWidth/2 ), 
	 	 						layoutPointY2PDF( bi.endY-3*borders[BorderInfo.BOTTOM_BORDER].borderWidth/4 ), 
	 	 						pdfMeasure(innerBorderWidth), bi.borderColor, "solid", cb ); //$NON-NLS-1$
	 	 				break;
	 				}
	 			}
	 		}
		}
		
		
		
		/**
		 * Draws a chunk of text at the pdf.
		 * @param text					the textArea to be drawed.
		 * @param tx					the X position of the textArea relative to current page.
		 * @param ty					the Y position of the textArea relative to current page.
		 * @param contentByte			the content byte to draw the text.
		 * @param contentByteHeight		the height of the content byte.
		 */
		protected void drawTextAt( ITextArea text, int tx, int ty, 
				PdfContentByte contentByte, float contentByteHeight )
		{	 
			IStyle style = text.getStyle();
			assert style!=null; 
			
		    //style.getFontVariant();     	small-caps or normal
		    //FIXME does NOT support small-caps now
			int textX = tx + (int)(text.getFontInfo( ).getFontSize( ) * hTextSpace);
			int textY = ty + (int)(text.getFontInfo( ).getFontSize( ) * vTextSpace);
			float fontSize = text.getFontInfo().getFontSize() * scale ;
			float characterSpacing = pdfMeasure( PropertyUtil.getDimensionValue(
		        	style.getProperty(StyleConstants.STYLE_LETTER_SPACING)) );
			float wordSpacing = pdfMeasure( PropertyUtil.getDimensionValue(
		        	style.getProperty(StyleConstants.STYLE_WORD_SPACING)) );
			contentByte.saveState();
			
			//clip the text 
			float x = layoutAreaX2PDF(tx);
			float y = layoutAreaY2PDFEx(ty, text.getHeight( ), contentByteHeight);
			contentByte.clip( );
			contentByte.rectangle( x, y, pdfMeasure( text.getWidth( ) ), pdfMeasure( text.getHeight( ) ) );
			contentByte.newPath( );
			
			//start drawing the text content
			contentByte.beginText();
			Color color = PropertyUtil.getColor(style.getProperty(StyleConstants.STYLE_COLOR));   
			if (null != color)
		    {
		    	contentByte.setColorFill(color);
		    	contentByte.setColorStroke( color );
		    }
			BaseFont font = text.getFontInfo().getBaseFont();
			contentByte.setFontAndSize(font, fontSize); 
			contentByte.setCharacterSpacing(characterSpacing);
			contentByte.setWordSpacing(wordSpacing);
		    placeText(contentByte, text.getFontInfo(), layoutAreaX2PDF(textX), 
		    		layoutAreaY2PDFEx(textY, text.getFontInfo().getBaseline(), contentByteHeight));
		    CSSValue align = text.getStyle( ).getProperty(
					StyleConstants.STYLE_TEXT_ALIGN );
			if ( ( font.getFontType( ) == BaseFont.FONT_TYPE_TTUNI )
					&& IStyle.JUSTIFY_VALUE.equals( align ) && wordSpacing > 0 )
			{
				String s = text.getText( );
				int idx = s.indexOf( ' ' );
				if ( idx >= 0 )
				{
					float spaceCorrection = -wordSpacing * 1000 / fontSize;
					PdfTextArray textArray = new PdfTextArray( s.substring( 0,
							idx ) );
					int lastIdx = idx;
					while ( ( idx = s.indexOf( ' ', lastIdx + 1 ) ) >= 0 )
					{
						textArray.add( spaceCorrection );
						textArray.add( s.substring( lastIdx, idx ) );
						lastIdx = idx;
					}
					textArray.add( spaceCorrection );
					textArray.add( s.substring( lastIdx ) );
					contentByte.showText( textArray );
				}
				else
				{
					contentByte.showText( s );
				}
			}
			else
			{
				contentByte.showText( text.getText( ) );
			}
			contentByte.endText( );
			contentByte.restoreState( );
		        
			//draw the overline,throughline or underline for the text if it has any. 
		    
			if ( IStyle.LINE_THROUGH_VALUE.equals( style
					.getProperty( IStyle.STYLE_TEXT_LINETHROUGH ) ) )
			{
				drawLine(
						layoutPointX2PDF( textX ),
						layoutPointY2PDF( textY
								+ text.getFontInfo( ).getLineThroughPosition( ) ),
						layoutPointX2PDF( textX + text.getWidth( ) ),
						layoutPointY2PDF( textY
								+ text.getFontInfo( ).getLineThroughPosition( ) ),
						text.getFontInfo( ).getLineWidth( ),
						PropertyUtil.getColor( style
								.getProperty( StyleConstants.STYLE_COLOR ) ),
						"solid", contentByte ); //$NON-NLS-1$
			}
			if ( IStyle.OVERLINE_VALUE.equals( style
					.getProperty( IStyle.STYLE_TEXT_OVERLINE ) ) )
			{
				drawLine( layoutPointX2PDF( textX ), layoutPointY2PDF( textY
						+ text.getFontInfo( ).getOverlinePosition( ) ),
						layoutPointX2PDF( textX + text.getWidth( ) ),
						layoutPointY2PDF( textY
								+ text.getFontInfo( ).getOverlinePosition( ) ),
						text.getFontInfo( ).getLineWidth( ),
						PropertyUtil.getColor( style
								.getProperty( StyleConstants.STYLE_COLOR ) ),
						"solid", contentByte ); //$NON-NLS-1$
			}
			if ( IStyle.UNDERLINE_VALUE.equals( style
					.getProperty( IStyle.STYLE_TEXT_UNDERLINE ) ) )
			{
				drawLine(
						layoutPointX2PDF( textX ),
						layoutPointY2PDF( textY
								+ text.getFontInfo( ).getUnderlinePosition( ) ),
						layoutPointX2PDF( textX + text.getWidth( ) ),
						layoutPointY2PDF( textY
								+ text.getFontInfo( ).getUnderlinePosition( ) ),
						text.getFontInfo( ).getLineWidth( ),
						PropertyUtil.getColor( style
								.getProperty( StyleConstants.STYLE_COLOR ) ),
						"solid", contentByte ); //$NON-NLS-1$
			} 
		}

		/**
		 * Draws image at the contentByte
		 * @param image		the ImageArea specified from the layout
		 */
		protected void drawImage( IImageArea image )
		{	
			
			
			Image img = null;
			TranscoderInput ti = null;
			cb.saveState();
			ContainerPosition curPos;
			if ( !containerStack.isEmpty() )
				curPos = (ContainerPosition)containerStack.peek();	
			else 
				curPos = new ContainerPosition(0, 0);
			int imageX = curPos.x + image.getX();
			int imageY = curPos.y + image.getY();
			IImageContent imageContent = ((IImageContent) image.getContent());
			
			try
			{
				
				boolean isSvg = false;
				
				//lookup the source type of the image area
				switch (imageContent.getImageSource())
				{
				case IImageContent.IMAGE_FILE:
					if (null == imageContent.getURI())
						return;
					
					URL url = reportDesign.findResource( imageContent
							.getURI( ), IResourceLocator.IMAGE );
					InputStream in = url.openStream( );
					
					if ( imageContent.getURI( ).endsWith( ".svg" ) ) //$NON-NLS-1$
						{
							isSvg = true;
							ti = new TranscoderInput( in );
						}
						else
						{
							try
							{
								byte[] buffer = new byte[in.available( )];
								in.read( buffer );
								img = Image.getInstance( buffer );
							}
							catch ( Exception ex )
							{
								logger
										.log( Level.WARNING, ex.getMessage( ),
												ex );
							}
							finally
							{
								in.close( );
							}

						}
					break;
				case IImageContent.IMAGE_URL:
					if (null == imageContent.getURI())
						return;
					
					if(imageContent.getURI().endsWith(".svg")) { //$NON-NLS-1$
						isSvg = true;
						ti = new TranscoderInput(imageContent.getURI( ));
					} else {
						img = Image.getInstance(imageContent.getURI());
					}
					break;
				case IImageContent.IMAGE_NAME:
				case IImageContent.IMAGE_EXPRESSION:
					if (null == imageContent.getData())
						return;
					isSvg = (( imageContent.getMIMEType( ) != null )
							&& imageContent.getMIMEType( ).equalsIgnoreCase( "image/svg+xml" )) //$NON-NLS-1$
							|| (( imageContent.getURI( ) != null )
							&& imageContent.getURI( ).toLowerCase( ).endsWith( ".svg" )) //$NON-NLS-1$
							|| (( imageContent.getExtension( ) != null )
							&& imageContent.getExtension( ).toLowerCase( ).endsWith( ".svg" )); //$NON-NLS-1$
					if(isSvg)
					{
						ti = new TranscoderInput(new ByteArrayInputStream(imageContent.getData( ))); 					
					} else {
						img = Image.getInstance(imageContent
								.getData());
					}
					
				}
					
				//img.setDpi(5*img.getDpiX(),5*img.getDpiY());
				// add the image to the given contentByte

				float width = pdfMeasure( image.getWidth( ) );
				float height = pdfMeasure( image.getHeight( ) );
				float x = layoutAreaX2PDF( imageX );
				float y = layoutAreaY2PDF( imageY, image.getHeight( ) );
				
				if(!isSvg) {
					cb.addImage( img, width, 0f, 0f, height, x, y );
					String helpText = imageContent.getHelpText( );
					if ( helpText != null )
					{
						showHelpText( x, y, width, height, helpText );
					}
				} else {
					
					try {

						if(ti!=null)
						{

				            PdfTemplate template = cb.createTemplate(new Float(width).floatValue(), new Float(height).floatValue());
				            Graphics2D g2 = template.createGraphics(new Float(width).floatValue(), new Float(height).floatValue());
				            
				            PrintTranscoder prm = new PrintTranscoder();
				            prm.addTranscodingHint( PrintTranscoder.KEY_SCALE_TO_PAGE, new Boolean(true) );
				            prm.transcode(ti, null);
				            PageFormat pg = new PageFormat();
				            Paper pp= new Paper();
				            pp.setSize(width, height);
				            pp.setImageableArea(0, 0, width, height);
				            pg.setPaper(pp); 
				            prm.print(g2, pg, 0); 
				            g2.dispose(); 
	
				            cb.addTemplate(template, new Float(x).floatValue(), new Float(y).floatValue());
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
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
			makeBookmark(image, curPos);
			
			//handle hyper-link action
			handleHyperlinkAction(image, curPos);
		}

		private void showHelpText( float x, float y, float width, float height,
				String helpText )
		{
			Rectangle rectangle = new Rectangle( x, y, x + width, y + height );
			PdfAnnotation annotation = PdfAnnotation.createSquareCircle( writer,
					rectangle, helpText, true );
			PdfBorderDictionary borderStyle = new PdfBorderDictionary( 0,
					PdfBorderDictionary.STYLE_SOLID, null );
			annotation.setBorderStyle( borderStyle );
			annotation.setFlags( 288 );
			writer.addAnnotation( annotation );
		}

		/**
		 * Draws the borders of a container.
		 * 
		 * @param borders
		 *            the border info
		 */
		private void drawBorder(BorderInfo border)
		{
			if(IStyle.SOLID_VALUE.equals( border.borderStyle ))
			{
				drawLine( layoutPointX2PDF( border.startX ), layoutPointY2PDF( border.startY ), 
 						layoutPointX2PDF( border.endX ), layoutPointY2PDF( border.endY ), 
 						pdfMeasure(border.borderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
				return;
			}
			
			if(IStyle.DOTTED_VALUE.equals( border.borderStyle ))
			{
				drawLine( layoutPointX2PDF( border.startX ), layoutPointY2PDF( border.startY ), 
 						layoutPointX2PDF( border.endX ), layoutPointY2PDF( border.endY ), 
 						pdfMeasure(border.borderWidth), border.borderColor, "dotted", cb ); //$NON-NLS-1$
				return;
			}
			if(IStyle.DASHED_VALUE.equals( border.borderStyle ))
			{
				drawLine( layoutPointX2PDF( border.startX ), layoutPointY2PDF( border.startY ), 
 						layoutPointX2PDF( border.endX ), layoutPointY2PDF( border.endY ), 
 						pdfMeasure(border.borderWidth), border.borderColor, "dashed", cb ); //$NON-NLS-1$
				return;
			}
			if(IStyle.DOUBLE_VALUE.equals( border.borderStyle ))
			{
 				int outerBorderWidth=border.borderWidth/3;
 				int innerBorderWidth=border.borderWidth/3;
 				
 				switch (border.borderType)
 				{
 				case BorderInfo.TOP_BORDER:
 					drawLine( layoutPointX2PDF( border.startX ), layoutPointY2PDF( border.startY - innerBorderWidth), 
 	 						layoutPointX2PDF( border.endX ), layoutPointY2PDF( border.endY ), 
 	 						pdfMeasure(outerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( border.startX+2*border.borderWidth/3 ), 
 	 						layoutPointY2PDF( border.startY + innerBorderWidth/2 ), 
 	 						layoutPointX2PDF( border.endX-2*border.borderWidth/3 ), 
 	 						layoutPointY2PDF( border.endY+border.borderWidth/2-innerBorderWidth/2 ), 
 	 						pdfMeasure(innerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				return;
 				case BorderInfo.RIGHT_BORDER:
 					drawLine( layoutPointX2PDF( border.startX ), layoutPointY2PDF( border.startY ), 
 	 						layoutPointX2PDF( border.endX+border.borderWidth/2-outerBorderWidth/2 ), layoutPointY2PDF( border.endY ), 
 	 						pdfMeasure(outerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( border.startX-border.borderWidth/2+innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( border.startY+2*border.borderWidth/3 ), 
 	 						layoutPointX2PDF( border.endX-border.borderWidth/2+innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( border.endY-2*border.borderWidth/3 ), 
 	 						pdfMeasure(innerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				return;
 				case BorderInfo.BOTTOM_BORDER:
 					drawLine( layoutPointX2PDF( border.startX ), layoutPointY2PDF( border.startY), 
 	 						layoutPointX2PDF( border.endX ), layoutPointY2PDF( border.endY+border.borderWidth/2-outerBorderWidth/2 ), 
 	 						pdfMeasure(outerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( border.startX+2*border.borderWidth/3 ), 
 	 						layoutPointY2PDF( border.startY-border.borderWidth/2+innerBorderWidth/2 ), 
 	 						layoutPointX2PDF( border.endX-2*border.borderWidth/3 ), 
 	 						layoutPointY2PDF( border.endY-border.borderWidth/2+innerBorderWidth/2 ), 
 	 						pdfMeasure(innerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				return;
 				case BorderInfo.LEFT_BORDER:
 					drawLine( layoutPointX2PDF( border.startX-border.borderWidth/2+outerBorderWidth/2 ), layoutPointY2PDF( border.startY ), 
 	 						layoutPointX2PDF( border.endX-border.borderWidth/2+outerBorderWidth/2 ), layoutPointY2PDF( border.endY ), 
 	 						pdfMeasure(outerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				drawLine( layoutPointX2PDF( border.startX+border.borderWidth/2-innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( border.startY+2*border.borderWidth/3 ), 
 	 						layoutPointX2PDF( border.endX+border.borderWidth/2-innerBorderWidth/2 ), 
 	 						layoutPointY2PDF( border.endY-2*border.borderWidth/3 ), 
 	 						pdfMeasure(innerBorderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
 	 				return;
 				}
	 		}
			drawLine( layoutPointX2PDF( border.startX ), layoutPointY2PDF( border.startY ), 
						layoutPointX2PDF( border.endX ), layoutPointY2PDF( border.endY ), 
						pdfMeasure(border.borderWidth), border.borderColor, "solid", cb ); //$NON-NLS-1$
			return;
		}
		
		
		/**
		 * Draws a line from the start position to the end position with the given
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
		 * Draws a line with the line-style specified in advance from the start position to 
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
		 * Draws the background color at the contentByteUnder of the pdf
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
		
		private final class tplValueTriple
		{
			private final float tplOrigin;
			private final float tplSize;
			private final float translation;
			public tplValueTriple(final float val1, final float val2, final float val3)
			{
				tplOrigin = val1;
				tplSize = val2;
				translation = val3;
			}
			float getTplOrigin()
			{
				return tplOrigin;
			}
			
			float getTplSize()
			{
				return tplSize;
			}
			
			float getTranslation()
			{
				return translation;
			}
			
		}
		/**
		 *
		 * @param absPos
		 *            the vertical position relative to its containing box
		 * @param containerBaseAbsPos
		 *            the absolute position of the container's top
		 * @param containerSize
		 *            container height
		 * @param ImageSize
		 *            the height of template which image lies in
		 * @return a triple(the vertical position of template's left-bottom origin,
		 *         template height, and image's vertical translation relative to the template )
		 */
		
		private tplValueTriple computeTplVerticalValTriple(float absPos, float containerBaseAbsPos, float containerSize, float ImageSize)
		{
			float tplOrigin = 0.0f, tplSize = 0.0f, translation = 0.0f;
			if(absPos <= 0)
			{
				if(ImageSize + absPos > 0 && ImageSize + absPos <= containerSize)
				{
					tplOrigin = containerBaseAbsPos - ImageSize - absPos;
					tplSize = ImageSize + absPos;
				}
				else if(ImageSize + absPos > containerSize)
				{
					tplOrigin = containerBaseAbsPos - containerSize;
					tplSize = containerSize;
					translation = containerSize - ImageSize - absPos;
				}
				else
				{
					//never draw
				}
			}
			else if(absPos >= containerSize)
			{
				//never draw
			}
			else
			{
				if(ImageSize + absPos <= containerSize)
				{
					tplOrigin = containerBaseAbsPos - ImageSize - absPos;
					tplSize = ImageSize;
					translation = 0.0f;
				}
				else
				{
					tplOrigin = containerBaseAbsPos - containerSize; 
					tplSize = containerSize - absPos;
					translation =  containerSize - absPos - ImageSize;
				}
				
			}
			return new tplValueTriple(tplOrigin, tplSize, translation);
		}
		
		
		/**
		 * 
		 * @param absPos
		 *            the horizontal position relative to its containing box
		 * @param containerBaseAbsPos
		 *            the absolute position of the container's left side
		 * @param containerSize
		 *            container width
		 * @param ImageSize
		 *            the width of template which image lies in
		 * @return a triple(the horizontal position of template's left-bottom origin,
		 *         template width, and image's horizontal translation relative to the template )
		 */
		private tplValueTriple computeTplHorizontalValPair(float absPos, float containerBaseAbsPos, float containerSize, float ImageSize)
		{
			float tplOrigin = 0.0f, tplSize = 0.0f, translation = 0.0f;
			if(absPos <= 0)
			{
				if(ImageSize + absPos > 0 && ImageSize + absPos <= containerSize)
				{
					tplOrigin = containerBaseAbsPos;
					tplSize = ImageSize + absPos;
				}
				else if(ImageSize + absPos > containerSize)
				{
					tplOrigin = containerBaseAbsPos;
					tplSize = containerSize;
				}
				else
				{
					//never create template
				}
				translation = absPos;
			}
			else if(absPos >= containerSize)
			{
				//	never create template
			}
			else
			{
				if(ImageSize + absPos <= containerSize)
				{
					tplOrigin = containerBaseAbsPos + absPos;
					tplSize = ImageSize;
				}
				else
				{
					tplOrigin = containerBaseAbsPos + absPos; 
					tplSize = containerSize - absPos;
				}
				translation = 0.0f;
			}
			return new tplValueTriple(tplOrigin, tplSize, translation);
		
		}
		
		/**
		 * Draws the backgound image at the contentByteUnder of the pdf with the given offset
		 * @param imageURI		the URI referring the image
		 * @param x				the start X coordinate at the pdf where the image is positioned
		 * @param y				the start Y coordinate at the pdf where the image is positioned
		 * @param width			the width of the background dimension
		 * @param height		the height of the background dimension
		 * @param positionX		the offset X percentage relating to start X
		 * @param positionY		the offset Y percentage relating to start Y
		 * @param repeat		the background-repeat property
		 * @param xMode			whether the horizontal position is a percentage value or not
		 * @param yMode			whether the vertical position is a percentage value or not
		 */
		private void drawBackgroundImage(String imageURI, float x, float y,
				float width, float height, float positionX, float positionY,
				String repeat, boolean xMode, boolean yMode) {
			// the image URI is empty, ignore it.
			if (null == imageURI) {
				return;
			}

			String id = imageURI;
			if (reportDesign != null) {
				URL url = reportDesign.findResource(imageURI,
						IResourceLocator.IMAGE);
				if (url != null) {
					id = url.toExternalForm();
				}
			}

			if (id == null || "".equals(id)) //$NON-NLS-1$
			{
				return;
			}

			// the background-repeat property is empty, use "repeat".
			if (null == repeat) {
				repeat = "repeat"; //$NON-NLS-1$
			}
			cbUnder.saveState();
			Image img = null;
			try {
				img = Image.getInstance(id);
				float absPosX,absPosY;
				if( xMode )
				{
					absPosX = (width - img.scaledWidth()) * positionX;
				}
				else
				{
					absPosX = positionX;
				}
				if(yMode)
				{
					absPosY = (height - img.scaledHeight()) * positionY;
				}
				else
				{
					absPosY = positionY;
				}
				// "no-repeat":
				if ("no-repeat".equalsIgnoreCase(repeat)) //$NON-NLS-1$
				{
					tplValueTriple triple = computeTplHorizontalValPair(absPosX, x, width, img.scaledWidth());
					float tplOriginX = triple.getTplOrigin();
					float tplWidth = triple.getTplSize();
					float translationX = triple.getTranslation();
					triple = computeTplVerticalValTriple(absPosY, y, height, img.scaledHeight());
					float tplOrininY = triple.getTplOrigin();
					float tplHeight = triple.getTplSize();
					float translationY = triple.getTranslation();
					
					PdfTemplate templateWhole = cbUnder.createTemplate(tplWidth, tplHeight);
					templateWhole.addImage(img, img.scaledWidth(), 0, 0, img.scaledHeight(), translationX, translationY);
					cbUnder.addTemplate(templateWhole, tplOriginX, tplOrininY);
					
				}
				// "repeat-x":
				else if ("repeat-x".equalsIgnoreCase(repeat)) //$NON-NLS-1$
				{
					float remainX = width;
					PdfTemplate template = null;
					// If the width of the container is smaller than the scaled
					// image width,
					// the repeat will never happen. So it is not necessary to build
					// a
					// template for futher usage.
					if (width > img.scaledWidth()) {
						if (height - absPosY > img.scaledHeight()) {
							template = cbUnder.createTemplate(img.scaledWidth(),
									img.scaledHeight());
							template.addImage(img, img.scaledWidth(), 0, 0, img
									.scaledHeight(), 0, 0);
						} else {
							template = cbUnder.createTemplate(img.scaledWidth(),
									height);
							template.addImage(img, img.scaledWidth(), 0, 0, img
									.scaledHeight(), 0, -img.scaledHeight()
									+ height);
						}
					}
					while (remainX > 0) {
						if (remainX < img.scaledWidth()) {

							if (height - absPosY > img.scaledHeight()) {
								PdfTemplate templateX = cbUnder.createTemplate(
										remainX, img.scaledHeight());
								templateX.addImage(img, img.scaledWidth(), 0, 0,
										img.scaledHeight(), 0, 0);
								cbUnder.addTemplate(templateX, x + width - remainX,
										y - absPosY - img.scaledHeight());
							} else {
								PdfTemplate templateX = cbUnder.createTemplate(
										remainX, height);
								templateX.addImage(img, img.scaledWidth(), 0, 0,
										img.scaledHeight(), 0, -img.scaledHeight()
												+ height - absPosY);
								cbUnder.addTemplate(templateX, x + width - remainX,
										y - absPosY - height);
							}
							remainX = 0;
						} else {
							if (height - absPosY > img.scaledHeight())
								cbUnder.addTemplate(template, x + width - remainX,
										y - absPosY - img.scaledHeight());
							else
								cbUnder.addTemplate(template, x + width - remainX,
										y - absPosY - height);
							remainX -= img.scaledWidth();
						}
					}
				}
				// "repeat-y":
				else if ("repeat-y".equalsIgnoreCase(repeat)) //$NON-NLS-1$
				{
					float remainY = height;
					// If the height of the container is smaller than the scaled
					// image height,
					// the repeat will never happen. So it is not necessary to build
					// a
					// template for futher usage.
					PdfTemplate template = null;
					if (height > img.scaledHeight()) {
						template = cbUnder.createTemplate(width - absPosX > img
								.scaledWidth() ? img.scaledWidth() : width
								- absPosX, img.scaledHeight());
						template.addImage(img, img.scaledWidth(), 0, 0, img
								.scaledHeight(), 0, 0);
					}
					while (remainY > 0) {
						if (remainY < img.scaledHeight()) {
							PdfTemplate templateY = cbUnder.createTemplate(width
									- absPosX > img.scaledWidth() ? img
									.scaledWidth() : width - absPosX, remainY);
							templateY.addImage(img, width > img.scaledWidth() ? img
									.scaledWidth() : width - absPosX, 0, 0, img
									.scaledHeight(), 0,
									-(img.scaledHeight() - remainY));
							cbUnder.addTemplate(templateY, x + absPosX, y - height);
							remainY = 0;
						} else {
							cbUnder.addTemplate(template, x + absPosX, y - height
									+ remainY - img.scaledHeight());
							remainY -= img.scaledHeight();
						}
					}
				}
				// "repeat":
				else if ("repeat".equalsIgnoreCase(repeat)) //$NON-NLS-1$
				{
					float remainX = width;
					float remainY = height;
					PdfTemplate template = null;
					// If the width of the container is smaller than the scaled
					// image width,
					// the repeat will never happen. So it is not necessary to build
					// a
					// template for futher usage.
					if (width > img.scaledWidth() && height > img.scaledHeight()) {
						template = cbUnder.createTemplate(img.scaledWidth(), img
								.scaledHeight());
						template.addImage(img, img.scaledWidth(), 0, 0, img
								.scaledHeight(), 0, 0);
					}

					while (remainY > 0) {
						remainX = width;
						// the bottom line
						if (remainY < img.scaledHeight()) {
							while (remainX > 0) {
								// the right-bottom one
								if (remainX < img.scaledWidth()) {
									PdfTemplate templateXY = cbUnder
											.createTemplate(remainX, remainY);
									templateXY.addImage(img, img.scaledWidth(), 0,
											0, img.scaledHeight(), 0, -img
													.scaledHeight()
													+ remainY);
									cbUnder.addTemplate(templateXY, x + width
											- remainX, y - height);
									remainX = 0;
								} else
								// non-right bottom line
								{
									PdfTemplate templateY = cbUnder.createTemplate(
											img.scaledWidth(), remainY);
									templateY.addImage(img, img.scaledWidth(), 0,
											0, img.scaledHeight(), 0, -img
													.scaledHeight()
													+ remainY);
									cbUnder.addTemplate(templateY, x + width
											- remainX, y - height);
									remainX -= img.scaledWidth();
								}
							}
							remainY = 0;
						} else
						// non-bottom lines
						{
							while (remainX > 0) {
								// the right ones
								if (remainX < img.scaledWidth()) {
									PdfTemplate templateX = cbUnder.createTemplate(
											remainX, img.scaledHeight());
									templateX.addImage(img, img.scaledWidth(), 0,
											0, img.scaledHeight(), 0, 0);
									cbUnder.addTemplate(templateX, x + width
											- remainX, y - height + remainY
											- img.scaledHeight());
									remainX = 0;
								} else {
									cbUnder.addTemplate(template, x + width
											- remainX, y - height + remainY
											- img.scaledHeight());
									remainX -= img.scaledWidth();
								}
							}
							remainY -= img.scaledHeight();
						}
					}
				}
			} catch (IOException ioe) {
				logger.log(Level.WARNING, ioe.getMessage(), ioe);
			} catch (BadElementException bee) {
				logger.log(Level.WARNING, bee.getMessage(), bee);
			} catch (DocumentException de) {
				logger.log(Level.WARNING, de.getMessage(), de);
			} catch (RuntimeException re) {
				logger.log(Level.WARNING, re.getMessage(), re);
			}
			cbUnder.restoreState();
		}
		
		private void placeText(PdfContentByte cb, FontInfo fi, float x, float y)
		{
			if (!fi.getSimulation())
			{
				cb.setTextMatrix(x,y);
				return;
			}	
			switch (fi.getFontStyle())
			{
			case Font.ITALIC:
				{
					simulateItalic(cb, x, y);
					break;
				}
			case Font.BOLD:
				{
	            	simulateBold(cb, x, y);
	            	break;
				}
			case Font.BOLDITALIC:
				{
					simulateBold(cb, x, y);
					simulateItalic(cb, x, y);
					break;
				}
			}
		}
		
		private void simulateBold(PdfContentByte cb, float x, float y)
		{
			cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
	    	cb.setLineWidth(0.9f);
	    	cb.setTextMatrix(x, y);
		}

		private void simulateItalic(PdfContentByte cb, float x, float y)
		{
			float alpha = (float) Math.tan(0f * Math.PI / 180);
			float beta = (float) Math.tan(15f * Math.PI / 180);
			cb.setTextMatrix(1, alpha, beta, 1, x, y);
		}
		
		/**
		 * Converts the layout measure to PDF, the measure of layout is 1000 times
		 * larger than that of PDF
		 * 
		 * @param layoutMeasure
		 *            the measure computed in layout manager
		 * @return the measure in PDF
		 */
		private float pdfMeasure( int layoutMeasure )
		{
			return layoutMeasure/lToP;
		}

		/**
		 * Converts the X coordinate of a point from layout to X coordinate in PDF
		 * @param layoutX 		the X coordinate specified from layout
		 * @return				the PDF X coordinate
		 */
		private float layoutPointX2PDF(int layoutX)
		{
			return pdfMeasure(layoutX);
		}
		
		/**
		 * Converts the Y coordinate of a point from layout to Y coordinate in PDF
		 * @param layoutY 		the Y coordinate specified from layout
		 * @return				the PDF Y coordinate
		 */
		private float layoutPointY2PDF (int layoutY)
		{
			return pageHeight - pdfMeasure(layoutY);
		}
		
		/**
		 * Converts the left X coordinate of an Area from layout 
		 * to the left X coordinate in PDF
		 * @param layoutX 		the X coordinate specified from layout
		 * @return				the PDF X coordinate
		 */
		private float layoutAreaX2PDF(int layoutX)
		{
			return pdfMeasure(layoutX);
		}
		
		/**
		 * Converts the top Y coordinate of an Area from layout to the start Y coordinate in pdf. 
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
		 * Converts the top Y coordinate of an Area from layout to the start Y coordinate in pdf. 
		 * to the bottom Y coordinate in PDF
		 * @param layoutY 				the Y coordinate specified from layout
		 * @param areaHeight			the height of the area whose coordinate need to be converted.
		 * 								To text area, the height is from the top of the text to
		 * 								the text's baseline.
		 * @param containerHeight		the height of the area's container 
		 * @return				the PDF Y coordinate
		 */
		private float layoutAreaY2PDFEx (int layoutY, int areaHeight, float containerHeight)
		{
			return containerHeight - pdfMeasure(layoutY) - pdfMeasure(areaHeight);
		}
	    
		/**
		 * Sets current area as a bookmark. 
		 * If the current area does NOT contain any bookmark info,
		 * this method does nothing.
		 * 
		 * @param area			the area which may need to be marked. 
		 * @param curPos 		the position, relative to the page, of the area's container.
		 */
		private void makeBookmark(IArea area, ContainerPosition curPos) 
		{
			IContent content = area.getContent();
			if( null != content )
			{
				int areaY = curPos.y + area.getY();
				String bookmark = content.getBookmark();
				if (null != bookmark)
				{
					cb.localDestination( bookmark, new PdfDestination(
							PdfDestination.XYZ, -1, layoutPointY2PDF(areaY), 0));
				}
			}
		}
		
		/**
		 * Handles the hyperlink, bookmark and drillthrough.
		 * 
		 * @param area			the area which needs to handle the hyperlink action.
		 * @param curPos		the position of the container of current area.
		 */
		private void handleHyperlinkAction(IArea area, ContainerPosition curPos)
		{
			IContent content = area.getContent();
			if( null != content )
			{
				int areaX = curPos.x + area.getX();
				int areaY = curPos.y + area.getY();
				IHyperlinkAction hlAction = content.getHyperlinkAction();
				String systemId = reportRunnable == null
						? null
						: reportRunnable.getReportName( );
				if ( null != hlAction )
				try
				{
					switch (hlAction.getType())
					{
					case IHyperlinkAction.ACTION_BOOKMARK: 
						writer.addAnnotation( new PdfAnnotation( writer,
								layoutPointX2PDF(areaX),
								layoutPointY2PDF(areaY+area.getHeight()),
								layoutPointX2PDF(areaX+area.getWidth()),
								layoutPointY2PDF(areaY),
								createPdfAction(
										hlAction.getHyperlink(), 
										hlAction.getBookmark(), 
										hlAction.getTargetWindow(), IHyperlinkAction.ACTION_BOOKMARK)) );
						break;
						
					case IHyperlinkAction.ACTION_HYPERLINK: 
						writer.addAnnotation( new PdfAnnotation( writer,
								layoutPointX2PDF(areaX),
								layoutPointY2PDF(areaY+area.getHeight()),
								layoutPointX2PDF(areaX+area.getWidth()),
								layoutPointY2PDF(areaY),
								createPdfAction(hlAction.getHyperlink(), null, hlAction.getTargetWindow(), IHyperlinkAction.ACTION_HYPERLINK)) );
						break;
						
					case IHyperlinkAction.ACTION_DRILLTHROUGH: 
						Action act = new Action( systemId, hlAction );
						
						IHTMLActionHandler actionHandler = null;
						Object ac = services.getOption( RenderOption.ACTION_HANDLER );
						if ( ac != null && ac instanceof IHTMLActionHandler )
						{
							actionHandler = (IHTMLActionHandler) ac;
						}
						
						String link = actionHandler.getURL( act, context );		
						writer.addAnnotation( new PdfAnnotation( writer,
								layoutPointX2PDF(areaX),
								layoutPointY2PDF(areaY+area.getHeight()),
								layoutPointX2PDF(areaX+area.getWidth()),
								layoutPointY2PDF(areaY),
			            		createPdfAction(link.toString(), null, hlAction.getTargetWindow(), IHyperlinkAction.ACTION_DRILLTHROUGH )));
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
		 * Creates a PdfAction.
		 * 
		 * @param hyperlink			the hyperlink.
		 * @param bookmark			the bookmark.
		 * @param target			if target equals "_blank", the target will be opened in a new window,
		 * 							else the target will be opened in the current window.
		 * @return					the created PdfAction.
		 */
		private PdfAction createPdfAction(String hyperlink, String bookmark, String target, int type)
		{
			if ("_blank".equalsIgnoreCase(target)) //$NON-NLS-1$
			// Opens the target in a new window.
			{
				return new PdfAction(hyperlink);
			}
			else
			
			// Opens the target in the current window.
			{
				if (type==IHyperlinkAction.ACTION_BOOKMARK)
				{
					return PdfAction.gotoLocalPage(bookmark, false);
				}
				else
				{
					return PdfAction.gotoRemotePage(hyperlink, bookmark, false, false);
				}
			}
		}

	
	}
	public void endGroup( IGroupContent group )
	{
		// TODO Auto-generated method stub
		
	}

	public void endListGroup( IListGroupContent group )
	{
		// TODO Auto-generated method stub
		
	}

	public void endTableBand( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	public void endTableGroup( ITableGroupContent group )
	{
		// TODO Auto-generated method stub
		
	}

	public void startGroup( IGroupContent group )
	{
		// TODO Auto-generated method stub
		
	}

	public void startListGroup( IListGroupContent group )
	{
		// TODO Auto-generated method stub
		
	}

	public void startTableBand( ITableBandContent band )
	{
		// TODO Auto-generated method stub
		
	}

	public void startTableGroup( ITableGroupContent group )
	{
		// TODO Auto-generated method stub
		
	}

}