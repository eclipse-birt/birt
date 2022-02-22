/*******************************************************************************
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright (c) 2004 Actuate Corporation.
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.i18n.ThreadResources;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.VersionInfo;
import org.eclipse.birt.report.model.util.XMLParserException;

import com.ibm.icu.util.ULocale;

/**
 * Test cases for ModuleUtil.
 */

public class ModuleUtilTest extends BaseTestCase {

	/**
	 * Test deserilaze an stream-represented data into an action structure.
	 *
	 * @throws XMLParserException
	 * @throws IOException
	 * @throws DesignFileException
	 */

	public void testDeserialize() throws XMLParserException, IOException, DesignFileException {
		InputStream is = ModuleUtilTest.class.getResourceAsStream("input/ActionDeserializeTest.xml"); //$NON-NLS-1$
		ActionHandle action = ModuleUtil.deserializeAction(is);
		assertEquals(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH, action.getLinkType());
		assertEquals("Window3", action.getTargetWindow()); //$NON-NLS-1$

		MemberHandle paramBindings = action.getParamBindings();
		assertEquals(2, paramBindings.getListValue().size());
		ParamBindingHandle paramBinding1 = (ParamBindingHandle) paramBindings.getAt(0);
		assertEquals("param1", paramBinding1.getParamName()); //$NON-NLS-1$
		assertEquals("1+1=3", paramBinding1.getExpression()); //$NON-NLS-1$

		MemberHandle searchKeys = action.getSearch();
		assertEquals(2, searchKeys.getListValue().size());
		SearchKeyHandle key1 = (SearchKeyHandle) searchKeys.getAt(0);
		assertEquals("\"E001\".equals(row[\"studentId\"])", key1.getExpression()); //$NON-NLS-1$

		// with chinese character inside.

		is = ModuleUtilTest.class.getResourceAsStream("input/ActionDeserializeTest_1.xml"); //$NON-NLS-1$

		action = ModuleUtil.deserializeAction(is);
		assertNotNull(action);
		assertEquals("/BIRT/\u4e2d\u6587.html", action.getURI()); //$NON-NLS-1$

		ActionHandle actionHandle = ModuleUtil.deserializeAction((String) null);
		assertNotNull(actionHandle);
		assertEquals("hyperlink", actionHandle.getLinkType()); //$NON-NLS-1$

	}

	/**
	 * Test serialize an action instance.
	 *
	 * @throws Exception
	 */

	public void testSerialize() throws Exception {
		openDesign("ActionSerializeTest.xml"); //$NON-NLS-1$
		ImageHandle image1 = (ImageHandle) designHandle.findElement("image1"); //$NON-NLS-1$ ,
		ActionHandle action1 = image1.getActionHandle();

		ImageHandle image2 = (ImageHandle) designHandle.findElement("image2"); //$NON-NLS-1$
		ActionHandle action2 = image2.getActionHandle();

		String str = ModuleUtil.serializeAction(action1);

		os = new ByteArrayOutputStream();
		os.write(str.getBytes());
		os.close();
		assertTrue(compareFile("ActionSerializeTest1_golden.xml")); //$NON-NLS-1$

		str = ModuleUtil.serializeAction(action2);
		os = new ByteArrayOutputStream();
		os.write(str.getBytes());
		os.close();
		assertTrue(compareFile("ActionSerializeTest2_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test CheckModule.
	 *
	 * Cases:
	 * <ul>
	 * <li>valid report design file
	 * <li>valid library file
	 * <li>invalid report design file
	 * <li>invalid library file
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testCheckModule() throws Exception {
		sessionHandle = new DesignEngine(null).newSessionHandle(ULocale.ENGLISH);
		assertNotNull(sessionHandle);

		String fileName = INPUT_FOLDER + "CellHandleTest.xml"; //$NON-NLS-1$
		InputStream inputStream = getResourceAStream(fileName);
		int rtnType = ModuleUtil.checkModule(sessionHandle, getResource(fileName).toString(), inputStream);
		assertEquals(ModuleUtil.REPORT_DESIGN, rtnType);

		fileName = INPUT_FOLDER + "Library_1.xml"; //$NON-NLS-1$
		inputStream = getResourceAStream(fileName);
		rtnType = ModuleUtil.checkModule(sessionHandle, getResource(fileName).toString(), inputStream);
		assertEquals(ModuleUtil.LIBRARY, rtnType);

		fileName = INPUT_FOLDER + "InValidDesign.xml"; //$NON-NLS-1$
		inputStream = getResourceAStream(fileName);
		rtnType = ModuleUtil.checkModule(sessionHandle, getResource(fileName).toString(), inputStream);
		assertEquals(ModuleUtil.INVALID_MODULE, rtnType);

		fileName = INPUT_FOLDER + "InValidLibrary.xml"; //$NON-NLS-1$
		inputStream = getResourceAStream(fileName);
		rtnType = ModuleUtil.checkModule(sessionHandle, getResource(fileName).toString(), inputStream);
		assertEquals(ModuleUtil.INVALID_MODULE, rtnType);
	}

	/**
	 * Test CheckVersion.
	 *
	 * Cases:
	 * <ul>
	 * <li>test design file with version value "1".
	 * <li>test design file with same version value of current version.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testCheckVersion() throws Exception {
		ThreadResources.setLocale(ULocale.ENGLISH);

		List infos = ModuleUtil.checkVersion(getResource(INPUT_FOLDER + "DesignWithoutLibrary.xml").toString());//$NON-NLS-1$
		assertEquals(1, infos.size());
		VersionInfo versionInfo = (VersionInfo) infos.get(0);
		assertEquals("1", versionInfo.getDesignFileVersion()); //$NON-NLS-1$
		assertEquals(VersionInfo.CONVERT_FOR_COLUMN_BINDING, versionInfo.getInfoCode());

		infos = ModuleUtil.checkVersion(getResource(INPUT_FOLDER + "ScalarParameterHandleTest.xml").toString()); //$NON-NLS-1$
		assertEquals(0, infos.size());

		infos = ModuleUtil.checkVersion(getResource(INPUT_FOLDER + "CheckVersionDesign.xml").toString()); //$NON-NLS-1$
		assertEquals(1, infos.size());
		versionInfo = (VersionInfo) infos.get(0);
		assertEquals("3.4.20.1", versionInfo.getDesignFileVersion()); //$NON-NLS-1$
		assertEquals(VersionInfo.INVALID_VERSION, versionInfo.getInfoCode());

		infos = ModuleUtil.checkVersion(getResource(INPUT_FOLDER + "PeerExtensionTest.xml").toString()); //$NON-NLS-1$
		assertEquals(1, infos.size());
		versionInfo = (VersionInfo) infos.get(0);
		assertEquals(VersionInfo.EXTENSION_COMPATIBILITY, versionInfo.getInfoCode());
	}

	/**
	 * test case: 1: the cube is extended from library cube. The hierarchy handle
	 * from dimensionCondition is the library hierarchy instance, but the hierarchy
	 * handle from the report design cube is the report design virtual element
	 * instance. To draw the joint condition from GUI, they should be same. 2: the
	 * hierarchy handle from the joint condition and the cube are all local
	 * hierarchy instance, they should be same.
	 *
	 * @throws DesignFileException
	 */
	public void testIsEqualHierarchyForJointCondition() throws DesignFileException {
		openDesign("testIsEqualHierarchyForJointCondition_report.xml");//$NON-NLS-1$

		TabularCubeHandle cube1 = (TabularCubeHandle) designHandle.findCube("Customer Cube");//$NON-NLS-1$
		assertNotNull(cube1);
		TabularCubeHandle cube2 = (TabularCubeHandle) designHandle.findCube("Customer Cube1");//$NON-NLS-1$
		assertNotNull(cube2);

		Iterator iter = cube1.joinConditionsIterator();
		DimensionConditionHandle condition = (DimensionConditionHandle) iter.next();
		HierarchyHandle conditionHierarchy = (HierarchyHandle) condition.getHierarchy();

		HierarchyHandle cubeHierarchy = cube1.getDimension("Group1")//$NON-NLS-1$
				.getDefaultHierarchy();

		assertNotNull(cubeHierarchy);

		assertTrue(ModuleUtil.isEqualHierarchiesForJointCondition(conditionHierarchy, cubeHierarchy));

		// cube2

		iter = cube2.joinConditionsIterator();
		condition = (DimensionConditionHandle) iter.next();
		conditionHierarchy = (HierarchyHandle) condition.getHierarchy();

		cubeHierarchy = cube2.getDimension("Group4").getDefaultHierarchy();//$NON-NLS-1$

		assertNotNull(cubeHierarchy);

		assertTrue(ModuleUtil.isEqualHierarchiesForJointCondition(conditionHierarchy, cubeHierarchy));

		condition = (DimensionConditionHandle) iter.next();
		conditionHierarchy = (HierarchyHandle) condition.getHierarchy();

		cubeHierarchy = cube2.getDimension("Group3").getDefaultHierarchy();//$NON-NLS-1$

		assertNotNull(cubeHierarchy);

		assertTrue(ModuleUtil.isEqualHierarchiesForJointCondition(conditionHierarchy, cubeHierarchy));
	}

	public void testIsValidElementName() throws DesignFileException, ContentException, NameException {

		openDesign("emptyDesign.xml");//$NON-NLS-1$

		DataSetHandle dataset = designHandle.getElementFactory().newOdaDataSet("validName");//$NON-NLS-1$

		assertTrue(ModuleUtil.isValidElementName(dataset));

		designHandle.getDataSets().add(dataset);

		DataSetHandle datasetA = designHandle.getElementFactory().newOdaDataSet(null);

		datasetA.getElement().setName(dataset.getName());

		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName("name/");//$NON-NLS-1$

		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName("name\\");//$NON-NLS-1$
		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName("name.");//$NON-NLS-1$
		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName("name!");//$NON-NLS-1$
		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName("name;");//$NON-NLS-1$
		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName("name,");//$NON-NLS-1$
		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName("");//$NON-NLS-1$
		assertFalse(ModuleUtil.isValidElementName(datasetA));

		datasetA.getElement().setName(null);
		assertFalse(ModuleUtil.isValidElementName(datasetA));

		LabelHandle label = designHandle.getElementFactory().newLabel("style");//$NON-NLS-1$
		label.getElement().setName("");//$NON-NLS-1$
		assertTrue(ModuleUtil.isValidElementName(label));

		label.getElement().setName(null);
		assertTrue(ModuleUtil.isValidElementName(label));

	}

	/**
	 * Test convert param type to column data type and convert column data type to
	 * param type.
	 */

	public void testTransferParamTypeAndColumnType() {
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING,
				ModuleUtil.convertParamTypeToColumnType(DesignChoiceConstants.PARAM_TYPE_STRING));

		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME,
				ModuleUtil.convertParamTypeToColumnType(DesignChoiceConstants.PARAM_TYPE_DATETIME));

		assertEquals(DesignChoiceConstants.PARAM_TYPE_STRING,
				ModuleUtil.convertColumnTypeToParamType(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING));

		assertEquals(DesignChoiceConstants.PARAM_TYPE_DATETIME,
				ModuleUtil.convertColumnTypeToParamType(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME));

		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_ANY,
				ModuleUtil.convertColumnTypeToParamType(DesignChoiceConstants.COLUMN_DATA_TYPE_BLOB));
	}

	/**
	 * Test isListStyleRuleValue method.
	 *
	 * @throws Exception
	 */

	public void testIsListStyleRuleValue() throws Exception {
		createDesign();
		StyleHandle style = (StyleHandle) designHandle.getElementFactory().newStyle("table"); //$NON-NLS-1$
		designHandle.getStyles().add(style);
		PropertyHandle propHandle = style.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

		HighlightRule rule = StructureFactory.createHighlightRule();
		rule.setOperator(DesignChoiceConstants.MAP_OPERATOR_IN);
		propHandle.addItem(rule);

		HighlightRule rule2 = StructureFactory.createHighlightRule();
		rule2.setOperator(DesignChoiceConstants.MAP_OPERATOR_EQ);
		propHandle.addItem(rule2);

		HighlightRuleHandle handle = (HighlightRuleHandle) propHandle.get(0);
		assertTrue(ModuleUtil.isListStyleRuleValue(handle));
		HighlightRuleHandle handle2 = (HighlightRuleHandle) propHandle.get(1);
		assertFalse(ModuleUtil.isListStyleRuleValue(handle2));
	}

	/**
	 * Test transfer script uid to script value and transfer script instance to
	 * script uid.
	 *
	 * @throws Exception
	 */
	public void testScriptuid() throws Exception {
		openDesign("ModuleUtilTest.xml"); //$NON-NLS-1$

		// transfer script instance to script uid.
		LabelHandle label1 = (LabelHandle) designHandle.getElementByID(62l);
		PropertyHandle propHandle = label1.getPropertyHandle(LabelHandle.ON_PREPARE_METHOD);
		String path = ModuleUtil.getScriptUID(propHandle);
		assertEquals("/report/body/label[@id=\"62\"]/method[@name=\"onPrepare\"]", //$NON-NLS-1$
				path);

		// propHandle = label1
		// .getPropertyHandle( StyleHandle.HIGHLIGHT_RULES_PROP );
		// StructureHandle structHandle = (StructureHandle) propHandle.get( 0 );
		// MemberHandle member = (MemberHandle) structHandle
		// .getMember( StyleRule.TEST_EXPR_MEMBER );
		// path = ModuleUtil.getScriptUID( member );
		// assertEquals(
		// "/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/expression[@name=\"testExpr\"]",
		// //$NON-NLS-1$
		// path );
		//
		// member = (MemberHandle) structHandle
		// .getMember( StyleRule.VALUE1_MEMBER );
		// path = ModuleUtil.getScriptUID( member, 0 );
		// assertEquals(
		// "/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/list-property[@name=\"value1\"]/value[1]",
		// //$NON-NLS-1$
		// path );
		//
		// LabelHandle label2 = (LabelHandle) designHandle.getElementByID( 63l
		// );
		// TOCHandle tochandle = label2.getTOC( );
		// member = tochandle.getMember( TOC.TOC_EXPRESSION );
		// path = ModuleUtil.getScriptUID( member );
		// assertEquals(
		// "/report/body/label[@id=\"63\"]/structure[@name=\"toc\"]/expression[@name=\"expressionValue\"]",
		// //$NON-NLS-1$
		// path );

		// transfer script uid to script value
		String value = ModuleUtil.getScript(designHandle, "/report/body/label[@id=\"62\"]/method[@name=\"onPrepare\"]"); //$NON-NLS-1$
		assertEquals("\"prepare\"", value);//$NON-NLS-1$

		// value = ModuleUtil
		// .getScript(
		// designHandle,
		// "/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/expression[@name=\"testExpr\"]"
		// ); //$NON-NLS-1$
		// assertEquals( "row[\"LASTNAME\"]", value );//$NON-NLS-1$
		//
		// value = ModuleUtil
		// .getScript(
		// designHandle,
		// "/report/body/label[@id=\"63\"]/structure[@name=\"toc\"]/expression[@name=\"expressionValue\"]"
		// ); //$NON-NLS-1$
		// assertEquals( "toc expression", value );//$NON-NLS-1$
		//
		// value = ModuleUtil
		// .getScript(
		// designHandle,
		// "/report/body/label[@id=\"62\"]/list-property[@name=\"highlightRules\"]/structure[1]/list-property[@name=\"value1\"]/value[1]"
		// ); //$NON-NLS-1$
		//
		// assertEquals( "\"Tseng\"", value );//$NON-NLS-1$
	}

	/**
	 * Tests the method getElementsByType in ModuleUtil.
	 *
	 * @throws Exception
	 */
	public void testGetElementsByType() throws Exception {
		openDesign("ModuleUtilTest_1.xml"); //$NON-NLS-1$

		assertTrue(ModuleUtil.getElementsByType(designHandle, ReportDesignConstants.IMAGE_ITEM).isEmpty());

		List<DesignElementHandle> elements = ModuleUtil.getElementsByType(designHandle,
				ReportDesignConstants.LABEL_ITEM);
		assertEquals(2, elements.size());
		assertTrue(elements.get(0) instanceof LabelHandle);
		assertTrue(elements.get(1) instanceof LabelHandle);

		assertEquals(1, ModuleUtil.getElementsByType(designHandle, ReportDesignConstants.TEXT_ITEM).size());
		assertEquals(2, ModuleUtil.getElementsByType(designHandle, ReportDesignConstants.TABLE_ITEM).size());
		assertEquals(3, ModuleUtil.getElementsByType(designHandle, ReportDesignConstants.LISTING_ITEM).size());
	}
}
