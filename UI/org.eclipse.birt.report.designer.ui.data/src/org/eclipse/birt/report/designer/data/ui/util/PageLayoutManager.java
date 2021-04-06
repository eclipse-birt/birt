/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.util;

import java.util.HashSet;

public class PageLayoutManager {

	private static HashSet<String> registeredSet = new HashSet<String>();

	public static void registerPage(String pageId) {
		if (pageId != null) {
			registeredSet.add(pageId);
		}
	}

	public static boolean isRegisteredPage(String pageId) {
		return pageId != null && registeredSet.contains(pageId);
	}

}
