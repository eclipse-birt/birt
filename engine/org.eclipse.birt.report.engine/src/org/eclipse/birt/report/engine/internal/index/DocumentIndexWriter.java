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
