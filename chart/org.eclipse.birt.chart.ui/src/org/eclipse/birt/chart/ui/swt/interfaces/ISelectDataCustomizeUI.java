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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public interface ISelectDataCustomizeUI {

	static final int ORTHOGONAL_SERIES = 1;
	static final int GROUPING_SERIES = 2;

	/**
	 * Creates left binding area
	 * 
	 * @param parent composite parent
	 */
	void createLeftBindingArea(Composite parent);

	void createRightBindingArea(Composite parent);

	void createBottomBindingArea(Composite parent);

	void refreshLeftBindingArea();

	void refreshRightBindingArea();

	void refreshBottomBindingArea();

	void selectLeftBindingArea(boolean selected, Object data);

	void selectRightBindingArea(boolean selected, Object data);

	void selectBottomBindingArea(boolean selected, Object data);

	/**
	 * Notifies changes according to EMF model notification
	 * 
	 * @param notification EMF model notification
	 */
	void notifyChange(Notification notification);

	/**
	 * Initializes all required resource.
	 * 
	 */
	void init();

	/**
	 * Disposes all resources.
	 * 
	 */
	void dispose();

	/**
	 * 
	 * @param areaType         <code>ORTHOGONAL_SERIES</code>,
	 *                         <code>GROUPING_SERIES</code>
	 * @param seriesdefinition
	 * @param context
	 * @param sTitle
	 * @return UI component
	 */
	ISelectDataComponent getAreaComponent(int areaType, SeriesDefinition seriesdefinition, ChartWizardContext context,
			String sTitle);

	void layoutAll();

	/**
	 * Gets current selected series index of each axis.
	 * 
	 * @return array of current selected series index
	 */
	int[] getSeriesIndex();

	/**
	 * Sets current selected series index of each axis.
	 * 
	 * @param seriesIndex array of current selected series index
	 */
	void setSeriesIndex(int[] seriesIndex);
}
