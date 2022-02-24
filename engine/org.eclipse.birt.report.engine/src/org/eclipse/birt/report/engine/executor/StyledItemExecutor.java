/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.report.engine.adapter.ExpressionUtil;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;

/**
 * Defines an abstract base class for all styled element executors, including
 * <code>DataItemExecutor</code>,<code>TextItemExecutor</code>, etc.. The class
 * provides methods for style manipulation, such as applying highlight and
 * mapping rules, calculating flattened (merged) styles, and so on.
 *
 */
public abstract class StyledItemExecutor extends ReportItemExecutor {

	private ExpressionUtil expressionUtil;

	/**
	 * constructor
	 *
	 * @param visitor the report executor visitor
	 */
	protected StyledItemExecutor(ExecutorManager manager, int type) {
		super(manager, type);
		expressionUtil = new ExpressionUtil();
	}

	/**
	 * Gets the style from the original design object, calculates the highlight
	 * style, merges the teo styles and then sets them on the corresponding content
	 * object.
	 *
	 * @param content the target content object.
	 * @param design  the original design object.
	 */
	protected void processStyle(ReportItemDesign design, IContent content) {
		HighlightDesign highlight = design.getHighlight();
		StyleDeclaration inlineStyle = null;
		if (highlight != null) {
			inlineStyle = createHighlightStyle(design.getHighlight());
		}
		Map<Integer, Expression> expressionStyles = design.getExpressionStyles();
		if (expressionStyles != null) {
			if (inlineStyle == null) {
				inlineStyle = (StyleDeclaration) report.createStyle();
			}
			populateExpressionStyles(inlineStyle, expressionStyles);
		}
		if (inlineStyle != null) {
			content.setInlineStyle(inlineStyle);
		}
	}

	private void populateExpressionStyles(StyleDeclaration style, Map<Integer, Expression> expressionStyles) {
		Set<Entry<Integer, Expression>> entrySet = expressionStyles.entrySet();
		for (Entry<Integer, Expression> entry : entrySet) {
			Expression expression = entry.getValue();
			int propertyIndex = entry.getKey();
			if (expression != null) {
				String value = evaluateString(expression);
				style.setCssText(propertyIndex, value);
			}
		}
	}

	/**
	 * Gets the style from the original column design object, calculates the
	 * highlight style, merges the teo styles and then sets them on the
	 * corresponding column object.
	 *
	 * @param column       the target column object.
	 * @param columnDesign the original column design object.
	 */
	protected void processColumnStyle(ColumnDesign columnDesign, IColumn column) {
		HighlightDesign highlight = columnDesign.getHighlight();
		if (highlight != null) {
			StyleDeclaration inlineStyle = createHighlightStyle(highlight);
			if (inlineStyle != null) {
				column.setInlineStyle(inlineStyle);
			}
		}
	}

	/**
	 * Get the highlight style.
	 *
	 * @param style          The style with highlight.
	 * @param defaultTestExp the test expression
	 * @return The highlight style.
	 */
	private StyleDeclaration createHighlightStyle(HighlightDesign highlight) {
		StyleDeclaration style = (StyleDeclaration) report.createStyle();
		for (int i = 0; i < highlight.getRuleCount(); i++) {
			HighlightRuleDesign rule = highlight.getRule(i);
			if (rule != null) {
				Expression expression = rule.getConditionExpr();
				if (expression == null) {
					IConditionalExpression condExpr = null;
					if (rule.ifValueIsList()) {
						condExpr = expressionUtil.createConditionExpression(rule.getTestExpression(),
								rule.getOperator(), rule.getValue1List());
					} else {
						condExpr = expressionUtil.createConditionalExpression(rule.getTestExpression(),
								rule.getOperator(), rule.getValue1(), rule.getValue2());
					}
					expression = Expression.newConditional(condExpr);
					rule.setConditionExpr(expression);
				}
				Boolean value = evaluateBoolean(expression);
				if ((value != null) && value.booleanValue()) {
					style.setProperties(rule.getStyle());
				}
			}
		}

		return style;
	}

	/**
	 * process the mapped rules.
	 *
	 * @param item    the design element used to create the data obj.
	 * @param dataObj Data object.
	 * @throws BirtException
	 */
	protected void processMappingValue(StyledElementDesign item, IDataContent dataObj) {
		MapDesign map = item.getMap();
		if (map == null) {
			return;
		}

		for (int i = 0; i < map.getRuleCount(); i++) {
			MapRuleDesign rule = map.getRule(i);
			if (rule != null) {
				Expression expression = rule.getConditionExpr();
				if (expression == null) {
					IConditionalExpression condExpr = null;
					if (rule.ifValueIsList()) {
						condExpr = expressionUtil.createConditionExpression(rule.getTestExpression(),
								rule.getOperator(), rule.getValue1List());
					} else {
						condExpr = expressionUtil.createConditionalExpression(rule.getTestExpression(),
								rule.getOperator(), rule.getValue1(), rule.getValue2());
					}
					expression = Expression.newConditional(condExpr);
					rule.setConditionExpr(expression);
				}

				Boolean value = evaluateBoolean(expression);
				if (value != null && value.booleanValue()) {
					dataObj.setLabelText(rule.getDisplayText());
					dataObj.setLabelKey(rule.getDisplayKey());
				}
			}
		}
	}

}
