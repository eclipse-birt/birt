/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.IMetaLogger;

import junit.framework.TestCase;

/**
 * Tests the meta log manager.
 */
public class MetaLogManagerTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test registerLogger.
	 * <p>
	 * The registered logger will be notified of the log(), shutDown() action.
	 */

	public void testRegisterLogger() {
		Logger logger = new Logger();
		MetaLogManager.registerLogger(logger);

		MetadataTestUtil.log("abc"); //$NON-NLS-1$
		assertTrue(logger.logged);

		MetaLogManager.shutDown();
		assertTrue(logger.closed);
	}

	/**
	 * Test removeLogger.
	 * <p>
	 * When a logger is removed from the manager, it will never be notified of the
	 * log() action. It's close() was triggered when removed from the manager.
	 */

	public void testRemoveLogger() {
		Logger logger = new Logger();
		MetaLogManager.registerLogger(logger);
		MetaLogManager.removeLogger(logger);

		MetadataTestUtil.log("abc"); //$NON-NLS-1$
		assertFalse(logger.logged);

		MetaLogManager.shutDown();
		assertTrue(logger.closed);
	}

	/**
	 * Implementation of the IMetaLogger.
	 *
	 */

	class Logger implements IMetaLogger {
		boolean logged = false;

		boolean closed = false;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.metadata.IMetaLogger#log(java.lang.String)
		 */
		@Override
		public void log(String message) {
			logged = true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.metadata.IMetaLogger#log(java.lang.String,
		 * java.lang.Throwable)
		 */
		@Override
		public void log(String message, Throwable t) {
			logged = true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.metadata.IMetaLogger#close()
		 */
		@Override
		public void close() {
			closed = true;
		}
	}
}
