/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.util;

import org.eclipse.swt.graphics.Image;

/**
 * Create and manage all tool items in toolbar.
 */
public class Tools {

	public int id;
	public String name;
	public String group;
	public int type;
	public Runnable action;
	public Image image = null;
	public Object data;
	private boolean bEnabled;

	public Tools(int id, String name, String group, int type) {
		super();
		this.id = id;
		this.name = name;
		this.group = group;
		this.type = type;
	}

	public Tools(int id, String name, String group, int type, Object data) {
		this(id, name, group, type);
		this.data = data;
	}

	public void setEnabled(boolean bEnabled) {
		this.bEnabled = bEnabled;
	}

	public boolean isEnabled() {
		return this.bEnabled;
	}
}
