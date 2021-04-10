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
