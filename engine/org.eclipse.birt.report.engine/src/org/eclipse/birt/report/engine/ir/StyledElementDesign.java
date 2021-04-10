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
 */
abstract public class StyledElementDesign extends ReportElementDesign {

	protected String styleName;

	protected MapDesign map = null;

	protected HighlightDesign highlight = null;

	/**
	 * get the style name
	 * 
	 * @return
	 */
	public String getStyleName() {
		return styleName;
	}

	/**
	 * set the style
	 * 
	 * @param style style of this element.
	 */
	public void setStyleName(String name) {
		this.styleName = name;
	}

	/**
	 * @return Returns the highlight.
	 */
	public HighlightDesign getHighlight() {
		return highlight;
	}

	/**
	 * @param highlight The highlight to set.
	 */
	public void setHighlight(HighlightDesign highlight) {
		this.highlight = highlight;
	}

	/**
	 * @return Returns the map.
	 */
	public MapDesign getMap() {
		return map;
	}

	/**
	 * @param map The map to set.
	 */
	public void setMap(MapDesign map) {
		this.map = map;
	}
}
