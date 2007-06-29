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

package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.InlineContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.Image;

public abstract class PageDeviceRender implements IAreaVisitor
{

	/**
	 * The default image folder
	 */
	public static final String IMAGE_FOLDER = "image"; //$NON-NLS-1$

	public static final int H_TEXT_SPACE = 30;

	public static final int V_TEXT_SPACE = 100;

	protected float scale;

	protected int hTextSpace = 30;

	protected int vTextSpace = 100;

	int pageHeight;

	int pageWidth;

	protected IReportRunnable reportRunnable;

	protected ReportDesignHandle reportDesign;

	protected IReportContext context;

	protected IEmitterServices services;

	private Stack containerStack = new Stack( );

	protected Logger logger = Logger.getLogger( PageDeviceRender.class
			.getName( ) );

	protected IPageDevice pageDevice;

	protected IPage pageGraphic;

	protected class ContainerPosition
	{

		public int x;
		public int y;

		public ContainerPosition( int x, int y )
		{
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Gets the output format. always returns "postscript".
	 */
	public abstract String getOutputFormat( );

	public abstract IPageDevice createPageDevice( String title,
			IReportContext context, IReportContent report ) throws Exception;

	/**
	 * Creates a document and create a PdfWriter
	 * 
	 * @param rc
	 *            the report content.
	 */
	public void start( IReportContent rc )
	{
		ReportDesignHandle designHandle = rc.getDesign( ).getReportDesign( );
		String title = designHandle.getStringProperty( IModuleModel.TITLE_PROP );
		try
		{
			pageDevice = createPageDevice( title, context, rc );
		}
		catch ( Exception e )
		{
			log( e, Level.SEVERE );
		}
	}

	protected void log( Throwable t, Level level )
	{
		logger.log( level, t.getMessage( ), t );
	}

	/**
	 * Closes the document.
	 * 
	 * @param rc
	 *            the report content.
	 */
	public void end( IReportContent rc )
	{
		try
		{
			pageDevice.close( );
		}
		catch ( Exception e )
		{
			log( e, Level.WARNING );
		}
	}

	public void setTotalPage( ITextArea totalPage )
	{
	}

	public void visitText( ITextArea textArea )
	{
		ContainerPosition curPos = getContainerPosition( );
		int x = curPos.x + getX( textArea );
		int y = curPos.y + getY( textArea );
		drawTextAt( textArea, x, y );
	}

	public void visitImage( IImageArea imageArea )
	{
		drawImage( imageArea );
	}

	public void visitAutoText( ITemplateArea templateArea )
	{
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
	 * If the container is a PageArea, this method creates a pdf page. If the
	 * container is the other containerAreas, such as TableArea, or just the
	 * border of textArea/imageArea this method draws the border and background
	 * of the given container.
	 * 
	 * @param container
	 *            the ContainerArea specified from layout
	 */
	protected void startContainer( IContainerArea container )
	{
		if ( container instanceof PageArea )
		{
			scale = container.getScale( );
			hTextSpace = (int) ( H_TEXT_SPACE * scale );
			vTextSpace = (int) ( V_TEXT_SPACE * scale );
			newPage( container );
			containerStack.push( new ContainerPosition( 0, 0 ) );
		}
		else
		{
			if ( needClip( container ) )
			{
				pageGraphic.clipSave( );
				clip( container );
			}
			drawContainer( container );
			ContainerPosition pos;
			if ( !containerStack.isEmpty( ) )
			{
				pos = (ContainerPosition) containerStack.peek( );
				ContainerPosition current = new ContainerPosition( pos.x
						+ getX( container ), pos.y + getY( container ) );
				containerStack.push( current );
			}
			else
			{
				containerStack.push( new ContainerPosition( getX( container ),
						getY( container ) ) );
			}
		}
	}

	private void clip( IContainerArea container )
	{
		ContainerPosition curPos = getContainerPosition( );
		int startX = curPos.x + getX( container );
		int startY = curPos.y + getY( container );
		int width = getWidth( container );
		int height = getHeight( container );
		pageGraphic.clip( startX, startY, width, height );
	}

	/**
	 * This method will be invoked while a containerArea ends.
	 * 
	 * @param container
	 *            the ContainerArea specified from layout
	 */
	protected void endContainer( IContainerArea container )
	{
		if ( container instanceof PageArea )
		{
			pageGraphic.dispose( );
		}
		else
		{
			if ( needClip( container ) )
			{
				pageGraphic.clipRestore( );
			}
		}
		if ( !containerStack.isEmpty( ) )
		{
			containerStack.pop( );
		}
	}

	private boolean needClip( IContainerArea container )
	{
		//only cell and inline container(image, inline text etc) need clip
		return ( container instanceof CellArea  ) || (container instanceof InlineContainerArea);
	}

	/**
	 * Creates a new PDF page
	 * 
	 * @param page
	 *            the PageArea specified from layout
	 */
	protected void newPage( IContainerArea page )
	{
		pageHeight = getHeight( page );
		pageWidth = getWidth( page );

		Color backgroundColor = PropertyUtil.getColor( page.getStyle( )
				.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
		pageGraphic = pageDevice.newPage( pageWidth, pageHeight,
				backgroundColor );

		// Draws background image for the new page. if the background image is
		// NOT set, draw nothing.
		drawBackgroundImage( page.getStyle( ), 0, 0, pageWidth, pageHeight );
	}

	protected ContainerPosition getContainerPosition( )
	{
		ContainerPosition curPos;
		if ( !containerStack.isEmpty( ) )
			curPos = (ContainerPosition) containerStack.peek( );
		else
			curPos = new ContainerPosition( 0, 0 );
		return curPos;
	}

	/**
	 * draw background image for the container
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
	private void drawBackgroundImage( IStyle containerStyle, int startX,
			int startY, int width, int height )
	{
		String imageUri = PropertyUtil.getBackgroundImage( containerStyle
				.getProperty( StyleConstants.STYLE_BACKGROUND_IMAGE ) );
		if ( imageUri == null )
		{
			return;
		}
		String imageUrl = imageUri;
		if ( reportDesign != null )
		{
			URL url = reportDesign.findResource( imageUri,
					IResourceLocator.IMAGE );
			if ( url != null )
			{
				imageUrl = url.toExternalForm( );
			}
		}

		if ( imageUrl == null || "".equals( imageUrl ) ) //$NON-NLS-1$
		{
			return;
		}

		FloatValue positionValX = (FloatValue) containerStyle
				.getProperty( StyleConstants.STYLE_BACKGROUND_POSITION_X );
		FloatValue positionValY = (FloatValue) containerStyle
				.getProperty( StyleConstants.STYLE_BACKGROUND_POSITION_Y );

		if ( positionValX == null || positionValY == null )
			return;
		boolean xMode, yMode;
		float positionX, positionY;
		if ( positionValX.getPrimitiveType( ) == CSSPrimitiveValue.CSS_PERCENTAGE )
		{
			positionX = PropertyUtil.getPercentageValue( positionValX );
			xMode = true;
		}
		else
		{
			positionX = getScaledValue( positionValX );
			xMode = false;
		}
		if ( positionValY.getPrimitiveType( ) == CSSPrimitiveValue.CSS_PERCENTAGE )
		{
			positionY = PropertyUtil.getPercentageValue( positionValY );
			yMode = true;
		}
		else
		{
			positionY = getScaledValue( positionValY );
			yMode = false;
		}
		drawBackgroundImage( imageUrl, startX, startY, width, height,
				positionX, positionY, containerStyle.getBackgroundRepeat( ),
				xMode, yMode );
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
		ContainerPosition curPos = getContainerPosition( );
		// content is null means it is the internal line area which has no
		// content mapping, so it has no background/border etc.
		if ( container.getContent( ) != null )
		{
			int layoutX = curPos.x + getX( container );
			int layoutY = curPos.y + getY( container );
			// the container's start position (the left top corner of the
			// container)
			int startX = layoutX;
			int startY = layoutY;

			// the dimension of the container
			int width = getWidth( container );
			int height = getHeight( container );

			// Draws background color for the container, if the backgound
			// color is NOT set, draw nothing.
			Color bc = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
			pageGraphic.drawBackgroundColor( bc, startX, startY, width, height );

			// Draws background image for the container. if the background
			// image is NOT set, draw nothing.
			drawBackgroundImage( style, startX, startY, width, height );

			// the width of each border
			int borderTopWidth = getScaledValue( style
					.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
			int borderLeftWidth = getScaledValue( style
					.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
			int borderBottomWidth = getScaledValue( style
					.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
			int borderRightWidth = getScaledValue( style
					.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );

			if ( borderTopWidth > 0 || borderLeftWidth > 0
					|| borderBottomWidth > 0 || borderRightWidth > 0 )
			{
				// the color of each border
				Color borderTopColor = PropertyUtil.getColor( style
						.getProperty( StyleConstants.STYLE_BORDER_TOP_COLOR ) );
				Color borderRightColor = PropertyUtil
						.getColor( style
								.getProperty( StyleConstants.STYLE_BORDER_RIGHT_COLOR ) );
				Color borderBottomColor = PropertyUtil
						.getColor( style
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_COLOR ) );
				Color borderLeftColor = PropertyUtil.getColor( style
						.getProperty( StyleConstants.STYLE_BORDER_LEFT_COLOR ) );

				// Caches the border info
				BorderInfo[] borders = new BorderInfo[4];
				borders[BorderInfo.TOP_BORDER] = new BorderInfo(
						layoutX,
						layoutY + borderTopWidth / 2,
						layoutX + getWidth( container ),
						layoutY + borderTopWidth / 2,
						borderTopWidth,
						borderTopColor,
						style
								.getProperty( StyleConstants.STYLE_BORDER_TOP_STYLE ),
						BorderInfo.TOP_BORDER );
				borders[BorderInfo.RIGHT_BORDER] = new BorderInfo(
						layoutX + getWidth( container ) - borderRightWidth / 2,
						layoutY,
						layoutX + getWidth( container ) - borderRightWidth / 2,
						layoutY + getHeight( container ),
						borderRightWidth,
						borderRightColor,
						style
								.getProperty( StyleConstants.STYLE_BORDER_RIGHT_STYLE ),
						BorderInfo.RIGHT_BORDER );
				borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo(
						layoutX,
						layoutY + getHeight( container ) - borderBottomWidth
								/ 2,
						layoutX + getWidth( container ),
						layoutY + getHeight( container ) - borderBottomWidth
								/ 2,
						borderBottomWidth,
						borderBottomColor,
						style
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_STYLE ),
						BorderInfo.BOTTOM_BORDER );
				borders[BorderInfo.LEFT_BORDER] = new BorderInfo(
						layoutX + borderLeftWidth / 2,
						layoutY,
						layoutX + borderLeftWidth / 2,
						layoutY + getHeight( container ),
						borderLeftWidth,
						borderLeftColor,
						style
								.getProperty( StyleConstants.STYLE_BORDER_LEFT_STYLE ),
						BorderInfo.LEFT_BORDER );

				// Draws the four borders of the container if there are any.
				// Each border is showed as a line.
				drawBorder( borders );
			}
		}
	}

	/**
	 * Draws a chunk of text at the pdf.
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
		IStyle style = text.getStyle( );
		assert style != null;

		// style.getFontVariant(); small-caps or normal
		float fontSize = text.getFontInfo( ).getFontSize( );
		int x = textX + getScaledValue( (int) ( fontSize * hTextSpace ) );
		int y = textY + getScaledValue( (int) ( fontSize * vTextSpace ) );
		FontInfo fontInfo = new FontInfo( text.getFontInfo( ) );
		fontInfo.setFontSize( fontInfo.getFontSize( ) * scale );
		int characterSpacing = getScaledValue( PropertyUtil
				.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_LETTER_SPACING ) ) );
		int wordSpacing = getScaledValue( PropertyUtil
				.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_WORD_SPACING ) ) );

		Color color = PropertyUtil.getColor( style
				.getProperty( StyleConstants.STYLE_COLOR ) );

		CSSValue align = text.getStyle( ).getProperty(
				StyleConstants.STYLE_TEXT_ALIGN );

		// draw the overline,throughline or underline for the text if it has
		// any.
		boolean linethrough = IStyle.LINE_THROUGH_VALUE.equals( style
				.getProperty( IStyle.STYLE_TEXT_LINETHROUGH ) );
		boolean overline = IStyle.OVERLINE_VALUE.equals( style
				.getProperty( IStyle.STYLE_TEXT_OVERLINE ) );
		boolean underline = IStyle.UNDERLINE_VALUE.equals( style
				.getProperty( IStyle.STYLE_TEXT_UNDERLINE ) );
		int width = getScaledValue( text.getWidth( ) );
		int height = getScaledValue( text.getHeight( ) );
		pageGraphic.clipSave( );
		int clipWidth = (int) ( width + height
				* EmitterUtil.getItalicHorizontalCoefficient( ) );
		pageGraphic.clip( textX, textY, clipWidth, height );
		TextStyle textStyle = new TextStyle( fontInfo, characterSpacing,
				wordSpacing, color, linethrough, overline, underline, align );
		drawTextAt( text, x, y, width, height, textStyle );
		pageGraphic.clipRestore( );
	}

	protected void drawTextAt( ITextArea text, int x, int y, int width,
			int height, TextStyle textStyle )
	{
		pageGraphic.drawText( text.getText( ), x, y, width, height, textStyle );
	}

	/**
	 * Draws image at the contentByte
	 * 
	 * @param image
	 *            the ImageArea specified from the layout
	 */
	protected void drawImage( IImageArea image )
	{
		// TODO: drawimage
		ContainerPosition curPos = getContainerPosition( );
		int imageX = curPos.x + getX( image );
		int imageY = curPos.y + getY( image );
		IImageContent imageContent = ( (IImageContent) image.getContent( ) );

		InputStream in = null;
		boolean isSvg = false;
		int height = getHeight( image );
		int width = getWidth( image );
		String helpText = imageContent.getHelpText( );
		try
		{
			// lookup the source type of the image area
			String uri = imageContent.getURI( );
			String extension = imageContent.getExtension( );
			switch ( imageContent.getImageSource( ) )
			{
				case IImageContent.IMAGE_FILE :
				case IImageContent.IMAGE_URL :
					if ( null == uri )
						return;
					if ( uri != null && uri.endsWith( ".svg" ) )
					{
						isSvg = true;
					}
					if ( isSvg )
					{
						pageGraphic.drawImage( transSvgToArray( uri ),
								extension, imageX, imageY, height, width,
								helpText );
					}
					else
					{
						pageGraphic.drawImage( uri, extension, imageX, imageY,
								height, width, helpText );
					}
					break;
				case IImageContent.IMAGE_NAME :
				case IImageContent.IMAGE_EXPRESSION :
					byte[] data = imageContent.getData( );
					if ( null == data )
						return;
					in = new ByteArrayInputStream( data );
					isSvg = ( ( imageContent.getMIMEType( ) != null ) && imageContent
							.getMIMEType( ).equalsIgnoreCase( "image/svg+xml" ) ) //$NON-NLS-1$
							|| ( ( uri != null ) && uri.toLowerCase( )
									.endsWith( ".svg" ) ) //$NON-NLS-1$
							|| ( ( imageContent.getExtension( ) != null ) && imageContent
									.getExtension( ).toLowerCase( ).endsWith(
											".svg" ) ); //$NON-NLS-1$
					if ( isSvg )
						data = transSvgToArray( in );
					pageGraphic.drawImage( data, extension, imageX, imageY,
							height, width, helpText );
					break;
			}
			if ( in == null )
				return;
		}
		catch ( Throwable t )
		{
			log( t, Level.WARNING );
		}
		finally
		{
			if ( in != null )
			{
				try
				{
					in.close( );
					in = null;
				}
				catch ( IOException e )
				{
					log( e, Level.WARNING );
				}
			}
		}
	}

	private byte[] transSvgToArray( String uri ) throws IOException
	{
		InputStream in = null;
		in = new URL( uri ).openStream( );
		return transSvgToArray( in );
	}

	private byte[] transSvgToArray( InputStream inputStream )
			throws IOException
	{
		JPEGTranscoder transcoder = new JPEGTranscoder( );
		// set the transcoding hints
		transcoder.addTranscodingHint( JPEGTranscoder.KEY_QUALITY, new Float(
				.8 ) );
		// create the transcoder input
		TranscoderInput input = new TranscoderInput( inputStream );
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
		return ostream.toByteArray( );
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
				pageGraphic.drawLine( bi.startX, bi.startY, bi.endX, bi.endY,
						bi.borderWidth, bi.borderColor, "dotted" ); //$NON-NLS-1$
			}
		}
		if ( null != dashed )
		{
			for ( Iterator it = dashed.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				pageGraphic.drawLine( bi.startX, bi.startY, bi.endX, bi.endY,
						bi.borderWidth, bi.borderColor, "dashed" ); //$NON-NLS-1$
			}
		}
		if ( null != solid )
		{
			for ( Iterator it = solid.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				pageGraphic.drawLine( bi.startX, bi.startY, bi.endX, bi.endY,
						bi.borderWidth, bi.borderColor, "solid" ); //$NON-NLS-1$
			}
		}
		if ( null != dbl )
		{
			for ( Iterator it = dbl.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				int borderWidth = bi.borderWidth;
				int outerBorderWidth = borderWidth / 4;
				int innerBorderWidth = borderWidth / 4;

				int startX = bi.startX;
				int startY = bi.startY;
				int endX = bi.endX;
				int endY = bi.endY;
				Color borderColor = bi.borderColor;
				switch ( bi.borderType )
				{
					case BorderInfo.TOP_BORDER :
						pageGraphic.drawLine( 
								startX, 
								startY - borderWidth / 2 + outerBorderWidth / 2, 
								endX, endY - borderWidth / 2 + outerBorderWidth / 2,
								outerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						pageGraphic.drawLine( 
								startX, 
								startY + borderWidth / 2 - innerBorderWidth / 2, 
								endX, endY + borderWidth / 2 - innerBorderWidth / 2,
								innerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						break;
					case BorderInfo.RIGHT_BORDER :
						pageGraphic.drawLine( 
								startX + borderWidth / 2 - outerBorderWidth / 2, 
								startY, 
								endX + borderWidth / 2 - outerBorderWidth / 2, 
								endY,
								outerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						pageGraphic.drawLine( 
								startX - borderWidth / 2 + innerBorderWidth / 2, 
								startY,
								endX - borderWidth / 2 + innerBorderWidth / 2, 
								endY,
								innerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						break;
					case BorderInfo.BOTTOM_BORDER :
						pageGraphic.drawLine( 
								startX, 
								startY + borderWidth / 2 - outerBorderWidth / 2, 
								endX,
								endY + borderWidth / 2 - outerBorderWidth / 2,
								outerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						pageGraphic.drawLine( 
								startX,
								startY - borderWidth / 2 + innerBorderWidth / 2, 
								endX, 
								endY - borderWidth / 2 + innerBorderWidth / 2, 
								innerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						break;
					case BorderInfo.LEFT_BORDER :
						pageGraphic.drawLine( 
								startX - borderWidth / 2 + outerBorderWidth / 2,
								startY, 
								endX - borderWidth / 2 + outerBorderWidth / 2, 
								endY,
								outerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						pageGraphic.drawLine( 
								startX + borderWidth / 2 - innerBorderWidth / 2, 
								startY,
								endX + borderWidth / 2 - innerBorderWidth / 2, 
								endY,
								innerBorderWidth, borderColor, "solid" ); //$NON-NLS-1$
						break;
				}
			}
		}
	}

	/**
	 * Draws the backgound image at the contentByteUnder of the pdf with the
	 * given offset
	 * 
	 * @param imageURI
	 *            the URI referring the image
	 * @param x
	 *            the start X coordinate at the pdf where the image is
	 *            positioned
	 * @param y
	 *            the start Y coordinate at the pdf where the image is
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
	private void drawBackgroundImage( String imageURI, int x, int y, int width,
			int height, float positionX, float positionY, String repeat,
			boolean xMode, boolean yMode )
	{
		// the image URI is empty, ignore it.
		if ( null == imageURI )
		{
			return;
		}
		String imageUrl = getImageUrl( imageURI );
		if ( imageUrl == null || "".equals( imageUrl ) ) //$NON-NLS-1$
		{
			return;
		}

		// the background-repeat property is empty, use "repeat".
		if ( null == repeat )
		{
			repeat = "repeat"; //$NON-NLS-1$
		}

		Image img = null;
		try
		{
			img = Image.getInstance( imageUrl );
			int absPosX, absPosY;
			if ( xMode )
			{
				absPosX = (int) ( ( width - img.scaledWidth( )
						* PDFConstants.LAYOUT_TO_PDF_RATIO ) * positionX );
			}
			else
			{
				absPosX = (int)positionX;
			}
			if ( yMode )
			{
				absPosY = (int) ( ( height - img.scaledHeight( )
						* PDFConstants.LAYOUT_TO_PDF_RATIO ) * positionY );
			}
			else
			{
				absPosY = (int)positionY;
			}
			pageGraphic.drawBackgroundImage( x, y, width, height, repeat,
					imageUrl, absPosX, absPosY );
		}
		catch ( Exception e )
		{
			log( e, Level.WARNING );
		}
	}

	private String getImageUrl( String imageUri )
	{
		String imageUrl = imageUri;
		if ( reportDesign != null )
		{
			URL url = reportDesign.findResource( imageUri,
					IResourceLocator.IMAGE );
			if ( url != null )
			{
				imageUrl = url.toExternalForm( );
			}
		}
		return imageUrl;
	}

	protected int getX( IArea area )
	{
		return getScaledValue( area.getX( ) );
	}

	protected int getY( IArea area )
	{
		return getScaledValue( area.getY( ) );
	}

	protected int getWidth( IArea area )
	{
		return getScaledValue( area.getWidth( ) );
	}

	protected int getHeight( IArea area )
	{
		return getScaledValue( area.getHeight( ) );
	}

	protected int getScaledValue( int value )
	{
		return (int) ( value * scale );
	}

	private int getScaledValue( CSSValue cssValue )
	{
		return getScaledValue( PropertyUtil.getDimensionValue( cssValue ) );
	}
}
