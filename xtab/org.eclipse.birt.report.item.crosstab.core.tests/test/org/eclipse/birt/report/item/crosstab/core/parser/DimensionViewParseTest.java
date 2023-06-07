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
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Test parse DimensionView property.
 *
 */

public class DimensionViewParseTest extends BaseTestCase {

	/**
	 * Test parser
	 *
	 * @throws Exception
	 */

	public void testParse() throws Exception {
		openDesign("DimensionViewParseTest.xml");//$NON-NLS-1$

		List errors = designHandle.getErrorList();
		assertEquals(1, errors.size());

		ExtendedItemHandle handle = (ExtendedItemHandle) designHandle.getBody().get(0);
		CrosstabReportItemHandle reportItemHandle = (CrosstabReportItemHandle) handle.getReportItem();
		CrosstabViewHandle viewHandle = reportItemHandle.getCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE);
		assertNotNull(viewHandle);

		PropertyHandle propHandle = viewHandle.getViewsProperty();
		ExtendedItemHandle dimensionExtendedHandle = (ExtendedItemHandle) propHandle.get(0);
		DimensionViewHandle dimensionHandle = (DimensionViewHandle) dimensionExtendedHandle.getReportItem();
		assertNotNull(dimensionHandle.getLevelsProperty().get(0));

		DimensionViewHandle dimensionViewHandle = viewHandle.getDimension(0);
		assertNotNull(dimensionViewHandle);
	}

	/**
	 * Semantic Check
	 *
	 * @throws Exception
	 */
	public void testSemanticCheck() throws Exception {
		openDesign("DimensionViewParseTest.xml");//$NON-NLS-1$
		List errors = designHandle.getErrorList();

		assertEquals(1, errors.size());
	}

	/**
	 * Test Writer
	 *
	 * @throws Exception
	 */

	public void testWriter() throws Exception {
		createDesign();

		designHandle.getModule().getRoot().getNameHelper().getNameSpace("style").getElements().get(0)
				.setProperty("fontFamily", "sans-serif");

		CubeHandle cubeHandle = prepareCube();

		ExtendedItemHandle extendHandle = CrosstabExtendedItemFactory.createCrosstabReportItem(designHandle.getRoot(),
				cubeHandle, null);
		designHandle.getBody().add(extendHandle);

		// create cross tab
		CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem(extendHandle);

		CrosstabViewHandle viewHandle = crosstabItem.addCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE);
		DimensionHandle dimensionHandle = cubeHandle.getDimension("Customer");//$NON-NLS-1$
		DimensionViewHandle dimensionViewHandle = viewHandle.insertDimension(dimensionHandle, -1);

		LevelHandle levelHandle = designHandle.findLevel("Customer/CUSTOMER_SEX");//$NON-NLS-1$
		dimensionViewHandle.insertLevel(levelHandle, -1);

		save(designHandle.getRoot());

		compareFile("DimensionViewParseTest_golden.xml");//$NON-NLS-1$

	}
}
