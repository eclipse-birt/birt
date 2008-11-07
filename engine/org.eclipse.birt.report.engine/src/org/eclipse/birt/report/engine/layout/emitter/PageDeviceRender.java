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

package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.TextStyle;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;
import org.eclipse.birt.report.engine.layout.emitter.TableBorder.Border;
import org.eclipse.birt.report.engine.layout.emitter.TableBorder.BorderSegment;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.util.SvgFile;
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
	
	public static final int ignoredOverflow = 3000;

	protected float scale;

	protected IReportRunnable reportRunnable;

	protected ReportDesignHandle reportDesign;

	protected IReportContext context;

	protected IEmitterServices services;

	protected int currentX;
	protected int currentY;
	
	protected Stack rowStyleStack = new Stack();
	
	/**
	 * for any (x,y) in the ContainerArea, if x<offsetX, the (x,y) will be
	 * omitted.
	 */
	protected int offsetX = 0;

	/**
	 * for any (x,y) in the ContainerArea, if y<offsetY, the (x,y) will be
	 * omitted.
	 */
	protected int offsetY = 0;

	protected Logger logger = Logger.getLogger( PageDeviceRender.class
			.getName( ) );

	protected IPageDevice pageDevice;

	protected IPage pageGraphic;

	/**
	 * Gets the output format.
	 */
	public abstract String getOutputFormat( );

	public abstract IPageDevice createPageDevice( String title, String author,
			String comments, IReportContext context, IReportContent report )
			throws Exception;

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
		String author = designHandle.getAuthor( );
		String description = designHandle
				.getStringProperty( IModuleModel.DESCRIPTION_PROP );
		try
		{
			pageDevice = createPageDevice( title, author, description, context,
					rc );
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
		drawText( textArea );
	}

	public void visitImage( IImageArea imageArea )
	{
		drawImage( imageArea );
	}

	public void visitAutoText( ITemplateArea templateArea )
	{
	}

	/**
	 * Visits a container
	 * 
	 * @param container
	 * @param offsetX
	 * @param offsetY
	 */
	public void visitContainer( IContainerArea container )
	{
		if ( container instanceof PageArea )
		{
			visitPage( (PageArea) container );
		}
		else
		{
			startContainer( container );
			visitChildren( container );
			endContainer( container );	
		}
	}

	protected void visitChildren( IContainerArea container )
	{
		Iterator iter = container.getChildren( );
		while ( iter.hasNext( ) )
		{
			IArea child = (IArea) iter.next( );
			child.accept( this );
		}
	}
	
	private static final int BODY_HEIGHT = 1;
	private static final int BODY_WIDTH = 2;
	
	private int getActualPageBodyWidth( PageArea page )
	{
		return getActualPageBodySize( page, BODY_WIDTH );
	}
	
	private int getActualPageBodyHeight( PageArea page )
	{
		return getActualPageBodySize( page, BODY_HEIGHT );
	}
	
	private int getActualPageBodySize( PageArea page, int direction )
	{
		int pref = 0;
		IContainerArea body = page.getBody( );
		if ( body == null )
		{
			return 0;
		}
		Iterator iter = page.getBody( ).getChildren( );
		while ( iter.hasNext( ) )
		{
			AbstractArea area = (AbstractArea) iter.next( );
			if( direction == BODY_HEIGHT )
			{
				pref = Math.max( pref, area.getY( )
						+ area.getHeight( ) );		
			}
			else
			{
				pref = Math.max( pref, area.getX( )
						+ area.getWidth( ) );
			}
		}
		return pref;
	}
	
	/**
	 * The container may be a TableArea, RowArea, etc. Or just the
	 * border of textArea/imageArea. This method draws the border and background
	 * of the given container.
	 * 
	 * @param container
	 *            the ContainerArea specified from layout
	 */
	protected void startContainer( IContainerArea container )
	{
		if ( container.needClip( ) )
		{
			startClip( container );
		}
		if ( container instanceof RowArea )
		{
			rowStyleStack.push( container.getStyle( ) );
		}
		else if ( container instanceof CellArea )
		{
			drawCell(container);
		}
		else
		{
			drawContainer( container );
		}
		currentX += getX( container );
		currentY += getY( container );
	}
	
	protected void drawCell( IContainerArea container )
	{
		Color rowbc = null;
		String rowImageUrl = null;
		IStyle rowStyle = null;
		// get the style of the row
		if ( rowStyleStack.size( ) > 0 )
		{
			rowStyle = (IStyle) rowStyleStack.peek( );
			rowbc = PropertyUtil.getColor( rowStyle
					.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
			rowImageUrl = getBackgroundImageUrl( rowStyle );
		}

		IStyle style = container.getStyle( );
		Color bc = PropertyUtil.getColor( style
				.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
		String imageUrl = getBackgroundImageUrl( style );

		if ( rowbc != null || rowImageUrl != null || bc != null
				|| imageUrl != null )
		{
			// the container's start position (the left top corner of the
			// container)
			int startX = currentX + getX( container );
			int startY = currentY + getY( container );

			// the dimension of the container
			int width = getWidth( container );
			int height = getHeight( container );

			if ( rowbc != null )
			{
				pageGraphic.drawBackgroundColor( rowbc, startX, startY, width,
						height );
			}
			if ( rowImageUrl != null )
			{
				drawBackgroundImage( rowStyle, rowImageUrl, startX, startY,
						width, height );
			}
			if ( bc != null )
			{
				// Draws background color for the container, if the background
				// color is NOT set, draws nothing.
				pageGraphic.drawBackgroundColor( bc, startX, startY, width,
						height );
			}
			if ( imageUrl != null )
			{
				// Draws background image for the container. if the background
				// image is NOT set, draws nothing.
				drawBackgroundImage( style, imageUrl, startX, startY, width, height );
			}
		}
	}
	
	
	/**
	 * Output a layout PageArea, extend the pageArea into multiple physical pages if needed.
	 * @param page
	 */
	protected void visitPage( PageArea page )
	{
		scale = page.getScale( );
		if ( page.isExtendToMultiplePages( ) )
		{
			// the actual used page body size.
			int pageBodyHeight = getActualPageBodyHeight( page );
			int pageBodyWidth = getActualPageBodyWidth( page );
			// get the user defined page body size.
			IContainerArea pageBody = page.getBody( );
			int definedBodyHeight = 0;
			int definedBodyWidth = 0;
			if ( pageBody != null )
			{
				definedBodyHeight = pageBody.getHeight( );
				definedBodyWidth = pageBody.getWidth( );	
			}
			
			if ( pageBodyHeight > definedBodyHeight )
			{
				addExtendDirection( EXTEND_ON_VERTICAL );
			}
			if ( pageBodyWidth > definedBodyWidth )
			{
				addExtendDirection( EXTEND_ON_HORIZONTAL );
			}

			offsetX = 0;
			offsetY = 0;
			if ( extendDirection == EXTEND_NONE )
			{
				addPage( page );
			}
			else if ( extendDirection == EXTEND_ON_HORIZONTAL )
			{
				do
				{
					addPage( page );
					offsetX += definedBodyWidth;
				} while ( offsetX < pageBodyWidth - ignoredOverflow );
			}

			else if ( extendDirection == EXTEND_ON_VERTICAL )
			{
				do
				{
					addPage( page );
					offsetY += definedBodyHeight;
				} while ( offsetY < pageBodyHeight - ignoredOverflow );
			}

			else if ( extendDirection == EXTEND_ON_HORIZONTAL_AND_VERTICAL )
			{
				do
				{
					do
					{
						addPage( page );
						offsetX += definedBodyWidth;
					} while ( offsetX < pageBodyWidth - ignoredOverflow );
					offsetX = 0;
					offsetY += definedBodyHeight;
				} while ( offsetY < pageBodyHeight - ignoredOverflow );
			}
			setExtendDirection( EXTEND_NONE );
		}
		else
		{
			addPage( page );
		}
	}
	
	/**
	 * Creates a page in given output format.
	 * 
	 * @param page	a layout page.
	 */
	protected void addPage( PageArea page )
	{
		// PageArea -> pageRoot -> Header/footer/body
		newPage( page );
		currentX = 0;
		currentY = 0;
		IContainerArea pageRoot = page.getRoot( );
		
		if ( pageRoot != null )
		{
			startContainer( page.getRoot( ) );
			IContainerArea pageHeader = page.getHeader( );
			if ( pageHeader != null )
			{
				visitContainer( pageHeader );	
			}
			IContainerArea pageFooter = page.getFooter( );
			if ( pageFooter != null )
			{
				visitContainer( pageFooter );	
			}
			IContainerArea pageBody = page.getBody( );
			if ( pageBody != null )
			{
				startContainer( pageBody );
				enterBody( );
				visitChildren( pageBody );
				exitBody( );
				endContainer( pageBody );	
			}
			endContainer( page.getRoot( ) );	
		}
		
		endContainer( page );
	}
	
	private void enterBody()
	{
		currentX -= offsetX;
		currentY -= offsetY;
	}
	
	private void exitBody()
	{
		currentX += offsetX;
		currentY += offsetY;
	}

	/**
	 * This method will be invoked while a containerArea ends.
	 * 
	 * @param container
	 *            the ContainerArea specified from layout
	 */
	protected void endContainer( IContainerArea container )
	{
		currentX -= getX( container );
		currentY -= getY( container );

		if ( container instanceof PageArea )
		{
			pageGraphic.dispose( );
		}
		else
		{
			if(container instanceof RowArea)
			{
				rowStyleStack.pop( );
			}
			if ( container instanceof TableArea )
			{
				drawTableBorder( (TableArea) container );
			}
			else if ( !( container instanceof CellArea ) )
			{
				BorderInfo[] borders = cacheBorderInfo( container );
				drawBorder( borders );
			}
			if ( container.needClip( ) )
			{
				endClip( );
			}
		}
	}

	/**
	 * Creates a new PDF page
	 * 
	 * @param page
	 *            the PageArea specified from layout
	 */
	protected void newPage( IContainerArea page )
	{
		int pageHeight = getHeight( page );
		int pageWidth = getWidth( page );

		Color backgroundColor = PropertyUtil.getColor( page.getStyle( )
				.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
		pageGraphic = pageDevice.newPage( pageWidth, pageHeight,
				backgroundColor );
		IStyle style = page.getStyle( );
		String imageUrl = getBackgroundImageUrl( style );
		if ( imageUrl != null )
		{
			// Draws background image for the new page. if the background image
			// is
			// NOT set, draw nothing.
			drawBackgroundImage( style, imageUrl, 0, 0, pageWidth, pageHeight );
		}

	}

	private int extendDirection = EXTEND_NONE;
	public static final int EXTEND_NONE = 0;
	public static final int EXTEND_ON_HORIZONTAL = 1;
	public static final int EXTEND_ON_VERTICAL = 2;
	public static final int EXTEND_ON_HORIZONTAL_AND_VERTICAL = 3;

	protected int getExtendDirection( )
	{
		return this.extendDirection;
	}

	protected void setExtendDirection( int direction )
	{
		this.extendDirection = direction;
	}

	protected void addExtendDirection( int direction )
	{
		this.extendDirection |= direction;
	}

	private void startClip( IArea area )
	{
		int startX = currentX + getX( area );
		int startY = currentY + getY( area );
		int width = getWidth( area );
		int height = getHeight( area );
		pageGraphic.startClip( startX, startY, width, height );
	}
	
	private void endClip( )
	{
		pageGraphic.endClip( );
	}
	
	/**
	 * draw background image for the container
	 * 
	 * @param containerStyle
	 *            the style of the container we draw background image for
	 * @param imageUrl
	 *            the url of background image 
	 * @param startX
	 *            the absolute horizontal position of the container
	 * @param startY
	 *            the absolute vertical position of the container
	 * @param width
	 *            container width
	 * @param height
	 *            container height
	 */
	private void drawBackgroundImage( IStyle containerStyle, String imageUrl, int startX,
			int startY, int width, int height )
	{
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
	
	protected String getBackgroundImageUrl(IStyle style)
	{
		String imageUri = PropertyUtil.getBackgroundImage( style
				.getProperty( StyleConstants.STYLE_BACKGROUND_IMAGE ) );
		if ( imageUri != null )
		{
			String url = getImageUrl( imageUri );
			if(url!=null && url.length( )>0)
			{
				return url;
			}
		}
		return null;
	}

	
	/**
	 * Draws a container's border, and its background color/image if there is
	 * any.
	 * 
	 * @param container
	 *            the containerArea whose border and background need to be drew
	 */
	protected void drawContainer( IContainerArea container )
	{
		// get the style of the container
		IStyle style = container.getStyle( );
		if ( null == style )
		{
			return;
		}
		// content is null means it is the internal line area which has no
		// content mapping, so it has no background/border etc.
		if ( container.getContent( ) != null )
		{
			
			// Draws background color for the container, if the background
			// color is NOT set, draws nothing.
			Color bc = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BACKGROUND_COLOR ) );
			String imageUrl = getBackgroundImageUrl( style );

			if ( bc != null || imageUrl != null )
			{
				// the container's start position (the left top corner of the
				// container)
				int startX = currentX + getX( container );
				int startY = currentY + getY( container );
	
				// the dimension of the container
				int width = getWidth( container );
				int height = getHeight( container );
	
				if ( bc != null )
				{
					pageGraphic.drawBackgroundColor( bc, startX, startY, width,
							height );
				}
				if ( imageUrl != null )
				{
					// Draws background image for the container. if the
					// background
					// image is NOT set, draws nothing.
					drawBackgroundImage( style, imageUrl, startX, startY,
							width, height );
				}
			}
		}
	}

	private BorderInfo[] cacheCellBorder( CellArea container )
	{
		// get the style of the container
		IStyle style = container.getStyle( );
		if ( null == style )
		{
			return null;
		}
		if ( container.getContent( ) == null )
		{
			return null;
		}
		// the width of each border
		int borderTopWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
		int borderLeftWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
		int borderBottomWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
		int borderRightWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );

		if ( borderTopWidth > 0 || borderLeftWidth > 0 || borderBottomWidth > 0
				|| borderRightWidth > 0 )
		{
			// the color of each border
			Color borderTopColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_TOP_COLOR ) );
			Color borderRightColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_RIGHT_COLOR ) );
			Color borderBottomColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_COLOR ) );
			Color borderLeftColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_LEFT_COLOR ) );

			// Caches the border info
			BorderInfo[] borders = new BorderInfo[4];
			borders[BorderInfo.TOP_BORDER] = new BorderInfo( 0, 0, 0, 0,
					borderTopWidth, borderTopColor,
					style.getProperty( StyleConstants.STYLE_BORDER_TOP_STYLE ),
					BorderInfo.TOP_BORDER );
			borders[BorderInfo.RIGHT_BORDER] = new BorderInfo(
					0,
					0,
					0,
					0,
					borderRightWidth,
					borderRightColor,
					style.getProperty( StyleConstants.STYLE_BORDER_RIGHT_STYLE ),
					BorderInfo.RIGHT_BORDER );
			borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo(
					0,
					0,
					0,
					0,
					borderBottomWidth,
					borderBottomColor,
					style
							.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_STYLE ),
					BorderInfo.BOTTOM_BORDER );
			borders[BorderInfo.LEFT_BORDER] = new BorderInfo(
					0,
					0,
					0,
					0,
					borderLeftWidth,
					borderLeftColor,
					style.getProperty( StyleConstants.STYLE_BORDER_LEFT_STYLE ),
					BorderInfo.LEFT_BORDER );
			return borders;
		}
		return null;
	}

	private BorderInfo[] cacheBorderInfo( IContainerArea container )
	{
		// get the style of the container
		IStyle style = container.getStyle( );
		if ( null == style )
		{
			return null;
		}
		if ( container.getContent( ) == null )
		{
			return null;
		}
		// the width of each border
		int borderTopWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
		int borderLeftWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
		int borderBottomWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
		int borderRightWidth = getScaledValue( style
				.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );

		if ( borderTopWidth > 0 || borderLeftWidth > 0 || borderBottomWidth > 0
				|| borderRightWidth > 0 )
		{
			// the color of each border
			Color borderTopColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_TOP_COLOR ) );
			Color borderRightColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_RIGHT_COLOR ) );
			Color borderBottomColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_COLOR ) );
			Color borderLeftColor = PropertyUtil.getColor( style
					.getProperty( StyleConstants.STYLE_BORDER_LEFT_COLOR ) );

			int startX = currentX + getX( container );
			int startY = currentY + getY( container );

			// Caches the border info
			BorderInfo[] borders = new BorderInfo[4];
			borders[BorderInfo.TOP_BORDER] = new BorderInfo( startX, startY
					+ borderTopWidth / 2, startX + getWidth( container ),
					startY + borderTopWidth / 2, borderTopWidth,
					borderTopColor,
					style.getProperty( StyleConstants.STYLE_BORDER_TOP_STYLE ),
					BorderInfo.TOP_BORDER );
			borders[BorderInfo.RIGHT_BORDER] = new BorderInfo(
					startX + getWidth( container ) - borderRightWidth / 2,
					startY,
					startX + getWidth( container ) - borderRightWidth / 2,
					startY + getHeight( container ),
					borderRightWidth,
					borderRightColor,
					style.getProperty( StyleConstants.STYLE_BORDER_RIGHT_STYLE ),
					BorderInfo.RIGHT_BORDER );
			borders[BorderInfo.BOTTOM_BORDER] = new BorderInfo(
					startX,
					startY + getHeight( container ) - borderBottomWidth / 2,
					startX + getWidth( container ),
					startY + getHeight( container ) - borderBottomWidth / 2,
					borderBottomWidth,
					borderBottomColor,
					style
							.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_STYLE ),
					BorderInfo.BOTTOM_BORDER );
			borders[BorderInfo.LEFT_BORDER] = new BorderInfo(
					startX + borderLeftWidth / 2,
					startY,
					startX + borderLeftWidth / 2,
					startY + getHeight( container ),
					borderLeftWidth,
					borderLeftColor,
					style.getProperty( StyleConstants.STYLE_BORDER_LEFT_STYLE ),
					BorderInfo.LEFT_BORDER );
			return borders;
		}
		return null;
	}

	/**
	 * Draws a text area.
	 * 
	 * @param text
	 *            the textArea to be drawn.
	 */
	protected void drawText( ITextArea text )
	{
		IStyle style = text.getStyle( );
		assert style != null;

		int textX = currentX + getX( text );
		int textY = currentY + getY( text );
		// style.getFontVariant(); small-caps or normal
		float fontSize = text.getFontInfo( ).getFontSize( );
		int x = textX + getScaledValue( (int) ( fontSize * H_TEXT_SPACE ) );
		int y = textY + getScaledValue( (int) ( fontSize * V_TEXT_SPACE ) );
		FontInfo fontInfo = new FontInfo( text.getFontInfo( ) );
		fontInfo.setFontSize( fontInfo.getFontSize( ) * scale );
		int characterSpacing = getScaledValue( PropertyUtil
				.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_LETTER_SPACING ) ) );
		int wordSpacing = getScaledValue( PropertyUtil.getDimensionValue( style
				.getProperty( StyleConstants.STYLE_WORD_SPACING ) ) );

		Color color = PropertyUtil.getColor( style
				.getProperty( StyleConstants.STYLE_COLOR ) );

		CSSValue align = style.getProperty( StyleConstants.STYLE_TEXT_ALIGN );

		// draw the overline,throughline or underline for the text if it has
		// any.
		boolean linethrough = IStyle.LINE_THROUGH_VALUE.equals( style
				.getProperty( IStyle.STYLE_TEXT_LINETHROUGH ) );
		boolean overline = IStyle.OVERLINE_VALUE.equals( style
				.getProperty( IStyle.STYLE_TEXT_OVERLINE ) );
		boolean underline = IStyle.UNDERLINE_VALUE.equals( style
				.getProperty( IStyle.STYLE_TEXT_UNDERLINE ) );
		boolean rtl = text instanceof TextArea ? ( ( (TextArea) text )
				.getRunLevel( ) & 1 ) != 0 : CSSConstants.CSS_RTL_VALUE
				.equals( style.getProperty( IStyle.STYLE_DIRECTION ) ); // bidi_hcg
		IContent content = text.getContent( );
		if ( content != null && content.getHyperlinkAction( ) != null )
		{
			IStyle contentStyle = content.getStyle( );
			CSSValue contentColor = contentStyle
					.getProperty( StyleConstants.STYLE_COLOR );
			if ( contentColor == null )
			{
				underline = true;
				color = Color.blue;
			}
		}

		TextStyle textStyle = new TextStyle( fontInfo, characterSpacing,
								wordSpacing, color, linethrough, overline, underline, rtl, align );
		drawTextAt( text, x, y, getWidth( text ), getHeight( text ), textStyle );
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
		int imageX = currentX + getX( image );
		int imageY = currentY + getY( image );
		IImageContent imageContent = ( (IImageContent) image.getContent( ) );

		InputStream in = null;
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
					if ( SvgFile.isSvg( uri ) )
					{
						pageGraphic.drawImage( uri, SvgFile
								.transSvgToArray( uri ), extension, imageX,
								imageY, height, width, helpText );
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
					String mimeType = imageContent.getMIMEType( );
					if ( SvgFile.isSvg( mimeType, uri, extension ) )
						data = SvgFile.transSvgToArray( in );
					pageGraphic.drawImage( uri, data, extension, imageX,
							imageY, height, width, helpText );
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

	private void drawBorder( TableBorder tb )
	{
		if ( null == tb )
			return;

		tb.findBreakPoints( );
		Border border = null;
		// draw column borders
		for ( Iterator i = tb.columnBorders.keySet( ).iterator( ); i.hasNext( ); )
		{
			Integer pos = (Integer) i.next( );
			if ( pos == tb.tableLRX )
			{
				continue;
			}
			border = (Border) tb.columnBorders.get( pos );
			for ( int j = 0; j < border.segments.size( ); j++ )
			{
				BorderSegment seg = (BorderSegment) border.segments.get( j );
				Border rs = (Border) tb.rowBorders.get( seg.start );
				Border re = (Border) tb.rowBorders.get( seg.end );
				if ( null == rs || null == re )
					continue;
				int sy = getScaledValue( rs.position + rs.width / 2 );
				int ey = getScaledValue( re.position + re.width / 2 );
				int x = getScaledValue( border.position + seg.width / 2 );
				if ( border.breakPoints.contains( new Integer( seg.start ) ) )
				{
					sy = getScaledValue( rs.position );
				}
				if ( border.breakPoints.contains( new Integer( seg.end ) ) )
				{
					if ( seg.end == tb.tableLRY )
					{
						ey = getScaledValue( re.position );
					}
					else
					{
						ey = getScaledValue( re.position + re.width );
					}
				}
				drawBorder( new BorderInfo( currentX + x, currentY + sy,
						currentX + x, currentY + ey,
						getScaledValue( seg.width ), seg.color, seg.style,
						BorderInfo.LEFT_BORDER ) );
			}
		}
		// draw right table border
		border = (Border) tb.columnBorders.get( tb.tableLRX );
		for ( int j = 0; j < border.segments.size( ); j++ )
		{
			BorderSegment seg = (BorderSegment) border.segments.get( j );
			Border rs = (Border) tb.rowBorders.get( seg.start );
			Border re = (Border) tb.rowBorders.get( seg.end );
			if ( null == rs || null == re )
				continue;
			int sy = getScaledValue( rs.position + rs.width / 2 );
			int ey = getScaledValue( re.position + re.width / 2 );
			int x = getScaledValue( border.position - seg.width / 2 );
			if ( border.breakPoints.contains( new Integer( seg.start ) ) )
			{
				sy = getScaledValue( rs.position );
			}
			if ( border.breakPoints.contains( new Integer( seg.end ) ) )
			{
				if ( seg.end == tb.tableLRY )
				{
					ey = getScaledValue( re.position );
				}
				else
				{
					ey = getScaledValue( re.position + re.width );
				}
			}
			drawBorder( new BorderInfo( currentX + x, currentY + sy, currentX
					+ x, currentY + ey, getScaledValue( seg.width ), seg.color,
					seg.style, BorderInfo.RIGHT_BORDER ) );
		}

		// draw row borders
		for ( Iterator i = tb.rowBorders.keySet( ).iterator( ); i.hasNext( ); )
		{
			Integer pos = (Integer) i.next( );
			if ( pos == tb.tableLRY )
			{
				continue;
			}

			border = (Border) tb.rowBorders.get( pos );
			for ( int j = 0; j < border.segments.size( ); j++ )
			{
				BorderSegment seg = (BorderSegment) border.segments.get( j );
				Border cs = (Border) tb.columnBorders.get( seg.start );
				Border ce = (Border) tb.columnBorders.get( seg.end );
				if ( null == cs || null == ce )
					continue;
				// we can also adjust the columns in this position
				int sx = getScaledValue( cs.position + cs.width / 2 );
				int ex = getScaledValue( ce.position + ce.width / 2 );
				int y = getScaledValue( border.position + seg.width / 2 );
				if ( border.breakPoints.contains( new Integer( seg.start ) ) )
				{
					if ( seg.start == tb.tableX && border.position != tb.tableY )
					{
						sx = getScaledValue( cs.position + cs.width );
					}
					else
					{
						sx = getScaledValue( cs.position );
					}
				}
				if ( border.breakPoints.contains( new Integer( seg.end ) ) )
				{
					if ( seg.end == tb.tableLRX )
					{
						if ( border.position == tb.tableY )
						{
							ex = getScaledValue( ce.position );
						}
						else
						{
							ex = getScaledValue( ce.position - ce.width );
						}
					}
					else
					{
						ex = getScaledValue( ce.position + ce.width );
					}
				}
				drawBorder( new BorderInfo( currentX + sx, currentY + y,
						currentX + ex, currentY + y,
						getScaledValue( seg.width ), seg.color, seg.style,
						BorderInfo.TOP_BORDER ) );
			}
		}
		// draw bottom table border
		border = (Border) tb.rowBorders.get( tb.tableLRY );
		for ( int j = 0; j < border.segments.size( ); j++ )
		{
			BorderSegment seg = (BorderSegment) border.segments.get( j );
			Border cs = (Border) tb.columnBorders.get( seg.start );
			Border ce = (Border) tb.columnBorders.get( seg.end );
			if ( null == cs || null == ce )
				continue;
			// we can also adjust the columns in this position
			int sx = getScaledValue( cs.position + cs.width / 2 );
			int ex = getScaledValue( ce.position + ce.width / 2 );
			int y = getScaledValue( border.position - seg.width / 2 );
			if ( border.breakPoints.contains( new Integer( seg.start ) ) )
			{
				sx = getScaledValue( cs.position );
			}
			if ( border.breakPoints.contains( new Integer( seg.end ) ) )
			{
				if ( seg.end == tb.tableLRX )
				{
					ex = getScaledValue( ce.position );
				}
				else
				{
					ex = getScaledValue( ce.position + ce.width );
				}
			}
			drawBorder( new BorderInfo( currentX + sx, currentY + y, currentX
					+ ex, currentY + y, getScaledValue( seg.width ), seg.color,
					seg.style, BorderInfo.BOTTOM_BORDER ) );
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
		if ( borders == null )
			return;
		// double>solid>dashed>dotted>none
		ArrayList dbl = null;
		ArrayList solid = null;
		ArrayList dashed = null;
		ArrayList dotted = null;

		for ( int i = 0; i < borders.length; i++ )
		{
			if ( "double".equals( borders[i].borderStyle ) )
			{
				if ( null == dbl )
				{
					dbl = new ArrayList( );
				}
				dbl.add( borders[i] );
			}
			else if ( "dashed".equals( borders[i].borderStyle ) )
			{
				if ( null == dashed )
				{
					dashed = new ArrayList( );
				}
				dashed.add( borders[i] );
			}
			else if ( "dotted".equals( borders[i].borderStyle ) )
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
				drawBorder( bi );
			}
		}
		if ( null != dashed )
		{
			for ( Iterator it = dashed.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				drawBorder( bi );
			}
		}
		if ( null != solid )
		{
			for ( Iterator it = solid.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				drawBorder( bi );
			}
		}
		if ( null != dbl )
		{
			for ( Iterator it = dbl.iterator( ); it.hasNext( ); )
			{
				BorderInfo bi = (BorderInfo) it.next( );
				drawDoubleBorder( bi );
			}
		}
	}

	private void drawBorder( BorderInfo bi )
	{
		if ( "double".equals( bi.borderStyle ) )
		{
			drawDoubleBorder( bi );
		}
		else
		{
			pageGraphic.drawLine( bi.startX, bi.startY, bi.endX, bi.endY,
					bi.borderWidth, bi.borderColor, bi.borderStyle );
		}
	}

	private void drawDoubleBorder( BorderInfo bi )
	{
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
			// Draws the outer border first, and then the inner border.
			case BorderInfo.TOP_BORDER :
				pageGraphic.drawLine( startX, startY - borderWidth / 2
						+ outerBorderWidth / 2, endX, endY - borderWidth / 2
						+ outerBorderWidth / 2, outerBorderWidth, borderColor,
						"solid" ); //$NON-NLS-1$
				pageGraphic.drawLine( startX, startY + borderWidth / 2
						- innerBorderWidth / 2, endX, endY + borderWidth / 2
						- innerBorderWidth / 2, innerBorderWidth, borderColor,
						"solid" ); //$NON-NLS-1$	
				break;
			case BorderInfo.RIGHT_BORDER :
				pageGraphic.drawLine( startX + borderWidth / 2
						- outerBorderWidth / 2, startY, endX + borderWidth / 2
						- outerBorderWidth / 2, endY, outerBorderWidth,
						borderColor, "solid" ); //$NON-NLS-1$
				pageGraphic.drawLine( startX - borderWidth / 2
						+ innerBorderWidth / 2, startY, endX - borderWidth / 2
						+ innerBorderWidth / 2, endY, innerBorderWidth,
						borderColor, "solid" ); //$NON-NLS-1$
				break;
			case BorderInfo.BOTTOM_BORDER :
				pageGraphic.drawLine( startX, startY + borderWidth / 2
						- outerBorderWidth / 2, endX, endY + borderWidth / 2
						- outerBorderWidth / 2, outerBorderWidth, borderColor,
						"solid" ); //$NON-NLS-1$
				pageGraphic.drawLine( startX, startY - borderWidth / 2
						+ innerBorderWidth / 2, endX, endY - borderWidth / 2
						+ innerBorderWidth / 2, innerBorderWidth, borderColor,
						"solid" ); //$NON-NLS-1$
				break;
			case BorderInfo.LEFT_BORDER :
				pageGraphic.drawLine( startX - borderWidth / 2
						+ outerBorderWidth / 2, startY, endX - borderWidth / 2
						+ outerBorderWidth / 2, endY, outerBorderWidth,
						borderColor, "solid" ); //$NON-NLS-1$
				pageGraphic.drawLine( startX + borderWidth / 2
						- innerBorderWidth / 2, startY, endX + borderWidth / 2
						- innerBorderWidth / 2, endY, innerBorderWidth,
						borderColor, "solid" ); //$NON-NLS-1$
				break;
		}
	}

	/**
	 * Draws the background image at the contentByteUnder of the pdf with the
	 * given offset
	 * 
	 * @param imageURI
	 *            the URI referring the image
	 * @param x
	 *            the start X coordinate at the PDF where the image is
	 *            positioned
	 * @param y
	 *            the start Y coordinate at the PDF where the image is
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

		if ( imageURI == null || "".equals( imageURI ) ) //$NON-NLS-1$
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
			img = Image.getInstance( new URL( imageURI ) );
			int absPosX, absPosY;
			if ( xMode )
			{
				absPosX = (int) ( ( width - img.scaledWidth( )
						* PDFConstants.LAYOUT_TO_PDF_RATIO ) * positionX );
			}
			else
			{
				absPosX = (int) positionX;
			}
			if ( yMode )
			{
				absPosY = (int) ( ( height - img.scaledHeight( )
						* PDFConstants.LAYOUT_TO_PDF_RATIO ) * positionY );
			}
			else
			{
				absPosY = (int) positionY;
			}
			pageGraphic.drawBackgroundImage( x, y, width, height, repeat,
					imageURI, absPosX, absPosY );
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

	protected void drawTableBorder( TableArea table )
	{
		TableBorder tb = new TableBorder( table.getX( ), table.getY( ) );
		traverseRows( tb, table, tb.tableX, tb.tableY );
		drawBorder( tb );
	}

	private void traverseRows( TableBorder tb, IContainerArea container,
			int offsetX, int offsetY )
	{
		for ( Iterator i = container.getChildren( ); i.hasNext( ); )
		{
			IArea area = (IArea) i.next( );
			if ( area instanceof IContainerArea )
			{
				offsetX += area.getX( );
				offsetY += area.getY( );
				if ( area instanceof RowArea )
				{
					handleBorderInRow( tb, (RowArea) area, offsetX, offsetY );
				}
				else
				{
					traverseRows( tb, (IContainerArea) area, offsetX, offsetY );
				}
				offsetX -= area.getX( );
				offsetY -= area.getY( );
			}
			else
			{
				continue;
			}
		}
	}

	private void handleBorderInRow( TableBorder tb, RowArea row, int offsetX,
			int offsetY )
	{
		for ( Iterator ri = row.getChildren( ); ri.hasNext( ); )
		{
			IArea area = (IArea) ri.next( );
			if ( !( area instanceof CellArea ) )
			{
				continue;
			}
			CellArea cell = (CellArea) area;
			BorderInfo[] borders = cacheCellBorder( cell );
			int cellX = offsetX + cell.getX( );
			int cellY = offsetY + cell.getY( );
			// the x coordinate of the cell's right boundary
			int cellRx = cellX + cell.getWidth( );
			// the y coordinate of the cell's bottom boundary
			int cellBy = cellY + cell.getHeight( );
			tb.addColumn( cellRx );
			tb.addRow( cellBy );
			if ( null != borders
					&& borders[BorderInfo.TOP_BORDER].borderWidth != 0 )
			{
				tb.setRowBorder( cellY, cellX, cellRx,
						borders[BorderInfo.TOP_BORDER].borderStyle,
						borders[BorderInfo.TOP_BORDER].borderWidth,
						borders[BorderInfo.TOP_BORDER].borderColor );
			}
			if ( null != borders
					&& borders[BorderInfo.LEFT_BORDER].borderWidth != 0 )
			{
				tb.setColumnBorder( cellX, cellY, cellBy,
						borders[BorderInfo.LEFT_BORDER].borderStyle,
						borders[BorderInfo.LEFT_BORDER].borderWidth,
						borders[BorderInfo.LEFT_BORDER].borderColor );
			}
			if ( null != borders
					&& borders[BorderInfo.BOTTOM_BORDER].borderWidth != 0 )
			{
				tb.setRowBorder( cellBy, cellX, cellRx,
						borders[BorderInfo.BOTTOM_BORDER].borderStyle,
						borders[BorderInfo.BOTTOM_BORDER].borderWidth,
						borders[BorderInfo.BOTTOM_BORDER].borderColor );
			}
			if ( null != borders
					&& borders[BorderInfo.RIGHT_BORDER].borderWidth != 0 )
			{
				tb.setColumnBorder( cellRx, cellY, cellBy,
						borders[BorderInfo.RIGHT_BORDER].borderStyle,
						borders[BorderInfo.RIGHT_BORDER].borderWidth,
						borders[BorderInfo.RIGHT_BORDER].borderColor );
			}
		}
	}

}
