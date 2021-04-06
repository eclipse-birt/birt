/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Set visibility for the item inside a table which extends from a library, its
 * setting doesn't work.
 * </p>
 * Test description:
 * <p>
 * Set visibility for a label inside a table which extends from a library, check
 * visibility of the label
 * </p>
 */

public class Regression_122077 extends BaseTestCase {

	private String filename = "Regression_122077.xml"; //$NON-NLS-1$
	private String LIBRARY = "Regression_122077_Lib.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);
		copyResource_INPUT(LIBRARY, LIBRARY);
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_122077() throws DesignFileException, SemanticException {
		openDesign(filename);

		TableHandle table = (TableHandle) designHandle.findElement("NewTable"); //$NON-NLS-1$
		assertNotNull(table);
		RowHandle row = (RowHandle) table.getHeader().get(0);
		CellHandle cell = (CellHandle) row.getCells().get(0);
		LabelHandle label = (LabelHandle) cell.getContent().get(0);

		assertNotNull(label);
		PropertyHandle propHandle = label.getPropertyHandle(ReportItem.VISIBILITY_PROP);

		HideRule structure = StructureFactory.createHideRule();
		propHandle.addItem(structure);

		Iterator iter = propHandle.iterator();
		HideRuleHandle structureHandle = (HideRuleHandle) iter.next();

		structureHandle.setExpression("expression"); //$NON-NLS-1$
		structureHandle.setFormat(DesignChoiceConstants.FORMAT_TYPE_PDF);

		assertEquals("expression", structureHandle.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_PDF, structureHandle.getFormat());
	}
}
