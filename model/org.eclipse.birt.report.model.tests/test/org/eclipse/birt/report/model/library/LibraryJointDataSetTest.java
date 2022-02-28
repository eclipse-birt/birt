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

package org.eclipse.birt.report.model.library;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.JoinConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for use library joint data set in the report design.
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 *
 * <tr>
 * <td>{@link #testGetJointConditionDataSet()}</td>
 * <td>Report uses a joint data set from library.</td>
 * <td>Values from getLeftDataSet/getRightDataSet are with library
 * namespace.</td>
 * </tr>
 *
 * </table>
 *
 */

public class LibraryJointDataSetTest extends BaseTestCase {

	/**
	 * Tests JointDataSetHandle.getLeftDataSet/getLeftDataSet
	 *
	 * @throws Exception
	 */

	public void testGetJointConditionDataSet() throws Exception {
		openDesign("DesignIncludeJointDataSet.xml"); //$NON-NLS-1$

		JointDataSetHandle dataSet = designHandle.findJointDataSet("Data Set"); //$NON-NLS-1$
		Iterator conditions = dataSet.joinConditionsIterator();

		JoinConditionHandle cond = (JoinConditionHandle) conditions.next();
		assertEquals("new_library.Rev", cond.getLeftDataSet()); //$NON-NLS-1$
		assertEquals("new_library.HistUnitsSales", cond.getRightDataSet()); //$NON-NLS-1$
	}

	/**
	 * For bug 201038, can't throw out exception when remove data set.
	 *
	 * @throws Exception
	 */

	public void testRemoveDataSetFromLib() throws Exception {
		openDesign("JointDataSetHandleTest_2.xml");//$NON-NLS-1$
		JointDataSetHandle dsHandle = designHandle.findJointDataSet("Data Set2");//$NON-NLS-1$
		dsHandle.removeDataSet("JointDataSetHandleTest_Lib.Data Set");//$NON-NLS-1$
	}
}
