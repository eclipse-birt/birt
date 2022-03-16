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
