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

package org.eclipse.birt.report.engine.emitter.html;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * Unit test for Class AttributeBuilder.
 * 
 */
public class AttributeBuilderTest extends TestCase {

	/**
	 * Test AttributeBuilder buildPos()
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>buildPos</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>it's obsolete</li>
	 * </ul>
	 * 
	 */
	public void testBuildPos() {
		DimensionType x = null;
		DimensionType y = null;
		DimensionType width = null;
		DimensionType height = null;

		assertEquals("", AttributeBuilder.buildPos(x, y, width, height));

		x = new DimensionType(1.0d, DimensionType.UNITS_MM);
		y = new DimensionType(2.0d, DimensionType.UNITS_MM);
		width = new DimensionType(3.0d, DimensionType.UNITS_MM);
		height = new DimensionType(4.0d, DimensionType.UNITS_MM);
		assertEquals("position: relative; left: 1mm; top: 2mm; width: 3mm; height: 4mm;", //$NON-NLS-1$
				AttributeBuilder.buildPos(x, y, width, height));
	}

	/**
	 * Test AttributeBuilder buildStyle()
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>buildStyle</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>the content in a style are output to a String properly</li>
	 * </ul>
	 * 
	 * @throws IOException
	 */
	public void testBuildStyle() {
		/*
		 * StyleDeclaration style = new StyleDeclaration( ); style.put(
		 * Style.BORDER_BOTTOM_COLOR_PROP, "gray" ); //$NON-NLS-1$ style.put(
		 * Style.BORDER_BOTTOM_WIDTH_PROP, "2.0pt" ); //$NON-NLS-1$ style.put(
		 * Style.BORDER_BOTTOM_STYLE_PROP, "dotted" ); //$NON-NLS-1$
		 * 
		 * style.put( Style.BACKGROUND_COLOR_PROP, "black" ); //$NON-NLS-1$
		 * 
		 * style.put( Style.FONT_FAMILY_PROP, "times new roman" ); //$NON-NLS-1$
		 * style.put( Style.COLOR_PROP, "red" ); //$NON-NLS-1$ style.put(
		 * Style.FONT_SIZE_PROP, "12.0pt" ); //$NON-NLS-1$ style.put(
		 * Style.FONT_WEIGHT_PROP, EngineIRConstants.FONT_WEIGHT_BOLD ); style.put(
		 * Style.TEXT_LINE_THROUGH_PROP, "line-through" ); //$NON-NLS-1$
		 * style.put(Style.TEXT_UNDERLINE_PROP, "underline" ); //$NON-NLS-1$
		 * 
		 * style.put( Style.PADDING_TOP_PROP, "1.0mm" ); //$NON-NLS-1$ style.put(
		 * Style.PADDING_RIGHT_PROP, "2.0mm" ); //$NON-NLS-1$ style.put(
		 * Style.PADDING_BOTTOM_PROP, "3.0mm" ); //$NON-NLS-1$ style.put(
		 * Style.PADDING_LEFT_PROP, "4.0mm" ); //$NON-NLS-1$ StringBuffer content = new
		 * StringBuffer( );
		 * 
		 * AttributeBuilder.buildStyle( content, style, null );
		 * 
		 * assertEquals(
		 * " font-family: times new roman; font-weight: bold; font-size: 12.0pt; text-decoration: line-through underline; padding-top: 1.0mm; padding-right: 2.0mm; padding-bottom: 3.0mm; padding-left: 4.0mm; border-bottom: 2.0pt dotted gray; color: red; background-color: black;"
		 * , //$NON-NLS-1$ content.toString( ) );
		 */ }
}
