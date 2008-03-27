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

package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.css.dom.ComputedStyle;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

/**
 * abstract area which is the default implementation of <code>IArea</code>
 * 
 */
public abstract class AbstractArea implements IArea
{

	/**
	 * style of this area
	 */
	protected IStyle style;

	/**
	 * x position of this area in parent area
	 */
	protected int x;

	/**
	 * y position of this area in parent area
	 */
	protected int y;

	/**
	 * width of this area
	 */
	protected int width;

	/**
	 * height of this area
	 */
	protected int height;

	/**
	 * the content object
	 */
	protected IContent content;


	/**
	 * constructor
	 * 
	 * @param content
	 */
	AbstractArea( IContent content )
	{
		this.content = content;
		if ( content != null )
		{
			style = new AreaStyle( (ComputedStyle) content.getComputedStyle( ) );
		}
		else
		{
			style = new AreaStyle( new BIRTCSSEngine( ) );
		}
	}
	
	AbstractArea( IReportContent report )
	{
		if ( report != null )
		{
			assert ( report instanceof ReportContent );
			style = new AreaStyle( ( (ReportContent) report ).getCSSEngine( ) );
		}
		else
		{
			style = new AreaStyle( new BIRTCSSEngine( ) );
		}

	}
	
	
	protected float scale = 1.0f;
	
	public void setScale(float scale)
	{
		this.scale = scale;
	}
	
	public float getScale()
	{
		return this.scale;
	}

	/**
	 * set allocated position
	 * 
	 * @param ax
	 * @param ay
	 */
	public void setAllocatedPosition( int ax, int ay )
	{
		x = ax
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) );
		y = ay
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_TOP ) );
	}

	/**
	 * set allocated height
	 * 
	 * @param aHeight
	 */
	public void setAllocatedHeight( int aHeight )
	{
		height = aHeight
				- PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_TOP ) )
				- PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_BOTTOM ) );
	}

	/**
	 * set allocated width
	 * 
	 * @param aWidth
	 */
	public void setAllocatedWidth( int aWidth )
	{
		int totalMarginWidth = PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );
		if( totalMarginWidth >= aWidth)
		{
			style.setProperty(IStyle.STYLE_MARGIN_LEFT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_RIGHT, IStyle.NUMBER_0);
			width = aWidth;
		}
		else
		{
			width = aWidth - totalMarginWidth;
		}
	}

	public void setContentHeight( int cHeight )
	{
		height = cHeight
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) )
				+ PropertyUtil
						.getDimensionValue( style
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_TOP ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_BOTTOM ) );
	}
	
	public void setContentWidth( int cWidth )
	{
		width = cWidth
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) )
				+ PropertyUtil
						.getDimensionValue( style
								.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_LEFT ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_RIGHT ) );
	}

	/**
	 * set allocated X position
	 * 
	 * @return
	 */
	public int getAllocatedX( )
	{
		return x
				- PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) );
	}

	/**
	 * set allocated Y position
	 * 
	 * @return
	 */
	public int getAllocatedY( )
	{
		return y
				- PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_TOP ) );
	}

	/**
	 * get content width
	 * 
	 * @return
	 */
	public int getContentWidth( )
	{
		int totalPaddngWidth = PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_LEFT ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_RIGHT ) );
		int totalBorderWidth = PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) )
				+ PropertyUtil
						.getDimensionValue( style
								.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
		if( width <= totalPaddngWidth )
		{
			style.setProperty(IStyle.STYLE_PADDING_LEFT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_RIGHT, IStyle.NUMBER_0);
			return width - totalBorderWidth ;
		}
		else
		{
			return width - totalPaddngWidth - totalBorderWidth ;
		}
	}

	/**
	 * get content height
	 * 
	 * @return
	 */
	public int getContentHeight( )
	{
		return height
				- PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) )
				- PropertyUtil
						.getDimensionValue( style
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) )
				- PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_TOP ) )
				- PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_PADDING_BOTTOM ) );
	}

	/**
	 * get allocated width
	 * 
	 * @return
	 */
	public int getAllocatedWidth( )
	{
		return width
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_LEFT ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_RIGHT ) );
	}
	
	

	/**
	 * get allocated height
	 * 
	 * @return
	 */
	public int getAllocatedHeight( )
	{
		return height + PropertyUtil.getDimensionValue( style
				.getProperty( StyleConstants.STYLE_MARGIN_TOP ) )
				+ PropertyUtil.getDimensionValue( style
						.getProperty( StyleConstants.STYLE_MARGIN_BOTTOM ) );
	}


	/**
	 * get style of this area
	 */
	public IStyle getStyle( )
	{
		return style;
	}

	/**
	 * get X position of this area
	 */
	public int getX( )
	{
		return x;
	}

	/**
	 * get Y position of this area
	 */
	public int getY( )
	{
		return y;
	}

	public void setPosition( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * set width of this area
	 * 
	 * @param width
	 */
	public void setWidth( int width )
	{
		this.width = width;
	}

	/**
	 * set width of this area
	 */
	public int getWidth( )
	{
		return width;
	}

	/**
	 * get height of this area
	 */
	public int getHeight( )
	{
		return height;
	}

	/**
	 * set height of this area
	 * 
	 * @param height
	 */
	public void setHeight( int height )
	{
		this.height = height;
	}
	
	/**
	 * Gets the baseline
	 * @return the baseline
	 */
	public int getBaseLine()
	{
		return height;
	}
	
	/**
	 * get content object
	 */
	public IContent getContent( )
	{
		return content;
	}
	
	protected void removeMargin()
	{
		style.setProperty(IStyle.STYLE_MARGIN_LEFT, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_MARGIN_RIGHT, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0);

	}
	protected void removeBorder()
	{
		style.setProperty(IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.NUMBER_0);
	}
	
	protected void removePadding()
	{
		style.setProperty(IStyle.STYLE_PADDING_LEFT, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_PADDING_RIGHT, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_PADDING_BOTTOM, IStyle.NUMBER_0);
	}
	

}
