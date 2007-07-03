/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt;

import java.awt.Color;
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
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class PPTWriter
{

	protected static Logger logger = Logger.getLogger( PPTRender.class.getName( ) );

	/**
	 * Output stream where postscript to be output.
	 */
	// protected PrintStream pptOutput = System.out;
	private OutputStream pptOutput = null;

	protected int currentPageNum = 0;
	private int shapeCount = 0;

	protected float pageWidth, pageHeight;

	// Holds all the images' name appears in report design
	private Map imageNames = new HashMap( );

	private List imageTitles = new ArrayList( );

	// Holds the extension types of all images
	private Map imageExtensions = new HashMap( );

	// Holds the files' name for each page
	private Map fileNamesLists = new TreeMap( );

	private Map currentImageData = new HashMap( );;

	public PPTWriter( OutputStream output )
	{
		// pptOutput = new PrintStream( output );
		pptOutput = output;
	}

	/**
	 * Creates a PPT Document.
	 * 
	 */
	public void start( )
	{
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
	 */
	public void end( )
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

	public void endPage( )
	{
		try
		{
			// Write out the image bytes
			for ( Iterator ite = imageTitles.iterator( ); ite.hasNext( ); )
			{
				String imageTitle = (String) ite.next( );
				byte[] imageData = (byte[]) currentImageData.get( imageTitle );
				generateImageBytes( imageTitle, imageData );
				pptOutput.write( "\n\n".getBytes( ) );
			}
			pptOutput.write( "</p:slide></body></html>\n".getBytes( ) ); //$NON-NLS-1$
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
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

	private void generateImageBytes( String imageTitle, byte[] imageData )
			throws IOException
	{
		exportImageHeader( imageTitle );

		Base64 base = new Base64( );
		pptOutput.write( base.encode( imageData ) );
	}

	/**
	 * Creates a new page.
	 * 
	 * @param page
	 *            the PageArea specified from layout
	 */
	public void newPage( float pageWidth, float pageHeight,
			Color backgroundColor )
	{
		currentPageNum++;
		currentImageData.clear( );
		imageTitles.clear( );
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;

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

		drawBackgroundColor( backgroundColor, 0, 0, pageWidth, pageHeight );
	}

	private String getFontName( BaseFont baseFont )
	{
		String[][] familyFontNames = baseFont.getFamilyFontName( );
		String[] family = familyFontNames[0];
		String fontName = family[family.length - 1];
		return fontName;
	}

	/**
	 * Draws a chunk of text on the PPT.
	 * 
	 * @param text
	 *            the textArea to be drawn.
	 * @param textX
	 *            the X position of the textArea relative to current page.
	 * @param textY
	 *            the Y position of the textArea relative to current page.
	 * @param contentByte
	 *            the content byte to draw the text.
	 * @param contentByteHeight
	 *            the height of the content byte.
	 */
	public void drawText( String text, float textX, float textY, float width,
			float height, FontInfo fontInfo, Color color, boolean linethrough,
			boolean overline, boolean underline )
	{

		BaseFont baseFont = fontInfo.getBaseFont( );
		String fontName = getFontName( baseFont );

		// splits font-family list
		// CSSValueList fontFamilies = fontInfo.(CSSValueList)
		// style.getProperty( StyleConstants.STYLE_FONT_FAMILY );
		String red = Integer.toHexString( color.getRed( ) );
		String green = Integer.toHexString( color.getGreen( ) );
		String blue = Integer.toHexString( color.getBlue( ) );

		red = red.length( ) == 1 ? "0" + red : red; //$NON-NLS-1$
		green = green.length( ) == 1 ? "0" + green : green; //$NON-NLS-1$
		blue = blue.length( ) == 1 ? "0" + blue : blue; //$NON-NLS-1$

		Charset charset = Charset.forName( "UTF-8" );
		ByteBuffer encodedText = charset.encode( text );
		try
		{
			pptOutput.write( ( "<v:shape id=3D't" + ( ++shapeCount ) + "' type=3D'#r'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			pptOutput.write( ( " style=3D'position:absolute;left:" + textX + "pt;top:" + textY + "pt;width:" + width + "pt;height:" + height + "pt;v-text-anchor:top;mso-wrap-style:square;'\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			pptOutput.write( ( " filled=3D'f' stroked=3D'f'>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<v:textbox style=3D'mso-fit-shape-to-text:f;' inset=3D'0.00pt 0.00pt 0.00pt 0.00pt'/>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "</v:shape>\n" ).getBytes( ) ); //$NON-NLS-1$
			pptOutput.write( ( "<div v:shape=3D't" + shapeCount + "'>\n" ).getBytes( ) ); //$NON-NLS-1$ //$NON-NLS-2$

			pptOutput.write( ( "<div style=3D'mso-text-indent-alt:" //$NON-NLS-1$
					+ 0
					+ ";text-align:left;'>" //$NON-NLS-1$
					+ "<span style=3D'font-family:" //$NON-NLS-1$
					+ fontName
					+ ";font-size:" //$NON-NLS-1$
					+ fontInfo.getFontSize( )
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

		// draw the over line,through line or underline for the text if it has
		// any.
		if ( linethrough )
		{
			drawLine( textX,
					textY + fontInfo.getLineThroughPosition( ),
					textX + width,
					textY + fontInfo.getLineThroughPosition( ),
					width,
					color,
					"solid" ); //$NON-NLS-1$
		}
		if ( overline )
		{
			drawLine( textX,
					textY + fontInfo.getOverlinePosition( ),
					textX + width,
					textY + fontInfo.getOverlinePosition( ),
					width,
					color,
					"solid" ); //$NON-NLS-1$
		}
		if ( underline )
		{
			drawLine( textX,
					textY + fontInfo.getUnderlinePosition( ),
					textX + width,
					textY + fontInfo.getUnderlinePosition( ),
					width,
					color,
					"solid" ); //$NON-NLS-1$
		}
	}

	public void drawImage( byte[] imageData, String extension, float imageX,
			float imageY, float height, float width, String helpText )
			throws Exception
	{
		String imageName;
		String imageTitle = "slide"
				+ currentPageNum
				+ "_image"
				+ ( ++shapeCount );
		imageTitles.add( imageTitle );

		if ( imageNames.containsKey( imageTitle ) )
		{
			imageName = (String) imageNames.get( imageTitle );
		}
		else
		{
			// Save in global image names map
			imageName = imageTitle + "." + extension;
			imageNames.put( imageTitle, imageName );
			imageExtensions.put( imageTitle, extension );
			recordFileLists( imageName );
			currentImageData.put( imageTitle, imageData );
		}
		exportImageDefn( imageName, imageTitle, width, height, imageX, imageY );
	}

	/**
	 * @param imageName
	 * @param imageTitle
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 */
	private void exportImageDefn( String imageName, String imageTitle,
			double width, double height, double x, double y )
	{
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
	 * line width, color, and style on the PPT.
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
	public void drawLine( double startX, double startY, double endX,
			double endY, double width, Color color, String lineStyle )
	{
		// if the border does NOT have color or the line width of the border
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
			// the other line styles, e.g. 'ridge', 'outset', 'groove', 'insert'
			// is NOT supported now.
			// We look it as the default line style -- 'solid'
			drawRawLine( startX, startY, endX, endY, width, color, "solid" );
		}
	}

	/**
	 * Draws a line with the line-style specified in advance from the start
	 * position to the end position with the given line width, color, and style
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
	 *            the color to be drawn
	 * @param x
	 *            the start X coordinate
	 * @param y
	 *            the start Y coordinate
	 * @param width
	 *            the width of the background dimension
	 * @param height
	 *            the height of the background dimension
	 */
	public void drawBackgroundColor( Color color, double x, double y,
			double width, double height )
	{
		if ( color == null )
		{
			return;
		}
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
	 * Draws the background image at the contentByteUnder of the PPT with the
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
	public void drawBackgroundImage( String imageURI, double x, double y,
			double width, double height, double positionX, double positionY,
			String repeat )
	{
		// TODO insert a back image
		if ( imageURI == null || imageURI.length( ) == 0 )
		{
			return;
		}
		byte[] imageData = null;
		try
		{
			InputStream imageStream = new URL( imageURI ).openStream( );
			imageData = new byte[imageStream.available( )];
			imageStream.read( imageData );
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}

		String imageTitle = "slide"
				+ currentPageNum
				+ "_image"
				+ ( ++shapeCount );
		imageTitles.add( imageTitle );
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
			imageNames.put( imageTitle, imageName );
			imageExtensions.put( imageTitle, extension );
			recordFileLists( imageName );
			currentImageData.put( imageTitle, imageData );
		}
		exportImageDefn( imageName, imageTitle, width, height, x, y );
	}

}
