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
package org.eclipse.birt.core.exception;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.junit.Test;

import junit.framework.TestCase;

/**
 *
 */
public class BirtExceptionTest extends TestCase {

	private static String FATAL_ERROR_KEY = "Fatal_Error";
	private static String CONCATENATED_ERROR_KEY = "Recoverable_Error";
	private static String CONCATENATED_ERROR_ONE_ARG_KEY = "Recoverable_Error_One_Arg";
	private static String fatalError = "A fatal error has happened. please contact system administrator.";
	private static String fatalErrorDE = "German: A fatal error has happened. please contact system administrator.";
	private static String concatenatedError = "A {0} error has happened. please contact {1}.";
	private static String concatenatedErrorDE = "German: A {0} error has happened. please contact {1}.";
	private static String concatenatedErrorOneArg = "A fatal error has happened. please contact {0}.";
	private static String concatenatedErrorDEOneArg = "German: A fatal error has happened. please contact {0}.";
	private static String testPluginId = "org.eclipse.birt.core";

	/*
	 * @see TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	@Test
	public void testNoRBException() {
		String errorCode = "No RB {0}";
		String error = "No RB Message";
		BirtException e = new BirtException(testPluginId, errorCode, "Message");
		assertEquals(errorCode, e.getErrorCode());
		assertEquals(error, e.getLocalizedMessage());
		assertEquals(error, e.getMessage());
	}

	@Test
	public void testCorruptFormatException() {
		String errorCode = "No RB {0";
		String error = "No RB {0";
		BirtException e = new BirtException(testPluginId, errorCode, "Message");
		assertEquals(errorCode, e.getErrorCode());
		assertEquals(error, e.getLocalizedMessage());
		assertEquals(error, e.getMessage());
	}

	@Test
	public void testBirtException() {
		// Tests for simple getters
		BirtException e1 = new BirtException(testPluginId, FATAL_ERROR_KEY, new MyResources());
		assertEquals(e1.getErrorCode(), FATAL_ERROR_KEY);
		assertEquals(e1.getLocalizedMessage(), fatalError);
		assertEquals(e1.getMessage(), fatalError);

		// Tests for localization
		BirtException e2 = new BirtException(testPluginId, FATAL_ERROR_KEY, new MyResources_de());
		assertEquals(e2.getLocalizedMessage(), fatalErrorDE);

		// Test for causes
		BirtException e3 = new BirtException(testPluginId, FATAL_ERROR_KEY, new MyResources_de(), e1);
		assertEquals(e3.getLocalizedMessage(), fatalErrorDE);
		assertEquals(e3.getCause().getLocalizedMessage(), fatalError);

		// Message formatting
		// Tests for simple getters
		BirtException e4 = new BirtException(testPluginId, CONCATENATED_ERROR_KEY,
				new Object[] { "fatal", "system administrator" }, new MyResources());
		assertEquals(e4.getErrorCode(), CONCATENATED_ERROR_KEY);
		assertEquals(e4.getLocalizedMessage(), fatalError);
		assertEquals(e4.getMessage(), fatalError);

		// Tests for localization
		BirtException e5 = new BirtException(testPluginId, CONCATENATED_ERROR_KEY,
				new Object[] { "fatal", "system administrator" }, new MyResources_de());
		assertEquals(e5.getLocalizedMessage(), fatalErrorDE);

		// Test for causes
		BirtException e6 = new BirtException(testPluginId, CONCATENATED_ERROR_KEY,
				new Object[] { "fatal", "system administrator" }, new MyResources_de(), e1);
		assertEquals(e6.getLocalizedMessage(), fatalErrorDE);
		assertEquals(e6.getCause().getLocalizedMessage(), fatalError);

		// Message formatting
		// Tests for simple getters
		BirtException e7 = new BirtException(testPluginId, CONCATENATED_ERROR_ONE_ARG_KEY,
				(Object) "system administrator", new MyResources());
		assertEquals(e7.getErrorCode(), CONCATENATED_ERROR_ONE_ARG_KEY);
		assertEquals(e7.getLocalizedMessage(), fatalError);
		assertEquals(e7.getMessage(), fatalError);

		// Tests for localization
		BirtException e8 = new BirtException(testPluginId, CONCATENATED_ERROR_ONE_ARG_KEY,
				(Object) "system administrator", new MyResources_de());
		assertEquals(e8.getLocalizedMessage(), fatalErrorDE);

		// Test for causes
		BirtException e9 = new BirtException(testPluginId, CONCATENATED_ERROR_ONE_ARG_KEY,
				(Object) "system administrator", new MyResources_de(), e1);
		assertEquals(e9.getLocalizedMessage(), fatalErrorDE);
		assertEquals(e9.getCause().getLocalizedMessage(), fatalError);

		// Test for plugin id
		BirtException e10 = new BirtException(testPluginId, CONCATENATED_ERROR_ONE_ARG_KEY, "system administrator");
		assertEquals(testPluginId, e10.getPluginId());
	}

	// default (English language, United States)
	private class MyResources extends ResourceBundle {
		@Override
		public Object handleGetObject(String key) {
			if (key.equals(FATAL_ERROR_KEY)) {
				return fatalError;
			}
			if (key.equals(CONCATENATED_ERROR_KEY)) {
				return concatenatedError;
			}
			if (key.equals(CONCATENATED_ERROR_ONE_ARG_KEY)) {
				return concatenatedErrorOneArg;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.ResourceBundle#getKeys()
		 */
		@Override
		public Enumeration getKeys() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	// German language
	private class MyResources_de extends MyResources {
		@Override
		public Object handleGetObject(String key) {
			if (key.equals(FATAL_ERROR_KEY)) {
				return fatalErrorDE;
			}
			if (key.equals(CONCATENATED_ERROR_KEY)) {
				return concatenatedErrorDE;
			}
			if (key.equals(CONCATENATED_ERROR_ONE_ARG_KEY)) {
				return concatenatedErrorDEOneArg;
			}
			return null;
		}
	}

}
