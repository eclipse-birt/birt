/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v2;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.internal.document.IPageHintWriter;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class PageHintReaderV2 implements IPageHintReader {

	protected IDocArchiveReader reader;
	protected RAInputStream indexStream;
	protected RAInputStream hintsStream;
	protected long totalPage = -1;
	protected int version;

	public PageHintReaderV2(IDocArchiveReader reader) throws IOException {
		this.reader = reader;
		try {
			hintsStream = reader.getStream(ReportDocumentConstants.PAGEHINT_STREAM);
			indexStream = reader.getStream(ReportDocumentConstants.PAGEHINT_INDEX_STREAM);
			version = readHintVersion(hintsStream);
		} catch (IOException ex) {
			close();
			throw ex;
		}
	}

	public int getVersion() {
		return version;
	}

	public static int readHintVersion(RAInputStream hintStream) throws IOException {
		hintStream.seek(0);
		int version = hintStream.readInt();
		if (version == 0) {
			return VERSION_1;
		}
		return version;
	}

	public void close() {
		try {
			if (hintsStream != null) {
				hintsStream.close();
				hintsStream = null;
			}
		} catch (IOException ex) {

		}

		try {
			if (indexStream != null) {
				indexStream.close();
				indexStream = null;
			}
		} catch (IOException ex) {

		}
	}

	synchronized public long getTotalPage() throws IOException {
		indexStream.refresh();
		indexStream.seek(0);
		totalPage = indexStream.readLong();
		return totalPage;
	}

	synchronized public IPageHint getPageHint(long pageNumber) throws IOException {
		indexStream.seek(pageNumber * 8);
		long offset = indexStream.readLong();
		hintsStream.seek(offset);
		return readPageHint(version, new DataInputStream(hintsStream));
	}

	protected IPageHint readPageHint(int version, DataInputStream in) throws IOException {
		switch (version) {
		case IPageHintWriter.VERSION_1:
			return readPageHintV1(in);
		case IPageHintWriter.VERSION_2:
			return readPageHintV2(in);
		case IPageHintWriter.VERSION_3:
			return readPageHintV3(in);
		default:
			throw new IOException("Unsupported page hint version " + version);
		}
	}

	public IPageHint readPageHintV3(DataInputStream in) throws IOException {
		long pageNumber = IOUtil.readLong(in);
		long offset = IOUtil.readLong(in);
		PageHint hint = new PageHint(pageNumber, offset);
		int sectionCount = IOUtil.readInt(in);
		for (int i = 0; i < sectionCount; i++) {
			PageSection section = new PageSection();
			section.starts = readInstanceIndex(in);
			section.ends = readInstanceIndex(in);
			section.startOffset = section.starts[section.starts.length - 1].getOffset();
			section.endOffset = section.ends[section.ends.length - 1].getOffset();
			hint.addSection(section);
		}

		int hintSize = IOUtil.readInt(in);
		for (int i = 0; i < hintSize; i++) {
			UnresolvedRowHint rowHint = new UnresolvedRowHint();
			rowHint.readObject(new DataInputStream(in));
			hint.addUnresolvedRowHint(rowHint);
		}
		return hint;
	}

	public IPageHint readPageHintV1(DataInputStream in) throws IOException {
		long pageNumber = IOUtil.readLong(in);
		long offset = IOUtil.readLong(in);
		PageHint hint = new PageHint(pageNumber, offset);
		int sectionCount = IOUtil.readInt(in);
		for (int i = 0; i < sectionCount; i++) {
			long startOffset = IOUtil.readLong(in);
			long endOffset = IOUtil.readLong(in);
			PageSection section = new PageSection();
			section.startOffset = startOffset;
			section.endOffset = endOffset;
			hint.addSection(section);
		}
		return hint;
	}

	public IPageHint readPageHintV2(DataInputStream in) throws IOException {
		long pageNumber = IOUtil.readLong(in);
		long offset = IOUtil.readLong(in);
		PageHint hint = new PageHint(pageNumber, offset);
		int sectionCount = IOUtil.readInt(in);
		for (int i = 0; i < sectionCount; i++) {
			PageSection section = new PageSection();
			section.starts = readInstanceIndex(in);
			section.ends = readInstanceIndex(in);
			section.startOffset = section.starts[section.starts.length - 1].getOffset();
			section.endOffset = section.ends[section.ends.length - 1].getOffset();
			/*
			 * section.startId = starts[0].getInstanceID().toString(); section.startOffset =
			 * starts[0].getOffset(); section.endId = endId; section.endOffset = endOffset;
			 */
			hint.addSection(section);
		}
		return hint;
	}

	protected InstanceIndex[] readInstanceIndex(DataInputStream in) throws IOException {
		int length = IOUtil.readInt(in);
		InstanceIndex[] indexes = new InstanceIndex[length];
		for (int i = 0; i < length; i++) {
			String id = IOUtil.readString(in);
			long offset = IOUtil.readLong(in);
			indexes[i] = new InstanceIndex(InstanceID.parse(id), offset);
		}
		return indexes;
	}

	public long getPageOffset(long pageNumber, String masterPage) throws IOException {
		return getPageHint(pageNumber).getOffset();
	}

	public Collection<PageVariable> getPageVariables() throws IOException {
		return new ArrayList<PageVariable>();
	}
}