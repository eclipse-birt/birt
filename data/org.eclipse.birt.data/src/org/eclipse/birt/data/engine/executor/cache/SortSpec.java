/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.cache;

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.expression.CompareHints;

/**
 * A simple sort specification store class, which is used to generate
 * corresponding comparator.
 */
public class SortSpec {
	private int[] sortKeyIndexes;
	private String[] sortKeyColumns;
	private int[] sortAscending;
	private CompareHints[] comparator;

	public static final int SORT_DISABLE = IGroupDefinition.NO_SORT;
	public static final int SORT_ASC = IGroupDefinition.SORT_ASC;
	public static final int SORT_DESC = IGroupDefinition.SORT_DESC;

	/**
	 * @param sortKeyIndexes
	 * @param sortKeyColumns
	 * @param sortAscending
	 */
	public SortSpec(int[] sortKeyIndexes, String[] sortKeyColumns, int[] sortAscending, CompareHints[] comparator) {
		this.setSortKeyIndexes(sortKeyIndexes);
		this.setSortKeyColumns(sortKeyColumns);
		this.setSortAscending(sortAscending);
		this.setComparator(comparator);
	}

	/**
	 * @return
	 */
	public int length() {
		if (getSortAscending() != null) {
			return getSortAscending().length;
		} else {
			return 0;
		}
	}

	void setComparator(CompareHints[] comparator) {
		this.comparator = comparator;
	}

	CompareHints[] getComparator() {
		return comparator;
	}

	void setSortAscending(int[] sortAscending) {
		this.sortAscending = sortAscending;
	}

	int[] getSortAscending() {
		return sortAscending;
	}

	void setSortKeyColumns(String[] sortKeyColumns) {
		this.sortKeyColumns = sortKeyColumns;
	}

	String[] getSortKeyColumns() {
		return sortKeyColumns;
	}

	void setSortKeyIndexes(int[] sortKeyIndexes) {
		this.sortKeyIndexes = sortKeyIndexes;
	}

	int[] getSortKeyIndexes() {
		return sortKeyIndexes;
	}

}
