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
