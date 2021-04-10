/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

/**
 * AbstractDialogHelper
 */
public abstract class AbstractDialogHelper implements IDialogHelper {

	protected Map<Integer, List<Listener>> listeners;
	protected Map<String, Object> props;
	protected Object container;

	public void addListener(int eventType, Listener listener) {
		if (listeners == null) {
			listeners = new HashMap<Integer, List<Listener>>();
		}

		List<Listener> list = listeners.get(eventType);

		if (list == null) {
			list = new ArrayList<Listener>();
			listeners.put(eventType, list);
		}

		if (!list.contains(listener)) {
			list.add(listener);
		}

	}

	public void createContent(Composite parent) {
	}

	public Object getProperty(String key) {
		if (props != null) {
			return props.get(key);
		}

		return null;
	}

	public void removeListener(int eventType, Listener listener) {
		if (listeners != null) {
			List<Listener> list = listeners.get(eventType);

			if (list != null && list.contains(listener)) {
				list.remove(listener);
			}
		}
	}

	public void setContainer(Object container) {
		this.container = container;
	}

	public void setProperty(String key, Object value) {
		if (props == null) {
			props = new HashMap<String, Object>();
		}

		props.put(key, value);
	}

	public void validate() {
	}

	public String[] getErrors() {
		return new String[0];
	}

	public void update(boolean inward) {
	}

	public Control getControl() {
		return null;
	}

}
