/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout;

/**
 *
 * Represents all dynamic layout information, includes
 * <ul>
 * <li>maximum available width
 * <li>maximum available height
 * <li>current position in block direction
 * <li>current position in inline direction
 * <li>the X offset from border box start point to content box start point
 * <li>the Y offset from border box start point to content box start point
 * <li>flag identify is area object is ready
 * </ul>
 *
 */
public interface ILayoutContext {

	/**
	 * get maximum available width in current line
	 *
	 * @return
	 */
	int getCurrentMaxContentWidth();

	int getCurrentMaxContentHeight();

	/**
	 * get current positon in inline direction, the origin point is content box
	 * start point of container
	 *
	 * @return
	 */
	int getCurrentIP();

	void setCurrentIP(int ip);

	/**
	 * get current positon in block direction, the origin point is content box start
	 * point of container
	 *
	 * @return
	 */
	int getCurrentBP();

	void setCurrentBP(int bp);

	/**
	 * the X offset from border box start point to content box start point
	 */
	int getOffsetX();

	void setOffsetX(int x);

	/**
	 * the Y offset from border box start point to content box start point
	 */
	int getOffsetY();

	void setOffsetY(int y);

}
