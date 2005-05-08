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

/**
 * Element has a style. elements with style. such as report item, column, row
 * etc.
 * 
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
abstract public class StyledElementDesign extends ReportElementDesign
{

	/**
	 * style associated with this element.
	 */
	protected StyleDesign style = new StyleDesign( );
	
	protected MapDesign map = null;
	
	protected HighlightDesign highlight = null;

	/**
	 * get the style
	 * 
	 * @return style
	 */
	public StyleDesign getStyle( )
	{
		return this.style;
	}

	/**
	 * set the style
	 * 
	 * @param style
	 *            style of this element.
	 */
	public void setStyle( StyleDesign style )
	{
		this.style = style;
	}

	public String getStyleName( )
	{
		if ( this.style != null )
		{
			return this.style.getName( );
		}
		return null;
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
}
