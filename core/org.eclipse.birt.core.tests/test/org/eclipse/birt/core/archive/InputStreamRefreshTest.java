/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.File;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class InputStreamRefreshTest extends TestCase {

	static final String ARCHIVE_NAME = "./utest/test.archive";
	static final String STREAM_NAME = "/teststream";

	@Before
	public void setUp() {
		ArchiveUtil.deleteAllFiles(new File(ARCHIVE_NAME));
	}

	@After
	public void tearDown() {
		ArchiveUtil.deleteAllFiles(new File(ARCHIVE_NAME));
	}

	@Test
	public void testReaderDuringWriter() throws Exception {
		ArchiveFile archive = new ArchiveFile(ARCHIVE_NAME, "rw");
		ArchiveWriter writer = new ArchiveWriter(archive);
		writer.initialize();
		ArchiveReader reader = new ArchiveReader(archive);
		reader.open();
		RAOutputStream ws = writer.createRandomAccessStream(STREAM_NAME);
		ws.writeInt(1);
		ws.flush();
		RAInputStream rs = reader.getStream(STREAM_NAME);
		assertEquals(1, rs.readInt());
		ws.seek(0);
		ws.writeLong(2L);
		ws.flush();
		// test refresh
		rs.refresh();
		rs.seek(0);
		assertEquals(2L, rs.readLong());

		rs.close();
		ws.close();
		reader.close();
		writer.finish();
		archive.close();
	}
}
