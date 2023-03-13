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
 * The methods Date.getMonth(),Date.getMilliseconds,Date.getTimezoneOffset have
 * wrong spelling
 * </p>
 * Test description:
 * <p>
 * 1. Check that those constants Class method can be correctly retrived by name
 * </p>
 * 2. Check that this Class list containing these methods.
 */

public class Regression_78431 extends BaseTestCase {

	/**
	 *
	 */
	public void test_regression_78431() {
		MetaDataDictionary instance = MetaDataDictionary.getInstance();

		// Retrieve by name

		IClassInfo number = instance.getClass("Date"); //$NON-NLS-1$
		IMethodInfo method1 = number.getMethod("getMonth"); //$NON-NLS-1$
		IMethodInfo method2 = number.getMethod("getMilliseconds"); //$NON-NLS-1$
		IMethodInfo method3 = number.getMethod("getTimezoneOffset"); //$NON-NLS-1$
		assertNotNull(method1);
		assertNotNull(method2);
		assertNotNull(method3);

		// Class/Method list containing this Class method

		List classInfos = instance.getClasses();
		IClassInfo numberCInfo = null;
		for (Iterator iter = classInfos.iterator(); iter.hasNext();) {
			IClassInfo cInfo = (IClassInfo) iter.next();
			if (cInfo.getName().equals("Date")) //$NON-NLS-1$
			{
				numberCInfo = cInfo;
			}
		}

		assertNotNull(numberCInfo);

		IMethodInfo methodInfo1 = null;
		IMethodInfo methodInfo2 = null;
		IMethodInfo methodInfo3 = null;
		List methodList = numberCInfo.getMethods();
		for (Iterator iter = methodList.iterator(); iter.hasNext();) {
			IMethodInfo mInfo = (IMethodInfo) iter.next();
			if (mInfo.getName().equals("getMonth")) //$NON-NLS-1$
			{
				methodInfo1 = mInfo;
				assertNotNull(methodInfo1);
			}
			if (mInfo.getName().equals("getMilliseconds")) //$NON-NLS-1$
			{
				methodInfo2 = mInfo;
				assertNotNull(methodInfo2);
			}
			if (mInfo.getName().equals("getTimezoneOffset")) //$NON-NLS-1$
			{
				methodInfo3 = mInfo;
				assertNotNull(methodInfo3);
			}

		}

	}
}
