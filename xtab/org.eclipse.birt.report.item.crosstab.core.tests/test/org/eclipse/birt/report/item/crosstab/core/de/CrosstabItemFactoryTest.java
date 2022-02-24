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

package org.eclipse.birt.report.item.crosstab.core.de;

import org.eclipse.birt.report.item.crosstab.core.BaseTestCase;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;

/**
 * Test <code>CrosstabItemFactory</code>
 * 
 */
public class CrosstabItemFactoryTest extends BaseTestCase {

	/**
	 * Test newReportItem method.
	 * 
	 * @throws Exception
	 */
	public void testNewReportItem() throws Exception {
		createDesign();

		IReportItem item = newReportItem(ICrosstabConstants.CROSSTAB_EXTENSION_NAME, designHandle);
		assertTrue(item instanceof CrosstabReportItemHandle);

		item = newReportItem(ICrosstabConstants.CROSSTAB_VIEW_EXTENSION_NAME, designHandle);
		assertTrue(item instanceof CrosstabViewHandle);

		item = newReportItem(ICrosstabConstants.DIMENSION_VIEW_EXTENSION_NAME, designHandle);
		assertTrue(item instanceof DimensionViewHandle);

		item = newReportItem(ICrosstabConstants.LEVEL_VIEW_EXTENSION_NAME, designHandle);
		assertTrue(item instanceof LevelViewHandle);

		item = newReportItem(ICrosstabConstants.MEASURE_VIEW_EXTENSION_NAME, designHandle);
		assertTrue(item instanceof MeasureViewHandle);

		item = newReportItem(ICrosstabConstants.CROSSTAB_CELL_EXTENSION_NAME, designHandle);
		assertTrue(item instanceof CrosstabCellHandle);

		item = newReportItem(ICrosstabConstants.AGGREGATION_CELL_EXTENSION_NAME, designHandle);
		assertTrue(item instanceof AggregationCellHandle);

		item = newReportItem("notExistExtendsionName", designHandle);//$NON-NLS-1$
		assertNull(item);
	}

	/**
	 * Create report item through element factory.
	 * 
	 * @param extensionName
	 * @param designHandle
	 * @return
	 */

	private IReportItem newReportItem(String extensionName, ReportDesignHandle designHandle) {
		ExtendedItem extendedItem = new ExtendedItem();
		extendedItem.setProperty(IExtendedItemModel.EXTENSION_NAME_PROP, extensionName);

		ExtendedItemHandle extendedItemHandle = (ExtendedItemHandle) extendedItem.getHandle(designHandle.getModule());
		CrosstabItemFactory factory = new CrosstabItemFactory();
		IReportItem item = factory.newReportItem(extendedItemHandle);
		return item;
	}
}
