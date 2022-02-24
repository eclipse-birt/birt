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

package org.eclipse.birt.core.archive;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.archive.compound.ArchiveEntry;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class FolderToArchiveTest extends TestCase {

	@Before
	@After
	public void removeDirectory() {
		ArchiveUtil.deleteAllFiles(new File("./utest"));
	}

	/**
	 * create a folder archive and save it as file archive.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSave() throws IOException {
		createFolderArchive("utest/test.folder");
		ArchiveUtil.convertFolderArchive("utest/test.folder", "utest/test.archive");
		checkArchive("utest/test.archive");
	}

	// test streams with reserved name, folder name, special characters
	private String[] entryNames = new String[] { "/.metadata", "/abc.txt", "/abc.txt/abc.txt", "/d:/dataset%%20.txt",
			"//host/e:/../<>|$@.", "//", "///" };

	private void createFolderArchive(String folder) throws IOException {

		ArchiveUtilTest.removeFile(new File(folder));

		FolderArchiveFile archive = new FolderArchiveFile(folder);
		try {
			archive.setSystemId("systemId");
			archive.setDependId("dependedId");
			for (String entryName : entryNames) {
				ArchiveEntry entry = archive.createEntry(entryName);
				try {
					byte[] bytes = entryName.getBytes("utf-8");
					entry.write(0, bytes, 0, bytes.length);
				} finally {
					entry.close();
				}
			}
		} finally {
			archive.close();
		}
	}

	private void checkArchive(String file) throws IOException {
		ArchiveFile af = new ArchiveFile(file, "r");
		try {
			assertEquals("systemId", af.getSystemId());
			assertEquals("dependedId", af.getDependId());
			List<String> entries = af.listEntries("/");
			assertEquals(entryNames.length, entries.size());
			for (String entryName : entryNames) {
				ArchiveEntry entry = af.openEntry(entryName);
				try {
					byte[] golden = entryName.getBytes("utf-8");
					assertEquals(golden.length, entry.getLength());
					byte[] bytes = new byte[golden.length];
					entry.read(0, bytes, 0, bytes.length);
					assertArrayEquals(golden, bytes);
				} finally {
					entry.close();
				}
			}
		} finally {
			af.close();
		}

	}

}
