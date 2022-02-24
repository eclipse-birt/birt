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

package org.eclipse.birt.report.model.extension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ArgumentInfoList;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests the extension pointer of org.eclipse.birt.report.model.reportItem.
 * <p>
 * New cases should be added into PeerExtensionTest.
 */
public class ReportItemExtensionTest extends BaseTestCase {

	String fileName = "ExtensionTest.xml"; //$NON-NLS-1$
	String goldenFileName_1 = "ExtensionTest_golden_1.xml"; //$NON-NLS-1$
	String goldenFileName_2 = "ExtensionTest_golden_2.xml"; //$NON-NLS-1$
	String goldenFileName_3 = "ExtensionTest_golden_3.xml"; //$NON-NLS-1$
	String checkFileName = "ExtensionTest_1.xml"; //$NON-NLS-1$
	String fileName_2 = "ExtensionTest_2.xml"; //$NON-NLS-1$
	String fileName_3 = "ExtensionTest_3.xml"; //$NON-NLS-1$
	String fileName_4 = "ExtensionTest_4.xml"; //$NON-NLS-1$
	String fileName_5 = "ExtensionTest_5.xml"; //$NON-NLS-1$
	String fileName_6 = "ExtensionTest_6.xml"; //$NON-NLS-1$
	String fileName_7 = "ExtensionTest_7.xml"; //$NON-NLS-1$
	String fileName_8 = "ExtensionTest_8.xml"; //$NON-NLS-1$
	String fileName_9 = "ExtensionTest_9.xml"; //$NON-NLS-1$
	String fileName_10 = "ExtensionTest_10.xml"; //$NON-NLS-1$

	protected static final String TESTING_MATRIX_NAME = "TestingMatrix"; //$NON-NLS-1$
	protected static final String TESTING_BOX_NAME = "TestingBox"; //$NON-NLS-1$

	private static final String TESTING_TABLE_NAME = "TestingTable"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ThreadResources.setLocale(ULocale.ENGLISH);
	}

	/**
	 * Tests the parser for the extension and the TestPeer--implementation of IPeer.
	 */
	public void testExtensionMeta() throws Exception {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();

		assertTrue(dd.getExtensions().size() >= 2);

		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(TESTING_MATRIX_NAME);
		assertNotNull(extDefn);
		assertEquals("TestingMatrix", extDefn.getDisplayName()); //$NON-NLS-1$
		assertEquals("Element.TestingMatrix", extDefn.getDisplayNameKey()); //$NON-NLS-1$
		assertEquals(TESTING_MATRIX_NAME, extDefn.getName());
		assertEquals(MetaDataConstants.REQUIRED_NAME, extDefn.getNameOption());
		assertEquals(true, extDefn.allowsUserProperties());

		assertEquals(TESTING_MATRIX_NAME, extDefn.getName());
		List<IElementPropertyDefn> propList = extDefn.getProperties();

		assertNotNull(extDefn.getProperty(ExtendedItem.EXTENSION_NAME_PROP));

		ElementPropertyDefn prop = (ElementPropertyDefn) propList.get(0);
		assertEquals(prop, extDefn.getProperty("test1")); //$NON-NLS-1$
		assertEquals("test1", prop.getName()); //$NON-NLS-1$
		assertEquals("Test1", prop.getDisplayName()); //$NON-NLS-1$
		assertEquals(PropertyType.STRING_TYPE, prop.getTypeCode());
		assertEquals(false, prop.isList());
		assertEquals(null, prop.getGroupName());
		assertEquals(null, prop.getChoices());
		assertEquals(null, prop.getStructDefn());
		assertEquals("default test&\"<", prop.getDefault()); //$NON-NLS-1$
		assertEquals(true, prop.canInherit());

		prop = (ElementPropertyDefn) propList.get(1);
		assertEquals(prop, extDefn.getProperty("test2")); //$NON-NLS-1$
		assertEquals("test2", prop.getName()); //$NON-NLS-1$
		assertEquals("Test2", prop.getDisplayName()); //$NON-NLS-1$
		assertEquals(PropertyType.INTEGER_TYPE, prop.getTypeCode());
		assertTrue(extDefn.isPropertyVisible("test2")); //$NON-NLS-1$
		assertEquals(false, prop.isList());
		assertEquals(null, prop.getGroupName());
		assertEquals(null, prop.getChoices());
		assertEquals(null, prop.getStructDefn());
		assertEquals(new Integer("90"), prop.getDefault()); //$NON-NLS-1$
		assertEquals(false, prop.canInherit());

		prop = (ElementPropertyDefn) propList.get(2);
		assertEquals(prop, extDefn.getProperty("test3")); //$NON-NLS-1$
		assertEquals("test3", prop.getName()); //$NON-NLS-1$
		assertEquals("Test3", prop.getDisplayName()); //$NON-NLS-1$
		assertEquals(PropertyType.XML_TYPE, prop.getTypeCode());
		assertFalse(extDefn.isPropertyVisible(prop.getName()));
		assertEquals(false, prop.isList());
		assertEquals(null, prop.getGroupName());
		assertEquals(null, prop.getChoices());
		assertEquals(null, prop.getStructDefn());
		assertEquals(null, prop.getDefault());
		assertEquals(true, prop.canInherit());
		// if the extension model property definition type is xml, the string
		// will be trimmed.
		assertNull(prop.validateValue(design, null, "")); //$NON-NLS-1$
		assertNull(prop.validateValue(design, null, "  ")); //$NON-NLS-1$
		assertEquals("test", prop.validateValue(design, null, " test ").toString());//$NON-NLS-1$ //$NON-NLS-2$

		prop = (ElementPropertyDefn) propList.get(3);
		assertEquals(prop, extDefn.getProperty("test5")); //$NON-NLS-1$
		assertEquals("test5", prop.getName()); //$NON-NLS-1$
		assertEquals("Test5", prop.getDisplayName()); //$NON-NLS-1$
		assertEquals(PropertyType.CHOICE_TYPE, prop.getTypeCode());
		assertEquals(false, prop.isList());
		assertEquals(null, prop.getGroupName());
		assertEquals(null, prop.getStructDefn());
		assertEquals(null, prop.getDefault());
		assertEquals(false, prop.canInherit());

		IChoiceSet choiceSet = prop.getChoices();
		assertEquals(3, choiceSet.getChoices().length);
		IChoice[] choices = choiceSet.getChoices();
		assertEquals("choice1", choices[0].getName()); //$NON-NLS-1$
		assertEquals("one", choices[0].getValue()); //$NON-NLS-1$
		assertEquals("Choices.test5.choice1", choices[0].getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("Choice One", choices[0].getDisplayName()); //$NON-NLS-1$
		assertEquals("choice2", choices[1].getName()); //$NON-NLS-1$
		assertEquals("two", choices[1].getValue()); //$NON-NLS-1$
		assertEquals("Choices.test5.choice2", choices[1].getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("Choice Two", choices[1].getDisplayName()); //$NON-NLS-1$
		assertEquals("choice3", choices[2].getName()); //$NON-NLS-1$
		assertEquals("three", choices[2].getValue()); //$NON-NLS-1$
		assertEquals("Choices.test5.choice3", choices[2].getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("choice3", choices[2].getDisplayName()); //$NON-NLS-1$

		prop = (ElementPropertyDefn) extDefn.getProperty("test6"); //$NON-NLS-1$
		assertEquals("Group 1", prop.getGroupName()); //$NON-NLS-1$

		prop = (ElementPropertyDefn) extDefn.getProperty("test7"); //$NON-NLS-1$
		assertEquals("Group 1", prop.getGroupName()); //$NON-NLS-1$

		List<IElementPropertyDefn> methods = extDefn.getMethods();
		ElementPropertyDefn methodProp = (ElementPropertyDefn) methods.get(0);
		IMethodInfo method = methodProp.getMethodInfo();

		assertNotNull(method);
		assertEquals("afterCloseDoc", method.getName()); //$NON-NLS-1$

		assertEquals("Element.TestingMatrix.afterCloseDoc", method.getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("Element.TestingMatrix.afterCloseDoc.toolTip", method.getToolTipKey()); //$NON-NLS-1$
		assertEquals("string", method.getReturnType()); //$NON-NLS-1$

		Iterator<IArgumentInfoList> iter = method.argumentListIterator();
		ArgumentInfoList argumentList = (ArgumentInfoList) iter.next();

		IArgumentInfo arg = argumentList.getArgument("reportContext"); //$NON-NLS-1$
		assertNotNull(arg);
		assertEquals("Element.TestingMatrix.afterCloseDoc.reportContext", arg.getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("IReportContext", arg.getType()); //$NON-NLS-1$
		arg = argumentList.getArgument("object"); //$NON-NLS-1$
		assertNotNull(arg);
		assertEquals("Element.TestingMatrix.afterCloseDoc.object", arg.getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("Object", arg.getType()); //$NON-NLS-1$

		// prop = (IPropertyDefinition) propList.get( 3 );
		// assertEquals( prop, peer.getPropertyDefn( "test4" ) ); //$NON-NLS-1$
		// assertEquals( "test4", prop.getName( ) ); //$NON-NLS-1$
		// assertEquals( "Element.Test.test4", prop.getDisplayName( ) );
		// //$NON-NLS-1$
		// assertEquals( PropertyType.STRUCT_TYPE, prop.getType( ) );
		// assertEquals( true, prop.isList( ) );
		// assertEquals( null, prop.getGroupName( ) );
		// assertEquals( null, prop.getChoices( ) );
		// assertEquals( 3, prop.getMembers( ).size( ) );
		// assertEquals( null, prop.getDefaultValue( ) );
		// assertEquals( true, prop.canInherit( ) );
		//
		// // test the members of the structure list property
		// List members = prop.getMembers( );
		//
		// prop = (IPropertyDefinition) members.get( 0 );
		// assertEquals( "member1", prop.getName( ) ); //$NON-NLS-1$
		// assertEquals( "Structure.test4.member1", prop.getDisplayName( ) );
		// //$NON-NLS-1$
		// assertEquals( PropertyType.DIMENSION_TYPE, prop.getType( ) );
		// assertEquals( false, prop.isList( ) );
		// assertEquals( null, prop.getGroupName( ) );
		// assertEquals( null, prop.getChoices( ) );
		// assertEquals( null, prop.getMembers( ) );
		// assertEquals( null, prop.getDefaultValue( ) );
		//
		// prop = (IPropertyDefinition) members.get( 1 );
		// assertEquals( "member2", prop.getName( ) ); //$NON-NLS-1$
		// assertEquals( "Structure.test4.member2", prop.getDisplayName( ) );
		// //$NON-NLS-1$
		// assertEquals( PropertyType.STRUCT_TYPE, prop.getType( ) );
		// assertEquals( false, prop.isList( ) );
		// assertEquals( null, prop.getGroupName( ) );
		// assertEquals( null, prop.getChoices( ) );
		// assertEquals( null, prop.getMembers( ) );
		// assertEquals( null, prop.getDefaultValue( ) );
		//
		// prop = (IPropertyDefinition) members.get( 2 );
		// assertEquals( "member3", prop.getName( ) ); //$NON-NLS-1$
		// assertEquals( "Structure.test4.member3", prop.getDisplayName( ) );
		// //$NON-NLS-1$
		// assertEquals( PropertyType.XML_TYPE, prop.getType( ) );
		// assertEquals( false, prop.isList( ) );
		// assertEquals( null, prop.getGroupName( ) );
		// assertEquals( null, prop.getChoices( ) );
		// assertEquals( null, prop.getMembers( ) );
		// assertEquals( null, prop.getDefaultValue( ) );
		//
		// // end of the member test
	}

	/**
	 * Tests the function of TestPeerElement and ExtendedItem to get/set property.
	 * 
	 * @throws Exception
	 */

	public void testExtension() throws Exception {
		openDesign(fileName);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		try {
			extendedHandle.setProperty(ExtendedItem.EXTENSION_NAME_PROP, "TestingMatrix"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_EXTENSION_SETTING_FORBIDDEN, e.getErrorCode());
		}

		// test the property values

		assertNotNull(((ExtendedItem) extendedHandle.getElement()).getExtendedElement());
		assertEquals(TESTING_MATRIX_NAME, extendedHandle.getExtensionName());
		assertEquals(1.2, extendedHandle.getX().getMeasure(), 0.00);
		assertEquals("2in", extendedHandle.getProperty("test1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(22, extendedHandle.getIntProperty("test2")); //$NON-NLS-1$
		assertEquals("type=bar,xScale=2,yScale=3,lineStyle=normal,script=internalScript", //$NON-NLS-1$
				extendedHandle.getProperty("test3")); //$NON-NLS-1$
		assertNull(extendedHandle.getProperty("test4")); //$NON-NLS-1$
		assertEquals("choice1", extendedHandle.getProperty("test5")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("script of afterCloseDoc", extendedHandle //$NON-NLS-1$
				.getProperty("afterCloseDoc"));//$NON-NLS-1$

		// read filter properties

		Iterator<?> iter1 = extendedHandle.filtersIterator();
		FilterConditionHandle filter = (FilterConditionHandle) iter1.next();
		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_LT, filter.getOperator());
		assertEquals("filter expression", filter.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", filter.getValue1()); //$NON-NLS-1$

		filter = (FilterConditionHandle) iter1.next();
		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_GE, filter.getOperator());
		assertEquals("expr", filter.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expr", filter.getValue1()); //$NON-NLS-1$

		assertNull(iter1.next());

		// set the property
		String yPropStr = new NumberFormatter(extendedHandle.getModule().getLocale()).format(11.2) + "in"; // $NON-NLS-1$
		extendedHandle.setProperty(ExtendedItem.Y_PROP, yPropStr);
		extendedHandle.setProperty("test5", "choice2"); //$NON-NLS-1$//$NON-NLS-2$

		try {
			extendedHandle.setProperty("test5", "wrong choice"); //$NON-NLS-1$//$NON-NLS-2$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		filter.setExpr("new expr"); //$NON-NLS-1$
		filter.setValue2("new value 2"); //$NON-NLS-1$

		extendedHandle.loadExtendedElement();
		assertNotNull(((ExtendedItem) extendedHandle.getElement()).getExtendedElement());
		assertEquals("type=bar,xScale=2,yScale=3,lineStyle=normal,script=internalScript", //$NON-NLS-1$
				extendedHandle.getProperty("test3")); //$NON-NLS-1$
		assertEquals("type=bar,xScale=2,yScale=3,lineStyle=normal,script=internalScript", //$NON-NLS-1$
				((ExtendedItem) extendedHandle.getElement()).getProperty(design, "test3")); //$NON-NLS-1$
		extendedHandle.setProperty("afterCloseDoc", //$NON-NLS-1$
				"new script of afterCloseDoc"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(goldenFileName_1));

		// after writer, read the golden file and test the properties twice

		openDesign(checkFileName);
		extendedHandle = null;
		extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		// test the property values

		assertEquals(1.2, extendedHandle.getX().getMeasure(), 0.00);
		assertEquals("2in", extendedHandle.getProperty("test1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(22, extendedHandle.getIntProperty("test2")); //$NON-NLS-1$
		assertEquals("type=bar,xScale=2,yScale=3,lineStyle=normal", extendedHandle.getProperty("test3")); //$NON-NLS-1$//$NON-NLS-2$

		// clear some property values

		extendedHandle.loadExtendedElement();
		extendedHandle.setProperty("test1", null); //$NON-NLS-1$
		save();
		assertTrue(compareFile(goldenFileName_2));
	}

	/**
	 * Tests the inheritance cases.
	 * 
	 * @throws Exception
	 */

	public void testExtends() throws Exception {
		openDesign(fileName);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		// test the extends case, that the parent is not the same extension
		// type as the target element

		ExtendedItem parent = (ExtendedItem) extendedHandle.getElementFactory()
				.newExtendedItem("parent", TESTING_BOX_NAME).getElement(); //$NON-NLS-1$

		try {
			designHandle.addElement(parent.handle(design), ReportDesign.BODY_SLOT);
			extendedHandle.setExtends(parent.handle(design));
			fail();
		} catch (ExtendsException e) {
			assertEquals(WrongTypeException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());
		}

		// creates a new extended item by element factory and
		// set-extends and succeed

		ExtendedItemHandle child = designHandle.getElementFactory().newExtendedItem("child", TESTING_MATRIX_NAME); //$NON-NLS-1$
		assertNotNull(child);
		assertEquals(TESTING_MATRIX_NAME, child.getExtensionName());
		assertEquals(TESTING_MATRIX_NAME, extendedHandle.getExtensionName());

		parent = (ExtendedItem) extendedHandle.getElementFactory().newExtendedItem("new parent", TESTING_MATRIX_NAME) //$NON-NLS-1$
				.getElement();

		designHandle.addElement(parent.handle(design), ReportDesign.COMPONENT_SLOT);
		child.setExtends(parent.handle(design));

		// parse the design file and find the wrong extends case.

		try {
			openDesign(fileName_2);
			fail();
		} catch (DesignFileException e) {

			assertEquals(DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR, e.getErrorCode());

			List<ErrorDetail> errors = e.getErrorList();

			assertEquals(2, errors.size());

			assertEquals(WrongTypeException.DESIGN_EXCEPTION_WRONG_TYPE, ((ErrorDetail) errors.get(0)).getErrorCode());
			assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION,
					((ErrorDetail) errors.get(1)).getErrorCode());
		}
	}

	/**
	 * Tests the property search. TODO: test getFactoryProperty.
	 * 
	 * @throws Exception
	 */

	public void testGetProperty() throws Exception {
		openDesign(fileName_3);

		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		assertEquals(1.2, extendedHandle.getX().getMeasure(), 0.0);
		assertEquals(2.4, extendedHandle.getY().getMeasure(), 0.0);

		// test1 property can inherit

		assertEquals("parent value", extendedHandle.getProperty("test1")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(null, extendedHandle.getElement().getLocalProperty(design, "test1")); //$NON-NLS-1$

		// test5 property can not inherit

		ExtendedItemHandle parent = (ExtendedItemHandle) designHandle.findElement("parent"); //$NON-NLS-1$
		assertEquals(parent, extendedHandle.getExtends());

		assertEquals("choice2", parent.getProperty("test5")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("choice1", extendedHandle.getProperty("test5")); //$NON-NLS-1$//$NON-NLS-2$

	}

	/**
	 * Tests all the style property definition and style property values in
	 * ExtendedItem.
	 * 
	 * @throws Exception
	 */

	public void testStyleProperty() throws Exception {
		openDesign(fileName_3);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		// test style property search algorithm

		// get style property from local

		assertEquals("white", extendedHandle.getStringProperty(Style.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$

		// get style property from container

		assertEquals(ColorPropertyType.BLUE, extendedHandle.getStringProperty(Style.COLOR_PROP));

		// get style property from pre-defined style

		assertEquals("normal", extendedHandle.getStringProperty(Style.FONT_VARIANT_PROP)); //$NON-NLS-1$

		// test all style property values from private style

		extendedHandle = (ExtendedItemHandle) designHandle.findElement("style extended item"); //$NON-NLS-1$

		// test get the style property definition from handle and from the
		// extended item directly.
		assertNotNull(extendedHandle.getDefn().getProperty(Style.FONT_FAMILY_PROP));

		assertNotNull(extendedHandle.getElement().getProperty(design, Style.FONT_FAMILY_PROP));

		assertEquals("fantasy", extendedHandle.getStringProperty(Style.FONT_FAMILY_PROP)); //$NON-NLS-1$
		assertEquals("red", extendedHandle.getStringProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("larger", extendedHandle.getStringProperty(Style.FONT_SIZE_PROP)); //$NON-NLS-1$
		assertEquals("italic", extendedHandle.getStringProperty(Style.FONT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("normal", extendedHandle.getStringProperty(Style.FONT_VARIANT_PROP)); //$NON-NLS-1$
		assertEquals("bold", extendedHandle.getStringProperty(Style.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("line-through", extendedHandle.getStringProperty(Style.TEXT_LINE_THROUGH_PROP)); //$NON-NLS-1$
		assertEquals("overline", extendedHandle.getStringProperty(Style.TEXT_OVERLINE_PROP)); //$NON-NLS-1$
		assertEquals("underline", extendedHandle.getStringProperty(Style.TEXT_UNDERLINE_PROP)); //$NON-NLS-1$

		assertEquals("dotted", extendedHandle.getStringProperty(Style.BORDER_TOP_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", extendedHandle.getStringProperty(Style.BORDER_TOP_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("blue", extendedHandle.getStringProperty(Style.BORDER_TOP_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("dashed", extendedHandle.getStringProperty(Style.BORDER_LEFT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", extendedHandle.getStringProperty(Style.BORDER_LEFT_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("green", extendedHandle.getStringProperty(Style.BORDER_LEFT_COLOR_PROP)); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID,
				extendedHandle.getStringProperty(Style.BORDER_BOTTOM_STYLE_PROP));
		assertEquals("thin", extendedHandle.getStringProperty(Style.BORDER_BOTTOM_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("red", extendedHandle.getStringProperty(Style.BORDER_BOTTOM_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("double", extendedHandle.getStringProperty(Style.BORDER_RIGHT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", extendedHandle.getStringProperty(Style.BORDER_RIGHT_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("maroon", extendedHandle.getStringProperty(Style.BORDER_RIGHT_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("1mm", extendedHandle.getStringProperty(Style.PADDING_TOP_PROP)); //$NON-NLS-1$
		assertEquals("2mm", extendedHandle.getStringProperty(Style.PADDING_LEFT_PROP)); //$NON-NLS-1$
		assertEquals("3mm", extendedHandle.getStringProperty(Style.PADDING_RIGHT_PROP)); //$NON-NLS-1$
		assertEquals("4mm", extendedHandle.getStringProperty(Style.PADDING_BOTTOM_PROP)); //$NON-NLS-1$

		assertEquals("scroll", extendedHandle.getStringProperty(Style.BACKGROUND_ATTACHMENT_PROP)); //$NON-NLS-1$
		assertEquals("red", extendedHandle.getStringProperty(Style.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$
		assertEquals("file", extendedHandle.getStringProperty(Style.BACKGROUND_IMAGE_PROP)); //$NON-NLS-1$
		assertEquals("center", extendedHandle.getStringProperty(Style.BACKGROUND_POSITION_X_PROP)); //$NON-NLS-1$
		assertEquals("top", extendedHandle.getStringProperty(Style.BACKGROUND_POSITION_Y_PROP)); //$NON-NLS-1$
		assertEquals("repeat", extendedHandle.getStringProperty(Style.BACKGROUND_REPEAT_PROP)); //$NON-NLS-1$

		assertEquals("right", extendedHandle.getStringProperty(Style.TEXT_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("5mm", extendedHandle.getStringProperty(Style.TEXT_INDENT_PROP)); //$NON-NLS-1$
		assertEquals("normal", extendedHandle.getStringProperty(Style.LETTER_SPACING_PROP)); //$NON-NLS-1$
		assertEquals("normal", extendedHandle.getStringProperty(Style.LINE_HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("19", extendedHandle.getStringProperty(Style.ORPHANS_PROP)); //$NON-NLS-1$
		assertEquals("uppercase", extendedHandle.getStringProperty(Style.TEXT_TRANSFORM_PROP)); //$NON-NLS-1$
		assertEquals("middle", extendedHandle.getStringProperty(Style.VERTICAL_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("nowrap", extendedHandle.getStringProperty(Style.WHITE_SPACE_PROP)); //$NON-NLS-1$
		assertEquals("12", extendedHandle.getStringProperty(Style.WIDOWS_PROP)); //$NON-NLS-1$
		assertEquals("normal", extendedHandle.getStringProperty(Style.WORD_SPACING_PROP)); //$NON-NLS-1$

		assertEquals("inline", extendedHandle.getStringProperty(Style.DISPLAY_PROP)); //$NON-NLS-1$
		assertEquals("My Page", extendedHandle.getStringProperty(Style.MASTER_PAGE_PROP)); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO,
				extendedHandle.getStringProperty(Style.PAGE_BREAK_AFTER_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_AUTO,
				extendedHandle.getStringProperty(Style.PAGE_BREAK_BEFORE_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO,
				extendedHandle.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));
		assertEquals("true", extendedHandle.getStringProperty(Style.SHOW_IF_BLANK_PROP)); //$NON-NLS-1$
		assertEquals("true", extendedHandle.getStringProperty(Style.CAN_SHRINK_PROP)); //$NON-NLS-1$

		assertEquals("right", extendedHandle.getStringProperty(Style.NUMBER_ALIGN_PROP)); //$NON-NLS-1$

		assertEquals("auto", extendedHandle.getStringProperty(Style.MARGIN_TOP_PROP)); //$NON-NLS-1$
		assertEquals("auto", extendedHandle.getStringProperty(Style.MARGIN_LEFT_PROP)); //$NON-NLS-1$
		assertEquals("auto", extendedHandle.getStringProperty(Style.MARGIN_RIGHT_PROP)); //$NON-NLS-1$
		assertEquals("auto", extendedHandle.getStringProperty(Style.MARGIN_BOTTOM_PROP)); //$NON-NLS-1$

		// assertEquals(
		// "[somefield]", extendedHandle.getStringProperty(
		// Style.MAP_TEST_EXPR_PROP ) ); //$NON-NLS-1$

		List<?> mapRules = (List<?>) extendedHandle.getProperty(Style.MAP_RULES_PROP);
		assertEquals(5, mapRules.size());
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ((MapRule) mapRules.get(0)).getOperator());
		assertEquals("Closed", ((MapRule) mapRules.get(0)).getDisplay()); //$NON-NLS-1$

		// assertEquals("[somefield]",( (MapRule) mapRules.get( 0 )
		// ).getTestExpression() ); //$NON-NLS-1$
		assertEquals("\"X\"", ((MapRule) mapRules.get(0)).getValue1()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.MAP_OPERATOR_TRUE, ((MapRule) mapRules.get(1)).getOperator());
		assertEquals("Open", ((MapRule) mapRules.get(1)).getDisplay()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.MAP_OPERATOR_LIKE, ((MapRule) mapRules.get(2)).getOperator());
		assertEquals("Unknown", ((MapRule) mapRules.get(2)).getDisplay()); //$NON-NLS-1$

		NameSpace ns = design.getNameHelper().getNameSpace(Module.STYLE_NAME_SPACE);
		assertEquals(3, ns.getCount());

		StyleHandle sh = extendedHandle.getPrivateStyle();
		Iterator<?> iter = sh.mapRulesIterator();
		assertNotNull(iter.next());
		assertNotNull(iter.next());
		assertNotNull(iter.next());

		ArrayList<ExtendedItemHandle> list = new ArrayList<ExtendedItemHandle>();

		list.add(extendedHandle);

		GroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, list);

		assertEquals("center", groupHandle.getStringProperty(Style.BACKGROUND_POSITION_X_PROP)); //$NON-NLS-1$

		extendedHandle = (ExtendedItemHandle) designHandle.findElement("predefined style item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		assertEquals(ColorPropertyType.BLUE, extendedHandle.getProperty(Style.COLOR_PROP));

		// drop the "testing-matrix" selector,

		designHandle.getStyles().drop(2);

		assertEquals(ColorPropertyType.BLACK, extendedHandle.getProperty(Style.COLOR_PROP));

		save();
		assertTrue(compareFile(goldenFileName_3));

	}

	/**
	 * @throws DesignFileException
	 */

	public void testStylePropertiesFromGroupElementHandle() throws DesignFileException {

		openDesign(fileName_3);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		ArrayList<ExtendedItemHandle> list = new ArrayList<ExtendedItemHandle>();

		list.add(extendedHandle);

		GroupElementHandle groupHandle = new SimpleGroupElementHandle(designHandle, list);

		// test style property search algorithm

		// get style property from local

		assertEquals("white", groupHandle.getStringProperty(Style.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$

		// get style property from container

		assertEquals(ColorPropertyType.BLUE, groupHandle.getStringProperty(Style.COLOR_PROP));

		// get style property from pre-defined style

		assertEquals("normal", groupHandle.getStringProperty(Style.FONT_VARIANT_PROP)); //$NON-NLS-1$

		// test all style property values from private style

		extendedHandle = (ExtendedItemHandle) designHandle.findElement("style extended item"); //$NON-NLS-1$

		list.clear();
		list.add(extendedHandle);
		groupHandle = new SimpleGroupElementHandle(designHandle, list);

		assertEquals("fantasy", groupHandle.getStringProperty(Style.FONT_FAMILY_PROP)); //$NON-NLS-1$
		assertEquals("red", groupHandle.getStringProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("larger", groupHandle.getStringProperty(Style.FONT_SIZE_PROP)); //$NON-NLS-1$
		assertEquals("italic", groupHandle.getStringProperty(Style.FONT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("normal", groupHandle.getStringProperty(Style.FONT_VARIANT_PROP)); //$NON-NLS-1$
		assertEquals("bold", groupHandle.getStringProperty(Style.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("line-through", groupHandle.getStringProperty(Style.TEXT_LINE_THROUGH_PROP)); //$NON-NLS-1$
		assertEquals("overline", groupHandle.getStringProperty(Style.TEXT_OVERLINE_PROP)); //$NON-NLS-1$
		assertEquals("underline", groupHandle.getStringProperty(Style.TEXT_UNDERLINE_PROP)); //$NON-NLS-1$

		assertEquals("dotted", groupHandle.getStringProperty(Style.BORDER_TOP_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", groupHandle.getStringProperty(Style.BORDER_TOP_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("blue", groupHandle.getStringProperty(Style.BORDER_TOP_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("dashed", groupHandle.getStringProperty(Style.BORDER_LEFT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", groupHandle.getStringProperty(Style.BORDER_LEFT_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("green", groupHandle.getStringProperty(Style.BORDER_LEFT_COLOR_PROP)); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID,
				groupHandle.getStringProperty(Style.BORDER_BOTTOM_STYLE_PROP));
		assertEquals("thin", groupHandle.getStringProperty(Style.BORDER_BOTTOM_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("red", groupHandle.getStringProperty(Style.BORDER_BOTTOM_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("double", groupHandle.getStringProperty(Style.BORDER_RIGHT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", groupHandle.getStringProperty(Style.BORDER_RIGHT_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("maroon", groupHandle.getStringProperty(Style.BORDER_RIGHT_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("1mm", groupHandle.getStringProperty(Style.PADDING_TOP_PROP)); //$NON-NLS-1$
		assertEquals("2mm", groupHandle.getStringProperty(Style.PADDING_LEFT_PROP)); //$NON-NLS-1$
		assertEquals("3mm", groupHandle.getStringProperty(Style.PADDING_RIGHT_PROP)); //$NON-NLS-1$
		assertEquals("4mm", groupHandle.getStringProperty(Style.PADDING_BOTTOM_PROP)); //$NON-NLS-1$

		assertEquals("scroll", groupHandle.getStringProperty(Style.BACKGROUND_ATTACHMENT_PROP)); //$NON-NLS-1$
		assertEquals("red", groupHandle.getStringProperty(Style.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$
		assertEquals("file", groupHandle.getStringProperty(Style.BACKGROUND_IMAGE_PROP)); //$NON-NLS-1$
		assertEquals("center", groupHandle.getStringProperty(Style.BACKGROUND_POSITION_X_PROP)); //$NON-NLS-1$
		assertEquals("top", groupHandle.getStringProperty(Style.BACKGROUND_POSITION_Y_PROP)); //$NON-NLS-1$
		assertEquals("repeat", groupHandle.getStringProperty(Style.BACKGROUND_REPEAT_PROP)); //$NON-NLS-1$

		assertEquals("right", groupHandle.getStringProperty(Style.TEXT_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("5mm", groupHandle.getStringProperty(Style.TEXT_INDENT_PROP)); //$NON-NLS-1$
		assertEquals("normal", groupHandle.getStringProperty(Style.LETTER_SPACING_PROP)); //$NON-NLS-1$
		assertEquals("normal", groupHandle.getStringProperty(Style.LINE_HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("19", groupHandle.getStringProperty(Style.ORPHANS_PROP)); //$NON-NLS-1$
		assertEquals("uppercase", groupHandle.getStringProperty(Style.TEXT_TRANSFORM_PROP)); //$NON-NLS-1$
		assertEquals("middle", groupHandle.getStringProperty(Style.VERTICAL_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("nowrap", groupHandle.getStringProperty(Style.WHITE_SPACE_PROP)); //$NON-NLS-1$
		assertEquals("12", groupHandle.getStringProperty(Style.WIDOWS_PROP)); //$NON-NLS-1$
		assertEquals("normal", groupHandle.getStringProperty(Style.WORD_SPACING_PROP)); //$NON-NLS-1$

		assertEquals("inline", groupHandle.getStringProperty(Style.DISPLAY_PROP)); //$NON-NLS-1$
		assertEquals("My Page", groupHandle.getStringProperty(Style.MASTER_PAGE_PROP)); //$NON-NLS-1$
		assertEquals("auto", groupHandle.getStringProperty(Style.PAGE_BREAK_AFTER_PROP)); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_AUTO,
				groupHandle.getStringProperty(Style.PAGE_BREAK_BEFORE_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO,
				groupHandle.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));
		assertEquals("true", groupHandle.getStringProperty(Style.SHOW_IF_BLANK_PROP)); //$NON-NLS-1$
		assertEquals("true", groupHandle.getStringProperty(Style.CAN_SHRINK_PROP)); //$NON-NLS-1$

		assertEquals("right", groupHandle.getStringProperty(Style.NUMBER_ALIGN_PROP)); //$NON-NLS-1$

		assertEquals("auto", groupHandle.getStringProperty(Style.MARGIN_TOP_PROP)); //$NON-NLS-1$
		assertEquals("auto", groupHandle.getStringProperty(Style.MARGIN_LEFT_PROP)); //$NON-NLS-1$
		assertEquals("auto", groupHandle.getStringProperty(Style.MARGIN_RIGHT_PROP)); //$NON-NLS-1$
		assertEquals("auto", groupHandle.getStringProperty(Style.MARGIN_BOTTOM_PROP)); //$NON-NLS-1$

		// assertEquals(
		// "[somefield]", groupHandle.getStringProperty(
		// Style.MAP_TEST_EXPR_PROP ) ); //$NON-NLS-1$

		GroupPropertyHandle groupPropertyHandle = groupHandle.getPropertyHandle(Style.MAP_RULES_PROP);
		assertNotNull(groupPropertyHandle);
	}

	/**
	 * Tests the commands: execute, redo , undo and check property.
	 * 
	 * @throws Exception
	 */

	public void testExtendedCommand() throws Exception {
		openDesign(fileName);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		// creates the extended element before operations

		extendedHandle.loadExtendedElement();

		// do-setProperty command

		extendedHandle.setProperty("company", "new value"); //$NON-NLS-1$//$NON-NLS-2$

		assertTrue(extendedHandle.getStringProperty("company").endsWith("execute")); //$NON-NLS-1$ //$NON-NLS-2$

		ActivityStack stack = design.getActivityStack();
		stack.undo();
		assertTrue(extendedHandle.getStringProperty("company").endsWith("undo")); //$NON-NLS-1$//$NON-NLS-2$

		stack.redo();
		assertTrue(extendedHandle.getStringProperty("company").endsWith("redo")); //$NON-NLS-1$//$NON-NLS-2$

	}

	/**
	 * Tests all the cases about dynamic property list of extended element in
	 * ExtendedItem.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testDynamicPropertyList() throws DesignFileException, SemanticException {
		openDesign(fileName, ULocale.ENGLISH);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		Set<String> set = new HashSet<String>();
		Iterator<?> iter = extendedHandle.getPropertyIterator();
		while (iter.hasNext()) {
			set.add(((PropertyHandle) iter.next()).getDefn().getName());
		}
		assertTrue(set.contains(ReportItem.X_PROP));
		assertTrue(set.contains(ReportItem.Y_PROP));

		assertTrue(set.contains("test1")); //$NON-NLS-1$
		assertTrue(set.contains("test2")); //$NON-NLS-1$
		assertTrue(set.contains("test3")); //$NON-NLS-1$
		assertTrue(set.contains("test5")); //$NON-NLS-1$
		assertTrue(set.contains("test6")); //$NON-NLS-1$
		assertTrue(set.contains("test7")); //$NON-NLS-1$

		assertTrue(set.contains("type")); //$NON-NLS-1$
		assertFalse(set.contains("radius")); //$NON-NLS-1$
		assertFalse(set.contains("pieWidth")); //$NON-NLS-1$
		assertFalse(set.contains("pieHeight")); //$NON-NLS-1$
		assertTrue(set.contains("xScale")); //$NON-NLS-1$
		assertTrue(set.contains("yScale")); //$NON-NLS-1$

		// Load extension element.

		extendedHandle.loadExtendedElement();

		ExtendedItem extendedItem = (ExtendedItem) extendedHandle.getElement();
		assertNotNull(extendedItem.getPropertyDefn("type")); //$NON-NLS-1$
		assertNotNull(extendedItem.getPropertyDefn("xScale")); //$NON-NLS-1$
		assertNotNull(extendedItem.getPropertyDefn("yScale")); //$NON-NLS-1$
		assertNull(extendedItem.getPropertyDefn("radius")); //$NON-NLS-1$
		assertNull(extendedItem.getPropertyDefn("pieWidth")); //$NON-NLS-1$
		assertNull(extendedItem.getPropertyDefn("pieHeight")); //$NON-NLS-1$

		assertEquals("Type", extendedItem.getPropertyDefn("type").getDisplayName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("X Scale", extendedItem.getPropertyDefn("xScale").getDisplayName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Y Scale", extendedItem.getPropertyDefn("yScale").getDisplayName()); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("Thin", extendedItem.getPropertyDefn("lineStyle").getChoices().getChoices()[0].getDisplayName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Normal", extendedItem.getPropertyDefn("lineStyle").getChoices().getChoices()[1].getDisplayName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Thick", extendedItem.getPropertyDefn("lineStyle").getChoices().getChoices()[2].getDisplayName()); //$NON-NLS-1$ //$NON-NLS-2$

		// Change the dynamci property list

		extendedHandle.setProperty("type", "pie"); //$NON-NLS-1$//$NON-NLS-2$

		assertNotNull(extendedItem.getPropertyDefn("type")); //$NON-NLS-1$
		assertNull(extendedItem.getPropertyDefn("xScale")); //$NON-NLS-1$
		assertNull(extendedItem.getPropertyDefn("yScale")); //$NON-NLS-1$
		assertNotNull(extendedItem.getPropertyDefn("radius")); //$NON-NLS-1$
		assertNotNull(extendedItem.getPropertyDefn("pieWidth")); //$NON-NLS-1$
		assertNotNull(extendedItem.getPropertyDefn("pieHeight")); //$NON-NLS-1$

		// test the methods defined in the plugin.xml & in the extension model
		// implementation

		assertEquals(8, extendedHandle.getMethods().size());

		List<?> methods = extendedHandle.getMethods();

		assertEquals("afterCloseDoc", ((ElementPropertyDefn) methods //$NON-NLS-1$
				.get(0)).getName());
		assertNotNull(((ElementPropertyDefn) methods.get(0)).getMethodInfo());
		assertEquals("test8", ((ElementPropertyDefn) methods.get(1)) //$NON-NLS-1$
				.getName());
		assertNotNull(((ElementPropertyDefn) methods.get(1)).getMethodInfo());
		assertEquals("onPrepare", ((ElementPropertyDefn) methods.get(2)) //$NON-NLS-1$
				.getName());
		assertEquals("onCreate", ((ElementPropertyDefn) methods.get(3)) //$NON-NLS-1$
				.getName());
		assertEquals("onRender", ((ElementPropertyDefn) methods.get(4)) //$NON-NLS-1$
				.getName());
		assertEquals("onPageBreak", //$NON-NLS-1$
				((ElementPropertyDefn) methods.get(5)).getName());
		assertEquals("firstMethod", ((ElementPropertyDefn) methods //$NON-NLS-1$
				.get(6)).getName());
		assertEquals("sencondMethod", ((ElementPropertyDefn) methods //$NON-NLS-1$
				.get(7)).getName());
		assertNotNull(((ElementPropertyDefn) methods.get(7)).getMethodInfo());

		try {
			extendedHandle.setProperty("xScale", "4"); //$NON-NLS-1$//$NON-NLS-2$
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyNameException);
		}

	}

	/**
	 * Tests TestingBox attribute.
	 */

	public void testTestingBox() {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(TESTING_BOX_NAME);
		assertNotNull(extDefn);
		assertEquals("TestingBox", extDefn.getDisplayName()); //$NON-NLS-1$
		assertEquals(null, extDefn.getDisplayNameKey());
		assertEquals(TESTING_BOX_NAME, extDefn.getName());
		assertEquals(MetaDataConstants.REQUIRED_NAME, extDefn.getNameOption());
		assertEquals(true, extDefn.allowsUserProperties());
		assertEquals(false, extDefn.hasStyle());

		List<?> propList = extDefn.getProperties();

		ElementPropertyDefn prop = (ElementPropertyDefn) extDefn.getProperty("usage"); //$NON-NLS-1$
		assertEquals(prop, extDefn.getProperty("usage")); //$NON-NLS-1$
		assertEquals("usage", prop.getName()); //$NON-NLS-1$
		assertEquals("usage", prop.getDisplayName()); //$NON-NLS-1$
		assertEquals(PropertyType.STRING_TYPE, prop.getTypeCode());
		assertEquals(false, prop.isList());
		assertEquals(null, prop.getGroupName());
		assertEquals(null, prop.getChoices());
		assertEquals(null, prop.getStructDefn());
		assertEquals("paper", prop.getDefault()); //$NON-NLS-1$
		assertEquals(true, prop.canInherit());

		prop = (ElementPropertyDefn) propList.get(1);
		assertEquals(prop, extDefn.getProperty("shape")); //$NON-NLS-1$
		assertEquals("shape", prop.getName()); //$NON-NLS-1$
		assertEquals("shape", prop.getDisplayName()); //$NON-NLS-1$
		assertEquals(PropertyType.CHOICE_TYPE, prop.getTypeCode());
		assertEquals(false, prop.isList());
		assertEquals(null, prop.getGroupName());
		assertEquals(null, prop.getStructDefn());
		assertEquals(null, prop.getDefault());
		assertEquals(false, prop.canInherit());

		IChoiceSet choiceSet = prop.getChoices();
		assertEquals(3, choiceSet.getChoices().length);
		IChoice[] choices = choiceSet.getChoices();
		assertEquals("cube", choices[0].getName()); //$NON-NLS-1$
		assertEquals("cube", choices[0].getValue()); //$NON-NLS-1$
		assertEquals("Choices.shape.cube", choices[0].getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("cube", choices[0].getDisplayName()); //$NON-NLS-1$
		assertEquals("sphere", choices[1].getName()); //$NON-NLS-1$
		assertEquals("sphere", choices[1].getValue()); //$NON-NLS-1$
		assertEquals("Choices.shape.sphere", choices[1].getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("sphere", choices[1].getDisplayName()); //$NON-NLS-1$
		assertEquals("cubiod", choices[2].getName()); //$NON-NLS-1$
		assertEquals("cubiod", choices[2].getValue()); //$NON-NLS-1$
		assertEquals("Choices.shape.cubiod", choices[2].getDisplayNameKey()); //$NON-NLS-1$
		assertEquals("cubiod", choices[2].getDisplayName()); //$NON-NLS-1$
	}

	/**
	 * Test master page can't insert chart and crosstab Now forbidden all extended
	 * item. see bugzill 188196
	 * 
	 * @throws Exception
	 */

	public void testExtendedItemNotAllowedInMasterpage() throws Exception {
		createDesign();

		SimpleMasterPageHandle masterPageHandle = designHandle.getElementFactory().newSimpleMasterPage("master page");//$NON-NLS-1$
		designHandle.getMasterPages().add(masterPageHandle);

		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.getElementFactory()
				.newElement(TESTING_BOX_NAME, "box1"); //$NON-NLS-1$

		assertFalse(masterPageHandle.canContain(masterPageHandle.getPageHeader().getSlotID(), itemHandle));
	}

	/**
	 * Tests adding extended item.
	 * 
	 * @throws Exception if any exception
	 */

	public void testAddExtendedItem() throws Exception {
		createDesign();
		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.getElementFactory()
				.newElement(TESTING_BOX_NAME, "box1"); //$NON-NLS-1$
		designHandle.getBody().add(itemHandle);

		assertEquals(TESTING_BOX_NAME, itemHandle.getDefn().getName());
		assertEquals(TESTING_BOX_NAME, ((ExtendedItem) itemHandle.getElement()).getExtDefn().getName());
		assertEquals(false, ((ExtendedItem) itemHandle.getElement()).getExtDefn().hasStyle());

		// itemHandle.loadExtendedElement( );
		itemHandle.setProperty("usage", "toy"); //$NON-NLS-1$ //$NON-NLS-2$

		save();
		assertTrue(compareFile("TestingBox_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Test create a child extended item base on another.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testAddExtendedItem2() throws Exception {
		openDesign(fileName_7);

		DesignElementHandle baseMatrixHandle = designHandle.getComponents().get(0);
		assertNotNull(baseMatrixHandle);
		assertEquals("baseMatrix", baseMatrixHandle.getName()); //$NON-NLS-1$
		DesignElementHandle newMatrixHandle = designHandle.getElementFactory().newElementFrom(baseMatrixHandle,
				"myMatrix"); //$NON-NLS-1$

		designHandle.getBody().add(newMatrixHandle);
		save();
		assertTrue(compareFile("TestAddExtendedItem_golden_2.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the clone issues: property values, handle, element.
	 * 
	 * @throws Exception
	 */

	public void testClone() throws Exception {
		openDesign(fileName);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		extendedHandle.loadExtendedElement();
		ExtendedItemHandle clonedHandle = (ExtendedItemHandle) extendedHandle.copy().getHandle(design);
		assertNotNull(clonedHandle);

		// test the property values of cloned element

		assertNotNull(((ExtendedItem) clonedHandle.getElement()).getExtendedElement());
		clonedHandle.loadExtendedElement();
		assertEquals(TESTING_MATRIX_NAME, clonedHandle.getExtensionName());
		assertEquals(1.2, clonedHandle.getX().getMeasure(), 0.00);
		assertEquals("2in", clonedHandle.getProperty("test1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(22, clonedHandle.getIntProperty("test2")); //$NON-NLS-1$
		assertEquals("type=bar,xScale=2,yScale=3,lineStyle=normal,script=internalScript", //$NON-NLS-1$
				clonedHandle.getProperty("test3")); //$NON-NLS-1$
		assertNull(clonedHandle.getProperty("test4")); //$NON-NLS-1$
		assertEquals("choice1", clonedHandle.getProperty("test5")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("bar", clonedHandle.getProperty("type")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(null, clonedHandle.getProperty("radius")); //$NON-NLS-1$
		assertEquals(null, clonedHandle.getProperty("pieWidth")); //$NON-NLS-1$
		assertEquals(null, clonedHandle.getProperty("pieHeight")); //$NON-NLS-1$
		assertEquals(2, clonedHandle.getIntProperty("xScale")); //$NON-NLS-1$
		assertEquals(3, clonedHandle.getIntProperty("yScale")); //$NON-NLS-1$
		assertEquals(null, clonedHandle.getProperty("company")); //$NON-NLS-1$
		assertEquals("normal", clonedHandle.getProperty("lineStyle")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Tests the notification mechanism: if the dynamic property list is changed
	 * after setting one extension property value, or not. There are different
	 * events.
	 * 
	 * @throws Exception
	 */

	public void testNotification() throws Exception {
		openDesign(fileName_3);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		MyListener containerListener = new MyListener();
		extendedHandle.addListener(containerListener);

		extendedHandle.loadExtendedElement();
		extendedHandle.setProperty("type", "pie"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(MyListener.PROPERTY_LIST_EVENT, containerListener.action);

		extendedHandle.setProperty("test1", "new"); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(MyListener.PROPERTY_EVENT, containerListener.action);

		extendedHandle.setProperty("test3", null); //$NON-NLS-1$
		// assertEquals( MyListener.PROPERTY_LIST_EVENT,
		// containerListener.action );

		extendedHandle.getElement().validateWithContents(design);

		// test changes on the testing-matrix selector.

		assertEquals(ColorPropertyType.BLUE, extendedHandle.getProperty(Style.COLOR_PROP));

		String selectorName = ((ExtendedItem) extendedHandle.getElement()).getExtDefn().getSelector();
		StyleHandle selector = designHandle.findNativeStyle(selectorName);

		selector.getColor().setStringValue(ColorPropertyType.TEAL);
		assertEquals(MyListener.STYLE_EVENT, containerListener.action);
	}

	/**
	 * Tests the setting and getting choice extension property.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testChoiceProperty() throws Exception {
		ExtendedItemHandle itemHandle = insertExtendedItem("matrix1", TESTING_MATRIX_NAME); //$NON-NLS-1$

		// itemHandle.loadExtendedElement( );
		itemHandle.setProperty("test5", "choice2");//$NON-NLS-1$//$NON-NLS-2$
		assertEquals("choice2", itemHandle.getProperty("test5"));//$NON-NLS-1$//$NON-NLS-2$

		try {
			itemHandle.setProperty("test5", "wrongChoice");//$NON-NLS-1$//$NON-NLS-2$
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
			assertEquals("choice2", itemHandle.getProperty("test5"));//$NON-NLS-1$//$NON-NLS-2$
		}

	}

	/**
	 * Tests the setting and getting choice property defined in dynamic property.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testChoiceDynamicProperty() throws Exception {
		ExtendedItemHandle itemHandle = insertExtendedItem("matrix1", TESTING_MATRIX_NAME); //$NON-NLS-1$

		// itemHandle.loadExtendedElement( );
		itemHandle.setProperty("lineStyle", "thin"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("thin", itemHandle.getProperty("lineStyle"));//$NON-NLS-1$//$NON-NLS-2$

		try {
			itemHandle.setProperty("lineStyle", "wrongChoice");//$NON-NLS-1$//$NON-NLS-2$
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
			assertEquals("thin", itemHandle.getProperty("lineStyle"));//$NON-NLS-1$//$NON-NLS-2$
		}
	}

	/**
	 * Tests <code>PropertyInvisible</code> tags in extension plugin.xml.
	 * 
	 * @throws Exception if any exception
	 */

	public void testExtensionPropertyInvisible() throws Exception {
		ExtendedItemHandle itemHandle = insertExtendedItem("matrix1", TESTING_MATRIX_NAME); //$NON-NLS-1$

		PropertyHandle prop = itemHandle.getPropertyHandle(ReportItem.BOOKMARK_PROP);
		assertFalse(prop.isVisible());

		prop = itemHandle.getPropertyHandle("test3");//$NON-NLS-1$
		assertFalse(prop.isVisible());

		prop = itemHandle.getPropertyHandle(ReportItem.DATA_SET_PROP);
		assertTrue(prop.isVisible());

		itemHandle = insertExtendedItem("testTable1", TESTING_TABLE_NAME); //$NON-NLS-1$
		assertNotNull(itemHandle.getPropertyDefn(IExtendedItemModel.EXTENSION_NAME_PROP));

		prop = itemHandle.getPropertyHandle(IExtendedItemModel.EXTENSION_NAME_PROP);
		assertFalse(prop.isVisible());

		prop = itemHandle.getPropertyHandle(IReportItemModel.CUBE_PROP);
		assertTrue(prop.isVisible());

	}

	/**
	 * Tests opening the design file with wrong extension. The element behaves as an
	 * extended item.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testOpenDesignFileWithWrongExtension() throws DesignFileException, SemanticException {
		openDesign(fileName_4);

		List<?> list = designHandle.getErrorList();
		assertEquals(1, list.size());

		int i = 0;
		assertEquals(SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND, ((ErrorDetail) list.get(i++)).getErrorCode());

		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$

		// load elements without valid extension.

		try {
			itemHandle.getReportItem();
			fail();
		} catch (ExtendedElementException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND, e.getErrorCode());
		}

		testExtendedItemWithoutExtension(itemHandle);

	}

	/**
	 * Tests method calls on an extended item without the valid extension.
	 * 
	 * @param itemHandle the handle of extended item.
	 * @throws SemanticException
	 */

	private void testExtendedItemWithoutExtension(ExtendedItemHandle itemHandle) throws SemanticException {

		// set property .
		String yPropStr = new NumberFormatter(itemHandle.getModule().getLocale()).format(12.5) + "cm"; //$NON-NLS-1$
		itemHandle.setStringProperty(ReportItem.Y_PROP, yPropStr);

		try {
			itemHandle.setStringProperty("test2", //$NON-NLS-1$
					"the value of undefined property"); //$NON-NLS-1$
			fail();
		} catch (PropertyNameException e) {
			assertEquals(PropertyNameException.DESIGN_EXCEPTION_PROPERTY_NAME_INVALID, e.getErrorCode());
		}

		// get property.

		assertEquals("12.5cm", itemHandle //$NON-NLS-1$
				.getStringProperty(ReportItem.Y_PROP));

		assertNull(itemHandle.getStringProperty("test2")); //$NON-NLS-1$

		// element definition and extension definition

		IElementDefn elementDefn = itemHandle.getDefn();
		assertEquals(elementDefn, MetaDataDictionary.getInstance().getElement(ReportDesignConstants.EXTENDED_ITEM));

		ExtendedItem element = (ExtendedItem) itemHandle.getElement();
		assertNull(element.getExtDefn());

		assertFalse(element.isExtensionXMLProperty(ReportItem.Y_PROP));
		assertFalse(element.isExtensionXMLProperty("test3")); //$NON-NLS-1$
	}

	/**
	 * Tests opening the design file without extension. The element behaves as an
	 * extended item.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testOpenDesignFileWithoutExtension() throws DesignFileException, SemanticException {
		openDesign(fileName_5);

		List<?> list = designHandle.getErrorList();
		assertEquals(1, list.size());

		int i = 0;
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION, ((ErrorDetail) list.get(i++)).getErrorCode());

		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$

		// load elements without valid extension.

		try {
			itemHandle.getReportItem();
			fail();
		} catch (ExtendedElementException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION, e.getErrorCode());
		}

		testExtendedItemWithoutExtension(itemHandle);
	}

	/**
	 * Returns a new extended item handle, which is inserted into body.
	 * 
	 * @param name          the element name
	 * @param extensionName the extension name
	 * @return the new extended item handle
	 * @throws Exception if any exception
	 */

	private ExtendedItemHandle insertExtendedItem(String name, String extensionName) throws Exception {
		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle((ULocale) null);
		designHandle = sessionHandle.createDesign();
		ExtendedItemHandle itemHandle = designHandle.getElementFactory().newExtendedItem(name, extensionName);
		designHandle.getBody().add(itemHandle);

		return itemHandle;
	}

	/**
	 * Tests lazy initializing extension.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testLoadExtendedElement() throws Exception {
		// Get property without initializing.

		openDesign(fileName);
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		assertEquals("bar", extendedHandle.getProperty("type")); //$NON-NLS-1$ //$NON-NLS-2$
		designHandle.close();

		// Get string property without initializing.

		openDesign(fileName);
		extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		assertEquals("bar", extendedHandle.getStringProperty("type")); //$NON-NLS-1$ //$NON-NLS-2$
		designHandle.close();

		// Set property without initializing.

		openDesign(fileName);
		extendedHandle = (ExtendedItemHandle) designHandle.findElement("right extended item"); //$NON-NLS-1$
		assertNotNull(extendedHandle);

		extendedHandle.setProperty("type", "pie"); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals("pie", extendedHandle.getProperty("type")); //$NON-NLS-1$ //$NON-NLS-2$

		// Get/Set property after initializing.

		extendedHandle.loadExtendedElement();

		assertEquals("pie", extendedHandle.getProperty("type")); //$NON-NLS-1$ //$NON-NLS-2$
		extendedHandle.setProperty("type", "bar"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("bar", extendedHandle.getProperty("type")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * The element type stored in some certain slot was defined in ROM. But after
	 * loading in the extension elements, we should show the extension element type
	 * within the slot which can hold ExtendedItem.
	 * 
	 * @throws DesignFileException
	 * 
	 */

	public void testTheSlotDefn() throws DesignFileException {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		IElementDefn elementDefn = dd.getElement("FreeForm"); //$NON-NLS-1$
		assertNotNull(elementDefn);

		SlotDefn slotDefn = (SlotDefn) elementDefn.getSlot(0);

		// TestingMatrix does appear in this list.

		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(TESTING_MATRIX_NAME);
		assertTrue(slotDefn.getContentExtendedElements().contains(extDefn));
		assertFalse(slotDefn.getContentElements().contains(extDefn));

		// TestingBox does appear in this list.

		extDefn = (ExtensionElementDefn) dd.getExtension(TESTING_BOX_NAME);
		assertTrue(slotDefn.getContentExtendedElements().contains(extDefn));
		assertFalse(slotDefn.getContentElements().contains(extDefn));

		// ExtendedItem does not appear in this list.

		ElementDefn extendedItemDefn = (ElementDefn) dd.getElement(ReportDesignConstants.EXTENDED_ITEM);
		assertFalse(slotDefn.getContentExtendedElements().contains(extendedItemDefn));
		assertTrue(slotDefn.getContentElements().contains(extendedItemDefn));

		openDesign("ExtensionTest_5.xml"); //$NON-NLS-1$

		FreeFormHandle freeForm = designHandle.getElementFactory().newFreeForm("MyForm"); //$NON-NLS-1$

		assertTrue(freeForm.canContain(FreeForm.REPORT_ITEMS_SLOT, "TestingMatrix")); //$NON-NLS-1$
		assertTrue(freeForm.canContain(FreeForm.REPORT_ITEMS_SLOT, "TestingBox")); //$NON-NLS-1$
		assertFalse(freeForm.canContain(FreeForm.REPORT_ITEMS_SLOT, "wrongExtension")); //$NON-NLS-1$

	}

	/**
	 * Tests the semantic check of data set required.
	 * 
	 * @throws Exception
	 */

	public void testSemanticCheck() throws Exception {
		openDesign(fileName_6);

		assertEquals(2, design.getErrorList().size());

		assertEquals("org.eclipse.birt.report.model.api.extension.ExtendedElementException", //$NON-NLS-1$
				((ErrorDetail) design.getErrorList().get(0)).getExceptionName());
		assertEquals("org.eclipse.birt.report.model.api.extension.ExtendedElementException", //$NON-NLS-1$
				((ErrorDetail) design.getErrorList().get(1)).getExceptionName());

		// move an extended item, which is originally in report-items slot of a
		// free-form
		// and the free-form is in component slot, to the body slot

		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.findElement("parent"); //$NON-NLS-1$
		assertNotNull(extendedHandle);
		extendedHandle.moveTo(designHandle, ReportDesign.BODY_SLOT);
		designHandle.checkReport();
		assertEquals(2, design.getErrorList().size());

		assertEquals("org.eclipse.birt.report.model.api.extension.ExtendedElementException", //$NON-NLS-1$
				((ErrorDetail) design.getErrorList().get(0)).getExceptionName());

		assertEquals("org.eclipse.birt.report.model.api.extension.ExtendedElementException", //$NON-NLS-1$
				((ErrorDetail) design.getErrorList().get(1)).getExceptionName());

	}

	/**
	 * Tests creating extension element in the design/library.
	 * 
	 * @throws Exception
	 */

	public void testCreateExtension() throws Exception {
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = session.createDesign();

		ExtendedItemHandle elementHandle = designHandle.getElementFactory().newExtendedItem(null, TESTING_MATRIX_NAME);
		assertNotNull(elementHandle.getName());

		libraryHandle = session.createLibrary();

		elementHandle = libraryHandle.getElementFactory().newExtendedItem(null, TESTING_MATRIX_NAME);
		assertEquals("NewTestingMatrix", elementHandle.getName()); //$NON-NLS-1$

		PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) MetaDataDictionary.getInstance()
				.getExtension(TESTING_MATRIX_NAME);
		IMessages msgs = extDefn.getReportItemFactory().getMessages();
		assertEquals("TestingMatrix", msgs.getMessage( //$NON-NLS-1$
				(String) extDefn.getDisplayNameKey(), ULocale.ENGLISH));

		elementHandle = libraryHandle.getElementFactory().newExtendedItem(null, TESTING_BOX_NAME);
		assertEquals("NewTestingBox", elementHandle.getName()); //$NON-NLS-1$

		extDefn = (PeerExtensionElementDefn) MetaDataDictionary.getInstance().getExtension(TESTING_BOX_NAME);
		assertNull(extDefn.getReportItemFactory().getMessages());

	}

	class MyListener implements Listener {

		static final int NA = 0;
		static final int PROPERTY_EVENT = 1;
		static final int PROPERTY_LIST_EVENT = 2;
		static final int STYLE_EVENT = 3;

		int action = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			if (ev.getEventType() == NotificationEvent.PROPERTY_EVENT) {
				action = PROPERTY_EVENT;
			} else if (ev.getEventType() == NotificationEvent.EXTENSION_PROPERTY_DEFINITION_EVENT) {
				action = PROPERTY_LIST_EVENT;
			} else if (ev.getEventType() == NotificationEvent.STYLE_EVENT) {
				action = STYLE_EVENT;
			} else {
				assert false;
			}

		}
	}

	/**
	 * Test get localized message in <code>ExtendedElementException</code>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testLocalizedMessage() throws Exception {
		openDesign(fileName_8, ULocale.ENGLISH);
		assertEquals(2, design.getErrorList().size());

		ErrorDetail eDetail = (ErrorDetail) design.getErrorList().get(0);
		String localizedMessage = eDetail.getMessage();
		String expect = "Extended exception in TestingMatrix"; //$NON-NLS-1$
		assertEquals(expect, localizedMessage);

		eDetail = (ErrorDetail) design.getErrorList().get(1);
		localizedMessage = eDetail.getMessage();
		expect = "local actuate"; //$NON-NLS-1$
		assertEquals(expect, localizedMessage);

	}

	/**
	 * Tests the validation method for the latest version design file. Same case
	 * with testLocalizedMessage( ).
	 * 
	 * @throws DesignFileException
	 */

	public void testChartValidation() throws DesignFileException {
		openDesign(fileName_9, ULocale.ENGLISH);

		assertEquals(2, design.getErrorList().size());

		ErrorDetail eDetail = (ErrorDetail) design.getErrorList().get(0);
		String localizedMessage = eDetail.getMessage();
		String expect = "Extended exception in TestingMatrix"; //$NON-NLS-1$
		assertEquals(expect, localizedMessage);

		eDetail = (ErrorDetail) design.getErrorList().get(1);
		localizedMessage = eDetail.getMessage();
		expect = "local actuate"; //$NON-NLS-1$
		assertEquals(expect, localizedMessage);
	}

	/**
	 * Tests the display lable of the extension defined selector.
	 * 
	 * @throws Exception
	 */

	public void testSelectorDisplayLabel() throws Exception {
		createDesign(ULocale.ENGLISH);

		StyleHandle style = designHandle.getElementFactory().newStyle("testing-matrix"); //$NON-NLS-1$
		assertEquals("Testing Matrix", style.getDisplayLabel()); //$NON-NLS-1$
	}

	/**
	 * Tests the use-properties by call getPropertyDefns().
	 * 
	 * @throws Exception
	 */

	public void testGetPropertyDefns() throws Exception {
		openDesign(fileName_10, ULocale.ENGLISH);
		assertNotNull(designHandle);

		ExtendedItemHandle extendedItemHandle = (ExtendedItemHandle) designHandle.findElement("testMatrixItem"); //$NON-NLS-1$
		assertNotNull(extendedItemHandle);
		PropertyDefn defn = extendedItemHandle.getElement().getPropertyDefn("abc"); //$NON-NLS-1$
		assertNotNull(defn);
		assertTrue(defn instanceof UserPropertyDefn);
	}

	/**
	 * Tests get/set altText and altTextID properties.
	 * 
	 * @throws Exception if opening design file failed.
	 */
	public void testAltTextProperty() throws Exception {
		openDesign("ExtensionTest_AltText.xml");//$NON-NLS-1$
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.getBody().get(0);
		assertNotNull(extendedHandle);

		assertEquals("chart is beautiful", extendedHandle.getAltText()); //$NON-NLS-1$
		assertEquals("chart id", extendedHandle.getAltTextKey()); //$NON-NLS-1$

		extendedHandle.setAltText("chart alt text");//$NON-NLS-1$
		extendedHandle.setAltTextKey("chart id 2");//$NON-NLS-1$

		assertEquals("chart alt text", extendedHandle.getAltText()); //$NON-NLS-1$
		assertEquals("chart id 2", extendedHandle.getAltTextKey()); //$NON-NLS-1$
		save();
		assertTrue(compareFile("ExtensionTest_AltText_golden.xml"));//$NON-NLS-1$
	}

	public void testExtensionDefaultStyle() throws SemanticException, DesignFileException {

		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = session.createDesign();
		boolean findboxStyle = false;
		SlotHandle styles = designHandle.getStyles();
		StyleHandle boxDefaultStyle = (StyleHandle) styles.get(0);
		assert styles != null;

		for (int i = 0; i < styles.getCount(); i++) {

			boxDefaultStyle = (StyleHandle) styles.get(i);
			if (boxDefaultStyle.getName().equals("BoxStyle")) {
				findboxStyle = true;
				break;
			}

		}
		assertTrue(findboxStyle);

		ExtendedItemHandle elementHandle = designHandle.getElementFactory().newExtendedItem(null, TESTING_BOX_NAME);
		assertNotNull(elementHandle.getName());

		assertEquals("BoxStyle", boxDefaultStyle.getName());
		assertEquals("#CCCCCC", boxDefaultStyle.getColor().getStringValue());
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, boxDefaultStyle.getBorderBottomStyle());
		assertEquals("1pt", boxDefaultStyle.getBorderBottomWidth().getValue().toString());
		assertEquals("10pt", boxDefaultStyle.getMarginRight().getValue().toString());

		assertEquals("Tahoma", boxDefaultStyle.getFontFamilyHandle().getStringValue());

		assertEquals("12pt", boxDefaultStyle.getFontSize().getValue().toString());

	}
}
