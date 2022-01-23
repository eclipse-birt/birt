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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.toc.ITOCConstants;
import org.eclipse.birt.report.engine.toc.ITOCWriter;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TOCEntry;

public class TOCWriterV3 implements ITOCWriter, ITOCConstants {

	private RAOutputStream out;

	private ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
	private DataOutputStream output = new DataOutputStream(buffer);

	private DocTreeNode root;

	private long offset;

	public TOCWriterV3(RAOutputStream out) throws IOException {
		this.out = out;
		IOUtil.writeString(output, VERSION_V3);

		out.write(buffer.toByteArray());

		offset = out.getOffset();
		root = new DocTreeNode();
		root.setNodeId("/");
		root.offset = (int) offset;
		writeTreeNode(root);
	}

	public ITreeNode getTree() {
		return root;
	}

	DocTreeNode getParent(TOCEntry entry) {
		TOCEntry parent = entry.getParent();
		if (parent != null) {
			return (DocTreeNode) parent.getTreeNode();
		}
		return root;
	}

	public void startTOCEntry(TOCEntry tocEntry) throws IOException {
		DocTreeNode node = new DocTreeNode(tocEntry);
		node.offset = (int) offset;

		// get parent entry
		DocTreeNode parent = getParent(tocEntry);
		node.setParent(parent);
		parent.childCount++;

		writeTreeNode(node);

		tocEntry.setTreeNode(node);
	}

	public void closeTOCEntry(TOCEntry entry) throws IOException {
		DocTreeNode node = (DocTreeNode) entry.getTreeNode();
		if (node != null) {
			// update the total child
			if (node.childCount > 0) {
				out.seek(node.offset + DocTreeNode.OFFSET_CHILD_COUNT);
				out.writeInt(node.childCount);
			}
		}
	}

	public void close() throws IOException {
		if (out != null) {
			try {
				if (root != null) {
					if (root.childCount > 0) {
						out.seek(root.offset + DocTreeNode.OFFSET_CHILD_COUNT);
						out.writeInt(root.childCount);
					}
					root = null;
				}
				out.close();
			} finally {
				out = null;
			}
		}
	}

	synchronized protected void writeTreeNode(DocTreeNode node) throws IOException {
		out.seek(node.offset);
		out.writeInt(node.next);
		out.writeInt(node.child);
		out.writeInt(node.childCount);
		offset += 12;

		buffer.reset();
		node.writeNode(output);
		byte[] data = buffer.toByteArray();
		out.writeInt(data.length);
		out.write(data);
		offset += 4;
		offset += data.length;

		updateIndex(node);
	}

	synchronized protected void updateIndex(DocTreeNode node) throws IOException {
		DocTreeNode parent = node.getParent();
		if (parent == null) {
			return;
		}
		if (parent.child == -1) {
			// this is the first child of the parent
			out.seek(parent.offset + DocTreeNode.OFFSET_CHILD);
			out.writeInt(node.offset);
		} else {
			// update the previous child
			out.seek(parent.child + DocTreeNode.OFFSET_NEXT);
			out.writeInt(node.offset);
		}
		parent.child = node.offset;
	}
}
