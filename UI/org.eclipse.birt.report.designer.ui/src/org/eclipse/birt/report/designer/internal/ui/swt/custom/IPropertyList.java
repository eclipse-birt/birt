/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

public interface IPropertyList {

	void setElements(Map categoryLabels);

	void setSelection(String key, int index);

	int getSelectionIndex();

	Control getControl();

	void addListener(int selection, Listener listener);

	String getSelectionKey();

	Object getTabList();

	Control getItem(int index);

}
