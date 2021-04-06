/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.toc;

import java.util.Comparator;

import junit.framework.TestCase;

public class TOCComparatorTest extends TestCase {

	public void testCompare() {
		Comparator<String> c = new TOCComparator();
		assertEquals(-1, c.compare("/", "__TOC_1"));
		assertEquals(-1, c.compare("/", "/"));
		assertEquals(1, c.compare("__TOC_1", "/"));

		assertEquals(0, c.compare("__TOC_1", "__TOC_1"));
		assertEquals(-1, c.compare("__TOC_1", "__TOC_2"));
		assertEquals(1, c.compare("__TOC_2", "__TOC_1"));
		assertEquals(-1, c.compare("__TOC_1", "__TOC_1_1"));
		assertEquals(1, c.compare("__TOC_1_1", "__TOC_1"));
		assertEquals(-1, c.compare("__TOC_1", "__TOC_12"));
		assertEquals(1, c.compare("__TOC_12", "__TOC_1"));

		assertEquals(-1, c.compare("__TOC_0_12", "__TOC_1_12"));

	}
}
