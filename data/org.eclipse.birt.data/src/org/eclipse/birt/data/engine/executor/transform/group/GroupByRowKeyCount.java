/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.transform.group;

public class GroupByRowKeyCount extends GroupBy {
	// how many different group keys are considered as in one big group
	private int keyCountOneGroup;

	// current different group keys count in the being generated big group
	private int currentGroupKeyCount = 1;

	public GroupByRowKeyCount(int keyCountOneGroup) {
		assert keyCountOneGroup > 1;
		this.keyCountOneGroup = keyCountOneGroup;
	}

	@Override
	public boolean isInSameGroup(Object currentGroupKey, Object previousGroupKey) {
		if (currentGroupKey == previousGroupKey) {
			return true;
		}
		if (currentGroupKey == null || previousGroupKey == null) {
			if (currentGroupKeyCount < keyCountOneGroup) {
				// current row is in the being generated big group
				currentGroupKeyCount++;
				return true;
			} else {
				// current row should be in the next big group to be generated
				currentGroupKeyCount = 1;
				return false;
			}
		}
		if (currentGroupKey.equals(previousGroupKey)) {
			return true;
		} else if (currentGroupKeyCount < keyCountOneGroup) {
			// current row is in the being generated big group
			currentGroupKeyCount++;
			return true;
		} else {
			// current row should be in the next big group to be generated
			currentGroupKeyCount = 1;
			return false;
		}
	}

	@Override
	public void reset() {
		currentGroupKeyCount = 1;
	}

}
