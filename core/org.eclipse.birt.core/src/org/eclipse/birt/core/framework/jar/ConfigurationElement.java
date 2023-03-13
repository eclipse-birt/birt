/*******************************************************************************
 * Copyright (c) 2010, 2011 Actuate Corporation.
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

package org.eclipse.birt.core.framework.jar;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Status;

public class ConfigurationElement implements IConfigurationElement {

	protected Object parent;
	protected Bundle bundle;
	protected IExtension extension;
	protected String name;
	protected String label;
	protected String value;
	protected HashMap<String, String> attributes;
	protected ConfigurationElement[] children;

	@Override
	public Object createExecutableExtension(String propertyName) throws CoreException {
		String value = attributes.get(propertyName);
		if (value != null) {
			try {
				Class<?> clazz = Class.forName(value);
				Object inst = clazz.newInstance();

				if (inst instanceof IExecutableExtension) {
					((IExecutableExtension) inst).setInitializationData(this, propertyName, null); // TODO support
																									// adapter data
				}
				return inst;
			} catch (Exception e) {
				throw new CoreException(new Status(IStatus.ERROR, "org.eclipse.birt.core", 0, e.getMessage(), e)); //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public String getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public String[] getAttributeNames() {
		return attributes.keySet().toArray(new String[attributes.size()]);
	}

	@Override
	public IConfigurationElement[] getChildren() {
		return children;
	}

	@Override
	public IConfigurationElement[] getChildren(String name) {
		ArrayList<IConfigurationElement> namedChildren = new ArrayList<>();
		for (IConfigurationElement child : children) {
			if (name.equals(child.getName())) {
				namedChildren.add(child);
			}
		}
		return namedChildren.toArray(new IConfigurationElement[namedChildren.size()]);
	}

	@Override
	public IExtension getDeclaringExtension() {
		if (extension != null) {
			return extension;
		}
		if (parent instanceof IExtension) {
			return (IExtension) parent;
		}
		if (parent instanceof ConfigurationElement) {
			return ((ConfigurationElement) parent).getDeclaringExtension();
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getParent() {
		return parent;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getAttribute(String arg0, String arg1) {
		return null;
	}

	@Override
	public String getAttributeAsIs(String name) {
		return getAttribute(name);
	}

	@Override
	public IContributor getContributor() throws InvalidRegistryObjectException {
		if (bundle == null) {
			IExtension declaringExtn = getDeclaringExtension();
			if (declaringExtn != null) {
				return declaringExtn.getContributor();
			}
			return null;
		}

		return bundle.getContributor();
	}

	@Override
	public String getNamespace() throws InvalidRegistryObjectException {
		return bundle.getSymbolicName();
	}

	@Override
	public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
		return bundle.getSymbolicName();
	}

	@Override
	public String getValue(String arg) throws InvalidRegistryObjectException {
		return null;
	}

	@Override
	public String getValueAsIs() throws InvalidRegistryObjectException {
		return value;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public int getHandleId() {
		return 11;
	}
}
