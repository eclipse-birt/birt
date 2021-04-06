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

package org.eclipse.birt.report.model.core;

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.TabularLevel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the name helper.
 */
public class NameHelperTest extends BaseTestCase {

	private static final String FILE_NAME = "NameHelperTest.xml"; //$NON-NLS-1$
	private static final String VARIABLE_ELEMENT_FILE_NAME = "VariableElementNameHelperTest.xml"; //$NON-NLS-1$
	private static final String ADD_ELEMENT_TEST_FILE = "AddVariableElementNameTest.xml"; //$NON-NLS-1$

	/**
	 * 
	 * @throws Exception
	 */
	public void testParser() throws Exception {
		openDesign(FILE_NAME);
		INameHelper nameHelper = design.getNameHelper();

		// find element with unqiue name in general name scope,such as dimension
		Cube cube = (Cube) nameHelper.getNameSpace(Module.CUBE_NAME_SPACE).getElement("testCube"); //$NON-NLS-1$
		assertNotNull(cube);

		Dimension dimension = (Dimension) nameHelper.getNameSpace(Module.DIMENSION_NAME_SPACE)
				.getElement("testDimension"); //$NON-NLS-1$
		assertNotNull(dimension);

		// find element with unique name in general name scope, such as level
		Level level = (Level) dimension.getNameHelper().getNameSpace(Dimension.LEVEL_NAME_SPACE)
				.getElement("testLevel"); //$NON-NLS-1$
		assertNotNull(level);
		assertNull(nameHelper.getNameSpace(Module.CUBE_NAME_SPACE).getElement(level.getName()));

		// test name count
		assertEquals(2, dimension.getNameHelper().getNameSpace(Dimension.LEVEL_NAME_SPACE).getCount());
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testMakeUniqueName() throws Exception {
		openDesign(FILE_NAME);

		// generate a unique name for level if it has not
		Dimension dimension = design.findDimension("testDimension"); //$NON-NLS-1$
		Level level = new TabularLevel(null);
		dimension.makeUniqueName(level);
		assertNotNull(level.getName());

		// if name exists, then make a new name
		level = new TabularLevel("testLevel"); //$NON-NLS-1$
		dimension.makeUniqueName(level);
		assertEquals("testLevel1", level.getName()); //$NON-NLS-1$

		// if call module.makeUniqueName, if level can not find the container
		// dimension, then do nothing
		level = new TabularLevel(null);
		design.makeUniqueName(level);
		assertNull(level.getName());

		// if level can get dimension container, then the name will be not null;
		DesignElement hierarchy = design.findOLAPElement("testHierarchy"); //$NON-NLS-1$
		level = new TabularLevel(null);
		hierarchy.add(design, level, IHierarchyModel.LEVELS_PROP);
		design.makeUniqueName(level);
		assertNotNull(level.getName());

		// test new getUniqueName method
		level = new TabularLevel(null);
		hierarchy.add(design, level, IHierarchyModel.LEVELS_PROP);
		NameExecutor executor = new NameExecutor(design, level);
		assertTrue(executor.hasNamespace());
		String name = executor.getUniqueName("testLevel"); //$NON-NLS-1$
		assertEquals("testLevel2", name); //$NON-NLS-1$
		level.setName(name);
		assertEquals(name, executor.getUniqueName("NewTestLevel")); //$NON-NLS-1$
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void testResolve() throws Exception {
		openDesign(FILE_NAME);

		String propName = "level"; //$NON-NLS-1$
		DesignElementHandle testExtended = designHandle.findElement("testTable"); //$NON-NLS-1$
		ElementRefValue refValue = (ElementRefValue) testExtended.getElement().getProperty(design, propName);
		assertTrue(refValue.isResolved());
		assertEquals(refValue, design.getNameHelper().resolve(testExtended.getElement(), refValue.getName(),
				(PropertyDefn) testExtended.getPropertyDefn(propName), null));
		Dimension dimension = design.findDimension("testDimension"); //$NON-NLS-1$
		assertEquals(refValue, dimension.getNameHelper().resolve(testExtended.getElement(), refValue.getName(),
				(PropertyDefn) testExtended.getPropertyDefn(propName), null));
	}

	/**
	 * Tests the clear of name helper when the transaction stack is empty.
	 * 
	 * @throws Exception
	 */
	public void testClear() throws Exception {
		openDesign(FILE_NAME);

		ElementFactory factory = designHandle.getElementFactory();
		ActivityStack stack = design.getActivityStack();

		String labelName = "labelName"; //$NON-NLS-1$
		String paramName = "paramName"; //$NON-NLS-1$

		LabelHandle labelHandle = factory.newLabel(labelName);

		stack.startTrans(null);

		// this parameter is created and not inserted to design tree
		ParameterHandle paramHandle = factory.newScalarParameter(paramName);
		paramHandle.setHelpText("helpTest"); //$NON-NLS-1$
		// this label is inserted to the tree
		designHandle.getBody().add(labelHandle);

		stack.commit();

		// for original label is inserted to tree, its name is inserted to name
		// space and can not used again
		labelHandle = factory.newLabel(labelName);
		assertEquals(labelName + "1", labelHandle.getName()); //$NON-NLS-1$

		// for original parameter is not inserted to tree and when stack is
		// commit and transaction is empty, at this case the cached parameter
		// names are all cleared, so can use the original name
		paramHandle = factory.newScalarParameter(paramName);
		assertEquals(paramName, paramHandle.getName());
	}

	/**
	 * @throws Exception
	 */
	public void testMakeUniqueNameOnVariableElement() throws Exception {
		openDesign(VARIABLE_ELEMENT_FILE_NAME);

		ElementFactory factory = designHandle.getElementFactory();
		VariableElementHandle handle = factory.newVariableElement(null);

		design.makeUniqueName(handle.getElement());

		assertEquals("NewVariableElement", handle.getName()); //$NON-NLS-1$
		assertEquals("NewVariableElement", handle.getVariableName()); //$NON-NLS-1$

		handle = factory.newVariableElement("testVariable");//$NON-NLS-1$
		design.makeUniqueName(handle.getElement());
		assertEquals("testVariable1", handle.getName()); //$NON-NLS-1$
		assertEquals("testVariable1", handle.getVariableName()); //$NON-NLS-1$

	}

	/**
	 * Tests verified variable element name.
	 * 
	 * @throws Exception
	 */
	public void testAddVariableElementName() throws Exception {
		openDesign(ADD_ELEMENT_TEST_FILE);

		ElementFactory factory = designHandle.getElementFactory();
		VariableElementHandle handle = factory.newVariableElement(null);
		handle.setName("testVariable"); //$NON-NLS-1$

		// if the report design contains the same variable element name, the
		// variable element could not be added.
		try {
			designHandle.add(IReportDesignModel.PAGE_VARIABLES_PROP, handle);
			fail();
		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE, e.getErrorCode());
		}

		// the variable element could not be renamed as the same variable name
		// in the report design scope.
		handle = designHandle.getPageVariable("testVariable1"); //$NON-NLS-1$
		try {
			handle.setName("testVariable"); //$NON-NLS-1$
			fail();
		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE, e.getErrorCode());
		}

		// if the extended item contains the same variable element name, the
		// variable element could not be added.

		handle = factory.newVariableElement(null);
		handle.setName("testExtendedItemVariable"); //$NON-NLS-1$
		DesignElementHandle extendedItem = designHandle.findElement("action1"); //$NON-NLS-1$
		extendedItem.add("variables", handle); //$NON-NLS-1$
		assertEquals("testExtendedItemVariable", handle.getName()); //$NON-NLS-1$
		assertEquals("testExtendedItemVariable", handle.getVariableName()); //$NON-NLS-1$
		List list = extendedItem.getListProperty("variables"); //$NON-NLS-1$
		assertEquals(3, list.size());

		// the variable element could be renamed as the same variable name
		// in the extended scope.
		handle = (VariableElementHandle) list.get(1);
		assertEquals("testExtendedItemVariable1", handle.getName()); //$NON-NLS-1$
		handle.setName("testExtendedItemVariable");//$NON-NLS-1$
		assertEquals("testExtendedItemVariable", handle.getName()); //$NON-NLS-1$
		assertEquals("testExtendedItemVariable", handle.getVariableName()); //$NON-NLS-1$

		// the variable element could be renamed as the same variable name as
		// the variable element which locates in the report design.
		handle.setName("testVariable");//$NON-NLS-1$
		assertEquals("testVariable", handle.getName()); //$NON-NLS-1$
		assertEquals("testVariable", handle.getVariableName()); //$NON-NLS-1$

	}

	/**
	 * If the variable element with name is drop, its name will be removed from the
	 * name space.
	 * 
	 * @throws Exception
	 */
	public void testDeleteVariableElement() throws Exception {
		openDesign(ADD_ELEMENT_TEST_FILE);

		DesignElementHandle handle = designHandle.getPageVariable("testVariable"); //$NON-NLS-1$
		handle.drop();

		handle = designHandle.getPageVariable("testVariable"); //$NON-NLS-1$

		assertNull(handle);
	}
}
