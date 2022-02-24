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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Unit test for JoinConditionHandle. The test cases are:
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="black">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 * 
 * <tr>
 * <td>testGetter</td>
 * <td><code>JoinConditionHandle</code>'s getters work.</td>
 * <td>All fields of the <code>JoinConditionHandle</code> can be read by the
 * respective getter.</td>
 * </tr>
 * 
 * <tr>
 * <td>testWriter</td>
 * <td><code>JoinConditionHandle</code>'s setters work.</td>
 * <td>All fields of the <code>JoinConditionHandle</code> can be set by the
 * resprective setter.</td>
 * </tr>
 * </table>
 * 
 * @see org.eclipse.birt.report.model.elements.JointDataSet
 */

public class JoinConditionHandleTest extends BaseTestCase {

	private String fileName = "JoinConditionHandleTest.xml"; //$NON-NLS-1$

	private JoinConditionHandle joinConditionHandle;

	/**
	 * Creates the joinConditionHandle.
	 */

	public void setUp() throws DesignFileException {
		openDesign(fileName);
		JointDataSetHandle dataSet = (JointDataSetHandle) designHandle.findJointDataSet("JointDataSet"); //$NON-NLS-1$
		Iterator joinConditionsIterator = dataSet.joinConditionsIterator();
		joinConditionHandle = (JoinConditionHandle) joinConditionsIterator.next();
	}

	/**
	 * Tests all getters.
	 */

	public void testGetValue() {
		assertEquals(DesignChoiceConstants.JOIN_TYPE_INNER, joinConditionHandle.getJoinType());
		assertEquals(DesignChoiceConstants.JOIN_OPERATOR_EQALS, joinConditionHandle.getOperator());
		assertEquals("DataSet1", joinConditionHandle.getLeftDataSet()); //$NON-NLS-1$
		assertEquals("DataSet2", joinConditionHandle.getRightDataSet()); //$NON-NLS-1$
		assertEquals("leftExpression", joinConditionHandle //$NON-NLS-1$
				.getLeftExpression());
		assertEquals("rightExpression", joinConditionHandle //$NON-NLS-1$
				.getRightExpression());
	}

	/**
	 * Tests all setters.
	 * 
	 * @throws SemanticException when value can't be set.
	 */

	public void testSetValue() throws SemanticException {
		joinConditionHandle.setJoinType(DesignChoiceConstants.JOIN_TYPE_LEFT_OUT);
		assertEquals(DesignChoiceConstants.JOIN_TYPE_LEFT_OUT, joinConditionHandle.getJoinType());

		String operator = DesignChoiceConstants.JOIN_OPERATOR_EQALS;
		joinConditionHandle.setOperator(operator);
		assertEquals(operator, joinConditionHandle.getOperator());

		String leftDataSet = "DataSet1"; //$NON-NLS-1$
		joinConditionHandle.setLeftDataSet(leftDataSet);
		assertEquals(leftDataSet, joinConditionHandle.getLeftDataSet());

		String rightDataSet = "DataSet2"; //$NON-NLS-1$
		joinConditionHandle.setRightDataSet(rightDataSet);
		assertEquals(rightDataSet, joinConditionHandle.getRightDataSet());

		String leftExpression = "leftExpression"; //$NON-NLS-1$
		joinConditionHandle.setLeftExpression(leftExpression);
		assertEquals(leftExpression, joinConditionHandle.getLeftExpression());

		String rightExpression = "rightExpression"; //$NON-NLS-1$
		joinConditionHandle.setRightExpression(rightExpression);
		assertEquals(rightExpression, joinConditionHandle.getRightExpression());
	}

}
