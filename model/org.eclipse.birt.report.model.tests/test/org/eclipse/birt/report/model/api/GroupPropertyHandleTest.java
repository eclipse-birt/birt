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
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextDataItemModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for GroupPropertyHandle.
 */

public class GroupPropertyHandleTest extends BaseTestCase {

	ElementFactory elemFactory = null;

	DesignElementHandle handle1 = null;
	DesignElementHandle handle2 = null;
	DesignElementHandle handle3 = null;

	GroupElementHandle groupElementHandle = null;

	GroupPropertyHandle groupPropertyHandle1 = null;
	GroupPropertyHandle groupPropertyHandle2 = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		createDesign();

		elemFactory = new ElementFactory(design);

		handle1 = elemFactory.newGraphicMasterPage("page1"); //$NON-NLS-1$
		handle2 = elemFactory.newGraphicMasterPage("page2"); //$NON-NLS-1$
		handle3 = elemFactory.newGraphicMasterPage("page3"); //$NON-NLS-1$

		handle1.setStringProperty(MasterPage.TYPE_PROP, DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		handle2.setStringProperty(MasterPage.TYPE_PROP, DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		handle3.setStringProperty(MasterPage.TYPE_PROP, DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		ArrayList elements = new ArrayList();
		elements.add(handle1);
		elements.add(handle2);
		elements.add(handle3);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		groupPropertyHandle1 = groupElementHandle.getPropertyHandle(MasterPage.HEIGHT_PROP);
		groupPropertyHandle2 = groupElementHandle.getPropertyHandle(MasterPage.COMMENTS_PROP);
	}

	/**
	 * 1. Same value. 2. different value.
	 * 
	 * @throws SemanticException
	 */

	public void testGetStringValue() throws SemanticException {
		// same value for comments.
		handle1.setProperty(MasterPage.COMMENTS_PROP, "Who am I?"); //$NON-NLS-1$
		handle2.setProperty(MasterPage.COMMENTS_PROP, "Who am I?"); //$NON-NLS-1$
		handle3.setProperty(MasterPage.COMMENTS_PROP, "Who am I?"); //$NON-NLS-1$

		// different value for height
		handle1.setProperty(MasterPage.HEIGHT_PROP, "12pt"); //$NON-NLS-1$
		handle2.setProperty(MasterPage.HEIGHT_PROP, "13pt"); //$NON-NLS-1$
		handle3.setProperty(MasterPage.HEIGHT_PROP, "12cm"); //$NON-NLS-1$

		assertNull(null, groupPropertyHandle1.getStringValue());

		assertEquals("Who am I?", groupPropertyHandle2.getStringValue()); //$NON-NLS-1$

		// set prop2 to same value

		handle2.setProperty(MasterPage.HEIGHT_PROP, "12pt"); //$NON-NLS-1$
		handle3.setProperty(MasterPage.HEIGHT_PROP, "12pt"); //$NON-NLS-1$

		assertEquals("12pt", groupPropertyHandle1.getStringValue()); //$NON-NLS-1$
	}

	/**
	 * 1. Same value. 2. different value.
	 * 
	 * @throws SemanticException
	 */

	public void testGetValue() throws SemanticException {
		// same value for comments.
		handle1.setProperty(MasterPage.COMMENTS_PROP, "Who am I?"); //$NON-NLS-1$
		handle2.setProperty(MasterPage.COMMENTS_PROP, "Who am I?"); //$NON-NLS-1$
		handle3.setProperty(MasterPage.COMMENTS_PROP, "Who am I?"); //$NON-NLS-1$

		// different value for height
		handle1.setProperty(MasterPage.HEIGHT_PROP, "12pt"); //$NON-NLS-1$
		handle2.setProperty(MasterPage.HEIGHT_PROP, "13pt"); //$NON-NLS-1$
		handle3.setProperty(MasterPage.HEIGHT_PROP, "12cm"); //$NON-NLS-1$

		assertNull(null, groupPropertyHandle1.getValue());

		assertEquals("Who am I?", groupPropertyHandle2.getValue()); //$NON-NLS-1$

		// set prop2 to same value

		handle2.setProperty(MasterPage.HEIGHT_PROP, "12pt"); //$NON-NLS-1$
		handle3.setProperty(MasterPage.HEIGHT_PROP, "12pt"); //$NON-NLS-1$

		assertTrue(groupPropertyHandle1.getValue() instanceof DimensionValue); // $NON-NLS-1$
		DimensionValue dimensionValue = (DimensionValue) groupPropertyHandle1.getValue();

		assertEquals(12d, dimensionValue.getMeasure());
		assertEquals(DesignChoiceConstants.UNITS_PT, dimensionValue.getUnits());

		TextDataHandle textHandle1 = elemFactory.newTextData(null);
		TextDataHandle textHandle2 = elemFactory.newTextData(null);
		TextDataHandle textHandle3 = elemFactory.newTextData(null);

		designHandle.getBody().add(textHandle1);
		designHandle.getBody().add(textHandle2);
		designHandle.getBody().add(textHandle3);

		ArrayList<TextDataHandle> list = new ArrayList<TextDataHandle>();
		list.add(textHandle1);
		list.add(textHandle2);
		list.add(textHandle3);

		GroupElementHandle groupHElementHandle = new SimpleGroupElementHandle(designHandle, list);
		GroupPropertyHandle propHandle = groupHElementHandle.getPropertyHandle(ITextDataItemModel.VALUE_EXPR_PROP);

		textHandle1.setValueExpr("value");
		textHandle2.setValueExpr("value");
		textHandle3.setValueExpr("value");

		assertTrue(propHandle.getValue() instanceof Expression);
		Expression expr = (Expression) propHandle.getValue();
		assertEquals("value", expr.getStringExpression());

		textHandle1.setProperty(ITextDataItemModel.VALUE_EXPR_PROP, new Expression("value", IExpressionType.CONSTANT));
		textHandle2.setProperty(ITextDataItemModel.VALUE_EXPR_PROP, new Expression("value", IExpressionType.CONSTANT));
		textHandle3.setProperty(ITextDataItemModel.VALUE_EXPR_PROP, new Expression("value", IExpressionType.CONSTANT));

		assertTrue(propHandle.getValue() instanceof Expression);

		expr = (Expression) propHandle.getValue();
		assertEquals("value", expr.getStringExpression());
		assertEquals(IExpressionType.CONSTANT, expr.getType());

		textHandle1.setProperty(ITextDataItemModel.VALUE_EXPR_PROP, new Expression("value", IExpressionType.CONSTANT));
		textHandle2.setProperty(ITextDataItemModel.VALUE_EXPR_PROP,
				new Expression("value", IExpressionType.JAVASCRIPT));
		textHandle3.setProperty(ITextDataItemModel.VALUE_EXPR_PROP, new Expression("value2", IExpressionType.CONSTANT));

		assertNull(propHandle.getValue());

		textHandle3.setProperty(ITextDataItemModel.VALUE_EXPR_PROP, new Expression("value", IExpressionType.CONSTANT));
		assertNull(propHandle.getValue());
	}

	/**
	 * Set value for a string and an dimension property.
	 * 
	 * @throws SemanticException
	 */

	public void testSetValue() throws SemanticException {
		groupPropertyHandle2.setValue("Rock likes soccer"); //$NON-NLS-1$

		assertEquals("Rock likes soccer", handle1.getStringProperty(MasterPage.COMMENTS_PROP)); //$NON-NLS-1$
		assertEquals("Rock likes soccer", handle2.getStringProperty(MasterPage.COMMENTS_PROP)); //$NON-NLS-1$
		assertEquals("Rock likes soccer", handle3.getStringProperty(MasterPage.COMMENTS_PROP)); //$NON-NLS-1$

		groupPropertyHandle1.setValue("1pt"); //$NON-NLS-1$

		assertEquals("1pt", handle1.getStringProperty(MasterPage.HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("1pt", handle2.getStringProperty(MasterPage.HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("1pt", handle3.getStringProperty(MasterPage.HEIGHT_PROP)); //$NON-NLS-1$

	}

	/**
	 * Tests the visibility of a group property handle.
	 * 
	 * <ul>
	 * <li>dataSet properties in two text elements.</li>
	 * <li>x properties in two label and one freeform elements.</li>
	 * <li>dataSet properties in two label and one freeform elements.</li>
	 * </ul>
	 */

	public void testVisibility() {
		createDesign();

		elemFactory = new ElementFactory(design);

		handle1 = elemFactory.newTextItem("text1"); //$NON-NLS-1$
		handle2 = elemFactory.newTextItem("text2"); //$NON-NLS-1$

		ArrayList elements = new ArrayList();
		elements.add(handle1);
		elements.add(handle2);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		groupPropertyHandle1 = groupElementHandle.getPropertyHandle(ReportItem.DATA_SET_PROP);
		assertTrue(groupPropertyHandle1.isVisible());
		assertFalse(groupPropertyHandle1.isReadOnly());

		groupPropertyHandle1 = groupElementHandle.getPropertyHandle("noprop"); //$NON-NLS-1$
		assertNull(groupPropertyHandle1);

		handle1 = elemFactory.newLabel("label1"); //$NON-NLS-1$
		handle2 = elemFactory.newFreeForm("form1"); //$NON-NLS-1$
		handle3 = elemFactory.newLabel("label2"); //$NON-NLS-1$

		elements = new ArrayList();
		elements.add(handle1);
		elements.add(handle2);
		elements.add(handle3);

		ReportDesignHandle Handle = design.handle();
		groupElementHandle = new SimpleGroupElementHandle(Handle, elements);

		groupPropertyHandle1 = groupElementHandle.getPropertyHandle(ReportItem.X_PROP);
		assertFalse(groupPropertyHandle1.isVisible());
		assertFalse(groupPropertyHandle1.isReadOnly());

		groupPropertyHandle1 = groupElementHandle.getPropertyHandle(ReportItem.DATA_SET_PROP);
		assertFalse(groupPropertyHandle1.isVisible());
		assertFalse(groupPropertyHandle1.isReadOnly());

		groupPropertyHandle1 = groupElementHandle.getPropertyHandle(ReportItem.HEIGHT_PROP);
		assertTrue(groupPropertyHandle1.isVisible());
		assertFalse(groupPropertyHandle1.isReadOnly());
	}

	/**
	 * Tests the equals() of a group property handle.
	 */

	public void testEquals() {
		// the contained elements are freeforms.

		// test on property handle.

		assertTrue(groupPropertyHandle1.equals(handle1.getPropertyHandle(MasterPage.HEIGHT_PROP)));

		assertFalse(groupPropertyHandle1.equals(handle1.getPropertyHandle(MasterPage.WIDTH_PROP)));

		// the element that not in the group element.

		DesignElementHandle handle4 = elemFactory.newGraphicMasterPage("page4"); //$NON-NLS-1$
		assertFalse(groupPropertyHandle1.equals(handle4.getPropertyHandle(MasterPage.WIDTH_PROP)));

		// test on group property handle

		assertTrue(groupPropertyHandle1.equals(groupElementHandle.getPropertyHandle(MasterPage.HEIGHT_PROP)));

		assertFalse(groupPropertyHandle1.equals(groupElementHandle.getPropertyHandle(MasterPage.WIDTH_PROP)));

		ArrayList elements = new ArrayList();
		elements.add(handle1);
		elements.add(handle2);
		elements.add(handle3);

		GroupElementHandle groupElementHandle1 = new SimpleGroupElementHandle(designHandle, elements);

		// different group element handle.

		assertFalse(groupPropertyHandle1.equals(groupElementHandle1.getPropertyHandle(MasterPage.WIDTH_PROP)));

	}

	/**
	 * 
	 * test get the reference element value list if the property is a element
	 * referencable type.
	 * 
	 * case1: get the data set list. case2: get the data source list. case3: get the
	 * style list.
	 * 
	 * @throws DesignFileException
	 */

	public void testgetReferenceElementValueList() throws DesignFileException {

		openDesign("PropertyHandleTest_1.xml"); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) designHandle.getComponents().get(1);

		LabelHandle label1 = (LabelHandle) designHandle.getComponents().get(0);

		ArrayList elements = new ArrayList();
		elements.add(label2);
		elements.add(label1);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);

		GroupPropertyHandle groupPropertyHandle = groupElementHandle.getPropertyHandle(Label.DATA_SET_PROP);

		List list = groupPropertyHandle.getReferenceableElementList();
		assertEquals(3, list.size());
		assertEquals("a", ((DesignElementHandle) list.get(0)).getName()); //$NON-NLS-1$
		assertEquals("b", ((DesignElementHandle) list.get(1)).getName()); //$NON-NLS-1$
		assertEquals("c", ((DesignElementHandle) list.get(2)).getName()); //$NON-NLS-1$

		groupPropertyHandle = groupElementHandle.getPropertyHandle(Label.STYLE_PROP);

		list = groupPropertyHandle.getReferenceableElementList();
		assertEquals(2, list.size());

		DataSetHandle dataSet1 = (DataSetHandle) designHandle.getDataSets().get(0);
		DataSetHandle dataSet2 = (DataSetHandle) designHandle.getDataSets().get(1);
		elements.clear();
		elements.add(dataSet1);
		elements.add(dataSet2);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		groupPropertyHandle = groupElementHandle.getPropertyHandle(SimpleDataSet.DATA_SOURCE_PROP);

		assertEquals(2, groupPropertyHandle.getReferenceableElementList().size());
	}

	/**
	 * Tests the getLocalStringValue.
	 * 
	 * @throws Exception
	 */

	public void testGetLocalValue() throws Exception {
		LabelHandle label = designHandle.getElementFactory().newLabel("local"); //$NON-NLS-1$
		LabelHandle parentLabel = designHandle.getElementFactory().newLabel("parent"); //$NON-NLS-1$
		LabelHandle childLabel = designHandle.getElementFactory().newLabel("child"); //$NON-NLS-1$
		LabelHandle styledLabel = designHandle.getElementFactory().newLabel("styled"); //$NON-NLS-1$

		// add style and labels

		SharedStyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$
		style.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_DOUBLE);
		designHandle.getStyles().add(style);
		designHandle.getBody().add(label);
		designHandle.getBody().add(childLabel);
		designHandle.getBody().add(styledLabel);
		designHandle.getComponents().add(parentLabel);
		childLabel.setExtends(parentLabel);
		parentLabel.setProperty(IStyleModel.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_DOUBLE);
		styledLabel.setStyle(style);
		label.setProperty(IStyleModel.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_DOUBLE);

		// test getLocalValue

		List elements = new ArrayList();
		elements.add(childLabel);
		elements.add(styledLabel);

		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		groupPropertyHandle1 = groupElementHandle.getPropertyHandle(IStyleModel.BORDER_LEFT_STYLE_PROP);
		assertNotNull(groupPropertyHandle1);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOUBLE, groupPropertyHandle1.getStringValue());
		assertNull(groupPropertyHandle1.getLocalStringValue());

		// add the label which has local value

		elements.add(label);
		groupElementHandle = new SimpleGroupElementHandle(designHandle, elements);
		groupPropertyHandle1 = groupElementHandle.getPropertyHandle(IStyleModel.BORDER_LEFT_STYLE_PROP);
		assertNotNull(groupPropertyHandle1);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOUBLE, groupPropertyHandle1.getStringValue());
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOUBLE, groupPropertyHandle1.getLocalStringValue());
	}

}
