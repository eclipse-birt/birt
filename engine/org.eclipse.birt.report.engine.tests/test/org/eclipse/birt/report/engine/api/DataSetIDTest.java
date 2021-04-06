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

package org.eclipse.birt.report.engine.api;

import junit.framework.TestCase;

public class DataSetIDTest extends TestCase {

	public void testParser() {
		checkParse("dataSet");

		checkParse("{dataSet}.0.group");

		checkParse("{{dataSet}.0.group}.0.group2");
	}

	protected void checkParse(String value) {
		DataSetID id = DataSetID.parse(value);
		assertEquals(value, id.toString());
	}

}
