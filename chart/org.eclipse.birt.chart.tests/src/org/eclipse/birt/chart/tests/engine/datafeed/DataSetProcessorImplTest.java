/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.engine.datafeed;

import org.eclipse.birt.chart.extension.datafeed.DataSetProcessorImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;

import junit.framework.TestCase;

/**
 * Test fromString(), getMinimum(), getMaximum() and populate() in
 * DataSetProcessorImpl.java
 *
 */
public class DataSetProcessorImplTest extends TestCase {
	NumberDataSet ds2 = NumberDataSetImpl.create(new double[] { 16.17, 24.21, -43.0 });

	TextDataSet ds3 = TextDataSetImpl.create(new String[] { "a", "ab", "b" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	DataSetProcessorImpl dsProcessor = new DataSetProcessorImpl();

	// Test getMaximun()
	public void testGetMaximun() throws Exception {
		assertEquals(new Double(24.21), dsProcessor.getMaximum(ds2));
	}

	// Test getMinimun()
	public void testGetMinimun() throws Exception {
		assertEquals(new Double(-43.0), dsProcessor.getMinimum(ds2));
	}

	// Test fromString()
	public void testFromString() throws Exception {
		assertEquals(ds2, dsProcessor.fromString(null, ds2));
	}

}
