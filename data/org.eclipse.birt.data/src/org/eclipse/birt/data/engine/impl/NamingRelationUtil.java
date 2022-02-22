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

package org.eclipse.birt.data.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.impl.document.NamingRelation;

/**
 *
 */

public class NamingRelationUtil {

	/**
	 *
	 * @param session
	 * @param queryDefn
	 * @param queryResults
	 */
	public static void merge(DataEngineSession session, IBaseQueryDefinition queryDefn, IQueryResults queryResults) {
		if (session == null || queryResults.getPreparedQuery() == null
				|| queryResults.getPreparedQuery().getReportQueryDefn() == null
				|| queryResults.getPreparedQuery().getReportQueryDefn().getBindings().size() == 0
				|| queryDefn instanceof ISubqueryDefinition) {
			return;
		}
		String elementId = queryDefn.getName();
		String bookmark = queryResults.getName();
		String rsId = queryResults.getID();

		NamingRelation relation = session.getNamingRelation();
		if (relation == null) {
			relation = new NamingRelation();
			session.setNamingRelation(relation);
		}
		Map bookmarkMap = relation.getBookmarkMap();
		Map elementIdMap = relation.getElementIdMap();
		if (bookmark != null) {
			if (bookmarkMap.get(bookmark) == null) {
				bookmarkMap.put(bookmark, rsId);
			} else {
				Object value = bookmarkMap.get(bookmark);
				if (value instanceof String) {
					Map subMap = new HashMap();
					subMap.put(getSubKey(subMap), value);
					bookmarkMap.put(bookmark, subMap);
				} else if (value instanceof Map) {
					Map subMap = (Map) value;
					subMap.put(getSubKey(subMap), rsId);
				}
			}
		}
		if (elementId != null) {
			if (elementIdMap.get(elementId) == null) {
				elementIdMap.put(elementId, rsId);
			} else {
				Object value = elementIdMap.get(elementId);
				if (value instanceof String) {
					Map subMap = new HashMap();
					subMap.put(getSubKey(subMap), value);
					elementIdMap.put(elementId, subMap);
				} else if (value instanceof Map) {
					Map subMap = (Map) value;
					subMap.put(getSubKey(subMap), rsId);
				}
			}
		}

	}

	/**
	 *
	 * @param subMap
	 * @return
	 */
	private static String getSubKey(Map subMap) {
		return String.valueOf(subMap.size() + 1);
	}

}
