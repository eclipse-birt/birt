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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for class <code>TemplateCommand</code>.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #checkAdd( Object content, int slotID )}</td>
 * <td>Test when add a template parameter, at the same time add a template
 * definition.</td>
 * <td>Both template parameter and template definition are copied to the new
 * design file</td>
 * </tr>
 *
 *
 * </table>
 *
 */

public class TemplateCommandTest extends BaseTestCase {

	/**
	 * Template element in design file
	 */
	private DesignElement templateItem = null;
	/**
	 * List element in design file
	 */
	private DesignElement designElement = null;

	/**
	 * Input,output, golden file
	 */
	private static final String INPUT_FILE = "TemplateCommandTest.xml";//$NON-NLS-1$
	private static final String GOLEAN_FILE = "TemplateCommandTest_golden.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		getParam();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		templateItem = null;
		designElement = null;
	}

	/**
	 * Init template element and list element which contains a template element
	 *
	 * @throws Exception
	 */
	private void getParam() throws Exception {
		openDesign(INPUT_FILE);

		List list = designHandle.getBody().getContents();
		designElement = (DesignElement) ((DesignElementHandle) list.get(0)).getElement().clone();
		templateItem = (DesignElement) ((DesignElementHandle) list.get(1)).getElement().clone();
		designHandle.close();
	}

	/**
	 * Test when add a template parameter ,at the same time add a template
	 * definition. Test checkAdd( Object content, int slotID ) method
	 *
	 * @throws Exception
	 */

	public void testCheckAdd() throws Exception {
		createDesign();
		design.getVersionManager().setVersion(DesignSchemaConstants.REPORT_VERSION);

		ContentCommand command = new ContentCommand(design, new ContainerContext(design, ReportDesign.BODY_SLOT));
		command.add(designElement);
		command.add(templateItem);

		save();
		assertTrue(compareFile(GOLEAN_FILE));
	}

	/**
	 * Tests clear RefTemplateParameter property.
	 *
	 * Test Case:
	 *
	 * <ul>
	 * <li>Remove template definition from module if the definition is no longer
	 * refferenced when setting</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testClearRefTemplateParameterProp() throws Exception {
		createDesign();
		LabelHandle label = designHandle.getElementFactory().newLabel("aaa");//$NON-NLS-1$
		designHandle.getBody().add(label);

		// create template report item

		TemplateElementHandle templateElement;
		templateElement = label.createTemplateElement("Def1"); //$NON-NLS-1$
		assertNotNull(templateElement);

		TemplateParameterDefinition definition = (TemplateParameterDefinition) design
				.findTemplateParameterDefinition("NewTemplateParameterDefinition"); //$NON-NLS-1$
		assertNotNull(definition);

		// transform to reprot item

		((TemplateReportItemHandle) templateElement).transformToReportItem(label);

		// create another template report item, discard the ex definition

		templateElement = label.createTemplateElement("Def2"); //$NON-NLS-1$
		assertNotNull(templateElement);// create sucessfully

		// the discarded definition does not exist any more

		definition = (TemplateParameterDefinition) design
				.findTemplateParameterDefinition("NewTemplateParameterDefinition"); //$NON-NLS-1$
		assertNull(definition);

		// the new definition

		definition = (TemplateParameterDefinition) design
				.findTemplateParameterDefinition("NewTemplateParameterDefinition1"); //$NON-NLS-1$
		assertNotNull(definition);

		// transform to reprot item again

		((TemplateReportItemHandle) templateElement).transformToReportItem(label);

		// revert to a real report item

		label.revertToReportItem();

		definition = (TemplateParameterDefinition) design
				.findTemplateParameterDefinition("NewTemplateParameterDefinition"); //$NON-NLS-1$
		assertNull(definition);
		definition = (TemplateParameterDefinition) design
				.findTemplateParameterDefinition("NewTemplateParameterDefinition1"); //$NON-NLS-1$
		assertNull(definition);
	}
}
