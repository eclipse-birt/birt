
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		if (this.groupStartingEndingIndex != null)
			this.currentIndex = new int[this.groupStartingEndingIndex.length];
	}

	public int getEndingGroupLevel(int index) {
		if (this.groupStartingEndingIndex == null)
			return 0;
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
					+ 1] < groupStartingEndingIndex[i - 1][this.currentIndex[i - 1] + 1])
				return i;
		}
		return 0;
	}
}
