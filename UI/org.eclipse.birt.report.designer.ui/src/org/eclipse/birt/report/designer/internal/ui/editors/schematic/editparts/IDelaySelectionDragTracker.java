/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
