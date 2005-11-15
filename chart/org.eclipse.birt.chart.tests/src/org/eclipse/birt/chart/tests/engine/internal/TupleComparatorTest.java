/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.engine.internal;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import org.eclipse.birt.chart.internal.factory.TupleComparator;

/**
 * Test class for checking the correctness of compareObjects() method 
 * in TupleComparator.java class.
 */

public class TupleComparatorTest extends TestCase {

	public void testCompareObjects() {

		assertEquals(0, TupleComparator.compareObjects(null, null) );
		assertEquals(-1, TupleComparator.compareObjects(null, "abc") ); //$NON-NLS-1$
		assertEquals(1, TupleComparator.compareObjects("abc", null) ); //$NON-NLS-1$
		
		assertEquals(1, TupleComparator.compareObjects("abc", "a") ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(-1, TupleComparator.compareObjects("ab", "b") ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, TupleComparator.compareObjects("abc", "abc") ); //$NON-NLS-1$ //$NON-NLS-2$
		
		Integer n1 = new Integer(80);
		Integer n2 = new Integer(60);
		assertEquals(1, TupleComparator.compareObjects(n1, n2) );
		assertEquals(-1, TupleComparator.compareObjects(n2, n1) );
		
		Date a = new Date((long)8E8);
		Date b = new Date((long)7E9);	
		assertEquals(-1, TupleComparator.compareObjects(a, b) );
		assertEquals(1, TupleComparator.compareObjects(b, a) );
		
        Calendar c1 = Calendar.getInstance();
        c1.set(1999,2,5);
        Calendar c2 = Calendar.getInstance();
        c2.set(2000,1,23);
		assertEquals(-1, TupleComparator.compareObjects(c1, c2) );
		assertEquals(1, TupleComparator.compareObjects(c2, c1) );
	}
}