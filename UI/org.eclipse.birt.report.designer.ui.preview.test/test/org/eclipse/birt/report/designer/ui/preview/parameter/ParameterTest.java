/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
