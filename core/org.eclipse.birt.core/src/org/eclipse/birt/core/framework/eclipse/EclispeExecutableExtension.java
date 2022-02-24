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

package org.eclipse.birt.core.framework.eclipse;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExecutableExtension;

/**
 * 
 */
class EclispeExecutableExtension implements IExecutableExtension {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.core.framework.IExecutableExtension#setInitializationData(
	 * org.eclipse.birt.core.framework.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws FrameworkException {
		// TODO Auto-generated method stub

	}

}
