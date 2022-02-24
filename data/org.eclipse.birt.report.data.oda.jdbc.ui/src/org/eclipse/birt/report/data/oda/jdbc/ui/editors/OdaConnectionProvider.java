/*
 *************************************************************************
 * Copyright (c) 2008, 2011 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;

public class OdaConnectionProvider {
	private DataSourceDesign dataSourceDesign;
	private IConnection connection;

	OdaConnectionProvider(DataSourceDesign dataSourceDesign) {
		this.dataSourceDesign = dataSourceDesign;
	}

	IConnection openConnection() throws OdaException {
		if (connection != null) {
			return connection;
		}
		IDriver jdbcDriver = JDBCDriverManager.getInstance().getDriver(dataSourceDesign.getEffectiveOdaExtensionId());
		try {
			connection = jdbcDriver.getConnection(dataSourceDesign.getEffectiveOdaExtensionId());

			Map appContext = new HashMap();
			ResourceIdentifiers resourceIdentifiers = dataSourceDesign.getHostResourceIdentifiers();
			if (resourceIdentifiers != null) {
				appContext.put(
						org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS,
						DesignSessionUtil.createRuntimeResourceIdentifiers(resourceIdentifiers));
			}
			connection.setAppContext(appContext);

			Properties prop = DesignSessionUtil.getEffectiveDataSourceProperties(dataSourceDesign);
			connection.open(prop);
		} catch (OdaException e) {
			connection = null;
			throw e;
		}
		return connection;
	}

	void release() {
		if (connection != null) {
			try {
				connection.close();
			} catch (OdaException e) {

			} finally {
				connection = null;
			}
		}
	}

}
