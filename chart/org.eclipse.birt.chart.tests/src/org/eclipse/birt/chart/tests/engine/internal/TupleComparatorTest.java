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

import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.birt.chart.internal.factory.TupleComparator;

import com.ibm.icu.util.Calendar;

/**
 * Test class for checking the correctness of compareObjects() method 
 * in TupleComparator.java class.
 */

public class TupleComparatorTest extends TestCase {

	public void testCompareObjects() {

		assertEquals(0, TupleComparator.compareObjects(null, null, null) );
		assertEquals(-1, TupleComparator.compareObjects(null, "abc", null) ); //$NON-NLS-1$
		assertEquals(1, TupleComparator.compareObjects("abc", null, null) ); //$NON-NLS-1$
		
		assertEquals(1, TupleComparator.compareObjects("abc", "a", null) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(-1, TupleComparator.compareObjects("ab", "b", null) ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, TupleComparator.compareObjects("abc", "abc", null) ); //$NON-NLS-1$ //$NON-NLS-2$
		
		Integer n1 = new Integer(80);
		Integer n2 = new Integer(60);
		assertEquals(1, TupleComparator.compareObjects(n1, n2, null) );
		assertEquals(-1, TupleComparator.compareObjects(n2, n1, null) );
		
		Date a = new Date((long)8E8);
		Date b = new Date((long)7E9);	
		assertEquals(-1, TupleComparator.compareObjects(a, b, null) );
		assertEquals(1, TupleComparator.compareObjects(b, a, null) );
		
        Calendar c1 = Calendar.getInstance();
        c1.set(1999,2,5);
        Calendar c2 = Calendar.getInstance();
        c2.set(2000,1,23);
		assertEquals(-1, TupleComparator.compareObjects(c1, c2, null) );
		assertEquals(1, TupleComparator.compareObjects(c2, c1, null) );
	}
}