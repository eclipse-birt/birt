/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.QueryResults;
import org.mozilla.javascript.Scriptable;

public class DummyPreparedQuery implements IPreparedQuery {

	/**
	 * Used for Result Set Sharing.
	 *
	 */
	private IQueryDefinition queryDefn;
	private String tempDir;
	private DataEngineContext context;
	private List<IGroupInstanceInfo> targetGroups;
	private DataEngineSession session;
	private Map appContext;

	/**
	 *
	 * @param queryDefn
	 * @param session
	 */
	public DummyPreparedQuery(IQueryDefinition queryDefn, DataEngineSession session, Map appContext) {
		this.queryDefn = queryDefn;
		this.session = session;
		this.tempDir = session.getTempDir();
		this.appContext = appContext;
	}

	/**
	 *
	 * @param queryDefn
	 * @param session
	 * @param context
	 * @param targetGroups
	 */
	public DummyPreparedQuery(IQueryDefinition queryDefn, DataEngineSession session, DataEngineContext context,
			List<IGroupInstanceInfo> targetGroups) {
		this(queryDefn, session, new HashMap());
		this.context = context;
		this.targetGroups = targetGroups;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.mozilla.
	 * javascript.Scriptable)
	 */
	@Override
	public IQueryResults execute(Scriptable queryScope) throws BirtException {
		return this.execute(null, queryScope);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.eclipse.birt
	 * .data.engine.api.IQueryResults, org.mozilla.javascript.Scriptable)
	 */
	@Override
	public IQueryResults execute(IQueryResults outerResults, Scriptable queryScope) throws BirtException {
		try {
			return this.execute((IBaseQueryResults) outerResults, queryScope);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getParameterMetaData()
	 */
	@Override
	public Collection getParameterMetaData() throws BirtException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getReportQueryDefn()
	 */
	@Override
	public IQueryDefinition getReportQueryDefn() {
		return this.queryDefn;
	}

	@Override
	public IQueryResults execute(IBaseQueryResults outerResults, Scriptable scope) throws DataException {
		try {
			if (context == null) {
				return new CachedQueryResults(session, this.queryDefn.getQueryResultsID(), this, this.appContext);
			} else {
				return new QueryResults(this.tempDir, this.context, this.queryDefn.getQueryResultsID(), outerResults,
						this.targetGroups);
			}
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}
}
