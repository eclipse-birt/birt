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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PrivateStyleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests parser compatibility.
 */

public class ParserCompatibilityTest extends BaseTestCase {

	private String resultSetClearFileName = "CompatibleResultSetClearTest.xml";//$NON-NLS-1$
	private String resultSetHintClearFileName = "CompatibleResultSetHintClearTest.xml";//$NON-NLS-1$
	private String resouceFileName = "CompatibleResourceFileTest.xml";//$NON-NLS-1$

	/**
	 * Test clear 'resultSet' property before version 3.2.2
	 * 
	 * @throws Exception
	 */

	public void testClearResultSet() throws Exception {
		openDesign(resultSetClearFileName);
		OdaDataSetHandle odaHandle = (OdaDataSetHandle) designHandle.getElementByID(5);
		assertNull(odaHandle.getListProperty(IDataSetModel.RESULT_SET_PROP));

	}

	/**
	 * Test clear 'resultSetHint' property between version 3.2.2 and 3.2.6
	 * 
	 * @throws Exception
	 */

	public void testClearResultSetHint() throws Exception {
		openDesign(resultSetHintClearFileName);
		OdaDataSetHandle odaHandle = (OdaDataSetHandle) designHandle.getElementByID(5);
		assertNull(odaHandle.getListProperty(IDataSetModel.RESULT_SET_HINTS_PROP));

	}

	/**
	 * Tests the compatibility for private and public driver properties in ODA data
	 * source.
	 * 
	 * @throws Exception if any exception
	 */

	public void testPrivateOdaDriverProperties() throws Exception {
		openDesign("CompatiblePublicOdaDriverProperties.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatiblePublicOdaDriverProperties_golden.xml"));//$NON-NLS-1$

		openDesign("CompatiblePrivateOdaDriverProperties.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatiblePrivateOdaDriverProperties_golden.xml"));//$NON-NLS-1$

	}

	/**
	 * Tests the compatibility for old ODA driver model property name in ODA data
	 * source.
	 * 
	 * @throws Exception if any exception
	 */

	public void testOldOdaDriverModelPropertyName() throws Exception {
		openDesign("CompatibleOldOdaDriverModelProperty.xml");//$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleOldOdaDriverModelProperty_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests the compatibility for the design file created from the deprecated
	 * extension point odaDriverModel.
	 * 
	 * @throws Exception if any exception
	 */
	public void testOdaDriverModelExtensionPoint() throws Exception {
		openDesign("CompatibleOdaDriverModelProperty.xml");//$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleOdaDriverModelProperty_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests the compatibility for encrypted property.
	 * 
	 * @throws Exception if any exception
	 */

	public void testEncryptedProperty() throws Exception {
		openDesign("CompatibleEncryptedProperty.xml");//$NON-NLS-1$

		save();

		// Now it's hard to test.
		// assertTrue( compareTextFile(
		// "CompatibleEncryptedProperty_golden.xml") );//$NON-NLS-1$

	}

	/**
	 * Tests the compatibility for "columnName" member of ComputedColumn structure.
	 * "columnName" is renamed to "name".
	 * 
	 * @throws Exception
	 */

	public void testComputedColumnsProperty() throws Exception {
		openDesign("CompatibleComputedColumnProperty.xml");//$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleComputedColumnProperty_golden.xml"));//$NON-NLS-1$

	}

	/**
	 * @throws Exception
	 */

	public void testOdaDataSetproperty() throws Exception {
		openDesign("CompatibleOdaDataSetProperty.xml");//$NON-NLS-1$

		OdaDataSetHandle dataSetHandle = (OdaDataSetHandle) designHandle.findDataSet("dataset1"); //$NON-NLS-1$
		assertNotNull(dataSetHandle);
		assertEquals(null, dataSetHandle.getQueryScript());

		save();
		assertTrue(compareFile("CompatibleOdaDataSetProperty_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Test cachedRowCount property in SimpleDataSet
	 * 
	 * @throws Exception
	 */

	public void testDataSetCachedRowCount() throws Exception {
		openDesign("CompatibleDataSetCachedRowCount.xml");//$NON-NLS-1$

		OdaDataSetHandle dataSetHandle = (OdaDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$
		assertNotNull(dataSetHandle);
		assertEquals(500, dataSetHandle.getCachedRowCount());

		save();
		assertTrue(compareFile("CompatibleDataSetCachedRowCount_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Old version: <property name="msgBaseName">message </property> New version:
	 * <property name="includeResource">message </property>
	 * <p>
	 * Old version: <property name="cheetSheet">cheet sheet </property> <br>
	 * New version: <property name="cheatSheet">cheat sheet </property>
	 * 
	 * @throws Exception
	 */

	public void testReportProperty() throws Exception {
		openDesign("CompatibleReportProperties.xml");//$NON-NLS-1$
		assertEquals("message", this.designHandle.getIncludeResource()); //$NON-NLS-1$
		assertEquals("cheet sheet", this.designHandle.getCheatSheet()); //$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleReportProperties_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Old version: <property name="groupStart">message </property> New version:
	 * <property name="intervalBase">message </property>
	 * 
	 * @throws Exception
	 */

	public void testListingGroupProperty() throws Exception {
		openDesign("CompatibleListingGroupProperties.xml");//$NON-NLS-1$
		ListHandle list = (ListHandle) designHandle.findElement("My List"); //$NON-NLS-1$
		SlotHandle groupSlot = list.getGroups();
		GroupHandle group = (GroupHandle) groupSlot.get(0);

		assertEquals("2004/12/12", group.getGroupStart()); //$NON-NLS-1$
		group.setGroupStart("101"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleListingGroupProperties_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Old version: <expression name="contentTypeExpr">a.row </expression>
	 * <p>
	 * New version: <property name="contentType">html </property>
	 * 
	 * @throws Exception if any exception
	 */

	public void testTextDataProperty() throws Exception {
		openDesign("CompatibleTextDataProperties.xml");//$NON-NLS-1$
		TextDataHandle dataHandle = (TextDataHandle) designHandle.findElement("Multi Line Data"); //$NON-NLS-1$
		assertNotNull(dataHandle.getElement());
		assertEquals("value expr", dataHandle.getValueExpr()); //$NON-NLS-1$
		assertEquals("content type expr", dataHandle.getContentTypeExpr()); //$NON-NLS-1$
		assertEquals("content type expr", dataHandle.getContentType()); //$NON-NLS-1$

		dataHandle = (TextDataHandle) designHandle.findElement("Text Data"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF, dataHandle.getContentType());
		dataHandle.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN);

		save();
		assertTrue(compareFile("CompatibleTextDataProperties_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests OdaDataSource with driver name or extension name.
	 * 
	 * @throws Exception if any exception
	 */

	public void testOdaDataSourceWithDriverNameAndExtensionName() throws Exception {
		openDesign("CompatibleOdaDataSourceWithDriverNameOrExtensionName.xml");//$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleOdaDataSourceWithDriverNameOrExtensionName_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests OdaDataSet with type
	 * 
	 * @throws Exception if any exception
	 */

	public void testOdaDataSetWithType() throws Exception {
		openDesign("CompatibleOdaDataSetWithType.xml");//$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleOdaDataSetWithType_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests DataSetParam structure with isNullable.
	 * 
	 * @throws Exception if any exception
	 */

	public void testDataSetParamWithIsNullable() throws Exception {
		openDesign("CompatibleDataSetParamWithIsNullable.xml");//$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleDataSetParamWithIsNullable_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public void testTableItemHighlightRule() throws Exception {

		openDesign("TableItemCompatibleTest.xml"); //$NON-NLS-1$
		TableHandle tableHandle = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$
		assertNotNull(tableHandle);

		PrivateStyleHandle styleHandle = (PrivateStyleHandle) tableHandle.getPrivateStyle();

		Iterator highlightHandles = styleHandle.highlightRulesIterator();

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);
		assertEquals("[this]", highlightHandle.getTestExpression()); //$NON-NLS-1$
		assertEquals("is-null", highlightHandle.getOperator()); //$NON-NLS-1$
		save();

		assertTrue(compareFile("TableHighlightRuleCompatible_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * @throws Exception
	 */
	public void testDataHighlightRule() throws Exception {
		openDesign("DataItemCompatibleTest.xml"); //$NON-NLS-1$
		DataItem data = (DataItem) design.findElement("Body Data"); //$NON-NLS-1$
		DataItemHandle dataHandle = (DataItemHandle) data.getHandle(design);
		assertNotNull(dataHandle);

		PrivateStyleHandle styleHandle = (PrivateStyleHandle) dataHandle.getPrivateStyle();

		Iterator highlightHandles = styleHandle.highlightRulesIterator();

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);
		assertEquals("[this]", highlightHandle.getTestExpression()); //$NON-NLS-1$
		assertEquals("is-null", highlightHandle.getOperator()); //$NON-NLS-1$
		save();

		assertTrue(compareFile("DataItemHighlightRuleCompatible_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * If the extension id is invalid, related properties should be parsed and the
	 * file can be opened.
	 * 
	 * @throws Exception
	 */

	public void testWrongExtensionID() throws Exception {
		openDesign("WrongExtensionID.xml");//$NON-NLS-1$

		save();

		OdaDataSetHandle setHandle = (OdaDataSetHandle) designHandle.findDataSet("dataset1"); //$NON-NLS-1$

		// this is not a ROM-defined property. The value is null.
		assertNull(setHandle.getProperty("queryScript")); //$NON-NLS-1$

		// user property is supported by ROM. should parse it.

		assertEquals("1", setHandle.getStringProperty("tmpVar")); //$NON-NLS-1$//$NON-NLS-2$
		assertTrue(compareFile("WrongExtensionID_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * "onRow" property value of table/list is set to be "onCreate" of detail rows.
	 * 
	 * "onFinish", "onStart" property values are ignored.
	 * 
	 * @throws Exception
	 */

	public void testOnMumbleProperty() throws Exception {
		openDesign("TableOnMumbleCompatibleTest.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("TableOnMumbleCompatibleTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public void testIncludedLibraryCompatible() throws Exception {

		openDesign("IncludedLibraryCompatibleTest.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("IncludedLibraryCompatibleTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Test compatibility of page breaks.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCompatibilityOfPageBreaks() throws Exception {
		openDesign("CompatiblePageBreaks.xml"); //$NON-NLS-1$
		TableHandle table = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$

		// test table
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO, table.getProperty(IStyleModel.PAGE_BREAK_AFTER_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS,
				table.getProperty(IStyleModel.PAGE_BREAK_BEFORE_PROP));

		// test group
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO,
				((GroupHandle) table.getGroups().get(0)).getProperty(IStyleModel.PAGE_BREAK_AFTER_PROP));

		// test row
		assertEquals("inherit", //$NON-NLS-1$
				((GroupHandle) table.getGroups().get(0)).getHeader().get(0)
						.getProperty(IStyleModel.PAGE_BREAK_AFTER_PROP));

		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, ((GroupHandle) table.getGroups().get(0)).getFooter()
				.get(0).getProperty(IStyleModel.PAGE_BREAK_AFTER_PROP));

	}

	/**
	 * Tests compatibility of the format property.
	 * 
	 * @throws Exception
	 */

	public void testScalarParameterFormatCompatible() throws Exception {
		openDesign("CompatibleScalarParameterFormat.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatibleScalarParameterFormat_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test all properties of the obsolete multi-line-data.
	 * 
	 * @throws Exception if opening design file failed.
	 */

	public void testObsoleteParser() throws Exception {
		openDesign("TextDataItemParseTest_obsolete.xml"); //$NON-NLS-1$
		TextDataHandle dataHandle = (TextDataHandle) designHandle.findElement("Multi Line Data"); //$NON-NLS-1$
		assertNotNull(dataHandle.getElement());
		assertEquals("value expr", dataHandle.getValueExpr()); //$NON-NLS-1$
		assertEquals("content type expr", dataHandle.getContentTypeExpr()); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testImageName() throws Exception {
		openDesign("CompatibleImageNameParseTest.xml"); //$NON-NLS-1$
		ImageHandle image = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$
		assertNull(image.getProperty(ReportItemHandle.BOUND_DATA_COLUMNS_PROP));
		assertEquals("image.jpg1", image.getImageName()); //$NON-NLS-1$
	}

	/**
	 * The backward compatibility for the old design in BIRT 1.0 or before. The flat
	 * file extension is not datatools.connectivity.oda....
	 * 
	 * @throws Exception
	 */

	public void testFlatfileExtendsionId() throws Exception {
		openDesign("CompatibleFlatFileExtensionId.xml"); //$NON-NLS-1$
		OdaDataSourceHandle source = (OdaDataSourceHandle) designHandle.findDataSource("Data Source1"); //$NON-NLS-1$
		assertEquals("org.eclipse.datatools.connectivity.oda.flatfile", source //$NON-NLS-1$
				.getExtensionID());

		OdaDataSetHandle set = (OdaDataSetHandle) designHandle.findDataSet("Data Set1"); //$NON-NLS-1$
		assertEquals("org.eclipse.datatools.connectivity.oda.flatfile.dataSet", set //$NON-NLS-1$
				.getExtensionID());
	}

	/**
	 * In BIRT 2.1.1, OdaDataSet.resultSetHints was removed. And the obsolete
	 * OdaDataSet.resultSetHints and OdaDataSet.resultSet are merged into the new
	 * OdaDataSet.resultSet.
	 * <p>
	 * The rule is by taking 1) the current ResultSet�s column name as the
	 * �nativeName�, and 2) the ResultSetHints�s column name as the �name�, in the
	 * merged OdaResultSetColumn structure.
	 * 
	 * 
	 * @throws Exception
	 */

	public void testOdaResultSets() throws Exception {
		openDesign("CompatibleOdaResultSetTest.xml"); //$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleOdaResultSet_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * ScriptdaDataSet.resultSet was removed. And the obsolete
	 * ScriptdaDataSet.resultSet are renamed to the new
	 * ScriptdaDataSet.resultSetHints.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testScriptDataSetResultSets() throws Exception {
		openDesign("CompatibleScriptDataSetResultSets.xml"); //$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleScriptDataSetResultSets_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests parse odadataset and odadatasouce if the extensionid is deprecated ,
	 * convert it to new one. this function apply after version 3.2.7
	 * 
	 * @throws Exception
	 */

	public void testMigrateNameSpace() throws Exception {
		openDesign("CompatibleConvertExtensionId.xml");//$NON-NLS-1$
		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle.findDataSource("Data Source"); //$NON-NLS-1$
		assertNotNull(dataSource);
		assertEquals("org.eclipse.datatools.enablement.oda.xml", dataSource.getExtensionID());//$NON-NLS-1$

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$
		assertNotNull(dataSet);
		assertEquals("org.eclipse.datatools.enablement.oda.xml.dataSet", dataSet.getExtensionID());//$NON-NLS-1$

	}

	/**
	 * <ul>
	 * <li>Tests toc backward.This function apply after version 3.2.9. TOC
	 * expression string to the TOC structure.
	 * <li>for version between 3 and 3.2.8, if no TOC specified, uses key expression
	 * as TOC.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testTOC() throws Exception {
		openDesign("CompatibleTOC.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleTOC_golden.xml")); //$NON-NLS-1$

		openDesign("CompatibleTOC_1.xml"); //$NON-NLS-1$
		TableHandle table1 = (TableHandle) designHandle.findElement("My table");//$NON-NLS-1$

		GroupHandle group = (GroupHandle) table1.getGroups().get(0);
		assertEquals("[Country]", group.getTocExpression()); //$NON-NLS-1$
		assertEquals("[Country]", group.getTOC().getExpression()); //$NON-NLS-1$

		ListHandle list1 = (ListHandle) designHandle.findElement("My List");//$NON-NLS-1$
		group = (GroupHandle) list1.getGroups().get(0);
		assertEquals("[Country]", group.getTocExpression()); //$NON-NLS-1$
		assertEquals("[Country]", group.getTOC().getExpression()); //$NON-NLS-1$

	}

	/**
	 * If visibilities of ODA Properties defined in plugin.xml are "hidden", treat
	 * them as private driver properties. The old design file with such properties
	 * will be converted.
	 * 
	 * @throws Exception
	 */

	public void testODAPrivateProperties() throws Exception {
		openDesign("CompatibleOdaPrivateProps.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleOdaPrivateProps_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * allowNull and allowBlank on ScalarParameter have been replaced by
	 * "isRequired". Rules are:
	 * 
	 * <table>
	 * <th align="left">For string data type</th>
	 * <tr>
	 * <td>Set isRequired=true; if allowBlank=false</td>
	 * </tr>
	 * <tr>
	 * <td>Set isRequired=false, if allowBalnk=true</td>
	 * </tr>
	 * <br>
	 * 
	 * <th align="left">For non string data type</th>
	 * <tr>
	 * <td>Set isRequired=true; if allowNull=false</td>
	 * </tr>
	 * <tr>
	 * <td>Set isRequired=false, if allowNull=true</td>
	 * </tr>
	 * </table>
	 * 
	 * @throws Exception
	 */

	public void testScalarParamAllowProps() throws Exception {
		openDesign("CompatibleScalarParamAllowPropsTest.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatibleScalarParamAllowPropsTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Since the design file version 3.2.11. THe aggregate on becomes the
	 * simple-property-list. The previous type is string.
	 * 
	 * @throws Exception
	 */

	public void testColumnBinding() throws Exception {
		openDesign("CompatibleColumnBindingTest.xml"); //$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleColumnBindingTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Since version 3.2.13, all the level is unique within dimension scope not
	 * general scope in the design.Test the parser for level reference and computed
	 * column conversion.
	 * 
	 * @throws Exception
	 */
	public void testCompatibileLevelName() throws Exception {
		openDesign("CompatibleLevelName.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatibleLevelName_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * If the design version is less than 3.2.13 and element names contain /,\,/.!;,
	 * these characters should be automatically changed to _.
	 * 
	 * @throws Exception
	 */

	public void testCompatibleInvalidCharsInName() throws Exception {
		openDesign("CompatibleInvalidCharsInName.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatibleInvalidCharsInName_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * If the design version is less than 3.2.16 and the string value is converted
	 * into the list value.
	 * 
	 * @throws DesignFileException
	 */

	public void testIncludeResource() throws DesignFileException {
		// validate the included resource which is parsed before version 3.2.16

		openDesign(resouceFileName, ULocale.ENGLISH);

		List list = designHandle.getListProperty(IModuleModel.INCLUDE_RESOURCE_PROP);

		assertEquals(1, list.size());
		assertEquals("library", list.get(0)); //$NON-NLS-1$
	}

	/**
	 * Tests the CDATA in the report. CDATA Characters in the design with the
	 * version before 3.2.16 should not be de-escaped during parsing and escaped
	 * during writing.
	 * <p>
	 * Used TextItem.CONTENT as test cases. What saw in the design file should be
	 * consistent with the text in the cases.
	 * 
	 * 
	 * @throws Exception
	 */

	public void testCDATAParser() throws Exception {
		openDesign("CompatibleCDATAParseTest.xml"); //$NON-NLS-1$

		TextItemHandle text = (TextItemHandle) designHandle.findElement("text1"); //$NON-NLS-1$

		assertEquals("text & < > ' \" static", //$NON-NLS-1$
				text.getContent());

		text = (TextItemHandle) designHandle.findElement("text2"); //$NON-NLS-1$
		assertEquals("text &amp; &lt; &gt; &apos; &quot; static", //$NON-NLS-1$
				text.getContent());

		text = (TextItemHandle) designHandle.findElement("text3"); //$NON-NLS-1$
		assertEquals("]]&gt;\n\n\n]]&gt; ]] &amp;gt; &amp;&amp;gt;", //$NON-NLS-1$
				text.getOnPrepare());

		save();

		saveOutputFile("CompatibleCDATAParseTest_golden.xml"); //$NON-NLS-1$
	}

	public void testScalarParamSortBy() throws Exception {
		openDesign("CompatibleSortByParseTest.xml"); //$NON-NLS-1$

		ScalarParameterHandle paramHandle = (ScalarParameterHandle) designHandle.getParameters().get(0);
		ExpressionHandle exprHandle = paramHandle.getExpressionProperty(ScalarParameterHandle.SORT_BY_COLUMN_PROP);
		assertNotNull(exprHandle);
		assertTrue(exprHandle.getValue() instanceof Expression);

		save();
		assertTrue(compareFile("CompatibleSortByParseTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests compatibility for parameter type property in <ScalarParameter>,
	 * <OdaDataSet> and <JointDataSet>.
	 * 
	 * @throws Exception
	 */
	public void testParameterType() throws Exception {
		openDesign("CompatibleParameterTypeTest.xml"); //$NON-NLS-1$

		SlotHandle params = designHandle.getParameters();

		// tests compatibility for any.
		ScalarParameterHandle handle = (ScalarParameterHandle) params.get(0);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_ANY, handle.getDataType());

		// tests default value.
		handle = (ScalarParameterHandle) params.get(1);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_STRING, handle.getDataType());

		// tests set parameter type property value as any.

		try {
			handle.setDataType(DesignChoiceConstants.PARAM_TYPE_ANY);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$

		Iterator parameters = dataSet.parametersIterator();

		DataSetParameterHandle parameter = (OdaDataSetParameterHandle) parameters.next();

		// tests compatibility for any.
		assertEquals(DesignChoiceConstants.PARAM_TYPE_ANY, parameter.getDataType());
		parameter = (OdaDataSetParameterHandle) parameters.next();

		// tests default value.
		assertEquals(DesignChoiceConstants.PARAM_TYPE_STRING, parameter.getDataType());

		// tests set parameter type property value as any.
		try {
			handle.setDataType(DesignChoiceConstants.PARAM_TYPE_ANY);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		JointDataSetHandle jointDataSet = (JointDataSetHandle) designHandle.findDataSet("JointDataSet"); //$NON-NLS-1$

		parameters = jointDataSet.parametersIterator();

		parameter = (DataSetParameterHandle) parameters.next();

		// tests compatibility for any.
		assertEquals(DesignChoiceConstants.PARAM_TYPE_ANY, parameter.getDataType());
		parameter = (DataSetParameterHandle) parameters.next();

		// tests default value.
		assertEquals(DesignChoiceConstants.PARAM_TYPE_STRING, parameter.getDataType());

		// tests set parameter type property value as any.
		try {
			handle.setDataType(DesignChoiceConstants.PARAM_TYPE_ANY);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * Tests compatibility for the the newHandlerOnEachEvent Property. For the old
	 * version report, if the eventHandlerClass property has value, the default
	 * value of the newHandlerOnEachEvent property will be true. If the
	 * eventHandlerClass has no value, the default value of the
	 * newHandlerOnEachEvent is false.
	 * 
	 * @throws Exception
	 */
	public void testNewHandlerOnEachEventProp() throws Exception {
		openDesign("CompatibleNewHandlerOnEachEventPropTest.xml"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleNewHandlerOnEachEventPropTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Test backward compatibility. If the version is less than 3.2.18, the master
	 * page margin is set left-1.25in,top-1.00in,right-1.25in,bottom-1.00in.
	 * 
	 * @throws Exception
	 */
	public void testPageMargin() throws Exception {
		openDesign("CompatiblePageMarginTest.xml");//$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatiblePageMarginTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test backward compatibility. If the version is less than 3.2.18, the master
	 * page locates in report design extends the master page locates in library
	 * which has the margin value, the master page margin will not be set.
	 * 
	 * @throws Exception
	 */
	public void testExtendedPageMargin() throws Exception {
		openDesign("CompatibleExtendedPageMarginTest.xml");//$NON-NLS-1$

		SimpleMasterPageHandle page = (SimpleMasterPageHandle) designHandle.findMasterPage("NewSimpleMasterPage"); //$NON-NLS-1$

		assertEquals("0.1in", page.getTopMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.2in", page.getLeftMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.3in", page.getBottomMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.4in", page.getRightMargin().getStringValue()); //$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleExtendedPageMarginTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests query text property parser compatibility. The contents in the property
	 * should be kept when the query text property is converted to literal XML.
	 * 
	 * @throws Exception
	 */
	public void testQueryTextParser() throws Exception {
		openDesign("CompatibleQueryTextParseTest.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleQueryTextParseTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Test backward compatibility. Uses ScalarParameter.defaultValue as examples.
	 * 
	 * @throws Exception
	 */

	public void testPropertyToExpression() throws Exception {
		openDesign("CompatiblePropToExprTest.xml");//$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatiblePropToExprTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests backward compatibility. The variableName property value will be
	 * converted to name property value of the variableElement.
	 * 
	 * @throws Exception
	 */
	public void testVariableNameParser() throws Exception {
		openDesign("CompatibleVariableNameTest.xml"); //$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleVariableNameTest_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests the compatibility about the default value change of layoutPreference.
	 * In 3.2.20, we change it from auto layout to fixed layout.
	 * 
	 * @throws Exception
	 */
	public void testReportLayoutPreference() throws Exception {
		openDesign("CompatibleReportLayoutPreferenceTest.xml"); //$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleReportLayoutPreferenceTest_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests the compatibility about the display name id. In 3.2.20, the
	 * displayNameID is converted from string type to resourceKey type.
	 * 
	 * @throws Exception
	 */
	public void testDisplayNameID() throws Exception {
		openDesign("CompatibleDisplayNameIDTest.xml"); //$NON-NLS-1$

		save();

		assertTrue(compareFile("CompatibleDisplayNameIDTest_golden.xml"));//$NON-NLS-1$
	}
}
