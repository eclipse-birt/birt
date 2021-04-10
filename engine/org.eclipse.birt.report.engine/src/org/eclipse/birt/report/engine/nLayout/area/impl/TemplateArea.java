/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.nLayout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.nLayout.area.ITemplateArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

public class TemplateArea extends TextArea implements ITemplateArea {
	protected int type;

	public TemplateArea(String text, TextStyle style, int type) {
		super(text, style);
		this.type = type;
	}

	public void accept(IAreaVisitor visitor) {
		visitor.visitAutoText(this);
	}

	public int getType() {
		return type;
	}
}
