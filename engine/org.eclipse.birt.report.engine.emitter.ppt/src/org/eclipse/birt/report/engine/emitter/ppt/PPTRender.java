/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
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
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

import com.lowagie.text.Font;

/**
 * The PPT render class.
 */
public class PPTRender implements IAreaVisitor
{

	protected static Logger logger = Logger.getLogger( PPTRender.class.getName( ) );

	/** The default output PPT file name. */
	public static final String REPORT_FILE = "Report.ppt"; //$NON-NLS-1$

	/** The ratio of layout measure to iText measure. */
	public static final float LAYOUT_TO_PPT_RATIO = 1000f;

	/** The h text space */
	public static final int H_TEXT_SPACE = 70;

	/** The v text space */
	public static final int V_TEXT_SPACE = 100;

	/** The output stream */
	private OutputStream pptOutput = null;

	/** The report content as a whole. */
	protected IReportContent report;

	protected int currentPageNum = 0;
	protected double pageWidth = 0.0d;
	protected double pageHeight = 0.0d;
	private int shapeCount = 0;

	/** The runnable report design. */
	protected IReportRunnable reportRunnable;

	/**
	 * The overall report design, defines a set of properties that describe the
	 * design as a whole like author, base and comments etc.
	 */
	protected ReportDesignHandle reportDesign;

	/**
	 * The report context,includes report parameters and configuration values.
	 */
	protected IReportContext context;

	/** The emitter service to supply emitters with necessary information. */
	protected IEmitterServices services;

	protected float scale;

	protected int hTextSpace = 70;

	protected int vTextSpace = 100;

	protected float lToP;

	/** The stack saves container position of report. */
	private final Stack containerStack = new Stack( );

	// Holds all the images' name appears in report design
	private Map imageNames = new HashMap( );

	// Holds the extension types of all images
	private Map imageExtensions = new HashMap( );

	// Holds the files' name for each page
	private Map fileNamesLists = new TreeMap( );

	private List currentImageContents = new ArrayList( );

	/**
	 * Returns the output format, always is "ppt".
	 * 
	 * @return the output format
	 */
	public String getOutputFormat( )
	{
		return "ppt";
	}

	/**
	 * Initializes the PPTEmitter.
	 * 
	 * @param services
	 *            the emitter svervices object.
	 */
	public void initialize( IEmitterServices services )
	{
		this.services = services;
		this.reportRunnable = services.getReportRunnable( );

		if ( reportRunnable != null )
		{
			reportDesign = (ReportDesignHandle) reportRunnable.getDesignHandle( );
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
				pptOutput = new FileOutputStream( file );
			}
		}
		catch ( FileNotFoundException fnfe )
		{
			logger.log( Level.WARNING, fnfe.getMessage( ), fnfe );
		}

		// While failed to get the outputStream from the output file name
		// specified from RenderOptionBase.OUTPUT_FILE_NAME, use
		// RenderOptionBase.OUTPUT_STREAM to build the outputStream.
		if ( pptOutput == null )
		{
			Object value = services.getOption( RenderOption.OUTPUT_STREAM );

			if ( value instanceof OutputStream )
			{
				pptOutput = (OutputStream) value;
			}

			// If the RenderOptionBase.OUTPUT_STREAM is NOT set, build the
			// outputStream from the REPORT_FILE param defined in this file.
			else
			{
				try
				{
					file = new File( REPORT_FILE );
					pptOutput = new FileOutputStream( file );
				}
				catch ( FileNotFoundException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
	}

	/**
	 * Creates a PPT Document.
	 * 
	 * @param rc
	 *            the report content
	 */
	public void start( IReportContent rc )
	{
		report = rc;
		if ( !imageNames.isEmpty( ) )
		{
			imageNames.clear( );
		}
		if ( !fileNamesLists.isEmpty( ) )
		{
			fileNamesLists.clear( );
		}

		try
		{
			pptOutput.write( "MIME-Version: 1.0\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "Content-Type: multipart/related; boundary=\"___Actuate_Content_Boundary___\"\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "--___Actuate_Content_Boundary___\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "Content-Location: file:///C:/___Actuate___/slide-show\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "Content-Transfer-Encoding: quoted-printable\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "Content-Type: text/html; charset=\"utf-8\"\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<html\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "xmlns=3D'http://www.w3.org/TR/REC-html40'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "xmlns:o=3D'urn:schemas-microsoft-com:office:office'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "xmlns:v=3D'urn:schemas-microsoft-com:vml'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ">\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<head>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<meta http-equiv=3D'Content-Type' content=3D'text/html; charset=3Dutf-8'>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<meta name=3D'ProgId' content=3D'PowerPoint.Slide'>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<meta name=3D'Generator' content=3D'Actuate View Server'>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<title>Actuate Report</title>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<xml><o:DocumentProperties>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<o:Author>Actuate View Server</o:Author>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "</o:DocumentProperties></xml><link rel=3DFile-List href=3D'file-list'>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<link rel=3DPresentation-XML href=3D'presentation'>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "</head><body/></html>\n".getBytes( ) ); //$NON-NLS-1$
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
	}

	/**
	 * Closes the document.
	 * 
	 * @param rc
	 *            the report content
	 */
	public void end( IReportContent rc )
	{
		if ( pptOutput != null )
		{
			try
			{
				pptOutput.write( "--___Actuate_Content_Boundary___\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "Content-Location: file:///C:/___Actuate___/presentation\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "Content-Transfer-Encoding: quoted-printable\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "Content-Type: text/xml; charset=\"utf-8\"\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "<xml\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( " xmlns:o=3D'urn:schemas-microsoft-com:office:office'\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( " xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( ">\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( ( "<p:presentation sizeof=3D'custom' slidesizex=3D'" + ( pageWidth * 8 ) + "' slidesizey=3D'" + ( pageHeight * 8 ) + "'>\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				for ( int i = 0; i < currentPageNum; i++ )
				{
					pptOutput.write( ( "<p:slide id=3D'" + ( i + 1 ) + "' href=3D's" + ( i + 1 ) + "'/>\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}

				pptOutput.write( "</p:presentation></xml>\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "--___Actuate_Content_Boundary___\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "Content-Location: file:///C:/___Actuate___/file-list\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "Content-Transfer-Encoding: quoted-printable\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "Content-Type: text/xml; charset=\"utf-8\"\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "<xml\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( " xmlns:o=3D'urn:schemas-microsoft-com:office:office'\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( " xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( ">\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "<o:MainFile href=3D'slide-show'/>\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "<o:File href=3D'presentation'/>\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "<o:File href=3D'file-list'/>\n".getBytes( ) ); //$NON-NLS-1$

				for ( int i = 0; i < currentPageNum; i++ )
				{
					pptOutput.write( ( "<o:File href=3D's" + ( i + 1 ) + "'/>\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
					if ( fileNamesLists.containsKey( new Integer( i + 1 ) ) )
					{
						List filenames = (List) fileNamesLists.get( new Integer( i + 1 ) );
						for ( Iterator ite = filenames.iterator( ); ite.hasNext( ); )
						{
							pptOutput.write( ( "<o:File href=3D\""
									+ (String) ite.next( ) + "\"/>\n" ).getBytes( ) );
						}
					}
				}

				pptOutput.write( "</xml>\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "\n".getBytes( ) ); //$NON-NLS-1$
				pptOutput.write( "--___Actuate_Content_Boundary___--\n".getBytes( ) ); //$NON-NLS-1$
			}
			catch ( IOException e )
			{
				e.printStackTrace( );
			}
		}

		pptOutput = null;
	}

	protected void endPage( ) throws IOException
	{
		String imageURI;
		// Write out the image bytes
		for ( Iterator ite = currentImageContents.iterator( ); ite.hasNext( ); )
		{
			Object content = ite.next( );
			if ( content instanceof IImageContent )
			{
				IImageContent imageContent = (IImageContent) content;
				generateImageBytes( imageContent );
			}
			else if ( content instanceof String )
			{
				imageURI = (String) content;
				generateImageBytes( imageURI );
			}
			pptOutput.write( "\n\n".getBytes( ) );
		}

		pptOutput.write( "</p:slide></body></html>\n".getBytes( ) ); //$NON-NLS-1$
	}

	private void exportImageHeader( String imagekey ) throws IOException
	{
		pptOutput.write( "\n".getBytes( ) );
		pptOutput.write( "--___Actuate_Content_Boundary___\n".getBytes( ) ); //$NON-NLS-1$
		pptOutput.write( ( "Content-Location: file:///C:/___Actuate___/"
				+ (String) imageNames.get( imagekey ) + "\n" ).getBytes( ) );
		pptOutput.write( "Content-Transfer-Encoding: base64\n".getBytes( ) );
		pptOutput.write( ( "Content-Type: image/"
				+ (String) imageExtensions.get( imagekey ) + "\n\n" ).getBytes( ) );
	}

	private void generateImageBytes( String imageURI ) throws IOException
	{
		exportImageHeader( imageURI );
		Base64 base = new Base64( );
		URL imageURL = null;
		if ( reportDesign != null )
		{
			imageURL = reportDesign.findResource( imageURI,
					IResourceLocator.IMAGE );
		}
		
		// fix 193886
		if ( imageURL == null )
		{
			return;
		}

		if ( imageURL.toString( ).toLowerCase( ).endsWith( ".svg" ) ) //$NON-NLS-1$
		{
			// convert svg image to JPEG image bytes
			JPEGTranscoder transcoder = new JPEGTranscoder( );
			// set the transcoding hints
			transcoder.addTranscodingHint( JPEGTranscoder.KEY_QUALITY,
					new Float( .8 ) );
			TranscoderInput input = new TranscoderInput( imageURL.toString( ) );
			// create the transcoder output
			ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
			TranscoderOutput output = new TranscoderOutput( ostream );
			try
			{
				transcoder.transcode( input, output );
			}
			catch ( TranscoderException e )
			{
			}
			// flush the stream
			ostream.flush( );
			// use the outputstream as Image input stream.
			pptOutput.write( base.encode( ostream.toByteArray( ) ) );
		}
		else
		{
			FileInputStream file = null;
			try
			{
				file = new FileInputStream( imageURI );
			}
			catch ( FileNotFoundException e )
			{
				throw e;
			}

			byte[] data = null;
			if ( file != null )
			{
				try
				{
					data = new byte[file.available( )];
					file.read( data );
				}
				catch ( IOException e1 )
				{
					throw e1;
				}
			}
			pptOutput.write( base.encode( data ) );
		}
	}

	private void generateImageBytes( IImageContent imageContent )
			throws IOException
	{
		if ( imageContent.getURI( ) == null )
		{
			exportImageHeader( imageContent.getName( ) );
		}
		else
		{
			exportImageHeader( imageContent.getURI( ) );
		}

		boolean isSvg = false;
		TranscoderInput transInput = null;
		Base64 base = new Base64( );
		byte[] imageData = null;

		try
		{
			if ( imageContent.getURI( ) == null )
			{
				imageData = imageContent.getData( );
				if ( imageContent.getExtension( ) != null
						&& imageContent.getExtension( )
								.equalsIgnoreCase( "svg" ) )
				{
					isSvg = true;
				}
			}
			else
			{
				// lookup the source type of the image area
				switch ( imageContent.getImageSource( ) )
				{
					case IImageContent.IMAGE_FILE :
						if ( imageContent.getURI( ) == null )
							return;

						URL url = reportDesign.findResource( imageContent.getURI( ),
								IResourceLocator.IMAGE );
						InputStream imageInput = url.openStream( );
						if ( imageContent.getURI( ).endsWith( ".svg" ) ) //$NON-NLS-1$
						{
							isSvg = true;
							transInput = new TranscoderInput( imageInput );
						}
						imageData = new byte[imageInput.available( )];
						imageInput.read( imageData );
						break;
					case IImageContent.IMAGE_URL :
						if ( imageContent.getURI( ) == null )
							return;

						if ( imageContent.getURI( ).endsWith( ".svg" ) )
						{
							isSvg = true;
							transInput = new TranscoderInput( imageContent.getURI( ) );
						}
						imageData = imageContent.getData( );
						break;
					case IImageContent.IMAGE_NAME :
					case IImageContent.IMAGE_EXPRESSION :
						if ( imageContent.getData( ) == null
								|| imageContent.getURI( ) == null )
							return;
						if ( imageContent.getURI( ).endsWith( ".svg" ) )
						{
							isSvg = true;
							transInput = new TranscoderInput( new ByteArrayInputStream( imageContent.getData( ) ) );
						}
						imageData = imageContent.getData( );
						break;
					default :
						imageData = imageContent.getData( );
						break;
				}
			}

			if ( !isSvg )
			{
				pptOutput.write( base.encode( imageData ) );
			}
			else
			{
				JPEGTranscoder transcoder = new JPEGTranscoder( );
				transcoder.addTranscodingHint( JPEGTranscoder.KEY_QUALITY,
						new Float( .8 ) );
				// create the transcoder output
				ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
				TranscoderOutput transOutput = new TranscoderOutput( ostream );
				try
				{
					transcoder.transcode( transInput, transOutput );
				}
				catch ( TranscoderException e )
				{
				}
				// flush the stream
				ostream.flush( );
				// use the outputstream as Image input stream.
				pptOutput.write( base.encode( ostream.toByteArray( ) ) );
			}
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
		catch ( Throwable t )
		{
			logger.log( Level.WARNING, t.getMessage( ), t );
		}
	}

	protected ContainerPosition getContainerPosition( )
	{
		ContainerPosition curPos;
		if ( !containerStack.isEmpty( ) )
		{
			curPos = (ContainerPosition) containerStack.peek( );
		}
		else
		{
			curPos = new ContainerPosition( 0, 0 );
		}
		return curPos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.layout.area.IAreaVisitor#setTotalPage(org.eclipse.birt.report.engine.layout.area.ITextArea)
	 */
	public void setTotalPage( ITextArea totalPage )
	{
//		ContainerPosition curPos = getContainerPosition( );
//
//		// set default spacing for text
//		int x = curPos.x + totalPage.getX( );
//		int y = curPos.y + totalPage.getY( );
//
//		drawTextAt( totalPage, x, y );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.layout.area.IAreaVisitor#visitText(org.eclipse.birt.report.engine.layout.area.ITextArea)
	 */
	public void visitText( ITextArea textArea )
	{
		ContainerPosition curPos = getContainerPosition( );

		// set default spacing for text
		int x = curPos.x + textArea.getX( );
		int y = curPos.y + textArea.getY( );

		drawTextAt( textArea, x, y );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.layout.area.IAreaVisitor#visitImage(org.eclipse.birt.report.engine.layout.area.IImageArea)
	 */
	public void visitImage( IImageArea imageArea )
	{
		drawImage( imageArea );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.layout.area.IAreaVisitor#visitAutoText(org.eclipse.birt.report.engine.layout.area.ITemplateArea)
	 */
	public void visitAutoText( ITemplateArea templateArea )
	{
		// assert(cb!=null);
		// if (null == tpl)
		// {
		// tplWidth = templateArea.getWidth();
		// tplHeight = templateArea.getHeight();
		// tpl = cb.createTemplate(pptMeasure(tplWidth),
		// pptMeasure(tplHeight));
		// }
		// cb.saveState();
		// ContainerPosition curPos;
		// if ( !containerStack.isEmpty() )
		// curPos = (ContainerPosition)containerStack.peek();
		// else
		// curPos = new ContainerPosition(0, 0);
		// float x = layoutAreaX2PPT(curPos.x + templateArea.getX());
		// float y = layoutAreaY2PPT(curPos.y + templateArea.getY(),
		// tplHeight);
		// cb.addTemplate(tpl, x, y);
		// cb.restoreState();
	}

	public void visitContainer( IContainerArea container )
	{
		startContainer( container );
		Iterator iter = container.getChildren( );
		while ( iter.hasNext( ) )
		{
			IArea child = (IArea) iter.next( );
			child.accept( this );
		}
		endContainer( container );
	}

	/**
	 * If the container is a PageArea, this method creates a new page. If the
	 * container is the other containerAreas, such as TableArea, or just the
	 * border of textArea/imageArea this method draws the border and background
	 * of the given container.
	 * 
	 * @param container
	 *            the ContainerArea specified from layout
	 */
	public void startContainer( IContainerArea container )
	{
		if ( container instanceof PageArea )
		{
			scale = container.getScale( );
			lToP = LAYOUT_TO_PPT_RATIO / scale;
			hTextSpace = (int) ( H_TEXT_SPACE * scale );
			vTextSpace = (int) ( V_TEXT_SPACE * scale );

			newPage( container );
			containerStack.push( new ContainerPosition( 0, 0 ) );
		}
		else
		{
			drawContainer( container );
			ContainerPosition pos;
			if ( !containerStack.isEmpty( ) )
			{
				pos = (ContainerPosition) containerStack.peek( );
				ContainerPosition current = new ContainerPosition( pos.x
						+ container.getX( ), pos.y + container.getY( ) );
				containerStack.push( current );
			}
			else
			{
				containerStack.push( new ContainerPosition( container.getX( ),
						container.getY( ) ) );
			}
		}
	}

	/**
	 * This method will be invoked while a containerArea ends.
	 * 
	 * @param container
	 *            the ContainerArea specified from layout
	 */
	public void endContainer( IContainerArea container )
	{
		if ( !containerStack.isEmpty( ) )
		{
			containerStack.pop( );
		}
	}

	/**
	 * Creates a new page.
	 * 
	 * @param page
	 *            the PageArea specified from layout
	 */
	protected void newPage( IContainerArea page )
	{
		currentPageNum++;
		currentImageContents.clear( );

		if ( pageWidth <= 0 )
		{
			pageWidth = pptMeasure( page.getWidth( ) );
		}

		if ( pageHeight <= 0 )
		{
			pageHeight = pptMeasure( page.getHeight( ) );
		}

		try
		{
			pptOutput.write( "--___Actuate_Content_Boundary___\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "Content-Location: file:///C:/___Actuate___/s" + currentPageNum + "\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			pptOutput.write( "Content-Transfer-Encoding: quoted-printable\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "Content-Type: text/html; charset=\"utf-8\"\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<html\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( " xmlns=3D'http://www.w3.org/TR/REC-html40'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( " xmlns:o=3D'urn:schemas-microsoft-com:office:office'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( " xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( " xmlns:v=3D'urn:schemas-microsoft-com:vml'\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ">\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<head/><body><p:slide>\n".getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( "<meta http-equiv=3D'Content-Type' content=3D'text/html; charset=3Dutf-8'>\n".getBytes( ) ); //$NON-NLS-1$
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}

		// // Draws background color for the container, if the backgound
		// color
		// // is NOT set, draw white.
		// Color bc = PropertyUtil.getColor( page.getStyle( )
		// .getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
		//
		// drawBackgroundColor( bc, 0, 0, width, height );
		//
		// // Draws background image for the new page. if the background
		// image
		// // is NOT set, draw nothing.
		// drawBackgroundImage( page.getStyle( ), 0, 0, width, height );
	}

	/**
	 * Draws background image for the container.
	 * 
	 * @param containerStyle
	 *            the style of the container we draw background image for
	 * @param startX
	 *            the absolute horizontal position of the container
	 * @param startY
	 *            the absolute vertical position of the container
	 * @param width
	 *            container width
	 * @param height
	 *            container height
	 */
	private void drawBackgroundImage( IStyle containerStyle, double startX,
			double startY, double width, double height )
	{
		String bi = PropertyUtil.getBackgroundImage( containerStyle.getProperty( StyleConstants.STYLE_BACKGROUND_IMAGE ) );
		if ( bi == null )
		{
			return;
		}
		FloatValue positionValX = (FloatValue) containerStyle.getProperty( StyleConstants.STYLE_BACKGROUND_POSITION_X );
		FloatValue positionValY = (FloatValue) containerStyle.getProperty( StyleConstants.STYLE_BACKGROUND_POSITION_Y );

		if ( positionValX == null || positionValY == null )
		{
			return;
		}

		boolean xMode, yMode;
		double positionX, positionY;

		if ( positionValX.getPrimitiveType( ) == CSSPrimitiveValue.CSS_PERCENTAGE )
		{
			positionX = PropertyUtil.getPercentageValue( positionValX );
			xMode = true;
		}
		else
		{
			positionX = pptMeasure( PropertyUtil.getDimensionValue( positionValX ) );
			xMode = false;
		}
		if ( positionValY.getPrimitiveType( ) == CSSPrimitiveValue.CSS_PERCENTAGE )
		{
			positionY = PropertyUtil.getPercentageValue( positionValY );
			yMode = true;
		}
		else
		{
			positionY = pptMeasure( PropertyUtil.getDimensionValue( positionValY ) );
			yMode = false;
		}

		drawBackgroundImage( bi,
				startX,
				startY,
				width,
				height,
				positionX,
				positionY,
				containerStyle.getBackgroundRepeat( ),
				xMode,
				yMode );
	}

	/**
	 * Draws a container's border, and its background color/image if there is
	 * any.
	 * 
	 * @param container
	 *            the containerArea whose border and background need to be
	 *            drawed
	 */
	protected void drawContainer( IContainerArea container )
	{
		// get the style of the container
		IStyle style = container.getStyle( );
		if ( null == style )
		{
			return;
		}
		ContainerPosition curPos;
		if ( !containerStack.isEmpty( ) )
			curPos = (ContainerPosition) containerStack.peek( );
		else
			curPos = new ContainerPosition( 0, 0 );
		// content is null means it is the internal line area which has no
		// content mapping, so it has no background/border etc.
		if ( container.getContent( ) != null )
		{
			int layoutX = curPos.x + container.getX( );
			int layoutY = curPos.y + container.getY( );
			// the container's start position (the left top corner of the
			// container)
			double startX = layoutPointX2PPT( layoutX );
			double startY = layoutPointY2PPT( layoutY );

			// the dimension of the container
			double width = pptMeasure( container.getWidth( ) );
			double height = pptMeasure( container.getHeight( ) );

			// Draws background color for the container, if the backgound
			// color is NOT set, draw nothing.
			Color bc = PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
			if ( bc != null )
			{
				drawBackgroundColor( bc, startX, startY, width, height );
			}

			// Draws background image for the container. if the background
			// image is NOT set, draw nothing.
			drawBackgroundImage( style, startX, startY, width, height );

			// the width of each border
			int borderTopWidth = PropertyUtil.getDimensionValue( style.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
			int borderLeftWidth = PropertyUtil.getDimensionValue( style.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
			int borderBottomWidth = PropertyUtil.getDimensionValue( style.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
			int borderRightWidth = PropertyUtil.getDimensionValue( style.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );

			if ( borderTopWidth > 0
					|| borderLeftWidth > 0
					|| borderBottomWidth > 0
					|| borderRightWidth > 0 )
			{
				// the color of each border
				Color borderTopColor = PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_BORDER_TOP_COLOR ) );
				Color borderRightColor = PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_BORDER_RIGHT_COLOR ) );
				Color borderBottomColor = PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_COLOR ) );
				Color borderLeftColor = PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_BORDER_LEFT_COLOR ) );

				// Caches the border info
				BorderInfo[] borders = new BorderInfo[4];
				borders[BorderInfo.TOP_BORDER] = new BorderInfo( layoutX,
						layoutY + borderTopWidth / 2,
						layoutX + container.getWidth( ),
						layoutY + borderTopWidth / 2,
						borderTopWidth,
						borderTopColor,
						style.getProperty( StyleConstants.STYLE_BORDER_TOP_STYLE ),
						BorderInfo.TOP_BORDER );
				borders[BorderInfo.RIGHT_BORDER] = new BorderInfo( layoutX
						+ container.getWidth( )
						- borderRightWidth
						/ 2,
						layoutY,
						layoutX + container.getWidth( ) - borderRightWidth / 2,
						layoutY + container.getHeight( ),
						borderRightWidth,
						borderRightColor,
						style.getProperty( StyleConstants.STYLE_BORDER_RIGHT_STYLE ),
						BorderInfo.RIGHT_BORDER );
				borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo( layoutX,
						layoutY
								+ container.getHeight( )
								- borderBottomWidth
								/ 2,
						layoutX + container.getWidth( ),
						layoutY
								+ container.getHeight( )
								- borderBottomWidth
								/ 2,
						borderBottomWidth,
						borderBottomColor,
						style.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_STYLE ),
						BorderInfo.BOTTOM_BORDER );
				borders[BorderInfo.LEFT_BORDER] = new BorderInfo( layoutX
						+ borderLeftWidth
						/ 2,
						layoutY,
						layoutX + borderLeftWidth / 2,
						layoutY + container.getHeight( ),
						borderLeftWidth,
						borderLeftColor,
						style.getProperty( StyleConstants.STYLE_BORDER_LEFT_STYLE ),
						BorderInfo.LEFT_BORDER );

				// Draws the four borders of the container if there are any.
				// Each border is showed as a line.
				drawBorder( borders );
			}
		}
	}

	/**
	 * Draws the borders of a container.
	 * 
	 * @param borders
	 *            the border info
	 */
	private void drawBorder( BorderInfo[] borders )
	{
		// double>solid>dashed>dotted>none
		ArrayList dbl = null;
		ArrayList solid = null;
		ArrayList dashed = null;
		ArrayList dotted = null;

		for ( int i = 0; i < borders.length; i++ )
		{
			if ( IStyle.DOUBLE_VALUE.equals( borders[i].borderStyle ) )
			{
				if ( null == dbl )
				{
					dbl = new ArrayList( );
				}
				dbl.add( borders[i] );
			}
			else if ( IStyle.DASHED_VALUE.equals( borders[i].borderStyle ) )
			{
				if ( null == dashed )
				{
					dashed = new ArrayList( );
				}
				dashed.add( borders[i] );
			}
			else if ( IStyle.DOTTED_VALUE.equals( borders[i].borderStyle ) )
			{
				if ( null == dotted )
				{
					dotted = new ArrayList( );
				}
				dotted.add( borders[i] );
			}
			// Uses the solid style as default style.
			else
			{
				if ( null == solid )
				{
					solid = new ArrayList( );
				}
				solid.add( borders[i] );
			}
		}
		if ( null != dotted )
		{
			for ( Iterator it = dotted.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				drawLine( layoutPointX2PPT( bi.startX ),
						layoutPointY2PPT( bi.startY ),
						layoutPointX2PPT( bi.endX ),
						layoutPointY2PPT( bi.endY ),
						pptMeasure( bi.borderWidth ),
						bi.borderColor,
						"dotted" ); //$NON-NLS-1$
			}
		}
		if ( null != dashed )
		{
			for ( Iterator it = dashed.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				drawLine( layoutPointX2PPT( bi.startX ),
						layoutPointY2PPT( bi.startY ),
						layoutPointX2PPT( bi.endX ),
						layoutPointY2PPT( bi.endY ),
						pptMeasure( bi.borderWidth ),
						bi.borderColor,
						"dashed" ); //$NON-NLS-1$
			}
		}
		if ( null != solid )
		{
			for ( Iterator it = solid.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				drawLine( layoutPointX2PPT( bi.startX ),
						layoutPointY2PPT( bi.startY ),
						layoutPointX2PPT( bi.endX ),
						layoutPointY2PPT( bi.endY ),
						pptMeasure( bi.borderWidth ),
						bi.borderColor,
						"solid" ); //$NON-NLS-1$
			}
		}
		if ( null != dbl )
		{
			for ( Iterator it = dbl.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				int outerBorderWidth = bi.borderWidth / 4;
				int innerBorderWidth = bi.borderWidth / 4;

				switch ( bi.borderType )
				{
					case BorderInfo.TOP_BORDER :
						drawLine( layoutPointX2PPT( bi.startX ),
								layoutPointY2PPT( bi.startY
										- bi.borderWidth
										/ 2
										+ outerBorderWidth
										/ 2 ),
								layoutPointX2PPT( bi.endX ),
								layoutPointY2PPT( bi.endY
										- bi.borderWidth
										/ 2
										+ outerBorderWidth
										/ 2 ),
								pptMeasure( outerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						drawLine( layoutPointX2PPT( bi.startX
								+ 3
								* borders[BorderInfo.LEFT_BORDER].borderWidth
								/ 4 ),
								layoutPointY2PPT( bi.startY
										+ bi.borderWidth
										/ 2
										- innerBorderWidth
										/ 2 ),
								layoutPointX2PPT( bi.endX
										- 3
										* borders[BorderInfo.RIGHT_BORDER].borderWidth
										/ 4 ),
								layoutPointY2PPT( bi.endY
										+ bi.borderWidth
										/ 2
										- innerBorderWidth
										/ 2 ),
								pptMeasure( innerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						break;
					case BorderInfo.RIGHT_BORDER :
						drawLine( layoutPointX2PPT( bi.startX
								+ bi.borderWidth
								/ 2
								- outerBorderWidth
								/ 2 ),
								layoutPointY2PPT( bi.startY ),
								layoutPointX2PPT( bi.endX
										+ bi.borderWidth
										/ 2
										- outerBorderWidth
										/ 2 ),
								layoutPointY2PPT( bi.endY ),
								pptMeasure( outerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						drawLine( layoutPointX2PPT( bi.startX
								- bi.borderWidth
								/ 2
								+ innerBorderWidth
								/ 2 ),
								layoutPointY2PPT( bi.startY
										+ 3
										* borders[BorderInfo.TOP_BORDER].borderWidth
										/ 4 ),
								layoutPointX2PPT( bi.endX
										- bi.borderWidth
										/ 2
										+ innerBorderWidth
										/ 2 ),
								layoutPointY2PPT( bi.endY
										- 3
										* borders[BorderInfo.BOTTOM_BORDER].borderWidth
										/ 4 ),
								pptMeasure( innerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						break;
					case BorderInfo.BOTTOM_BORDER :
						drawLine( layoutPointX2PPT( bi.startX ),
								layoutPointY2PPT( bi.startY
										+ bi.borderWidth
										/ 2
										- outerBorderWidth
										/ 2 ),
								layoutPointX2PPT( bi.endX ),
								layoutPointY2PPT( bi.endY
										+ bi.borderWidth
										/ 2
										- outerBorderWidth
										/ 2 ),
								pptMeasure( outerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						drawLine( layoutPointX2PPT( bi.startX
								+ 3
								* borders[BorderInfo.LEFT_BORDER].borderWidth
								/ 4 ),
								layoutPointY2PPT( bi.startY
										- bi.borderWidth
										/ 2
										+ innerBorderWidth
										/ 2 ),
								layoutPointX2PPT( bi.endX
										- 3
										* borders[BorderInfo.RIGHT_BORDER].borderWidth
										/ 4 ),
								layoutPointY2PPT( bi.endY
										- bi.borderWidth
										/ 2
										+ innerBorderWidth
										/ 2 ),
								pptMeasure( innerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						break;
					case BorderInfo.LEFT_BORDER :
						drawLine( layoutPointX2PPT( bi.startX
								- bi.borderWidth
								/ 2
								+ outerBorderWidth
								/ 2 ),
								layoutPointY2PPT( bi.startY ),
								layoutPointX2PPT( bi.endX
										- bi.borderWidth
										/ 2
										+ outerBorderWidth
										/ 2 ),
								layoutPointY2PPT( bi.endY ),
								pptMeasure( outerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						drawLine( layoutPointX2PPT( bi.startX
								+ bi.borderWidth
								/ 2
								- innerBorderWidth
								/ 2 ),
								layoutPointY2PPT( bi.startY
										+ 3
										* borders[BorderInfo.TOP_BORDER].borderWidth
										/ 4 ),
								layoutPointX2PPT( bi.endX
										+ bi.borderWidth
										/ 2
										- innerBorderWidth
										/ 2 ),
								layoutPointY2PPT( bi.endY
										- 3
										* borders[BorderInfo.BOTTOM_BORDER].borderWidth
										/ 4 ),
								pptMeasure( innerBorderWidth ),
								bi.borderColor,
								"solid" ); //$NON-NLS-1$
						break;
				}
			}
		}
	}

	/**
	 * Draws a chunk of text on the PPT.
	 * 
	 * @param text
	 *            the textArea to be drawed.
	 * @param textX
	 *            the X position of the textArea relative to current page.
	 * @param textY
	 *            the Y position of the textArea relative to current page.
	 * @param contentByte
	 *            the content byte to draw the text.
	 * @param contentByteHeight
	 *            the height of the content byte.
	 */
	protected void drawTextAt( ITextArea text, int textX, int textY )
	{
		if ( text == null )
		{
			return;
		}

		IStyle style = text.getStyle( );
		assert style != null;
		FontInfo fontInfo = text.getFontInfo( );

		// style.getFontVariant(); small-caps or normal
		// FIXME does NOT support small-caps now
		textX += text.getFontInfo( ).getFontSize( ) * hTextSpace;
		textY += text.getFontInfo( ).getFontSize( ) * vTextSpace;
		// float fontSize = text.getFontInfo().getFontSize();
		// float characterSpacing = pptMeasure(
		// PropertyUtil.getDimensionValue(
		// style.getProperty(StyleConstants.STYLE_LETTER_SPACING)) );
		// float wordSpacing = pptMeasure( PropertyUtil.getDimensionValue(
		// style.getProperty(StyleConstants.STYLE_WORD_SPACING)) );

		// contentByte.saveState();
		// //start drawing the text content
		// contentByte.beginText();

		Color color = PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_COLOR ) );
		FontInfo fi = text.getFontInfo( );

		// splits font-family list
		CSSValueList fontFamilies = (CSSValueList) style.getProperty( StyleConstants.STYLE_FONT_FAMILY );
		String red = Integer.toHexString( color.getRed( ) );
		String green = Integer.toHexString( color.getGreen( ) );
		String blue = Integer.toHexString( color.getBlue( ) );

		red = red.length( ) == 1 ? "0" + red : red; //$NON-NLS-1$
		green = green.length( ) == 1 ? "0" + green : green; //$NON-NLS-1$
		blue = blue.length( ) == 1 ? "0" + blue : blue; //$NON-NLS-1$

		Charset charset = Charset.forName( "UTF-8" );
		ByteBuffer encodedText = charset.encode( text.getText( ) );
		try
		{
			pptOutput.write( ( "<v:shape id=3D't" + ( ++shapeCount ) + "' type=3D'#r'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			pptOutput.write( ( " style=3D'position:absolute;left:" + textX / LAYOUT_TO_PPT_RATIO + "pt;top:" + textY / LAYOUT_TO_PPT_RATIO + "pt;width:" + text.getWidth( ) / LAYOUT_TO_PPT_RATIO + "pt;height:" + text.getHeight( ) / LAYOUT_TO_PPT_RATIO + "pt;v-text-anchor:top;mso-wrap-style:square;'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			pptOutput.write( ( " filled=3D'f' stroked=3D'f'>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<v:textbox style=3D'mso-fit-shape-to-text:f;' inset=3D'0.00pt 0.00pt 0.00pt 0.00pt'/>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "</v:shape>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<div v:shape=3D't" + shapeCount + "'>\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$

			pptOutput.write( ( "<div style=3D'mso-text-indent-alt:" //$NON-NLS-1$
					+ 0
					+ ";text-align:left;'>" //$NON-NLS-1$
					+ "<span style=3D'font-family:" //$NON-NLS-1$
					+ fontFamilies.getCssText( )
					+ ";font-size:" //$NON-NLS-1$
					+ fi.getFontSize( )
					* scale
					+ "pt;color:#" //$NON-NLS-1$
					+ red
					+ green
					+ blue + ";'>" ).getBytes( ) ); //$NON-NLS-1$
			// + text.getText( ) + "</span></div>\n" ).getBytes( ) );

			if ( fontInfo != null && fontInfo.getFontStyle( ) == Font.ITALIC )
			{
				pptOutput.write( ( "<i>" ).getBytes( ) );
				pptOutput.write( encodedText.array( ) );
				pptOutput.write( ( "</i>" ).getBytes( ) );
			}
			else
			{
				pptOutput.write( encodedText.array( ) );
			}
			pptOutput.write( ( "</span></div>\n" ).getBytes( ) );

			pptOutput.write( ( "</div>\n" ).getBytes( ) ); //$NON-NLS-1$
		}
		catch ( IOException ioe )
		{
			// e.printStackTrace( );
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}

		// contentByte.showText(text.getText());
		// contentByte.endText();
		// contentByte.restoreState();

		// draw the overline,throughline or underline for the text if it has
		// any.

		if ( IStyle.LINE_THROUGH_VALUE.equals( style.getProperty( IStyle.STYLE_TEXT_LINETHROUGH ) ) )
		{
			drawLine( layoutPointX2PPT( textX ),
					layoutPointY2PPT( textY
							+ text.getFontInfo( ).getLineThroughPosition( ) ),
					layoutPointX2PPT( textX + text.getWidth( ) ),
					layoutPointY2PPT( textY
							+ text.getFontInfo( ).getLineThroughPosition( ) ),
					text.getFontInfo( ).getLineWidth( ),
					PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_COLOR ) ),
					"solid" ); //$NON-NLS-1$
		}
		if ( IStyle.OVERLINE_VALUE.equals( style.getProperty( IStyle.STYLE_TEXT_OVERLINE ) ) )
		{
			drawLine( layoutPointX2PPT( textX ),
					layoutPointY2PPT( textY
							+ text.getFontInfo( ).getOverlinePosition( ) ),
					layoutPointX2PPT( textX + text.getWidth( ) ),
					layoutPointY2PPT( textY
							+ text.getFontInfo( ).getOverlinePosition( ) ),
					text.getFontInfo( ).getLineWidth( ),
					PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_COLOR ) ),
					"solid" ); //$NON-NLS-1$
		}
		if ( IStyle.UNDERLINE_VALUE.equals( style.getProperty( IStyle.STYLE_TEXT_UNDERLINE ) ) )
		{
			drawLine( layoutPointX2PPT( textX ),
					layoutPointY2PPT( textY
							+ text.getFontInfo( ).getUnderlinePosition( ) ),
					layoutPointX2PPT( textX + text.getWidth( ) ),
					layoutPointY2PPT( textY
							+ text.getFontInfo( ).getUnderlinePosition( ) ),
					text.getFontInfo( ).getLineWidth( ),
					PropertyUtil.getColor( style.getProperty( StyleConstants.STYLE_COLOR ) ),
					"solid" ); //$NON-NLS-1$
		}
	}

	/**
	 * Draws image into the PPT.
	 * 
	 * @param image
	 *            the ImageArea specified from the layout
	 */
	protected void drawImage( IImageArea image )
	{
		if ( image == null )
			return;

		ContainerPosition curPos;
		String imageName;
		shapeCount++;
		if ( !containerStack.isEmpty( ) )
		{
			curPos = (ContainerPosition) containerStack.peek( );
		}
		else
		{
			curPos = new ContainerPosition( 0, 0 );
		}
		int imageX = curPos.x + image.getX( );
		int imageY = curPos.y + image.getY( );

		// TODO insert a image
		IImageContent imageContent = ( (IImageContent) image.getContent( ) );

		String imageTitle = "slide" + currentPageNum + "_image" + shapeCount;
		if ( imageContent.getURI( ) != null )
		{
			if ( imageNames.containsKey( imageContent.getURI( ) ) )
			{
				imageName = (String) imageNames.get( imageContent.getURI( ) );
			}
			else
			{
				// Save in global image names map
				String extension = getImageExtension( imageContent.getURI( ) );
				imageName = imageTitle + "." + extension;
				imageNames.put( imageContent.getURI( ), imageName );
				imageExtensions.put( imageContent.getURI( ), extension );
				recordFileLists( imageName );
				currentImageContents.add( imageContent );
			}
		}
		else
		// Chart is render as a image, but hasn't URI
		{
			if ( imageNames.containsKey( imageContent.getName( ) ) )
			{
				imageName = (String) imageNames.get( imageContent.getName( ) );
			}
			else
			{
				// Save in global image names map
				String extension = imageContent.getExtension( );
				imageName = imageTitle + "." + extension;
				imageNames.put( imageContent.getName( ), imageName );
				imageExtensions.put( imageContent.getName( ), extension );
				recordFileLists( imageName );
				currentImageContents.add( imageContent );
			}
		}

		double width = pptMeasure( image.getWidth( ) );
		double height = pptMeasure( image.getHeight( ) );
		double x = layoutAreaX2PPT( imageX );
		// double y = layoutAreaY2PPT( imageY, image.getHeight( ) );
		double y = layoutAreaY2PPT( imageY );
		try
		{
			pptOutput.write( ( "<v:shape id=3D'" + ( shapeCount ) + "' type=3D'#r'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			pptOutput.write( ( " style=3D'position:absolute;left:" + x + "pt;top:" + y + "pt;width:" + width + "pt;height:" + height + "pt'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			pptOutput.write( ( " filled=3D'f' stroked=3D'f'>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<v:imagedata src=3D\"" + imageName + "\" o:title=3D\"" + imageTitle + "\"/>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<o:lock v:ext=3D'"
					+ "edit"
					+ "' aspectratio=3D't"
					+ "'/>\n" ).getBytes( ) );
			pptOutput.write( ( "</v:shape>\n" ).getBytes( ) ); //$NON-NLS-1$			
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
	}

	private String getImageExtension( String imageURI )
	{
		String rectifiedImageURI = imageURI.replace( '.', '&' );
		String extension = imageURI.substring( rectifiedImageURI.lastIndexOf( '&' ) + 1 )
				.toLowerCase( );

		if ( extension.equals( "svg" ) )
		{
			extension = "jpg";
		}
		return extension;
	}

	/*
	 * Save the image name into file list of current page
	 */
	private void recordFileLists( String filename )
	{
		Integer pageNum = new Integer( currentPageNum );

		if ( fileNamesLists.containsKey( pageNum ) )
		{
			( (List) fileNamesLists.get( pageNum ) ).add( filename );
		}
		else
		{
			List fileNames = new ArrayList( );
			fileNames.add( filename );
			fileNamesLists.put( pageNum, fileNames );
		}
	}

	/**
	 * Draws a line from the start position to the end position with the given
	 * linewidth, color, and style on the PPT.
	 * 
	 * @param startX
	 *            the start X coordinate of the line
	 * @param startY
	 *            the start Y coordinate of the line
	 * @param endX
	 *            the end X coordinate of the line
	 * @param endY
	 *            the end Y coordinate of the line
	 * @param width
	 *            the lineWidth
	 * @param color
	 *            the color of the line
	 * @param lineStyle
	 *            the given line style
	 */
	private void drawLine( double startX, double startY, double endX,
			double endY, double width, Color color, String lineStyle )
	{
		// if the border does NOT have color or the linewidth of the border
		// is zero
		// or the lineStyle is "none", just return.
		if ( null == color
				|| 0f == width
				|| "none".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			return;
		}
		if ( lineStyle.equalsIgnoreCase( "solid" )
				|| lineStyle.equalsIgnoreCase( "dashed" )
				|| lineStyle.equalsIgnoreCase( "dotted" )
				|| lineStyle.equalsIgnoreCase( "double" ) )
		{
			drawRawLine( startX, startY, endX, endY, width, color, lineStyle );
		}
		else
		{
			// the other line styles, e.g. 'ridge', 'outset', 'groove', 'inset'
			// is NOT supported now.
			// We look it as the default line style -- 'solid'
			drawRawLine( startX, startY, endX, endY, width, color, "solid" );
		}
	}

	/**
	 * Draws a line with the line-style specified in advance from the start
	 * position to the end position with the given linewidth, color, and style
	 * on the PPT. If the line-style is NOT set before invoking this method,
	 * "solid" will be used as the default line-style.
	 * 
	 * @param startX
	 *            the start X coordinate of the line
	 * @param startY
	 *            the start Y coordinate of the line
	 * @param endX
	 *            the end X coordinate of the line
	 * @param endY
	 *            the end Y coordinate of the line
	 * @param width
	 *            the lineWidth
	 * @param color
	 *            the color of the line
	 */
	private void drawRawLine( double startX, double startY, double endX,
			double endY, double width, Color color, String lineStyle )
	{
		// TODO insert a line
		try
		{
			pptOutput.write( ( "<v:line id=3D\"" + ( ++shapeCount ) + "\"" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			pptOutput.write( ( " style=3D'position:absolute' from=3D\"" + startX + "pt," + startY + "pt\"" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			pptOutput.write( ( " to=3D\"" + endX + "pt," + endY + "pt\"" ).getBytes( ) );
			pptOutput.write( ( " strokecolor=3D\"#" + Integer.toHexString( color.getRGB( ) & 0x00ffffff ) + "\"" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( " strokeweight=3D\"" + width + "pt\"" ).getBytes( ) ); //$NON-NLS-1$
			if ( lineStyle.equalsIgnoreCase( "dashed" ) )
			{
				pptOutput.write( ( "<v:stroke dashstyle=3D\"dash\"/>\n" ).getBytes( ) );
			}
			else if ( lineStyle.equalsIgnoreCase( "dotted" ) )
			{
				pptOutput.write( ( "<v:stroke dashstyle=3D\"1 1\"/>\n" ).getBytes( ) );
			}
			else if ( lineStyle.equalsIgnoreCase( "double" ) )
			{
				pptOutput.write( ( "<v:stroke linestyle=3D\"thinThin\"/>\n" ).getBytes( ) );
			}
			else
			{
				pptOutput.write( ( "/>\n" ).getBytes( ) );
				return;
			}
			pptOutput.write( ( ">\n" ).getBytes( ) );
			pptOutput.write( ( "</v:line>\n" ).getBytes( ) );
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
	}

	/**
	 * Draws the background color of the PPT.
	 * 
	 * @param color
	 *            the color to be drawed
	 * @param x
	 *            the start X coordinate
	 * @param y
	 *            the start Y coordinate
	 * @param width
	 *            the width of the background dimension
	 * @param height
	 *            the height of the background dimension
	 */
	private void drawBackgroundColor( Color color, double x, double y,
			double width, double height )
	{
		// TODO set back ground
		try
		{
			pptOutput.write( ( "<v:rect id=3D\"" + ( ++shapeCount ) + "\"" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			pptOutput.write( ( " style=3D'position:absolute;left:"
					+ x
					+ "pt;top:"
					+ y
					+ "pt;width:"
					+ width
					+ "pt;height:"
					+ height + "pt'" ).getBytes( ) );
			pptOutput.write( ( " fillcolor=3D\"#" + Integer.toHexString( color.getRGB( ) & 0x00ffffff ) + "\"" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( " stroked=3D\"f" + "\"/>\n" ).getBytes( ) ); //$NON-NLS-1$			
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
	}

	/**
	 * Draws the backgound image at the contentByteUnder of the PPT with the
	 * given offset
	 * 
	 * @param imageURI
	 *            the URI referring the image
	 * @param x
	 *            the start X coordinate at the PPT where the image is
	 *            positioned
	 * @param y
	 *            the start Y coordinate at the PPT where the image is
	 *            positioned
	 * @param width
	 *            the width of the background dimension
	 * @param height
	 *            the height of the background dimension
	 * @param positionX
	 *            the offset X percentage relating to start X
	 * @param positionY
	 *            the offset Y percentage relating to start Y
	 * @param repeat
	 *            the background-repeat property
	 * @param xMode
	 *            whether the horizontal position is a percentage value or not
	 * @param yMode
	 *            whether the vertical position is a percentage value or not
	 */
	private void drawBackgroundImage( String imageURI, double x, double y,
			double width, double height, double positionX, double positionY,
			String repeat, boolean xMode, boolean yMode )
	{
		// TODO insert a back image
		if ( imageURI == null || imageURI.length( ) == 0 )
		{
			return;
		}
		String imageTitle = "slide"
				+ currentPageNum
				+ "_image"
				+ ( ++shapeCount );
		String imageName;
		if ( imageNames.containsKey( imageURI ) )
		{
			imageName = (String) imageNames.get( imageURI );
		}
		else
		{
			// Save in global image names map
			String extension = getImageExtension( imageURI );
			imageName = imageTitle + "." + extension;
			imageNames.put( imageURI, imageName );
			imageExtensions.put( imageURI, extension );
			recordFileLists( imageName );
			currentImageContents.add( imageURI );
		}
		try
		{
			// pptOutput.write( ( "<v:fill src=3D\"" + imageName + "\"
			// o:title=3D\"" + imageTitle + "\"" ).getBytes( ) ); //$NON-NLS-1$
			// pptOutput.write( ( " opacity=3d\"1\" color2=3D\"white\"
			// o:opacity=3D\"1\" size=3D\""
			// + width
			// + ","
			// + height
			// + "\" aspect=3D\"ignore\" origin=3D\""
			// + x
			// + ","
			// + y + "\"" ).getBytes( ) );
			// pptOutput.write( ( " position=3D\"" + positionX + "," + positionY
			// + "\" recolor=3D\"t\" rotate=3D\"t\" alignshape=3D\"t\"
			// angle=3D\"0\" type=3D\"frame\"/>\n" ).getBytes( ) );
			// pptOutput.write( ( "<o:lock v:ext=3D'"
			// + "edit"
			// + "' aspectratio=3D't"
			// + "'/>\n" ).getBytes( ) );

			pptOutput.write( ( "<v:shape id=3D'" + ( shapeCount ) + "' type=3D'#r'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			pptOutput.write( ( " style=3D'position:absolute;left:" + x + "pt;top:" + y + "pt;width:" + width + "pt;height:" + height + "pt'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			pptOutput.write( ( " filled=3D'f' stroked=3D'f'>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<v:imagedata src=3D\"" + imageName + "\" o:title=3D\"" + imageTitle + "\"/>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<o:lock v:ext=3D'"
					+ "edit"
					+ "' aspectratio=3D't"
					+ "'/>\n" ).getBytes( ) );
			pptOutput.write( ( "</v:shape>\n" ).getBytes( ) ); //$NON-NLS-1$	
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
	}

	/**
	 * Converts the layout measure to PPT, the measure of layout is 1000 times
	 * larger than that of PPT.
	 * 
	 * @param layoutMeasure
	 *            the measure computed in layout manager
	 * @return the measure in PPT
	 */
	private double pptMeasure( float layoutMeasure )
	{
		return layoutMeasure / lToP;
	}

	/**
	 * Converts the X coordinate of a point from layout to X coordinate in PPT.
	 * 
	 * @param layoutX
	 *            the X coordinate specified from layout
	 * @return the PPT X coordinate
	 */
	private double layoutPointX2PPT( int layoutX )
	{
		return pptMeasure( layoutX );
	}

	/**
	 * Converts the Y coordinate of a point from layout to Y coordinate in PPT.
	 * 
	 * @param layoutY
	 *            the Y coordinate specified from layout
	 * @return the PPT Y coordinate
	 */
	private double layoutPointY2PPT( float layoutY )
	{
		return pptMeasure( layoutY );
	}

	/**
	 * Converts the left X coordinate of an Area from layout to the left X
	 * coordinate in PPT.
	 * 
	 * @param layoutX
	 *            the X coordinate specified from layout
	 * @return the PPT X coordinate
	 */
	private double layoutAreaX2PPT( int layoutX )
	{
		return pptMeasure( layoutX );
	}

	/**
	 * Converts the top Y coordinate of an Area from layout to the start Y
	 * coordinate in PPT, to the bottom Y coordinate in PPT.
	 * 
	 * @param layoutY
	 *            the Y coordinate specified from layout
	 * @param areaHeight
	 *            the height of the area whose coordinate need to be converted.
	 *            To text area, the height is from the top of the text to the
	 *            text's baseline.
	 * @return the PPT Y coordinate
	 */
	private double layoutAreaY2PPT( int layoutY )
	{
		return pptMeasure( layoutY );
	}

	/**
	 * The class defines container position of report.
	 */
	private class ContainerPosition
	{

		/** The x postion. */
		private int x;

		/** The y postion. */
		private int y;

		/**
		 * Creates a container position of report.
		 */
		public ContainerPosition( int x, int y )
		{
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * The class defines border info of report.
	 */
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

		public BorderInfo( int startX, int startY, int endX, int endY,
				int borderWidth, Color borderColor, CSSValue borderStyle,
				int borderType )
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
}
