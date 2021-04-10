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
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: Predefined table header, table footer and table detail styles
 * don't work when previewed.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Add a predefined table header style.
 * <li>Create a table and bind it to a data set, edit it.
 * <li>Preview it and find that predefined table header style doesn't work.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Define a predefined table-header style, ensure that FactoryHandle can
 * retrieve the style property value.
 * </p>
 */
public class Regression_117834 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */

	public void test_regression_117834() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table", 1, 1, 1, 1); //$NON-NLS-1$
		designHandle.getBody().add(table);

		StyleHandle table_header = factory.newStyle("table-header"); //$NON-NLS-1$
		table_header.setFontStyle(DesignChoiceConstants.FONT_STYLE_ITALIC);
		designHandle.getStyles().add(table_header);

		// static prop value.

		RowHandle headerRow = (RowHandle) table.getHeader().get(0);
		assertEquals("italic", headerRow.getStringProperty(StyleHandle.FONT_STYLE_PROP)); //$NON-NLS-1$

		// factory prop value

		FactoryPropertyHandle factoryPropHandle = headerRow.getFactoryPropertyHandle(StyleHandle.FONT_STYLE_PROP);
		assertEquals("italic", factoryPropHandle.getStringValue()); //$NON-NLS-1$
	}
}
