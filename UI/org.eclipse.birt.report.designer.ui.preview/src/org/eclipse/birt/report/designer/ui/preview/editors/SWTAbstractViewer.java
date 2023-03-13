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

package org.eclipse.birt.report.designer.ui.preview.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 *
 */

public abstract class SWTAbstractViewer extends AbstractViewer {

	/**
	 * Creates and lays out the top level composite for the viewer.
	 *
	 * @param parent
	 */
	public abstract Control createUI(Composite parent);

	/**
	 * Returns the top level control for this viewer.
	 *
	 * @return
	 */
	public abstract Control getUI();

	@Override
	public void close() {
		super.close();
		Control control = getUI();
		if (control != null && !control.isDisposed()) {
			control.dispose();
		}
	}

}
