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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.JoinConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.JoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for the parser and writer of JointDataSet. The test cases are:
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="black">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 *
 * <tr>
 * <td>testParser</td>
 * <td>In the XML file, data set contains two normal data sets and one joint
 * data set. The joint data set has a joit condition.</td>
 * <td>The dataset and the join condition should be correctly parsed.</td>
 * </tr>
 *
 * <tr>
 * <td>testWriter</td>
 * <td>A joint data set which contains a join condition.</td>
 * <td>The data set should be correctly saved into the output file.</td>
 * </tr>
 *
 * <tr>
 * <td>testValidation</td>
 * <td>A report design file has six joint data sets, each one of which has a
 * blank field.</td>
 * <td>After the design file is parsed, six sematic errors should be
 * returned.</td>
 * </tr>
 *
 * <tr>
 * <td>testSemanticCheck</td>
 * <td>A joint data set refers to an unexist data set will create semantic
 * error.</td>
 * <td>A semantic error with invalid element reference error code will be
 * returned after reprot is parsed.</td>
 * </tr>
 *
 * </table>
 *
 * @see org.eclipse.birt.report.model.elements.JointDataSet
 */

public class JointDataSetParseTest extends BaseTestCase {

	String inputFileName = "JointDataSetParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "JointDataSetParseTest_golden.xml"; //$NON-NLS-1$

	String validationInputFileName = "JointDataSetParseTest_validation.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "JointDataSetParseTest_semantic.xml"; //$NON-NLS-1$

	/**
	 * Tests joint data set can be correctly parsed.
	 *
	 * @throws DesignFileException design file can't be parsed correctly.
	 */

	public void testParser() throws DesignFileException {
		openDesign(inputFileName);
		JointDataSetHandle dataSet = designHandle.findJointDataSet("JointDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);
		assertEquals(0, dataSet.getRowFetchLimit());
		// [T46404] default valut to true
		assertTrue(dataSet.needsCache());

		// get the sub data sets by element level
		List dataSets = dataSet.getElement().getListProperty(design, JointDataSet.DATA_SETS_PROP);
		assertEquals(2, dataSets.size());
		DataSetHandle ds1 = designHandle.findDataSet("DataSet1");//$NON-NLS-1$
		assertSame(ds1.getElement(), ((ElementRefValue) dataSets.get(0)).getElement());
		assertEquals(30, ds1.getRowFetchLimit());
		assertTrue(ds1.needsCache());
		DataSetHandle ds2 = designHandle.findDataSet("DataSet2");//$NON-NLS-1$
		assertEquals(0, ds2.getRowFetchLimit());
		assertSame(ds2.getElement(), ((ElementRefValue) dataSets.get(1)).getElement());

		// get the sub data sets by handle API
		Iterator dataSetIter = dataSet.dataSetsIterator();
		assertEquals(ds1, dataSetIter.next());
		assertEquals(ds2, dataSetIter.next());
		assertFalse(dataSetIter.hasNext());

		// test joinCondition iterator
		Iterator joinConditionsIterator = dataSet.joinConditionsIterator();
		assertTrue(joinConditionsIterator.hasNext());
		JoinConditionHandle joinConditionHandle = (JoinConditionHandle) joinConditionsIterator.next();
		assertFalse(joinConditionsIterator.hasNext());

		assertEquals(DesignChoiceConstants.JOIN_TYPE_INNER, joinConditionHandle.getJoinType());
		assertEquals(DesignChoiceConstants.JOIN_OPERATOR_EQALS, joinConditionHandle.getOperator());
		assertEquals("DataSet1", joinConditionHandle.getLeftDataSet()); //$NON-NLS-1$
		assertEquals("DataSet2", joinConditionHandle.getRightDataSet()); //$NON-NLS-1$
		assertEquals("leftExpression", joinConditionHandle //$NON-NLS-1$
				.getLeftExpression());
		assertEquals("rightExpression", joinConditionHandle //$NON-NLS-1$
				.getRightExpression());

		TableHandle table = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertEquals("JointDataSet", table.getDataSet().getName()); //$NON-NLS-1$

		List parameters = (List) dataSet.getProperty(SimpleDataSet.PARAMETERS_PROP);
		assertEquals(1, parameters.size());

		// Test "input-parameters" on DataSet

		DataSetParameter parameter = (DataSetParameter) parameters.get(0);
		assertEquals(1, parameter.getPosition().intValue());
		assertEquals("name", parameter.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, parameter.getDataType());
		assertTrue(parameter.isOptional());
		assertEquals("default value 1", parameter.getDefaultValue()); //$NON-NLS-1$
		assertEquals(true, parameter.allowNull());

		// Test "result-set" on DataSet

		List columns = (List) dataSet.getProperty(DataSet.RESULT_SET_PROP);
		assertEquals(1, columns.size());
		ResultSetColumn column = (ResultSetColumn) columns.get(0);
		assertEquals(1, column.getPosition().intValue());
		assertEquals("name", column.getColumnName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING, column.getDataType());

		// Test "computed-columns" on DataSet

		columns = (List) dataSet.getProperty(DataSet.COMPUTED_COLUMNS_PROP);
		assertEquals(1, columns.size());

		ComputedColumn computedColumn = (ComputedColumn) columns.get(0);
		assertEquals("column1", computedColumn.getColumnName()); //$NON-NLS-1$
		assertEquals("expression1", computedColumn.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, computedColumn.getDataType());

		// Test "column-hints" on DataSet

		List columnHints = (List) dataSet.getProperty(DataSet.COLUMN_HINTS_PROP);
		assertEquals(1, columnHints.size());

		ColumnHint columnHint = (ColumnHint) columnHints.get(0);
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

		// Test "filter" on DataSet

		ArrayList filters = (ArrayList) dataSet.getProperty(DataSet.FILTER_PROP);

		assertEquals(1, filters.size());
		assertEquals("lt", ((FilterCondition) filters.get(0)).getOperator()); //$NON-NLS-1$
		assertEquals("filter expression", ((FilterCondition) filters.get(0)).getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", ((FilterCondition) filters.get(0)).getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", ((FilterCondition) filters.get(0)).getValue2()); //$NON-NLS-1$

	}

	/**
	 * Tests joint data set can be correctly written into design file
	 *
	 * @throws Exception design file exception
	 */

	public void testWriter() throws Exception {
		openDesign(inputFileName);
		JointDataSetHandle dataSet = designHandle.findJointDataSet("JointDataSet"); //$NON-NLS-1$
		assertNotNull(dataSet);
		dataSet.setRowFetchLimit(10);

		DataSetHandle ds1 = designHandle.findDataSet("DataSet1");//$NON-NLS-1$
		ds1.setRowFetchLimit(50);
		ds1.setNeedsCache(false);

		DataSetHandle ds2 = designHandle.findDataSet("DataSet2");//$NON-NLS-1$
		ds2.setRowFetchLimit(20);

		dataSet.addDataSet("DataSet3"); //$NON-NLS-1$

		JoinCondition condition = StructureFactory.createJoinCondition();
		condition.setJoinType(DesignChoiceConstants.JOIN_TYPE_LEFT_OUT);
		condition.setOperator(DesignChoiceConstants.JOIN_OPERATOR_EQALS);
		condition.setLeftDataSet("DataSet2"); //$NON-NLS-1$
		condition.setRightDataSet("DataSet3"); //$NON-NLS-1$
		condition.setLeftExpression("leftExpression"); //$NON-NLS-1$
		condition.setRightExpression("rightExpression"); //$NON-NLS-1$

		PropertyHandle conditionHandle = dataSet.getPropertyHandle(JointDataSet.JOIN_CONDITONS_PROP);
		conditionHandle.addItem(condition);
		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * If there is any blank field in the join condition, a semantic error should be
	 * returned by the parser.
	 *
	 * In the input file, there are six joint data sets, each one of which has a
	 * blank field. So after the file is parsed, six semantic errors should be
	 * returned.
	 *
	 * @throws Exception design file exception
	 */

	public void testValidation() throws Exception {
		openDesign(validationInputFileName);
		List errors = designHandle.getErrorList();
		assertEquals(6, errors.size());
		assertHasError((ErrorDetail) errors.get(0), "JointDataSet1"); //$NON-NLS-1$
		assertHasError((ErrorDetail) errors.get(1), "JointDataSet2"); //$NON-NLS-1$
		assertHasError((ErrorDetail) errors.get(2), "JointDataSet3"); //$NON-NLS-1$
		assertHasError((ErrorDetail) errors.get(3), "JointDataSet4"); //$NON-NLS-1$
		assertHasError((ErrorDetail) errors.get(4), "JointDataSet5"); //$NON-NLS-1$
		assertHasError((ErrorDetail) errors.get(5), "JointDataSet6"); //$NON-NLS-1$
	}

	/**
	 * Tests invalid reference to unexist data set will get semantic error.
	 *
	 * @throws DesignFileException
	 */
	public void testSemanticCheck() throws DesignFileException {
		openDesign(semanticCheckFileName);
		List errors = design.getErrorList();
		assertEquals(1, errors.size());
		ErrorDetail error = (ErrorDetail) errors.get(0);
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF, error.getErrorCode());
		assertEquals("JointDataSet", error.getElement().getName()); //$NON-NLS-1$
	}

	/**
	 * Checks there is a semantic error. It is the value required exception with the
	 * given data set.
	 *
	 * @param errors      the error list.
	 * @param dataSetName the name of the data set.
	 */

	private void assertHasError(ErrorDetail errorDetail, String dataSetName) {
		assertTrue(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED.equals(errorDetail.getErrorCode())
				&& errorDetail.getElement() instanceof JointDataSet
				&& dataSetName.equals(errorDetail.getElement().getName()));
	}

}
