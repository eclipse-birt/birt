
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
package org.eclipse.birt.data.engine.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class SubqueryDefinitionCopyUtil {
	/**
	 * 
	 * @param name
	 * @param srcSubQueryDefn
	 * @return
	 * @throws DataException
	 */
	public static SubqueryDefinition createSubqueryDefinition(String name, ISubqueryDefinition srcSubQueryDefn)
			throws DataException {
		SubqueryDefinition destSubQueryDefn = new SubqueryDefinition(name, srcSubQueryDefn.getParentQuery());
		destSubQueryDefn.setApplyOnGroupFlag(srcSubQueryDefn.applyOnGroup());
		destSubQueryDefn.setMaxRows(srcSubQueryDefn.getMaxRows());
		destSubQueryDefn.setUsesDetails(srcSubQueryDefn.usesDetails());
		copyGroupList(srcSubQueryDefn, destSubQueryDefn);
		copyExpressions(srcSubQueryDefn, destSubQueryDefn);
		copySubQueryList(srcSubQueryDefn, destSubQueryDefn);
		copySortList(srcSubQueryDefn, destSubQueryDefn);
		copyFilterList(srcSubQueryDefn, destSubQueryDefn);
		return destSubQueryDefn;
	}

	/**
	 * 
	 * @param srcSubQueryDefn
	 * @param destSubQueryDefn
	 * @throws DataException
	 */
	private static void copyExpressions(ISubqueryDefinition srcSubQueryDefn, SubqueryDefinition destSubQueryDefn)
			throws DataException {
		Map bindings = srcSubQueryDefn.getBindings();
		Iterator it = bindings.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry o = (Map.Entry) it.next();
			destSubQueryDefn.addBinding(convertToBindings(o.getValue()));
		}
	}

	/**
	 * 
	 * @param bindings
	 * @param o
	 * @return
	 */
	private static IBinding convertToBindings(Object binding) {
		if (binding instanceof IBinding) {
			return (IBinding) binding;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param srcSubQueryDefn
	 * @param destSubQueryDefn
	 */
	private static void copyGroupList(ISubqueryDefinition srcSubQueryDefn, SubqueryDefinition destSubQueryDefn) {
		List groupList = srcSubQueryDefn.getGroups();
		for (int i = 0; i < groupList.size(); i++) {
			destSubQueryDefn.addGroup((GroupDefinition) groupList.get(i));
		}
	}

	/**
	 * 
	 * @param srcSubQueryDefn
	 * @param destSubQueryDefn
	 */
	private static void copySubQueryList(ISubqueryDefinition srcSubQueryDefn, SubqueryDefinition destSubQueryDefn) {
		Object[] subQueryDefn = srcSubQueryDefn.getSubqueries().toArray();
		for (int i = 0; i < subQueryDefn.length; i++) {
			destSubQueryDefn.addSubquery((SubqueryDefinition) subQueryDefn[i]);
		}
	}

	/**
	 * 
	 * @param srcSubQueryDefn
	 * @param destSubQueryDefn
	 */
	private static void copySortList(ISubqueryDefinition srcSubQueryDefn, SubqueryDefinition destSubQueryDefn) {
		List sortList = srcSubQueryDefn.getSorts();
		for (int i = 0; i < sortList.size(); i++) {
			destSubQueryDefn.addSort((SortDefinition) sortList.get(i));
		}
	}

	/**
	 * 
	 * @param srcSubQueryDefn
	 * @param destSubQueryDefn
	 */
	private static void copyFilterList(ISubqueryDefinition srcSubQueryDefn, SubqueryDefinition destSubQueryDefn) {
		List filterList = srcSubQueryDefn.getFilters();
		for (int i = 0; i < filterList.size(); i++) {
			destSubQueryDefn.addFilter((IFilterDefinition) filterList.get(i));
		}
	}
}
