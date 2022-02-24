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

package org.eclipse.birt.chart.ui.swt;

import java.util.Vector;

import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class DefaultSelectDataComponent implements ISelectDataComponent {

	private transient Vector vListeners = null;

	public Composite createArea(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void selectArea(boolean selected, Object data) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		if (vListeners != null && !vListeners.isEmpty()) {
			vListeners.clear();
		}
	}

	public void addListener(Listener listener) {
		if (vListeners == null) {
			vListeners = new Vector();
		}
		vListeners.add(listener);
	}

	protected void fireEvent(Event e) {
		if (vListeners != null && !vListeners.isEmpty()) {
			for (int i = 0; i < vListeners.size(); i++) {
				((Listener) vListeners.get(i)).handleEvent(e);
			}
		}
	}

	public void bindAssociatedLabel(Label label) {
		// do nothing

	}

	public void bindAssociatedName(String name) {
		// do nothing

	}

}
