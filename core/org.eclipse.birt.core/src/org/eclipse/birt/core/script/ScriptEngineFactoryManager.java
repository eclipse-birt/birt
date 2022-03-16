/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.core.script;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

abstract public class ScriptEngineFactoryManager {

	protected static Logger logger = Logger.getLogger(ScriptEngineFactoryManager.class.getName());

	static private ScriptEngineFactoryManager instance;

	static public void setInstance(ScriptEngineFactoryManager manager) {
		instance = manager;
	}

	static public ScriptEngineFactoryManager getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (ScriptEngineFactoryManager.class) {
			if (instance == null) {
				try {
					Class clazz = Class.forName("org.eclipse.birt.core.internal.plugin.ScriptEngineFactoryManagerImpl");
					if (clazz != null) {
						instance = (ScriptEngineFactoryManager) clazz.newInstance();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return instance;
		}
	}

	private Map<String, IScriptEngineFactory> factories;

	protected ScriptEngineFactoryManager() {
		factories = new HashMap<>();
	}

	synchronized public IScriptEngineFactory getScriptEngineFactory(String language) {
		if (factories.containsKey(language)) {
			return factories.get(language);
		}
		IScriptEngineFactory factory = createFactory(language);
		factories.put(language, factory);
		return factory;
	}

	protected abstract IScriptEngineFactory createFactory(String language);
}
