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

import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents a highlight rule that says how a data item should appear based on
 * the value within it. All highlight rules are driven by a value expression
 * defined on the style. Each rule has an expression that matches a set of
 * values, and a set of font and border instructions for how to format the data
 * item when the rule "fires."
 *  
 */

public class HighlightRule extends StyleRule
{

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

	/**
	 * Name of this structure within the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "HighlightRule"; //$NON-NLS-1$

	/**
	 * Default Constructor.
	 */

	public HighlightRule( )
	{
		super( );
	}

	/**
	 * Constructs the highlight rule with an operator and its arguments.
	 * 
	 * @param op
	 *            the supported operator. One of the internal choice values
	 *            identified in the meta-data dictionary
	 * @param v1
	 *            the comparison value expressions for operators that take one
	 *            or two arguments (equals, like, between)
	 * @param v2
	 *            the second comparison value for operators that take two
	 *            arguments (between)
	 */

	public HighlightRule( String op, String v1, String v2 )
	{
		super( op, v1, v2 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getDefn()
	 */

	public IStructureDefn getDefn( )
	{
		return MetaDataDictionary.getInstance( ).getStructure( STRUCTURE_NAME );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.model.api.SimpleValueHandle,
	 *      int)
	 */
	public StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		return new HighlightRuleHandle( valueHandle, index );
	}
}