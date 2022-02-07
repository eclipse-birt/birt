/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.List;

import org.eclipse.birt.report.designer.ui.preview.BaseTestCase;

/**
 * Tests <code>IParameter</code> instance.
 *
 */

public class ParameterTest extends BaseTestCase {

	/**
	 * Test getPropertyValue method.
	 * 
	 * @throws Exception
	 */

	public void testGetPropertyValue() throws Exception {
		ParameterFactory factory = new ParameterFactory(engineTask);

		List children = factory.getRootChildren();
		assertEquals(3, children.size());

		ScalarParam param = (ScalarParam) children.get(2);
		assertFalse(param.isRequired());

		assertFalse(param.isRequired());
	}
}
