
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.aggregation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.IAggrDefnManager;

/**
 * 
 */

public class AggrDefnRoundManager {
	private IAggrInfo[] aggrDefns;
	private int[][] roundStartingEndingIndex;

	public AggrDefnRoundManager(List aggrDefns) {
		this.aggrDefns = new IAggrInfo[aggrDefns.size()];
		for (int i = 0; i < aggrDefns.size(); i++) {
			this.aggrDefns[i] = (IAggrInfo) aggrDefns.get(i);
		}
		populateRoundStartingEndingIndex();

	}

	/**
	 * The number of round of aggregation calculation so that all the aggregation
	 * definitions in current AggrDefnRoundManager can be calcualted.
	 * 
	 * @return
	 */
	public int getRound() {
		return this.roundStartingEndingIndex.length;
	}

	/**
	 * 
	 */
	private void populateRoundStartingEndingIndex() {
		List breakIndex = new ArrayList();
		breakIndex.add(Integer.valueOf(0));
		for (int i = 1; i < this.aggrDefns.length; i++) {
			if (this.aggrDefns[i].getRound() != this.aggrDefns[i - 1].getRound()) {
				breakIndex.add(Integer.valueOf(i));
			}
		}
		this.roundStartingEndingIndex = new int[breakIndex.size()][2];
		for (int i = 0; i < breakIndex.size(); i++) {
			this.roundStartingEndingIndex[i][0] = ((Integer) breakIndex.get(i)).intValue();
			if (i == breakIndex.size() - 1) {
				this.roundStartingEndingIndex[i][1] = this.aggrDefns.length;
			} else {
				this.roundStartingEndingIndex[i][1] = ((Integer) breakIndex.get(i + 1)).intValue();
			}
		}
	}

	/**
	 * Get the aggrDefnManager for given round.
	 * 
	 * @param round
	 * @return
	 */
	public IAggrDefnManager getAggrDefnManager(int round) {
		List aggrDefn = new ArrayList();
		for (int i = this.roundStartingEndingIndex[round][0]; i < this.roundStartingEndingIndex[round][1]; i++) {
			aggrDefn.add(this.aggrDefns[i]);
		}
		return new AggrDefnManager(aggrDefn);
	}

}
