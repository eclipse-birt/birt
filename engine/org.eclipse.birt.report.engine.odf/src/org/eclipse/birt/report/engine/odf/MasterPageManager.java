/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager for master pages.
 * 
 * For each page in the output, an instance of the master page must be created.
 * This class keeps track of the number of instances for each master page and
 * generates their names accordingly.
 */
public class MasterPageManager {
	private static final String BASE_NAME = "Mp"; //$NON-NLS-1$

	private int masterPageCount;

	private String generateName(String baseName, int num) {
		if (num > 0) {
			baseName += "-" + masterPageCount + "-" + num; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return baseName;
	}

	/**
	 * Map from design's master page name to master page info
	 */
	private Map<String, Integer> masterPages;
	private int currentInstanceCount;
	private String currentMasterPage;

	public MasterPageManager() {
		masterPages = new HashMap<String, Integer>();
		currentInstanceCount = 0;
		masterPageCount = 0;
		currentMasterPage = null;
	}

	public void newPage(String masterPageName) {
		Integer info = masterPages.get(masterPageName);
		if (info == null) {
			currentInstanceCount = 0;
			masterPageCount++;
		} else {
			currentInstanceCount = info.intValue();
		}

		currentInstanceCount++;
		masterPages.put(masterPageName, currentInstanceCount);
		currentMasterPage = masterPageName;
	}

	public String getCurrentMasterPage() {
		if (currentMasterPage != null) {
			return generateName(BASE_NAME, currentInstanceCount);
		}
		return null;
	}

	public int getInstanceNumber() {
		return currentInstanceCount;
	}

}
