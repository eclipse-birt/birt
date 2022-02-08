/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.querymodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Reference graph constructed from a PojoQuery
 */
public class ReferenceGraph {
	private ReferenceNode[] roots;

	private ColumnReferenceNode[] columnReferences;

	private Map<String, Integer> columnNameIndexMap; // index is 1-based

	public ReferenceGraph(ReferenceNode[] roots) {
		assert roots != null;
		this.roots = roots;
	}

	public int getColumnCount() throws OdaException {
		return getColumnReferences().length;
	}

	/**
	 * @param index: column index; 1-based
	 * @return
	 */
	public Column getColumn(int index) throws OdaException {
		return getColumnReferences()[index - 1].getColumn();
	}

	/**
	 * @return the roots
	 */
	public ReferenceNode[] getRoots() {
		return roots;
	}

	private Map<String, Integer> getColumnNameIndexMap() throws OdaException {
		if (columnNameIndexMap == null) {
			columnNameIndexMap = new HashMap<String, Integer>();
			int i = 1; // index is 1-based
			for (ColumnReferenceNode crn : getColumnReferences()) {
				String name = crn.getColumn().getName();
				if (columnNameIndexMap.containsKey(name)) {
					throw new OdaException(Messages.getString("ReferenceGraph.DuplicateColumnName", name)); //$NON-NLS-1$
				}
				columnNameIndexMap.put(name, i++);
			}
		}
		return columnNameIndexMap;
	}

	/**
	 * 
	 * @param columnName
	 * @return the index of this column: 1-based
	 * @throws OdaException
	 */
	public int findColumn(String columnName) throws OdaException {
		Integer index = getColumnNameIndexMap().get(columnName);
		if (index != null) {
			return index.intValue();
		} else {
			throw new OdaException(Messages.getString("ReferenceGraph.InexistentColumnName", columnName)); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param index: column index; 1-based
	 * @return
	 */
	public ColumnReferenceNode getColumnReferenceNode(int index) throws OdaException {
		return getColumnReferences()[index - 1];
	}

	/**
	 * @return the columnReferences
	 */
	public ColumnReferenceNode[] getColumnReferences() throws OdaException {
		if (columnReferences == null) {
			List<ColumnReferenceNode> result = new ArrayList<ColumnReferenceNode>();
			for (ReferenceNode rn : roots) {
				result.addAll(Arrays.asList(rn.getColumnReferenceNodes()));
			}

			// check duplicate index
			Set<Integer> indexes = new HashSet<Integer>();
			for (ColumnReferenceNode crn : result) {
				if (indexes.contains(crn.getColumn().getIndex())) {
					throw new OdaException(
							Messages.getString("ReferenceGraph.DuplicateColumnIndex", crn.getColumn().getIndex())); //$NON-NLS-1$
				}
				indexes.add(crn.getColumn().getIndex());
			}

			columnReferences = result.toArray(new ColumnReferenceNode[0]);
			Arrays.sort(columnReferences);
		}
		return columnReferences;
	}

	public static ReferenceGraph create(PojoQuery query) {
		List<ReferenceNode> roots = new ArrayList<ReferenceNode>();
		for (IColumnsMapping mapping : query.getColumnsMappings()) {
			roots.add(mapping.createReferenceNode(null));
		}
		return new ReferenceGraph(roots.toArray(new ReferenceNode[0]));
	}

}
