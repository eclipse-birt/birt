
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.impl.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.impl.internal.InnermostNextable;
import org.eclipse.birt.data.oda.pojo.impl.internal.Nextable;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class InnermostNextableTest {
	@SuppressWarnings({ "unchecked", "nls" })
	@Test
	public void testInnermostNextable() {
		InnermostNextable in = new InnermostNextable(null);
		assertFalse(in.next());

		List l = new ArrayList();
		in = new InnermostNextable(Nextable.createNextable(l));
		assertFalse(in.next());

		l = new ArrayList();
		l.add(null);
		l.add("s1");
		l.add(new String[0]);

		List subList = new ArrayList();
		subList.addAll(Arrays.asList(new String[] { null, "s2", "s3" }));
		l.add(subList);

		String[][] subArray = new String[][] { { null, "s4", "s5" }, { "s6" } };
		l.add(subArray);

		String[] expectedValues = { null, "s1", null, "s2", "s3", null, "s4", "s5", "s6" };

		in = new InnermostNextable(Nextable.createNextable(l));
		int rowIndex = 0;
		while (in.next()) {
			Object value = in.getValue();
			assertEquals(expectedValues[rowIndex], value);
			rowIndex++;
		}
		assertEquals(expectedValues.length, rowIndex);
		assertFalse(in.next());
	}
}
