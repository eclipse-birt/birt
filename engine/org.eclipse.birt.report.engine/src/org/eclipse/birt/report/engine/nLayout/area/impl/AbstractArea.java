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

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * abstract area which is the default implementation of <code>IArea</code>
 * 
 */
public abstract class AbstractArea implements IArea
{

	protected static Logger logger = Logger.getLogger( AbstractArea.class
			.getName( ) );
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
	 * the baseline
	 */
	protected int baseLine = 0;

	protected float scale = 1.0f;

	protected transient CSSValue vAlign;

	protected String bookmark = null;

	protected transient ContainerArea parent;

	AbstractArea( AbstractArea area )
	{
		this.x = area.getX( );
		this.y = area.getY( );
		this.baseLine = area.getBaseLine( );
		this.bookmark = area.getBookmark( );
		this.action = area.getAction( );
		this.scale = area.getScale( );
		this.width = area.getWidth( );
		this.height = area.getHeight( );
	}

	public ContainerArea getParent( )
	{
		return parent;
	}

	public void setParent( ContainerArea parent )
	{
		this.parent = parent;
	}

	public CSSValue getVerticalAlign( )
	{
		return vAlign;
	}

	public void setVerticalAlign( CSSValue vAlign )
	{
		this.vAlign = vAlign;
	}

	AbstractArea( )
	{

	}

	public String getBookmark( )
	{
		return bookmark;
	}

	public void setBookmark( String bookmark )
	{
		this.bookmark = bookmark;
	}

	public IHyperlinkAction getAction( )
	{
		return action;
	}

	public void setAction( IHyperlinkAction action )
	{
		this.action = action;
	}

	public void setX( int x )
	{
		this.x = x;
	}

	public void setY( int y )
	{
		this.y = y;
	}

	protected IHyperlinkAction action = null;

	public void setScale( float scale )
	{
		this.scale = scale;
	}

	public float getScale( )
	{
		return this.scale;
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

	public void setAllocatedPosition( int x, int y )
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

	public int getAllocatedWidth( )
	{
		return width;
	}

	public int getAllocatedHeight( )
	{
		return height;
	}

	/**
	 * Sets the baseLine
	 * 
	 * @param baseLine
	 */
	public void setBaseLine( int baseLine )
	{
		this.baseLine = baseLine;
	}

	/**
	 * Gets the baseline
	 * 
	 * @return the baseline
	 */
	public int getBaseLine( )
	{
		if ( baseLine == 0 )
		{
			return height;
		}
		else
		{
			return baseLine;
		}

	}

	public abstract AbstractArea cloneArea( );

	public AbstractArea deepClone( )
	{
		return cloneArea( );
	}

	protected void validateBoxProperty( IStyle style, int maxWidth,
			int maxHeight )
	{
		// support negative margin
		int leftMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_LEFT ), maxWidth );
		int rightMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_RIGHT ), maxWidth );
		int topMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_TOP ), maxWidth );
		int bottomMargin = getDimensionValue( style
				.getProperty( IStyle.STYLE_MARGIN_BOTTOM ), maxWidth );

		// do not support negative paddding
		int leftPadding = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_PADDING_LEFT ), maxWidth ) );
		int rightPadding = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_PADDING_RIGHT ), maxWidth ) );
		int topPadding = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_PADDING_TOP ), maxWidth ) );
		int bottomPadding = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_PADDING_BOTTOM ), maxWidth ) );
		// border does not support negative value, do not support pencentage
		// dimension
		int leftBorder = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_BORDER_LEFT_WIDTH ), 0 ) );
		int rightBorder = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_BORDER_RIGHT_WIDTH ), 0 ) );
		int topBorder = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_BORDER_TOP_WIDTH ), 0 ) );
		int bottomBorder = Math.max( 0, getDimensionValue( style
				.getProperty( IStyle.STYLE_BORDER_BOTTOM_WIDTH ), 0 ) );

		int[] vs = new int[]{rightMargin, leftMargin, rightPadding,
				leftPadding, rightBorder, leftBorder};
		resolveBoxConflict( vs, maxWidth );

		int[] hs = new int[]{bottomMargin, topMargin, bottomPadding,
				topPadding, bottomBorder, topBorder};
		// resolveBoxConflict( hs, maxHeight );

		style.setProperty( IStyle.STYLE_MARGIN_LEFT, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[1] ) );
		style.setProperty( IStyle.STYLE_MARGIN_RIGHT, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[0] ) );
		style.setProperty( IStyle.STYLE_MARGIN_TOP, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[1] ) );
		style.setProperty( IStyle.STYLE_MARGIN_BOTTOM, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[0] ) );

		style.setProperty( IStyle.STYLE_PADDING_LEFT, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[3] ) );
		style.setProperty( IStyle.STYLE_PADDING_RIGHT, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[2] ) );
		style.setProperty( IStyle.STYLE_PADDING_TOP, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[3] ) );
		style.setProperty( IStyle.STYLE_PADDING_BOTTOM, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[2] ) );

		style.setProperty( IStyle.STYLE_BORDER_LEFT_WIDTH, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[5] ) );
		style.setProperty( IStyle.STYLE_BORDER_RIGHT_WIDTH, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, vs[4] ) );
		style.setProperty( IStyle.STYLE_BORDER_TOP_WIDTH, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[5] ) );
		style.setProperty( IStyle.STYLE_BORDER_BOTTOM_WIDTH, new FloatValue(
				CSSPrimitiveValue.CSS_NUMBER, hs[4] ) );
	}

	private void resolveConflict( int[] values, int maxTotal, int total,
			int start )
	{
		int length = values.length - start;
		if ( length == 0 )
		{
			return;
		}
		assert ( length > 0 );
		if ( total > maxTotal )
		{
			int othersTotal = total - values[start];
			if ( values[start] > 0 )
			{
				values[start] = 0;
			}
			resolveConflict( values, maxTotal, othersTotal, start + 1 );
		}
	}

	protected int getDimensionValue( String d )
	{

		if ( d == null )
		{
			return 0;
		}
		try
		{
			if ( d.endsWith( "in" ) || d.endsWith( "in" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return (int) ( ( Float.valueOf( d
						.substring( 0, d.length( ) - 2 ) ).floatValue( ) ) * 72000.0f );
			}
			else if ( d.endsWith( "cm" ) || d.endsWith( "CM" ) ) //$NON-NLS-1$//$NON-NLS-2$
			{
				return (int) ( ( Float.valueOf( d
						.substring( 0, d.length( ) - 2 ) ).floatValue( ) ) * 72000.0f / 2.54f );
			}
			else if ( d.endsWith( "mm" ) || d.endsWith( "MM" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return (int) ( ( Float.valueOf( d
						.substring( 0, d.length( ) - 2 ) ).floatValue( ) ) * 7200.0f / 2.54f );
			}
			else if ( d.endsWith( "px" ) || d.endsWith( "PX" ) ) //$NON-NLS-1$//$NON-NLS-2$
			{
				return (int) ( ( Float.valueOf( d
						.substring( 0, d.length( ) - 2 ) ).floatValue( ) ) / 96.0f * 72000.0f );// set
				// as
				// 96dpi
			}
			else
			{
				return (int) ( ( Float.valueOf( d ).floatValue( ) ) );
			}
		}
		catch ( NumberFormatException ex )
		{
			// logger.log( Level.WARNING, ex.getLocalizedMessage( ) );
			return 0;
		}
	}

	protected int getDimensionValue( IContent content, DimensionType d )
	{
		return getDimensionValue( content, d, 0 );
	}

	public int getAllocatedX( )
	{
		return x;
	}

	public int getAllocatedY( )
	{
		return y;
	}

	protected int getDimensionValue( IContent content, DimensionType d,
			int referenceLength )
	{
		if ( d != null )
		{
			try
			{
				String units = d.getUnits( );
				if ( units.equals( EngineIRConstants.UNITS_PT )
						|| units.equals( EngineIRConstants.UNITS_CM )
						|| units.equals( EngineIRConstants.UNITS_MM )
						|| units.equals( EngineIRConstants.UNITS_PC )
						|| units.equals( EngineIRConstants.UNITS_IN ) )
				{
					double point = d.convertTo( EngineIRConstants.UNITS_PT ) * 1000;
					return (int) point;
				}
				else if ( units.equals( EngineIRConstants.UNITS_PX ) )
				{
					double point = d.getMeasure( ) / 72.0d * 72000d;
					return (int) point;
				}
				else if ( units.equals( EngineIRConstants.UNITS_PERCENTAGE ) )
				{
					double point = referenceLength * d.getMeasure( ) / 100.0;
					return (int) point;
				}
				else if ( units.equals( EngineIRConstants.UNITS_EM )
						|| units.equals( EngineIRConstants.UNITS_EX ) )
				{
					int size = 9000;
					if ( content != null )
					{
						IStyle style = content.getComputedStyle( );
						CSSValue fontSize = style
								.getProperty( IStyle.STYLE_FONT_SIZE );
						size = getDimensionValue( fontSize );
					}
					double point = size * d.getMeasure( );
					return (int) point;
				}
			}
			catch ( Exception e )
			{
				// logger.log( Level.WARNING, e.getLocalizedMessage( ) );
				return 0;
			}
		}
		return 0;
	}

	protected int getDimensionValue( CSSValue value )
	{
		return getDimensionValue( value, 0 );
	}

	protected void resolveBoxConflict( int[] vs, int max )
	{
		int vTotal = 0;
		for ( int i = 0; i < vs.length; i++ )
		{
			vTotal += vs[i];
		}
		resolveConflict( vs, max, vTotal, 0 );
	}

	protected int getDimensionValue( CSSValue value, int referenceLength )
	{
		if ( value != null && ( value instanceof FloatValue ) )
		{
			FloatValue fv = (FloatValue) value;
			float v = fv.getFloatValue( );
			switch ( fv.getPrimitiveType( ) )
			{
				case CSSPrimitiveValue.CSS_CM :
					return (int) ( v * 72000 / 2.54 );

				case CSSPrimitiveValue.CSS_IN :
					return (int) ( v * 72000 );

				case CSSPrimitiveValue.CSS_MM :
					return (int) ( v * 7200 / 2.54 );

				case CSSPrimitiveValue.CSS_PT :
					return (int) ( v * 1000 );
				case CSSPrimitiveValue.CSS_NUMBER :
					return (int) v;
				case CSSPrimitiveValue.CSS_PERCENTAGE :

					return (int) ( referenceLength * v / 100.0 );
			}
		}
		return 0;
	}

}
