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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.IRegistryEventListener;

public class ExtensionRegistry implements IExtensionRegistry {
	// use HashMap to ensure no duplicated entries are registered
	protected HashMap<String, Bundle> bundles = new HashMap<>();
	protected HashMap<String, ExtensionPoint> extensionPoints = new HashMap<>();
	protected HashMap<String, Extension> extensions = new HashMap<>();

	ExtensionRegistry() {
	}

	public void addBundle(Bundle bundle) {
		this.bundles.put(bundle.getSymbolicName(), bundle);
		Extension[] extensions = bundle.getExtensions();
		for (Extension extension : extensions) {
			this.extensions.put(extension.getUniqueIdentifier(), extension);
		}
		ExtensionPoint[] points = bundle.getExtensionPoints();
		for (ExtensionPoint point : points) {
			this.extensionPoints.put(point.getUniqueIdentifier(), point);
		}
	}

	@Override
	public IConfigurationElement[] getConfigurationElementsFor(String extensionId) {
		IExtension extension = getExtension(extensionId);
		if (extension != null) {
			return extension.getConfigurationElements();
		}
		return null;
	}

	@Override
	public IExtension getExtension(String extensionId) {
		return extensions.get(extensionId);
	}

	@Override
	public IExtension getExtension(String extensionPointId, String extensionId) {
		IExtension extension = getExtension(extensionId);
		if (extension != null) {
			if (extension.getExtensionPointUniqueIdentifier().equals(extensionPointId)) {
				return extension;
			}
		}
		return null;
	}

	@Override
	public IExtension[] getExtensions(String extensionPointId) {
		ArrayList<IExtension> extensions = new ArrayList<>();
		for (Bundle bundle : bundles.values()) {
			Extension[] bundleExtensions = bundle.getExtensions();
			for (Extension extension : bundleExtensions) {
				String extPointId = extension.getExtensionPointUniqueIdentifier();
				if (extPointId.equals(extensionPointId)) {
					extensions.add(extension);
				}
			}
		}
		return extensions.toArray(new IExtension[extensions.size()]);
	}

	@Override
	public IExtensionPoint getExtensionPoint(String extensionPointId) {
		return extensionPoints.get(extensionPointId);
	}

	@Override
	public IExtensionPoint[] getExtensionPoints() {
		return extensionPoints.values().toArray(new IExtensionPoint[extensionPoints.size()]);
	}

	@Override
	public boolean addContribution(InputStream is, IContributor contributor, boolean persist, String name,
			ResourceBundle translationBundle, Object token) throws IllegalArgumentException

	{
		throw new UnsupportedOperationException("addContribution is not implemented yet");
	}

	@Override
	public void addListener(IRegistryEventListener listener) {
		throw new UnsupportedOperationException("addListener is not implemented yet");
	}

	@Override
	public void addListener(IRegistryEventListener listener, String extensionPointId) {
		throw new UnsupportedOperationException("addListener is not implemented yet");
	}

	@Override
	public void addRegistryChangeListener(IRegistryChangeListener arg0) {
		throw new UnsupportedOperationException("addRegistryChangeListener is not implemented yet");
	}

	@Override
	public void addRegistryChangeListener(IRegistryChangeListener listener, String token) {
		throw new UnsupportedOperationException("addRegistryChangeListener is not implemented yet");
	}

	@Override
	public IConfigurationElement[] getConfigurationElementsFor(String namespace, String extensionPointName) {
		IExtensionPoint extPoint = getExtensionPoint(namespace, extensionPointName);
		if (extPoint == null) {
			return new IConfigurationElement[0];
		}
		return extPoint.getConfigurationElements();
	}

	@Override
	public IConfigurationElement[] getConfigurationElementsFor(String namespace, String extensionPointName,
			String extensionId) {
		IExtension extension = getExtension(namespace, extensionPointName, extensionId);
		if (extension == null) {
			return new IConfigurationElement[0];
		}
		return extension.getConfigurationElements();
	}

	@Override
	public IExtension getExtension(String namespace, String extensionPointName, String extensionId) {

		IExtensionPoint point = getExtensionPoint(namespace, extensionPointName);
		if (point != null) {
			return point.getExtension(extensionId);
		}
		return null;
	}

	@Override
	public IExtensionPoint getExtensionPoint(String namespace, String name) {
		IExtensionPoint[] points = getExtensionPoints(namespace);
		for (IExtensionPoint point : points) {
			if (name.equals(point.getSimpleIdentifier())) {
				return point;
			}
		}
		return null;
	}

	@Override
	public IExtensionPoint[] getExtensionPoints(String namespace) {
		ArrayList<IExtensionPoint> extPoints = new ArrayList<>();
		Collection<ExtensionPoint> allExtPoints = extensionPoints.values();
		for (IExtensionPoint extPoint : allExtPoints) {
			if (namespace.equals(extPoint.getNamespace())) {
				extPoints.add(extPoint);
			}
		}
		return extPoints.toArray(new IExtensionPoint[extPoints.size()]);
	}

	@Override
	public IExtensionPoint[] getExtensionPoints(IContributor contributor) {
		for (Bundle bundle : bundles.values()) {
			if (bundle.getContributor() == contributor) {
				return bundle.getExtensionPoints();
			}
		}
		return new IExtensionPoint[] {};
	}

	@Override
	public IExtension[] getExtensions(IContributor contributor) {
		for (Bundle bundle : bundles.values()) {
			if (bundle.getContributor() == contributor) {
				return bundle.getExtensions();
			}
		}
		return new IExtension[] {};
	}

	@Override
	public String[] getNamespaces() {
		return bundles.keySet().toArray(new String[bundles.size()]);
	}

	@Override
	public boolean isMultiLanguage() {
		return false;
	}

	@Override
	public boolean removeExtension(IExtension extension, Object token) throws IllegalArgumentException {
		throw new UnsupportedOperationException("removeExtension is not implemented yet");
	}

	@Override
	public boolean removeExtensionPoint(IExtensionPoint extensionPoint, Object token) throws IllegalArgumentException {
		throw new UnsupportedOperationException("removeExtensionPoint is not implemented yet");
	}

	@Override
	public void removeListener(IRegistryEventListener listener) {
		throw new UnsupportedOperationException("removeListener is not implemented yet");
	}

	@Override
	public void removeRegistryChangeListener(IRegistryChangeListener listener) {
		throw new UnsupportedOperationException("removeRegistryChangeListener is not implemented yet");
	}

	@Override
	public void stop(Object token) throws IllegalArgumentException {
		throw new UnsupportedOperationException("stop is not implemented yet");
	}
}
