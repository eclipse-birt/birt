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

package org.eclipse.birt.report.tests.model.regression;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * The method String.fromCharCode() is not listed in expressin builder
 * </p>
 * Test description:
 * <p>
 * 1. Check that this constants Class method can be correctly retrived by a
 * name.
 * </p>
 * 2. Check that this Class list containing this method.
 */
public class Regression_78923 extends BaseTestCase {

	/**
	 *
	 */
	public void test_regression_78923() {
		MetaDataDictionary instance = MetaDataDictionary.getInstance();

		// Retrieve by name

		IClassInfo string = instance.getClass("String"); //$NON-NLS-1$
		IMethodInfo method = string.getMethod("fromCharCode"); //$NON-NLS-1$
		assertNotNull(method);

		// Class/Method list containing this Class method

		List classInfos = instance.getClasses();
		IClassInfo stringCInfo = null;
		for (Iterator iter = classInfos.iterator(); iter.hasNext();) {
			IClassInfo cInfo = (IClassInfo) iter.next();
			if (cInfo.getName().equals("String")) //$NON-NLS-1$
			{
				stringCInfo = cInfo;
			}
		}

		assertNotNull(stringCInfo);

		IMethodInfo methodInfo = null;
		List methodList = stringCInfo.getMethods();
		for (Iterator iter = methodList.iterator(); iter.hasNext();) {
			IMethodInfo mInfo = (IMethodInfo) iter.next();
			if (mInfo.getName().equals("fromCharCode")) //$NON-NLS-1$
			{
				methodInfo = mInfo;
			}
		}

		assertNotNull(methodInfo);

	}
}
