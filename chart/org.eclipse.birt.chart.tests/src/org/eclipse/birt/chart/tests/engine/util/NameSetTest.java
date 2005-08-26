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
import org.eclipse.birt.chart.util.NameSet;

public class NameSetTest extends TestCase {
	
	String[] set = {"Name 1", "Name 2", "Name 3"};
	NameSet nameSet;
	
	protected void setUp() throws Exception {
		super.setUp();
		nameSet = new NameSet("-", "-", set);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		nameSet = null;		
	}
	
	public void testGetNames(){
		assertEquals(set, nameSet.getNames());
	}
	
	public void testGetDisplayNames(){
		String[] a = nameSet.getDisplayNames();
		for (int i = 0; i < 3; i++){
			assertEquals("!-"+set[i]+"-!", a[i]);
		}
	}
	
	public void testGetNameIndex(){
		assertEquals(0, nameSet.getNameIndex("Name 1"));
		assertEquals(2, nameSet.getNameIndex("Name 3"));
		assertEquals(-1, nameSet.getNameIndex("Not Found"));
	}
	
	public void testGetSafeNameIndex(){
		assertEquals(0, nameSet.getSafeNameIndex("Name 1"));
		assertEquals(2, nameSet.getSafeNameIndex("Name 3"));
		assertEquals(0, nameSet.getSafeNameIndex("Not Found"));
	}
	
	public void testGetDisplayNameByName(){
		assertEquals("!-Name 1-!", nameSet.getDisplayNameByName("Name 1"));
		assertNull(nameSet.getDisplayNameByName("Not Found"));
	}
	
	public void testGetNameByDisplayName(){
		assertEquals("Name 1", nameSet.getNameByDisplayName("!-Name 1-!"));
		assertNull(nameSet.getNameByDisplayName("Not Found"));
	}

}
