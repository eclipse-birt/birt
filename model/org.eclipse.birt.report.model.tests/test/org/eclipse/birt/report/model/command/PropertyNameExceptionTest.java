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

import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by PropertyNameException.
 */

public class PropertyNameExceptionTest extends BaseTestCase {

	/**
	 * Tests the error message.
	 *
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {

		DesignElement table = new TableItem();
		os = new ByteArrayOutputStream();
		table.setName("customerTable"); //$NON-NLS-1$

		Structure customColor = new CustomColor();
		customColor.setProperty(CustomColor.NAME_MEMBER, "Color1"); //$NON-NLS-1$

		PropertyNameException error = new PropertyNameException(table, GridItem.BOOKMARK_PROP);
		print(error);

		error = new PropertyNameException(table, customColor, CustomColor.COLOR_MEMBER);
		print(error);

		os.close();

		assertTrue(compareFile("PropertyNameExceptionError.golden.txt")); //$NON-NLS-1$

	}

	private void print(PropertyNameException error) {
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
