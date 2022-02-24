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

package org.eclipse.birt.report.engine.ir;

import junit.framework.TestCase;

/**
 * base class of Group type test
 * 
 */
public abstract class GroupTestCase extends TestCase {

	protected GroupDesign group;

	public GroupTestCase(GroupDesign g) {
		group = g;
	}

	/**
	 * Test get/setKeyExpr method
	 * 
	 * set the key expression
	 * 
	 * then get it to test if they work correctly
	 */

	public void testBaseGroup() {
		// Expression exp = new Expression( );

		// Set
		// group.setKeyExpr( exp );

		// Get
		// assertEquals( group.getKeyExpr( ), exp );
	}
}
