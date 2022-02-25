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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for PropertyCommand.
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testSetProperty()}</td>
 * <td>The property name is not defined in the meta.xml.</td>
 * <td>throw out exception.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>The property name can be found in the meta.xml and the property is not
 * intrinsic</td>
 * <td>Property is set successfully.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>The name can be found in the meta.xml and the property is an intrinsic
 * property</td>
 * <td>property value is set successfully.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Changes property value and checks property value.</td>
 * <td>getProperty is the same as input value</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetandClearProperty()}</td>
 * <td>set property value and check the get property value</td>
 * <td>the value of property is the same as input value</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>clear property value and check the result</td>
 * <td>after clear property , the size of result is zero.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testAddItem()}</td>
 * <td>Add map rules to a style which contains null list.</td>
 * <td>The style will have a map rule after executing the command and save file
 * is equal to the golden file PropertyCommandTest_golden_4.xml</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Add a map rule to a style which already has one a map rule.</td>
 * <td>After executing the command, the map rule list size is 2 and save file
 * is equal to the golden file PropertyCommandTest_golden_5.xml</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Add a map rule to a style which has no map rule, but its parent has a
 * map rule.</td>
 * <td>After executing the command, the map rule list size is 2 and save file
 * is equal to the golden file PropertyCommandTest_golden_6.xml</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetMember()}</td>
 * <td>The value is null.</td>
 * <td>getValue is null.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>The value is the same as old value.</td>
 * <td>Keeps the input value.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Normal case.</td>
 * <td>Keeps the input value.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testReplaceItemOne()}</td>
 * <td>Execute the command, save the file</td>
 * <td>compare with a golden file, they should be identical</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Undo the command, save the file</td>
 * <td>compare with a golden file, should be identical</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Redo the command, save the file</td>
 * <td>compare with a golden file, should be identical</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testReplaceItemTwo()}</td>
 * <td>Use the first structure list in My-Style to replace the first structure
 * list in style2</td>
 * <td>the value of structure was replaced</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Undo the operation and use the first structure list in style2 to replace
 * the first structure list in My-Style</td>
 * <td>he value of structure was replaced</td>
 * </tr>
 *
 *
 * <tr>
 * <td>{@link #testRemoveAllItems()}</td>
 * <td>use propertycommand to remove all map-rule items</td>
 * <td>after remove all map-rule items ,the size of map.rules should be zero
 * </td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testInsertAndRemoveItem()}</td>
 * <td>insert new item named InsertAndRemove</td>
 * <td>after insert new item , size of rules changes to three</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>remove this new item</td>
 * <td>after remove that inserted item ,size of rules change to two</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testInsertAndRemoveItem()}</td>
 * <td>undo and redo those two operations</td>
 * <td>after undo and redo operation , size of rules plus or reduce one</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>remove item from null structure list
 *
 * </td>
 * <td>remove item from null list must throw out exception</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testMoveItem()}</td>
 * <td>test move item operation from not null structure list</td>
 * <td>Matches the golden file.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>undo and redo several times</td>
 * <td>Matches the golden file.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>test move item operation from null structure list</td>
 * <td>Matches the golden file.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>move item to position which out of array size
 * <td>The exception with IndexOutOfBoundException is thrown.</td>
 * </tr>
 *
 *
 * <tr>
 * <td>{@link #testPropertyEvent}</td>
 * <td>basic notification with event test.</td>
 * <td>after changing , getpropertyname should be equal to the setter one</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testNotification}</td>
 * <td>Use listener to test if notification works or not.</td>
 * <td>value of property displayName change to listener</td>
 * </tr>
 *
 * </table>
 *
 *
 */

/**
 * @author Administrator
 *
 */
public class PropertyCommandTest extends BaseTestCase {

	/**
	 * The report element to be tested, that is, the property will be set on this
	 * report element.
	 */

	MasterPage page;

	/**
	 * The action handle.
	 */

	ActionHandle actionHandle = null;

	ImageHandle imageHandle = null;

	/*
	 * @see TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// open xml file and get Handle and ReportDesign

		openDesign("PropertyCommandTest.xml"); //$NON-NLS-1$
		page = new GraphicMasterPage();
		page.getHandle(design).setName("Page");//$NON-NLS-1$
		page.setProperty(MasterPage.TYPE_PROP, DesignChoiceConstants.PAGE_SIZE_CUSTOM);

		imageHandle = ((ImageHandle) designHandle.findElement("Image1")); //$NON-NLS-1$
		actionHandle = imageHandle.getActionHandle();

	}

	/**
	 * Invoke
	 * {@link org.eclipse.birt.report.model.command.PropertyCommand#setProperty(String,Object)}
	 * to Test the method: setProperty.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>The property name is not defined in the meta.xml.</li>
	 * <li>The property name can be found in the meta.xml and the property is not
	 * intrinsic</li>
	 * <li>The name can be found in the meta.xml and the property is an intrinsic
	 * property</li>
	 * <li>changes property value and checks property value.</li>
	 * </ul>
	 *
	 * @throws SemanticException
	 */

	public void testSetProperty() throws SemanticException {
		// hello can't be found in meta.xml

		propertyOperate("hello", "HelloWorld", false);//$NON-NLS-1$ //$NON-NLS-2$

		// displayName can be found in meta.xml but it is not intrinsic

		propertyOperate("displayName", "SampleSection", true);//$NON-NLS-1$ //$NON-NLS-2$

		// Because PropertyHandle just support
		// setProperty(ElementPropertyDefn,Object)
		// doesn't support setProperty(String,Object)
		// so use PropertyCommand to setProperty(String,Object)

		PropertyCommand commond = new PropertyCommand(design, page);
		commond.setProperty("name", "hello"); //$NON-NLS-1$ //$NON-NLS-2$
		Object o = page.getHandle(design).getPropertyHandle("name") //$NON-NLS-1$
				.getValue();
		assertEquals("hello", o.toString()); //$NON-NLS-1$

		// change name of displayName property which is not intrinsic

		String propName = "displayName";//$NON-NLS-1$
		ElementPropertyDefn prop = page.getPropertyDefn(propName);
		Object value = "blue he";//$NON-NLS-1$
		PropertyDefnOperate(prop, value);

		// change name of hello property which is not in meta.xml

		propName = "hello";//$NON-NLS-1$
		prop = page.getPropertyDefn(propName);
		assertNull(prop);

		// change name of name property which is intrinsic

		propName = "name";//$NON-NLS-1$
		prop = page.getPropertyDefn(propName);
		value = "hello blue";//$NON-NLS-1$
		PropertyDefnOperate(prop, value);

	}

	/**
	 * Tests the setProperty that the value is structure and the context of it is
	 * not null. In this case, we will make a copy of the given structure and then
	 * set it to the element.
	 *
	 * @throws Exception
	 */
	public void testSetProperty_1() throws Exception {
		// action has set in image, so it has structure context
		Action action = (Action) actionHandle.getStructure();
		assertEquals(actionHandle.getStructure(), ((List) imageHandle.getProperty(ImageHandle.ACTION_PROP)).get(0));
		StructureContext context = action.getContext();
		assertNotNull(context);

		// set this action to another element, we will make a copy and original
		// action will have no change
		LabelHandle labelHandle = designHandle.getElementFactory().newLabel(null);
		ActionHandle labelActionHandle = labelHandle.setAction(action);
		assertNotSame(action, labelActionHandle.getStructure());
		assertEquals(context, action.getContext());
	}

	/**
	 * Test setProperty method. especially for compatible with set toc string value.
	 *
	 * @throws SemanticException
	 */

	public void testBackwardTOCSetProperty() throws SemanticException {
		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("lable1");//$NON-NLS-1$
		designHandle.getBody().add(labelHandle);
		labelHandle.setStringProperty(IReportItemModel.TOC_PROP, "exp1");//$NON-NLS-1$

		assertEquals("exp1", labelHandle.getTOC().getExpression());//$NON-NLS-1$

		labelHandle.setStringProperty(IReportItemModel.TOC_PROP, "exp2");//$NON-NLS-1$

		assertEquals("exp2", labelHandle.getTOC().getExpression());//$NON-NLS-1$

		designHandle.getModule().getActivityStack().undo();

		assertEquals("exp1", labelHandle.getTOC().getExpression());//$NON-NLS-1$
	}

	/**
	 * Test for method clearProperty(String).
	 * <p>
	 * Test Cases:
	 *
	 * <ul>
	 * <li>sets new property</li>
	 * <li>clears just added new property</li>
	 * </ul>
	 *
	 * @throws SemanticException
	 */

	public void testSetandClearProperty() throws SemanticException {
		propertyOperate("height", "2mm", true);//$NON-NLS-1$ //$NON-NLS-2$
		propertyOperate("height", null, true);//$NON-NLS-1$
		propertyOperate("displayName", "world", true);//$NON-NLS-1$ //$NON-NLS-2$
		propertyOperate("displayName", null, true);//$NON-NLS-1$
	}

	/**
	 * Test if property handle can set and get value rightly.
	 *
	 * @param prop  get property name from it
	 * @param value property value
	 */

	private void PropertyDefnOperate(ElementPropertyDefn prop, Object value) {
		// The property name can be found in the meta.xml

		try {
			page.getHandle(design).getPropertyHandle(prop.getName()).setValue(value);
			assertEquals(value, page.getHandle(design).getPropertyHandle(prop.getName()).getValue());
		} catch (SemanticException e) {
			fail(e.toString());
		}
	}

	/**
	 * Test for clearproperty method.
	 * <p>
	 * If value is null ,then judge object is null or not
	 * <p>
	 * If value is not null,then judge object is equal to value or not
	 *
	 * @param name  name of property
	 * @param value value of property
	 * @param type
	 *
	 * @throws SemanticException
	 */

	private void propertyOperate(String name, String value, boolean type) throws SemanticException {
		Object o;
		if (type) {
			if (value == null) {
				// using command to clear property value

				PropertyCommand commond = new PropertyCommand(design, page);
				commond.clearProperty(name);
				o = page.getHandle(design).getPropertyHandle(name).getValue();
				assertNull(o);
			} else {
				// use PropertyHandle to setProperty

				page.getHandle(design).getPropertyHandle(name).setValue(value);
				o = page.getHandle(design).getPropertyHandle(name).getValue();
				assertEquals(value, o.toString());
			}
		} else {
			try {
				// set property value

				page.getHandle(design).setProperty(name, value);
				fail("must throw out PropertyNameException");//$NON-NLS-1$
			} catch (SemanticException e) {
				assertTrue(e instanceof PropertyNameException);
			}

			// check property is null or not

			o = page.getHandle(design).getProperty(name);
			assertNull(o);
		}
	}

	/**
	 *
	 * Tests the method PropertyCommand#addItem().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>Add map rules to a style which contains null list.</li>
	 * <li>Add a map rule to a style which already has one</li>
	 * <li>Add a map rule to a style which has no map rule, but its parent has a map
	 * rule.</li>
	 * <li>Add a structure in the list to the list again. The new structure will be
	 * added.
	 * </ul>
	 *
	 * @throws Exception
	 */
	public void testAddItem() throws Exception {
		StyleElement style = design.findStyle("Style1"); //$NON-NLS-1$
		AddItemRule(style, false);
		saveOperate("PropertyCommandTest_golden_4.xml");//$NON-NLS-1$

		// delete latest new added style

		DeleteRule(style);

		// add map rules to Style2 which contains structure list that has two
		// maprules.

		style = design.findStyle("Style2"); //$NON-NLS-1$
		AddItemRule(style, true);
		saveOperate("PropertyCommandTest_golden_5.xml");//$NON-NLS-1$

		DeleteRule(style);

		// though Style3 has no map rule ,but its parent Style2 has two map rule
		// add map rule to Style3

		style = design.findStyle("Style3"); //$NON-NLS-1$
		AddItemRule(style, false);
		saveOperate("PropertyCommandTest_golden_6.xml");//$NON-NLS-1$

		// add the same structure to the list twice. The structure will be
		// copied and added.

		PropertyHandle propHandle = style.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);

		MapRuleHandle rule = (MapRuleHandle) propHandle.iterator().next();
		propHandle.addItem(rule.getStructure());

		assertEquals(2, propHandle.getListValue().size());

		MapRule rule1 = (MapRule) propHandle.getListValue().get(0);
		MapRule rule2 = (MapRule) propHandle.getListValue().get(1);
		assertTrue(rule1 != rule2);
		assertTrue(rule1.equals(rule2));
	}

	/**
	 * Tests addItem()
	 *
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>Table define a highlight rule list, row inherited it. Now add a new rule
	 * on the row, ensure that the list value in table won't be affected.</li>
	 * <li>Label2 extends from label1, and label1 has defined an Action. Now add a
	 * parameter binding to label2, ensure that list value in label1 won't be
	 * affected.
	 *
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 * @throws SemanticException
	 */

	public void testAddItem2() throws Exception {
		// Case 1:

		ElementFactory factory = new ElementFactory(design);

		TableHandle table = factory.newTableItem("newTable", 3); //$NON-NLS-1$
		designHandle.getBody().add(table);

		HighlightRule highlightRule = StructureFactory.createHighlightRule();
		highlightRule.setTestExpression("row[\"Company\"]"); //$NON-NLS-1$
		highlightRule.setOperator("eq"); //$NON-NLS-1$
		highlightRule.setValue1("Eclipse"); //$NON-NLS-1$
		highlightRule.setProperty(HighlightRule.COLOR_MEMBER, "red"); //$NON-NLS-1$

		// table has a highlight rule.

		table.getPrivateStyle().getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).addItem(highlightRule);

		// row inherited that rule.

		RowHandle row = (RowHandle) table.getDetail().getContents().get(0);

		List inheritedRules = (List) row.getProperty(StyleHandle.HIGHLIGHT_RULES_PROP);
		assertNull(inheritedRules);

		// Add a new rule to row, list value in table should not be affected.

		HighlightRule highlightRule2 = StructureFactory.createHighlightRule();
		highlightRule.setTestExpression("row[\"CustomID\"]"); //$NON-NLS-1$
		highlightRule.setOperator("eq"); //$NON-NLS-1$
		highlightRule.setValue1("Momo"); //$NON-NLS-1$
		highlightRule.setProperty(HighlightRule.COLOR_MEMBER, "blue"); //$NON-NLS-1$

		PropertyHandle rulesHandle = row.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);
		rulesHandle.addItem(highlightRule2);

		assertEquals(1, rulesHandle.getListValue().size());
		assertEquals(1, table.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).getListValue().size());

		// Case 2:
		LabelHandle label1 = factory.newLabel("label1"); //$NON-NLS-1$
		LabelHandle label2 = factory.newLabel("label2"); //$NON-NLS-1$
		designHandle.getComponents().add(label1);
		designHandle.getBody().add(label2);

		label2.setExtendsName("label1"); //$NON-NLS-1$

		// Action is of drill though type with a parameter binding.

		Action action = StructureFactory.createAction();
		ActionHandle actionHandle = label1.setAction(action);
		actionHandle.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH);

		ParamBinding param = StructureFactory.createParamBinding();
		param.setParamName("p1"); //$NON-NLS-1$
		param.setExpression("A+B"); //$NON-NLS-1$

		actionHandle.addParamBinding(param);

		// Ensure that child label inherits the action.
		ActionHandle inheritedAction = label2.getActionHandle();
		assertNotNull(inheritedAction);
		assertEquals("drill-through", inheritedAction.getLinkType()); //$NON-NLS-1$

		ParamBinding param2 = StructureFactory.createParamBinding();
		param2.setParamName("p2"); //$NON-NLS-1$
		param2.setExpression("B+C"); //$NON-NLS-1$

		// Add a new parameter binding to label2, ensure that label1 won't be
		// affected.

		inheritedAction.getParamBindings().addItem(param2);
		assertEquals(1, actionHandle.getParamBindings().getListValue().size());
		assertEquals(2, inheritedAction.getParamBindings().getListValue().size());
	}

	/**
	 * delete latest added new style item.
	 *
	 * @param style style element of design
	 * @throws PropertyValueException
	 */
	private void DeleteRule(StyleElement style) throws PropertyValueException {
		PropertyHandle propHandle = style.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);

		List rules = (List) style.getProperty(design, Style.MAP_RULES_PROP);
		propHandle.removeItem(rules.size() - 1);
	}

	/**
	 * Tests PropertyCommand#setMember(MemberRef,Object).
	 *
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>setMemeber value is null ,</li>
	 * <li>setMemeber value is empty "" ,</li>
	 * <li>setMember value is the same as old value</li>
	 * <li>Normal case.</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testSetMember() throws Exception {
		// get Style2 sharedstylehandle

		SharedStyleHandle shareHandle = designHandle.findStyle("Style2");//$NON-NLS-1$

		// get map.rules property handle

		PropertyHandle propHandle = shareHandle.getPropertyHandle(Style.MAP_RULES_PROP);
		assertNotNull(propHandle);

		// get first maprule structure

		StructureHandle strHandle = propHandle.getAt(0);
		assertNotNull(strHandle);

		// get display member

		MemberHandle memberHandle = strHandle.getMember("display");//$NON-NLS-1$
		assertEquals("Open", memberHandle.getValue());//$NON-NLS-1$

		// set a null value

		memberHandle.setValue(null);
		assertNull(memberHandle.getValue());

		// set display member

		memberHandle.setValue("hello world"); //$NON-NLS-1$
		assertEquals("hello world", memberHandle.getValue());//$NON-NLS-1$

		// set the same value

		memberHandle.setValue("hello world"); //$NON-NLS-1$
		assertEquals("hello world", memberHandle.getValue());//$NON-NLS-1$

		// set a empty value

		memberHandle = strHandle.getMember(MapRule.VALUE2_MEMBER);

		memberHandle.setValue(""); //$NON-NLS-1$
		assertNull(memberHandle.getValue());
	}

	/**
	 * Tests PropertyCommand#setMember(MemberRef,Object)
	 *
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>Table define a highlight rule list, row inherited it, change the rule
	 * property on row won't affect the value on table.</li>
	 * <li>Label2 extends from label1, and label1 has defined an Action, test that
	 * change the action on label2 won't affect the value on label1.</li>
	 * </ul>
	 *
	 * @throws SemanticException
	 */

	public void testSetMember2() throws SemanticException {
		ElementFactory factory = new ElementFactory(design);

		// Case 1:

		TableHandle table = factory.newTableItem("newTable", 3); //$NON-NLS-1$
		designHandle.getBody().add(table);

		HighlightRule highlightRule = StructureFactory.createHighlightRule();
		highlightRule.setTestExpression("row[\"Company\"]"); //$NON-NLS-1$
		highlightRule.setOperator("eq"); //$NON-NLS-1$
		highlightRule.setValue1("Eclipse"); //$NON-NLS-1$
		highlightRule.setProperty(HighlightRule.COLOR_MEMBER, "red"); //$NON-NLS-1$

		// table has a highlightrule.

		table.getPrivateStyle().getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).addItem(highlightRule);

		// row inherited that rule.

		RowHandle row = (RowHandle) table.getDetail().getContents().get(0);

		List inheritedRules = (List) row.getProperty(StyleHandle.HIGHLIGHT_RULES_PROP);
		assertNull(inheritedRules);

		HighlightRuleHandle rule1 = (HighlightRuleHandle) table.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP)
				.getAt(0);
		assertNotNull(rule1);

		assertEquals("row[\"Company\"]", rule1.getTestExpression()); //$NON-NLS-1$
		assertEquals("eq", rule1.getOperator()); //$NON-NLS-1$
		assertEquals("Eclipse", rule1.getValue1()); //$NON-NLS-1$
		assertEquals("red", (String) rule1.getColor().getValue()); //$NON-NLS-1$

		// now I local change property on the row, ensure that change should be
		// make only in the row itself.

		rule1.getColor().setValue("blue"); //$NON-NLS-1$

		assertEquals("blue", rule1.getMember(HighlightRule.COLOR_MEMBER).getStringValue()); //$NON-NLS-1$
		assertEquals("blue", table.getPropertyHandle( //$NON-NLS-1$
				StyleHandle.HIGHLIGHT_RULES_PROP).getAt(0).getMember(HighlightRule.COLOR_MEMBER).getStringValue());

		// Case 2:

		LabelHandle label1 = factory.newLabel("label1"); //$NON-NLS-1$
		LabelHandle label2 = factory.newLabel("label2"); //$NON-NLS-1$
		designHandle.getComponents().add(label1);
		designHandle.getBody().add(label2);

		label2.setExtendsName("label1"); //$NON-NLS-1$

		Action action = StructureFactory.createAction();
		ActionHandle actionHandle = label1.setAction(action);
		actionHandle.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK);
		actionHandle.setTargetBookmark("BK1"); //$NON-NLS-1$

		ActionHandle inheritedAction = label2.getActionHandle();
		assertNotNull(inheritedAction);
		assertEquals("BK1", inheritedAction.getTargetBookmark()); //$NON-NLS-1$

		// change the action on the child.
		inheritedAction.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
		inheritedAction.setURI("/statistics.html"); //$NON-NLS-1$

		assertEquals("bookmark-link", label1.getActionHandle().getLinkType()); //$NON-NLS-1$
		assertEquals("BK1", label1.getActionHandle().getTargetBookmark()); //$NON-NLS-1$
	}

	/**
	 * Tests PropertyCommand#setMember(MemberRef,Object)
	 *
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>Sets key of sortKey structure to empty string</li>
	 * <li>Throw out SemanticException</li>
	 * </ul>
	 * <ul>
	 * <li>Sets expression of filterCondition structure to empty string</li>
	 * <li>Throw out SemanticException</li>
	 * </ul>
	 *
	 * @throws SemanticException
	 */

	public void testSetMemberForSortAndFilter() throws SemanticException {
		ElementFactory factory = new ElementFactory(design);

		// Case for sort:

		TableHandle tableHandle = factory.newTableItem("newTable", 3); //$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		SortKey sortKey = StructureFactory.createSortKey();
		sortKey.setKey("Key");//$NON-NLS-1$
		PropertyHandle propertyHandle = tableHandle.getPropertyHandle(ListingHandle.SORT_PROP);
		SortKeyHandle sortHandle = (SortKeyHandle) propertyHandle.addItem(sortKey);

		try {
			MemberHandle memberHandle = sortHandle.getMember(SortKey.KEY_MEMBER);
			memberHandle.setStringValue("");//$NON-NLS-1$

			fail("throw out semanticException");//$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}

		assertEquals("Key", sortHandle.getKey());//$NON-NLS-1$

		try {
			sortHandle.setKey("");//$NON-NLS-1$

			fail("throw out semanticException");//$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());

		}
		assertEquals("Key", sortHandle.getKey());//$NON-NLS-1$

		// Case for filter:

		FilterCondition filterCondition = StructureFactory.createFilterCond();
		filterCondition.setExpr("row[\"column\"]");//$NON-NLS-1$
		filterCondition.setOperator("between");//$NON-NLS-1$
		filterCondition.setValue1("1");//$NON-NLS-1$
		filterCondition.setValue2("100");//$NON-NLS-1$
		propertyHandle = tableHandle.getPropertyHandle(ListingHandle.FILTER_PROP);
		FilterConditionHandle conditionHandle = (FilterConditionHandle) propertyHandle.addItem(filterCondition);

		try {
			MemberHandle memberHandle = conditionHandle.getMember(FilterCondition.EXPR_MEMBER);
			memberHandle.setStringValue("");//$NON-NLS-1$

			fail("throw out semanticException");//$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}
		assertEquals("row[\"column\"]", conditionHandle.getExpr());//$NON-NLS-1$
		try {
			conditionHandle.setExpr("");//$NON-NLS-1$

			fail("throw out semanticException");//$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());

		}

		assertEquals("row[\"column\"]", conditionHandle.getExpr());//$NON-NLS-1$
	}

	/**
	 * Invoke
	 * {@link org.eclipse.birt.report.model.command.PropertyCommand#removeAllItems(MemberRef)}
	 * to test removeAllItems method.
	 *
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>use PropertyCommand to remove all maprule items</li>
	 * <li>use PropertyCommand to remove all Drillthrough SearchKeys for an
	 * action.</li>
	 * </ul>
	 *
	 * Analysis:
	 *
	 * In <code>removeAllItems</code>, it must confirm <code>propDefn</code> is an
	 * object of <code>StructListPropertyType</code>.
	 * (PropertyCommand#checkListProperty) While if the property is set to empty,
	 * the value must be validated. (PropertyCommand#validateValue)
	 *
	 * The problem is that <code>StructListPropertyType</code> always throws an
	 * PropertyValueException when it tries to validate the value.
	 * (StructListPropertyType#validateValue)
	 *
	 * This likes conflict.
	 *
	 * @throws SemanticException
	 */

	public void testRemoveAllItems() throws SemanticException {
		// remove first level list(Property)

		SharedStyleHandle shareHandle = designHandle.findStyle("Style2");//$NON-NLS-1$
		PropertyHandle propHandle = shareHandle.getPropertyHandle(Style.MAP_RULES_PROP);
		assertNotNull(propHandle);
		ComplexPropertyCommand command = new ComplexPropertyCommand(design, shareHandle.getElement());
		command.removeAllItems(propHandle.getContext());

		assertNull(shareHandle.getProperty(Style.MAP_RULES_PROP));

		// remove second level list(Member)

		MemberHandle memberHandle = actionHandle.getMember(Action.SEARCH_MEMBER);

		assertEquals(2, memberHandle.getListValue().size());

		command = new ComplexPropertyCommand(design, imageHandle.getElement());
		command.removeAllItems(memberHandle.getContext());

		assertNull(memberHandle.getListValue());
	}

	/**
	 * Tests removing items with position.
	 *
	 * @throws SemanticException if any exception
	 */

	public void testRemoveItemWithPositino() throws SemanticException {
		SharedStyleHandle shareHandle = designHandle.findStyle("Style2");//$NON-NLS-1$
		PropertyHandle propHandle = shareHandle.getPropertyHandle(Style.MAP_RULES_PROP);
		assertNotNull(propHandle);
		List list = propHandle.getListValue();
		assertEquals(2, list.size());
		assertEquals("Open", ((MapRule) list.get(0)).getDisplay()); //$NON-NLS-1$
		assertEquals("Design", ((MapRule) list.get(1)).getDisplay()); //$NON-NLS-1$

		// Delete the second one
		ComplexPropertyCommand command = new ComplexPropertyCommand(design, shareHandle.getElement());
		command.removeItem(new StructureContext(shareHandle.getElement(),
				(ElementPropertyDefn) propHandle.getPropertyDefn(), null), 1);

		list = propHandle.getListValue();
		assertEquals(1, list.size());
		assertEquals("Open", ((MapRule) list.get(0)).getDisplay()); //$NON-NLS-1$

		// Delete the first one
		command = new ComplexPropertyCommand(design, shareHandle.getElement());
		command.removeItem(new StructureContext(shareHandle.getElement(),
				(ElementPropertyDefn) propHandle.getPropertyDefn(), null), 0);

		list = propHandle.getListValue();
		assertNull(list);

		try {
			command = new ComplexPropertyCommand(design, shareHandle.getElement());
			command.removeItem(new StructureContext(shareHandle.getElement(),
					(ElementPropertyDefn) propHandle.getPropertyDefn(), null), 4);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * Tests removing items with item instance.
	 *
	 * @throws PropertyValueException if any exception
	 */

	public void testRemoveItemWithItem() throws PropertyValueException {
		// Reload from design file
		SharedStyleHandle shareHandle = designHandle.findStyle("Style2");//$NON-NLS-1$
		PropertyHandle propHandle = shareHandle.getPropertyHandle(Style.MAP_RULES_PROP);
		assertNotNull(propHandle);
		List list = propHandle.getListValue();
		assertEquals(2, list.size());
		assertEquals("Open", ((MapRule) list.get(0)).getDisplay()); //$NON-NLS-1$
		assertEquals("Design", ((MapRule) list.get(1)).getDisplay()); //$NON-NLS-1$

		// Delete the second one
		ComplexPropertyCommand command = new ComplexPropertyCommand(design, shareHandle.getElement());
		command.removeItem(new StructureContext(shareHandle.getElement(),
				(ElementPropertyDefn) propHandle.getPropertyDefn(), null), (MapRule) list.get(1));

		list = propHandle.getListValue();
		assertEquals(1, list.size());
		assertEquals("Open", ((MapRule) list.get(0)).getDisplay()); //$NON-NLS-1$

		// Delete the first one
		command = new ComplexPropertyCommand(design, shareHandle.getElement());
		command.removeItem(new StructureContext(shareHandle.getElement(),
				(ElementPropertyDefn) propHandle.getPropertyDefn(), null), (MapRule) list.get(0));

		list = propHandle.getListValue();
		assertNull(list);

		try {
			command = new ComplexPropertyCommand(design, shareHandle.getElement());
			command.removeItem(new StructureContext(shareHandle.getElement(),
					(ElementPropertyDefn) propHandle.getPropertyDefn(), null), new MapRule());
			fail();
		} catch (PropertyValueException e) {

		}

	}

	/**
	 * Deal with three situations of add item operation.
	 *
	 * @param style StyleElement which ReportDesign finds
	 * @param type  mark of size of rules is zero or not
	 * @throws SemanticException if any exception.
	 */

	private void AddItemRule(StyleElement style, boolean type) throws SemanticException {
		// create one new MapRule

		MapRule rule = new MapRule();
		PropertyDefn propDefn = (PropertyDefn) rule.getDefn().findProperty(MapRule.DISPLAY_MEMBER);
		rule.setProperty(propDefn, "Rule1"); //$NON-NLS-1$

		// get map.rules property handle and add maprule

		PropertyHandle propHandle = style.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);
		propHandle.addItem(rule);

		// check size

		List rules = (List) style.getProperty(design, Style.MAP_RULES_PROP);
		assertTrue(rules.size() == judgeType(type));

		// undo and check size

		design.getActivityStack().undo();
		rules = (List) style.getProperty(design, Style.MAP_RULES_PROP);
		if (type) {
			assertTrue(rules.size() == 2);
		} else {
			assertNull(rules);
		}

		// redo and check size

		design.getActivityStack().redo();
		rules = (List) style.getProperty(design, Style.MAP_RULES_PROP);
		assertTrue(rules.size() == judgeType(type));
	}

	/**
	 * If type is false mean EmptyList ,then return 1, else return 3.
	 *
	 * @param type mark of size of rules is zero or not
	 *
	 * @return the integer flag
	 */

	private int judgeType(boolean type) {
		if (type) {
			return 3;
		}

		return 1;
	}

	/**
	 * Invoke
	 * {@link org.eclipse.birt.report.model.command.PropertyCommand#replaceItem(MemberRef, IStructure, IStructure)}
	 * .
	 *
	 * <p>
	 * Test Cases:
	 *
	 * <ul>
	 * <li>Execute the command, save the file,compare with a golden file, they
	 * should be identical</li>
	 * <li>Undo the command, save the file,compare with a golden file, should be
	 * </li>
	 * <li>Redo the command, save the file, compare with a golden file, should be
	 * identical</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testReplaceItemOne() throws Exception {
		StyleElement style = design.findStyle("Style2");//$NON-NLS-1$
		MapRule rule = new MapRule();

		// add item operation

		PropertyHandle propHandle = style.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);
		PropertyDefn propDefn = (PropertyDefn) rule.getDefn().findProperty(MapRule.DISPLAY_MEMBER);
		rule.setProperty(propDefn, "RuleReplace");//$NON-NLS-1$
		propHandle.addItem(rule);

		saveOperate("PropertyCommandTest_golden.xml");//$NON-NLS-1$

		// replace item operation

		MapRule ruleNew = new MapRule();
		propDefn = (PropertyDefn) rule.getDefn().findProperty(MapRule.DISPLAY_MEMBER);
		ruleNew.setProperty(propDefn, "NewItem"); //$NON-NLS-1$
		propHandle.replaceItem(rule, ruleNew);
		saveOperate("PropertyCommandTest_golden_1.xml");//$NON-NLS-1$

		// undo , redo to test if the result is equal to excepted value

		design.getActivityStack().undo();
		saveOperate("PropertyCommandTest_golden_2.xml");//$NON-NLS-1$
		design.getActivityStack().redo();
		saveOperate("PropertyCommandTest_golden_1.xml");//$NON-NLS-1$

	}

	/**
	 * Tests replace methods. Test case:
	 * <ul>
	 * <li>get the map rule list from My-Style.</li>
	 * <li>get the map rule list from style2</li>
	 * <li>use the second structure list in My-Style to replace the first structure
	 * list in style2</li>
	 * <li>use the first structure list in style2 to replace the second structure
	 * list in My-Style.</li>
	 *
	 * <li>Replace an item within a member list</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testReplaceItemTwo() throws Exception {
		SharedStyleHandle styleHandle1 = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		SharedStyleHandle styleHandle2 = designHandle.findStyle("Style2"); //$NON-NLS-1$

		ComplexPropertyCommand command1 = new ComplexPropertyCommand(design, styleHandle1.getElement());
		ComplexPropertyCommand command2 = new ComplexPropertyCommand(design, styleHandle2.getElement());

		CommandStack cs = designHandle.getCommandStack();

		Iterator maprules1 = styleHandle1.mapRulesIterator();
		Iterator maprules2 = styleHandle2.mapRulesIterator();

		MapRuleHandle rule1 = (MapRuleHandle) maprules1.next();
		MapRuleHandle rule2 = (MapRuleHandle) maprules2.next();

		// get member reference directly from the property handle

		StructureContext mem1 = styleHandle1.getPropertyHandle(Style.MAP_RULES_PROP).getContext();
		StructureContext mem2 = styleHandle2.getPropertyHandle(Style.MAP_RULES_PROP).getContext();

		// use the rule1 structure to replace rule2 structure

		command2.replaceItem(mem2, rule2.getStructure(), rule1.getStructure());

		// assert whether the value was replaced

		PropertyHandle propHandle = styleHandle2.getPropertyHandle(Style.MAP_RULES_PROP);
		rule2 = ((MapRuleHandle) propHandle.getAt(0));
		assertEquals("Closed", rule2.getDisplay()); //$NON-NLS-1$

		assertTrue(cs.canUndo());
		cs.undo();

		rule2 = ((MapRuleHandle) propHandle.getAt(0));
		assertEquals("Open", rule2.getDisplay()); //$NON-NLS-1$

		// use the sTwoHandle structure to replace sHandle structure
		command1.replaceItem(mem1, rule1.getStructure(), rule2.getStructure());
		propHandle = styleHandle1.getPropertyHandle(Style.MAP_RULES_PROP);
		rule1 = ((MapRuleHandle) propHandle.getAt(0));
		assertEquals("Open", rule1.getDisplay()); //$NON-NLS-1$

		try {
			command1.replaceItem(mem1, new CustomColor(), rule2.getStructure());
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}

		// replace item within list member.

		MemberHandle memberHandle = actionHandle.getMember(Action.SEARCH_MEMBER);

		assertEquals("searchKey1", ((SearchKeyHandle) memberHandle.getAt(0)).getExpression()); //$NON-NLS-1$

		SearchKey key = StructureFactory.createSearchKey();
		key.setExpression("SearchKey3"); //$NON-NLS-1$

		command1 = new ComplexPropertyCommand(design, imageHandle.getElement());
		command1.replaceItem(memberHandle.getContext(), memberHandle.getAt(0).getStructure(), key);

		assertEquals("SearchKey3", ((SearchKeyHandle) memberHandle.getAt(0)).getExpression()); //$NON-NLS-1$
	}

	/**
	 * Save as file and compare text file.
	 *
	 * @param saveFile    storage file
	 * @param compareFile compare file
	 * @throws Exception
	 */

	private void saveOperate(String compareFile) throws Exception {
		save();
		assertTrue(compareFile(compareFile));
	}

	/**
	 * Invoke
	 * {@link org.eclipse.birt.report.model.command.PropertyCommand#insertItem(MemberRef,IStructure, int)}
	 * to test InsertItem method.
	 * <p>
	 * Invoke
	 * {@link org.eclipse.birt.report.model.command.PropertyCommand#removeItem(MemberRef, int)}
	 * to test RemoveItem method
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>insert new item named InsertAndRemove</li>
	 * <li>remove this new item</li>
	 * <li>undo and redo those two operations</li>
	 * <li>remove item from null structure list</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testInsertAndRemoveItem() throws Exception {
		StyleElement style = design.findStyle("Style2"); //$NON-NLS-1$

		// check if original Style2 has two properties

		List rules = (List) style.getProperty(design, Style.MAP_RULES_PROP);
		assertTrue(rules.size() == 2);

		// insert one item to last position

		MapRule rule = new MapRule();
		PropertyDefn propDefn = (PropertyDefn) rule.getDefn().findProperty(MapRule.DISPLAY_MEMBER);
		rule.setProperty(propDefn, "InsertAndRemove"); //$NON-NLS-1$
		PropertyHandle propHandle = style.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);
		propHandle.insertItem(rule, rules.size());
		propHandle.addItem(null);
		CustomColor color = new CustomColor();
		try {
			propHandle.insertItem(color, rules.size());
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE, e.getErrorCode());
		}

		// save check result

		saveOperate("PropertyCommandTest_golden_7.xml");//$NON-NLS-1$

		// remove the last rule.

		propHandle.removeItem(rules.size() - 1);
		rules = (List) style.getProperty(design, Style.MAP_RULES_PROP);
		assertTrue(rules.size() == 2);

		// undo and redo operation

		AddItemRule(style, true);

		// test remove an item from a null structure list.
		// structure list in Style1 is null

		style = design.findStyle("Style1"); //$NON-NLS-1$
		propHandle = style.getHandle(design).getPropertyHandle(Style.MAP_RULES_PROP);

		// rules is null

		rules = (List) style.getProperty(design, Style.MAP_RULES_PROP);
		assertNull(rules);

		// if remove from null list , must throw out PropertyValueException.

		try {
			propHandle.removeItem(0);
			fail(" remove null list should throw out PropertyValueException !"); //$NON-NLS-1$
		} catch (Exception e) {
			assertTrue(e instanceof PropertyValueException);
		}

		// remove second level list(Member)

		// 1. Add one structure to the list member.

		MemberHandle memberHandle = actionHandle.getMember(Action.PARAM_BINDINGS_MEMBER);
		assertNull(memberHandle.getListValue());

		ParamBinding paramBinding = StructureFactory.createParamBinding();
		paramBinding.setParamName("param1"); //$NON-NLS-1$
		paramBinding.setExpression("expr1"); //$NON-NLS-1$

		ComplexPropertyCommand command = new ComplexPropertyCommand(design, imageHandle.getElement());
		command.addItem(memberHandle.getContext(), paramBinding);

		assertEquals(1, memberHandle.getListValue().size());

		// 2. Remove a structure from a list member.

		memberHandle = actionHandle.getMember(Action.SEARCH_MEMBER);
		assertEquals(2, memberHandle.getListValue().size());

		command = new ComplexPropertyCommand(design, imageHandle.getElement());
		command.removeItem(memberHandle.getContext(), 1);

		assertEquals(1, memberHandle.getListValue().size());

		saveOperate("PropertyCommandTest_golden_11.xml");//$NON-NLS-1$

	}

	/**
	 * Invoke
	 * {@link org.eclipse.birt.report.model.command.PropertyCommand#moveItem(MemberRef, int, int)}
	 * to test.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>test move item operation from not null structure list</li>
	 * <li>undo and redo several times</li>
	 * <li>test move item operation from null structure list</li>
	 * <li>Test moving items between list member.</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testMoveItem() throws Exception {
		StyleHandle styleHandle = designHandle.findStyle("Style2"); //$NON-NLS-1$
		ActivityStack as = design.getActivityStack();

		// check if original Style2 has two properties

		List rules = (List) styleHandle.getProperty(Style.MAP_RULES_PROP);
		assertTrue(rules.size() == 2);

		// get map.rules handle

		PropertyHandle propHandle = styleHandle.getPropertyHandle(Style.MAP_RULES_PROP);

		// move item from index zero to index one

		propHandle.moveItem(0, 2);
		saveOperate("PropertyCommandTest_golden_9.xml");//$NON-NLS-1$

		// undo and check result

		as.undo();
		saveOperate("PropertyCommandTest_golden_10.xml");//$NON-NLS-1$

		// redo and check result

		as.redo();
		saveOperate("PropertyCommandTest_golden_9.xml");//$NON-NLS-1$

		// move Item to the end of the list.

		propHandle.moveItem(0, rules.size());
		saveOperate("PropertyCommandTest_golden_10.xml");//$NON-NLS-1$

		// move item from null structure list

		styleHandle = designHandle.findStyle("Style1"); //$NON-NLS-1$
		propHandle = styleHandle.getPropertyHandle(Style.MAP_RULES_PROP);

		try {
			propHandle.moveItem(0, 6);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof PropertyValueException);
		}

		// test the undo of MoveItemListRecord about the address reference about
		// bug 279217
		styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		propHandle = styleHandle.getPropertyHandle(Style.MAP_RULES_PROP);

		as.startTrans(null);

		rules = (List) styleHandle.getProperty(Style.MAP_RULES_PROP);
		propHandle.moveItem(0, 1);
		propHandle.clearValue();
		propHandle.addItem(rules.get(0));
		propHandle.addItem(rules.get(1));
		as.commit();

		as.undo();

		// move item within a member list.

		MemberHandle memberHandle = actionHandle.getMember(Action.SEARCH_MEMBER);
		assertEquals(2, memberHandle.getListValue().size());

		ComplexPropertyCommand command = new ComplexPropertyCommand(design, imageHandle.getElement());
		command.moveItem(memberHandle.getContext(), 0, 2);

		assertEquals("searchKey1", ((SearchKeyHandle) memberHandle.getAt(1)).getExpression()); //$NON-NLS-1$
		assertEquals("searchKey2", ((SearchKeyHandle) memberHandle.getAt(0)).getExpression()); //$NON-NLS-1$

	}

	/**
	 * Tests RemoveItem logic
	 *
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>Table define a highlight rule list, row inherited it, drop the rule on
	 * row won't affect the rules on table.</li> Label2 extends from label1, and
	 * label1 has defined an Action, test that remove a drill through parameter
	 * structure from the action on label2 won't affect the value on label1.
	 * </ul>
	 *
	 * @throws SemanticException
	 */
	public void testRemoveItem() throws SemanticException {
		// Case 1:

		ElementFactory factory = new ElementFactory(design);

		TableHandle table = factory.newTableItem("newTable", 3); //$NON-NLS-1$
		designHandle.getBody().add(table);

		HighlightRule highlightRule = StructureFactory.createHighlightRule();
		highlightRule.setTestExpression("row[\"Company\"]"); //$NON-NLS-1$
		highlightRule.setOperator("eq"); //$NON-NLS-1$
		highlightRule.setValue1("Eclipse"); //$NON-NLS-1$
		highlightRule.setProperty(HighlightRule.COLOR_MEMBER, "red"); //$NON-NLS-1$

		// table has a highlightrule.

		table.getPrivateStyle().getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).addItem(highlightRule);

		// row inherited that rule.

		RowHandle row = (RowHandle) table.getDetail().getContents().get(0);

		List inheritedRules = (List) row.getProperty(StyleHandle.HIGHLIGHT_RULES_PROP);
		assertNull(inheritedRules);

		// remove rule from row, table should not be affected.

		PropertyHandle rulesHandle = row.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);
		// rulesHandle.removeItem( 0 );

		assertFalse(rulesHandle.iterator().hasNext());
		assertEquals(1, table.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).getListValue().size());

		// Case 2:
		LabelHandle label1 = factory.newLabel("label1"); //$NON-NLS-1$
		LabelHandle label2 = factory.newLabel("label2"); //$NON-NLS-1$
		designHandle.getComponents().add(label1);
		designHandle.getBody().add(label2);

		label2.setExtendsName("label1"); //$NON-NLS-1$

		// Action is of drill though type with a parameter binding.

		Action action = StructureFactory.createAction();
		ActionHandle actionHandle = label1.setAction(action);
		actionHandle.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH);

		ParamBinding param = StructureFactory.createParamBinding();
		param.setParamName("p1"); //$NON-NLS-1$
		param.setExpression("A+B"); //$NON-NLS-1$

		actionHandle.addParamBinding(param);

		// Ensure that child label inherits the action.
		ActionHandle inheritedAction = label2.getActionHandle();
		assertNotNull(inheritedAction);
		assertEquals("drill-through", inheritedAction.getLinkType()); //$NON-NLS-1$

		// remove the first parameter binding from child, ensure that parent
		// list won't be affected.

		inheritedAction.getParamBindings().removeItem(0);
		assertEquals(1, actionHandle.getParamBindings().getListValue().size());
	}

	/**
	 *
	 * @throws Exception
	 */
	public void testElementTypePropertyCommand() throws Exception {
		CubeHandle cubeHandle = designHandle.getElementFactory().newTabularCube(null);
		designHandle.getCubes().add(cubeHandle);

		MeasureGroupHandle measureGroupHandle = designHandle.getElementFactory()
				.newTabularMeasureGroup("testMeasureGroup"); //$NON-NLS-1$
		cubeHandle.setProperty(ICubeModel.MEASURE_GROUPS_PROP, measureGroupHandle);
		assertEquals(cubeHandle, measureGroupHandle.getContainer());
		assertEquals(measureGroupHandle.getElement(),
				design.getNameHelper().getNameSpace(Module.CUBE_NAME_SPACE).getElement(measureGroupHandle.getName()));
	}

	/**
	 * The newValue and oldValue in PropertyRecord once be set in constructor, then
	 * should never be changed. This test case is for testing if the old local value
	 * is a list, then we should make a copy for the list, but not point oldValue to
	 * the same list instance with Element. And the undo, redo, rollback status
	 * should be correct.
	 *
	 * @throws Exception
	 */

	public void testProeprtyRecordForListValue() throws Exception {
		createDesign();

		TableHandle table = designHandle.getElementFactory().newTableItem("table"); //$NON-NLS-1$
		designHandle.getBody().add(table);

		ActivityStack stack = (ActivityStack) designHandle.getCommandStack();
		stack.flush();

		ComputedColumn column = new ComputedColumn();
		column.setDataType("string"); //$NON-NLS-1$
		column.setExpression("expression"); //$NON-NLS-1$
		column.setName("name"); //$NON-NLS-1$

		table.addColumnBinding(column, true);

		stack = (ActivityStack) designHandle.getCommandStack();
		assertTrue(stack.canUndo());

		table.getPropertyHandle(TableHandle.BOUND_DATA_COLUMNS_PROP).clearValue();

		assertTrue(stack.canUndo());

		stack.undo();

		assertTrue(stack.canUndo());
		assertTrue(table.columnBindingsIterator().hasNext());

		stack.undo();

		assertFalse(stack.canUndo());
		assertFalse(table.columnBindingsIterator().hasNext());
	}

	/**
	 * Invoke {@link org.eclipse.birt.report.model.api.command.PropertyEvent}to test
	 * property event.
	 * <p>
	 *
	 * Test Case:
	 *
	 * <ul>
	 * <li>basic notification with event test.</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testPropertyEvent() throws Exception {
		StyleElement style = design.findStyle("Style2"); //$NON-NLS-1$

		// check propertyname is Blue He or not

		PropertyEvent event = new PropertyEvent(style, "Blue He");//$NON-NLS-1$
		assertEquals("Blue He", event.getPropertyName());//$NON-NLS-1$

		// change propertyname and check propertyname is Blue or not

		event.setPropertyName("Blue");//$NON-NLS-1$
		assertEquals("Blue", event.getPropertyName());//$NON-NLS-1$
	}

	/**
	 * Unit test for the listener.
	 *
	 * <p>
	 *
	 * Test Case:
	 * <ul>
	 * <li>Use listener to test if notification works or not.</li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testNotification() throws Exception {
		SharedStyleHandle myStyle = designHandle.findStyle("My-Style"); //$NON-NLS-1$

		MyPropertyListener listener = new MyPropertyListener();
		myStyle.addListener(listener);

		// Set property value

		listener.propertyChanged = false;
		myStyle.setProperty("displayName", "hello"); //$NON-NLS-1$//$NON-NLS-2$
		assertTrue(listener.propertyChanged);

		// Clear property value

		listener.propertyChanged = false;
		myStyle.setProperty("displayName", null); //$NON-NLS-1$
		assertTrue(listener.propertyChanged);

		// Set member value

		listener.propertyChanged = false;
		PropertyHandle mapRulesHandle = myStyle.getPropertyHandle(Style.MAP_RULES_PROP);
		StructureHandle mapRuleHandle = mapRulesHandle.getAt(0);
		MemberHandle operatorHandle = mapRuleHandle.getMember(MapRule.OPERATOR_MEMBER);
		operatorHandle.setValue(DesignChoiceConstants.MAP_OPERATOR_BETWEEN);
		assertTrue(listener.propertyChanged);

		// Add structure

		listener.propertyChanged = false;
		MapRule newMapRule = new MapRule();
		newMapRule.setProperty(MapRule.OPERATOR_MEMBER, DesignChoiceConstants.MAP_OPERATOR_LIKE);
		mapRulesHandle.addItem(newMapRule);
		assertTrue(listener.propertyChanged);

		// Replace structure

		listener.propertyChanged = false;
		MapRule anotherNewMapRule = new MapRule();
		anotherNewMapRule.setProperty(MapRule.OPERATOR_MEMBER, DesignChoiceConstants.MAP_OPERATOR_GE);
		mapRulesHandle.replaceItem(newMapRule, anotherNewMapRule);
		assertTrue(listener.propertyChanged);

		// Move structure to the end.

		listener.propertyChanged = false;
		mapRulesHandle.moveItem(0, 2);
		assertTrue(listener.propertyChanged);

		// Remove structure

		listener.propertyChanged = false;
		mapRulesHandle.removeItem(0);
		assertTrue(listener.propertyChanged);

	}

	/**
	 * Tests event notification when variable element property changed.
	 *
	 * @throws Exception
	 */
	public void testVariableElementPropertyNotification() throws Exception {
		openDesign("VariableElementPropertyNotificationTest.xml"); //$NON-NLS-1$

		MyPropertyListener listener = new MyPropertyListener();

		designHandle.addListener(listener);

		List<VariableElementHandle> list = designHandle.getPageVariables();

		VariableElementHandle variableElementHandle = list.get(0);
		variableElementHandle.setName("test"); //$NON-NLS-1$
		assertTrue(listener.propertyChanged);

		listener.propertyChanged = false;
		variableElementHandle = list.get(1);
		variableElementHandle.setType(DesignChoiceConstants.VARIABLE_TYPE_REPORT);
		assertTrue(listener.propertyChanged);

	}

	/**
	 * When drops TOC/highlightRule, the corresponding back reference on the style
	 * elements should be removed. Otherwise, it may cause NPE during the style
	 * broadcast process.
	 * <p>
	 * Uses TOC as examples to test this feature. see Bug 286598
	 *
	 * @throws Exception
	 */

	public void testBackRefWhenDropStructure() throws Exception {
		openDesign("PropertyCommandTest_TOC.xml"); //$NON-NLS-1$

		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		StyleHandle style = designHandle.findStyle("NewStyle"); //$NON-NLS-1$

		// make sure there is one reference.

		Iterator<BackRef> iter1 = style.clientsIterator();
		assertTrue(iter1.hasNext());

		TOC toc = StructureFactory.createTOC("test1 toc");
		label1.addTOC(toc);

		// if the toc is dropped and set with a new one. There is no back
		// reference.

		iter1 = style.clientsIterator();
		assertFalse(iter1.hasNext());
	}

	class MyPropertyListener implements Listener {

		boolean propertyChanged = false;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			propertyChanged = true;
		}

	}

}
