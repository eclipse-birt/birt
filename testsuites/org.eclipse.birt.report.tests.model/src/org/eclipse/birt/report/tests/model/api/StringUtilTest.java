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

package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for StringUtil class.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testDoubleToString()}</td>
 * <td>Test doParse in the util</td>
 * <td>Parse dimension with locale correctly</td>
 * </tr>
 * </table>
 * 
 */

public class StringUtilTest extends BaseTestCase {

	/**
	 * Tests <code>doubleToString(double d, int fNumber, ULocale locale)</code>.
	 */

	public void testDoubleToString() {
		assertEquals("123456,789", StringUtil.doubleToString(123456.7890123, 3, ULocale.FRANCE)); //$NON-NLS-1$

		// test the E-expo format double
		assertEquals("123456,7", StringUtil.doubleToString(123.4567E3, 3, ULocale.FRANCE)); //$NON-NLS-1$
	}

}
