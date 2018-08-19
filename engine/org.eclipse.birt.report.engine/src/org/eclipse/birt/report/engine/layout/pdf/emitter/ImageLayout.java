/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.ObjectContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.BlockContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.ImageArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.util.FlashFile;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

public class ImageLayout extends Layout
{

	public static final int TYPE_IMAGE_OBJECT = 0;
	public static final int TYPE_FLASH_OBJECT = 1;

	private Layout layout = null;
	private ContainerLayout parentLayout = null;

	private int objectType = TYPE_IMAGE_OBJECT;
	private boolean withFlashVars = false;

	private static HashMap<Integer, ArrayList<String>> unsupportedFormats = new HashMap<Integer, ArrayList<String>>( );
	
	static
	{
		ArrayList<String> flashUnsupportedFormatList = new ArrayList<String>( );
		flashUnsupportedFormatList.add( "postscript" );
		flashUnsupportedFormatList.add( "ppt" );
		unsupportedFormats.put( TYPE_FLASH_OBJECT, flashUnsupportedFormatList );
	};

	public ImageLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
		parentLayout = parentContext;
	}

	public void layout( ) throws BirtException
	{
		if ( layout != null )
		{
			layout.layout( );
		}
	}

	protected void closeLayout( ) throws BirtException
	{
		if ( layout != null )
		{
			layout.closeLayout( );
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
			layout = new ConcreteImageLayout( context, parentLayout, content,
					imageObject );
		}
		else
		{
			// display the alt text.
			IReportContent report = context.getReport( );
			if ( report == null )
			{
				return;
			}
			ITextContent altTextContent = report
					.createTextContent( imageContent );
			altTextContent.setText( imageContent.getAltText( ) );
			layout = new BlockTextLayout( context, parentLayout, altTextContent );
			layout.initialize( );
		}
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
			if ( null != flash.getParamValueByName( "flashvars" ) )
			{
				withFlashVars = true;
			}
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
		if ( withFlashVars )
		{
			return false;
		}
		ArrayList<String> formats = unsupportedFormats.get( type );
		if ( formats != null
				&& formats.contains( context.getFormat( ).toLowerCase( ) ) )
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}

class ConcreteImageLayout extends Layout
{

	/** The DpiX */
	private int resolutionX = 0;
	/** The DpiY */
	private int resolutionY = 0;

	private boolean fitToContainer = false;

	private Image imageObject = null;
	
	protected final static int DEFAULT_WIDHT = 212000;

	protected final static int DEFAULT_HEIGHT = 130000;

	protected IImageContent image;

	protected ContainerArea root;

	private Dimension intrinsic;

	private static final String BOOKMARK_PREFIX = "javascript:catchBookmark('";
	
	public ConcreteImageLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content, Image imageObject )
	{
		super( context, parentContext, content );
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
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws BadElementException
	 */
	protected Dimension getIntrinsicDimension( IImageContent content,
			Image image ) throws BadElementException, MalformedURLException,
			IOException
	{
		if ( image != null )
		{
			// The DPI resolution of the image.
			// the preference of the DPI setting is:
			// 1. the resolution restored in content.
			// 2. the resolution in image file.
			// 3. use the DPI in render options.
			// 4. the DPI in report designHandle.
			// 5. the JRE screen resolution.
			// 6. the default DPI (96).
			int contentResolution = content.getResolution( );
			if ( contentResolution != 0 )
			{
				resolutionX = contentResolution;
				resolutionY = contentResolution;
			}
			else
			{
				resolutionX = PropertyUtil.getImageDpi( content, image
						.getDpiX( ), context.getDpi( ) );
				resolutionY = PropertyUtil.getImageDpi( content, image
						.getDpiY( ), context.getDpi( ) );
			}
			return new Dimension( (int) ( image.getPlainWidth( ) * 1000
					/ resolutionX * 72 ), (int) ( image.getPlainHeight( ) * 1000
					/ resolutionY * 72 ) );
		}
		return null;
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
		int specifiedWidth = getDimensionValue( content.getWidth( ), resolutionX, pWidth );
		int specifiedHeight = getDimensionValue( content.getHeight( ), resolutionY, 0 );
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
			assert ( parent instanceof IInlineStackingLayout );
			IInlineStackingLayout lineParent = (IInlineStackingLayout) parent;
			if ( root.getAllocatedWidth( ) > parent.getCurrentMaxContentWidth( ) )
			{
				if ( !lineParent.isEmptyLine( ) )
				{
					lineParent.endLine( );
					layout( );
				}
				else
				{
					scale( );
					parent.addToRoot( root, 0 );
					return;
				}
			}
			else
			{
				scale( );
				parent.addToRoot( root, 0 );
				return;
			}
		}
		else
		{
			if ( parent.isPageEmpty( ) )
			{
				scale( );
			}
			boolean succeed = parent.addArea( root, 0 );
			if ( succeed )
			{
				return;
			}
			else
			{
				if ( !parent.isPageEmpty( ) )
				{
					parent.autoPageBreak( );
				}
				scale( );
				parent.addToRoot( root, parent.contextList.size( ) - 1 );
				if ( parent.isInBlockStacking )
				{
					if ( parent.contextList.size( ) > 1 )
					{
						parent.closeExcludingLast( );
					}
				}
			}
		}
	}

	private void scale( )
	{
		if ( !fitToContainer )
		{
			return;
		}
		if ( root.getAllocatedWidth( ) > parent.getCurrentMaxContentWidth( )
				|| root.getAllocatedHeight( ) > parent
						.getCurrentMaxContentHeight( ) )
		{
			int maxWidth = parent.getCurrentMaxContentWidth( );
			int maxHeight = parent.getCurrentMaxContentHeight( );
			double ratio = (double) maxWidth / (double) maxHeight;
			if ( ratio < intrinsic.getRatio( ) )
			{
				// use parent width
				root.setContentWidth( maxWidth );
				root.setContentHeight( (int) ( maxWidth / intrinsic.getRatio( ) ) );
				if ( root.getChildren( ).hasNext( ) )
				{
					// this root should only has one child.
					ImageArea image = (ImageArea) root.getChildren( ).next( );
					image.setAllocatedWidth( maxWidth );
					image.setAllocatedHeight( (int) ( maxWidth / intrinsic.getRatio( ) ) );
				}
			}
			else
			{
				// use parent height
				root.setContentHeight( maxHeight );
				root.setContentWidth( (int) ( maxHeight * intrinsic.getRatio( ) ) );
				if ( root.getChildren( ).hasNext( ) )
				{
					// this root should only has one child.
					ImageArea image = (ImageArea) root.getChildren( ).next( );
					image.setAllocatedHeight( maxHeight );
					image.setAllocatedWidth( (int) ( maxHeight * intrinsic.getRatio( ) ) );
				}
			}
		}
	}

	protected void init( )
	{
		assert ( content instanceof IImageContent );
		image = (IImageContent) content;

		if ( PropertyUtil.isInlineElement( image ) )
		{
			root = (ContainerArea) AreaFactory.createInlineContainer( image,
					true, true );
		}
		else
		{
			root = (ContainerArea) AreaFactory.createBlockContainer( image );
		}

		// First, the width of root is set to its parent's max available width.
		root.setAllocatedWidth( parent.getCurrentMaxContentWidth( ) );

		Dimension contentDimension = getSpecifiedDimension( image, root
				.getContentWidth( ), true );
		ImageArea imageArea = (ImageArea) AreaFactory.createImageArea( image );
		imageArea.setWidth( contentDimension.getWidth( ) );
		imageArea.setHeight( contentDimension.getHeight( ) );

		root.addChild( imageArea );
		imageArea.setPosition( root.getContentX( ), root.getContentY( ) );

		// Adjust the dimension of root.
		root.setContentWidth( imageArea.getWidth( ) );
		root.setContentHeight( imageArea.getHeight( ) );
		processChartLegend( image, imageArea );
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
		IContainerContent mapContent = reportContent.createContainerContent( );
		mapContent.setHyperlinkAction( link );
		BlockContainerArea area = (BlockContainerArea) AreaFactory
				.createBlockContainer( mapContent );
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

	protected void closeLayout( )
	{
		if ( !PropertyUtil.isInlineElement( image ) )
			// We align inline elements (here - inline container parenting the
			// inline image) in LineLayout, but not block-level image.
			// Invoke it here, since it should not be done by ContainerLayout
			// always.
			// TODO: Check if this can be done in a neater way.
			parent.align( root );
	}

	protected void initialize( )
	{
		// TODO Auto-generated method stub

	}

}
