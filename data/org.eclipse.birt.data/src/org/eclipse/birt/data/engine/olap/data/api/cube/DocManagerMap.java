
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.api.cube;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;

/**
 * This class is used to maps data engine key to documentManager.
 */

public class DocManagerMap {
	private static DocManagerMap instance = new DocManagerMap();

	private ThreadLocal<Map> tmap = new ThreadLocal<>() {

		@Override
		protected Map initialValue() {
			return new HashMap();
		}

	};

	protected static Logger logger = Logger.getLogger(DocManagerMap.class.getName());

	/**
	 *
	 * @return
	 */
	public static DocManagerMap getDocManagerMap() {
		return instance;
	}

	/**
	 *
	 * @param key
	 * @param manager
	 */
	public void set(String dataEngineKey, String key, IDocumentManager manager) {
		Map docManagerMap = null;
		Map map = tmap.get();
		if (map.containsKey(dataEngineKey)) {
			docManagerMap = (Map) map.get(dataEngineKey);
		} else {
			docManagerMap = new HashMap();
			map.put(dataEngineKey, docManagerMap);
		}
		docManagerMap.put(key, manager);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public IDocumentManager get(String dataEngineKey, String key) {
		Map docManagerMap;
		Map map = tmap.get();
		if (!map.containsKey(dataEngineKey)) {
			return null;
		}
		docManagerMap = (Map) map.get(dataEngineKey);
		if (!docManagerMap.containsKey(key)) {
			return null;
		}
		return (IDocumentManager) docManagerMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IShutdownListener#dataEngineShutdown(java.
	 * lang.String)
	 */
	public void close(String dataEngineKey) {
		Map docManagerMap;
		Map map = tmap.get();
		if (!map.containsKey(dataEngineKey)) {
			return;
		}
		docManagerMap = (Map) map.get(dataEngineKey);
		Collection docManagers = docManagerMap.values();
		if (docManagers == null || docManagers.size() == 0) {
			return;
		}
		Iterator docManagerIterator = docManagers.iterator();
		while (docManagerIterator.hasNext()) {
			try {
				((IDocumentManager) docManagerIterator.next()).close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.log(Level.WARNING, "IOException is thrown when document manage is closed!");
			}
		}
		docManagerMap.clear();
		map.remove(dataEngineKey);
		tmap.remove();
	}

}
