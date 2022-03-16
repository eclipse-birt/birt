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

package org.eclipse.birt.report.designer.ui.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Instances of this interface take charge of creating attribute pages which
 * reside in the given TabFolder control.
 */
public interface IPageGenerator {

	/**
	 * Creates the content of the page control.
	 *
	 * @param parent
	 * @param input
	 */
	void createControl(Composite parent, Object input);

	/**
	 * Returns the page control.
	 *
	 * @return
	 */
	Control getControl();

	/**
	 * Returns the input.
	 *
	 * @return
	 */
	Object getInput();

	/**
	 * Refresh the page content based on current input
	 */
	void refresh();
}
