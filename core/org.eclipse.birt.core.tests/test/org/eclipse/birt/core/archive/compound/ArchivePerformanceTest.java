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
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

public class ArchivePerformanceTest extends TestCase {

	int STREAM_COUNT = 127;
	int STREAM_SIZE = 4097 * 7;
	int BUFFER_SIZE = 64;

	long readSize = 0;
	long writeSize = 0;

	@Ignore("ignore performance test")
	@Test
	public void testPerformance() throws IOException {
		for (int i = 0; i < 5; i++) {
			removeFile(new File("./utest/"));
			long start, end;
			start = System.currentTimeMillis();
			doFileWrite();
			end = System.currentTimeMillis();
			System.out.println("FILE WRITE:" + (end - start));

			start = System.currentTimeMillis();
			doArchiveWrite();
			end = System.currentTimeMillis();
			System.out.println("ARCHIVE WRITE:" + (end - start));

			/*
			 * start = System.currentTimeMillis( ); doFileRead( ); end =
			 * System.currentTimeMillis( ); System.out.println( "FILE READ:" + ( end - start
			 * ) );
			 *
			 * start = System.currentTimeMillis( ); doArchiveRead( ); end =
			 * System.currentTimeMillis( ); System.out.println( "ARCHIVE READ:" + ( end -
			 * start ) );
			 */

			removeFile(new File("./utest/"));
		}
	}

	void doFileWrite() throws IOException {
		new File("./utest/file").mkdirs();
		for (int i = 0; i < STREAM_COUNT; i++) {
			RandomAccessFile file = new RandomAccessFile("./utest/file/" + i, "rw");
			byte[] buffer = new byte[BUFFER_SIZE];
			long length = 0;
			do {
				int size = (int) Math.round(Math.random() * BUFFER_SIZE);
				if (length + size > STREAM_SIZE) {
					size = (int) (STREAM_SIZE - length);
				}
				file.seek(length);
				file.write(buffer, 0, size);
				writeSize += size;
				length += size;
			} while (length < STREAM_SIZE);
			file.close();
		}
	}

	void doFileRead() throws IOException {
		for (int i = 0; i < STREAM_COUNT; i++) {
			RandomAccessFile file = new RandomAccessFile("./utest/file/" + i, "rw");
			byte[] buffer = new byte[BUFFER_SIZE];
			long offset = 0;
			do {
				int size = (int) Math.round(Math.random() * BUFFER_SIZE);
				if (offset + size > STREAM_SIZE) {
					size = (int) (STREAM_SIZE - offset);
				}
				file.seek(offset);
				offset += file.read(buffer, 0, size);
			} while (offset < STREAM_SIZE);
			readSize += offset;
			file.close();
		}
	}

	void doArchiveRead() throws IOException {
		new File("./utest/").mkdirs();
		ArchiveFile archive = new ArchiveFile("./utest/archive", "r");
		for (int i = 0; i < STREAM_COUNT; i++) {
			ArchiveEntry entry = archive.openEntry("./utest/file/" + i);
			try {
				byte[] buffer = new byte[BUFFER_SIZE];
				long offset = 0;
				do {
					int size = (int) Math.round(Math.random() * BUFFER_SIZE);
					if (offset + size > STREAM_SIZE) {
						size = (int) (STREAM_SIZE - offset);
					}

					entry.read(offset, buffer, 0, size);
					offset += size;
				} while (offset < STREAM_SIZE);
			} finally {
				entry.close();
			}
		}
		archive.close();
	}

	void doArchiveWrite() throws IOException {
		new File("./utest/").mkdirs();
		ArchiveFile archive = new ArchiveFile("./utest/archive", "rw");
		for (int i = 0; i < STREAM_COUNT; i++) {
			ArchiveEntry entry = archive.createEntry("./utest/file/" + i);

			byte[] buffer = new byte[BUFFER_SIZE];
			long length = 0;
			do {
				int size = (int) Math.round(Math.random() * BUFFER_SIZE);
				if (length + size > STREAM_SIZE) {
					size = (int) (STREAM_SIZE - length);
				}

				entry.write(length, buffer, 0, size);
				length += size;
			} while (length < STREAM_SIZE);
		}
		archive.close();
	}

	void removeFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				removeFile(files[i]);
			}
		}
		file.delete();
	}
}
