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

package org.eclipse.birt.core.ui.swt.custom;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * ICustomChoice
 */
public interface ICustomChoice {

	void setValue(Object value);

	Object getValue();

	void addListener(int eventType, Listener listener);

	void notifyListeners(int eventType, Event event);

	void redraw();

	void setEnabled(boolean enabled);

	boolean isEnabled();

	Point getSize();

	void setLayoutData(Object layoutData);
}
