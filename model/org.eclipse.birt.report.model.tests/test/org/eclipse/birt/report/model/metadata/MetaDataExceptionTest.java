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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.api.ModelException;

/**
 * Test case for MetaDataException.
 */
public class MetaDataExceptionTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Check to see each error code has there description in the
	 * "MetaError.properties".
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */

	public void testCheckMetaErrorConsistency() throws IOException, IllegalArgumentException, IllegalAccessException {
		Properties props = new Properties();
		props.load(MetaDataException.class.getResourceAsStream(MetaDataException.ERROR_FILE));

		int PUBLIC_FINAL_STATIC = Modifier.PUBLIC | Modifier.FINAL | Modifier.STATIC;

		boolean success = true;
		Field[] fields = MetaDataException.class.getDeclaredFields();
		String errorCode = null;

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];

			if (PUBLIC_FINAL_STATIC == field.getModifiers()) {

				errorCode = (String) fields[i].get(null);
				if (errorCode.equalsIgnoreCase(ModelException.PLUGIN_ID))
					continue;

				if (!props.containsKey(errorCode)) {
					System.out.println(
							"MetaDataException ErrorCode: " + errorCode + " not described in 'MetaError.properties'."); //$NON-NLS-1$ //$NON-NLS-2$
					success = false;
				}
			}
		}

		assertTrue(success);
	}
}
