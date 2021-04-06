/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;

public class PageIndexReader {
	IDocArchiveReader reader;
	protected HashMap pages = new HashMap();

	long offset = 0;

	public PageIndexReader(IDocArchiveReader reader) throws IOException {
		this.reader = reader;
	}

	protected long getOffset(String masterPage) {
		Object value = pages.get(masterPage);
		if (value != null && value instanceof Long) {
			return ((Long) value).longValue();
		} else {
			return -1;
		}
	}

	public long getPageOffset(String masterPage) throws IOException {
		Object value = pages.get(masterPage);
		if (value != null && value instanceof Long) {
			return ((Long) value).longValue();
		} else {
			RAInputStream indexStream = null;
			try {
				indexStream = reader.getStream(ReportDocumentConstants.PAGE_INDEX_STREAM);
				DataInputStream input = new DataInputStream(indexStream);
				while (true) {
					String masterPageName = IOUtil.readString(input);
					long pageOffset = IOUtil.readLong(input);
					pages.put(masterPageName, new Long(pageOffset));
				}
			} catch (EOFException eef) {

			} catch (IOException ex) {
				throw ex;
			} finally {
				if (indexStream != null) {
					try {
						indexStream.close();
					} catch (IOException e) {

					}
					indexStream = null;
				}
			}

			value = pages.get(masterPage);
			if (value != null && value instanceof Long) {
				return ((Long) value).longValue();
			}
			Iterator iterator = pages.values().iterator();
			while (iterator.hasNext()) {
				return ((Long) iterator.next()).longValue();
			}
		}
		return 0;
	}

	public void close() {

	}

}
