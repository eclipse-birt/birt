/*
 *************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestAdvQueryImpl;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestParamMetaDataImpl;
import org.eclipse.birt.data.engine.odaconsumer.testutil.OdaTestDriverCase;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Test ODA Consumer handling of output parameters, by index or native name.
 */
@Ignore("Ignore tests that require manual setup")
public class OutputParametersTest extends OdaTestDriverCase {
	@Test
	public void testOutputParamDataTypeMapping() {
		PreparedStatement hostStmt = null;
		Object outParam2Value = null;
		Object outParam3Value = null;

		try {
			// uses default dataSetType in plugin.xml
			hostStmt = getOpenedConnection().prepareStatement(null, TestAdvQueryImpl.TEST_CASE_OUTPUTPARAM);
			assertTrue(hostStmt != null);

			boolean execStatus = hostStmt.execute();
			assertTrue(execStatus);

			outParam2Value = hostStmt.getParameterValue(2);
			outParam3Value = hostStmt.getParameterValue(3);
		} catch (DataException e1) {
			fail("testOutputParamDataTypeMapping failed: " + e1.toString());
		}

		// parameter 2 is expected to have a data type with mapping
		// in test driver's plugin.xml, and would thus trigger the
		// correct call to getDate, returning a Date value
		assertTrue(outParam2Value != null);
		assertTrue(outParam2Value instanceof java.util.Date);

		// parameter 3 is not expected to have a data type with mapping
		// in test driver's plugin.xml; so it will be mapped to a String by default,
		// and getString will be called, returning a String value
		assertTrue(outParam3Value != null);
		assertTrue(outParam3Value instanceof String);
	}

	@Test
	public void testGetParameterByNativeName() throws Exception {
		PreparedStatement hostStmt = null;
		Object outParam1Value = null;
		Object outParam2Value = null;
		Object outParam3Value = null;

		try {
			// uses default dataSetType in plugin.xml
			hostStmt = getOpenedConnection().prepareStatement(null, TestAdvQueryImpl.TEST_CASE_OUTPUTPARAM);
			assertTrue(hostStmt != null);

			boolean execStatus = hostStmt.execute();
			assertTrue(execStatus);

			// no hints specified,
			// specify native name directly
			outParam1Value = hostStmt.getParameterValue(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 1);
			outParam2Value = hostStmt.getParameterValue(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 2);
			outParam3Value = hostStmt.getParameterValue(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 3);
		} catch (DataException e1) {
			fail("testGetParameterByNativeName failed: " + e1.toString());
		}

		assertTrue(outParam1Value != null);
		assertTrue(outParam1Value instanceof String);
		assertTrue(((String) outParam1Value).startsWith(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX));

		// parameter 2 is expected to have a data type with mapping
		// in test driver's plugin.xml, and would thus trigger the
		// correct call to getDate, returning a Date value
		assertTrue(outParam2Value != null);
		assertTrue(outParam2Value instanceof java.util.Date);

		// test driver does not support getBoolean by name,
		// parameter 3 is not expected to have a data type with mapping
		// in test driver's plugin.xml; so it will be mapped to a String by default,
		// and getString will be called, returning a String value
		assertTrue(outParam3Value != null);
		assertTrue(outParam3Value instanceof String);
		assertTrue(((String) outParam3Value).startsWith(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX));
	}

	@Test
	public void testGetParameterWithNativeNameInHints() throws Exception {
		PreparedStatement hostStmt = null;
		Object outParam1Value = null;
		Object outParam2Value = null;
		Object outParam3Value = null;

		try {
			// uses default dataSetType in plugin.xml
			hostStmt = getOpenedConnection().prepareStatement(null, TestAdvQueryImpl.TEST_CASE_OUTPUTPARAM);
			assertTrue(hostStmt != null);

			ParameterHint hint = new ParameterHint("ParamName1", true, false);
			hint.setNativeName(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 1);
			hostStmt.addParameterHint(hint);

			hint = new ParameterHint("ParamName2", true, false);
			hint.setNativeName(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 2);
			hostStmt.addParameterHint(hint);

			hint = new ParameterHint("ParamName3", true, false);
			hint.setNativeName(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 3);
			hostStmt.addParameterHint(hint);

			boolean execStatus = hostStmt.execute();
			assertTrue(execStatus);

			// use hint's param name, and let it look up corresponding native name
			outParam1Value = hostStmt.getParameterValue("ParamName1");
			outParam2Value = hostStmt.getParameterValue("ParamName2");
			outParam3Value = hostStmt.getParameterValue("ParamName3");
		} catch (DataException e1) {
			fail("testGetParameterByNativeName failed: " + e1.toString());
		}

		assertTrue(outParam1Value != null);
		assertTrue(outParam1Value instanceof String);
		assertTrue(((String) outParam1Value).startsWith(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX));

		// parameter 2 is expected to have a data type with mapping
		// in test driver's plugin.xml, and would thus trigger the
		// correct call to getDate, returning a Date value
		assertTrue(outParam2Value != null);
		assertTrue(outParam2Value instanceof java.util.Date);

		// test driver does not support getBoolean by name,
		// parameter 3 is not expected to have a data type with mapping
		// in test driver's plugin.xml; so it will be mapped to a String by default,
		// and getString will be called, returning a String value
		assertTrue(outParam3Value != null);
		assertTrue(outParam3Value instanceof String);
		assertTrue(((String) outParam3Value).startsWith(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX));
	}

	@Test
	public void testGetParameterWithPositionInHints() throws Exception {
		PreparedStatement hostStmt = null;
		Object outParam3Value = null;

		try {
			// uses default dataSetType in plugin.xml
			hostStmt = getOpenedConnection().prepareStatement(null, TestAdvQueryImpl.TEST_CASE_OUTPUTPARAM);
			assertTrue(hostStmt != null);

			// hint w/o native name will be merged by position
			ParameterHint hint = new ParameterHint("ParamName3", true, false);
			hint.setPosition(3);
			hostStmt.addParameterHint(hint);

			boolean execStatus = hostStmt.execute();
			assertTrue(execStatus);

			// use hint's param name, and let it look up corresponding native name
			outParam3Value = hostStmt.getParameterValue("ParamName3");
		} catch (DataException e1) {
			fail("testGetParameterByNativeName failed: " + e1.toString());
		}

		// test driver does not support getBoolean by name,
		// parameter 3 is not expected to have a data type with mapping
		// in test driver's plugin.xml; so it will be mapped to a String by default,
		// and getString will be called, returning a String value
		assertTrue(outParam3Value != null);
		assertTrue(outParam3Value instanceof String);
		assertTrue(((String) outParam3Value).startsWith(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX));
	}

}
