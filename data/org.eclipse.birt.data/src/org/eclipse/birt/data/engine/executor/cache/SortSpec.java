/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache;

/**
 * A simple sort specification store class, which is used to generate
 * corresponding comparator.
 */
public class SortSpec
{
	int[] sortKeyIndexes;
	String[] sortKeyColumns;
	boolean[] sortAscending;

	/**
	 * @param sortKeyIndexes
	 * @param sortKeyColumns
	 * @param sortAscending
	 */
	public SortSpec( int[] sortKeyIndexes, String[] sortKeyColumns,
			boolean[] sortAscending )
	{
		this.sortKeyIndexes = sortKeyIndexes;
		this.sortKeyColumns = sortKeyColumns;
		this.sortAscending = sortAscending;
	}
	
	/**
	 * @return
	 */
	public int length( )
	{
		if ( sortAscending != null )
			return sortAscending.length;
		else
			return 0;
	}
	
}
