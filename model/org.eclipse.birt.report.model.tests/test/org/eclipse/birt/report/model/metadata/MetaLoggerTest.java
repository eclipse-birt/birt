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

package org.eclipse.birt.report.model.metadata;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IMetaLogger;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.XMLParserException;

import com.ibm.icu.util.ULocale;

/**
 * Test case to test meta-data error logging. Test to check that all the
 * exceptions have been.
 * 
 */
public class MetaLoggerTest extends AbstractMetaTest {

	protected void tearDown() throws Exception {
	}

	private IMetaLogger logger = null;

	/**
	 * List of errors collected during parsing.
	 */

	private List errorList = null;

	// Two kinds of exception.

	private final static int METAREADER_EXCEPTION = 0;
	private final static int METADATA_EXCEPTION = 1;

	/**
	 * Constructor.
	 */

	public MetaLoggerTest() {
		// clear up the manager, remove the default one.

		MetaLogManager.shutDown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// don not call super.setUp().
		// We wanna to hold the meta initialization.
		errorList = null;
	}

	/**
	 * Remove the logger from the manager, the stream locked by the logger will be
	 * released.
	 * 
	 */

	protected void clearup() {
		if (logger != null)
			MetaLogManager.removeLogger(logger);

		logger = null;
	}

	/**
	 * Reset and load the metadata with the specified rom file.
	 * 
	 * @param fileName rom file name.
	 */

	private void loadMeta(String fileName) {
		ThreadResources.setLocale(ULocale.getDefault());

		try {
			MetaDataDictionary.reset();
			loadMetaData(MetaLoggerTest.class.getResourceAsStream("input/" + fileName)); //$NON-NLS-1$
		} catch (MetaDataParserException e) {
			if (e.getException() instanceof XMLParserException) {
				errorList = ((XMLParserException) e.getException()).getErrorList();
			}
		}
	}

	/**
	 * Input file not exist.
	 * 
	 * @throws Exception
	 */

	public void test_FILE_NOT_FOUND() throws Exception {
		// this.logger = createAndRegisterLogger( "metaLog.log" ); //$NON-NLS-1$

		MetaDataDictionary.reset();
		try {
			MetaDataReader.read("input/none-exsit.def"); //$NON-NLS-1$
		} catch (MetaDataParserException e) {
			assertEquals(MetaDataParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * Parser Error.
	 * <p>
	 * In this case, input stream is null when passed to the parser.
	 * 
	 */

	public void test_PARSER_ERROR() {
		// this.logger = createAndRegisterLogger( "metaLog1.log" );
		// //$NON-NLS-1$

		try {
			loadMetaData(MetaLoggerTest.class.getResourceAsStream("input/none-exsit.def")); //$NON-NLS-1$
		} catch (MetaDataParserException e) {
			assertEquals(MetaDataParserException.DESIGN_EXCEPTION_PARSER_ERROR, e.getErrorCode());
		}
	}

	/**
	 * Tests the name required cases. Name required for:
	 * <ul>
	 * <li>ChoiceType</li>
	 * <li>Element</li>
	 * <li>Member</li>
	 * <li>Property</li>
	 * <li>Slot</li>
	 * <li>Slot Type</li>
	 * <li>Structure</li>
	 * <li>StyleProperty</li>
	 * <li>Style(Predefined)</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file missing the followings names:
	 * <ul>
	 * <li>ChoiceType: fontFamily</li>
	 * <li>Element: JdbcDataSource</li>
	 * <li>Member: MapRule.operator</li>
	 * <li>Property: ReportDesign.author</li>
	 * <li>Slot: ReportDesign.styles</li>
	 * <li>Slot Type: Slot type of ReportDesign.masterPages</li>
	 * <li>Structure: HighlightRule</li>
	 * <li>StyleProperty: First styleProperty within Data (name =
	 * "background-color").</li>
	 * <li>Style: Predefined style: group-header-1</li>
	 * </ul>
	 */

	public void test_NAME_REQUIRED() {
		// ChoiceType caught
		// Element caught
		// Member caught
		// Property caught
		// Slot caught
		// Slot Type caught
		// Structure caught
		// StyleProperty caught
		// Style(Predefined) caught

		// this.logger = createAndRegisterLogger( "metaLog2.log" );
		// //$NON-NLS-1$

		loadMeta("romTest2.def"); //$NON-NLS-1$
		assertEquals(13, errorList.size());
	}

	/**
	 * Tests display name key required cases. DisplayNameID required for:
	 * <ul>
	 * <li>ChoiceType</li>
	 * <li>Element</li>
	 * <li>Member</li>
	 * <li>Property</li>
	 * <li>Slot</li>
	 * <li>Structure</li>
	 * <li>Style(Predefined)</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file missing the followings displayNameIDs:
	 * <ul>
	 * <li>ChoiceType: fontFamil.serif</li>
	 * <li>Element: JdbcDataSource</li>
	 * <li>Member: MapRule.operator</li>
	 * <li>Property: ReportDesign.author</li>
	 * <li>Slot: ReportDesign.styles</li>
	 * <li>Structure: CustomColor</li>
	 * <li>Style: Predefined style: group-header-1</li>
	 * </ul>
	 */

	public void test_DISPLAY_NAME_ID_REQUIRED() {
		// Choice caught
		// Element caught
		// Member caught
		// Property caught
		// Slot caught
		// Structure caught
		// Style(Predefined) caught

		// this.logger = createAndRegisterLogger( "metaLog3.log" );
		// //$NON-NLS-1$
		loadMeta("romTest3.def"); //$NON-NLS-1$
		assertEquals(8, errorList.size());
	}

	/**
	 * Tests multipleCardinality required case. multipleCardinality required for:
	 * <ul>
	 * <li>Slot</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file missing the follwings multipleCardinality:
	 * <ul>
	 * <li>ReportDesign.styles</li>
	 * <li>ReportDesign.parameters</li>
	 * </ul>
	 */
	public void test_MULTIPLE_CARDINALITY_REQUIRED() {
		// ReportDesign.styles caught
		// ReportDesign.parameters caught

		// this.logger = createAndRegisterLogger( "metaLog4.log" );
		// //$NON-NLS-1$
		loadMeta("romTest4.def"); //$NON-NLS-1$

		assertEquals(3, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_MULTIPLE_CARDINALITY_REQUIRED, 0,
				METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_MULTIPLE_CARDINALITY_REQUIRED, 1,
				METAREADER_EXCEPTION);

	}

	/**
	 * Tests type required cases. Type required for:
	 * <ul>
	 * <li>Member</li>
	 * <li>Property</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file missing the followings types:
	 * <ul>
	 * <li>Member: HighlightRule.operator</li>
	 * <li>Property: ReportDesign.author</li>
	 * </ul>
	 */

	public void test_TYPE_REQUIRED() {
		// Member caught
		// Property caught

		// this.logger = createAndRegisterLogger( "metaLog5.log" );
		// //$NON-NLS-1$
		loadMeta("romTest5.def"); //$NON-NLS-1$

		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_TYPE_REQUIRED, 0, METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_TYPE_REQUIRED, 1, METAREADER_EXCEPTION);
		assertEquals(3, errorList.size());
	}

	/**
	 * Tests name space. nameSpace of element can only be one of followings:
	 * <ul>
	 * <li>none</li>
	 * <li>masterPage</li>
	 * <li>parameter</li>
	 * <li>element</li>
	 * <li>dataSource</li>
	 * <li>dataSet</li>
	 * <li>style</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file set the DataSource.namespace = "invalid-namespace"
	 * 
	 */
	public void test_INVALID_NAME_SPACE() {
		// DataSource.namespace caught

		// this.logger = createAndRegisterLogger( "metaLog6.log" );
		// //$NON-NLS-1$
		loadMeta("romTest6.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_INVALID_NAME_SPACE, 0, METAREADER_EXCEPTION);
	}

	/**
	 * Tests xml name required cases. XML Name required for:
	 * <ul>
	 * <li>Choice</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file missing the xml name for:
	 * <ul>
	 * <li>Choice: fontWeight.lighter</li>
	 * </ul>
	 */
	public void test_XML_NAME_REQUIRED() {
		// fontWeight.normal caught

		// this.logger = createAndRegisterLogger( "metaLog7.log" );
		// //$NON-NLS-1$
		loadMeta("romTest7.def"); //$NON-NLS-1$
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_XML_NAME_REQUIRED, 0, METAREADER_EXCEPTION);
		assertEquals(2, errorList.size());
	}

	/**
	 * Default value for a property is not valid.
	 * 
	 * <p>
	 * In this case, input file has the follwing wrong default value:
	 * 
	 * ReportDesign.units.default="none-exsit-units"
	 * 
	 */
	public void test_INVALID_DEFAULT() {
		// ReportDesign.unis caught

		// this.logger = createAndRegisterLogger( "metaLog8.log" );
		// //$NON-NLS-1$
		loadMeta("romTest8.def"); //$NON-NLS-1$
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_DEFAULT, 0, METAREADER_EXCEPTION);
		assertEquals(2, errorList.size());
	}

	/**
	 * Tests the invalid type cases. Invalid type required for:
	 * <ul>
	 * <li>Property</li>
	 * <li>Member</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file has the following wrong type:
	 * <ul>
	 * <li>Property: ReportDesing.author.type="none-exsit-type"</li>
	 * <li>Member: HighlightRule.operator.type="none-exsit-type"</li>
	 * </ul>
	 */
	public void test_INVALID_TYPE() {
		// Property caught
		// Member caught

		// this.logger = createAndRegisterLogger( "metaLog9.log" );
		// //$NON-NLS-1$
		loadMeta("romTest9.def"); //$NON-NLS-1$
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE, 0, METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_TYPE, 1, METAREADER_EXCEPTION);
		assertEquals(3, errorList.size());
	}

	/*
	 * 
	 * 
	 * public void test_BUILD_FAILED( ) { }
	 */

	/**
	 * 
	 * DisplayNameId required for PropertyGroup.
	 * <p>
	 * In this case, input file missing the following displayNameId:
	 * <ul>
	 * <li>Style.font</li>
	 * </ul>
	 */
	public void test_GROUP_NAME_ID_REQUIRED() {
		// Style.font caught

		// this.logger = createAndRegisterLogger( "metaLog11.log" );
		// //$NON-NLS-1$
		loadMeta("romTest11.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_GROUP_NAME_ID_REQUIRED, 0, METAREADER_EXCEPTION);
	}

	/**
	 * 
	 * For property which type = "choice", ChoiceType indicated by detailType must
	 * have been defined already and valid.
	 * 
	 * <p>
	 * In this case, input file set the following wrong detail type:
	 * <ul>
	 * <li>Member: MapRule.operator.detailType="none-exist-choiceset"</li>
	 * <li>Property: ReportDesign.units.detailType="none-exist-choiceset"</li>
	 * </ul>
	 */

	public void test_INVALID_CHOICE_TYPE() {
		// Member caught
		// Property caught

		// this.logger = createAndRegisterLogger( "metaLog12.log" );
		// //$NON-NLS-1$
		loadMeta("romTest12.def"); //$NON-NLS-1$
		assertEquals(3, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_CHOICE_TYPE, 0, METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_CHOICE_TYPE, 1, METAREADER_EXCEPTION);
	}

	/**
	 * 
	 * For property which type = "choice", detailType must be specified.
	 * 
	 * <p>
	 * In this case, input file missing the following detailType:
	 * <ul>
	 * <li>Member: MapRule.operator.detailType=""</li>
	 * <li>Property: ReportDesign.units.detailType=""</li>
	 * </ul>
	 */
	public void test_CHOICE_TYPE_REQUIRED() {
		// Member caught
		// Property caught

		// this.logger = createAndRegisterLogger( "metaLog13.log" );
		// //$NON-NLS-1$

		loadMeta("romTest13.def"); //$NON-NLS-1$
		assertEquals(3, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_CHOICE_TYPE_REQUIRED, 0, METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_CHOICE_TYPE_REQUIRED, 1, METAREADER_EXCEPTION);
	}

	/**
	 * Tests structure type required cases. If type="StructList", detailType must be
	 * specified for:
	 * <ul>
	 * <li>Property</li>
	 * <li>Member</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file missing the detailType for a property
	 * type="StuctList":
	 * <ul>
	 * <li>Property: ReportDesign.colorPalette.detailType=""</li>
	 * <li>Member: Add a test member DrillThroughSearchKeys.testMember
	 * type="structList" detailType=""</li>
	 * </ul>
	 */
	public void test_STRUCT_TYPE_REQUIRED() {
		// Property caught
		// Member caught

		// this.logger = createAndRegisterLogger( "metaLog14.log" );
		// //$NON-NLS-1$
		loadMeta("romTest14.def"); //$NON-NLS-1$
		assertEquals(3, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_STRUCT_TYPE_REQUIRED, 0, METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_STRUCT_TYPE_REQUIRED, 1, METAREADER_EXCEPTION);
	}

	/**
	 * If type="StructList", the Structure specified by detailType must already have
	 * been defined.
	 * 
	 * <ul>
	 * <li>Property</li>
	 * <li>Member</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file have the following wrong detailTypes for property
	 * type="StuctList":
	 * <ul>
	 * <li>Property:
	 * ReportDesign.colorPalette.detailType="none-exsit-structureList"</li>
	 * <li>Member: Add a test member DrillThroughSearchKeys.testMember
	 * type="structList" detailType="not-defined-yet".</li>
	 * </ul>
	 */
	public void test_INVALID_STRUCT_TYPE() {
		// Property caught
		// Member caught

		// this.logger = createAndRegisterLogger( "metaLog15.log" );
		// //$NON-NLS-1$
		loadMeta("romTest15.def"); //$NON-NLS-1$
		assertEquals(2, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_STRUCT_TYPE, 0, METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_STRUCT_TYPE, 1, METAREADER_EXCEPTION);
	}

	/**
	 * 
	 * If type="element", detailType must be specified.
	 * <ul>
	 * <li>Choice</li>
	 * </ul>
	 * 
	 * <p>
	 * In this case, input file missing the detailType for a property
	 * type="Element":
	 * <ul>
	 * <li>Property: DataSet.dataSource.detailType</li>
	 * </ul>
	 */
	public void test_ELEMENT_REF_TYPE_REQUIRED() {
		// Property caught
		// this.logger = createAndRegisterLogger( "metaLog16.log" );
		// //$NON-NLS-1$
		loadMeta("romTest16.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_ELEMENT_REF_TYPE_REQUIRED, 0, METAREADER_EXCEPTION);
	}

	// Followings are cases from logging MetaDataException.

	/**
	 * Missing element name when adding an element to the meta dictionary.
	 */
	public void test_MISSING_ELEMENT_NAME() {
		// This exception can never be caught during meta parsing.
		// In the ElementDefnState, state will return when elementName = null or
		// // "".

		ElementDefn newElement = new ElementDefn();
		try {
			MetadataTestUtil.addElementDefn(MetaDataDictionary.getInstance(), newElement);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_NAME, e.getErrorCode());
			assertEquals(
					"Message:Missing element name when adding an element to the meta-data dictionary. Error code:MISSING_ELEMENT_NAME", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Duplicate element name when adding to the metadata dictionary.
	 * <p>
	 * In this case, input file have defined two elementDefn with the same name
	 * "DataSource".
	 * 
	 */
	public void test_DUPLICATE_ELEMENT_NAME() {
		// Duplication of DataSource caught.

		// this.logger = createAndRegisterLogger( "metaLog17.log" );
		// //$NON-NLS-1$
		loadMeta("romTest17.def"); //$NON-NLS-1$
		assertEquals(2, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_NAME, 0, METADATA_EXCEPTION);

		MetaDataDictionary.reset();
		ElementDefn elementA = new ElementDefn();
		MetadataTestUtil.setName(elementA, "Name"); //$NON-NLS-1$

		ElementDefn elementB = new ElementDefn();
		MetadataTestUtil.setName(elementB, "Name"); //$NON-NLS-1$

		try {
			MetadataTestUtil.addElementDefn(MetaDataDictionary.getInstance(), elementA);
			MetadataTestUtil.addElementDefn(MetaDataDictionary.getInstance(), elementB);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_NAME, e.getErrorCode());
			assertEquals(
					"Message:Duplicate element names when adding the element [Name] to the meta-data dictionary. Error code:DUPLICATE_ELEMENT_NAME", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Predefined stye must have a name.
	 * 
	 */

	public void test_MISSING_STYLE_NAME() {
		// This exception can never be caught during meta parsing.
		// In the ElementDefnState, state will return when elementName = null or
		// // "".

		PredefinedStyle style = new PredefinedStyle();

		try {
			MetadataTestUtil.addPredefinedStyle(MetaDataDictionary.getInstance(), style);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_MISSING_STYLE_NAME, e.getErrorCode());
			assertEquals(
					"Message:Missing style name when adding a predefined style to the meta-data dictionary. Error code:MISSING_STYLE_NAME", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Duplicate predefined style name when adding to the metadata dictionary.
	 * <p>
	 * In this case, input file have defined two predefined style with the same name
	 * "group-header-1".
	 * 
	 */
	public void test_DUPLICATE_STYLE_NAME() {
		// this.logger = createAndRegisterLogger( "metaLog18.log" );
		// //$NON-NLS-1$
		loadMeta("romTest18.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_STYLE_NAME, 0, METADATA_EXCEPTION);

		MetaDataDictionary.reset();
		ElementDefn elementA = new ElementDefn();
		MetadataTestUtil.setName(elementA, "Name"); //$NON-NLS-1$

		ElementDefn elementB = new ElementDefn();
		MetadataTestUtil.setName(elementB, "Name");//$NON-NLS-1$

		try {
			MetadataTestUtil.addElementDefn(MetaDataDictionary.getInstance(), elementA);
			MetadataTestUtil.addElementDefn(MetaDataDictionary.getInstance(), elementB);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_NAME, e.getErrorCode());
			assertEquals(
					"Message:Duplicate element names when adding the element [Name] to the meta-data dictionary. Error code:DUPLICATE_ELEMENT_NAME", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Missing Style element, rom must have definition for the style element.
	 * <p>
	 * In this case, input file missing element name="Style"
	 */
	public void test_STYLE_TYPE_MISSING() {
		// this.logger = createAndRegisterLogger( "metaLog19.log" );
		// //$NON-NLS-1$
		loadMeta("romTest19.def"); //$NON-NLS-1$
		assertEquals(59, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_STYLE_TYPE_MISSING, 0, METADATA_EXCEPTION);
	}

	/**
	 * Element can not have two properties with the same name.
	 * <p>
	 * In this case, input file has two property name="author" defined on
	 * ReportDesign, "displayName" defined on DataSource and "font-family" defined
	 * on MasterPage.
	 */

	public void test_DUPLICATE_PROPERTY() {
		// this.logger = createAndRegisterLogger( "metaLog20.log" );
		// //$NON-NLS-1$
		loadMeta("romTest20.def"); //$NON-NLS-1$
		assertEquals(3, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_PROPERTY, 0, METADATA_EXCEPTION);
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_PROPERTY, 1, METADATA_EXCEPTION);
	}

	/**
	 * Element extends from another element that was not found.
	 * <p>
	 * In this case, input file has the following violation:
	 * ScalarParameter.extends="none-exsit-parent" (which should be Parameter)
	 * 
	 */
	public void test_ELEMENT_PARENT_NOT_FOUND() {
		// this.logger = createAndRegisterLogger( "metaLog21.log" );
		// //$NON-NLS-1$
		loadMeta("romTest21.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_ELEMENT_PARENT_NOT_FOUND, 0, METADATA_EXCEPTION);
	}

	/**
	 * 1. "hasStyle" attribute of an element is false, but it has StyleProperties
	 * defined on it.
	 * <p>
	 * 2. "hasStyle" attribute of an element is true and it is an container(slot
	 * count>0), but it has StyleProperties defined on it.
	 * <p>
	 * In this case, input file has the following violation: 1. DataSource.hasStyle
	 * = false. But it has defined a styleProperty "color" on it.
	 * <p>
	 * 2. GraphicMasterPage.hasStyle = "true" and slotCount > 0. But it has defined
	 * a styleProperty "color" on it.
	 */
	public void test_ILLEGAL_STYLE_PROPS() {
		// case1 caught
		// case2 caught

		// this.logger = createAndRegisterLogger( "metaLog22.log" );
		// //$NON-NLS-1$
		loadMeta("romTest22.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_ILLEGAL_STYLE_PROPS, 0, METADATA_EXCEPTION);

		// this.logger = createAndRegisterLogger( "metaLog23.log" );
		// //$NON-NLS-1$
		loadMeta("romTest23.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_ILLEGAL_STYLE_PROPS, 0, METADATA_EXCEPTION);
	}

	/**
	 * If element A is abstract then its parent must also be abstract.
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * JdbcDataSource extends from DataSource.
	 * <p>
	 * DataSource.isAbstract = "false", JdbcDataSource.isAbstract = "true".
	 * 
	 */
	public void test_ILLEGAL_ABSTRACT_ELEMENT() {
		// this.logger = createAndRegisterLogger( "metaLog24.log" );
		// //$NON-NLS-1$
		loadMeta("romTest24.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_ILLEGAL_ABSTRACT_ELEMENT, 0, METADATA_EXCEPTION);
	}

	/**
	 * Style property defined on element was not found.
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * Define a <StyleProperty name="none-exsit-styleProperty"/> on Label
	 * 
	 */
	public void test_STYLE_PROP_NOT_FOUND() {
		// this.logger = createAndRegisterLogger( "metaLog25.log" );
		// //$NON-NLS-1$
		loadMeta("romTest25.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_STYLE_PROP_NOT_FOUND, 0, METADATA_EXCEPTION);
	}

	/**
	 * The PropertyType of the property has not been set. ( propDefn.getType() ==
	 * null )
	 * 
	 */

	public void test_PROP_TYPE_ERROR() {
		// This build time exception can never be caught during parsing.
		// In the PropertyState, state will return when
		// dictionary.getPropertyType( type ) == null.
		// This property will be exsit when building the element.

		PropertyDefn propDefn = new SystemPropertyDefn();
		propDefn.setName("Property1"); //$NON-NLS-1$

		try {
			MetadataTestUtil.buildPropertyDefn(propDefn);
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_PROP_TYPE_ERROR, e.getErrorCode());
			assertEquals(
					"Message:PropertyType of the property [Property1] has not been set. ( propDefn.getType() == null ) Error code:PROP_TYPE_ERROR", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * The ChoiceSet has not been set on the property definition. ( type==
	 * PropertyType.CHOICE_TYPE && getChoices() == null )
	 * 
	 */
	public void test_MISSING_PROP_CHOICES() {
		PropertyDefn propDefn = new SystemPropertyDefn();
		propDefn.setName("Property1"); //$NON-NLS-1$
		propDefn.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.CHOICE_TYPE));

		try {
			MetadataTestUtil.buildPropertyDefn(propDefn);
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_MISSING_PROP_CHOICES, e.getErrorCode());
			assertEquals(
					"Message:Missing ChoiceSet for choice type property [Property1]. Error code:MISSING_PROP_CHOICES", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * A slot should hold at least one content element.
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * ParameterGroup has slot, but with no content type.
	 * 
	 */

	public void test_MISSING_SLOT_TYPE() {
		// this.logger = createAndRegisterLogger( "metaLog26.log" );
		// //$NON-NLS-1$
		loadMeta("romTest26.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_MISSING_SLOT_TYPE, 0, METADATA_EXCEPTION);
	}

	/**
	 * Slot displayNameID attribute must be specified.
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * ParameterGroup.parameters.displayNameID="".
	 */

	public void test_MISSING_SLOT_NAME() {
		// this.logger = createAndRegisterLogger( "metaLog27.log" );
		// //$NON-NLS-1$
		loadMeta("romTest27.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_MISSING_SLOT_NAME, 0, METADATA_EXCEPTION);
	}

	/**
	 * Slot type for the element is not valid, the target element definition can not
	 * be found.
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * ParameterGroup.parameters.type1.name="none-exsit-type".
	 */

	public void test_INVALID_SLOT_TYPE() {
		// this.logger = createAndRegisterLogger( "metaLog28.log" );
		// //$NON-NLS-1$
		loadMeta("romTest28.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_INVALID_SLOT_TYPE, 0, METADATA_EXCEPTION);
	}

	/**
	 * Some element: 1)Style 2)ReportElement 3)ReportDesign must been defined in
	 * rom.def.
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * ReportDesign not defined.
	 */

	public void test_ELEMENT_NAME_CONST() {
		// this.logger = createAndRegisterLogger( "metaLog29.log" );
		// //$NON-NLS-1$
		loadMeta("romTest29.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_ELEMENT_NAME_CONST, 0, METADATA_EXCEPTION);
	}

	/**
	 * Missing choice set name when adding a Choice Set to the meta dictionary.
	 * 
	 */
	public void test_MISSING_CHOICE_SET_NAME() {
		ChoiceSet choices1 = new ChoiceSet(null);
		try {
			MetadataTestUtil.addChoiceSet(MetaDataDictionary.getInstance(), choices1);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_MISSING_CHOICE_SET_NAME, e.getErrorCode());
			assertEquals(
					"Message:Missing choice set name when adding a Choice Set to the meta-data dictionary. Error code:MISSING_CHOICE_SET_NAME", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Duplicate choice set names when adding the choice set to the meta dictionary.
	 * 
	 */
	public void test_DUPLICATE_CHOICE_SET_NAME() {
		ChoiceSet choices1 = new ChoiceSet("choices1"); //$NON-NLS-1$
		ChoiceSet choices2 = new ChoiceSet("choices1"); //$NON-NLS-1$
		try {
			MetadataTestUtil.addChoiceSet(MetaDataDictionary.getInstance(), choices1);
			MetadataTestUtil.addChoiceSet(MetaDataDictionary.getInstance(), choices2);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_CHOICE_SET_NAME, e.getErrorCode());
			assertEquals(
					"Message:Duplicate choice set names when adding the choice set [choices1] to the meta-data dictionary. Error code:DUPLICATE_CHOICE_SET_NAME", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Missing structure name when adding a structure to the meta dictionary.
	 * 
	 */
	public void test_MISSING_STRUCT_NAME() {
		StructureDefn structDefn = new StructureDefn(null);
		try {
			MetadataTestUtil.addStructureDefn(MetaDataDictionary.getInstance(), structDefn);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_MISSING_STRUCT_NAME, e.getErrorCode());
			assertEquals(
					"Message:Missing structure name when adding a structure to the meta-data dictionary. Error code:MISSING_STRUCT_NAME", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Duplicate structure name when adding the structure to the meta dictionary.
	 * 
	 */

	public void test_DUPLICATE_STRUCT_NAME() {
		StructureDefn structDefn1 = new StructureDefn("struct"); //$NON-NLS-1$
		StructureDefn structDefn2 = new StructureDefn("struct"); //$NON-NLS-1$
		try {
			MetadataTestUtil.addStructureDefn(MetaDataDictionary.getInstance(), structDefn1);
			MetadataTestUtil.addStructureDefn(MetaDataDictionary.getInstance(), structDefn2);
			fail();
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_STRUCT_NAME, e.getErrorCode());
			assertEquals(
					"Message:Duplicate structure names when adding the structure [struct] to the meta-data dictionary. Error code:DUPLICATE_STRUCT_NAME", //$NON-NLS-1$
					e.getMessage().trim());

		}
	}

	/**
	 * The detail sturcture definition has not been set on the property definition.
	 * ( type == PropertyType.STRUCT_LIST_TYPE && getStructDefn() == null)
	 * 
	 */

	public void test_MISSING_STRUCT_DEFN() {
		loadMeta("rom.def");
		PropertyDefn prop = new SystemPropertyDefn();
		prop.setName("prop1"); //$NON-NLS-1$
		prop.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.STRUCT_TYPE));
		prop.setDetails(null);
		prop.setOwner((ElementDefn) MetaDataDictionary.getInstance().getElement(ReportDesignConstants.TABLE_ITEM));

		try {
			MetadataTestUtil.buildPropertyDefn(prop);
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_MISSING_STRUCT_DEFN, e.getErrorCode());
			assertEquals(
					"Message:Missing structure definition for structure list type property [prop1] in [Table]. Error code:MISSING_STRUCT_DEFN", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * The detail referenced element name has not been set on the property
	 * definition.( type == ELEMENT_REF_TYPE && detail == null )
	 * 
	 */

	public void test_MISSING_ELEMENT_TYPE() {
		PropertyDefn prop = new SystemPropertyDefn();
		prop.setName("prop1"); //$NON-NLS-1$
		prop.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.ELEMENT_REF_TYPE));
		prop.setDetails(null);

		try {
			MetadataTestUtil.buildPropertyDefn(prop);
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE, e.getErrorCode());
			assertEquals(
					"Message:Missing element type for elementRef property [prop1]. Error code:MISSING_ELEMENT_TYPE", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Element referenced by the property was not defined. (
	 * dictionary.getElement(name) == null )
	 * 
	 */

	public void test_UNDEFINED_ELEMENT_TYPE() {
		PropertyDefn prop = new SystemPropertyDefn();
		prop.setName("prop1"); //$NON-NLS-1$
		prop.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.ELEMENT_REF_TYPE));
		prop.setDetails("none-exsit-element-ref"); //$NON-NLS-1$

		try {
			MetadataTestUtil.buildPropertyDefn(prop);
		} catch (MetaDataException e) {
			assertEquals(MetaDataException.DESIGN_EXCEPTION_UNDEFINED_ELEMENT_TYPE, e.getErrorCode());
			assertEquals(
					"Message:Element [none-exsit-element-ref] specified by property [prop1] is not defined. Error code:UNDEFINED_ELEMENT_TYPE", //$NON-NLS-1$
					e.getMessage().trim());
		}
	}

	/**
	 * Element referenced by the property is an unnamed element.
	 * (namespace=='no_namespace').
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * DataSet.dataSource.type = "element" and DataSet.dataSource.detailType =
	 * "DataSource".
	 * <p>
	 * But, DataSource.nameSpace="none"
	 */

	public void test_UNNAMED_ELEMENT_TYPE() {
		// this.logger = createAndRegisterLogger( "metaLog30.log" );
		// //$NON-NLS-1$
		loadMeta("romTest30.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_UNNAMED_ELEMENT_TYPE, 0, METADATA_EXCEPTION);
	}

	/**
	 * Inconsistent property, style property cannot also be intrinsic. (isIntrinsic(
	 * ) && isStyleProperty( ) )
	 * <p>
	 * In this case, input file has the following violations:
	 * <p>
	 * Style.font-family.isStyleProperty = "true" and its isIntrinsic="true".
	 */

	public void test_INCONSISTENT_PROP_TYPE() {
		// this.logger = createAndRegisterLogger( "metaLog31.log" );
		// //$NON-NLS-1$
		loadMeta("romTest31.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_INCONSISTENT_PROP_TYPE, 0, METADATA_EXCEPTION);
	}

	/**
	 * missing javaClass attribute for a element definition that is not abstract.
	 * <p>
	 * In this case, input file has the following violations: ReportDesign is
	 * none-abstract, it missing the javaClass attribute.
	 * 
	 */

	public void test_MISSING_JAVA_CLASS() {
		// this.logger = createAndRegisterLogger( "metaLog32.log" );
		// //$NON-NLS-1$
		loadMeta("romTest32.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_MISSING_JAVA_CLASS, 0, METADATA_EXCEPTION);

	}

	/**
	 * The specified element java class cannot be instantiated maybe because it is
	 * an interface or is an abstract class or that the specified java class doesn't
	 * not provide a default constructor( or a constructor that takes no argument ).
	 * 
	 * <p>
	 * In this case, input file has the following violations:
	 * ReportDesign.javaClass="org.eclipse.birt.report.model.core.RootElement",
	 * which is an abstract class.
	 */

	public void test_JAVA_CLASS_INITIALIZE_ERROR() {
		// this.logger = createAndRegisterLogger( "metaLog33.log" );
		// //$NON-NLS-1$
		loadMeta("romTest33.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_JAVA_CLASS_INITIALIZE_ERROR, 0, METADATA_EXCEPTION);

	}

	/**
	 * The specified element java class can not be found in the current class path.
	 * <p>
	 * In this case, input file has the following violations:
	 * ReportDesign.javaClass="none-exsit-class-name".
	 * 
	 */

	public void test_JAVA_CLASS_JAVA_CLASS_LOAD_ERROR() {
		// this.logger = createAndRegisterLogger( "metaLog34.log" );
		// //$NON-NLS-1$
		loadMeta("romTest34.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_JAVA_CLASS_LOAD_ERROR, 0, METADATA_EXCEPTION);
	}

	/**
	 * The specified element java class is not a Design Element, that is, the class
	 * is not a kind of <code>DesignElement</code>.
	 * <p>
	 * In this case, input file has the following violations:
	 * ReportDesign.javaClass="java.util.HashMap", which is not a DesignElement.
	 * 
	 */
	public void test_INVALID_ELEMENT_JAVA_CLASS() {
		// this.logger = createAndRegisterLogger( "metaLog35.log" );
		// //$NON-NLS-1$
		loadMeta("romTest35.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());

		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_INVALID_ELEMENT_JAVA_CLASS, 0, METADATA_EXCEPTION);
	}

	/**
	 * In one element definition, two methods have the same name.
	 * 
	 */
	public void test_DUPLICATE_METHOD_NAME() {
		// this.logger = createAndRegisterLogger( "metaLog36.log" );
		// //$NON-NLS-1$
		loadMeta("romTest36.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());

		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_NAME, 0, METADATA_EXCEPTION);
	}

	/**
	 * In one element method definition, two arguments have the same name.
	 * 
	 */
	public void test_DUPLICATE_ARGUMENT_NAME() {
		// this.logger = createAndRegisterLogger( "metaLog37.log" );
		// //$NON-NLS-1$
		loadMeta("romTest37.def"); //$NON-NLS-1$
		assertEquals(1, errorList.size());

		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ARGUMENT_NAME, 0, METADATA_EXCEPTION);
	}

	/**
	 * <Allowed></Allowed> indicates the restriction information, it can only apply
	 * to a dimension or a choice property.
	 * 
	 * <p>
	 * In this case, input file has the following in valid restriction.
	 * <ul>
	 * <li>Property: Style.font-family is a string property.</li>
	 * </ul>
	 */

	public void test_RESTRICTION_NOT_ALLOWED() {
		// this.logger = createAndRegisterLogger( "metaLog38.log" );
		// //$NON-NLS-1$
		loadMeta("romTest38.def"); //$NON-NLS-1$

		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_RESTRICTION_NOT_ALLOWED, 0, METAREADER_EXCEPTION);
	}

	/**
	 * <Allowed>in,cm </Allowed> indicates the restriction information, it can only
	 * apply to a dimension or a choice property.
	 * <p>
	 * And the information in it should be in the original unit or choice set.
	 * <p>
	 * In this case, input file has the following in valid restriction.
	 * <ul>
	 * <li>Property: Style.font-size is a dimension property. allowed =
	 * "in,cm,XXXX", "XXXX" is not valid</li>
	 * <li>Property: Style.font-style is a choice property. allowed = "italic,YYYY",
	 * "YYYY" is not valid</li> TODO: Restriction on Member.
	 * 
	 * </ul>
	 */

	public void test_INVALID_RESTRICTION() {
		// this.logger = createAndRegisterLogger( "metaLog39.log" );
		// //$NON-NLS-1$
		loadMeta("romTest39.def"); //$NON-NLS-1$

		assertEquals(2, errorList.size());
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_RESTRICTION, 0, METAREADER_EXCEPTION);
		assertErrorCode(MetaDataParserException.DESIGN_EXCEPTION_INVALID_RESTRICTION, 1, METAREADER_EXCEPTION);
	}

	/**
	 * Test that default value not in the allowed choices.
	 * <p>
	 * In the case 1: ReportDesign.units has an allowed choices[in,cm,mm,pt]. But
	 * the default value for it is px, not in the list.
	 */

	public void test_INVALID_DEFAULT_VALUE() {
		// this.logger = createAndRegisterLogger( "metaLog40.log" );
		// //$NON-NLS-1$
		loadMeta("romTest40.def"); //$NON-NLS-1$

		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE, 0, METADATA_EXCEPTION);
	}

	/**
	 * Test that default value not in the allowed choices.
	 * <p>
	 * In the case Style.font-size is a dimension type, allowed [in,cm], but default
	 * value is 12pt, where pt is not allowed
	 */

	public void test_INVALID_DEFAULT_VALUE2() {
		// this.logger = createAndRegisterLogger( "metaLog41.log" );
		// //$NON-NLS-1$
		loadMeta("romTest41.def"); //$NON-NLS-1$

		assertEquals(1, errorList.size());
		assertErrorCode(MetaDataException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE, 0, METADATA_EXCEPTION);
	}

	// Followings are cases from logging MetaDataException.

	/**
	 * Assert one error code.
	 * 
	 * @param expected expected error code string.
	 * @param index    error index in the error list.
	 * @param type     0 for MetaReaderException; 1 for MetaDataException.
	 */

	private void assertErrorCode(String expected, int index, int type) {
		assert type == METAREADER_EXCEPTION || type == METADATA_EXCEPTION;

		assertErrorCode(expected, index, index, type);
	}

	/**
	 * Assert sevaral errors in the error list. Assert there error code.
	 * 
	 * @param expected expected error code string
	 * @param from     from index in the error list.
	 * @param to       to index in the error list.
	 * @param type     type 0 for MetaReaderException; 1 for MetaDataException.
	 */

	private void assertErrorCode(String expected, int from, int to, int type) {
		assert errorList.size() > to;
		assert from <= to;
		assert from >= 0;

		for (int i = from; i < to; i++) {
			if (type == 0) {
				assertEquals(expected, ((MetaDataParserException) errorList.get(i)).getErrorCode());
			} else if (type == 1) {
				assertEquals(expected, ((MetaDataException) ((MetaDataParserException) errorList.get(i)).getException())
						.getErrorCode());

			}
		}
	}

}
