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

package org.eclipse.birt.chart.tests.engine.util;

import junit.framework.TestCase;

import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.util.ChartUtil;

public class ChartUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testIsColorTransparent() {
		assertFalse(ChartUtil.isColorTransparent(ColorDefinitionImpl.BLUE()));
		assertTrue(ChartUtil.isColorTransparent(ColorDefinitionImpl.TRANSPARENT()));
	}
	
	public void testMathGT(){
		assertTrue(ChartUtil.mathGT(1.0 + 1.0 * 1E-9, 1.0));
		assertFalse(ChartUtil.mathGT(1.0 + 1.0 * 1E-11, 1.0));
	}
	
	public void testMathLT(){
		assertTrue(ChartUtil.mathLT(1.0, 1.0 + 1.0 * 1E-9));
		assertFalse(ChartUtil.mathLT(1.0, 1.0 + 1.0 * 1E-11));
	}
	
	public void testMathEqual(){
		assertFalse(ChartUtil.mathEqual(1.0 + 1.0 * 1E-9, 1.0));
		assertTrue(ChartUtil.mathEqual(1.0 + 1.0 * 1E-11, 1.0));
		assertTrue(ChartUtil.mathEqual(1.0, 1.0 + 1.0 * 1E-11));
	}
}
