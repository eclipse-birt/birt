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
import java.util.List;

import org.eclipse.birt.report.engine.content.impl.BookmarkContent;

public interface IDocumentIndexReader extends IDocumentIndexVersion {

	int getVersion();

	List<String> getBookmarks() throws IOException;

	List<BookmarkContent> getBookmarkContents() throws IOException;

	long getPageOfBookmark(String bookmark) throws IOException;

	long getOffsetOfBookmark(String bookmark) throws IOException;

	long getOffsetOfInstance(String instanceId) throws IOException;

	BookmarkContent getBookmark(String bookmark) throws IOException;

	void close() throws IOException;
}
