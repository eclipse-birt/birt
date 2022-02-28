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

package org.eclipse.birt.data.engine.olap.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.junit.Test;

/**
 *
 */

public class DiskIndexTest {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testDiskIndexBytes() throws IOException, DataException {
		int keyNumber = 100;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = 0; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { getBytes(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		for (int i = 0; i < keyNumber; i++) {
			// System.out.println( i );
			key = indexTree.findFirst(new Object[] { getBytes(i) });
			assertEquals(key.getKey()[0], getBytes(i));
			assertEquals(key.getDimensionPos()[0], i);
			assertEquals(key.getOffset()[0], i * 4);
			int iValue = (int) (Math.random() * keyNumber);
			key = indexTree.findFirst(new Object[] { getBytes(iValue) });
			assertEquals(key.getKey()[0], getBytes(iValue));
			assertEquals(key.getDimensionPos()[0], iValue);
			assertEquals(key.getOffset()[0], iValue * 4);
		}
		assertEquals(indexTree.findFirst(new Object[] { getBytes(120) }), null);
		keyList.clear();
		keyList.close();
	}

	private Bytes getBytes(int i) {
		byte[] b;
		b = new byte[3];
		b[0] = (byte) i;
		b[1] = (byte) (i + 1);
		b[2] = (byte) (i + 2);
		return new Bytes(b);
	}

	@Test
	public void testDiskIndexDate() throws IOException, DataException {
		for (int i = 1; i < 10; i++) {
			DiskIndexDateTest(i);
		}
		DiskIndexDateTest(200);
		DiskIndexDateTest(2000);
	}

	private void DiskIndexDateTest(int keyNumber) throws IOException, DataException {
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = 0; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Date(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);

		for (int i = 0; i < keyNumber; i++) {
			key = indexTree.findFirst(new Object[] { new Date(i) });
			assertEquals(key.getKey()[0], new Date(i));
			assertEquals(key.getDimensionPos()[0], i);
			assertEquals(key.getOffset()[0], i * 4);
			int iValue = (int) (Math.random() * keyNumber);
			key = indexTree.findFirst(new Object[] { new Date(iValue) });
			assertEquals(key.getKey()[0], new Date(iValue));
			assertEquals(key.getDimensionPos()[0], iValue);
			assertEquals(key.getOffset()[0], iValue * 4);
		}
		assertEquals(indexTree.findFirst(new Object[] { new Date(-200) }), null);
		assertEquals(indexTree.findFirst(new Object[] { new Date(keyNumber) }), null);
		keyList.clear();
		keyList.close();

	}

	@Test
	public void testDiskIndexInteger() throws IOException, DataException {
		int keyNumber = 10000;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = -100; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Integer(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		System.out.println("create index running  :" + System.currentTimeMillis() / 100);
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		System.out.println("create index finished :" + System.currentTimeMillis() / 100);
		System.out.println("search running    :" + System.currentTimeMillis() / 100);
		for (int i = -100; i < keyNumber; i++) {
			// System.out.println( i );
			key = indexTree.findFirst(new Object[] { new Integer(i) });
			assertEquals(key.getKey()[0], new Integer(i));
			assertEquals(key.getDimensionPos()[0], i);
			assertEquals(key.getOffset()[0], i * 4);
			int iValue = (int) (Math.random() * keyNumber);
			key = indexTree.findFirst(new Object[] { new Integer(iValue) });
			assertEquals(key.getKey()[0], new Integer(iValue));
			assertEquals(key.getDimensionPos()[0], iValue);
			assertEquals(key.getOffset()[0], iValue * 4);
		}
		assertEquals(indexTree.findFirst(new Object[] { new Integer(-200) }), null);
		assertEquals(indexTree.findFirst(new Object[] { new Integer(keyNumber + 200) }), null);
		System.out.println("search finished   :" + System.currentTimeMillis() / 100);
		keyList.clear();
		keyList.close();
	}

	@Test
	public void testDiskIndexInteger1() throws IOException, DataException {
		integerTestForDiskIndex(1);
		integerTestForDiskIndex(2);
		integerTestForDiskIndex(3);
		integerTestForDiskIndex(4);
		integerTestForDiskIndex(6);
		integerTestForDiskIndex(8);
		integerTestForDiskIndex(9);
		integerTestForDiskIndex(10);
	}

	private void integerTestForDiskIndex(int keyNumber) throws IOException, DataException {
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = 0; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Integer(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		for (int i = 0; i < keyNumber; i++) {
			// System.out.println( i );
			key = indexTree.findFirst(new Object[] { new Integer(i) });
			assertEquals(key.getKey()[0], new Integer(i));
			assertEquals(key.getDimensionPos()[0], i);
			assertEquals(key.getOffset()[0], i * 4);
			int iValue = (int) (Math.random() * keyNumber);
			key = indexTree.findFirst(new Object[] { new Integer(iValue) });
			assertEquals(key.getKey()[0], new Integer(iValue));
			assertEquals(key.getDimensionPos()[0], iValue);
			assertEquals(key.getOffset()[0], iValue * 4);
		}
		assertEquals(indexTree.findFirst(new Object[] { new Integer(-100) }), null);
		assertEquals(indexTree.findFirst(new Object[] { new Integer(keyNumber) }), null);
		keyList.clear();
		keyList.close();
	}

	@Test
	public void testDiskIndexInteger2() throws IOException, DataException {
		int keyNumber = 3000;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = -100; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Integer(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		System.out.println("create index running  :" + System.currentTimeMillis() / 100);
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		System.out.println("create index finished :" + System.currentTimeMillis() / 100);
		System.out.println("search running    :" + System.currentTimeMillis() / 100);
		ISelection[] selections = new ISelection[1];
		selections[0] = SelectionFactory.createRangeSelection(new Object[] { new Integer(1) },
				new Object[] { new Integer(100) }, true, false);
		IDiskArray list = indexTree.find(selections);
		assertEquals(list.size(), 99);
		for (int i = 0; i < list.size(); i++) {
			key = (IndexKey) list.get(i);
			assertEquals(key.getKey()[0], new Integer(i + 1));
			assertEquals(key.getDimensionPos()[0], i + 1);
			assertEquals(key.getOffset()[0], (i + 1) * 4);
		}

		selections[0] = SelectionFactory.createRangeSelection(new Object[] { new Integer(-1000) },
				new Object[] { new Integer(100) }, true, false);
		list = indexTree.find(selections);
		assertEquals(list.size(), 200);
		for (int i = 0; i < list.size(); i++) {
			key = (IndexKey) list.get(i);
			assertEquals(key.getKey()[0], new Integer(i - 100));
			assertEquals(key.getDimensionPos()[0], i - 100);
			assertEquals(key.getOffset()[0], (i - 100) * 4);
		}

		selections[0] = SelectionFactory.createRangeSelection(null, new Object[] { new Integer(100) }, true, false);
		list = indexTree.find(selections);
		assertEquals(list.size(), 200);
		for (int i = 0; i < list.size(); i++) {
			key = (IndexKey) list.get(i);
			assertEquals(key.getKey()[0], new Integer(i - 100));
			assertEquals(key.getDimensionPos()[0], i - 100);
			assertEquals(key.getOffset()[0], (i - 100) * 4);
		}

		selections[0] = SelectionFactory.createRangeSelection(new Object[] { new Integer(keyNumber + 1) },
				new Object[] { new Integer(keyNumber + 10) }, true, false);
		list = indexTree.find(selections);
		assertTrue(list == null);
		keyList.clear();
		keyList.close();
		System.out.println("search finished   :" + System.currentTimeMillis() / 100);

	}

	@Test
	public void testDiskIndexInteger3() throws IOException, DataException {
		int keyNumber = 3000;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = -100; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Integer(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		System.out.println("create index running  :" + System.currentTimeMillis() / 100);
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		System.out.println("create index finished :" + System.currentTimeMillis() / 100);
		System.out.println("search running    :" + System.currentTimeMillis() / 100);

		Object[][] selectedObjs = { { new Integer(-101) }, { new Integer(-100) }, { new Integer(-32) },
				{ new Integer(-10) }, { new Integer(0) }, { new Integer(1) }, { new Integer(81) },
				{ new Integer(keyNumber - 1) }, { new Integer(keyNumber) }, { new Integer(keyNumber + 1) } };

		int[] resultObjs = { -100, -32, -10, 0, 1, 81, keyNumber - 1 };

		ISelection[] selections = new ISelection[1];
		selections[0] = SelectionFactory.createMutiKeySelection(selectedObjs);

		IDiskArray list = indexTree.find(selections);
		assertEquals(list.size(), resultObjs.length);
		for (int i = 0; i < list.size(); i++) {
			key = (IndexKey) list.get(i);
			assertEquals(key.getKey()[0], new Integer(resultObjs[i]));
			assertEquals(key.getDimensionPos()[0], resultObjs[i]);
			assertEquals(key.getOffset()[0], (resultObjs[i]) * 4);
		}
		list.clear();
		list.close();
		keyList.clear();
		keyList.close();
		System.out.println("search finished   :" + System.currentTimeMillis() / 100);
	}

	@Test
	public void testDiskIndexInteger4() throws IOException, DataException {
		int keyNumber = 100000;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = -100; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Integer(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		System.out.println("create index running  :" + System.currentTimeMillis() / 100);
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		System.out.println("create index finished :" + System.currentTimeMillis() / 100);
		Object[][] selectedObjs1 = { { new Integer(-101) }, { new Integer(-100) }, { new Integer(-32) },
				{ new Integer(-10) }, { new Integer(0) }, { new Integer(1) }, { new Integer(81) }, };
		Object[][] selectedObjs2 = { { new Integer(keyNumber - 1) }, { new Integer(keyNumber) },
				{ new Integer(keyNumber + 1) } };

		int[] resultObjs1 = { -100, -32, -10, 0, 1, 81 };
		int[] resultObjs2 = { keyNumber - 1 };

		ISelection[] selections = new ISelection[5];
		selections[0] = SelectionFactory.createMutiKeySelection(selectedObjs1);
		selections[1] = SelectionFactory.createRangeSelection(new Object[] { new Integer(1000) },
				new Object[] { new Integer(1100) }, true, true);
		selections[2] = SelectionFactory.createRangeSelection(new Object[] { new Integer(1101) },
				new Object[] { new Integer(2001) }, false, true);
		selections[3] = SelectionFactory.createRangeSelection(new Object[] { new Integer(10000) },
				new Object[] { new Integer(12001) }, true, false);
		selections[4] = SelectionFactory.createMutiKeySelection(selectedObjs2);
		System.out.println("search running    :" + System.currentTimeMillis() / 100);
		IDiskArray list = null;
		for (int i = 0; i < 1; i++) {
			list = indexTree.find(selections);
		}
		System.out.println("search finished   :" + System.currentTimeMillis() / 100);
		assertEquals(list.size(), resultObjs1.length + 101 + 900 + 2001 + resultObjs2.length);

		for (int i = 0; i < resultObjs1.length; i++) {
			key = (IndexKey) list.get(i);
			assertEquals(key.getKey()[0], new Integer(resultObjs1[i]));
			assertEquals(key.getDimensionPos()[0], resultObjs1[i]);
			assertEquals(key.getOffset()[0], (resultObjs1[i]) * 4);
		}

		for (int i = 0; i < 101; i++) {
			key = (IndexKey) list.get(resultObjs1.length + i);
			assertEquals(key.getKey()[0], new Integer(1000 + i));
			assertEquals(key.getDimensionPos()[0], 1000 + i);
			assertEquals(key.getOffset()[0], (1000 + i) * 4);
		}

		for (int i = 0; i < 900; i++) {
			key = (IndexKey) list.get(resultObjs1.length + 101 + i);
			assertEquals(key.getKey()[0], new Integer(1102 + i));
			assertEquals(key.getDimensionPos()[0], 1102 + i);
			assertEquals(key.getOffset()[0], (1102 + i) * 4);
		}

		for (int i = 0; i < 2001; i++) {
			key = (IndexKey) list.get(resultObjs1.length + 101 + 900 + i);
			assertEquals(key.getKey()[0], new Integer(10000 + i));
			assertEquals(key.getDimensionPos()[0], 10000 + i);
			assertEquals(key.getOffset()[0], (10000 + i) * 4);
		}

		for (int i = 0; i < resultObjs2.length; i++) {
			key = (IndexKey) list.get(resultObjs1.length + 101 + 900 + 2001 + i);
			assertEquals(key.getKey()[0], new Integer(resultObjs2[i]));
			assertEquals(key.getDimensionPos()[0], resultObjs2[i]);
			assertEquals(key.getOffset()[0], (resultObjs2[i]) * 4);
		}
		list.clear();
		list.close();
		keyList.clear();
		keyList.close();
	}

	@Test
	public void testDiskIndexInteger5() throws IOException, DataException {
		int keyNumber = 1000;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = 0; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Integer(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		key = new IndexKey();
		key.setKey(new Object[] { new Integer(81) });
		key.setDimensionPos(new int[] { keyNumber });
		key.setOffset(new int[] { keyNumber * 4 });
		keyList.add(key);
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		System.out.println("create index finished :" + System.currentTimeMillis() / 100);
		Object[][] selectedObjs1 = { { new Integer(0) }, { new Integer(81) }, };
		Object[][] selectedObjs2 = { { new Integer(keyNumber - 1) }, { new Integer(keyNumber) },
				{ new Integer(keyNumber + 1) } };

		ISelection[] selections = new ISelection[3];
		selections[0] = SelectionFactory.createMutiKeySelection(selectedObjs1);
		selections[1] = SelectionFactory.createRangeSelection(new Object[] { new Integer(81) },
				new Object[] { new Integer(90) }, true, true);
		selections[2] = SelectionFactory.createMutiKeySelection(selectedObjs2);
		System.out.println("search running    :" + System.currentTimeMillis() / 100);
		IDiskArray list = null;
		for (int i = 0; i < 1; i++) {
			list = indexTree.find(selections);
		}

		key = (IndexKey) list.get(0);
		assertEquals(key.getKey()[0], new Integer(0));
		assertEquals(key.getDimensionPos()[0], 0);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(1);
		assertEquals(key.getKey()[0], new Integer(81));
		assertEquals(key.getDimensionPos()[0], 81);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(1);
		assertEquals(key.getKey()[0], new Integer(81));
		assertEquals(key.getDimensionPos()[1], keyNumber);
		assertEquals(key.getOffset()[1], (key.getDimensionPos()[1]) * 4);
		list.clear();
		list.close();
		keyList.clear();
		keyList.close();
	}

	@Test
	public void testDiskIndexTopBottom() throws IOException, DataException {
		int keyNumber = 1000;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), keyNumber);
		IndexKey key = null;
		for (int i = 0; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { new Integer(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		key = new IndexKey();
		key.setKey(new Object[] { new Integer(1001) });
		key.setDimensionPos(new int[] { 1001 });
		key.setOffset(new int[] { 1001 * 4 });
		keyList.add(key);
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);

		IDiskArray list;

		list = indexTree.topN(1);
		assertEquals(list.size(), 1);
		key = (IndexKey) list.get(0);
		assertEquals(key.getKey()[0], new Integer(1001));
		assertEquals(key.getDimensionPos()[0], 1001);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);

		list = indexTree.topN(3);
		assertEquals(list.size(), 3);
		key = (IndexKey) list.get(0);
		assertEquals(key.getKey()[0], new Integer(998));
		assertEquals(key.getDimensionPos()[0], 998);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(1);
		assertEquals(key.getKey()[0], new Integer(999));
		assertEquals(key.getDimensionPos()[0], 999);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(2);
		assertEquals(key.getKey()[0], new Integer(1001));
		assertEquals(key.getDimensionPos()[0], 1001);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);

		list = indexTree.topPercent(0.003);

		assertEquals(list.size(), 3);
		key = (IndexKey) list.get(0);
		assertEquals(key.getKey()[0], new Integer(998));
		assertEquals(key.getDimensionPos()[0], 998);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(1);
		assertEquals(key.getKey()[0], new Integer(999));
		assertEquals(key.getDimensionPos()[0], 999);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(2);
		assertEquals(key.getKey()[0], new Integer(1001));
		assertEquals(key.getDimensionPos()[0], 1001);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);

		list = indexTree.bottomN(3);

		assertEquals(list.size(), 3);
		key = (IndexKey) list.get(0);
		assertEquals(key.getKey()[0], new Integer(0));
		assertEquals(key.getDimensionPos()[0], 0);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(1);
		assertEquals(key.getKey()[0], new Integer(1));
		assertEquals(key.getDimensionPos()[0], 1);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(2);
		assertEquals(key.getKey()[0], new Integer(2));
		assertEquals(key.getDimensionPos()[0], 2);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);

		list = indexTree.bottomPercent(0.003);

		assertEquals(list.size(), 3);
		key = (IndexKey) list.get(0);
		assertEquals(key.getKey()[0], new Integer(0));
		assertEquals(key.getDimensionPos()[0], 0);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(1);
		assertEquals(key.getKey()[0], new Integer(1));
		assertEquals(key.getDimensionPos()[0], 1);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		key = (IndexKey) list.get(2);
		assertEquals(key.getKey()[0], new Integer(2));
		assertEquals(key.getDimensionPos()[0], 2);
		assertEquals(key.getOffset()[0], (key.getDimensionPos()[0]) * 4);
		list.clear();
		list.close();
		keyList.clear();
		keyList.close();
	}

	@Test
	public void testDiskIndexString4() throws IOException, DataException {
		int keyNumber = 100000;
		BufferedStructureArray keyList = new BufferedStructureArray(IndexKey.getCreator(), 4000);
		IndexKey key = null;
		for (int i = 1; i < keyNumber; i++) {
			key = new IndexKey();
			key.setKey(new Object[] { "abcdefghijklmno" + Integer.toString(i) });
			key.setDimensionPos(new int[] { i });
			key.setOffset(new int[] { i * 4 });
			keyList.add(key);
		}
		System.out.println("create index running  :" + System.currentTimeMillis() / 100);
		DiskIndex indexTree = DiskIndex.createIndex(DocumentManagerFactory.createFileDocumentManager(), "student_index",
				3, keyList, false);
		System.out.println("create index finished :" + System.currentTimeMillis() / 100);
		Object[][] selectedObjs1 = { { "abcdefghijklmno" + Integer.toString(0) },
				{ "abcdefghijklmno" + Integer.toString(100) }, { "abcdefghijklmno" + Integer.toString(101) },
				{ "abcdefghijklmno" + Integer.toString(102) }, { "abcdefghijklmno" + Integer.toString(110) },
				{ "abcdefghijklmno" + Integer.toString(121) }, { "abcdefghijklmno" + Integer.toString(122) },
				{ "abcdefghijklmno" + Integer.toString(134) } };
		Object[][] selectedObjs2 = { { "abcdefghijklmno" + Integer.toString(9999) },
				{ "abcdefghijklmno" + Integer.toString(99999) }, { "abcdefghijklmno" + Integer.toString(999999) } };

		int[] resultObjs1 = { 100, 101, 102, 110, 121, 122, 134 };
		int[] resultObjs2 = { 9999, 99999 };

		ISelection[] selections = new ISelection[5];
		selections[0] = SelectionFactory.createMutiKeySelection(selectedObjs1);
		selections[1] = SelectionFactory.createRangeSelection(
				new Object[] { "abcdefghijklmno" + Integer.toString(20000) },
				new Object[] { "abcdefghijklmno" + Integer.toString(20100) }, true, true);
		selections[2] = SelectionFactory.createRangeSelection(
				new Object[] { "abcdefghijklmno" + Integer.toString(30000) },
				new Object[] { "abcdefghijklmno" + Integer.toString(30900) }, false, true);
		selections[3] = SelectionFactory.createRangeSelection(
				new Object[] { "abcdefghijklmno" + Integer.toString(40000) },
				new Object[] { "abcdefghijklmno" + Integer.toString(42001) }, true, false);
		selections[4] = SelectionFactory.createMutiKeySelection(selectedObjs2);
		System.out.println("search running    :" + System.currentTimeMillis() / 100);
		IDiskArray list = null;
		for (int i = 0; i < 1; i++) {
			list = indexTree.find(selections);
		}
		System.out.println("search finished   :" + System.currentTimeMillis() / 100);
		assertEquals(list.size(), 3343);

		for (int i = 0; i < resultObjs1.length; i++) {
			key = (IndexKey) list.get(i);
			assertEquals(key.getKey()[0], "abcdefghijklmno" + Integer.toString(resultObjs1[i]));
			assertEquals(key.getDimensionPos()[0], resultObjs1[i]);
			assertEquals(key.getOffset()[0], (resultObjs1[i]) * 4);
		}

		for (int i = 0; i < 101; i++) {
			key = (IndexKey) list.get(resultObjs1.length + i);
			assertEquals(key.getDimensionPos()[0], Integer.parseInt(key.getKey()[0].toString().substring(15)));
			assertEquals(key.getOffset()[0], (Integer.parseInt(key.getKey()[0].toString().substring(15))) * 4);
		}

		for (int i = 0; i < resultObjs2.length; i++) {
			key = (IndexKey) list.get(list.size() - resultObjs2.length + i);
			assertEquals(key.getKey()[0], "abcdefghijklmno" + Integer.toString(resultObjs2[i]));
			assertEquals(key.getDimensionPos()[0], resultObjs2[i]);
			assertEquals(key.getOffset()[0], (resultObjs2[i]) * 4);
		}
		list.clear();
		list.close();
		keyList.clear();
		keyList.close();
	}
}
