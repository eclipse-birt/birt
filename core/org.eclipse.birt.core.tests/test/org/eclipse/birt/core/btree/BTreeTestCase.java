/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.core.btree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import junit.framework.TestCase;

abstract public class BTreeTestCase extends TestCase {

	static final String BTREE_INPUT_RESOURCE = "org/eclipse/birt/core/btree/btree.input.txt";

	BTree createBTree() throws Exception {
		BTree btree = new BTree();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(BTREE_INPUT_RESOURCE);
		try (in) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			while (line != null) {
				int value = Integer.parseInt(line);
				btree.insert(new Integer(value), String.valueOf(value));
				line = reader.readLine();
			}
		}
		return btree;
	}

	Collection<String> createSampleInput() throws Exception {
		ArrayList<String> input = new ArrayList<>(10000);
		Random random = new Random();
		for (int i = 0; i < 10000; i++) {
			int value = random.nextInt(500);
			input.add(String.valueOf(value));
		}
		return input;
	}

	public static class IntegerSerializer implements BTreeSerializer<Integer> {

		@Override
		public byte[] getBytes(Integer object) throws IOException {
			byte[] bytes = new byte[4];
			BTreeUtils.integerToBytes(object.intValue(), bytes);
			return bytes;
		}

		@Override
		public Integer getObject(byte[] bytes) throws IOException, ClassNotFoundException {
			return new Integer(BTreeUtils.bytesToInteger(bytes));
		}
	}
}
