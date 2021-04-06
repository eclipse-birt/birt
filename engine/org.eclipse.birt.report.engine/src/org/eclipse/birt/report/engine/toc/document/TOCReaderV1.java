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

package org.eclipse.birt.report.engine.toc.document;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.toc.ITOCConstants;
import org.eclipse.birt.report.engine.toc.ITOCReader;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TreeNode;

public class TOCReaderV1 implements ITOCReader, ITOCConstants {

	TreeNode root;

	public TOCReaderV1(InputStream in, ClassLoader loader) throws IOException {
		this(in, loader, false);
	}

	public TOCReaderV1(InputStream in, ClassLoader loader, boolean checkVersion) throws IOException {
		DataInputStream input = new DataInputStream(in);
		if (checkVersion) {
			String version = IOUtil.readString(input);
			if (!VERSION_V1.equals(version)) {
				throw new IOException("Unsupported version:" + version);
			}
		}
		root = readNode(input, loader);
	}

	public void close() throws IOException {

	}

	public ITreeNode readTree() throws IOException {
		return root;
	}

	public MemTreeNode readNode(DataInputStream input, ClassLoader loader) throws IOException {
		MemTreeNode node = new MemTreeNode();
		String nodeId = IOUtil.readString(input);
		String displayString = IOUtil.readString(input);
		String bookmark = IOUtil.readString(input);
		String hiddenFormats = IOUtil.readString(input);
		boolean isGroupRoot = IOUtil.readBool(input);
		Object tocValue = IOUtil.readObject(input, loader);
		node.setNodeId(nodeId);
		node.setTOCValue(displayString);
		node.setBookmark(bookmark);
		node.setHiddenFormats(hiddenFormats);
		node.setGroup(isGroupRoot);
		node.setTOCValue(tocValue);
		int size = IOUtil.readInt(input);
		for (int i = 0; i < size; i++) {
			MemTreeNode child = readNode(input, loader);
			node.addChild(child);
		}

		return node;
	}

}
