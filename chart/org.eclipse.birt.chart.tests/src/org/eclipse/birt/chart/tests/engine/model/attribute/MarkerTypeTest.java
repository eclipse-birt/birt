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
import org.eclipse.birt.chart.model.attribute.MarkerType;

public class MarkerTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(MarkerType.CROSSHAIR, MarkerType.CROSSHAIR_LITERAL.getValue());
		assertEquals(MarkerType.TRIANGLE, MarkerType.TRIANGLE_LITERAL.getValue());
		assertEquals(MarkerType.BOX, MarkerType.BOX_LITERAL.getValue());
		assertEquals(MarkerType.CIRCLE, MarkerType.CIRCLE_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(MarkerType.CROSSHAIR_LITERAL, MarkerType.get(MarkerType.CROSSHAIR));
		assertEquals(MarkerType.TRIANGLE_LITERAL, MarkerType.get(MarkerType.TRIANGLE));

		assertEquals(MarkerType.CROSSHAIR_LITERAL, MarkerType.get("Crosshair")); //$NON-NLS-1$
		assertEquals(MarkerType.TRIANGLE_LITERAL, MarkerType.get("Triangle")); //$NON-NLS-1$
		assertEquals(MarkerType.BOX_LITERAL, MarkerType.get("Box")); //$NON-NLS-1$
		assertEquals(MarkerType.CIRCLE_LITERAL, MarkerType.get("Circle")); //$NON-NLS-1$

		assertNull(MarkerType.get("No Match")); //$NON-NLS-1$
	}
}
