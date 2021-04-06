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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortHintHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests oda data set case. <table border="1" cellpadding="0" cellspacing="0" *
 * * * * * * * * * * * * * * style="border-collapse: collapse"
 * bordercolor="#111111" width="100%" * * * * * * * * * * * * * *
 * id="AutoNumber6">
 * 
 * <tr>
 * <td width="33%"><b>Method </b></td>
 * <td width="33%"><b>Test Case </b></td>
 * <td width="34%"><b>Expected Result </b></td>
 * </tr>
 * 
 * <tr>
 * <td width="33%">{@link #testParserDesignFile()}</td>
 * <td width="33%">Test all properties</td>
 * <td width="34%">the correct value returned.</td>
 * </tr>
 * 
 * <tr>
 * <td width="33%">{@link #testWriterDesignFile()}</td>
 * <td width="33%">Set new value to properties and save it.</td>
 * <td width="34%">new value should be save into the output file, and output
 * file is same as golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testQueryConflictCheck()}</td>
 * <td>The query conflict is forbidden.</td>
 * <td>Property value error found</td>
 * </tr>
 * </table>
 * 
 * @see org.eclipse.birt.report.model.elements.OdaDataSource
 * @see org.eclipse.birt.report.model.elements.OdaDataSet
 */

public class OdaDataSetParseTest extends BaseTestCase {

	static final String fileName = "OdaDataSetParseTest.xml"; //$NON-NLS-1$
	static final String goldenFileName = "OdaDataSetParseTest_golden.xml"; //$NON-NLS-1$

	static final String queryTextInputFileName = "OdaDataSetParseTest_1.xml";//$NON-NLS-1$
	static final String queryTextGoldenFileName = "OdaDataSetQueryTextParseTest_golden.xml";//$NON-NLS-1$

	static final String obsoleteFileName = "OdaDataSetParseTest_obsolete.xml"; //$NON-NLS-1$
	static final String obsoleteGoldenFileName = "OdaDataSetParseTest_obsolete_golden.xml";//$NON-NLS-1$

	static final String queryTextInputFileName2 = "OdaDataSetParseTest_3.xml";//$NON-NLS-1$
	static final String queryTextGoldenFileName2 = "OdaDataSetParseTest_golden_3.xml";//$NON-NLS-1$

	static final String extendedPropertyInput = "OdaDataSetParseTest_4.xml";//$NON-NLS-1$

	static final String anyDataTypeInput = "OdaDataSetParseTest_5.xml";//$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 * 
	 * @throws Exception
	 */

	public void testParserDesignFile() throws Exception {
		openDesign(fileName);

		parse();
		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("MyDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);

		// test resultsethints and parameters definition
		ElementPropertyDefn propDefn = (ElementPropertyDefn) dataSet
				.getPropertyDefn(OdaDataSetHandle.RESULT_SET_HINTS_PROP);
		assertEquals(OdaResultSetColumn.STRUCTURE_NAME, propDefn.getStructDefn().getName());
		propDefn = (ElementPropertyDefn) dataSet.getPropertyDefn(OdaDataSetHandle.PARAMETERS_PROP);
		assertEquals(OdaDataSetParameter.STRUCT_NAME, propDefn.getStructDefn().getName());

		assertEquals("1.1", dataSet.getDesigerStateVersion()); //$NON-NLS-1$
		assertEquals("content as string", dataSet //$NON-NLS-1$
				.getDesigerStateContentAsString());
		assertEquals("content as blob", new String(dataSet //$NON-NLS-1$
				.getDesigerStateContentAsBlob(), OdaDesignerState.CHARSET));

		// test ACL properties
		ExpressionHandle expressionHandle = dataSet.getACLExpression();
		Expression value = (Expression) expressionHandle.getValue();
		assertEquals("ACL expression", value.getStringExpression()); //$NON-NLS-1$
		assertEquals(IExpressionType.JAVASCRIPT, value.getType());
		expressionHandle = dataSet.getRowACLExpression();
		value = (Expression) expressionHandle.getValue();
		assertEquals("row ACL expression", value.getStringExpression()); //$NON-NLS-1$
		assertEquals(IExpressionType.JAVASCRIPT, value.getType());

	}

	/**
	 * This test sets properties, writes the design file and compares it with golden
	 * file.
	 * 
	 * @throws Exception
	 */

	public void testWriterDesignFile() throws Exception {
		openDesign(fileName);
		write();

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("MyDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);

		dataSet.setDesigerStateVersion("2.1"); //$NON-NLS-1$
		dataSet.setDesigerStateContentAsString("new content as string"); //$NON-NLS-1$

		dataSet.setIsVisible(true);
		dataSet.setLocale(ULocale.FRANCE);
		dataSet.setNullsOrdering(DesignChoiceConstants.NULLS_ORDERING_EXCLUDE_NULLS);

		String strBlob = "new content as blob"; //$NON-NLS-1$

		dataSet.setDesigerStateContentAsBlob(strBlob.getBytes(OdaDesignerState.CHARSET));

		dataSet.setDataSetRowLimit(888);
		assertEquals(888, dataSet.getDataSetRowLimit());

		UserPropertyDefn prop = new UserPropertyDefn();
		prop.setName("hello"); //$NON-NLS-1$
		PropertyType typeDefn = MetaDataDictionary.getInstance().getPropertyType(PropertyType.STRING_TYPE_NAME);
		prop.setType(typeDefn);
		dataSet.addUserPropertyDefn(prop);

		// change sortHints on data set.
		Iterator sortHints = dataSet.sortHintsIterator();
		SortHintHandle sortHint = (SortHintHandle) sortHints.next();

		sortHint.setColumnName("newSortHint"); //$NON-NLS-1$
		sortHint.setPosition(1);
		sortHint.setDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
		sortHint.setNullValueOrdering(DesignChoiceConstants.NULL_VALUE_ORDERING_TYPE_NULLISLAST);
		sortHint.setOptional(false);

		// ACL properties
		ExpressionHandle expressionHandle = dataSet.getACLExpression();
		expressionHandle.setValue(new Expression("new " + expressionHandle.getStringValue(), IExpressionType.CONSTANT)); //$NON-NLS-1$
		expressionHandle = dataSet.getRowACLExpression();
		expressionHandle.setValue(new Expression("new " + expressionHandle.getStringValue(), IExpressionType.CONSTANT)); //$NON-NLS-1$

		// test writint data set parameters new properties
		PropertyHandle paramters = dataSet.getPropertyHandle(OdaDataSetHandle.PARAMETERS_PROP);

		DataSetParameterHandle parameter = (DataSetParameterHandle) paramters.getAt(0);
		parameter.setDisplayName("New Name"); //$NON-NLS-1$
		parameter.setDisplayNameKey("newNameKey"); //$NON-NLS-1$

		parameter = (DataSetParameterHandle) paramters.getAt(1);
		parameter.setHeading("New Date Heading"); //$NON-NLS-1$
		parameter.setHeadingKey("newDataKey"); //$NON-NLS-1$

		parameter = (DataSetParameterHandle) paramters.getAt(2);
		parameter.setHelpText("New ID Number"); //$NON-NLS-1$
		parameter.setHelpTextKey("newIdKey"); //$NON-NLS-1$

		parameter = (DataSetParameterHandle) paramters.getAt(3);
		parameter.setDescription("New Name Description"); //$NON-NLS-1$
		parameter.setDescription("newNameDescriptionKey"); //$NON-NLS-1$

		// test writing new column hints properties

		PropertyHandle columnHints = dataSet.getPropertyHandle(SimpleDataSet.COLUMN_HINTS_PROP);
		ColumnHintHandle columnHintHandle = (ColumnHintHandle) columnHints.getAt(0);
		columnHintHandle.setHeading("newHeading"); //$NON-NLS-1$
		columnHintHandle.setHeadingKey("newHeadingID"); //$NON-NLS-1$
		columnHintHandle.setDisplayLength(10); // $NON-NLS-1$
		columnHintHandle.setHorizontalAlign(DesignChoiceConstants.TEXT_ALIGN_JUSTIFY);
		assertTrue(columnHintHandle.isLocal(ColumnHint.WORD_WRAP_MEMBER));
		columnHintHandle.setWordWrap(false);
		columnHintHandle.setTextFormat(DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE);
		columnHintHandle.setDescription("New Description"); //$NON-NLS-1$
		columnHintHandle.setDescriptionKey("newDescriptionKey"); //$NON-NLS-1$

		columnHintHandle.setAnalysisColumn("new analysis column"); //$NON-NLS-1$
		columnHintHandle.setIndexColumn(false);
		columnHintHandle.setCompresssed(false);

		NumberFormatValue numberFormat = new NumberFormatValue();
		numberFormat.setCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		numberFormat.setPattern("test number pattern"); //$NON-NLS-1$
		columnHintHandle.setValueFormat(numberFormat);

		ComputedColumnHandle computedColumn = (ComputedColumnHandle) dataSet.computedColumnsIterator().next();
		computedColumn.setAllowExport(true);

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 * 
	 * @throws Exception
	 */

	public void testReadWriteObsoleteDesignFile() throws Exception {
		openDesign(obsoleteFileName);
		write();

		save();
		assertTrue(compareFile(obsoleteGoldenFileName));
	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 * 
	 * @throws Exception
	 */

	private void parse() throws Exception {
		openDesign(fileName);

		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle.findDataSource("extendedDataSource"); //$NON-NLS-1$
		assertNotNull(dataSource);

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("MyDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);
		assertEquals("select * from user", dataSet.getQueryText()); //$NON-NLS-1$
		assertEquals("userid", dataSet.getResultSetName()); //$NON-NLS-1$
		assertEquals(2, dataSet.getResultSetNumber());

		assertEquals(dataSource, dataSet.getDataSource());

		assertFalse(dataSet.isVisible());
		assertEquals(ULocale.US, dataSet.getLocale());
		assertEquals(DesignChoiceConstants.NULLS_ORDERING_NULLS_HIGHEST, dataSet.getNullsOrdering());

		// Test "input-parameters" on DataSet

		List parameters = (List) dataSet.getProperty(OdaDataSet.PARAMETERS_PROP);
		assertEquals(5, parameters.size());

		int i = 0;
		OdaDataSetParameter parameter = (OdaDataSetParameter) parameters.get(i++);
		assertEquals(1, parameter.getPosition().intValue());
		assertEquals("name", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, parameter.getDataType());
		assertTrue(parameter.isOptional());
		assertEquals("default value 1", parameter.getDefaultValue()); //$NON-NLS-1$
		assertEquals(true, parameter.allowNull());
		assertEquals(-100, parameter.getNativeDataType().intValue());
		assertEquals("Name", parameter.getDisplayName()); //$NON-NLS-1$
		assertEquals("nameID", parameter.getDisplayNameKey()); //$NON-NLS-1$

		parameter = (OdaDataSetParameter) parameters.get(i++);
		assertEquals(2, parameter.getPosition().intValue());
		assertEquals("date", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME, parameter.getDataType());
		assertFalse(parameter.isOptional());
		assertEquals("Date", parameter.getHeading()); //$NON-NLS-1$
		assertEquals("dateID", parameter.getHeadingKey()); //$NON-NLS-1$

		parameter = (OdaDataSetParameter) parameters.get(i++);
		assertEquals(3, parameter.getPosition().intValue());
		assertEquals("id", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, parameter.getDataType());
		assertTrue(parameter.isOptional());

		// Test "output-parameters" on DataSet

		parameter = (OdaDataSetParameter) parameters.get(i++);
		assertEquals("birth", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME, parameter.getDataType());
		assertEquals("Birthday", parameter.getHelpText()); //$NON-NLS-1$
		assertEquals("birthID", parameter.getHelpTextKey()); //$NON-NLS-1$

		parameter = (OdaDataSetParameter) parameters.get(i++);
		assertEquals("title", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, parameter.getDataType());
		assertEquals("Title", parameter.getDescription()); //$NON-NLS-1$
		assertEquals("titleID", parameter.getDescriptionKey()); //$NON-NLS-1$

		// Test "parameter-bindings" on DataSet

		List bindings = (List) dataSet.getProperty(SimpleDataSet.PARAM_BINDINGS_PROP);
		assertEquals(2, bindings.size());

		i = 0;
		ParamBinding binding = (ParamBinding) bindings.get(i++);
		assertEquals("param1", binding.getParamName()); //$NON-NLS-1$
		assertEquals("value1", binding.getExpression()); //$NON-NLS-1$

		binding = (ParamBinding) bindings.get(i++);
		assertEquals("param2", binding.getParamName()); //$NON-NLS-1$
		assertEquals("value2", binding.getExpression()); //$NON-NLS-1$

		// Test "computed-columns" on DataSet

		List columns = (List) dataSet.getProperty(SimpleDataSet.COMPUTED_COLUMNS_PROP);
		assertEquals(3, columns.size());

		i = 0;
		ComputedColumn computedColumn = (ComputedColumn) columns.get(i++);
		assertEquals("column1", computedColumn.getName()); //$NON-NLS-1$
		assertEquals("expression1", computedColumn.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, computedColumn.getDataType());

		computedColumn = (ComputedColumn) columns.get(i++);
		assertEquals("column2", computedColumn.getName()); //$NON-NLS-1$
		assertEquals("expression2", computedColumn.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME, computedColumn.getDataType());

		computedColumn = (ComputedColumn) columns.get(i++);
		assertEquals("column3", computedColumn.getName()); //$NON-NLS-1$
		assertEquals("expression3", computedColumn.getExpression()); //$NON-NLS-1$

		// tests sortHints on data set.
		Iterator sortHints = dataSet.sortHintsIterator();
		SortHintHandle sortHint = (SortHintHandle) sortHints.next();
		assertEquals("sortHint", sortHint.getColumnName()); //$NON-NLS-1$
		assertEquals(3, sortHint.getPosition());
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_DESC, sortHint.getDirection());
		assertEquals(DesignChoiceConstants.NULL_VALUE_ORDERING_TYPE_NULLISFIRST, sortHint.getNullValueOrdering());
		assertTrue(sortHint.isOptional());

		// Test "column-hints" on DataSet

		List columnHints = (List) dataSet.getProperty(SimpleDataSet.COLUMN_HINTS_PROP);
		assertEquals(1, columnHints.size());

		i = 0;
		ColumnHint columnHint = (ColumnHint) columnHints.get(i++);
		PropertyDefn member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.COLUMN_NAME_MEMBER);
		assertEquals("username", columnHint.getProperty(design, member)); //$NON-NLS-1$
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.ALIAS_MEMBER);
		assertEquals("userid", columnHint.getProperty(design, member)); //$NON-NLS-1$
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.SEARCHING_MEMBER);
		assertEquals(DesignChoiceConstants.SEARCH_TYPE_NONE, columnHint.getProperty(design, member));
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.EXPORT_MEMBER);
		assertEquals(DesignChoiceConstants.EXPORT_TYPE_ALWAYS, columnHint.getProperty(design, member));
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.ANALYSIS_MEMBER);
		assertEquals(DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION, columnHint.getProperty(design, member));
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.PARENT_LEVEL_MEMBER);
		assertEquals("4", columnHint.getProperty(design, member)); //$NON-NLS-1$
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.FORMAT_MEMBER);
		assertEquals("##.###", columnHint.getProperty(design, member)); //$NON-NLS-1$
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.DISPLAY_NAME_ID_MEMBER);
		assertEquals("message.column-hint.username", columnHint.getProperty(design, member)); //$NON-NLS-1$
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.DISPLAY_NAME_MEMBER);
		assertEquals("User Name", columnHint.getProperty(design, member)); //$NON-NLS-1$
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.HELP_TEXT_ID_MEMBER);
		assertEquals("message.column-hint.help", columnHint.getProperty(design, member)); //$NON-NLS-1$
		member = (PropertyDefn) columnHint.getDefn().getMember(ColumnHint.HELP_TEXT_MEMBER);
		assertEquals("Help me!", columnHint.getProperty(design, member)); //$NON-NLS-1$

		// new properties
		assertEquals("Heading", columnHint.getProperty(design, //$NON-NLS-1$
				ColumnHint.HEADING_MEMBER));
		assertEquals("HeadingID", columnHint.getProperty(design, //$NON-NLS-1$
				ColumnHint.HEADING_ID_MEMBER));
		assertEquals(5, columnHint.getProperty(design, ColumnHint.DISPLAY_LENGTH_MEMBER));
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_CENTER,
				columnHint.getProperty(design, ColumnHint.HORIZONTAL_ALIGN_MEMBER));
		assertEquals(true, columnHint.getProperty(design, ColumnHint.WORD_WRAP_MEMBER));
		assertEquals(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE,
				columnHint.getProperty(design, ColumnHint.TEXT_FORMAT_MEMBER));
		assertEquals("Description", columnHint.getProperty(design, //$NON-NLS-1$
				ColumnHint.DESCRIPTION_MEMBER));
		assertEquals("descriptionKey", columnHint.getProperty(design, //$NON-NLS-1$
				ColumnHint.DESCRIPTION_ID_MEMBER));

		// format
		ColumnHintHandle columnHintHandle = (ColumnHintHandle) dataSet.columnHintsIterator().next();
		assertNotNull(columnHintHandle);
		assertEquals(DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED,
				columnHintHandle.getValueFormat().getCategory());
		assertEquals("string pattern", columnHintHandle.getValueFormat().getPattern());

		assertTrue(columnHintHandle.isIndexColumn());
		assertTrue(columnHintHandle.isCompressed());
		// Test "filter" on DataSet

		ArrayList filters = (ArrayList) dataSet.getProperty(SimpleDataSet.FILTER_PROP);

		assertEquals(1, filters.size());
		assertEquals("lt", ((FilterCondition) filters.get(0)).getOperator()); //$NON-NLS-1$
		assertEquals("filter expression", ((FilterCondition) filters.get(0)).getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", ((FilterCondition) filters.get(0)).getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", ((FilterCondition) filters.get(0)).getValue2()); //$NON-NLS-1$

		dataSet = (OdaDataSetHandle) designHandle.findDataSet("SecondDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);
		assertEquals(null, dataSet.getQueryScript());
		assertEquals(1, dataSet.getResultSetNumber());

	}

	/**
	 * This test sets properties, writes the design file and compares it with golden
	 * file.
	 * 
	 * @throws Exception
	 */

	private void write() throws Exception {
		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle.findDataSource("extendedDataSource"); //$NON-NLS-1$
		assertNotNull(dataSource);

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("MyDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);
		dataSet.setQueryText("new text"); //$NON-NLS-1$
		dataSet.setResultSetName("new result set"); //$NON-NLS-1$
		dataSet.setResultSetNumber(3);

		dataSet.setCachedRowCount(20);
		assertEquals(20, dataSet.getCachedRowCount());

		// Change "input-parameters" on DataSet

		List parameters = (List) dataSet.getProperty(OdaDataSet.PARAMETERS_PROP);
		assertEquals(5, parameters.size());

		PropertyDefn posDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.PARAMETERS_PROP)
				.getStructDefn().getMember(DataSetParameter.POSITION_MEMBER);
		PropertyDefn nameDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.PARAMETERS_PROP)
				.getStructDefn().getMember(DataSetParameter.NAME_MEMBER);
		PropertyDefn dataTypeDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.PARAMETERS_PROP)
				.getStructDefn().getMember(DataSetParameter.DATA_TYPE_MEMBER);

		int i = 0;
		OdaDataSetParameter parameter = (OdaDataSetParameter) parameters.get(i++);
		parameter.setProperty(posDefn, Integer.valueOf("91")); //$NON-NLS-1$
		parameter.setProperty(nameDefn, "new name"); //$NON-NLS-1$
		parameter.setProperty(dataTypeDefn, DesignChoiceConstants.PARAM_TYPE_FLOAT);
		parameter.setIsOptional(false);
		parameter.setDefaultValue("new default value 1"); //$NON-NLS-1$
		parameter.setAllowNull(true);
		parameter.setNativeDataType(Integer.valueOf("22")); //$NON-NLS-1$

		parameter = (OdaDataSetParameter) parameters.get(i++);
		parameter.setProperty(posDefn, Integer.valueOf("92")); //$NON-NLS-1$
		parameter.setProperty(nameDefn, "new date"); //$NON-NLS-1$
		parameter.setIsOptional(false);
		parameter.setAllowNull(true);

		parameter = (OdaDataSetParameter) parameters.get(i++);
		parameter.setProperty(posDefn, Integer.valueOf("93")); //$NON-NLS-1$
		parameter.setProperty(nameDefn, "new id"); //$NON-NLS-1$
		parameter.setProperty(dataTypeDefn, DesignChoiceConstants.PARAM_TYPE_DECIMAL);
		parameter.setIsOptional(false);

		// Change "output-parameters" on DataSet

		parameter = (OdaDataSetParameter) parameters.get(i++);
		parameter.setProperty(nameDefn, "new name"); //$NON-NLS-1$
		parameter.setProperty(dataTypeDefn, DesignChoiceConstants.PARAM_TYPE_FLOAT);

		parameter = (OdaDataSetParameter) parameters.get(i++);
		parameter.setProperty(nameDefn, "new date"); //$NON-NLS-1$

		parameter = new OdaDataSetParameter();
		parameter.setProperty(nameDefn, "new id"); //$NON-NLS-1$
		parameter.setProperty(dataTypeDefn, DesignChoiceConstants.PARAM_TYPE_DECIMAL);
		parameter.setIsOutput(true);
		dataSet.getPropertyHandle(OdaDataSet.PARAMETERS_PROP).addItem(parameter);

		// Change "param-binding" on DataSet

		nameDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.PARAM_BINDINGS_PROP)
				.getStructDefn().getMember(ParamBinding.PARAM_NAME_MEMBER);
		PropertyDefn valueDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.PARAM_BINDINGS_PROP)
				.getStructDefn().getMember(ParamBinding.EXPRESSION_MEMBER);

		List bindings = (List) dataSet.getProperty(SimpleDataSet.PARAM_BINDINGS_PROP);
		assertEquals(2, bindings.size());

		i = 0;
		ParamBinding binding = (ParamBinding) bindings.get(i++);
		binding.setProperty(nameDefn, "new param1"); //$NON-NLS-1$
		List values = new ArrayList();
		values.add("new value1"); //$NON-NLS-1$
		binding.setProperty(valueDefn, values);

		binding = (ParamBinding) bindings.get(i++);
		binding.setProperty(nameDefn, "new param2"); //$NON-NLS-1$
		values = new ArrayList();
		values.add("new value2"); //$NON-NLS-1$
		binding.setProperty(valueDefn, values);

		// Change "result-set" on DataSet

		posDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.RESULT_SET_PROP).getStructDefn()
				.getMember(ResultSetColumn.POSITION_MEMBER);
		nameDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.RESULT_SET_PROP).getStructDefn()
				.getMember(ResultSetColumn.NAME_MEMBER);
		dataTypeDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.RESULT_SET_PROP)
				.getStructDefn().getMember(ResultSetColumn.DATA_TYPE_MEMBER);

		// Change "computed-columns" in DataSet

		nameDefn = (PropertyDefn) dataSet.getElement().getPropertyDefn(SimpleDataSet.COMPUTED_COLUMNS_PROP)
				.getStructDefn().getMember(ComputedColumn.NAME_MEMBER);
		PropertyDefn expressionDefn = (PropertyDefn) dataSet.getElement()
				.getPropertyDefn(SimpleDataSet.COMPUTED_COLUMNS_PROP).getStructDefn()
				.getMember(ComputedColumn.EXPRESSION_MEMBER);

		List columns = (List) dataSet.getElement().getProperty(design, SimpleDataSet.COMPUTED_COLUMNS_PROP);
		assertEquals(3, columns.size());

		i = 0;
		ComputedColumn computedColumn = (ComputedColumn) columns.get(i++);
		computedColumn.setProperty(nameDefn, "new column1"); //$NON-NLS-1$
		computedColumn.setProperty(expressionDefn, "new expression 1"); //$NON-NLS-1$
		computedColumn.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL);

		computedColumn = (ComputedColumn) columns.get(i++);
		computedColumn.setProperty(nameDefn, "new column2"); //$NON-NLS-1$
		computedColumn.setProperty(expressionDefn, "new expression 2"); //$NON-NLS-1$
		computedColumn.setDataType(null);

		computedColumn = (ComputedColumn) columns.get(i++);
		computedColumn.setProperty(nameDefn, "new column3"); //$NON-NLS-1$
		computedColumn.setProperty(expressionDefn, "new expression 3"); //$NON-NLS-1$

		// Change "column-hints" on DataSet

		List columnHints = (List) dataSet.getProperty(SimpleDataSet.COLUMN_HINTS_PROP);
		assertEquals(1, columnHints.size());

		i = 0;
		ColumnHint columnHint = (ColumnHint) columnHints.get(i++);
		StructureDefn structureDefn = (StructureDefn) dataSet.getElement()
				.getPropertyDefn(SimpleDataSet.COLUMN_HINTS_PROP).getStructDefn();
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.COLUMN_NAME_MEMBER), "new username"); //$NON-NLS-1$
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.ALIAS_MEMBER), "new userid"); //$NON-NLS-1$
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.SEARCHING_MEMBER),
				DesignChoiceConstants.SEARCH_TYPE_INDEXED);
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.EXPORT_MEMBER),
				DesignChoiceConstants.EXPORT_TYPE_IF_REALIZED);
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.ANALYSIS_MEMBER),
				DesignChoiceConstants.ANALYSIS_TYPE_DETAIL);
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.PARENT_LEVEL_MEMBER), "new level"); //$NON-NLS-1$
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.FORMAT_MEMBER), "new format"); //$NON-NLS-1$
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.DISPLAY_NAME_ID_MEMBER),
				"new display name id"); //$NON-NLS-1$
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.DISPLAY_NAME_MEMBER),
				"new display name"); //$NON-NLS-1$
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.HELP_TEXT_ID_MEMBER),
				"new help text id"); //$NON-NLS-1$
		columnHint.setProperty((PropertyDefn) structureDefn.getMember(ColumnHint.HELP_TEXT_MEMBER), "new help text"); //$NON-NLS-1$
	}

	/**
	 * Tests to write empty string of queryText to the design file.
	 * 
	 * @throws Exception
	 */

	public void testWriteEmptyQueryText() throws Exception {
		openDesign(queryTextInputFileName);

		// Test if it can parser and write querytext when the type of it is
		// changed to literalstringproperty type

		OdaDataSetHandle odaHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet2"); //$NON-NLS-1$
		assertEquals("", odaHandle.getQueryText()); //$NON-NLS-1$

		odaHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet3"); //$NON-NLS-1$
		assertEquals("  ", odaHandle.getQueryText()); //$NON-NLS-1$

		save();
		assertTrue(compareFile(queryTextGoldenFileName));
	}

	/**
	 * Tests all properties on an data set. JdbcSelectDataSet is used.
	 * 
	 * @throws Exception
	 */

	public void testProperties() throws Exception {
		OdaDataSetHandle dataSet = getDataSet();

		OdaDataSetHandle emptyDsHandle = (OdaDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$

		// Test dataSetRowLimit property

		assertEquals(0, emptyDsHandle.getDataSetRowLimit());
		emptyDsHandle.setDataSetRowLimit(100);
		assertEquals(100, emptyDsHandle.getDataSetRowLimit());

		emptyDsHandle.setQueryText(" "); //$NON-NLS-1$
		assertEquals(" ", emptyDsHandle.getQueryText()); //$NON-NLS-1$

		emptyDsHandle.setQueryText(""); //$NON-NLS-1$
		assertEquals("", emptyDsHandle.getQueryText()); //$NON-NLS-1$

		// Test DataSet property

		assertEquals("myDataSource", dataSet.getDataSource().getName()); //$NON-NLS-1$
		assertEquals("myDataSource", dataSet.getDataSourceName()); //$NON-NLS-1$
		assertEquals("script_beforeopen", dataSet.getBeforeOpen()); //$NON-NLS-1$
		assertEquals("script_beforeclose", dataSet.getBeforeClose()); //$NON-NLS-1$
		assertEquals("script_onfetch", dataSet.getOnFetch()); //$NON-NLS-1$
		assertEquals("script_afteropen", dataSet.getAfterOpen()); //$NON-NLS-1$
		assertEquals("script_afterclose", dataSet.getAfterClose()); //$NON-NLS-1$

		// Test "input-parameters" on DataSet

		Iterator parameters = dataSet.parametersIterator();

		OdaDataSetParameterHandle parameter = (OdaDataSetParameterHandle) parameters.next();

		assertEquals(1, parameter.getPosition().intValue());
		assertEquals("name", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, parameter.getDataType());
		assertTrue(parameter.isOptional());
		assertEquals("default value 1", parameter.getDefaultValue()); //$NON-NLS-1$
		assertTrue(parameter.isInput());
		assertFalse(parameter.isOutput());

		parameter.setDefaultValue("new default value 1"); //$NON-NLS-1$
		assertEquals("new default value 1", parameter.getDefaultValue()); //$NON-NLS-1$

		// 3 parameters totally.

		assertTrue(parameters.hasNext());
		parameters.next();
		assertTrue(parameters.hasNext());
		parameters.next();
		assertTrue(parameters.hasNext());

		// Test "output-parameters" on DataSet

		parameter = (OdaDataSetParameterHandle) parameters.next();
		assertEquals("birth", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME, parameter.getDataType());
		assertTrue(parameter.isOutput());

		parameter = (OdaDataSetParameterHandle) parameters.next();
		assertEquals("title", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, parameter.getDataType());

		parameter = (OdaDataSetParameterHandle) parameters.next();
		assertEquals("startdate", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATE, parameter.getDataType());

		parameter = (OdaDataSetParameterHandle) parameters.next();
		assertEquals("enddate", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_TIME, parameter.getDataType());

		assertFalse(parameters.hasNext());

		// Test "param-bindings" on DataSet

		Iterator bindings = dataSet.paramBindingsIterator();

		ParamBindingHandle binding = (ParamBindingHandle) bindings.next();
		assertEquals("param1", binding.getParamName()); //$NON-NLS-1$
		assertEquals("value1", binding.getExpression()); //$NON-NLS-1$

		assertNotNull(bindings.next());
		assertNull(bindings.next());

		// Test "result-set" on DataSet

		List resultSetProps = dataSet.getListProperty(IDataSetModel.RESULT_SET_PROP);
		assertNotNull(resultSetProps);
		assertEquals(3, resultSetProps.size());

		// Test "computed-columns" on DataSet

		Iterator columns = dataSet.computedColumnsIterator();

		ComputedColumnHandle computedColumn = (ComputedColumnHandle) columns.next();
		assertEquals("column1", computedColumn.getName()); //$NON-NLS-1$
		assertEquals("expression1", computedColumn.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, computedColumn.getDataType());
		computedColumn = (ComputedColumnHandle) columns.next();
		assertEquals("column2", computedColumn.getName()); //$NON-NLS-1$
		assertEquals("expression2", computedColumn.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME, computedColumn.getDataType());
		computedColumn = (ComputedColumnHandle) columns.next();
		assertEquals("column3", computedColumn.getName()); //$NON-NLS-1$
		assertEquals("expression3", computedColumn.getExpression()); //$NON-NLS-1$
		assertFalse(computedColumn.allowExport());

		// Test "column-hints" on DataSet

		Iterator columnHints = dataSet.columnHintsIterator();

		ColumnHintHandle columnHint = (ColumnHintHandle) columnHints.next();

		assertEquals("username", columnHint.getColumnName()); //$NON-NLS-1$
		assertEquals("userid", columnHint.getAlias()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.SEARCH_TYPE_NONE, columnHint.getSearching());
		assertEquals(DesignChoiceConstants.EXPORT_TYPE_ALWAYS, columnHint.getExport());
		assertEquals(DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION, columnHint.getAnalysis());
		assertEquals("4", columnHint.getParentLevel()); //$NON-NLS-1$
		assertEquals("##.###", columnHint.getFormat()); //$NON-NLS-1$
		assertEquals("message.column-hint.username", columnHint //$NON-NLS-1$
				.getDisplayNameKey());
		assertEquals("User Name", columnHint.getDisplayName()); //$NON-NLS-1$
		assertEquals("message.column-hint.help", columnHint //$NON-NLS-1$
				.getHelpTextKey());
		assertEquals("Help me!", columnHint.getHelpText()); //$NON-NLS-1$
		assertTrue(columnHint.isOnColumnLayout());

		assertEquals("Test Column", columnHint.getAnalysisColumn()); //$NON-NLS-1$
		assertFalse(columnHint.isLocal(ColumnHint.WORD_WRAP_MEMBER));

		// Test "filter" on DataSet

		Iterator filters = dataSet.filtersIterator();

		FilterConditionHandle filterHandle = (FilterConditionHandle) filters.next();

		assertEquals("lt", filterHandle.getOperator()); //$NON-NLS-1$
		assertEquals("filter expression", filterHandle.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", filterHandle.getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", filterHandle.getValue2()); //$NON-NLS-1$

		assertNull(filters.next());

		// cases for add/remove filters on data set

		FilterCondition fc = StructureFactory.createFilterCond();
		fc.setExpr("new filter expr"); //$NON-NLS-1$
		fc.setOperator(DesignChoiceConstants.FILTER_OPERATOR_FALSE);
		dataSet.addFilter(fc);

		assertEquals(2, dataSet.getListProperty(OdaDataSetHandle.FILTER_PROP).size());

		dataSet.removeFilter(fc);
		assertEquals(1, dataSet.getListProperty(OdaDataSetHandle.FILTER_PROP).size());

		// writes properties to get a new file.

		OdaDataSource dataSource2 = (OdaDataSource) design.findDataSource("myDataSource2"); //$NON-NLS-1$

		// Change DataSet property

		dataSet.setDataSource(dataSource2.getName());
		dataSet.setBeforeOpen("New before open"); //$NON-NLS-1$
		dataSet.setBeforeClose("New before close"); //$NON-NLS-1$
		dataSet.setAfterOpen("New after open"); //$NON-NLS-1$
		dataSet.setAfterClose("New after open"); //$NON-NLS-1$
		dataSet.setOnFetch("New on fetch"); //$NON-NLS-1$

		// Change "input-parameters" on DataSet

		parameters = dataSet.parametersIterator();
		parameter = (OdaDataSetParameterHandle) parameters.next();

		parameter = (OdaDataSetParameterHandle) parameters.next();
		parameter.setPosition(Integer.valueOf("91")); //$NON-NLS-1$
		parameter.setName("new name"); //$NON-NLS-1$
		parameter.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT);
		parameter.setIsOptional(false);

		// Change "output-parameters" on DataSet

		parameter = (OdaDataSetParameterHandle) parameters.next();
		parameter.setName("new name"); //$NON-NLS-1$
		parameter.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT);
		parameter.setPosition(Integer.valueOf("91")); //$NON-NLS-1$

		// Change "param-binding" on DataSet

		bindings = dataSet.paramBindingsIterator();

		binding = (ParamBindingHandle) bindings.next();
		binding.setParamName("new param1"); //$NON-NLS-1$
		binding.setExpression("new value1"); //$NON-NLS-1$

		binding = (ParamBindingHandle) bindings.next();

		binding.setParamName("new param2"); //$NON-NLS-1$
		binding.setParamName("new value2"); //$NON-NLS-1$

		// Change "computed-columns" in DataSet

		columns = dataSet.computedColumnsIterator();
		computedColumn = (ComputedColumnHandle) columns.next();

		computedColumn.setName("new column1"); //$NON-NLS-1$
		computedColumn.setExpression("new expression 1"); //$NON-NLS-1$
		computedColumn.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL);

		computedColumn = (ComputedColumnHandle) columns.next();
		computedColumn.setName("new column2"); //$NON-NLS-1$
		computedColumn.setExpression("new expression 2"); //$NON-NLS-1$

		// Change "column-hints" on DataSet

		columnHints = dataSet.columnHintsIterator();

		columnHint = (ColumnHintHandle) columnHints.next();

		columnHint.setColumnName("new username"); //$NON-NLS-1$
		columnHint.setAlias("new userid"); //$NON-NLS-1$
		columnHint.setSearching(DesignChoiceConstants.SEARCH_TYPE_INDEXED);
		columnHint.setExport(DesignChoiceConstants.EXPORT_TYPE_IF_REALIZED);
		columnHint.setAnalysis(DesignChoiceConstants.ANALYSIS_TYPE_MEASURE);
		columnHint.setParentLevel("new level"); //$NON-NLS-1$
		columnHint.setFormat("new format"); //$NON-NLS-1$
		columnHint.setDisplayNameKey("new display name id"); //$NON-NLS-1$
		columnHint.setDisplayName("new display name"); //$NON-NLS-1$
		columnHint.setHelpTextKey("new help text id"); //$NON-NLS-1$
		columnHint.setHelpText("new help text"); //$NON-NLS-1$
		columnHint.setOnColumnLayout(false);

	}

	public void testDataSetParameterSynchronization() throws Exception {
		designHandle = createDesign();

		// Add one data set to design

		DataSetHandle dataSetHandle = designHandle.getElementFactory().newOdaDataSet("DataSet"); //$NON-NLS-1$
		designHandle.getDataSets().add(dataSetHandle);

		// Add one parameter to data set

		OdaDataSetParameter parameter = StructureFactory.createOdaDataSetParameter();
		parameter.setName("param1"); //$NON-NLS-1$
		PropertyHandle parameterHandle = dataSetHandle.getPropertyHandle(DataSetHandle.PARAMETERS_PROP);
		parameterHandle.addItem(parameter);

		// Add one param binding to data set

		ParamBinding dataSetBinding = StructureFactory.createParamBinding();
		dataSetBinding.setParamName("param1"); //$NON-NLS-1$
		PropertyHandle dataSetBindingHandle = dataSetHandle.getPropertyHandle(DataSetHandle.PARAM_BINDINGS_PROP);
		dataSetBindingHandle.addItem(dataSetBinding);

		// Add one table to design

		TableHandle tableHandle = designHandle.getElementFactory().newTableItem("Table"); //$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		// Let table use data set and create one parameter binding

		tableHandle.setDataSet(dataSetHandle);
		ParamBinding tableBinding = StructureFactory.createParamBinding();
		tableBinding.setParamName("param1"); //$NON-NLS-1$
		PropertyHandle tableBindingHandle = tableHandle.getPropertyHandle(TableHandle.PARAM_BINDINGS_PROP);
		tableBindingHandle.addItem(tableBinding);

		// Add one image to design

		ImageHandle imageHandle = designHandle.getElementFactory().newImage("Image"); //$NON-NLS-1$
		designHandle.getBody().add(imageHandle);

		// Let image use data set and create one action

		imageHandle.setDataSet(dataSetHandle);
		Action action = StructureFactory.createAction();
		imageHandle.setAction(action);

		// Add one parameter binding in action

		ActionHandle actionHandle = imageHandle.getActionHandle();
		ParamBinding actionBinding = StructureFactory.createParamBinding();
		actionBinding.setParamName("param1"); //$NON-NLS-1$
		actionHandle.addParamBinding(actionBinding);

		// Rename parameter name and parameter bindings are updated

		parameterHandle.getAt(0).setProperty(DataSetParameter.NAME_MEMBER, "param2"); //$NON-NLS-1$
		assertEquals("param2", dataSetBinding.getParamName()); //$NON-NLS-1$
		assertEquals("param2", tableBinding.getParamName()); //$NON-NLS-1$
		assertEquals("param2", actionBinding.getParamName()); //$NON-NLS-1$

		// Remove parameter name and parameter bindings are updated

		parameterHandle.removeItem(0);
		assertNull(dataSetBindingHandle.getListValue());
		assertNull(tableBindingHandle.getListValue());
		assertNull(actionHandle.getMember(Action.PARAM_BINDINGS_MEMBER).getListValue());
	}

	/**
	 * Returns the data set for testing.
	 * 
	 * @return the data set for testing.
	 * @throws Exception if any exception.
	 */

	private OdaDataSetHandle getDataSet() throws Exception {
		openDesign("OdaDataSetParseTest_2.xml"); //$NON-NLS-1$

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);

		return dataSet;
	}

	/**
	 * Tests query text property.
	 */
	public void testQueryText() throws Exception {
		openDesign(queryTextInputFileName2);

		OdaDataSetHandle odaHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet1"); //$NON-NLS-1$

		assertEquals(" test ]]> test  ", odaHandle.getQueryText()); //$NON-NLS-1$

		odaHandle = (OdaDataSetHandle) designHandle.findDataSet("myDataSet2"); //$NON-NLS-1$

		assertEquals("select * from CLASSICMODELS.CUSTOMERS", odaHandle.getQueryText()); //$NON-NLS-1$

		odaHandle.setQueryText("  select * from CLASSICMODELS.CUSTOMERS test ]]> test  "); //$NON-NLS-1$

		save();

		assertTrue(compareFile(queryTextGoldenFileName2));
	}

	/**
	 * The structure context for the extended property is valid. Should not null.
	 * 
	 * @throws Exception
	 */

	public void testExtendedPropertyContext() throws Exception {
		openDesign(extendedPropertyInput);

		OdaDataSetHandle tmpSet = (OdaDataSetHandle) designHandle.findDataSet("DataSet"); //$NON-NLS-1$

		Iterator<ExtendedPropertyHandle> iter1 = tmpSet.privateDriverPropertiesIterator();
		assertNotNull(iter1.next());
	}

	public void testAnyDataType() throws Exception {
		openDesign(anyDataTypeInput);
		assertTrue(designHandle.getWarningList().isEmpty());
		assertTrue(designHandle.getErrorList().isEmpty());
	}
}