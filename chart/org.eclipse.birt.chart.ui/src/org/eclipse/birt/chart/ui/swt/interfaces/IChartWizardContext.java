/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;

/**
 * Chart's extension of IWizardContext
 */

public interface IChartWizardContext<C extends IChartObject> extends IWizardContext {

	/**
	 * 
	 * @return the UIServiceProvider in context.
	 */
	IUIServiceProvider getUIServiceProvider();

	/**
	 * 
	 * @return the DataServiceProvider in context.
	 */
	IDataServiceProvider getDataServiceProvider();

	/**
	 * @return DataSheet
	 */
	IChartDataSheet getDataSheet();

	/**
	 * 
	 * @return the StyleProcessor
	 */
	IStyleProcessor getProcessor();

	/**
	 * set the StyleProcessor
	 */
	void setProcessor(IStyleProcessor processor);

	/**
	 * 
	 * @return ExtendedItem
	 */
	Object getExtendedItem();

	/**
	 * 
	 * @param extendedItem
	 */
	void setExtendedItem(Object extendedItem);

	/**
	 * Returns the model on which wizard context is used.
	 */
	C getModel();

	/**
	 * Returns if the UI is enabled or not.The UI, including task, subtask or toggle
	 * button, is identified by the exclusive id.
	 * 
	 * @param id the exclusive id to identify the UI
	 * @return the UI enabled state
	 * @since 2.3
	 */
	boolean isEnabled(String id);
}
