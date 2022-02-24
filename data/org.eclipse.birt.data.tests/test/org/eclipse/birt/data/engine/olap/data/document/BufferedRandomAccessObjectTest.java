
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.document.BlockRandomAccessObject;
import org.eclipse.birt.data.engine.olap.data.document.BufferedRandomDataAccessObject;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.document.IObjectAllocTable;
import org.eclipse.birt.data.engine.olap.data.document.SimpleRandomAccessObject;
import org.eclipse.birt.data.engine.olap.data.util.BufferedRandomAccessFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class BufferedRandomAccessObjectTest {
	private static final String tmpPath = System.getProperty("java.io.tmpdir");

	IDocumentManager documentManager = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void bufferedRandomAccessObjectSetUp() throws Exception {
		documentManager = DocumentManagerFactory.createFileDocumentManager();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
	public void bufferedRandomAccessObjectTearDown() throws Exception {
		documentManager.close();
	}

	@Test
	public void testInteger() throws IOException {
		int objectNumber = 1001;
		assertTrue(documentManager.createDocumentObject("testInteger") != null);
		IDocumentObject documentObject = documentManager.openDocumentObject("testInteger");
		for (int i = 0; i < objectNumber; i++) {
			documentObject.writeInt(i);
		}
		documentObject.seek(0);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(documentObject.readInt(), i);
		}
		documentObject.seek(400);
		assertEquals(documentObject.readInt(), 100);
		documentObject.seek(804);
		assertEquals(documentObject.readInt(), 201);
		assertEquals(documentObject.readInt(), 202);
		documentObject.seek(2804);
		documentObject.writeInt(1000001);
		assertEquals(documentObject.readInt(), 702);
		documentObject.seek(2804);
		assertEquals(documentObject.readInt(), 1000001);
		documentObject.close();
	}

	@Test
	public void testInteger1() throws IOException {
		int objectNumber = 1001;
		BufferedRandomDataAccessObject documentObject = new BufferedRandomDataAccessObject(
				new SimpleRandomAccessObject(new File(tmpPath + File.separatorChar + "testInteger1"), "rw"), 1024);
		for (int i = 0; i < objectNumber; i++) {
			documentObject.writeInt(i);
		}
		documentObject.seek(0);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(documentObject.readInt(), i);
		}
		documentObject.seek(400);
		assertEquals(documentObject.readInt(), 100);
		documentObject.seek(804);
		assertEquals(documentObject.readInt(), 201);
		assertEquals(documentObject.readInt(), 202);
		documentObject.seek(2804);
		documentObject.writeInt(1000001);
		assertEquals(documentObject.readInt(), 702);
		documentObject.seek(2804);
		assertEquals(documentObject.readInt(), 1000001);
		documentObject.close();
	}

	@Test
	public void testLong() throws IOException {
		int objectNumber = 1001;
		BufferedRandomDataAccessObject documentObject = new BufferedRandomDataAccessObject(
				new SimpleRandomAccessObject(new File(tmpPath + File.separatorChar + "testInteger1"), "rw"), 1024);
		for (int i = 0; i < objectNumber; i++) {
			documentObject.writeLong(i);
		}
		documentObject.seek(0);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(documentObject.readLong(), i);
		}
		documentObject.seek(800);
		assertEquals(documentObject.readLong(), 100);
		documentObject.seek(1608);
		assertEquals(documentObject.readLong(), 201);
		assertEquals(documentObject.readLong(), 202);
		documentObject.seek(5608);
		documentObject.writeLong(1000001);
		assertEquals(documentObject.readLong(), 702);
		documentObject.seek(5608);
		assertEquals(documentObject.readLong(), 1000001);
		documentObject.close();
	}

	@Test
	public void testInteger2() throws IOException {
		BlockRandomAccessObject documentObject = new BlockRandomAccessObject(
				new BufferedRandomAccessFile(new File(tmpPath + File.separatorChar + "testInteger1"), "rw", 1024),
				"testInteger2", 0, 0, new DocumentObjectAllocatedTable());
		byte[] bytes = new byte[1024];
		bytes[0] = 1;
		bytes[1] = 2;
		documentObject.seek(0);
		documentObject.write(bytes, 0, bytes.length);
		documentObject.write(bytes, 0, bytes.length);
		documentObject.write(bytes, 0, bytes.length);
		bytes = new byte[932];
		documentObject.write(bytes, 0, bytes.length);

		bytes = new byte[1024];
		documentObject.seek(0);
		assertEquals(documentObject.read(bytes, 0, bytes.length), 1024);
		assertEquals(bytes[0], 1);
		assertEquals(bytes[1], 2);
		documentObject.close();
	}

	@Test
	public void testInteger3() throws IOException {
		BlockRandomAccessObject documentObject = new BlockRandomAccessObject(
				new BufferedRandomAccessFile(new File(tmpPath + File.separatorChar + "testInteger1"), "rw", 1024),
				"testInteger2", 0, 0, new DocumentObjectAllocatedTable());
		byte[] bytes = new byte[1024];
		bytes[0] = 1;
		bytes[1] = 2;
		documentObject.seek(0);
		documentObject.write(bytes, 0, bytes.length);
		documentObject.write(bytes, 0, bytes.length);
		documentObject.write(bytes, 0, bytes.length);
		bytes = new byte[932];
		documentObject.write(bytes, 0, bytes.length);

		assertEquals(documentObject.read(bytes, 0, bytes.length), -1);
		documentObject.close();
	}

	@Test
	public void testByteA() throws IOException {
		BlockRandomAccessObject documentObject = new BlockRandomAccessObject(
				new BufferedRandomAccessFile(new File(tmpPath + File.separatorChar + "testByteA1"), "rw", 1024),
				"testByteA2", 0, 0, new DocumentObjectAllocatedTable());
		byte[] bytes = new byte[15978];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (i % 128);
		}
		documentObject.seek(0);
		documentObject.write(bytes, 0, bytes.length);
		documentObject.seek(0);
		byte[] tBytes = new byte[4096];
		assertEquals(documentObject.read(tBytes, 0, 10), 10);
		assertEquals(documentObject.read(tBytes, 10, tBytes.length - 10), tBytes.length - 10);
		System.arraycopy(tBytes, 0, bytes, 0, tBytes.length);
		assertEquals(documentObject.read(tBytes, 0, tBytes.length), tBytes.length);
		System.arraycopy(tBytes, 0, bytes, tBytes.length, tBytes.length);
		assertEquals(documentObject.read(tBytes, 0, tBytes.length), tBytes.length);
		System.arraycopy(tBytes, 0, bytes, tBytes.length * 2, tBytes.length);
		assertEquals(documentObject.read(tBytes, 0, tBytes.length), bytes.length - 3 * tBytes.length);
		System.arraycopy(tBytes, 0, bytes, tBytes.length * 3, bytes.length - 3 * tBytes.length);

		for (int i = 0; i < bytes.length; i++) {
			assertEquals(bytes[i], (byte) (i % 128));
		}
		documentObject.close();
	}

	@Test
	public void testString() throws IOException {
		int objectNumber = 3000;
		assertTrue(documentManager.createDocumentObject("testString") != null);
		IDocumentObject documentObject = documentManager.openDocumentObject("testString");
		for (int i = 0; i < objectNumber; i++) {
			documentObject.writeString("string" + i);
		}
		documentObject.seek(0);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(documentObject.readString(), "string" + i);
		}
		documentObject.close();
	}

	@Test
	public void testBigDecimal() throws IOException {
		int objectNumber = 3000;
		assertTrue(documentManager.createDocumentObject("testBigDecimal") != null);
		IDocumentObject documentObject = documentManager.openDocumentObject("testBigDecimal");
		for (int i = 0; i < objectNumber; i++) {
			documentObject.writeBigDecimal(new BigDecimal("1010101010101010101010" + i));
		}
		documentObject.seek(0);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(documentObject.readBigDecimal(), new BigDecimal("1010101010101010101010" + i));
		}
		documentObject.close();
	}

	@Test
	public void testDate() throws IOException {
		int objectNumber = 4101;
		assertTrue(documentManager.createDocumentObject("testDate") != null);
		IDocumentObject documentObject = documentManager.openDocumentObject("testDate");
		for (int i = 0; i < objectNumber; i++) {
			documentObject.writeDate(new Date(1900100000 + i * 1000));
		}
		documentObject.seek(0);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(documentObject.readDate(), new Date(1900100000 + i * 1000));
		}
		documentObject.close();
	}

	@Test
	public void testMixed() throws IOException {
		int objectNumber = 1001;
		assertTrue(documentManager.createDocumentObject("testMixed") != null);
		IDocumentObject documentObject = documentManager.openDocumentObject("testMixed");
		for (int i = 0; i < objectNumber; i++) {
			documentObject.writeInt(i);
		}
		documentObject.seek(0);
		for (int i = 0; i < objectNumber; i++) {
			assertEquals(documentObject.readInt(), i);
		}
		documentObject.writeBigDecimal(new BigDecimal("1010101010101"));
		documentObject.writeDate(new Date(12202000));
		documentObject.writeString("testString");
		documentObject.writeShort(1300);
		documentObject.writeInt(30000011);
		// write object
		documentObject.writeObject(new StringBuffer("s1"));
		documentObject.seek(0);
		documentObject.skipBytes(objectNumber * 4);
		assertEquals(documentObject.readBigDecimal(), new BigDecimal("1010101010101"));
		assertEquals(documentObject.readDate(), new Date(12202000));
		assertEquals(documentObject.readString(), "testString");
		assertEquals(documentObject.readShort(), 1300);
		assertEquals(documentObject.readInt(), 30000011);
		Object o = documentObject.readObject();
		assertTrue(o instanceof StringBuffer);
		assertEquals(o.toString(), "s1");
	}
}

class DocumentObjectAllocatedTable implements IObjectAllocTable {
	int maxBlockNumber = 0;

	public int allocateBlock(int blockNumber) throws IOException {
		maxBlockNumber = Math.max(maxBlockNumber, blockNumber + 1);
		return blockNumber + 1;
	}

	public int getNextBlock(int blockNumber) throws IOException {
		if (blockNumber + 1 > maxBlockNumber)
			return 0;
		else
			return blockNumber + 1;
	}

	public void setObjectLength(String name, long length) throws IOException {

	}

}
