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
