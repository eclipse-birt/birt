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

package org.eclipse.birt.report.engine.internal.document;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * read a index (binary search)
 *
 */
public class OffsetIndexReader {
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(OffsetIndexReader.class.getName());

	protected String indexFile;
	protected RandomAccessFile index;

	public OffsetIndexReader(String indexFile) {
		this.indexFile = indexFile;
	}

	public void open() throws IOException {
		File file = new File(indexFile);
		index = new RandomAccessFile(file, "r");
	}

	public void close() {
		if (index != null) {
			try {
				index.close();
			} catch (Exception ex) {
			}
		}
	}

	public long find(long target) {
		try {
			long length = index.length();
			if (length < 16) {
				return -1;
			}

			// the first one
			if (target == 0) {
				index.seek(8);
				return index.readLong();
			}

			// use binary search to find the target

			long min = 1;
			long max = length / 16 - 1;
			long ref = (min + max) / 2;
			do {
				index.seek(16 * ref);
				long offset = index.readLong();
				if (target == offset) {
					return index.readLong();
				}
				if (target > offset) {
					min = ref + 1;
					ref = (min + max) / 2;

				} else {
					max = ref - 1;
					ref = (min + max) / 2;
				}
			} while (min <= max);
		} catch (Exception ex) {
			logger.log(Level.WARNING, ex.getMessage(), ex);
		}
		return -1;
	}
}
