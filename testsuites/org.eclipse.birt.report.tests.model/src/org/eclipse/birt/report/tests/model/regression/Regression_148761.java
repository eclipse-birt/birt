/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Unable to copy a report item from template to report
 * <p>
 * Steps:
 * <p>
 * <ol>
 * <li>Create a template, add a Text item "t1" or others
 * <li>Create template item using "t1"
 * <li>Double click the template item and we get a report item again.
 * <li>We copy the item, paste to another report file.
 * </ol>
 * <p>
 * <b>Expected:</b>
 * <p>
 * the text item can be copied.
 * <p>
 * <b>Actual result:</b>
 * <p>
 * Nothing happened, not copied and no error msg.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Follow the steps, paste the text to a new report, make sure it is correctly
 * copy/pasted.
 * <p>
 */
public class Regression_148761 extends BaseTestCase {

	private final static String TEMPLATE = "regression_148761_template.xml"; //$NON-NLS-1$
	private final static String filename = "regression_148761.xml"; //$NON-NLS-1$
	private String tempTemplatePath, tempFilePath;

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( TEMPLATE , TEMPLATE );
		// copyInputToFile ( INPUT_FOLDER + "/" + TEMPLATE );
		tempTemplatePath = copyInputToFile(INPUT_FOLDER + "/" + TEMPLATE);
		tempFilePath = copyInputToFile(INPUT_FOLDER + "/" + filename);
		System.out.println("Template location: " + tempTemplatePath);
		System.out.println("File location: " + tempFilePath);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_148761() throws DesignFileException, SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);

		ReportDesignHandle template = session.openDesign(tempTemplatePath);

		// System.out.println (template);

		TextItemHandle text1 = (TextItemHandle) template.findElement("t1"); //$NON-NLS-1$

		// Create template item using "t1"

		TemplateReportItemHandle templateText1 = (TemplateReportItemHandle) text1
				.createTemplateElement("templateText1"); //$NON-NLS-1$
		IDesignElement copy = templateText1.copyDefaultElement();

		// Double click the template item and we get a report item again.

		templateText1.transformToReportItem((ReportItemHandle) copy.getHandle(template.getModule()));
		TextItemHandle text2 = (TextItemHandle) template.findElement("t1"); //$NON-NLS-1$

		// paste the text to a new report, make sure it is correctly
		// copy/pasted.
		openDesign(tempFilePath, false);

		ReportDesignHandle newDesignHandle = session.createDesign();
		newDesignHandle.getBody().paste(text2.copy().getHandle(design));

		TextItemHandle pastedText = (TextItemHandle) newDesignHandle.findElement("t1"); //$NON-NLS-1$

		assertNotNull(pastedText);
		assertEquals("sample text", pastedText.getContent()); //$NON-NLS-1$
	}
}
