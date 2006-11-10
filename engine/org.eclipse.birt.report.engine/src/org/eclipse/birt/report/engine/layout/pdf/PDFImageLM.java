/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.layout.ILineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.ImageArea;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

/**
 * 
 * This layout mananger implements formatting and locating of image content.
 * <p>
 * Image is an atomic conponent, so it can not be split. if the size exceeds the
 * boundry, user agent should overflow or clip it.
 * <p>
 * if layout manager can not retrieve the instrinsic dimension of image, layout
 * mangager set the instrinsic dimension to the default value (1,1). logger will
 * log this error, but this can not interrupt the layout process.
 * <p>
 * this layout manager genrate image area which perhaps has border, render
 * should take reponsibility to draw the image and its border
 * <p>
 * the dimension algorithm shows as following table:
 * <p>
 * <table>
 * <tr>
 * <td>scale</td>
 * <td>height</td>
 * <td>width</td>
 * <td>notes</td>
 * </tr>
 * <tr>
 * <td rowspan="3">true</td>
 * <td>X</td>
 * <td>X</td>
 * <td rowspan="3">Following the CSS defined algorithm.</td>
 * </tr>
 * <tr>
 * <td>X</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>X</td>
 * </tr>
 * <tr>
 * <td rowspan="4">false</td>
 * <td>X</td>
 * <td>X</td>
 * <td>Use the defined width and height</td>
 * </tr>
 * <tr>
 * <td>X</td>
 * <td></td>
 * <td>Use the defined height and intrinsic width</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>X</td>
 * <td>Use the intrinsic height, defined width</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td>Use the intrinsic size</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class PDFImageLM extends PDFLeafItemLM
{

	protected final static int DEFAULT_WIDHT = 212000;

	protected final static int DEFAULT_HEIGHT = 130000;

	protected IImageContent image;

	protected int maxWidth;

	protected ContainerArea root;

	public PDFImageLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		init( );

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
	protected Dimension getIntrinsicDimension( IImageContent content )
			throws BadElementException, MalformedURLException, IOException
	{
		Image image = null;
		switch ( content.getImageSource( ) )
		{
			case IImageContent.IMAGE_FILE :
				URL url = new URL(content.getURI( ));
				InputStream in = url.openStream( );
				try
				{
					byte[] buffer = new byte[in.available( )];
					in.read( buffer );
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
				image = Image.getInstance( content.getData( ) );
				break;

			case IImageContent.IMAGE_URL :
				image = Image.getInstance( new URL( content.getURI( ) ) );
				break;
			default :
				assert ( false );
		}
		if ( image != null )
		{
			Object design = content.getGenerateBy( );
			int resolution = 96;
			if ( design instanceof ExtendedItemDesign )
			{
				resolution = 192;
			}
			return new Dimension( (int) ( image.plainWidth( ) * 1000
					/ resolution * 72 ), (int) ( image.plainHeight( ) * 1000
					/ resolution * 72 ) );
		}
		return null;
	}

	protected Dimension getSpecifiedDimension( IImageContent content )
	{
		Dimension dim = new Dimension( DEFAULT_WIDHT, DEFAULT_HEIGHT );
		Dimension instrinsic = null;
		try
		{
			instrinsic = getIntrinsicDimension( content );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ) );
		}
		boolean scale = false;
		int specifiedWidth = getDimensionValue( content.getWidth( ) );
		int specifiedHeight = getDimensionValue( content.getHeight( ) );
		if ( instrinsic == null )
		{
			dim.setDimension( specifiedWidth == 0
					? DEFAULT_WIDHT
					: specifiedWidth, specifiedHeight == 0
					? DEFAULT_HEIGHT
					: specifiedHeight );
			return dim;
		}
		if ( scale )
		{
			double ratio = instrinsic.getRatio( );

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
					dim.setDimension( instrinsic.getWidth( ), instrinsic
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
					dim.setDimension( specifiedWidth, instrinsic.getHeight( ) );
				}
			}
			else
			{
				if ( specifiedHeight > 0 )
				{
					dim.setDimension( instrinsic.getWidth( ), specifiedHeight );
				}
				else
				{
					dim.setDimension( instrinsic.getWidth( ), instrinsic
							.getHeight( ) );
				}
			}
		}
		return dim;
	}

	public boolean layoutChildren( )
	{
		if ( root == null )
		{
			return false;
		}
		assert ( parent instanceof ILineStackingLayoutManager );
		ILineStackingLayoutManager lineParent = (ILineStackingLayoutManager) parent;
		// if height exceed current available value, must page break;
		if ( root.getAllocatedHeight( ) > lineParent.getCurrentMaxContentHeight() )
		{
			if ( !parent.isPageEmpty( ) )
			{
				return true;
			}
			else
			{
				parent.addArea( root, false, false );
				return false;
			}
		}
		else
		{
			if ( parent.getCurrentIP( ) + root.getAllocatedWidth( ) > maxWidth )
			{
				if ( !lineParent.isEmptyLine( ) )
				{
					boolean ret = lineParent.endLine( );
					assert ( ret );
					return layoutChildren( );
				}
				else
				{
					parent.addArea( root, false, false );
					return false;
				}
			}
			else
			{
				parent.addArea( root, false, false );
				return false;
			}
		}
	}

	protected void init( )
	{
		assert ( content instanceof IImageContent );
		image = (IImageContent) content;
		maxWidth = parent.getCurrentMaxContentWidth( );
		
		Dimension contentDimension = getSpecifiedDimension( image );
		root = (ContainerArea) createInlineContainer( image, true,
				true );
		validateBoxProperty( root.getStyle( ), maxWidth, context.getMaxHeight( ) );
		
		//set max content width
		root.setAllocatedWidth( maxWidth );
		int maxContentWidth = root.getContentWidth( );
		if(contentDimension.getWidth() > maxContentWidth)
		{
			contentDimension.setDimension(maxContentWidth, (int)(maxContentWidth/contentDimension.getRatio()));
		}
		
		ImageArea imageArea = (ImageArea)AreaFactory.createImageArea( image );
		imageArea.setWidth(contentDimension.getWidth());
		imageArea.setHeight(contentDimension.getHeight());
		root.addChild(imageArea);
		
		imageArea.setPosition( root.getContentX( ), root.getContentY( ) );
		root.setContentWidth( contentDimension.getWidth( ) );
		root.setContentHeight( contentDimension.getHeight( ) );
	}
	
}