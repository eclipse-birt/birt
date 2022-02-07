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
