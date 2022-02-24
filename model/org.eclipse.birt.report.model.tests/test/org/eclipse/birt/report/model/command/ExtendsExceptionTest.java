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

import org.eclipse.birt.report.model.api.command.CircularExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by ExtendsException.
 */

public class ExtendsExceptionTest extends BaseTestCase {

	/**
	 * Tests the error message.
	 *
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {

		DesignElement table = new TableItem();
		os = new ByteArrayOutputStream();
		table.setName("customerTable"); //$NON-NLS-1$

		DesignElement parent = new GridItem();
		parent.setName("parentGrid"); //$NON-NLS-1$

		String extendsName = "parentTable"; //$NON-NLS-1$

		ExtendsException error = new InvalidParentException(table, extendsName,
				InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND);
		print(error);

		error = new WrongTypeException(table, parent, WrongTypeException.DESIGN_EXCEPTION_WRONG_TYPE);
		print(error);

		error = new ExtendsForbiddenException(table, extendsName,
				ExtendsForbiddenException.DESIGN_EXCEPTION_CANT_EXTEND);
		print(error);

		error = new CircularExtendsException(table, extendsName, CircularExtendsException.DESIGN_EXCEPTION_SELF_EXTEND);
		print(error);

		error = new CircularExtendsException(table, parent, CircularExtendsException.DESIGN_EXCEPTION_CIRCULAR);
		print(error);

		error = new InvalidParentException(table, extendsName, InvalidParentException.DESIGN_EXCEPTION_UNNAMED_PARENT);
		print(error);

		error = new ExtendsForbiddenException(table, extendsName,
				ExtendsForbiddenException.DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT);
		print(error);

		os.close();

		assertTrue(compareFile("ExtendsExceptionError.golden.txt")); //$NON-NLS-1$

	}

	private void print(ExtendsException error) {
		String code = error.getErrorCode();
		try {
			os.write(code.getBytes());
			for (int i = code.length(); i < 60; i++) {
				os.write(' ');
			}
			os.write(error.getMessage().getBytes());
			os.write('\n');
		} catch (IOException e) {
			assert false;
		}
	}

}
