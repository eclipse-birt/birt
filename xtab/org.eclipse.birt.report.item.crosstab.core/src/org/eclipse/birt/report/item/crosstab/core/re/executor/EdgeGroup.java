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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

/**
 * EdgeGroup
 */
class EdgeGroup {

	int dimensionIndex;
	int levelIndex;

	String dimensionName;
	String levelName;

	EdgeGroup(int dimensionIndex, int levelIndex, String dimensionName, String levelName) {
		this.dimensionIndex = dimensionIndex;
		this.levelIndex = levelIndex;
		this.dimensionName = dimensionName;
		this.levelName = levelName;
	}
}
