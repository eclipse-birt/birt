/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.cache;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.junit.Ignore;
import org.junit.Test;

import testutil.BaseTestCase;

/**
 * Test the feature of disk-based data manipulation. Only used in manual test
 * situation, since it needs the outer data base other than derby embed data
 * base.
 */
@Ignore("Test must be run manually")
public class DiskBasedManualTest extends BaseTestCase {
	// connection property
	private String url = "jdbc:mysql://spmdb/test";
	private String driverClass = "com.mysql.jdbc.Driver";
	private String user = "root";
	private String password = "root";
	private String queryText = "select * from l_customer";

	private OdaDataSourceDesign odaDataSource;
	private OdaDataSetDesign odaDataSet;
	private QueryDefinition queryDefinition;
	private String[] columnNameArray;
	private IBaseExpression[] expressionArray;

	/** JDBC data source and data set info */
	private static final String JDBC_DATA_SOURCE_TYPE = "org.eclipse.birt.report.data.oda.jdbc";
	private static final String JDBC_DATA_SET_TYPE = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet";

	/**
	 * @return IBaseDataSourceDesign
	 * @throws Exception
	 */
	private IBaseDataSourceDesign getDataSource() throws Exception {
		if (odaDataSource != null) {
			return odaDataSource;
		}

		odaDataSource = new OdaDataSourceDesign("Test Data Source");
		odaDataSource.setExtensionID(JDBC_DATA_SOURCE_TYPE);
		odaDataSource.addPublicProperty("odaURL", url);
		odaDataSource.addPublicProperty("odaDriverClass", driverClass);
		odaDataSource.addPublicProperty("odaUser", user);
		odaDataSource.addPublicProperty("odaPassword", password);

		return odaDataSource;
	}

	/**
	 * @return IBaseDataSetDesign
	 * @throws Exception
	 */
	private IBaseDataSetDesign getDataSet() throws Exception {
		if (odaDataSet != null) {
			return odaDataSet;
		}

		odaDataSet = new OdaDataSetDesign("Test Data Set");
		odaDataSet.setDataSource(getDataSource().getName());
		odaDataSet.setExtensionID(JDBC_DATA_SET_TYPE);
		odaDataSet.setQueryText(getQueryText());

		// computed column definition
		ComputedColumn cc = new ComputedColumn("C_CC1", "row.C_CUSTKEY+10", DataType.INTEGER_TYPE);
		odaDataSet.addComputedColumn(cc);

		return odaDataSet;
	}

	/**
	 * @return query defintion with computed column and sort definition
	 * @throws Exception
	 */
	private QueryDefinition getQueryDefn() throws Exception {
		if (queryDefinition != null) {
			return queryDefinition;
		}

		queryDefinition = new QueryDefinition();
		queryDefinition.setDataSetName(getDataSet().getName());

		// add expression based on group defintion
		expressionArray = new IBaseExpression[1];
		columnNameArray = new String[1];

		ScriptExpression expr = new ScriptExpression("dataSetRow.C_CC1");
		expressionArray[0] = expr;
		columnNameArray[0] = "C_CC1";
		for (int i = 0; i < expressionArray.length; i++) {
			queryDefinition.addResultSetExpression(columnNameArray[i], expressionArray[i]);
		}

		// group defintion
//		GroupDefinition gd = new GroupDefinition( );
//		gd.setInterval( IGroupDefinition.NUMERIC_INTERVAL );
//		gd.setKeyColumn( "C_CC1" );
//		queryDefinition.addGroup( gd );

		// sort definition
		SortDefinition sd = new SortDefinition();
		sd.setColumn("C_CC1");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		queryDefinition.addSort(sd);

		return queryDefinition;
	}

	/**
	 * @return query text used in JDBC data set
	 */
	private String getQueryText() {
		int maxRows = 20000;
		if (maxRows > 0) {
			return queryText + " where l_customer.C_CUSTKEY < " + maxRows;
		} else {
			return queryText;
		}
	}

	/**
	 * Test disk based feature
	 *
	 * @throws BirtException
	 * @throws Exception
	 */
	@Test
	public void testDiskBased() throws BirtException, Exception {
		System.setProperty("BIRT_HOME", "./test");
		System.setProperty("PROPERTY_RUN_UNDER_ECLIPSE", "false");
		Platform.startup(null);

		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setTmpdir(this.getTempDir());
		DataEngine de = DataEngine.newDataEngine(context);

		de.defineDataSource(this.getDataSource());
		de.defineDataSet(this.getDataSet());

		IPreparedQuery pq = de.prepare(this.getQueryDefn());
		IQueryResults qr = pq.execute(null);

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			for (int i = 0; i < columnNameArray.length; i++) {
				System.out.println(ri.getValue(columnNameArray[i]));
			}
		}

		ri.close();
		qr.close();
		de.shutdown();
	}

}
