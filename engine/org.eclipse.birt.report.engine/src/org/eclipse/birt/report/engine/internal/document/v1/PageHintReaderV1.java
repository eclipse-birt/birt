/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v1;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;

public class PageHintReaderV1 implements IPageHintReader {
	protected IReportDocument document;
	ArrayList pageHints = new ArrayList();

	public PageHintReaderV1(IReportDocument document) throws IOException {
		this.document = document;
		IDocArchiveReader reader = document.getArchive();

		InputStream in = reader.getStream(ReportDocumentConstants.PAGEHINT_STREAM);
		try {
			DataInputStream di = new DataInputStream(new BufferedInputStream(in));
			long pageCount = IOUtil.readLong(di);
			for (long i = 0; i < pageCount; i++) {
				IPageHint hint = readPageHint(di);
				pageHints.add(hint);
			}
		} catch (IOException ex) {
			in.close();
			throw ex;
		}
	}

	public int getVersion() {
		return VERSION_0;

	}

	public void close() {
	}

	public long getTotalPage() throws IOException {
		return pageHints.size();
	}

	public IPageHint getPageHint(long pageNumber) throws IOException {
		return (IPageHint) pageHints.get((int) pageNumber);
	}

	public long findPage(long offset) throws IOException {
		for (int i = 0; i < pageHints.size(); i++) {
			IPageHint hint = (IPageHint) pageHints.get(i);
			PageSection section = hint.getSection(0);
			if (section.startOffset > offset) {
				return i + 1;
			}
		}
		return pageHints.size();
	}

	private PageHint readPageHint(DataInputStream in) throws IOException {
		long pageNumber = IOUtil.readLong(in);
		long pageOffset = IOUtil.readLong(in);
		PageHint hint = new PageHint(pageNumber, pageOffset);
		PageSection section = new PageSection();
		section.startOffset = IOUtil.readLong(in);
		section.endOffset = IOUtil.readLong(in);
		hint.addSection(section);
		return hint;
	}

	public long getPageOffset(long pageNumber, String masterPage) throws IOException {
		return getPageHint(pageNumber).getOffset();
	}

	public Collection<PageVariable> getPageVariables() throws IOException {
		return new ArrayList<PageVariable>();
	}

}
