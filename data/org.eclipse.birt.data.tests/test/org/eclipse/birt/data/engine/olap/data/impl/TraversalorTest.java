
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

/**
 *
 */

public class TraversalorTest {
	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	/**
	 *
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
	public void testTraversalor() throws IOException, BirtException {
		int[] lengthArray = { 1, 1, 1 };
		Traversalor traversalor = new Traversalor(lengthArray);
		while (traversalor.next()) {
			System.out.println(traversalor.getInt(0) + ", " + traversalor.getInt(1) + ", " + traversalor.getInt(2));
		}
	}
}
