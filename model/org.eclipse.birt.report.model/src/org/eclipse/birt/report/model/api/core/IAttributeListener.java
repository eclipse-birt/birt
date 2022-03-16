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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * Receives file name events after one attribute of the report design is
 * changed.
 */

public interface IAttributeListener {

	/**
	 * Notifies the element is validated.
	 *
	 * @param targetElement the validated element
	 * @param ev            the validation event which contains the error
	 *                      information
	 */

	void fileNameChanged(ModuleHandle targetElement, AttributeEvent ev);
}
