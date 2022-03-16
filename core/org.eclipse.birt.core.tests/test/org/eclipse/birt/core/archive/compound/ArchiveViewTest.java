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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ArchiveViewTest extends TestCase {

	static final String TEST_FOLDER = "./utest/";
	static final String ARCHIVE_FILE = TEST_FOLDER + "archive.rptdocument";
	static final String VIEW_FILE = TEST_FOLDER + "view.rptdocument";
	static final String REPORT_DOCUMENT_RESOURCE = "org/eclipse/birt/core/archive/compound/ArchiveViewTest.rptdocument";

	@Override
	@Before
	public void setUp() {
		new File(TEST_FOLDER).mkdirs();

		copyResource(REPORT_DOCUMENT_RESOURCE, ARCHIVE_FILE);
	}

	@Override
	@After
	public void tearDown() {
		new File(ARCHIVE_FILE).delete();
		new File(VIEW_FILE).delete();
		new File(TEST_FOLDER).delete();
		new File(ARCHIVE_FILE).delete();
	}

	@Test
	public void testModify() throws IOException {
		final int entryCount = 100;
		byte[] mes = new byte[entryCount * 2];
		ArchiveFile archive = new ArchiveFile(ARCHIVE_FILE, "rw");
		// 1. write 50 entries into archive document.
		for (int index = 1; index <= entryCount / 2; index++) {
			ArchiveEntry entry = archive.createEntry("/entry/" + index);
			entry.write(0, mes, 0, index);
			entry.close();
		}
		archive.flush();
		ArchiveFile viewFile = new ArchiveFile(VIEW_FILE, "rw");
		// 2. new view archive
		ArchiveView view = new ArchiveView(viewFile, archive, false);
		// 3. read 100 entries from view document. check them.
		// [1-50] should be what we input. and [51-100] should be null.
		for (int index = 1; index <= entryCount / 2; index++) {
			ArchiveEntry entry = view.openEntry("/entry/" + index);
			assertTrue(entry != null);
			assertEquals(index, entry.getLength());
			entry.close();
		}
		for (int index = entryCount / 2 + 1; index <= entryCount; index++) {
			assertTrue(!view.exists("/entry/" + index));
		}

		// 4. modify all 100 entries, and save the data into view document.
		for (int index = 1; index <= entryCount; index++) {
			ArchiveEntry entry = view.createEntry("/entry/" + index);
			entry.write(0, mes, 0, index * 2);
			entry.close();
		}

		// 5. verify the entries in view document
		for (int index = 1; index <= entryCount; index++) {
			ArchiveEntry entry = view.openEntry("/entry/" + index);
			assertTrue(entry != null);
			assertTrue(entry.getLength() == index * 2);
			entry.close();
		}
		view.close();
		viewFile.close();
		archive.close();

		// 6. verify the entries in archive document
		archive = new ArchiveFile(ARCHIVE_FILE, "r");
		for (int index = 1; index <= entryCount / 2; index++) {
			ArchiveEntry entry = archive.openEntry("/entry/" + index);
			assertTrue(entry != null);
			assertTrue(entry.getLength() == index);
			entry.close();
		}
	}

	@Test
	public void testReadAndWrite() throws IOException {
		ArchiveFile archiveFile = new ArchiveFile(ARCHIVE_FILE, "rw");
		ArchiveFile viewFile = new ArchiveFile(VIEW_FILE, "rw");
		ArchiveView view = new ArchiveView(viewFile, archiveFile, false);
		view.setCacheSize(64 * 1024);
		createArchive(view);
		checkArchive(view);
		assertTrue(view.getUsedCache() > 0);
		assertTrue(view.getUsedCache() <= 64 * 1024);
		view.close();
		assertTrue(view.getUsedCache() == 0);
	}

	@Test
	public void testReadAndWriteV2() throws IOException {
		ArchiveFile archive = new ArchiveFile(ARCHIVE_FILE, "r");
		ArchiveFile viewFile = new ArchiveFile(VIEW_FILE, "rw");
		ArchiveView view = new ArchiveView(viewFile, archive, false);
		view.setCacheSize(64 * 1024);
		createArchive(view);
		checkArchive(view);
		assertTrue(view.getUsedCache() > 0);
		assertTrue(view.getUsedCache() <= 64 * 1024);
		view.close();
		assertTrue(view.getUsedCache() == 0);
	}

	private void copyResource(String src, String tgt) {
		File parent = new File(tgt).getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		InputStream in = getClass().getClassLoader().getResourceAsStream(src);
		assertTrue(in != null);
		try {
			FileOutputStream fos = new FileOutputStream(tgt);
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ((readCount = in.read(fileData)) != -1) {
				fos.write(fileData, 0, readCount);
			}
			fos.close();
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	public void createArchive(IArchiveFile archive) throws IOException {
		int entryCount = 1024;
		byte[] b = new byte[entryCount];
		for (int i = 0; i < entryCount; i++) {
			ArchiveEntry entry = archive.createEntry("/entry/" + i);
			entry.write(0, b, 0, i);
			entry.close();
		}
	}

	void checkArchive(IArchiveFile archive) throws IOException {
		int entryCount = 1024;
		for (int i = 0; i < entryCount; i++) {
			ArchiveEntry entry = archive.openEntry("/entry/" + i);
			try {
				assertTrue(entry != null);
				assertEquals(i, entry.getLength());
			} finally {
				entry.close();
			}
		}
	}

	@Test
	public void testFlush() throws IOException {
		ArchiveWriter writer = new ArchiveWriter(ARCHIVE_FILE);
		try {
			RAOutputStream out = writer.createOutputStream("/test");
			try (out) {
				out.writeInt(15);
			}
		} finally {
			writer.finish();
		}

		ArchiveView view = new ArchiveView(VIEW_FILE, ARCHIVE_FILE, "rw");
		try {
			ArchiveReader reader = new ArchiveReader(view);
			// read out the old value
			assertEquals(15, readInt(reader, "/test"));
			writer = new ArchiveWriter(view);
			RAOutputStream out = writer.getOutputStream("/test");
			out.writeInt(30);
			assertEquals(15, readInt(reader, "/test"));
			view.flush();
			assertEquals(30, readInt(reader, "/test"));
			out.close();

			out = writer.createOutputStream("/testnew");
			out.writeInt(30);
			assertEquals(0, getLength(reader, "/testnew"));
			view.flush();
			assertEquals(30, readInt(reader, "/testnew"));
			out.close();

			reader.close();
			writer.finish();
		} finally {
			view.close();
		}
	}

	protected int readInt(ArchiveReader reader, String name) throws IOException {
		RAInputStream in = reader.getInputStream(name);
		try (in) {
			return in.readInt();
		}
	}

	protected int getLength(ArchiveReader reader, String name) throws IOException {
		RAInputStream in = reader.getInputStream(name);
		try (in) {
			return in.available();
		}
	}
}
