/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.controller;

import org.eclipse.birt.report.designer.ui.preview.extension.IViewer;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Widget;

public interface IController {

	public void setViewer(IViewer viewer);

	public Widget getPane();

	public void setBusy(boolean b);

	public void addButton(String text, String toolTip, SelectionListener listener);
}
