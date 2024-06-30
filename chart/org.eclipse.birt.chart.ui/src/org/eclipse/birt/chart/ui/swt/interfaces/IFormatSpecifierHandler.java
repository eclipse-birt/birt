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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Shell;

/**
 * This interface defines method(s) to process format specifier.
 */

public interface IFormatSpecifierHandler {
	/**
	 * Creates a UI to process format specifier.
	 *
	 * @param shellParent
	 * @param title
	 * @param axisType
	 * @param formatspecifier
	 * @param target
	 * @param attrName
	 * @param context
	 * @return instance of format specifier
	 */
	FormatSpecifier handleFormatSpecifier(Shell shellParent, String title, AxisType[] axisTypes,
			FormatSpecifier formatspecifier, EObject target, String attrName, ChartWizardContext context);
}
