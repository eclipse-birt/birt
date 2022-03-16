
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
	private DataEngineSession session;

	/**
	 *
	 * @param query
	 * @param appContext
	 */
	public PreparedSubCubeQuery(ISubCubeQueryDefinition query, Map appContext, DataEngineSession session) {
		this.query = query;
		this.session = session;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#execute(org.mozilla.
	 * javascript.Scriptable)
	 */
	@Override
	public ICubeQueryResults execute(Scriptable scope) throws DataException {
		throw new DataException(ResourceConstants.NO_PARENT_RESULT_CURSOR);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#execute(org.eclipse.
	 * birt.data.engine.api.IBaseQueryResults, org.mozilla.javascript.Scriptable)
	 */
	@Override
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
	@Override
	public IBaseCubeQueryDefinition getCubeQueryDefinition() {
		return query;
	}

}
