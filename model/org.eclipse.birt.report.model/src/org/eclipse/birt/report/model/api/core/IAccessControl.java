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

package org.eclipse.birt.report.model.api.core;

/**
 * Represents the visibility level of a module.
 */

public interface IAccessControl {
	/**
	 * Native resources.
	 */

	public static final int NATIVE_LEVEL = 0;

	/**
	 * Resources in directly included libraries.
	 */

	public static final int DIRECTLY_INCLUDED_LEVEL = 1;

	/**
	 * All included libraries. No matter directly or indirectly.
	 */

	public static final int ARBITARY_LEVEL = Integer.MAX_VALUE;
}
