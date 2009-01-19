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

package org.eclipse.birt.report.engine.ir;

import org.eclipse.birt.report.engine.content.IStyle;


/**
 * Element has a style. elements with style. such as report item, column, row
 * etc.
 * 
 */
abstract public class StyledElementDesign extends ReportElementDesign
{

	protected String styleClass;
	
	protected MapDesign map = null;
	
	protected HighlightDesign highlight = null;

	protected IStyle style;

	/**
	 * get the style name
	 * @return
	 */
	public String getStyleClass()
	{
		return styleClass;
	}

	/**
	 * set the style
	 * 
	 * @param style
	 *            style of this element.
	 */
	public void setStyleClass( String styleClass )
	{
		this.styleClass = styleClass;
	}

	/**
	 * @return Returns the highlight.
	 */
	public HighlightDesign getHighlight( )
	{
		return highlight;
	}
	/**
	 * @param highlight The highlight to set.
	 */
	public void setHighlight( HighlightDesign highlight )
	{
		this.highlight = highlight;
	}
	/**
	 * @return Returns the map.
	 */
	public MapDesign getMap( )
	{
		return map;
	}
	/**
	 * @param map The map to set.
	 */
	public void setMap( MapDesign map )
	{
		this.map = map;
	}

	public IStyle getStyle( )
	{
		return style;
	}

	public void setStyle( IStyle style )
	{
		this.style = style;
	}
}
