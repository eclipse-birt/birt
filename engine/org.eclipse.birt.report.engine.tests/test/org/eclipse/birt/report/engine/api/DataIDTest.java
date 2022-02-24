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

package org.eclipse.birt.report.engine.api;

import junit.framework.TestCase;

public class DataIDTest extends TestCase {

	public void testParse() {
		checkParse("dataSet:0");

		checkParse("{dataSet}.0.group:0");

		checkParse("{{dataSet}.0.group}.0.group3:0");

	}

	protected void checkParse(String value) {
		DataID id = DataID.parse(value);
		assertEquals(value, id.toString());
	}

	public void testEquals() {
		DataSetID dataSet = new DataSetID("ABC");
		// test data id equals
		assertEquals(new DataID(dataSet, 1), new DataID(dataSet, 1));
		// test cube id equals
		assertEquals(new DataID(dataSet, "B"), new DataID(dataSet, "B"));

		// test data id != cube id
		assertTrue(!new DataID(dataSet, 1).equals(new DataID(dataSet, "A")));
	}
}
