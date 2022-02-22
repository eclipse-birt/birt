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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.IAbsoluteFontSizeValueProvider;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;

/**
 * Represents the default implementation for
 * <code>IAbsoluteFontSizeValueProvider</code>. This provider defines
 * <ul>
 * <li>xx-small = 7pt
 * <li>x-small = 8pt
 * <li>small = 10pt
 * <li>medium = 12pt
 * <li>large = 14pt
 * <li>x-large = 17pt
 * <li>xx-large = 20pt
 * </ul>
 **/

public class DefaultAbsoluteFontSizeValueProvider implements IAbsoluteFontSizeValueProvider {

	private static DefaultAbsoluteFontSizeValueProvider instance = new DefaultAbsoluteFontSizeValueProvider();

	private static final DimensionValue xxSmall = new DimensionValue(7, DesignChoiceConstants.UNITS_PT);
	private static final DimensionValue xSmall = new DimensionValue(8, DesignChoiceConstants.UNITS_PT);
	private static final DimensionValue small = new DimensionValue(10, DesignChoiceConstants.UNITS_PT);
	private static final DimensionValue medium = new DimensionValue(12, DesignChoiceConstants.UNITS_PT);
	private static final DimensionValue large = new DimensionValue(14, DesignChoiceConstants.UNITS_PT);
	private static final DimensionValue xLarge = new DimensionValue(17, DesignChoiceConstants.UNITS_PT);
	private static final DimensionValue xxLarge = new DimensionValue(20, DesignChoiceConstants.UNITS_PT);

	/**
	 * Returns the instance.
	 *
	 * @return the instance
	 */

	public static DefaultAbsoluteFontSizeValueProvider getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.IAbsoluteFontSizeValueProvider#getValueOf(
	 * java.lang.String)
	 */

	@Override
	public DimensionValue getValueOf(String fontSizeConstant) {
		if (DesignChoiceConstants.FONT_SIZE_XX_SMALL.equalsIgnoreCase(fontSizeConstant)) {
			return xxSmall;
		} else if (DesignChoiceConstants.FONT_SIZE_X_SMALL.equalsIgnoreCase(fontSizeConstant)) {
			return xSmall;
		} else if (DesignChoiceConstants.FONT_SIZE_SMALL.equalsIgnoreCase(fontSizeConstant)) {
			return small;
		} else if (DesignChoiceConstants.FONT_SIZE_MEDIUM.equalsIgnoreCase(fontSizeConstant)) {
			return medium;
		} else if (DesignChoiceConstants.FONT_SIZE_LARGE.equalsIgnoreCase(fontSizeConstant)) {
			return large;
		} else if (DesignChoiceConstants.FONT_SIZE_X_LARGE.equalsIgnoreCase(fontSizeConstant)) {
			return xLarge;
		} else if (DesignChoiceConstants.FONT_SIZE_XX_LARGE.equalsIgnoreCase(fontSizeConstant)) {
			return xxLarge;
		}

		return null;
	}

}
