/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;

/**
 * Class for to select the cell
 */

public interface IDelaySelectionDragTracker extends DragTracker {
	/**
	 * Sets the statrt location
	 *
	 * @param p
	 */
	void setStartLocation(Point p);

	/**
	 * Sets the state
	 *
	 * @param state
	 */
	void setState(int state);

	/**
	 * Handles the drag
	 *
	 * @return
	 */
	boolean handleDragInProgress();

	/**
	 * Set the location
	 *
	 * @param p
	 */
	void setLocation(Point p);

	/**
	 * Gets the source editpart
	 *
	 * @return
	 */
	EditPart getSourceEditPart();
}
