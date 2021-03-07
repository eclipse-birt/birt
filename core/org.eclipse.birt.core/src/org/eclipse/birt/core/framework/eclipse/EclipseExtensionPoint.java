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
	@Override
	public IConfigurationElement[] getConfigurationElements() {
		return EclipsePlatform.wrap(object.getConfigurationElements());
	}

	/**
	 * @param extensionId
	 * @return
	 */
	@Override
	public IExtension getExtension(String extensionId) {
		return EclipsePlatform.wrap(object.getExtension(extensionId));
	}

	/**
	 * @return
	 */
	@Override
	public IExtension[] getExtensions() {
		return EclipsePlatform.wrap(object.getExtensions());
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
	public String getSchemaReference() {
		return object.getSchemaReference();
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
