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
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * Test parse MeasureView property.
 *
 */

public class MeasureViewParseTest extends BaseTestCase {

	/**
	 * Test parser
	 *
	 * @throws Exception
	 */

	public void testParse() throws Exception {
		openDesign("MeasureViewParseTest.xml");//$NON-NLS-1$
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.getElementByID(64l);
		MeasureViewHandle measureHandle = (MeasureViewHandle) extendedHandle.getReportItem();
		assertEquals("COUNTRY", measureHandle.getCubeMeasureName());//$NON-NLS-1$
		// detail cell
		assertNotNull(measureHandle.getCell());
		assertNotNull(measureHandle.getHeader());
		assertEquals(1, measureHandle.getAggregationCount());
		assertNotNull(measureHandle.getAggregationCell(0));
	}

	/**
	 * Semantic Check
	 *
	 * @throws Exception
	 */
	public void testSemanticCheck() throws Exception {
		openDesign("MeasureViewParseTest.xml");//$NON-NLS-1$
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

		crosstabItem.insertMeasure(cubeHandle.getMeasure("QUANTITY_PRICE"), -1);//$NON-NLS-1$

		save(designHandle.getRoot());

		compareFile("MeasureViewParseTest_golden.xml");//$NON-NLS-1$
	}

}
