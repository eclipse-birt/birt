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

package org.eclipse.birt.report.model.api.metadata;

import org.eclipse.birt.report.model.metadata.ColorPropertyType;

/**
 * The interface for constants of colors used in BIRT.
 */

public interface IColorConstants {

	/**
	 * The color black. sRGB value "#000000".
	 */

	String BLACK = "black"; //$NON-NLS-1$

	/**
	 * The color blue. sRGB value "#0000FF".
	 */

	String BLUE = "blue"; //$NON-NLS-1$

	/**
	 * The color aqua. sRGB value "#00FFFF".
	 */

	String AQUA = "aqua"; //$NON-NLS-1$
	/**
	 * The color white. sRGB value ""#FFFFFF".
	 */

	String WHITE = "white"; //$NON-NLS-1$
	/**
	 * The color red. sRGB value "#FF0000".
	 */

	String RED = "red"; //$NON-NLS-1$
	/**
	 * The color blue. sRGB value "#FFA500".
	 */

	String ORANGE = "orange"; //$NON-NLS-1$
	/**
	 * The color silver. sRGB value "#C0C0C0".
	 */

	String SILVER = "silver"; //$NON-NLS-1$
	/**
	 * The color gray. sRGB value "#808080".
	 */

	String GRAY = "gray"; //$NON-NLS-1$
	/**
	 * The color maroon. sRGB value "#800000".
	 */

	String MAROON = "maroon"; //$NON-NLS-1$
	/**
	 * The color purple. sRGB value "#800080".
	 */

	String PURPLE = "purple"; //$NON-NLS-1$
	/**
	 * The color fuchsia. sRGB value "#FF00FF".
	 */

	String FUCHSIA = "fuchsia"; //$NON-NLS-1$
	/**
	 * The color green. sRGB value "#008000".
	 */

	String GREEN = "green"; //$NON-NLS-1$
	/**
	 * The color lime. sRGB value "#00FF00".
	 */

	String LIME = "lime"; //$NON-NLS-1$
	/**
	 * The color olive. sRGB value "#808000".
	 */

	String OLIVE = "olive"; //$NON-NLS-1$
	/**
	 * The color yellow. sRGB value "#FFFF00".
	 */

	String YELLOW = "yellow"; //$NON-NLS-1$
	/**
	 * The color navy. sRGB value "#000080".
	 */

	String NAVY = "navy"; //$NON-NLS-1$
	/**
	 * The color teal. sRGB value "#008080".
	 */

	String TEAL = "teal"; //$NON-NLS-1$

	/**
	 * ChoiceSet name for color set.
	 */

	String COLORS_CHOICE_SET = ColorPropertyType.COLORS_CHOICE_SET;

}
