/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.computation;

import junit.framework.TestCase;

import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;

import com.ibm.icu.text.DecimalFormat;

public class ValueFormatterTest extends TestCase {

	public void testCorrectNumber() {

	}

	public void testFormat1() throws ChartException {
		assertEquals(null, ValueFormatter.format(null, null, null, null));
		assertEquals("ABC", ValueFormatter.format("ABC", null, null, null));//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void testFormat2() throws ChartException {
		assertEquals("13.1", ValueFormatter.format(new Double(13.1), null, null, new DecimalFormat()));//$NON-NLS-1$
		assertEquals("13.1", //$NON-NLS-1$
				ValueFormatter.format(NumberDataElementImpl.create(13.1), null, null, new DecimalFormat()));
	}
}
