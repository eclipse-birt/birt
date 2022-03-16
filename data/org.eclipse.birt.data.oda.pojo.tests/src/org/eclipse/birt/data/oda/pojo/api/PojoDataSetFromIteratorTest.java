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
package org.eclipse.birt.data.oda.pojo.api;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.junit.Test;

/**
 *
 */

public class PojoDataSetFromIteratorTest {

	@SuppressWarnings("nls")
	@Test
	public void testNext() throws OdaException {
		IPojoDataSet pds = new PojoDataSetFromIterator() {
			@SuppressWarnings("unchecked")
			@Override
			protected Iterator fetchPojos() throws OdaException {
				return null;
			}
		};
		pds.open(null, null);
		assertTrue(pds.next() == null);
		pds.close();

		pds = new PojoDataSetFromIterator() {
			@SuppressWarnings("unchecked")
			@Override
			protected Iterator fetchPojos() throws OdaException {
				return createIteratorFromArray(new Object[0]);
			}
		};
		pds.open(null, null);
		assertTrue(pds.next() == null);
		pds.close();

		pds = new PojoDataSetFromIterator() {
			@SuppressWarnings({ "unchecked" })
			@Override
			protected Iterator fetchPojos() throws OdaException {
				return createIteratorFromArray(new Object[] { "1", "2", "3" });
			}
		};
		pds.open(null, null);
		assertTrue(pds.next().equals("1"));
		assertTrue(pds.next().equals("2"));
		assertTrue(pds.next().equals("3"));
		assertTrue(pds.next() == null);
		pds.close();

		pds.open(null, null);
		assertTrue(pds.next().equals("1"));
		assertTrue(pds.next().equals("2"));
		assertTrue(pds.next().equals("3"));
		assertTrue(pds.next() == null);
		pds.close();

		pds = new PojoDataSetFromIterator() {
			@SuppressWarnings("unchecked")
			@Override
			protected Iterator fetchPojos() throws OdaException {
				return createIteratorFromArray(new Object[] { null, "1", null, null, "2", null, "3", null, null });
			}
		};

		pds.open(null, null);
		assertTrue(pds.next().equals("1"));
		assertTrue(pds.next().equals("2"));
		assertTrue(pds.next().equals("3"));
		assertTrue(pds.next() == null);
		pds.close();

		pds.open(null, null);
		assertTrue(pds.next().equals("1"));
		assertTrue(pds.next().equals("2"));
		assertTrue(pds.next().equals("3"));
		assertTrue(pds.next() == null);
		pds.close();
	}

	@SuppressWarnings("unchecked")
	private static Iterator createIteratorFromArray(Object[] array) {
		if (array == null) {
			return null;
		}
		List l = new ArrayList();
		for (Object o : array) {
			l.add(o);
		}
		return l.iterator();
	}
}
