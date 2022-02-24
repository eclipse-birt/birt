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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for JarFileHandle. The test cases are:
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="black">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 * 
 * <tr>
 * <td>testGetter</td>
 * <td><code>JarFileHandle</code>'s getters work.</td>
 * <td>All fields of the <code>JarFileHandle</code> can be read by the
 * respective getter.</td>
 * </tr>
 * 
 * <tr>
 * <td>testWriter</td>
 * <td><code>JarFileHandle</code>'s setters work.</td>
 * <td>All fields of the <code>JarFileHandle</code> can be set by the
 * resprective setter.</td>
 * </tr>
 * </table>
 * 
 * @see org.eclipse.birt.report.model.elements.ScriptLib
 */

public class ScriptLibHandleTest extends BaseTestCase {

	private String fileName = "ScriptLibHandleTest.xml"; //$NON-NLS-1$

	private ScriptLibHandle scriptLibHandle = null;

	/**
	 * Creates the JarFileHandle.
	 */

	protected void setUp() throws Exception {
		super.setUp();
		openDesign(fileName);

		Iterator iterator = designHandle.scriptLibsIterator();
		scriptLibHandle = (ScriptLibHandle) iterator.next();
	}

	/**
	 * Tests all getters.
	 */

	public void testGetValue() {
		assertEquals("a.jar", scriptLibHandle.getName());//$NON-NLS-1$

	}

	/**
	 * Tests all setters.
	 * 
	 * @throws SemanticException when value can't be set.
	 */

	public void testSetValue() throws SemanticException {
		scriptLibHandle.setName("he.jar");//$NON-NLS-1$
		assertEquals("he.jar", scriptLibHandle.getName());//$NON-NLS-1$

	}
}
