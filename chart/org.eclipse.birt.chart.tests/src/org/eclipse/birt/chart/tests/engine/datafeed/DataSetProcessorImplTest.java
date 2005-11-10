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

package org.eclipse.birt.chart.tests.engine.datafeed;

import java.util.Calendar;

import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.datafeed.DataSetProcessorImpl;

import junit.framework.TestCase;


/**
 * Test fromString(), getMinimum(), getMaximum() and populate() 
 * in DataSetProcessorImpl.java
 * 
 */
public class DataSetProcessorImplTest extends TestCase {
	
	DateTimeDataSet ds1 = DateTimeDataSetImpl.create(new Calendar[] { 
			new CDateTime(2001, 5, 1),
			new CDateTime(2001, 4, 11), 
			new CDateTime(2002, 8, 23) }
			);
	
	NumberDataSet ds2 = NumberDataSetImpl.create(new double[] {
			16.17, 24.21, -43.0});
	
	TextDataSet ds3 = TextDataSetImpl.create(new String[] {
			"a", "ab", "b"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	
	DataSetProcessorImpl dsProcessor = new DataSetProcessorImpl();
	
	//Test getMaximun()
	public void testGetMaximun() throws Exception {
		
		assertEquals(new CDateTime(2002, 8, 23), dsProcessor.getMaximum(ds1));
		assertEquals(new Double(24.21), dsProcessor.getMaximum(ds2));	
	}
	
	//Test getMinimun()
	public void testGetMinimun() throws Exception {
		
		assertEquals(new CDateTime(2001, 4, 11), dsProcessor.getMinimum(ds1));
		assertEquals(new Double(-43.0), dsProcessor.getMinimum(ds2));
	}
	
	//Test fromString()
	public void testFromString() throws Exception {
		assertEquals(ds1, dsProcessor.fromString(null, ds1));		
	}

}
