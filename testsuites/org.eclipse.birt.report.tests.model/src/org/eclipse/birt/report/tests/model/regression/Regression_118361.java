/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: [In eclipse 3.0.1]:After change the name of masterpage, when
 * save the changes. Throw out an error
 * <p>
 * Step to reproduce:
 * <ol>
 * <li>new a report
 * <li>In the masterpage view. Select general tab in property editor.
 * <li>change the Name, and save it
 * </ol>
 * <b>Expected results:</b>
 * <p>
 * the modification is saved without exception
 * <p>
 * <b>Actual results:</b>
 * <p>
 * throw out an error: Java Model Exception: Java Model Status [simple does not
 * exist]
 * </p>
 * Test description:
 * <p>
 * Rename the masterpage and save the report, make sure there won't be any
 * exception.
 * </p>
 */
public class Regression_118361 extends BaseTestCase {

	private final static String outFileName = "regression_118361.out";

	/**
	 * @throws ContentException
	 * @throws NameException
	 */

	public void test_regression_118361() throws ContentException, NameException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		SimpleMasterPageHandle page = factory.newSimpleMasterPage("oldName"); //$NON-NLS-1$

		designHandle.getMasterPages().add(page);

		MasterPageHandle pageHandle = designHandle.findMasterPage("oldName"); //$NON-NLS-1$
		try {
			pageHandle.setName("newName"); //$NON-NLS-1$

			// makeOutputDir( );
			// designHandle.saveAs( this.getFullQualifiedClassName( ) + "/" + OUTPUT_FOLDER
			// + "/" + "regression_118361.out" ); //$NON-NLS-1$
			String TempFile = this.genOutputFile(outFileName);
			designHandle.saveAs(TempFile);
		} catch (Exception e) {
			// if rename failed, the case failed.

			fail();
		}

	}
}
