/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void close() {
		super.close();
		Control control = getUI();
		if (control != null && !control.isDisposed())
			control.dispose();
	}

}
