/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;

public interface IArea {

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

	String getBookmark();

	IHyperlinkAction getAction();
}
