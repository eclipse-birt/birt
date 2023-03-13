/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.toc.document.TOCReaderV0;
import org.eclipse.birt.report.engine.toc.document.TOCReaderV1;
import org.eclipse.birt.report.engine.toc.document.TOCReaderV2;
import org.eclipse.birt.report.engine.toc.document.TOCReaderV3;

public class TOCReader implements ITOCReader, ITOCConstants {

	protected String version;
	protected ITOCReader reader;
	protected boolean ownedStream;
	protected RAInputStream stream;

	public TOCReader(IDocArchiveReader archive, ClassLoader loader) throws IOException {
		this(archive.getInputStream(TOC_STREAM), loader);
		ownedStream = true;
	}

	public TOCReader(RAInputStream in, ClassLoader loader) throws IOException {
		this.stream = in;
		version = getVersion(in);
		if (VERSION_V0.equals(version)) {
			reader = new TOCReaderV0(in, false);
		} else if (VERSION_V1.equals(version)) {
			reader = new TOCReaderV1(in, loader, false);
		} else if (VERSION_V2.equals(version)) {
			reader = new TOCReaderV2(in, loader, false);
		} else if (VERSION_V3.equals(version)) {
			reader = new TOCReaderV3(in, loader, false);
		} else {
			in.close();
			throw new IOException("Unsupporter version :" + version);
		}
	}

	public TOCReader(InputStream in, ClassLoader loader) throws IOException {
		version = getVersion(in);
		if (VERSION_V0.equals(version)) {
			reader = new TOCReaderV0(in);
		} else if (VERSION_V1.equals(version)) {
			reader = new TOCReaderV1(in, loader);
		} else if (VERSION_V2.equals(version)) {
			reader = new TOCReaderV2(in, loader);
		} else {
			throw new IOException("Unsupporter version :" + version);
		}
	}

	public String getVersion() {
		return version;
	}

	@Override
	public ITreeNode readTree() throws IOException {
		if (reader != null) {
			return reader.readTree();
		}
		return null;
	}

	@Override
	public void close() throws IOException {
		try {
			if (reader != null) {
				reader.close();
			}
		} finally {
			reader = null;
			if (ownedStream && stream != null) {
				try {
					stream.close();
				} finally {
					stream = null;
				}
			}
		}
	}

	static String getVersion(IDocArchiveReader archive) throws IOException {
		RAInputStream in = archive.getInputStream(TOC_STREAM);
		try (in) {
			return getVersion(in);
		}
	}

	static String getVersion(InputStream in) throws IOException {
		DataInputStream input = new DataInputStream(in);
		String header = IOUtil.readString(input);
		if (header.startsWith(VERSION_PREFIX)) {
			return header;
		}
		return VERSION_V0;
	}
}
