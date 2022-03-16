/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

	@Override
	public void addListener(int eventType, Listener listener) {
		if (listeners == null) {
			listeners = new HashMap<>();
		}

		List<Listener> list = listeners.get(eventType);

		if (list == null) {
			list = new ArrayList<>();
			listeners.put(eventType, list);
		}

		if (!list.contains(listener)) {
			list.add(listener);
		}

	}

	@Override
	public void createContent(Composite parent) {
	}

	@Override
	public Object getProperty(String key) {
		if (props != null) {
			return props.get(key);
		}

		return null;
	}

	@Override
	public void removeListener(int eventType, Listener listener) {
		if (listeners != null) {
			List<Listener> list = listeners.get(eventType);

			if (list != null && list.contains(listener)) {
				list.remove(listener);
			}
		}
	}

	@Override
	public void setContainer(Object container) {
		this.container = container;
	}

	@Override
	public void setProperty(String key, Object value) {
		if (props == null) {
			props = new HashMap<>();
		}

		props.put(key, value);
	}

	@Override
	public void validate() {
	}

	@Override
	public String[] getErrors() {
		return new String[0];
	}

	@Override
	public void update(boolean inward) {
	}

	@Override
	public Control getControl() {
		return null;
	}

}
