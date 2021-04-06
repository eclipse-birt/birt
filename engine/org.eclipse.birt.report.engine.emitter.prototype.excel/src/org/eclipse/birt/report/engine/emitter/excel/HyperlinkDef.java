/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.awt.Color;
import java.io.Serializable;

public class HyperlinkDef implements Serializable {
	private static final long serialVersionUID = 5933271313761755249L;
	private String url;
	private int type;
	private String toolTip;
	private Color color;

	public HyperlinkDef(String url, int type, String toolTip) {
		this.url = url;
		this.type = type;
		this.toolTip = toolTip;
	}

	public String getUrl() {
		return url;
	}

	public int getType() {
		return type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}
}
