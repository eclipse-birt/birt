/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>ReportParameterConverter test</b>
 * <p>
 * This case tests methods in ReportParameterConverter API.
 */
public class ReportParameterConverterTest extends EngineCase {

	private TimeZone fTimeZone;

	/**
	 * @param name
	 */
	public ReportParameterConverterTest(String name) {
		super(name);
		fTimeZone = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test suite
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(ReportParameterConverterTest.class);
	}

	/**
	 * Test format(java.lang.Object reportParameterObj) method
	 */
	public void testFormat() {
		Object pStr = "p1Value";
		Object pDate = new Date("2005/05/06");
		Object pBool = new Boolean("false");
		Object pInt = new Integer(2);
		Object pFloat = new Float(0.25);

		// string parameter
		ReportParameterConverter converter = new ReportParameterConverter("(@@)", Locale.US);
		String pGet;
		pGet = converter.format(pStr);
		assertEquals("format string fail", "p1Val(ue)", pGet);
		// datetime parameter
		converter = new ReportParameterConverter("yyyy", Locale.US);
		pGet = converter.format(pDate);
		assertEquals("format datetime fail", "2005", pGet);
		// boolean parameter
		converter = new ReportParameterConverter("", Locale.US);
		pGet = converter.format(pBool);
		assertEquals("format  fail", "false", pGet);

		// float parameter
		/*
		 * converter=new ReportParameterConverter(null,Locale.US);
		 * pGet=converter.format(pFloat); assertEquals("format() fail","0.25",pGet);
		 */
		// integer parameter
		/*
		 * converter=new ReportParameterConverter("",Locale.US);
		 * pGet=converter.format(pInt); assertEquals("format() fail","2",pGet);
		 */
	}

	/**
	 * Test parse(java.lang.String reportParameterValue, int parameterValueType)
	 * method
	 */
	public void testParse() {
		String str1 = "str", date1 = "2005/05/06", bool1 = "true";
		String int1 = "8", float1 = "3.5";
		ReportParameterConverter converter = new ReportParameterConverter("(@@)", Locale.US);
		assertTrue("parse string fail", converter.parse(str1, IScalarParameterDefn.TYPE_STRING) instanceof String);
		assertEquals("str", converter.parse(str1, IScalarParameterDefn.TYPE_STRING).toString());
		assertTrue("parse datetime fail", converter.parse(date1, IScalarParameterDefn.TYPE_DATE_TIME) instanceof Date);
		assertEquals("Tue Jan 05 00:00:00 UTC 2173",
				converter.parse(date1, IScalarParameterDefn.TYPE_DATE_TIME).toString());
		assertTrue("parse boolean fail", converter.parse(bool1, IScalarParameterDefn.TYPE_BOOLEAN) instanceof Boolean);
		assertEquals("true", converter.parse(bool1, IScalarParameterDefn.TYPE_BOOLEAN).toString());
		assertTrue("parse float fail", converter.parse(float1, IScalarParameterDefn.TYPE_FLOAT) instanceof Double);
		float1 = "2.3E02";
		assertEquals("230.0", converter.parse(float1, IScalarParameterDefn.TYPE_FLOAT).toString());

		assertTrue("parse integer fail", converter.parse(int1, IScalarParameterDefn.TYPE_INTEGER) instanceof Integer);
	}
}
