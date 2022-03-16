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
package org.eclipse.birt.data.engine.binding;

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.junit.Test;

import testutil.ConfigText;

/**
 *
 */
public class DistinctValueTest extends APITestCase {

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Binding.TestData.TableName"),
				ConfigText.getString("Binding.TestData.TableSQL"), "testData3.txt");
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	/**
	 * @throws Exception
	 */
	@Test
	public void testBasic() throws Exception {
		this.dataSet.setDistinctValue(true);
		QueryDefinition queryDefn = newReportQuery();

		// column mapping
		String[] name = { "testColumn1", "testColumn2", "testColumn3", "testColumn4", "testColumn5", "testColumn6", };
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression("dataSetRow.COUNTRY");
		se[1] = new ScriptExpression("dataSetRow.CITY");
		se[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		se[3] = new ScriptExpression("dataSetRow.AMOUNT");
		se[4] = new ScriptExpression("dataSetRow.ORDERED");
		se[5] = new ScriptExpression("dataSetRow.NULL_COLUMN");
		for (int i = 0; i < name.length; i++) {
			queryDefn.addResultSetExpression(name[i], se[i]);
		}

		IResultIterator ri = executeQuery(queryDefn);
		while (ri.next()) {
			String str = "";
			for (int i = 0; i < name.length; i++) {
				str += ri.getValue(name[i]);

				if (i < name.length - 1) {
					str += ", ";
				}
			}
			testPrintln(str);
		}

		checkOutputFile();
	}

	@Test
	public void testQueryDistinct() throws Exception {
		this.dataSet.setDistinctValue(false);
		QueryDefinition queryDefn = newReportQuery();
		queryDefn.setDistinctValue(true);
		// column mapping
		String[] name = { "testColumn1", "testColumn2", "testColumn3", "testColumn4", "testColumn5", "testColumn6", };
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression("dataSetRow.COUNTRY");
		se[1] = new ScriptExpression("dataSetRow.CITY");
		se[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		se[3] = new ScriptExpression("dataSetRow.AMOUNT");
		se[4] = new ScriptExpression("dataSetRow.ORDERED");
		se[5] = new ScriptExpression("dataSetRow.NULL_COLUMN");
		for (int i = 0; i < name.length; i++) {
			queryDefn.addResultSetExpression(name[i], se[i]);
		}
//		SortDefinition sd = new SortDefinition( );
//		sd.setExpression( ExpressionUtil.createJSRowExpression( "testColumn3" ) );
//		queryDefn.addSort( sd );
		IResultIterator ri = executeQuery(queryDefn);
		while (ri.next()) {
			String str = "";
			for (int i = 0; i < name.length; i++) {
				str += ri.getValue(name[i]);

				if (i < name.length - 1) {
					str += ", ";
				}
			}
			testPrintln(str);
		}

		checkOutputFile();
	}

}
