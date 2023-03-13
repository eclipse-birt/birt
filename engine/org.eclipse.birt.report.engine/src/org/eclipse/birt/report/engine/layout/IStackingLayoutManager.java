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

import org.eclipse.birt.report.engine.layout.area.IArea;

public interface IStackingLayoutManager extends ILayoutManager, ILayoutContext {

	/**
	 * The method is called by children layout manager. The child layout manager
	 * submits the results of layout to parent
	 *
	 * @param area
	 * @param keepWithPrevious
	 * @param keepWithNext
	 * @return true if submit succeeded
	 */
	boolean addArea(IArea area, boolean keepWithPrevious, boolean keepWithNext);

	/**
	 * Identify if current page is empty
	 *
	 * @return
	 */
	boolean isPageEmpty();

}
