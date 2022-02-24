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
package org.eclipse.birt.data.engine.impl.document.util;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.RDLoadUtil;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

/**
 * Used for reading subquery result which is generated from existing report
 * document.
 */
public class ExprResultSet2 extends ExprResultSet {
	/**
	 * @param streamManager
	 * @param rdGroupUtil
	 * @param version
	 * @param isBasedOnSecondRD
	 * @throws DataException
	 */
	public ExprResultSet2(String tempDir, StreamManager streamManager, int version, boolean isBasedOnSecondRD,
			int rowIdStartingIndex) throws DataException {
		super(tempDir, streamManager, version, isBasedOnSecondRD, null, rowIdStartingIndex, null);
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.ExprResultSet#prepare()
	 */
	protected void prepare() throws DataException {
		this.rdGroupUtil = RDLoadUtil.loadGroupUtil(tempDir, streamManager, StreamManager.ROOT_STREAM,
				StreamManager.SELF_SCOPE);

		int parentIndex = 0;
		try {
			InputStream inputStream = streamManager.getInStream(DataEngineContext.SUBQUERY_PARENTINDEX_STREAM,
					StreamManager.SUB_QUERY_STREAM, StreamManager.SELF_SCOPE);
			parentIndex = IOUtil.readInt(inputStream);
		} catch (IOException e) {
			throw new DataException(e.getMessage());
		}

		rowExprsRAIs = streamManager.getInStream2(DataEngineContext.EXPR_VALUE_STREAM, StreamManager.SUB_QUERY_STREAM,
				StreamManager.BASE_SCOPE, parentIndex);
		rowLenRAIs = streamManager.getInStream2(DataEngineContext.EXPR_ROWLEN_STREAM, StreamManager.SUB_QUERY_STREAM,
				StreamManager.BASE_SCOPE, parentIndex);
		rowInfoRAIs = streamManager.getInStream(DataEngineContext.ROW_INDEX_STREAM, StreamManager.SUB_QUERY_STREAM,
				StreamManager.SELF_SCOPE);
		this.exprResultReader = new ExprDataReader2(tempDir, rowExprsRAIs, rowLenRAIs, rowInfoRAIs, version, null);

		this.rowCount = this.exprResultReader.getCount();
	}

}
