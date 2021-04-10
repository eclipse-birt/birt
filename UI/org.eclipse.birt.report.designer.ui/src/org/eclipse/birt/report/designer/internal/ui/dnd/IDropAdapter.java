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

package org.eclipse.birt.report.designer.internal.ui.dnd;

/**
 * 
 */

public interface IDropAdapter {

	/**
	 * Validate the tranfer object can be droped to traget with special operation
	 * and location.
	 * 
	 * @param transfer
	 * @param target
	 * @param operation
	 * @param location
	 * @return
	 */
	public int canDrop(Object transfer, Object target, int operation, DNDLocation location);

	/**
	 * Perform the drop operation.
	 * 
	 * @param transfer
	 * @param target
	 * @param operation
	 * @param location
	 * @return
	 */
	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location);
}
