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
