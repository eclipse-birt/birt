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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.CalculationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * API test cases for ComputedColumnHandle.
 * 
 */

public class ComputedColumnHandleTest extends BaseTestCase {

	/**
	 * To test add/remove methods on aggregateOn and argument lists.
	 * 
	 * @throws Exception
	 */

	public void testSimpleListProperties() throws Exception {
		createDesign();

		DataItemHandle data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$
		designHandle.getBody().add(data);

		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName("column 1"); //$NON-NLS-1$
		column.setExpression("expression 1"); //$NON-NLS-1$

		// test clear aggregate on string list on structure

		column.setAggregateOn("agg 1"); //$NON-NLS-1$
		column.addAggregateOn("agg 2"); //$NON-NLS-1$
		assertEquals(2, column.getAggregateOnList().size());
		column.clearAggregateOnList();
		assertEquals(0, column.getAggregateOnList().size());

		ComputedColumnHandle columnHandle = data.addColumnBinding(column, false);
		AggregationArgument argument = new AggregationArgument();
		argument.setName("arg_1"); //$NON-NLS-1$
		argument.setValue("argument 1"); //$NON-NLS-1$

		columnHandle.addArgument(argument);

		Iterator iter = columnHandle.argumentsIterator();
		assertTrue(iter.hasNext());

		columnHandle.removeArgument(argument);
		iter = columnHandle.argumentsIterator();
		assertFalse(iter.hasNext());

		columnHandle.addAggregateOn("group 1"); //$NON-NLS-1$
		List aggregates = columnHandle.getAggregateOnList();
		assertEquals(1, aggregates.size());

		columnHandle.removeAggregateOn("group 1"); //$NON-NLS-1$
		aggregates = columnHandle.getAggregateOnList();
		assertEquals(0, aggregates.size());

		// test clear aggregate on stirng list on handle

		columnHandle.setAggregateOn("agg 1"); //$NON-NLS-1$
		columnHandle.addAggregateOn("agg 2"); //$NON-NLS-1$
		assertEquals(2, columnHandle.getAggregateOnList().size());
		columnHandle.clearAggregateOnList();
		assertEquals(0, columnHandle.getAggregateOnList().size());
	}

	/**
	 * To test add arguments on the ComputedColumn structure.
	 * 
	 * @throws Exception
	 */

	public void testArguments() throws Exception {
		createDesign();

		DataItemHandle data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$
		designHandle.getBody().add(data);

		ComputedColumn column = StructureFactory.createComputedColumn();
		AggregationArgument argument = StructureFactory.createAggregationArgument();
		argument.setName("argu1"); //$NON-NLS-1$
		argument.setValue("value1");//$NON-NLS-1$
		column.addArgument(argument);

		argument = StructureFactory.createAggregationArgument();
		argument.setName("argu2"); //$NON-NLS-1$
		argument.setValue("value2"); //$NON-NLS-1$
		column.addArgument(argument);

		column.setName("column1"); //$NON-NLS-1$
		column.setExpression("expression 1"); //$NON-NLS-1$

		data.addColumnBinding(column, false);

		column = StructureFactory.createComputedColumn();
		argument = StructureFactory.createAggregationArgument();
		argument.setName("argu3"); //$NON-NLS-1$
		argument.setValue("value3");//$NON-NLS-1$
		column.addArgument(argument);

		// the argument is null, exception should be thrown

		argument = StructureFactory.createAggregationArgument();
		argument.setValue("value4"); //$NON-NLS-1$
		column.addArgument(argument);

		column.setName("column2"); //$NON-NLS-1$
		column.setExpression("expression 2"); //$NON-NLS-1$

		try {
			data.addColumnBinding(column, false);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}

	}

	/**
	 * For bug 200645, skip choice check in ComputedColumn structure.
	 * 
	 * @throws Exception
	 */

	public void testSkipComputedColumnValidation() throws Exception {
		openDesign("ComputedColumnHandleTest.xml");//$NON-NLS-1$
		ScalarParameterHandle paramHandle = (ScalarParameterHandle) designHandle.findParameter("MyParam1");//$NON-NLS-1$

		ComputedColumnHandle columnHandle = (ComputedColumnHandle) paramHandle.getColumnBindings().get(0);
		assertEquals("sum 2", columnHandle.getAggregateFunction());//$NON-NLS-1$

		ComputedColumnHandle columnHandle2 = (ComputedColumnHandle) paramHandle.getColumnBindings().get(1);
		assertEquals("MAX", columnHandle2.getAggregateFunction());//$NON-NLS-1$

		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setProperty(ComputedColumn.AGGREGATEON_FUNCTION_MEMBER, "count 2");//$NON-NLS-1$
		column.setName("column3");//$NON-NLS-1$

		PropertyHandle propHandle = paramHandle.getColumnBindings();
		propHandle.addItem(column);

		ComputedColumnHandle columnHandle3 = (ComputedColumnHandle) propHandle.get(2);
		assertEquals("count 2", columnHandle3.getAggregateFunction());//$NON-NLS-1$

		save();
		assertTrue(compareFile("ComputedColumnHandleTest_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Tests dataType property for column binding
	 * 
	 * @throws Exception
	 */
	public void testDataTypeInComputedColumn() throws Exception {
		openDesign("ComputedColumnHandleTest_1.xml"); //$NON-NLS-1$

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$

		List columns = (List) dataSet.getProperty(SimpleDataSet.COMPUTED_COLUMNS_PROP);

		// tests convert dataType of column binding from any to string.
		ComputedColumn computedColumn = (ComputedColumn) columns.get(0);
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_ANY, computedColumn.getDataType());

		// tests the default value of dataType in column binding.
		computedColumn = (ComputedColumn) columns.get(1);
		assertNull(computedColumn.getDataType());

		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$

		columns = (List) table.getProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP);

		// tests convert dataType of column binding from any to string.
		computedColumn = (ComputedColumn) columns.get(0);
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_ANY, computedColumn.getDataType());

		// tests the default value of dataType in column binding.
		computedColumn = (ComputedColumn) columns.get(1);
		assertNull(computedColumn.getDataType());

		ScalarParameterHandle param1 = (ScalarParameterHandle) designHandle.findParameter("NewParameter"); //$NON-NLS-1$

		columns = (List) param1.getProperty(IScalarParameterModel.BOUND_DATA_COLUMNS_PROP);

		// tests convert dataType of column binding from any to string.
		computedColumn = (ComputedColumn) columns.get(0);
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_ANY, computedColumn.getDataType());

		// tests the default value of dataType in column binding.
		computedColumn = (ComputedColumn) columns.get(1);
		assertNull(computedColumn.getDataType());

	}

	/**
	 * Tests the new feature of TimePeriod. TED 41378.
	 * 
	 * @throws Exception
	 */
	public void testTimePeriod() throws Exception {
		openDesign("ComputedColumnHandleTest_2.xml"); //$NON-NLS-1$

		ScalarParameterHandle paramHandle = (ScalarParameterHandle) designHandle.findParameter("MyParam1"); //$NON-NLS-1$
		Iterator iter = paramHandle.columnBindingsIterator();

		// test getters
		ComputedColumnHandle columnHandle = (ComputedColumnHandle) iter.next();
		assertEquals("next_n", columnHandle.getCalculationType()); //$NON-NLS-1$
		Iterator argumentIter = columnHandle.calculationArgumentsIterator();
		CalculationArgumentHandle argumentHandle = (CalculationArgumentHandle) argumentIter.next();
		assertEquals("calculation_argument_1", argumentHandle.getName()); //$NON-NLS-1$
		assertEquals("calculation_argument_1_value", argumentHandle.getValue().getStringValue()); //$NON-NLS-1$
		argumentHandle = (CalculationArgumentHandle) argumentIter.next();
		assertEquals("calculation_argument_2", argumentHandle.getName()); //$NON-NLS-1$
		assertEquals("calculation_argument_2_value", argumentHandle.getValue().getStringValue()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.REFERENCE_DATE_TYPE_TODAY, columnHandle.getReferenceDateType());
		assertEquals("12", columnHandle.getReferenceDateValue().getStringValue()); //$NON-NLS-1$
		assertEquals("time dimension expression", columnHandle.getTimeDimension()); //$NON-NLS-1$

		// test writers, case 1: set in existing column
		columnHandle.removeCalculationArgument((CalculationArgument) argumentHandle.getStructure());
		columnHandle = (ComputedColumnHandle) iter.next();
		CalculationArgument struct = StructureFactory.createCalculationArgument();
		struct.setName("new_argument"); //$NON-NLS-1$
		struct.setValue(new Expression("new_argument_value", IExpressionType.CONSTANT)); //$NON-NLS-1$
		columnHandle.addCalculationArgument(struct);
		columnHandle.setCalculationType("current_year"); //$NON-NLS-1$
		columnHandle.setReferenceDateType(DesignChoiceConstants.REFERENCE_DATE_TYPE_ENDING_DATE_IN_DIMENSION);

		// case 2: create column and then set time period members
		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName("newColumn"); //$NON-NLS-1$
		column.setCalculationType("current_month"); //$NON-NLS-1$
		column.addCalculationArgument(struct);
		column.setReferenceDateType(DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE);
		column.setReferenceDateValue(new Expression("newly reference date", IExpressionType.CONSTANT)); //$NON-NLS-1$
		column.setTimeDimension("newly time dimension"); //$NON-NLS-1$
		paramHandle.addColumnBinding(column, false);

		save();
		assertTrue(compareFile("ComputedColumnHandleTest_golden_1.xml"));//$NON-NLS-1$
	}

}
