
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.api;

import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.mozilla.javascript.Scriptable;

/**
 * The IPreparedCubeQuery provides methods to acquire ICubeQueryResults instance
 * from an ICubeQueryDefinition
 */

public interface IPreparedCubeQuery extends IBasePreparedQuery {
	/**
	 * Return the CubeCursor as defined by ICubeQueryDefinition.
	 * 
	 * @param scope
	 * @return
	 * @throws DataException
	 * @deprecated
	 */
	public ICubeQueryResults execute(Scriptable scope) throws DataException;

	/**
	 * Return the query definition which is used to generate current
	 * IPreparedCubeQuery instance.
	 * 
	 * @return
	 */
	public IBaseCubeQueryDefinition getCubeQueryDefinition();

	/**
	 * Executes the prepared execution plan as an inner query that appears within
	 * the scope of another query. The outer query must have been prepared and
	 * executed, and its results given as a parameter to this method.
	 * 
	 * @param outerResults
	 * @param scope
	 * @return
	 * @throws DataException
	 */
	public ICubeQueryResults execute(IBaseQueryResults outerResults, Scriptable scope) throws DataException;
}
