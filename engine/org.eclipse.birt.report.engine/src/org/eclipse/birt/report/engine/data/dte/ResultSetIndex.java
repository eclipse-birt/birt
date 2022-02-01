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

package org.eclipse.birt.report.engine.data.dte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ResultSetIndex {

	public ResultSetIndex() {
	}

	private static class QueryResultSets {

		Map<String, ResultSets> results = new HashMap<String, ResultSets>();

		void addResultSet(String parent, String rawId, String rset) {
			ResultSets rsets = results.get(parent);
			if (rsets == null) {
				rsets = new ResultSets();
				results.put(parent, rsets);
			}
			rsets.addResultSet(rawId, rset);
		}

		String getResultSet(String parent, String row) {
			ResultSets rsets = getResultSets(parent);
			return rsets == null ? null : rsets.getResultSet(row);
		}

		String[] getResultSetWithRawId(String parent, String row) {
			ResultSets rsets = getResultSets(parent);
			return rsets == null ? null : rsets.getResultSetWithRawId(row);
		}

		ResultSets getResultSets(String parent) {
			ResultSets rsets = results.get(parent);
			if (rsets == null) {
				return null;
			}
			return rsets;
		}
	}

	private static class ResultSets {

		static Comparator<ResultSetEntry> comparator = new Comparator<ResultSetEntry>() {

			public int compare(ResultSetEntry e1, ResultSetEntry e2) {
				if (e1.row == e2.row) {
					return 0;
				}
				if (e1.row < e2.row) {
					return -1;
				}
				return 1;
			}
		};
		ResultSetEntry[] entries;
		Collection<ResultSetEntry> rsets = new ArrayList<ResultSetEntry>();
		Map<String, String> stringIdResets = new HashMap<String, String>();

		void addResultSet(String rawId, String rset) {
			try {
				int intRawId = Integer.parseInt(rawId);
				addWithIntId(intRawId, rset);
			} catch (NumberFormatException ex) {
				addWithStringId(rawId, rset);
			}
		}

		String getResultSet(String rawId) {
			try {
				int intRawId = Integer.parseInt(rawId);
				return getIntRowId(intRawId);
			} catch (NumberFormatException ex) {
				return getResultSetWithStringRowId((String) rawId);
			}
		}

		String[] getResultSetWithRawId(String rawId) {
			try {
				int intRawId = Integer.parseInt(rawId);
				return getResultSetWithRawId(intRawId);
			} catch (NumberFormatException ex) {
				return new String[] { getResultSetWithStringRowId((String) rawId), null };
			}
		}

		private void addWithIntId(int rowId, String rset) {
			if (entries != null) {
				throw new IllegalStateException();
			}
			rsets.add(new ResultSetEntry(rowId, rset));
		}

		private void addWithStringId(String rowId, String rset) {
			stringIdResets.put(rowId, rset);

		}

		private String getResultSetWithStringRowId(String rowId) {
			return stringIdResets.get(rowId);
		}

		private String getIntRowId(int rowId) {
			ResultSetEntry entry = getResultSetEntry(rowId);
			return entry == null ? null : entry.rset;
		}

		private String[] getResultSetWithRawId(int rawId) {
			ResultSetEntry entry = getResultSetEntry(rawId);
			return entry == null ? null : new String[] { entry.rset, String.valueOf(entry.row) };
		}

		private ResultSetEntry getResultSetEntry(int rawId) {
			if (entries == null) {
				entries = rsets.toArray(new ResultSetEntry[rsets.size()]);
				Arrays.sort(entries, comparator);
			}
			ResultSetEntry entry = null;
			int index = Arrays.binarySearch(entries, new ResultSetEntry(rawId, ""), comparator);
			if (index < 0) {
				index = -(index + 1) - 1;
			}
			if (index < 0) {
				index = 0;
			}
			if (index >= 0 && index < entries.length) {
				entry = entries[index];
			}
			return entry;
		}
	}

	private static class ResultSetEntry {

		int row;
		String rset;

		ResultSetEntry(int row, String rset) {
			this.row = row;
			this.rset = rset;
		}
	}

	private Map<String, QueryResultSets> queries = new HashMap<String, QueryResultSets>();

	public void addResultSet(String query, String parent, String rawId, String rset) {
		QueryResultSets rsets = queries.get(query);
		if (rsets == null) {
			rsets = new QueryResultSets();
			queries.put(query, rsets);
		}
		rsets.addResultSet(parent, rawId, rset);
	}

	public String getResultSet(String query, String parent, String rawId) {
		QueryResultSets rsets = queries.get(query);
		if (rsets != null) {
			String rset = rsets.getResultSet(parent, rawId);
			if (rset == null) {
				if (parent != null) {
					int charAt = parent.indexOf("_");
					if (charAt != -1) {
						String root = parent.substring(0, charAt);
						return rsets.getResultSet(root, rawId);
					}
				}
			}
			return rset;
		}
		return null;
	}

	public String[] getResultSetWithRawId(String query, String parent, String raw) {
		QueryResultSets rsets = queries.get(query);
		if (rsets != null) {
			String[] rset = rsets.getResultSetWithRawId(parent, raw);
			if (rset == null) {
				if (parent != null) {
					int charAt = parent.indexOf("_");
					if (charAt != -1) {
						String root = parent.substring(0, charAt);
						return rsets.getResultSetWithRawId(root, raw);
					}
				}
			}
			return rset;
		}
		return null;
	}
}
