/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import java.util.ArrayList;

import org.eclipse.birt.chart.model.component.Series;

/**
 * StackGroup
 */
public class StackGroup
{

	ArrayList alSeries = new ArrayList( );

	ArrayList alUnitPositions = null;

	final int iSharedUnitIndex;

	int iSharedUnitCount = 1;

	/**
	 * The constructor.
	 */
	StackGroup( int iSharedUnitIndex )
	{
		this.iSharedUnitIndex = iSharedUnitIndex;
	}

	/**
	 * 
	 * @param iSharedUnitCount
	 */
	final void updateCount( int iSharedUnitCount )
	{
		this.iSharedUnitCount = iSharedUnitCount;
	}

	/**
	 * 
	 * @param se
	 */
	final void addSeries( Series se )
	{
		alSeries.add( se );
	}

	/**
	 * 
	 * @return
	 */
	final ArrayList getSeries( )
	{
		return alSeries;
	}

	/**
	 * 
	 * @return
	 */
	public final int getSharedIndex( )
	{
		return iSharedUnitIndex;
	}

	/**
	 * 
	 * @return
	 */
	public final int getSharedCount( )
	{
		return iSharedUnitCount;
	}
}
