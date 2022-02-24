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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.core.framework.PlatformLauncher;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.spi.IRegistryProvider;

public class ServiceLauncher extends PlatformLauncher {

	static final String MANIFEST_ENTRY = "META-INF/MANIFEST.MF";

	static Logger logger = Logger.getLogger(Platform.class.getName());

	private ServicePlatform platform;

	public ServiceLauncher() {
	}

	public void startup(final PlatformConfig config) throws FrameworkException {
		platform = new ServicePlatform(config);

		try {
			Enumeration<URL> plugins = ServiceLauncher.class.getClassLoader().getResources(MANIFEST_ENTRY);

			while (plugins.hasMoreElements()) {
				// the wsjar:// URL in websphere doesn't support .. to get the
				// parent folder, so we construct the root from the file path
				URL root = null;
				URL url = plugins.nextElement();
				String path = url.toExternalForm();
				if (path.endsWith(MANIFEST_ENTRY)) {
					String rootPath = path.substring(0, path.length() - MANIFEST_ENTRY.length());
					root = new URL(url, rootPath);
				} else {
					root = new URL(url, "..");
				}
				try {
					platform.installBundle(root);
				} catch (Exception ex) {
					logger.log(Level.WARNING, "Failed to install plugin from " + root, ex);
				}
			}
			platform.startup();

			Platform.setPlatform(platform);

			RegistryFactory.setDefaultRegistryProvider(new IRegistryProvider() {

				public IExtensionRegistry getRegistry() {
					return platform.extensionRegistry;
				}
			});
		} catch (IOException ex) {
			throw new FrameworkException("Can't find any bundle from the classpath", ex);
		} catch (CoreException ex) {
			throw new FrameworkException("Can't register the ExtensionRegistry classpath", ex);
		}

	}

	public void shutdown() {
		Platform.setPlatform(null);
		if (platform != null) {
			platform.shutdown();
			platform = null;
			RegistryProviderFactory.releaseDefault();
		}
	}
}
