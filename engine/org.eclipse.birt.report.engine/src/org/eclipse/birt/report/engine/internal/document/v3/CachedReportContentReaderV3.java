/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;

public class CachedReportContentReaderV3 {
	protected ReportContentReaderV3 reader;

	public CachedReportContentReaderV3(ReportContent reportContent, RAInputStream stream, ExecutionContext context)
			throws IOException {
		this.reader = new ReportContentReaderV3(reportContent, stream, context.getApplicationClassLoader());
	}

	public boolean isEmpty() {
		return reader.isEmpty();
	}

	public void close() {
		if (reader != null) {
			reader.close();
			caches.clear();
			reader = null;
		}
	}

	protected static class CacheEntry {
		long offset;
		IContent content;

		public CacheEntry(long offset, IContent content) {
			this.offset = offset;
			this.content = content;
		}
	}

	protected HashMap caches = new HashMap();

	protected void addCache(long offset, IContent content) {
		caches.put(new Long(offset), new CacheEntry(offset, content));
	}

	protected void removeCache(long offset) {
		final Long hashKey = new Long(offset);
		caches.remove(hashKey);
		return;
	}

	protected IContent findCache(long offset) {
		final Long hashKey = new Long(offset);
		final CacheEntry cache = (CacheEntry) caches.get(hashKey);
		if (cache != null)
			return cache.content;
		return null;
	}

	public void unloadContent(long offset) {
		removeCache(offset);
	}

	public IContent loadContent(long offset) throws IOException {
		IContent content = findCache(offset);
		if (content != null) {
			return content;
		}

		// try to load the content from the stream

		content = reader.readContent(offset);
		DocumentExtension docExt = (DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION);
		long pOffset = docExt.getParent();
		if (pOffset != -1) {
			IContent parent = loadContent(pOffset);
			content.setParent(parent);
		}
		addCache(offset, content);
		return content;
	}

	public long getRootOffset() {
		return reader.getRoot();
	}

	public long getCurrentOffset() {
		return reader.getOffset();
	}
}
