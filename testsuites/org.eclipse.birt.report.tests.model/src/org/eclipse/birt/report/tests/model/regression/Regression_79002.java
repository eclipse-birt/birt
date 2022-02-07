/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Spelling error in Global->underfined, Expected result: "undefined" Actual
 * result: "underfined"
 * </p>
 * Test description:
 * <p>
 * Assert that the correct script object can be retrived from Model rom.
 * </p>
 */
public class Regression_79002 extends BaseTestCase {

	/**
	 * 
	 */
	public void test_regression_79002() {
		IMetaDataDictionary meta = MetaDataDictionary.getInstance();
		IClassInfo classInfo = meta.getClass("Global"); //$NON-NLS-1$

		assertNotNull(classInfo);

		IMemberInfo memInfo = classInfo.getMember("undefined"); //$NON-NLS-1$
		assertNotNull(memInfo);

		IMemberInfo errorMemInfo = classInfo.getMember("underfined"); //$NON-NLS-1$
		assertNull(errorMemInfo);
	}
}
