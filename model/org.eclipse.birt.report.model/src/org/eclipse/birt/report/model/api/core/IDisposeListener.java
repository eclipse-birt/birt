/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * Receives dispose events after one report design is disposed.
 */

public interface IDisposeListener {
	/**
	 * Notifies the element is disposed.
	 * 
	 * @param targetElement the disposed report design
	 * @param ev            the dispose event
	 */

	public void moduleDisposed(ModuleHandle targetElement, DisposeEvent ev);
}
