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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 *
 */

public interface ISelectDataComponent extends IChartUIAccessible {

	Composite createArea(Composite parent);

	void selectArea(boolean selected, Object data);

	void dispose();

	void addListener(Listener listener);
}
