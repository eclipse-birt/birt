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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.internal.document.IPageHintReader;
import org.eclipse.birt.report.engine.internal.document.IPageHintWriter;
import org.eclipse.birt.report.engine.internal.document.PageIndexReader;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.presentation.SizeBasedPageSection;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

/**
 * Fixed layout page hint reader.
 */
public class FixedLayoutPageHintReader implements IPageHintReader {
	protected IDocArchiveReader reader;
	protected RAInputStream indexStream;
	protected RAInputStream hintsStream;
	protected PageIndexReader pageIndexReader;
	protected long totalPage = -1;
	protected ArrayList<PageVariable> pageVariables;
	protected int version;

	public FixedLayoutPageHintReader(IDocArchiveReader reader) throws IOException {
		this.reader = reader;
		try {
			hintsStream = reader.getStream(ReportDocumentConstants.PAGEHINT_STREAM);
			indexStream = reader.getStream(ReportDocumentConstants.PAGEHINT_INDEX_STREAM);
			pageIndexReader = new PageIndexReader(reader);
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
			if (indexStream != null) {
				indexStream.close();
				indexStream = null;
			}
			if (pageIndexReader != null) {
				pageIndexReader.close();
				pageIndexReader = null;
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

	/**
	 * The page variable is only supported in VERSION_6
	 */
	synchronized public Collection<PageVariable> getPageVariables() throws IOException {
		if (pageVariables == null) {
			pageVariables = new ArrayList<PageVariable>();
			if (version == VERSION_6) {
				indexStream.seek(8);
				long offset = indexStream.readLong();
				if (offset != -1) {
					hintsStream.seek(offset);
					readPageVariables(new DataInputStream(hintsStream), pageVariables);
				}
			}
		}
		return pageVariables;
	}

	/**
	 * return the hint offset for the page.
	 * 
	 * before version 6, the offset is 8 * pageNumber. the 1st long is the total
	 * page. the page number starts from integer 1.
	 * 
	 * after (include) version 6, the offset is 8 * (pageNumber + 1). the 1st long
	 * is the total page, the 2nd long is the offset to page variable. the page
	 * number starts from integer 1.
	 * 
	 * @param pageNumber
	 * @return the offset of the hints in the hint stream.
	 */
	private long getHintOffset(long pageNumber) {
		return (pageNumber + 1) * 8;
	}

	synchronized public IPageHint getPageHint(long pageNumber) throws IOException {
		long indexOffset = getHintOffset(pageNumber);
		indexStream.seek(indexOffset);
		long offset = indexStream.readLong();
		hintsStream.seek(offset);
		return readPageHint(version, new DataInputStream(hintsStream));
	}

	protected IPageHint readPageHint(int version, DataInputStream in) throws IOException {
		if (version == IPageHintWriter.VERSION_FIXED_LAYOUT) {
			return readFixedLayoutPageHint(in);
		} else {
			throw new IOException("Unsupported page hint version " + version);
		}
	}

	public IPageHint readFixedLayoutPageHint(DataInputStream in) throws IOException {
		long pageNumber = IOUtil.readLong(in);
		String masterPage = IOUtil.readString(in);
		PageHint hint = new PageHint(pageNumber, masterPage);
		hint.setOffset(pageIndexReader.getPageOffset(masterPage));
		int sectionCount = IOUtil.readInt(in);
		for (int i = 0; i < sectionCount; i++) {
			PageSection section = null;
			int sectionType = IOUtil.readInt(in);
			if (sectionType == PageSection.TYPE_FIXED_LAYOUT_PAGE_SECTION) {
				section = new SizeBasedPageSection();
			} else {
				section = new PageSection();
			}
			section.read(in);
			hint.addSection(section);
		}

		int hintSize = IOUtil.readInt(in);
		for (int i = 0; i < hintSize; i++) {
			UnresolvedRowHint rowHint = new UnresolvedRowHint();
			rowHint.readObject(new DataInputStream(in));
			hint.addUnresolvedRowHint(rowHint);
		}
		int columnHintSize = IOUtil.readInt(in);
		for (int i = 0; i < columnHintSize; i++) {
			String tableId = IOUtil.readString(in);
			int start = IOUtil.readInt(in);
			int columnCount = IOUtil.readInt(in);
			hint.addTableColumnHint(new TableColumnHint(tableId, start, columnCount));
		}
		Collection<PageVariable> variables = hint.getPageVariables();
		readPageVariables(in, variables);
		return hint;
	}

	public IPageHint readPageHintV5(DataInputStream in) throws IOException {
		IPageHint hint = readPageHintV4(in);
		int columnHintSize = IOUtil.readInt(in);
		for (int i = 0; i < columnHintSize; i++) {
			String tableId = IOUtil.readString(in);
			int start = IOUtil.readInt(in);
			int columnCount = IOUtil.readInt(in);
			hint.addTableColumnHint(new TableColumnHint(tableId, start, columnCount));
		}
		return hint;
	}

	public IPageHint readPageHintV4(DataInputStream in) throws IOException {
		long pageNumber = IOUtil.readLong(in);
		String masterPage = IOUtil.readString(in);
		PageHint hint = new PageHint(pageNumber, masterPage);
		hint.setOffset(pageIndexReader.getPageOffset(masterPage));
		int sectionCount = IOUtil.readInt(in);
		for (int i = 0; i < sectionCount; i++) {
			PageSection section = new PageSection();
			section.read(in);
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

	public long getPageOffset(long pageNumber, String masterPage) throws IOException {
		return pageIndexReader.getPageOffset(masterPage);
	}

	protected void readPageVariables(DataInputStream in, Collection<PageVariable> variables) throws IOException {
		int count = IOUtil.readInt(in);
		for (int i = 0; i < count; i++) {
			PageVariable variable = readPageVariable(in);
			variables.add(variable);
		}
	}

	private PageVariable readPageVariable(DataInputStream in) throws IOException {
		String name = IOUtil.readString(in);
		String scope = IOUtil.readString(in);
		Object value = IOUtil.readObject(in);
		return new PageVariable(name, scope, value);
	}

}
