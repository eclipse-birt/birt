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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
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
			IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
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
				image = Image.getInstance( content.getURI( ) );
				break;
			case IImageContent.IMAGE_NAME :
			case IImageContent.IMAGE_EXPRESSION :
				image = Image.getInstance( content.getData( ) );
				break;

			case IImageContent.IMAGE_URI :
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
		if ( root.getAllocatedHeight( ) > lineParent.getMaxAvaHeight( ) )
		{
			if ( !parent.isPageEmpty( ) )
			{
				return true;
			}
			else
			{
				parent.addArea( root );
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
					parent.addArea( root );
					return false;
				}
			}
			else
			{
				parent.addArea( root );
				return false;
			}
		}
	}

	protected void init( )
	{
		assert ( content instanceof IImageContent );
		image = (IImageContent) content;
		maxWidth = parent.getMaxAvaWidth( );
		
		Dimension content = getSpecifiedDimension( image );
		IStyle style = image.getComputedStyle( );
		root = (ContainerArea) AreaFactory.createInlineContainer( image, true,
				true );
		int marginWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );
		int borderWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
		int paddingWidth = getDimensionValue( style
				.getProperty( StyleConstants.STYLE_PADDING_LEFT ) )
				+ getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_RIGHT ) );
		
		IStyle areaStyle = root.getStyle( );
		
		if(marginWidth>maxWidth)
		{
			//remove margin
			areaStyle.setMarginLeft("0"); //$NON-NLS-1$
			areaStyle.setMarginRight("0"); //$NON-NLS-1$
			marginWidth = 0;
		}
		int maxContentWidthWithBorder = maxWidth - marginWidth;
		if(borderWidth > maxContentWidthWithBorder)
		{
			//remove border
			areaStyle.setProperty(IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.NUMBER_0);
			areaStyle.setProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.NUMBER_0);
			borderWidth = 0;
		}
		int maxContentWidthWithoutBorder = maxContentWidthWithBorder - borderWidth;
		if(paddingWidth > maxContentWidthWithoutBorder)
		{
			//remove padding
			areaStyle.setProperty(IStyle.STYLE_PADDING_LEFT, IStyle.NUMBER_0);
			areaStyle.setProperty(IStyle.STYLE_PADDING_RIGHT, IStyle.NUMBER_0);
			paddingWidth = 0;
		}
		int maxContentWidth = maxContentWidthWithoutBorder - paddingWidth;
		if(content.getWidth() > maxContentWidth)
		{
			content.setDimension(maxContentWidth, (int)(maxContentWidth/content.getRatio()));
		}
		
		ImageArea imageArea = (ImageArea)AreaFactory.createImageArea(image, content);
		root.addChild(imageArea);
		
		int posX = (borderWidth==0 ? 0 : 
				getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH)))
				+ (paddingWidth ==0 ? 0 : 
				getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_PADDING_LEFT)));
		int posY = getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH))
					+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_PADDING_TOP));
		
		imageArea.setPosition( posX, posY );
		
		root.setWidth(content.getWidth()
				+ ( borderWidth==0 ? 0 : 
					(getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH))
					+getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH))))
				+ ( paddingWidth==0 ? 0 : 
					(getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_PADDING_LEFT))
					+getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_PADDING_RIGHT))))
		);
		root.setHeight(content.getHeight()
				+getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH))
				+getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH))
				+getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_PADDING_TOP))
				+getDimensionValue(image.getComputedStyle().getProperty(StyleConstants.STYLE_PADDING_BOTTOM))
		);

		imageArea.setWidth(content.getWidth());
		imageArea.setHeight(content.getHeight());
	}

}