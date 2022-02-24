/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;

public class DefaultConfigLoaderManager {

	private static final String EXTENSION_CONFIG_LOADER_CONTRIBUTOR = "org.eclipse.birt.report.engine.emitter.config.DefaultConfigLoader";

	private static final DefaultConfigLoaderManager instance = new DefaultConfigLoaderManager();

	private List<IDefaultConfigLoader> loaders = new ArrayList<IDefaultConfigLoader>();

	private DefaultConfigLoaderManager() {
		try {
			initLoaders();
		} catch (Exception e) {
		}
	}

	public static DefaultConfigLoaderManager getInstance() {
		return instance;
	}

	public Map<String, RenderOptionDefn> loadConfigFor(String bundleName, IEmitterDescriptor descriptor) {
		Map<String, RenderOptionDefn> renderOptions = new HashMap<String, RenderOptionDefn>();
		for (IDefaultConfigLoader loader : getSortedConfigLoaders()) {
			Map<String, RenderOptionDefn> options = loader.loadConfigFor(bundleName, descriptor);
			for (Entry<String, RenderOptionDefn> option : options.entrySet()) {
				renderOptions.put(option.getKey(), option.getValue());
			}
		}
		return renderOptions;
	}

	private List<IDefaultConfigLoader> getSortedConfigLoaders() {
		return Collections.unmodifiableList(loaders);
	}

	/**
	 * Returns all extension elements.
	 * 
	 * @return all extension elements.
	 * @throws FrameworkException
	 */
	private void initLoaders() throws FrameworkException {

		IExtensionRegistry registry = Platform.getExtensionRegistry();

		if (registry == null) {
			return;
		}

		IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_CONFIG_LOADER_CONTRIBUTOR);

		if (extensionPoint == null) {
			return;
		}

		for (IExtension extension : extensionPoint.getExtensions()) {
			if (extension != null) {
				IConfigurationElement[] elements = extension.getConfigurationElements();

				if (elements != null) {
					for (IConfigurationElement element : elements) {
						if (element != null) {
							IDefaultConfigLoader loader = (IDefaultConfigLoader) element
									.createExecutableExtension("class");
							loaders.add(loader);
						}
					}
				}
			}
		}

		Collections.sort(loaders, new Comparator<IDefaultConfigLoader>() {

			public int compare(IDefaultConfigLoader arg0, IDefaultConfigLoader arg1) {
				return arg0.getPriority() - arg1.getPriority();
			}
		});
	}
}
