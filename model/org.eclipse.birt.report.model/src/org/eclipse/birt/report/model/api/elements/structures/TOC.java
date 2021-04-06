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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.PropertyStructure;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * The TOC structure defines a TOC. TOC is table of content.
 */

public class TOC extends PropertyStructure {

	/**
	 * Name of this structure.
	 */

	public final static String TOC_STRUCT = "TOCStructure"; //$NON-NLS-1$

	/**
	 * TOC expression.
	 */

	public final static String TOC_EXPRESSION = "expressionValue"; //$NON-NLS-1$

	/**
	 * TOC style.
	 */

	public final static String TOC_STYLE = "TOCStyle"; //$NON-NLS-1$

	public static final String BORDER_TOP_STYLE_MEMBER = Style.BORDER_TOP_STYLE_PROP;
	public static final String BORDER_TOP_WIDTH_MEMBER = Style.BORDER_TOP_WIDTH_PROP;
	public static final String BORDER_TOP_COLOR_MEMBER = Style.BORDER_TOP_COLOR_PROP;
	public static final String BORDER_LEFT_STYLE_MEMBER = Style.BORDER_LEFT_STYLE_PROP;
	public static final String BORDER_LEFT_WIDTH_MEMBER = Style.BORDER_LEFT_WIDTH_PROP;
	public static final String BORDER_LEFT_COLOR_MEMBER = Style.BORDER_LEFT_COLOR_PROP;
	public static final String BORDER_BOTTOM_STYLE_MEMBER = Style.BORDER_BOTTOM_STYLE_PROP;
	public static final String BORDER_BOTTOM_WIDTH_MEMBER = Style.BORDER_BOTTOM_WIDTH_PROP;
	public static final String BORDER_BOTTOM_COLOR_MEMBER = Style.BORDER_BOTTOM_COLOR_PROP;
	public static final String BORDER_RIGHT_STYLE_MEMBER = Style.BORDER_RIGHT_STYLE_PROP;
	public static final String BORDER_RIGHT_WIDTH_MEMBER = Style.BORDER_RIGHT_WIDTH_PROP;
	public static final String BORDER_RIGHT_COLOR_MEMBER = Style.BORDER_RIGHT_COLOR_PROP;
	public static final String BACKGROUND_COLOR_MEMBER = Style.BACKGROUND_COLOR_PROP;
	public static final String DATE_TIME_FORMAT_MEMBER = Style.DATE_TIME_FORMAT_PROP;
	public static final String NUMBER_FORMAT_MEMBER = Style.NUMBER_FORMAT_PROP;
	public static final String NUMBER_ALIGN_MEMBER = Style.NUMBER_ALIGN_PROP;
	public static final String STRING_FORMAT_MEMBER = Style.STRING_FORMAT_PROP;
	public static final String FONT_FAMILY_MEMBER = Style.FONT_FAMILY_PROP;
	public static final String FONT_SIZE_MEMBER = Style.FONT_SIZE_PROP;
	public static final String FONT_STYLE_MEMBER = Style.FONT_STYLE_PROP;
	public static final String FONT_WEIGHT_MEMBER = Style.FONT_WEIGHT_PROP;
	public static final String FONT_VARIANT_MEMBER = Style.FONT_VARIANT_PROP;
	public static final String COLOR_MEMBER = Style.COLOR_PROP;
	public static final String TEXT_UNDERLINE_MEMBER = Style.TEXT_UNDERLINE_PROP;
	public static final String TEXT_OVERLINE_MEMBER = Style.TEXT_OVERLINE_PROP;
	public static final String TEXT_LINE_THROUGH_MEMBER = Style.TEXT_LINE_THROUGH_PROP;
	public static final String TEXT_ALIGN_MEMBER = Style.TEXT_ALIGN_PROP;
	public static final String TEXT_TRANSFORM_MEMBER = Style.TEXT_TRANSFORM_PROP;
	public static final String TEXT_INDENT_MEMBER = Style.TEXT_INDENT_PROP;
	public static final String TEXT_DIRECTION_MEMBER = Style.TEXT_DIRECTION_PROP; // bidi_hcg

	/**
	 * The reference to a style.
	 */

	private ElementRefValue style = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getHandle(org.eclipse.birt
	 * .report.model.api.SimpleValueHandle)
	 */

	public StructureHandle getHandle(SimpleValueHandle valueHandle) {
		return new TOCHandle(valueHandle.getElementHandle(), getContext());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return TOC_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		return getStringProperty(null, TOC_EXPRESSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.elements.structures.StyleRule#
	 * getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (TOC_STYLE.equals(propName))
			return style;

		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.elements.structures.StyleRule#
	 * setIntrinsicProperty(java.lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (TOC_STYLE.equals(propName)) {
			if (value instanceof String)
				style = new ElementRefValue(StringUtil.extractNamespace((String) value),
						StringUtil.extractName((String) value));
			else if (value instanceof StyleElement)
				style = new ElementRefValue(null, (Style) value);
			else
				style = (ElementRefValue) value;
		} else
			super.setIntrinsicProperty(propName, value);
	}

	/**
	 * Sets toc expression.
	 * 
	 * @param expression toc expression
	 * @throws SemanticException
	 */

	public void setExpression(String expression) throws SemanticException {
		setProperty(TOC_EXPRESSION, expression);
	}

	/**
	 * Gets toc expression.
	 * 
	 * @return toc expression
	 */

	public String getExpression() {
		return getStringProperty(null, TOC_EXPRESSION);
	}

}
