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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.junit.Test;
import static org.junit.Assert.*;


public class GroupByRowKeyCountTest {
	private String[] sortedRowKeys = new String[] {
			null,
			null,
			"",
			"",
			"",
			"",
			"A",
			"A",
			"A",
			"B",
			"C",
			"C",
			"D",
			"D",
			"D",
			"D",
			"E",
			"F",
			"F",
			"F",
			"G",
			"G",
			"G",
			"H",
			"I",
			"J",
			"K",
			"K",
			"L"
		};
	
	private GroupByRowKeyCount[] groupBys = new GroupByRowKeyCount[] {
			new GroupByRowKeyCount(1),
			new GroupByRowKeyCount(2),
			new GroupByRowKeyCount(3),
			new GroupByRowKeyCount(4),
			new GroupByRowKeyCount(5),
			new GroupByRowKeyCount(6),
			new GroupByRowKeyCount(7),
			new GroupByRowKeyCount(8),
			new GroupByRowKeyCount(9),
			new GroupByRowKeyCount(10),
			new GroupByRowKeyCount(11),
			new GroupByRowKeyCount(12),
			new GroupByRowKeyCount(13),
			new GroupByRowKeyCount(14),
			new GroupByRowKeyCount(15),
		};
	
	private String[][] correctGroupKeyResults = new String[][] {
			new String[] {null, "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"},
			new String[] {null, "A", "C", "E", "G", "I", "K"},   
			new String[] {null, "B", "E", "H", "K"}, 
			new String[] {null, "C", "G", "K"}, 
			new String[] {null, "D", "I"}, 
			new String[] {null, "E", "K"}, 
			new String[] {null, "F"},
			new String[] {null, "G"},
			new String[] {null, "H"},
			new String[] {null, "I"},
			new String[] {null, "J"},
			new String[] {null, "K"},
			new String[] {null, "L"},
			new String[] {null},
			new String[] {null},
		};
	@Test
    public void testGroupByRowKeyCount()
	{
		for (int i = 0; i < groupBys.length; i++)
		{
			List groupKeys = new ArrayList();
			groupKeys.add( sortedRowKeys[0] );
			String prevRowKey = sortedRowKeys[0];
			for (int j = 1; j < sortedRowKeys.length; j++) 
			{
				String currentRowKey = sortedRowKeys[j];
				if (!groupBys[i].isInSameGroup( currentRowKey, prevRowKey )) {
					groupKeys.add( currentRowKey );
				}
				prevRowKey = currentRowKey;
			}
			String[] groupKeyArray = new String[groupKeys.size()];
			groupKeys.toArray( groupKeyArray );
			assertTrue( Arrays.equals( correctGroupKeyResults[i], groupKeyArray ));
		}
	}
}
