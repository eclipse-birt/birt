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
package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;

/**
 * A simple wrapper class denotes for the inherited computed column handle
 */
public class InheritedComputedColumnHandle {
	private ComputedColumnHandle handle;

	public InheritedComputedColumnHandle(ComputedColumnHandle handle) {
		super();
		this.handle = handle;
	}

	public String getName() {
		return handle.getName();
	}

	public ComputedColumnHandle getHandle() {
		return handle;
	}
}
