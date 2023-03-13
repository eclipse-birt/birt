/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 *
 * Unit test for Class StructPropertyDefn.
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testDefnType()}</td>
 * <td>Tests the type of <code>StructPropertyDefn</code>.</td>
 * <td>It is <code>PropertyDefn.STRUCT_PROPERTY.</code></td>
 * </tr>
 *
 *
 */

public class StructPropertyDefnTest extends BaseTestCase {
	private StructPropertyDefn propertyDefn = new StructPropertyDefn();

	public void testDefnType() {
		assertTrue(propertyDefn.isStructureMember());
	}

}
