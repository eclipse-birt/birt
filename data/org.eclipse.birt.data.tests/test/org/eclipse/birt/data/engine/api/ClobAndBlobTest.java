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
package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.Test;

import testutil.ConfigText;

/**
 *
 */
public class ClobAndBlobTest extends APITestCase {
	// expression array
	private String[] beName;
	private IBaseExpression[] beArray;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestBlobAndClob.TableName"),
				ConfigText.getString("Api.TestBlobAndClob.TableSQL"),
				ConfigText.getString("Api.TestBlobAndClob.TestDataFileName"));
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testClobAndBlob() throws Exception {
		QueryDefinition queryDefn = newReportQuery();
		prepareExpression(queryDefn);

		IResultIterator ri = executeQuery(queryDefn);
		IResultMetaData md = ri.getResultMetaData();

		while (ri.next()) {
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < beArray.length; i++) {
				if (md.getColumnTypeName(i + 1).equals(DataType.BINARY_TYPE_NAME)
						|| md.getColumnTypeName(i + 1).equals(DataType.BLOB_TYPE_NAME)) {
					str.append(ri.getBytes(beName[i]));
				} else {
					str.append(ri.getValue(beName[i]));
				}

				if (i < beArray.length - 1) {
					str.append(", ");
				}
			}
			testPrintln(str.toString());
		}

		checkOutputFile();
	}

	/**
	 * Add expression to query definition
	 *
	 * @param queryDefn
	 * @throws DataException
	 */
	private void prepareExpression(QueryDefinition queryDefn) throws DataException {
		beName = new String[3];
		beArray = new ScriptExpression[3];

		beName[0] = "_ID";
		ScriptExpression se = new ScriptExpression("dataSetRow.ID");
		se.setDataType(DataType.INTEGER_TYPE);
		beArray[0] = se;

		beName[1] = "_NAME";
		se = new ScriptExpression("dataSetRow.NAME");
		se.setDataType(DataType.STRING_TYPE);
		beArray[1] = se;

		beName[2] = "_INFO";
		se = new ScriptExpression("dataSetRow.INFO");
		se.setDataType(DataType.BLOB_TYPE);
		beArray[2] = se;

		for (int i = 0; i < beName.length; i++) {
			queryDefn.addBinding(new Binding(beName[i], beArray[i]));
		}
	}

}
