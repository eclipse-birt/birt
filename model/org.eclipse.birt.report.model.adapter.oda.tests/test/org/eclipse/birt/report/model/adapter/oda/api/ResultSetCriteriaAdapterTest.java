/*******************************************************************************
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.model.adapter.oda.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.adapter.oda.impl.ResultSetCriteriaAdapter;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.SortHintHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.NullOrderingType;
import org.eclipse.datatools.connectivity.oda.design.ResultSetCriteria;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.SortDirectionType;
import org.eclipse.datatools.connectivity.oda.design.SortKey;
import org.eclipse.datatools.connectivity.oda.design.SortSpecification;
import org.eclipse.emf.common.util.EList;

/**
 * Test cases for FilterAdapter
 */

public class ResultSetCriteriaAdapterTest extends BaseTestCase {

	/**
	 * Creates a blank oda data set design
	 */

	private DataSetDesign createDataSetDesign() {
		DataSetDesign setDesign = DesignFactory.eINSTANCE.createDataSetDesign();
		setDesign.setOdaExtensionDataSetId("org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"); //$NON-NLS-1$
		DataSourceDesign dataSource = DesignFactory.eINSTANCE.createDataSourceDesign();
		dataSource.setOdaExtensionDataSourceId("org.eclipse.birt.report.data.oda.jdbc"); //$NON-NLS-1$
		setDesign.setDataSourceDesign(dataSource);
		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition();

		// no exception in conversion; go ahead and assign to specified
		// dataSetDesign
		setDesign.setPrimaryResultSet(resultSetDefn);
		return setDesign;
	}

	/**
	 * Tests convert sort hint from report to oda.
	 *
	 * @throws Exception
	 */
	public void testSortHintFromReportToOda() throws Exception {
		openDesign("SortHintTest.xml"); //$NON-NLS-1$

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("MyDataSet"); //$NON-NLS-1$
		DataSetDesign setDesign = createDataSetDesign();
		ResultSetCriteriaAdapter adapter = createAdapter(setHandle, setDesign);
		adapter.updateODAResultSetCriteria();

		ResultSetDefinition resultSet = setDesign.getPrimaryResultSet();
		EList<SortKey> list = resultSet.getCriteria().getRowOrdering().getSortKeys();

		assertEquals(3, list.size());

		SortKey key = list.get(0);
		assertEquals("sortHint1", key.getColumnName()); //$NON-NLS-1$
		assertEquals(1, key.getColumnPosition());
		assertEquals(SortDirectionType.DESCENDING, key.getSortDirection());
		assertEquals(NullOrderingType.NULLS_FIRST, key.getNullValueOrdering());
		assertTrue(key.isOptional());

		key = list.get(1);
		assertEquals("sortHint2", key.getColumnName()); //$NON-NLS-1$
		assertEquals(2, key.getColumnPosition());
		assertEquals(SortDirectionType.ASCENDING, key.getSortDirection());
		assertEquals(NullOrderingType.NULLS_FIRST, key.getNullValueOrdering());
		assertFalse(key.isOptional());

		key = list.get(2);
		assertEquals("sortHint3", key.getColumnName()); //$NON-NLS-1$
		assertEquals(3, key.getColumnPosition());
		assertEquals(SortDirectionType.DESCENDING, key.getSortDirection());
		assertEquals(NullOrderingType.NULLS_LAST, key.getNullValueOrdering());
		assertTrue(key.isOptional());

		DesignValues values = ModelFactory.eINSTANCE.createDesignValues();
		values.setResultSets(setDesign.getResultSets());
		saveDesignValuesToFile(values);

		assertTrue(compareTextFile("SortHintFromReportToOdaTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests convert sort hint from report to oda.
	 *
	 * @throws Exception
	 */
	public void testEmptySortHintFromReportToOda() throws Exception {

		openDesign("EmptySortHintTest.xml"); //$NON-NLS-1$

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("MyDataSet"); //$NON-NLS-1$
		DataSetDesign setDesign = createDataSetDesign();

		assertNull(setDesign.getPrimaryResultSet().getCriteria().getRowOrdering());

		ResultSetCriteriaAdapter adapter = createAdapter(setHandle, setDesign);
		adapter.updateODAResultSetCriteria();

		ResultSetDefinition resultSet = setDesign.getPrimaryResultSet();

		// if an Oda data set has no BIRT sort hints, the Adapter should create
		// an empty SortSpecification.
		assertNotNull(resultSet.getCriteria().getRowOrdering());
	}

	/**
	 * Tests convert sort hint from oda to report.
	 *
	 * @throws Exception
	 */
	public void testSortHintFromOdaToReport() throws Exception {
		openDesign("SortHintTest.xml"); //$NON-NLS-1$

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("MyDataSet"); //$NON-NLS-1$
		DataSetDesign setDesign = createTestSortHintDataSetDesign(false);
		ResultSetCriteriaAdapter adapter = createAdapter(setHandle, setDesign);
		adapter.updateROMSortAndFilter();

		Iterator iter = setHandle.sortHintsIterator();

		SortHintHandle handle = (SortHintHandle) iter.next();

		assertEquals("1", handle.getColumnName()); //$NON-NLS-1$
		assertEquals(1, handle.getPosition());
		assertEquals(DesignChoiceConstants.NULL_VALUE_ORDERING_TYPE_NULLISFIRST, handle.getNullValueOrdering());
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_ASC, handle.getDirection());
		assertFalse(handle.isOptional());

		handle = (SortHintHandle) iter.next();

		assertEquals("2", handle.getColumnName()); //$NON-NLS-1$
		assertEquals(2, handle.getPosition());
		assertEquals(DesignChoiceConstants.NULL_VALUE_ORDERING_TYPE_NULLISLAST, handle.getNullValueOrdering());
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_DESC, handle.getDirection());
		assertTrue(handle.isOptional());

		save();
		assertTrue(compareTextFile("SortHintFromOdaToReportTest_golden.xml")); //$NON-NLS-1$

		adapter = createAdapter(setHandle, createTestSortHintDataSetDesign(true));

		// Doing nothing expected
		adapter.updateROMSortAndFilter();

		save();
		assertTrue(compareTextFile("SortHintFromOdaToReportTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Creates data set design with sort hint.
	 *
	 * @param sortSpecIsNull Indicates if the sort specification is null
	 * @return data set design.
	 */
	private DataSetDesign createTestSortHintDataSetDesign(boolean sortSpecIsNull) {
		DataSetDesign setDesign = createDataSetDesign();

		if (sortSpecIsNull) {
			return setDesign;
		}

		ResultSetDefinition resultSetDefn = setDesign.getPrimaryResultSet();

		ResultSetCriteria criteria = resultSetDefn.getCriteria();

		SortSpecification sortSpec = criteria.getRowOrdering();

		if (sortSpec == null) {
			sortSpec = DesignFactory.eINSTANCE.createSortSpecification();
			criteria.setRowOrdering(sortSpec);
		}

		EList<SortKey> list = sortSpec.getSortKeys();

		SortKey key = DesignFactory.eINSTANCE.createSortKey();

		key.setColumnName("1"); //$NON-NLS-1$
		key.setColumnPosition(1);
		key.setNullValueOrdering(NullOrderingType.NULLS_FIRST);
		key.setOptional(false);
		key.setSortDirection(SortDirectionType.ASCENDING);

		list.add(key);

		key = DesignFactory.eINSTANCE.createSortKey();

		key.setColumnName("2"); //$NON-NLS-1$
		key.setColumnPosition(2);
		key.setNullValueOrdering(NullOrderingType.NULLS_LAST);
		key.setOptional(true);
		key.setSortDirection(SortDirectionType.DESCENDING);

		list.add(key);

		return setDesign;
	}

	private ResultSetCriteriaAdapter createAdapter(OdaDataSetHandle setHandle, DataSetDesign setDesign) {
		return new ResultSetCriteriaAdapter(setHandle, setDesign, setDesign.getOdaExtensionDataSetId(), true, true);
	}
}
