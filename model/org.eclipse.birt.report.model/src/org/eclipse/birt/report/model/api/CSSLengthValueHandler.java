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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents the handler which computes the length value as CSS specification
 * describes.
 */

class CSSLengthValueHandler {

	final static String ABSOLUTE_FONT_SIZE_CONSTANTS[] = { DesignChoiceConstants.FONT_SIZE_XX_SMALL,
			DesignChoiceConstants.FONT_SIZE_X_SMALL, DesignChoiceConstants.FONT_SIZE_SMALL,
			DesignChoiceConstants.FONT_SIZE_MEDIUM, DesignChoiceConstants.FONT_SIZE_LARGE,
			DesignChoiceConstants.FONT_SIZE_X_LARGE, DesignChoiceConstants.FONT_SIZE_XX_LARGE };

	static DimensionValue defaultFontSizeValue = null;

	IAbsoluteFontSizeValueProvider provider;
	DimensionHandle dimensionHandle;

	CSSLengthValueHandler(DimensionHandle dimensionHandle) {
		this.dimensionHandle = dimensionHandle;
		DesignSessionImpl session = dimensionHandle.getElementHandle().getModule().getSession();
		provider = session.getPredefinedFontSizeProvider();
	}

	/**
	 * Returns the default value of the property this handler is processing on.
	 * 
	 * @return the default value in dimension value
	 */

	DimensionValue getDefaultFontSizeValue() {
		if (defaultFontSizeValue == null) {
			ElementDefn styleDefn = (ElementDefn) MetaDataDictionary.getInstance()
					.getElement(ReportDesignConstants.STYLE_ELEMENT);

			ElementPropertyDefn fontSizePropDefn = (ElementPropertyDefn) styleDefn
					.getProperty(IStyleModel.FONT_SIZE_PROP);

			Object defaultValue = fontSizePropDefn.getDefault();
			if (defaultValue instanceof DimensionValue)
				defaultFontSizeValue = (DimensionValue) defaultValue;
			else
				defaultFontSizeValue = provider.getValueOf((String) defaultValue);
		}
		return defaultFontSizeValue;
	}

	/**
	 * Computes the dimension value which is relative to font size. The units which
	 * can be computed are:
	 * <ul>
	 * <li>UNITS_EM
	 * <li>UNITS_EX
	 * <li>UNITS_PERCENTAGE
	 * </ul>
	 * <p>
	 * The following shows the computation rule. For example, font size = 12px
	 * <p>
	 * 2em = 12px * 2(em) = 24px
	 * <p>
	 * 4ex = 12px * 4(ex) / 2 = 24px
	 * <p>
	 * 50% = 12px * 50(%) / 100 = 6px
	 * 
	 * @param fontSizeValue the absolute dimension value of font size
	 * @param relativeValue the relative dimension value to compute
	 * @return the absolute dimension value. Return null if
	 *         <code>relativeValue</code> has the unit which is not in the above
	 *         list.
	 */

	DimensionValue computeRelativeValue(DimensionValue fontSizeValue, DimensionValue relativeValue) {
		assert CSSLengthValueHandler.isAbsoluteUnit(fontSizeValue.getUnits());

		if (!CSSLengthValueHandler.isAbsoluteUnit(fontSizeValue.getUnits())) {
			throw new IllegalArgumentException("The argument \"fontSizeValue\" should be absolute."); //$NON-NLS-1$
		}
		if (relativeValue == null) {
			throw new IllegalArgumentException("The argument \"relativeValue\" should not be null."); //$NON-NLS-1$
		}

		if (CSSLengthValueHandler.isAbsoluteUnit(relativeValue.getUnits())) {
			return relativeValue;
		}

		// Perform computation

		if (DesignChoiceConstants.UNITS_EM.equals(relativeValue.getUnits())) {
			return new DimensionValue(fontSizeValue.getMeasure() * relativeValue.getMeasure(),
					fontSizeValue.getUnits());
		} else if (DesignChoiceConstants.UNITS_EX.equals(relativeValue.getUnits())) {
			return new DimensionValue(fontSizeValue.getMeasure() * relativeValue.getMeasure() / 2,
					fontSizeValue.getUnits());
		} else if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(relativeValue.getUnits())) {
			return new DimensionValue(fontSizeValue.getMeasure() * relativeValue.getMeasure() / 100,
					fontSizeValue.getUnits());
		}

		assert false;
		return null;
	}

	/**
	 * Returns whether the given unit is absolute. The absolute unit includes
	 * absolute length unit and pixel unit.
	 * 
	 * @param unit the unit to check
	 * @return true if the given unit is absolute
	 */

	static boolean isAbsoluteUnit(String unit) {
		return DimensionUtil.isAbsoluteUnit(unit) || DesignChoiceConstants.UNITS_PX.equalsIgnoreCase(unit);
	}

}