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

package org.eclipse.birt.report.model.activity;

import java.util.List;

/**
 * Wrapper to return internal vairable for tests under independent plug-in
 * "model.tests".
 */

public class ActivityTestUtil {

	/**
	 * Wrapper method to return internal variable listener in activity stack.
	 *
	 * @param as activity stack
	 * @return listeners in activity stack.
	 */

	public static List getActivityListener(ActivityStack as) {
		return as.listeners;
	}
}
