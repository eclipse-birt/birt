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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests property binding related issues.
 */

public class PropertyBindingTest extends BaseTestCase {

	private static final String FILE_NAME = "PropertyBindingTest.xml"; //$NON-NLS-1$
	private static final String PROP_NAME = "pswd"; //$NON-NLS-1$

	/**
	 * Tests parser and properties.
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(FILE_NAME);
		assertNotNull(designHandle);

		testParser(designHandle);

	}

	/**
	 * Tests the parser for property binding both for normal openDesign and
	 * design.clone() action.
	 * 
	 * @param moduleHandle
	 */

	private void testParser(ModuleHandle moduleHandle) {
		List bindingList = moduleHandle.getListProperty(Module.PROPERTY_BINDINGS_PROP);
		assertEquals(4, bindingList.size());

		// test list and member values

		PropertyBinding binding = (PropertyBinding) bindingList.get(0);
		assertEquals("text", binding.getName()); //$NON-NLS-1$
		assertEquals(23, binding.getID().longValue());
		assertEquals("params[p1]", binding.getValue()); //$NON-NLS-1$

		binding = (PropertyBinding) bindingList.get(1);
		assertEquals("column", binding.getName()); //$NON-NLS-1$
		assertEquals(22, binding.getID().longValue());
		assertEquals("params[p2]", binding.getValue()); //$NON-NLS-1$

		binding = (PropertyBinding) bindingList.get(2);
		assertEquals(PROP_NAME, binding.getName());
		assertEquals(30, binding.getID().longValue());
		assertEquals("newPassword", binding.getValue()); //$NON-NLS-1$

		binding = (PropertyBinding) bindingList.get(3);
		assertEquals(PROP_NAME, binding.getName());
		assertEquals(32, binding.getID().longValue());
		assertEquals("unencryptedPassword", binding.getValue()); //$NON-NLS-1$

		// get the element based on the id and test getPropertyBinding method

		DesignElementHandle tempHandle = moduleHandle.getElementByID(23);
		assertNotNull(tempHandle);
		assertTrue(tempHandle instanceof LabelHandle);
		assertNotNull(tempHandle.getPropertyDefn("text")); //$NON-NLS-1$
		assertEquals("params[p1]", tempHandle.getPropertyBindingExpression("text").getStringExpression()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(bindingList.get(0), moduleHandle.getModule().findPropertyBinding(tempHandle.getElement(), "text")); //$NON-NLS-1$

		tempHandle = moduleHandle.getElementByID(22);
		assertNotNull(tempHandle);
		assertTrue(tempHandle instanceof CellHandle);
		assertNotNull(tempHandle.getPropertyDefn("column")); //$NON-NLS-1$
		assertEquals("params[p2]", tempHandle.getPropertyBinding("column")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(bindingList.get(1),
				moduleHandle.getModule().findPropertyBinding(tempHandle.getElement(), "column")); //$NON-NLS-1$

		tempHandle = moduleHandle.getElementByID(30);
		assertNotNull(tempHandle);
		assertTrue(tempHandle instanceof ExtendedItemHandle);
		assertNotNull(tempHandle.getPropertyDefn(PROP_NAME));
		assertEquals("newPassword", tempHandle.getPropertyBinding(PROP_NAME)); //$NON-NLS-1$
		assertEquals(bindingList.get(2),
				moduleHandle.getModule().findPropertyBinding(tempHandle.getElement(), PROP_NAME));
	}

	/**
	 * Tests setPropertyBinding method and writer.
	 * 
	 * @throws Exception
	 */

	public void testWriter() throws Exception {
		openDesign(FILE_NAME);
		assertNotNull(designHandle);

		// add a new table property binding

		TableHandle table = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$
		assertNotNull(table);
		table.setPropertyBinding(TableItem.BOOKMARK_PROP, "params[p]"); //$NON-NLS-1$

		// clear cell property binding

		CellHandle cell = (CellHandle) designHandle.getElementByID(22);
		assertNotNull(cell);
		cell.setPropertyBinding(CellHandle.COLUMN_PROP, (String) null);

		// update label property binding

		LabelHandle label = (LabelHandle) designHandle.getElementByID(23);
		assertNotNull(label);
		label.setPropertyBinding(LabelHandle.TEXT_PROP, new Expression("params[p3]", null)); //$NON-NLS-1$

		// set another label property binding with the same property

		label = (LabelHandle) designHandle.getElementByID(26);
		assertNotNull(label);
		label.setPropertyBinding(LabelHandle.TEXT_PROP, "params[p3]"); //$NON-NLS-1$

		// set the encrypted new value
		ExtendedItemHandle extended = (ExtendedItemHandle) designHandle.getElementByID(30);
		Expression expr = new Expression("setNewPassword", IExpressionType.CONSTANT);//$NON-NLS-1$
		extended.setPropertyBinding(PROP_NAME, expr);

		// add a binding for another extended item
		extended = (ExtendedItemHandle) designHandle.getElementByID(31);
		extended.setPropertyBinding(PROP_NAME, "createPassword"); //$NON-NLS-1$

		// test structure list validation for property binding

		try {
			PropertyBinding binding = new PropertyBinding();
			binding.setName(LabelHandle.TEXT_PROP);
			binding.setID(26);
			binding.setValue("params[p]"); //$NON-NLS-1$
			designHandle.getPropertyHandle(Module.PROPERTY_BINDINGS_PROP).addItem(binding);
			fail();
		} catch (SemanticException e) {
			assert e instanceof PropertyValueException;
			PropertyValueException exception = (PropertyValueException) e;
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, exception.getErrorCode());
			assertEquals(design, exception.getElement());
			assertEquals(Module.PROPERTY_BINDINGS_PROP, exception.getPropertyName());
		}

		// save and compare

		save();
		assertTrue(compareFile("PropertyBindingTest_golden.xml")); //$NON-NLS-1$

		// test exception

		table = designHandle.getElementFactory().newTableItem(null);
		try {
			table.setPropertyBinding(TableHandle.BOOKMARK_PROP, "params[p]"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_PROPERTY_BINDING_FORBIDDEN, e.getErrorCode());
		}
		designHandle.getBody().add(table);
		try {
			table.setPropertyBinding(LabelHandle.ACTION_PROP, "prams[p]"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_PROPERTY_NAME, e.getErrorCode());
		}

	}

	/**
	 * Tests the delete action and check whether the property bindings for the
	 * deleted element are dropped too.
	 * 
	 * @throws Exception
	 */

	public void testDelete() throws Exception {
		openDesign(FILE_NAME);
		assertNotNull(designHandle);

		LabelHandle label = (LabelHandle) designHandle.getElementByID(23);
		assertNotNull(label);
		label.setPropertyBinding(LabelHandle.BOOKMARK_PROP, new Expression("params[p]", null)); //$NON-NLS-1$

		label.drop();

		// save and compare

		save();
		assertTrue(compareFile("PropertyBindingTest_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the property bindings are still effective when the module is cloned.
	 * 
	 * @throws Exception
	 */

	public void testClone() throws Exception {
		openDesign(FILE_NAME);
		assertNotNull(designHandle);

		ReportDesignHandle copyHandle = (ReportDesignHandle) designHandle.copy().getHandle(null);
		assertNotNull(copyHandle);
		assertNotNull(copyHandle);

		testParser(copyHandle);

		// save and compare, the input and the output is the same

		save();
		assertTrue(compareFile("PropertyBindingTest_golden_2.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests get intrinsic value. If the expression type is javascript the value
	 * will not be encrypted.
	 * 
	 * @throws Exception
	 */
	public void testIntrinsicValue() throws Exception {
		openDesign("PropertyBindingTest_1.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		List bindingList = designHandle.getListProperty(Module.PROPERTY_BINDINGS_PROP);
		assertEquals(2, bindingList.size());

		PropertyBinding binding = (PropertyBinding) bindingList.get(0);

		assertEquals("test", binding.getName()); //$NON-NLS-1$
		assertEquals(30, binding.getID().longValue());
		assertEquals("setNewPassword", binding.getValue()); //$NON-NLS-1$

		Expression expression = binding.getExpressionProperty(PropertyBinding.VALUE_MEMBER);
		assertEquals(ExpressionType.CONSTANT, expression.getType());

		binding = (PropertyBinding) bindingList.get(1);

		assertEquals(PROP_NAME, binding.getName());
		assertEquals(30, binding.getID().longValue());
		assertEquals("test", binding.getValue()); //$NON-NLS-1$

		expression = binding.getExpressionProperty(PropertyBinding.VALUE_MEMBER);
		assertEquals(ExpressionType.JAVASCRIPT, expression.getType());

		designHandle.getPropertyHandle(Module.PROPERTY_BINDINGS_PROP).removeItem(binding);

		bindingList = designHandle.getListProperty(Module.PROPERTY_BINDINGS_PROP);
		assertEquals(1, bindingList.size());

		binding = new PropertyBinding();
		binding.setName(PROP_NAME);
		binding.setID(30);
		binding.setEncryption("base64"); //$NON-NLS-1$
		expression = new Expression("123456", //$NON-NLS-1$
				ExpressionType.JAVASCRIPT);
		binding.setProperty(PropertyBinding.VALUE_MEMBER, expression);
		designHandle.getPropertyHandle(Module.PROPERTY_BINDINGS_PROP).addItem(binding);

		bindingList = designHandle.getListProperty(Module.PROPERTY_BINDINGS_PROP);
		assertEquals(2, bindingList.size());
		binding = (PropertyBinding) bindingList.get(1);

		expression = binding.getExpressionProperty(PropertyBinding.VALUE_MEMBER);
		assertEquals("123456", expression.getStringExpression()); //$NON-NLS-1$
		assertEquals(ExpressionType.JAVASCRIPT, expression.getType());

		save();
		assertTrue(compareFile("PropertyBindingTest_1_golden.xml")); //$NON-NLS-1$
	}
}
