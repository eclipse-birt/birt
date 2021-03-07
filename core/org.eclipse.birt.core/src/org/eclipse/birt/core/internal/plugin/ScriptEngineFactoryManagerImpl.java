/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.internal.plugin;

import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.core.script.IScriptEngineFactory;
import org.eclipse.birt.core.script.ScriptEngineFactoryManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ScriptEngineFactoryManagerImpl extends ScriptEngineFactoryManager {

	HashMap<String, IConfigurationElement> configs;

	public ScriptEngineFactoryManagerImpl() {
		configs = new HashMap<>();
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint("org.eclipse.birt.core.ScriptEngineFactory");

		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension extension : extensions) {
			IConfigurationElement[] configurations = extension.getConfigurationElements();
			for (IConfigurationElement configuration : configurations) {
				String scriptName = configuration.getAttribute("scriptName");
				configs.put(scriptName, configuration);
			}
		}
	}

	@Override
	protected IScriptEngineFactory createFactory(String language) {
		if (configs.containsKey(language)) {
			IConfigurationElement configuration = configs.get(language);
			try {
				Object object = configuration.createExecutableExtension("factoryClass");
				if (object instanceof IScriptEngineFactory) {
					return (IScriptEngineFactory) object;
				}
			} catch (CoreException e) {
				logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return null;
	}
}
