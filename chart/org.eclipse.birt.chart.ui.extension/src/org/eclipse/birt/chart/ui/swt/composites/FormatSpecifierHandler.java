/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.swt.interfaces.IFormatSpecifierHandler;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * FormatSpecifierHandler
 */

public class FormatSpecifierHandler implements IFormatSpecifierHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IFormatSpecifierHandler#
	 * handleFormatSpecifier(org.eclipse.swt.widgets.Shell,
	 * org.eclipse.birt.chart.model.attribute.AxisType, java.lang.String,
	 * org.eclipse.emf.ecore.EObject,
	 * org.eclipse.birt.chart.model.attribute.FormatSpecifier, java.lang.String,
	 * org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext)
	 */
	public FormatSpecifier handleFormatSpecifier(Shell shellParent, String title, AxisType[] axisTypes,
			FormatSpecifier formatspecifier, EObject target, String attrName, ChartWizardContext context) {
		FormatSpecifierDialog editor = new FormatSpecifierDialog(shellParent, formatspecifier, axisTypes, title);
		if (editor.open() == Window.OK) {
			if (editor.getFormatSpecifier() == null) {
				ChartElementUtil.setEObjectAttribute(target, attrName, null, true);
			} else {
				ChartElementUtil.setEObjectAttribute(target, attrName, editor.getFormatSpecifier(), false);
			}
			return editor.getFormatSpecifier();
		}
		return null;
	}

}
