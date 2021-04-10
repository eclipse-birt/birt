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
