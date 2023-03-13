/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public abstract class Layout {

	protected ContainerLayout parent;

	protected LayoutEngineContext context;

	protected IContent content;

	protected int specifiedWidth;

	protected int specifiedHeight;

	protected static Logger logger = Logger.getLogger(Layout.class.getName());

	public Layout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		this.context = context;
		this.parent = parent;
		this.content = content;
	}

	public abstract void layout() throws BirtException;

	protected abstract void initialize() throws BirtException;

	/**
	 * end current area if it is the last area of content, add bottom box property
	 *
	 * @throws BirtException
	 *
	 */
	protected abstract void closeLayout() throws BirtException;

	protected void calculateSpecifiedWidth() {
		if (content != null) {
			if (parent != null) {
				specifiedWidth = getDimensionValue(content.getWidth(), parent.getCurrentMaxContentWidth());
			} else {
				specifiedWidth = getDimensionValue(content.getWidth());
			}
		}
	}

	protected void calculateSpecifiedHeight() {
		if (content != null) {
			specifiedHeight = getDimensionValue(content.getHeight());
		}
	}

	protected int getDimensionValue(CSSValue value) {
		return getDimensionValue(value, 0);
	}

	protected void validateBoxProperty(IContent content, IStyle style, int maxWidth, int maxHeight)

	{
		if (content == null) {
			return;
		}
		IStyle contentStyle = content.getStyle();
		if (contentStyle == null || contentStyle.isEmpty()) {
			return;
		}
		validateBoxProperty(style, maxWidth, maxHeight);
	}

	protected void validateBoxProperty(IStyle style, int maxWidth, int maxHeight) {
		// support negative margin
		int leftMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_LEFT), maxWidth);
		int rightMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_RIGHT), maxWidth);
		int topMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_TOP), maxWidth);
		int bottomMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_BOTTOM), maxWidth);

		// do not support negative paddding
		int leftPadding = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_LEFT), maxWidth));
		int rightPadding = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_RIGHT), maxWidth));
		int topPadding = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_TOP), maxWidth));
		int bottomPadding = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_BOTTOM), maxWidth));
		// border does not support negative value, do not support pencentage
		// dimension
		int leftBorder = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_LEFT_WIDTH), 0));
		int rightBorder = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH), 0));
		int topBorder = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_TOP_WIDTH), 0));
		int bottomBorder = Math.max(0, getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH), 0));

		int[] vs = { rightMargin, leftMargin, rightPadding, leftPadding, rightBorder, leftBorder };
		resolveBoxConflict(vs, maxWidth);

		int[] hs = { bottomMargin, topMargin, bottomPadding, topPadding, bottomBorder, topBorder };
		// resolveBoxConflict( hs, maxHeight );

		style.setProperty(IStyle.STYLE_MARGIN_LEFT, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[1]));
		style.setProperty(IStyle.STYLE_MARGIN_RIGHT, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[0]));
		style.setProperty(IStyle.STYLE_MARGIN_TOP, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[1]));
		style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[0]));

		style.setProperty(IStyle.STYLE_PADDING_LEFT, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[3]));
		style.setProperty(IStyle.STYLE_PADDING_RIGHT, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[2]));
		style.setProperty(IStyle.STYLE_PADDING_TOP, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[3]));
		style.setProperty(IStyle.STYLE_PADDING_BOTTOM, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[2]));

		style.setProperty(IStyle.STYLE_BORDER_LEFT_WIDTH, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[5]));
		style.setProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[4]));
		style.setProperty(IStyle.STYLE_BORDER_TOP_WIDTH, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[5]));
		style.setProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[4]));
	}

	private void resolveConflict(int[] values, int maxTotal, int total, int start) {
		int length = values.length - start;
		if (length == 0) {
			return;
		}
		assert (length > 0);
		if (total > maxTotal) {
			int othersTotal = total - values[start];
			if (values[start] > 0) {
				values[start] = 0;
			}
			resolveConflict(values, maxTotal, othersTotal, start + 1);
		}
	}

	protected int getDimensionValue(String d) {

		if (d == null) {
			return 0;
		}
		try {
			if (d.endsWith("in") || d.endsWith("in")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return (int) ((Float.parseFloat(d.substring(0, d.length() - 2))) * 72000.0f);
			} else if (d.endsWith("cm") || d.endsWith("CM")) //$NON-NLS-1$//$NON-NLS-2$
			{
				return (int) ((Float.parseFloat(d.substring(0, d.length() - 2))) * 72000.0f / 2.54f);
			} else if (d.endsWith("mm") || d.endsWith("MM")) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return (int) ((Float.parseFloat(d.substring(0, d.length() - 2))) * 7200.0f / 2.54f);
			} else if (d.endsWith("px") || d.endsWith("PX")) //$NON-NLS-1$//$NON-NLS-2$
			{
				return (int) ((Float.parseFloat(d.substring(0, d.length() - 2))) / 96.0f * 72000.0f);// set
				// as
				// 96dpi
			} else {
				return (int) ((Float.parseFloat(d)));
			}
		} catch (NumberFormatException ex) {
			logger.log(Level.WARNING, ex.getLocalizedMessage());
			return 0;
		}
	}

	protected int getDimensionValue(DimensionType d) {
		return getDimensionValue(d, 0);
	}

	protected int getDimensionValue(DimensionType d, int referenceLength) {
		return getDimensionValue(d, 0, referenceLength);
	}

	protected int getDimensionValue(DimensionType d, int dpi, int referenceLength) {
		if (d == null) {
			return 0;
		}
		try {
			String units = d.getUnits();
			if (units.equals(EngineIRConstants.UNITS_PT) || units.equals(EngineIRConstants.UNITS_CM)
					|| units.equals(EngineIRConstants.UNITS_MM) || units.equals(EngineIRConstants.UNITS_PC)
					|| units.equals(EngineIRConstants.UNITS_IN)) {
				double point = d.convertTo(EngineIRConstants.UNITS_PT) * 1000;
				return (int) point;
			} else if (units.equals(EngineIRConstants.UNITS_PX)) {
				if (dpi == 0) {
					dpi = getResolution();
				}
				double point = d.getMeasure() / dpi * 72000d;
				return (int) point;
			} else if (units.equals(EngineIRConstants.UNITS_PERCENTAGE)) {
				double point = referenceLength * d.getMeasure() / 100.0;
				return (int) point;
			} else if (units.equals(EngineIRConstants.UNITS_EM) || units.equals(EngineIRConstants.UNITS_EX)) {
				int size = 9000;
				if (content != null) {
					IStyle style = content.getComputedStyle();
					CSSValue fontSize = style.getProperty(IStyle.STYLE_FONT_SIZE);
					size = getDimensionValue(fontSize);
				}
				double point = size * d.getMeasure();
				return (int) point;
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
			return 0;
		}
		return 0;
	}

	protected int getResolution() {
		int resolution;
		ReportDesignHandle designHandle = content.getReportContent().getDesign().getReportDesign();
		resolution = designHandle.getImageDPI();

		if (0 == resolution) {
			resolution = context.getDpi();
		}
		if (0 == resolution) {
			resolution = 96;
		}
		return resolution;
	}

	protected void resolveBoxConflict(int[] vs, int max) {
		int vTotal = 0;
		for (int i = 0; i < vs.length; i++) {
			vTotal += vs[i];
		}
		resolveConflict(vs, max, vTotal, 0);
	}

	protected int getDimensionValue(CSSValue value, int referenceLength) {
		if (value != null && (value instanceof FloatValue)) {
			FloatValue fv = (FloatValue) value;
			float v = fv.getFloatValue();
			switch (fv.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_CM:
				return (int) (v * 72000 / 2.54);

			case CSSPrimitiveValue.CSS_IN:
				return (int) (v * 72000);

			case CSSPrimitiveValue.CSS_MM:
				return (int) (v * 7200 / 2.54);

			case CSSPrimitiveValue.CSS_PT:
				return (int) (v * 1000);
			case CSSPrimitiveValue.CSS_NUMBER:
				return (int) v;
			case CSSPrimitiveValue.CSS_PERCENTAGE:

				return (int) (referenceLength * v / 100.0);
			}
		}
		return 0;
	}

	protected TableLayout getTableLayoutManager() {
		ContainerLayout lm = parent;
		while (lm != null && !(lm instanceof TableLayout)) {
			lm = lm.getParent();
		}
		if (lm == null) {
			assert (false);
		}
		return (TableLayout) lm;
	}

	public ContainerLayout getParent() {
		return parent;
	}

	protected void removeMargin(IStyle style) {
		if (style != null) {
			style.setProperty(IStyle.STYLE_MARGIN_LEFT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_RIGHT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0);
		}
	}

	protected void visitContent(IContent content, IContentEmitter emitter) throws BirtException {
		ContentEmitterUtil.startContent(content, emitter);
		java.util.Collection children = content.getChildren();
		if (children != null && !children.isEmpty()) {
			Iterator iter = children.iterator();
			while (iter.hasNext()) {
				IContent child = (IContent) iter.next();
				visitContent(child, emitter);
			}
		}
		ContentEmitterUtil.endContent(content, emitter);
	}

}
