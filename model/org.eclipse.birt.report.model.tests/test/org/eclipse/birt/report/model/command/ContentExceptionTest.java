/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by ContentException.
 */

public class ContentExceptionTest extends BaseTestCase {

	/**
	 * Tests the error message.
	 * 
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {

		DesignElement table = new TableItem();
		table.setName("customerTable"); //$NON-NLS-1$
		os = new ByteArrayOutputStream();

		DesignElement row = new TableRow();

		ContentException error = new ContentException(table, TableItem.COLUMN_SLOT,
				ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, row, ContentException.DESIGN_EXCEPTION_WRONG_TYPE);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, ContentException.DESIGN_EXCEPTION_NOT_CONTAINER);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL);

		error = new ContentException(row, TableItem.COLUMN_SLOT, table, ContentException.DESIGN_EXCEPTION_RECURSIVE);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, ContentException.DESIGN_EXCEPTION_HAS_NO_CONTAINER);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, ContentException.DESIGN_EXCEPTION_MOVE_FORBIDDEN);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, new TemplateReportItem("test"), //$NON-NLS-1$
				ContentException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, row,
				ContentException.DESIGN_EXCEPTION_CONTENT_ALREADY_INSERTED);
		print(error);

		error = new ContentException(table, TableItem.COLUMN_SLOT, new TemplateReportItem("test"), //$NON-NLS-1$
				ContentException.DESIGN_EXCEPTION_INVALID_POSITION);
		print(error);

		// System.out.println(error.getLocalizedMessage());
		os.close();

		assertTrue(compareFile("ContentExceptionError.golden.txt")); //$NON-NLS-1$

	}

	private void print(ContentException error) {
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
