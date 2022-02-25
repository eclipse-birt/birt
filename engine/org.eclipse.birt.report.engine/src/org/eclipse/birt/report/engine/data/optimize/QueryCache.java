/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.optimize;

import java.util.HashMap;

public class QueryCache {

	public QueryCache() {
		query2state = new HashMap<>();
		cachedQueries = new HashMap();
	}

	private HashMap<Object, QueryState> query2state;
	private HashMap cachedQueries;

	public boolean needExecute(Object query, Object owner, boolean cached) {
		if (owner == null) {
			return true;
		}
		QueryState state = query2state.get(query);
		if (state == null) {
			state = new QueryState();
			query2state.put(query, state);
		}
		if (state.count() == 0) {
			state.addOwner(owner);
			state.setCached(cached);
			return true;
		}
		if (state.isOwnerAdded(owner)) {
			if (state.cached()) {
				if (cached) {
					state.setCached(cached);
					state.resetOwner(owner);
					return false;
				}
			}
			state.setCached(cached);
			state.resetOwner(owner);
			return true;
		}
		if (state.cached()) {
			if (!cached) {
				state.setCached(false);
				state.addOwner(owner);
				return false;
			}
		}
		state.addOwner(owner);
		return false;
	}

	public void putCachedQuery(Object query, Object rsid) {
		cachedQueries.put(query, rsid);
	}

	public Object getCachedQuery(Object query) {
		return cachedQueries.get(query);
	}

	public void close() {
		query2state.clear();
		cachedQueries.clear();
	}

	public boolean getState(Object query) {
		QueryState state = query2state.get(query);
		return state.cached();
	}
}
