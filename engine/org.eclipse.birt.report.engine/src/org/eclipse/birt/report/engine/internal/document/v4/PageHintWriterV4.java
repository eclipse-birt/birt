/*******************************************************************************
 * Copyright (c) 2007,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.internal.document.IPageHintWriter;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class PageHintWriterV4 implements IPageHintWriter {
	protected IDocArchiveWriter writer;
	protected RAOutputStream indexStream;
	protected RAOutputStream hintsStream;

	public PageHintWriterV4(IDocArchiveWriter writer) throws IOException {
		init(writer);
	}

	protected void init(IDocArchiveWriter writer) throws IOException {
		this.writer = writer;
		try {
			hintsStream = writer.createRandomAccessStream(ReportDocumentConstants.PAGEHINT_STREAM);
			hintsStream.writeInt(IPageHintWriter.VERSION);
			indexStream = writer.createRandomAccessStream(ReportDocumentConstants.PAGEHINT_INDEX_STREAM);
			indexStream.writeLong(0); // total page
			indexStream.writeLong(-1);// global variable offset
		} catch (IOException ex) {
			close();
			throw ex;
		}
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

	private ByteArrayOutputStream writeBuffer = new ByteArrayOutputStream();
	private DataOutputStream hintBuffer = new DataOutputStream(writeBuffer);

	public void writePageHint(IPageHint pageHint) throws IOException {
		long offset = hintsStream.getOffset();
		// the 1st long is a version
		// the 2nd long is the offset to page variables
		// the 3nd long is the first page offset
		indexStream.seek(pageHint.getPageNumber() * 8 + 8);
		indexStream.writeLong(offset);
		writeBuffer.reset();
		writePageHint(hintBuffer, pageHint);
		hintsStream.write(writeBuffer.toByteArray());
	}

	public void writeTotalPage(long totalPage) throws IOException {
		indexStream.seek(0);
		indexStream.writeLong(totalPage);
	}

	public void writePageVariables(Collection<PageVariable> variables) throws IOException {
		long offset = hintsStream.getOffset();
		writeBuffer.reset();
		writePageVariables(hintBuffer, variables);
		hintsStream.write(writeBuffer.toByteArray());
		hintsStream.flush();
		indexStream.seek(8);
		indexStream.writeLong(offset);
	}

	protected void writePageHint(DataOutputStream out, IPageHint hint) throws IOException {
		IOUtil.writeLong(out, hint.getPageNumber());
		IOUtil.writeString(out, hint.getMasterPage());
		int sectionCount = hint.getSectionCount();
		IOUtil.writeInt(out, sectionCount);
		for (int i = 0; i < sectionCount; i++) {
			PageSection section = hint.getSection(i);
			writeInstanceIndex(out, section.starts);
			writeInstanceIndex(out, section.ends);
		}

		int hintSize = hint.getUnresolvedRowCount();
		IOUtil.writeInt(out, hintSize);
		;
		for (int i = 0; i < hintSize; i++) {
			UnresolvedRowHint rowHint = hint.getUnresolvedRowHint(i);
			rowHint.writeObject(out);
		}

		int columnHintSize = hint.getTableColumnHintCount();
		IOUtil.writeInt(out, columnHintSize);
		for (int i = 0; i < columnHintSize; i++) {
			TableColumnHint columnHint = hint.getTableColumnHint(i);
			IOUtil.writeString(out, columnHint.getTableId());
			IOUtil.writeInt(out, columnHint.getStart());
			IOUtil.writeInt(out, columnHint.getColumnCount());
		}

		Collection<PageVariable> variables = hint.getPageVariables();
		writePageVariables(out, variables);
	}

	protected void writeInstanceIndex(DataOutputStream out, InstanceIndex[] indexes) throws IOException {
		if (indexes == null) {
			IOUtil.writeInt(out, 0);
			return;
		}
		IOUtil.writeInt(out, indexes.length);
		for (int i = 0; i < indexes.length; i++) {
			IOUtil.writeString(out, indexes[i].getInstanceID().toString());
			IOUtil.writeLong(out, indexes[i].getOffset());
		}
	}

	protected void writePageVariables(DataOutputStream out, Collection<PageVariable> variables) throws IOException {
		IOUtil.writeInt(out, variables.size());
		for (PageVariable variable : variables) {
			IOUtil.writeString(out, variable.getName());
			IOUtil.writeString(out, variable.getScope());
			IOUtil.writeObject(out, variable.getValue());
		}
	}
}
