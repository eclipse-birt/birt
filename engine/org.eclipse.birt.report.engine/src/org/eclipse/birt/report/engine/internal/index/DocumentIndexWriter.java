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

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.content.impl.BookmarkContent;
import org.eclipse.birt.report.engine.internal.index.v2.DocumentIndexWriterV2;

public class DocumentIndexWriter implements IDocumentIndexWriter {

	IDocumentIndexWriter writer;

	public DocumentIndexWriter(IDocArchiveWriter archive) throws IOException {
		writer = new DocumentIndexWriterV2(archive);
	}

	public void close() throws IOException {
		if (writer != null) {
			writer.close();
			writer = null;
		}
	}

	public void setOffsetOfBookmark(String bookmark, long offset) throws IOException {
		writer.setOffsetOfBookmark(bookmark, offset);
	}

	public void setOffsetOfInstance(String instanceId, long offset) throws IOException {
		writer.setOffsetOfInstance(instanceId, offset);

	}

	public void setBookmark(String bookmark, BookmarkContent content) throws IOException {
		writer.setBookmark(bookmark, content);
	}

}
