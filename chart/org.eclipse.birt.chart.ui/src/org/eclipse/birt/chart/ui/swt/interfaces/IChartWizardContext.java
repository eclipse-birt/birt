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

import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;

/**
 * Chart's extension of IWizardContext
 */

public interface IChartWizardContext extends IWizardContext
{

	/**
	 * 
	 * @return the UIServiceProvider in context.
	 */
	IUIServiceProvider getUIServiceProvider( );

	/**
	 * 
	 * @return the DataServiceProvider in context.
	 */
	IDataServiceProvider getDataServiceProvider( );

	/**
	 * @return DataSheet
	 */
	IChartDataSheet getDataSheet( );

	/**
	 * 
	 * @return the StyleProcessor
	 */
	IStyleProcessor getProcessor( );

	/**
	 * set the StyleProcessor
	 */
	void setProcessor( IStyleProcessor processor );

	/**
	 * 
	 * @return ExtendedItem
	 */
	Object getExtendedItem( );

	/**
	 * 
	 * @param extendedItem
	 */
	void setExtendedItem( Object extendedItem );
}
