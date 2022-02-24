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

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;

public class LeaderLineStyleTest extends TestCase {

	public void testConstant() {
		assertEquals(LeaderLineStyle.FIXED_LENGTH, LeaderLineStyle.FIXED_LENGTH_LITERAL.getValue());
		assertEquals(LeaderLineStyle.STRETCH_TO_SIDE, LeaderLineStyle.STRETCH_TO_SIDE_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(LeaderLineStyle.FIXED_LENGTH_LITERAL, LeaderLineStyle.get(LeaderLineStyle.FIXED_LENGTH));

		assertEquals(LeaderLineStyle.FIXED_LENGTH_LITERAL, LeaderLineStyle.get("Fixed_Length")); //$NON-NLS-1$
		assertEquals(LeaderLineStyle.STRETCH_TO_SIDE_LITERAL, LeaderLineStyle.get("Stretch_To_Side")); //$NON-NLS-1$

		assertNull(LeaderLineStyle.get("No Match")); //$NON-NLS-1$
	}
}
