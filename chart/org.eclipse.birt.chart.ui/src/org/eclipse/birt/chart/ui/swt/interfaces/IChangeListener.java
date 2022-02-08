/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.Chart;

/**
 * This interface provides a mechanism whereby a custom UI sheet provider can
 * register a class to manage addition and removal of UI sheets dynamically
 * based on changes in the model.
 * 
 * @author Actuate Corporation
 */
public interface IChangeListener {

	public void chartModified(Chart chartModel, IUIManager uiManager);

	public void initialize(Chart chartModel, IUIManager uiManager);
}
