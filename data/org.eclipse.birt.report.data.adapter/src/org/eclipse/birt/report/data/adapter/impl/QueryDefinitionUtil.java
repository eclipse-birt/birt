
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.QueryCompUtil;
import org.eclipse.birt.data.engine.impl.SubqueryDefinitionCopyUtil;
import org.eclipse.birt.report.data.adapter.api.IQueryDefinitionUtil;

/**
 * 
 */

public class QueryDefinitionUtil implements IQueryDefinitionUtil {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.adapter.api.IQueryDefinitionUtil#
	 * createSubqueryDefinition(java.lang.String,
	 * org.eclipse.birt.data.engine.api.ISubqueryDefinition)
	 */
	public SubqueryDefinition createSubqueryDefinition(String name, ISubqueryDefinition srcSubQueryDefn)
			throws DataException {
		return SubqueryDefinitionCopyUtil.createSubqueryDefinition(name, srcSubQueryDefn);
	}

	public Map<String, IBinding> getAccessibleBindings(IBaseQueryDefinition qd) throws DataException {
		return org.eclipse.birt.data.engine.impl.QueryDefinitionUtil.getAccessibleBindings(qd);
	}

	public boolean isEqualQuery(IBaseQueryDefinition q1, IBaseQueryDefinition q2) throws DataException {
		if (q1 == q2) {
			return true;
		}
		if (q1 == null && q2 != null) {
			return false;
		}
		if (q1 != null && q2 == null) {
			return false;
		}
		if (!q1.getClass().equals(q2.getClass())) {
			return false;
		}
		return QueryCompUtil.isQueryDefnEqual(-1, q1, q2, false, false);
	}

}
