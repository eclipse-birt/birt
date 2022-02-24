/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.group.GroupInfo;

import org.junit.Test;
import static org.junit.Assert.*;

public class GroupInfoUtilTest {
	static final int SOURCE = 1;
	static final int TARGET = 2;

	/*
	 * groups[level] is an ArrayList of GroupInfo objects at the specified level.
	 * Level is a 0-based group index, with 0 denoting the outermost group, etc.
	 * Example: Row GroupKey1 GroupKey2 GroupKey3 Column4 Column5 0: CHINA BEIJING
	 * 2003 Cola $100 1: CHINA BEIJING 2003 Pizza $320 2: CHINA BEIJING 2004 Cola
	 * $402 3: CHINA SHANGHAI 2003 Cola $553 4: CHINA SHANGHAI 2003 Pizza $223 5:
	 * CHINA SHANGHAI 2004 Cola $226 6: USA CHICAGO 2004 Pizza $133 7: USA NEW YORK
	 * 2004 Cola $339 8: USA NEW YORK 2004 Cola $297
	 * 
	 * groups: (parent, child) LEVEL 0 LEVEL 1 LEVEL 2
	 * ============================================ 0: -,0 0,0 0,0 1: -,2 0,2 0,2 2:
	 * 1,4 1,3 3: 1,5 1,5 4: 2,6 5: 3,7
	 */

	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.document.GroupInfoUtil.getGroup(List[])'
	 */
	@Test
	public void testGetGroup() {
		List[] source = getTest1(SOURCE);
		List[] target = getTest1(TARGET);
		assertTrue(twoGroupListArrayEqual(source, target));
		List[] result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest2(SOURCE);
		target = getTest2(TARGET);
		assertFalse(twoGroupListArrayEqual(source, target));
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest3(SOURCE);
		target = getTest3(TARGET);
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest4(SOURCE);
		target = getTest4(TARGET);
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest5(SOURCE);
		target = getTest5(TARGET);
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest6(SOURCE);
		target = getTest6(TARGET);
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest7(SOURCE);
		target = getTest7(TARGET);
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest8(SOURCE);
		target = getTest8(TARGET);
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));

		source = getTest9(SOURCE);
		target = getTest9(TARGET);
		result = GroupInfoUtil.cleanUnUsedGroupInstance(source);
		assertTrue(twoGroupListArrayEqual(result, target));
	}

	/**
	 * Without remove any group instance.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest0(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 ================ 0: -,0 1: -,4
			 */

			List[] lists = new List[1];
			List level0 = new ArrayList();
			lists[0] = level0;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 4));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 ================ 0: -,0 1: -,2
			 */

			List[] lists = new List[1];
			List level0 = new ArrayList();
			lists[0] = level0;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			return lists;
		}
		return null;
	}

	/**
	 * Without remove any group instance.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest1(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,3 3: 1,5 1,5 4: 2,6 5: 3,7
			 */

			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, 7));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,3 3: 1,5 1,5 4: 2,6 5: 3,7
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, 7));
			return lists;
		}
		return null;
	}

	/**
	 * A leaf should be removed, but its parent group still exist.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest2(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,-2 3: 1,5 1,5 4: 2,6 5: 3,7
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, -2));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, 7));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,3 1,5 3: 1,4 2,6 4: 3,7
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 3));
			level1.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, 7));
			return lists;
		}
		return null;
	}

	/**
	 * A leaf will be removed. Its parent group is also removed.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest3(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,3 3: 1,5 1,5 4: 2,-2 5: 3,7
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, -2));
			level2.add(getGroupInfo(3, 7));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,3 3: 1,5 4: 2,7 5:
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 7));
			return lists;
		}
		return null;
	}

	/**
	 * Without remove any group instance.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest4(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,-2 3: 1,5 1,-2 4: 2,6 5: 3,7
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, -2));
			level2.add(getGroupInfo(1, -2));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, 7));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,1 1,2 0,2 2: 1,3 1,6 3: 2,7
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 1));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(1, 2));
			level1.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 6));
			level2.add(getGroupInfo(2, 7));
			return lists;
		}
		return null;
	}

	/**
	 * All removed.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest5(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,-2 1: -,2 0,2 0,-2 2: 1,4 1,-2 3: 1,5 1,-2 4: 2,-2 5: 3,-2
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, -2));
			level2.add(getGroupInfo(0, -2));
			level2.add(getGroupInfo(1, -2));
			level2.add(getGroupInfo(1, -2));
			level2.add(getGroupInfo(2, -2));
			level2.add(getGroupInfo(3, -2));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,3 3: 1,5 1,5 4: 2,6 5: 3,7
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			return lists;
		}
		return null;
	}

	/**
	 * Partly removed.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest6(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,-2 1: -,2 0,2 0,1 2: 1,4 1,-2 3: 1,5 1,-2 4: 2,-2 5: 3,-2
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, -2));
			level2.add(getGroupInfo(0, 1));
			level2.add(getGroupInfo(1, -2));
			level2.add(getGroupInfo(1, -2));
			level2.add(getGroupInfo(2, -2));
			level2.add(getGroupInfo(3, -2));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,1
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level1.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 1));
			return lists;
		}
		return null;
	}

	/**
	 * Remove exactly last leaf.
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest7(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,3 3: 1,5 1,5 4: 2,6 5: 3,-2
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, -2));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL 0 LEVEL 1 LEVEL 2 ============================================ 0: -,0
			 * 0,0 0,0 1: -,2 0,2 0,2 2: 1,4 1,3 3: 1,5 4: 2,6
			 */
			List[] lists = new List[3];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			return lists;
		}
		return null;
	}

	/**
	 * Complex case
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest8(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL0 LEVEL 1 LEVEL 2 LEVEL 3
			 * ========================================================= 0: -,0 0,0 0,0 0,0
			 * 1: -,2 0,2 0,2 0,2 2: -,3 1,4 1,4 1,3 3: 2,5 1,5 1,5 4: 2,6 2,6 5: 3,8 3,7 6:
			 * 4,-2 7: 4,-2 8: 5,10
			 */
			List[] lists = new List[4];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			List level3 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			lists[3] = level3;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level0.add(getGroupInfo(-1, 3));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(2, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, 8));
			level3.add(getGroupInfo(0, 0));
			level3.add(getGroupInfo(0, 2));
			level3.add(getGroupInfo(1, 3));
			level3.add(getGroupInfo(1, 5));
			level3.add(getGroupInfo(2, 6));
			level3.add(getGroupInfo(3, 7));
			level3.add(getGroupInfo(4, -2));
			level3.add(getGroupInfo(4, -2));
			level3.add(getGroupInfo(5, 10));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL0 LEVEL 1 LEVEL 2 LEVEL 3
			 * ========================================================= 0: -,0 0,0 0,0 0,0
			 * 1: -,2 0,2 0,2 0,2 2: 1,4 1,4 1,3 3: 1,5 1,5 4: 2,6 2,6 5: 3,7 6: 4,10
			 */
			List[] lists = new List[4];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			List level3 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			lists[3] = level3;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level3.add(getGroupInfo(0, 0));
			level3.add(getGroupInfo(0, 2));
			level3.add(getGroupInfo(1, 3));
			level3.add(getGroupInfo(1, 5));
			level3.add(getGroupInfo(2, 6));
			level3.add(getGroupInfo(3, 7));
			level3.add(getGroupInfo(4, 10));
			return lists;
		}
		return null;
	}

	/**
	 * Complex case
	 * 
	 * @param type
	 * @return
	 */
	private List[] getTest9(int type) {
		if (type == SOURCE) {
			/*
			 * LEVEL0 LEVEL 1 LEVEL 2 LEVEL 3
			 * ========================================================= 0: -,0 0,0 0,0 0,0
			 * 1: -,2 0,2 0,2 0,-2 2: -,3 1,4 1,4 1,3 3: 2,5 1,5 1,5 4: 2,6 2,6 5: 3,8 3,7
			 * 6: 4,-2 7: 4,-2 8: 5,10
			 */
			List[] lists = new List[4];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			List level3 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			lists[3] = level3;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level0.add(getGroupInfo(-1, 3));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level1.add(getGroupInfo(2, 5));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 2));
			level2.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(1, 5));
			level2.add(getGroupInfo(2, 6));
			level2.add(getGroupInfo(3, 8));
			level3.add(getGroupInfo(0, 0));
			level3.add(getGroupInfo(0, -2));
			level3.add(getGroupInfo(1, 3));
			level3.add(getGroupInfo(1, 5));
			level3.add(getGroupInfo(2, 6));
			level3.add(getGroupInfo(3, 7));
			level3.add(getGroupInfo(4, -2));
			level3.add(getGroupInfo(4, -2));
			level3.add(getGroupInfo(5, 10));
			return lists;
		}

		if (type == TARGET) {
			/*
			 * LEVEL0 LEVEL 1 LEVEL 2 LEVEL 3
			 * ========================================================= 0: -,0 0,0 0,0 0,0
			 * 1: -,2 0,2 0,1 1,3 2: 1,4 1,3 1,6 3: 1,4 2,6 4: 2,5 3,7 5: 4,10
			 */
			List[] lists = new List[4];
			List level0 = new ArrayList();
			List level1 = new ArrayList();
			List level2 = new ArrayList();
			List level3 = new ArrayList();
			lists[0] = level0;
			lists[1] = level1;
			lists[2] = level2;
			lists[3] = level3;
			level0.add(getGroupInfo(-1, 0));
			level0.add(getGroupInfo(-1, 2));
			level1.add(getGroupInfo(0, 0));
			level1.add(getGroupInfo(0, 2));
			level1.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(0, 0));
			level2.add(getGroupInfo(0, 1));
			level2.add(getGroupInfo(1, 3));
			level2.add(getGroupInfo(1, 4));
			level2.add(getGroupInfo(2, 5));
			level3.add(getGroupInfo(0, 0));
			level3.add(getGroupInfo(1, 3));
			level3.add(getGroupInfo(1, 5));
			level3.add(getGroupInfo(2, 6));
			level3.add(getGroupInfo(3, 7));
			level3.add(getGroupInfo(4, 10));
			return lists;
		}
		return null;
	}

	/**
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	private boolean twoGroupListArrayEqual(List[] g1, List[] g2) {
		if (g1.length != g2.length)
			return false;
		for (int i = 0; i < g1.length; i++) {
			if (!twoListEqual(g1[i], g2[i]))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	private boolean twoListEqual(List g1, List g2) {
		if (g1.size() != g2.size())
			return false;
		for (int i = 0; i < g1.size(); i++) {
			GroupInfo info1 = (GroupInfo) g1.get(i);
			GroupInfo info2 = (GroupInfo) g2.get(i);
			if (info1.parent != info2.parent || info1.firstChild != info2.firstChild)
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	private GroupInfo getGroupInfo(int parent, int child) {
		GroupInfo info = new GroupInfo();
		info.parent = parent;
		info.firstChild = child;
		return info;
	}

	@Test
	public void testInvalidGroupIndex() throws Exception {
		List[] groups = getExampleGroupList();
		int[] indexArray = { 0, 5, 4, 3, 6, 8 };
		try {
			GroupInfoUtil.getGroupInfo(groups, indexArray);
			fail("should not get there");
		} catch (DataException e) {

		}
	}

	/**
	 * test remove one group item , not entire group
	 * 
	 * @throws BirtException
	 */
	@Test
	public void testRemoveOneGroupItem() throws Exception {
		List[] groups = getExampleGroupList();
		int[] indexArray = { 0, 2, 4, 5, 6, 8 };
		List[] returnGroups = GroupInfoUtil.getGroupInfo(groups, indexArray);
		List refactorList = returnGroups[returnGroups.length - 1];

		List expectedList = new ArrayList();
		expectedList.add(getGroupInfo(0, 0));
		expectedList.add(getGroupInfo(0, 1));
		expectedList.add(getGroupInfo(1, 2));
		expectedList.add(getGroupInfo(1, 3));
		expectedList.add(getGroupInfo(2, 4));
		expectedList.add(getGroupInfo(3, 5));

		assertTrue(twoListEqual(expectedList, refactorList));

	}

	@Test
	public void testEntireGroup() throws Exception {
		List[] groups = getExampleGroupList();
		int[] indexArray = { 0, 3, 4, 5, 6, 8 };
		List[] returnGroups = GroupInfoUtil.getGroupInfo(groups, indexArray);
		List refactorList = returnGroups[returnGroups.length - 1];

		List expectedList = new ArrayList();
		expectedList.add(getGroupInfo(0, 0));
		expectedList.add(getGroupInfo(0, 1));
		expectedList.add(getGroupInfo(1, 3));
		expectedList.add(getGroupInfo(2, 4));
		expectedList.add(getGroupInfo(3, 5));

		assertTrue(twoListEqual(expectedList, refactorList));
	}

	@Test
	public void testMultipleEntireGroup() throws Exception {
		List[] groups = getExampleGroupList();
		int[] indexArray = { 0, 5, 6, 8 };
		List[] returnGroups = GroupInfoUtil.getGroupInfo(groups, indexArray);
		List refactorList = returnGroups[returnGroups.length - 1];

		List expectedList = new ArrayList();
		expectedList.add(getGroupInfo(0, 0));
		expectedList.add(getGroupInfo(0, 1));
		expectedList.add(getGroupInfo(1, 2));
		expectedList.add(getGroupInfo(2, 3));

		assertTrue(twoListEqual(expectedList, refactorList));
	}

	/**
	 * 
	 * @throws DataException
	 */
	@Test
	public void testGetGroupInfo() throws DataException {
		List[] groups = getTest0(SOURCE);
		int[] indexArray = { 0, 1, 4, 5 };
		List[] result = GroupInfoUtil.getGroupInfo(groups, indexArray);
		assertTrue(twoGroupListArrayEqual(result, getTest0(TARGET)));
	}

	private List[] getExampleGroupList() {
		List level0 = new ArrayList();
		List level1 = new ArrayList();
		List level2 = new ArrayList();

		level0.add(getGroupInfo(-1, 0));
		level0.add(getGroupInfo(-1, 2));

		level1.add(getGroupInfo(0, 0));
		level1.add(getGroupInfo(0, 2));
		level1.add(getGroupInfo(1, 4));
		level1.add(getGroupInfo(1, 5));

		level2.add(getGroupInfo(0, 0));
		level2.add(getGroupInfo(0, 2));
		level2.add(getGroupInfo(1, 3));
		level2.add(getGroupInfo(1, 5));
		level2.add(getGroupInfo(2, 6));
		level2.add(getGroupInfo(3, 7));

		List[] groups = { level0, level1, level2 };
		return groups;
	}
}
