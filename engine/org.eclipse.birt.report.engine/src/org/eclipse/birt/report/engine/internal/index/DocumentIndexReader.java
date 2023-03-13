/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

	@Override
	public void close() throws IOException {
		if (reader != null) {
			try {
				reader.close();
			} finally {
				reader = null;
			}
		}
	}

	@Override
	public int getVersion() {
		return reader.getVersion();
	}

	@Override
	public long getOffsetOfBookmark(String bookmark) throws IOException {
		return reader.getOffsetOfBookmark(bookmark);
	}

	@Override
	public long getOffsetOfInstance(String instanceId) throws IOException {
		return reader.getOffsetOfInstance(instanceId);
	}

	@Override
	public long getPageOfBookmark(String bookmark) throws IOException {
		return reader.getPageOfBookmark(bookmark);
	}

	@Override
	public BookmarkContent getBookmark(String bookmark) throws IOException {
		return reader.getBookmark(bookmark);
	}

	@Override
	public List<String> getBookmarks() throws IOException {
		return reader.getBookmarks();
	}

	@Override
	public List<BookmarkContent> getBookmarkContents() throws IOException {
		return reader.getBookmarkContents();
	}
}
