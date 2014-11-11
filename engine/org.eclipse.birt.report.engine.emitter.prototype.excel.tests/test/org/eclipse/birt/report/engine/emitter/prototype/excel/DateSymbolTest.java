/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.prototype.excel;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;

import com.ibm.icu.util.ULocale;

public class DateSymbolTest extends TestCase
{

	public void testParse( )
	{
		assertEquals( "'a", ExcelUtil.parse( null, "'''a'", ULocale.US ) );
		assertEquals( "M/d/yy",
				ExcelUtil.parse( null, "Short Date", ULocale.US ) );
		// .getDefault( ) ) );
		assertEquals( "MMM d, yyyy h:mm AM/PM",
				ExcelUtil.parse( null, null, ULocale.US ) );
		assertEquals( "yyyy.MM.dd  at HH:mm:ss ", ExcelUtil.parse( null,
				"yyyy.MM.dd G 'at' HH:mm:ss z", ULocale.US ) );
		assertEquals( "hh o'clock AM/PM ",
				ExcelUtil.parse( null, "hh 'o''clock' a z", ULocale.US ) );
		assertEquals( "yyyy-MM-ddTHH:mm:ss.SSS", ExcelUtil.parse( null,
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ", ULocale.US ) );
		assertEquals( "ddd d MMM yyyy HH:mm:ss ",
				ExcelUtil.parse( null, "EEE d MMM yyyy HH:mm:ss Z", ULocale.US ) );
		assertEquals( "ddd MMM d, 'yy",
				ExcelUtil.parse( null, "EEE MMM d, ''yy", ULocale.US ) );
	}
	public void testFormatNumberPattern()
	{
		assertEquals("###0.00", ExcelUtil.formatNumberPattern("###0.00"));
		assertEquals(
				"a\\bc\\d\\ef\\g\\hijkl\\m\\nopqr\\stuvwx\\yz\\*\\\"\\@\\/#",
				ExcelUtil
						.formatNumberPattern("abcdefghijklmnopqrstuvwxyz*\"@/"));
	}
}
