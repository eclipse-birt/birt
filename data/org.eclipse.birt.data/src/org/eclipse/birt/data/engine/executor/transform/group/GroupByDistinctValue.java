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

package org.eclipse.birt.data.engine.executor.transform.group;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * Distinction implementation of GroupBy.
 */

class GroupByDistinctValue extends GroupBy
{

	/*
	 * @see org.eclipse.birt.data.engine.api.GroupExtraDefn#isWithinSameGroup(java.lang.Object,
	 *      java.lang.Object)
	 */
	boolean isSameGroup( Object currentGroupKey, Object previousGroupKey )
			throws DataException
	{
		assert ( currentGroupKey != null );
		assert ( previousGroupKey != null );

		return currentGroupKey.equals( previousGroupKey );
	}

}