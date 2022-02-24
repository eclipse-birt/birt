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
