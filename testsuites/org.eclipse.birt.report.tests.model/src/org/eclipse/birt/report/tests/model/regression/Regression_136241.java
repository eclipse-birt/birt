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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.DocumentUtil;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: Preview template items in web viewer and pdf didn't get correct
 * effect.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create template items.
 * <li>Preview in Web Viewer and pdf.
 * </ol>
 * 
 * <b>Expected result:</b>
 * <p>
 * Same effect as in html.
 * <p>
 * <b>Actual result:</b>
 * <p>
 * In web viewer, see only blank box. In pdf, blank page.
 * </p>
 * Test description:
 * <p>
 * The issue is template description is not saved in Report document. Reassign
 * to MODEL, we test to see that model has written out the template element and
 * its corresponding definition during serialization.
 * 
 */
public class Regression_136241 extends BaseTestCase {

	private final static String OUTPUT = "regression_136241.out"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
	}

	protected void tearDown() throws Exception {
		super.tearDown();

	}

	/**
	 * @throws SemanticException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DesignFileException
	 */

	public void test_regression_136241()
			throws SemanticException, FileNotFoundException, IOException, DesignFileException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$
		designHandle.getBody().add(label);

		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) label
				.createTemplateElement("templateLabel"); //$NON-NLS-1$
		templateLabel.setDescription("template label description"); //$NON-NLS-1$

		// serialize the report.

		String TempFile = this.genOutputFile(OUTPUT);
		DocumentUtil.serialize(designHandle, new FileOutputStream(TempFile));

		// open the output, make sure the report template item and its
		// definition are written out.

		designHandle = session.openDesign(TempFile);

		TemplateReportItemHandle template = (TemplateReportItemHandle) designHandle.findElement("templateLabel"); //$NON-NLS-1$
		assertNotNull(template);

		TemplateParameterDefinition defn = designHandle.getModule()
				.findTemplateParameterDefinition("NewTemplateParameterDefinition"); //$NON-NLS-1$
		assertNotNull(defn);
		assertEquals("Label", defn.getAllowedType(designHandle.getModule())); //$NON-NLS-1$
		assertEquals("template label description", defn.getDescription(designHandle.getModule())); //$NON-NLS-1$

	}
}
