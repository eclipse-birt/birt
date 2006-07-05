/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.tests.engine.datafeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.birt.chart.internal.datafeed.ResultSetDataSet;
import org.eclipse.birt.chart.internal.datafeed.ResultSetWrapper;

public class ResultSetDataSetTest extends TestCase {
	private String exp[] = { "Product", "Manufacturer", "Month" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	List list = new ArrayList(Arrays.asList(exp));
	Set expressionKey = new HashSet(list);

	List result = new ArrayList();
	Object[] oaTuple1 = new Object[] {"A", "M1", new Integer(10)};//$NON-NLS-1$ //$NON-NLS-2$
	Object[] oaTuple2 = new Object[] {"B", "M2", new Integer(10)};//$NON-NLS-1$ //$NON-NLS-2$

	protected void setUp() throws Exception {
		super.setUp();
		result.add(oaTuple1);
		result.add(oaTuple2);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		result = null;
	}
	
	ResultSetWrapper wrapper = new ResultSetWrapper(expressionKey,
			result, null);
	
	int[] iaColumnIndexes = {2};
	ResultSetDataSet dataset = new ResultSetDataSet(wrapper, iaColumnIndexes, 0, 1 );

	public void testHasNext(){
		assertEquals(true, dataset.hasNext());
	}
	
	public void testSize() {
		assertEquals(1, dataset.getSize());
	}
}
