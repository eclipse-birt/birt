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

package org.eclipse.birt.core.config;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.After;
import org.junit.Test;

import junit.framework.TestCase;

public class FileConfigVarManagerTest extends TestCase {
	@After
	public void tearDown() {
		File file = new File("configvartest");
		if (file.exists()) {
			file.delete();
		}
	}

	/*
	 * Class under test for void FileConfigVarManager()
	 */
	@Test
	public void testFileConfigVarManager() {
		FileConfigVarManager manager = new FileConfigVarManager();
		assertNotNull(manager.getConfigVar("os.version")); // get from system
		assertNull(manager.getConfigVar("BooleanValue"));
	}

	/*
	 * Class under test for void FileConfigVarManager(String)
	 */
	@Test
	public void testFileConfigVarManagerString() {
		File f = new File("configvartest");
		try {
			if (f.exists())
				f.delete();
			f.createNewFile();
			FileOutputStream ostream = new FileOutputStream(f);
			ostream.write(
					"StrValue=string\nos.version=Windows2000\nIntValue=1234\nIntValueAsDouble=123.45\nInvalidIntValue=abcd\nBooleanValue=true\nInvalidBooleanValue=abc"
							.getBytes());
			ostream.close();
			FileConfigVarManager manager = new FileConfigVarManager(f.getAbsolutePath());
			assertEquals(manager.getConfigVar("os.version"), "Windows2000");
			assertEquals(manager.getConfigVar("BooleanValue"), "true");
			assertEquals(manager.getConfigBoolean("BooleanValue"), true);
			assertEquals(manager.getConfigInteger("IntValue"), new Integer(1234));
			assertNull(manager.getConfigInteger("IntValueAsDouble"));
			assertEquals(manager.getConfigVar("StrValue"), "string");
			assertNull(manager.getConfigInteger("InvalidIntValue"));
			assertNull(manager.getConfigInteger("InvalidBooleanValue"));
		} catch (Exception e) {
			assertTrue(false); // Should not be here
		} finally {
			if (f.exists())
				f.delete();
		}
	}

}
