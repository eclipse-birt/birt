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

package org.eclipse.birt.report.engine.data.dte;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;

public class DteMetaInfoIOUtil {
	/**
	 * Meta information's version. From version 1, rowId will be stored as a String
	 * by supporting cube result set, while it is Long before.
	 */
	protected final static String VERSION_1 = "__version__1";

	/**
	 * In Version 2, the stored information is changed to: [parent id] [raw id]
	 * [query id] [rset id] [row id]
	 */
	protected final static String VERSION_2 = "__version__2";

	/**
	 * save the metadata into the streams.
	 *
	 * @param key
	 */
	static public void storeMetaInfo(DataOutputStream dos, String pRsetId, String rawId, String queryId, String rsetId,
			String rowId) throws IOException {
		IOUtil.writeString(dos, pRsetId);
		IOUtil.writeString(dos, rawId);
		IOUtil.writeString(dos, queryId);
		IOUtil.writeString(dos, rsetId);
		IOUtil.writeString(dos, rowId);
	}

	static public void startMetaInfo(DataOutputStream dos) throws IOException {
		IOUtil.writeString(dos, VERSION_2);
	}

	static public ArrayList loadAllDteMetaInfo(IDocArchiveReader archive) throws IOException {
		ArrayList result = new ArrayList();

		if (archive.exists(ReportDocumentConstants.DATA_META_STREAM)) {
			InputStream in = archive.getStream(ReportDocumentConstants.DATA_META_STREAM);
			try (in) {
				loadDteMetaInfo(result, new DataInputStream(in));
			}
		}

		if (archive.exists(ReportDocumentConstants.DATA_SNAP_META_STREAM)) {
			InputStream in = archive.getStream(ReportDocumentConstants.DATA_SNAP_META_STREAM);
			try (in) {
				loadDteMetaInfo(result, new DataInputStream(in));
			}
		}

		return result;
	}

	static public ArrayList loadDteMetaInfo(IDocArchiveReader archive) throws IOException {
		ArrayList result = new ArrayList();

		if (archive.exists(ReportDocumentConstants.DATA_SNAP_META_STREAM)) {
			InputStream in = archive.getStream(ReportDocumentConstants.DATA_SNAP_META_STREAM);
			try (in) {
				loadDteMetaInfo(result, new DataInputStream(in));
			}
		} else if (archive.exists(ReportDocumentConstants.DATA_META_STREAM)) {
			InputStream in = archive.getStream(ReportDocumentConstants.DATA_META_STREAM);
			try (in) {
				loadDteMetaInfo(result, new DataInputStream(in));
			}
		}
		return result;
	}

	static public void loadDteMetaInfo(ArrayList result, DataInputStream dis) throws IOException {
		try {
			String version = IOUtil.readString(dis);
			boolean version1 = VERSION_1.equals(version);
			boolean version2 = VERSION_2.equals(version);

			String pRsetId;
			String rawId;
			String queryId;
			String rsetId;
			String rowId = "-1";

			if (version1 || version2) {
				pRsetId = IOUtil.readString(dis);
				rawId = IOUtil.readString(dis);
			} else {
				pRsetId = version;
				rawId = String.valueOf(IOUtil.readLong(dis));
			}

			queryId = IOUtil.readString(dis);
			rsetId = IOUtil.readString(dis);
			if (version2) {
				rowId = IOUtil.readString(dis);
			}
			result.add(new String[] { pRsetId, rawId, queryId, rsetId, rowId });

			while (true) {
				pRsetId = IOUtil.readString(dis);
				if (version1 || version2) {
					rawId = IOUtil.readString(dis);
				} else {
					rawId = String.valueOf(IOUtil.readLong(dis));
				}
				queryId = IOUtil.readString(dis);
				rsetId = IOUtil.readString(dis);
				if (version2) {
					rowId = IOUtil.readString(dis);
				}
				result.add(new String[] { pRsetId, rawId, queryId, rsetId, rowId });
			}
		} catch (EOFException eofe) {
			// we expect that there should be an EOFexception
		}
	}
}
