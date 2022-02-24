/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * NPE will be thrown out when undo extending library element
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a report and a library
 * <li>Add a label and a table in the library
 * <li>Report includes the library and extends library.label and library.table
 * <li>Undo extending library.table
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Follow the steps, ensure that no NPE will throw when doing the undo
 * operation.
 * </p>
 */
public class Regression_116558 extends BaseTestCase {

	private final static String INPUT = "regression_116558.xml"; //$NON-NLS-1$

	private final static String LIB = "regression_116558_lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

		copyResource_INPUT(LIB, LIB);
	}

	/**
	 * @throws DesignFileException
	 * @throws ExtendsException
	 * @throws NameException
	 * @throws ContentException
	 */
	public void test_regression_116558() throws DesignFileException, ExtendsException, ContentException, NameException {
		openDesign(INPUT);

		LibraryHandle lib = designHandle.getLibrary("regression_116558_lib"); //$NON-NLS-1$
		LabelHandle parentLabel = (LabelHandle) lib.findElement("label1"); //$NON-NLS-1$
		TableHandle parentTabel = (TableHandle) lib.findElement("tabel1"); //$NON-NLS-1$

		// extends label and table

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle childLabel = (LabelHandle) factory.newElementFrom(parentLabel, "childLabel1"); //$NON-NLS-1$
		TableHandle childTabel = (TableHandle) factory.newElementFrom(parentTabel, "childTabel1"); //$NON-NLS-1$

		designHandle.getBody().add(childLabel);
		designHandle.getBody().add(childTabel);

		assertNotNull(designHandle.findElement("childLabel1")); //$NON-NLS-1$
		assertNotNull(designHandle.findElement("childTabel1")); //$NON-NLS-1$

		// undo
		designHandle.getCommandStack().undo();
		assertNull(designHandle.findElement("childTabel1")); //$NON-NLS-1$
		assertNotNull(designHandle.findElement("childLabel1")); //$NON-NLS-1$

		// undo
		designHandle.getCommandStack().undo();
		assertNull(designHandle.findElement("childLabel1")); //$NON-NLS-1$
		assertNull(designHandle.findElement("childTabel1")); //$NON-NLS-1$

	}
}
