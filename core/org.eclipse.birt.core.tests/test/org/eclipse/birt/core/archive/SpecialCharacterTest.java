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

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class SpecialCharacterTest extends TestCase {

	/**
	 * test stream name contains invalid characters, such as: :, ::, %, ., .., /, \,
	 * //, \\
	 * 
	 * @throws Exception
	 */
	public void doTest(IDocArchiveWriter writer, IDocArchiveReader reader) throws IOException {
		final String[] fileNames = new String[] { "d:/abc.txt", "../abc/txt", "./txt", "...txt", "....txt", ".//.txt",
				"%25.txt", ":.txt", "::", "\\\\.txt", "\\.txt" };

		for (String fileName : fileNames) {
			RAOutputStream ws = writer.createRandomAccessStream(fileName);
			ws.write(fileName.getBytes("utf-8"));
			ws.close();
		}

		for (String fileName : fileNames) {
			RAInputStream rs = reader.getInputStream(fileName);
			byte[] b = new byte[rs.available()];
			rs.read(b);
			assertEquals(new String(b, "utf-8"), fileName);
		}
	}

	@Before
	@After
	public void removeDirectory() {
		ArchiveUtil.deleteAllFiles(new File("./utest"));
	}

	@Test
	public void testFolderReaderWriter() throws IOException {
		FolderArchiveWriter writer = new FolderArchiveWriter("./utest/folder.rw");
		FolderArchiveReader reader = new FolderArchiveReader("./utest/folder.rw");
		try {
			doTest(writer, reader);
		} finally {
			reader.close();
			writer.close();
		}
	}

	@Test
	public void testFolderArchive() throws IOException {
		FolderArchive archive = new FolderArchive("./utest/folder.archive");
		try {
			doTest(archive, archive);
		} finally {
			archive.close();
		}

	}

	@Test
	public void testFileArchive() throws IOException {
		ArchiveFile af = new ArchiveFile("./utest/archive.test", "rwt");
		IDocArchiveReader reader = new ArchiveReader(af);
		IDocArchiveWriter writer = new ArchiveWriter(af);
		try {
			doTest(writer, reader);
		} finally {
			af.close();
		}
	}
}
