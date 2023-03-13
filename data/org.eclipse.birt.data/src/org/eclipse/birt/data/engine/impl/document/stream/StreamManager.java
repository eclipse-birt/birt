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

package org.eclipse.birt.data.engine.impl.document.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDManager;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;

/**
 * Manage the input stream and output stream when reading/writing data into
 * report document. There is a big difference for queryResultID in
 * reading/writing. In reading context, the rootQueryResultID,
 * parentQueryResultID and queryResultID will be combined to get the correct
 * input stream. But in writing context, only the queryResultID is needed.
 */
public class StreamManager {

	/**
	 * A possible directory of stream:
	 *
	 * [QuRs0] ----------------------------------<<QUERY_ROOT_STREAM>>,
	 * <<BASE_SCOPE>> DataSetData ExprMetaInfo ExprValue QueryDefn GroupInfo
	 * QueryIDInfo ResultClass RowLengthInfo OriginalQueryDefinition -----[IAMTEST]
	 * ---------------------------<<SUB_QUERY_ROOT_STREAM>> SubQueryInfo
	 * ---------[0] ---------------------------<<SUB_QUERY_STREAM>> ExprMetaInfo
	 * ExprValue GroupInfo RowLengthInfo ---------[1] ExprMetaInfo ExprValue
	 * GroupInfo RowLengthInfo -----[QuRs1]
	 * -----------------------------<<QUERY_ROOT_STREAM>>, <<SELF_SCOPE>> QueryDefn
	 * GroupInfo RowIndexInfo ---------[IAMTEST]
	 * -----------------------<<SUB_QUERY_ROOT_STREAM>> SubQueryInfo
	 * -------------[0] -----------------------<<SUB_QUERY_STREAM>> GroupInfo
	 * RowIndexInfo << maybe refer to 1 of parent >> -------------[1] GroupInfo
	 * RowIndexInfo << maybe refer to 0 of parent >>
	 *
	 */

	/**
	 * Actually RDLoad and RDSave needs to have a statue to indicate which type of
	 * query is running, and possible values are:
	 *
	 * Normal query. Normal sub query.
	 *
	 * Query is generated from normal query. Sub query is generated from normal sub
	 * query.
	 */

	//
	private DataEngineContext context;

	private String rootQueryResultID;
	private String parentQueryResultID;
	private String selfQueryResultID;

	private String subQueryID;
	private String subQueryName;

	public final static int BASE_SCOPE = 0;
	public final static int PARENT_SCOPE = 1;
	public final static int SELF_SCOPE = 2;

	public final static int ROOT_STREAM = 0;
	public final static int SUB_QUERY_ROOT_STREAM = 1;
	public final static int SUB_QUERY_STREAM = 2;

	private HashMap<StreamID, StreamWriter> cachedStreamManagers;
	private HashMap<StreamID, MetaStreamReader> metaManagers;
	private HashMap<StreamID, DataStreamReader> dataMetaManagers;
	private int version;
	private static Logger logger = Logger.getLogger(StreamManager.class.getName());

	/**
	 * @param context
	 * @throws DataException
	 */
	public StreamManager(DataEngineContext context, QueryResultInfo queryResultInfo) throws DataException {
		this.context = context;

		this.rootQueryResultID = queryResultInfo.getRootQueryResultID();
		this.parentQueryResultID = queryResultInfo.getParentQueryResultID();
		this.selfQueryResultID = queryResultInfo.getSelfQueryResultID();

		this.subQueryName = queryResultInfo.getSubQueryName();
		this.subQueryID = subQueryName == null ? null
				: QueryResultIDUtil.buildSubQueryID(subQueryName, queryResultInfo.getIndex());
		this.cachedStreamManagers = new HashMap<>();
		this.metaManagers = new HashMap<>();
		this.dataMetaManagers = new HashMap<>();
		VersionManager vm = new VersionManager(context);
		if (context.getMode() == DataEngineContext.MODE_GENERATION) {
			this.version = vm.getVersion(this.getQueryResultUID());

			// Only in the BDO mode the document version in a generation task
			// returns a non VERSION_2_0 value. The BDO is introduced after 2_5_0
			// so we are safe to use 2_0 as the indication of BDO mode

			if (this.version == VersionManager.VERSION_2_0) {
				// non .data based report document generation mode, or BDO generation mode. Save
				// one general version for
				// all the queries
				this.version = VersionManager.getLatestVersion();
			}

			vm.setVersion(version, this.getQueryResultUID());
			// Keep it by now so that not to introduce potential backward issue.
			vm.setVersion(VersionManager.getLatestVersion(), null);

		} else {
			String queryResultId = this.getQueryResultUID();
			this.version = vm.getVersion(queryResultId);

			// TODO: Remove me. We should not reset the version id in IV. We add following
			// logic
			// to temporiarily resolve [25079]. Dashboard's using of Data Engine API is
			// incorrect.We always need first issue a generation task before any IV
			// operation.
			if (this.version == 0 && queryResultId != null) {
				this.version = VersionManager.getLatestVersion();
				if (this.context.getDocWriter() != null) {
					vm.setVersion(this.version, queryResultId);
				}
			}
		}
	}

	public RAInputStream getInStream(String streamSubName) throws DataException {
		StreamID streamID = getStreamID(0, ROOT_STREAM, BASE_SCOPE);
		RAInputStream inputStream;
		try {
			if (!context.getDocWriter().exists("/" + streamID.getStartStream() + "/" + streamSubName)) {
				return null;
			}
			inputStream = context.getDocWriter().getInputStream("/" + streamID.getStartStream() + "/" + streamSubName);
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
		return inputStream;
	}

	public RAOutputStream getOutStream(String streamSubName) throws DataException {
		StreamID streamID = getStreamID(0, ROOT_STREAM, BASE_SCOPE);
		RAOutputStream outputStream;
		try {
			outputStream = context.getDocWriter()
					.getOutputStream("/" + streamID.getStartStream() + "/" + streamSubName);
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
		return outputStream;
	}

	public RAOutputStream getOutStream(int streamType, int subIndex) throws DataException {
		StreamID streamID = getStreamID(0, ROOT_STREAM, BASE_SCOPE);
		RAOutputStream outputStream;
		try {
			String path = DataEngineContext.getPath(streamID.getStartStream(), streamID.getSubQueryStream(),
					streamType);
			outputStream = context.getDocWriter().getOutputStream(path + "/" + subIndex);
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
		return outputStream;
	}

	public String getOutStreamName(String streamSubName) throws DataException {
		StreamID streamID = getStreamID(0, ROOT_STREAM, BASE_SCOPE);
		return "/" + streamID.getStartStream() + "/" + streamSubName;
	}

	/**
	 * @param streamType
	 * @param streamPos
	 * @param streamScope
	 * @return
	 * @throws DataException
	 */
	public OutputStream getOutStream(int streamType, int streamPos, int streamScope) throws DataException {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		if (!useTempStream(streamType)) {
			RAOutputStream outputStream = context.getOutputStream(streamID.getStartStream(),
					streamID.getSubQueryStream(), streamType);
			if (this.version >= VersionManager.VERSION_2_5_2_1) {
				return outputStream;
			}
			if (streamType == DataEngineContext.DATASET_DATA_STREAM && this.version >= VersionManager.VERSION_2_2_0) {
				try {
					outputStream.seek(outputStream.length());
					outputStream.writeInt(0);
					outputStream.writeInt(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.log(Level.FINE, e.getMessage(), e);
				}
			}
			return outputStream;
		} else {
			int sType = DataEngineContext.META_STREAM;
			if (streamType == DataEngineContext.DATASET_DATA_STREAM
					|| streamType == DataEngineContext.DATASET_META_STREAM) {
				sType = DataEngineContext.DATASET_DATA_STREAM;
			}

			return this.getTempStreamManager(getStreamID(sType, streamPos, streamScope)).getOutputStream(streamType);
		}
	}

	public OutputStream getOutStream(int streamType, int streamPos, int streamScope, String subName)
			throws DataException {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		return context.getOutputStream(streamID.getStartStream(), streamID.getSubQueryStream(), streamType, subName);
	}

	/**
	 * @param streamType
	 * @return
	 * @throws DataException
	 */
	public RAInputStream getInStream(int streamType, String startStream, String subQueryStream) throws DataException {
		StreamID streamID = new StreamID(startStream, subQueryStream);
		return createInputStream(streamID, streamType);
	}

	/**
	 * @param streamType
	 * @return
	 * @throws DataException
	 */
	public RAInputStream getInStream(int streamType, int streamPos, int streamScope) throws DataException {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		return createInputStream(streamID, streamType);
	}

	public RAInputStream getInStream(int streamType, int streamPos, int streamScope, String subname)
			throws DataException {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		return (RAInputStream) context.getInputStream(streamID.getStartStream(), streamID.getSubQueryStream(),
				streamType, subname);
	}

	/**
	 * This logic is special for sub query, since it needs to dynamically change to
	 * locate its parent sub query folder. The reason is the sub query indx in the
	 * new sub query might not be the same as its original sub query index. Don't
	 * use it in other cases.
	 *
	 * @param streamType
	 * @param streamPos
	 * @param streamScope
	 * @return the input stream for sub query index of original sub query
	 * @throws DataException
	 */
	public RAInputStream getInStream2(int streamType, int streamPos, int streamScope, int parentIndex)
			throws DataException {
		String tempID = subQueryID;
		subQueryID = QueryResultIDUtil.buildSubQueryID(subQueryName, parentIndex);
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		subQueryID = tempID;

		return createInputStream(streamID, streamType);
	}

	private RAInputStream createInputStream(StreamID streamID, int streamType) throws DataException {
		if (!useTempStream(streamType)) {
			RAInputStream stream = (RAInputStream) context.getInputStream(streamID.getStartStream(),
					streamID.getSubQueryStream(), streamType);
			if (this.version >= VersionManager.VERSION_2_5_2_1) {
				return stream;
			}
			if (streamType == DataEngineContext.DATASET_DATA_STREAM && this.version >= VersionManager.VERSION_2_2_0) {
				try {
					stream.readInt();
					int size = stream.readInt();
					stream.skip(size);
					stream.readInt();
					stream.readInt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.log(Level.FINE, e.getMessage(), e);
				}
			}
			return stream;
		} else {
			return this.getMetaManager(streamID, streamType).getRAInputStream(streamType);
		}
	}

	/**
	 *
	 * @return
	 */
	public IDocArchiveWriter getDocWriter() {
		return context.getDocWriter();
	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws DataException
	 */
	private StreamReader getMetaManager(StreamID id, int sType) throws DataException {
		if (sType == DataEngineContext.DATASET_DATA_STREAM || sType == DataEngineContext.DATASET_META_STREAM) {
			if (this.dataMetaManagers.get(id) == null) {
				this.dataMetaManagers.put(id, new DataStreamReader(this.context, id));
			}
			return this.dataMetaManagers.get(id);
		}

		if (this.metaManagers.get(id) == null) {
			this.metaManagers.put(id, new MetaStreamReader(this.context, id));

		}
		return this.metaManagers.get(id);
	}

	public void dropStreams(int streamType) throws DataException {
		if (this.context.getDocWriter() != null) {
			StreamID streamID = getStreamID(streamType, StreamManager.ROOT_STREAM, StreamManager.BASE_SCOPE);
			try {
				String path = DataEngineContext.getPath(streamID.getStartStream(), streamID.getSubQueryStream(),
						streamType);
				List<String> streams = context.getDocWriter().listStreams(path);
				for (String streamName : streams) {
					context.getDocWriter().dropStream(streamName);
				}
				if (context.getDocWriter().exists(path)) {
					context.getDocWriter().dropStream(path);
				}

			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}
	}

	public List<RAInputStream> getInStreams(int streamType, int streamPos, int streamScope) throws DataException {

		if (this.context.getDocReader() != null) {
			StreamID streamID = getStreamID(streamType, streamPos, streamScope);
			try {
				List<String> streams = this.context.getDocReader().listStreams(
						DataEngineContext.getPath(streamID.getStartStream(), streamID.getSubQueryStream(), streamType));
				Collections.sort(streams);
				List<RAInputStream> results = new ArrayList<>();
				for (String streamName : streams) {
					results.add(this.context.getDocReader().getInputStream(streamName));
				}
				return results;
			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}
		return new ArrayList<>();
	}

	/**
	 * @param streamType
	 * @return
	 * @throws DataException
	 */
	public boolean hasInStream(int streamType, int streamPos, int streamScope) throws DataException {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		if (useTempStream(streamType)) {
			/*
			 * if ( this.metaManagers.get( streamID ) == null ) return false;
			 */
			return this.getMetaManager(streamID, streamType).hasInputStream(streamType);
		} else {
			return context.hasInStream(streamID.getStartStream(), streamID.getSubQueryStream(), streamType);
		}
	}

	public boolean hasInStream(int streamType, int streamPos, int streamScope, String subname) throws DataException {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		return context.hasInStream(streamID.getStartStream(), streamID.getSubQueryStream(), streamType, subname);
	}

	/**
	 * @param streamType
	 * @return
	 */
	public boolean hasOutStream(int streamType, int streamPos, int streamScope) {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		if (!useTempStream(streamType)) {
			return context.hasOutStream(streamID.getStartStream(), streamID.getSubQueryStream(), streamType);
		} else {
			return this.getTempStreamManager(getStreamID(DataEngineContext.META_STREAM, streamPos, streamScope))
					.hasOutputStream(streamID);
		}
	}

	/**
	 * Drop specified streamType
	 *
	 * @param streamType
	 * @throws DataException
	 */
	public void dropStream1(int streamType) throws DataException {
		StreamID streamID = getStreamID(streamType, StreamManager.ROOT_STREAM, StreamManager.BASE_SCOPE);
		context.dropStream(streamID.getStartStream(), null, streamType);
	}

	/**
	 * Drop specified sub stream
	 *
	 * @param subStream
	 * @throws DataException
	 */
	public void dropStream2(String subStream) throws DataException {
		StreamID streamID = getStreamID(-1, StreamManager.ROOT_STREAM, StreamManager.BASE_SCOPE);
		String realStream = streamID.getStartStream() + "/" + subStream;
		context.dropStream(realStream);
	}

	public void dropStream(int streamType, int streamPos, int streamScope, String subName) {
		StreamID streamID = getStreamID(streamType, streamPos, streamScope);
		context.dropStream(streamID.getStartStream(), streamID.getSubQueryStream(), streamType, subName);
	}

	/**
	 *
	 * @return
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * @param streamPos
	 * @param streamScope
	 * @return
	 */
	private StreamID getStreamID(int streamType, int streamPos, int streamScope) {
		String startStream = null;
		if (streamScope == BASE_SCOPE) {
			if (rootQueryResultID != null) {
				startStream = rootQueryResultID;
			} else {
				startStream = selfQueryResultID;
			}
		} else if (streamScope == PARENT_SCOPE) {
			startStream = QueryResultIDUtil.getRealStreamID(rootQueryResultID, parentQueryResultID);
		} else if (streamScope == SELF_SCOPE) {
			startStream = QueryResultIDUtil.getRealStreamID(rootQueryResultID, selfQueryResultID);
		}

		String subQueryStream = null;
		if (!isSubquery()) {
			if (streamPos == ROOT_STREAM) {
				subQueryStream = null;
			} else if (streamPos == SUB_QUERY_ROOT_STREAM) {
				subQueryStream = subQueryName;
			} else if (streamPos == SUB_QUERY_STREAM) {
				subQueryStream = subQueryID;
			}
		} else if (streamPos == ROOT_STREAM) {
			subQueryStream = subQueryID;
		} else if (streamPos == SUB_QUERY_ROOT_STREAM) {
			subQueryStream = subQueryName;
		} else if (streamPos == SUB_QUERY_STREAM) {
			subQueryStream = subQueryID;
		}

		return new StreamID(startStream, subQueryStream);
	}

	/**
	 * @return
	 */
	public boolean isSubquery() {
		return this.subQueryName != null;
	}

	/**
	 * Original query -> report document 1
	 *
	 * new query ---------> report document 1.2
	 *
	 * new query ---------------------> report document 1.2.1
	 *
	 * new query ---------> report document 1.3
	 *
	 * Used in read. To determine whether the associated stream manager is to read
	 * the query which is running on a report document.
	 *
	 * @return
	 * @throws DataException
	 */
	public boolean isBasedOnSecondRD() throws DataException {
		return parentQueryResultID != null;
	}

	/**
	 * Used in read and write. To determine whether the associated stream manager is
	 * to read/write the query which is running on a report document. If
	 * isBasedOnSecondRD is true, isSecondRD must be true.
	 *
	 * @return
	 * @throws DataException
	 */
	public boolean isSecondRD() throws DataException {
		return rootQueryResultID != null;
	}

	/**
	 * @return
	 */
	public String getQueryResultUID() {
		return QueryResultIDUtil.buildID(rootQueryResultID, selfQueryResultID);
	}

	private StreamWriter getTempStreamManager(StreamID id) {
		if (this.cachedStreamManagers.get(id) == null) {
			this.cachedStreamManagers.put(id, new StreamWriter(this.context, id));
		}
		return this.cachedStreamManagers.get(id);
	}

	/**
	 *
	 * @param streamType
	 * @return
	 */
	private boolean useTempStream(int streamType) {
		if (this.version < VersionManager.VERSION_2_2 || this.version >= VersionManager.VERSION_2_5_2_1) {
			return false;
		}

		switch (streamType) {
		case DataEngineContext.DATASET_DATA_STREAM:
			return false;
		case DataEngineContext.DATASET_META_STREAM:
			return !(this.version < VersionManager.VERSION_2_2_0);
		case DataEngineContext.DATASET_DATA_LEN_STREAM:
			return false;
		case DataEngineContext.EXPR_VALUE_STREAM:
			return false;
		case DataEngineContext.EXPR_ROWLEN_STREAM:
			return this.version < VersionManager.VERSION_2_2_0;
		case DataEngineContext.EXPR_META_STREAM:
			return true;
		case DataEngineContext.GROUP_INFO_STREAM:
			return true;
		case DataEngineContext.SUBQUERY_INFO_STREAM:
			return false;
		case DataEngineContext.QUERY_DEFN_STREAM:
			return true;
		case DataEngineContext.ORIGINAL_QUERY_DEFN_STREAM:
			return false;
		case DataEngineContext.ROW_INDEX_STREAM:
			return true;
		case DataEngineContext.QUERYID_INFO_STREAM:
			return true;
		case DataEngineContext.SUBQUERY_PARENTINDEX_STREAM:
			return true;
		case DataEngineContext.PLS_GROUPLEVEL_STREAM:
			return false;
		case DataEngineContext.META_STREAM:
			return false;
		case DataEngineContext.META_INDEX_STREAM:
			return false;
		case DataEngineContext.AGGR_INDEX_STREAM:
			return true;
		case DataEngineContext.AGGR_VALUE_STREAM:
			return true;
		default:
			return false;

		}
	}

	public void clearOldStreamsForIV() throws DataException {
		this.dropStream1(DataEngineContext.EXPR_VALUE_STREAM);
		this.dropStream1(DataEngineContext.EXPR_ROWLEN_STREAM);
		// remove QUERYID_INFO_STREAM
		QueryResultIDManager.cleanChildOfRoot(this);
		this.dropStream1(DataEngineContext.META_STREAM);
		this.dropStream1(DataEngineContext.META_INDEX_STREAM);

		this.dropStreams(DataEngineContext.PROGRESSIVE_VIEWING_GROUP_STREAM);
		this.dropStreams(DataEngineContext.AGGR_INDEX_STREAM);
		this.dropStreams(DataEngineContext.AGGR_VALUE_STREAM);
		this.dropStreams(DataEngineContext.COMBINED_AGGR_INDEX_STREAM);
		this.dropStreams(DataEngineContext.COMBINED_AGGR_VALUE_STREAM);
	}

}
