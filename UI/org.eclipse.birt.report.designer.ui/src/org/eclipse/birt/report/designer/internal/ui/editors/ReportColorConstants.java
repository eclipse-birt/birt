/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Actuate Coporation - Copy code to BIRT package and define BIRT colors
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors;

import org.eclipse.birt.report.designer.core.CorePlugin;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Color Constants
 *
 */
public interface ReportColorConstants {
	Color greyFillColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.GreyFillColor", //$NON-NLS-1$
			new RGB(135, 135, 135));
	Color textFillColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.textFillColor", //$NON-NLS-1$
			new RGB(135, 135, 135));

	Color SelctionFillColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.SelctionFillColor", //$NON-NLS-1$
			new RGB(10, 36, 106));

	Color HandleFillColor = ColorManager.getColor(212, 208, 200);

	Color HandleBorderColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.HandleBorderColor", //$NON-NLS-1$
			new RGB(128, 128, 128));

	Color MarginBorderColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.MarginBorderColor", //$NON-NLS-1$
			new RGB(197, 223, 244));

	Color MarginMarkerColor = ColorManager.getColor(170, 170, 170);

	Color ListControlFillColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.ListControlFillColor", //$NON-NLS-1$
			new RGB(238, 236, 246));

	Color TableGuideTextColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.TableGuideTextColor", //$NON-NLS-1$
			new RGB(147, 137, 145));

	Color TableGuideFillColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.GuideFillColor", //$NON-NLS-1$
			new RGB(239, 239, 247));

	Color ShadowLineColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.ShadowLineColor", //$NON-NLS-1$
			new RGB(204, 204, 204));

	Color DarkShadowLineColor = ColorManager.getColor("org.eclipse.birt.report.designer.ui.DarkShadowLineColor", //$NON-NLS-1$
			new RGB(128, 128, 128));

	Color RedWarning = ColorManager.getColor(255, 0, 0);

	Color MultipleSelectionHandleColor = ColorManager.getColor(
			"org.eclipse.birt.report.designer.ui.MultipleSelectionHandleColor", //$NON-NLS-1$
			new RGB(200, 200, 200));

	Color ReportRootBackgroundColor = CorePlugin.ReportRootBackgroundColor;

	Color ReportBackground = ColorManager.getColor("org.eclipse.birt.report.designer.ui.ReportBackground", //$NON-NLS-1$
			new RGB(255, 255, 255));
	Color ReportForeground = CorePlugin.ReportForeground;

	Color DarkGrayForground = ColorManager.getColor("org.eclipse.birt.report.designer.ui.DarkGrayForground", //$NON-NLS-1$
			new RGB(64, 64, 64));

	Color JSCOMMENTCOLOR = ColorManager.getColor("org.eclipse.birt.report.designer.ui.JSCOMMENTCOLOR", //$NON-NLS-1$
			new RGB(63, 127, 95));

	Color JSSTRINGCOLOR = ColorManager.getColor("org.eclipse.birt.report.designer.ui.JSSTRINGCOLOR", //$NON-NLS-1$
			new RGB(42, 0, 255));

	Color JSKEYWORDCOLOR = ColorManager.getColor("org.eclipse.birt.report.designer.ui.JSKEYWORDCOLOR", //$NON-NLS-1$
			new RGB(127, 0, 85));

	Color JSLINENUMBERCOLOR = ColorManager.getColor("org.eclipse.birt.report.designer.ui.JSLINENUMBERCOLOR", //$NON-NLS-1$
			new RGB(127, 127, 127));

	Color JSMETHODCOLOR = ColorManager.getColor("org.eclipse.birt.report.designer.ui.JSMETHODCOLOR", //$NON-NLS-1$
			new RGB(81, 97, 122));

	Color JSOBJECTCOLOR = ColorManager.getColor("org.eclipse.birt.report.designer.ui.JSOBJECTCOLOR", //$NON-NLS-1$
			new RGB(181, 87, 50));

	Color[] ShadowColors = {

			ColorManager.getColor(92, 114, 143), ColorManager.getColor(97, 118, 147),
			ColorManager.getColor(102, 123, 151), ColorManager.getColor(111, 129, 158),
			ColorManager.getColor(120, 137, 165), ColorManager.getColor(128, 144, 172),
			ColorManager.getColor(136, 150, 178), ColorManager.getColor(142, 155, 183),
			ColorManager.getColor(148, 160, 188), ColorManager.getColor(152, 163, 191),
			ColorManager.getColor(154, 165, 193), ColorManager.getColor(156, 166, 194),
			ColorManager.getColor(156, 167, 195), };

}
