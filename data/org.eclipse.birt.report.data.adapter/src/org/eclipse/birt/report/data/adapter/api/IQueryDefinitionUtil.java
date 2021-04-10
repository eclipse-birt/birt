
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public interface IQueryDefinitionUtil {
	/**
	 * 
	 * @param name
	 * @param srcSubQueryDefn
	 * @return
	 * @throws DataException
	 */
	public SubqueryDefinition createSubqueryDefinition(String name, ISubqueryDefinition srcSubQueryDefn)
			throws DataException;

	/**
	 * Get all accessible bindings from a query definition. If <code>qd</code> is a
	 * sub query definition, returns bindings defined onto itself and all
	 * not-aggregation bindings from its ancestors. If <code>qd</code> is not a sub
	 * query definition, just returns its bindings defined onto itself
	 * 
	 * @param qd
	 * @return
	 * @throws DataException
	 */
	Map<String, IBinding> getAccessibleBindings(IBaseQueryDefinition qd) throws DataException;

	/**
	 * Notice: the name of SubqueryDefinition is not compared
	 * 
	 * @param q1
	 * @param q2
	 * @return
	 * @throws DataException
	 */
	public boolean isEqualQuery(IBaseQueryDefinition q1, IBaseQueryDefinition q2) throws DataException;
}
