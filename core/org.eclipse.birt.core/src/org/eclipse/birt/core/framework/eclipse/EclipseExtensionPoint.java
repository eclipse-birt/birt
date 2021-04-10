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
import org.eclipse.birt.core.framework.IExtensionPoint;

/**
 * 
 */
class EclipseExtensionPoint implements IExtensionPoint {
	org.eclipse.core.runtime.IExtensionPoint object;

	EclipseExtensionPoint(org.eclipse.core.runtime.IExtensionPoint object) {
		this.object = object;
	}

	/**
	 * @return
	 */
	public IConfigurationElement[] getConfigurationElements() {
		return EclipsePlatform.wrap(object.getConfigurationElements());
	}

	/**
	 * @param extensionId
	 * @return
	 */
	public IExtension getExtension(String extensionId) {
		return EclipsePlatform.wrap(object.getExtension(extensionId));
	}

	/**
	 * @return
	 */
	public IExtension[] getExtensions() {
		return EclipsePlatform.wrap(object.getExtensions());
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
	public String getSchemaReference() {
		return object.getSchemaReference();
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
