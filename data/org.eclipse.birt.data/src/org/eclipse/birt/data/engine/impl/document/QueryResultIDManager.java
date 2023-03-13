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
package org.eclipse.birt.data.engine.impl.document;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

/**
 * Manage the query result id
 */
public class QueryResultIDManager {

	/**
	 * @param streamManager
	 * @return
	 * @throws DataException
	 */
	public static String getNextID(DataEngineSession session, String rootQueryResultID) throws DataException {
		StreamManager streamManager = new StreamManager(session.getEngineContext(),
				new QueryResultInfo(rootQueryResultID, null, -1));

		Set idSet = getIDMap(streamManager).keySet();

		String queryID = null;
		while (true) {
			queryID = session.getQueryResultIDUtil().nextID();
			if (!idSet.contains(queryID)) {
				break;
			}
		}

		return queryID;
	}

	/**
	 * @param streamManager
	 * @param filterList
	 * @throws DataException
	 */
	public static void appendChildToRoot(StreamManager streamManager, List filterList) throws DataException {
		Map idMap = getIDMap(streamManager);

		// write content
		try {
			OutputStream os = streamManager.getOutStream(DataEngineContext.QUERYID_INFO_STREAM,
					StreamManager.ROOT_STREAM, StreamManager.BASE_SCOPE);
			DataOutputStream dos = new DataOutputStream(os);

			int size = idMap.size();
			IOUtil.writeInt(dos, size + 1);

			if (size > 0) {
				Set entrySet = idMap.entrySet();
				Iterator it = entrySet.iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Entry) it.next();
					IOUtil.writeString(dos, (String) entry.getKey());
					IOUtil.writeInt(dos, ((Integer) entry.getValue()).intValue());
				}
			}

			IOUtil.writeString(dos, streamManager.getQueryResultUID());
			IOUtil.writeInt(dos, FilterDefnUtil.hashCode(filterList));

			dos.close();
			os.close();
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e);
		}
	}

	/**
	 * @param streamManager
	 * @param filterList
	 * @throws DataException
	 */
	public static void cleanChildOfRoot(StreamManager streamManager) throws DataException {
		if (!streamManager.hasInStream(DataEngineContext.QUERYID_INFO_STREAM, StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE)) {
			return;
		}

		Map map = getIDMap(streamManager);

		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			String queryID = (String) entry.getKey();
			String _2partID = QueryResultIDUtil.get2PartID(queryID);
			streamManager.dropStream2(_2partID);
		}
	}

	/**
	 * @param streamManager
	 * @return
	 * @throws DataException
	 */
	private static Map getIDMap(StreamManager streamManager) throws DataException {
		Map idMap = new LinkedHashMap();
		if (streamManager.hasInStream(DataEngineContext.QUERYID_INFO_STREAM, StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE)) {
			try {
				InputStream is = streamManager.getInStream(DataEngineContext.QUERYID_INFO_STREAM,
						StreamManager.ROOT_STREAM, StreamManager.BASE_SCOPE);
				BufferedInputStream buffIs = new BufferedInputStream(is);
				DataInputStream dis = new DataInputStream(buffIs);

				int existingNum = IOUtil.readInt(buffIs);

				for (int i = 0; i < existingNum; i++) {
					idMap.put(IOUtil.readString(dis), Integer.valueOf(IOUtil.readInt(dis)));
				}

				dis.close();
				buffIs.close();
				is.close();
			} catch (IOException e) {
				throw new DataException(ResourceConstants.RD_LOAD_ERROR, e);
			}
		}

		return idMap;
	}

}
