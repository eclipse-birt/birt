/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.5 $ $Date: 2007/02/01 10:58:57 $
 */

public class ConnectionMetaDataManager implements Serializable {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -5760267960286269980L;

	private ArrayList metaDatas = new ArrayList();

	private static ConnectionMetaDataManager manager = new ConnectionMetaDataManager();

	/**
	 *  
	 */
	private ConnectionMetaDataManager() {
		super();
	}

	public static final ConnectionMetaDataManager getInstance() {
		return manager;
	}

	public ConnectionMetaData getMetaData(String classname, String url, String username, String password,
			Properties properties, long timeout) {
		// construct a new meta data instance
		ConnectionMetaData metaData = new ConnectionMetaData();
		metaData.setClassname(classname);
		metaData.setUrl(url);
		metaData.setUsername(username);
		metaData.setPassword(password);
		metaData.setProperties(properties);
		metaData.setTimeout(timeout);

		// Iterate through the list and find out whether this meta data object
		// exists
		Iterator iter = metaDatas.iterator();
		while (iter.hasNext()) {
			ConnectionMetaData data = (ConnectionMetaData) iter.next();
			if (data.equals(metaData)) {
				return data;
			}
		}

		// If we are here then this is a new item
		// add it in
		metaDatas.add(metaData);

		return metaData;
	}

	public void clearCache() {
		// dispose all the meta data instances
		Iterator iter = metaDatas.iterator();
		while (iter.hasNext()) {
			ConnectionMetaData data = (ConnectionMetaData) iter.next();
			data.clearCache();
		}

		metaDatas.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		clearCache();
		super.finalize();
	}
}
