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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The constant variable in Math.E object doesn't work.
 * 
 * 
 * </p>
 * Test description:
 * <p>
 * 1. Check that this constants Class Object can be correctly retrived by a
 * name.
 * </p>
 * 2. Check that this Class list containing this Class Object.
 */
public class Regression_79140 extends BaseTestCase {

	/**
	 * 
	 */
	public void test_regression_79140() {
		MetaDataDictionary instance = MetaDataDictionary.getInstance();

		// 1. Retrieve by name.

		IClassInfo math = instance.getClass("Math"); //$NON-NLS-1$
		IMemberInfo e = math.getMember("E"); //$NON-NLS-1$
		assertNotNull(e);

		// 2. Class/Method list containing this Class Object.

		List classInfos = instance.getClasses();
		IClassInfo mathCInfo = null;
		for (Iterator iter = classInfos.iterator(); iter.hasNext();) {
			IClassInfo cInfo = (IClassInfo) iter.next();
			if (cInfo.getName().equals("Math")) //$NON-NLS-1$
			{
				mathCInfo = cInfo;
			}
		}

		assertNotNull(mathCInfo);

		IMemberInfo memberInfo = null;
		List methodList = mathCInfo.getMembers();
		for (Iterator iter = methodList.iterator(); iter.hasNext();) {
			IMemberInfo methodInfo = (IMemberInfo) iter.next();
			if (methodInfo.getName().equals("E")) //$NON-NLS-1$
			{
				memberInfo = methodInfo;
			}
		}

		assertNotNull(memberInfo);
		assertEquals("E", memberInfo.getDisplayName()); //$NON-NLS-1$
	}
}
