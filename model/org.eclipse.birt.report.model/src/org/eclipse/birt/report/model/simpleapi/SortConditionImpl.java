/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;

/**
 * Implements of Sort Condition
 *
 */

public class SortConditionImpl extends Structure implements ISortCondition {

	private SortKey sort;

	/**
	 * Constructor
	 *
	 * @param sortHandle
	 */

	public SortConditionImpl() {
		super(null);
		sort = createSortCondition();
	}

	/**
	 * Constructor
	 *
	 * @param sortHandle
	 */

	public SortConditionImpl(SortKeyHandle sortHandle) {
		super(sortHandle);
		if (sortHandle == null) {
			sort = createSortCondition();
		} else {
			structureHandle = sortHandle;
			sort = (SortKey) sortHandle.getStructure();
		}
	}

	/**
	 * Constructor
	 *
	 * @param sort
	 */
	public SortConditionImpl(SortKey sort) {
		super(null);
		if (sort == null) {
			this.sort = createSortCondition();
		} else {
			this.sort = sort;
		}
	}

	/**
	 * Create instance of <code>SorterKey</code>
	 *
	 * @return instance
	 */
	private SortKey createSortCondition() {
		SortKey s = new SortKey();
		return s;
	}

	@Override
	public String getDirection() {
		return sort.getDirection();
	}

	@Override
	public String getKey() {
		return sort.getKey();
	}

	@Override
	public void setDirection(String direction) throws SemanticException {
		if (structureHandle != null) {
			setProperty(SortKey.DIRECTION_MEMBER, direction);
			return;
		}

		sort.setDirection(direction);
	}

	@Override
	public void setKey(String key) throws SemanticException {
		// key is required
		if (structureHandle != null) {
			setProperty(SortKey.KEY_MEMBER, key);
			return;
		}

		sort.setKey(key);
	}

	@Override
	public IStructure getStructure() {
		return sort;
	}

}
