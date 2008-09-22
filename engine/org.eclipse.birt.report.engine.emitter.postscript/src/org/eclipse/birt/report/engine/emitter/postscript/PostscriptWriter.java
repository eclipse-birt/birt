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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.ITrueTypeWriter;
import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.TrueTypeFont;
import org.eclipse.birt.report.engine.emitter.postscript.truetypefont.Util;
import org.eclipse.birt.report.engine.emitter.postscript.util.FileUtil;
import org.eclipse.birt.report.engine.layout.emitter.util.BackgroundImageLayout;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
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
	final protected static char hd[] = {'0', '1', '2', '3', '4', '5', '6', '7',
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
	 * log
	 */
	protected static Logger log = Logger.getLogger( PostscriptWriter.class
			.getName( ) );

	/**
	 * Current page index with 1 as default value.
	 */
	private int pageIndex = 1;
	
	/**
	 * Height of current page.
	 */
	private float pageHeight = DEFAULT_PAGE_HEIGHT;
	
	private static Set<String> intrinsicFonts = new HashSet<String>( );

	private int imageIndex = 0;
	
	private Map<String, String> cachedImageSource;
	
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
		this.out = new PrintStream( o );
		this.cachedImageSource = new HashMap<String, String>();
		emitProlog( title );
	}

	public void clipRect( float x, float y, float width, float height )
	{
		y = transformY( y );
		out.println( x + " " + y + " " + width + " " + height + " rcl" );
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
	public void drawImage( String imageId, InputStream imageStream, float x, float y,
			float width, float height ) throws Exception
	{
		Image image = ImageIO.read( imageStream );
		drawImage( imageId, image, x, y, width, height );
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
	public void drawImage( String imageId, Image image, float x, float y, float width,
			float height ) throws IOException
	{
		if ( image == null )
		{
			throw new IllegalArgumentException( "null image." );
		}
		y = transformY( y );
		
		//if imageId is null, the image will not be cached.
		if ( imageId == null )
		{
			outputUncachedImage( image, x, y, width, height );
		}
		else
		{
			// NOTICE: if width or height is 0, then the width and height will
			// be replaced by the intrinsic width and height of the image. This
			// logic is hard coded in postscript code and can't be found in java
			// code.
			outputCachedImage( imageId, image, x, y, width, height );
		}
	}

	private void outputCachedImage( String imageId, Image image, float x,
			float y, float width, float height ) throws IOException
	{
		String imageName = getImageName( imageId, image );
		out.print( imageName + " ");
		out.print( x + " " + y + " ");
		out.println( width + " " + height + " drawimage");
	}

	private void outputUncachedImage( Image image, float x, float y,
			float width, float height ) throws IOException
	{
		ArrayImageSource imageSource = getImageSource( image );
		out.print( x + " " + y + " ");
		out.print( width + " " + height + " " );
		out.print( imageSource.getWidth( ) + " " + imageSource.getHeight( ) );
		out.println( " drawstreamimage");
		outputImageSource( imageSource );
		out.println( "grestore" );
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

	private String getImageName( String imageId, Image image ) throws IOException
	{
		String name = (String) cachedImageSource.get( imageId );
		if ( name == null )
		{
			name = "image" + imageIndex++;
			cachedImageSource.put( imageId, name );
			ArrayImageSource imageSource = getImageSource( image );
			outputNamedImageSource( name, imageSource );
		}
		return name;
	}

	private void outputNamedImageSource( String name, ArrayImageSource imageSource )
	{
		out.println( "startDefImage" );
		outputImageSource( imageSource );
		out.println( "/" + name + " " + imageSource.getWidth( ) + " "
				+ imageSource.getHeight( ) + " endDefImage" );
	}

	private void outputImageSource( ArrayImageSource imageSource )
	{
		int originalWidth = imageSource.getWidth( );
		int originalHeight = imageSource.getHeight( );
		try
		{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream( );
			DeflaterOutputStream deflateOut = new DeflaterOutputStream(
					byteOut, new Deflater( Deflater.DEFAULT_COMPRESSION ) );
			for ( int i = 0; i < originalHeight; i++ )
			{
				for ( int j = 0; j < originalWidth; j++ )
				{
					int pixel = imageSource.getRGB( j, i );
					int alpha = ( pixel >> 24 ) & 0xff;
					int red = ( pixel >> 16 ) & 0xff;
					int green = ( pixel >> 8 ) & 0xff;
					int blue = pixel & 0xff;
					deflateOut.write( transferColor( alpha, red ) );
					deflateOut.write( transferColor( alpha, green ) );
					deflateOut.write( transferColor( alpha, blue ) );
				}
			}
			deflateOut.finish( );
			deflateOut.close( );
			byte[] byteArray = byteOut.toByteArray( );
			byteOut.close( );
			out.print( Util.toHexString( byteArray )+">" );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
	}

	private int transferColor( int alpha, int color )
	{
		return 255 - (255 - color) * alpha / 255;
	}
	
	protected void drawRect( float x, float y, float width, float height )
	{
		drawRawRect( x, y, width, height );
		out.println( "fill" );
	}

	private void drawRawRect( float x, float y, float width, float height )
	{
		y = transformY( y );
		out.println( x + " " + y + " " + width + " " + height + " rect" );
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
	 * @throws Exception 
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

			ImageIcon imageIcon = new ImageIcon( image );
			Position imageSize = new Position( imageIcon.getIconWidth( ),
					imageIcon.getIconHeight( ) );
			Position areaPosition = new Position( x, y );
			Position areaSize = new Position( width, height );
			Position imagePosition = new Position( x + positionX, y + positionY );
			BackgroundImageLayout layout = new BackgroundImageLayout(
					areaPosition, areaSize, imagePosition, imageSize );
			Collection positions = layout.getImagePositions( repeat );
			gSave( );
			setColor( Color.WHITE );
			out.println( "newpath" );
			drawRawRect( x, y, width, height );
			out.println( "closepath clip" );
			Iterator iterator = positions.iterator( );
			while ( iterator.hasNext( ) )
			{
				Position position = (Position) iterator.next( );
				drawImage( imageURI, image, position.getX( ), position.getY( ),
						imageSize.getX( ), imageSize.getY( ) );
			}
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
		int dashMode = 0;
		if ( "dashed".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			dashMode = 1;
		}
		else if ( "dotted".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			dashMode = 2;
		}
		startY = transformY( startY );
		endY = transformY( endY );
		outputColor( color );
		out.print( width + " " + dashMode + " ");
		out.print( startX + " " + startY + " ");
		out.print( endX + " " + endY + " ");
		out.println( "drawline");
	}

	private void gRestore( )
	{
		out.println( "grestore" );
	}

	private void gSave( )
	{
		out.println( "gsave" );
	}

	private Map<File, ITrueTypeWriter> trueTypeFontWriters = new HashMap<File, ITrueTypeWriter>( );

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
		String text = str;
		String drawCommand = "drawstring";
		boolean needSimulateItalic = false;
		if ( fontInfo != null )
		{
			float fontSize = fontInfo.getFontSize( );
			int fontStyle = fontInfo.getFontStyle( );
			if ( fontInfo.getSimulation( ) )
			{
				if ( fontStyle == Font.BOLD || fontStyle == Font.BOLDITALIC )
				{
					float offset = (float) ( fontSize * Math.log10( fontSize ) / 100 );
					drawCommand = offset + " drawBoldString";
				}
				if ( fontStyle == Font.ITALIC || fontStyle == Font.BOLDITALIC )
				{
					needSimulateItalic = true;
				}
			}
			BaseFont baseFont = fontInfo.getBaseFont( );
			String fontName = baseFont.getPostscriptFontName( );
			text = applyFont( fontName, fontStyle, fontSize, text );
		}
		color = color == null ? Color.black : color;
		outputColor( color );
		out.print( x + " " + y + " " );
		out.print( wordSpacing + " " + letterSpacing + " " );
		out.println( text + " " + needSimulateItalic + " " + drawCommand );
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
	}

	public void fillRect( float x, float y, float width, float height,
			Color color )
	{
		gSave( );
		setColor( color );
		drawRect( x, y, width, height );
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

	public void setColor( Color c )
	{
		outputColor( c );
		out.println( " setrgbcolor" );
	}

	private void outputColor( Color c )
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
		out.print( " " );
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
		out.println( "/" + psName + " " + size + " usefont" );
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
				log.log( Level.WARNING, "apply font: " + fontName );
			}
			return null;
		}
	}

	private String applyIntrinsicFont( String fontName, int fontStyle, float fontSize, String text )
	{
		setFont( fontName, fontSize );
		text = escapeSpecialCharacter( text );
		return ( "(" + text + ")" );
	}

	/**
	 * Escape the characters "(", ")", and "\" in a postscript string by "\".
	 * 
	 * @param source
	 * @return
	 */
	private static String escapeSpecialCharacter( String source )
	{
		Pattern pattern = Pattern.compile( "(\\\\|\\)|\\()" );
		Matcher matcher = pattern.matcher( source );
		StringBuffer buffer = new StringBuffer( );
		while ( matcher.find( ) )
		{
			matcher.appendReplacement( buffer, "\\\\\\" + matcher.group( 1 ) );
		}
		matcher.appendTail( buffer );
		return buffer.toString( );
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
			log.log( Level.WARNING, "font path: " + fontName );
		}
		return null;
	}

	private Object getField( final Class fontFactoryClass,
			final String fieldName, final Object instaces )
			throws NoSuchFieldException, IllegalAccessException
	{
		try
		{
			Object field = (Object) AccessController
					.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

						public Object run( ) throws IllegalArgumentException,
								IllegalAccessException, NoSuchFieldException
						{
							Field fldTrueTypeFonts = fontFactoryClass
									.getDeclaredField( fieldName );// $NON-SEC-3
							fldTrueTypeFonts.setAccessible( true );// $NON-SEC-2
							return fldTrueTypeFonts.get( instaces );
						}
					} );
			return field;
		}
		catch ( PrivilegedActionException e )
		{
			Exception typedException = e.getException( );
			if ( typedException instanceof IllegalArgumentException )
			{
				throw (IllegalArgumentException) typedException;
			}
			if ( typedException instanceof IllegalAccessException )
			{
				throw (IllegalAccessException) typedException;
			}
			if ( typedException instanceof NoSuchFieldException )
			{
				throw (NoSuchFieldException) typedException;
			}
		}
		return null;
	}

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

	public void startRenderer( ) throws IOException
	{
		startRenderer( null, null );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.postscript.IWriter#startRenderer()
	 */
	public void startRenderer( String author, String description )
			throws IOException
	{
		out.println("%%Creator: " + author);
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
		
		public int[] getData()
		{
			return imageSource;
		}
	}
	
	public void close( ) throws IOException
	{
		stopRenderer( );
		out.close( );
	}
}
