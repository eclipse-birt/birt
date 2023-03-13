/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.chart.tests.engine.model.attribute;

import org.eclipse.birt.chart.model.attribute.DateFormatDetail;

import junit.framework.TestCase;

public class DateFormatDetailTest extends TestCase {

	public void testConstant() {
		assertEquals(DateFormatDetail.DATE, DateFormatDetail.DATE_LITERAL.getValue());
		assertEquals(DateFormatDetail.DATE_TIME, DateFormatDetail.DATE_TIME_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(DateFormatDetail.DATE_LITERAL, DateFormatDetail.get(DateFormatDetail.DATE));

		assertEquals(DateFormatDetail.DATE_LITERAL, DateFormatDetail.get("Date")); //$NON-NLS-1$
		assertEquals(DateFormatDetail.DATE_TIME_LITERAL, DateFormatDetail.get("Date_Time")); //$NON-NLS-1$

		assertNull(DateFormatDetail.get("No Match")); //$NON-NLS-1$
	}
}
