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

package org.eclipse.birt.report.model.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by UserPropertyException.
 */

public class UserPropertyExceptionTest extends BaseTestCase {
	/**
	 * Tests the error message.
	 * 
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {

		DesignElement table = new TableItem();
		os = new ByteArrayOutputStream();

		table.setName("customerTable"); //$NON-NLS-1$

		String propName = "userProp1"; //$NON-NLS-1$

		UserPropertyException error = new UserPropertyException(table, "", //$NON-NLS-1$
				UserPropertyException.DESIGN_EXCEPTION_NAME_REQUIRED);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_DUPLICATE_NAME);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_MISSING_CHOICES);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_INVALID_DISPLAY_ID);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_NOT_FOUND);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_USER_PROP_DISALLOWED);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_CHOICE_NAME_REQUIRED);
		print(error);

		error = new UserPropertyException(table, propName,
				UserPropertyException.DESIGN_EXCEPTION_CHOICE_VALUE_REQUIRED);
		print(error);

		error = new UserPropertyException(table, propName, UserPropertyException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE);
		print(error);

		os.close();

		assertTrue(compareFile("UserPropertyExceptionError.golden.txt")); //$NON-NLS-1$

	}

	private void print(UserPropertyException error) {
		String code = error.getErrorCode();
		try {
			os.write(code.getBytes());
			for (int i = code.length(); i < 60; i++)
				os.write(' ');
			os.write(error.getMessage().getBytes());
			os.write('\n');
		} catch (IOException e) {
			assert false;
		}
	}

}
