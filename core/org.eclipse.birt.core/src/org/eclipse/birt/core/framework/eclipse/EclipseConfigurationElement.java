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
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.core.runtime.CoreException;

/**
 *
 */
class EclipseConfigurationElement implements IConfigurationElement {
	org.eclipse.core.runtime.IConfigurationElement object;

	EclipseConfigurationElement(org.eclipse.core.runtime.IConfigurationElement object) {
		this.object = object;
	}

	@Override
	public Object createExecutableExtension(String propertyName) throws FrameworkException {
		try {
			return object.createExecutableExtension(propertyName);
		} catch (CoreException ex) {
			throw new FrameworkException(ResourceConstants.CREATE_EXTENSION_FAIL, ex);
		}
	}

	@Override
	public String getAttribute(String name) {
		return object.getAttribute(name);
	}

	/**
	 * @deprecated The method is equivalent to the {@link #getAttribute(String)}.
	 *             Contrary to its description, this method returns a translated
	 *             value. Use the {@link #getAttribute(String)} method instead.
	 */
	@Deprecated
	@Override
	public String getAttributeAsIs(String name) {
		return object.getAttributeAsIs(name);
	}

	@Override
	public String[] getAttributeNames() {
		return object.getAttributeNames();
	}

	@Override
	public IConfigurationElement[] getChildren() {
		return EclipsePlatform.wrap(object.getChildren());
	}

	@Override
	public IConfigurationElement[] getChildren(String name) {
		return EclipsePlatform.wrap(object.getChildren(name));
	}

	@Override
	public IExtension getDeclaringExtension() {
		return EclipsePlatform.wrap(object.getDeclaringExtension());
	}

	@Override
	public String getName() {
		return object.getName();
	}

	@Override
	public Object getParent() {
		return EclipsePlatform.wrap(object.getParent());
	}

	@Override
	public String getValue() {
		return object.getValue();
	}

	/**
	 * @deprecated The method is equivalent to the {@link #getValue()}. Contrary to
	 *             its description, this method returns a translated value. Use the
	 *             {@link #getValue()} method instead.
	 */
	@Deprecated
	@Override
	public String getValueAsIs() {
		return object.getValueAsIs();
	}

}
