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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test parsing of Sort tag.
 */
public class SortingParserTest extends BaseTestCase {

	private final static String FILE_NAME = "SortingParserTest.xml"; //$NON-NLS-1$
	private final static String SEMANTIC_CHECK_FILE_NAME = "SortingParserTest_1.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE_NAME = "SortingParserTest_golden.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(FILE_NAME);
	}

	/**
	 * Test parser of the sorting tag.
	 */

	public void testParser() {
		TableItem table = (TableItem) design.findElement("My table"); //$NON-NLS-1$
		assertNotNull(table);

		ArrayList sorting = (ArrayList) table.getProperty(design, TableItem.SORT_PROP);
		assertEquals(2, sorting.size());

		SortKey sortEntry = (SortKey) sorting.get(0);
		assertEquals("age", sortEntry.getKey()); //$NON-NLS-1$
		assertEquals("asc", sortEntry.getDirection()); //$NON-NLS-1$

		ListItem list = (ListItem) design.findElement("My list"); //$NON-NLS-1$
		assertNotNull(table);

		sorting = (ArrayList) list.getProperty(design, TableItem.SORT_PROP);
		assertEquals(2, sorting.size());

	}

	/**
	 * Performs the semantic check test.
	 * 
	 * @throws DesignFileException if any syntax error found in design file.
	 */

	public void testSemanticCheck() throws DesignFileException {
		openDesign(SEMANTIC_CHECK_FILE_NAME);

		List errors = design.getErrorList();
		assertEquals(3, errors.size());

		int i = 0;
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				((ErrorDetail) errors.get(i++)).getErrorCode());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				((ErrorDetail) errors.get(i++)).getErrorCode());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				((ErrorDetail) errors.get(i++)).getErrorCode());

	}

	/**
	 * Test writer.
	 * 
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		save();
		assertTrue(compareFile(GOLDEN_FILE_NAME));
	}
}