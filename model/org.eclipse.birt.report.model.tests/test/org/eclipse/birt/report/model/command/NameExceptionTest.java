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

import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by NameException.
 */

public class NameExceptionTest extends BaseTestCase {

	/**
	 * Tests the error message.
	 * 
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {

		DesignElement table = new TableItem();
		os = new ByteArrayOutputStream();

		table.setName("customerTable"); //$NON-NLS-1$

		NameException error = new NameException(table, "", //$NON-NLS-1$
				NameException.DESIGN_EXCEPTION_NAME_REQUIRED);
		print(error);

		error = new NameException(table, table.getName(), NameException.DESIGN_EXCEPTION_NAME_FORBIDDEN);
		print(error);

		error = new NameException(table, table.getName(), NameException.DESIGN_EXCEPTION_DUPLICATE);
		print(error);

		error = new NameException(table, table.getName(), NameException.DESIGN_EXCEPTION_HAS_REFERENCES);
		print(error);

		os.close();

		assertTrue(compareFile("NameExceptionError.golden.txt")); //$NON-NLS-1$

	}

	private void print(NameException error) {
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
