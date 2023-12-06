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
package org.eclipse.birt.report.engine.layout.area;

import java.util.Iterator;

/**
 * Container area interface
 *
 * @since 3.3
 *
 */
public interface IContainerArea extends IArea {

	/**
	 * Get the children
	 *
	 * @return Return the children
	 */
	Iterator<?> getChildren();

	/**
	 * Get the count of children
	 *
	 * @return Return the count of children
	 */
	int getChildrenCount();

	/**
	 * Add area child
	 *
	 * @param area child area
	 */
	void addChild(IArea area);

	/**
	 * Get clip needed
	 *
	 * @return need clip
	 */
	boolean needClip();

	/**
	 * Set the clip needed
	 *
	 * @param needClip needed clip
	 */
	void setNeedClip(boolean needClip);

}
