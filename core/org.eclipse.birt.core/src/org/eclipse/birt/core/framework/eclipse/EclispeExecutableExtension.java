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
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws FrameworkException {
		// TODO Auto-generated method stub

	}

}
