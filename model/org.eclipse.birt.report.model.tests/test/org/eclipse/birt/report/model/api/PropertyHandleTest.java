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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for PropertyHandle class.
 * 
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>testItemOperations</td>
 * <td>Tests on adding, moving, removing, inserting, replacing maps rules in a
 * style.</td>
 * <td>The operations can be completed successfully and the output file matches
 * the golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td>testSetGetValues</td>
 * <td>Sets and gets the property value in different prototype like float,
 * string.</td>
 * <td>Values can be set/gotten correctly and the output file matches the golden
 * file.</td>
 * </tr>
 * 
 * <tr>
 * <td>testOtherOperations</td>
 * <td>Gets the property definition.</td>
 * <td>Retrieves the property definition correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Gets the structure handle of FONT_WEIGHT and MAP_RULE properties.</td>
 * <td>The structure handle of FONT_WEIGHT is null and the structure handle of
 * MAP_RULE is <code>null</code>.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class PropertyHandleTest extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("PropertyHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests get property definition, choices of property and structure handle of a
	 * property.
	 */
	public void testOtherOperations() {
		StyleElement myStyle = design.findStyle("My-Style"); //$NON-NLS-1$
		PropertyHandle propHandle = myStyle.getHandle(design).getPropertyHandle(Style.FONT_FAMILY_PROP);

		assertNotNull(propHandle.getChoices());
		propHandle = myStyle.getHandle(design).getPropertyHandle(Style.FONT_WEIGHT_PROP);
		assertNotNull(propHandle.getChoices());

		PropertyDefn propDefn = (ElementPropertyDefn) propHandle.getDefn();
		assertEquals(Style.FONT_WEIGHT_PROP, propDefn.getName());
		assertNull(propHandle.getAt(0));

		propHandle = myStyle.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);
		assertNull(propHandle.getAt(0));

		StructureContext memberRef = propHandle.getContext();
		propDefn = memberRef.getElementProp();
		assertEquals(Style.MAP_RULES_PROP, propDefn.getName());

		Label label = (Label) design.findElement("base"); //$NON-NLS-1$
		propHandle = label.getHandle(design).getPropertyHandle(Label.TEXT_PROP);
		assertNull(propHandle.getChoices());
		propDefn = (ElementPropertyDefn) propHandle.getPropertyDefn();
		assertEquals(Label.TEXT_PROP, propDefn.getName());

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("child1"); //$NON-NLS-1$

		propHandle = labelHandle.getPropertyHandle(Style.COLOR_PROP);
		assertTrue(propHandle.isSet());
		assertFalse(propHandle.isLocal());

		propHandle = labelHandle.getPropertyHandle(Label.TEXT_PROP);
		assertTrue(propHandle.isSet());
		assertFalse(propHandle.isLocal());

		propHandle = labelHandle.getPropertyHandle(Label.NAME_PROP);
		assertTrue(propHandle.isSet());
		assertTrue(propHandle.isLocal());

	}

	/**
	 * tests the localized property value and non-localized property value can be
	 * returned correctly.
	 * 
	 * @throws SemanticException
	 */
	public void testGetDisplayValue() throws SemanticException {

		// in German, "." means the same thing with "," for numbers in En. So,
		// "5000,000" in English shuold be written as "5000.000" or "5.000" in
		// Germany.

		createDesign(ULocale.GERMANY);

		SimpleMasterPageHandle masterPage = designHandle.getElementFactory().newSimpleMasterPage("page"); //$NON-NLS-1$

		designHandle.getMasterPages().add(masterPage);
		masterPage.setProperty(SimpleMasterPageHandle.HEADER_HEIGHT_PROP, "500.0,000"); //$NON-NLS-1$

		PropertyHandle propertyHandle = masterPage.getPropertyHandle(SimpleMasterPageHandle.HEADER_HEIGHT_PROP);

		assertEquals("5.000in", propertyHandle.getDisplayValue()); //$NON-NLS-1$
		assertEquals("5000in", propertyHandle.getStringValue()); //$NON-NLS-1$

	}

	/**
	 * Tests add, move, remove, insert, replace map rules in a style.
	 * 
	 * @throws Exception if the property is not a list property, the index of the
	 *                   item is invalid or the output file cannot be saved
	 *                   correctly.
	 */
	@SuppressWarnings("unchecked")
	public void testItemOperations() throws Exception {
		SharedStyleHandle myStyleHandle = (SharedStyleHandle) design.findStyle("My-Style").getHandle(design); //$NON-NLS-1$

		// add one highlight rule.

		MapRule rule = new MapRule();
		rule.setProperty(MapRule.DISPLAY_MEMBER, "addItem1"); //$NON-NLS-1$

		// get map.rules property handle and add one map rule

		PropertyHandle propHandle = myStyleHandle.getPropertyHandle(Style.MAP_RULES_PROP);
		propHandle.addItem(rule);

		rule = new MapRule();
		rule.setProperty(MapRule.DISPLAY_MEMBER, "insert1"); //$NON-NLS-1$
		rule.setProperty((PropertyDefn) rule.getDefn().findProperty(MapRule.OPERATOR_MEMBER),
				DesignChoiceConstants.MAP_OPERATOR_LIKE);
		propHandle.insertItem(rule, 1);

		rule = new MapRule();
		rule.setProperty(MapRule.DISPLAY_MEMBER, "insert2"); //$NON-NLS-1$
		rule.setProperty((PropertyDefn) rule.getDefn().findProperty(MapRule.OPERATOR_MEMBER),
				DesignChoiceConstants.MAP_OPERATOR_GE);
		propHandle.insertItem(rule, 2);

		MapRule replaceRule = new MapRule();
		replaceRule.setProperty(MapRule.DISPLAY_MEMBER, "replace1"); //$NON-NLS-1$
		replaceRule.setProperty((PropertyDefn) rule.getDefn().findProperty(MapRule.OPERATOR_MEMBER),
				DesignChoiceConstants.MAP_OPERATOR_LIKE);

		// replaces the insert2 as replace1.

		propHandle.replaceItem(rule, replaceRule);

		// changes the position of replace1 and insert1.

		propHandle.moveItem(0, 2);

		// changes the position of replace1 and addItem1.

		propHandle.moveItem(2, 1);

		StructureHandle structHandle = propHandle.getAt(2);
		assertNotNull(structHandle);

		MemberHandle memberHandle = structHandle.getMember(MapRule.OPERATOR_MEMBER);
		memberHandle.setValue(DesignChoiceConstants.MAP_OPERATOR_FALSE);

		myStyleHandle = (SharedStyleHandle) design.findStyle("Style1").getHandle(design); //$NON-NLS-1$

		// get map.rules property handle and add one map rule

		propHandle = myStyleHandle.getPropertyHandle(Style.MAP_RULES_PROP);
		assertEquals(3, propHandle.getListValue().size());

		try {
			propHandle.removeItem(5);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// pass
		}

		propHandle.removeItem(2);
		propHandle.removeItem(0);

		List<MapRule> rules = (List<MapRule>) myStyleHandle.getProperty(Style.MAP_RULES_PROP);
		assertEquals(1, rules.size());

		save();
		assertTrue(compareFile("PropertyHandleTest_golden.xml")); //$NON-NLS-1$

		propHandle.clearValue();

		// add two identical structure to a list.
		// Notice: this is a GUI error to allow this operation, anyway, it
		// doesn't
		// do any harm to us.

		MapRule newRule = new MapRule();
		newRule.setValue1("value1"); //$NON-NLS-1$

		propHandle.addItem(newRule);
		propHandle.addItem(newRule);
		assertEquals(2, propHandle.getListValue().size());

		MapRuleHandle mapRuleHandle0 = (MapRuleHandle) propHandle.getAt(0);
		assertEquals("value1", mapRuleHandle0.getValue1()); //$NON-NLS-1$

		MapRuleHandle mapRuleHandle1 = (MapRuleHandle) propHandle.getAt(1);
		assertEquals("value1", mapRuleHandle1.getValue1()); //$NON-NLS-1$

		mapRuleHandle0.setValue1("new value"); //$NON-NLS-1$
		assertEquals("new value", mapRuleHandle0.getValue1()); //$NON-NLS-1$

		propHandle.removeItem(newRule);
		assertEquals(1, propHandle.getListValue().size());

	}

	/**
	 * Tests to set and get property values.
	 * 
	 * @throws Exception if values are invalid or the output file cannot be saved
	 *                   correctly.
	 */
	@SuppressWarnings("unchecked")
	public void testSetGetValues() throws Exception {
		StyleElement myStyle = design.findStyle("Style1"); //$NON-NLS-1$

		// get map.rules property handle and add one map rule

		PropertyHandle propHandle = myStyle.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);

		List<MapRule> rules = (List<MapRule>) myStyle.getProperty(design, Style.MAP_RULES_PROP);
		MapRule rule = new MapRule();
		rule.setProperty(MapRule.DISPLAY_MEMBER, "set map rules"); //$NON-NLS-1$
		rules.add(rule);

		try {
			propHandle.setValue(rules);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		propHandle = myStyle.getHandle(design).getPropertyHandle(Style.COLOR_PROP);
		propHandle.setValue("yellow"); //$NON-NLS-1$
		assertEquals("yellow", propHandle.getStringValue()); //$NON-NLS-1$

		propHandle = myStyle.getHandle(design).getPropertyHandle(Style.BORDER_BOTTOM_WIDTH_PROP);
		propHandle.setValue(DesignChoiceConstants.LINE_WIDTH_THIN);
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THIN, propHandle.getStringValue());

		// the text property on a label.

		Label label = (Label) design.findElement("base"); //$NON-NLS-1$
		propHandle = label.getHandle(design).getPropertyHandle(Label.TEXT_PROP);
		propHandle.setStringValue("new label text"); //$NON-NLS-1$
		assertEquals("new label text", propHandle.getValue()); //$NON-NLS-1$

		// the height property on a label

		propHandle = label.getHandle(design).getPropertyHandle(ReportItem.HEIGHT_PROP);
		propHandle.setStringValue(new NumberFormatter(propHandle.getModule().getLocale()).format(1.23) + "mm");

		assertEquals(PropertyType.DIMENSION_TYPE, propHandle.getTypeCode());
		assertEquals("1.23mm", propHandle.getStringValue()); //$NON-NLS-1$

		// an integer type.

		int widows = myStyle.getHandle(design).getPropertyHandle(Style.WIDOWS_PROP).getIntValue();
		assertEquals(2, widows);

		myStyle.getHandle(design).getPropertyHandle(Style.WIDOWS_PROP).setIntValue(6);
		widows = myStyle.getHandle(design).getPropertyHandle(Style.WIDOWS_PROP).getIntValue();
		assertEquals(6, widows);

		myStyle.getHandle(design).getPropertyHandle(Style.WIDOWS_PROP).clearValue();

		save();
		assertTrue(compareFile("PropertyHandleTest_golden_1.xml")); //$NON-NLS-1$

	}

	/**
	 * test whether the two property handle are equal.
	 * 
	 * case1: get property handle twice from style.font-family. These two handle
	 * should be equal.
	 * 
	 * case2: get property handle from style.font-family and
	 * style.BACKGROUND_ATTACHMENT_PROP. These two proeprty handles should not be
	 * equal.
	 * 
	 * case3: a property handle should not be equal to null.
	 * 
	 * case4: get property handle from different element but same property. These
	 * two handle should not be equal.
	 */
	public void testEqual() {

		StyleElement myStyle = design.findStyle("My-Style"); //$NON-NLS-1$
		PropertyHandle propHandle = myStyle.getHandle(design).getPropertyHandle(Style.FONT_FAMILY_PROP);
		PropertyHandle propHandle1 = myStyle.getHandle(design).getPropertyHandle(Style.FONT_FAMILY_PROP);

		assertTrue(propHandle.equals(propHandle1));

		assertFalse(propHandle.equals(null));

		propHandle1 = myStyle.getHandle(design).getPropertyHandle(Style.BACKGROUND_ATTACHMENT_PROP);

		assertFalse(propHandle.equals(propHandle1));

		StyleElement style = design.findStyle("Style1"); //$NON-NLS-1$
		propHandle1 = style.getHandle(design).getPropertyHandle(Style.FONT_FAMILY_PROP);
		assertFalse(propHandle.equals(propHandle1));

	}

	/**
	 * 
	 * test get the reference element value list if the property is a element
	 * referencable type.
	 * 
	 * case1: get the data set list. case2: get the data source list. case3: get the
	 * style list.
	 * 
	 * @throws Exception
	 */
	public void testgetReferenceElementValueList() throws Exception {

		openDesign("PropertyHandleTest_1.xml"); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) designHandle.getComponents().get(1);

		PropertyHandle propertyHandle = label2.getPropertyHandle(Label.DATA_SET_PROP);

		List<?> list = propertyHandle.getReferenceableElementList();
		assertEquals(3, list.size());

		propertyHandle = label2.getPropertyHandle(Label.STYLE_PROP);

		list = propertyHandle.getReferenceableElementList();
		assertEquals(2, list.size());

		DataSetHandle dataSet = (DataSetHandle) designHandle.getDataSets().get(0);
		propertyHandle = dataSet.getPropertyHandle(SimpleDataSet.DATA_SOURCE_PROP);

		assertEquals(2, propertyHandle.getReferenceableElementList().size());

		// test cube list
		propertyHandle = label2.getPropertyHandle(IReportItemModel.CUBE_PROP);
		assertEquals(0, propertyHandle.getReferenceableElementList().size());
		CubeHandle cubeHandle = designHandle.getElementFactory().newTabularCube(null);
		designHandle.getCubes().add(cubeHandle);
		assertEquals(1, propertyHandle.getReferenceableElementList().size());
	}

	/**
	 * Tests property visibilities defined in ROM.
	 */
	public void testPropertyVisibilities() throws Exception {

		createDesign();

		ElementFactory elemFactory = new ElementFactory(design);
		LabelHandle label = elemFactory.newLabel("label1"); //$NON-NLS-1$

		PropertyHandle propHandle = label.getPropertyHandle(ReportItem.DATA_SET_PROP);
		assertFalse(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		propHandle = label.getPropertyHandle(ReportItem.HEIGHT_PROP);
		assertTrue(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		CellHandle cell = elemFactory.newCell();
		propHandle = cell.getPropertyHandle(Cell.COLUMN_PROP);
		assertFalse(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		TextItemHandle text = elemFactory.newTextItem("text1"); //$NON-NLS-1$
		propHandle = text.getPropertyHandle(ReportItem.DATA_SET_PROP);
		assertTrue(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		CubeHandle cube = elemFactory.newTabularCube("cube1"); //$NON-NLS-1$
		propHandle = cube.getPropertyHandle(ICubeModel.DIMENSIONS_PROP);
		assertFalse(propHandle.isVisible());
		propHandle = cube.getPropertyHandle(ICubeModel.FILTER_PROP);
		assertFalse(propHandle.isVisible());

		// tests visibility of row which locates in the header and footer of the
		// table.
		TableHandle table = elemFactory.newTableItem("table", 3); //$NON-NLS-1$
		SlotHandle headerSlot = table.getHeader();
		RowHandle rowHandle = (RowHandle) headerSlot.get(0);
		propHandle = rowHandle.getPropertyHandle(ITableRowModel.REPEATABLE_PROP);
		assertTrue(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		SlotHandle footerSlot = table.getFooter();
		rowHandle = (RowHandle) footerSlot.get(0);
		propHandle = rowHandle.getPropertyHandle(ITableRowModel.REPEATABLE_PROP);
		assertTrue(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		SlotHandle detailSlot = table.getDetail();
		rowHandle = (RowHandle) detailSlot.get(0);
		propHandle = rowHandle.getPropertyHandle(ITableRowModel.REPEATABLE_PROP);
		assertFalse(propHandle.isVisible());
		assertTrue(propHandle.isReadOnly());

		// tests visibility of row which locates in the header and footer of the
		// table group.
		SlotHandle groupSlot = table.getGroups();
		TableGroupHandle group = elemFactory.newTableGroup();
		groupSlot.add(group);
		SlotHandle groupFooterSlot = group.getFooter();
		rowHandle = elemFactory.newTableRow();
		groupFooterSlot.add(rowHandle);
		propHandle = rowHandle.getPropertyHandle(ITableRowModel.REPEATABLE_PROP);
		assertTrue(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		SlotHandle groupHeaderSlot = group.getHeader();
		rowHandle = elemFactory.newTableRow();
		groupHeaderSlot.add(rowHandle);
		propHandle = rowHandle.getPropertyHandle(ITableRowModel.REPEATABLE_PROP);
		assertTrue(propHandle.isVisible());
		assertFalse(propHandle.isReadOnly());

		// tests visibility of row which locates in the grid.
		GridHandle grid = elemFactory.newGridItem("grid", 2, 2); //$NON-NLS-1$
		SlotHandle rowSlot = grid.getRows();
		rowHandle = (RowHandle) rowSlot.get(0);
		propHandle = rowHandle.getPropertyHandle(ITableRowModel.REPEATABLE_PROP);
		assertFalse(propHandle.isVisible());
		assertTrue(propHandle.isReadOnly());

		// both hide and readonly are set
		ExtendedItemHandle extendedItem = elemFactory.newExtendedItem(null, "TestingMatrix"); //$NON-NLS-1$
		propHandle = extendedItem.getPropertyHandle(ExtendedItemHandle.BOOKMARK_PROP);
		assertFalse(propHandle.isVisible());
		assertTrue(propHandle.isReadOnly());
	}

	/**
	 * Tests the cached member reference, which is used to make the structure handle
	 * can not be broken by adding or dropping structure .
	 * 
	 * @throws Exception if any exception
	 */
	public void testCachedMemberRef() throws Exception {
		StyleHandle style = designHandle.findStyle("Style1"); //$NON-NLS-1$
		PropertyHandle propHandle = style.getPropertyHandle(Style.MAP_RULES_PROP);

		ArrayList<Object> list = new ArrayList<Object>();
		Iterator<?> iter = propHandle.iterator();
		while (iter.hasNext()) {
			StructureHandle structHandle = (StructureHandle) iter.next();
			list.add(structHandle);
		}

		// The structures: like, eq, ge

		assertEquals(DesignChoiceConstants.MAP_OPERATOR_LIKE,
				((StructureHandle) list.get(0)).getProperty(MapRule.OPERATOR_MEMBER));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ,
				((StructureHandle) list.get(1)).getProperty(MapRule.OPERATOR_MEMBER));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_GE,
				((StructureHandle) list.get(2)).getProperty(MapRule.OPERATOR_MEMBER));

		MapRule ruleToDropped = (MapRule) ((StructureHandle) list.get(1)).getStructure();

		// Drop the second structure: eq

		propHandle.removeItem(1);

		PropertyDefn memberDefn = (PropertyDefn) ((StructureHandle) list.get(2)).getDefn()
				.getMember(MapRule.OPERATOR_MEMBER);

		// Check the first one

		MapRuleHandle rule = ((MapRuleHandle) list.get(0));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_LIKE, rule.getStructure().getProperty(design, memberDefn));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_LIKE, rule.getProperty(MapRule.OPERATOR_MEMBER));

		// Check the second one

		MapRuleHandle ruleDropped = ((MapRuleHandle) list.get(1));
		assertEquals(null, ruleDropped.getStructure());

		// All operation related with MemberHandle is not allowed.

		assertFalse(ruleDropped.iterator().hasNext());
		assertNull(ruleDropped.iterator().next());

		try {
			ruleDropped.getProperty(MapRule.OPERATOR_MEMBER);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}

		try {
			ruleDropped.getMember(MapRule.OPERATOR_MEMBER);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}

		try {
			ruleDropped.getOperator();
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}

		try {
			ruleDropped.setProperty(MapRule.OPERATOR_MEMBER, DesignChoiceConstants.MAP_OPERATOR_LE);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}

		// Check the third one

		rule = ((MapRuleHandle) list.get(2));

		assertEquals(DesignChoiceConstants.MAP_OPERATOR_GE, rule.getStructure().getProperty(design, memberDefn));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_GE, rule.getProperty(MapRule.OPERATOR_MEMBER));

		// Add the dropped structure to the end.

		MapRuleHandle ruleAdded = (MapRuleHandle) propHandle.addItem(ruleToDropped);
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ruleDropped.getStructure().getProperty(design, memberDefn));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ruleDropped.getProperty(MapRule.OPERATOR_MEMBER));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ruleAdded.getStructure().getProperty(design, memberDefn));
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ruleAdded.getProperty(MapRule.OPERATOR_MEMBER));
	}

	/**
	 * 1)Properties that may cause structure change are not allowed to change within
	 * child elements.
	 * <p>
	 * 2)Test setting intrinsic and non-intrinsic properties.
	 * 
	 * @throws DesignFileException
	 * @throws Exception
	 */
	public void testCompoundExtendsOperations() throws Exception {
		openDesign("PropertyHandleTest_2.xml"); //$NON-NLS-1$
		GridHandle grid1 = (GridHandle) designHandle.findElement("Grid1"); //$NON-NLS-1$
		RowHandle row1 = (RowHandle) grid1.getRows().get(0);
		CellHandle cell1 = (CellHandle) row1.getCells().get(0);
		LabelHandle label1 = (LabelHandle) cell1.getContent().get(0);
		NumberFormatter numberFormatter = new NumberFormatter(designHandle.getModule().getLocale());

		try {
			cell1.setRowSpan(2);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		try {
			cell1.setColumn(2);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN, e.getErrorCode());
		}

		grid1.setWidth(numberFormatter.format(31.2) + "mm"); //$NON-NLS-1$
		grid1.setStyleName("style1"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FONT_SIZE_LARGER, grid1.getStringProperty(IStyleModel.FONT_SIZE_PROP));

		row1.setStringProperty(RowHandle.HEIGHT_PROP, "13pt"); //$NON-NLS-1$
		row1.setStyleName("style2"); //$NON-NLS-1$

		label1.setName("New label"); //$NON-NLS-1$

		cell1.setStringProperty(CellHandle.WIDTH_PROP, "20pt"); //$NON-NLS-1$
		cell1.setStyleName("style2"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLD, cell1.getStringProperty(IStyleModel.FONT_WEIGHT_PROP));

		save();
		assertTrue(compareFile("PropertyHandleTest_golden2.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests get int value of a property.
	 * 
	 * @throws Exception
	 */

	public void testGetIntValue() throws Exception {
		createDesign();
		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("aaa"); //$NON-NLS-1$
		label.setProperty(IStyleModel.COLOR_PROP, IColorConstants.RED);
		assertEquals(16711680, label.getIntProperty(IStyleModel.COLOR_PROP));
		assertEquals(16711680, label.getPropertyHandle(IStyleModel.COLOR_PROP).getIntValue());
		assertEquals(IColorConstants.RED, label.getPropertyHandle(IStyleModel.COLOR_PROP).getStringValue());
		assertEquals(IColorConstants.RED, label.getStringProperty(IStyleModel.COLOR_PROP));
	}

	/**
	 * Tests setValue method.
	 * 
	 * @throws Exception
	 */

	public void testSetValue() throws Exception {
		openDesign("PropertyHandleTest_setValue.xml");//$NON-NLS-1$
		OdaDataSetHandle odaHandle = (OdaDataSetHandle) designHandle.getElementByID(35);
		assertNotNull(odaHandle);
		PropertyHandle propHandle = odaHandle.getPropertyHandle(OdaDataSetHandle.RESULT_SET_PROP);
		propHandle.setValue(new ArrayList<Object>());
		assertFalse(propHandle.iterator().hasNext());

		super.compareFile("PropertyHandleTest_setValue_golden.xml");//$NON-NLS-1$
	}
}