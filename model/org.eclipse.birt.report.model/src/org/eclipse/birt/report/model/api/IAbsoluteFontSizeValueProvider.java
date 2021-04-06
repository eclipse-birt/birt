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

import org.eclipse.birt.report.model.api.metadata.DimensionValue;

/**
 * Provides the absolute dimension value for absolute font size constant.
 */

public interface IAbsoluteFontSizeValueProvider {

	/**
	 * Returns the dimension value of absolute font size constant. These constants
	 * are defined in <code>DesignChoiceConstants</code>. And the absolute value of
	 * the following should be provided:
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
	 * @param fontSizeConstant the absolute font size constant
	 * @return the absolute dimension value. The unit of the returned value should
	 *         be one of px, in, cm, mm, and pt.
	 */

	public DimensionValue getValueOf(String fontSizeConstant);

}