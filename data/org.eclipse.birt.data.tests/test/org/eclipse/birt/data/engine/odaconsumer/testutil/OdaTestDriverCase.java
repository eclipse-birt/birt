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

package org.eclipse.birt.data.engine.odaconsumer.testutil;

import static org.junit.Assert.assertTrue;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.Connection;
import org.eclipse.birt.data.engine.odaconsumer.ConnectionManager;
import org.junit.After;
import org.junit.Before;

public abstract class OdaTestDriverCase {
	protected final String TEST_DRIVER_ID = "org.eclipse.birt.data.engine.odaconsumer.testdriver";

	private ConnectionManager sm_connManager;
	private Connection m_hostConn;

	static {
		if (System.getProperty("BIRT_HOME") == null) {
			System.setProperty("BIRT_HOME", "./test");
		}
		System.setProperty("PROPERTY_RUN_UNDER_ECLIPSE", "false");

		try {
			Platform.startup(null);
		} catch (BirtException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void odaTestDriverSetUp() throws Exception {

		if (sm_connManager == null) {
			sm_connManager = ConnectionManager.getInstance();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void odaTestDriverTearDown() throws Exception {
		try {
			if (m_hostConn != null) {
				m_hostConn.close();
			}
		} catch (DataException ex) {
			ex.printStackTrace();
		}
		ConnectionManager.releaseInstance();
	}

	protected ConnectionManager getConnectionManager() {
		return sm_connManager;
	}

	protected Connection getOpenedConnection() {
		try {
			if (m_hostConn == null) {
				m_hostConn = sm_connManager.openConnection(TEST_DRIVER_ID, null, null);
			}
		} catch (DataException ex) {
			ex.printStackTrace();
		}
		assertTrue(m_hostConn != null);
		return m_hostConn;
	}
}
