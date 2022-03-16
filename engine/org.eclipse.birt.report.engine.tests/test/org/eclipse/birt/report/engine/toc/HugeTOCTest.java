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

package org.eclipse.birt.report.engine.toc;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;

public class HugeTOCTest extends TOCTestCase {

	static final int TOC_SIZE = 1000;
	static final String TOC_FILE = "./utest/test.toc";

	public void testHugeTOC() throws IOException {
		new File(TOC_FILE).delete();
		ArchiveFile archive = new ArchiveFile(TOC_FILE, "rw");

		System.out.println("create TOC with " + TOC_SIZE * TOC_SIZE + " entries");
		try {
			ITOCWriter writer = new TOCWriter(new ArchiveWriter(archive));
			try {
				TOCBuilder builder = new TOCBuilder(writer);
				for (int i = 0; i < TOC_SIZE; i++) {
					TOCEntry level1 = startEntry(builder, null, "TOC_" + i);
					for (int j = 0; j < TOC_SIZE; j++) {
						createEntry(builder, level1, "TOC_" + i + "_" + j);
					}
					closeEntry(builder, level1);
				}
			} finally {
				writer.close();
			}

			System.out.println("file length:" + new File(TOC_FILE).length());

			System.out.println("read TOC with " + TOC_SIZE * TOC_SIZE + " entries");

			TOCReader reader = new TOCReader(new ArchiveReader(archive), ClassLoader.getSystemClassLoader());
			try {
				ITreeNode root = reader.readTree();
				assertEquals("/", root.getNodeId());
				Collection<ITreeNode> nodes1 = root.getChildren();
				assertEquals(TOC_SIZE, nodes1.size());
				int index1 = 0;
				for (ITreeNode node1 : nodes1) {
					assertEquals("TOC_" + index1, node1.getTOCValue());
					Collection<ITreeNode> nodes2 = node1.getChildren();
					assertEquals(TOC_SIZE, nodes2.size());
					int index2 = 0;
					for (ITreeNode node2 : nodes2) {
						assertEquals("TOC_" + index1 + "_" + index2, node2.getTOCValue());
						assertTrue(node2.getChildren().isEmpty());
						index2++;
					}
					index1++;
				}
			} finally {
				reader.close();
			}

		} finally {
			archive.close();
		}
	}
}
