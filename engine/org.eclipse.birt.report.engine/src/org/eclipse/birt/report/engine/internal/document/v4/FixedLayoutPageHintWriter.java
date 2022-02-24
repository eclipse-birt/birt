/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.internal.document.IPageHintWriter;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class FixedLayoutPageHintWriter extends PageHintWriterV4 implements IPageHintWriter {
	public FixedLayoutPageHintWriter(IDocArchiveWriter writer) throws IOException {
		super(writer);
	}

	@Override
	protected void init(IDocArchiveWriter writer) throws IOException {
		this.writer = writer;
		try {
			hintsStream = writer.createRandomAccessStream(ReportDocumentConstants.PAGEHINT_STREAM);
			hintsStream.writeInt(IPageHintWriter.VERSION_FIXED_LAYOUT);
			indexStream = writer.createRandomAccessStream(ReportDocumentConstants.PAGEHINT_INDEX_STREAM);
			indexStream.writeLong(0); // total page
			indexStream.writeLong(-1);// global variable offset
		} catch (IOException ex) {
			close();
			throw ex;
		}
	}

	@Override
	protected void writePageHint(DataOutputStream out, IPageHint hint) throws IOException {
		IOUtil.writeLong(out, hint.getPageNumber());
		IOUtil.writeString(out, hint.getMasterPage());
		int sectionCount = hint.getSectionCount();
		IOUtil.writeInt(out, sectionCount);
		for (int i = 0; i < sectionCount; i++) {
			PageSection section = hint.getSection(i);
			section.write(out);
		}

		int hintSize = hint.getUnresolvedRowCount();
		IOUtil.writeInt(out, hintSize);

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

	@Override
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
}
