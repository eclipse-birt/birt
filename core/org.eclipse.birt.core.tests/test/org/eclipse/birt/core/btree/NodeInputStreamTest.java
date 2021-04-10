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

package org.eclipse.birt.core.btree;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class NodeInputStreamTest extends TestCase {
	@Test
	public void testInputStream() throws IOException {
		NodeFile file = createNodeFile();
		NodeInputStream in = new NodeInputStream(file, 0);
		DataInput input = new DataInputStream(in);
		for (int i = 0; i < 1022; i++) {
			int v = input.readInt();
			assertEquals(i, v);
		}
		long v = input.readLong();
		assertEquals(Long.toHexString(0x1234567887654321L), Long.toHexString(v));
		for (int i = 0; i < 10; i++) {
			String s = input.readUTF();
			assertEquals(String.valueOf(i), s);
		}
		int[] blocks = in.getUsedBlocks();
		assertEquals(2, blocks.length);
		assertEquals(0, blocks[0]);
		assertEquals(1, blocks[1]);
		in.close();
	}

	protected NodeFile createNodeFile() throws IOException {
		NodeFile file = new RAMBTreeFile();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream(BTreeFile.BLOCK_SIZE);
		DataOutput out = new DataOutputStream(buffer);
		out.writeInt(1);
		for (int i = 0; i < 1022; i++) {
			out.writeInt(i);
		}
		out.writeInt(0x12345678);
		file.writeBlock(0, buffer.toByteArray());
		buffer.reset();

		out = new DataOutputStream(buffer);
		out.writeInt(-1);
		out.writeInt(0x87654321);
		for (int i = 0; i < 10; i++) {
			out.writeUTF(String.valueOf(i));
		}
		file.writeBlock(1, buffer.toByteArray());
		return file;
	}
}
