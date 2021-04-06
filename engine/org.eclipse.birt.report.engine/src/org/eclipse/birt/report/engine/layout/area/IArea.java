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
package org.eclipse.birt.report.engine.layout.area;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;

public interface IArea {
	/**
	 * Gets the content object of the area.
	 * 
	 * @return the content object.
	 */
	IContent getContent();

	/**
	 * Gets the content's computed style.
	 * 
	 * @return the computed style.
	 */
	IStyle getStyle();

	/**
	 * Gets the x coordinate of the area's top-left border corner.
	 * 
	 * @return the x coordinate.
	 */
	int getX();

	/**
	 * Gets the y coordinate of the area's top-left border corner.
	 * 
	 * @return the y coordinate.
	 */
	int getY();

	/**
	 * Gets the width of the area, ignoring margin.
	 * 
	 * @return the width of the area without margin.
	 */
	int getWidth();

	/**
	 * Gets the height of the area, ignoring margin.
	 * 
	 * @return the height of the area without margin.
	 */
	int getHeight();

	void accept(IAreaVisitor visitor);

	/**
	 * Gets the scale of the area. The <i>scale</i> property is normally used in
	 * render time to determine the zoom ratio of the content in this area.
	 * 
	 * @return the scale.
	 */
	float getScale();
}
