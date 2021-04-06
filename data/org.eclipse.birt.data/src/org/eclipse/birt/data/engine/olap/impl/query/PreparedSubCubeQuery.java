
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.mozilla.javascript.Scriptable;

/**
 * prepare the sub cube query
 *
 */
public class PreparedSubCubeQuery implements IPreparedCubeQuery {
	private ISubCubeQueryDefinition query;
	private Map appContext;
	private DataEngineSession session;

	/**
	 * 
	 * @param query
	 * @param appContext
	 */
	public PreparedSubCubeQuery(ISubCubeQueryDefinition query, Map appContext, DataEngineSession session) {
		this.query = query;
		this.appContext = appContext;
		this.session = session;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#execute(org.mozilla.
	 * javascript.Scriptable)
	 */
	public ICubeQueryResults execute(Scriptable scope) throws DataException {
		throw new DataException(ResourceConstants.NO_PARENT_RESULT_CURSOR);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#execute(org.eclipse.
	 * birt.data.engine.api.IBaseQueryResults, org.mozilla.javascript.Scriptable)
	 */
	public ICubeQueryResults execute(IBaseQueryResults outerResults, Scriptable scope) throws DataException {
		if (outerResults instanceof ICubeQueryResults) {
			ICubeQueryResults parent = (ICubeQueryResults) outerResults;
			return new SubCubeQueryResults(query, parent, scope, session.getEngineContext().getScriptContext());
		} else {
			throw new DataException(ResourceConstants.NO_PARENT_RESULT_CURSOR);
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#
	 * getCubeQueryDefinition()
	 */
	public IBaseCubeQueryDefinition getCubeQueryDefinition() {
		return query;
	}

}
