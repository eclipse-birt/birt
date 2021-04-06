/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package testutil;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;

/**
 * Used to create Oda.Jdbc datasource and dataset
 */
public class JDBCOdaDataSource {
	private OdaDataSourceDesign jdbDataSource;
	private OdaDataSetDesign jdbcDataSet;

	public static final String DATA_SOURCE_TYPE = "org.eclipse.birt.report.data.oda.jdbc";
	public static final String DATA_SET_TYPE = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet";

	public static final String SP_DATA_SET_TYPE = "org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet";

	/**
	 * Oda.Jdbc data source needs following information to create an instance
	 * 
	 * @param url
	 * @param driverClass
	 * @param user
	 * @param password
	 * @throws BirtException
	 */
	JDBCOdaDataSource(String url, String driverClass, String user, String password) throws BirtException {
		jdbDataSource = new OdaDataSourceDesign("Test Data Source");
		jdbDataSource.setExtensionID(DATA_SOURCE_TYPE);
		jdbDataSource.addPublicProperty("odaURL", url);
		jdbDataSource.addPublicProperty("odaDriverClass", driverClass);
		jdbDataSource.addPublicProperty("odaUser", user);
		jdbDataSource.addPublicProperty("odaPassword", password);

		jdbcDataSet = new OdaDataSetDesign("Test Data Set");
		jdbcDataSet.setDataSource(jdbDataSource.getName());
		jdbcDataSet.setExtensionID(DATA_SET_TYPE);
	}

	/**
	 * Get OdaDataSourceDesign
	 * 
	 * @return dataSource
	 */
	public OdaDataSourceDesign getOdaDataSourceDesign() {
		return jdbDataSource;
	}

	/**
	 * Get OdaDataSetDesign
	 * 
	 * @return dataSet
	 */
	public OdaDataSetDesign getOdaDataSetDesign() {
		return jdbcDataSet;
	}

}
