/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.jdbc;

class ConnectionPoolFactory {

	private static IConnectionPoolManager mgr_instance;

	/**
	 * get the connection manager instance
	 *
	 * @return
	 */
	static IConnectionPoolManager getInstance() {
		if (mgr_instance == null) {
			synchronized (ConnectionPoolFactory.class) {
				Class clazz;
				try {
					clazz = Class.forName("org.eclipse.birt.report.data.oda.jdbc.connectionpool.ConnectionPoolManager");
				} catch (ClassNotFoundException e) {
					return null;
				}
				if (IConnectionPoolManager.class.isAssignableFrom(clazz)) {
					try {
						mgr_instance = (IConnectionPoolManager) (clazz.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
					}
				}
			}
		}

		return mgr_instance;
	}
}
