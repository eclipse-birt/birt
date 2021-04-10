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

public class InstanceIDTest extends TestCase {

	public void testParse() {
		checkParse("/32");
		checkParse("/32(dataSet:0)");
		checkParseChild1("/32(dataSet:0)/3");
		checkParseChild2("/32({dataSet}.3.group3:0)/4");

	}

	protected void checkParse(String value) {
		InstanceID id = InstanceID.parse(value);
		assertEquals(value, id.toString());
	}

	protected void checkParseChild1(String value) {
		InstanceID id = InstanceID.parse(value);
		assertEquals("/3", id.toString());
	}

	protected void checkParseChild2(String value) {
		InstanceID id = InstanceID.parse(value);
		assertEquals("/4", id.toString());
	}

}
