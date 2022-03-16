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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests GroupElementHandle.
 */

public class GroupElementHandleTest extends BaseTestCase {

	ElementFactory elemFactory = null;

	/*
	 * @see TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		design = (ReportDesign) DesignEngine.newSession(ULocale.ENGLISH).createDesign().getModule();
		designHandle = (ReportDesignHandle) design.getHandle(design);

		elemFactory = new ElementFactory(design);
	}

	/**
	 * We fabricates 3 element( ElementA, ElementB and ElementC) and 5 property
	 * definitions(P1,P2,P3,P4,P5) and 2 user property definitions(UP1,UP2). Apply
	 * property on elEments as:
	 * <p>
	 * 1.
	 * <ul>
	 * <li>ElemnetA(P1,P2,P3,P4,P5).</li>
	 * <li>ElemnetB(P1,P2,P3).</li>
	 * <li>ElemnetC(P3,P4,P5).</li>
	 * </ul>
	 * So, the result should be: A,B,C share common property P3.
	 * <p>
	 * 2.
	 * <ul>
	 * <li>ElemnetA(P1).</li>
	 * <li>ElemnetB(P1).</li>
	 * <li>ElemnetC(P3).</li>
	 * </ul>
	 * So, the result should be: A,B,C doesn't share common property.
	 * <p>
	 * 3.
	 * <ul>
	 * <li>ElementA(P1)</li>
	 * <li>ElementB(P2)</li>
	 * <li>ElementC(P3)</li>
	 * </ul>
	 * So, the result should be: A,B,C doesn't share common property.
	 * <p>
	 * 4. consider user properties.
	 * <ul>
	 * <li>ElementA(P1,UP1,UP2)</li>
	 * <li>ElementB(P2,UP1)</li>
	 * </ul>
	 * So, the result should be: A,B share common property UP1.
	 * <p>
	 * Also, test to see the handle is mutable.
	 *
	 * @throws MetaDataException
	 */

	public void testGetCommonProperties() throws MetaDataException {
		// case1:
		// ElemnetA(P1,P2,P3,P4,P5)
		// ElemnetB(P1,P2,P3)
		// ElemnetC(P3,P4,P5)

		MockupElementDefn elemDefn1 = new MockupElementDefn();
		MockupElementDefn elemDefn2 = new MockupElementDefn();
		MockupElementDefn elemDefn3 = new MockupElementDefn();

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		PropertyType type = dd.getPropertyType(IPropertyType.STRING_TYPE);
		ElementPropertyDefn prop1 = new SystemPropertyDefn();
		prop1.setName("prop1"); //$NON-NLS-1$
		prop1.setType(type);
		ElementPropertyDefn prop2 = new SystemPropertyDefn();
		prop2.setName("prop2"); //$NON-NLS-1$
		prop2.setType(type);
		ElementPropertyDefn prop3 = new SystemPropertyDefn();
		prop3.setName("prop3"); //$NON-NLS-1$
		prop3.setType(type);
		ElementPropertyDefn prop4 = new SystemPropertyDefn();
		prop4.setName("prop4"); //$NON-NLS-1$
		prop4.setType(type);
		ElementPropertyDefn prop5 = new SystemPropertyDefn();
		prop5.setName("prop5"); //$NON-NLS-1$
		prop5.setType(type);

		elemDefn1.addProperty(prop1);
		elemDefn1.addProperty(prop2);
		elemDefn1.addProperty(prop3);
		elemDefn1.addProperty(prop4);
		elemDefn1.addProperty(prop5);

		elemDefn2.addProperty(prop1);
		elemDefn2.addProperty(prop2);
		elemDefn2.addProperty(prop3);

		elemDefn3.addProperty(prop3);
		elemDefn3.addProperty(prop4);
		elemDefn3.addProperty(prop5);

		elemDefn1.build();
		elemDefn2.build();
		elemDefn3.build();

		DesignElementHandle handle1 = new FakeElementHandle(design, new MockupDesignElement(elemDefn1));
		DesignElementHandle handle2 = new FakeElementHandle(design, new MockupDesignElement(elemDefn2));
		DesignElementHandle handle3 = new FakeElementHandle(design, new MockupDesignElement(elemDefn3));

		ArrayList<DesignElementHandle> elements = new ArrayList<>();
		elements.add(handle1);
		elements.add(handle2);
		elements.add(handle3);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertEquals(elements.size(), groupElementHandle.getElements().size());

		List commProperties = groupElementHandle.getCommonProperties();
		assertEquals(1, commProperties.size());
		assertEquals("prop3", ((PropertyDefn) commProperties.get(0)).getName()); //$NON-NLS-1$

		// case2:
		// ElemnetA(P1)
		// ElemnetB(P1)
		// ElemnetC(P3)

		elemDefn1 = new MockupElementDefn();
		elemDefn2 = new MockupElementDefn();
		elemDefn3 = new MockupElementDefn();

		elemDefn1.addProperty(prop1);
		elemDefn2.addProperty(prop1);
		elemDefn3.addProperty(prop3);

		elemDefn1.build();
		elemDefn2.build();
		elemDefn3.build();

		handle1 = new FakeElementHandle(design, new MockupDesignElement(elemDefn1));
		handle2 = new FakeElementHandle(design, new MockupDesignElement(elemDefn2));
		handle3 = new FakeElementHandle(design, new MockupDesignElement(elemDefn3));

		elements = new ArrayList<>();
		elements.add(handle1);
		elements.add(handle2);
		elements.add(handle3);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		commProperties = groupElementHandle.getCommonProperties();
		assertEquals(0, commProperties.size());

		// case3:
		// ElementA(P1)
		// ElementB(P2)
		// ElementC(P3)

		elemDefn1 = new MockupElementDefn();
		elemDefn2 = new MockupElementDefn();
		elemDefn3 = new MockupElementDefn();

		elemDefn1.addProperty(prop1);
		elemDefn2.addProperty(prop2);
		elemDefn3.addProperty(prop3);

		elemDefn1.build();
		elemDefn2.build();
		elemDefn3.build();

		handle1 = new FakeElementHandle(design, new MockupDesignElement(elemDefn1));
		handle2 = new FakeElementHandle(design, new MockupDesignElement(elemDefn2));
		handle3 = new FakeElementHandle(design, new MockupDesignElement(elemDefn3));

		elements = new ArrayList<>();
		elements.add(handle1);
		elements.add(handle2);
		elements.add(handle3);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		commProperties = groupElementHandle.getCommonProperties();
		assertEquals(0, commProperties.size());

		// case4:
		// ElementA(P1,UP1,UP2)
		// ElementB(P2,UP1)

		elemDefn1 = new MockupElementDefn();
		elemDefn2 = new MockupElementDefn();

		elemDefn1.addProperty(prop1);
		elemDefn2.addProperty(prop2);

		elemDefn1.build();
		elemDefn2.build();

		DesignElement element1 = new MockupDesignElement(elemDefn1);
		DesignElement element2 = new MockupDesignElement(elemDefn2);

		UserPropertyDefn up1 = new UserPropertyDefn();
		up1.setName("UP1"); //$NON-NLS-1$
		UserPropertyDefn up2 = new UserPropertyDefn();
		up2.setName("UP2"); //$NON-NLS-1$

		element1.addUserPropertyDefn(up1);
		element1.addUserPropertyDefn(up2);
		element2.addUserPropertyDefn(up1);

		handle1 = new FakeElementHandle(design, element1);
		handle2 = new FakeElementHandle(design, element2);

		elements = new ArrayList<>();
		elements.add(handle1);
		elements.add(handle2);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		commProperties = groupElementHandle.getCommonProperties();
		assertEquals(1, commProperties.size());

		// test to see the handle is mutable.
		elemDefn1 = new MockupElementDefn();
		elemDefn2 = new MockupElementDefn();

		elemDefn1.addProperty(prop1);
		elemDefn1.addProperty(prop2);
		elemDefn2.addProperty(prop1);

		elemDefn1.build();
		elemDefn2.build();

		handle1 = new FakeElementHandle(design, new MockupDesignElement(elemDefn1));
		handle2 = new FakeElementHandle(design, new MockupDesignElement(elemDefn2));

		elements = new ArrayList<>();
		elements.add(handle1);
		elements.add(handle2);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		commProperties = groupElementHandle.getCommonProperties();
		assertEquals(1, commProperties.size());

		// Check mutable handle feature, change the original list.
		// add a new element handle to the original list.

		elemDefn3 = new MockupElementDefn();

		elemDefn3.addProperty(prop3);
		elemDefn3.build();

		handle3 = new FakeElementHandle(design, new MockupDesignElement(elemDefn3));

		elements.add(handle3);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		commProperties = groupElementHandle.getCommonProperties();
		assertEquals(0, commProperties.size());

	}

	/**
	 * Finds the common properties with design element handles and slot handles.
	 *
	 * @throws MetaDataException
	 */

	public void testGetCommonPropertiesWithSlotHandle() throws MetaDataException {
		// with the slot handle

		ArrayList elements1 = new ArrayList();
		FreeFormHandle formHandle = elemFactory.newFreeForm("ex"); //$NON-NLS-1$
		elements1.add(formHandle);
		elements1.add(elemFactory.newOdaDataSource("ex", null)); //$NON-NLS-1$
		elements1.add(new SlotHandle(formHandle, FreeForm.REPORT_ITEMS_SLOT));

		GroupElementHandle groupElementHandle1 = new SimpleGroupElementHandle(designHandle, elements1);

		assertEquals(0, groupElementHandle1.getCommonProperties().size());
	}

	/**
	 * Tests isSameType() method in GroupElementHandle.
	 */

	public void testIsSameType() {
		// different
		ArrayList elements = new ArrayList();
		elements.add(elemFactory.newOdaDataSource("ex1", null)); //$NON-NLS-1$
		elements.add(elemFactory.newScriptDataSource("script1")); //$NON-NLS-1$

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertFalse(groupElementHandle.isSameType());

		// same
		ArrayList elements2 = new ArrayList();
		elements2.add(elemFactory.newOdaDataSource("ex", null)); //$NON-NLS-1$
		elements2.add(elemFactory.newOdaDataSource("ex", null)); //$NON-NLS-1$

		GroupElementHandle groupElementHandle2 = new SimpleGroupElementHandle(designHandle, elements2);

		assertTrue(groupElementHandle2.isSameType());

		// with the slot handle

		ArrayList elements3 = new ArrayList();
		FreeFormHandle formHandle = elemFactory.newFreeForm("ex"); //$NON-NLS-1$
		elements3.add(formHandle);
		elements3.add(elemFactory.newFreeForm("ex")); //$NON-NLS-1$
		elements3.add(new SlotHandle(formHandle, FreeForm.REPORT_ITEMS_SLOT));

		GroupElementHandle groupElementHandle3 = new SimpleGroupElementHandle(designHandle, elements3);

		assertFalse(groupElementHandle3.isSameType());
	}

	/**
	 * Tests set value function.
	 * <p>
	 * 1. set a property defined in rom.def.
	 * <p>
	 * 2. set a user property value.
	 * <p>
	 * 3. test
	 * {@link org.eclipse.birt.report.model.api.GroupElementHandle#getPropertyHandle(String)}
	 *
	 * @throws SemanticException
	 * @throws UserPropertyException
	 * @throws PropertyValueException
	 * @throws MetaDataException
	 */

	public void testSetValue()
			throws SemanticException, UserPropertyException, PropertyValueException, MetaDataException {
		// 1. set a property defined in rom.def.

		// Two kind of master page share property "Height"
		DesignElementHandle handle1 = elemFactory.newGraphicMasterPage("page1"); //$NON-NLS-1$
		DesignElementHandle handle2 = elemFactory.newSimpleMasterPage("page2"); //$NON-NLS-1$

		ArrayList elements = new ArrayList();
		elements.add(handle1);
		elements.add(handle2);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		// success.

		groupElementHandle.setStringProperty(MasterPage.TYPE_PROP, DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		groupElementHandle.setStringProperty(MasterPage.HEIGHT_PROP, "12pt"); //$NON-NLS-1$

		assertEquals("12pt", handle1.getStringProperty(MasterPage.HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("12pt", handle2.getStringProperty(MasterPage.HEIGHT_PROP)); //$NON-NLS-1$

		groupElementHandle.clearProperty(MasterPage.HEIGHT_PROP);
		assertNull(handle1.getStringProperty(MasterPage.HEIGHT_PROP));
		assertNull(handle2.getStringProperty(MasterPage.HEIGHT_PROP));

		// 2. set a user property value.

		UserPropertyDefn up1 = new UserPropertyDefn();
		up1.setName("UP1"); //$NON-NLS-1$
		up1.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.INTEGER_TYPE));

		handle1.addUserPropertyDefn(up1);
		handle2.addUserPropertyDefn(up1);

		groupElementHandle.setProperty("UP1", "123"); //$NON-NLS-1$//$NON-NLS-2$

		assertEquals("123", handle1.getStringProperty("UP1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("123", handle2.getStringProperty("UP1")); //$NON-NLS-1$ //$NON-NLS-2$

		// 3. test getPropertyHandle.

		// "height" is shared
		GroupPropertyHandle propHandle = groupElementHandle.getPropertyHandle(MasterPage.HEIGHT_PROP);
		assertNotNull(propHandle);

		// "columns" is not shared
		propHandle = groupElementHandle.getPropertyHandle(SimpleMasterPage.HEADER_HEIGHT_PROP);
		assertNull(propHandle);

	}

	/**
	 * Test clear all properties.
	 *
	 * @throws SemanticException
	 *
	 */
	public void testClearAllProperties() throws SemanticException {
		ArrayList elements = new ArrayList();

		LabelHandle element1 = elemFactory.newLabel("label"); //$NON-NLS-1$
		TextItemHandle element2 = elemFactory.newTextItem("text"); //$NON-NLS-1$

		element1.setComments("New ReportItem"); //$NON-NLS-1$
		element1.setHeight("12pt"); //$NON-NLS-1$
		element1.setWidth("24pt"); //$NON-NLS-1$
		element1.setAction(new Action());

		element2.setComments("New ReportItem"); //$NON-NLS-1$
		element2.setHeight("12pt"); //$NON-NLS-1$
		element2.setWidth("24mm"); //$NON-NLS-1$
		element2.setContent("New Text"); //$NON-NLS-1$

		elements.add(element2);
		elements.add(element1);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertEquals("12pt", groupElementHandle //$NON-NLS-1$
				.getStringProperty(ReportItemHandle.HEIGHT_PROP));
		assertEquals("New ReportItem", groupElementHandle //$NON-NLS-1$
				.getStringProperty(ReportItemHandle.COMMENTS_PROP));
		assertNull(groupElementHandle.getStringProperty(ReportItemHandle.WIDTH_PROP));
		assertNull(groupElementHandle.getStringProperty(TextItemHandle.CONTENT_PROP));

		groupElementHandle.clearLocalProperties();

		// common properties are cleared.

		assertNull(groupElementHandle.getStringProperty(ReportItemHandle.HEIGHT_PROP));
		assertNull(groupElementHandle.getStringProperty(ReportItemHandle.COMMENTS_PROP));

		assertEquals("New Text", element2.getContent()); //$NON-NLS-1$
		assertNotNull(element1.getActionHandle());

		assertEquals("New Text", element2.getContent()); //$NON-NLS-1$
		assertNotNull(element1.getActionHandle());

		// 2. extends property shouldn't be cleared.

		LabelHandle baseLabel1 = elemFactory.newLabel("BaseLabel1"); //$NON-NLS-1$
		LabelHandle baseLabel2 = elemFactory.newLabel("BaseLabel2"); //$NON-NLS-1$

		designHandle.getComponents().add(baseLabel1);
		designHandle.getComponents().add(baseLabel2);

		LabelHandle label1 = (LabelHandle) elemFactory.newElementFrom(baseLabel1, "Label1"); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) elemFactory.newElementFrom(baseLabel2, "Label2"); //$NON-NLS-1$

		elements = new ArrayList();
		elements.add(label1);
		elements.add(label2);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		groupElementHandle.clearLocalProperties();
		assertNotNull(label1.getExtends());
		assertNotNull(label2.getExtends());

		// The name of masterPage is required .

		DesignElementHandle masterPage1 = elemFactory.newGraphicMasterPage("page1"); //$NON-NLS-1$
		designHandle.getMasterPages().add(masterPage1);
		elements.clear();
		elements.add(masterPage1);
		elements.add(label1);
		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		groupElementHandle.clearLocalProperties();

		// test extended item

		DesignElementHandle parentExtendedItem = elemFactory.newExtendedItem("parent", "TestingMatrix"); //$NON-NLS-1$ //$NON-NLS-2$
		designHandle.getComponents().add(parentExtendedItem);
		DesignElementHandle childExtendedItem = elemFactory.newElementFrom(parentExtendedItem, "child"); //$NON-NLS-1$
		designHandle.getBody().add(childExtendedItem);

		assertEquals(new Integer(0), parentExtendedItem.getProperty("xScale")); //$NON-NLS-1$
		assertEquals(new Integer(0), childExtendedItem.getProperty("xScale")); //$NON-NLS-1$

		elements.clear();
		elements.add(childExtendedItem);
		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		childExtendedItem.setProperty("xScale", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(new Integer(0), parentExtendedItem.getProperty("xScale")); //$NON-NLS-1$
		assertEquals(new Integer(3), childExtendedItem.getProperty("xScale")); //$NON-NLS-1$

		assertTrue(groupElementHandle.hasLocalPropertiesForExtendedElements());
		groupElementHandle.clearLocalProperties();

		((ExtendedItemHandle) childExtendedItem).loadExtendedElement();
		assertEquals(new Integer(0), parentExtendedItem.getProperty("xScale")); //$NON-NLS-1$
		assertEquals(new Integer(0), childExtendedItem.getProperty("xScale")); //$NON-NLS-1$

		design.getActivityStack().undo();

		((ExtendedItemHandle) childExtendedItem).loadExtendedElement();
		assertEquals(new Integer(0), parentExtendedItem.getProperty("xScale")); //$NON-NLS-1$
		assertEquals(new Integer(3), childExtendedItem.getProperty("xScale")); //$NON-NLS-1$

		design.getActivityStack().redo();

		((ExtendedItemHandle) childExtendedItem).loadExtendedElement();
		assertEquals(new Integer(0), parentExtendedItem.getProperty("xScale")); //$NON-NLS-1$
		assertEquals(new Integer(0), childExtendedItem.getProperty("xScale")); //$NON-NLS-1$
	}

	/**
	 * test isExtendedElements().
	 *
	 * @throws NameException
	 * @throws ContentException
	 * @throws ExtendsException
	 *
	 */

	public void testIsExtendedElements() throws ContentException, NameException, ExtendsException {
		ArrayList elements = new ArrayList();

		LabelHandle baseLabel = elemFactory.newLabel("baseLabel"); //$NON-NLS-1$
		TextItemHandle baseText = elemFactory.newTextItem("baseText"); //$NON-NLS-1$

		designHandle.getComponents().add(baseLabel);
		designHandle.getComponents().add(baseText);

		LabelHandle element1 = elemFactory.newLabel("label"); //$NON-NLS-1$
		TextItemHandle element2 = elemFactory.newTextItem("text"); //$NON-NLS-1$

		element1.setExtends(baseLabel);
		element2.setExtends(baseText);

		elements.add(element2);
		elements.add(element1);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertTrue(groupElementHandle.isExtendedElements());

		element2.setExtends(null);

		assertFalse(groupElementHandle.isExtendedElements());
	}

	/**
	 * Tests 'hasLocalPropertiesForExtendedElements' method.
	 * <ul>
	 * <li>Label has 'name' properties, can't enable restore.</li>
	 * <li>Get label without any property inside grid element.</li>
	 * <li>Modify one property of label and 'restore' button should be enabled.</li>
	 * <li>Modify one property of label in grid and 'restore' button should be
	 * enabled.</li>
	 * </ul>
	 *
	 * @throws Exception any exception
	 */

	public void testExtendsItemHasLocalProperties() throws Exception {
		openDesign("GroupElementHandleTest.xml"); //$NON-NLS-1$

		// Label has 'name' properties, needn't restore.

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("aa");//$NON-NLS-1$

		List<DesignElementHandle> elements = new ArrayList<>();
		elements.add(labelHandle);
		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		assertFalse(groupElementHandle.hasLocalPropertiesForExtendedElements());

		// Modify one property of label and 'restore' button should be enabled.

		labelHandle.setText("test aa");//$NON-NLS-1$
		assertTrue(groupElementHandle.hasLocalPropertiesForExtendedElements());

		// Get label without any property inside grid element

		labelHandle = (LabelHandle) designHandle.getElementByID(20);
		elements.clear();
		elements.add(labelHandle);
		assertFalse(groupElementHandle.hasLocalPropertiesForExtendedElements());

		// Modify one property of label in grid and 'restore' button should be
		// enabled.

		labelHandle.setStringProperty("text", "liblabeltext");//$NON-NLS-1$//$NON-NLS-2$
		assertTrue(groupElementHandle.hasLocalPropertiesForExtendedElements());

		// tests cube element which extends cube from library.
		CubeHandle cube = designHandle.findCube("Data Cube"); //$NON-NLS-1$
		elements.clear();
		elements.add(cube);
		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		// TODO see (T59677) and commit dbe22fcbcb8c910ec066eeb531a5e4a6af1a4d18.
		// comments test case now.
		// assertFalse( groupElementHandle.hasLocalPropertiesForExtendedElements( ) );

	}

	/**
	 * @throws SemanticException
	 *
	 *
	 */

	public void testHasLocalProperties() throws SemanticException {
		ArrayList elements = new ArrayList();

		LabelHandle element1 = elemFactory.newLabel("label1"); //$NON-NLS-1$
		LabelHandle element2 = elemFactory.newLabel("label2"); //$NON-NLS-1$

		element1.setText("new Label1"); //$NON-NLS-1$

		TextItemHandle element3 = elemFactory.newTextItem("text"); //$NON-NLS-1$

		elements.add(element1);
		elements.add(element2);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		// not parents.

		assertFalse(groupElementHandle.hasLocalPropertiesForExtendedElements());

		LabelHandle baseLabel = elemFactory.newLabel("baseLabel"); //$NON-NLS-1$
		designHandle.getComponents().add(baseLabel);

		element1.setExtends(baseLabel);
		element2.setExtends(baseLabel);

		// same type and both has parent and has local property.

		assertTrue(groupElementHandle.hasLocalPropertiesForExtendedElements());

		elements.add(element3);

		// Not the same type.
		assertFalse(groupElementHandle.hasLocalPropertiesForExtendedElements());

	}

	/**
	 * Test propertyIterator().`
	 *
	 * @throws SemanticException
	 */

	public void testGetPropertyIterator() throws SemanticException {
		// 1. String property type.
		ArrayList elements = new ArrayList();

		LabelHandle element1 = elemFactory.newLabel("label"); //$NON-NLS-1$
		TextItemHandle element2 = elemFactory.newTextItem("text"); //$NON-NLS-1$

		elements.add(element2);
		elements.add(element1);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		Iterator iter = groupElementHandle.propertyIterator();
		for (; iter.hasNext();) {
			GroupPropertyHandle propHandle = (GroupPropertyHandle) iter.next();

			IElementPropertyDefn propDefn = propHandle.getPropertyDefn();
			if (ReportItem.HEIGHT_PROP.equalsIgnoreCase(propDefn.getName())) {
				propHandle.setStringValue("12pt"); //$NON-NLS-1$
				assertEquals("12pt", propHandle.getStringValue()); //$NON-NLS-1$
				propHandle.clearValue();
			}
		}
	}

	/**
	 * Test visiblePropertyIterator().
	 *
	 * @throws SemanticException
	 */

	public void testGetVisiblePropertyIterator() throws SemanticException {
		// 1. String property type.

		ArrayList elements = new ArrayList();

		// bookmark and toc are all invisible

		LabelHandle element1 = elemFactory.newLabel("label"); //$NON-NLS-1$
		TextItemHandle element2 = elemFactory.newTextItem("text"); //$NON-NLS-1$

		elements.add(element2);
		elements.add(element1);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		Iterator iter = groupElementHandle.visiblePropertyIterator();
		for (; iter.hasNext();) {
			GroupPropertyHandle propHandle = (GroupPropertyHandle) iter.next();
			assertTrue(propHandle.isVisible());
		}

	}

	/**
	 * Test shareSameValue().
	 *
	 * @throws SemanticException
	 */

	public void testShareSameValue() throws SemanticException {
		// the property is not

		ArrayList elements = new ArrayList();

		LabelHandle label1 = elemFactory.newLabel("Label1"); //$NON-NLS-1$
		LabelHandle label2 = elemFactory.newLabel("Label2"); //$NON-NLS-1$

		elements.add(label1);
		elements.add(label2);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertFalse(groupElementHandle.shareSameValue("not-defined-prop")); //$NON-NLS-1$

		label1.setBookmark("bookmark1"); //$NON-NLS-1$
		label2.setBookmark("bookmark1"); //$NON-NLS-1$

		assertTrue(groupElementHandle.shareSameValue(Label.BOOKMARK_PROP));
		assertEquals("bookmark1", groupElementHandle.getStringProperty(Label.BOOKMARK_PROP)); //$NON-NLS-1$

		label1.setBookmark(null);
		label2.setBookmark(null);

		assertTrue(groupElementHandle.shareSameValue(Label.BOOKMARK_PROP));
		assertEquals(null, groupElementHandle.getStringProperty(Label.BOOKMARK_PROP));

		label1.setBookmark(null);
		label2.setBookmark("bookmark2"); //$NON-NLS-1$

		assertFalse(groupElementHandle.shareSameValue(Label.BOOKMARK_PROP));
		assertEquals(null, groupElementHandle.getStringProperty(Label.BOOKMARK_PROP));
	}

	/**
	 * Test getStringValue().
	 *
	 * @throws SemanticException
	 */

	public void testGetStringValue() throws SemanticException {

		// 1. String property type.

		ArrayList elements = new ArrayList();

		LineHandle element1 = elemFactory.newLineItem("item1"); //$NON-NLS-1$
		LineHandle element2 = elemFactory.newLineItem("item2"); //$NON-NLS-1$
		LineHandle element3 = elemFactory.newLineItem("item3"); //$NON-NLS-1$

		elements.add(element1);
		elements.add(element2);
		elements.add(element3);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		// same value.

		element1.setProperty(DataSource.COMMENTS_PROP, "Hi, Rock"); //$NON-NLS-1$
		element2.setProperty(DataSource.COMMENTS_PROP, "Hi, Rock"); //$NON-NLS-1$
		element3.setProperty(DataSource.COMMENTS_PROP, "Hi, Rock"); //$NON-NLS-1$

		assertEquals("Hi, Rock", groupElementHandle.getStringProperty(DataSource.COMMENTS_PROP)); //$NON-NLS-1$

		// different value.

		element2.setProperty(DataSource.COMMENTS_PROP, "Hi, Ivy"); //$NON-NLS-1$
		assertEquals(null, groupElementHandle.getStringProperty(DataSource.COMMENTS_PROP));

		// values are all unset.

		element1.setProperty(DataSource.COMMENTS_PROP, null);
		element2.setProperty(DataSource.COMMENTS_PROP, null);
		element3.setProperty(DataSource.COMMENTS_PROP, null);
		assertEquals(null, groupElementHandle.getStringProperty(DataSource.COMMENTS_PROP));

		// 2. Dimension property type.

		elements = new ArrayList();

		LabelHandle label1 = elemFactory.newLabel("Label1"); //$NON-NLS-1$
		LabelHandle label2 = elemFactory.newLabel("Label2"); //$NON-NLS-1$

		elements.add(label1);
		elements.add(label2);

		// same value.
		label1.setX("123.123mm"); //$NON-NLS-1$
		label2.setX("123.123mm"); //$NON-NLS-1$

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		assertEquals("123.123mm", groupElementHandle.getStringProperty(Label.X_PROP)); //$NON-NLS-1$

		// different value.
		label1.setX("123.123mm"); //$NON-NLS-1$
		label2.setX("111.123mm"); //$NON-NLS-1$

		assertEquals(null, groupElementHandle.getStringProperty(Label.X_PROP));

	}

	/**
	 * Tests getting report design and its handle from a group element handle.
	 *
	 * @throws SemanticException
	 */

	public void testGetDesign() throws SemanticException {
		DesignElementHandle handle1 = elemFactory.newGraphicMasterPage("page1"); //$NON-NLS-1$
		DesignElementHandle handle2 = elemFactory.newSimpleMasterPage("page2"); //$NON-NLS-1$

		ArrayList elements = new ArrayList();
		elements.add(handle1);
		elements.add(handle2);

		GroupElementHandle groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertEquals(design, groupElementHandle.getModule());
		assertEquals(designHandle, groupElementHandle.getModuleHandle());

	}

	/**
	 * Test visiblePropertyIterator().
	 *
	 * @throws SemanticException
	 */

	public void testPropertyVisibilities() throws SemanticException {
		// 1. String property type.

		ArrayList elements = new ArrayList();

		// bookmark and toc are all invisible

		DataItemHandle element1 = elemFactory.newDataItem("data1");//$NON-NLS-1$
		TextItemHandle element2 = elemFactory.newTextItem("text"); //$NON-NLS-1$
		LabelHandle element3 = elemFactory.newLabel("label1"); //$NON-NLS-1$

		elements.add(element1);
		elements.add(element2);
		elements.add(element3);

		ReportDesignHandle designHandle = (ReportDesignHandle) design.getHandle(design);

		GroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertTrue(groupHandle.getPropertyHandle(DesignElementHandle.NAME_PROP).isVisible());
		assertTrue(groupHandle.getPropertyHandle(ReportItem.HEIGHT_PROP).isVisible());
		assertFalse(groupHandle.getPropertyHandle(ReportItem.PROPERTY_MASKS_PROP).isVisible());
		assertFalse(groupHandle.getPropertyHandle(ReportItem.DATA_SET_PROP).isVisible());
		assertFalse(groupHandle.getPropertyHandle(ReportItem.X_PROP).isVisible());
	}

	/**
	 * @throws SemanticException
	 */
	public void testGetDisplayValue() throws SemanticException {

		SessionHandle session = DesignEngine.newSession(ULocale.GERMAN);
		ReportDesignHandle designHandle = session.createDesign();

		ArrayList elements = new ArrayList();

		ElementFactory factory = designHandle.getElementFactory();
		SimpleMasterPageHandle page1 = factory.newSimpleMasterPage("page1"); //$NON-NLS-1$
		SimpleMasterPageHandle page2 = factory.newSimpleMasterPage("page2"); //$NON-NLS-1$

		elements.add(page1);
		elements.add(page2);

		GroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, elements);

		GroupPropertyHandle groupPropertyHandle = groupHandle
				.getPropertyHandle(SimpleMasterPageHandle.HEADER_HEIGHT_PROP);
		groupPropertyHandle.setValue("500.0"); //$NON-NLS-1$

		assertEquals("5.000in", groupPropertyHandle.getDisplayValue()); //$NON-NLS-1$
		assertEquals("5000in", groupPropertyHandle.getStringValue()); //$NON-NLS-1$

	}

	/**
	 * Test isPropertyReadOnly() & isPropertyVisible().
	 *
	 * @throws SemanticException
	 */

	public void testIsPropertyReadOnlyAndIsPropertyVisible() throws SemanticException {
		ArrayList elements = new ArrayList();

		// bookmark and toc are all invisible

		SimpleMasterPageHandle element = elemFactory.newSimpleMasterPage("my page");//$NON-NLS-1$
		LabelHandle element1 = elemFactory.newLabel("label1"); //$NON-NLS-1$
		element.addElement(element1, SimpleMasterPage.PAGE_HEADER_SLOT);

		elements.add(element1);
		GroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertTrue(!groupHandle.getPropertyHandle(ReportItem.TOC_PROP).isVisible());
		assertTrue(!groupHandle.getPropertyHandle(ReportItem.BOOKMARK_PROP).isVisible());
		assertTrue(!groupHandle.getPropertyHandle(Style.PAGE_BREAK_AFTER_PROP).isVisible());
		assertTrue(!groupHandle.getPropertyHandle(Style.PAGE_BREAK_BEFORE_PROP).isVisible());

		assertTrue(groupHandle.getPropertyHandle(ReportItem.TOC_PROP).isReadOnly());
		assertTrue(groupHandle.getPropertyHandle(ReportItem.BOOKMARK_PROP).isReadOnly());
		assertTrue(groupHandle.getPropertyHandle(Style.PAGE_BREAK_AFTER_PROP).isReadOnly());
		assertTrue(groupHandle.getPropertyHandle(Style.PAGE_BREAK_BEFORE_PROP).isReadOnly());

		// drop is only visible for cells in group
		elements.clear();
		TableHandle table = elemFactory.newTableItem("table");
		RowHandle row = elemFactory.newTableRow();
		CellHandle cell = elemFactory.newCell();
		row.addElement(cell, RowHandle.CONTENT_SLOT);
		elements.add(cell);
		groupHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertTrue(!groupHandle.getPropertyHandle(CellHandle.DROP_PROP).isVisible());
		assertTrue(groupHandle.getPropertyHandle(CellHandle.DROP_PROP).isReadOnly());
	}

	/**
	 * Test getDisplayProperty().
	 *
	 * @throws Exception
	 */

	public void testGetDisplayProperty() throws Exception {
		createDesign(TEST_LOCALE);
		elemFactory = new ElementFactory(design);

		ArrayList elements = new ArrayList();

		LabelHandle element1 = elemFactory.newLabel("label1"); //$NON-NLS-1$
		designHandle.addElement(element1, IReportDesignModel.BODY_SLOT);

		elements.add(element1);
		GroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, elements);

		assertEquals("\u9ed1\u8272", groupHandle //$NON-NLS-1$
				.getDisplayProperty(IStyleModel.COLOR_PROP));
	}

	static class FakeElementHandle extends DesignElementHandle {

		DesignElement element = null;

		public FakeElementHandle(ReportDesign design, DesignElement element) {
			super(design);
			this.element = element;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getElement()
		 */
		@Override
		public DesignElement getElement() {
			return this.element;
		}
	}

	static class MockupElementDefn extends ElementDefn {

		public MockupElementDefn() {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.metadata.ElementDefn#build()
		 */

		@Override
		protected void build() throws MetaDataException {
			// cached property definitions defined on parents

			ElementDefn tmpDefn = this;
			while (tmpDefn != null) {
				List<IElementPropertyDefn> props = tmpDefn.getLocalProperties();
				for (int i = 0; i < props.size(); i++) {
					IElementPropertyDefn propDefn = props.get(i);
					cachedProperties.put(propDefn.getName(), propDefn);
				}
				tmpDefn = (ElementDefn) tmpDefn.getParent();
			}
		}
	}

	static class MockupDesignElement extends DesignElement {

		MockupElementDefn defn = null;

		public MockupDesignElement(MockupElementDefn defn) {
			this.cachedDefn = defn;
			this.defn = defn;
		}

		@Override
		public IElementDefn getDefn() {
			return this.defn;
		}

		@Override
		public void apply(ElementVisitor visitor) {
		}

		@Override
		public String getElementName() {
			return null;
		}

		@Override
		public DesignElementHandle getHandle(Module rootElement) {
			return null;
		}

	}
}
