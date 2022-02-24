/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.ir.VisibilityRuleDesign;

public class EngineIRParserTest extends TestCase {

	protected Report loadDesign(String design) throws Exception {
		InputStream in = this.getClass().getResourceAsStream(design);
		assertTrue(in != null);
		ReportParser parser = new ReportParser();
		return parser.parse("", in);
	}

	public void testReportItemDesign() throws Exception {
		Report report = loadDesign("report_item_test.rptdesign");
		ReportItemDesign item = report.getContent(0);

		long ID = item.getID();
		String name = item.getName();
		String extend = item.getExtends();
		String javaClass = item.getJavaClass();
		// String styleName = item.getStyleName( );

		DimensionType x = item.getX();
		DimensionType y = item.getY();
		DimensionType width = item.getWidth();
		DimensionType height = item.getHeight();
		Expression onCreate = item.getOnCreate();
		Expression onRender = item.getOnRender();
		Expression onPageBreak = item.getOnPageBreak();
		boolean useCachedResult = item.useCachedResult();

		assertEquals(7, ID);
		assertEquals("name", name);
		assertEquals(null, extend);
		assertEquals("javaEventHandle", javaClass);
		// Not applicable, a default style will be used.
		// assertEquals( null, styleName );
		assertEquals("1in", x.toString());
		assertEquals("1in", y.toString());
		assertEquals("1in", width.toString());
		assertEquals("1in", height.toString());
		assertEquals("onCreate", onCreate.getScriptText());
		assertEquals("onRender", onRender.getScriptText());
		assertEquals("onPageBreak", onPageBreak.getScriptText());
		assertEquals(false, useCachedResult);
	}

	public void testBookmark() throws Exception {
		Report report = loadDesign("bookmark_test.rptdesign");
		ReportItemDesign item = report.getContent(0);
		Expression bookmark = item.getBookmark();
		assertEquals(Expression.SCRIPT, bookmark.getType());
		assertEquals("bookmark-expr", bookmark.getScriptText());

		item = report.getContent(1);
		bookmark = item.getBookmark();
		assertEquals(Expression.CONSTANT, bookmark.getType());
		assertEquals("bookmark-value", bookmark.getScriptText());
	}

	public void testTOC() throws Exception {
		Report report = loadDesign("toc_test.rptdesign");
		ReportItemDesign item = report.getContent(0);
		Expression toc = item.getTOC();
		assertEquals(Expression.SCRIPT, toc.getType());
		assertEquals("toc-expr", toc.getScriptText());

		item = report.getContent(1);
		toc = item.getTOC();
		assertEquals(Expression.CONSTANT, toc.getType());
		assertEquals("toc-value", toc.getScriptText());
	}

	public void testUserProperty() throws Exception {
		Report report = loadDesign("user_property_test.rptdesign");
		Map<String, Expression> exprs = report.getUserProperties();
		assertEquals(1, exprs.size());
		assertExpression("report_expr", exprs.get("report_expr"));

		ReportItemDesign item = report.getContent(0);
		exprs = item.getUserProperties();

		assertEquals(7, exprs.size());
		assertExpression("name_expression", exprs.get("name_expr"));
		// FIXME: MODEL doens't support the constant yet
		assertExpression("name_value", exprs.get("name_value"));

		assertConstant("string", exprs.get("name_string"));
		assertConstant("1", exprs.get("name_integer"));
		assertConstant("true", exprs.get("name_boolean"));
		assertConstant("2009-06-03 00:00:00", exprs.get("name_datetime"));
		assertConstant("1.0", exprs.get("name_float"));
	}

	public void testMap() throws Exception {
		Report report = loadDesign("map_test.rptdesign");
		ReportItemDesign item = report.getContent(0);
		MapDesign map = item.getMap();
		assertEquals(4, map.getRuleCount());
		MapRuleDesign rule = map.getRule(0);

		assertEquals("value", rule.getDisplayText());
		assertEquals("value-key", rule.getDisplayKey());
		assertEquals("between", rule.getOperator());

		assertConstant("value", rule.getTestExpression());
		assertConstant("value", rule.getValue1());
		assertConstant("value", rule.getValue2());

		rule = map.getRule(1);

		assertEquals("expr", rule.getDisplayText());
		assertEquals("expr-key", rule.getDisplayKey());
		assertEquals("between", rule.getOperator());

		assertExpression("expr", rule.getTestExpression());
		assertExpression("expr", rule.getValue1());
		assertExpression("expr", rule.getValue2());

		rule = map.getRule(2);

		assertEquals("value", rule.getDisplayText());
		assertEquals("value-key", rule.getDisplayKey());
		assertEquals("in", rule.getOperator());

		assertConstant("value", rule.getTestExpression());
		List<Expression> exprs = rule.getValue1List();
		assertEquals(3, exprs.size());
		for (Expression expr : exprs) {
			assertConstant("value", expr);
		}
		assertEquals(null, rule.getValue2());

		rule = map.getRule(3);

		assertEquals("expr", rule.getDisplayText());
		assertEquals("expr-key", rule.getDisplayKey());
		assertEquals("in", rule.getOperator());

		assertExpression("expr", rule.getTestExpression());
		exprs = rule.getValue1List();
		assertEquals(3, exprs.size());
		for (Expression expr : exprs) {
			assertExpression("expr", expr);
		}
		assertEquals(null, rule.getValue2());
	}

	public void testHighlight() throws Exception {
		Report report = loadDesign("highlight_test.rptdesign");
		ReportItemDesign item = report.getContent(0);

		HighlightDesign highlight = item.getHighlight();
		assertEquals(1, highlight.getRuleCount());
		HighlightRuleDesign rule = highlight.getRule(0);

		assertEquals("between", rule.getOperator());

		Expression expr = rule.getTestExpression();
		assertEquals(Expression.SCRIPT, expr.getType());
		assertEquals("expr", expr.getScriptText());

		expr = rule.getValue1();
		assertEquals(Expression.SCRIPT, expr.getType());
		assertEquals("expr", expr.getScriptText());

		expr = rule.getValue2();
		assertEquals(Expression.SCRIPT, expr.getType());
		assertEquals("expr", expr.getScriptText());

		IStyle style = rule.getStyle();
		assertEquals(1, style.getLength());
		assertEquals("serif", style.getFontFamily());
	}

	public void testVisibility() throws Exception {

		Report report = loadDesign("visibility_test.rptdesign");

		ReportItemDesign item = report.getContent(0);
		VisibilityDesign visibility = item.getVisibility();
		assertEquals(1, visibility.count());

		VisibilityRuleDesign rule = visibility.getRule(0);
		Expression expr = rule.getExpression();

		assertEquals("all", rule.getFormat());
		assertEquals(Expression.SCRIPT, expr.getType());
		assertEquals("true", expr.getScriptText());

		item = report.getContent(1);
		visibility = item.getVisibility();
		assertEquals(1, visibility.count());
		rule = visibility.getRule(0);
		expr = rule.getExpression();

		assertEquals("all", rule.getFormat());
		assertEquals(Expression.CONSTANT, expr.getType());
		assertEquals("true", expr.getScriptText());

	}

	public void testAction() throws Exception {
		Report report = loadDesign("action_test.rptdesign");

		ActionDesign action = report.getContent(0).getAction();
		assertEquals(ActionDesign.ACTION_HYPERLINK, action.getActionType());
		assertEquals("_blank", action.getTargetWindow());
		assertExpression("uri-expr", action.getHyperlink());
		assertEquals("tooltips", action.getTooltip());
		assertEquals(null, action.getBookmark());
		assertEquals(null, action.getDrillThrough());

		action = report.getContent(1).getAction();
		assertEquals(ActionDesign.ACTION_HYPERLINK, action.getActionType());
		assertConstant("uri-value", action.getHyperlink());

		action = report.getContent(2).getAction();
		assertEquals(ActionDesign.ACTION_BOOKMARK, action.getActionType());
		assertEquals(null, action.getHyperlink());
		assertExpression("bookmark-expr", action.getBookmark());
		assertEquals(null, action.getDrillThrough());

		action = report.getContent(3).getAction();
		assertEquals(ActionDesign.ACTION_BOOKMARK, action.getActionType());
		assertConstant("bookmark-value", action.getBookmark());

		// drill-through to report design with expression
		action = report.getContent(4).getAction();
		assertEquals(ActionDesign.ACTION_DRILLTHROUGH, action.getActionType());
		assertEquals(null, action.getBookmark());
		assertEquals(null, action.getHyperlink());
		DrillThroughActionDesign drill = action.getDrillThrough();
		assertEquals("report-design", drill.getTargetFileType());
		// FIXME: should support expression
		assertConstant("design-expr", drill.getReportName());
		// assertExpression( "design-expr", drill.getReportName( ) );
		assertEquals(true, drill.getBookmarkType());
		assertExpression("bookmark-expr", drill.getBookmark());
		assertEquals("xls", drill.getFormat());
		Map<String, List<Expression>> exprs = drill.getParameters();
		assertEquals(2, exprs.size());
		assertExpression("param-expr", exprs.get("param-expr").get(0));
		assertConstant("param-value", exprs.get("param-value").get(0));

		action = report.getContent(5).getAction();
		assertEquals(ActionDesign.ACTION_DRILLTHROUGH, action.getActionType());
		drill = action.getDrillThrough();
		assertEquals("report-design", drill.getTargetFileType());
		assertConstant("design-value", drill.getReportName());
		assertEquals(false, drill.getBookmarkType());
		assertConstant("bookmark-value", drill.getBookmark());

		action = report.getContent(6).getAction();
		assertEquals(ActionDesign.ACTION_DRILLTHROUGH, action.getActionType());
		drill = action.getDrillThrough();
		assertEquals("report-document", drill.getTargetFileType());
		// FIXME: should support expression document name
		assertConstant("document-expr", drill.getReportName());
		// assertExpression( "document-expr", drill.getReportName( ) );

		action = report.getContent(7).getAction();
		assertEquals(ActionDesign.ACTION_DRILLTHROUGH, action.getActionType());
		drill = action.getDrillThrough();
		assertEquals("report-document", drill.getTargetFileType());
		assertConstant("document-value", drill.getReportName());
	}

	public void testCell() throws Exception {
		Report report = loadDesign("cell_test.rptdesign");

		GridItemDesign grid = (GridItemDesign) report.getContent(0);
		RowDesign row = grid.getRow(0);
		CellDesign cell = row.getCell(0);
		assertEquals("blue", cell.getDiagonalColor());
		assertEquals(1, cell.getDiagonalNumber());
		assertEquals("solid", cell.getDiagonalStyle());
		assertEquals("thick", cell.getDiagonalWidth().toString());
		assertEquals("red", cell.getAntidiagonalColor());
		assertEquals(1, cell.getAntidiagonalNumber());
		assertEquals("solid", cell.getAntidiagonalStyle());
		assertEquals("thick", cell.getAntidiagonalWidth().toString());

		assertEquals("colgroup", cell.getScope());
		assertExpression("bookmark-expr", cell.getBookmark());
		assertExpression("header-expr", cell.getHeaders());

		cell = row.getCell(1);
		assertConstant("bookmark-value", cell.getBookmark());
		assertConstant("header-value", cell.getHeaders());
	}

	static protected void assertConstant(String value, Expression expr) {
		assertEquals(Expression.CONSTANT, expr.getType());
		assertEquals(value, expr.getScriptText());
	}

	static protected void assertExpression(String value, Expression expr) {
		assertEquals(Expression.SCRIPT, expr.getType());
		assertEquals(value, expr.getScriptText());
	}
}
