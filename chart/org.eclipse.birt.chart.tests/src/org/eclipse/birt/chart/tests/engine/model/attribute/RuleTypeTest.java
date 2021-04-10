/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.RuleType;

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