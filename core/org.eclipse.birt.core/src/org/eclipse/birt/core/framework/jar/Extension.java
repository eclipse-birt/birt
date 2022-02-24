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

	public IConfigurationElement[] getConfigurationElements() {
		return configuration;
	}

	public String getExtensionPointUniqueIdentifier() {
		return extensionPointId;
	}

	public String getLabel() {
		return label;
	}

	public String getUniqueIdentifier() {
		return uniqueId != null ? uniqueId : getNamespaceIdentifier();
	}

	public String getNamespace() {
		return namespace;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uniqueId);
		sb.append(" extends ");
		sb.append(extensionPointId);
		sb.append(" from ");
		sb.append(bundle.getSymbolicName());
		return sb.toString();
	}

	public IContributor getContributor() throws InvalidRegistryObjectException {
		return bundle.getContributor();
	}

	public String getLabel(String arg0) throws InvalidRegistryObjectException {
		return null;
	}

	public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
		return bundle.getSymbolicName();
	}

	public String getSimpleIdentifier() throws InvalidRegistryObjectException {
		return name;
	}

	public boolean isValid() {
		return true;
	}
}
