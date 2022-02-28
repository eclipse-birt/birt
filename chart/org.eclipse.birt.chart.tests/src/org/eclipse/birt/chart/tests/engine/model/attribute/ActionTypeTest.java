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

import org.eclipse.birt.chart.model.attribute.ActionType;

import junit.framework.TestCase;

public class ActionTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(ActionType.URL_REDIRECT, ActionType.URL_REDIRECT_LITERAL.getValue());
		assertEquals(ActionType.SHOW_TOOLTIP, ActionType.SHOW_TOOLTIP_LITERAL.getValue());
		assertEquals(ActionType.TOGGLE_VISIBILITY, ActionType.TOGGLE_VISIBILITY_LITERAL.getValue());
		assertEquals(ActionType.INVOKE_SCRIPT, ActionType.INVOKE_SCRIPT_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(ActionType.URL_REDIRECT_LITERAL, ActionType.get(ActionType.URL_REDIRECT));
		assertEquals(ActionType.SHOW_TOOLTIP_LITERAL, ActionType.get(ActionType.SHOW_TOOLTIP));

		assertEquals(ActionType.URL_REDIRECT_LITERAL, ActionType.get("URL_Redirect")); //$NON-NLS-1$
		assertEquals(ActionType.SHOW_TOOLTIP_LITERAL, ActionType.get("Show_Tooltip"));//$NON-NLS-1$
		assertEquals(ActionType.TOGGLE_VISIBILITY_LITERAL, ActionType.get("Toggle_Visibility"));//$NON-NLS-1$
		assertEquals(ActionType.INVOKE_SCRIPT_LITERAL, ActionType.get("Invoke_Script"));//$NON-NLS-1$

		assertNull(ActionType.get("No Match"));//$NON-NLS-1$
	}
}
