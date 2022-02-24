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

package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class UpgradeArchiveTest extends TestCase {
	@Before
	public void setUp() {
		new File("./utest/").mkdir();
	}

	@After
	public void tearDown() {
		new File("./utest/").delete();
	}

	@Test
	public void testUpgrade() throws IOException {
		saveResource("V2_1_1.rptarchive", "./utest/test.rptarchive");
		ArchiveFile af = new ArchiveFile("./utest/test.rptarchive", "rw+");
		try {
			byte[] buffer = new byte[4096];
			for (int i = 0; i < 128; i++) {
				ArchiveEntry entry = af.openEntry("/" + i);
				assertEquals(i * 4, entry.getLength());
				for (int j = 0; j < i; j++) {
					entry.read(j * 4, buffer, 0, 4);
					int v = ArchiveUtil.bytesToInteger(buffer);
					assertEquals(j, v);
				}
				entry.close();
			}
		} finally {
			af.close();
		}
		new File("./utest/test.rptarchive").delete();
	}

	@Test
	public void testArchiveV1() throws IOException {
		saveResource("V2_1_1.rptarchive", "./utest/test.rptarchive");
		ArchiveFile af = new ArchiveFile("./utest/test.rptarchive", "r");
		try {
			byte[] buffer = new byte[4096];
			for (int i = 0; i < 128; i++) {
				ArchiveEntry entry = af.openEntry("/" + i);
				assertEquals(i * 4, entry.getLength());
				for (int j = 0; j < i; j++) {
					entry.read(j * 4, buffer, 0, 4);
					int v = ArchiveUtil.bytesToInteger(buffer);
					assertEquals(j, v);
				}
				entry.close();
			}
			// read multiple times to ensure the archive entry is not shared
			for (int i = 0; i < 128; i++) {
				ArchiveEntry entry = af.openEntry("/" + i);
				assertEquals(i * 4, entry.getLength());
				for (int j = 0; j < i; j++) {
					entry.read(j * 4, buffer, 0, 4);
					int v = ArchiveUtil.bytesToInteger(buffer);
					assertEquals(j, v);
				}
				entry.close();
			}
		} finally {
			af.close();
		}
		new File("./utest/test.rptarchive").delete();
	}

	protected void saveResource(String resource, String file) throws IOException {
		InputStream in = getClass().getResourceAsStream(resource);
		try {
			OutputStream out = new FileOutputStream(file);
			try {
				byte[] buffer = new byte[4096];
				int size = in.read(buffer);
				do {
					out.write(buffer, 0, size);
					size = in.read(buffer);
				} while (size != -1);

			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}
}
