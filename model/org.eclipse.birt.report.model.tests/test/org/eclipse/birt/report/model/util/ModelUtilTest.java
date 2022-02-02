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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;

/**
 * Test cases for ModelUtil
 * 
 */

public class ModelUtilTest extends BaseTestCase {

	/**
	 * Test whether the output element handles are sorted.
	 */

	public void testSortElementsByName() {
		createDesign();
		ElementFactory factory = new ElementFactory(designHandle.getModule());
		DataSetHandle ds1 = factory.newScriptDataSet("b"); //$NON-NLS-1$
		DataSetHandle ds2 = factory.newScriptDataSet("a"); //$NON-NLS-1$
		DataSetHandle ds3 = factory.newScriptDataSet("c"); //$NON-NLS-1$

		List elementList = new ArrayList();
		elementList.add(ds1);
		elementList.add(ds2);
		elementList.add(ds3);

		List sortedList = ModelUtil.sortElementsByName(elementList);
		assertEquals("a", ((DesignElementHandle) sortedList.get(0)).getName()); //$NON-NLS-1$
		assertEquals("b", ((DesignElementHandle) sortedList.get(1)).getName()); //$NON-NLS-1$
		assertEquals("c", ((DesignElementHandle) sortedList.get(2)).getName()); //$NON-NLS-1$
	}
}
