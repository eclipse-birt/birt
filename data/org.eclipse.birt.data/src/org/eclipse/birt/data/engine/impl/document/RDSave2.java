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

import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * Used to save the query which is running based on a report document and the
 * result is based on the result set of report document instead of data set. For
 * the latter case, RDSave class will be used.
 */
class RDSave2 implements IRDSave {
	private DataEngineContext context;
	private StreamManager streamManager;
	private RDSaveUtil saveUtilHelper;

	/**
	 * @param context
	 * @param queryDefn
	 * @param queryResultID
	 * @param rowCount
	 * @param subQueryName
	 * @param subQueryIndex
	 * @throws DataException
	 */
	RDSave2(DataEngineContext context, IBaseQueryDefinition queryDefn, QueryResultInfo queryResultInfo)
			throws DataException {
		this.context = context;

		this.streamManager = new StreamManager(context, queryResultInfo);
		this.saveUtilHelper = new RDSaveUtil(this.context, queryDefn, this.streamManager);
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.RDSave#saveExprValue(int,
	 * java.util.Map)
	 */
	public void saveExprValue(int currIndex, Map valueMap) throws DataException {
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.RDSave#saveFinish(int)
	 */
	public void saveFinish(int currIndex) throws DataException {
		this.saveUtilHelper.saveChildQueryID();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IRDSave#saveResultIterator(org.
	 * eclipse.birt.data.engine.odi.IResultIterator, int, int[])
	 */
	public void saveResultIterator(IResultIterator odiResult, int groupLevel, int[] subQueryInfo) throws DataException {
		saveUtilHelper.saveResultIterator(odiResult, groupLevel, subQueryInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.document.IRDSave#saveStart()
	 */
	public void saveStart() throws DataException {
		this.saveUtilHelper.saveQueryDefn();
	}

}
