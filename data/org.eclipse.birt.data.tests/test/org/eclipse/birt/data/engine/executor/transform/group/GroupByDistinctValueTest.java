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


import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.group.GroupBy;
import org.eclipse.birt.data.engine.odi.IQuery;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for GroupByDistinctValue
 */

public class GroupByDistinctValueTest {
	protected GroupDefinition groupDefn;
	protected GroupBy groupBy;
	
	/*
	 * @see TestCase#setUp()
	 */
	@Before
    public void groupByDistinctValueSetUp() throws Exception
	{
		groupDefn = new GroupDefinition();
		groupDefn.setInterval(IGroupDefinition.NO_INTERVAL);
		groupDefn.setIntervalRange(0);
		
		groupBy = this.getInstance( groupDefn );

	}

	/**
	 * Test GroupByDistinctValue#isInSameGroup
	 * @throws DataException
	 *
	 */
	@Test
    public void testIsInSameGroup( ) throws DataException
	{
		Object currentGroupKey;
		Object previousGroupKey;
		
		currentGroupKey = null;
		previousGroupKey = null;
		assertTrue(groupBy.isInSameGroup(currentGroupKey, previousGroupKey));
		
		currentGroupKey = "test";
		previousGroupKey = null;
		assertFalse(groupBy.isInSameGroup(currentGroupKey, previousGroupKey));
		
		currentGroupKey = "test";
		previousGroupKey = "test";
		assertTrue(groupBy.isInSameGroup(currentGroupKey, previousGroupKey));
		
		currentGroupKey = "test";
		previousGroupKey = "test2";
		assertFalse(groupBy.isInSameGroup(currentGroupKey, previousGroupKey));
		
		currentGroupKey = new Integer("1");
		previousGroupKey = new Integer("1");
		assertTrue(groupBy.isInSameGroup(currentGroupKey, previousGroupKey));
		
		currentGroupKey = new Integer("1");
		previousGroupKey = new Integer("2");
		assertFalse(groupBy.isInSameGroup(currentGroupKey, previousGroupKey));
		
		currentGroupKey = new Integer("1");
		previousGroupKey = new Double("1");
		assertFalse(groupBy.isInSameGroup(currentGroupKey, previousGroupKey));
		
	}
	
	/**
	 * 
	 * @param groupDefn
	 * @return
	 * @throws DataException
	 */
	protected GroupBy getInstance( GroupDefinition groupDefn ) throws DataException
	{
		String groupKey = groupDefn.getKeyColumn();
		IQuery.GroupSpec groupSpec = new IQuery.GroupSpec( groupKey );
		groupSpec.setName( groupDefn.getName() );
		groupSpec.setInterval( groupDefn.getInterval());
		groupSpec.setIntervalRange( groupDefn.getIntervalRange());
		groupSpec.setIntervalStart( groupDefn.getIntervalStart());
		groupSpec.setSortDirection( groupDefn.getSortDirection());
		
		return GroupBy.newInstance( groupSpec, 0, null, null );
	}	

}
