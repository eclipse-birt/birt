/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.swt.widgets.Label;

/**
 * This interface defines the methods to add accessibility to chart components.
 */

public interface IChartUIAccessible {

	/**
	 * add accessibility with the given Label's text.
	 * 
	 * @param label Label Object.
	 */
	void bindAssociatedLabel(Label label);

	/**
	 * add accessibility with the given name.
	 * 
	 * @param name String Object.
	 */
	void bindAssociatedName(String name);
}
