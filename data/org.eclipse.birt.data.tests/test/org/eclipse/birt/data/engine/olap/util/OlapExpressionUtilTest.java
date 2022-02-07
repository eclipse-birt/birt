/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.util;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class OlapExpressionUtilTest {
	/**
	 * @throws DataException
	 * 
	 */
	@Test
	public void testGetTargetLevel() throws DataException {
		assertEquals("level1", OlapExpressionUtil.getTargetDimLevel("dimension[\"dim1\"][\"level1\"]").getLevelName());

		try {
			assertEquals(null, OlapExpressionUtil.getTargetDimLevel("dimension[\"dim1\"]"));
		} catch (DataException ex) {
			assertEquals(ResourceConstants.LEVEL_NAME_NOT_FOUND, ex.getErrorCode());
		}
	}

	@Test
	public void testComplexDimensionExpression() {
		String expr = "var kind = dimension[\"Group1\"][\"Field1\"][\"Attribute1\"]\n"
				+ "var numerator = data[\"Data Column Binding1\"]\n" + "var denominator";

		boolean result = OlapExpressionUtil.isComplexDimensionExpr(expr);
		assertEquals(result, true);
	}

}
