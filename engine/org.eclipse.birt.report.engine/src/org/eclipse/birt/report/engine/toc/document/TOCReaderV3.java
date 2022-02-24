/*******************************************************************************
 * Copyright (c) 2008,2009 Actuate Corporation.
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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.toc.ITOCConstants;
import org.eclipse.birt.report.engine.toc.ITOCReader;
import org.eclipse.birt.report.engine.toc.ITreeNode;

public class TOCReaderV3 implements ITOCReader, ITOCConstants {

	static final Logger logger = Logger.getLogger(TOCReaderV3.class.getName());
	DocTreeNode root;
	RAInputStream in;
	ClassLoader classloader;

	public TOCReaderV3(RAInputStream in, ClassLoader loader) throws IOException {
		this(in, loader, false);
	}

	public TOCReaderV3(RAInputStream in, ClassLoader loader, boolean checkVersion) throws IOException {
		this.in = in;
		this.classloader = loader;

		if (checkVersion) {
			DataInputStream input = new DataInputStream(in);
			String version = IOUtil.readString(input);
			if (!VERSION_V3.equals(version)) {
				throw new IOException("Unsupported version:" + version);
			}
		}

		int offset = (int) in.getOffset();
		root = readNode(offset);
	}

	public void close() throws IOException {
		if (in != null) {
			try {
				in.close();
			} finally {
				in.close();
			}
		}
	}

	public ITreeNode readTree() throws IOException {
		return root;
	}

	synchronized private DocTreeNode readNode(int offset) throws IOException {
		DocTreeNode node = new DocTreeNode();
		node.offset = offset;
		in.seek(offset);
		node.next = in.readInt();
		node.child = in.readInt();
		node.childCount = in.readInt();
		int byteSize = in.readInt();
		byte[] bytes = new byte[byteSize];
		in.readFully(bytes, 0, byteSize);
		DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes));

		node.readNode(input, classloader);

		node.children = new NodeCollection(node);

		return node;
	}

	private class NodeCollection extends AbstractCollection<ITreeNode> {

		DocTreeNode parent;

		NodeCollection(DocTreeNode parent) {
			this.parent = parent;
		}

		public Iterator<ITreeNode> iterator() {
			return new NodeCollectionIterator(parent);
		}

		public int size() {
			return parent.childCount;
		}

		private class NodeCollectionIterator implements Iterator<ITreeNode> {

			DocTreeNode parent;
			int nextIndex;
			int nextOffset;
			boolean fatalError;

			NodeCollectionIterator(DocTreeNode parent) {
				this.parent = parent;
				this.nextIndex = 0;
				this.nextOffset = parent.child;
				this.fatalError = false;
			}

			public boolean hasNext() {
				if (!fatalError) {
					return nextIndex < parent.childCount;
				}
				return false;
			}

			public ITreeNode next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				try {
					DocTreeNode nextNode = readNode(nextOffset);
					nextNode.parent = parent;
					nextIndex++;
					nextOffset = nextNode.next;
					return nextNode;
				} catch (IOException ex) {
					logger.log(Level.INFO, "failed to load the toc node at " + nextOffset, ex);
					fatalError = true;
				}
				return null;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
	}
}
