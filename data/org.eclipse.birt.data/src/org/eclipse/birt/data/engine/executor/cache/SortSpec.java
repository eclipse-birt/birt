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

import java.util.Comparator;

import org.eclipse.birt.data.engine.expression.CompareHints;

/**
 * A simple sort specification store class, which is used to generate
 * corresponding comparator.
 */
public class SortSpec
{
	private int[] sortKeyIndexes;
	private String[] sortKeyColumns;
	private boolean[] sortAscending;
	private CompareHints[] comparator;
	/**
	 * @param sortKeyIndexes
	 * @param sortKeyColumns
	 * @param sortAscending
	 */
	public SortSpec( int[] sortKeyIndexes, String[] sortKeyColumns,
			boolean[] sortAscending, CompareHints[] comparator )
	{
		this.setSortKeyIndexes( sortKeyIndexes );
		this.setSortKeyColumns( sortKeyColumns );
		this.setSortAscending( sortAscending );
		this.setComparator( comparator );
	}
	
	/**
	 * @return
	 */
	public int length( )
	{
		if ( getSortAscending() != null )
			return getSortAscending().length;
		else
			return 0;
	}

	void setComparator( CompareHints[] comparator )
	{
		this.comparator = comparator;
	}

	CompareHints[] getComparator( )
	{
		return comparator;
	}

	void setSortAscending( boolean[] sortAscending )
	{
		this.sortAscending = sortAscending;
	}

	boolean[] getSortAscending( )
	{
		return sortAscending;
	}

	void setSortKeyColumns( String[] sortKeyColumns )
	{
		this.sortKeyColumns = sortKeyColumns;
	}

	String[] getSortKeyColumns( )
	{
		return sortKeyColumns;
	}

	void setSortKeyIndexes( int[] sortKeyIndexes )
	{
		this.sortKeyIndexes = sortKeyIndexes;
	}

	int[] getSortKeyIndexes( )
	{
		return sortKeyIndexes;
	}
	
}
