/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.internal.index.v0.DocumentIndexReaderV0;
import org.eclipse.birt.report.engine.internal.index.v1.DocumentIndexReaderV1;
import org.eclipse.birt.report.engine.internal.index.v2.DocumentIndexReaderV2;

public class DocumentIndexReader implements IDocumentIndexReader {
	IDocumentIndexReader reader;

	public DocumentIndexReader(int version, HashMap<String, Long> bookmarks, HashMap<String, Long> reportlets,
			HashMap<String, Long> pageNumbers) throws IOException {
		if (version != VERSION_1) {
			throw new IOException("unsupporter version number:" + version);
		}
		reader = new DocumentIndexReaderV1(bookmarks, reportlets, pageNumbers);
	}

	public DocumentIndexReader(int version, IDocArchiveReader archive) throws IOException {
		switch (version) {
		case VERSION_0:
			this.reader = new DocumentIndexReaderV0(archive);
			break;
		case VERSION_2:
			this.reader = new DocumentIndexReaderV2(archive);
			break;
		default:
			throw new IOException("unsupported version number:" + version);
		}
	}

	public void close() throws IOException {
		if (reader != null) {
			try {
				reader.close();
			} finally {
				reader = null;
			}
		}
	}

	public int getVersion() {
		return reader.getVersion();
	}

	public long getOffsetOfBookmark(String bookmark) throws IOException {
		return reader.getOffsetOfBookmark(bookmark);
	}

	public long getOffsetOfInstance(String instanceId) throws IOException {
		return reader.getOffsetOfInstance(instanceId);
	}

	public long getPageOfBookmark(String bookmark) throws IOException {
		return reader.getPageOfBookmark(bookmark);
	}

	public BookmarkContent getBookmark(String bookmark) throws IOException {
		return reader.getBookmark(bookmark);
	}

	public List<String> getBookmarks() throws IOException {
		return reader.getBookmarks();
	}

	public List<BookmarkContent> getBookmarkContents() throws IOException {
		return reader.getBookmarkContents();
	}
}
