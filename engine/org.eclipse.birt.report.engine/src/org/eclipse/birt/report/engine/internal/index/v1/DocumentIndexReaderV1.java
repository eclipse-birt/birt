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

package org.eclipse.birt.report.engine.internal.index.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.internal.index.IDocumentIndexReader;
import org.eclipse.birt.report.engine.toc.TOCBuilder;

public class DocumentIndexReaderV1 implements IDocumentIndexReader, ReportDocumentConstants {

	private HashMap<String, Long> bookmarks;
	private HashMap<String, Long> reportlets;
	private HashMap<String, Long> pageNumbers;

	public DocumentIndexReaderV1(HashMap<String, Long> bookmarks, HashMap<String, Long> reportlets,
			HashMap<String, Long> pageNumbers) throws IOException {
		this.bookmarks = bookmarks;
		this.reportlets = reportlets;
		this.pageNumbers = pageNumbers;
	}

	@Override
	public int getVersion() {
		return VERSION_1;
	}

	@Override
	public void close() {
	}

	@Override
	public long getOffsetOfBookmark(String bookmark) throws IOException {
		if (bookmarks != null) {
			Long offset = bookmarks.get(bookmark);
			if (offset != null) {
				return offset.longValue();
			}
		}
		return -1L;
	}

	@Override
	public long getOffsetOfInstance(String instanceId) throws IOException {
		if (reportlets != null) {
			Long offset = reportlets.get(instanceId);
			if (offset != null) {
				return offset.longValue();
			}
		}
		return -1L;
	}

	@Override
	public long getPageOfBookmark(String bookmark) throws IOException {
		if (pageNumbers != null) {
			Long pageNumber = pageNumbers.get(bookmark);
			if (pageNumber != null) {
				return pageNumber.longValue();
			}
		}
		return -1L;
	}

	@Override
	public List<String> getBookmarks() throws IOException {
		if (pageNumbers != null) {
			ArrayList<String> list = new ArrayList<>();
			for (String bookmark : pageNumbers.keySet()) {
				if (bookmark != null && !bookmark.startsWith(TOCBuilder.TOC_PREFIX)) {
					list.add(bookmark);
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public BookmarkContent getBookmark(String bookmark) {
		return null;
	}

	@Override
	public List<BookmarkContent> getBookmarkContents() throws IOException {
		return null;
	}
}
