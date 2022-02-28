/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editors;

public interface IPageStaleType {

	/**
	 * None stale type.
	 */
	int NONE = 0;

	/**
	 * Model had change stale type.
	 */
	int MODEL_CHANGED = 1;

	/**
	 * Code had change stale type.
	 */
	int CODE_CHANGED = 2;

	int MODEL_RELOAD = 4;
}
