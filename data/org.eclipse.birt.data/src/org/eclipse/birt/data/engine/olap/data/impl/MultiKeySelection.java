
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

public class MultiKeySelection implements ISelection
{
	private Object[][] keys;
	private Object[] minKey = null;
	private Object[] maxKey = null;
	
	/**
	 * 
	 * @param keys
	 */
	public MultiKeySelection( Object[][] keys )
	{
		assert keys != null && keys.length > 0;
		minKey = keys[0];
		maxKey = keys[0];
		for ( int i = 1; i < keys.length; i++ )
		{
			if ( CompareUtil.compare( keys[i], minKey ) < 0 )
			{
				minKey = keys[i];
			}
			if ( CompareUtil.compare( keys[i], maxKey ) > 0 )
			{
				maxKey = keys[i];
			}
		}
		this.keys = keys;
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ISelection#isSelected(java.lang.Object[])
	 */
	public boolean isSelected( Object[] key )
	{
		for( int i=0;i<keys.length;i++)
		{
			if( CompareUtil.compare( keys[i], key ) == 0 )
			{
				return true;
			}
		}
		return false;
	}

}
