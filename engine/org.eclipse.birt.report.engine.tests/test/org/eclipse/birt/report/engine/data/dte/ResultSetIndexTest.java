/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.dte;

import junit.framework.TestCase;

public class ResultSetIndexTest extends TestCase {

	void setupIndex(ResultSetIndex index) {
		index.addResultSet("master-page", null, "-1", "master-page");
		index.addResultSet("query", null, "-1", "query");
		index.addResultSet("nest", "query", "0", "nest1");
		index.addResultSet("nest", "query", "10", "nest2");
		index.addResultSet("nest", "query", "12", "nest3");
		index.addResultSet("nest", "query", "String", "nest4");
	}

	public void testResultSet() {
		ResultSetIndex index = new ResultSetIndex();
		setupIndex(index);
		assertEquals("master-page", index.getResultSet("master-page", null, "-1"));
		assertEquals("query", index.getResultSet("query", null, "-1"));
		assertEquals("nest1", index.getResultSet("nest", "query", "-1"));
		assertEquals("nest1", index.getResultSet("nest", "query", "0"));
		assertEquals("nest1", index.getResultSet("nest", "query", "9"));
		assertEquals("nest2", index.getResultSet("nest", "query", "10"));
		assertEquals("nest3", index.getResultSet("nest", "query", "12"));
		assertEquals("nest4", index.getResultSet("nest", "query", "String"));
	}
}
