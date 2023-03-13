/*
 *************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestAdvQueryImpl;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestParamMetaDataImpl;
import org.eclipse.birt.data.engine.odaconsumer.testutil.OdaTestDriverCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test ODA Consumer handling of input parameters by native name.
 */
@Ignore("Ignore tests that require manual setup")
public class ParameterInNativeNameTest extends OdaTestDriverCase {
	@Test
	public void testSetParameterByNativeName() throws Exception {
		PreparedStatement hostStmt = null;

		try {
			hostStmt = getOpenedConnection().prepareStatement(null, TestAdvQueryImpl.TEST_CASE_IN_PARAM_NAME);
			assertTrue(hostStmt != null);

			// no hints specified,
			// specify native name directly
			hostStmt.setParameterValue(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 1, "stringValue");
			hostStmt.setParameterValue(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 2,
					Date.valueOf("2005-11-13"));

			// test driver does not support setBoolean by name,
			// without hints to get corresponding index, expects it to retry setString by
			// name
			hostStmt.setParameterValue(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 3,
					Boolean.valueOf("true"));

			boolean execStatus = hostStmt.execute();
			assertTrue(execStatus);
		} catch (DataException e1) {
			fail("testSetParameterByNativeName failed: " + e1.toString());
		}
	}

	@Test
	public void testSetParameterWithNameInHints() throws Exception {
		PreparedStatement hostStmt = null;

		try {
			hostStmt = getOpenedConnection().prepareStatement(null, TestAdvQueryImpl.TEST_CASE_IN_PARAM_NAME);
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

			// use hint's param name, and let it look up corresponding native name
			hostStmt.setParameterValue("ParamName1", "stringValue");
			hostStmt.setParameterValue("ParamName2", Date.valueOf("2005-11-13"));

			// test driver does not support setBoolean by name,
			// without corresponding index in hint, expects it to retry setString by name
			hostStmt.setParameterValue("ParamName3", Boolean.valueOf("true"));

			boolean execStatus = hostStmt.execute();
			assertTrue(execStatus);
		} catch (DataException e1) {
			fail("testSetParameterWithNameInHints failed: " + e1.toString());
		}
	}

	@Test
	public void testSetParameterWithPositionInHints() throws Exception {
		PreparedStatement hostStmt = null;

		try {
			hostStmt = getOpenedConnection().prepareStatement(null, TestAdvQueryImpl.TEST_CASE_IN_PARAM_NAME);
			assertTrue(hostStmt != null);

			ParameterHint hint = new ParameterHint("ParamName3", true, false);
			hint.setPosition(3);
			hint.setNativeName(TestParamMetaDataImpl.TEST_PARAM_NATIVE_NAME_PREFIX + 3);
			hostStmt.addParameterHint(hint);

			// test driver does not support setBoolean by name,
			// with corresponding index in hint, expects it to
			// setBoolean by id, which throws an OdaException, and
			// triggers retry to setString by name
			hostStmt.setParameterValue("ParamName3", Boolean.valueOf("true"));

			boolean execStatus = hostStmt.execute();
			assertTrue(execStatus);
		} catch (DataException e1) {
			fail("testSetParameterWithPositionInHints failed: " + e1.toString());
		}
	}

}
