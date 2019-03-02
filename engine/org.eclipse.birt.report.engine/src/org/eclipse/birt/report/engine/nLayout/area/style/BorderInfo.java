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
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;



public class BorderInfo  extends AreaConstants
{
	
	
	
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
		this(PropertyUtil.getColor( color ), valueStyleMap.get( style ), PropertyUtil.getDimensionValue( width ));
	}
	
	public BorderInfo(CSSValue color, CSSValue style, int width)
	{
		this(PropertyUtil.getColor( color ), valueStyleMap.get( style ),width);
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
