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