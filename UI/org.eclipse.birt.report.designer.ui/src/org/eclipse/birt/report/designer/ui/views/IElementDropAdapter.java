/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.views;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

/**
 * 
 */

public interface IElementDropAdapter {

	/**
	 * Validates dropping on the given object. This method is called whenever some
	 * aspect of the drop operation changes.
	 * <p>
	 * Subclasses must implement this method to define which drops make sense. If
	 * clients return true, then they will be allowed to handle the drop in
	 * {@link #handleDrop(DropTargetEvent, Object) }.
	 * </p>
	 * 
	 * @param target       the object that the mouse is currently hovering over, or
	 *                     <code>null</code> if the mouse is hovering over empty
	 *                     space
	 * @param operation    the current drag operation (copy, move, etc.)
	 * @param transferType the current transfer type
	 * @return A status indicating whether the drop is valid.
	 */
	public abstract boolean validateDrop(Object target, int operation, int location, Object transfer,
			TransferData transferType);

	/**
	 * Carry out the DND operation.
	 * 
	 * @param aDropTargetEvent The drop target event.
	 * @param aTarget          The object being dragged onto
	 * @return A status indicating whether the drop completed OK.
	 */
	public abstract boolean handleDrop(Object target, int operation, int location, Object transfer);
}
