/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.util;

/**
 * Constants used in UI
 */

public class ChartUIConstants
{

	public static final String IMAGE_RA_BOTTOMLEFT = "icons/obj16/ra_bottomleft.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_BOTTOMRIGHT = "icons/obj16/ra_bottomright.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_LEFTUP = "icons/obj16/ra_leftup.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_LEFTDOWN = "icons/obj16/ra_leftright.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_RIGHTUP = "icons/obj16/ra_rightup.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_RIGHTDOWN = "icons/obj16/ra_rightdown.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_TOPLEFT = "icons/obj16/ra_topleft.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_TOPRIGHT = "icons/obj16/ra_topright.gif"; //$NON-NLS-1$

	public static final String IMAGE_DELETE = "icons/obj16/delete_edit.gif"; //$NON-NLS-1$
	public static final String IMAGE_SIGMA = "icons/obj16/sigma.gif"; //$NON-NLS-1$
	
	// Icons in Outline view
	public static final String IMAGE_OUTLINE = "icons/obj16/outline.gif"; //$NON-NLS-1$
	public static final String IMAGE_OUTLINE_LIB = "icons/obj16/outline_lib.gif"; //$NON-NLS-1$
	
	//Constants for position scope
	public static final int ALLOW_ABOVE_POSITION = 1;
	public static final int ALLOW_BELOW_POSITION = 2;
	public static final int ALLOW_LEFT_POSITION = 4;
	public static final int ALLOW_RIGHT_POSITION = 8;
	public static final int ALLOW_IN_POSITION = 16;
	public static final int ALLOW_OUT_POSITION = 32;
	public static final int ALLOW_VERTICAL_POSITION = ALLOW_ABOVE_POSITION
			| ALLOW_BELOW_POSITION;
	public static final int ALLOW_HORIZONTAL_POSITION = ALLOW_LEFT_POSITION
			| ALLOW_RIGHT_POSITION;
	public static final int ALLOW_INOUT_POSITION = ALLOW_IN_POSITION
			| ALLOW_OUT_POSITION;
	public static final int ALLOW_ALL_POSITION = ALLOW_VERTICAL_POSITION
			| ALLOW_HORIZONTAL_POSITION | ALLOW_INOUT_POSITION;
	
	public static final String NON_STACKED_TYPE = "non-stacked"; //$NON-NLS-1$
	public static final String STACKED_TYPE = "stacked"; //$NON-NLS-1$
}
