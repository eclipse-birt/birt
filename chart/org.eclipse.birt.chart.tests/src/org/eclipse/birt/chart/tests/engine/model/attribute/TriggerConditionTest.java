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
import org.eclipse.birt.chart.model.attribute.TriggerCondition;

public class TriggerConditionTest extends TestCase {

	public void testConstant() {
		assertEquals(TriggerCondition.ONBLUR, TriggerCondition.ONBLUR_LITERAL.getValue());
		assertEquals(TriggerCondition.ONKEYDOWN, TriggerCondition.ONKEYDOWN_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(TriggerCondition.ONCLICK_LITERAL, TriggerCondition.get(TriggerCondition.ONCLICK));
		assertEquals(TriggerCondition.ONMOUSEOVER_LITERAL, TriggerCondition.get("onmouseover")); //$NON-NLS-1$

		assertNull(TriggerCondition.get("No Match")); //$NON-NLS-1$
	}
}
