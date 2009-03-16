/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.ObjectContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.ULocale;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public class ImageAreaLayout implements ILayout
{

	public static final int TYPE_IMAGE_OBJECT = 0;
	public static final int TYPE_FLASH_OBJECT = 1;

	private ILayout layout = null;

	private ContainerArea parent;
	private IImageContent content;
	private LayoutContext context;
	private int objectType = TYPE_IMAGE_OBJECT;

	public ImageAreaLayout( ContainerArea parent, LayoutContext context,
			IImageContent content )
	{
		this.parent = parent;
		this.content = content;
		this.context = context;
	}

	public void layout( ) throws BirtException
	{
		initialize( );
		if ( layout != null )
		{
			layout.layout( );
		}

	}

	protected void initialize( ) throws BirtException
	{
		checkObjectType( );
		// choose the layout manager
		IImageContent imageContent = (IImageContent) content;
		Image imageObject = null;
		boolean isFlash = FlashFile.isFlash( imageContent.getMIMEType( ),
				imageContent.getURI( ), imageContent.getExtension( ) );
		if ( !isFlash )
		{
			imageObject = EmitterUtil.getImage( imageContent );
		}
		if ( isOutputSupported( objectType )
				&& ( isFlash || imageObject != null ) )
		{
			// the output format can display this kind of object and the object
			// is accessible.
			layout = new ConcreteImageLayout( context, parent, content,
					imageObject );
		}
		else
		{
			// display the alt text.
			ITextContent altTextContent = createAltText( (IImageContent) content );
			if ( null == altTextContent )
			{
				return;
			}
			layout = new BlockTextArea( parent, context, altTextContent );
			( (BlockTextArea) layout ).initialize( );
		}
	}
	
	private ITextContent createAltText( IImageContent imageContent )
	{
		IReportContent report = imageContent.getReportContent( );
		if ( report == null )
		{
			return null;
		}
		ITextContent altTextContent = report
				.createTextContent( imageContent );
		String alt = imageContent.getAltText( );
		if ( null == alt )
		{
			ULocale locale = ULocale.forLocale( context.getLocale( ) );
			if ( locale == null )
			{
				locale = ULocale.getDefault( );
			}
			EngineResourceHandle resourceHandle = new EngineResourceHandle(
					locale );
			if ( objectType == TYPE_FLASH_OBJECT )
			{
				alt = resourceHandle
						.getMessage( MessageConstants.FLASH_OBJECT_NOT_SUPPORTED_PROMPT );
			}
			else
			{
				alt = resourceHandle
						.getMessage( MessageConstants.REPORT_ITEM_NOT_SUPPORTED_PROMPT );
			}
		}
		altTextContent.setText( alt );
		return altTextContent;
	}

	protected void checkObjectType( )
	{
		IImageContent image = (IImageContent) content;
		String uri = image.getURI( );
		String mimeType = image.getMIMEType( );
		String extension = image.getExtension( );
		if ( FlashFile.isFlash( mimeType, uri, extension ) )
		{
			objectType = TYPE_FLASH_OBJECT;
			ObjectContent flash = (ObjectContent) image;
		}
		else
		{
			objectType = TYPE_IMAGE_OBJECT;
		}
	}

	/**
	 * Is the target output emitter support the object type. Currently the
	 * object type can be image and flash.
	 */
	private boolean isOutputSupported( int type )
	{
		String supportedImageFormats = context.getSupportedImageFormats( );
		
		if ( type == TYPE_IMAGE_OBJECT )
		{
			if ( -1 != supportedImageFormats.indexOf( "PNG" )
					|| -1 != supportedImageFormats.indexOf( "GIF" )
					|| -1 != supportedImageFormats.indexOf( "BMP" )
					|| -1 != supportedImageFormats.indexOf( "JPG" ) )
				return true;
		}
		
		else if ( type == TYPE_FLASH_OBJECT )
		{
			if ( -1 != supportedImageFormats.indexOf( "SWF" ))
			{
				return true;				
			}
		}
		return false;		
	}
}

class ConcreteImageLayout implements ILayout
{

	protected static Logger logger = Logger
			.getLogger( ConcreteImageLayout.class.getName( ) );
	/** The DpiX */
	private int resolutionX = 0;
	/** The DpiY */
	private int resolutionY = 0;

	private Image imageObject = null;

	private ContainerArea parent;

	protected final static int DEFAULT_WIDHT = 212000;

	protected final static int DEFAULT_HEIGHT = 130000;

	protected IImageContent image;

	protected ContainerArea root;

	private Dimension intrinsic;

	private static final String BOOKMARK_PREFIX = "javascript:catchBookmark('";

	private LayoutContext context;

	private boolean fitToContainer = false;

	public ConcreteImageLayout( LayoutContext context, ContainerArea parent,
			IImageContent content, Image imageObject )
	{
		this.context = context;
		this.image = content;
		this.parent = parent;
		this.imageObject = imageObject;
		Object reportItemDesign = content.getGenerateBy( );
		if ( null != reportItemDesign )
		{
			if ( reportItemDesign instanceof ImageItemDesign )
			{
				fitToContainer = ( (ImageItemDesign) reportItemDesign )
						.isFitToContainer( );
			}
		}
	}

	/**
	 * get intrinsic dimension of image in pixels. Now only support png, bmp,
	 * jpg, gif.
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws BadElementException
	 */
	protected Dimension getIntrinsicDimension( IImageContent content,
			Image image )
	{
		if ( image != null )
		{
			// The DPI resolution of the image.
			// the preference of the DPI setting is:
			// 1. the resolution restored in content.
			// 2. the resolution in image file.
			// 3. the DPI in report designHandle.
			// 4. use the DPI in render options.
			// 5. the default DPI (96).
			int contentResolution = content.getResolution( );
			if ( contentResolution != 0 )
			{
				resolutionX = contentResolution;
				resolutionY = contentResolution;
			}
			else
			{
				resolutionX = image.getDpiX( );
				resolutionY = image.getDpiY( );
				if ( 0 == resolutionX || 0 == resolutionY )
				{
					ReportDesignHandle designHandle = content
							.getReportContent( ).getDesign( ).getReportDesign( );
					resolutionX = designHandle.getImageDPI( );
					resolutionY = designHandle.getImageDPI( );
				}
				if ( 0 == resolutionX || 0 == resolutionY )
				{
					resolutionX = context.getDpi( );
					resolutionY = context.getDpi( );
				}
				if ( 0 == resolutionX || 0 == resolutionY )
				{
					resolutionX = 96;
					resolutionY = 96;
				}
			}
			return new Dimension( (int) ( image.plainWidth( ) * 1000
					/ resolutionX * 72 ), (int) ( image.plainHeight( ) * 1000
					/ resolutionY * 72 ) );
		}
		return null;
	}

	protected int getResolution( )
	{
		int resolution = 0;
		ReportDesignHandle designHandle = image.getReportContent( ).getDesign( )
				.getReportDesign( );
		resolution = designHandle.getImageDPI( );

		if ( 0 == resolution )
		{
			resolution = context.getDpi( );
		}
		if ( 0 == resolution )
		{
			resolution = 96;
		}
		return resolution;
	}

	protected Dimension getSpecifiedDimension( IImageContent content,
			int pWidth, boolean scale )
	{
		Dimension dim = new Dimension( DEFAULT_WIDHT, DEFAULT_HEIGHT );
		try
		{
			intrinsic = getIntrinsicDimension( content, imageObject );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ) );
		}
		int dpi = getResolution( );
		int specifiedWidth = PropertyUtil.getDimensionValue( content, content
				.getWidth( ), dpi, pWidth );
		int specifiedHeight = PropertyUtil.getDimensionValue( content, content
				.getHeight( ), dpi, 0 );
		if ( intrinsic == null )
		{
			dim.setDimension( specifiedWidth == 0
					? DEFAULT_WIDHT
					: specifiedWidth, specifiedHeight == 0
					? DEFAULT_HEIGHT
					: specifiedHeight );
			return dim;
		}
		if ( scale ) // always does scale.
		{
			double ratio = intrinsic.getRatio( );

			if ( specifiedWidth > 0 )
			{
				if ( specifiedHeight > 0 )
				{
					dim.setDimension( specifiedWidth, specifiedHeight );
				}
				else
				{
					dim.setDimension( specifiedWidth,
							(int) ( specifiedWidth / ratio ) );
				}
			}
			else
			{
				if ( specifiedHeight > 0 )
				{
					dim.setDimension( (int) ( specifiedHeight * ratio ),
							specifiedHeight );
				}
				else
				{
					dim.setDimension( intrinsic.getWidth( ), intrinsic
							.getHeight( ) );
				}
			}
		}
		else
		{
			if ( specifiedWidth > 0 )
			{
				if ( specifiedHeight > 0 )
				{
					dim.setDimension( specifiedWidth, specifiedHeight );
				}
				else
				{
					dim.setDimension( specifiedWidth, intrinsic.getHeight( ) );
				}
			}
			else
			{
				if ( specifiedHeight > 0 )
				{
					dim.setDimension( intrinsic.getWidth( ), specifiedHeight );
				}
				else
				{
					dim.setDimension( intrinsic.getWidth( ), intrinsic
							.getHeight( ) );
				}
			}
		}
		return dim;
	}

	public void layout( ) throws BirtException
	{
		init( );
		// For inline image, the hierarchy is
		// LineArea->InlineContainer->ImageArea.
		// the root is InlineContainer, so we just need to add the root directly
		// to its parent(LineArea).
		// In LineAreaLM, the lineArea will enlarge itself to hold the root.
		// For block image, the hierarchy is BlockContainer->ImageArea
		if ( PropertyUtil.isInlineElement( image ) )
		{
			// inline image
			assert ( parent instanceof InlineStackingArea );
			InlineStackingArea lineParent = (InlineStackingArea) parent;
			if ( root.getAllocatedWidth( ) > parent.getCurrentMaxContentWidth( ) )
			{
				if ( !lineParent.isEmptyLine( ) )
				{
					lineParent.endLine( );
					layout( );
				}
				else
				{
					parent.add( root );
					parent.update( root );
					return;
				}
			}
			else
			{
				parent.add( root );
				parent.update( root );
				return;
			}
		}
		else
		{
			parent.add( root );
			if ( !parent.isInInlineStacking && context.isAutoPageBreak( ) )
			{
				int aHeight = root.getAllocatedHeight( );
				if ( aHeight + parent.getAbsoluteBP( ) > context.getMaxBP( ) )
				{
					parent.autoPageBreak( );
				}
			}
			parent.update( root );
		}
	}

	protected void init( ) throws BirtException
	{

		if ( PropertyUtil.isInlineElement( image ) )
		{
			root = new ImageInlineContainer( parent, context, image );
		}
		else
		{
			root = new ImageBlockContainer( parent, context, image );
		}

		root.initialize( );

		// First, the width of root is set to its parent's max available width.
		root.setAllocatedWidth( parent.getCurrentMaxContentWidth( ) );
		root.setMaxAvaWidth( root.getContentWidth( ) );
		Dimension contentDimension = getSpecifiedDimension( image, root
				.getContentWidth( ), true );
		ImageArea imageArea = createImageArea( image );
		// implement fitToContainer
		int actualHeight = contentDimension.getHeight( );
		int actualWidth = contentDimension.getWidth( );
		int maxHeight = root.getMaxAvaHeight( );
		int maxWidth = root.getMaxAvaWidth( );
		int cHeight = contentDimension.getHeight( );
		int cWidth = contentDimension.getWidth( );

		if ( cHeight > maxHeight || cWidth > maxWidth )
		{
			if ( fitToContainer )
			{

				float rh = ( (float) maxHeight ) / cHeight;
				float rw = ( (float) maxWidth ) / cWidth;
				if ( rh > rw )
				{
					actualHeight = (int) ( rw * maxHeight );
					actualWidth = maxWidth;
				}
				else
				{
					actualHeight = maxHeight;
					actualWidth = (int) ( rh * maxWidth );
				}
			}
			else
			{
				root.setNeedClip( true );
				root.setAllocatedHeight( Math.min( maxHeight, cHeight ));
				root.setAllocatedWidth( Math.min( maxWidth, cWidth ));
			}
			imageArea.setWidth( actualWidth );
			imageArea.setHeight( actualHeight );
		}
		else
		{
			imageArea.setWidth( actualWidth );
			imageArea.setHeight( actualHeight );
			root.setContentWidth( imageArea.getWidth( ) );
			root.setContentHeight( imageArea.getHeight( ) );
		}
		root.addChild( imageArea );
		imageArea.setPosition( root.getContentX( ), root.getContentY( ) );

		// Adjust the dimension of root.
		processChartLegend( image, imageArea );
		root.finished = true;
	}

	protected ImageArea createImageArea( IImageContent content )
	{
		ImageArea area = new ImageArea( );
		switch ( content.getImageSource( ) )
		{
			case IImageContent.IMAGE_FILE :
			case IImageContent.IMAGE_URL :
				area.setUrl( content.getURI( ) );
				break;
			case IImageContent.IMAGE_NAME :
			case IImageContent.IMAGE_EXPRESSION :
				area.setData( content.getData( ) );
				break;
		}
		return area;
	}

	/**
	 * Creates legend for chart.
	 * 
	 * @param imageContent
	 *            the image content of the chart.
	 * @param imageArea
	 *            the imageArea of the chart.
	 */
	private void processChartLegend( IImageContent imageContent,
			IImageArea imageArea )
	{
		if ( null == intrinsic )
		{
			return;
		}
		Object imageMapObject = imageContent.getImageMap( );
		boolean hasImageMap = ( imageMapObject != null )
				&& ( imageMapObject instanceof String )
				&& ( ( (String) imageMapObject ).length( ) > 0 );
		if ( hasImageMap )
		{
			createImageMap( (String) imageMapObject, imageArea );
		}
	}

	private void createImageMap( String imageMapObject, IImageArea imageArea )
	{
		Pattern pattern = Pattern
				.compile( "<AREA[^<>]*coords=\"([\\d,]*)\" href=\"([^<>\"]*)\" target=\"([^<>\"]*)\"/>" );
		Matcher matcher = pattern.matcher( imageMapObject );
		while ( matcher.find( ) )
		{
			try
			{
				int[] area = getArea( matcher.group( 1 ) );
				String url = matcher.group( 2 );
				String targetWindow = matcher.group( 3 );
				createImageMap( area, imageArea, url, targetWindow );
			}
			catch ( NumberFormatException e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
			}
		}
	}

	private void createImageMap( int[] area, IImageArea imageArea, String url,
			String targetWindow )
	{
		if ( url == null )
		{
			return;
		}
		ActionContent link = new ActionContent( );
		if ( isBookmark( url ) )
		{
			String bookmark = getBookmark( url );
			link.setBookmark( bookmark );
		}
		else
		{
			link.setHyperlink( url, targetWindow );
		}
		area = getAbsoluteArea( area, imageArea );
		createImageMapContainer( area[0], area[1], area[2], area[3], link );
	}

	/**
	 * Creates an image map container, which is an empty container with an hyper
	 * link.
	 * 
	 * @param x
	 *            x coordinate of lower left corner of the container.
	 * @param y
	 *            y coordinate of lower left corner of the container.
	 * @param width
	 *            width of the container.
	 * @param height
	 *            height of the container.
	 * @param link
	 *            destination of the hyperlink.
	 */
	private void createImageMapContainer( int x, int y, int width, int height,
			IHyperlinkAction link )
	{
		ReportContent reportContent = (ReportContent) image.getReportContent( );
		// IContainerContent mapContent = reportContent.createContainerContent(
		// );
		// mapContent.setHyperlinkAction( link );
		BlockContainerArea area = new BlockContainerArea( );
		area.setAction( link );
		area.setPosition( x, y );
		area.setWidth( width );
		area.setHeight( height );
		root.addChild( area );
	}

	/**
	 * Calculates the absolute positions of image map when given the position of
	 * image. The image map position is relative to the left up corner of the
	 * image.
	 * 
	 * The argument and returned value are both 4 length integer area, the four
	 * value of which are x, y of up left corner, width and height respectively.
	 * 
	 * @param area
	 *            rectangle area of a image map.
	 * @param imageArea
	 *            image area of the image in which the image map is.
	 * @return absolute position of the image map.
	 */
	private int[] getAbsoluteArea( int[] area, IImageArea imageArea )
	{
		assert ( intrinsic != null );
		for ( int i = 0; i < 4; )
		{
			area[i] = getTranslatedLengthX( area[i] );
			i++;
			area[i] = getTranslatedLengthY( area[i] );
			i++;
		}
		int[] result = new int[4];
		int imageX = imageArea.getX( );
		int imageY = imageArea.getY( );
		int imageHeight = imageArea.getHeight( );
		int imageWidth = imageArea.getWidth( );
		int intrinsicWidth = intrinsic.getWidth( );
		int intrinsicHeight = intrinsic.getHeight( );
		float ratio = (float) imageWidth / (float) intrinsicWidth;
		result[0] = imageX + (int) ( area[0] * ratio );
		result[2] = (int) ( area[2] * ratio );
		ratio = (float) imageHeight / (float) intrinsicHeight;
		result[1] = imageY + (int) ( area[1] * ratio );
		result[3] = (int) ( area[3] * ratio );
		return result;
	}

	private int getTranslatedLengthX( int length )
	{
		return length * 1000 / resolutionX * 72;
	}

	private int getTranslatedLengthY( int length )
	{
		return length * 1000 / resolutionY * 72;
	}

	/**
	 * Check if a url is of an internal bookmark.
	 * 
	 * @param url
	 *            the url string.
	 * @return true if and only if the url is of an internal bookmark.
	 */
	private boolean isBookmark( String url )
	{
		return url.startsWith( BOOKMARK_PREFIX ) && url.endsWith( "')" );
	}

	/**
	 * Parses out bookmark name from a url for interanl bookmark.
	 * 
	 * @param url
	 *            the url string
	 * @return the bookmark name.
	 */
	private String getBookmark( String url )
	{
		int start = url.indexOf( BOOKMARK_PREFIX ) + BOOKMARK_PREFIX.length( );
		int end = url.length( ) - 2;
		return url.substring( start, end );
	}

	/**
	 * Parse the image map position from a string which is of format "x1, y1,
	 * x2, y2".
	 * 
	 * @param string
	 *            the position string.
	 * @return a array which contains the x, y coordinate of left up corner,
	 *         width and height in sequence.
	 * 
	 */
	private int[] getArea( String string )
	{
		String[] rawDatas = string.split( "," );
		int[] area = new int[4];
		area[0] = Integer.parseInt( rawDatas[0] );
		area[1] = Integer.parseInt( rawDatas[1] );
		area[2] = Integer.parseInt( rawDatas[4] ) - area[0];
		area[3] = Integer.parseInt( rawDatas[5] ) - area[1];
		return area;
	}

	protected void close( )
	{
		// if ( !PropertyUtil.isInlineElement( image ) )
		// We align inline elements (here - inline container parenting the
		// inline image) in LineLayout, but not block-level image.
		// Invoke it here, since it should not be done by ContainerLayout
		// always.
		// TODO: Check if this can be done in a neater way.
		// parent.align( root );
	}

}
