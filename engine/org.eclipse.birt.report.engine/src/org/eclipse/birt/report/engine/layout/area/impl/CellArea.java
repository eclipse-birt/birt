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

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public class CellArea extends ContainerArea
{
	static Value DEFAULT_PADDING = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 1500);
	
	protected int rowSpan = -1;
	
	CellArea(ICellContent cell)
	{
		super(cell);
		//remove all border
		removeBorder();
		setDefaultPadding();
	}

	public int getColumnID()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getColumn();
		}
		return 0;
	}
	
	public int getRowID()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getRow();
		}
		return 0;
	}
	
	public int getColSpan()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getColSpan();
		}
		return 1;
	}
	
	public int getRowSpan()
	{
		if(content!=null)
		{
			return ((ICellContent)content).getRowSpan();
		}
		return 1;
	}
	
	protected void setDefaultPadding()
	{
		
		if(content!=null)
		{
			IStyle contentStyle = content.getStyle( );
			CSSValue padding = contentStyle.getProperty( IStyle.STYLE_PADDING_TOP );
			if(padding==null)
			{
				style.setProperty( IStyle.STYLE_PADDING_TOP, DEFAULT_PADDING);
			}
			padding = contentStyle.getProperty( IStyle.STYLE_PADDING_BOTTOM );
			if(padding==null)
			{
				style.setProperty( IStyle.STYLE_PADDING_BOTTOM, DEFAULT_PADDING);
			}
			padding = contentStyle.getProperty( IStyle.STYLE_PADDING_LEFT );
			if(padding==null)
			{
				style.setProperty( IStyle.STYLE_PADDING_LEFT, DEFAULT_PADDING);
			}
			padding = contentStyle.getProperty( IStyle.STYLE_PADDING_RIGHT );
			if(padding==null)
			{
				style.setProperty( IStyle.STYLE_PADDING_RIGHT, DEFAULT_PADDING);
			}
		}
	}

}
