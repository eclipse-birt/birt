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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents the handler which processes the computed length value as CSS
 * specification describes.
 */

final class ComputedValueHandler extends CSSLengthValueHandler {

	/**
	 * Default constructor.
	 *
	 * @param dimensionHandle
	 */

	ComputedValueHandler(DimensionHandle dimensionHandle) {
		super(dimensionHandle);
	}

	/**
	 * Returns the absolute value for other length property, instead of font size.
	 *
	 * @param relativeDimensionValue the relative dimension value
	 * @return the absolute dimension value
	 */

	DimensionValue getAbsoluteValueForLength(DimensionValue relativeDimensionValue) {
		FactoryPropertyHandle factoryHandle = dimensionHandle.getElementHandle()
				.getFactoryPropertyHandle(dimensionHandle.getPropertyDefn().getName());
		// Get the absolute dimension value of the container element.

		DesignElementHandle containerHandle = dimensionHandle.getElementHandle().getContainer();
		DimensionHandle dimensionHandleFromContainer = containerHandle
				.getDimensionProperty(dimensionHandle.getPropertyDefn().getName());

		DimensionValue absoluteFontSizeValue = getDefaultFontSizeValue();

		DimensionHandle factualFontSizeHanle = getFactualFontSizeHandle();
		if (factualFontSizeHanle != null) {
			DimensionValue absoluteFontSizeValueTemp = factualFontSizeHanle.getAbsoluteValue();
			if (absoluteFontSizeValueTemp != null) {
				absoluteFontSizeValue = absoluteFontSizeValueTemp;
			}
		}

		// if value is set in this element, then compute the absolute value
		if (factoryHandle != null) {
			return computeRelativeValue(absoluteFontSizeValue, relativeDimensionValue);
		}

		// if this property can inherit, then get the computed value from
		// container directly
		if (dimensionHandle.getPropertyDefn().canInherit()) {
			return dimensionHandleFromContainer.getAbsoluteValue();
		}

		// the property can not inherit, then get the default value
		Object defaultValue = dimensionHandle.getPropertyDefn().getDefault();
		if (defaultValue instanceof DimensionValue) {
			DimensionValue defaultDimensionValue = (DimensionValue) defaultValue;
			if (CSSLengthValueHandler.isAbsoluteUnit(defaultDimensionValue.getUnits())) {
				return defaultDimensionValue;
			}
			return computeRelativeValue(absoluteFontSizeValue, defaultDimensionValue);
		}
		return null;
	}

	/**
	 * Returns the font size dimension handle of the element which provides the
	 * factual font size.
	 *
	 * @return the font size dimension handle
	 */

	private DimensionHandle getFactualFontSizeHandle() {
		String unit = dimensionHandle.getUnits();
		if (!DesignChoiceConstants.UNITS_EM.equalsIgnoreCase(unit)
				&& !DesignChoiceConstants.UNITS_EX.equalsIgnoreCase(unit)
				&& !DesignChoiceConstants.UNITS_PERCENTAGE.equalsIgnoreCase(unit)) {
			assert false;

			return dimensionHandle.elementHandle.getDimensionProperty(IStyleModel.FONT_SIZE_PROP);
		}

		Object propValue = null;
		ElementPropertyDefn fontSizePropDefn = (ElementPropertyDefn) MetaDataDictionary.getInstance()
				.getElement(ReportDesignConstants.STYLE_ELEMENT).getProperty(IStyleModel.FONT_SIZE_PROP);

		DesignElementHandle e = dimensionHandle.getElementHandle();

		// Located the element which has the property this dimension represents.
		boolean computedPropertyFound = false;
		while (e != null) {
			if (!computedPropertyFound) {
				propValue = e.getElement().getStrategy().getPropertyFromElement(dimensionHandle.getModule(),
						e.getElement(), dimensionHandle.propDefn);
				if (propValue != null) {
					computedPropertyFound = true;
				}
			}

			if (computedPropertyFound) {
				propValue = e.getElement().getStrategy().getPropertyFromElement(dimensionHandle.getModule(),
						e.getElement(), fontSizePropDefn);
				if (propValue != null) {
					break;
				}
			}

			// If the property this dimension represents can not be inherited.

			if (!dimensionHandle.propDefn.canInherit()) {
				break;
			}

			e = e.getContainer();
		}

		if (e != null) {
			return e.getDimensionProperty(IStyleModel.FONT_SIZE_PROP);
		}

		return null;
	}

}
