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

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by StyleException.
 */

public class StyleExceptionTest extends BaseTestCase {

	/**
	 * Tests the error message.
	 *
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {
		DesignElement table = new TableItem();
		os = new ByteArrayOutputStream();
		table.setName("customerTable"); //$NON-NLS-1$

		StyleException error = new StyleException(table, "style1", StyleException.DESIGN_EXCEPTION_FORBIDDEN); //$NON-NLS-1$
		print(error);

		error = new StyleException(table, "style1", StyleException.DESIGN_EXCEPTION_NOT_FOUND); //$NON-NLS-1$
		print(error);

		os.close();

		assertTrue(compareFile("StyleExceptionError.golden.txt")); //$NON-NLS-1$

	}

	private void print(StyleException error) {
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

	/**
	 *
	 *
	 */
	public void testModelException() {
		ModelException e = new SemanticException(new Label(), "test error code"); //$NON-NLS-1$
		assertEquals(ModelException.PLUGIN_ID, e.getPluginId());
	}

}
