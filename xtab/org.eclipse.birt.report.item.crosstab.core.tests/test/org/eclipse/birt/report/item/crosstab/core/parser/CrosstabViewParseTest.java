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

package org.eclipse.birt.report.item.crosstab.core.parser;

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.BaseTestCase;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;

/**
 * Test parse CrosstabView property.
 *
 */

public class CrosstabViewParseTest extends BaseTestCase {

	/**
	 * Test parser
	 *
	 * @throws Exception
	 */

	public void testParse() throws Exception {
		openDesign("CrosstabViewParseTest.xml");//$NON-NLS-1$

		List errors = designHandle.getErrorList();
		// 1 error: no cube defined for this crosstab
		assertEquals(1, errors.size());

		ExtendedItemHandle handle = (ExtendedItemHandle) designHandle.getBody().get(0);
		CrosstabReportItemHandle reportItemHandle = (CrosstabReportItemHandle) handle.getReportItem();
		CrosstabViewHandle viewHandle = reportItemHandle.getCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE);
		assertNotNull(viewHandle);

		CrosstabCellHandle cellHandle = viewHandle.getGrandTotal();
		assertNotNull(cellHandle);

		PropertyHandle propHandle = viewHandle.getViewsProperty();
		assertNotNull(propHandle.get(0));

		// how to get MemberValue?

	}

	/**
	 * Semantic Check
	 *
	 * @throws Exception
	 */
	public void testSemanticCheck() throws Exception {
		openDesign("CrosstabViewParseTest.xml");//$NON-NLS-1$
		List errors = designHandle.getErrorList();
		// 1 error: no cube defined for this crosstab
		assertEquals(1, errors.size());
	}

	/**
	 * Test Writer
	 *
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		createDesign();
		CubeHandle cubeHandle = prepareCube();
		ExtendedItemHandle extendHandle = CrosstabExtendedItemFactory.createCrosstabReportItem(designHandle.getRoot(),
				cubeHandle, null);
		designHandle.getBody().add(extendHandle);

		// create cross tab
		CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem(extendHandle);

		CrosstabViewHandle viewHandle = crosstabItem.addCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE);
		DimensionHandle dimensionHandle = cubeHandle.getDimension("Customer");//$NON-NLS-1$

		ExtendedItemHandle grandTotal = CrosstabExtendedItemFactory.createCrosstabCell(designHandle.getRoot());
		viewHandle.getGrandTotalProperty().add(grandTotal);

		viewHandle.insertDimension(dimensionHandle, -1);

		save(designHandle.getRoot());

		compareFile("CrosstabViewParseTest_golden.xml");//$NON-NLS-1$
	}
}
