/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.toc.document.MemTOCWriter;

/**
 * A class for building up TOC hierarchy
 */
public class TOCBuilder implements ITOCConstants {

	private ITOCWriter writer;

	public TOCBuilder() {
		writer = new MemTOCWriter();
	}

	public TOCBuilder(ITOCWriter writer) throws IOException {
		if (writer == null) {
			this.writer = new MemTOCWriter();
		} else {
			this.writer = writer;
		}
	}

	public TOCBuilder(ExecutionContext context) throws IOException {
		ReportDocumentWriter document = context.getReportDocWriter();
		if (document != null) {
			IDocArchiveWriter archive = document.getArchive();
			writer = new TOCWriter(archive);
		} else {
			writer = new MemTOCWriter();
		}
	}

	public void close() throws IOException {
		if (writer != null) {
			writer.close();
			writer = null;
		}
	}

	public TOCEntry startGroupEntry(TOCEntry parent, Object tocValue, String bookmark, String hiddenFormats,
			long elementId) {
		return startEntry(parent, tocValue, bookmark, hiddenFormats, true, elementId);
	}

	public void closeGroupEntry(TOCEntry group) {
		closeEntry(group);
	}

	/**
	 * @param displayString display string for the TOC entry
	 * @param bookmark
	 */
	public TOCEntry startEntry(TOCEntry parent, Object tocValue, String bookmark, String hiddenFormats,
			long elementId) {
		return startEntry(parent, tocValue, bookmark, hiddenFormats, false, elementId);
	}

	public TOCEntry startEntry(TOCEntry parent, Object tocValue, String bookmark, long elementId) {
		return startEntry(parent, tocValue, bookmark, null, false, elementId);
	}

	public TOCEntry startDummyEntry(TOCEntry parent, String hiddenFormats) {
		return startEntry(parent, null, null, hiddenFormats, false, -1);
	}

	public TOCEntry createEntry(TOCEntry parent, Object tocValue, String bookmark, long elementId) {
		TOCEntry entry = startEntry(parent, tocValue, bookmark, null, false, elementId);
		closeEntry(entry);
		return entry;
	}

	int nextChildId;

	private String getNextId(TOCEntry parent) {
		if (parent == null) {
			return TOC_PREFIX + nextChildId++;
		} else {
			return parent.getNodeId() + "_" + parent.nextChildId++;
		}
	}

	private TOCEntry startEntry(TOCEntry parent, Object tocValue, String bookmark, String hiddenFormats,
			boolean isGroup, long elementId) {
		TOCEntry entry = new TOCEntry();
		entry.setParent(parent);
		entry.setNodeId(getNextId(parent));
		entry.setBookmark(bookmark == null ? entry.getNodeId() : bookmark);
		entry.setHiddenFormats(hiddenFormats);
		entry.setGroup(isGroup);
		entry.setTOCValue(tocValue);
		entry.setElementId(elementId);

		if (tocValue != null) {
			writeTOCEntry(entry);
		}

		return entry;
	}

	/**
	 * close the entry. for top level toc, all entry must be put into the root
	 * entry. for group toc, we must create a root entry, and put all others into
	 * the root entry.
	 */
	public void closeEntry(TOCEntry entry) {
		if (entry.getTreeNode() != null) {
			try {
				writer.closeTOCEntry(entry);
			} catch (IOException ex) {
			}
		}
	}

	private void writeTOCEntry(TOCEntry entry) {
		TOCEntry parent = entry.getParent();
		if (parent != null && parent.getTreeNode() == null) {
			parent.setTOCValue("");
			writeTOCEntry(parent);
		}

		try {
			writer.startTOCEntry(entry);
		} catch (IOException ex) {
		}
	}

	public ITreeNode getTOCTree() {
		return writer.getTree();
	}
}
