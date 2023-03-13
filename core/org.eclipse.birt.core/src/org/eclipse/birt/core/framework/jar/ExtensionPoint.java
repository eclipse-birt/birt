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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

@SuppressWarnings("deprecation")
public class ExtensionPoint implements IExtensionPoint {

	protected Bundle bundle;
	protected String uniqueId;
	protected String namespace;
	protected String name;
	protected ConfigurationElement[] allExtConfigurations;
	protected String schema;

	ExtensionPoint(Bundle bundle, String id) {
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
			this.namespace = id.substring(0, dotAt);
		}
	}

	@Override
	public IConfigurationElement[] getConfigurationElements() {
		if (allExtConfigurations == null) {
			ArrayList<IConfigurationElement> extConfigList = new ArrayList<>();
			IExtension[] extensions = getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				IConfigurationElement[] extConfigurations = extensions[i].getConfigurationElements();
				for (int j = 0; j < extConfigurations.length; j++) {
					extConfigList.add(extConfigurations[j]);
				}
			}

			allExtConfigurations = extConfigList.toArray(new ConfigurationElement[extConfigList.size()]);
		}

		return allExtConfigurations;
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public String getLabel(String locale) throws InvalidRegistryObjectException {
		return null;
	}

	@Override
	public String getSchemaReference() {
		return schema;
	}

	@Override
	public String getUniqueIdentifier() {
		return uniqueId;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uniqueId);
		sb.append(" from ");
		sb.append(bundle.getSymbolicName());
		return sb.toString();
	}

	@Override
	public IContributor getContributor() throws InvalidRegistryObjectException {
		return bundle.getContributor();
	}

	@Override
	public IExtension getExtension(String extensionId) throws InvalidRegistryObjectException {
		return bundle.platform.extensionRegistry.getExtension(uniqueId, extensionId);
	}

	@Override
	public IExtension[] getExtensions() throws InvalidRegistryObjectException {
		return bundle.platform.extensionRegistry.getExtensions(uniqueId);
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
