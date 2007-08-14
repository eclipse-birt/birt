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

package org.eclipse.birt.report.engine.emitter.postscript;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.ITrueTypeWriter;
import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.TrueTypeFont;
import org.eclipse.birt.report.engine.emitter.postscript.util.FileUtil;
import org.eclipse.birt.report.engine.layout.emitter.util.BackgroundImageLayout;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
import org.eclipse.birt.report.engine.layout.pdf.font.FontHandler;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.pdf.BaseFont;

public class PostscriptWriter
{

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER = BaseFont.COURIER;

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER_BOLD = BaseFont.COURIER_BOLD;

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER_OBLIQUE = BaseFont.COURIER_OBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String COURIER_BOLDOBLIQUE = BaseFont.COURIER_BOLDOBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA = BaseFont.HELVETICA;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA_BOLD = BaseFont.HELVETICA_BOLD;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA_OBLIQUE = BaseFont.HELVETICA_OBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String HELVETICA_BOLDOBLIQUE = BaseFont.HELVETICA_BOLDOBLIQUE;

	/** This is a possible value of a base 14 type 1 font */
	public static final String SYMBOL = BaseFont.SYMBOL;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES = "Times";

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_ROMAN = BaseFont.TIMES_ROMAN;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_BOLD = BaseFont.TIMES_BOLD;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_ITALIC = BaseFont.TIMES_ITALIC;

	/** This is a possible value of a base 14 type 1 font */
	public static final String TIMES_BOLDITALIC = BaseFont.TIMES_BOLDITALIC;

	/** This is a possible value of a base 14 type 1 font */
	public static final String ZAPFDINGBATS = BaseFont.ZAPFDINGBATS;

	/**
	 * Default page height.
	 */
	final protected static int DEFAULT_PAGE_HEIGHT = 792;
	/**
	 * Default page width.
	 */
	final protected static int DEFAULT_PAGE_WIDTH = 612;
	/**
	 * Table mapping decimal numbers to hexadecimal numbers.
	 */
	final protected static byte hd[] = {'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	/**
	 * Output stream where postscript to be output.
	 */
	protected PrintStream out = System.out;
	/**
	 * The current color
	 */
	protected Color clr = Color.white;
	/**
	 * The current font
	 */
	protected Font font = new Font( Font.HELVETICA, 12, Font.NORMAL );

	/**
	 * Current page index with 1 as default value.
	 */
	private int pageIndex = 1;
	
	/**
	 * Height of current page.
	 */
	private float pageHeight = DEFAULT_PAGE_HEIGHT;
	
	private static Set intrinsicFonts = new HashSet( );

	static
	{
		intrinsicFonts.add( COURIER );
		intrinsicFonts.add( COURIER_BOLD );
		intrinsicFonts.add( COURIER_OBLIQUE );
		intrinsicFonts.add( COURIER_BOLDOBLIQUE );
		intrinsicFonts.add( HELVETICA );
		intrinsicFonts.add( HELVETICA_BOLD );
		intrinsicFonts.add( HELVETICA_OBLIQUE );
		intrinsicFonts.add( HELVETICA_BOLDOBLIQUE );
		intrinsicFonts.add( SYMBOL );
		intrinsicFonts.add( TIMES );
		intrinsicFonts.add( TIMES_ROMAN );
		intrinsicFonts.add( TIMES_BOLD );
		intrinsicFonts.add( TIMES_ITALIC );
		intrinsicFonts.add( TIMES_BOLDITALIC );
		intrinsicFonts.add( ZAPFDINGBATS );
	}

	public static boolean isIntrinsicFont( String fontName )
	{
		return intrinsicFonts.contains( fontName );
	}

	/**
	 * Constructor.
	 * 
	 * @param out
	 *            Output stream for PostScript output.
	 * @param title
	 *            title of the postscript document.
	 */
	public PostscriptWriter( OutputStream o, String title )
	{
		out = new PrintStream( o );
		emitProlog( title );
		FontHandler.prepareFonts( );
	}

	public void clipRect( float x, float y, float width, float height )
	{
		y = transformY( y );
		
		out.println( x + " " + y + " moveto" );
		out.println( ( x + width ) + " " + y + " lineto" );
		out.println( ( x + width ) + " " + ( y - height ) + " lineto" );
		out.println( x + " " + ( y - height ) + " lineto" );
		out.println( x + " " + y + " lineto" );

		out.println( "closepath eoclip newpath" );
	}

	public void clipSave()
	{
		out.println( "gsave" );
	}
	
	public void clipRestore()
	{
		out.println( "grestore" );
	}
	
	/**
	 * Draws a image.
	 * 
	 * @param imageStream
	 *            the source input stream of the image.
	 * @param x
	 *            the x position.
	 * @param y
	 *            the y position.
	 * @param width
	 *            the image width.
	 * @param height
	 *            the image height.
	 * @throws IOException
	 */
	public void drawImage( InputStream imageStream, float x, float y,
			float width, float height ) throws Exception
	{
		Image image = ImageIO.read( imageStream );
		drawImage( image, x, y, width, height, null );
	}

	/**
	 * Draws a image with specified image data, position, size and background
	 * color.
	 * 
	 * @param image
	 *            the source image data.
	 * @param x
	 *            the x position.
	 * @param y
	 *            the y position.
	 * @param width
	 *            the image width.
	 * @param height
	 *            the image height.
	 * @param bgcolor
	 *            the background color.
	 * @throws Exception
	 */
	public void drawImage( Image image, float x, float y, float width,
			float height, Color bgcolor ) throws Exception
	{
		if ( image == null )
		{
			throw new IllegalArgumentException( "null image." );
		}
		ImageSource imageSource = getImageSource( image );
		drawImage( imageSource, x, y, width, height, bgcolor );
	}

	private ArrayImageSource getImageSource( Image image ) throws IOException
	{
		ImageIcon imageIcon = new ImageIcon( image );
		int w = imageIcon.getIconWidth( );
		int h = imageIcon.getIconHeight( );
		int[] pixels = new int[w * h];

		try
		{
			PixelGrabber pg = new PixelGrabber( image, 0, 0, w, h, pixels, 0, w );
			pg.grabPixels( );
			if ( ( pg.getStatus( ) & ImageObserver.ABORT ) != 0 )
			{
				throw new IOException( "failed to load image contents" );
			}
		}
		catch ( InterruptedException e )
		{
			throw new IOException( "image load interrupted" );
		}

		ArrayImageSource imageSource = new ArrayImageSource( w, h, pixels );
		return imageSource;
	}

	private void drawImage( ImageSource imageSource, float x, float y,
			float width, float height, Color bgcolor )
	{
		int originalWidth = imageSource.getWidth( );
		int originalHeight = imageSource.getHeight( );
		y = transformY( y );
		gSave( );
		out.println( "% build a temporary dictionary" );
		out.println( "20 dict begin" );
		out.print( "/pix " );
		out.print( originalWidth * 3 );
		out.println( " string def" );

		out.println( "% define space for color conversions" );
		out.print( "/grays " );
		out.print( originalWidth );
		out.println( " string def  % space for gray scale line" );

		out.println( "% lower left corner" );
		out.print( x );
		out.print( " " );
		out.print( y );
		out.println( " translate" );

		if ( height == 0 || width == 0 )
		{
			height = originalHeight;
			width = originalWidth;
		}

		out.println( "% size of image" );
		out.print( width );
		out.print( " " );
		out.print( height );
		out.println( " scale" );

		out.print( originalWidth );
		out.print( " " );
		out.print( originalHeight );
		out.println( " 8" );

		out.print( "[" );
		out.print( originalWidth );
		out.print( " 0 0 -" );
		out.print( originalHeight );
		out.print( " 0 " );
		out.print( 0 );
		out.println( "]" );

		out.println( "{currentfile pix readhexstring pop}" );
		out.println( "false 3 colorimage" );
		out.println( "" );

		byte[] sb = new byte[originalHeight * originalWidth * 6];
		int offset = 0;
		for ( int i = 0; i < originalHeight; i++ )
		{
			if ( bgcolor == null )
			{
				for ( int j = 0; j < originalWidth; j++ )
				{
					int pixel = imageSource.getRGB( j, i );
					int alpha = ( pixel >> 24 ) & 0xff;
					int red = ( pixel >> 16 ) & 0xff;
					int green = ( pixel >> 8 ) & 0xff;
					int blue = pixel & 0xff;
					red = transferColor( alpha, red );
					green = transferColor( alpha, green );
					blue = transferColor( alpha, blue );
					offset = toBytes( offset, sb, red );
					offset = toBytes( offset, sb, green );
					offset = toBytes( offset, sb, blue );
				}
			}
			else
			{
				// TODO:implement or remove it.
			}
		}
		out.println( new String( sb ) );
		out.println( "" );
		out.println( "end" );
		gRestore( );
	}

	private int transferColor( int alpha, int color )
	{
		return 255 - (255 - color) * alpha / 255;
	}
	
	private int toBytes( int offset, byte[] buffer, int value )
	{
		buffer[offset++] = hd[( ( value & 0xF0 ) >> 4 )];
		buffer[offset++] = hd[( value & 0xF )];
		return offset;
	}

	protected void drawRect( float x, float y, float width, float height,
			boolean fill )
	{
		drawRawRect( x, y, width, height );
		if ( fill )
			out.println( "fill" );
		else
			out.println( "stroke" );

	}

	private void drawRawRect( float x, float y, float width, float height )
	{
		y = transformY( y ) - height;
		out.print( x );
		out.print( " " );
		out.print( y );
		out.println( " moveto " );
		out.print( width );
		out.print( " " );
		out.print( 0 );
		out.println( " rlineto " );
		out.print( 0 );
		out.print( " " );
		out.print( height );
		out.println( " rlineto " );
		out.print( -width );
		out.print( " " );
		out.print( 0 );
		out.println( " rlineto " );
		out.print( 0 );
		out.print( " " );
		out.print( -height );
		out.println( " rlineto " );
	}

	/**
	 * Draws background image in a rectangle area with specified repeat pattern.
	 * <br>
	 * <br>
	 * The repeat mode can be: <table border="solid">
	 * <tr>
	 * <td align="center"><B>Name</td>
	 * <td align="center"><B>What for</td>
	 * </tr>
	 * <tr>
	 * <td>no-repeat</td>
	 * <td>Don't repeat.</td>
	 * </tr>
	 * <tr>
	 * <td>repeat-x</td>
	 * <td>Only repeat on x orientation.</td>
	 * </tr>
	 * <tr>
	 * <td>repeat-y</td>
	 * <td>Only repeat on y orientation.</td>
	 * </tr>
	 * <tr>
	 * <td>repeat</td>
	 * <td>Repeat on x and y orientation.</td>
	 * </tr>
	 * </table>
	 * 
	 * @param imageURI
	 *            the uri of the background image.
	 * @param x
	 *            the x coordinate of the rectangle area.
	 * @param y
	 *            the y coordinate of the rectangle area.
	 * @param width
	 *            the width of the rectangle area.
	 * @param height
	 *            the height of the rectangle area.
	 * @param positionX
	 *            the initial x position of the background image.
	 * @param positionY
	 *            the initial y position of the background image.
	 * @param repeat
	 *            the repeat mode.
	 * @throws IOException
	 *             when the iamge cann't be read from the url.
	 */
	public void drawBackgroundImage( String imageURI, float x, float y,
			float width, float height, float positionX, float positionY,
			String repeat ) throws IOException
	{
		URL url = new URL( imageURI );
		InputStream imageStream = null;
		try
		{
			imageStream = url.openStream( );
			Image image = ImageIO.read( imageStream );
			ImageSource imageSource = getImageSource( image );

			Position areaPosition = new Position( x, y );
			Position areaSize = new Position( width, height );
			Position imagePosition = new Position( x + positionX, y + positionY );
			// TODO: need to confirm the tansformation from unit pixel to
			// pointer.
			Position imageSize = new Position( imageSource.getWidth( ),
					imageSource.getHeight( ) );
			BackgroundImageLayout layout = new BackgroundImageLayout(
					areaPosition, areaSize, imagePosition, imageSize );
			Collection positions = layout.getImagePositions( repeat );
			gSave( );
			out.println( "clipsave" );
			setColor( Color.WHITE );
			out.println( "newpath" );
			drawRawRect( x, y, width, height );
			out.println( "closepath clip" );
			Iterator iterator = positions.iterator( );
			while ( iterator.hasNext( ) )
			{
				Position position = (Position) iterator.next( );
				drawImage( imageSource, position.getX( ), position.getY( ),
						imageSize.getX( ), imageSize.getY( ), null );
			}
			out.println( "cliprestore" );
			gRestore( );
		}
		finally
		{
			if ( imageStream != null )
			{
				imageStream.close( );
				imageStream = null;
			}
		}
	}

	/**
	 * Draws a line from (startX, startY) to (endX, endY).
	 * 
	 * @param startX
	 *            the x coordinate of start point.
	 * @param startY
	 *            the y coordinate of start point.
	 * @param endX
	 *            the x coordinate of end point.
	 * @param endY
	 *            the y coordinate of end point.
	 */
	public void drawLine( float startX, float startY, float endX, float endY )
	{
		startY = transformY( startY );
		endY = transformY( endY );
		out.print( startX );
		out.print( " " );
		out.print( startY );
		out.print( " moveto " );
		out.print( endX );
		out.print( " " );
		out.print( endY );
		out.println( " lineto stroke" );
	}

	/**
	 * Draws a line from (startX, startY) to (endX, endY) with specified line
	 * width, color and line style.
	 * 
	 * Line style can be "dotted", "dash", and "double".
	 * 
	 * @param startX
	 *            the x coordinate of start point.
	 * @param startY
	 *            the y coordinate of start point.
	 * @param endX
	 *            the x coordinate of end point.
	 * @param endY
	 *            the y coordinate of end point.
	 * @param width
	 *            the line width.
	 * @param color
	 *            the color.
	 * @param lineStyle
	 *            the line style.
	 */
	public void drawLine( float startX, float startY, float endX, float endY,
			float width, Color color, String lineStyle )
	{
		if ( null == color || 0f == width
				|| "none".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			return;
		}
		// double is not supported.
		if ( "double".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			return;
		}
		gSave( );
		if ( color != null )
		{
			setColor( color );
		}
		setLineWidth( width );
		if ( "dashed".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			setDashLine( width );
		}
		else if ( "dotted".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			setDottedLine( width );
		}
		drawLine( startX, startY, endX, endY );
		gRestore( );
	}

	private void gRestore( )
	{
		out.println( "grestore" );
	}

	private void gSave( )
	{
		out.println( "gsave" );
	}

	private void setLineWidth( float lineWidth )
	{
		out.print( lineWidth );
		out.println( " setlinewidth" );
	}

	private void setDashLine( float lineWidth )
	{
		int width = (int) Math.ceil( lineWidth );
		out.println( "[" + 3 * width + " " + 2 * width + "] 0 setdash" );
	}

	private void setDottedLine( float lineWidth )
	{
		int width = (int) Math.ceil( lineWidth );
		out.println( "[" + width + "] 0 setdash" );
	}

	private Map trueTypeFontWriters = new HashMap( );

	public void drawRect( int x, int y, int width, int height )
	{
		out.println( "%drawRect" );
		drawRect( x, y, width, height, false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#drawString(java.lang.String,
	 *      int, int, org.eclipse.birt.report.engine.layout.pdf.font.FontInfo,
	 *      float, float, java.awt.Color, boolean, boolean, boolean)
	 */
	public void drawString( String str, float x, float y, FontInfo fontInfo,
			float letterSpacing, float wordSpacing, Color color,
			boolean linethrough, boolean overline, boolean underline,
			CSSValue align )
	{
		y = transformY( y );
		gSave( );
		String text = str;
		if ( fontInfo != null )
		{
			BaseFont baseFont = fontInfo.getBaseFont( );
			String fontName = baseFont.getPostscriptFontName( );
			text = applyFont( fontName, fontInfo.getFontStyle( ), fontInfo
					.getFontSize( ), text );
		}
		color = color == null ? Color.black : color;
		setColor( color );
		out.print( x );
		out.print( " " );
		out.print( y );
		out.print( " moveto " );
		out.print( wordSpacing );
		out.print( " 0 8#040 " );
		out.print( letterSpacing );
		out.print( " 0 " );
		out.print( text );
		out.println( " awidthshow stroke" );
		gRestore( );
	}

	/**
	 * Top of every PS file
	 */

	protected void emitProlog( String title )
	{
		out.println( "%!PS-Adobe-3.0" );
		if ( title != null )
		{
			out.println( "%%Title: " + title );
		}
		out.println( "% (C)2006 Actuate Inc." );
		setFont( font );
	}

	public void fillRect( float x, float y, float width, float height,
			Color color )
	{
		gSave( );
		setColor( color );
		out.println( "%fillRect" );
		drawRect( x, y, width, height, true );
		gRestore( );
	}

	/**
	 * Disposes of this graphics context once it is no longer referenced.
	 * 
	 * @see #dispose
	 */

	public void finalize( )
	{
		dispose( );
	}

	public void dispose( )
	{
		out.println( "%dispose" );
	}

	public Color getColor( )
	{
		return clr;
	}

	public Font getFont( )
	{
		return font;
	}

	public void scale( float sx, float sy )
	{
		out.print( sx );
		out.print( " " );
		out.print( sy );
		out.println( " scale" );
	}

	public void setColor( Color c )
	{
		if ( c == null )
		{
			return;
		}
		out.print( c.getRed( ) / 255.0 );
		out.print( " " );
		out.print( c.getGreen( ) / 255.0 );
		out.print( " " );
		out.print( c.getBlue( ) / 255.0 );
		out.println( " setrgbcolor" );
	}

	public void setFont( Font f )
	{
		if ( f != null )
		{
			this.font = f;
			String javaName = font.getFamilyname( );
			int javaStyle = font.style( );
			setFont( javaName, javaStyle );
		}
	}

	private void setFont( String psName, float size )
	{
		out.println( "/" + psName + " findfont" );
		out.print( size );
		out.println( " scalefont setfont" );
	}

	private String applyFont( String fontName, int fontStyle, float fontSize,
			String text )
	{
		if ( isIntrinsicFont( fontName ) )
		{
			return applyIntrinsicFont( fontName, fontStyle, fontSize, text );
		}
		else
		{
			try
			{
				String fontPath = getFontPath( fontName );
				if ( fontPath == null )
				{
					return applyIntrinsicFont( fontName, fontStyle, fontSize, text );
				}
				ITrueTypeWriter trueTypeWriter = getTrueTypeFontWriter( fontPath );
				
				//Space can't be included in a identity.
				String displayName = fontName.replace( ' ', '_' );
				trueTypeWriter.useDisplayName( displayName );
				trueTypeWriter.ensureGlyphsAvailable( text );
				setFont( displayName, fontSize );
				return toHexString( text );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
			return null;
		}
	}

	private String applyIntrinsicFont( String fontName, int fontStyle, float fontSize, String text )
	{
		setFont( fontName, fontSize );
		if ( text.endsWith( "\\" ) )
		{
			text = text + "\\";
		}
		text = text.replaceAll( "\\)", "\\\\)" );
		return ( "(" + text + ")" );
	}

	private String toHexString( String text )
	{
		StringBuffer buffer = new StringBuffer( );
		buffer.append( '<' );
		for ( int i = 0; i < text.length( ); i++ )
		{
			buffer.append( toHexString( text.charAt( i ) ) );
		}
		buffer.append( '>' );
		return buffer.toString( );
	}

	private String toHexString( char c )
	{
		final String[] padding = {"0", "00", "000"};
		String result = Integer.toHexString( c );
		if ( result.length( ) < 4 )
		{
			result = padding[3 - result.length( )] + result;
		}
		return result;
	}

	private ITrueTypeWriter getTrueTypeFontWriter( String fontPath )
			throws DocumentException, IOException
	{
		File file = new File( fontPath );
		ITrueTypeWriter trueTypeWriter = (ITrueTypeWriter) trueTypeFontWriters
				.get( file );
		if ( trueTypeWriter != null )
		{
			return trueTypeWriter;
		}
		else
		{
			TrueTypeFont ttFont = TrueTypeFont.getInstance( fontPath );
			trueTypeWriter = ttFont.getTrueTypeWriter( out );
			trueTypeWriter.initialize( );
			trueTypeFontWriters.put( file, trueTypeWriter );
			return trueTypeWriter;
		}
	}

	private String getFontPath( String fontName )
	{
		try
		{
			FontFactoryImp fontImpl = FontFactory.getFontImp( );
			Properties trueTypeFonts = (Properties) getField(
					FontFactoryImp.class, "trueTypeFonts", fontImpl );
			String fontPath = trueTypeFonts.getProperty( fontName.toLowerCase( ) );
			return fontPath;
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
		return null;
	}

	private Object getField( Class fontFactoryClass, String fieldName,
			Object instaces ) throws NoSuchFieldException,
			IllegalAccessException
	{
		Field fldTrueTypeFonts = fontFactoryClass.getDeclaredField( fieldName );
		fldTrueTypeFonts.setAccessible( true );
		Object field = fldTrueTypeFonts.get( instaces );
		return field;
	}

	/**
	 * Returns a String object representing this Graphic's value.
	 */

	public String toString( )
	{
		return getClass( ).getName( ) + "[font=" + getFont( ) + ",color="
				+ getColor( ) + "]";
	}

	/**
	 * Flip Y coords so Postscript looks like Java
	 */

	protected float transformY( float y )
	{
		return pageHeight - y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#translate(int,
	 *      int)
	 */

	public void translate( int x, int y )
	{
		out.print( x );
		out.print( " " );
		out.print( y );
		out.println( " translate" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#startRenderer()
	 */
	public void startRenderer( ) throws IOException
	{
		FileUtil.load(
				"org/eclipse/birt/report/engine/emitter/postscript/header.ps",
				out );
	}

	public void fillPage( Color color )
	{
		if ( color == null )
		{
			return;
		}
		gSave( );
		setColor( color );
		out.println( "clippath fill" );
		gRestore( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#startPage(float,
	 *      float)
	 */
	public void startPage( float pageWidth, float pageHeight )
	{
		this.pageHeight = pageHeight;
		out.println( "%%Page: " + pageIndex + " " + pageIndex );
		out.println( "%%PageBoundingBox: 0 0 " + (int) Math.round( pageWidth )
				+ " " + (int) Math.round( pageHeight ) );
		out.println( "%%BeginPage" );
		++pageIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#endPage()
	 */
	public void endPage( )
	{
		out.println( "showpage" );
		out.println( "%%PageTrailer" );
		out.println( "%%EndPage" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#stopRenderer()
	 */
	public void stopRenderer( ) throws IOException
	{
		out.println( "%%Trailer" );
		out.println( "%%Pages: " + ( pageIndex - 1 ) );
		out.println( "%%EOF" );
		out.flush( );
	}

	public abstract class ImageSource
	{

		protected int height;
		protected int width;

		public ImageSource( int width, int height )
		{
			this.width = width;
			this.height = height;
		}

		public int getHeight( )
		{
			return height;
		}

		public int getWidth( )
		{
			return width;
		}

		public abstract int getRGB( int x, int y );
	}

	public class ArrayImageSource extends ImageSource
	{

		private int[] imageSource;

		public ArrayImageSource( int width, int height, int[] imageSource )
		{
			super( width, height );
			this.imageSource = imageSource;
		}

		public int getRGB( int x, int y )
		{
			return imageSource[y * width + x];
		}
	}
}
