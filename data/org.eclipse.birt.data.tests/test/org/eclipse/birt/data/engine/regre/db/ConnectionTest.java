/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.regre.db;

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import testutil.ConfigText;

/**
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
abstract public class ConnectionTest extends APITestCase {

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void connectionSetUp() throws Exception {
		System.setProperty("DTETest.otherDB", "true");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void connectionTearDown() throws Exception {
		System.setProperty("DTETest.otherDB", "false");
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Regre.ConnectTest.TableName"), null, null);
	}

	/*
	 * An Empyt ReportQueryDefn
	 */
	@Test
	public void testConnection() throws Exception {
		IBaseExpression[] expressions = { new ScriptExpression("dataSetRow.CLIENTID", 0),
				new ScriptExpression("dataSetRow.CITY", 0), new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.EMAIL", 0) };
		createAndRunQuery(expressions);
	}

	private void createAndRunQuery(IBaseExpression[] expressions) throws Exception {
		String[] names = { "_CLIENTID", "_CITY", "_COUNTRY", "_EMAIL" };

		QueryDefinition queryDefn = newReportQuery();
		if (expressions != null) {
			for (int i = 0; i < expressions.length; i++) {
				queryDefn.addResultSetExpression(names[i], expressions[i]);
			}
		}

		outputQueryResult(executeQuery(queryDefn), names);
	}
}
