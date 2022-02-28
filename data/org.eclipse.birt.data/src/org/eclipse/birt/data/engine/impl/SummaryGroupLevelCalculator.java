
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

/**
 *
 */

public class SummaryGroupLevelCalculator {
	private int[][] groupStartingEndingIndex;
	private int[] currentIndex;

	public SummaryGroupLevelCalculator(int[][] groupStartingEndingIndex) {
		this.groupStartingEndingIndex = groupStartingEndingIndex;
		if (this.groupStartingEndingIndex != null) {
			this.currentIndex = new int[this.groupStartingEndingIndex.length];
		}
	}

	public int getEndingGroupLevel(int index) {
		if (this.groupStartingEndingIndex == null) {
			return 0;
		}
		for (int x = 0; x < groupStartingEndingIndex.length; x++) {
			for (int y = this.currentIndex[x]; y < this.groupStartingEndingIndex[x].length; y = y + 2) {
				if (index >= this.groupStartingEndingIndex[x][y] && index < this.groupStartingEndingIndex[x][y + 1]) {
					this.currentIndex[x] = y;
					break;
				}
			}
		}

		for (int i = groupStartingEndingIndex.length - 1; i > 0; i--) {

			if (groupStartingEndingIndex[i][this.currentIndex[i]
					+ 1] < groupStartingEndingIndex[i - 1][this.currentIndex[i - 1] + 1]) {
				return i;
			}
		}
		return 0;
	}
}
