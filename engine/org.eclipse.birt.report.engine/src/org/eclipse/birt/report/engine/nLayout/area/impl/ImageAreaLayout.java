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
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.ObjectContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.engine.util.SvgFile;

import com.ibm.icu.util.ULocale;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public class ImageAreaLayout implements ILayout
{

	public static final int TYPE_IMAGE_OBJECT = 0;
	public static final int TYPE_FLASH_OBJECT = 1;
	public static final int TYPE_SVG_OBJECT = 2;
	private static final int RESOURCE_UNREACHABLE = 0;
	private static final int UNSUPPORTED_OBJECTS = 1;

	public int objectType = TYPE_IMAGE_OBJECT;
	private ILayout layout = null;
	private ContainerArea parent;
	private IImageContent content;
	private LayoutContext context;

	protected static Logger logger = Logger.getLogger( ImageAreaLayout.class
			.getName( ) );

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

		if ( isOutputSupported( objectType ) )
		{
			if ( objectType == TYPE_IMAGE_OBJECT )
			{
				imageObject = EmitterUtil.getImage( imageContent );
			}
			if ( imageObject != null && objectType == TYPE_IMAGE_OBJECT
					|| objectType == TYPE_FLASH_OBJECT
					|| objectType == TYPE_SVG_OBJECT )
			{
				// the output format can display this kind of object and the
				// object is accessible.
				layout = new ConcreteImageLayout( context, parent, content,
						imageObject );
			}
			else
			{
				// display the alt text or prompt object not accessible.
				layout = createAltTextLayout( RESOURCE_UNREACHABLE );
			}
		}
		else
		{
			if ( objectType == TYPE_SVG_OBJECT )
			{
				// convert to a simple image, like PNG.
				imageObject = EmitterUtil.getImage( imageContent );
				if ( imageObject != null )
				{
					layout = new ConcreteImageLayout( context, parent, content,
							imageObject );
					return;
				}
			}
			// display the alt text or prompt unsupported objects.
			layout = createAltTextLayout( UNSUPPORTED_OBJECTS );
		}
	}

	private ILayout createAltTextLayout( int altTextType )
	{
		ITextContent altTextContent = createAltText( (IImageContent) content,
				altTextType );
		if ( null == altTextContent )
		{
			return null;
		}
		return new BlockTextArea( parent, context, altTextContent );
	}

	private ITextContent createAltText( IImageContent imageContent,
			int altTextType )
	{
		IReportContent report = imageContent.getReportContent( );
		if ( report == null )
		{
			return null;
		}
		ITextContent altTextContent = report.createTextContent( imageContent );
		altTextContent.setParent( imageContent.getParent( ) );
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
			if ( altTextType == UNSUPPORTED_OBJECTS )
			{
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
			if ( altTextType == RESOURCE_UNREACHABLE )
			{
				alt = resourceHandle
						.getMessage( MessageConstants.RESOURCE_UNREACHABLE_PROMPT );
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
		}
		else if ( SvgFile.isSvg( mimeType, uri, extension ) )
		{
			objectType = TYPE_SVG_OBJECT;
		}
		else
		{
			objectType = TYPE_IMAGE_OBJECT;
		}
	}

	/**
	 * Check if the target output emitter supports the object type.
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
			if ( -1 != supportedImageFormats.indexOf( "SWF" ) )
			{
				return true;
			}
		}
		else if ( type == TYPE_SVG_OBJECT )
		{
			if ( -1 != supportedImageFormats.indexOf( "SVG" ) )
			{
				return true;
			}
		}
		return false;
	}

	class ConcreteImageLayout implements ILayout
	{

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

		private BlockTextArea innerText = null;

		public ConcreteImageLayout( LayoutContext context,
				ContainerArea parent, IImageContent content, Image imageObject )
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
		 * get intrinsic dimension of image in pixels. Now only support png,
		 * bmp, jpg, gif.
		 * 
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
				return new Dimension( (int) ( image.plainWidth( ) * 1000
						/ resolutionX * 72 ), (int) ( image.plainHeight( )
						* 1000 / resolutionY * 72 ) );
			}
			return null;
		}

		protected Dimension getSpecifiedDimension( IImageContent content,
				int pWidth, boolean scale )
		{
			// prepare the DPI for the image.
			int imageFileDpiX = 0;
			int imageFileDpiY = 0;
			if ( imageObject != null )
			{
				imageFileDpiX = imageObject.getDpiX( );
				imageFileDpiY = imageObject.getDpiY( );
			}
			resolutionX = PropertyUtil.getImageDpi( content, imageFileDpiX,
					context.getDpi( ) );
			resolutionY = PropertyUtil.getImageDpi( content, imageFileDpiY,
					context.getDpi( ) );

			try
			{
				intrinsic = getIntrinsicDimension( content, imageObject );
			}
			catch ( Exception e )
			{
				logger.log( Level.SEVERE, e.getLocalizedMessage( ) );
			}
			int specifiedWidth = PropertyUtil.getDimensionValue( content,
					content.getWidth( ), resolutionX, pWidth );
			int specifiedHeight = PropertyUtil.getDimensionValue( content,
					content.getHeight( ), resolutionY, 0 );

			Dimension dim = new Dimension( DEFAULT_WIDHT, DEFAULT_HEIGHT );
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
						dim
								.setDimension( specifiedWidth, intrinsic
										.getHeight( ) );
					}
				}
				else
				{
					if ( specifiedHeight > 0 )
					{
						dim.setDimension( intrinsic.getWidth( ),
								specifiedHeight );
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

			boolean isEmptyLine = true;
			boolean innerTextInserted = false;
			if ( "pdf".equalsIgnoreCase( context.getFormat( ) )
					&& objectType == TYPE_FLASH_OBJECT )
			{
				innerTextInserted = true;
				innerText = createInnerTextLayout( );
				innerText.content.getStyle( ).setProperty(
						IStyle.STYLE_TEXT_ALIGN, IStyle.CENTER_VALUE );
				innerText.setVerticalAlign( IStyle.MIDDLE_VALUE );
				// save current root status
				if ( PropertyUtil.isInlineElement( image ) )
				{
					// inline image
					InlineStackingArea lineParent = (InlineStackingArea) parent;
					isEmptyLine = lineParent.isEmptyLine( );
				}
				int lastIP = root.currentIP;
				int lastBP = root.currentBP;

				innerText.layout( );
				// set the text position manually.
				innerText.setAllocatedPosition( 0, 0 );

				// restore the root status.
				root.currentIP = lastIP;
				root.currentBP = lastBP;
			}

			// For inline image, the hierarchy is
			// LineArea->InlineContainer->ImageArea.
			// the root is InlineContainer, so we just need to add the root
			// directly
			// to its parent(LineArea).
			// In LineAreaLM, the lineArea will enlarge itself to hold the root.
			// For block image, the hierarchy is BlockContainer->ImageArea
			if ( PropertyUtil.isInlineElement( image ) )
			{
				// inline image
				assert ( parent instanceof InlineStackingArea );
				InlineStackingArea lineParent = (InlineStackingArea) parent;
				if ( root.getAllocatedWidth( ) > parent
						.getCurrentMaxContentWidth( ) )
				{
					if ( ( innerTextInserted && !isEmptyLine )
							|| ( !innerTextInserted && !lineParent
									.isEmptyLine( ) ) )
					{
						lineParent.endLine( false );
						layout( );
					}
					else
					{
						parent.add( root );
						root.finished = true;
						parent.update( root );
						return;
					}
				}
				else
				{
					parent.add( root );
					root.finished = true;
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
				root.finished = true;
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

			// First, the width of root is set to its parent's max available
			// width.
			root.setAllocatedWidth( parent.getMaxAvaWidth( ) );
			root.setMaxAvaWidth( root.getContentWidth( ) );
			Dimension contentDimension = getSpecifiedDimension( image, root
					.getContentWidth( ), true );
			ImageArea imageArea = createImageArea( image );
			imageArea.setParent( root );
			// implement fitToContainer
			// the maxHeight is the image's max possible height in an empty
			// page.
			int maxHeight = root.getMaxAvaHeight( );
			int maxWidth = root.getMaxAvaWidth( );
			int cHeight = contentDimension.getHeight( );
			int cWidth = contentDimension.getWidth( );

			int actualHeight = cHeight;
			int actualWidth = cWidth;

			if ( cHeight > maxHeight || cWidth > maxWidth )
			{
				if ( fitToContainer )
				{
					float rh = ( (float) maxHeight ) / cHeight;
					float rw = ( (float) maxWidth ) / cWidth;
					if ( rh > rw )
					{
						actualHeight = (int) ( (float) cHeight * maxWidth / cWidth );
						actualWidth = maxWidth;
					}
					else
					{
						actualHeight = maxHeight;
						actualWidth = (int) ( (float) cWidth * maxHeight / cHeight );
					}
					imageArea.setWidth( actualWidth );
					imageArea.setHeight( actualHeight );
					root.setContentWidth( imageArea.getWidth( ) );
					root.setContentHeight( imageArea.getHeight( ) );
				}
				else
				{
					// Fix Bugzilla – Bug 268921 [Automation][Regression]Fit to
					// page does not work in PDF
					if ( context.getPageOverflow( ) == IPDFRenderOption.FIT_TO_PAGE_SIZE
							|| context.getPageOverflow( ) == IPDFRenderOption.ENLARGE_PAGE_SIZE )
					{
						imageArea.setWidth( actualWidth );
						imageArea.setHeight( actualHeight );
						root.setContentHeight( actualHeight );
						root.setContentWidth( actualWidth );
					}
					else
					{
						imageArea.setWidth( actualWidth );
						imageArea.setHeight( actualHeight );
						root.setNeedClip( true );
						root
								.setAllocatedHeight( Math.min( maxHeight,
										cHeight ) );
						root.setAllocatedWidth( Math.min( maxWidth, cWidth ) );
					}
				}
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

			if ( context.getEngineTaskType( ) != IEngineTask.TASK_RUN )
			{
				processChartLegend( image, imageArea );
			}
			root.finished = false;
		}

		protected ImageArea createImageArea( IImageContent content )
		{
			ImageArea area = new ImageArea( );
			String mimeType = content.getMIMEType( );
			String extension = content.getExtension( );
			area.setExtension( extension );
			area.setMIMEType( mimeType );

			switch ( content.getImageSource( ) )
			{
				case IImageContent.IMAGE_FILE :
				case IImageContent.IMAGE_URL :
					area.setUrl( content.getURI( ) );
					break;
				case IImageContent.IMAGE_NAME :
					area.setUrl( "NamedImage_" + content.getURI( ) );
					// area.setData( content.getData( ) );
					break;
				case IImageContent.IMAGE_EXPRESSION :
					// area.setData( content.getData( ) );
					break;
			}

			if ( SvgFile.isSvg( mimeType, null, extension )
					&& imageObject != null )
			{
				area.setData( imageObject.rawData( ) );
			}
			else
			{
				area.setData( content.getData( ) );
			}

			if ( content instanceof ObjectContent )
			{
				ObjectContent object = (ObjectContent) content;
				area.setParameters( object.getParamters( ) );
			}
			area.setAction( content.getHyperlinkAction( ) );
			return area;
		}

		private BlockTextArea createInnerTextLayout( )
		{
			IReportContent report = image.getReportContent( );
			if ( report == null )
			{
				return null;
			}
			ITextContent promptTextContent = report.createTextContent( image );
			ULocale locale = ULocale.forLocale( context.getLocale( ) );
			if ( locale == null )
			{
				locale = ULocale.getDefault( );
			}
			EngineResourceHandle resourceHandle = new EngineResourceHandle(
					locale );

			String prompt = resourceHandle
					.getMessage( MessageConstants.UPDATE_USER_AGENT_PROMPT );

			promptTextContent.setText( prompt );
			return new BlockTextArea( root, context, promptTextContent );
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
			if ( imageMapObject == null )
			{
				return;
			}
			String[] maps = imageMapObject.split( "/>" );
			Pattern pattern = Pattern.compile( " ([^=]*)=\"([^\"]*)\"" );
			for ( String map : maps )
			{
				map = map.trim( );
				if ( map.length( ) == 0 )
				{
					continue;
				}
				Map<String, String> attributes = new TreeMap<String, String>( );
				Matcher matcher = pattern.matcher( map );
				while ( matcher.find( ) )
				{
					attributes.put( matcher.group( 1 ), matcher.group( 2 ) );
				}
				try
				{
					if ( attributes.size( ) > 0 )
					{
						int[] vertices = getVertices( attributes.get( "coords" ) );
						if( vertices == null )
						{
							return;
						}
						String url = attributes.get( "href" );
						String targetWindow = attributes.get( "target" );
						createImageMap( vertices, imageArea, url, targetWindow );
					}
				}
				catch ( NumberFormatException e )
				{
					logger.log( Level.WARNING, e.getMessage( ), e );
				}
			}
		}
		
		
		/**
		 * Parse the image map position from a string which is of format "X1, Y1,
		 * X2, Y2, ... Xn, Yn".
		 * 
		 * @param string
		 *            the position string.
		 * @return a array which contains the sequence of x, y coordinates.
		 * 
		 */
		private int[] getVertices( String string )
		{
			String[] rawDatas = string.split( "," );
			if ( rawDatas.length % 2 == 0 )
			{
				int[] area = new int[rawDatas.length];
				for ( int i = 0; i < rawDatas.length; i++ )
				{
					area[i] = Integer.parseInt( rawDatas[i] );
				}
				return area;
			}
			return null;
		}

		private void createImageMap( int[] vertices, IImageArea imageArea,
				String url, String targetWindow )
		{
			if ( url == null )
			{
				return;
			}
			url = url.replaceAll( "&amp;", "&" );
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
			createImageMapContainer( vertices, link, imageArea );
		}

		/**
		 * Creates an image map container, which is an empty container with an hyper
		 * link.
		 * 
		 * @param vertices
		 *            the sequence of the peak point coordinate.
		 * @param link
		 *            destination of the hyperlink.
		 * @param imageArea
		 *            the image area.
		 */
		private void createImageMapContainer( int[] vertices, IHyperlinkAction link,
				IImageArea imageArea )
		{
			vertices = getAbsoluteVertices( vertices, imageArea );
			if ( "pdf".equalsIgnoreCase( context.getFormat( ) ) )
			{
				imageArea.addImageMap( vertices, link );
			}
			else
			{
				BlockContainerArea area = new BlockContainerArea( );
				area.setAction( link );
				area.setPosition( vertices[0], vertices[1] );
				area.setWidth( vertices[4] - vertices[0] );
				area.setHeight( vertices[5] - vertices[1] );
				root.addChild( area );
			}
		}
		
		/**
		 * Calculates the absolute positions of image map when given the position of
		 * image. The image map position is relative to the left up corner of the
		 * image.
		 * 
		 * The argument and returned value are both 4 length integer area, the four
		 * value of which are x, y of up left corner, width and height respectively.
		 * 
		 * @param vertex
		 *            the vertex coordinates.
		 * @param imageArea
		 *            image area of the image in which the image map is.
		 * @return absolute position of the image map.
		 */
		private int[] getAbsoluteVertices( int[] vertex, IImageArea imageArea )
		{
			assert ( intrinsic != null );
			int[] result = null;

			int imageHeight = imageArea.getHeight( );
			int imageWidth = imageArea.getWidth( );
			int intrinsicWidth = intrinsic.getWidth( );
			int intrinsicHeight = intrinsic.getHeight( );
			float ratioX = (float) imageWidth / (float) intrinsicWidth;
			float ratioY = (float) imageHeight / (float) intrinsicHeight;
			int imageX = imageArea.getX( );
			int imageY = imageArea.getY( );
			
			if ( "pdf".equalsIgnoreCase( context.getFormat( ) ) )
			{
				result = new int[vertex.length];
				for ( int i = 0; i < vertex.length; )
				{
					result[i] = imageX + (int) ( getTranslatedLengthX(vertex[i]) * ratioX );
					i++;
					result[i] = imageY + (int) ( getTranslatedLengthY(vertex[i]) * ratioY );
					i++;
				}
			}
			else
			{
				for ( int i = 0; i < 4; )
				{
					vertex[i] = getTranslatedLengthX( vertex[i] );
					i++;
					vertex[i] = getTranslatedLengthY( vertex[i] );
					i++;
				}
				result = new int[4];	
				result[0] = imageX + (int) ( vertex[0] * ratioX );
				result[1] = imageY + (int) ( vertex[1] * ratioY );
				result[2] = (int) ( vertex[2] * ratioX );
				result[3] = (int) ( vertex[3] * ratioY );
			}
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
			int start = url.indexOf( BOOKMARK_PREFIX )
					+ BOOKMARK_PREFIX.length( );
			int end = url.length( ) - 2;
			return url.substring( start, end );
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
}
