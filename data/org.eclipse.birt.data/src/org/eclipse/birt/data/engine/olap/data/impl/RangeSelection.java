
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl;

import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;

/**
 * 
 */

public class RangeSelection implements ISelection
{
	private Object[] minKey;
	private Object[] maxKey;
	private boolean containsMinKey;
	private boolean containsMaxKey;
	
	/**
	 * 
	 * @param minKey
	 * @param maxKey
	 * @param containsMinKey
	 * @param containsMaxKey
	 */
	public RangeSelection( Object[] minKey, Object[] maxKey, boolean containsMinKey,
			boolean containsMaxKey )
	{
		this.minKey = minKey;
		this.maxKey = maxKey;
		this.containsMinKey = containsMinKey;
		this.containsMaxKey = containsMaxKey;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ISelection#isSelected(java.lang.Object[])
	 */
	public boolean isSelected( Object[] key )
	{
		if ( minKey != null )
		{
			if ( containsMinKey )
			{
				if ( CompareUtil.compare( key, minKey ) < 0 )
				{
					return false;
				}
			}
			else
			{
				if ( CompareUtil.compare( key, minKey ) <= 0 )
				{
					return false;
				}
			}
		}
		if ( maxKey != null )
		{
			if ( containsMaxKey )
			{
				if ( CompareUtil.compare( key, maxKey ) > 0 )
				{
					return false;
				}
			}
			else
			{
				if ( CompareUtil.compare( key, maxKey ) >= 0 )
				{
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ISelection#getMax()
	 */
	public Object[] getMax( )
	{
		return maxKey;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ISelection#getMin()
	 */
	public Object[] getMin( )
	{
		return minKey;
	}

}
