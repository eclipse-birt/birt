/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Actuate Coporation - Copy code to BIRT package and define BIRT colors
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors;

import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * Color Constants
 * 
 */
public interface ReportColorConstants
{

	public final static Color greyFillColor = ColorManager.getColor( 135,
			135,
			135 );
	public final static Color SelctionFillColor = ColorManager.getColor( 10,
			36,
			106 );// 0x0A246A
	public final static Color HandleFillColor = ColorManager.getColor( 212,
			208,
			200 );
	public final static Color HandleBorderColor = ColorConstants.gray;

	public final static Color MarginBorderColor = ColorManager.getColor( 197,
			223,
			244 );// 0xC5DFF4
	public final static Color MarginMarkerColor = ColorManager.getColor( 170,
			170,
			170 );// 0xAAAAAA
	public final static Color ListControlFillColor = ColorManager.getColor( 238,
			236,
			246 );// 0xEEECF6
	public final static Color TableGuideTextColor = ColorManager.getColor( 147,
			137,
			145 );// 0x938991
	public final static Color TableGuideFillColor = ColorManager.getColor( 239,
			239,
			247 );// 0xEFEFF7
	public final static Color ShadowLineColor = ColorManager.getColor( 204,
			204,
			204 );// 0xCCCCCC

	public final static Color RedWarning = ColorManager.getColor( 255, 0, 0 );

	public final static Color MultipleSelectionHandleColor = ColorManager.getColor( 200,
			200,
			200 );

	public final static Color ReportBackgroundColor = ColorManager.getColor( 157,
			167,
			195 );

	public final static Color[] ShadowColors = new Color[]{

			ColorManager.getColor( 92, 114, 143 ),
			ColorManager.getColor( 97, 118, 147 ),
			ColorManager.getColor( 102, 123, 151 ),
			ColorManager.getColor( 111, 129, 158 ),
			ColorManager.getColor( 120, 137, 165 ),
			ColorManager.getColor( 128, 144, 172 ),
			ColorManager.getColor( 136, 150, 178 ),
			ColorManager.getColor( 142, 155, 183 ),
			ColorManager.getColor( 148, 160, 188 ),
			ColorManager.getColor( 152, 163, 191 ),
			ColorManager.getColor( 154, 165, 193 ),
			ColorManager.getColor( 156, 166, 194 ),
			ColorManager.getColor( 156, 167, 195 ),
	};

}