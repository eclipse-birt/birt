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

	public void fileNameChanged(ModuleHandle targetElement, AttributeEvent ev);
}