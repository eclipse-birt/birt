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

package org.eclipse.birt.core.format;

import java.text.ParseException;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * StringFormatterTest.
 *
 * Design for test whether StringFormatter Class can do a correct formating,
 * translate the string according to the format string
 *
 */
public class StringFormatterTest extends TestCase {

	// test function StringFormatter.applyPattern &
	// StringFormatter.applyPattern()
	@Test
	public void testApplyPattern() {
		StringFormatter sampleStr = new StringFormatter();
		sampleStr.applyPattern("@@@@&&@@@<<<>>>!@@@&>");
		assertEquals("@@@@&&@@@<<<>>>!@@@&>", sampleStr.getPattern());
	}

	// test function StringFormatter.format(String Str)
	@Test
	public void testFormat() {
		StringFormatter sampleStr = new StringFormatter();
		assertEquals("", sampleStr.getPattern());
		sampleStr.applyPattern("@@@@");
		assertEquals("@@@@", sampleStr.getPattern());
		assertEquals("1234fggggg", sampleStr.format("1234fggggg"));
		sampleStr.applyPattern("@@@@!");
		assertEquals("1234fggggg", sampleStr.format("1234fggggg"));

		sampleStr.applyPattern("@@@@!");
		assertEquals("123 ", sampleStr.format("123"));
		sampleStr.applyPattern("@@@@!!!");
		assertEquals("123 ", sampleStr.format("123"));
		sampleStr.applyPattern("@@@@");
		assertEquals("123456", sampleStr.format("123456"));
		sampleStr.applyPattern("@@@@!");
		assertEquals("123456", sampleStr.format("123456"));
		sampleStr.applyPattern("(@@)@@@");
		assertEquals("(  )123", sampleStr.format("123"));
		sampleStr.applyPattern("&&&&!");
		assertEquals("123", sampleStr.format("123"));
		assertEquals("123", sampleStr.format(" 123  "));
		sampleStr.applyPattern("&&&&!^");
		assertEquals(" 123  ", sampleStr.format(" 123  "));
		sampleStr.applyPattern("&&&&&&");
		assertEquals("123", sampleStr.format("123"));
		sampleStr.applyPattern("@@@&!");
		assertEquals("123", sampleStr.format("123"));
		sampleStr.applyPattern("&@@@&!");
		assertEquals("123 ", sampleStr.format("123"));
		sampleStr.applyPattern("@@@&");
		assertEquals(" 123", sampleStr.format("123"));

		sampleStr.applyPattern("@@@@@aaa!");
		assertEquals("123  aaa", sampleStr.format("123"));
		sampleStr.applyPattern("@@aaa!");
		assertEquals("12aaa3", sampleStr.format("123"));
		sampleStr.applyPattern("@@aaa&&&!");
		assertEquals("12aaa3", sampleStr.format("123"));
		sampleStr.applyPattern("@@@@@aaa>!");
		assertEquals("123  aaa", sampleStr.format("123"));

		sampleStr.applyPattern("@@@@<!");
		assertEquals("1234fggggg", sampleStr.format("1234fggggg"));
		sampleStr.applyPattern("@@@@<!");
		assertEquals("1234fggggg", sampleStr.format("1234fggGgG"));
		sampleStr.applyPattern("@@@@>!");
		assertEquals("1234FGGGGG", sampleStr.format("1234fggggg"));
		sampleStr.applyPattern("@@@@&&&>!");
		assertEquals("1234FGGGGG", sampleStr.format("1234fggggg"));

		sampleStr.applyPattern("@@@@&&@@@<<<>>>@@@&>");
		assertEquals("   1234FGGGGG", sampleStr.format("1234fggggg"));

		sampleStr.applyPattern("!");
		assertEquals("123", sampleStr.format("123"));
		sampleStr.applyPattern("");
		assertEquals("123", sampleStr.format("123"));
		sampleStr.applyPattern(">");
		assertEquals("123AAA", sampleStr.format("123aaA"));
		assertEquals("123AAA", sampleStr.format("123aaA"));

		sampleStr.applyPattern("***\"!");
		assertEquals("***\"123", sampleStr.format("123"));
		sampleStr.applyPattern("***&YY&&&!");
		assertEquals("***1YY23", sampleStr.format("123"));
		sampleStr.applyPattern("***&YY@@@!");
		assertEquals("***1YY23 ", sampleStr.format("123"));

		// test for SSN
		sampleStr.applyPattern("@@@-@@-@@@@!");
		assertEquals("600-00-03274", sampleStr.format("6000003274"));
		// test for zipcode+4
		sampleStr.applyPattern("@@@@@-@@@@!");
		assertEquals("94305-0110", sampleStr.format("943050110"));
		assertEquals("94305-0110", sampleStr.format("943050110   "));
		sampleStr.applyPattern("@@@@@-@@@@!^");
		assertEquals("94305-0110   ", sampleStr.format("943050110   "));
		sampleStr.applyPattern("@@@@@-@@@@");
		assertEquals("94305-0110", sampleStr.format("943050110"));
		assertEquals("94305-0110", sampleStr.format("   943050110"));
		sampleStr.applyPattern("@@@@@-@@@@^");
		assertEquals("   94305-0110", sampleStr.format("   943050110"));
		// test for zipcode
		sampleStr.applyPattern("@@@@@!");
		assertEquals("94305", sampleStr.format("94305"));
		sampleStr.applyPattern("@@@@@");
		assertEquals("94305", sampleStr.format("94305"));
		// test for phonenumber
		sampleStr.applyPattern("(@@@)-@@@-@@@@!");
		assertEquals("(650)-837-2345,", sampleStr.format("6508372345,"));
		sampleStr.applyPattern("(@@@)-@@@-@@@@");
		assertEquals("(650)-837-2345", sampleStr.format("6508372345"));

	}

	@Test
	public void testParser() throws Exception {
		StringFormatter sampleStr = new StringFormatter();
		assertEquals("", sampleStr.getPattern());
		sampleStr.applyPattern("@@@@");
		assertEquals("1234fggggg", sampleStr.parser("1234fggggg"));
		sampleStr.applyPattern("@@@@!");
		assertEquals("1234fggggg", sampleStr.parser("1234fggggg"));

		sampleStr.applyPattern("@@@@!");
		assertEquals("123", sampleStr.parser("123 "));
		sampleStr.applyPattern("@@@@!!!");
		assertEquals("123", sampleStr.parser("123 "));
		sampleStr.applyPattern("@@@@");
		assertEquals("123456", sampleStr.parser("123456"));
		sampleStr.applyPattern("@@@@!");
		assertEquals("123456", sampleStr.parser("123456"));
		sampleStr.applyPattern("(@@)@@@");
		assertEquals("123", sampleStr.parser("(  )123"));
		sampleStr.applyPattern("(&&)@@@");
		assertEquals("  123", sampleStr.parser("(  )123"));
		sampleStr.applyPattern("&&&&!");
		assertEquals("123 ", sampleStr.parser("123"));
		sampleStr.applyPattern("&&&&&&");
		assertEquals("   123", sampleStr.parser("123"));
		sampleStr.applyPattern("@@@&!");
		assertEquals("123 ", sampleStr.parser("123"));
		sampleStr.applyPattern("&@@@&!");
		try {
			assertEquals("123 ", sampleStr.parser("123"));
			assert (false);
		} catch (ParseException ex) {
			assert (true);
		}
		sampleStr.applyPattern("@@@&");
		assertEquals("123", sampleStr.parser(" 123"));

		sampleStr.applyPattern("@@@@@aaa!");
		assertEquals("123", sampleStr.parser("123  aaa"));
		sampleStr.applyPattern("@@@&&aaa!");
		assertEquals("123  ", sampleStr.parser("123  aaa"));
		sampleStr.applyPattern("@@aaa!");
		assertEquals("123", sampleStr.parser("12aaa3"));
		sampleStr.applyPattern("@@aaa&&&!");
		try {
			assertEquals("12aaa3", sampleStr.parser("123"));
			assert (false);
		} catch (ParseException ex) {
			assert (true);
		}
		sampleStr.applyPattern("@@@@@aaa>!");
		assertEquals("123", sampleStr.parser("123"));

		sampleStr.applyPattern("@@@@<!");
		assertEquals("1234fggggg", sampleStr.parser("1234fggggg"));
		sampleStr.applyPattern("@@@@<!");
		assertEquals("1234fggGgG", sampleStr.parser("1234fggGgG"));
		sampleStr.applyPattern("@@@@>!");
		assertEquals("1234fggggg", sampleStr.parser("1234fggggg"));
		sampleStr.applyPattern("@@@@&&&>!");
		assertEquals("1234fggggg", sampleStr.parser("1234fggggg"));

		sampleStr.applyPattern("@@@@&&@@@<<<>>>@@@&>");
		assertEquals("1234fggggg", sampleStr.parser("1234fggggg"));

		sampleStr.applyPattern("!");
		assertEquals("123", sampleStr.parser("123"));
		sampleStr.applyPattern("");
		assertEquals("123", sampleStr.parser("123"));
		sampleStr.applyPattern(">");
		assertEquals("123aaA", sampleStr.parser("123aaA"));

		sampleStr.applyPattern("***\"!");
		assertEquals("123", sampleStr.parser("***\"123"));
		sampleStr.applyPattern("***&YY&&&!");
		assertEquals("123 ", sampleStr.parser("***1YY23"));
		sampleStr.applyPattern("***&YY@@@!");
		assertEquals("123", sampleStr.parser("***1YY23 "));

		sampleStr.applyPattern("***\"!^");
		assertEquals("123", sampleStr.parser("***\"123"));
		sampleStr.applyPattern("***&YY&&&!^");
		assertEquals("123 ", sampleStr.parser("***1YY23"));
		sampleStr.applyPattern("***&YY@@@!^");
		assertEquals("123", sampleStr.parser("***1YY23 "));

		// test for SSN
		sampleStr.applyPattern("@@@-@@-@@@@!");
		assertEquals("6000003274", sampleStr.parser("600-00-03274"));
		// test for zipcode+4
		sampleStr.applyPattern("@@@@@-@@@@!");
		assertEquals("943050110", sampleStr.parser("94305-0110"));
		sampleStr.applyPattern("@@@@@-@@@@");
		assertEquals("943050110", sampleStr.parser("94305-0110"));
		// test for zipcode
		sampleStr.applyPattern("@@@@@!");
		assertEquals("94305", sampleStr.parser("94305"));
		sampleStr.applyPattern("@@@@@");
		assertEquals("94305", sampleStr.parser("94305"));
		// test for phonenumber
		sampleStr.applyPattern("(@@@)-@@@-@@@@!");
		assertEquals("6508372345", sampleStr.parser("(650)-837-2345"));
		sampleStr.applyPattern("(@@@)-@@@-@@@@");
		assertEquals("6508372345", sampleStr.parser("(650)-837-2345"));
		sampleStr.applyPattern("(@@@)-@@@-@@@@!");
		assertEquals("65083723456", sampleStr.parser("(650)-837-23456"));
		sampleStr.applyPattern("(@@@)-@@@-@@@@");
		assertEquals("346508372345", sampleStr.parser("34(650)-837-2345"));

	}

	@Test
	public void testTrim() {
		StringFormatter sampleStr = new StringFormatter("Zip Code + 4");
		assertEquals("650837-2000", sampleStr.format("6508372000"));
		assertEquals("650837-2000", sampleStr.format(" 6508372000 "));
		assertEquals("     - 650", sampleStr.format("650 "));
	}
}
