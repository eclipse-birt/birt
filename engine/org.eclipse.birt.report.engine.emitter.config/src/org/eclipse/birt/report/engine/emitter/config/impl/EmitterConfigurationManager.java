/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.config.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.emitter.config.IEmitterConfigurationManager;
import org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor;

/**
 * This is an utility class to retrieve emitter config extension points.
 */
public class EmitterConfigurationManager implements IEmitterConfigurationManager {

	/** The extension point ID of emitter config. */
	private static final String EXTENSION_EMITTER_CONFIG_CONTRIBUTOR = "org.eclipse.birt.report.engine.emitter.config"; //$NON-NLS-1$

	private Map<String, IConfigurationElement> configCache;

	private Map<String, IEmitterDescriptor> descriptorCache;

	/**
	 * The default constructor
	 */
	public EmitterConfigurationManager() {
		try {
			initExtensions();
		} catch (FrameworkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns all extension elements.
	 *
	 * @return all extension elements.
	 * @throws FrameworkException
	 */
	private void initExtensions() throws FrameworkException {
		descriptorCache = new HashMap<>();
		configCache = new HashMap<>();

		IExtensionRegistry registry = Platform.getExtensionRegistry();

		if (registry == null) {
			return;
		}

		IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_EMITTER_CONFIG_CONTRIBUTOR);

		if (extensionPoint == null) {
			return;
		}

		for (IExtension extension : extensionPoint.getExtensions()) {
			if (extension != null) {
				IConfigurationElement[] elements = extension.getConfigurationElements();

				if (elements != null) {
					for (IConfigurationElement element : elements) {
						if (element != null) {
							String key = element.getAttribute("id"); //$NON-NLS-1$

							IConfigurationElement oldElement = configCache.get(key);

							configCache.put(key, getPrioritized(oldElement, element));
						}
					}
				}
			}
		}
	}

	private IConfigurationElement getPrioritized(IConfigurationElement eleA, IConfigurationElement eleB) {
		int priotiryA = eleA == null ? 0 : 1;
		int priotiryB = eleB == null ? 0 : 1;

		if (priotiryA + priotiryB == 0) {
			return eleA;
		}

		if (priotiryA + priotiryB == 1) {
			return (priotiryA - priotiryB < 0) ? eleB : eleA;
		}

		try {
			priotiryA = Integer.parseInt(eleA.getAttribute("priority")); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			priotiryA = 0;
		}

		try {
			priotiryB = Integer.parseInt(eleB.getAttribute("priority")); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			priotiryB = 0;
		}

		return (priotiryA - priotiryB < 0) ? eleB : eleA;
	}

	/**
	 * Returns an emitter descriptor with the specified emitter ID.
	 *
	 * @param emitterID the emitter ID.
	 * @return an emitter descriptor with the specified emitter ID.
	 */
	@Override
	public synchronized IEmitterDescriptor getEmitterDescriptor(String emitterID) {
		if (emitterID == null) {
			return null;
		}

		IEmitterDescriptor desc = getCachedEmitterDescriptor(emitterID);

		if (desc == null) {
			IConfigurationElement element = configCache.get(emitterID);

			if (element != null) {
				try {
					desc = (IEmitterDescriptor) element.createExecutableExtension("class"); //$NON-NLS-1$

					descriptorCache.put(emitterID, desc);
				} catch (FrameworkException e) {
					e.printStackTrace();
				}
			}
		}

		return desc;
	}

	@Override
	public synchronized IEmitterDescriptor getEmitterDescriptor(String emitterID, Locale locale) {
		if (emitterID == null) {
			return null;
		}

		IEmitterDescriptor desc = getEmitterDescriptor(emitterID);

		if (desc != null) {
			desc.setLocale(locale);
		}

		return desc;
	}

	public synchronized IEmitterDescriptor getCachedEmitterDescriptor(String emitterID) {
		if (emitterID != null) {
			return descriptorCache.get(emitterID);
		}
		return null;
	}

	/**
	 * Register a custom emitter descriptor manually. It will overwrite the
	 * descriptor with same emitter id if exists.
	 *
	 * @param descriptor
	 */
	@Override
	@Deprecated
	public synchronized void registerEmitterDescriptor(IEmitterDescriptor descriptor) {
		if (descriptor != null && descriptor.getID() != null) {
			descriptorCache.put(descriptor.getID(), descriptor);
		}
	}

	/**
	 * Remove a custom emitter descriptor manually. If there is a descriptor
	 * registered through extension with same emitter id, then this descriptor will
	 * still be returned in following <code>getEmitterDescriptor()</code> call.
	 *
	 * @param descriptor
	 */
	@Override
	@Deprecated
	public synchronized void deregisterEmitterDescriptor(IEmitterDescriptor descriptor) {
		if (descriptor != null && descriptor.getID() != null) {
			IEmitterDescriptor oldDesc = descriptorCache.get(descriptor.getID());

			if (oldDesc == descriptor) {
				descriptorCache.remove(descriptor.getID());
			}
		}
	}
}
