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

package org.eclipse.birt.report.model.api.core;

/**
 * Represents the visibility level of a module.
 */

public interface IAccessControl {
	/**
	 * Native resources.
	 */

	int NATIVE_LEVEL = 0;

	/**
	 * Resources in directly included libraries.
	 */

	int DIRECTLY_INCLUDED_LEVEL = 1;

	/**
	 * All included libraries. No matter directly or indirectly.
	 */

	int ARBITARY_LEVEL = Integer.MAX_VALUE;
}
