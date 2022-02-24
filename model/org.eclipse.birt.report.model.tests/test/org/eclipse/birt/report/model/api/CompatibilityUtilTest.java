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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.CompatibilityUtil;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for CompatibiltyUtil.
 */

public class CompatibilityUtilTest extends BaseTestCase {

	/**
	 * Test cases to add result set columns to cached metadata.
	 * 
	 * @throws Exception
	 */

	public void testUpdateCachedMetaDataResultSet() throws Exception {
		createDesign();

		ScriptDataSetHandle setHandle = designHandle.getElementFactory().newScriptDataSet("dataset1"); //$NON-NLS-1$

		// no cached meta data.

		List columns = createResultColumns();

		CompatibilityUtil.updateResultSetinCachedMetaData(setHandle, columns);

		CachedMetaDataHandle cachedMeta = setHandle.getCachedMetaDataHandle();
		assertNotNull(cachedMeta);

		List newColumns = (List) cachedMeta.getResultSet().getValue();
		assertEquals(2, newColumns.size());

		// try failed cases.

		columns.clear();

		columns.add(StructureFactory.createColumnHint());
		try {
			CompatibilityUtil.updateResultSetinCachedMetaData(setHandle, columns);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE, e.getErrorCode());
		}

		columns.clear();

		columns.add(StructureFactory.createResultSetColumn());
		try {
			CompatibilityUtil.updateResultSetinCachedMetaData(setHandle, columns);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}

		// has cached metadata already.

		cachedMeta.setProperty(CachedMetaData.RESULT_SET_MEMBER, null);

		columns = createResultColumns();
		CompatibilityUtil.updateResultSetinCachedMetaData(setHandle, columns);

		newColumns = (List) cachedMeta.getResultSet().getValue();
		assertEquals(2, newColumns.size());

	}

	private List createResultColumns() {
		List columns = new ArrayList();
		ResultSetColumn column = StructureFactory.createResultSetColumn();
		column.setColumnName("column1");
		column.setPosition(new Integer(1));
		columns.add(column);

		column = StructureFactory.createResultSetColumn();
		column.setColumnName("column2");
		column.setPosition(new Integer(2));
		columns.add(column);

		return columns;
	}

	/**
	 * Tests addResultSetColumn method.
	 * 
	 * @throws Exception
	 */

	public void testAddResultSetColumn() throws Exception {
		createDesign();

		OdaDataSetHandle dsHandle = designHandle.getElementFactory().newOdaDataSet("newDataSet", null); //$NON-NLS-1$
		designHandle.getDataSets().add(dsHandle);

		OdaResultSetColumn rsColumn = new OdaResultSetColumn();

		rsColumn.setColumnName("columnName");//$NON-NLS-1$
		rsColumn.setDataType("string");//$NON-NLS-1$
		rsColumn.setPosition(new Integer(1));

		List list = new ArrayList();
		list.add(rsColumn);

		MockListener listener = new MockListener();
		dsHandle.addListener(listener);

		MockListener listener2 = new MockListener();
		designHandle.addListener(listener2);

		CompatibilityUtil.addResultSetColumn(dsHandle, list);

		assertEquals(0, listener.getCount());
		assertEquals(0, listener2.getCount());

		PropertyHandle resultSetColumnHandle = dsHandle.getPropertyHandle(DataSetHandle.RESULT_SET_PROP);

		OdaResultSetColumnHandle setHandle = (OdaResultSetColumnHandle) resultSetColumnHandle.getAt(0);
		assertEquals("columnName", setHandle.getColumnName());//$NON-NLS-1$
		assertEquals("string", setHandle.getDataType());//$NON-NLS-1$
		assertEquals(1, setHandle.getPosition().intValue());
	}

	/**
	 * Mock listener
	 * 
	 */

	private class MockListener implements Listener {

		private int count = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.api.core.Listener#elementChanged(org.eclipse.
		 * birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			++count;
		}

		/**
		 * Gets event count.
		 * 
		 * @return event count.
		 */

		public int getCount() {
			return count;
		}

	}
}
