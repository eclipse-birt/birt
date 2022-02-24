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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
import org.eclipse.birt.report.engine.adapter.ExpressionUtil;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * ContentUtil
 */
class ContentUtil {

	private static final Logger logger = Logger.getLogger(ContentUtil.class.getName());

	/**
	 * Prevent from instantiation
	 */
	private ContentUtil() {
	}

	static void processStyle(IExecutorContext context, IContent content, AbstractCrosstabItemHandle handle,
			IBaseResultSet evaluator, Map styleCache) throws BirtException {
		IStyle style = processStyle(context.getReportContent(), handle, evaluator, styleCache);

		if (style != null && !style.isEmpty()) {
			content.setInlineStyle(style);
		}
	}

	static IStyle processStyle(IReportContent reportContent, AbstractCrosstabItemHandle handle,
			IBaseResultSet evaluator, Map styleCache) throws BirtException {
		ReportElementHandle modelHandle = getReportElementHandle(handle);

		if (modelHandle == null || reportContent == null || evaluator == null) {
			return null;
		}

		// !!! local style is processed in reprot engine now, we only process
		// highlight here

		Iterator<HighlightRuleHandle> highlightRules = null;
		Iterator<IConditionalExpression> highlightCondExprs = null;

		if (styleCache != null) {
			Object[] cachedData = (Object[]) styleCache.get(modelHandle);

			if (cachedData != null) {
				List<HighlightRuleHandle> rules = (List<HighlightRuleHandle>) cachedData[0];
				if (rules != null) {
					highlightRules = rules.iterator();
				}

				List<IConditionalExpression> exprs = (List<IConditionalExpression>) cachedData[1];
				if (exprs != null) {
					highlightCondExprs = exprs.iterator();
				}
			} else {
				List<HighlightRuleHandle> rules = null;
				List<IConditionalExpression> exprs = null;

				StyleHandle privateStyle = modelHandle.getPrivateStyle();

				if (privateStyle != null) {
					rules = new ArrayList<HighlightRuleHandle>();
					exprs = new ArrayList<IConditionalExpression>();

					Iterator itr = privateStyle.highlightRulesIterator();

					while (itr != null && itr.hasNext()) {
						rules.add((HighlightRuleHandle) itr.next());
					}

					if (rules.isEmpty()) {
						rules = null;
					} else {
						exprs = setupHighlightExprs(rules);

						highlightRules = rules.iterator();
						highlightCondExprs = exprs.iterator();
					}
				}

				styleCache.put(modelHandle, new Object[] { rules, exprs });
			}
		} else {
			StyleHandle privateStyle = modelHandle.getPrivateStyle();

			if (privateStyle != null) {
				highlightRules = privateStyle.highlightRulesIterator();
			}
		}

		if (highlightRules == null) {
			return null;
		}

		IStyle highlightStyle = reportContent.createStyle();

		setupHighlightStyle(modelHandle, highlightRules, highlightCondExprs, highlightStyle, evaluator);

		if (!highlightStyle.isEmpty()) {
			return highlightStyle;
		}

		return null;
	}

	private static List<IConditionalExpression> setupHighlightExprs(List<HighlightRuleHandle> rules)
			throws BirtException {
		List<IConditionalExpression> exprs = new ArrayList<IConditionalExpression>();

		DataRequestSession session = DataRequestSession
				.newSession(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION));

		try {
			IModelAdapter modelAdapter = session.getModelAdaptor();

			for (HighlightRuleHandle rule : rules) {
				IConditionalExpression expression = convertHighlightExpression(rule, modelAdapter);

				exprs.add(expression);
			}
		} finally {
			session.shutdown();
		}

		return exprs;
	}

	private static IConditionalExpression convertHighlightExpression(HighlightRuleHandle rule,
			IModelAdapter modelAdapter) {
		ConditionalExpression condExpr = null;

		if (ModuleUtil.isListStyleRuleValue(rule)) {
			List<ScriptExpression> vals = null;

			List<Expression> val1list = rule.getValue1ExpressionList().getListValue();

			if (val1list != null) {
				vals = new ArrayList<ScriptExpression>();

				for (Expression expr : val1list) {
					vals.add(modelAdapter.adaptExpression(expr, ExpressionLocation.CUBE));
				}
			}

			condExpr = new ConditionalExpression(
					modelAdapter.adaptExpression(
							(Expression) rule.getExpressionProperty(HighlightRule.TEST_EXPR_MEMBER).getValue(),
							ExpressionLocation.CUBE),
					DataAdapterUtil.adaptModelFilterOperator(rule.getOperator()), vals);
		} else {
			Expression value1 = null;

			List<Expression> val1list = rule.getValue1ExpressionList().getListValue();

			if (val1list != null && val1list.size() > 0) {
				value1 = val1list.get(0);
			}

			condExpr = new ConditionalExpression(
					modelAdapter.adaptExpression(
							(Expression) rule.getExpressionProperty(HighlightRule.TEST_EXPR_MEMBER).getValue(),
							ExpressionLocation.CUBE),
					DataAdapterUtil.adaptModelFilterOperator(rule.getOperator()),
					modelAdapter.adaptExpression(value1, ExpressionLocation.CUBE),
					modelAdapter.adaptExpression(
							(Expression) rule.getExpressionProperty(StyleRule.VALUE2_MEMBER).getValue(),
							ExpressionLocation.CUBE));
		}

		return ExpressionUtil.transformConditionalExpression(condExpr);
	}

	private static void setupHighlightStyle(ReportElementHandle handle, Iterator<HighlightRuleHandle> highlightRules,
			Iterator<IConditionalExpression> cachedHighlightCondExprs, IStyle style, IBaseResultSet evaluator)
			throws BirtException {
		if (cachedHighlightCondExprs != null) {
			while (highlightRules.hasNext()) {
				HighlightRuleHandle rule = highlightRules.next();

				IConditionalExpression expression = cachedHighlightCondExprs.next();

				try {
					Object value = evaluator.evaluate(expression);

					if (value instanceof Boolean && ((Boolean) value).booleanValue()) {
						setupRuleStyle(rule, style);
					}
				} catch (BirtException e) {
					// only log a warning for invalid highlight rule processing
					logger.log(Level.WARNING, e.getLocalizedMessage(), e);
				}
			}

			return;
		}

		DataRequestSession session = DataRequestSession
				.newSession(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION));

		try {
			IModelAdapter modelAdapter = session.getModelAdaptor();

			while (highlightRules.hasNext()) {
				HighlightRuleHandle rule = highlightRules.next();

				IConditionalExpression expression = convertHighlightExpression(rule, modelAdapter);

				try {
					Object value = evaluator.evaluate(expression);

					if (value instanceof Boolean && ((Boolean) value).booleanValue()) {
						setupRuleStyle(rule, style);
					}
				} catch (BirtException e) {
					// only log a warning for invalid highlight rule processing
					logger.log(Level.WARNING, e.getLocalizedMessage(), e);
				}
			}
		} finally {
			session.shutdown();
		}
	}

	static void processVisibility(IExecutorContext context, IContent content, AbstractCrosstabItemHandle handle,
			IBaseResultSet evaluator) throws BirtException {
		String visibleFormat = processVisibility(handle, evaluator);

		if (visibleFormat != null) {
			IStyle style = content.getInlineStyle();

			if (style == null) {
				style = context.getReportContent().createStyle();
				content.setInlineStyle(style);
			}

			style.setVisibleFormat(visibleFormat);
		}

	}

	static String processVisibility(AbstractCrosstabItemHandle handle, IBaseResultSet evaluator) throws BirtException {
		ReportItemHandle modelHandle = getReportItemHandle(handle);

		if (modelHandle == null || evaluator == null) {
			return null;
		}

		Iterator visItr = modelHandle.visibilityRulesIterator();

		if (visItr != null && visItr.hasNext()) {
			StringBuffer buffer = new StringBuffer();

			while (visItr.hasNext()) {
				HideRuleHandle rule = (HideRuleHandle) visItr.next();

				String expr = validExpression(rule.getExpression());

				Object result = null;
				if (expr != null) {
					result = evaluateExpression(rule.getExpressionProperty(HideRule.VALUE_EXPR_MEMBER), evaluator);
				}

				if (result == null || !(result instanceof Boolean)) {
					continue;
				}

				boolean isHidden = ((Boolean) result).booleanValue();
				if (!isHidden) {
					continue;
				}

				// we should use rule as the string as
				buffer.append(rule.getFormat()).append(", "); //$NON-NLS-1$
			}

			int len = buffer.length();
			if (len > 2) {
				buffer.delete(len - 2, len);
			}

			return buffer.toString();
		}

		return null;
	}

	static void processScope(IExecutorContext context, ICellContent content, CrosstabCellHandle handle,
			IBaseResultSet evaluator) throws BirtException {
		if (handle == null || evaluator == null || handle.getModelHandle() == null) {
			return;
		}

		String scope = handle.getModelHandle().getStringProperty(ICellModel.SCOPE_PROP);
		if (scope != null) {
			content.setScope(scope);
		}
	}

	static void processHeaders(IExecutorContext context, ICellContent content, CrosstabCellHandle handle,
			IBaseResultSet evaluator) throws BirtException {
		if (handle == null || evaluator == null || handle.getModelHandle() == null) {
			return;
		}

		String headers = handle.getModelHandle().getStringProperty(ICellModel.HEADERS_PROP);
		if (validExpression(headers) != null) {
			Object tmp = evaluateExpression(handle.getModelHandle().getExpressionProperty(ICellModel.HEADERS_PROP),
					evaluator);
			if (tmp != null && !tmp.equals("")) //$NON-NLS-1$
			{
				content.setHeaders(tmp.toString());
			}
		}
	}

	static void processBookmark(IExecutorContext context, IContent content, AbstractCrosstabItemHandle handle,
			IBaseResultSet evaluator) throws BirtException {
		ReportItemHandle modelHandle = getReportItemHandle(handle);

		if (modelHandle == null || evaluator == null) {
			return;
		}

		String bookmark = modelHandle.getBookmark();
		if (validExpression(bookmark) != null) {
			Object tmp = evaluateExpression(modelHandle.getExpressionProperty(IReportItemModel.BOOKMARK_PROP),
					evaluator);
			if (tmp != null && !tmp.equals("")) //$NON-NLS-1$
			{
				content.setBookmark(tmp.toString());
			}
		}

		TOCHandle toc = modelHandle.getTOC();
		if (toc != null && validExpression(toc.getExpression()) != null) {
			Object tmp = evaluateExpression(toc.getExpressionProperty(TOC.TOC_EXPRESSION), evaluator);
			if (tmp != null) {
				content.setTOC(tmp);
			}
		}

	}

	static void processAction(IExecutorContext context, IContent content, AbstractCrosstabItemHandle handle) {
		// TODO no action for crosstab itself?
		return;
	}

	private static String validExpression(String expr) {
		if (expr != null && expr.trim().length() > 0) {
			return expr;
		}
		return null;
	}

	private static Object evaluateExpression(ExpressionHandle expHandle, IBaseResultSet evaluator)
			throws BirtException {
		if (expHandle != null) {
			// XXX note this method cannot process the "constant" type, need use
			// modeladapter in that case.
			return evaluator.evaluate(expHandle.getType(), expHandle.getStringExpression());
		}

		return null;
	}

	private static ReportItemHandle getReportItemHandle(AbstractCrosstabItemHandle handle) {
		if (handle != null && handle.getModelHandle() instanceof ReportItemHandle) {
			return (ReportItemHandle) handle.getModelHandle();
		}
		return null;
	}

	private static ReportElementHandle getReportElementHandle(AbstractCrosstabItemHandle handle) {
		if (handle != null && handle.getModelHandle() instanceof ReportElementHandle) {
			return (ReportElementHandle) handle.getModelHandle();
		}
		return null;
	}

	// private static void setupPrivateStyle( ReportElementHandle handle,
	// IStyle style )
	// {
	// // Background
	// style.setBackgroundColor( getElementProperty( handle,
	// Style.BACKGROUND_COLOR_PROP,
	// true ) );
	// style.setBackgroundImage( getElementProperty( handle,
	// Style.BACKGROUND_IMAGE_PROP ) );
	// style.setBackgroundPositionX( getElementProperty( handle,
	// Style.BACKGROUND_POSITION_X_PROP ) );
	// style.setBackgroundPositionY( getElementProperty( handle,
	// Style.BACKGROUND_POSITION_Y_PROP ) );
	// style.setBackgroundRepeat( getElementProperty( handle,
	// Style.BACKGROUND_REPEAT_PROP ) );
	//
	// // Text related
	// style.setTextAlign( getElementProperty( handle, Style.TEXT_ALIGN_PROP )
	// );
	// style.setTextIndent( getElementProperty( handle, Style.TEXT_INDENT_PROP )
	// );
	//
	// style.setTextUnderline( getElementProperty( handle,
	// Style.TEXT_UNDERLINE_PROP ) );
	//
	// style.setTextLineThrough( getElementProperty( handle,
	// Style.TEXT_LINE_THROUGH_PROP ) );
	// style.setTextOverline( getElementProperty( handle,
	// Style.TEXT_OVERLINE_PROP ) );
	//
	// style.setLetterSpacing( getElementProperty( handle,
	// Style.LETTER_SPACING_PROP ) );
	// style.setLineHeight( getElementProperty( handle, Style.LINE_HEIGHT_PROP )
	// );
	// style.setOrphans( getElementProperty( handle, Style.ORPHANS_PROP ) );
	// style.setTextTransform( getElementProperty( handle,
	// Style.TEXT_TRANSFORM_PROP ) );
	// style.setVerticalAlign( getElementProperty( handle,
	// Style.VERTICAL_ALIGN_PROP ) );
	// style.setWhiteSpace( getElementProperty( handle, Style.WHITE_SPACE_PROP )
	// );
	// style.setWidows( getElementProperty( handle, Style.WIDOWS_PROP ) );
	// style.setWordSpacing( getElementProperty( handle,
	// Style.WORD_SPACING_PROP ) );
	//
	// // Section properties
	// style.setDisplay( getElementProperty( handle, Style.DISPLAY_PROP ) );
	// style.setMasterPage( getElementProperty( handle, Style.MASTER_PAGE_PROP )
	// );
	// String pageBreakAfter = getElementProperty( handle,
	// StyleHandle.PAGE_BREAK_AFTER_PROP );
	// style.setPageBreakAfter( decodePageBreak( pageBreakAfter ) );
	// String pageBreakBefore = getElementProperty( handle,
	// StyleHandle.PAGE_BREAK_BEFORE_PROP );
	// style.setPageBreakBefore( decodePageBreak( pageBreakBefore ) );
	//
	// style.setPageBreakInside( getElementProperty( handle,
	// Style.PAGE_BREAK_INSIDE_PROP ) );
	//
	// // Font related
	// style.setFontFamily( getElementProperty( handle, Style.FONT_FAMILY_PROP )
	// );
	// style.setColor( getElementProperty( handle, Style.COLOR_PROP, true ) );
	// style.setFontSize( getElementProperty( handle, Style.FONT_SIZE_PROP ) );
	// style.setFontStyle( getElementProperty( handle, Style.FONT_STYLE_PROP )
	// );
	// style.setFontWeight( getElementProperty( handle, Style.FONT_WEIGHT_PROP )
	// );
	// style.setFontVariant( getElementProperty( handle,
	// Style.FONT_VARIANT_PROP ) );
	//
	// // Border
	// style.setBorderBottomColor( getElementProperty( handle,
	// Style.BORDER_BOTTOM_COLOR_PROP,
	// true ) );
	// style.setBorderBottomStyle( getElementProperty( handle,
	// Style.BORDER_BOTTOM_STYLE_PROP ) );
	// style.setBorderBottomWidth( getElementProperty( handle,
	// Style.BORDER_BOTTOM_WIDTH_PROP ) );
	// style.setBorderLeftColor( getElementProperty( handle,
	// Style.BORDER_LEFT_COLOR_PROP,
	// true ) );
	// style.setBorderLeftStyle( getElementProperty( handle,
	// Style.BORDER_LEFT_STYLE_PROP ) );
	// style.setBorderLeftWidth( getElementProperty( handle,
	// Style.BORDER_LEFT_WIDTH_PROP ) );
	// style.setBorderRightColor( getElementProperty( handle,
	// Style.BORDER_RIGHT_COLOR_PROP,
	// true ) );
	// style.setBorderRightStyle( getElementProperty( handle,
	// Style.BORDER_RIGHT_STYLE_PROP ) );
	// style.setBorderRightWidth( getElementProperty( handle,
	// Style.BORDER_RIGHT_WIDTH_PROP ) );
	// style.setBorderTopColor( getElementProperty( handle,
	// Style.BORDER_TOP_COLOR_PROP,
	// true ) );
	// style.setBorderTopStyle( getElementProperty( handle,
	// Style.BORDER_TOP_STYLE_PROP ) );
	// style.setBorderTopWidth( getElementProperty( handle,
	// Style.BORDER_TOP_WIDTH_PROP ) );
	//
	// // Margin
	// style.setMarginTop( getElementProperty( handle, Style.MARGIN_TOP_PROP )
	// );
	// style.setMarginLeft( getElementProperty( handle, Style.MARGIN_LEFT_PROP )
	// );
	// style.setMarginBottom( getElementProperty( handle,
	// Style.MARGIN_BOTTOM_PROP ) );
	// style.setMarginRight( getElementProperty( handle,
	// Style.MARGIN_RIGHT_PROP ) );
	//
	// // Padding
	// style.setPaddingTop( getElementProperty( handle, Style.PADDING_TOP_PROP )
	// );
	// style.setPaddingLeft( getElementProperty( handle,
	// Style.PADDING_LEFT_PROP ) );
	// style.setPaddingBottom( getElementProperty( handle,
	// Style.PADDING_BOTTOM_PROP ) );
	// style.setPaddingRight( getElementProperty( handle,
	// Style.PADDING_RIGHT_PROP ) );
	//
	// // Data Formatting
	// style.setNumberAlign( getElementProperty( handle,
	// Style.NUMBER_ALIGN_PROP ) );
	// style.setDateFormat( getElementProperty( handle,
	// Style.DATE_TIME_FORMAT_PROP ) );
	// style.setNumberFormat( getElementProperty( handle,
	// Style.NUMBER_FORMAT_PROP ) );
	// style.setStringFormat( getElementProperty( handle,
	// Style.STRING_FORMAT_PROP ) );
	//
	// // Others
	// style.setCanShrink( getElementProperty( handle, Style.CAN_SHRINK_PROP )
	// );
	// style.setShowIfBlank( getElementProperty( handle,
	// Style.SHOW_IF_BLANK_PROP ) );
	// }

	private static IStyle setupRuleStyle(StructureHandle highlight, IStyle style) {
		String value;

		// Background
		value = getMemberProperty(highlight, HighlightRule.BACKGROUND_COLOR_MEMBER);
		if (value != null) {
			style.setBackgroundColor(value);
		}
		// style.setBackgroundPositionX(getMemberProperty(highlight,
		// HighlightRule.BACKGROUND_POSITION_X_MEMBER));
		// style.setBackgroundPositionY(getMemberProperty(highlight,
		// HighlightRule.BACKGROUND_POSITION_Y_MEMBER));
		// style.setBackgroundRepeat(getMemberProperty(highlight,
		// HighlightRule.BACKGROUND_REPEAT_MEMBER));

		// Text related
		value = getMemberProperty(highlight, HighlightRule.TEXT_ALIGN_MEMBER);
		if (value != null) {
			style.setTextAlign(value);
		}
		value = getMemberProperty(highlight, HighlightRule.TEXT_INDENT_MEMBER);
		if (value != null) {
			style.setTextIndent(value);
		}
		value = getMemberProperty(highlight, Style.TEXT_UNDERLINE_PROP);
		if (value != null) {
			style.setTextUnderline(value);
		}
		value = getMemberProperty(highlight, Style.TEXT_LINE_THROUGH_PROP);
		if (value != null) {
			style.setTextLineThrough(value);
		}
		value = getMemberProperty(highlight, Style.TEXT_OVERLINE_PROP);
		if (value != null) {
			style.setTextOverline(value);
		}
		// style.setLetterSpacing(getMemberProperty(highlight,
		// HighlightRule.LETTER_SPACING_MEMBER));
		// style.setLineHeight(getMemberProperty(highlight,
		// HighlightRule.LINE_HEIGHT_MEMBER));
		// style.setOrphans(getMemberProperty(highlight,
		// HighlightRule.ORPHANS_MEMBER));
		value = getMemberProperty(highlight, HighlightRule.TEXT_TRANSFORM_MEMBER);
		if (value != null) {
			style.setTextTransform(value);
		}
		// style.setVerticalAlign(getMemberProperty(highlight,
		// HighlightRule.VERTICAL_ALIGN_MEMBER));
		// style.setWhiteSpace(getMemberProperty(highlight,
		// HighlightRule.WHITE_SPACE_MEMBER));
		// style.setWidows(getMemberProperty(highlight,
		// HighlightRule.WIDOWS_MEMBER));
		// style.setWordSpacing(getMemberProperty(highlight,
		// HighlightRule.WORD_SPACING_MEMBER));

		// Section properties
		// style.setDisplay(getMemberProperty(highlight,
		// HighlightRule.DISPLAY_MEMBER));
		// style.setMasterPage(getMemberProperty(highlight,
		// HighlightRule.MASTER_PAGE_MEMBER));
		// style.setPageBreakAfter(getMemberProperty(highlight,
		// HighlightRule.PAGE_BREAK_AFTER_MEMBER));
		// style.setPageBreakBefore(getMemberProperty(highlight,
		// HighlightRule.PAGE_BREAK_BEFORE_MEMBER));
		// style.setPageBreakInside(getMemberProperty(highlight,
		// HighlightRule.PAGE_BREAK_INSIDE_MEMBER));

		// Font related
		value = getMemberProperty(highlight, HighlightRule.FONT_FAMILY_MEMBER);
		if (value != null) {
			style.setFontFamily(value);
		}
		value = getMemberProperty(highlight, HighlightRule.COLOR_MEMBER);
		if (value != null) {
			style.setColor(value);
		}
		value = getMemberProperty(highlight, HighlightRule.FONT_SIZE_MEMBER);
		if (value != null) {
			style.setFontSize(value);
		}
		value = getMemberProperty(highlight, HighlightRule.FONT_STYLE_MEMBER);
		if (value != null) {
			style.setFontStyle(value);
		}
		value = getMemberProperty(highlight, HighlightRule.FONT_WEIGHT_MEMBER);
		if (value != null) {
			style.setFontWeight(value);
		}
		value = getMemberProperty(highlight, HighlightRule.FONT_VARIANT_MEMBER);
		if (value != null) {
			style.setFontVariant(value);
		}

		// Border
		value = getMemberProperty(highlight, HighlightRule.BORDER_BOTTOM_COLOR_MEMBER);
		if (value != null) {
			style.setBorderBottomColor(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_BOTTOM_STYLE_MEMBER);
		if (value != null) {
			style.setBorderBottomStyle(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_BOTTOM_WIDTH_MEMBER);
		if (value != null) {
			style.setBorderBottomWidth(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_LEFT_COLOR_MEMBER);
		if (value != null) {
			style.setBorderLeftColor(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_LEFT_STYLE_MEMBER);
		if (value != null) {
			style.setBorderLeftStyle(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_LEFT_WIDTH_MEMBER);
		if (value != null) {
			style.setBorderLeftWidth(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_RIGHT_COLOR_MEMBER);
		if (value != null) {
			style.setBorderRightColor(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_RIGHT_STYLE_MEMBER);
		if (value != null) {
			style.setBorderRightStyle(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_RIGHT_WIDTH_MEMBER);
		if (value != null) {
			style.setBorderRightWidth(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_TOP_COLOR_MEMBER);
		if (value != null) {
			style.setBorderTopColor(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_TOP_STYLE_MEMBER);
		if (value != null) {
			style.setBorderTopStyle(value);
		}
		value = getMemberProperty(highlight, HighlightRule.BORDER_TOP_WIDTH_MEMBER);
		if (value != null) {
			style.setBorderTopWidth(value);
		}

		// Margin
		// style.setMarginTop(getMemberProperty(highlight,
		// HighlightRule.MARGIN_TOP_MEMBER));
		// style.setMarginLeft(getMemberProperty(highlight,
		// HighlightRule.MARGIN_LEFT_MEMBER));
		// style.setMarginBottom(getMemberProperty(highlight,
		// HighlightRule.MARGIN_BOTTOM_MEMBER));
		// style.setMarginRight(getMemberProperty(highlight,
		// HighlightRule.MARGIN_RIGHT_MEMBER));

		// Padding
		// style.setPaddingTop(getMemberProperty(highlight,
		// HighlightRule.PADDING_TOP_MEMBER));
		// style.setPaddingLeft(getMemberProperty(highlight,
		// HighlightRule.PADDING_LEFT_MEMBER));
		// style.setPaddingBottom(getMemberProperty(highlight,
		// HighlightRule.PADDING_BOTTOM_MEMBER));
		// style.setPaddingRight(getMemberProperty(highlight,
		// HighlightRule.PADDING_RIGHT_MEMBER));

		// Data Formatting
		value = getMemberProperty(highlight, HighlightRule.NUMBER_ALIGN_MEMBER);
		if (value != null) {
			style.setNumberAlign(value);
		}
		value = getMemberProperty(highlight, HighlightRule.DATE_TIME_FORMAT_MEMBER);
		if (value != null) {
			style.setDateFormat(value);
		}
		value = getMemberProperty(highlight, HighlightRule.NUMBER_FORMAT_MEMBER);
		if (value != null) {
			style.setNumberFormat(value);
		}
		value = getMemberProperty(highlight, HighlightRule.STRING_FORMAT_MEMBER);
		if (value != null) {
			style.setStringFormat(value);
		}

		// Others
		// style.setCanShrink(getMemberProperty(highlight,
		// HighlightRule.CAN_SHRINK_MEMBER));
		// style.setShowIfBlank(getMemberProperty(highlight,
		// HighlightRule.SHOW_IF_BLANK_MEMBER));
		// bidi_hcg Bidi related
		value = getMemberProperty(highlight, Style.TEXT_DIRECTION_PROP);
		if (value != null) {
			style.setDirection(value);
		}
		return style;
	}

	// private static String decodePageBreak( String pageBreak )
	// {
	// if ( pageBreak == null )
	// {
	// return null;
	// }
	// if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals( pageBreak ) )
	// {
	// return IStyle.CSS_ALWAYS_VALUE;
	// }
	// if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST.equals(
	// pageBreak ) )
	// {
	// return IStyle.CSS_ALWAYS_VALUE;
	// }
	// if ( DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO.equals( pageBreak ) )
	// {
	// return IStyle.CSS_AUTO_VALUE;
	// }
	// if ( DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID.equals( pageBreak ) )
	// {
	// return IStyle.CSS_AVOID_VALUE;
	// }
	// if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals( pageBreak ) )
	// {
	// return IStyle.CSS_ALWAYS_VALUE;
	// }
	// if (
	// DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST.equals(
	// pageBreak ) )
	// {
	// return IStyle.CSS_ALWAYS_VALUE;
	// }
	// if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_AUTO.equals( pageBreak ) )
	// {
	// return IStyle.CSS_AUTO_VALUE;
	// }
	// if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_AVOID.equals( pageBreak ) )
	// {
	// return IStyle.CSS_AVOID_VALUE;
	// }
	// return IStyle.CSS_AUTO_VALUE;
	// }

	// private static String getElementProperty( ReportElementHandle handle,
	// String name )
	// {
	// return getElementProperty( handle, name, false );
	// }

	// private static String getElementProperty( ReportElementHandle handle,
	// String name, boolean isColorProperty )
	// {
	// FactoryPropertyHandle prop = handle.getFactoryPropertyHandle( name );
	// if ( prop != null && prop.isSet( ) )
	// {
	// if ( isColorProperty )
	// {
	// return prop.getColorValue( );
	// }
	//
	// return prop.getStringValue( );
	// }
	// return null;
	// }

	private static String getMemberProperty(StructureHandle handle, String name) {
		MemberHandle prop = handle.getMember(name);
		if (prop != null) {
			Object value = prop.getContext().getLocalValue(handle.getModule());
			if (value != null) {
				return prop.getStringValue();
			}

			// for highlight rule, reutrn the referred style local value
			if (handle instanceof HighlightRuleHandle) {
				StyleHandle styleHandle = ((HighlightRuleHandle) handle).getStyle();
				if (styleHandle != null) {
					FactoryPropertyHandle propHandle = styleHandle.getFactoryPropertyHandle(name);
					if (propHandle != null) {
						return propHandle.getStringValue();
					}
				}
			}
		}
		return null;
	}

	static DimensionType createDimension(DimensionHandle handle) {
		if (handle == null || !handle.isSet()) {
			return null;
		}

		// Extended Choice
		if (handle.isKeyword()) {
			return new DimensionType(handle.getStringValue());
		}

		// set measure and unit
		double measure = handle.getMeasure();
		String unit = handle.getUnits();
		return new DimensionType(measure, unit);
	}

}
