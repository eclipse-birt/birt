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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test CompatibleIntervalState class
 *
 */

public class CompatibleIntervalStateTest extends BaseTestCase {

	/**
	 * Test value of interval property in level element.
	 *
	 * @throws Exception
	 */

	public void testIntervalValue() throws Exception {
		openDesign("CompatibleIntervalState.xml"); //$NON-NLS-1$
		CubeHandle cubeHandle = (CubeHandle) designHandle.findCube("Customer Cube");//$NON-NLS-1$
		assertNotNull(cubeHandle);

		LevelHandle levelHandle = (LevelHandle) designHandle.findLevel("Group1/OFFICECODE");//$NON-NLS-1$
		assertNotNull(levelHandle);

		assertEquals(DesignChoiceConstants.INTERVAL_TYPE_NONE, levelHandle.getInterval());
	}
}
