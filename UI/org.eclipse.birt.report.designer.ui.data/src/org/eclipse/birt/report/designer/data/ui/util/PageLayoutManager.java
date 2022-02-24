/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.util;

import java.util.HashSet;

public class PageLayoutManager {

	private static HashSet<String> registeredSet = new HashSet<>();

	public static void registerPage(String pageId) {
		if (pageId != null) {
			registeredSet.add(pageId);
		}
	}

	public static boolean isRegisteredPage(String pageId) {
		return pageId != null && registeredSet.contains(pageId);
	}

}
