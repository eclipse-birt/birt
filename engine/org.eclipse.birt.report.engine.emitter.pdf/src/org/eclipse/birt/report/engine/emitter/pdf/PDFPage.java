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
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.emitter.AbstractPage;
import org.eclipse.birt.report.engine.layout.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
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
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfTextArray;
import com.lowagie.text.pdf.PdfWriter;

public class PDFPage extends AbstractPage
{

	/**
	 * The Pdf Writer
	 */
	private PdfWriter writer = null;

	/**
	 * Template for totalpage
	 */
	private PdfTemplate totalPageTemplate = null;

	/**
	 * ContentByte layer for pdf, cb covers cbUnder.
	 */
	private PdfContentByte contentByte, cbUnder = null;

	private static Logger logger = Logger.getLogger( PDFPage.class.getName( ) );

	//Current text is total page.
	boolean isTotalPage = false;
	
	public PDFPage( int pageWidth, int pageHeight, Document document,
			PdfWriter writer )
	{
		super( pageWidth, pageHeight );
		this.writer = writer;
		// Creates a pdf page, get its contentByte and contentByteUnder
		try
		{
			Rectangle pageSize = new Rectangle( this.pageWidth, this.pageHeight );
			document.setPageSize( pageSize );
			if ( !document.isOpen( ) )
				document.open( );
			else
				document.newPage( );
			this.contentByte = writer.getDirectContent( );
			this.cbUnder = writer.getDirectContentUnder( );
		}
		catch ( DocumentException de )
		{
			logger.log( Level.SEVERE, de.getMessage( ), de );
		}
	}


	protected void clip( float startX, float startY, float width, float height )
	{
		startY = transformY( startY, height );
		contentByte.clip( );
		contentByte.rectangle( startX, startY, width, height );
		contentByte.newPath( );
		cbUnder.clip( );
		cbUnder.rectangle( startX, startY, width, height );
		cbUnder.newPath( );
	}

	public void clipRestore( )
	{
		cbUnder.restoreState( );
		contentByte.restoreState( );
	}

	public void clipSave( )
	{
		cbUnder.saveState( );
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
		cbUnder.saveState( );
		cbUnder.setColorFill( color );
		cbUnder.rectangle( x, y, width, height );
		cbUnder.fill( );
		cbUnder.restoreState( );
	}

	protected void drawBackgroundImage( float x, float y, float width,
			float height, String repeat, String imageUrl, float absPosX,
			float absPosY ) throws IOException
	{
		y = transformY( y );
		cbUnder.saveState( );
		Image img = null;
		try
		{
			img = Image.getInstance( imageUrl );
			if ( "no-repeat".equalsIgnoreCase( repeat ) ) //$NON-NLS-1$
			{
				TplValueTriple triple = computeTplHorizontalValPair( absPosX,
						x, width, img.scaledWidth( ) );
				float tplOriginX = triple.getTplOrigin( );
				float tplWidth = triple.getTplSize( );
				float translationX = triple.getTranslation( );
				triple = computeTplVerticalValTriple( absPosY, y, height, img
						.scaledHeight( ) );
				float tplOrininY = triple.getTplOrigin( );
				float tplHeight = triple.getTplSize( );
				float translationY = triple.getTranslation( );

				PdfTemplate templateWhole = cbUnder.createTemplate( tplWidth,
						tplHeight );
				templateWhole.addImage( img, img.scaledWidth( ), 0, 0, img
						.scaledHeight( ), translationX, translationY );
				cbUnder.addTemplate( templateWhole, tplOriginX, tplOrininY );

			}
			// "repeat-x":
			else if ( "repeat-x".equalsIgnoreCase( repeat ) ) //$NON-NLS-1$
			{
				float remainX = width;
				PdfTemplate template = null;
				// If the width of the container is smaller than the scaled
				// image width, the repeat will never happen. So it is not 
				// necessary to build a template for futher usage.
				if ( width > img.scaledWidth( ) )
				{
					if ( height - absPosY > img.scaledHeight( ) )
					{
						template = cbUnder.createTemplate( img.scaledWidth( ),
								img.scaledHeight( ) );
						template.addImage( img, img.scaledWidth( ), 0, 0, img
								.scaledHeight( ), 0, 0 );
					}
					else
					{
						template = cbUnder.createTemplate( img.scaledWidth( ),
								height );
						template.addImage( img, img.scaledWidth( ), 0, 0, img
								.scaledHeight( ), 0, -img.scaledHeight( )
								+ height );
					}
				}
				while ( remainX > 0 )
				{
					if ( remainX < img.scaledWidth( ) )
					{

						if ( height - absPosY > img.scaledHeight( ) )
						{
							PdfTemplate templateX = cbUnder.createTemplate(
									remainX, img.scaledHeight( ) );
							templateX.addImage( img, img.scaledWidth( ), 0, 0,
									img.scaledHeight( ), 0, 0 );
							cbUnder.addTemplate( templateX,
									x + width - remainX, y - absPosY
											- img.scaledHeight( ) );
						}
						else
						{
							PdfTemplate templateX = cbUnder.createTemplate(
									remainX, height );
							templateX.addImage( img, img.scaledWidth( ), 0, 0,
									img.scaledHeight( ), 0, -img.scaledHeight( )
											+ height - absPosY );
							cbUnder.addTemplate( templateX,
									x + width - remainX, y - absPosY - height );
						}
						remainX = 0;
					}
					else
					{
						if ( height - absPosY > img.scaledHeight( ) )
							cbUnder.addTemplate( template, x + width - remainX,
									y - absPosY - img.scaledHeight( ) );
						else
							cbUnder.addTemplate( template, x + width - remainX,
									y - absPosY - height );
						remainX -= img.scaledWidth( );
					}
				}
			}
			// "repeat-y":
			else if ( "repeat-y".equalsIgnoreCase( repeat ) ) //$NON-NLS-1$
			{
				float remainY = height;
				// If the height of the container is smaller than the scaled
				// image height, the repeat will never happen. So it is not 
				// necessary to build a template for futher usage.
				PdfTemplate template = null;
				if ( height > img.scaledHeight( ) )
				{
					template = cbUnder.createTemplate( width - absPosX > img
							.scaledWidth( ) ? img.scaledWidth( ) : width
							- absPosX, img.scaledHeight( ) );
					template.addImage( img, img.scaledWidth( ), 0, 0, img
							.scaledHeight( ), 0, 0 );
				}
				while ( remainY > 0 )
				{
					if ( remainY < img.scaledHeight( ) )
					{
						PdfTemplate templateY = cbUnder.createTemplate( width
								- absPosX > img.scaledWidth( ) ? img
								.scaledWidth( ) : width - absPosX, remainY );
						templateY.addImage( img, width > img.scaledWidth( )
								? img.scaledWidth( )
								: width - absPosX, 0, 0, img.scaledHeight( ),
								0, -( img.scaledHeight( ) - remainY ) );
						cbUnder
								.addTemplate( templateY, x + absPosX, y
										- height );
						remainY = 0;
					}
					else
					{
						cbUnder.addTemplate( template, x + absPosX, y - height
								+ remainY - img.scaledHeight( ) );
						remainY -= img.scaledHeight( );
					}
				}
			}
			// "repeat":
			else if ( "repeat".equalsIgnoreCase( repeat ) ) //$NON-NLS-1$
			{
				float remainX = width;
				float remainY = height;
				PdfTemplate template = null;
				// If the width of the container is smaller than the scaled
				// image width, the repeat will never happen. So it is not 
				// necessary to build a template for futher usage.
				if ( width > img.scaledWidth( ) && height > img.scaledHeight( ) )
				{
					template = cbUnder.createTemplate( img.scaledWidth( ), img
							.scaledHeight( ) );
					template.addImage( img, img.scaledWidth( ), 0, 0, img
							.scaledHeight( ), 0, 0 );
				}

				while ( remainY > 0 )
				{
					remainX = width;
					// the bottom line
					if ( remainY < img.scaledHeight( ) )
					{
						while ( remainX > 0 )
						{
							// the right-bottom one
							if ( remainX < img.scaledWidth( ) )
							{
								PdfTemplate templateXY = cbUnder
										.createTemplate( remainX, remainY );
								templateXY.addImage( img, img.scaledWidth( ),
										0, 0, img.scaledHeight( ), 0, -img
												.scaledHeight( )
												+ remainY );
								cbUnder.addTemplate( templateXY, x + width
										- remainX, y - height );
								remainX = 0;
							}
							else
							// non-right bottom line
							{
								PdfTemplate templateY = cbUnder.createTemplate(
										img.scaledWidth( ), remainY );
								templateY.addImage( img, img.scaledWidth( ), 0,
										0, img.scaledHeight( ), 0, -img
												.scaledHeight( )
												+ remainY );
								cbUnder.addTemplate( templateY, x + width
										- remainX, y - height );
								remainX -= img.scaledWidth( );
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
							if ( remainX < img.scaledWidth( ) )
							{
								PdfTemplate templateX = cbUnder.createTemplate(
										remainX, img.scaledHeight( ) );
								templateX.addImage( img, img.scaledWidth( ), 0,
										0, img.scaledHeight( ), 0, 0 );
								cbUnder.addTemplate( templateX, x + width
										- remainX, y - height + remainY
										- img.scaledHeight( ) );
								remainX = 0;
							}
							else
							{
								cbUnder.addTemplate( template, x + width
										- remainX, y - height + remainY
										- img.scaledHeight( ) );
								remainX -= img.scaledWidth( );
							}
						}
						remainY -= img.scaledHeight( );
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
		cbUnder.restoreState( );
	}

	protected void drawImage( byte[] imageData, String extension, float imageX,
			float imageY, float height, float width, String helpText )
			throws Exception
	{
		Image image = Image.getInstance( imageData );
		drawImage( image, imageX, imageY, height, width, helpText );
	}

	protected void drawImage( String uri, String extension, float imageX,
			float imageY, float height, float width, String helpText )
			throws Exception
	{
		Image image = Image.getInstance( new URL( uri ) );
		drawImage( image, imageX, imageY, height, width, helpText );
	}

	/**
	 * Draws a line with the line-style specified in advance from the start
	 * position to the end position with the given linewidth, color, and style
	 * at the given pdf layer. If the line-style is NOT set before invoking this
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
			float width, Color color, String lineStyle )
	{
		// if the border does NOT have color or the linewidth of the border is
		// zero or the lineStyle is "none", just return.
		if ( null == color || 0f == width
				|| "none".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			return;
		}
		contentByte.saveState( );
		if ( "solid".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			contentByte.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		if ( "dashed".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			contentByte.setLineDash( 3 * width, 2 * width, 0f );
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		else if ( "dotted".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			contentByte.setLineDash( width, width, 0f );
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		else if ( "double".equalsIgnoreCase( lineStyle ) ) //$NON-NLS-1$
		{
			return;
		}
		// the other line styles, e.g. 'ridge', 'outset', 'groove', 'inset' is
		// NOT supported now.
		// We look it as the default line style -- 'solid'
		else
		{
			contentByte.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
			drawRawLine( startX, startY, endX, endY, width, color, contentByte );
		}
		contentByte.restoreState( );
	}

	protected void drawText( String text, float textX, float textY, float baseline, float width,
			float height, TextStyle fontStyle )
	{
		drawText( text, textX, textY + baseline, width, height, fontStyle
				.getFontInfo( ),
				convertToPoint( fontStyle.getLetterSpacing( ) ),
				convertToPoint( fontStyle.getWordSpacing( ) ), fontStyle
						.getColor( ), fontStyle.isLinethrough( ), fontStyle
						.isOverline( ), fontStyle.isUnderline( ), fontStyle
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
		if ( totalPageTemplate != null )
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
		if ( totalPageTemplate == null )
		{
			totalPageTemplate = contentByte.createTemplate( width, height );
		}
		y = transformY( y, height );
		contentByte.saveState( );
		contentByte.addTemplate( totalPageTemplate, x, y );
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
		contentByte.moveTo( startX, startY );
		contentByte.setLineWidth( width );
		contentByte.lineTo( endX, endY );
		contentByte.setColorStroke( color );
		contentByte.stroke( );
	}

	private void drawText( String text, float textX, float textY,
			FontInfo fontInfo, float characterSpacing, float wordSpacing,
			Color color, CSSValue align )
	{
		PdfContentByte currentContentByte = isTotalPage
				? totalPageTemplate
				: contentByte;
		float containerHeight = isTotalPage
				? totalPageTemplate.getHeight( )
				: pageHeight;
		isTotalPage = false;
		currentContentByte.saveState( );
		// start drawing the text content
		currentContentByte.beginText( );
		if ( null != color )
		{
			currentContentByte.setColorFill( color );
			currentContentByte.setColorStroke( color );
		}
		BaseFont font = fontInfo.getBaseFont( );
		float fontSize = fontInfo.getFontSize( );
		currentContentByte.setFontAndSize( font, fontSize );
		currentContentByte.setCharacterSpacing( characterSpacing );
		currentContentByte.setWordSpacing( wordSpacing );
		placeText( currentContentByte, fontInfo, textX, transformY( textY, 0,
				containerHeight ) );
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

	private void placeText( PdfContentByte cb, FontInfo fi, float x, float y )
	{
		if ( !fi.getSimulation( ) )
		{
			cb.setTextMatrix( x, y );
			return;
		}
		switch ( fi.getFontStyle( ) )
		{
			case Font.ITALIC :
			{
				simulateItalic( cb, x, y );
				break;
			}
			case Font.BOLD :
			{
				simulateBold( cb, x, y );
				break;
			}
			case Font.BOLDITALIC :
			{
				simulateBold( cb, x, y );
				simulateItalic( cb, x, y );
				break;
			}
		}
	}

	private void simulateBold( PdfContentByte cb, float x, float y )
	{
		cb.setTextRenderingMode( PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE );
		cb.setLineWidth( 0.9f );
		cb.setTextMatrix( x, y );
	}

	private void simulateItalic( PdfContentByte cb, float x, float y )
	{
		float alpha = (float) Math.tan( 0f * Math.PI / 180 );
		float beta = EmitterUtil.getItalicHorizontalCoefficient( );
		cb.setTextMatrix( 1, alpha, beta, 1, x, y );
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
		contentByte.addImage( image, width, 0f, 0f, height, imageX, imageY );
		if ( helpText != null )
		{
			showHelpText( imageX, imageY, width, height, helpText );
		}
		contentByte.restoreState( );
	}
}
