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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import java.util.HashMap;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;

/**
 * Base class for border
 *  
 */

public abstract class BaseBorder extends AbstractBorder
{

	/**
	 * Bottom border width.
	 */
	public String bottomWidth;
	/**
	 * Bottom border style.
	 */
	public String bottomStyle;
	/**
	 * Bottom border color.
	 */
	public String bottomColor;
	/**
	 * Top border width.
	 */
	public String topWidth;
	/**
	 * Top border style.
	 */
	public String topStyle;
	/**
	 * Top border color.
	 */
	public String topColor;
	/**
	 * Left border width.
	 */
	public String leftWidth;
	/**
	 * Left border style.
	 */
	public String leftStyle;
	/**
	 * Left border color.
	 */
	public String leftColor;
	/**
	 * Right border width.
	 */
	public String rightWidth;
	/**
	 * Right border style.
	 */
	public String rightStyle;
	/**
	 * Right border color.
	 */
	public String rightColor;

	protected int i_bottom_style, i_bottom_width = 1;
	protected int i_top_style, i_top_width = 1;
	protected int i_left_style, i_left_width = 1;
	protected int i_right_style, i_right_width = 1;

	private static final HashMap styleMap = new HashMap( );
	private static final HashMap widthMap = new HashMap( );

	private static final double EPS = 1.0E-10;

	protected int leftGap, rightGap, bottomGap, topGap;

	static
	{
		styleMap.put( "solid", new Integer( SWT.LINE_SOLID ) );//$NON-NLS-1$
		styleMap.put( "dotted", new Integer( SWT.LINE_DOT ) );//$NON-NLS-1$
		styleMap.put( "dashed", new Integer( SWT.LINE_DASH ) );//$NON-NLS-1$
		styleMap.put( "double", new Integer( -2 ) );//$NON-NLS-1$
		styleMap.put( "none", new Integer( 0 ) );//$NON-NLS-1$

		widthMap.put( "thin", new Integer( 1 ) );//$NON-NLS-1$
		widthMap.put( "medium", new Integer( 2 ) );//$NON-NLS-1$
		widthMap.put( "thick", new Integer( 3 ) );//$NON-NLS-1$
	}

	/**
	 * Since the insets now include border and padding, use this to get the true
	 * and non-revised border insets.
	 * 
	 * @return border insets.
	 */
	public abstract Insets getTrueBorderInsets( );

	/**
	 * Since the insets now include border and padding, use this to get the
	 * border insets. This value may be revised according to specified element.
	 * 
	 * @return border insets.
	 */
	public abstract Insets getBorderInsets( );

	/**
	 * Sets the insets for padding.
	 * 
	 * @param in
	 */
	public abstract void setPaddingInsets( Insets in );

	/**
	 * Returns the border style.
	 * 
	 * @param obj
	 * @return
	 */
	protected int getBorderStyle( Object obj )
	{
		Integer retValue = (Integer) ( styleMap.get( obj ) );
		if ( retValue == null )
		{
			return SWT.LINE_DASH;
		}

		return retValue.intValue( );
	}

	/**
	 * Returns the border width as pixel.
	 * 
	 * @param obj
	 * @return
	 */
	protected int getBorderWidth( Object obj )
	{
		//handle non-predefined values.
		if ( obj instanceof String )
		{
			String[] rt = DEUtil.splitString( (String) obj );

			if ( rt[0] != null && DEUtil.isValidNumber( rt[0] ) )
			{
				double w = DEUtil.convertoToPixel( new DimensionValue( Double.parseDouble( rt[0] ),
						rt[1] ) );

				// if the width is too small,
				// think it's zero
				if ( w <= EPS )
				{
					return 0;
				}

				// if the width is not too small;
				// think it's minimum size is 1
				return Math.max( 1, (int) w );
			}
		}

		//handle predefined values.
		Integer retValue = (Integer) ( widthMap.get( obj ) );

		if ( retValue == null )
		{
			return 1;
		}

		return retValue.intValue( );
	}

	/**
	 * Convenient method to return the specified border style directly.
	 * 
	 * @return
	 */
	public int getLeftBorderStyle( )
	{
		return getBorderStyle( leftStyle );
	}

	/**
	 * Convenient method to return the specified border style directly.
	 * 
	 * @return
	 */
	public int getRightBorderStyle( )
	{
		return getBorderStyle( rightStyle );
	}

	/**
	 * Convenient method to return the specified border style directly.
	 * 
	 * @return
	 */
	public int getTopBorderStyle( )
	{
		return getBorderStyle( topStyle );
	}

	/**
	 * Convenient method to return the specified border style directly.
	 * 
	 * @return
	 */
	public int getBottomBorderStyle( )
	{
		return getBorderStyle( bottomStyle );
	}

	/**
	 * Convenient method to return the specified border width directly.
	 * 
	 * @return
	 */
	public int getLeftBorderWidth( )
	{
		return getBorderWidth( leftWidth );
	}

	/**
	 * Convenient method to return the specified border width directly.
	 * 
	 * @return
	 */
	public int getRightBorderWidth( )
	{
		return getBorderWidth( rightWidth );
	}

	/**
	 * Convenient method to return the specified border width directly.
	 * 
	 * @return
	 */
	public int getTopBorderWidth( )
	{
		return getBorderWidth( topWidth );
	}

	/**
	 * Convenient method to return the specified border width directly.
	 * 
	 * @return
	 */
	public int getBottomBorderWidth( )
	{
		return getBorderWidth( bottomWidth );
	}

	/**
	 * Convenient method to return the specified border color directly.
	 * 
	 * @return
	 */
	public int getLeftBorderColor( )
	{
		return ColorUtil.parseColor( leftColor );
	}

	/**
	 * Convenient method to return the specified border color directly.
	 * 
	 * @return
	 */
	public int getRightBorderColor( )
	{
		return ColorUtil.parseColor( rightColor );
	}

	/**
	 * Convenient method to return the specified border color directly.
	 * 
	 * @return
	 */
	public int getTopBorderColor( )
	{
		return ColorUtil.parseColor( topColor );
	}

	/**
	 * Convenient method to return the specified border color directly.
	 * 
	 * @return
	 */
	public int getBottomBorderColor( )
	{
		return ColorUtil.parseColor( bottomColor );
	}

}