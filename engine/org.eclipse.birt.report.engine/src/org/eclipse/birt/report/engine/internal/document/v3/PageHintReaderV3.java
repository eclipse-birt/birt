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
import org.eclipse.birt.report.engine.internal.document.PageIndexReader;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

/**
 * <h3>Format of VERSION_5</h3>
 * 
 * <h3>Format of VERSION_6</h3>
 * 
 * Compared with the VERSION_5, version 6 adds the support for page variables.
 * There are two kinds of page variables: report level and the page level. Those
 * two kinds of page variables are saved into different places, the report level
 * page variables are saved at the end of page hint stream only once while the
 * page level page variables are saved into each page hint.
 * 
 * <h4>structure of index stream</h4>
 * 
 * <table border="all" width="80%">
 * <tr>
 * <th>TYPE</th>
 * <th>COMMENT</th>
 * </tr>
 * <tr>
 * <td>LONG</td>
 * <td>Total Page</td>
 * </tr>
 * <tr>
 * <td>LONG</td>
 * <td>Page variables offset</td>
 * </tr>
 * <tr>
 * <td>LONG</td>
 * <td>Offset of First Page</td>
 * </tr>
 * <tr>
 * <td>LONG</td>
 * <td>Offset of 2nd page</td>
 * </tr>
 * <tr>
 * <td colspan="2">index to other pages...</td>
 * </tr>
 * </table>
 * 
 * <h4>structure of page hint stream</h4>
 * 
 * <table border="all" width="80%">
 * <tr>
 * <th>TYPE</th>
 * <th>COMMENT</th>
 * </tr>
 * <tr>
 * >
 * <td>INT</td>
 * <td>version, 6</td>
 * </tr>
 * <tr>
 * <td>page hint v6</td>
 * <td>page hint for the first page</td>
 * </tr>
 * <tr>
 * <td colspan="2">page hints for other pages</td>
 * </tr>
 * <tr>
 * <td colspan="2">report level page variables (saved only once)</td>
 * <tr>
 * </table>
 * 
 * <h4>structure for the page hint v6</h4>
 * <table border="all" width="80%">
 * <tr>
 * <th>TYPE</th>
 * <th>COMMENT</th>
 * </tr>
 * <tr>
 * <td colspan="2">page hint v5</td>
 * </tr>
 * <tr>
 * <td colspan="2">page variables</td>
 * </tr>
 * </table>
 * 
 * <h4>structure for page variables</h4>
 * <table border="all" width="80%">
 * <tr>
 * <th>TYPE</th>
 * <th>COMMENT</th>
 * </tr>
 * <tr>
 * <td>INT</td>
 * <td>count</td>
 * </tr>
 * <tr>
 * <td>STRING</td>
 * <td>LEVEL</td>
 * </tr>
 * <tr>
 * <td>STRING</td>
 * <td>SCOPE</td>
 * </tr>
 * <tr>
 * <td>STRING</td>
 * <td>NAME</td>
 * </tr>
 * <tr>
 * <td>OBJECT</td>
 * <td>VALUE</td>
 * </tr>
 * <tr>
 * <td colspan="2">remain variables, each contains 3 string and 1 object
 * <td>
 * </tr>
 * </table>
 */
public class PageHintReaderV3 implements IPageHintReader {

	protected IDocArchiveReader reader;
	protected RAInputStream indexStream;
	protected RAInputStream hintsStream;
	protected PageIndexReader pageIndexReader;
	protected long totalPage = -1;
	protected ArrayList<PageVariable> pageVariables;
	protected int version;

	public PageHintReaderV3(IDocArchiveReader reader) throws IOException {
		this.reader = reader;
		try {
			hintsStream = reader.getStream(ReportDocumentConstants.PAGEHINT_STREAM);
			indexStream = reader.getStream(ReportDocumentConstants.PAGEHINT_INDEX_STREAM);
			pageIndexReader = new PageIndexReader(reader);
			version = readHintVersion(hintsStream);
			if (version != VERSION_3 && version != VERSION_4 && version != VERSION_5 && version != VERSION_6) {
				throw new IOException("unsupported hint version:" + version);
			}
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
	 * number is start from integer 1.
	 * 
	 * @param pageNumber
	 * @return the offset of the hints in the hint stream.
	 */
	private long getHintOffset(long pageNumber) {
		switch (version) {
		case VERSION_3:
		case VERSION_4:
		case VERSION_5:
			return pageNumber * 8;
		case VERSION_6:
			return (pageNumber + 1) * 8;
		default:
			assert false;
			return -1;
		}
	}

	synchronized public IPageHint getPageHint(long pageNumber) throws IOException {
		long indexOffset = getHintOffset(pageNumber);
		indexStream.seek(indexOffset);
		long offset = indexStream.readLong();
		hintsStream.seek(offset);
		return readPageHint(version, new DataInputStream(hintsStream));
	}

	protected IPageHint readPageHint(int version, DataInputStream in) throws IOException {
		switch (version) {
		case IPageHintWriter.VERSION_4:
			return readPageHintV4(in);
		case IPageHintWriter.VERSION_5:
			return readPageHintV5(in);
		case IPageHintWriter.VERSION_6:
			return readPageHintV6(in);
		default:
			throw new IOException("Unsupported page hint version " + version);
		}
	}

	public PageHint readPageHintV6(DataInputStream in) throws IOException {
		PageHint hint = readPageHintV5(in);
		Collection<PageVariable> variables = hint.getPageVariables();
		readPageVariables(in, variables);
		return hint;
	}

	public PageHint readPageHintV5(DataInputStream in) throws IOException {
		PageHint hint = readPageHintV4(in);
		int columnHintSize = IOUtil.readInt(in);
		for (int i = 0; i < columnHintSize; i++) {
			String tableId = IOUtil.readString(in);
			int start = IOUtil.readInt(in);
			int columnCount = IOUtil.readInt(in);
			hint.addTableColumnHint(new TableColumnHint(tableId, start, columnCount));
		}
		return hint;
	}

	public PageHint readPageHintV4(DataInputStream in) throws IOException {
		long pageNumber = IOUtil.readLong(in);
		String masterPage = IOUtil.readString(in);
		PageHint hint = new PageHint(pageNumber, masterPage);
		hint.setOffset(pageIndexReader.getPageOffset(masterPage));
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
