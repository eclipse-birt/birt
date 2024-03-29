
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.junit.Test;

/**
 *
 */

public class LevelMemberTest {
	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testSaveAndLoad() throws IOException {
		int keyCount = 10000;
		BufferedStructureArray bufferedStructureArray = new BufferedStructureArray(Member.getCreator(), 2000);
		for (int i = 0; i < keyCount; i++) {
			bufferedStructureArray.add(create(i));
		}
		for (int i = 0; i < keyCount; i++) {
			Member member1 = (Member) bufferedStructureArray.get(i);
			Member member2 = create(i);
			assertEquals(member1.compareTo(member2), 0);
			assertEquals(member1.getAttributes()[0], member2.getAttributes()[0]);
			assertEquals(member1.getAttributes()[1], member2.getAttributes()[1]);
		}
		bufferedStructureArray.clear();
		bufferedStructureArray.close();
	}

	private Member create(int i) {
		Member key = new Member();
		key.setKeyValues(new Object[] { new Integer(i) });
		key.setAttributes(new Object[2]);
		key.getAttributes()[0] = String.valueOf(i + 1);
		key.getAttributes()[1] = new Date(i + 2);
		return key;
	}
}
