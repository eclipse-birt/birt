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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import testutil.ConfigText;

/**
 * Util class, provide following utility: 1: dump info of ResultMetaData 2:
 * provide a helper class for CachedResultSet In odi layer, it is not easy to
 * construct an instance of CachedResultSet to do test, but easy in Impl Layer.
 * So this helper class is placed here.
 */
public class Util {
	public static String getMetaDadataInfo(IResultMetaData meta) throws BirtException {
		StringBuffer stringBuff = new StringBuffer();

		if (meta == null || meta.getColumnCount() == 0) {
			stringBuff.append("null");
			stringBuff.append("\n");
		} else {
			int count = meta.getColumnCount();
			stringBuff.append("Index\tName\tAlias\tLabel\tType\tTypeName");
			stringBuff.append("\n");
			for (int i = 1; i <= count; i++) {
				stringBuff.append(Integer.toString(i));
				stringBuff.append(":\t");
				try {
					stringBuff.append(meta.getColumnName(i));
				} catch (DataException e) {
					stringBuff.append("<Exception>");
				}

				stringBuff.append(",\t");
				try {
					stringBuff.append(meta.getColumnAlias(i));
				} catch (DataException e) {
					stringBuff.append("<Exception>");
				}

				stringBuff.append(",\t");
				try {
					stringBuff.append(meta.getColumnLabel(i));
				} catch (DataException e) {
					stringBuff.append("<Exception>");
				}

				stringBuff.append(",\t");
				try {
					stringBuff.append(Integer.toString((meta.getColumnType(i))));
				} catch (DataException e) {
					stringBuff.append("<Exception>");
				}

				stringBuff.append(",\t");
				try {
					stringBuff.append(meta.getColumnTypeName(i));
				} catch (DataException e) {
					stringBuff.append("<Exception>");
				}

				stringBuff.append("");
				stringBuff.append("\n");
			}
		}

		return new String(stringBuff);
	}

	/**
	 * Helper class for CachedResultSet in executor test package
	 *
	 */
	public static class CachedResultSetTestHelper extends APITestCase {

		/*
		 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
		 */
		protected DataSourceInfo getDataSourceInfo() {
			return new DataSourceInfo(ConfigText.getString("Executor.TestData1.TableName"),
					ConfigText.getString("Executor.TestData1.TableSQL"),
					ConfigText.getString("Executor.TestData1.TestDataFileName"));
		}

		public CachedResultSet getDefaultCachedResultSet() throws Exception {
			IQueryDefinition queryDefn = getDefaultQueryDefn(this.dataSet.getName());
			ResultIterator resultIterator = (ResultIterator) executeQuery(queryDefn);
			return (CachedResultSet) resultIterator.getOdiResult();
		}

		public CachedResultSet getDefaultSubQueryCachedResultSet() throws Exception {
			IQueryDefinition queryDefn = getDefaultQueryDefnWithSubQuery(this.dataSet.getName());
			ResultIterator resultIterator = (ResultIterator) executeQuery(queryDefn);

			resultIterator.next();
			Context cx = Context.enter();
			ResultIterator subIterator;
			try {
				ScriptableObject scope = cx.initStandardObjects();
				subIterator = (ResultIterator) resultIterator.getSecondaryIterator("IAMTEST", //$NON-NLS-1$
						scope);
			} catch (DataException e) {
				throw (e);
			} finally {
				Context.exit();
			}

			return (CachedResultSet) subIterator.getOdiResult();
		}
	}

}
