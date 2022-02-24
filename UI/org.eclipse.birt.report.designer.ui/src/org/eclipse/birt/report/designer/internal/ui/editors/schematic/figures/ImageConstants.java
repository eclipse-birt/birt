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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.draw2d.PositionConstants;

/**
 * Defines image Constants.
 */

public class ImageConstants implements PositionConstants {

	/** no_repeat */
	public static int NO_REPEAT = 0;

	/** repeat-x */
	public static final int REPEAT_X = 1;

	/** repeat-y */
	public static final int REPEAT_Y = 2;

	/** repeat */
	public static final int REPEAT = REPEAT_X | REPEAT_Y;

}
