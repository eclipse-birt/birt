/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.impl.internal;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.junit.Test;

/**
 *
 */

public class PojoDataSetFromCustomClassTest {

	@SuppressWarnings({ "unchecked", "nls" })
	@Test
	public void testNext() throws OdaException {
		Class c = CustomDataSetClass.class;
		IPojoDataSet pds = new PojoDataSetFromCustomClass(c);
		pds.open(null, null);
		assertEquals("1", pds.next());
		assertEquals("2", pds.next());
		assertEquals("3", pds.next());
		assertEquals(null, pds.next());
		pds.close();

		pds.open(null, null);
		assertEquals("1", pds.next());
		assertEquals("2", pds.next());
		assertEquals("3", pds.next());
		assertEquals(null, pds.next());
		pds.close();
	}

	public static class CustomDataSetClass {
		private int i;
		private Object[] objects;

		@SuppressWarnings("nls")
		public void open(Object appContext, Map<String, Object> params) {
			objects = new Object[] { "1", "2", "3" };
			i = 0;
		}

		public Object next() {
			if (i >= objects.length) {
				return null;
			}
			return objects[i++];
		}

		public void close() {
			objects = null;
		}

	}
}
