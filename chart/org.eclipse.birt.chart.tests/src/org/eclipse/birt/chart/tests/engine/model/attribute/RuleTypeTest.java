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
package org.eclipse.birt.chart.tests.engine.model.attribute;

import org.eclipse.birt.chart.model.attribute.RuleType;

import junit.framework.TestCase;

public class RuleTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(RuleType.FILTER, RuleType.FILTER_LITERAL.getValue());
		assertEquals(RuleType.SUPPRESS, RuleType.SUPPRESS_LITERAL.getValue());
		assertEquals(RuleType.LINK, RuleType.LINK_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(RuleType.FILTER_LITERAL, RuleType.get(RuleType.FILTER));
		assertEquals(RuleType.SUPPRESS_LITERAL, RuleType.get(RuleType.SUPPRESS));

		assertEquals(RuleType.FILTER_LITERAL, RuleType.get("Filter")); //$NON-NLS-1$
		assertEquals(RuleType.SUPPRESS_LITERAL, RuleType.get("Suppress")); //$NON-NLS-1$
		assertEquals(RuleType.LINK_LITERAL, RuleType.get("Link")); //$NON-NLS-1$

		assertNull(RuleType.get("No Match")); //$NON-NLS-1$
	}
}
