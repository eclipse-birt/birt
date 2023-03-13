/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.model.adapter.oda.util;

import junit.framework.TestCase;

/**
 * Test cases for trim and add single quotation for the default value.
 *
 */

public class ParameterValueUtilTest extends TestCase {

	/**
	 * Test literal with/without embedded single quote character.
	 */

	public void testParameterValueUtil() {
		// test normal literal value w/o embedded quote character

		assertQuoteConversion("'1'", "1"); //$NON-NLS-1$//$NON-NLS-2$
		assertQuoteConversion("'normal value'", "normal value"); //$NON-NLS-1$//$NON-NLS-2$
		assertQuoteConversion("'[0PROJECT].[P10000000]'", //$NON-NLS-1$
				"[0PROJECT].[P10000000]"); //$NON-NLS-1$

		// test literal value w embedded quote character
		assertQuoteConversion("'It\\'s ok.'", "It's ok."); //$NON-NLS-1$//$NON-NLS-2$
		assertQuoteConversion("'It\\'s ok. Isn\\'t it?'", "It's ok. Isn't it?"); //$NON-NLS-1$//$NON-NLS-2$
		assertQuoteConversion("'It\\'s ok. Isn\\'t it? Y\\'eah.'", //$NON-NLS-1$
				"It's ok. Isn't it? Y'eah."); //$NON-NLS-1$
		assertQuoteConversion("'It\\'\\'s ok.'", "It''s ok."); //$NON-NLS-1$//$NON-NLS-2$
		assertQuoteConversion("'\\'It\\'s ok.\\''", "'It's ok.'");//$NON-NLS-1$//$NON-NLS-2$
		assertQuoteConversion("'\\'\\''", "''");//$NON-NLS-1$//$NON-NLS-2$

		// test empty or null literal value
		assertQuoteConversion("''", ""); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(ParameterValueUtil.toJsExprValue(null, null));

		// test conversion of non-quoted js expression value
		assertNull(ParameterValueUtil.toLiteralValue(null));
		assertEquals("'begin literal quote", ParameterValueUtil //$NON-NLS-1$
				.toLiteralValue("'begin literal quote")); //$NON-NLS-1$
		assertEquals("end literal quote'", ParameterValueUtil //$NON-NLS-1$
				.toLiteralValue("end literal quote'")); //$NON-NLS-1$

		// test empty or null literal value

		assertFalse(ParameterValueUtil.isQuoted("\"a\"+\"b\""));//$NON-NLS-1$
		assertTrue(ParameterValueUtil.isQuoted("\"a+b\""));//$NON-NLS-1$
	}

	private void assertQuoteConversion(String expectedQuotedValue, String literalValue) {
		String actualQuotedValue = ParameterValueUtil.toJsExprValue(literalValue, null);
		assertEquals(expectedQuotedValue, actualQuotedValue);

		assertEquals(literalValue, ParameterValueUtil.toLiteralValue(actualQuotedValue));
	}

}
