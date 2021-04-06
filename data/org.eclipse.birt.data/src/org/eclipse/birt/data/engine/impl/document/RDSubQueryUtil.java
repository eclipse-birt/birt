/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.document;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Sub query util class for save and load.
 * 
 * Sub query is a special query because its data is extracted from its
 * associated parent query rathen than from an independent data set. User does
 * not need to explicitly preparation job and can directly retrieve the sub
 * query data from the result iterator of its parent query. In report document,
 * this behavior will kept as before. To achieve it, DtE needs to implicitly
 * store the sub query data when result iterator for it is closed.
 * 
 * As for a normal query, the ID for it is its query result ID, but for a sub
 * query, its ID is its parent query result ID, the name of sub query and its
 * group index (sub query index). So there is below foler hierarchy for a sub
 * query.
 * 
 * ----parent query results ID (folder) --------------------------sub query name
 * (file) ----------------------------------------sub query index 1 (folder)
 * ----------------------------------------sub query index 2 (folder)
 * 
 * In getting sub query, how to know which current sub query index is. We need
 * to ssave such a relationship about the parent result index with its sub query
 * index. This information will be generated in report generation time and will
 * be used in report presentation time. Such a relationship is attached with a
 * sub query name, not with sub query index, so there is only one copy of it.
 * Above hierarchy can be updated as follows:
 * 
 * ----parent query results ID (folder) --------------------------sub query name
 * (file) --------------------------sub query information (file)
 * ----------------------------------------sub query index 1 (folder)
 * ----------------------------------------sub query index 2 (folder)
 */
public class RDSubQueryUtil {
	// data engine context
	private DataEngineContext context;

	// parent query result id
	private String queryResultID;

	// sub query name
	private String subQueryName;

	// group level
	private int groupLevel;

	/**
	 * A simple corresponding relationship of parent query group information. An
	 * exmaple is: [0, 4, 4, 8]. It can be rearranged as below: 0, 4 (0-3 belongs to
	 * group 1) 4, 8 (4-7 belongs to group 2)
	 */
	private int[] subQueryInfo;

	/**
	 * @param context
	 */
	RDSubQueryUtil(DataEngineContext context, String queryResultID, String subQueryName) {
		this.context = context;
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
	}

	/**
	 * @param subQueryInfo
	 * @throws DataException
	 */
	public static void doSave(OutputStream stream, int groupLevel, int[] subQueryInfo) throws DataException {
		if (subQueryInfo == null)
			return;

		try {
			IOUtil.writeInt(stream, groupLevel);

			int size = subQueryInfo.length;
			IOUtil.writeInt(stream, size);
			for (int i = 0; i < size; i++)
				IOUtil.writeInt(stream, subQueryInfo[i]);

			stream.close();
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_SAVE_ERROR, e, "Subquery");
		}
	}

	/**
	 * @return sub query index for currParentIndex
	 * @throws DataException
	 */
	int getSubQueryIndex(int currParentIndex) throws DataException {
		this.loadSubQuery();
		return findSubQueryIndex(currParentIndex, this.groupLevel, this.subQueryInfo);
	}

	/**
	 * @return sub query info
	 * @throws DataException
	 */
	private void loadSubQuery() throws DataException {
		if (subQueryInfo != null)
			return;

		InputStream stream = context.getInputStream(queryResultID, subQueryName,
				DataEngineContext.SUBQUERY_INFO_STREAM);
		try {
			BufferedInputStream bis = new BufferedInputStream(stream);

			groupLevel = IOUtil.readInt(bis);

			int size = IOUtil.readInt(bis);
			subQueryInfo = new int[size];
			for (int i = 0; i < size; i++)
				subQueryInfo[i] = IOUtil.readInt(bis);

			bis.close();
			stream.close();
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_LOAD_ERROR, e, "Subquery");
		}
	}

	/**
	 * @param currParentIndex
	 * @return
	 */
	private static int findSubQueryIndex(int currParentIndex, int groupLevel, int[] subQueryInfo) {
		if (groupLevel == 0)
			return 0;

		int subQueryCount = subQueryInfo.length / 2;
		int subQueryIndex;

		for (subQueryIndex = 0; subQueryIndex < subQueryCount; subQueryIndex++)
			if (currParentIndex < subQueryInfo[subQueryIndex * 2 + 1])
				break;

		return subQueryIndex;
	}

}
