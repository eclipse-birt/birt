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
	@Override
	public IConfigurationElement[] getConfigurationElements() {
		return EclipsePlatform.wrap(object.getConfigurationElements());
	}

	/**
	 * @return
	 */
	@Override
	public String getExtensionPointUniqueIdentifier() {
		return object.getExtensionPointUniqueIdentifier();
	}

	/**
	 * @return
	 */
	@Override
	public String getLabel() {
		return object.getLabel();
	}

	/**
	 * @return
	 */
	@Override
	public String getNamespace() {
		return object.getContributor().getName();
	}

	/**
	 * @return
	 */
	@Override
	public String getSimpleIdentifier() {
		return object.getSimpleIdentifier();
	}

	/**
	 * @return
	 */
	@Override
	public String getUniqueIdentifier() {
		return object.getUniqueIdentifier();
	}
}
