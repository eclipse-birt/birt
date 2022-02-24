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

package org.eclipse.birt.report.designer.ui.extensions;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * The interface used to define the builder for the extended element
 */

public interface IReportItemBuilderUI {

	/**
	 * Opens the builder to create or edit an element
	 * 
	 * @param handle the handle of the element to edit, if it is null, a new element
	 *               will be created.
	 * @return Returns Windows.OK if the edit operation finished,or Windows.CANCEL
	 *         if the operation is cancelled
	 */
	public int open(ExtendedItemHandle handle);
}
