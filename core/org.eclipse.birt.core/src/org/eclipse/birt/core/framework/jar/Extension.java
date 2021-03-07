/*******************************************************************************
 * Copyright (c) 2010, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework.jar;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

@SuppressWarnings("deprecation")
public class Extension implements IExtension {

	protected Bundle bundle;
	protected String namespace;
	protected String uniqueId;
	protected String name;
	protected String extensionPointId;
	protected String label;

	protected ConfigurationElement[] configuration;

	Extension(Bundle bundle, String id) {
		this.bundle = bundle;
		int dotAt = id.lastIndexOf('.');
		if (dotAt == -1) {
			this.namespace = bundle.getSymbolicName();
			this.name = id;
			if (id.length() != 0) {
				this.uniqueId = this.namespace + "." + id;
			}
		} else {
			this.uniqueId = id;
			this.name = id.substring(dotAt + 1);
		}
	}

	@Override
	public IConfigurationElement[] getConfigurationElements() {
		return configuration;
	}

	@Override
	public String getExtensionPointUniqueIdentifier() {
		return extensionPointId;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getUniqueIdentifier() {
		return uniqueId != null ? uniqueId : getNamespaceIdentifier();
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uniqueId);
		sb.append(" extends ");
		sb.append(extensionPointId);
		sb.append(" from ");
		sb.append(bundle.getSymbolicName());
		return sb.toString();
	}

	@Override
	public IContributor getContributor() throws InvalidRegistryObjectException {
		return bundle.getContributor();
	}

	@Override
	public String getLabel(String arg0) throws InvalidRegistryObjectException {
		return null;
	}

	@Override
	public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
		return bundle.getSymbolicName();
	}

	@Override
	public String getSimpleIdentifier() throws InvalidRegistryObjectException {
		return name;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
