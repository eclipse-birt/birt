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

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;

/**
 * Defines the interface for extendable element.
 */

public interface IExtendableElement {
	/**
	 * Returns the extension element definition which contains all property
	 * definition from extension and Model.
	 * 
	 * @return extension element definition
	 */

	public ExtensionElementDefn getExtDefn();

}
