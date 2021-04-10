/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Details: Different preview on list
 * 
 * Step:
 * <ol>
 * <li>New a report design, create datasource and dataset.
 * <li>Add a list, bending the dataset, add a data with column of dataset into
 * detail row.
 * <li>Change the data to template item and input some word in Instructions.
 * <li>New another report design, create the same datasource and dataset.
 * <li>Copy the list from the 1st report to the 2nd report.
 * <li>Preview these report design
 * </ol>
 * <p>
 * <b>Actual result:</b>
 * <p>
 * It is find that the result of them are not the same. Please see the attached
 * images.
 * </p>
 * <b>Test description:</b>
 * <p>
 * The problem is that Model forget to copy the template definitions when doing
 * copy of an template report items. Test to ensure that template definitions
 * are also copied.
 * </p>
 */
public class Regression_132641 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 * @throws IOException
	 * 
	 */
	public void test_regression_132641() throws SemanticException, IOException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle1 = session.createDesign();

		ElementFactory factory = designHandle1.getElementFactory();
		LabelHandle label = factory.newLabel("label"); //$NON-NLS-1$
		label.setText("abc"); //$NON-NLS-1$
		designHandle1.getBody().add(label);

		// convert to template element.

		designHandle1.findElement("label").createTemplateElement("templateLabel"); //$NON-NLS-1$//$NON-NLS-2$
		IDesignElement copy = designHandle1.findElement("templateLabel") //$NON-NLS-1$
				.copy();

		ReportDesignHandle designHandle2 = session.createDesign();
		designHandle2.getBody().paste(copy);

		TemplateReportItemHandle templateLabel = (TemplateReportItemHandle) designHandle2.findElement("templateLabel"); //$NON-NLS-1$

		assertNotNull(templateLabel);
		assertEquals("NewTemplateParameterDefinition", //$NON-NLS-1$
				templateLabel.getStringProperty(TemplateReportItemHandle.REF_TEMPLATE_PARAMETER_PROP));

		// make sure the template definition is also copied.

		assertNotNull(designHandle2.getModule().findTemplateParameterDefinition("NewTemplateParameterDefinition")); //$NON-NLS-1$
	}
}
