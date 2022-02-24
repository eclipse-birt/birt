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

package org.eclipse.birt.report.designer.ui.extensions;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Adapter class for IPropertyTabUI, default doing nothing.
 */
public class PropertyTabUIAdapter implements IPropertyTabUI {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#
	 * getTabDisplayName()
	 */
	public String getTabDisplayName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#elementChanged(
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged(NotificationEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI#dispose()
	 */
	public void dispose() {
	}

	public void buildUI(Composite parent) {
		// TODO Auto-generated method stub

	}

	protected Object input;

	public void setInput(Object input) {
		this.input = input;
	}

	public Control getControl() {
		// TODO Auto-generated method stub
		return null;
	}

}
