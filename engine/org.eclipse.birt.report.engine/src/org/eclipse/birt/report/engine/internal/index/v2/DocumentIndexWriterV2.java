/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.index.v2;

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.internal.index.IDocumentIndexWriter;

public class DocumentIndexWriterV2 implements IDocumentIndexWriter, DocumentIndexV2Constants, ReportDocumentConstants {

	IDocArchiveWriter archive;

	IndexWriter bookmarks;
	IndexWriter reportlets;
	IndexWriter pageNumbers;

	public DocumentIndexWriterV2(IDocArchiveWriter archive) throws IOException {
		this.archive = archive;
	}

	public void close() throws IOException {
		if (bookmarks != null) {
			bookmarks.close();
		}
		if (reportlets != null) {
			reportlets.close();
			reportlets = null;
		}
		if (pageNumbers != null) {
			pageNumbers.close();
			pageNumbers = null;
		}
	}

	public void setOffsetOfBookmark(String bookmark, long offset) throws IOException {
		if (bookmarks == null) {
			bookmarks = createIndexWriter(archive, REPORTLET_BOOKMARK_INDEX_STREAM);
		}
		if (bookmarks != null) {
			bookmarks.add(bookmark, offset);
		}
	}

	public void setOffsetOfInstance(String instanceId, long offset) throws IOException {
		if (reportlets == null) {
			reportlets = createIndexWriter(archive, REPORTLET_ID_INDEX_STREAM);
		}
		if (reportlets != null) {
			reportlets.add(instanceId, offset);
		}
	}

	public void setBookmark(String bookmark, BookmarkContent content) throws IOException {
		if (pageNumbers == null) {
			pageNumbers = createIndexWriter(archive, BOOKMARK_STREAM);
		}
		if (pageNumbers != null) {
			pageNumbers.add(bookmark, content);
		}
	}

	IndexWriter createIndexWriter(IDocArchiveWriter archive, String name) {
		return new IndexWriter(archive, name);
	}

}
