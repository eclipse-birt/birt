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

package org.eclipse.birt.data.engine.executor.transform.group;

/**
 * Distinction implementation of GroupBy.
 */

class GroupByDistinctValue extends GroupBy {
	@Override
	public boolean isInSameGroup(Object currentGroupKey, Object previousGroupKey) {
		if (previousGroupKey == currentGroupKey) {
			return true;
		}

		if (previousGroupKey == null || currentGroupKey == null) {
			return false;
		}

		return currentGroupKey.equals(previousGroupKey);
	}

}
