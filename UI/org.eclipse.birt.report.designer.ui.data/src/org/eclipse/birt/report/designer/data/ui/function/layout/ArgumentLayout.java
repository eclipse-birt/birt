/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.function.layout;

import org.eclipse.birt.report.designer.internal.ui.data.function.layout.IArgumentLayout;

public class ArgumentLayout implements IArgumentLayout {

	private int layoutHint;
	private String name;

	public ArgumentLayout(String name, int layoutHint) {
		this.name = name;
		this.layoutHint = layoutHint;
	}

	public ArgumentLayout(String name) {
		this.name = name;
		this.layoutHint = ALIGN_BLOCK;
	}

	public int getLayoutHint() {
		return this.layoutHint;
	}

	public void setLayoutHint(int layoutHint) {
		this.layoutHint = layoutHint;
	}

	public String getName() {
		return name;
	}

}
