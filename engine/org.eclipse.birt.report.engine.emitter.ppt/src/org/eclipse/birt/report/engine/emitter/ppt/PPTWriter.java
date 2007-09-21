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
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.engine.layout.emitter.util.BackgroundImageLayout;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class PPTWriter
{

	protected static Logger logger = Logger.getLogger( PPTRender.class
			.getName( ) );

	/**
	 * Output stream where postscript to be output.
	 */
	private PrintWriter writer = null;

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
		try
		{
			writer = new PrintWriter(
					new OutputStreamWriter( output, "UTF-8" ), false );
		}
		catch ( UnsupportedEncodingException e )
		{
			assert ( false );
		}
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

		println( "MIME-Version: 1.0" ); //$NON-NLS-1$
		println( "Content-Type: multipart/related; boundary=\"___Actuate_Content_Boundary___\"" ); //$NON-NLS-1$
		println( "" ); //$NON-NLS-1$
		println( "--___Actuate_Content_Boundary___" ); //$NON-NLS-1$
		println( "Content-Location: file:///C:/___Actuate___/slide-show" ); //$NON-NLS-1$
		println( "Content-Transfer-Encoding: quoted-printable" ); //$NON-NLS-1$
		println( "Content-Type: text/html; charset=\"utf-8\"" ); //$NON-NLS-1$
		println( "" ); //$NON-NLS-1$
		println( "<html" ); //$NON-NLS-1$
		println( "xmlns=3D'http://www.w3.org/TR/REC-html40'" ); //$NON-NLS-1$
		println( "xmlns:o=3D'urn:schemas-microsoft-com:office:office'" ); //$NON-NLS-1$
		println( "xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'" ); //$NON-NLS-1$
		println( "xmlns:v=3D'urn:schemas-microsoft-com:vml'" ); //$NON-NLS-1$
		println( ">" ); //$NON-NLS-1$
		println( "<head>" ); //$NON-NLS-1$
		println( "<meta http-equiv=3D'Content-Type' content=3D'text/html; charset=3Dutf-8'>" ); //$NON-NLS-1$
		println( "<meta name=3D'ProgId' content=3D'PowerPoint.Slide'>" ); //$NON-NLS-1$
		println( "<meta name=3D'Generator' content=3D'Actuate View Server'>" ); //$NON-NLS-1$
		println( "<title>Actuate Report</title>" ); //$NON-NLS-1$
		println( "<xml><o:DocumentProperties>" ); //$NON-NLS-1$
		println( "<o:Author>Actuate View Server</o:Author>" ); //$NON-NLS-1$
		println( "</o:DocumentProperties></xml><link rel=3DFile-List href=3D'file-list'>" ); //$NON-NLS-1$
		println( "<link rel=3DPresentation-XML href=3D'presentation'>" ); //$NON-NLS-1$
		println( "</head><body/></html>" ); //$NON-NLS-1$
	}

	private void print( String text )
	{
		writer.print( text );
	}

	private void println( String text )
	{
		writer.println( text );
	}

	private void print( byte[] data )
	{
		print( new String( data ) );
	}

	/**
	 * Closes the document.
	 * 
	 */
	public void end( )
	{
		println( "--___Actuate_Content_Boundary___" ); //$NON-NLS-1$
		println( "Content-Location: file:///C:/___Actuate___/presentation" ); //$NON-NLS-1$
		println( "Content-Transfer-Encoding: quoted-printable" ); //$NON-NLS-1$
		println( "Content-Type: text/xml; charset=\"utf-8\"" ); //$NON-NLS-1$
		println( "" ); //$NON-NLS-1$
		println( "<xml" ); //$NON-NLS-1$
		println( " xmlns:o=3D'urn:schemas-microsoft-com:office:office'" ); //$NON-NLS-1$
		println( " xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'" ); //$NON-NLS-1$
		println( ">" ); //$NON-NLS-1$
		println( ( "<p:presentation sizeof=3D'custom' slidesizex=3D'" + ( pageWidth * 8 ) + "' slidesizey=3D'" + ( pageHeight * 8 ) + "'>" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		for ( int i = 0; i < currentPageNum; i++ )
		{
			println( ( "<p:slide id=3D'" + ( i + 1 ) + "' href=3D's" + ( i + 1 ) + "'/>" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		println( "</p:presentation></xml>" ); //$NON-NLS-1$
		println( "" ); //$NON-NLS-1$
		println( "--___Actuate_Content_Boundary___" ); //$NON-NLS-1$
		println( "Content-Location: file:///C:/___Actuate___/file-list" ); //$NON-NLS-1$
		println( "Content-Transfer-Encoding: quoted-printable" ); //$NON-NLS-1$
		println( "Content-Type: text/xml; charset=\"utf-8\"" ); //$NON-NLS-1$
		println( "<xml" ); //$NON-NLS-1$
		println( " xmlns:o=3D'urn:schemas-microsoft-com:office:office'" ); //$NON-NLS-1$
		println( " xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'" ); //$NON-NLS-1$
		println( ">" ); //$NON-NLS-1$
		println( "<o:MainFile href=3D'slide-show'/>" ); //$NON-NLS-1$
		println( "<o:File href=3D'presentation'/>" ); //$NON-NLS-1$
		println( "<o:File href=3D'file-list'/>" ); //$NON-NLS-1$

		for ( int i = 0; i < currentPageNum; i++ )
		{
			println( ( "<o:File href=3D's" + ( i + 1 ) + "'/>" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			if ( fileNamesLists.containsKey( new Integer( i + 1 ) ) )
			{
				List filenames = (List) fileNamesLists
						.get( new Integer( i + 1 ) );
				for ( Iterator ite = filenames.iterator( ); ite.hasNext( ); )
				{
					println( ( "<o:File href=3D\"" + (String) ite.next( ) + "\"/>" ) );
				}
			}
		}

		println( "</xml>" ); //$NON-NLS-1$
		println( "" ); //$NON-NLS-1$
		println( "--___Actuate_Content_Boundary___--" ); //$NON-NLS-1$
		writer.close( );
		writer = null;
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
				println( "\n" );
			}
			println( "</p:slide></body></html>" ); //$NON-NLS-1$
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
	}

	private void exportImageHeader( String imagekey ) throws IOException
	{
		println( "" );
		println( "--___Actuate_Content_Boundary___" ); //$NON-NLS-1$
		println( "Content-Location: file:///C:/___Actuate___/"
				+ (String) imageNames.get( imagekey ) + "" );
		println( "Content-Transfer-Encoding: base64" );
		println( "Content-Type: image/"
				+ (String) imageExtensions.get( imagekey ) + "\n" );
	}

	private void generateImageBytes( String imageTitle, byte[] imageData )
			throws IOException
	{
		exportImageHeader( imageTitle );

		Base64 base = new Base64( );
		print( base.encode( imageData ) );
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

		println( "--___Actuate_Content_Boundary___" ); //$NON-NLS-1$
		println( "Content-Location: file:///C:/___Actuate___/s" + currentPageNum + "" ); //$NON-NLS-1$ //$NON-NLS-2$
		println( "Content-Transfer-Encoding: quoted-printable" ); //$NON-NLS-1$
		println( "Content-Type: text/html; charset=\"utf-8\"" ); //$NON-NLS-1$
		println( "" ); //$NON-NLS-1$
		println( "<html" ); //$NON-NLS-1$
		println( " xmlns=3D'http://www.w3.org/TR/REC-html40'" ); //$NON-NLS-1$
		println( " xmlns:o=3D'urn:schemas-microsoft-com:office:office'" ); //$NON-NLS-1$
		println( " xmlns:p=3D'urn:schemas-microsoft-com:office:powerpoint'" ); //$NON-NLS-1$
		println( " xmlns:v=3D'urn:schemas-microsoft-com:vml'" ); //$NON-NLS-1$
		println( ">" ); //$NON-NLS-1$
		println( "<head/><body><p:slide>" ); //$NON-NLS-1$
		println( "<meta http-equiv=3D'Content-Type' content=3D'text/html; charset=3Dutf-8'>" ); //$NON-NLS-1$
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
			float height, FontInfo fontInfo, Color color )
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

		println( "<v:shape id=3D't" + ( ++shapeCount ) + "' type=3D'#r'" ); //$NON-NLS-1$ //$NON-NLS-2$
		println( " style=3D'position:absolute;left:" + textX + "pt;top:" + textY + "pt;width:" + width + "pt;height:" + height + "pt;v-text-anchor:top;mso-wrap-style:square;'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		println( " filled=3D'f' stroked=3D'f'>" ); //$NON-NLS-1$
		println( "<v:textbox style=3D'mso-fit-shape-to-text:f;' inset=3D'0.00pt 0.00pt 0.00pt 0.00pt'/>" ); //$NON-NLS-1$
		println( "</v:shape>" ); //$NON-NLS-1$
		println( "<div v:shape=3D't" + shapeCount + "'>" ); //$NON-NLS-1$ //$NON-NLS-2$

		println( "<div style=3D'mso-text-indent-alt:" //$NON-NLS-1$
				+ 0 + ";text-align:left;'>" //$NON-NLS-1$
				+ "<span style=3D'font-family:" //$NON-NLS-1$
				+ fontName + ";font-size:" //$NON-NLS-1$
				+ fontInfo.getFontSize( ) + "pt;color:#" //$NON-NLS-1$
				+ red + green + blue + ";'>" ); //$NON-NLS-1$
		// + text.getText( ) + "</span></div>\n" );

		boolean isItalic = fontInfo != null
				&& ( fontInfo.getFontStyle( ) & Font.ITALIC ) != 0;
		boolean isBold = fontInfo != null
				&& ( fontInfo.getFontStyle( ) & Font.BOLD ) != 0;
		if ( isItalic )
		{
			print( "<i>" );
		}
		if ( isBold )
		{
			print( "<b>" );
		}
		print( text );
		if ( isBold )
		{
			print( "</b>" );
		}
		if ( isItalic )
		{
			print( "</i>" );
		}
		println( "</span></div>" ); //$NON-NLS-1$
		println( "</div>" ); //$NON-NLS-1$
	}

	public void drawImage( byte[] imageData, String extension, float imageX,
			float imageY, float height, float width, String helpText )
			throws Exception
	{
		String imageName;
		String imageTitle = "slide" + currentPageNum + "_image"
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
		println( "<v:shape id=3D'" + ( shapeCount ) + "' type=3D'#r'" ); //$NON-NLS-1$ //$NON-NLS-2$
		println( " style=3D'position:absolute;left:" + x + "pt;top:" + y + "pt;width:" + width + "pt;height:" + height + "pt'" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		println( " filled=3D'f' stroked=3D'f'>" ); //$NON-NLS-1$
		println( "<v:imagedata src=3D\"" + imageName + "\" o:title=3D\"" + imageTitle + "\"/>" ); //$NON-NLS-1$
		println( "<o:lock v:ext=3D'" + "edit" + "' aspectratio=3D't" + "'/>" );
		println( "</v:shape>" ); //$NON-NLS-1$			
	}

	private String getImageExtension( String imageURI )
	{
		String rectifiedImageURI = imageURI.replace( '.', '&' );
		String extension = imageURI.substring(
				rectifiedImageURI.lastIndexOf( '&' ) + 1 ).toLowerCase( );

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
		if ( null == color || 0f == width
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
		print( "<v:line id=3D\"" + ( ++shapeCount ) + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$
		print( " style=3D'position:absolute' from=3D\"" + startX + "pt," + startY + "pt\"" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		print( " to=3D\"" + endX + "pt," + endY + "pt\"" );
		print( " strokecolor=3D\"#" + Integer.toHexString( color.getRGB( ) & 0x00ffffff ) + "\"" ); //$NON-NLS-1$
		print( " strokeweight=3D\"" + width + "pt\"" ); //$NON-NLS-1$
		if ( lineStyle.equalsIgnoreCase( "dashed" ) )
		{
			println( "<v:stroke dashstyle=3D\"dash\"/>" );
		}
		else if ( lineStyle.equalsIgnoreCase( "dotted" ) )
		{
			println( "<v:stroke dashstyle=3D\"1 1\"/>" );
		}
		else if ( lineStyle.equalsIgnoreCase( "double" ) )
		{
			println( "<v:stroke linestyle=3D\"thinThin\"/>" );
		}
		else
		{
			println( "/>" );
			return;
		}
		println( ">" );
		println( "</v:line>" );
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
		print( "<v:rect id=3D\"" + ( ++shapeCount ) + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$
		print( " style=3D'position:absolute;left:" + x + "pt;top:" + y
				+ "pt;width:" + width + "pt;height:" + height + "pt'" );
		print( " fillcolor=3D\"#" + Integer.toHexString( color.getRGB( ) & 0x00ffffff ) + "\"" ); //$NON-NLS-1$
		println( " stroked=3D\"f" + "\"/>" ); //$NON-NLS-1$			
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
	public void drawBackgroundImage( String imageURI, float x, float y,
			float width, float height, float positionX, float positionY,
			String repeat )
	{
		// TODO insert a back image
		if ( imageURI == null || imageURI.length( ) == 0 )
		{
			return;
		}
		float imageWidth = 0;
		float imageHeight = 0;
		byte[] imageData = null;
		InputStream imageStream = null;
		try
		{
			URL url = new URL( imageURI );
			imageStream = url.openStream( );
			imageData = getImageData( imageStream );
			imageStream.close( );
			imageStream = url.openStream( );
			Image image = ImageIO.read( imageStream );
			ImageIcon imageIcon = new ImageIcon( image );
			imageWidth = imageIcon.getIconWidth( );
			imageHeight = imageIcon.getIconHeight( );
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
		finally
		{
			if ( imageStream != null )
			{
				try
				{
					imageStream.close( );
				}
				catch ( IOException e )
				{
				}
			}
		}

		String imageTitle = "slide" + currentPageNum + "_image"
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

		Position areaPosition = new Position( x, y );
		Position areaSize = new Position( width, height );
		Position imagePosition = new Position( x + positionX, y + positionY );
		// TODO: need to confirm the tansformation from unit pixel to
		// pointer.
		Position imageSize = new Position( imageWidth, imageHeight );
		BackgroundImageLayout layout = new BackgroundImageLayout( areaPosition,
				areaSize, imagePosition, imageSize );
		Collection positions = layout.getImagePositions( repeat );
		Iterator iterator = positions.iterator( );
		while ( iterator.hasNext( ) )
		{
			Position position = (Position) iterator.next( );
			exportImageDefn( imageName, imageTitle, imageWidth, imageHeight,
					position.getX( ), position.getY( ) );
		}
	}

	private byte[] getImageData( InputStream imageStream ) throws IOException
	{
		byte[] imageData;
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		int data = -1;
		while( (data = imageStream.read( ) ) >=0)
		{
			byteArrayStream.write( data );
		}
		imageData = byteArrayStream.toByteArray( );
		return imageData;
	}
}
