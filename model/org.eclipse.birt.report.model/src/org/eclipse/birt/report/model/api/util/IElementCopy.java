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

package org.eclipse.birt.report.model.api.util;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * The copied object created from <code>CopyUtil.copy()</code>.
 * 
 */

public interface IElementCopy {

	/**
	 * Gets the element which keeps the "extends" relationship and unlocalized
	 * information, it should be only used to validate copy/paste actions.
	 * 
	 * @param handle the module handle
	 * 
	 * @return the design element handle
	 */

	DesignElementHandle getHandle(ModuleHandle handle);;
}
