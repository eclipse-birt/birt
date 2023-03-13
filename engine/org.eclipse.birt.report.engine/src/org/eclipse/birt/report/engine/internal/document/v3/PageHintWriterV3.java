/*******************************************************************************
 * Copyright (c) 2007,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v3;

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
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class PageHintWriterV3 implements IPageHintWriter {

	protected IDocArchiveWriter writer;
	protected RAOutputStream indexStream;
	protected RAOutputStream hintsStream;

	public PageHintWriterV3(IDocArchiveWriter writer) throws IOException {
		this.writer = writer;
		try {
			hintsStream = writer.createRandomAccessStream(ReportDocumentConstants.PAGEHINT_STREAM);
			hintsStream.writeInt(IPageHintWriter.VERSION_3);
			indexStream = writer.createRandomAccessStream(ReportDocumentConstants.PAGEHINT_INDEX_STREAM);
		} catch (IOException ex) {
			close();
			throw ex;
		}
	}

	@Override
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

	@Override
	public void writePageHint(IPageHint pageHint) throws IOException {
		long offset = hintsStream.getOffset();
		indexStream.seek(pageHint.getPageNumber() * 8);
		indexStream.writeLong(offset);
		writeBuffer.reset();
		writePageHint(hintBuffer, pageHint);
		hintsStream.write(writeBuffer.toByteArray());
	}

	@Override
	public void writeTotalPage(long totalPage) throws IOException {
		indexStream.seek(0);
		indexStream.writeLong(totalPage);
	}

	protected void writePageHint(DataOutputStream out, IPageHint hint) throws IOException {
		IOUtil.writeLong(out, hint.getPageNumber());
		IOUtil.writeLong(out, hint.getOffset());
		int sectionCount = hint.getSectionCount();
		IOUtil.writeInt(out, sectionCount);
		for (int i = 0; i < sectionCount; i++) {
			PageSection section = hint.getSection(i);
			writeInstanceIndex(out, section.starts);
			writeInstanceIndex(out, section.ends);
		}

		int hintSize = hint.getUnresolvedRowCount();
		IOUtil.writeInt(out, hintSize);

		for (int i = 0; i < hintSize; i++) {
			UnresolvedRowHint rowHint = hint.getUnresolvedRowHint(i);
			rowHint.writeObject(out);
		}
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

	@Override
	public void writePageVariables(Collection<PageVariable> variables) throws IOException {
		throw new IOException("unsupported operation: writePageVariables");
	}

}
