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

package org.eclipse.birt.report.model.simpleapi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.MultiRowItem;
import org.eclipse.birt.report.model.api.extension.SimpleRowItem;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.simpleapi.ILabel;
import org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for simple API classes and methods.
 */

public class SimpleApiTest extends BaseTestCase {

	private final static String FILENAME = "DesignFileTest.xml"; //$NON-NLS-1$
	private final static String OUT_FILENAME = "DesignFileTest_out.xml"; //$NON-NLS-1$

	private final static String GOLDEN_FILENAME = "DesignFileTest_golden.xml"; //$NON-NLS-1$

	private IReportDesign simpleDesign;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BaseTestCase#setUp()
	 */

	protected void setUp() throws Exception {

		DesignEngine engine = new DesignEngine(null);

		InputStream ins = getResource(INPUT_FOLDER + FILENAME).openStream();
		simpleDesign = engine.openDesign(INPUT_FOLDER + FILENAME, ins, null);
	}

	/**
	 * @throws Exception
	 */

	public void testSimpleElement() throws Exception {

		ITable table = (ITable) simpleDesign.getReportElement("my table"); //$NON-NLS-1$
		assertNotNull(table);

		ILabel label = (ILabel) simpleDesign.getReportElement("my label"); //$NON-NLS-1$
		assertNotNull(label);

		IReportItem matrix = (IReportItem) simpleDesign.getReportElement("testMatrix"); //$NON-NLS-1$
		assertNotNull(matrix);
		assertTrue(matrix instanceof IMultiRowItem);
		assertTrue(matrix instanceof MultiRowItem);
		assertEquals("org.eclipse.birt.report.model.tests.matrix.Matrix", //$NON-NLS-1$
				matrix.getClass().getName());

		IReportItem box = (IReportItem) simpleDesign.getReportElement("testBox"); //$NON-NLS-1$
		assertNotNull(box);
		assertFalse(box instanceof IMultiRowItem);
		assertTrue(box instanceof SimpleRowItem);
		assertEquals("org.eclipse.birt.report.model.tests.box.Box", //$NON-NLS-1$
				box.getClass().getName());
	}

	/**
	 * gets the url of the resource.
	 * 
	 * @param name name of the resource
	 * @return the url of the resource
	 */

	protected URL getResource(String name) {
		return this.getClass().getResource(name);
	}

	/**
	 * Opens a design file, modifies it and then saves it.
	 * 
	 * @throws Exception
	 */

	public void testSaveDesign() throws Exception {
		ITable table = (ITable) simpleDesign.getReportElement("my table"); //$NON-NLS-1$
		assertNotNull(table);

		table.setName("my new table"); //$NON-NLS-1$

		simpleDesign.saveAs(getOutputFolder(OUT_FILENAME));

		assertTrue(compareFile(GOLDEN_FILENAME, OUT_FILENAME));
	}

	/**
	 * @param outputFile
	 * @return
	 * @throws IOException
	 */

	private String getOutputFolder(String outputFile) throws IOException {
		String folder = getTempFolder() + OUTPUT_FOLDER;
		File tmpFolder = new File(folder);
		if (!tmpFolder.exists())
			tmpFolder.mkdirs();

		return folder + outputFile;
	}

	/**
	 * Valid values are set for the structure handle. In such case, exceptions
	 * should be thrown.
	 * 
	 * @throws Exception
	 */

	public void testStructureHandle() throws Exception {
		ITable table = (ITable) simpleDesign.getReportElement("my table"); //$NON-NLS-1$
		assertNotNull(table);

		ISortCondition[] sorts = table.getSortConditions();
		ISortCondition sort = sorts[0];

		try {
			sort.setDirection("invalidValue"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

	}

	/**
	 * Test cases for undo/redo on script calls. All calls should not be able to
	 * undo/redo.
	 * 
	 * @throws Exception
	 */

	public void testUndoRedo() throws Exception {
		openDesign(FILENAME);

		simpleDesign = (IReportDesign) SimpleElementFactory.getInstance().getElement(designHandle);

		ITable table = (ITable) simpleDesign.getReportElement("my table"); //$NON-NLS-1$
		assertNotNull(table);

		table.setCaption("new caption"); //$NON-NLS-1$
		assertFalse(designHandle.getCommandStack().canUndo());

		simpleDesign.setTheme(null);
		assertFalse(designHandle.getCommandStack().canUndo());

		assertNull(designHandle.getProperty(ReportDesignHandle.THEME_PROP));

	}
}
