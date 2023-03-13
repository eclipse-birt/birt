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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test cases for sort element and filter element.
 */
public class FilterAndSortParseTest extends BaseTestCase {

	private static final String FILE_NAME = "FilterAndSortParseTest.xml"; //$NON-NLS-1$

	/**
	 *
	 * @throws Exception
	 */
	public void testParser() throws Exception {
		openDesign(FILE_NAME);
		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$
		assertNotNull(testTable);

		// test filter properties
		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		assertEquals(2, valueList.size());
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList.get(0);
		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_LT, filter.getOperator());
		assertTrue(filter.isOptional());
		assertEquals("filter expression", filter.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", filter.getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", filter.getValue2()); //$NON-NLS-1$
		ExpressionHandle exprHandle = filter.getExpressionProperty(FilterCondition.VALUE2_MEMBER);
		assertEquals(IExpressionType.CONSTANT, exprHandle.getType());
		assertEquals("value2 expression", exprHandle.getStringExpression()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.FILTER_TARGET_RESULT_SET, filter.getFilterTarget());
		assertEquals("ext name", filter.getExtensionName()); //$NON-NLS-1$
		assertEquals("ext id", filter.getExtensionExprId()); //$NON-NLS-1$
		assertTrue(filter.pushDown());
		assertEquals("DynamicFilterParam", filter.getDynamicFilterParameter()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FILTER_CONDITION_TYPE_SLICER, filter.getType());
		assertTrue(filter.updateAggregation());

		// test member value in filter
		MemberValueHandle memberValue = filter.getMember();
		assertEquals("value_1", memberValue.getValue()); //$NON-NLS-1$
		assertEquals("testDimension/testLevel", memberValue.getCubeLevelName()); //$NON-NLS-1$
		assertNotNull(memberValue.getLevel());
		// test filter condition structures in member value
		Iterator<FilterConditionHandle> iter = memberValue.filtersIterator();
		FilterConditionHandle filterStructHandle = iter.next();
		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_LT, filterStructHandle.getOperator());

		memberValue = (MemberValueHandle) memberValue.getContent(IMemberValueModel.MEMBER_VALUES_PROP, 0);
		assertEquals("value_2", memberValue.getValue()); //$NON-NLS-1$
		assertNull(memberValue.getLevel());
		assertNull(memberValue.getCubeLevelName());

		// test sort properties
		valueList = testTable.getListProperty("sorts"); //$NON-NLS-1$
		assertEquals(2, valueList.size());
		SortElementHandle sort = (SortElementHandle) valueList.get(0);
		assertEquals("key_1", sort.getKey()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_DESC, sort.getDirection());
		assertEquals(8, sort.getStrength());
		assertEquals(ULocale.GERMAN, sort.getLocale());

		// test member value in sort
		memberValue = sort.getMember();
		assertEquals("value_1", memberValue.getValue()); //$NON-NLS-1$
		assertEquals("testDimension/testLevel", memberValue.getCubeLevelName()); //$NON-NLS-1$
		assertNotNull(memberValue.getLevel());
		memberValue = (MemberValueHandle) memberValue.getContent(IMemberValueModel.MEMBER_VALUES_PROP, 0);
		assertEquals("value_2", memberValue.getValue()); //$NON-NLS-1$
		assertNull(memberValue.getLevel());
		assertNull(memberValue.getCubeLevelName());

		// test filter condition

		TableHandle table = (TableHandle) designHandle.findElement("Test table"); //$NON-NLS-1$
		assertNotNull(table);

		FilterConditionHandle filterHandle = (FilterConditionHandle) table.filtersIterator().next();

		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_LT, filterHandle.getOperator());
		assertEquals("filter expression", filterHandle.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", filterHandle.getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", filterHandle.getValue2()); //$NON-NLS-1$
		assertEquals("ext name", filterHandle.getExtensionName()); //$NON-NLS-1$
		assertEquals("ext id", filterHandle.getExtensionExprId()); //$NON-NLS-1$
		assertTrue(filterHandle.pushDown());
		assertEquals("DynamicFilterParam", filterHandle.getDynamicFilterParameter()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FILTER_CONDITION_TYPE_SLICER, filterHandle.getType());
		assertTrue(filterHandle.updateAggregation());
		assertEquals("customed 1", filterHandle.getCustomValue()); //$NON-NLS-1$
	}

	/**
	 *
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		openDesign(FILE_NAME);
		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$
		assertNotNull(testTable);

		final String valuePrefix = "new "; //$NON-NLS-1$

		// test filter properties
		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		assertEquals(2, valueList.size());
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList.get(0);
		filter.setOperator(DesignChoiceConstants.FILTER_OPERATOR_GE);
		filter.setOptional(false);
		filter.setExpr(valuePrefix + filter.getExpr());
		filter.setValue1(valuePrefix + filter.getValue1());
		filter.setValue2(valuePrefix + filter.getValue2());
		filter.setFilterTarget(DesignChoiceConstants.FILTER_TARGET_DATA_SET);
		filter.setExtensionExprId(valuePrefix + filter.getExtensionExprId());
		filter.setExtensionName(valuePrefix + filter.getExtensionName());
		filter.setPushDown(false);
		filter.setDynamicFilterParameter(valuePrefix + filter.getDynamicFilterParameter());
		filter.setType(DesignChoiceConstants.FILTER_CONDITION_TYPE_SIMPLE);
		filter.setUpdateAggregation(false);

		// test member value in filter
		MemberValueHandle memberValue = filter.getMember();
		memberValue.setValue(valuePrefix + memberValue.getValue());
		memberValue.setLevel(designHandle.findLevel("testDimension/testLevel_one")); //$NON-NLS-1$
		// test filter condition structures in member value
		Iterator<FilterConditionHandle> iter = memberValue.filtersIterator();
		FilterConditionHandle filterStructHandle = iter.next();
		memberValue.getPropertyHandle(IMemberValueModel.FILTER_PROP).addItem(filterStructHandle.getStructure().copy());

		// test sort properties
		valueList = testTable.getListProperty("sorts"); //$NON-NLS-1$
		assertEquals(2, valueList.size());
		SortElementHandle sort = (SortElementHandle) valueList.get(0);
		sort.setKey(valuePrefix + sort.getKey());
		sort.setDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
		sort.setStrength(12);
		sort.setLocale(ULocale.CANADA_FRENCH);

		// test member value in sort
		memberValue = sort.getMember();
		memberValue.setValue(valuePrefix + memberValue.getValue());
		memberValue.setLevel(designHandle.findLevel("testDimension/testLevel_one")); //$NON-NLS-1$

		// test filter condition

		TableHandle table = (TableHandle) designHandle.findElement("Test table"); //$NON-NLS-1$
		assertNotNull(table);

		Iterator<FilterConditionHandle> iter1 = table.filtersIterator();
		FilterConditionHandle filterHandle = iter1.next();

		filterHandle.setOperator(DesignChoiceConstants.FILTER_OPERATOR_GT);
		filterHandle.setExpr(valuePrefix + filterHandle.getExpr());
		filterHandle.setValue1(valuePrefix + filterHandle.getValue1());
		filterHandle.setExtensionExprId(valuePrefix + filterHandle.getExtensionExprId());
		filterHandle.setExtensionName(valuePrefix + filterHandle.getExtensionName());
		filterHandle.setPushDown(false);
		filterHandle.setDynamicFilterParameter(valuePrefix + filterHandle.getDynamicFilterParameter());
		filterHandle.setType(DesignChoiceConstants.FILTER_CONDITION_TYPE_SIMPLE);
		filterHandle.setUpdateAggregation(false);
		filterHandle = iter1.next();
		List<Expression> tmpList = new ArrayList<>();
		tmpList.add(new Expression("constant1", ExpressionType.CONSTANT)); //$NON-NLS-1$
		filterHandle.setValue1(tmpList);
		filterHandle.setValue2(new Expression("constant2", ExpressionType.CONSTANT)); //$NON-NLS-1$ )
		filterHandle.setCustomValue("customed updated"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("FilterAndSortParseTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests the cases for supporting user properties in FilterConditionElement.
	 *
	 * @throws Exception
	 */
	public void testUserProperties() throws Exception {
		openDesign("FilterAndSortParseTest_1.xml"); //$NON-NLS-1$
		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$
		assertNotNull(testTable);

		// test user property in filter condition element
		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		assertEquals(2, valueList.size());
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList.get(0);
		List<UserPropertyDefn> userProps = filter.getUserProperties();
		assertEquals(1, userProps.size());
		UserPropertyDefn userProp = userProps.get(0);
		assertEquals(userProp, filter.getPropertyDefn(userProp.getName()));
		assertEquals("valueProp1", filter.getStringProperty(userProp.getName())); //$NON-NLS-1$

		// add user property in filter condition element
		filter = (FilterConditionElementHandle) valueList.get(1);
		userProp = new UserPropertyDefn();
		userProp.setName("userProp2"); //$NON-NLS-1$
		userProp.setType(MetaDataDictionary.getInstance().getPropertyType(IPropertyType.INTEGER_TYPE));
		filter.addUserPropertyDefn(userProp);
		filter.setIntProperty(userProp.getName(), 12);

		// save and test writer
		save();
		assertTrue(compareFile("FilterAndSortParseTest_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testPropertyHandleIterator() throws Exception {
		openDesign(FILE_NAME);
		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$
		assertNotNull(testTable);

		PropertyHandle propHandle = testTable.getPropertyHandle("filter"); //$NON-NLS-1$
		Iterator filters = propHandle.iterator();
		assertTrue(filters.hasNext());
		assertTrue(filters.next() instanceof FilterConditionElementHandle);
		assertTrue(filters.hasNext());
		assertTrue(filters.next() instanceof FilterConditionElementHandle);

		assertFalse(filters.hasNext());
	}
}
