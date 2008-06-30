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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.BlockContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.ImageArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public class ImageLayout extends Layout
{

	public ImageLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
	}

	protected final static int DEFAULT_WIDHT = 212000;

	protected final static int DEFAULT_HEIGHT = 130000;

	protected IImageContent image;

	protected ContainerArea root;

	private Dimension intrinsic;

	private static final String BOOKMARK_PREFIX = "javascript:catchBookmark('";

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
	protected Dimension getIntrinsicDimension( IImageContent content )
			throws BadElementException, MalformedURLException, IOException
	{
		Image image = null;
		String uri = content.getURI( );
		String mimeType = content.getMIMEType( );
		String extension = content.getExtension( );
		if (FlashFile.isFlash(mimeType, uri, extension))
		{
			return null;
		}
		switch ( content.getImageSource( ) )
		{
			case IImageContent.IMAGE_FILE :
				ReportDesignHandle design = content.getReportContent( )
						.getDesign( ).getReportDesign( );
				URL url = design.findResource( uri, IResourceLocator.IMAGE );
				InputStream in = url.openStream( );
				try
				{
					byte[] buffer;
					if ( SvgFile.isSvg( content.getURI( ) ) )
					{
						buffer = SvgFile.transSvgToArray( in );
					}
					else
					{
						ArrayList<Byte> bytes = new ArrayList<Byte>( );
						int data = in.read( );
						while ( data != -1 )
						{
							bytes.add( (byte) data );
							data = in.read( );
						}
						buffer = new byte[bytes.size( )];
						for ( int i = 0; i < buffer.length; i++ )
						{
							buffer[i] = bytes.get( i );
						}
					}
					image = Image.getInstance( buffer );
				}
				catch ( Exception ex )
				{
					logger.log( Level.WARNING, ex.getMessage( ), ex );
				}
				finally
				{
					in.close( );
				}
				break;
			case IImageContent.IMAGE_NAME :
			case IImageContent.IMAGE_EXPRESSION :
				byte[] data = content.getData( );
				in = new ByteArrayInputStream( data );
				if ( SvgFile.isSvg( mimeType, uri, extension ) )
					data = SvgFile.transSvgToArray( in );
				image = Image.getInstance( data );
				break;

			case IImageContent.IMAGE_URL :
				if ( SvgFile.isSvg( uri ) )
				{
					image = Image.getInstance( SvgFile.transSvgToArray( uri ) );
				}
				else
				{
					image = Image.getInstance( new URL( content.getURI( ) ) );
				}

				break;
			default :
				assert ( false );
		}
		if ( image != null )
		{
			// The DPI resolution of the image.
			// the preference of the DPI setting is: 
			// 1. the resolution restored in content.
			// 2. the resolution in image file, if the image is set to "auto" dpi from designer.
			// 3. use the DPI in render options.
			// 4. the default DPI (96).
			int resolutionX = 0;
			int resolutionY = 0;
			int contentResolution = content.getResolution( );
			if ( contentResolution != 0 )
			{
				resolutionX = contentResolution;
				resolutionY = contentResolution;
			}
			else
			{
				
//				if ( content.isAutoDPI() )
// 				the image is set to auto dpi from designer.
//				{
//					resolutionX = image.getDpiX( );
//					resolutionY = image.getDpiY( );	
//				}
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

	protected Dimension getSpecifiedDimension( IImageContent content,
			int pWidth, boolean scale )
	{
		Dimension dim = new Dimension( DEFAULT_WIDHT, DEFAULT_HEIGHT );
		try
		{
			intrinsic = getIntrinsicDimension( content );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ) );
		}
		int specifiedWidth = getDimensionValue( content.getWidth( ), pWidth );
		int specifiedHeight = getDimensionValue( content.getHeight( ) );
		if ( intrinsic == null )
		{
			dim.setDimension( specifiedWidth == 0
					? DEFAULT_WIDHT
					: specifiedWidth, specifiedHeight == 0
					? DEFAULT_HEIGHT
					: specifiedHeight );
			return dim;
		}
		if ( scale )  // always does scale.
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

	public void layout( )
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
					boolean ret = lineParent.endLine( );
					layout( );
				}
			}
			else
			{
				parent.addToRoot(root, 0);
				return;
			}
		}
		else
		{
			boolean succeed = parent.addArea( root, 0 );
			if(succeed)
			{
				return;
			}
			else
			{
				if(!parent.isPageEmpty())
				{
					parent.autoPageBreak();
				}
				parent.addToRoot(root, parent.contextList.size()-1);
				if(parent.isInBlockStacking)
				{
					parent.flushFinishedPage();
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
		for ( int i = 0; i < 4; i++ )
		{
			area[i] = getTranslatedLength( area[i] );
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

	private int getTranslatedLength( int length )
	{
		return length * 1000 / 192 * 72;
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
		// TODO Auto-generated method stub
		
	}

	protected void initialize( )
	{
		// TODO Auto-generated method stub
		
	}

}
