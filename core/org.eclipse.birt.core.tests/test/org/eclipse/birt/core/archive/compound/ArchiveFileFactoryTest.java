/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ArchiveFileFactoryTest extends TestCase {

	static final int TEST_COUNT = 50;

	static final String TEST_FOLDER = "./utest/";
	static final String ARCHIVE_FILE = TEST_FOLDER + "archiveFileName";
	static final String VIEW_FILE = TEST_FOLDER + "viewFileName";
	static final String ARCHIVE_ID = "archiveId";
	static final String VIEW_ID = "viewId";

	@Before
	public void setUp() {
		new File(TEST_FOLDER).mkdirs();
	}

	@After
	public void tearDown() {
		new File(ARCHIVE_FILE).delete();
		new File(VIEW_FILE).delete();
		new File(TEST_FOLDER).delete();
	}

	@Test
	public void testCreateAndOpenArchive() throws IOException {
		IArchiveFileFactory factory = new ArchiveFileFactory();
		IArchiveFile writeArchive = factory.createArchive(ARCHIVE_ID);
		byte[] mes = new byte[TEST_COUNT * 2];
		for (int index = 0; index < TEST_COUNT; index++) {
			ArchiveEntry entry = writeArchive.createEntry("/entry/" + index);
			entry.write(0, mes, 0, index);
			entry.close();
		}
		writeArchive.close();

		IArchiveFile readArchive = factory.openArchive(ARCHIVE_ID, "r");
		assertEquals(ARCHIVE_ID, readArchive.getSystemId());
		assertEquals(null, readArchive.getDependId());
		for (int index = 0; index < TEST_COUNT; index++) {
			ArchiveEntry entry = readArchive.openEntry("/entry/" + index);
			assertTrue(entry != null);
			assertTrue(entry.getLength() == index);
			entry.close();
		}
		readArchive.close();
	}

	@Test
	public void testCreateAndOpenView() throws IOException {
		IArchiveFileFactory factory = new ArchiveFileFactory();
		IArchiveFile dependArchive = factory.createArchive(ARCHIVE_ID);
		byte[] mes = new byte[TEST_COUNT * 2];
		for (int index = 0; index < 10; index++) {
			ArchiveEntry entry = dependArchive.createEntry("/entry/1." + index);
			entry.write(0, mes, 0, index);
			entry.close();
		}
		IArchiveFile viewArchive = factory.createView(VIEW_ID, dependArchive);
		for (int index = 10; index < 20; index++) {
			ArchiveEntry entry = viewArchive.createEntry("/entry/2." + index);
			entry.write(0, mes, 0, index);
			entry.close();
		}
		viewArchive.flush();
		viewArchive.close();

		IArchiveFile openView = factory.openView(VIEW_ID, "r", dependArchive);
		assertEquals(ARCHIVE_ID, dependArchive.getSystemId());
		assertEquals(null, dependArchive.getDependId());
		assertEquals(VIEW_ID, openView.getSystemId());
		assertEquals(ARCHIVE_ID, openView.getDependId());
		for (int index = 0; index < 10; index++) {
			ArchiveEntry entry = openView.openEntry("/entry/1." + index);
			assertTrue(entry != null);
			assertTrue(entry.getLength() == index);
			entry.close();
		}
		for (int index = 10; index < 20; index++) {
			ArchiveEntry entry = openView.openEntry("/entry/2." + index);
			assertTrue(entry != null);
			assertTrue(entry.getLength() == index);
			entry.close();
		}
		openView.close();
		dependArchive.close();

		IArchiveFile openView2 = factory.openArchive(VIEW_ID, "r");
		assertEquals(VIEW_ID, openView2.getSystemId());
		assertEquals(ARCHIVE_ID, openView2.getDependId());

		openView2.close();
	}
}
