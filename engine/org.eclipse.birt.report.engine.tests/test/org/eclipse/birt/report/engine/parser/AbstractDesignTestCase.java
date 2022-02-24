/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.parser;

import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.ir.Report;

abstract public class AbstractDesignTestCase extends TestCase {

	protected Report report = null;

	void loadDesign(String design) {
		try {
			InputStream in = this.getClass().getResourceAsStream(design);
			assertTrue(in != null);
			ReportParser parser = new ReportParser();
			report = parser.parse("", in);
			assertTrue(report != null);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}
}
