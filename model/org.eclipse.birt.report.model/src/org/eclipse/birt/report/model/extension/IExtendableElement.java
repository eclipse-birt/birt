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
 * TODO: Document required
 */
public interface IExtendableElement
{
	/**
	 * Name of the property that identifies the name of the extension. BIRT uses
	 * the property to find extension definition in our meta-data dictionary.
	 */

	public static final String EXTENSION_NAME_PROP = "extensionName"; //$NON-NLS-1$

	public ExtensionElementDefn getExtDefn( );

	
}
