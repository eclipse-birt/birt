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

package org.eclipse.birt.report.engine.internal.index.v2;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;

public class IndexReadWriteTest extends TestCase {

	static final String ARCHIVE_NAME = "./utest/index.dat";
	static final String ENTRY_NAME = "/test";

	public void testInline() throws Exception {
		int entryCount = IndexReader.MAX_INLINE_ENTIRES;
		ArchiveFile af = new ArchiveFile(ARCHIVE_NAME, "rw");
		try {
			IndexWriter writer = new IndexWriter(new ArchiveWriter(af), ENTRY_NAME);
			try {
				for (int i = 0; i < entryCount; i++) {
					writer.add(String.valueOf(i), Long.valueOf(i));
				}
			} finally {
				writer.close();
			}

			IndexReader reader = new IndexReader(new ArchiveReader(af), ENTRY_NAME);
			try {
				for (int i = 0; i < entryCount; i++) {
					Long value = reader.getLong(String.valueOf(i));
					assertEquals(i, value.intValue());
				}
			} finally {

				reader.close();
			}

		}

		finally {
			af.close();
		}

	}

	public void testExternal() throws Exception {
		int entryCount = IndexReader.MAX_INLINE_ENTIRES + 1;
		ArchiveFile af = new ArchiveFile(ARCHIVE_NAME, "rw");
		try {
			IndexWriter writer = new IndexWriter(new ArchiveWriter(af), ENTRY_NAME);
			try {
				for (int i = 0; i < entryCount; i++) {
					writer.add(String.valueOf(i), Long.valueOf(i));
				}
			} finally {
				writer.close();
			}

			IndexReader reader = new IndexReader(new ArchiveReader(af), ENTRY_NAME);
			try {
				for (int i = 0; i < entryCount; i++) {
					Long value = reader.getLong(String.valueOf(i));
					assertEquals(i, value.intValue());
				}
			} finally {

				reader.close();
			}
		}

		finally {
			af.close();
		}
	}
}
