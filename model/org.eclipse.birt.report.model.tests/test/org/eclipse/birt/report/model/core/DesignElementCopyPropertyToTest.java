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

package org.eclipse.birt.report.model.core;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the copy properties to another element.
 */

public class DesignElementCopyPropertyToTest extends BaseTestCase {

	/*
	 * @see BaseTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests copying property to target element.
	 *
	 * @throws Exception if any exception
	 */

	public void testCopyPropertiesTo() throws Exception {
		openDesign("DesignElementCopyPropertyToTest.xml"); //$NON-NLS-1$

		GridHandle gridHandle = (GridHandle) designHandle.findElement("grid1"); //$NON-NLS-1$
		assertNotNull(gridHandle);

		GridHandle newGridHandle = designHandle.getElementFactory().newGridItem("newGrid"); //$NON-NLS-1$

		gridHandle.copyPropertyTo(GridItem.STYLE_PROP, newGridHandle);
		Object obj = newGridHandle.getProperty(StyledElement.STYLE_PROP);
		assertNotNull(obj);
		assertEquals("style1", (String) obj); //$NON-NLS-1$
		assertEquals("style1", newGridHandle.getStyle().getName()); //$NON-NLS-1$

		gridHandle.copyPropertyTo(GridItem.DATA_SET_PROP, newGridHandle);
		assertEquals("MyDataSet", newGridHandle.getDataSet().getName()); //$NON-NLS-1$

		gridHandle.copyPropertyTo(GridItem.X_PROP, newGridHandle);
		assertEquals("12mm", newGridHandle.getX().getStringValue()); //$NON-NLS-1$

		gridHandle.copyPropertyTo(GridItem.PARAM_BINDINGS_PROP, newGridHandle);
		PropertyHandle propHandle = newGridHandle.getPropertyHandle(GridItem.PARAM_BINDINGS_PROP);
		Iterator iter = propHandle.iterator();
		assertEquals("param1", ((ParamBindingHandle) iter.next()).getParamName()); //$NON-NLS-1$
		assertEquals("param2", ((ParamBindingHandle) iter.next()).getParamName()); //$NON-NLS-1$
		assertEquals(null, iter.next());

		designHandle.getBody().add(newGridHandle);

		assertEquals("style1", newGridHandle.getStyle().getName()); //$NON-NLS-1$
		assertEquals("MyDataSet", newGridHandle.getDataSet().getName()); //$NON-NLS-1$
		assertEquals("12mm", newGridHandle.getX().getStringValue()); //$NON-NLS-1$
	}

	/**
	 * Tests copying property which is forbidden.
	 *
	 * @throws Exception if any exception
	 */

	public void testCopyingForbiddenProperty() throws Exception {
		openDesign("DesignElementCopyPropertyToTest.xml"); //$NON-NLS-1$

		GridHandle gridHandle = (GridHandle) designHandle.findElement("grid1"); //$NON-NLS-1$
		assertNotNull(gridHandle);

		GridHandle newGridHandle = designHandle.getElementFactory().newGridItem("newGrid"); //$NON-NLS-1$

		try {
			gridHandle.copyPropertyTo(GridItem.NAME_PROP, newGridHandle);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_PROPERTY_COPY_FORBIDDEN, e.getErrorCode());
			assertEquals("newGrid", newGridHandle.getName()); //$NON-NLS-1$
		}

		try {
			gridHandle.copyPropertyTo(GridItem.EXTENDS_PROP, newGridHandle);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_PROPERTY_COPY_FORBIDDEN, e.getErrorCode());
			assertEquals(null, newGridHandle.getExtends());
		}

	}
}
