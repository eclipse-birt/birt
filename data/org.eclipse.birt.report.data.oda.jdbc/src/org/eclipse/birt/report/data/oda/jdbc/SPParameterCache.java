/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: See git history
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps the parameters of stored procedures that {@link CallStatement} looked
 * up through {@link java.sql.DatabaseMetaData#getProcedureColumns}. One
 * instance belongs to one ODA {@link Connection} and is cleared when that
 * connection is opened or closed, so a report looks a procedure up only once.
 * Callers hand in and get back copies of the lists.
 */
class SPParameterCache {

	private final Map<List<String>, List<ParameterDefn>> cache = new ConcurrentHashMap<>();

	/**
	 * @param key everything the lookup depends on
	 * @return a copy of the cached parameters, or null if nothing is cached
	 */
	List<ParameterDefn> get(List<String> key) {
		List<ParameterDefn> params = cache.get(key);
		return params == null ? null : new ArrayList<>(params);
	}

	/**
	 * Keeps a copy of the parameters. An empty list is ignored, so a procedure
	 * that is deployed later is still found.
	 */
	void put(List<String> key, List<ParameterDefn> params) {
		if (!params.isEmpty()) {
			cache.put(key, new ArrayList<>(params));
		}
	}

	void clear() {
		cache.clear();
	}
}
