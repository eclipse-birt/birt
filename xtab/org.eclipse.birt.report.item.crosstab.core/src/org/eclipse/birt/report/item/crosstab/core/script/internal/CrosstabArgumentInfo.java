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

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import org.eclipse.birt.report.model.api.scripts.ArgumentInfo;

/**
 * CrosstabArgumentInfo
 */
public class CrosstabArgumentInfo extends ArgumentInfo {

	private String name;

	CrosstabArgumentInfo(Class<?> argumentType, String argumentName) {
		super(argumentType);

		this.name = argumentName;
	}

	public String getName() {
		return name == null ? "" : name; //$NON-NLS-1$
	}

	public String getDisplayName() {
		return getName();
	}
}
