/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.DimensionValueUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

@SuppressWarnings("restriction")
/**
 * TestCases for DimensionValueUtil class.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testDoParse()}</td>
 * <td>Test doParse in the util</td>
 * <td>Parse dimension with locale correctly</td>
 * </tr>
 * </table>
 *
 */
public class DimensionValueUtilTest extends BaseTestCase {

	/**
	 * Test doParse in the util.
	 *
	 * @throws PropertyValueException
	 */
	public void testDoParse() throws PropertyValueException {
		DimensionValue dv = DimensionValueUtil.doParse("1,2cm", true, ULocale.FRENCH);
		assertEquals("1.2cm", dv.toString());

		dv = DimensionValueUtil.doParse("1.2cm", true, null);
		assertEquals("1.2cm", dv.toString());

		try {
			dv = DimensionValueUtil.doParse("1,2cm", false, ULocale.FRENCH);
			fail();
		} catch (PropertyValueException e) {
		}

		dv = DimensionValueUtil.doParse("1.2cm", false, ULocale.FRENCH);
		assertEquals("1.2cm", dv.toString());
	}
}
