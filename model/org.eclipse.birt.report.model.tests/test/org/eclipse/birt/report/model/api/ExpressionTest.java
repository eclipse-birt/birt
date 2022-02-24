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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Cases for a new internal data structure Expression and its handle
 * ExpressionHandle.
 * <p>
 * For exporting cases, please see ElementExporterTest.
 * 
 */

public class ExpressionTest extends BaseTestCase {

	/**
	 * 
	 */

	private static final String INPUT_FILE = "ExpressionTest.xml"; //$NON-NLS-1$

	/**
	 * Test cases:
	 * <ul>
	 * <li>get/set values on expression values for elements.
	 * <li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testGetAndSetValuesOnElement() throws Exception {
		openDesign(INPUT_FILE);

		// ScalarParameter.defaultValue

		ScalarParameterHandle param = (ScalarParameterHandle) designHandle.findParameter("Param1"); //$NON-NLS-1$
		List<Expression> values = (List<Expression>) param.getProperty(ScalarParameterHandle.DEFAULT_VALUE_PROP);

		assertEquals(3, values.size());
		equals(values.get(0), "value1", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(values.get(1), "value2", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(values.get(2), "value3", //$NON-NLS-1$
				ExpressionType.JAVASCRIPT);

		values = new ArrayList<Expression>();
		values.add(new Expression("value1", ExpressionType.JAVASCRIPT)); //$NON-NLS-1$
		values.add(new Expression("123", ExpressionType.CONSTANT)); //$NON-NLS-1$

		param.setProperty(ScalarParameterHandle.DEFAULT_VALUE_PROP, values);
		values = (List<Expression>) param.getListProperty(ScalarParameterHandle.DEFAULT_VALUE_PROP);
		assertEquals(2, values.size());
		equals(values.get(0), "value1", ExpressionType.JAVASCRIPT); //$NON-NLS-1$
		equals(values.get(1), "123", ExpressionType.CONSTANT); //$NON-NLS-1$

		// ReportItem.bookmark

		DataItemHandle tmpItem = (DataItemHandle) designHandle.findElement("bodyData"); //$NON-NLS-1$

		ExpressionHandle exprHandle = tmpItem.getExpressionProperty(IReportItemModel.BOOKMARK_PROP);
		assertEquals("true", exprHandle.getExpression()); //$NON-NLS-1$
		assertEquals(ExpressionType.JAVASCRIPT, exprHandle.getType());

		exprHandle.setExpression("123"); //$NON-NLS-1$
		exprHandle.setType(ExpressionType.CONSTANT);

		Expression expr = (Expression) tmpItem.getElement().getProperty(design, IReportItemModel.BOOKMARK_PROP);
		equals(expr, "123", ExpressionType.CONSTANT); //$NON-NLS-1$

		// ReportItem.onRender

		Object tmpValue = tmpItem.getProperty(IReportItemModel.ON_RENDER_METHOD);
		assertTrue(tmpValue instanceof String);
	}

	/**
	 * @param expr
	 * @param expr1
	 * @param type
	 * @return
	 */

	private static void equals(Expression expr, Object expr1, String type) {
		assert expr != null;

		if (!ModelUtil.isEquals(expr.getExpression(), expr1))
			assertTrue(false);

		if (!ModelUtil.isEquals(expr.getType(), type))
			assertTrue(false);

	}

	/**
	 * @throws Exception
	 */

	public void testCopy() throws Exception {
		openDesign(INPUT_FILE);

		ScalarParameterHandle param = (ScalarParameterHandle) designHandle.findParameter("Param1"); //$NON-NLS-1$

		List<Expression> values = (List<Expression>) param.getProperty(ScalarParameterHandle.DEFAULT_VALUE_PROP);

		List<Expression> cloned = (List) ModelUtil
				.copyValue(param.getPropertyDefn(ScalarParameterHandle.DEFAULT_VALUE_PROP), values);

		assertEquals(3, cloned.size());
		equals(cloned.get(0), "value1", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(cloned.get(1), "value2", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(cloned.get(2), "value3", //$NON-NLS-1$
				ExpressionType.JAVASCRIPT);

		assertTrue(cloned.get(0) != values.get(0));
		assertTrue(cloned.get(1) != values.get(1));
		assertTrue(cloned.get(2) != values.get(2));
	}

	/**
	 * Tests all get/set methods for the expression value defined on the structure.
	 * Uses SortKey as the example.
	 * 
	 * @throws Exception
	 */

	public void testGetAndSetValuesOnStructure() throws Exception {
		createDesign();

		TableHandle table = designHandle.getElementFactory().newTableItem("table1"); //$NON-NLS-1$
		designHandle.getBody().add(table);

		PropertyHandle propHandle = table.getPropertyHandle(ListingElement.SORT_PROP);

		SortKey sortKey = StructureFactory.createSortKey();
		sortKey.setProperty(SortKey.KEY_MEMBER, new Expression("expression", ExpressionType.JAVASCRIPT));
		propHandle.addItem(sortKey);

		Iterator iter = propHandle.iterator();
		SortKeyHandle sortHandle = (SortKeyHandle) iter.next();

		assertEquals("expression", sortHandle.getKey()); //$NON-NLS-1$
		ExpressionHandle tmpExpr = sortHandle.getExpressionProperty(SortKey.KEY_MEMBER);
		assertEquals("expression", sortHandle.getKey()); //$NON-NLS-1$
		assertEquals("expression", tmpExpr.getStringExpression()); //$NON-NLS-1$
		assertEquals(ExpressionType.JAVASCRIPT, tmpExpr.getType());

		sortHandle.setProperty(SortKey.KEY_MEMBER, new Expression("new expression", ExpressionType.JAVASCRIPT)); //$NON-NLS-1$
		assertEquals("new expression", sortHandle.getKey()); //$NON-NLS-1$
	}

	/**
	 * Tests ExpressionListHandle class. getListValue/setListValue().
	 * 
	 * @throws Exception
	 */

	public void testExpressionListHandle() throws Exception {
		// use the same file in API compatibility

		openDesign("CompatibleValue1Test.xml"); //$NON-NLS-1$

		// test style rule handle

		StyleHandle tmpStyle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		Iterator iter1 = tmpStyle.highlightRulesIterator();
		HighlightRuleHandle tmpHighlight = (HighlightRuleHandle) iter1.next();

		ExpressionListHandle tmpHandle = tmpHighlight.getValue1ExpressionList();
		List<Expression> tmpValues = tmpHandle.getListValue();
		equals(tmpValues.get(0), "hi_value1", ExpressionType.JAVASCRIPT); //$NON-NLS-1$
		equals(tmpValues.get(1), "hi_value2", ExpressionType.CONSTANT); //$NON-NLS-1$

		iter1 = tmpStyle.mapRulesIterator();
		MapRuleHandle tmpMapRule = (MapRuleHandle) iter1.next();

		tmpHandle = tmpMapRule.getValue1ExpressionList();
		tmpValues = tmpHandle.getListValue();
		equals(tmpValues.get(0), "map_value1", ExpressionType.JAVASCRIPT); //$NON-NLS-1$
		equals(tmpValues.get(1), "map_value2", ExpressionType.CONSTANT); //$NON-NLS-1$

		List<Expression> newValues = new ArrayList<Expression>();

		newValues.add(new Expression("new a", ExpressionType.CONSTANT)); //$NON-NLS-1$
		newValues.add(new Expression("new b", ExpressionType.CONSTANT)); //$NON-NLS-1$
		newValues.add(new Expression("new c", ExpressionType.CONSTANT));//$NON-NLS-1$
		tmpHandle.setListValue(newValues);

		tmpValues = tmpHandle.getListValue();
		equals(tmpValues.get(0), "new a", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(tmpValues.get(1), "new b", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(tmpValues.get(2), "new c", ExpressionType.CONSTANT); //$NON-NLS-1$

		// test filter properties

		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$
		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList.get(0);

		tmpHandle = filter.getValue1ExpressionList();
		tmpValues = tmpHandle.getListValue();
		equals(tmpValues.get(0), "filter_value1", ExpressionType.JAVASCRIPT); //$NON-NLS-1$
		equals(tmpValues.get(1), "filter_value2", ExpressionType.JAVASCRIPT); //$NON-NLS-1$

		tmpHandle.setListValue(newValues);

		tmpValues = tmpHandle.getListValue();
		equals(tmpValues.get(0), "new a", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(tmpValues.get(1), "new b", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(tmpValues.get(2), "new c", ExpressionType.CONSTANT); //$NON-NLS-1$

		TableHandle tmpTable = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		iter1 = tmpTable.getPrivateStyle().mapRulesIterator();

		// table map rules
		tmpMapRule = (MapRuleHandle) iter1.next();

		tmpHandle = tmpMapRule.getValue1ExpressionList();
		tmpValues = tmpHandle.getListValue();

		equals(tmpValues.get(0), "1", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(tmpValues.get(1), "2", ExpressionType.CONSTANT); //$NON-NLS-1$
		equals(tmpValues.get(2), "4", ExpressionType.CONSTANT); //$NON-NLS-1$

	}

	/**
	 * The expression type on Action won't be missed in the parser.
	 * 
	 * @throws Exception
	 */

	public void testConstantTypeOnAction() throws Exception {
		openDesign(INPUT_FILE);

		ActionHandle actionHandle = ((ImageHandle) designHandle.findElement("Image1")).getActionHandle(); //$NON-NLS-1$

		ExpressionHandle exprHandle = actionHandle.getExpressionProperty(Action.URI_MEMBER);

		// test the expression type.
		assertEquals(ExpressionType.CONSTANT, exprHandle.getType());
	}

	/**
	 * 
	 * @throws Exception
	 */

	public void testTOC() throws Exception {
		openDesign(INPUT_FILE);

		DataItemHandle tmpData = (DataItemHandle) designHandle.findElement("bodyData"); //$NON-NLS-1$

		TOCHandle tocHandle = tmpData.getTOC();
		TOC toc = (TOC) tocHandle.getStructure();

		// the compatible case

		toc.setExpression("new statistics"); //$NON-NLS-1$

		ExpressionHandle exprHandle = tocHandle.getExpressionProperty(TOC.TOC_EXPRESSION);

		// should have no exception.

		assertEquals(ExpressionType.JAVASCRIPT, exprHandle.getType());
	}
}
