/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.junit.Test;

import junit.framework.TestCase;

public class BufferTest extends TestCase {

	static int TEST_COUNT = 1024;
	static int LENGTH = 1024 * 1024;
	static String ENTRY_NAME = "/test/abc.dat";
	static String ARCHIVE_NAME = "datafile.dat";

	@Test
	public void testBuffer() throws IOException {
		ArchiveFile af = new ArchiveFile(ARCHIVE_NAME, "rwt");
		try {
			ArchiveWriter writer = new ArchiveWriter(af);
			ArchiveReader reader = new ArchiveReader(af);
			System.out.println("warm....");
			testWriteByte(writer);
			testReadByte(reader);
			testWriteInteger(writer);
			testReadInteger(reader);
			testWriteLong(writer);
			testReadLong(reader);
			testWriteBytes(writer);
			testReadBytes(reader);
			System.out.println();

			// testByte( writer, reader );
			// testInteger( writer, reader );
			// testLong( writer, reader );
			// testBytes( writer, reader );

		} finally {
			af.close();
			new File(ARCHIVE_NAME).delete();
		}
	}

	static void testByte(ArchiveWriter writer, ArchiveReader reader) throws IOException {
		long start, end;
		System.out.print("byte");
		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testWriteByte(writer);
		}
		end = System.currentTimeMillis();
		System.out.print("\twrite:" + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testReadByte(reader);
		}
		end = System.currentTimeMillis();
		System.out.println("\tread:" + (end - start));
	}

	static void testWriteByte(ArchiveWriter writer) throws IOException {
		RAOutputStream out = writer.createOutputStream(ENTRY_NAME);
		try {
			for (int i = 0; i < LENGTH; i++) {
				out.write(i);
			}
		} finally {
			out.close();
		}
	}

	static void testReadByte(ArchiveReader reader) throws IOException {
		RAInputStream in = reader.getInputStream(ENTRY_NAME);
		try {
			for (int i = 0; i < LENGTH; i++) {
				int v = in.read();
			}
		} finally {
			in.close();
		}
	}

	static void testInteger(ArchiveWriter writer, ArchiveReader reader) throws IOException {
		long start, end;
		System.out.print("integer");
		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testWriteInteger(writer);
		}
		end = System.currentTimeMillis();
		System.out.print("\twrite:" + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testReadInteger(reader);
		}
		end = System.currentTimeMillis();
		System.out.println("\tread:" + (end - start));

	}

	static void testWriteInteger(ArchiveWriter writer) throws IOException {
		RAOutputStream out = writer.createOutputStream(ENTRY_NAME);
		try {
			int length = LENGTH / 4;
			for (int i = 0; i < length; i++) {
				out.writeInt(i);
			}
		} finally {
			out.close();
		}
	}

	static void testReadInteger(ArchiveReader reader) throws IOException {
		RAInputStream in = reader.getInputStream(ENTRY_NAME);
		try {
			int count = 0;
			try {
				while (true) {
					int v = in.readInt();
					count++;
				}
			} catch (EOFException ex) {
			}
			if (count != LENGTH / 4) {
				System.out.print("x");
			}
		} finally {
			in.close();
		}
	}

	static void testLong(ArchiveWriter writer, ArchiveReader reader) throws IOException {
		long start, end;
		System.out.print("long");
		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testWriteLong(writer);
		}
		end = System.currentTimeMillis();
		System.out.print("\twrite:" + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testReadLong(reader);
		}
		end = System.currentTimeMillis();
		System.out.println("\tread:" + (end - start));
	}

	static void testWriteLong(ArchiveWriter writer) throws IOException {
		RAOutputStream out = writer.createOutputStream(ENTRY_NAME);
		try {
			int length = LENGTH / 8;
			for (int i = 0; i < length; i++) {
				out.writeLong(i);
			}
		} finally {
			out.close();
		}
	}

	static void testReadLong(ArchiveReader reader) throws IOException {
		RAInputStream in = reader.getInputStream(ENTRY_NAME);
		try {
			int count = 0;
			try {
				while (true) {
					long v = in.readLong();
					count++;
				}
			} catch (EOFException ex) {
			}
			if (count != LENGTH / 8) {
				System.out.print("x");
			}
		} finally {
			in.close();
		}
	}

	protected static void testBytes(ArchiveWriter writer, ArchiveReader reader) throws IOException {
		long start, end;
		System.out.print("bytes");
		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testWriteBytes(writer);
		}
		end = System.currentTimeMillis();
		System.out.print("\twrite:" + (end - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < TEST_COUNT; i++) {
			testReadBytes(reader);
		}
		end = System.currentTimeMillis();
		System.out.println("\tread:" + (end - start));

	}

	static int[] sizes = new int[] { 1, 7, 13, 31, 61, 113, 251, 509, 1021, 2039, 4091, 4093 };

	static void testWriteBytes(ArchiveWriter writer) throws IOException {
		RAOutputStream out = writer.createOutputStream(ENTRY_NAME);
		try {
			byte[] data = new byte[8192];
			int length = 0;
			int count = 0;
			while (length < LENGTH) {
				int size = sizes[count % sizes.length];
				out.write(data, 0, size);
				length += size;
				count++;
			}
		} finally {
			out.close();
		}
	}

	static void testReadBytes(ArchiveReader reader) throws IOException {
		RAInputStream in = reader.getInputStream(ENTRY_NAME);
		try {

			byte[] data = new byte[8192];
			int length = 0;
			int count = 0;
			while (length < LENGTH) {
				int size = sizes[count % sizes.length];
				in.readFully(data, 0, size);
				count++;
				length += size;
			}
		} finally {
			in.close();
		}
	}

}
