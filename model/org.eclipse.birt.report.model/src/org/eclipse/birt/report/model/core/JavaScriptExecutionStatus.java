/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.core;

/**
 * Represents whether the current thread is running a java script.
 *
 * Restricting access to encrypted password from java script event handlers
 * needs the context from where the encrypted password is accessed. This class
 * provides the required context for differentiating encrypted password access
 * from java script event handlers.
 */

public class JavaScriptExecutionStatus {

	private static final ThreadLocal<Boolean> CURRENT = new ThreadLocal<>() {

		@Override
		protected Boolean initialValue() {
			return false;
		}
	};

	public static void setExeucting(boolean executionOnGoing) {
		CURRENT.set(executionOnGoing);
	}

	public static boolean isExecuting() {
		return CURRENT.get();
	}

	public static void remove() {
		CURRENT.remove();
	}

}
