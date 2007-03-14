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

/**
 * 
 */

public class SelectionFactory
{

	/**
	 * 
	 * @param selectedObjects
	 * @return
	 */
	public static ISelection createObjectsSelection( Object[][] selectedObjects )
	{
		return new ObjectsSelection( selectedObjects );
	}

	/**
	 * 
	 * @param minKey
	 * @param maxKey
	 * @param containsMinKey
	 * @param containsMaxKey
	 * @return
	 */
	public static ISelection createRangeSelection( Object[] minKey, Object[] maxKey,
			boolean containsMinKey, boolean containsMaxKey )
	{
		return new RangeSelection( minKey,
				maxKey,
				containsMinKey,
				containsMaxKey );
	}

}
