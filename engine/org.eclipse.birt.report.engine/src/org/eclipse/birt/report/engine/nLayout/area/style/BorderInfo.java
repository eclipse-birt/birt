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

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.awt.Color;
import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;



public class BorderInfo
{
	/**
	 * the "dotted" value
	 */
	public final static int BORDER_STYLE_DOTTED = 0;
	/**
	 * the "solid" value
	 */
	public static final int BORDER_STYLE_SOLID = 1;
	/**
	 * the "dashed" value
	 */
	public static final int BORDER_STYLE_DASHED = 2;
	/**
	 * the "double" value
	 */
	public static final int BORDER_STYLE_DOUBLE = 3;
	/**
	 * the "groove" value
	 */
	public static final int BORDER_STYLE_GROOVE = 4;
	/**
	 * the "ridge" value
	 */
	public static final int BORDER_STYLE_RIDGE = 5;
	/**
	 * the "inset" value
	 */
	public static final int BORDER_STYLE_INSET = 6;
	/**
	 * the "outset" value
	 */
	public static final int BORDER_STYLE_OUTSET = 7;  
	
	/**
	 * the "none" value
	 */
	public final static int BORDER_STYLE_NONE = 8;
	
	/**
	 * the "hidden" value
	 */
	public final static int BORDER_STYLE_HIDDEN = 9;
	
	public static HashMap<CSSValue, Integer> styleMap = new HashMap<CSSValue, Integer>();
	static
	{
		styleMap.put( IStyle.DOTTED_VALUE, BORDER_STYLE_DOTTED);
		styleMap.put( IStyle.SOLID_VALUE, BORDER_STYLE_SOLID );
		styleMap.put( IStyle.DASHED_VALUE, BORDER_STYLE_DASHED);
		styleMap.put( IStyle.DOUBLE_VALUE, BORDER_STYLE_DOUBLE);
		styleMap.put( IStyle.GROOVE_VALUE, BORDER_STYLE_GROOVE );
		styleMap.put( IStyle.RIDGE_VALUE, BORDER_STYLE_RIDGE );
		styleMap.put( IStyle.INSET_VALUE, BORDER_STYLE_INSET );
		styleMap.put( IStyle.OUTSET_VALUE, BORDER_STYLE_OUTSET );
		styleMap.put( IStyle.NONE_VALUE, BORDER_STYLE_NONE );
		styleMap.put( IStyle.HIDDEN_VALUE, BORDER_STYLE_HIDDEN );
		
	}
	
	
	
	private Color color;
	private int width;
	private int style;
	
	public BorderInfo(Color color, int style, int width)
	{
		this.color = color;
		this.style = style;
		this.width = width;
	}
	
	public BorderInfo(BorderInfo border)
	{
		this.color = border.color;
		this.style = border.style;
		this.width = border.width;
	}
	
	public BorderInfo(CSSValue color, CSSValue style, CSSValue width)
	{
		this(PropertyUtil.getColor( color ), styleMap.get( style ), PropertyUtil.getDimensionValue( width ));
	}
	
	public BorderInfo(CSSValue color, CSSValue style, int width)
	{
		this(PropertyUtil.getColor( color ), styleMap.get( style ),width);
	}
	
	public int getStyle()
	{
		return style;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public int getWidth()
	{
		return width;
	}
}
