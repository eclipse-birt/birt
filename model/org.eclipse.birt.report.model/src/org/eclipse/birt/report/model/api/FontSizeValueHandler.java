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

package org.eclipse.birt.report.model.api;

import java.util.Stack;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Represents the handler which processes the font size computation as CSS
 * specification describes.
 */

final class FontSizeValueHandler extends CSSLengthValueHandler {

	/**
	 * Default constructor.
	 * 
	 * @param dimensionHandle
	 */

	FontSizeValueHandler(DimensionHandle dimensionHandle) {
		super(dimensionHandle);
	}

	/**
	 * Converts one font size constant to dimension value.
	 * 
	 * @param fontSizeConstant the font size constant to convert, relative or
	 *                         absolute.
	 * @return the converted dimension value. Return <code>null</code> if the given
	 *         <code>fontSizeConstant</code> is invalid.
	 */

	DimensionValue convertFontSizeConstant(String fontSizeConstant) {
		if (DimensionUtil.isAbsoluteFontSize(fontSizeConstant)) {
			return provider.getValueOf(fontSizeConstant);
		} else if (DimensionUtil.isRelativeFontSize(fontSizeConstant)) {
			return getAbsoluteFontSizeValue();
		}

		return null;
	}

	/**
	 * Tries to get an absolute font size constant. This method works only when the
	 * value is relative font size constant, otherwise, return null.
	 * 
	 * @return the absolute font size dimension value if possible
	 */

	private DimensionValue getAbsoluteFontSizeValue() {
		Stack stack = new Stack();

		boolean absoluteConstantFound = false;

		// Cache the relative / absolute font size constant in stack.

		DesignElementHandle e = dimensionHandle.getElementHandle();
		while (e != null) {
			DimensionHandle fontSizeHandle = e.getDimensionProperty(IStyleModel.FONT_SIZE_PROP);
			if (fontSizeHandle == null)
				break;

			Object value = fontSizeHandle.getValue();
			if (value instanceof DimensionValue)
				return (DimensionValue) value;

			String fontSizeConstant = (String) value;

			stack.push(fontSizeConstant);
			if (DimensionUtil.isAbsoluteFontSize(fontSizeConstant)) {
				absoluteConstantFound = true;
				break;
			}

			e = e.getContainer();
		}

		// Use default value if no absolute font size constant is found.

		if (!absoluteConstantFound) {
			Object defaultValue = dimensionHandle.getDefn().getDefault();
			if (defaultValue instanceof DimensionValue)
				return (DimensionValue) defaultValue;

			stack.push(defaultValue);
		}

		// The stack has at least two items.

		assert !stack.isEmpty();

		// Compute the absolute font size constant with the relative ones kept
		// in stack.

		String absoluteFontSizeConstant = (String) stack.pop();
		while (!stack.isEmpty()) {
			String constant = (String) stack.pop();

			absoluteFontSizeConstant = convertRelativeConstantToAbsolute(constant, absoluteFontSizeConstant);
		}

		return provider.getValueOf(absoluteFontSizeConstant);
	}

	/**
	 * Converts one relative font size constant to the absolute one based on the
	 * given absolute one. The sequence for conversion is:
	 * <ul>
	 * <li><code>FONT_SIZE_XX_SMALL</code>
	 * <li><code>FONT_SIZE_X_SMALL</code>
	 * <li><code>FONT_SIZE_SMALL</code>
	 * <li><code>FONT_SIZE_MEDIUM</code>
	 * <li><code>FONT_SIZE_LARGE</code>
	 * <li><code>FONT_SIZE_X_LARGE</code>
	 * <li><code>FONT_SIZE_XX_LARGE</code>
	 * </ul>
	 * 
	 * <p>
	 * For example,
	 * <p>
	 * <ul>
	 * <li>The absolute base font size constant is "MEDIUM", and the relative one
	 * "LARGER" is converted to "LARGE".
	 * <li>The absolute base font size constant is "XX_SMALL", and the relative one
	 * "SMALLER" is converted to "XX_SMALL".
	 * </ul>
	 * 
	 * @param relativeConstant the relative constant to convert
	 * @param baseConstant     the base constant on which this conversion is based
	 * @return the absolute font size constant
	 */

	private String convertRelativeConstantToAbsolute(String relativeConstant, String baseConstant) {
		for (int i = 0; i < ABSOLUTE_FONT_SIZE_CONSTANTS.length; i++) {
			if (ABSOLUTE_FONT_SIZE_CONSTANTS[i].equals(baseConstant)) {
				if (DesignChoiceConstants.FONT_SIZE_LARGER.equals(relativeConstant)) {
					if (i == ABSOLUTE_FONT_SIZE_CONSTANTS.length - 1)
						return ABSOLUTE_FONT_SIZE_CONSTANTS[i];

					return ABSOLUTE_FONT_SIZE_CONSTANTS[i + 1];
				} else if (DesignChoiceConstants.FONT_SIZE_SMALLER.equals(relativeConstant)) {
					if (i == 0)
						return ABSOLUTE_FONT_SIZE_CONSTANTS[i];

					return ABSOLUTE_FONT_SIZE_CONSTANTS[i - 1];
				}
			}
		}

		assert false;
		return null;
	}

	/**
	 * Gets the absolute dimension value of font size with the given relative value.
	 * 
	 * @param relativeDimensionValue the relative dimension value
	 * 
	 * @return the absolute dimension value
	 */

	DimensionValue getAbsoluteValueForFontSize(DimensionValue relativeDimensionValue) {
		assert relativeDimensionValue == null
				|| !CSSLengthValueHandler.isAbsoluteUnit(relativeDimensionValue.getUnits());

		FactoryPropertyHandle fontSizeFactoryHandle = dimensionHandle.getElementHandle()
				.getFactoryPropertyHandle(dimensionHandle.getPropertyDefn().getName());
		// Get the absolute dimension value of the container element.

		DesignElementHandle containerHandle = dimensionHandle.getElementHandle().getContainer();
		DimensionHandle dimensionHandleFromContainer = containerHandle
				.getDimensionProperty(dimensionHandle.getPropertyDefn().getName());

		DimensionValue absoluteValueFromContainer = null;
		if (dimensionHandleFromContainer != null) {
			absoluteValueFromContainer = dimensionHandleFromContainer.getAbsoluteValue();

			assert absoluteValueFromContainer != null;
		} else {
			absoluteValueFromContainer = getDefaultFontSizeValue();
		}

		DimensionValue absoluteDimensionValue = fontSizeFactoryHandle == null ? absoluteValueFromContainer
				: computeRelativeValue(absoluteValueFromContainer, relativeDimensionValue);

		assert (isAbsoluteUnit(absoluteDimensionValue.getUnits()));

		return absoluteDimensionValue;
	}

}