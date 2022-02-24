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
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test CompatibleDateTimeLevelTypeState.
 * 
 */

public class CompatibleDateTimeLevelTypeStateTest extends BaseTestCase {

	/**
	 * Test change 'day' to 'day-of-year' and 'week' to 'week-of-year'.
	 * 
	 * @throws Exception
	 */

	public void testTransferDateTimeLevelTypeState() throws Exception {
		openDesign("CompatibleDateTimeLevelTypeStateTest.xml"); //$NON-NLS-1$
		LevelHandle dayLevel = designHandle.findLevel("DATE/Day");//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR, dayLevel.getDateTimeLevelType());
		LevelHandle weekLevel = designHandle.findLevel("DATE/Week");//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR, weekLevel.getDateTimeLevelType());
	}
}
