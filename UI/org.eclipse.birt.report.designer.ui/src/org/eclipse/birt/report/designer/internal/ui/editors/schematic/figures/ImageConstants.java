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