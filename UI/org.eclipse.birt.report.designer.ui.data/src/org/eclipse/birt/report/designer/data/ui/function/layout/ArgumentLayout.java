/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

	@Override
	public int getLayoutHint() {
		return this.layoutHint;
	}

	@Override
	public void setLayoutHint(int layoutHint) {
		this.layoutHint = layoutHint;
	}

	@Override
	public String getName() {
		return name;
	}

}
