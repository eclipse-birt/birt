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

import junit.framework.TestCase;

import org.eclipse.birt.chart.internal.datafeed.GroupingLookupHelper;
import org.eclipse.birt.chart.internal.datafeed.ResultSetWrapper;

public class ResultSetWrapperTest extends TestCase {

	private String dataExp[] = { "Product", "Manufacturer", "Month", "Month" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	List dataList = new ArrayList(Arrays.asList(dataExp));
	private String aggExp[] = { "Sum", "Sum", "Sum", "Avg" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	List aggList = new ArrayList(Arrays.asList(aggExp));

	List result = new ArrayList();
	Object[] oaTuple1 = new Object[] { "A", "M1", Integer.valueOf(10), Integer.valueOf(10) };//$NON-NLS-1$ //$NON-NLS-2$
	Object[] oaTuple2 = new Object[] { "B", "M2", Integer.valueOf(8), Integer.valueOf(8) };//$NON-NLS-1$ //$NON-NLS-2$

	ResultSetWrapper wrapper;

	protected void setUp() throws Exception {
		super.setUp();
		result.add(oaTuple1);
		result.add(oaTuple2);

		wrapper = new ResultSetWrapper(new GroupingLookupHelper(dataList, aggList), result, null);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		result = null;
		dataList = null;
		aggList = null;
	}

	public void testGetColumnCount() {
		assertEquals(4, wrapper.getColumnCount());
	}

	public void testGetRowCount() {
		assertEquals(2, wrapper.getRowCount());
	}

	public void testGetGroupCount() {
		assertEquals(1, wrapper.getGroupCount());
	}

	public void testGetGroupKey() {
		assertEquals(Integer.valueOf(10), wrapper.getGroupKey(0, 2));
		assertEquals("A", wrapper.getGroupKey(0, "Product", "Sum"));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(Integer.valueOf(10), wrapper.getGroupKey(0, "Month", "Sum"));//$NON-NLS-1$ //$NON-NLS-2$
	}
}
