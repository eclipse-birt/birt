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

import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;

/**
 * 
 */
class EclipseExtension implements IExtension {
	org.eclipse.core.runtime.IExtension object;

	EclipseExtension(org.eclipse.core.runtime.IExtension object) {
		this.object = object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.framework.IExtension#getConfigurationElements()
	 */
	public IConfigurationElement[] getConfigurationElements() {
		return EclipsePlatform.wrap(object.getConfigurationElements());
	}

	/**
	 * @return
	 */
	public String getExtensionPointUniqueIdentifier() {
		return object.getExtensionPointUniqueIdentifier();
	}

	/**
	 * @return
	 */
	public String getLabel() {
		return object.getLabel();
	}

	/**
	 * @return
	 */
	public String getNamespace() {
		return object.getContributor().getName();
	}

	/**
	 * @return
	 */
	public String getSimpleIdentifier() {
		return object.getSimpleIdentifier();
	}

	/**
	 * @return
	 */
	public String getUniqueIdentifier() {
		return object.getUniqueIdentifier();
	}
}
