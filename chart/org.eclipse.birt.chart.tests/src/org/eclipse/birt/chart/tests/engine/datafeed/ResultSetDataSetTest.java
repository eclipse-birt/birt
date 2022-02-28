/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.tests.engine.datafeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.chart.internal.datafeed.GroupingLookupHelper;
import org.eclipse.birt.chart.internal.datafeed.ResultSetDataSet;
import org.eclipse.birt.chart.internal.datafeed.ResultSetWrapper;

import junit.framework.TestCase;

public class ResultSetDataSetTest extends TestCase {

	private String dataExp[] = { "Product", "Manufacturer", "Month", "Month" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	List dataList = new ArrayList(Arrays.asList(dataExp));
	private String aggExp[] = { "Sum", "Sum", "Sum", "Avg" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	List aggList = new ArrayList(Arrays.asList(aggExp));

	List result = new ArrayList();
	Object[] oaTuple1 = { "A", "M1", Integer.valueOf(10), Integer.valueOf(10) };//$NON-NLS-1$ //$NON-NLS-2$
	Object[] oaTuple2 = { "B", "M2", Integer.valueOf(8), Integer.valueOf(8) };//$NON-NLS-1$ //$NON-NLS-2$

	ResultSetWrapper wrapper;
	ResultSetDataSet dataset;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		result.add(oaTuple1);
		result.add(oaTuple2);

		wrapper = new ResultSetWrapper(new GroupingLookupHelper(dataList, aggList), result, null);

		int[] iaColumnIndexes = { 2 };
		dataset = new ResultSetDataSet(wrapper, iaColumnIndexes, 0, 1);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		result = null;
		dataList = null;
		aggList = null;
	}

	public void testHasNext() {
		assertEquals(true, dataset.hasNext());
	}

	public void testSize() {
		assertEquals(1, dataset.getSize());
	}
}
