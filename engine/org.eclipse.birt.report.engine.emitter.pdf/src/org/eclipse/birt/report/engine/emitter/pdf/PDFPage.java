/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.layout.emitter.AbstractPage;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.w3c.dom.css.CSSValue;

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
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfTextArray;
import com.lowagie.text.pdf.PdfWriter;

public class PDFPage extends AbstractPage
{

	/**
	 * The PDF Writer
	 */
	private PdfWriter writer = null;

	/**
	 * ContentByte layer for PDF
	 */
	private PdfContentByte contentByte = null;

	private static Logger logger = Logger.getLogger( PDFPage.class.getName( ) );

	//Current text is total page.
	boolean isTotalPage = false;
	
	private PDFPageDevice pageDevice;
	
	public PDFPage( int pageWidth, int pageHeight, Document document,
			PdfWriter writer, PDFPageDevice pageDevice )
	{
		super( pageWidth, pageHeight );
		this.writer = writer;
		this.pageDevice = pageDevice;
		try
		{
			Rectangle pageSize = new Rectangle( this.pageWidth, this.pageHeight );
			document.setPageSize( pageSize );
			if ( !document.isOpen( ) )
				document.open( );
			else
				document.newPage( );
			this.contentByte = writer.getDirectContent( );
		}
		catch ( DocumentException de )
		{
			logger.log( Level.SEVERE, de.getMessage( ), de );
		}
	}

	protected void clip( float startX, float startY, float width, float height )
	{
		startY = transformY( startY, height );
		contentByte.rectangle( startX, startY, width, height );
		contentByte.clip( );
		contentByte.newPath( );
	}

	protected void restoreState( )
	{
		contentByte.restoreState( );
	}

	protected void saveState( )
	{
		contentByte.saveState( );
	}

	public void dispose( )
	{
	}

	protected void drawBackgroundColor( Color color, float x, float y,
			float width, float height )
	{
		if ( null == color )
		{
			return;
		}
		y = transformY( y, height );
		contentByte.saveState( );
		contentByte.setColorFill( color );
		contentByte.concatCTM( 1, 0, 0, 1, x, y );
		contentByte.rectangle( 0, 0, width, height );
		contentByte.fill( );
		contentByte.restoreState( );
	}

	protected void drawBackgroundImage( float x, float y, float width,
			float height, int repeat, String imageUrl, float absPosX,
			float absPosY ) throws IOException
	{
		y = transformY( y );
		contentByte.saveState( );
		Image img = null;
		try
		{
			try
			{
				img = Image.getInstance( new URL( imageUrl ) );
			}
			catch ( IOException e )
			{
				if ( SvgFile.isSvg( imageUrl ) )
				{
					try
					{
						img = Image
								.getInstance( SvgFile.transSvgToArray( imageUrl ) );
					}
					catch ( IOException ex )
					{
						throw ex;
					}
				}
				else
				{
					throw e;
				}
			}
			int resolutionX = img.getDpiX( );
			int resolutionY = img.getDpiY( );
			if ( 0 == resolutionX || 0 == resolutionY )
			{
				resolutionX = 96;
				resolutionY = 96;
			}
			float imageWidth = img.plainWidth( ) / resolutionX * 72;
			float imageHeight = img.plainHeight( ) / resolutionY * 72;

			if ( BackgroundImageInfo.NO_REPEAT == repeat ) //$NON-NLS-1$
			{
				TplValueTriple triple = computeTplHorizontalValPair( absPosX,
						x, width, imageWidth );
				float tplOriginX = triple.getTplOrigin( );
				float tplWidth = triple.getTplSize( );
				float translationX = triple.getTranslation( );
				triple = computeTplVerticalValTriple( absPosY, y, height, imageHeight );
				float tplOrininY = triple.getTplOrigin( );
				float tplHeight = triple.getTplSize( );
				float translationY = triple.getTranslation( );

				PdfTemplate templateWhole = contentByte.createTemplate( tplWidth,
						tplHeight );
				templateWhole.addImage( img, imageWidth, 0, 0, imageHeight, translationX, translationY );
				contentByte.addTemplate( templateWhole, tplOriginX, tplOrininY );

			}
			// "repeat-x":
			else if ( BackgroundImageInfo.REPEAT_X == repeat ) //$NON-NLS-1$
			{
				float remainX = width;
				PdfTemplate template = null;
				// If the width of the container is smaller than the scaled
				// image width, the repeat will never happen. So it is not 
				// necessary to build a template for further usage.
				if ( width > imageWidth )
				{
					if ( height - absPosY > imageHeight )
					{
						template = contentByte.createTemplate( imageWidth,
								imageHeight );
						template.addImage( img, imageWidth, 0, 0, imageHeight, 0, 0 );
					}
					else
					{
						template = contentByte.createTemplate( imageWidth,
								height );
						template.addImage( img, imageWidth, 0, 0, imageHeight, 0, -imageHeight
								+ height );
					}
				}
				while ( remainX > 0 )
				{
					if ( remainX < imageWidth )
					{

						if ( height - absPosY > imageHeight )
						{
							PdfTemplate templateX = contentByte.createTemplate(
									remainX, imageHeight );
							templateX.addImage( img, imageWidth, 0, 0,
									imageHeight, 0, 0 );
							contentByte.addTemplate( templateX,
									x + width - remainX, y - absPosY
											- imageHeight );
						}
						else
						{
							PdfTemplate templateX = contentByte.createTemplate(
									remainX, height );
							templateX.addImage( img, imageWidth, 0, 0,
									imageHeight, 0, -imageHeight
											+ height - absPosY );
							contentByte.addTemplate( templateX,
									x + width - remainX, y - absPosY - height );
						}
						remainX = 0;
					}
					else
					{
						if ( height - absPosY > imageHeight )
							contentByte.addTemplate( template, x + width - remainX,
									y - absPosY - imageHeight );
						else
							contentByte.addTemplate( template, x + width - remainX,
									y - absPosY - height );
						remainX -= imageWidth;
					}
				}
			}
			// "repeat-y":
			else if ( BackgroundImageInfo.REPEAT_Y == repeat ) //$NON-NLS-1$
			{
				float remainY = height;
				// If the height of the container is smaller than the scaled
				// image height, the repeat will never happen. So it is not 
				// necessary to build a template for further usage.
				PdfTemplate template = null;
				if ( height > imageHeight )
				{
					template = contentByte.createTemplate( width - absPosX > imageWidth ? imageWidth : width
							- absPosX, imageHeight );
					template.addImage( img, imageWidth, 0, 0, imageHeight, 0, 0 );
				}
				while ( remainY > 0 )
				{
					if ( remainY < imageHeight )
					{
						PdfTemplate templateY = contentByte.createTemplate( width
								- absPosX > imageWidth ? imageWidth : width - absPosX, remainY );
						templateY.addImage( img, width > imageWidth
								? imageWidth
								: width - absPosX, 0, 0, imageHeight,
								0, -( imageHeight - remainY ) );
						contentByte
								.addTemplate( templateY, x + absPosX, y
										- height );
						remainY = 0;
					}
					else
					{
						contentByte.addTemplate( template, x + absPosX, y - height
								+ remainY - imageHeight );
						remainY -= imageHeight;
					}
				}
			}
			// "repeat":
			else if ( BackgroundImageInfo.REPEAT == repeat) //$NON-NLS-1$
			{
				float remainX = width;
				float remainY = height;
				PdfTemplate template = null;
				// If the width of the container is smaller than the scaled
				// image width, the repeat will never happen. So it is not 
				// necessary to build a template for further usage.
				if ( width > imageWidth && height > imageHeight )
				{
					template = contentByte.createTemplate( imageWidth, imageHeight );
					template.addImage( img, imageWidth, 0, 0, imageHeight, 0, 0 );
				}

				while ( remainY > 0 )
				{
					remainX = width;
					// the bottom line
					if ( remainY < imageHeight )
					{
						while ( remainX > 0 )
						{
							// the right-bottom one
							if ( remainX < imageWidth )
							{
								PdfTemplate templateXY = contentByte
										.createTemplate( remainX, remainY );
								templateXY.addImage( img, imageWidth,
										0, 0, imageHeight, 0, -imageHeight
												+ remainY );
								contentByte.addTemplate( templateXY, x + width
										- remainX, y - height );
								remainX = 0;
							}
							else
							// non-right bottom line
							{
								PdfTemplate templateY = contentByte.createTemplate(
										imageWidth, remainY );
								templateY.addImage( img, imageWidth, 0,
										0, imageHeight, 0, -imageHeight
												+ remainY );
								contentByte.addTemplate( templateY, x + width
										- remainX, y - height );
								remainX -= imageWidth;
							}
						}
						remainY = 0;
					}
					else
					// non-bottom lines
					{
						while ( remainX > 0 )
						{
							// the right ones
							if ( remainX < imageWidth )
							{
								PdfTemplate templateX = contentByte.createTemplate(
										remainX, imageHeight );
								templateX.addImage( img, imageWidth, 0,
										0, imageHeight, 0, 0 );
								contentByte.addTemplate( templateX, x + width
										- remainX, y - height + remainY
										- imageHeight );
								remainX = 0;
							}
							else
							{
								contentByte.addTemplate( template, x + width
										- remainX, y - height + remainY
										- imageHeight );
								remainX -= imageWidth;
							}
						}
						remainY -= imageHeight;
					}
				}
			}
		}
		catch ( IOException ioe )
		{
			logger.log( Level.WARNING, ioe.getMessage( ), ioe );
		}
		catch ( BadElementException bee )
		{
			logger.log( Level.WARNING, bee.getMessage( ), bee );
		}
		catch ( DocumentException de )
		{
			logger.log( Level.WARNING, de.getMessage( ), de );
		}
		catch ( RuntimeException re )
		{
			logger.log( Level.WARNING, re.getMessage( ), re );
		}
		contentByte.restoreState( );
	}

	protected void drawImage( String imageId, byte[] imageData,
			String extension, float imageX, float imageY, float height,
			float width, String helpText, Map params ) throws Exception
	{
		if ( FlashFile.isFlash( null, null, extension ) )
		{
			embedFlash( null, imageData, imageX, imageY, height, width, helpText );
		}
		else
		{
			Image image = Image.getInstance( imageData );
			drawImage( image, imageX, imageY, height, width, helpText );
		}
	}

	protected void drawImage( String uri, String extension, float imageX,
			float imageY, float height, float width, String helpText, Map params )
			throws Exception
	{
		if ( FlashFile.isFlash( null, uri, extension ) )
		{
			embedFlash( uri, null, imageX, imageY, height, width, helpText );
		}
		else
		{
			Image image = Image.getInstance( new URL( uri ) );
			drawImage( image, imageX, imageY, height, width, helpText );
		}
	}
	
	/**
	 * Draws a line with the line-style specified in advance from the start
	 * position to the end position with the given line width, color, and style
	 * at the given PDF layer. If the line-style is NOT set before invoking this
	 * method, "solid" will be used as the default line-style.
	 * 
	 * @param startX
	 *            the start X coordinate of the line.
	 * @param startY
	 *            the start Y coordinate of the line.
	 * @param endX
	 *            the end X coordinate of the line.
	 * @param endY
	 *            the end Y coordinate of the line.
	 * @param width
	 *            the lineWidth
	 * @param color
	 *            the color of the line.
	 * @param lineStyle
	 *            the style of the line.
	 */
	protected void drawLine( float startX, float startY, float endX, float endY,
			float width, Color color, int lineStyle )
	{
		// if the border does NOT have color or the line width of the border is
		// zero or the lineStyle is "none", just return.
		if ( null == color || 0f == width
				|| BorderInfo.BORDER_STYLE_NONE==lineStyle ) //$NON-NLS-1$
		{
			return;
		}
		contentByte.saveState( );
		if ( BorderInfo.BORDER_STYLE_SOLID==lineStyle ) //$NON-NLS-1$
		{
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		else if ( BorderInfo.BORDER_STYLE_DASHED==lineStyle ) //$NON-NLS-1$
		{
			contentByte.setLineDash( 3 * width, 2 * width, 0f );
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		else if ( BorderInfo.BORDER_STYLE_DOTTED==lineStyle ) //$NON-NLS-1$
		{
			contentByte.setLineDash( width, width, 0f );
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		else if ( BorderInfo.BORDER_STYLE_DOUBLE==lineStyle ) //$NON-NLS-1$
		{
			return;
		}
		// the other line styles, e.g. 'ridge', 'outset', 'groove', 'inset' is
		// NOT supported now.
		// We look it as the default line style -- 'solid'
		else
		{
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		contentByte.restoreState( );
	}

	protected void drawText( String text, float textX, float textY, float baseline, float width,
			float height, TextStyle textStyle )
	{
		drawText( text, textX, textY + baseline, width, height, textStyle
				.getFontInfo( ),
				convertToPoint( textStyle.getLetterSpacing( ) ),
				convertToPoint( textStyle.getWordSpacing( ) ), textStyle
						.getColor( ), textStyle.isLinethrough( ), textStyle
						.isOverline( ), textStyle.isUnderline( ), textStyle
						.getAlign( ) );
	}

	private void drawText( String text, float textX, float textY, float width,
			float height, FontInfo fontInfo, float characterSpacing,
			float wordSpacing, Color color, boolean linethrough,
			boolean overline, boolean underline, CSSValue align )
	{
		drawText( text, textX, textY, fontInfo, characterSpacing, wordSpacing,
				color, align );
	}

	public void drawTotalPage( String text, int textX, int textY, int width,
			int height, TextStyle textInfo )
	{
		if ( pageDevice.getPDFTemplate( ) != null )
		{
			isTotalPage = true;
			drawText( text, textX, textY, width, height, textInfo );
		}
	}

	public void createBookmark( String bookmark, int x, int y, int width,
			int height )
	{
		createBookmark( bookmark, convertToPoint( x ), convertToPoint( y ),
				convertToPoint( width ), convertToPoint( height ) );
	}

	private void createBookmark( String bookmark, float x, float y, float width,
			float height )
	{
		contentByte.localDestination( bookmark, new PdfDestination(
				PdfDestination.XYZ, -1, transformY( y ), 0 ) );
	}

	public void createHyperlink( String hyperlink, String bookmark,
			String targetWindow, int type, int x, int y, int width, int height )
	{
		createHyperlink( hyperlink, bookmark, targetWindow, type,
				convertToPoint( x ), convertToPoint( y ),
				convertToPoint( width ), convertToPoint( height ) );
	}

	private void createHyperlink( String hyperlink, String bookmark,
			String targetWindow, int type, float x, float y, float width,
			float height )
	{
		y = transformY( y, height );
		writer.addAnnotation( new PdfAnnotation( writer, x, y, x + width, y
				+ height, createPdfAction( hyperlink, bookmark, targetWindow,
				type ) ) );
	}

	public void createTotalPageTemplate( int x, int y, int width, int height )
	{
		createTotalPageTemplate( convertToPoint( x ), convertToPoint( y ),
				convertToPoint( width ), convertToPoint( height ) );
	}

	private void createTotalPageTemplate( float x, float y, float width,
			float height )
	{
		if ( pageDevice.getPDFTemplate( ) == null )
		{
			pageDevice.setPDFTemplate( contentByte.createTemplate( width, height ));
		}
		y = transformY( y, height );
		contentByte.saveState( );
		contentByte.addTemplate( pageDevice.getPDFTemplate( ), x, y );
		contentByte.restoreState( );
	}


	/**
	 * Draws a line with the line-style specified in advance from the start
	 * position to the end position with the given linewidth, color, and style
	 * at the given pdf layer. If the line-style is NOT set before invoking this
	 * method, "solid" will be used as the default line-style.
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
	 * @param contentByte
	 *            the given pdf layer
	 */
	private void drawRawLine( float startX, float startY, float endX,
			float endY, float width, Color color, PdfContentByte contentByte )
	{
		startY = transformY( startY );
		endY = transformY( endY );
		contentByte.concatCTM( 1, 0, 0, 1, startX, startY );

		contentByte.moveTo( 0, 0 );
		contentByte.lineTo( endX - startX, endY - startY );
		
		contentByte.setLineWidth( width );
		contentByte.setColorStroke( color );
		contentByte.stroke( );
	}

	private void drawText( String text, float textX, float textY,
			FontInfo fontInfo, float characterSpacing, float wordSpacing,
			Color color, CSSValue align )
	{
		PdfContentByte currentContentByte = isTotalPage
				? pageDevice.getPDFTemplate( )
				: contentByte;
		float containerHeight = isTotalPage
				? pageDevice.getPDFTemplate( ).getHeight( )
				: pageHeight;
		isTotalPage = false;
		currentContentByte.saveState( );
		// start drawing the text content
		currentContentByte.beginText( );
		if ( null != color && !Color.BLACK.equals( color ) )
		{
			currentContentByte.setColorFill( color );
			currentContentByte.setColorStroke( color );
		}
		BaseFont font = fontInfo.getBaseFont( );
		float fontSize = fontInfo.getFontSize( );
		currentContentByte.setFontAndSize( font, fontSize );
		if ( characterSpacing != 0 )
		{
			currentContentByte.setCharacterSpacing( characterSpacing );
		}
		if ( wordSpacing != 0 )
		{
			currentContentByte.setWordSpacing( wordSpacing );
		}
		setTextMatrix( currentContentByte, fontInfo, textX, transformY( textY, 0, containerHeight ) );
		if ( ( font.getFontType( ) == BaseFont.FONT_TYPE_TTUNI )
				&& IStyle.JUSTIFY_VALUE.equals( align ) && wordSpacing > 0 )
		{
			int idx = text.indexOf( ' ' );
			if ( idx >= 0 )
			{
				float spaceCorrection = -wordSpacing * 1000 / fontSize;
				PdfTextArray textArray = new PdfTextArray( text.substring( 0,
						idx ) );
				int lastIdx = idx;
				while ( ( idx = text.indexOf( ' ', lastIdx + 1 ) ) >= 0 )
				{
					textArray.add( spaceCorrection );
					textArray.add( text.substring( lastIdx, idx ) );
					lastIdx = idx;
				}
				textArray.add( spaceCorrection );
				textArray.add( text.substring( lastIdx ) );
				currentContentByte.showText( textArray );
			}
			else
			{
				currentContentByte.showText( text );
			}
		}
		else
		{
			currentContentByte.showText( text );
		}
		currentContentByte.endText( );
		currentContentByte.restoreState( );
	}

	/**
	 * Creates a PdfAction.
	 * 
	 * @param hyperlink
	 *            the hyperlink.
	 * @param bookmark
	 *            the bookmark.
	 * @param target
	 *            if target equals "_blank", the target will be opened in a new
	 *            window, else the target will be opened in the current window.
	 * @return the created PdfAction.
	 */
	private PdfAction createPdfAction( String hyperlink, String bookmark,
			String target, int type )
	{
		// patch from Ales Novy
		if ("_top".equalsIgnoreCase(target) || 
			"_parent".equalsIgnoreCase(target) || 
            "_blank".equalsIgnoreCase(target) || 
            "_self".equalsIgnoreCase(target)) 
		// Opens the target in a new window.
		{
			return new PdfAction( hyperlink );
		}
		else

		// Opens the target in the current window.
		{
			if ( type == IHyperlinkAction.ACTION_BOOKMARK )
			{
				return PdfAction.gotoLocalPage( bookmark, false );
			}
			else
			{
				return PdfAction.gotoRemotePage( hyperlink, bookmark, false,
						false );
			}
		}
	}

	private void setTextMatrix( PdfContentByte cb, FontInfo fi, float x, float y )
	{
		cb.concatCTM( 1, 0, 0, 1, x, y );
		if ( !fi.getSimulation( ) )
		{
			cb.setTextMatrix( 0, 0 );
			return;
		}
		switch ( fi.getFontStyle( ) )
		{
			case Font.ITALIC :
			{
				simulateItalic( cb );
				break;
			}
			case Font.BOLD :
			{
				simulateBold( cb );
				break;
			}
			case Font.BOLDITALIC :
			{
				simulateBold( cb );
				simulateItalic( cb );
				break;
			}
		}
	}

	private void simulateBold( PdfContentByte cb )
	{
		cb.setTextRenderingMode( PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE );
		cb.setLineWidth( 0.9f );
		cb.setTextMatrix( 0, 0 );
	}

	private void simulateItalic( PdfContentByte cb )
	{
		float beta = EmitterUtil.ITALIC_HORIZONTAL_COEFFICIENT;
		cb.setTextMatrix( 1, 0, beta, 1, 0, 0 );
	}

	private final class TplValueTriple
	{

		private final float tplOrigin;
		private final float tplSize;
		private final float translation;

		public TplValueTriple( final float val1, final float val2,
				final float val3 )
		{
			tplOrigin = val1;
			tplSize = val2;
			translation = val3;
		}

		float getTplOrigin( )
		{
			return tplOrigin;
		}

		float getTplSize( )
		{
			return tplSize;
		}

		float getTranslation( )
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
	 *         template height, and image's vertical translation relative to the
	 *         template )
	 */
	private TplValueTriple computeTplVerticalValTriple( float absPos,
			float containerBaseAbsPos, float containerSize, float ImageSize )
	{
		float tplOrigin = 0.0f, tplSize = 0.0f, translation = 0.0f;
		if ( absPos <= 0 )
		{
			if ( ImageSize + absPos > 0 && ImageSize + absPos <= containerSize )
			{
				tplOrigin = containerBaseAbsPos - ImageSize - absPos;
				tplSize = ImageSize + absPos;
			}
			else if ( ImageSize + absPos > containerSize )
			{
				tplOrigin = containerBaseAbsPos - containerSize;
				tplSize = containerSize;
				translation = containerSize - ImageSize - absPos;
			}
			else
			{
				// never draw
			}
		}
		else if ( absPos >= containerSize )
		{
			// never draw
		}
		else
		{
			if ( ImageSize + absPos <= containerSize )
			{
				tplOrigin = containerBaseAbsPos - ImageSize - absPos;
				tplSize = ImageSize;
				translation = 0.0f;
			}
			else
			{
				tplOrigin = containerBaseAbsPos - containerSize;
				tplSize = containerSize - absPos;
				translation = containerSize - absPos - ImageSize;
			}

		}
		return new TplValueTriple( tplOrigin, tplSize, translation );
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
	 * 
	 * @param absPos
	 *            the horizontal position relative to its containing box
	 * @param containerBaseAbsPos
	 *            the absolute position of the container's left side
	 * @param containerSize
	 *            container width
	 * @param ImageSize
	 *            the width of template which image lies in
	 * @return a triple(the horizontal position of template's left-bottom
	 *         origin, template width, and image's horizontal translation
	 *         relative to the template )
	 */
	private TplValueTriple computeTplHorizontalValPair( float absPos,
			float containerBaseAbsPos, float containerSize, float ImageSize )
	{
		float tplOrigin = 0.0f, tplSize = 0.0f, translation = 0.0f;
		if ( absPos <= 0 )
		{
			if ( ImageSize + absPos > 0 && ImageSize + absPos <= containerSize )
			{
				tplOrigin = containerBaseAbsPos;
				tplSize = ImageSize + absPos;
			}
			else if ( ImageSize + absPos > containerSize )
			{
				tplOrigin = containerBaseAbsPos;
				tplSize = containerSize;
			}
			else
			{
				// never create template
			}
			translation = absPos;
		}
		else if ( absPos >= containerSize )
		{
			// never create template
		}
		else
		{
			if ( ImageSize + absPos <= containerSize )
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
		return new TplValueTriple( tplOrigin, tplSize, translation );

	}

	private void drawImage( Image image, float imageX, float imageY,
			float height, float width, String helpText )
			throws DocumentException
	{
		imageY = transformY( imageY, height );
		contentByte.saveState( );
		contentByte.concatCTM( 1, 0, 0, 1, imageX, imageY );
		contentByte.addImage( image, width, 0f, 0f, height, 0f, 0f );
		if ( helpText != null )
		{
			showHelpText( imageX, imageY, width, height, helpText );
		}
		contentByte.restoreState( );
	}
	
	private void embedFlash( String flashPath, byte[] flashData, float x, float y, float height,
			float width, String helpText ) throws IOException
	{
		y = transformY( y, height );
		contentByte.saveState( );
		PdfFileSpecification fs = PdfFileSpecification.fileEmbedded( writer,
				flashPath, helpText, flashData );
		PdfAnnotation annot = PdfAnnotation.createScreen( writer,
				new Rectangle( x, y, x + width, y + height ), helpText, fs,
				"application/x-shockwave-flash", true );
		writer.addAnnotation( annot );
		if ( helpText != null )
		{
			showHelpText( x, y, width, height, helpText );
		}
		contentByte.restoreState( );
	}
}
