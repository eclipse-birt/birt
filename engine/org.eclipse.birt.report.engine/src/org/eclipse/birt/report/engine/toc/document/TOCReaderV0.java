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

package org.eclipse.birt.report.engine.toc.document;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.toc.ITOCReader;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TreeNode;

public class TOCReaderV0 implements ITOCReader {

	TreeNode root;

	public TOCReaderV0(InputStream in) throws IOException {
		this(in, false);
	}

	public TOCReaderV0(InputStream in, boolean checkVersion) throws IOException {
		DataInputStream input = new DataInputStream(in);
		if (checkVersion) {
			// skip the first string as it should be the root label
			IOUtil.readString(input);
		}
		root = readRoot(input);
	}

	public TOCReaderV0(TreeNode root) {
		this.root = root;
	}

	public ITreeNode readTree() throws IOException {
		return root;
	}

	public void close() throws IOException {
	}

	protected MemTreeNode readRoot(DataInputStream input) throws IOException {
		MemTreeNode node = new MemTreeNode();
		String nodeId = "/";
		String displayString = IOUtil.readString(input);
		String bookmark = IOUtil.readString(input);
		node.setNodeId(nodeId);
		node.setTOCValue(displayString);
		node.setBookmark(bookmark);
		int size = IOUtil.readInt(input);
		for (int i = 0; i < size; i++) {
			MemTreeNode child = readNode(input);
			node.addChild(child);
		}
		return node;

	}

	public MemTreeNode readNode(DataInputStream input) throws IOException {
		MemTreeNode node = new MemTreeNode();
		String nodeId = IOUtil.readString(input);
		String displayString = IOUtil.readString(input);
		String bookmark = IOUtil.readString(input);
		node.setNodeId(nodeId);
		node.setTOCValue(displayString);
		node.setBookmark(bookmark);
		int size = IOUtil.readInt(input);
		for (int i = 0; i < size; i++) {
			MemTreeNode child = readNode(input);
			node.addChild(child);
		}
		return node;
	}
}
